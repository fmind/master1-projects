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
 * Regroupe les cases et les cartes du jeu ainsi que le dépôt de l'argent des amendes.
 * @author freaxmind
 */
public class Plateau {
    private ArrayList<Case> cases
    private ArrayList<Carte> chances
    private ArrayList<Carte> caisses
    private int argentCentre

    Plateau() {
        this.cases = []
        this.chances = []
        this.caisses = []
        this.argentCentre = 0

        this.constructionCases()
        this.constructionCartes()
        this.melangerCartes()
    }

    /**
     * Création des cases du plateau
     * l'ordre d'ajout fait office d'index
     */
    private void constructionCases() {
        this.cases << new Case("Départ")
        this.cases << new Case("Boulevard de Belleville")
        this.cases << new Case("Caisse de communauté")
        this.cases << new Case("Rue Lecourbe")
        this.cases << new Case("Impôt sur le revenu")
        this.cases << new Case("Gare de Montparnasse")
        this.cases << new Case("Rue de Vaugirard")
        this.cases << new Case("Chance")
        this.cases << new Case("Rue de Courcelles")
        this.cases << new Case("Avenue de la République")
        this.cases << new Case("Prison")
        this.cases << new Case("Boulevard de la Villette")
        this.cases << new Case("Compagnie de distribution d'électricité")
        this.cases << new Case("Avenue de Neuilly")
        this.cases << new Case("Rue de Paradis")
        this.cases << new Case("Gare de Lyon")
        this.cases << new Case("Avenue Mozard")
        this.cases << new Case("Caisse de communauté")
        this.cases << new Case("Boulevard Saint-Michel")
        this.cases << new Case("Place Pigalle")
        this.cases << new Case("Parc Gratuit")
        this.cases << new Case("Avenue Matigon")
        this.cases << new Case("Chance")
        this.cases << new Case("Boulevard Malesherbes")
        this.cases << new Case("Avenue Henri-Martin")
        this.cases << new Case("Gare du Nord")
        this.cases << new Case("Faubourg Saint-Honoré")
        this.cases << new Case("Place de la Bourse")
        this.cases << new Case("Compagnie de distribution des eaux")
        this.cases << new Case("Rue de la Fayette")
        this.cases << new Case("Aller en Prison")
        this.cases << new Case("Avenue de Breteuil")
        this.cases << new Case("Avenue Foch")
        this.cases << new Case("Caisse de communauté")
        this.cases << new Case("Boulevard des capucines")
        this.cases << new Case("Gare Saint-Lazarre")
        this.cases << new Case("Chance")
        this.cases << new Case("Avenue des Champs-Élysées")
        this.cases << new Case("Taxe de luxe")
        this.cases << new Case("Rue de la Paix")
    }

    /**
     * Création des cartes du plateau
     * carte chance et communautaire
     */
    private void constructionCartes() {
        this.chances << new Carte("Voyage a Bicylette",
            "La puissance d'Yves Montant vous fait avancer de 3 cases", 3, true
        )
        this.chances << new Carte("Voyage en Delorean",
            "2.21 Gigowatts !!! Avancer de 8 cases" , 8
        )
        this.chances << new Carte("Aller en prison",
            "Quel chance !", 30
        )
        this.chances << new Carte("Aller en prison",
            "Quel chance !", 30
        )
        this.chances << new Carte("Aller Gare MontParnasse",
            "Le train va partir sans vous", 5
        )
        this.chances << new Carte("Aller Gare de Lyon",
            "Direction le soleil et les garçons/filles toutes nues", 15
        )
        this.chances << new Carte("Aller Gare du Nord",
            "Direction le cercle polaire", 25
        )
        this.chances << new Carte("Aller Gare Saint-Lazarre",
            "Y'a il un pilote de train dans l'avion ?", 35
        )
        this.chances << new Carte("Aller Rue Lecourbe",
            "C'est bien pommé par ici", 3
        )
        this.chances << new Carte("Aller Rue de Courcelle",
            "Célèbre grâce à la pub", 8
        )
        this.chances << new Carte("Aller Rue de Paradis",
            "Vanessa, tu es la ?", 14
        )
        this.chances << new Carte("Aller Place Pigalle",
            "Ca ressemble à Piccadilly", 19
        )
        this.chances << new Carte("Aller Avenue Matignon",
            "Aucune décision importante n'est prise ici", 21
        )
        this.chances << new Carte("Aller Place de la Bourse",
            "Ou la vie", 27
        )
        this.chances << new Carte("Aller Avenue Foch",
            "Oui mon général !", 32
        )
        this.chances << new Carte("Aller Rue de la paix",
            "Si tu veux la paix, prépare la guerre", 39
        )

        this.caisses << new Carte("Bourse d'étude",
            "Vos brillants résultats scolaires vous rapportent 10000\$", 1000
        )
        this.caisses << new Carte("Panne de photocopieuse",
            "Le réparateur doit venir réparer la photocopieuse pour 10000\$", -10000
        )
        this.caisses << new Carte("Vous héritez",
            "La tante Frida a changé son fusil d'épaule, recevez 30000\$", 3000
        )
        this.caisses << new Carte("Contravention",
            "PV de 30000\$ pour excès de vitesse. A combien rouliez-vous ?", -30000
        )
        this.caisses << new Carte("1er prix de beauté",
            "Qui est le/la plus beau/belle ? Vous pour 25000\$", 2500
        )
        this.caisses << new Carte("Chirurgie esthétique",
            "Ravallement de de façade pour 25000\$", -25000
        )
        this.caisses << new Carte("2e prix de beauté",
            "Vous n'êtes pas le/la plus beau/belle, voilà 15000\$ pour vous consoler", 1500
        )
        this.caisses << new Carte("Nouvel impôt Hollande",
            "La gauche est au pouvoir, payez 15000\$", -15000
        )
        this.caisses << new Carte("Dividendes",
            "Vos placements juteux vous rapportent 35000\$", 3500
        )
        this.caisses << new Carte("Marée noire",
            "Votre résidence secondaire à la plage est toute mazoutée. 35000\$ pour la nettoyer", -35000
        )
        this.caisses << new Carte("Trésor dans votre jardin",
            "Des pirates ont entéré un trésor dans votre jardin d'un montant de 32000\$", 3200
        )
        this.caisses << new Carte("Taxe carbone",
            "Pollueur = Payeur, uniquement dans les jeux de société. 32000\$", -32000
        )
        this.caisses << new Carte("Votre anniversaire",
            "Vous gagnez 1 an d'âge et 12000\$ de liquidité", 1200
        )
        this.caisses << new Carte("Anniversaire (pas le vôtre)",
            "Vous offrez un super cadeau d'une valeur de 12000\$", -12000
        )
        this.caisses << new Carte("Économies d'énergie",
            "Votre nouvelle cabane bio dans la forêt vous fait économiser 22000\$", 2200
        )
        this.caisses << new Carte("Nouvelle chaudière",
            "Histoire de passer l'hiver, vous achetez une nouvelle chaudière pour 22000\$", -22000
        )
    }

    /**
     * Mélange les paquets de carte pour varier parties
     */
    void melangerCartes() {
        Collections.shuffle(this.chances)
        Collections.shuffle(this.caisses)
    }

    /**
     * Retourne le nom d'une case en fonction de son index
     */
    String nomCase(int position) {
        return this.cases[position].nom
    }

    /**
     * Pioche la carte au dessus du paquet chance et la replace à la fin
     */
    Carte piocherChance() {
        Carte carte = this.chances.pop()
        this.chances.add(0, carte)

        return carte
    }

    /**
     * Pioche la carte au dessus du paquet caisse com. et la remplace à la fin
     */
    Carte piocherCaisse() {
        Carte carte = this.caisses.pop()
        this.caisses.add(0, carte)

        return carte
    }

    /**
     * Retourne l'argent au centre du plateau
     */
    int prendreArgentCentre() {
        int montant = this.argentCentre
        this.argentCentre = 0

        return montant
    }

    /**
     * Dépose de l'argent au centre du plateau
     */
    void deposerArgentCentre(int montant) {
        this.argentCentre += montant
    }
}
