from flask import Flask, request, jsonify
app = Flask(__name__)

counter = 0

@app.route("/authenticate", methods=["POST"])
def authenticateapi():
	f = fopen("file" + str(counter) + ".txt", "w")
	f.write(jsonify(request.json))
	f.close()
	counter += 1
	return jsonify({
		"authenticated" : True
	})


@app.route("/", methods=["GET"])
def main():
	return "Hi there!"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)