#!/usr/bin/python

"""
Database module

"""

import sqlite3


def init():
    conn = sqlite3.connect(os.path.dirname(os.path.realpath(__file__)) +'/sdn.db')
    conn.row_factory = sqlite3.Row
    return conn
