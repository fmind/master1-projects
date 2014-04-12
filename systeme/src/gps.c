/*
 * File: gps.c
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#include "gps.h"
#include "debug.h"
#include "string.h"
#include <pthread.h>
#include <signal.h>
#include <time.h>
#include <stdlib.h>
#include <stdio.h>

#define ACCELERATION 100

// Drapeau indiquant aux threads qu'ils peuvent continuer leurs actions
int continuer = 1;

/**
 * Écrit dans un fichier en ajoutant un horodatage
 */
void horodatage(FILE* file, char* msg) {
    time_t t = time(NULL);
    struct tm* tm = localtime(&t);

    fprintf(file, "[%02d:%02d:%02d] %s", tm->tm_hour, tm->tm_min, tm->tm_sec, msg);
    fflush(file);
}

/**
 * Simule la position du véhicule en fonction de sa précédente vitesse
 * 
 * @note les calculs sont en mètres et secondes
 */
Localisation* gps_simulation(GPS* gps) {
    // Calcul la vitesse réelle du véhicule en fonction de la vitesse max et du comportement
    float vmax = gps->localisations->troncon->vmax * 1000 / 3600.0;
    float vreel = vmax;
    int pcent = 0;
    if (gps->comportement->pmin || gps->comportement->pmax) {
        pcent = gps->comportement->pmin + rand() % (gps->comportement->pmax - gps->comportement->pmin);
        vreel = vmax + vmax * pcent/100;
    }

    // Calcul la nouvelle position du véhicule en tenant compte du coefficient d'accélération du temps
    time_t maintenant = time(NULL);
    int dt = maintenant - gps->localisations->t;
    int dp = vreel * dt * ACCELERATION;
    float dp_km = dp / 1000.0;
    float position = gps->localisations->position + dp_km;

    // Créer la position associée à la localisation
    Localisation* localisation = localisation_init(position, parcours_troncon(gps->parcours, position), gps->localisations);
    localisation->t = maintenant;

    // Affiche le détail du calcul à l'utilisateur
    #ifdef DEBUG
    printf("[$6] ");
    printf("vmax: %d km/h (%.2f m/s)", gps->localisations->troncon->vmax, vmax);
    printf("\t\t");
    printf("vreel: %.2f m/s (%d%% de vmax)", vreel, 100 + pcent);
    printf("\t\t");
    printf("dt: %ds", dt);
    printf("\t\t");
    printf("dp: %d m (%.2f km)", dp, dp_km);
    printf("\t\t");
    printf("position: %.2f km", position);
    printf("\n");
    #endif

    return localisation;
}

/**
 * Fonction asynchrone échantillonnant les relevés (simulés) du satellite
 * @thread
 */
void gps_capteur(void* arg) {
    GPS* gps = (GPS*) arg;
    Localisation* loc = NULL;
    char buffer[1024];

    do {
        loc = gps_simulation(gps);

        // Calcul de la vitesse du véhicule
        float dp = loc->position - gps->localisations->position;
        int dt = (loc->t - gps->localisations->t) * ACCELERATION;
        gps->vitesse = (dp / dt) * 3600;

        // Pour la première mesure (dt = 0)
        if (isnan(gps->vitesse))
            gps->vitesse = 0;

        // Si la destination est atteint, arrête le capteur
        if (loc->position >= gps->parcours->longueur) {
            continuer = 0;
            break;
        }

        // Affiche les informations renvoyées par la fonction de simulation
        #ifdef DEBUG
        sprintf(buffer, "%.2f km/h\t\t\t\t\t\t\t\tDT = %ds\tDP = %.2f km\n", gps->vitesse, dt, dp); 
        horodatage(stdout, buffer);
        #endif

        gps->localisations = loc;
        sleep(1);
    } while (continuer);

    pthread_exit(EXIT_SUCCESS);
}

/**
 * Fonction asynchrone affichant la vitesse du véhicule
 * @thread
 */
void gps_compteur(void* arg) {
    GPS* gps = (GPS*) arg;
    char buffer [255];

    while (continuer) {
        sprintf(buffer, "Vitesse: %.0f km/h\t\tPosition: %.0f km\n", gps->vitesse, gps->localisations->position);
        horodatage(gps->compteur, buffer);
        sleep(1);
    }

    horodatage(gps->compteur, "FIN\n");

    pthread_exit(EXIT_SUCCESS);
}

/**
 * Fonction asynchrone avertissant le conducteur en cas de dépassement de vitesse
 * on avertit qu'une seule fois l'utilisateur jusqu'à ce qu'il redescende sous la vitesse max. autorisée
 * @thread
 */
void gps_avertisseur(void* arg) {
    GPS* gps = (GPS*) arg;

    while (continuer) {
        float depassement = gps->vitesse - gps->localisations->troncon->vmax;

        // Tolérance 0: le conducteur ne doit pas dépassé la vitesse max. autorisée de 1 km
        if (!gps->alerte && depassement > 0) {
            horodatage(gps->avertisseur, "ATTENTION: vous dépassez la vitesse max autorisée !\n");
            gps->alerte = 1;
        } else if (gps->alerte && depassement <= 0) {
            horodatage(gps->avertisseur, ".\n");
            gps->alerte = 0;
        }

        sleep(1);
    }

    horodatage(gps->avertisseur, "FIN\n");

    pthread_exit(EXIT_SUCCESS);
}

/**
 * Arrête le GPS dès que possible mais sans l'abimer
 */
void gps_arret_urgence() {
    LOG("Arrêt d'urgence ...");
    continuer = 0;
}

Comportement* comportement_init(char* label, int pmin, int pmax) {
    Comportement* comportement = malloc(sizeof(Comportement));
    comportement->pmin = pmin;
    comportement->pmax = pmax;
    comportement->label = label;

    return comportement;
}

void comportement_delete(Comportement* comportement) {
    if (!comportement)
        return;

    free(comportement);
}

Localisation* localisation_init(float position, Troncon* troncon, Localisation* suivant) {
    Localisation* localisation = malloc(sizeof(Localisation));
    localisation->t = time(NULL);
    localisation->position = position;
    localisation->troncon = troncon;
    localisation->suivant = suivant;

    return localisation;
}

void localisation_delete(Localisation* localisation) {
    if (!localisation)
        return;

    if (localisation->suivant)
        localisation_delete(localisation->suivant);

    free(localisation);
}

GPS* gps_allumer() {
    LOG("Allumage du GPS ...");

    GPS* gps = malloc(sizeof(GPS));
    gps->vitesse = 0;
    gps->alerte = 0;
    gps->compteur = fopen("../dev/compteur", "w");
    gps->avertisseur = fopen("../dev/avertisseur", "w");
    gps->capteur_thread = malloc(sizeof(pthread_t));
    gps->compteur_thread = malloc(sizeof(pthread_t));
    gps->avertisseur_thread = malloc(sizeof(pthread_t));
    gps->parcours = NULL;
    gps->localisations = NULL;
    gps->comportement = NULL;

    // Vérification des périphériques
    if (!gps->compteur || !gps->avertisseur) {
        ERROR("Impossible d'accéder aux périphériques");
        gps_eteindre(gps);
        exit(EXIT_FAILURE);
    }

    signal(SIGINT, gps_arret_urgence);
    
    // Génération VRAIMENT aléatoire des vitesses réelles
    //srand(time(NULL));

    return gps;
}

int gps_programmer(GPS* gps, int argc, char* argv[]) {
    LOG("Programmation du GPS ...");
    
    // Paramètres du parcours
    gps->parcours = parcours_parse(argc, argv);
    if (!gps->parcours)
        return 0;
    else
        parcours_print(gps->parcours);

    // Ajoute la première localisation
    gps->localisations = localisation_init(0, parcours_troncon(gps->parcours, 0), NULL);

    // Vérification du nombre d'argument
    if (argc < 3) {
        ERROR("nombre d'argument invalide\nsyntaxe : simultamtam -c <type> -l1 <longueur1> -v1 <vmax1> -l2 <longueur2> -v2 <vmax2>");
        return 0;
    }

    // Paramètres du comportement
    char* label = malloc(strlen(argv[2]) * sizeof(char));
    label = argv[2];
    if (!strcmp(label, "calme"))
        gps->comportement = comportement_init(label, -10, 0);
    else if (!strcmp(label, "normal"))
        gps->comportement = comportement_init(label, -10, 10);
    else if (!strcmp(label, "enerve"))
        gps->comportement = comportement_init(label, -10, 30);
    else {
        ERROR("Le comportement doit être dans la liste suivante: (calme, normal, enerve)");
        return 0;
    }

    printf("\t# Comportement du conducteur: %s (entre %d%% et %d%% de la vitesse max. autorisée)\n", gps->comportement->label, gps->comportement->pmin, gps->comportement->pmax);

    printf("\t# Accélération temporelle: %dx\n", ACCELERATION);

    return 1;
}

void gps_eteindre(GPS* gps) {
    LOG("Extinction du GPS ...");

    parcours_delete(gps->parcours);
    localisation_delete(gps->localisations);
    comportement_delete(gps->comportement);

    if (gps->compteur)
        fclose(gps->compteur);
    if (gps->avertisseur)
        fclose(gps->avertisseur);

    free(gps->capteur_thread);
    free(gps->compteur_thread);
    free(gps->avertisseur_thread);

    free(gps);
}

void gps_parcourir(GPS* gps) {
    LOG("Traitement en cours ...");

    // Les threads démarrent dès qu'ils sont crées, donc on les initialise le plus tard possible
    pthread_create(gps->capteur_thread, NULL, (void*) gps_capteur, gps);
    pthread_create(gps->compteur_thread, NULL, (void*) gps_compteur, gps);
    pthread_create(gps->avertisseur_thread, NULL, (void*) gps_avertisseur, gps);

    // Demande au processus principal d'attendre les threads avant de quitter la fonction
    pthread_join(*gps->capteur_thread, NULL);
    pthread_join(*gps->compteur_thread, NULL);
    pthread_join(*gps->avertisseur_thread, NULL);

    LOG("Fin du parcours ...");
}

