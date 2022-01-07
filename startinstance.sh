#!/bin/bash

cd /opt/vtagadmin
./start-qc.sh start 80
sleep 10
curl -L http://127.0.0.1:80