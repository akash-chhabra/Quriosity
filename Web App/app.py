import os
import random
import requests

from flask import Flask, jsonify
from blockchain import Blockchain
from pubsub import PubSub

app = Flask(__name__)
blockchain = Blockchain()
pubsub = PubSub(blockchain)

@app.route('/')
def route_default():
    return "TEST"

@app.route('/blockchain')
def route_blockchain():
    return jsonify(blockchain.to_json())

@app.route('/blockchain/mine')
def route_mine():
    transection_data = "temp data"
    blockchain.addBlock(transection_data)
    block = blockchain.chain[-1]
    pubsub.broadcast_block(block)
    return jsonify(block.to_json())

ROOT_PORT = 5000
PORT = ROOT_PORT
if __name__ == '__main__':
    print('PEER',os.environ.get('PEER'))

    if os.environ.get('PEER') == 'T':
        PORT = random.randint(5001,6000)
        result = requests.get(f'http://localhost:{ROOT_PORT}/blockchain')
        result_blockchain = Blockchain.from_json(result.json())

        try:
            blockchain.replace_chain(result_blockchain.chain)
            print('\n Blockchain updated successfully')
        except Exception as e:
            print(f'\nError in syncing {e}')
    app.run(port=PORT)