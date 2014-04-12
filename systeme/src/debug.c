/*
 * File: debug.c
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#include "debug.h"
#include <stdlib.h>
#include <stdio.h>

void LOG(char *mess) {
#ifdef DEBUG
    printf("-- %s\n", mess);
#endif
}

void ERROR(char* mess) {
    printf("\n!! ERREUR !! %s\n", mess);
}
