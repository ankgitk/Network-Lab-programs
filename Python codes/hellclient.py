import socket                   # Import socket module
import random
import base64
import hashlib
import sys

s = socket.socket()             # Create a socket object
host = socket.gethostname()     # Get local machine name
port = 7890                    # Reserve a port for your service.

s.connect((host, port))

g = input('Enter the value of g : ')
p = input('Enter the value of p : ')
s.send(str(g))
Ack = s.recv(1024)
print Ack
s.send(str(p))
Ack = s.recv(1024)
print Ack

print '\nAlice Side Calculations:'
a = random.randint(5, 10)
print "alice 's randomly generated secret: ",a
A = (g**a) % p
print "BOB's calculated value (A): ",A,' (g^a) mod p'

s.send(str(A))
print("Receiving Value 'B' from server...")
B = int(s.recv(1024))
print("Value 'B' from BOB = " + repr(B))

print '\nGenerating Key on ALICE Side:'
keyA=(B**a) % p
print 'KeyA: ',keyA,' (B^a) mod p'
print 'Key Calculated on ALICE side : \n',hashlib.sha256(str(keyA)).hexdigest()

print('\nReceiving Key from BOB...')
Key_server = s.recv(1024)
print("Key computed using sha256 of the key")
print("Key Recieved from BOB = \n" + Key_server)


msg = s.recv(1024)
print msg
print("Keys matched")
s.close()
print('Connection closed')

