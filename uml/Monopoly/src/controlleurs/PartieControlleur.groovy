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

package controlleurs

import controlleurs.PlateauControlleur
import vues.CliPartieUI
import modeles.*
import modeles.materiel.*

/**
 * Controlleur de partie: gère la partie tour par tour
 * @author freaxmind
 */
public class PartieControlleur {
    private PlateauControlleur proxy    // actions sur les cases
    private CliPartieUI ui
    Partie partie
    int tour                            // nombre de tour d'affilé du joueur actuel
    boolean enchere                     // détermine si une enchère doit se jouer à la fin du tour

    PartieControlleur(Partie partie) {
        this.partie = partie
        this.proxy = new PlateauControlleur(partie)
    }

    /**
     * Renseigne l'interface du controlleur
     */
    void setUi(CliPartieUI ui) {
        this.ui = ui
        this.proxy.setUi(this.ui)
    }

    /**
     * Action d'achat pour le joueur actuel (construction ou transfert de propriété)
     */
    void acheter() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        Contrat contrat = banquier.contrat(joueur.position)

        try {
            if (this.ui.confirmationAchat()) {
                if (contrat)    // si le joueur possède le titre
                    banquier.construire(joueur.position, joueur)
                else
                    banquier.transferer(joueur.position, joueur)
            }

            // pas besoin de faire une enchère si tout c'est bien passé
            this.enchere = false
        } catch(ContratException e) {
            this.ui.information(e.message)
        }

        this.actionsManuelles()
    }

    /**
     * Vend n'importe quel maison du plateau à la banque
     */
    void vendreMaison() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        ArrayList<Contrat> contrats = banquier.contrats(joueur, [avec_maison: true])

        if (!contrats.isEmpty()) {
            int position = this.ui.demandeSelectionMaison(contrats)

            if (position >= 0)
                banquier.vendreMaison(joueur, position)
        }

        this.actionsManuelles()
    }

    /**
     * Vend n'importe quel hotel du plateau à la banque
     */
    void vendreHotel() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        ArrayList<Contrat> contrats = banquier.contrats(joueur, [avec_hotel: true])

        if (!contrats.isEmpty()) {
            int position = this.ui.demandeSelectionHotel(contrats)

            if (position >= 0)
                banquier.vendreHotel(joueur, position)
        }

        this.actionsManuelles()
    }

    /**
     * Vend n'importe quel titre de propriété à un autre joueur
     * l'acheteur doit acccepter la proposition
     */
    void vendreTitrePropriete() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        ArrayList<Joueur> adversaires = this.partie.joueurs - joueur
        ArrayList<Contrat> contrats = banquier.contrats(joueur)

        if (!contrats.isEmpty()) {
            int position = this.ui.demandeSelectionPropriete(contrats)

            if (position >= 0) {
                Joueur acheteur = this.ui.demandeSelectionJoueur(adversaires)
                Contrat contrat = banquier.contrat(position)

                if (acheteur && this.ui.confirmationAchat(contrat))
                    banquier.vendreTitre(joueur, position, acheteur)
            }
        }

        this.actionsManuelles()
    }

    /**
     * Passe au prochain joueur
     * si le joueur refuse d'acheter un titre de propriété, met le terrain aux enchères
     */
    void passerJoueurSuivant() {
        if (!this.enchere)
            return

        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        TitrePropriete titre = banquier.titre(joueur.position)
        Contrat contrat = banquier.contrat(joueur.position)
        def (Joueur acheteur, Integer prix) = this.ui.demandeEnchere()

        try {
            if (titre && !contrat && acheteur && prix >= titre.prix)
                banquier.transferer(joueur.position, acheteur, prix)
        } catch(ContratException e) {
            this.ui.information(e.message)
        }
    }

    /**
     * Quitte la partie après confirmation
     */
    boolean quitterPartie() {
        Joueur joueur = this.partie.actuel

        if (this.ui.confirmationQuitterPartie())
            joueur.abandonner()
        else
            this.actionsManuelles()
    }

    /**
     * Joue la partie tant qu'elle n'est pas terminé
     * affiche le résultat en fin de partie
     */
    def jouer() {
        while (!this.partie.estTermine()) {
            this.tour = 1
            this.partie.joueurSuivant()
            this.jouerTour()
        }

        this.ui.afficherResultat()
    }

    /**
     * Joue un tour
     * peut être appelé plusieurs fois en cas de double
     */
    private void jouerTour() {
        Joueur joueur = this.partie.actuel
        this.partie.total++
        joueur.lance = this.ui.lancerDes()

        this.debutTour()
        this.actionsAutomatiques()
        this.actionsManuelles()
        this.finTour()
    }

    /**
     * Actions du début du tour
     *  - tentative de libérer le joueur
     *  - place le joueur en prison si il fait 3 doubles d'affilés
     */
    private void debutTour() {
        Joueur joueur = this.partie.actuel

        // tentative de libération
        if (joueur.estPrison()) {
            int libere = this.partie.liberer()
            if (libere) // annulation du double
                joueur.lance.doublee = false

            if (libere == 1)
                this.ui.information("Vous êtes libéré de prison !")
            else if (libere == -1)
                this.ui.information("Vous êtes libéré de prison avec une amende de 5000\$")
            else
                this.ui.information("Vous resté en prison ce tour-ci")
        }

        // 3 doubles d'affilé = emprisonnement
        if (joueur.lance.doublee && this.tour == 3) {
            this.partie.emprisonner()
            this.ui.information("3e double d'affilé !")
            this.ui.information("Aller en prison, ne passer pas par la case départ et ne toucher pas 20000€")
        }
    }

    /**
     * Actions automatique:
     *  - toucher un salaire lors d'un tour complet
     *  - actions sur les cases
     */
    private actionsAutomatiques() {
        Joueur joueur = this.partie.actuel
        Banquier banquier = this.partie.banquier
        boolean tour_complet = joueur.avancer(joueur.lance.somme)
        TitrePropriete titre = banquier.titre(joueur.position)
        Contrat contrat = banquier.contrat(joueur.position)

        this.proxy.dispatch()
        this.enchere = titre && !contrat    // vrai si on met le terrain aux enchères

        // passage par la case départ
        if (tour_complet && !joueur.estPrison()) {
            this.partie.toucherSalaire()
            ui.information("Tour complet: vous recevez 20000\$")
        }
    }

    /**
     * Actions manuelles du joueurs
     * renvoie vers les actions du controlleurs
     */
    private actionsManuelles() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        ArrayList<Integer> exclude = []

        // exlusion de l'action achat
        if (!banquier.estAchetable(joueur.position, joueur))
            exclude << this.ui.ACHETER

        // exclusion des actions de vente
        if (banquier.contrats(joueur).isEmpty())
            exclude << this.ui.VENDRE_TITRE
        if (banquier.contrats(joueur, [avec_maison: true]).isEmpty())
            exclude << this.ui.VENDRE_MAISON
        if (banquier.contrats(joueur, [avec_hotel: true]).isEmpty())
            exclude << this.ui.VENDRE_HOTEL

        this.ui.afficherMenu([exclude: exclude])
    }

    /**
     * Actions de fin de tour:
     *  - tour supplémentaire
     *  - fin de la partie si le joueur abandonne ou est en faillite
     */
    private void finTour() {
        Joueur joueur = this.partie.actuel

        // retire le joueur de la partie
        if (joueur.estFaillite() || joueur.estAbandon()) {
            this.partie.retirer()
            return
        }

        // tour supplémentaire
        if (joueur.lance.doublee && !joueur.estPrison()) {
            this.tour++
            jouerTour()
        }
    }
}
