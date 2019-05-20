from django.shortcuts import render, redirect
from django.http import JsonResponse
import requests
from SDN_Python import helper as h
from . import forms, models
from django.views.decorators.csrf import csrf_exempt

def home(request):
 return render(request, "dashboard.html", {}) 

def nodes(request):
  try:
    a = requests.get(h.url('/operational/opendaylight-inventory:nodes/'), auth=h.creds())
    return JsonResponse(a.json())
  except:
    return JsonResponse({})

@csrf_exempt
def iperf(request):
    if request.method == "POST":
        # add to bandwidth
        IpForm = forms.Iperf(request.POST)
        print(request.POST)
        if IpForm.is_valid():
            # valid / add to db
            data = IpForm.cleaned_data['data']
            start = IpForm.cleaned_data['start']
            stop = IpForm.cleaned_data['stop']
            threads = IpForm.cleaned_data['threads']
            interval = IpForm.cleaned_data['interval']
            transfer = IpForm.cleaned_data['transfer']
            bandwidth = IpForm.cleaned_data['bandwidth']
            duration = IpForm.cleaned_data['duration']

            try:
                iperfModel = models.Iperf()
                iperfModel.data = data
                iperfModel.start = start
                iperfModel.stop = stop
                iperfModel.threads = threads
                iperfModel.interval = interval
                iperfModel.transfer = transfer
                iperfModel.bandwidth = bandwidth
                iperfModel.duration = duration

                # saving data
                iperfModel.save()
                return JsonResponse({'success': True})

            except:
                # failed to save data
                return JsonResponse({'success': False })
        else:
            return JsonResponse({})
    else:
        return JsonResponse({})


@csrf_exempt
def obs(request):
    if request.method == "POST":
        obsForm = forms.Obs(request.POST)
        print(request.POST)
        if obsForm.is_valid():
            # valid
            start = obsForm.cleaned_data['start']
            stop = obsForm.cleaned_data['stop']
            old = obsForm.cleaned_data['old']
            new = obsForm.cleaned_data['new']
            duration = obsForm.cleaned_data['duration']

            try:
                obsModel = models.Obs()
                obsModel.start = start
                obsModel.stop = stop
                obsModel.old = old
                obsModel.new = new
                obsModel.duration = duration
                obsModel.save()

                return JsonResponse({'success': True})
            except:

                return JsonResponse({'success': False})
        else:
            return JsonResponse({})
    else:
        return JsonResponse({})

