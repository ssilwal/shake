import os

from flask import Flask, request, g, redirect,render_template, flash, jsonify
from flask_pymongo import PyMongo

app = Flask("shakefl")
app.config.from_object(__name__)

mongo = PyMongo(app)

@app.route('/')
def show_all():
    online_all = mongo.db.myColl.find({'username':"tswift"})
    return render_template('index.html',
            online_all=online_all)

@app.route('/add_data', methods=['POST'])
def add_data():
    data = request.json
    print(data)
    return jsonify({
        "authenticated":True
    })
