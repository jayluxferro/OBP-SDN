from django.db import models

# Create your models here.

class Configs(models.Model):
    description = models.CharField(max_length=200)
    console = models.IntegerField()
    command = models.TextField()


class Iperf(models.Model):
    data = models.CharField(max_length=1000)
    start = models.CharField(max_length=200)
    stop = models.CharField(max_length=200)
    threads = models.IntegerField()
    interval = models.CharField(max_length=200)
    transfer = models.CharField(max_length=100)
    bandwidth = models.CharField(max_length=100)
    duration = models.CharField(max_length=100)


class Obs(models.Model):
    start = models.CharField(max_length=200)
    stop = models.CharField(max_length=200)
    old = models.CharField(max_length=200)
    new = models.CharField(max_length=200)
    duration = models.CharField(max_length=200)
