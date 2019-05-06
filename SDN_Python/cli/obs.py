#!/usr/bin/python
"""
Interfaces Detector
"""

import sys

# adding helper function
sys.path.append('../SDN_Python')
import helper
import logger as d
import db
import socketio


# defaults
sio = socketio.Client()

try:
    sio.connect('http://127.0.0.1:5000')

except KeyboardInterrupt:
        sys.exit()

except:
        sys.exit()
