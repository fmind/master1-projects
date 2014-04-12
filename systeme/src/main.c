/*
 * File: main.c
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#include "gps.h"
#include "parcours.h"
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[]) {
    GPS* gps = gps_allumer();

    if (gps_programmer(gps, argc, argv)) {
        /*
        LOG("Test de la fonction 'parcours_troncon' pour le cas test-1");
        Troncon* troncon;
        int positions[7] = {0, 10, 25, 70, 100, 180, 300};
        int i;
        for (i = 0; i< 7; i++) {
            troncon = parcours_troncon(gps->parcours, positions[i]);
            if (troncon)
                printf("Position: %d\t=> troncon(%d km, %d km/h)\n", positions[i], troncon->longueur, troncon->vmax);
            else
                printf("Position: %d\t=> aucun troncon !!\n");
        }
        */

        gps_parcourir(gps);
    }

    gps_eteindre(gps);
    
    return EXIT_SUCCESS;
}
