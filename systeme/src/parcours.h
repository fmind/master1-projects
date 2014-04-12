/*
 * File: parcours.h
 * Author: Médéric HURIER <mederic.hurier6@etu.univ-lorraine.fr>
 */

#ifndef PARCOURS_H
#define PARCOURS_H

/**
 * Tronçon d'une route (droite) définie par sa longueur et sa vitesse maximale
 */
struct Troncon {
    int longueur;             // en km
    int vmax;                 // en km/h
    struct Troncon* suivant;  // liste chaînée, le départ est en tête de liste
};
typedef struct Troncon Troncon;

/**
 * Représente le parcours de l'utilisateur comme une suite de tronçon à emprunter
 */
struct Parcours {
    Troncon* troncons;        // le départ est en tête de liste
    int longueur;             // distance totale du parcours en km
};
typedef struct Parcours Parcours;

/**
 * Créer un nouveau Troncon
 */
Troncon* troncon_init();

/**
 * Supprime récursivement les tronçons à partir de la tête de liste
 */
void troncon_delete(Troncon* troncon);

/**
 * Créer un nouveau parcours à partir des arguments de la liste de commande
 */
Parcours* parcours_parse(int argc, char* argv[]);

/**
 * Supprime manuellement un parcours
 */
void parcours_delete(Parcours* parcours);

/**
 * Affiche le parcours dans la sortie standard
 */
void parcours_print(Parcours* parcours);

/**
 * Retourne le troncon en fonction de sa position sur le parcours
 * Si position = 0, renvoie le 1er tronçon.
 * Si position > total du parcours, renvoie le dernier tronçon
 */
Troncon* parcours_troncon(Parcours* parcours, int position);

#endif
