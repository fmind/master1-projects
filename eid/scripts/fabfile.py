#!usr/bin/env python
# -*- coding: utf-8 -*-

from fabric.api import *


def for_students_by_year(k=2, year=None, level=None, country=None):
    filters = ""
    filters += " AND annee_id='{0}'".format(year) if year else ""
    filters += " AND niveau='{0}'".format(level) if level else ""
    filters += " AND pays='{0}'".format(country) if country else ""

    query = """ \n
            SELECT
                CONCAT(eco.pays, '_', res.annee_id, '_', cla.niveau, '_',
                       etu.nom, '_', prenom)
                as row,
                'note finale' as col,
                note_finale as value
            FROM RESULTATS_ANNEE res
            INNER JOIN ECOLES eco ON res.ecole_id = eco.id
            INNER JOIN CLASSES cla ON res.classe_id = cla.id
            INNER JOIN ETUDIANTS etu ON res.etudiant_id = etu.id
            WHERE 1 {0}
            ORDER BY row, col
            """.format(filters)

    local('python cluster.py -k {0} -c 1 -d e -s --query="{1}"'
          .format(k, query))


def for_students_by_modul(k=2, year=None, level=None, country=None, coef=None):
    filters = ""
    filters += " AND annee_id='{0}'".format(year) if year else ""
    filters += " AND niveau='{0}'".format(level) if level else ""
    filters += " AND pays='{0}'".format(country) if country else ""
    filters += " AND coefficient='{0}'".format(coef) if coef else ""

    query = """ \n
            SELECT
                CONCAT(eco.pays, '_', res.annee_id, '_', cla.niveau, '_',
                       etu.nom, '_', prenom)
                as row,
                modu.categorie as col,
                note as value
            FROM RESULTATS_MODULE res
            INNER JOIN ECOLES eco ON res.ecole_id = eco.id
            INNER JOIN CLASSES cla ON res.classe_id = cla.id
            INNER JOIN ETUDIANTS etu ON res.etudiant_id = etu.id
            INNER JOIN MODULES modu ON res.module_id = modu.id
            WHERE 1 {0}
            ORDER BY row, col
            """.format(filters)

    local('python cluster.py -k {0} -c 30 -s --query="{1}"'
          .format(k, query))


def for_students_by_subject(k=2, year=None, level=None, country=None,
                            modul=None, coef=None):
    filters = ""
    filters += " AND annee_id='{0}'".format(year) if year else ""
    filters += " AND niveau='{0}'".format(level) if level else ""
    filters += " AND pays='{0}'".format(country) if country else ""
    filters += " AND categorie='{0}'".format(modul) if modul else ""
    filters += " AND mat.coefficient='{0}'".format(coef) if coef else ""

    query = """ \n
            SELECT
                CONCAT(eco.pays, '_', res.annee_id, '_', cla.niveau, '_',
                       etu.nom, '_', prenom)
                as row,
                mat.nom as col,
                note as value
            FROM RESULTATS_MATIERE res
            INNER JOIN ECOLES eco ON res.ecole_id = eco.id
            INNER JOIN CLASSES cla ON res.classe_id = cla.id
            INNER JOIN ETUDIANTS etu ON res.etudiant_id = etu.id
            INNER JOIN MODULES modu ON res.module_id = modu.id
            INNER JOIN MATIERES mat ON res.module_id = mat.id
            WHERE 1 {0}
            ORDER BY row, col
            """.format(filters)

    local('python cluster.py -k {0} -c 100 -s --query="{1}"'
          .format(k, query))


def for_subjects(k=2, year=None, level=None, country=None,
                            modul=None, coef=None):
    filters = ""
    filters += " AND annee_id='{0}'".format(year) if year else ""
    filters += " AND niveau='{0}'".format(level) if level else ""
    filters += " AND pays='{0}'".format(country) if country else ""
    filters += " AND categorie='{0}'".format(modul) if modul else ""
    filters += " AND mat.coefficient='{0}'".format(coef) if coef else ""

    query = """ \n
            SELECT
                CONCAT(eco.pays, '_', res.annee_id, '_', cla.niveau, '_',
                       etu.nom, '_', prenom)
                as row,
                mat.nom as col,
                note as value
            FROM RESULTATS_MATIERE res
            INNER JOIN ECOLES eco ON res.ecole_id = eco.id
            INNER JOIN CLASSES cla ON res.classe_id = cla.id
            INNER JOIN ETUDIANTS etu ON res.etudiant_id = etu.id
            INNER JOIN MODULES modu ON res.module_id = modu.id
            INNER JOIN MATIERES mat ON res.module_id = mat.id
            WHERE 1 {0}
            ORDER BY row, col
            """.format(filters)

    local('python cluster.py -k {0} -t -c 600 -s --query="{1}"'
          .format(k, query))


def prompt():
    ''' Open a MySQL prompt. '''
    local('mysql --host=nancy.pypatinec.fr --user=eid --password=eid@2013 eid')
