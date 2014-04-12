#!/usr/bin/env python
#-*- coding: utf-8 -*-

from bottle import route, redirect, run, template, static_file
from datetime import datetime
import bottle, sqlite3

connection = sqlite3.connect("score.db")


@route('/img/<filename:path>')
def send_image(filename):
    return static_file(filename, root='img/', mimetype='image/jpeg')

@route('/')
def index():
    cursor = connection.cursor()
    scores = cursor.execute('''SELECT * FROM Scores ORDER BY score DESC LIMIT 6''').fetchall()
    bottle.TEMPLATES.clear()

    return template('score', scores=scores)

#no html (for API)
@route('/top')
def top():
    cursor = connection.cursor()
    scores = cursor.execute('''SELECT * FROM Scores ORDER BY score DESC LIMIT 6''').fetchall()

    top = list()
    i = 0
    for name, score, date in scores:
        i += 1
        t = u"{0};{1};{2};{3}".format(i, name, score, date)
        top.append(t)

    return "\n".join(top)

@route('/new/:name/:score')
def new(name, score):
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    connection.execute('''INSERT INTO Scores VALUES('{0}', '{1}', '{2}')'''.format(name, score, now))
    connection.commit()
    redirect('/')

run(host='127.0.0.1', port=9000)
