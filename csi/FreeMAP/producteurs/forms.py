# -*- coding: utf-8 -*-

from django import forms
import gestion


class ProducteurPropositionForm(gestion.forms.PropositionForm):
    class Meta:
        model = gestion.models.Proposition
        exclude = ('quantite_commandee', 'producteur',)
