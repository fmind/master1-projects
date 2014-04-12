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

/**
 * Représente un joueur humain (contrairement aux banquiers qui ne sont pas humains)
 * @author freaxmind
 */
public class Joueur {
    String nom
    String couleur              // pas utilisé mais demandé par le cdc
    Lance lance                 // met en cache le dernier lancé du joueur
    int argent
    int position
    int tempsPrison             // temps que le joueur a passé en prison (en tour)
    private boolean prison
    private boolean abandon

    Joueur(String nom, String couleur) {
        this.nom = nom
        this.couleur = couleur
        this.argent = 150_000
        this.position = 0
        this.tempsPrison = 0
        this.prison = false
        this.abandon = false
    }

    /**
     * Retourne vrai si le joueur est en faillite (condition pour perdre la partie)
     */
    boolean estFaillite() {
        if (this.argent < 0)
            return true
        return false
    }

    /**
     * Retourne vrai si le joueur est en prison
     */
    boolean estPrison() {
        return this.prison
    }

    /**
     * Marque le joueur comme "emprisonné"
     * nécessite d'être déplacer (voir partie.emprisonner)
     */
    void emprisonner() {
        this.prison = true
        this.tempsPrison = 0
    }

    /**
     * Retire le marqeur "prison" du joueur
     */
    void liberer() {
        this.prison = false
        this.tempsPrison = 0
    }

    /**
     * Retourne vrai si le joueur a abandonné
     */
    boolean estAbandon() {
        return this.abandon
    }

    /**
     * Marque le joueur comme ayant abandonné
     */
    void abandonner() {
        this.abandon = true
    }

    /**
     * Avance le joueur d'un nombre de case
     * aucun déplacement si le joueur est en prison
     *
     * retourne vrai si le joueur a fait un tour de plateau
     */
    boolean avancer(int n) {
        if (this.prison)
            return false

        if (this.position+n >= 40) {
            this.position = this.position + n - 40
            return true
        } else {
            this.position += n
            return false
        }
    }

    /**
     * Place le joueur a la case d'index n
     * aucun déplacement si le joueur est en prison
     *
     * retourne vrai si le joueur a fait un tour de plateau
     */
    boolean aller(int n) {
        if (this.prison)
            return false

        boolean tour_complet = (n < this.position)
        this.position = n

        return tour_complet
    }

    /**
     * Retourne le nom du joueur
     */
    String toString() {
        return this.nom
    }
}
