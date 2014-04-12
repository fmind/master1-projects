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
 * Génère un lancé aléatoire de 2 dés
 * @author freaxmind
 */
public class Lance {
    int d1          // valeur du dés 1
    int d2          // valeur du dés 2
    int somme       // somme des dés
    boolean doublee

    private static Random generator = new Random()

    Lance() {
        int d1 = this.generator.nextInt(6) + 1
        int d2 = this.generator.nextInt(6) + 1

        this.d1 = d1
        this.d2 = d2
        this.somme = d1 + d2
        this.doublee = (d1 == d2)
    }

    String toString() {
        "Lancé" + (this.doublee ?  " DOUBLE " : "") + ": ${somme} (${d1} + ${d2})"
    }
}
