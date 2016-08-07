import os
import sqlite3

from flask import Flask, request, session, g, redirect, url_for, abort, \
     render_template, flash

from flask_pymongo import PyMongo

app = Flask('flaskr')
app.config.from_object('flaskr')

mongo = PyMongo(app)

#Load default config
app.config.update(dict(
    DATABASE=os.path.join(app.root_path, 'flaskr.db'),
    SECRET_KEY='development key',
    USERNAME='admin',
    PASSWORD='default'
))
app.config.from_envvar('FLASKR_SETTINGS', silent=True)

#
# def init_db():
#     db = get_db()
#     with app.open_resource('schema.sql', mode='r') as f:
#         db.cursor().executescript(f.read())
#     db.commit()
#
# @app.cli.command('initdb')
# def initdb_command():
#     """Initializes the database"""
#     print('Trying to initialize...')
#     init_db()
#     print ('Initialized the database.')
#
# def connect_db():
#     rv = sqlite3.connect(app.config['DATABASE'])
#     rv.row_factory = sqlite3.Row
#     return rv
# def get_db():
#     """Opens a new database connection if there is none yet for the
#     current application context.
#     """
#     if not hasattr(g, 'sqlite_db'):
#         g.sqlite_db = connect_db()
#     return g.sqlite_db
# @app.teardown_appcontext
# def close_db(error):
#     """Closes the database again at the end of the request."""
#     if hasattr(g, 'sqlite_db'):
#         g.sqlite_db.close()
@app.route('/')
def show_shakes():
    online_users = mongo.db.users.find({'online': True})
    return render_template('index.html',
        online_users=online_users)
    # db = get_db()
    # cur = db.execute('select username, shakedata from shakes order by id desc')
    # shakes = cur.fetchall()
    # return render_template("show_shakes.html", shakes=shakes)
# @app.route('/add', methods=['POST'])
# def add_shake():
#     if not session.get('logged_in'):
#         abort(401)
#     db = get_db()
#     db.execute('insert into shakes (username, shakedata) values (?, ?)',
#                     [request.form['username'], request.form['shakedata']])
#     db.commit()
#     flash('New entry was successfully posted')
#     return redirect(url_for('show_shakes'))
#
# @app.route('/login', methods=['GET','POST'])
# def login():
#     error = None
#     if request.method == 'POST':
#         if request.form['username'] != app.config['USERNAME']:
#             error = 'Invalid username'
#         elif request.form['password'] != app.config['PASSWORD']:
#             error = 'Invalid password'
#         else:
#             session['logged_in'] = True
#             flash('You were logged in')
#             return redirect(url_for('show_shakes'))
#     return render_template('login.html', error=error)
# @app.route('/logout')
# def logout():
#     session.pop('logged_in',None)
#     flash('You were logged out')
#     return redirect(url_for('show_shakes'))
#
# @app.route('/add_data', method=['POST'])
# def add_data():
#     data = request.json

# if __name__ == "__main__":
#     app.run()
