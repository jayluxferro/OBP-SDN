from django.conf.urls import url
from . import views

urlpatterns = [
  url(r'^$', views.home, name='home'), 
  url(r'^nodes/', views.nodes, name='nodes'), 
  url(r'^iperf/', views.iperf, name='iperf'),
  url(r'^obs/', views.obs, name='obs'),
  url(r'^packets/', views.packets, name='packets'),
  url(r'^ping/', views.packets, name='ping'),
]
