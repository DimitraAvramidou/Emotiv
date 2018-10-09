IOANNA LYKOUDI 1115201400091
DIMITRA AVRAMIDOU 1115201400001

The implementation consists of a Publisher, a Subscriber & MQTT Broker.

The message is transmitted from Publisher to Subscriber via MQTT Broker
in conjunction with Paho.

Publisher: Developed and implemented in Eclipse. The user inputs the 
trasmitted message. It is important to mention that in order to finally
achieve the correct transmission of the message, it is necessary to define
the same topic simultaneously in Publisher & Subscriber.

Subscriber: Developed and implemented on Android Studio. To make the connection
and eventually the mobile device receives the message, there must be an interface
between the computer and the mobile. Thus, the publisher connects to localhost and
the subscriber to the computer's ip. All of the above work on condition that they 
are connected at the same port (1883). When the message finally arrives at the mobile
device, the user is alerted by sound and visual notification, ie by activating the
flash and playing notification sound of our choice.


_______________________________________________________________________________________


Η υλοποίηση αποτελείται από έναν Publisher, έναν Subscriber & MQTT Broker.

Το μέσο που πραγματοποιεί την μετάδοση του μηνύματος από Publisher σε Subscriber
είναι ο MQTT Broker σε συνδυασμό με τo Paho. 

Publisher: Έχει υλοποιηθεί και αναπτυχθεί σε Eclipse. Το μήνυμα που μεταδίδεται
απλά γράφεται ως όρισμα από τον χρήστη. Σημαντικό είναι να αναφερθεί, ότι 
για να επιτευχθεί τελικά η σωστή μετάδοση του μηνύματος απαραίτητο είναι να 
ορίσουμε το ίδιο topic , ταυτόχρονα σε Publisher & Subscriber.  

Subscriber: Έχει υλοποιηθεί και αναπτυχθεί σε Android Studio. Για να γίνει η 
σύνδεση και τελικά να παραλάβει η κινητή συσκευή το μήνυμα,
πρέπει να υπάρχει διασύνδεση μεταξύ του υπολογιστή και του κινητού.
Έτσι, ο publisher συνδέεται με localhost και ο subscriber με την ip του 
υπολογιστή.
Όλα τα παραπάνω λειτουργούν με την προϋπόθεση ότι γίνονται στο ίδιο port (1883).
Όταν τελικά το μήνυμα φτάσει στην κινητή συσκευή τότε ο χρήστης ειδοποιείται
με ηχητική και οπτική ειδοποίση, δηλαδή με ενεργοποίηση του flash και 
αναπαραγωγής ήχου της επιλογής μας.
