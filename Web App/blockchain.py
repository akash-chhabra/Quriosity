from block import Block
from transaction import Transaction
from cal_balance import cal_balance

class Blockchain:
    def __init__(self):
        self.chain = [Block.genesis()]
        self.pending_transaction = []

    def addBlock(self,data):
        self.chain.append(Block.mine_block(self.chain[-1],data))

    def __repr__(self):
        return f'Blockchain: {self.chain}'


    def replace_chain(self, chain):
        if len(chain) <= len(self.chain):
            raise Exception('Cannot replace. The incoming chain must be longer.')
        try:
            Blockchain.is_valid_chain(chain)
        except Exception as e:
            raise Exception(f'Cannot replace. Invalid chain: {e}')
        self.chain = chain

    def to_json(self):
        serialized_chain = []
        for block in self.chain:
            serialized_chain.append(block.to_json())
        return serialized_chain

    def pending_to_json(self):
        serialized_chain = []
        for transaction in self.pending_transaction:
            serialized_chain.append(transaction.to_json())
        return serialized_chain

    @staticmethod
    def from_json(json_chain):
        blockchain = Blockchain()
        blockchain.chain = list(map(lambda json_block: Block.from_json(json_block), json_chain))
        return blockchain


    @staticmethod
    def pending_from_json(json_chain):
        blockchain = Blockchain()
        blockchain.pending_transaction = list(map(lambda json_transaction: Transaction.from_json(json_transaction), json_chain))
        return blockchain.pending_transaction

    @staticmethod
    def is_valid_chain(chain):
        if chain[0] != Block.genesis():
            raise Exception('The genesis block is not valid')
        for i in range(1,len(chain)):
            block = chain[i]
            last_block = chain[i-1]
            Block.is_valid_block(last_block,block)

def main():
    
    b = Blockchain()
    # for i in range(3):
    #     t = Transaction(f'from{i}',f'to{i}',i)
    #     b.addBlock(t.to_json())
    # print(b.chain)

    # print('\nbalance ',cal_balance('to1',b))
    # for i in range(3):
    #     b.addBlock(i)
    # print(b)
    # b1 = Blockchain()
    # for i in range(4):
    #     b1.addBlock(i)
    # print(b1)

    # b.replace_chain(b1.chain)

if __name__ == "__main__":
    main()