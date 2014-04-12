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

import modeles.Joueur
import modeles.Contrat

/**
 * Titre de propriété associé à une case du plateau
 * @author freaxmind
 */
abstract public class TitrePropriete {
    int id          // Identifiant unique (numéro de case du plateau)
    String nom
    int prix
    int groupe

    TitrePropriete(int id, String nom, int prix, int groupe) {
        this.id = id
        this.nom = nom
        this.prix = prix
        this.groupe = groupe
    }

    /**
     * Retourne le montant du loyer pour un séjour
     */
    abstract int loyer(Joueur payeur, Contrat contrat, ArrayList<Contrat> groupe)
}
