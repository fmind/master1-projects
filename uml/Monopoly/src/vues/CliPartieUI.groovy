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

import controlleurs.PartieControlleur
import controlleurs.PlateauControlleur
import modeles.*
import modeles.materiel.*

/**
 * Interface en ligne de commande pour une partie de Monopoly
 * @author freaxmind
 */
public class CliPartieUI {
    private PartieControlleur ctrl
    private BufferedReader reader
    private Map choices

    // identifiants des actions
    public static int ACHETER       = 1
    public static int VENDRE_MAISON = 2
    public static int VENDRE_HOTEL  = 3
    public static int VENDRE_TITRE  = 4
    public static int PASSER        = 5
    public static int QUITTER       = 6

    CliPartieUI(PartieControlleur ctrl) {
        this.ctrl = ctrl
        this.reader = System.in.newReader()
        this.choices = [:]

        this.choices[this.ACHETER]          = "Acheter"
        this.choices[this.VENDRE_MAISON]    = "Vendre une maison"
        this.choices[this.VENDRE_HOTEL]     = "Vendre un hotel"
        this.choices[this.VENDRE_TITRE]     = "Vendre un titre"
        this.choices[this.PASSER]           = "Passer au joueur suivant"
        this.choices[this.QUITTER]          = "Quitter la partie"
    }

    /**
     * Dispatcher d'action au controlleur
     */
    private void selection(Map args) {
        Partie partie = this.ctrl.partie
        String input = this.reader.readLine()
        int action = input.isNumber() ? input as int : 0

        // action à exclure
        if (args?.exclude?.contains(action) || !this.choices.keySet().contains(action)) {
            println "La commande que vous avez saisi n'est pas reconnu !"
            this.afficherMenu(args)
            return
        }

        switch (action) {
            case this.ACHETER:
                this.ctrl.acheter()
                break
            case this.VENDRE_MAISON:
                this.ctrl.vendreMaison()
                break
            case this.VENDRE_HOTEL:
                this.ctrl.vendreHotel()
                break
            case this.VENDRE_TITRE:
                this.ctrl.vendreTitrePropriete()
                break
            case this.PASSER:
                this.ctrl.passerJoueurSuivant()
                break
            case this.QUITTER:
                this.ctrl.quitterPartie()
                break
        }
    }

    /**
     * Affiche le menu d'action
     */
    void afficherMenu(Map args = [:]) {
        Partie partie = this.ctrl.partie
        Joueur joueur = partie.actuel
        Banquier banquier = partie.banquier
        ArrayList<Contrat> contrats = banquier.contrats(joueur)
        Contrat contrat = banquier.contrat(joueur.position)
        String nomCase = partie.plateau.nomCase(joueur.position)

        println ""
        if (contrats)
            println "Propriétés: ${contrats.collect {it.titre.nom}.join(', ')}"
        println "Position: ${nomCase}(${joueur.position})${contrat ? ', propriété de '+contrat.proprietaire : ''}"
        if (contrat && contrat.construit())
            println "Bâtiments: [ ${contrat.hotel ? 'H ': ''}${'M '*contrat.maisons}]"
        println "Argent: ${joueur.argent} \$"

        println "\nACTIONS:"

        this.choices.each {key, value ->
            if (!args?.exclude?.contains(key))
                println "Taper $key: $value"
        }
        print ">> "

        this.selection(args)
    }

    /**
     * Demande intéractive de lancer les dés
     */
    Lance lancerDes() {
        Partie partie = this.ctrl.partie
        Joueur joueur = partie.actuel

        println "\n"
        println "-"*100
        println "TOUR N°${partie.total}: ${joueur.nom}"
        print "Appuyer sur entrée pour lancer les dés: "
        //this.reader.readLine()

        Lance lance = new Lance()
        println lance

        return lance
    }

    /**
     * Confirme l'achat d'un bien
     */
    boolean confirmationAchat(Contrat objet = null) {
        Partie partie = this.ctrl.partie
        Banquier banquier = partie.banquier
        Joueur joueur = partie.actuel
        String nomCase = partie.plateau.nomCase(joueur.position)
        String bien = ""

        if (!objet) {
            Contrat contrat = banquier.contrat(joueur.position)
            TitrePropriete titre = contrat?.titre

            if (!contrat) {
                bien = "un titre de propriété"
            } else if (titre instanceof Rue) {
                if (contrat.maisons < 4)
                    bien = "une maison"
                else (contrat.maisons == 4)
                    bien = "un hotel"
            }

            print "\nÊtes vous sûr de vouloir acheter ${bien} à ${nomCase} ? (o/N): "
        } else {
            bien = objet.titre.nom

            print "\nÊtes vous sûr de vouloir acheter ${bien} à ${joueur} ? (o/N): "
        }

        String input = this.reader.readLine().capitalize()
        if (input == 'O')
            return true
        return false
    }

    /**
     * Demande au joueur de choisir une propriété
     */
    Integer demandeSelectionPropriete(ArrayList<Contrat> contrats) {
        ArrayList<Integer> choices = []
        contrats.each {
            choices << it.titre.id.toString()
        }

        println "\nListe de vos titres:"
        contrats.each {
            String maison = (it.maisons) ? " (avec ${it.maisons} maisons)" : ""
            String hotel = (it.hotel) ? " (avec un hotel)" : ""
            println "${it.titre.id}: ${it.titre.nom}${maison}${hotel}"
        }

        print "\nEntrer le numéro du titre de propriété: "
        String input = this.reader.readLine()

        if (input.isInteger() && choices.contains(input))
            return input.toInteger()

        println "Le numéro du titre de propriété doit être dans la liste !!"
        return -1
    }

    /**
     * Demande au joueur de sélectionner une de ses maisons
     */
    Integer demandeSelectionMaison(ArrayList<Contrat> contrats) {
        ArrayList<Integer> choices = []
        contrats.each {
            choices << it.titre.id.toString()
        }

        println "\nListe de vos maisons:"
        contrats.each {
            println "${it.titre.id}: ${it.titre.nom} (${it.maisons})"
        }

        print "\nEntrer le numéro de la maison: "
        String input = this.reader.readLine()

        if (input.isInteger() && choices.contains(input))
            return input.toInteger()

        println "Le numéro de la maison doit être dans la liste !!"
        return -1
    }

    /**
     * Demande au joueur de sélectionner un de ses hotels
     */
    Integer demandeSelectionHotel(ArrayList<Contrat> contrats) {
        ArrayList<Integer> choices = []
        contrats.each {
            choices << it.titre.id.toString()
        }

        println "\nListe de vos hotels:"
        contrats.each {
            println "${it.titre.id}: ${it.titre.nom})"
        }

        print "\nEntrer le numéro de l'hôtel: "
        String input = this.reader.readLine()

        if (input.isInteger() && choices.contains(input))
            return input.toInteger()

        println "Le numéro de l'hotel doit être dans la liste !!"
        return -1
    }

    /**
     * Demande de sélection d'un joueur
     */
    Joueur demandeSelectionJoueur(ArrayList<Joueur> joueurs) {
        ArrayList<Integer> choices = []
        joueurs.eachWithIndex {it, i ->
            choices << i.toString()
        }

        println "\nListe des joueurs:"
        joueurs.eachWithIndex {it, i ->
            println "${i}: ${it}"
        }

        print "\nEntrer le numéro du joueur: "
        String input = this.reader.readLine()

        if (input.isInteger() && choices.contains(input))
            return joueurs[input.toInteger()]

        println "Le numéro du joueur doit être dans la liste !!"
        return null
    }

    /**
     * Demande d'enchère pour trouver l'encherisseur et le montant de l'enchère
     */
    def demandeEnchere() {
        Partie partie = this.ctrl.partie
        Joueur joueur = partie.actuel
        Banquier banquier = partie.banquier
        TitrePropriete titre = banquier.titre(joueur.position)
        Joueur encherisseur = null
        int prix = titre.prix
        String input = ""

        println "\nNouvelle enchère sur ${titre.nom} à ${prix} \$"
        println "appuyer sur n'importe quelle touche pour passer l'enchère"
        encherisseur = this.demandeSelectionJoueur(partie.joueurs)

        if (!encherisseur) {
            println "\nAucun encherisseur ne s'est proposé pour le titre"
        } else if (encherisseur.argent < prix) {
            encherisseur = null
            println "Vous n'avez pas assez d'argent pour acheter la propriété"
        } else {
            print "\nVous avez ${encherisseur.argent} \$\nEntrer le prix d'achat: "
            input = this.reader.readLine()

            if (!input.isInteger() || input.toInteger() < prix) {
                println "Le prix d'achat doit être supérieur au prix actuel"
                encherisseur = null
            } else if (input.toInteger() > encherisseur.argent) {
                println "Vous n'avez pas assez d'argent pour acheter ce titre"
                encherisseur = null
            } else {
                prix = input.toInteger()
            }
        }

        return [encherisseur, prix]
    }

    /**
     * Affiche un message d'information
     */
    void information(String message) {
        if (!message.isEmpty())
            println "\n# ${message}"
    }

    /**
     * Confirme la fin de la partie pour le joueur actuel
     */
    boolean confirmationQuitterPartie() {
        print "\nÊtes vous sûr de vouloir quitter la partie ? (o/N): "

        String input = this.reader.readLine().capitalize()
        if (input == 'O')
            return true
        return false
    }

    /**
     * Affiche le résultat de la partie
     */
    void afficherResultat() {
        Partie partie = this.ctrl.partie
        Joueur gagnant = partie.gagnant()

        println "\n"
        println "="*50
        println "JEU TERMINE"
        println "="*50
        println "\nLe/La gagnant(e) est: ${gagnant.nom} avec ${gagnant.argent}\$"
        println "\nMerci d'avoir jouer !"
    }
}
