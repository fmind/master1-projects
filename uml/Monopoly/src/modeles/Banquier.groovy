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
import modeles.materiel.CompagnieDistribution
import modeles.materiel.Gare
import modeles.materiel.Rue

/**
 * Joueur non humain qui gère les propriétés et les contrats
 * @author freaxmind
 */
public class Banquier {
    private Map<Integer, TitrePropriete> titres
    private Map<Integer, Contrat> contrats
    private int maisons                         // nombre de maison disponible
    private int hotels                          // nombre d'hotel disponible

    Banquier() {
        this.titres = [:]
        this.contrats = [:]
        this.maisons = 32
        this.hotels = 12

        constructionTitres()
    }

    /**
     * Initialise tous les titres de propriétés
     */
    private constructionTitres() {
        // Rue
        this.titres[1] = new Rue(1, "Boulevard de Belleville", 6000, 1,
            200, 1000, 3000, 9000, 16000, 25000, 5000, 5000
        )
        this.titres[3] = new Rue(3, "Rue Lecourbe", 6000, 1,
            400, 2000, 6000, 18000, 32000, 45000, 5000, 5000
        )
        this.titres[6] = new Rue(6, "Rue de Vaugirard", 10000, 2,
            600, 3000, 9000, 27000, 40000, 55000, 5000, 5000
        )
        this.titres[8] = new Rue(8, "Rue de Courcelles", 10000, 2,
            600, 3000, 9000, 27000, 40000, 55000, 5000, 5000
        )
        this.titres[9] = new Rue(9, "Avenue de la République", 12000, 2,
            800, 4000, 10000, 30000, 45000, 60000, 5000, 5000
        )
        this.titres[11] = new Rue(11, "Boulevard de la Vilette", 14000, 3,
            1000, 5000, 15000, 45000, 62000, 75000, 10000, 10000
        )
        this.titres[13] = new Rue(13, "Avenue de Neuilly", 14000, 3,
            1000, 5000, 15000, 45000, 62000, 75000, 10000, 10000
        )
        this.titres[14] = new Rue(14, "Rue de Paradis", 16000, 3,
            1200, 6000, 18000, 50000, 70000, 90000, 10000, 10000
        )
        this.titres[16] = new Rue(16, "Avenue Mozart", 18000, 4,
            1400, 7000, 20000, 55000, 75000, 95000, 10000, 10000
        )
        this.titres[18] = new Rue(18, "Boulevard Saint-Michel", 18000, 4,
            1400, 7000, 20000, 55000, 75000, 95000, 10000, 10000
        )
        this.titres[19] = new Rue(19, "Place Pigalle", 20000, 4,
            1600, 8000, 22000, 60000, 80000, 100000, 10000, 10000
        )
        this.titres[21] = new Rue(21, "Avenue Matignon", 22000, 5,
            1800, 9000, 25000, 70000, 87500, 105000, 15000, 15000
        )
        this.titres[23] = new Rue(23, "Boulevard Malesherbes", 22000, 5,
            1800, 9000, 25000, 70000, 87500, 105000, 15000, 15000
        )
        this.titres[24] = new Rue(24, "Avenue Henri-Martin", 24000, 5,
            2000, 10000, 30000, 75000, 92500, 110000, 15000, 15000
        )
        this.titres[26] = new Rue(26, "Faubourg Saint-Honoré", 26000, 6,
            2200, 11000, 33000, 80000, 97500, 115000, 15000, 15000
        )
        this.titres[27] = new Rue(27, "Place de la Bourse", 26000, 6,
            2200, 11000, 33000, 80000, 97500, 115000, 15000, 15000
        )
        this.titres[29] = new Rue(29, "Rue la Fayette", 28000, 6,
            2400, 12000, 36000, 85000, 102500, 120000, 15000, 15000
        )
        this.titres[31] = new Rue(31, "Avenue de Breteuil", 30000, 7,
            2600, 13000, 39000, 90000, 110000, 127500, 20000, 20000
        )
        this.titres[32] = new Rue(32, "Avenue Foche", 30000, 7,
            2600, 13000, 39000, 90000, 110000, 127500, 20000, 20000
        )
        this.titres[34] = new Rue(34, "Boulevard des Capucines", 32000, 7,
            2800, 15000, 45000, 100000, 120000, 140000, 20000, 20000
        )
        this.titres[37] = new Rue(37, "Avenue des Champs-Élysées", 35000, 8,
            3500, 17500, 50000, 110000, 130000, 150000, 20000, 20000
        )
        this.titres[39] = new Rue(39, "Rue de la paix", 40000, 8,
            5000, 20000, 60000, 140000, 170000, 200000, 20000, 20000
        )

        // Gares
        this.titres[5] = new Gare(5, "Gare Montparnasse", 20000, 9)
        this.titres[15] = new Gare(15, "Gare de Lyon", 20000, 9)
        this.titres[25] = new Gare(25, "Gare du Nord", 20000, 9)
        this.titres[35] = new Gare(35, "Gare Saint-Lazarre", 20000, 9)

        // Rues
        this.titres[12] = new CompagnieDistribution(12, "Compagnie de distribution d'électricité", 15000, 10)
        this.titres[28] = new CompagnieDistribution(28, "Compagnie de distribution des eaux", 15000, 10)
    }

    /**
     * Retourne le titre en fonction du numéro de case
     */
    TitrePropriete titre(int numero) {
        return this.titres[numero]
    }

    /**
     * Retourne le contrat en fonction du numéro de case
     */
    Contrat contrat(int numero) {
        return this.contrats[numero]
    }

    /**
     * Retourne les contrats de joueurs
     *
     * condition optionnelle: avec_maison et avec_hotel
     */
    ArrayList<Contrat> contrats(Joueur joueur, Map args = [:]) {
        ArrayList<Contrat> liste = []
        boolean avec_maison = (args.avec_maison) ? true : false
        boolean avec_hotel = (args.avec_hotel) ? true : false

        this.contrats.each {i, contrat ->
            if (contrat.proprietaire == joueur) {
                if (avec_maison && contrat.maisons)
                    liste << this.contrats[i]
                else if (avec_hotel && contrat.hotel)
                    liste << this.contrats[i]
                else if (!avec_hotel && !avec_maison)
                    liste << this.contrats[i]
            }
        }

        return liste
    }

    /**
     * Retourne tous les contrats associés à un groupe de propriété
     */
    ArrayList<Contrat> groupe(int groupe) {
        ArrayList<Contrat> liste = []

        this.titres.each {i, titre ->
            if (titre.groupe == groupe)
                liste << this.contrats[i]
        }

        return liste
    }

    /**
     * Retourne vrai si il est possible d'acheter:
     *  - avec titre disponible
     *  - est battissable
     */
    boolean estAchetable(Integer position, Joueur joueur) {
        TitrePropriete titre = this.titre(position)
        Contrat contrat = this.contrat(position)
        ArrayList<Contrat> contrats = this.contrats(joueur)

        // associé à aucun titre
        if (!titre)
            return false
        // titre disponible
        else if (titre && !contrat)
            return true

        return this.estBattisable(joueur.position, joueur)
    }

    /**
     * Retourne vrai si le terrain est disponible
     *
     *  - uniquement pour les rues
     *  - uniquement pour le joueur propriétaire
     *  - le propriétaire doit posséder tous les terrains du groupe
     *  - pas d'hotel (construction la plus luxueuse)
     */
    boolean estBattisable(Integer position, Joueur joueur) {
        TitrePropriete titre = this.titres[position]
        Contrat contrat = this.contrats[position]

        if (!titre || !(titre instanceof Rue))
            return false

        Rue rue = (Rue) titre
        ArrayList<Contrat> groupe = this.groupe(rue.groupe)
        if (contrat.proprietaire != joueur)
            return false

        for (g in groupe) {
            if (!g || g.proprietaire != joueur)
                return false
        }

        if (contrat.hotel)
            return false

        return true
    }

    /**
     * Transfère la propriété d'un titre à un autre joueur sous plusieurs conditions
     *  - le joueur a assez d'argent
     */
    void transferer(int numero, Joueur acheteur, Integer prix = null)
            throws  ContratException {
        TitrePropriete titre = this.titres[numero]
        Contrat contrat = this.contrats[numero]

        if (!titre) // sécurité
            return

        // si le joueur a assez d'argent
        Integer montant = (prix) ? prix : titre.prix
        if (acheteur.argent < montant)
            throw new ContratException("Vous n'avez pas d'argent pour acheter cette propriété")

        acheteur.argent -= montant
        if (contrat?.proprietaire)
            contrat.proprietaire.argent += montant
        this.contrats[numero] = new Contrat(acheteur, titre)
    }

    /**
     * Construit un batiment sur le terrain sous plusieurs conditions:
     *  - uniquement pour les rues
     *  - sans hotel
     *  - batiments disponibles
     *  - assez d'argent
     *  - tous les terrains du groupe
     */
    void construire(int numero, Joueur acheteur)
            throws  ContratException {
        TitrePropriete titre = this.titres[numero]
        Contrat contrat = this.contrats[numero]
        ArrayList<Contrat> groupe = this.groupe(titre.groupe)
        int montant = 0

        if (!titre) // sécurité
            return

        // uniquement pour les rues
        if (!(titre instanceof Rue))
            throw new ContratException("Ce terrain n'est pas constructible")
        Rue rue = (Rue) titre

        // le propriétaire doit posséder tous les groupes
        groupe.each {
            if (!it || it.proprietaire != acheteur)
                throw new ContratException("Vous devez posséder tout le groupe de terrain pour construire")
        }

        // sans hotel
        if (contrat.hotel)
            throw new ContratException("Vous avez déjà un hôtel sur ce terrain")

        // construction hotel
        if (contrat.maisons == 4) {
            if (this.hotels == 0)
                throw new ContratException("Crise de l'immobilier: Aucun hôtel disponible")

            montant = rue.prixConstructionHotel
            if (montant > acheteur.argent)
                throw new ContratException("Vous n'avez pas assez d'argent pour acheter cet hôtel")

            acheteur.argent -= montant
            this.maisons += 4
            this.hotels -= 1
            contrat.maisons = 0
            contrat.hotel = 1
        // construction maison
        } else {
            if (this.maisons == 0)
                throw new ContratException("Crise de l'immobilier: Aucune maison disponible")

            montant = rue.prixConstructionMaison
            if (montant > acheteur.argent)
                throw new ContratException("Vous n'avez pas assez d'argent pour acheter cette maison")

            acheteur.argent -= montant
            this.maisons--
            contrat.maisons++
        }
    }

    /**
     * Récupère tous les biens d'un joueur (titres, maisons, hotels)
     */
    void saisir(Joueur joueur) {
        Iterator it = this.contrats.entrySet().iterator()

        while (it.hasNext()) {
            Contrat contrat = it.next().value

            if (contrat.proprietaire == joueur) {
                this.hotels += (contrat.hotel) ? 1 : 0
                this.maisons += contrat.maisons
                it.remove()
            }
        }
    }

    /**
     * Vend une maison dont le joueur est propriétaire
     */
    void vendreMaison(Joueur joueur, Integer position) {
        TitrePropriete titre = this.titres[position]
        Contrat contrat = this.contrats[position]

        if (!contrat || !contrat.maisons)
            return

        joueur.argent += titre.prixConstructionMaison
        contrat.maisons--
        this.maisons++
    }

    /**
     * Vend un hotel dont le joueur est propriétaire
     */
    void vendreHotel(Joueur joueur, Integer position) {
        TitrePropriete titre = this.titres[position]
        Contrat contrat = this.contrats[position]

        if (!contrat || !contrat.hotel)
            return

        joueur.argent += titre.prixConstructionHotel + 4 * titre.prixConstructionMaison
        contrat.hotel = false
        this.hotels++
    }

    /**
     * Vend un titre de propriété à un acheteur
     */
    void vendreTitre(Joueur joueur, Integer position, Joueur acheteur) {
        TitrePropriete titre = this.titres[position]
        Contrat contrat = this.contrats[position]

        if (!contrat || !contrat)
            return

        // calcul le montant du transfert
        int total = titre.prix
        if (titre instanceof Rue) {
            Rue rue = (Rue) titre

            if (contrat.maisons)
                total += contrat.maisons * rue.prixConstructionMaison
            else if (contrat.hotel)
                total += rue.prixConstructionHotel
        }

        // vente du titre
        joueur.argent += total
        acheteur.argent -= total
        contrat.proprietaire = acheteur
    }
}
