# -*- coding: utf-8 -*-

from django import forms
from gestion.models import Client, Abonnement


class AbonnementForm(forms.ModelForm):
    paniers = forms.IntegerField(label="Nombre de panier par semaine", min_value=1, max_value=100) 
    jour = forms.ChoiceField(label="Jour de livraison", choices=Abonnement.JOURS_LIVRAISON)

    class Meta:
        model = Client
