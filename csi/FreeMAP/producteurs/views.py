# -*- coding: utf-8 -*-

from django.contrib.auth.decorators import login_required
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import render_to_response, redirect
from django.template.context import RequestContext
from producteurs.forms import ProducteurPropositionForm
from gestion.models import Proposition, Producteur


@login_required()
def propositions(request):
    if not 'producteurs' in [g['name'] for g in request.user.groups.values()]:
        raise Exception("Vous n'avez pas la permission de proposer des produits")

    producteur = Producteur.objects.get(login=request.user.username)
    encours, historique = Proposition.par_producteur(producteur)

    var = {'encours': encours, 'historique': historique,}

    return render_to_response('proposition.html', var,
                                context_instance=RequestContext(request))


@csrf_exempt
@login_required()
def nouvelle(request):
    if request.method == 'POST':
        form = ProducteurPropositionForm(request.POST)
        if form.is_valid():
            proposition = form.save(commit=False)
            proposition.quantite_commandee = 0
            proposition.producteur = Producteur.objects.get(login=request.user.username)
            proposition.save()
            return redirect(propositions)
    else:
        form = ProducteurPropositionForm()

    var = {'form': form}

    return render_to_response('nouvelle.html', var,
                                context_instance=RequestContext(request))
