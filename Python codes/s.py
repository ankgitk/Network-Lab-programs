import socket
import dbwithsqllite
host='127.0.0.1'
port=5028
s=socket.socket()
s.bind((host,port))
s.listen(1)
c,addr=s.accept()
while True:
	d=c.recv(1024)
	print(str(d))
	if(d=='exit'):
		break
	c.send(dbwithsqllite.q(d))
c.close()
