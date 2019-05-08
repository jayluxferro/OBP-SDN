#!/usr/bin/python
"""
Interfaces Detector
"""

import sys
import json

# adding helper function
sys.path.append('../SDN_Python')

import helper
import logger as d
import db
import socketio
import pprint
from time import sleep

# defaults
sio = socketio.Client()



try:
    sio.connect('http://127.0.0.1:5000')

except KeyboardInterrupt:
        sys.exit()
except:
    print("No connection to server")
    sys.exit()


## getting all interfaces on openvswitches

data = json.loads(helper.nodes())
data = data['nodes']['node']

switches = []
def getNodes():
    try:
        for x in data:
  
            # checking through all nodes
            switchData = []       
            for n in x['node-connector']:
                branch =  n['opendaylight-port-statistics:flow-capable-node-connector-statistics']
            
                if branch['packets']['received'] > 0:
                    port = n['flow-node-inventory:port-number']
                    link = n['flow-node-inventory:current-speed']
                    interface = n['flow-node-inventory:name']
                    tx = branch['packets']['transmitted']
                    rx = branch['packets']['received']
                    switchData.append({ 'port': port, 'interface': interface, 'rx': rx, 'tx': tx, 'link': link })

            switches.append({ 'id': x['id'], 'data': switchData })
            
        # send data to be analyzed
        d.default('Sending captured nodes to IPC')
        sio.emit('nodes', {'data': switches})

    except Exception as e:
        print(e)
        sio.disconnect()
        sys.exit()

if __name__ == "__main__":
    while True:
        getNodes()
        sleep(5)

