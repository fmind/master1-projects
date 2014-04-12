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

package modeles.materiel

/**
 * Représente une carte chance ou caisse communautaire du plateau
 * permet de déplacer le joueur ou de lui demander/verser un paiement
 * @author freaxmind
 */
public class Carte {
    String nom
    String description
    int valeur
    boolean relatif     // pour le déplacement, aller à un index ou avancer

    Carte(String nom, String description, int valeur, boolean relatif = false) {
        this.nom = nom
        this.description = description
        this.valeur = valeur
        this.relatif = relatif
    }
}
