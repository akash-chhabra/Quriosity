import time
from cal_hash import cal_hash

NANOS = 1
MICROS = 1000 * NANOS
MILLIS = 1000 * MICROS
SEC = 1000 * MILLIS

MINE_RATE = 4 * SEC


GENESIS_DATA = {
    'timestamp': 1,
    'last_hash': 'genesis_last_hash', 
    'hash': 'genesis_hash', 
    'data':[],
    'difficulty': 10, 
    'nonce':'genesis_nonce'
}

def hex_to_bin(hex_data):
    return bin(int(hex_data,16))[2:].zfill(256)

class Block:
    def __init__(self, timestamp, last_hash, hash, data, difficulty, nonce):
        self.timestamp = timestamp
        self.last_hash = last_hash
        self.hash = hash
        self.data = data
        # print('%'*10,type(data),len(data))
        # if len(data) > 0:
        #     print('@'*10,type(data[0]))
        # self.data.extend(data)
        self.difficulty = difficulty
        self.nonce = nonce

        
    def __repr__(self):
        return (
            'Block('
            f'timestamp: {self.timestamp}, '
            f'last_hash: {self.last_hash}, '
            f'hash: {self.hash}, '
            f'data: {self.data}, '
            f'difficulty: {self.difficulty}, '
            f'nonce: {self.nonce})\n'
        )

    def __eq__(self,other):
        return self.__dict__ == other.__dict__

    def to_json(self):
        return self.__dict__

    @staticmethod
    def from_json(json_block):
        print('\njson received',json_block)
        print('\n block created',Block(**json_block))
        return Block(**json_block)
    
    @staticmethod
    def mine_block(last_block,data):
        timestamp = time.time_ns()
        last_hash = last_block.hash
        difficulty = Block.update_difficulty(last_block,timestamp)
        nonce = 0
        hash = cal_hash(timestamp,last_hash,data, difficulty, nonce)
        while hex_to_bin(hash)[0:difficulty] != '0'*difficulty:
            nonce += 1
            timestamp = time.time_ns()
            difficulty = Block.update_difficulty(last_block,timestamp)
            hash = cal_hash(timestamp,last_hash,data, difficulty, nonce)
            
        return Block(timestamp, last_hash, hash, data, difficulty, nonce)

    @staticmethod
    def genesis():
        return Block(**GENESIS_DATA)

    @staticmethod
    def update_difficulty(last_block,new_timestamp):
        if (new_timestamp - last_block.timestamp) < MINE_RATE:
            return last_block.difficulty + 1
        if last_block.difficulty > 1:
            return last_block.difficulty - 1
        return 1

    @staticmethod
    def is_valid_block(last_block,block):
        if block.last_hash != last_block.hash:
            raise Exception('Last block hash is not correct')
        if hex_to_bin(block.hash)[0:block.difficulty] != '0'*block.difficulty:
            raise Exception('Proof of work not correct')
        if abs(last_block.difficulty - block.difficulty) > 1:
            raise Exception('Difficulty must be adjusted by 1')
        
        recalculated_hash = cal_hash(
            block.timestamp,
            block.last_hash,
            block.data,
            block.difficulty,
            block.nonce
        )
        if recalculated_hash != block.hash:
            print(f'\nhash not match {recalculated_hash} != {block.hash}')
            print(block)
            raise Exception('The block hash is not correct')

def main():
    gb = Block.genesis()
    b1 = Block.mine_block(gb,'a')
    print(gb,b1)
    b1.hash = '4551a5718dc320a16c607bc4088d75fe92abf617f3825813a140c37139173adc'
    Block.is_valid_block(gb,b1)

if __name__ == "__main__":
    main()