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
 * Représente un titre de propriété de type compagnie de distribution
 * @author freaxmind
 */
public class CompagnieDistribution extends TitrePropriete {
    static int loyercoef1 = 400
    static int loyercoef2 = 1000

    CompagnieDistribution(int id, String nom, int prix, int groupe) {
        super(id, nom, prix, groupe)
    }

    /**
     * Retourne le loyer de séjour dans cette compagnie
     */
    int loyer(Joueur payeur, Contrat contrat, ArrayList<Contrat> groupe) {
        Joueur proprietaire = contrat.proprietaire
        int nb_groupe = 0
        int montant = 0

        // Calcul du nombre de groupe
        groupe.each {
            if (it?.proprietaire == proprietaire)
                nb_groupe++
        }

        if (nb_groupe == 1)
            montant = this.loyercoef1 * payeur.lance.somme
        if (nb_groupe == 2)
            montant = this.loyercoef2 * payeur.lance.somme

        // Débit et crédit des joueurs
        payeur.argent -= montant
        proprietaire.argent += montant

        return montant
    }
}
