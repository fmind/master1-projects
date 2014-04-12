# -*- coding: utf-8 -*-

from django.template.context import RequestContext
from django.http import HttpResponse, Http404
from django.shortcuts import render_to_response, redirect
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.decorators import login_required
from django.utils import simplejson
from gestion.models import *


@login_required()
def composition(request):
    """ Index du module de composition """
    if not request.user.is_superuser:
        raise Http404

    from datetime import datetime
    week_number = datetime.today().isocalendar()[1]
    periode = Periode.objects.get(semaine=week_number)
    panier = Panier.objects.get(periode=periode)
    constitutions = Constitution.objects.filter(panier=panier)
    if len(constitutions):
        raise Exception("Le panier de la semaine à déjà été crée")

    nb, trimestre = Panier.get_nb_by_periode(week_number)
    types = TypeProduit.objects.all()

    # (ré)inititialise la session
    request.session['composition'] = {}
    request.session['composition_nb'] = nb
    request.session['composition_frais'] = 0
    var = {'title': 'Composition du panier de la semaine N°' + str(week_number), 'types': types, 'nb': nb, 'week_number': week_number, 'prix_moyen': trimestre.prix_panier}

    return render_to_response('composition.html', var,
                              context_instance=RequestContext(request))


@csrf_exempt
def ajout(request):
    """ Ajoute un type de produit en session avec les propositions associées """
    if not request.is_ajax() or 'id' not in request.POST or 'qte' not in request.POST:
        raise Http404

    type_id = int(request.POST['id'])
    qte = int(request.POST['qte'])
    nb = request.session['composition_nb']
    composition = request.session['composition']
    propositions, total, manque = Proposition.calcul(type_id, qte*nb)
    type_produit = TypeProduit.objects.get(pk=type_id)
    composition[type_id] = {
        'type': type_produit.intitule,
        'par_panier': qte,
        'total': total,
        'manque': manque,
        'propositions': propositions,
    }

    # mise à jour de la session
    request.session['composition'] = composition
    data = {'composition': composition, 'nb': request.session['composition_nb'], 'frais': request.session['composition_frais']}

    return HttpResponse(simplejson.dumps(data), mimetype='application/json')

    
@csrf_exempt
def retour(request):
    """ Retire un type de la produit de la session """
    if not request.is_ajax() or 'id' not in request.POST:
        raise Http404

    type_id = int(request.POST['id'])
    composition = request.session['composition']
    if type_id in composition:
        del(composition[type_id])

    # met à jour la session
    request.session['composition'] = composition
    data = {'composition': composition, 'nb': request.session['composition_nb'], 'frais': request.session['composition_frais']}

    return HttpResponse(simplejson.dumps(data), mimetype='application/json')


@csrf_exempt
def frais(request):
    """ Met à jour les frais supplémentaires associés au panier """
    if not request.is_ajax() or 'frais' not in request.POST:
        raise Http404

    frais = float(request.POST['frais'])
    request.session['composition_frais'] = round(frais, 2)

    data = {}

    return HttpResponse(simplejson.dumps(data), mimetype='application/json')


@csrf_exempt
def valider(request):
    """ Valide le panier de la semaine """
    from datetime import datetime
    from django.conf import settings
    composition = request.session['composition']
    frais = request.session['composition_frais']
    nb = request.session['composition_nb']
    prix_revient_total = 0
    week_number = datetime.today().isocalendar()[1]
    periode = Periode.objects.get(semaine=week_number)
    panier = Panier.objects.get(periode=periode)
    abonnements = Abonnement.objects.filter(trimestre=periode.trimestre)
    produits_manquants = {}
    
    # créer une ligne de constitution pour chaque type de produit
    for type_id, infos in composition.items():
        propositions = infos['propositions']
        prix_revient_total += infos['total']
        con = Constitution()
        con.panier = panier
        con.quantite = infos['par_panier']
        con.type_produit = type_id
        con.save()

        # informe l'utilisateur qu'un produit est manquant
        if infos['manque']:
            produits_manquants[infos['type']] = infos['manque']

        # créer une commande producteur pour chaque proposition retenue
        for prop in propositions:
            com = CommandeProducteur()
            com.quantite_commandee = prop['quantite']
            com.proposition = Proposition.objects.get(pk=prop['id'])
            com.periode = periode
            com.amap = settings.AMAP
            com.save()

    # met à jours les informations du panier
    prix_revient = (float(prix_revient_total) + frais) / float(nb)
    panier.prix_revient = round(prix_revient, 2)
    panier.frais_supplementaires = round(frais, 2)
    panier.save()

    # création automatique des commandes utilisateurs (basées sur les abonnements)
    for abo in abonnements:
        comutil = CommandeClient()
        comutil.jour = abo.jour
        comutil.paniers = abo.paniers
        comutil.occasionnelle = False
        comutil.client = abo.client
        comutil.panier = panier
        comutil.save()

    var = {'panier': panier, 'nb': nb, 'prix_moyen': periode.trimestre.prix_panier, 'produits_manquants': produits_manquants}

    return render_to_response('validation.html', var,
                              context_instance=RequestContext(request))
