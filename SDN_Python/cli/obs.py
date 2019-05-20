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
import telnetlib
import requests
import time

# defaults
sio = socketio.Client()
b5 = 0
b4 = 0

# default url
webUrl = "http://localhost:8000/"

try:
    sio.connect('http://127.0.0.1:5000')

except KeyboardInterrupt:
        sys.exit()

except:
        sys.exit()



def sendData(dat):
    d.warning(str(dat))
    res = requests.post(webUrl + 'obs/', data=dat)
    d.success(str(res.text))

# done connecting to websocket; connect to telnet
@sio.on('5005')
def op5(data):
    info = data['data']
    ip = info['ip']
    console = info['console']
    src = '192.168.80.9'
    dst = '192.168.100.8'
    interface = info['interface']
    tx = info['tx']
    rx = info['tx']

    tx = int(tx) + int(rx) 
    minB = 0
    maxB = tx
    ip = '192.168.198.128'

    global b5
    
    # create queue
    if b5 != maxB:
        d.default('Creating queue: ' + str(ip) + ':' + str(console))
        queue = helper.createQ(interface, minB, maxB)
        tn = telnetlib.Telnet(ip, console)
        tn.write("ovs-vsctl -- --all destroy QoS -- --all destroy Queue\n".encode('ascii'))
        tn.write((queue + "\n").encode('ascii'))
        d.success('Queue created...: ' + str(console))

        # push to odl
        helper.uploadFlow(src, dst)

        # sending data to web app
        webData = {'start':  str(info['start']), 'stop':  str(time.time()), 'duration':  str(time.time() - info['start']), 'old': str(b5), 'new': str(maxB)}
        sendData(webData)
        
        b5 = maxB
    else:
        d.default('Not setting bandwidth: no changes detected')

@sio.on('5014')
def op4(data):
    info = data['data']
    ip = info['ip']
    console = info['console']
    dst = '192.168.80.9'
    src = '192.168.100.8'
    interface = info['interface']
    tx = info['tx']
    rx = info['tx']

    tx = int(tx) + int(rx) 
    minB = 0
    maxB = tx

    ip = '192.168.198.128'
    
    global b4

    if b4 != maxB:
        d.default('Creating queue: ' + str(ip) + ':' + str(console))
        # create queue
        queue = helper.createQ(interface, minB, maxB)
        tn = telnetlib.Telnet(ip, console)
        # delete existing queue
        tn.write("ovs-vsctl -- --all destroy QoS -- --all destroy Queue\n".encode('ascii'))
        tn.write((queue + "\n").encode('ascii'))
        d.success('Queue created: ' + str(console))

        # push to odl
        helper.uploadFlow(src, dst)
    else:
        d.default('Not setting bandwidth: no changes detected')





