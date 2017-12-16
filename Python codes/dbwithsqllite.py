import sqlite3
def q(d):
	a=d.split(" ")
	h=""
	if(a[0]=='create' and a[1]=='database'):
		try:	
			a[2]="\'"+a[2]+".db\'"
			conn=sqlite3.connect(a[2])
			return ("database created and opened successfully")
		except:
			return("error in query")
	elif(a[0]=='CREATE' and a[1]=='TABLE'):
		try:
			conn=sqlite3.connect('test.db')
			c=conn.cursor()
			c.execute(d)
			return ("table created succesfully")
		except:
			return("error in query")
	elif(a[0]=='INSERT'):
		try:
			a="\""+d+"\""
			conn=sqlite3.connect('test.db')
			c=conn.cursor()
			c.execute(d)
			conn.commit()
			return("Records created successfully");
		except:
			return("error in query")
	elif(a[0]=='SELECT' and a[1]=='*'):	
		try:
			conn=sqlite3.connect('test.db')
			c=conn.cursor()
			e=[]
                	for row in c.execute(d):
				e.append(row)
			s='OUTPUT QUERIES ARE : \n'
			for i in e:
				for j in i:
					s+=str(j)+"\t"
				s+="\n"		
			return(s)
		except:
			return("error in query")
	elif(a[0]=='DELETE' and a[1]=='FROM'):
		try:
			conn=sqlite3.connect('test.db')
			c=conn.cursor()
			c.execute(d)
			conn.commit()
			return("Records deleted")
		except:
			return("error in query")
	else:
		return("error")
