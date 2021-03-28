import time as t
from cal_hash import cal_hash
from flask import jsonify
from cal_balance import cal_balance

class Transaction:
    def __init__(self,sender = None, receiver = None, amt = None, time = None,hash = None):
        self.time = time or t.time_ns()
        self.sender = sender
        self.receiver = receiver
        self.amt = amt
        self.hash = hash or cal_hash(self.sender,self.receiver,self.amt,self.time)
    
    def __repr__(self):
        return (
            'Transaction('
            f'timestamp: {self.time}, '
            f'sender_address: {self.sender}, '
            f'receiver_address: {self.receiver}, '
            f'Amount: {self.amt})'
        )

    def isValidTransaction(self,blockchain):
        print('in trans isvalid start')
        # if not self.amt:
        #     raise Exception('Amount must be Integer')
        if self.amt <= 0:
            raise Exception('Amount must be greater than 0') 
        # print(f'\n the bal: {cal_balance(blockchain,self.sender)}')
        if(self.hash != cal_hash(self.sender,self.receiver,self.amt,self.time)):
            raise Exception('Transaction Hash is not correct')
        print('1 if done')
        if(self.sender == self.receiver):
            raise Exception('Sender is equal to receiver')
        print('2 if done')
        if(self.sender == "MINER_REWARD"):
            raise Exception('You cannot do this')
        # if not self.signature or len(self.signature) == 0:
            # print("No Signature!")
            # return False;
        print('in trans calling cal')
        if cal_balance(blockchain,self.sender) - self.amt < 0:
            print('\nif',self.sender == 'Current_user')
            raise Exception(f'Insuficient amount: {self.sender} has {cal_balance(blockchain,self.sender)}')

        

    def to_json(self):
        return self.__dict__

    @staticmethod
    def from_json(json_transaction):
        return Transaction(**json_transaction)



def main():
    t= Transaction('aa','bb',50)
    print(t.__dict__)
    json_data = {'time': 1616847697284522300, 'sender': 'aa', 'receiver': 'bb', 'amt': 50, 'hash': 'f2cd84adc33db87cd7ac5d3bed4559b25e5526387e55fe2c8c1f2440f2a82f89'}
    print(Transaction.from_json(json_data))
    
if __name__ == "__main__":
    main()