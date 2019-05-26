#!/usr/bin/python

"""
Database module

"""

import sqlite3
import os

prefix="dashboard_"

def init():
    conn = sqlite3.connect(os.path.dirname(os.path.realpath(__file__)) +'/../db.sqlite3')
    conn.row_factory = sqlite3.Row
    return conn

def getData(table):
    conn = init()
    cursor = conn.cursor()
    cursor.execute("select * from {0} order by id asc".format(prefix + table))
    return cursor.fetchall()

