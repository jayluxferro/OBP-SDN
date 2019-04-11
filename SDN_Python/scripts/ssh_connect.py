#!/usr/bin/python3

import paramiko
import sys

ssh = paramiko.SSHClient()
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
ssh.connect(sys.argv[1], username=sys.argv[2], password=sys.argv[3])
ssh_stdin, ssh_stdout, ssh_stderr = ssh.exec_command(sys.argv[4])

while True:
    recv_data = ssh_stdout.channel.recv(1024)
    if len(recv_data) == 0:
        sys.exit(0)
    for x in recv_data.decode().split('\n'):
        print(x)

