#!/usr/bin/python

import matplotlib.pyplot as plt
import numpy as np
import db


### Getting ping results
ping = []
for i in db.getData('ping'):
    ping.append(float(i['duration']))

# display  graph
plt.figure(1)
plt.plot(np.linspace(1, len(ping), len(ping)), ping, '-ro')
plt.xlabel('Attempts')
plt.ylabel('Round Trip Time (ms)')
plt.title('Round Trip Time')
plt.grid(True)
plt.show()


### Bandwidth Change
iperf = []
iperfInterval = []
for i in db.getData('iperf'):
    iperf.append(float(i['bandwidth']))
    iperfInterval.append(float(i['interval']))
iperf.sort()
plt.figure(2)
plt.plot(np.linspace(1, len(iperf), len(iperf)), iperf, '-bo')
plt.grid(True)
plt.xlabel('Number of parallel connections')
plt.ylabel('Bandwidth (Mbps)')
plt.title('On-Demand Bandwidth Test')
plt.show()


### Throughput
tx = []
for i in db.getData('packets'):
    tx.append(float(i['tx']))
tx.sort()
plt.figure(3)
plt.plot(np.linspace(1, len(tx), len(tx)), tx, 'r')
plt.grid(True)
plt.ylabel('Bytes')
plt.title('Throughput')
plt.show()


## Bandwidth change per thread vs duration
fix, ax1 = plt.subplots()
color = 'tab:red'
ax1.set_xlabel('Number of Parallel Connections')
ax1.set_ylabel('Bandwidth (Mbps)', color=color)
ax1.plot(np.linspace(1, len(iperf), len(iperf)), iperf, color=color)
ax1.tick_params(axis='y', labelcolor=color)

ax2 = ax1.twinx()
color = 'tab:blue'
ax2.set_ylabel('Duration (seconds)')
ax2.plot(np.linspace(1, len(iperfInterval), len(iperfInterval)), iperfInterval, color=color)
ax2.tick_params(axis='y', labelcolor=color)
plt.grid(True)
plt.show()

