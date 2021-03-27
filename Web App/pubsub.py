import time

from pubnub.pubnub import PubNub
from pubnub.pnconfiguration import PNConfiguration
from pubnub.callbacks import SubscribeCallback

from block import Block

pnconfig = PNConfiguration()
pnconfig.subscribe_key = 'sub-c-8a7e998e-8e6c-11eb-968e-467c259650fa'
pnconfig.publish_key = 'pub-c-186ab70f-5ad2-418c-a8da-463879b95f3a'



CHANNELS = {
    'TEST':'TEST',
    'BLOCK':'BLOCK'
}


class Listener(SubscribeCallback):
    def __init__(self,blockchain):
        self.blockchain = blockchain
    def message(self,pubnub,message_object):
        print(f'\n -- Channel: {message_object.channel} | message: {message_object.message}')
        
        if message_object.channel == CHANNELS['BLOCK']:
            received_block = Block.from_json(message_object.message)
            potential_chain = self.blockchain.chain.copy()
            potential_chain.append(received_block)
            try:
                self.blockchain.replace_chain(potential_chain)
                print('\n Local chain updated successfully')
            except Exception as e:
                print(f'\n Cannot replace chain: {e}')


class PubSub():
    def __init__(self,blockchain):
        self.pubnub = PubNub(pnconfig)
        self.pubnub.subscribe().channels(CHANNELS.values()).execute()
        self.pubnub.add_listener(Listener(blockchain))
    
    def publish(self,channel,message):
        self.pubnub.publish().channel(channel).message(message).sync()


    def broadcast_block(self,block):
        self.publish(CHANNELS['BLOCK'],block.to_json())


def main():
    pubsub = PubSub()
    time.sleep(1)
    pubsub.publish(CHANNELS['TEST'],'my msg')
if __name__ == '__main__':
    main()