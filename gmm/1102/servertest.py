import queue
import socket

wait_count = 0
wait_queue = queue.Queue()


def get_flask():
    print("Start GET FLASK")
    s = socket.socket()
    host = socket.gethostname()
    port_number = 12222
    # s.setsockopt(socket.SOL_SOCKET, socket.50_REUSEADDR, 1)
    s.bind((host, port_number))
    s.setblocking(1)
    s.listen(5)
    c = None
    a = 1

    while True:
        print("#")
        print(a)
        print(c)
        if c is None or a == 1:
            # Halts
            print('[Waiting for connection..]')
            c, addr = s.accept()
            print('Got connection from', addr)
            a = 0
        else:
            # Halts
            # time.sleep(3)
            print('[Waiting for response...]')
            wait_str = (c.recv(1024)).decode('utf-8')
            print(wait_str)
            print(len(wait_str))

            wait_list = wait_str.split()
            if len(wait_list) < 2:
                print('continue')
                c = None
                a = 1
                continue

            global wait_count

            wait_queue.put(wait_list)
            wait_count = wait_count + 1

            if wait_str == '0':
                print("shutdown")
                return 1
            c = None
            a = 1
