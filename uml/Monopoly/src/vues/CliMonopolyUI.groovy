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

package vues

import controlleurs.MonopolyControlleur
import controlleurs.PartieControlleur
import vues.CliPartieUI

/**
 * Interface en ligne de commande pour commencer une partie de Monopoly
 * @author freaxmind
 */
public class CliMonopolyUI {
    private MonopolyControlleur ctrl
    private BufferedReader reader
    private Random generator
    private Map choices

    // identifiants des actions
    static int COMMENCER    = 1;
    static int INSCRIRE     = 2;
    static int QUITTER      = 3;

    CliMonopolyUI(MonopolyControlleur ctrl) {
        this.ctrl = ctrl
        this.reader = System.in.newReader()
        this.generator = new Random()
        this.choices = [:]

        this.choices[this.COMMENCER]    = "Commencer le jeu"
        this.choices[this.INSCRIRE]     = "Inscrire un joueur"
        this.choices[this.QUITTER]      = "Quitter le jeu"
    }

    /**
     * Dispacther d'action au controlleur
     */
    private void selection(Map args) {
        String input = this.reader.readLine()
        int action = input.isNumber() ? input as int : 0

        // vérifie l'action
        if (args?.exclude?.contains(action) || !this.choices.keySet().contains(action)) {
            println "La commande que vous avez saisi n'est pas reconnu !"
            this.afficherMenu(args)
            return
        }

        switch (action) {
            case this.COMMENCER:
                ctrl.commencerJeu()
                break
            case this.INSCRIRE:
                ctrl.inscrireJoueur()
                break
            case this.QUITTER:
                ctrl.quitterJeu()
                break
        }
    }

    /**
     * Message de bienvenue
     */
    void bienvenue() {
        println "=" * 50
        println "BIENVENUE AU JEU DE MONOPOLY DE FREAXMIND"
        println "=" * 50
    }

    /**
     * Message d'au revoir
     */
    void auRevoir() {
        println "\nAu revoir !"
    }

    /**
     * Affiche le menu d'action
     */
    void afficherMenu(Map args) {
        println ""
        println "-" * 100

        if (args?.error)
            println args.error
        if (args?.joueurs && !args.joueurs.isEmpty()) {
            print "Joueurs participants: "
            println args.joueurs.collect {nom, couleur ->
                "$nom($couleur) "
            }.join(', ')
        }

        println "\nMENU PRINCIPAL:"
        this.choices.each {key, value ->
            if (!args?.exclude.contains(key))
                println "Taper $key: $value"
        }

        print ">> "

        this.selection(args)
    }

    /**
     * Lancé de dé aléatoire
     */
    int lancerDes(String joueur) {
        print "\n$joueur~ appuyer sur entrée pour lancer les dés: "
        this.reader.readLine()

        int d1 = this.generator.nextInt(6) + 1
        int d2 = this.generator.nextInt(6) + 1
        int somme = d1 + d2

        println "Votre lancé: $d1 + $d2 = $somme"

        return somme
    }

    /**
     * Demande le nom du joueur à inscrire
     */
    String demandeNomJoueur() {
        String input

        println ""
        while (true) {
            print "\nEntrer le nom du joueur: "
            input = this.reader.readLine()

            if (input ==~ /[a-zA-z0-9]+/ && input.size() < 20)
                return input.capitalize()
            else
                println "Le nom doit être un mot non vide de moins de 20 caractères !!"
        }
    }

    /**
     * Demande la couleur du pion d'un joueur
     */
    String demandeCouleurPion(ArrayList couleurs) {
        String input

        println ""
        while (true) {
            println "\nCouleurs disponibles: ${couleurs.join(', ')}"
            print "Entrer la couleur du pion: "

            input = this.reader.readLine().capitalize()
            if (input in couleurs)
                return input
            else
                println "La couleur doit être dans la liste des couleurs disponibles !"
        }
    }

    /**
     * Confirme l'action de quitter le jeu
     */
    boolean confirmationQuitterJeu() {
        print "\nÊtes vous sûr de vouloir quitter le jeu ? (o/N): "

        String input = this.reader.readLine().capitalize()
        if (input == 'O')
            return true
        return false
    }

    /**
     * Retourne l'interface pour gérer la partie
     */
    CliPartieUI getPartieUi(PartieControlleur ctrl) {
        return new CliPartieUI(ctrl)
    }
}
