import socket
host='127.0.0.1'
port=5028
s=socket.socket()
s.connect((host,port))
while True:
	d=raw_input("Enter your query : ")
	s.send(d)
	if(d=='exit'):
		break
	d=s.recv(1024)
	print(str(d))
s.close()
