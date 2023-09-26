import paho.mqtt.client as mqtt
import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from mqtt_init import *


cred = credentials.Certificate("HITGARDEN.json")
firebase_admin.initialize_app(cred)
# Get a Firestore client instance
db = firestore.client()
# Reference the HITGARDEN collection and the document with ID "1"
hitgarden_ref = db.collection("HITGARDEN")



# broker list
brokers=["iot.eclipse.org","broker.hivemq.com",\
         "test.mosquitto.org","192.168.8.167","139.162.222.115"]

broker=brokers[1]


def on_log(client, userdata, level, buf):
        print("log: "+buf)
def on_connect(client, userdata, flags, rc):
    if rc==0:
        print("connected OK")
    else:
        print("Bad connection Returned code=",rc)
def on_disconnect(client, userdata, flags, rc=0):
        print("DisConnected result code "+str(rc))
def on_message(client,userdata,msg):
        topic=msg.topic
        m_decode=str(msg.payload.decode("utf-8","ignore"))
        print("Message Topic: ", topic)
        print("message received: ", m_decode)
        if "Troubleshoot" in m_decode:
            doc_ref = hitgarden_ref.document("1")
            doc_ref.update({"service_status": 1})
        elif "Lights" in m_decode:
            doc_ref = hitgarden_ref.document("2")
            doc_ref.update({"service_status": 1})
        elif "30" in m_decode:
            doc_ref = hitgarden_ref.document("0")
            doc_ref.update({"service_status": 1})
        


client = mqtt.Client("IOT_sub_22032021_YY44554", clean_session=True) # create new client instance

client.on_connect=on_connect  #bind call back function
client.on_disconnect=on_disconnect
client.on_log=on_log
client.on_message=on_message

    
print("Connecting to broker ",broker)
port=1883
client.connect(broker,port)     #connect to broker

sub_topic= 'hitgarden/garden/all'


# Next 2 loops will publishing 40 messages to one topic(house) and 2 subtopics(sensor_0 and sensor_1)
# client.publish("matzi/house/sensor_0","my 'retained' 2 message",0,True)
# for j in range(2):
#         for i in range(20):
#              client.publish("matzi/house/sensor_"+str(j),"my  "+str(i)+" message")
#              print("Sent: 'my  "+str(i)+" message'" + " to: 'matzi/house/sensor_"+str(j)+"'")
#              time.sleep(1)

# client.disconnect() # disconnect

# print("End publish_client run script")

client.loop_start()  #Start loop
### part for your change
client.subscribe(sub_topic,qos=0)
##client.publish("house/sensor1","my first message")
##
time.sleep(300)
client.loop_stop()    #Stop loop 
client.disconnect()