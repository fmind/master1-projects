#!/usr/bin/env python
# -*- coding: utf-8 -*-

# Copyright (C) 2012 Freaxmind
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

__author__ = 'Freaxmind'
__email__ = 'freaxmind@freaxmind.pro'
__url__ = 'http://www.freaxmind.pro'
__version__ = '0.1'
__license__ = 'GPLv3'

from Pycluster import *
import MySQLdb as mdb
import decimal
import argparse
import sys

# arguments
parser = argparse.ArgumentParser(description="K-Means Clustering")
parser.add_argument('--query', '-q', required=True,
                    help="SQL Query")
parser.add_argument('-k', type=int,
                    help="Value of k")
parser.add_argument('--indcount', '-i', type=int, default=5,
                    help="Number of individuals to display")
parser.add_argument('--colcount', '-c', type=int, default=100,
                    help="Number of columns in the data matrix")
parser.add_argument('--sort', '-s', action='store_true', default=False,
                    help="Sort the result by group")
parser.add_argument('--transpose', '-t', action='store_true', default=False,
                    help="Transpose the rows/cols before computing")
parser.add_argument('--method', '-m', default="a",
                    help="c.f. PyCluster documentation (p 28)")
parser.add_argument('--distance', '-d', default="c",
                    help="c.f. PyCluster documentation (p 28)")


# DB credentials
HOST = 'nancy.pypatinec.fr'
USER = 'eid'
PASS = 'eid@2013'
DB = 'eid'

if __name__ == "__main__":
    print parser.description
    print ''
    args = parser.parse_args()

    # query
    connection = mdb.connect(HOST, USER, PASS, DB)
    cursor = connection.cursor()
    cursor.execute(args.query)
    connection.close()

    if cursor.rowcount == 0:
        print "aucune donnée renvoyée"
        sys.exit(0)

    # build data => convert MySQL result to a matrix
    data = []
    inds = []
    header = []
    groups = [0] * args.k
    last = None
    i = -1    # index of row
    j = -1    # index of col
    for line in cursor.fetchall():
        row = line[0]
        col = line[1]
        value = line[2]
        # new row
        if row != last:
            i += 1
            data.append([None] * args.colcount)
            inds.append(row)
            last = row
        # new col
        if not col in header:
            header.append(col)
        # new value
        j = header.index(col)
        data[i][j] = float(value)

    # build mask (missing data)
    mask = [[1] * args.colcount for _ in range(len(data))]
    for i, row in enumerate(data):
        for j, col in enumerate(row):
            if col is None:
                mask[i][j] = 0

    # k-means clustering
    labels, error, nfound = kcluster(data, args.k, transpose=args.transpose,
                                    mask=mask, method=args.method,
                                    dist=args.distance)

    # transpose (optional)
    if args.transpose:
        data = zip(*data)
        data = [list(row) for row in data[:len(header)]]
        labels = labels[:len(header)]
        header, inds = inds, header

    # insert label and ind
    header = ["GROUPE", "IND"] + header
    for i, row in enumerate(data):
        row.insert(0, labels[i])
        row.insert(1, inds[i])

    # sort (option)
    if args.sort:
        data = sorted(data, key=lambda row: row[0])

    # print result
    print "nombre de ligne: {0} ({1})".format(len(data), cursor.rowcount)
    print "nombre de colonne: {0} ({1})".format(len(header) - 2, args.colcount)
    print "taux d'erreur: {0}".format(error)

    print ""
    print "\t".join(header)
    for i, row in enumerate(data):
        col = [str(c) for c in row[:len(header)]]
        groups[row[0]] += 1
        print " | ".join(col)

    print "nombre de ligne: {0} ({1})".format(len(data), cursor.rowcount)
    print "nombre de colonne: {0} ({1})".format(len(header) - 2, args.colcount)
    print "taux d'erreur: {0}".format(error)
    print "\nGroupes:"
    for k, group in enumerate(groups):
        print "Groupe {0}: {1} individus".format(k, group)
