# -*- coding: utf-8 -*-

from django import forms
from models import *
from datetime import datetime


# Champs communs à plusieurs formulaires
type_produit_field = forms.ModelChoiceField(TypeProduit.objects.all(), empty_label=None)
amap_field = forms.ModelChoiceField(Amap.objects.all(), empty_label=None)
proposition_field = forms.ModelChoiceField(Proposition.objects.filter(
                                                date_peremption__gt=datetime.now()).extra(
                                                where = ['quantite_proposee - quantite_commandee > 0',]),
                                            empty_label=None)


class ProducteurForm(forms.ModelForm):
    class Meta:
        model = Producteur        
        widgets = {
            'password': forms.PasswordInput(render_value=True),
        }


class ClientForm(forms.ModelForm):
    class Meta:
        model = Client
        widgets = {
            'password': forms.PasswordInput(render_value=True),
        }


class FormAvecTypeProduit(forms.ModelForm):
    """ Classe générique pour tout formulaire avec un champ type de produit """
    type_produit = type_produit_field

    def clean_type_produit(self):
        data = self.cleaned_data['type_produit']
        return data.id


class PropositionForm(FormAvecTypeProduit):
    class Meta:
        model = Proposition


class ConstitutionForm(FormAvecTypeProduit):
    class Meta:
        model = Constitution


class FormAvecAmap(forms.ModelForm):
    """ Classe générique pour tout formulaire avec un champ AMAP """
    amap = amap_field

    def clean_amap(self):
        data = self.cleaned_data['amap']
        return data.id


class CommandeProducteurForm(FormAvecAmap):
    proposition = proposition_field

    class Meta:
        model = CommandeProducteur


class CommandeAmapForm(FormAvecTypeProduit, FormAvecAmap):
    class Meta:
        model = CommandeAmap
