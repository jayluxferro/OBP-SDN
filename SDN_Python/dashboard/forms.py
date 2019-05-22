from django import forms

class Iperf(forms.Form):
    data = forms.CharField(max_length=1000)
    start = forms.CharField(max_length=200)
    stop = forms.CharField(max_length=200)
    threads = forms.IntegerField()
    interval = forms.CharField(max_length=200)
    transfer = forms.CharField(max_length=100)
    bandwidth = forms.CharField(max_length=100)
    duration = forms.CharField(max_length=100)


class Obs(forms.Form):
    start = forms.CharField(max_length=200)
    stop = forms.CharField(max_length=200)
    old = forms.CharField(max_length=200)
    new = forms.CharField(max_length=200)
    duration = forms.CharField(max_length=200)


class Packets(forms.Form):
    tx = forms.CharField(max_length=200)
    rx = forms.CharField(max_length=200)


class Ping(forms.Form):
    duration = forms.CharField(max_length=200)
