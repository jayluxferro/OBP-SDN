#!/usr/bin/python

"""
IPC

"""

import socketio
import eventlet
import pprint
import sys
import logger as d
import db

### DEFAULTS #####
sio = socketio.Server()
app = socketio.WSGIApp(sio, static_files={
    '/': {'content_type': 'text/html', 'filename': 'index.html'}
})


## connection defaults ###

sio.on('connect')
def connect(sid, environ):
    d.success('Client socket opened => ' + sid)


sio.on('disconnect')
def disconnect(sid):
    d.error('Client socket closed => ' + sid)

try:
    eventlet.wsgi.server(eventlet.listen(('127.0.0.1', 5000)), app)
except KeyboardInterrupt:
    sys.exit()