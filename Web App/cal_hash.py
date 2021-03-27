from hashlib import sha256
import json

def toString(data):
    return json.dumps(data)

def cal_hash(*args):
    string_args = sorted(map(toString, args))
    string_data = ''.join(string_args)
    return sha256(string_data.encode('utf-8')).hexdigest()


def main():
    print(f"hash {cal_hash([1,2,3])}")


if __name__ == "__main__":
    main()