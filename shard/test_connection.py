from lnx2 import Packet, Connection, Channel, RTM_AUTO_ORDERED, ClientSocket, NothingToRead, RTM_MANUAL, RTM_AUTO
import sys, select, socket, hashlib, time

# Prepare channel and connection
c_0 = Channel(0, RTM_AUTO_ORDERED, 10, 60)
c_2 = Channel(2, RTM_MANUAL, 5, 1) 
c_3 = Channel(3, RTM_AUTO, 10, 60)

conn = Connection([c_0, c_2, c_3], 15)

def wait_until_clear(chanid):
    """Waits until there is no tx activity on channel chanid"""
    while conn[chanid].is_tx_in_progress():
        rs, ws, xs = select.select([sock.socket], [sock.socket], [], 5)
        if len(ws) == 1:
            sock.on_sendable()
        if len(rs) == 1:
            sock.on_readable()

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
		
# Send input periodically
while True:
	try:
		msg = conn[3].read()
	except NothingToRead:
		pass
	else:
		k = 'Channel 3 message: '
		if msg[0] == 0:
			k += 'player %s connected' % ((msg[1] << 8) + msg[2], )
		elif msg[0] == 1:
			k += 'player %s disconnected' % ((msg[1] << 8) + msg[2], )
		else:
			k += 'and for now something completely different!'
		print k

	conn[2].write('\x00\x10\x00\x08\x01\x0A')
	wait_until_clear(2)
	time.sleep(4)
