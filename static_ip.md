on command line 

"route -ne" copy default getaway<br>
"cat /etc/resolv.conf" copy all nameservers

"sudo nano /etc/dhcpcd.conf" 

based to 

interface eth0
static ip_address=10.0.0.100

interface wlan0
static ip_address=10.0.0.99

static routers=10.0.0.1
static domain_name_servers=75.75.75.75 75.75.76.76 2001:558:feed::1 2001:558:feed::2


replace the corent values 

gateway -> routers<br>
nameservers -> domain_name_mainservers


 [source](https://www.youtube.com/watch?v=yd2hwce98Aw)
