import os,csv
import numpy as np
import pymongo

connection = pymongo.MongoClient()

def analyzeGesture(gestureNo):
    #for filename in os.listdir('gesture'+ str(gestureNo)):
    for filename in os.listdir('gesture1'):
        with open('gesture1/'+ filename, 'rt') as csvfile:
            rowReader = csv.reader(csvfile, delimiter=',')
            counter = 0
            for row in rowReader:
                for r in row:
                    print(r)
            if(connection.testgesture.g1.count() == 0):
                for row in rowReader:
                    for r in row:
                        print(r)
                    connection.testgesture.g1.insert({
                        'id': counter,
                        'data':row
                    })
                    counter +=1
                    #mongo.db.g1.insert(row);
                    print(row)
            print (counter)
def main():
    print('Start analyzing gesture...')
    gesture_number = 1
    analyzeGesture(gesture_number)
    print('Printing contents of g1: ')
    for doc in connection.testgesture.g1.find({}):
        print(doc)
    print('Completed!')

main()
