Structure du dossier:
    - docs/
        * Rapport au format ODT et PDF
        * Captures d'écran de l'application
        * Sujet du projet
    - FreeMAP/
        * code source du projet (application Django)
        * gestion/ module de l'interface d'administration et de composition
            # models.py modèle de domaine
            # views.py traitements
            # templates/ affichages
        * utilisateurs/ module de l'interface client
        * producteurs/ module de l'interface producteur


Connexion à l'interface de la base de données:
    - adresse : https://phppgadmin.alwaysdata.com/
    - login/mdp de la base principale (local1 + commune) : freaxmind_freemap_main / FR33style
    - login/mpd de la base secondiare (local2): freaxmind_freemap_other / FR33stole
    
    
Connexion à l'interface de gestion des AMAP:
    - site principal:
        - adresse: http://freemap-main.freaxmind.pro/administration/
        - login / mdp : armelle / brun
    - site secondaire:
        - adresse: http://freemap-other.freaxmind.pro/administration/
        - login / mdp : armelle / brun


L'accès aux différentes table de la base se fait en déroulant le menu à gauche (cf. postgresql/index.png).
Les tables que nous avons implémenté sont préfixées par "gestion_".

Après avoir selectionner une table, on peux voir la liste des triggers associés à celle-ci en cliquant sur le lien "Triggers" présent en haut (cf. postgresql/liste des triggers.png)

Le code des triggers que nous avons implémenté est présent dans les fonctions et est accessible en déroulant le menu à gauche (cf. postgresql/liste des fonctions.png)

