def cal_balance(blockchain,user):
    print('cal start')
    balance = 0
    chain = blockchain.chain
    print('in cal_bal l1 start')
    for i in range(1,len(chain)):
        block = blockchain.chain[i]
        for transaction in block.data:
            if type(transaction) != dict:
                transaction = transaction.to_json()
            # print('got match', balance)
            # print(f'type {type(transaction)}')
            # print(f'trans {transaction}')
            sender = transaction.get('sender')
            receiver = transaction.get('receiver')
            amt = transaction.get('amt')
            if sender == user:
                balance -= amt
            elif receiver == user:
                balance += amt
    print('in cal_bal l1 done')


    if len(blockchain.pending_transaction)>=1:
        print('in cal_bal l2 start')
        for transaction in blockchain.pending_transaction:
            if type(transaction) != dict:
                transaction = transaction.to_json()
            # print(f'type {type(transaction)}')
            # print(f'trans {transaction}')
            sender = transaction.get('sender')
            receiver = transaction.get('receiver')
            amt = transaction.get('amt')
            if sender == user:
                balance -= amt
            elif receiver == user:
                balance += amt
        print('in cal_bal l2 done')
        
    return balance 


def main():
    print(blockchain.chain)
if __name__ == "__main__":
    main()
