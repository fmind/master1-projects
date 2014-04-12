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

import vues.CliPartieUI
import modeles.Partie
import modeles.Banquier
import modeles.Joueur
import modeles.Contrat
import modeles.ContratException
import modeles.materiel.Carte

/**
 * Controlleur du plateau: gère les actions des cases
 * @author freaxmind
 */
public class PlateauControlleur {
    CliPartieUI ui
    private Partie partie

    PlateauControlleur(Partie partie) {
        this.partie = partie
    }

    /**
     * Execute une action de case en fonction de la position du joueur actuel
     */
    void dispatch() {
        Joueur joueur = this.partie.actuel

        switch (joueur.position) {
            case 0:
                this.caseDepart()
                break
            case 4:
                this.caseImpot(20000)
                break
            case 10:
                this.casePrison()
                break
            case 20:
                this.caseParcGratuit()
                break
            case 30:
                this.caseAllerPrison()
                break
            case 38:
                this.caseImpot(10000)
                break
            case [2, 17, 33]:
                this.caseCarteCommunautaire()
                break
            case [7, 22, 36]:
                this.caseChance()
                break
            default:
                this.casePropriete()
        }
    }

    /**
     * Action case départ: 20 000 $ supplémentaires
     */
    private void caseDepart() {
        Joueur joueur = this.partie.actuel
        String nomCase = this.partie.plateau.nomCase(joueur.position)
        joueur.argent += 20_000

        this.ui.information("${nomCase}: Vous touchez 20000\$ supplémentaire !")
    }

    /**
     * Action impot: verse une amende au centre du plateau
     */
    private void caseImpot(int montant) {
        Joueur joueur = this.partie.actuel
        String nomCase = this.partie.plateau.nomCase(joueur.position)
        this.partie.plateau.deposerArgentCentre(montant)
        joueur.argent -= montant

        this.ui.information("${nomCase}: vous versez ${montant}\$")
    }

    /**
     * Action parc gratuit: récupère l'argent au centre du plateau
     */
    private void caseParcGratuit() {
        Joueur joueur = this.partie.actuel
        String nomCase = this.partie.plateau.nomCase(joueur.position)
        int argentCentre = this.partie.plateau.prendreArgentCentre()
        joueur.argent += argentCentre

        if (argentCentre > 0)
            this.ui.information("${nomCase}: vous recevez ${argentCentre}\$")
    }

    /**
     * Action prison: affiche l'info simple visite
     */
    private void casePrison() {
        Joueur joueur = this.partie.actuel

        if (!joueur.estPrison())
            this.ui.information("Simple visite")
    }

    /**
     * Action aller en prison: envoie le joueur en prison
     */
    private void caseAllerPrison() {
        Joueur joueur = this.partie.actuel
        String nomCase = this.partie.plateau.nomCase(joueur.position)
        this.partie.emprisonner()

        this.ui.information("${nomCase}: ne passer pas par la case départ et ne toucher pas 20000€")
    }

    /**
     * Action carte communautaire: pioche une carte
     */
    private void caseCarteCommunautaire() {
        Joueur joueur = this.partie.actuel
        Carte carte = this.partie.plateau.piocherCaisse()

        joueur.argent += carte.valeur
        if (carte.valeur < 0)   // dépot de l'argent en cas d'amende
            this.partie.plateau.deposerArgentCentre(Math.abs(carte.valeur))

        this.ui.information("Carte communautaire ${carte.nom}:\n\"${carte.description}\"")
    }

    /**
     * Action case chance: pioche une case chance
     */
    private void caseChance() {
        Joueur joueur = this.partie.actuel
        Carte carte = this.partie.plateau.piocherChance()

        // déplace le joueur sur le plateau
        boolean tour_complet = false
        if (carte.relatif)
            tour_complet = joueur.avancer(carte.valeur)
        else
            tour_complet = joueur.aller(carte.valeur)

        this.ui.information("Carte chance ${carte.nom}:\"\n${carte.description}\"")

        this.dispatch() // action sur la nouvelle case

        if (tour_complet && !joueur.estPrison()) {
            joueur.argent += 20_000
            this.ui.information("Tour complet (par chance): vous recevez 20000\$)")
        }

    }

    /**
     * Action case propriété: payer le loyer
     */
    private void casePropriete() {
        Banquier banquier = this.partie.banquier
        Joueur joueur = this.partie.actuel
        Contrat contrat = banquier.contrat(joueur.position)

        if (!contrat)
            return

        Joueur proprietaire = contrat.proprietaire
        ArrayList<Contrat> groupe = banquier.groupe(contrat.titre.groupe)

        if (joueur != proprietaire) {
            int montant = contrat.titre.loyer(joueur, contrat, groupe)
            this.ui.information("Vous payez un loyer de ${montant}\$ à ${contrat.proprietaire}")
        }
    }
}
