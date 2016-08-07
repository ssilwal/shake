import os
import numpy as np

from flask import Flask, request, g, redirect,render_template, flash, jsonify
from flask_pymongo import PyMongo
from pykalman import KalmanFilter

app = Flask("shakefl")
app.config.from_object(__name__)

mongo = PyMongo(app)

@app.route('/')
def show_all():
    pkm()
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

def pkm():
    #https://pykalman.github.io
    #READ Basic Usage
    print("Beginning Kalman Filtering....")
    kf = KalmanFilter(initial_state_mean=0, n_dim_obs=9)

    #measurements - array of 9x1 vals
    #each a_x, a_y, a_z, g_x, g_y, g_z, m_x, m_y, m_z
    measurements = getMeasurementsFromDB()
    smooth_inputs = [[2,0], [2,1], [2,2]]
    #traditional assumed params are known, but we can get from EM on the measurements
    #we get better predictions of hidden states (true position) with smooth function
    kf.em(measurements).smooth(smooth_inputs)

    print(measurements)
    print(kf.em(measurements, n_iter=5))
