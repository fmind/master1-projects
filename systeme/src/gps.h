/*
 * File: gps.h
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#ifndef GPS_H
#define GPS_H

#include "parcours.h"
#include <pthread.h>
#include <stdio.h>

/*
 * Type de conduite du conducteur parmi (calme, normal et énervé)
 */
struct Comportement {
    int pmin;         // pourcentage min. de la vitesse maximale autorisée (ex: -10 %) 
    int pmax;         // pourcentage max. de la vitesse maximale autorisée (ex: +30 %)
    char* label;
};
typedef struct Comportement Comportement;

/**
 * Créer un nouveau comportement
 */
Comportement* comportement_init(char* label, int pmin, int pmax);

/**
 * Supprime manuellement un comportement
 */
void comportement_delete(Comportement* comportement);

/*
 * Échantillon de l'antenne GPS
 */
struct Localisation {
  time_t t;                     // timestamp de la mesure en secondes
  float position;               // position du véhicule sur le parcours
  Troncon* troncon;             // troncon associée à la position
  struct Localisation* suivant; // liste chaîné, le plus récent échantillon est en tête de liste
};
typedef struct Localisation Localisation;

/**
 * Créer une nouvelle localisation
 */
Localisation* localisation_init(float position, Troncon* troncon, Localisation* suivant);

/**
 * Supprime récursivement les localisations à partir de la tête de liste
 */
void localisation_delete(Localisation* localisation);

/*
 * Structure principale décrivant un GPS avec ses périphériques et ses fonctions
 */
struct GPS {
    // attributs
    float vitesse;                  // vitesse actuelle du véhicule en km/h
    int alerte;                     // informe si l'utilisateur est en train d'être alerté d'un dépassement
    // périphériques
    FILE* compteur;                 // périphérique associé au compteur de vitesse
    FILE* avertisseur;              // périphérique associé à l'avertisseur
    // fonctions temps réel
    pthread_t* capteur_thread;      // ajoute périodiquement de nouvelles localisations
    pthread_t* compteur_thread;     // affiche périodiquement la vitesse sur le compteur
    pthread_t* avertisseur_thread;  // avertit l'utilisateur qui dépasse la vitesse max. autorisée
    // programmation
    Parcours* parcours;             // parcours programmé
    Localisation* localisations;    // échantillons de la localisation du conducteur
    Comportement* comportement;     // type de conduite du conducteur
};
typedef struct GPS GPS;

/**
 * Créer un nouveau GPS
 */
GPS* gps_allumer();

/**
 * Programme l'itinéraire et le comportement
 */
int gps_programer(GPS* gps, int argc, char* argv[]);

/**
 * Parcours l'itinétaire programmé
 */
void gps_parcourir(GPS* gps);

/**
 * Éteind le GPS proprement
 */
void gps_eteindre(GPS* gps);

#endif
