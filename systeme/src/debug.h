/*
 * File: debug.h
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#ifndef DEBUG_H
#define DEBUG_H

#define DEBUG

/**
 * Informe le développeur de l'exécution du programme si "DEBUG" est définie
 */
void LOG(char* mess);

/**
 * Informe l'utilisateur d'une erreur
 */
void ERROR(char* mess);

#endif
