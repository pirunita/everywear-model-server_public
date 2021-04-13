import queue
import socket
import time
import threading

wait_count = 0
wait_queue = queue.Queue()
complete = False


def get_flask():
    print("Start GET FLASK")
    s = socket.socket()
    host = socket.gethostname()
    port_number = 12222
    #s.setsockopt(socket.SOL_SOCKET, socket.50_REUSEADDR, 1)
    s.bind((host, port_number))
    s.setblocking(1)
    s.listen(5)
    c = None
    a = 1

    while True:

        print('[Waiting for connection..]')
        c, addr = s.accept()
        print('Got connection from', addr)

        print('[Waiting for response...]')
        wait_str = (c.recv(1024)).decode('utf-8')
        print(wait_str)
        print(len(wait_str))
        wait_list = wait_str.split()
        # Halts
        #time.sleep(3)


        if len(wait_list) < 2:
            print('continue')
            c = None
            a = 1
            continue

        global wait_count, complete

        wait_queue.put(wait_list)
        wait_count = wait_count + 1

        #save_c = copy(c)

        while complete != True:
            continue

        c.send("test".encode('utf-8'))

        c = None
        complete = False
        #c, addr = s.accept()


threading._start_new_thread(get_flask,())
while True:
    while wait_queue.qsize() != 0:
        time.sleep(5)
        print("RUN")
        queue_data = wait_queue.get()
        wait_count = wait_count - 1
        complete = True

