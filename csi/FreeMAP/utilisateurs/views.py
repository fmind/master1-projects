# -*- coding: utf-8 -*-
from django.shortcuts import render_to_response, redirect
from django.template.context import RequestContext


def index(request):
    from gestion.models import Constitution, TypeProduit
    from datetime import datetime
    week_number = datetime.today().isocalendar()[1]
    constitutions = Constitution.par_periode(week_number)
    produits = []

    for c in constitutions:
        type_produit = TypeProduit.objects.get(pk=c.type_produit)
        produits.append([type_produit, c.quantite])

    var = {'produits': produits,}

    return render_to_response('index.html', var,
                              context_instance=RequestContext(request))


def bravo(request):
    return render_to_response('bravo.html',
                              context_instance=RequestContext(request))


def abonnement(request):
    from utilisateurs.forms import AbonnementForm
    from gestion.models import Panier, Abonnement, Client
    from datetime import datetime
    week_number = datetime.today().isocalendar()[1]
    nb_paniers, trimestre = Panier.get_nb_by_periode(week_number)

    if request.method == 'POST':
        form = AbonnementForm(request.POST)
        if form.is_valid():
            paniers_par_semaine = form.cleaned_data['paniers']
            jour = form.cleaned_data['jour']
            client = form.save()
            abonnement = Abonnement()
            abonnement.trimestre = trimestre
            abonnement.client = client
            abonnement.paniers = paniers_par_semaine
            abonnement.jour = jour
            abonnement.save()
            return bravo(request)
    else:
        form = AbonnementForm()

    var = {'form': form, 'nb': nb_paniers+1, 'trimestre': trimestre}

    return render_to_response('abonnement.html', var,
                              context_instance=RequestContext(request))
