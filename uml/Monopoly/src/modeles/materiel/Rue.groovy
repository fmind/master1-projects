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
 * Représente une titre de propriété de type rue
 * @author freaxmind
 */
public class Rue extends TitrePropriete {
    // Loyer
    int loyerTerrainNu
    int loyerMaison1
    int loyerMaison2
    int loyerMaison3
    int loyerMaison4
    int loyerHotel
    // Constructions
    int prixConstructionMaison
    int prixConstructionHotel

    Rue(int id, String nom, int prix, int groupe,
        int terrainNu, int maison1, int maison2, int maison3, int maison4,
        int hotel, int constructionMaison, int constructionHotel) {
        super(id, nom, prix, groupe)

        this.loyerTerrainNu = terrainNu
        this.loyerMaison1 = maison1
        this.loyerMaison2 = maison2
        this.loyerMaison3 = maison3
        this.loyerMaison4 = maison4
        this.loyerHotel = hotel
        this.prixConstructionMaison = constructionMaison
        this.prixConstructionHotel = constructionHotel
    }

    /**
     * Retourne le loyer de séjour dans cette rue
     * dépend du nombre de maisons/hotel et des autres terrains du groupe
     */
    int loyer(Joueur payeur, Contrat contrat, ArrayList<Contrat> groupe) {
        Joueur proprietaire = contrat.proprietaire

        // Calcul du montant
        int montant = 0
        if (contrat.hotel)
            montant = this.loyerHotel
        else if (contrat.maisons == 4)
            montant = this.loyerMaison4
        else if (contrat.maisons == 3)
            montant = this.loyerMaison3
        else if (contrat.maisons == 2)
            montant = this.loyerMaison2
        else if (contrat.maisons == 1)
            montant = this.loyerMaison1
        else if (!contrat.hotel && !contrat.maisons) {
            boolean tous = true

            groupe.each {
                if (!it || it.proprietaire != proprietaire)
                    tous = false
            }

            montant = (tous) ? this.loyerTerrainNu * 2 : this.loyerTerrainNu
        }

        // Débit et crédit des joueurs
        payeur.argent -= montant
        proprietaire.argent += montant

        return montant
    }
}
