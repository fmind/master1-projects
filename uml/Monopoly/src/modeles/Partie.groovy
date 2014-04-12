/*
 * Copyright (C) 2012 freaxmind
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package modeles

import modeles.materiel.Plateau

/**
 * Informations sur le déroulement d'une partie
 * @author freaxmind
 */
public class Partie {
    Plateau plateau
    Banquier banquier
    ArrayList<Joueur> joueurs
    Joueur actuel               // Joueur qui effectue les actions du tour
    int total                   // Nombre de tour depuis le début de la partie
    private Iterator tours      // Permet de passer au tour suivant

    Partie(Map participants, String premier) {
        this.plateau = new Plateau()
        this.banquier = new Banquier()

        // Ajout des joueurs dans l'ordre à partir du gagnant du lancés de dés
        boolean trouve = false
        ArrayList<Joueur> avant = []
        ArrayList<Joueur> apres = []
        participants.each {nom, couleur ->
            Joueur joueur = new Joueur(nom, couleur)

            if (nom == premier)
                trouve = true

            if (trouve)
                avant << joueur
            else
                apres << joueur
        }
        this.joueurs = avant + apres
        this.actuel = null
        this.total = 0
        this.tours = this.joueurs.iterator()
    }

    /**
     * Retourne vrai si la partie est terminée
     */
    boolean estTermine() {
        if (this.joueurs.size() < 2)
            return true
        return false
    }

    /**
     * Gagnant de la partie une fois celle ci terminée
     */
    Joueur gagnant() {
        if (this.joueurs.size() == 1)
            return this.joueurs.first()
        return null
    }

    /**
     * Passe au tour suivant et change le joueur actuel
     */
    void joueurSuivant() {
        if (!this.tours.hasNext())
            this.tours = this.joueurs.iterator()
        this.actuel = this.tours.next()
    }

    /**
     * Donne un salaire à un joueur qui fait un tour complet
     */
    void toucherSalaire() {
        Joueur joueur = this.actuel

        if (!joueur.estPrison())
            joueur.argent += 20_000
    }

    /**
     * Met le joueur en prison sans passer par la case départ
     */
    void emprisonner() {
        Joueur joueur = this.actuel

        joueur.aller(10)
        joueur.emprisonner()
    }

    /**
     * Essaye de libérer un joueur de prison
     * return 1 si le joueur est libéré sans amende
     * return 0 si le joueur n'est pas libéré
     * return -1 si le joueur est libéré avec amende
     */
    int liberer() {
        Joueur joueur = this.actuel
        Lance lance = joueur.lance
        joueur.tempsPrison++

        // libération automatique si le joueur fait un double
        if (lance.doublee) {
            joueur.liberer()
            return 1
        }
        // fait payer une amende au joueur au bout de 3 tours en prison
        else if (joueur.tempsPrison > 3) {
            joueur.argent -= 5_000
            joueur.liberer()
            return -1
        }

        return 0
    }

    /**
     * Retire le joueur de la partie et saisie ses titres
     */
    void retirer() {
        Joueur joueur = this.actuel
        Banquier banquier = this.banquier

        banquier.saisir(joueur)
        this.tours.remove()
    }
}
