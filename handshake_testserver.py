from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route("/authenticate", methods=["POST"])
def authenticateapi():
	data = request.json
	print data

	return jsonify({
		"authenticated" : True,
	})

@app.route("/sign", methods=["POST"])
def signapi():
	data = request.json
	print data

	return jsonify({
		"signed" : True,
	})

@app.route("/", methods=["GET"])
def main():
	return "Hi there!"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)