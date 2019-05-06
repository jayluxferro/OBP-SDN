"""
Author: Jay Lux Ferro
Helper Functions
"""

import requests
import json

def url(url):
  return 'http://192.168.198.135:8181/restconf{0}'.format(url)

def creds():
  return ('admin', 'admin')

def nodes():
    try:
        a = requests.get(url('/operational/opendaylight-inventory:nodes/'), auth=creds())
        return json.dumps(a.json())
    except:
        return json.dumps({})


