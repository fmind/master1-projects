# -*- coding: utf-8 -*-

from django.contrib import admin
from models import *
from forms import *


class AmapSite(admin.sites.AdminSite):
    """ Administration personnalisée du site (url supplémentaires) """
    
    def get_urls(self):
        """ Ajoute les URLs de composition à l'administration """
        from django.conf.urls.defaults import patterns, url
        urls = super(AmapSite, self).get_urls()
        site_urls = patterns('',
            url(r'^composition/$', 'gestion.views.composition'),
            url(r'^composition/ajout$', 'gestion.views.ajout'),
            url(r'^composition/retour$', 'gestion.views.retour'),
            url(r'^composition/frais$', 'gestion.views.frais'),
            url(r'^composition/valider$', 'gestion.views.valider'),
        )
        
        return site_urls + urls

        
class AmapAdmin(admin.ModelAdmin):
    list_display = ('nom', 'email',)


class TypeProduitAdmin(admin.ModelAdmin):
    list_display = ('miniature', 'intitule', 'legume')
    list_filter = ('legume',)


class TrimestreAdmin(admin.ModelAdmin):
    list_display = ('__unicode__', 'prix_panier',)
    list_filter = ('annee', 'saison',)


class ClientAdmin(admin.ModelAdmin):
    list_display = ('__unicode__', 'email', 'login', 'ajoute_le', 'modifie_le',)
    list_filter = ('ajoute_le',)
    form = ClientForm


class AbonnementAdmin(admin.ModelAdmin):
    list_display = ('__unicode__', 'paniers', 'jour', 'ajoute_le',)
    list_filter = ('trimestre', 'jour', 'ajoute_le',)


class CommandeClientAdmin(admin.ModelAdmin):
    list_display = ('id', 'client', 'panier', 'paniers', 'jour', 'occasionnelle', 'ajoute_le',)
    list_filter = ('jour', 'occasionnelle', 'ajoute_le',)


class PanierAdmin(admin.ModelAdmin):
    list_display = ('__unicode__', 'prix_revient', 'frais_supplementaires', 'ajoute_le',)
    list_filter = ('ajoute_le',)
    readonly_fields = ('prix_revient', 'periode',)

    def has_add_permission(self, request):
        return False


class ConstitutionAdmin(admin.ModelAdmin):
    list_display = ('__unicode__', 'panier',)
    readonly_fields = ('quantite', 'type_produit', 'panier')

    def has_add_permission(self, request):
        return False


class ProducteurAdmin(admin.ModelAdmin):
    list_display = ('siret', 'nom', 'login', 'ajoute_le', 'modifie_le',)
    list_filter = ('ajoute_le',)
    form = ProducteurForm


class PropositionAdmin(admin.ModelAdmin):
    list_display = ('id', 'producteur', 'produit', 'quantite_proposee', 'quantite_commandee',
                    'prix', 'date_peremption', 'ajoute_le', 'modifie_le',)
    list_filter = ('ajoute_le', 'modifie_le',)
    form = PropositionForm


class CommandeProducteurAdmin(admin.ModelAdmin):
    list_display = ('producteur', 'nom_amap', 'produit', 'quantite_commandee', 'prix', 'periode', 'ajoute_le',)
    list_filter = ('ajoute_le',)
    form = CommandeProducteurForm


class CommandeAmapAdmin(admin.ModelAdmin):
    list_display = ('nom_amap', 'produit', 'quantite_commandee', 'periode', 'ajoute_le',)
    list_filter = ('ajoute_le',)
    form = CommandeAmapForm


# Remplace le gestionnaire d'administration par défaut par celui du site
site = AmapSite()
site._registry = admin.site._registry
admin.site = site

# Enregistrement des modèles
admin.site.register(TypeProduit, TypeProduitAdmin)
admin.site.register(Trimestre, TrimestreAdmin)
admin.site.register(Client, ClientAdmin)
admin.site.register(Abonnement, AbonnementAdmin)
admin.site.register(CommandeClient, CommandeClientAdmin)
admin.site.register(Panier, PanierAdmin)
admin.site.register(Constitution, ConstitutionAdmin)
admin.site.register(Producteur, ProducteurAdmin)
admin.site.register(Proposition, PropositionAdmin)
admin.site.register(CommandeProducteur, CommandeProducteurAdmin)
admin.site.register(CommandeAmap, CommandeAmapAdmin)
