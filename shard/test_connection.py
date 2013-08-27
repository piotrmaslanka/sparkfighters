from lnx2 import Packet, Connection, Channel, RTM_AUTO_ORDERED, ClientSocket, NothingToRead
import sys, select, socket, hashlib

# Prepare channel and connection
c_0 = Channel(0, RTM_AUTO_ORDERED, 10, 60)

conn = Connection([c_0], 15)

def packon(chanid):
    """Waits until a packet is received on channel chanid. Returns it"""
    while True:
        rs, ws, xs = select.select([sock.socket], [sock.socket], [], 5)
        if len(ws) == 1:
            sock.on_sendable()
        if len(rs) == 1:
            sock.on_readable()
        if conn.has_new_data:
            conn.has_new_data = False
            try:
                p = conn[chanid].read()
            except NothingToRead:
                pass
            else:
                return p


# Get connection data and set up socket
remote_address = sys.argv[1], int(sys.argv[2])
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock = ClientSocket(sock, remote_address, conn)


# let's try logging in
conn[0].write('test')
print 'Sent login, awaiting challenge...'
challenge = packon(0)

print 'Challenge received, sending back response...'
replica = hashlib.sha1('test'+challenge).hexdigest()
conn[0].write(replica)

connect = packon(0)
if connect == 'OK':
    print 'Response OK, connected!'

packon(0)	# We don't care about participant info right now
conn[0].write('RDY') # we are ready

while True:
	pa = packon(0)
	if pa == '0':
		print 'Waiting for cinematics'
	elif pa == '1':
		print 'Cinematics started'
	elif pa == '2':
		print 'Game started'
		break