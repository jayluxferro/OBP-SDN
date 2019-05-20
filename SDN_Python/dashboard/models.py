from django.db import models

# Create your models here.

class Configs(models.Model):
    description = models.CharField(max_length=200)
    console = models.IntegerField()
    command = models.TextField()
