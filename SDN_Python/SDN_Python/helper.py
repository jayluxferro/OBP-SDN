"""
Author: Jay Lux Ferro
Helper Functions
"""

def url(url):
  return 'http://localhost:8181/restconf{0}'.format(url)

def creds():
  return ('admin', 'admin')
