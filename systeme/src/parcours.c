/*
 * File: parcours.c
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#include "parcours.h"
#include "string.h"
#include <stdlib.h>
#include <stdio.h>

Troncon* troncon_init(int longueur, int vmax, Troncon* precedent) {
    Troncon* troncon = malloc(sizeof(Troncon));
    troncon->longueur = longueur;
    troncon->vmax = vmax;
    if (precedent)
        precedent->suivant = troncon;

    return troncon;
}

void troncon_delete(Troncon* troncon) {
    if (!troncon)
        return;

    if (troncon->suivant)
        troncon_delete(troncon->suivant);
    
    free(troncon); 
}

Parcours* parcours_parse(int argc, char* argv[]) {
    // Vérification du bon nombre d'argument
    if (argc < 7 || ((argc-7) % 4) != 0) {
        ERROR("nombre d'argument invalide\nsyntaxe : simultamtam -c <type> -l1 <longueur1> -v1 <vmax1> -l2 <longueur2> -v2 <vmax2>");
        return NULL;
    }

    Parcours* parcours = malloc(sizeof(Parcours));
    parcours->longueur = 0;
    parcours->troncons = NULL;
    Troncon* troncon = NULL;

    int l, v;
    int i, rang;
    char error[1024];

    for (i=3, rang = 1; i < argc; i += 4, rang++) {
        l = atoi(argv[i+1]);
        v = atoi(argv[i+3]);

        // Vérification que les vitesses sont des entiers positifs 
        if (l <= 0) {
            sprintf(error, "la valeur de l'argument n°%d (rang %d) '%s' n'est pas un entier positif", i+2, rang, argv[i+1]);
            ERROR(error);
            parcours_delete(parcours);
            return NULL;
        } else if (v <= 0) {
            sprintf(error, "la valeur de l'argument n°%d (rang %d) '%s' n'est pas un entier positif", i+4, rang, argv[i+3]);
            ERROR(error);
            parcours_delete(parcours);
            return NULL;
        }

        troncon = troncon_init(l, v, troncon);
        parcours->longueur += troncon->longueur;

        // uniquement pour le premier tronçon crée
        if (!parcours->troncons)
            parcours->troncons = troncon;
    }

    return parcours;
}

void parcours_delete(Parcours* parcours) {
    if (!parcours)
        return;

    if (parcours->troncons)
        troncon_delete(parcours->troncons);

    free(parcours);
}

void parcours_print(Parcours* parcours) {
    Troncon* curseur = parcours->troncons;

    printf("\t# Parcours programmé (%d km):\n", parcours->longueur);

    while (curseur) {
        printf("\t\t- %d km \t[%d km/h]\n", curseur->longueur, curseur->vmax);
        curseur = curseur->suivant;
    }
}

Troncon* parcours_troncon(Parcours* parcours, int position) {
    Troncon* curseur = parcours->troncons;
    Troncon* troncon = curseur;
    int total = 0;

    while (curseur && total <= position) {
        troncon = curseur;
        total += curseur->longueur;
        curseur = curseur->suivant;
    }

    return troncon;
}
