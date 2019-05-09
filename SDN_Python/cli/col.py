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
import subprocess

# defaults
sio = socketio.Client()

def clearScreen():
    subprocess.run('clear')


############# Event Handlers ####################
@sio.on('nodes')
def nodes(data):
    clearScreen()
    #d.warning('Data received: ' + str(data))
    info = data['data']
    
    d.warning(str("#" * 100))
    d.warning("| Switches               | Interface | Tx | Rx | Port Number | IP Address |")
    d.warning("#" * 100)
    for x in info:
        if x['id'] == "openflow:60174091252288":
            d.warning("| " + str(x['id']) + ' | ' + x['data'][1]['interface'] + '      | ' + str(x['data'][1]['tx']) + ' | ' + str(x['data'][1]['rx']) + ' | ' + str(x['data'][1]['port']) + '           | ' + x['data'][1]['ip'] + '|')
        else:
            d.warning("| " + str(x['id']) + ' | ' + x['data'][3]['interface'] + '      | ' + str(x['data'][3]['tx']) + ' | ' + str(x['data'][3]['rx']) + ' | ' + str(x['data'][3]['port']) + '           | ' + x['data'][3]['ip'] + ' |')
    d.warning("#" * 100)
    









############### ... MAIN ... ################################

if __name__   == "__main__":
    try:
        sio.connect('http://127.0.0.1:5000')

    except KeyboardInterrupt:
        sys.exit()

    except:
        sys.exit()
