from django.shortcuts import render, redirect
from django.http import JsonResponse
import requests
from SDN_Python import helper as h

def home(request):
 return render(request, "dashboard.html", {}) 

def nodes(request):
  try:
    a = requests.get(h.url('/operational/opendaylight-inventory:nodes/'), auth=h.creds())
    return JsonResponse(a.json())
  except:
    return JsonResponse({})
