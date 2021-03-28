import os
import random
import requests
import time

from flask import Flask, jsonify,render_template, url_for, request,flash, redirect
from forms import MineForm, TransactionForm
from blockchain import Blockchain
from transaction import Transaction
from pubsub import PubSub
from cal_balance import cal_balance


app = Flask(__name__)
app.config['SECRET_KEY'] = '8aedcc8599c74b3dbc7b00e934eb604d'
blockchain = Blockchain()
pubsub = PubSub(blockchain)
current_user = f'user_{str(random.randint(1,100))}'
other_user = f'user_{str(random.randint(101,200))}'

MINING_REWARD = 100

# @app.template_filter('ctime')
# def timectime(s):
#     return time.ctime(s) # datetime.datetime.fromtimestamp(s)

@app.route('/')
def route_default():
    return 'TEST'

@app.route('/blockchain', methods=['GET', 'POST'])
def route_blockchain():
    form = MineForm()
    if request.method == 'GET':
        user = [current_user, cal_balance(blockchain,current_user)]
        chain = blockchain.chain
        return render_template('blockchain.html',user = user, chain=chain, form=form)
    if form.validate_on_submit():
        route_mine()
        flash('Block Mined Successfully', 'success')
        return redirect(url_for('route_blockchain'))
    else:
        return jsonify(blockchain.to_json())


@app.route('/blockchain/mine')
def route_mine():
    if len(blockchain.pending_transaction) == 0:
        blockchain.addBlock([Transaction('MINER_REWARD',current_user,MINING_REWARD).to_json()])
    else:
        blockchain.pending_transaction.append(Transaction('MINER_REWARD',current_user,MINING_REWARD))
        all_transaction = blockchain.pending_to_json()
        blockchain.addBlock(all_transaction)
        blockchain.pending_transaction = []
    block = blockchain.chain[-1]
    pubsub.broadcast_block(block)
    pubsub.broadcast_pending_transaction(blockchain)
    return jsonify(block.to_json())


@app.route('/blockchain/pending_transaction', methods=['GET', 'POST'])
def route_pending_transaction():
    form = MineForm()
    if request.method == 'GET':
        user = [current_user, cal_balance(blockchain,current_user)]
        pending = blockchain.pending_transaction
        return render_template('pending.html',user = user, pending=pending,form=form)
    if form.validate_on_submit():
        route_mine()
        flash('Block Mined Successfully', 'success')
        return redirect(url_for('route_blockchain'))
    else:
        return jsonify(blockchain.pending_to_json())


@app.route('/blockchain/transaction', methods=['GET', 'POST'])
def route_transaction():
    form = TransactionForm()
    if request.method == 'GET':
        user = [current_user, cal_balance(blockchain,current_user)]
        return render_template('transaction.html',user = user,form=form)

    if form.validate_on_submit():
        t = Transaction(current_user,form.receiver.data,form.amt.data)
        try:
            t.isValidTransaction(blockchain)
            blockchain.pending_transaction.append(t)
        except Exception as e:
            flash(f'Invalid Transaction: {e}','danger')
            return redirect(url_for('route_transaction'))
        else:
            print('updated pending transcations',blockchain.pending_transaction)
            pubsub.broadcast_pending_transaction(blockchain)
        return redirect(url_for('route_blockchain'))
    # if request.method == 'POST':    
    #     last_transaction = blockchain.pending_transaction[-1]
    #     return jsonify(last_transaction.to_json())


# def compare_chains():
#     result = requests.get(f'http://localhost:{ROOT_PORT}/blockchain')
#     new = Blockchain.from_json(result.json())
#     og = blockchain
#     print('#*50')
#     print(f'len {len(og.chain)}, {len(new.chain)}')
#     for i in range(0,len(og.chain)):
#         print(f'Block#{i}')
#         print('og\n',og.chain[i].data)
#         print('new\n',new.chain[i].data)
#     print('#*50')
    




ROOT_PORT = 7000
PORT = ROOT_PORT
if __name__ == '__main__':
    print('PEER',os.environ.get('PEER'))

    if os.environ.get('PEER') == 'T':
        PORT = random.randint(5001,6000)
        # Syncing Blockchain
        # result = requests.get(f'http://localhost:{ROOT_PORT}/blockchain')
        # result_blockchain = Blockchain.from_json(result.json())
        # print('received cahin\n',result_blockchain)
        # try:
        #     blockchain.replace_chain(result_blockchain.chain)
        #     print('\n Blockchain updated successfully')
        # except Exception as e:
        #     print(f'\nError in syncing chain: {e}')

        # Syncing pending_transaction
        # result = requests.get(f'http://localhost:{ROOT_PORT}/blockchain/pending_transaction')
        # result_pending_chain = Blockchain.pending_from_json(result.json())
        # try:
        #     blockchain.pending_transaction = result_pending_chain.copy()
        # except Exception as e:
        #     print(f'\nError in syncing pending transaction: {e}')
    app.run(port=PORT)