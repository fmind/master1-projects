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

import controlleurs.PartieControlleur
import vues.CliMonopolyUI
import vues.CliPartieUI
import modeles.Partie

/**
 * Controlleur de Monopoly: inscris les joueurs et commence la partie
 * @author freaxmind
 */
public class MonopolyControlleur {
    CliMonopolyUI ui
    Map joueurs = [:]
    //private Map joueurs = ['Médéric': 'Rouge', 'Lina': 'Jaune']

    public static ArrayList COULEURS = ['Rouge', 'Vert', 'Bleu', 'Jaune', 'Magenta', 'Noir']

    /**
     * Affiche les actions possibles par le joueur
     */
    private void afficherMenu(error = "") {
        ArrayList exclude = []

        // actions à exclure
        if (this.joueurs.size() < 2)
            exclude << this.ui.COMMENCER
        else if (this.joueurs.size() >= 6)
            exclude << this.ui.INSCRIRE

        this.ui.afficherMenu(joueurs: this.joueurs, error: error, exclude: exclude)
    }

    /**
     * Retourne le joueur gagnant du lancé des dés
     * ce joueur commence la partie
     */
    private String gagnantLancerDes() {
        ArrayList joueurs = this.joueurs.keySet()
        ArrayList gagnant = []

        // départage les ex-aquo entres eux
        while (gagnant.size() != 1) {
            int max = 0

            joueurs.each {
                int somme = this.ui.lancerDes(it)

                if (somme > max) {
                    max = somme
                    gagnant = [it]
                } else if (somme == max) {
                    gagnant << it
                }
            }

            // un seul joueur peut gagner
            if (gagnant.size() > 1)
                joueurs = gagnant
        }

        return gagnant.pop()
    }

    /**
     * Inscris un nouveau joueur (jusqu'à 6)
     */
    void inscrireJoueur() {
        def (couleurs_prises, couleurs_dispo) = this.COULEURS.split {
            it in joueurs.values()
        }

        String nom = this.ui.demandeNomJoueur()
        String pion = this.ui.demandeCouleurPion(couleurs_dispo)

        String error
        if (nom in this.joueurs.keySet())
            error = "\nCe nom est déjà pris par un autre joueur !\n"
        else
            this.joueurs[nom] = pion

        this.afficherMenu(error)
    }

    /**
     * Commence une nouvelle partie
     */
    void commencerJeu() {
        String premier = this.gagnantLancerDes()
        //String premier = this.joueurs.keySet().toArray()[new Random().nextInt(this.joueurs.size())]
        Partie partie = new Partie(this.joueurs, premier)

        PartieControlleur ctrl = new PartieControlleur(partie);
        ctrl.setUi(this.ui.getPartieUi(ctrl));
        ctrl.jouer();
    }

    /**
     * Quitte le jeu
     */
    void quitterJeu() {
        if (this.ui.confirmationQuitterJeu())
            this.ui.auRevoir()
        else
            this.afficherMenu()
    }

    /**
     * Lance le controlleur (start)
     */
    def lancer() {
        this.ui.bienvenue()
        this.afficherMenu()
    }
}
