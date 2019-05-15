#!/usr/bin/python
"""
Interfaces Detector
"""

import sys
from prettytable import PrettyTable


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
    
    p = PrettyTable()
    p.field_names = ["Switches", "Interface", "Tx", "Rx", "Port Number", "IP Address", "Console Port"]
    
    for x in info:
        p.add_row([str(x['id']), x['data'][0]['interface'], str(x['data'][0]['tx']), str(x['data'][0]['rx']), str(x['data'][0]['port']), str(x['data'][0]['ip']), str(x['data'][0]['console'])])
        sio.emit(str(x['data'][0]['console']), { 'data': x['data'][0] })
        """
        if x['id'] == "openflow:60174091252288":
            p.add_row([str(x['id']), x['data'][0]['interface'], str(x['data'][0]['tx']), str(x['data'][0]['rx']), str(x['data'][0]['port']), str(x['data'][0]['ip']), str(x['data'][0]['console'])])
        else:
            p.add_row([str(x['id']), x['data'][3]['interface'], str(x['data'][3]['tx']), str(x['data'][3]['rx']), str(x['data'][3]['port']), str(x['data'][3]['ip']), str(x['data'][3]['console'])])
    
        """
    # display table
    print(p)









############### ... MAIN ... ################################

if __name__   == "__main__":
    try:
        sio.connect('http://127.0.0.1:5000')

    except KeyboardInterrupt:
        sys.exit()

    except:
        sys.exit()
