import json, sys

for filename in sys.argv[1:]:
	f = open(filename, "r")
	outf = open(filename[:-4] + ".csv","w")
	data = json.loads(f.read())["data"]
	for row in data:
		vector = str(row["data"])[1:-1]
		outf.write(vector + "\n")