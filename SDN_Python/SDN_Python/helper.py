"""
Author: Jay Lux Ferro
Helper Functions
"""

import requests
import json

## odl IP
#ip = '192.168.198.135'
ip = 'localhost'

def url(url):
  return 'http://{0}:8181/restconf{1}'.format(ip, url)

def creds():
  return ('admin', 'admin')

def nodes():
    try:
        a = requests.get(url('/operational/opendaylight-inventory:nodes/'), auth=creds())
        return json.dumps(a.json())
    except:
        return json.dumps({})

def uploadFlow(s, d):
    url = "http://{0}:8181/nodes/node/openflow:1/table/0/flow/iperf".format(ip)
    payload = "<flow xmlns=\"urn:opendaylight:flow:inventory\">\n<id>iperf</id>\n<instructions>\n<instruction>\n<order>0</order>\n<apply-actions>\n<action>\n<order>1</order>\n<output-action>\n    <output-node-connector>NORMAL</output-node-connector>\n    <max-length>65535</max-length>\n</output-action>\n</action>\n<action>\n<order>0</order>\n<set-queue-action>\n    <queue-id>123</queue-id>\n</set-queue-action>\n</action>\n</apply-actions>\n</instruction>\n</instructions>\n<barrier>true</barrier>\n<flow-name>iperf</flow-name>\n<match>\n<ethernet-match>\n            <ethernet-type>\n                <type>2048</type>\n            </ethernet-type>\n        </ethernet-match>\n        <ipv4-source>{0}/32</ipv4-source>\n        <ipv4-destination>{1}/32</ipv4-destination>\n        <ip-match>\n            <ip-protocol>6</ip-protocol>         \n        </ip-match>\n        <tcp-destination-port>7000</tcp-destination-port>\n</match>\n<hard-timeout>0</hard-timeout>\n<priority>32768</priority>\n<table_id>0</table_id>\n<idle-timeout>0</idle-timeout>\n</flow>".format(s, d)
    response = requests.put(url, data=payload, auth=creds())
    return response.text


def createQ(interface, minB, maxB):
    return "ovs-vsctl set port {0} qos=@newqos -- --id=@newqos create qos type=linux-htb other-config:max-rate={2} queues:1=@q1 queues:2=@q2 -- --id=@q1 create queue other-config:min-rate={1} other-config:max-rate={2} -- --id=@q2 create queue other-config:min-rate={1} other-config:max-rate={2}".format(interface, minB, maxB)
