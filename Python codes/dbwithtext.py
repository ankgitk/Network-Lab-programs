import sqlite3
def q(d):
	a=d.split(" ")
	h=""
	if(a[0]=='create' and a[1]=='database'):
		a[2]="\'"+a[2]+".db\'"
		c=sqlite3.connect(a[2])
		return ("database created and opened successfully")
	elif(a[0]=='create' and a[1]=='table'):
		a="\'\'\'"+d+";\'\'\'"	
		c=sqlite3.connect('test.db')
		print(a)
		c.execute(a)
		return ("table created succesfully")
	elif(a[0]=='insert'):
		a="\""+d+"\""
		c=sqlite3.connect('test.db')
		c.execute(a);
		c.commit()
		return("Records created successfully");
	elif(a[0]=='select'):
		a="\""+d+"\""
		c=sqlite3.connect('test.db')
		e=c.execute(a)
		g=""
		for f in e:
			g+=f+"\n"
		return g
	else:
		return("error")
