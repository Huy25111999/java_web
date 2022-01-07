mongodump -u "sadmin" -p '123456aA@' -d localhostdb -o /opt/bu
tar -zcvf localhostdb.tar.gz localhostdb/
exit
sudo scp root@...:/opt/bu/localhostdb.tar.gz /home/db/