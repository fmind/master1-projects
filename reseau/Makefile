INTERFACE=lo

info:
	/sbin/ifconfig

tftp: info
	sudo /usr/sbin/tcpdump -i $(INTERFACE) -nn -X udp port 1069

pop3: info
	sudo /usr/sbin/tcpdump -i $(INTERFACE) -nn -X tcp port 1100

clean:
	rm get/*
