import socket                   # Import socket module
import random
import base64
import hashlib
import sys

port = 7890                     # Reserve a port for your service.
s = socket.socket()             # Create a socket object
host = socket.gethostname()     # Get local machine name
s.bind((host, port))            # Bind to the port
s.listen(5)                     # Now wait for client connection.

print 'Server listening...'

while True:
    conn, addr = s.accept()     # Establish connection with client.
    print 'Got connection from', addr
    g = int(conn.recv(1024))
    conn.send('g Sent to BOB!')
    p = int(conn.recv(1024))
    conn.send('p Sent to BOB!')
    
    print 'g: ',g,' (a shared value), n: ',p, ' (a prime number)'
    
    print '\nBOB Side Calculations:'
    b = random.randint(10, 20)    
    print "b (BOBs randomly generated secret): ",b
    B = (g**b) % p    
    print "BOB's calculated value (B): ",B,' (g^b) mod p'    
    
    print("Receiving Value 'A' from Alice...0")
    A = int(conn.recv(1024))
    print("Value 'A' from Alice= " + repr(A))
    conn.send(str(B))
    
    print '\nGenerating Key on BOBs Side:'
    keyB=(A**b) % p
    print 'KeyB: ',keyB,' (A^b) mod p'
    print 'Key Calculated on BOBs side: \n',hashlib.sha256(str(keyB)).hexdigest()
    print 'Keys matched'
    conn.send(str(hashlib.sha256(str(keyB)).hexdigest()))
    conn.send('\n\nClosing BOBs connection...')
    conn.close()


