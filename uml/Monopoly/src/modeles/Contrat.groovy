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

import modeles.materiel.TitrePropriete

/**
 * Association entre un titre de propriété et un joueur
 * avec des maisons ou un hotel (uniquement pour les rues)
 * @author freaxmind
 */
public class Contrat {
    Joueur proprietaire
    TitrePropriete titre
    int maisons
    boolean hotel

    Contrat(Joueur proprietaire, TitrePropriete titre) {
        this.proprietaire = proprietaire
        this.titre = titre
        this.maisons = 0
        this.hotel = false
    }

    /**
     * Retourne vrai si des constructions sont présentes sur le terrain
     */
    boolean construit() {
        return (this.hotel || this.maisons)
    }
}
