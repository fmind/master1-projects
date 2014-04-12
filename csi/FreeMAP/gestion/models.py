# -*- coding: utf-8 -*-

from django.db import models
from django.dispatch import receiver
from django.core.exceptions import ValidationError
from sorl import thumbnail


def validate_peremption(value):
    from datetime import date
    if value <= date.today():
        raise ValidationError(u"La date de péremption est dépassée")


class Amap(models.Model):
    nom = models.CharField('nom', max_length=50, unique=True)
    adresse = models.TextField('adresse')
    email = models.EmailField('email')

    DATABASE = 'common'

    def __unicode__(self):
        return self.nom

    class Meta:
        ordering = ('nom',)
        verbose_name = "AMAP"
        verbose_name_plural = "AMAP"


class TypeProduit(models.Model):
    intitule = models.CharField("intitulé", max_length=50, unique=True)
    legume = models.BooleanField("légume ?")
    photo = thumbnail.ImageField(upload_to='images/produits')

    DATABASE = 'common'

    def __unicode__(self):
        return self.intitule

    def miniature(self):
        from sorl.thumbnail import get_thumbnail
        img = get_thumbnail(self.photo, '50x50')
        return '<img src="{0}"/>'.format(img.url)
    miniature.allow_tags = True

    class Meta:
        ordering = ('intitule',)
        verbose_name = "Type de Produit"
        verbose_name_plural = "Types de Produits"

@receiver(models.signals.pre_delete, sender=TypeProduit)
def delete_photo(sender, instance, *args, **kargs):
    """ Supprime les photos lorsqu'un type de produit est supprimé """
    from sorl import thumbnail
    if instance.photo:
        thumbnail.delete(instance.photo)


class Trimestre(models.Model):
    SAISONS = (
        (1, u'Printemps'),
        (2, u'Ete'),
        (3, u'Automne'),
        (4, u'Hiver'),
    )
    annee = models.PositiveIntegerField("année")
    saison = models.PositiveIntegerField("saison", choices=SAISONS)
    prix_panier = models.FloatField("prix du panier")

    def __unicode__(self):
        return u"{0} {1}".format(self.SAISONS[self.saison-1][1], self.annee)

    class Meta:
        ordering = ('-annee', '-saison',)
        unique_together = (('saison', 'annee'),)
        verbose_name = "Trimestre"
        verbose_name_plural = "Trimestres"


class Periode(models.Model):
    semaine = models.PositiveIntegerField("numéro de semaine")
    trimestre = models.ForeignKey('Trimestre', verbose_name="trimestre")

    def __unicode__(self):
        return "{0} semaine de {1}".format(self.semaine, self.trimestre.annee)

    class Meta:
        ordering = ('-id',)
        unique_together = (('trimestre', 'semaine'),)
        verbose_name = "Période"
        verbose_name_plural = "Périodes"


class Client(models.Model):
    nom = models.CharField("nom", max_length=50)
    prenom = models.CharField("prénom", max_length=50)
    adresse = models.TextField("adresse")
    email = models.EmailField("email", unique=True)
    telephone = models.CharField("téléphone", max_length=15)
    login = models.CharField("identifiant", max_length=15, unique=True)
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    modifie_le = models.DateField("Modifié le", auto_now=True)

    def __unicode__(self):
        return u"{0} {1}".format(self.prenom, self.nom,)

    class Meta:
        ordering = ('nom', 'prenom',)
        verbose_name = "Client"
        verbose_name_plural = "Clients"


class Abonnement(models.Model):
    JOURS_LIVRAISON = (
        (2, 'Mardi'),
        (4, 'Jeudi'),
    )
    jour = models.PositiveIntegerField("jour de livraison", choices=JOURS_LIVRAISON)
    paniers = models.PositiveIntegerField("nombre de panier", default=1)
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    client = models.ForeignKey('Client', verbose_name="client")
    trimestre = models.ForeignKey('Trimestre', verbose_name="trimestre")

    def __unicode__(self):
        return u"{0} pour {1}".format(self.client, self.trimestre)

    class Meta:
        ordering = ('-ajoute_le',)
        verbose_name = "Abonnement"
        verbose_name_plural = "Abonnements"


class CommandeClient(models.Model):
    jour = models.PositiveIntegerField("jour de livraison", db_index=True, choices=Abonnement.JOURS_LIVRAISON)
    paniers = models.PositiveIntegerField("nombre de panier")
    occasionnelle = models.BooleanField("commande occasionnelle", default=True)
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    client = models.ForeignKey('Client', verbose_name="client")
    panier = models.ForeignKey('Panier', verbose_name="panier")

    def __unicode__(self):
        return unicode(self.id)

    class Meta:
        ordering = ('-ajoute_le',)
        verbose_name = "Commande Client"
        verbose_name_plural = "Commandes Client"


class Panier(models.Model):
    prix_revient = models.FloatField("prix de revient")
    frais_supplementaires = models.FloatField("frais supp.", default=0)
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    periode = models.ForeignKey('Periode', verbose_name="période", unique=True)

    def __unicode__(self):
        return "Panier de la semaine {0} en {1}".format(self.periode.semaine, self.periode.trimestre.annee)

    @staticmethod
    def get_nb_by_periode(week_number):
        """ Retourne le nombre de panier à réaliser pour une période """
        periode = Periode.objects.get(semaine=week_number)
        panier = Panier.objects.get(periode=periode)
        total_abonnes = Abonnement.objects.filter(trimestre=periode.trimestre) \
                                  .aggregate(models.Sum('paniers'))['paniers__sum'] 
        total_occasionnelles = CommandeClient.objects.filter(panier=panier) \
                                             .aggregate(models.Sum('paniers'))['paniers__sum']
        if total_abonnes is None:
            total_abonnes = 0
        if total_occasionnelles is None:
            total_occasionnelles = 0

        return total_abonnes + total_occasionnelles, periode.trimestre

    class Meta:
        ordering = ('-ajoute_le',)
        verbose_name = "Panier"
        verbose_name_plural = "Paniers"


class Constitution(models.Model):
    quantite = models.PositiveIntegerField("quantité")
    panier = models.ForeignKey('Panier', verbose_name="panier")
    type_produit = models.PositiveIntegerField("Type de produit", db_column='type_produit_id') #models.ForeignKey('TypeProduit', verbose_name="type de produit")

    def __unicode__(self):
        return u"{0} {1}".format(self.quantite, self.produit())

    def produit(self):
        return TypeProduit.objects.get(pk=self.type_produit).intitule

    @staticmethod
    def par_periode(week_number):
        periode = Periode.objects.get(semaine=week_number)
        panier = Panier.objects.get(periode=periode)
        constitutions = Constitution.objects.filter(panier=panier)

        return constitutions

    class Meta:
        ordering = ('-panier', 'type_produit')
        unique_together = (('panier', 'type_produit'),)
        verbose_name = "Produit du panier"
        verbose_name_plural = "Produits du panier"


class Producteur(models.Model):
    siret = models.CharField("SIRET", max_length=14, unique=True)
    nom = models.CharField("nom", max_length=50)
    adresse = models.TextField("adresse")
    login = models.CharField("identifiant", max_length=15, unique=True)
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    modifie_le = models.DateField("Modifié le", auto_now=True)

    def __unicode__(self):
        return self.nom

    class Meta:
        ordering = ('nom',)
        verbose_name = "Producteur"
        verbose_name_plural = "Producteurs"


class Proposition(models.Model):
    quantite_proposee = models.PositiveIntegerField("quantité proposée")
    quantite_commandee = models.PositiveIntegerField("quantité commandée")
    prix = models.FloatField("prix du lot")
    date_peremption = models.DateField("date de péremption", validators=[validate_peremption])
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    modifie_le = models.DateField("Modifié le", auto_now=True)
    producteur = models.ForeignKey('Producteur', verbose_name="producteur")
    type_produit = models.PositiveIntegerField("Type de produit", db_column='type_produit_id') #models.ForeignKey('TypeProduit', verbose_name="type de produit")

    def __unicode__(self):
        return u"{0} {1} de {2} pour {3} €".format(self.quantite_proposee-self.quantite_commandee, self.produit(), self.producteur, self.prix)

    def produit(self):
        return TypeProduit.objects.get(pk=self.type_produit).intitule

    @staticmethod
    def par_producteur(producteur):
        from datetime import datetime
        propositions = Proposition.objects.filter(producteur=producteur).order_by('-date_peremption')
        encours = propositions.filter(date_peremption__gt=datetime.today()).filter(quantite_proposee__gt=models.F('quantite_commandee'))
        historique = propositions.filter(models.Q(date_peremption__lte=datetime.today()) | models.Q(quantite_proposee__lte=models.F('quantite_commandee')))

        return encours, historique

    @staticmethod
    def calcul(type_id, qte):
        """ Retourne un ensemble de propositions valides pour un type de produit
            [(proposition, quantite)], prix, manque
        """
        from datetime import datetime
        type_produit = TypeProduit.objects.get(pk=type_id)
        propositions = Proposition.objects.filter(type_produit=type_id) \
                                          .filter(date_peremption__gt=datetime.today()) \
                                          .filter(quantite_proposee__gt=models.F('quantite_commandee'))\
                                          .extra(select={'quantite_disponible': 'quantite_proposee - quantite_commandee'}) \
                                          .order_by('prix', 'date_peremption')

        props = []
        total = 0
        manque = qte
        for p in propositions:
            prop = {'id': p.id, 'prix': p.prix, 'producteur': str(p.producteur)}
            # quantité suffisante pour satisfaire la demande
            if p.quantite_disponible >= manque:
                prop['quantite'] = manque
                props.append(prop)
                total += manque * p.prix
                manque = 0
                break
            # quantité insuffisante
            else:
                prop['quantite'] = p.quantite_disponible
                props.append(prop)
                total += p.quantite_disponible * p.prix
                manque -= p.quantite_disponible

        return props, total, manque

    class Meta:
        ordering = ('-ajoute_le',)
        verbose_name = "Proposition"
        verbose_name_plural = "Propositions"


class CommandeProducteur(models.Model):
    quantite_commandee = models.PositiveIntegerField("quantité commandée")
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    proposition = models.ForeignKey('Proposition', verbose_name="proposition")
    periode = models.ForeignKey('Periode', verbose_name="période")
    amap = models.PositiveIntegerField("Amap", db_column='amap_id')                 #amap = models.ForeignKey('Amap', verbose_name="AMAP")

    def __unicode__(self):
        return unicode(self.id)

    def nom_amap(self):
        return Amap.objects.get(pk=self.amap).nom

    def producteur(self):
        return self.proposition.producteur

    def prix(self):
        return self.proposition.prix

    def produit(self):
        return self.proposition.produit()

    class Meta:
        ordering = ('-ajoute_le',)
        unique_together = (('amap', 'periode', 'proposition'),)
        verbose_name = "Commande Producteur"
        verbose_name_plural = "Commandes Producteurs"


class CommandeAmap(models.Model):
    quantite_commandee = models.PositiveIntegerField("quantité commandée")
    ajoute_le = models.DateField("Ajouté le", auto_now_add=True)
    periode = models.ForeignKey('Periode', verbose_name="période")
    type_produit = models.PositiveIntegerField("Type de produit", db_column='type_produit_id') #models.ForeignKey('TypeProduit', verbose_name="type de produit")
    amap = models.PositiveIntegerField("Amap", db_column='amap_id')                 #amap = models.ForeignKey('Amap', verbose_name="AMAP")

    def __unicode__(self):
        return unicode(self.id)

    def produit(self):
        return TypeProduit.objects.get(pk=self.type_produit).intitule

    def nom_amap(self):
        return Amap.objects.get(pk=self.amap).nom

    class Meta:
        ordering = ('-ajoute_le',)
        unique_together = (('periode', 'amap', 'type_produit'),)
        verbose_name = "Commande AMAP"
        verbose_name_plural = "Commande AMAP"


