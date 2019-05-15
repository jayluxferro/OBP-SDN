import sys

sys.path.append('../SDN_Python')

import helper as f


if __name__ == "__main__":
    print(f.createQ('eth0', 1000, 20000))

