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

import modeles.Contrat
import modeles.Joueur

/**
 * Représente un titre de propriété de type gare
 * @author freaxmind
 */
public class Gare extends TitrePropriete {
    static int loyer1Gare = 2500
    static int loyer2Gare = 5000
    static int loyer3Gare = 10000
    static int loyer4Gare = 20000

    Gare(int id, String nom, int prix, int groupe) {
        super(id, nom, prix, groupe)
    }

    /**
     * Retourne le loyer de séjour dans cette gare
     * dépend du nombre de gare possédée par le propriétaire
     */
    int loyer(Joueur payeur, Contrat contrat, ArrayList<Contrat> groupe) {
        Joueur proprietaire = contrat.proprietaire
        int nb_groupe = 0
        int montant = 0

        // Calcul le nombre de gare du propriétaire
        groupe.each {
            if (it?.proprietaire == proprietaire)
                nb_groupe++
        }

        if (nb_groupe == 1)
            montant = this.loyer1Gare
        if (nb_groupe == 2)
            montant = this.loyer2Gare
        if (nb_groupe == 3)
            montant = this.loyer3Gare
        if (nb_groupe == 4)
            montant = this.loyer4Gare

        // Débit et crédit des joueurs
        payeur.argent -= montant
        proprietaire.argent += montant

        return montant
    }
}
