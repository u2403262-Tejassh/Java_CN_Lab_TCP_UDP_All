JAVA COMPUTER NETWORKS LAB – TCP AND UDP COLLECTION
====================================================

Contents
--------
1. DVR – Distance Vector Routing
   DVR_TCP / DVR_UDP
2. Sliding Window – Stop and Wait ARQ
   StopAndWait_TCP / StopAndWait_UDP
3. Sliding Window – Go-Back-N ARQ
   GoBackN_TCP / GoBackN_UDP
4. Sliding Window – Selective Repeat ARQ
   SelectiveRepeat_TCP / SelectiveRepeat_UDP
5. FTP
   FTP_TCP / FTP_UDP
6. SMTP
   SMTP_TCP / SMTP_UDP
7. Leaky Bucket
   LeakyBucket_TCP / LeakyBucket_UDP
8. Chatting
   Chat_TCP / Chat_UDP
9. Calculator
   Calculator_TCP / Calculator_UDP

General notes
-------------
- These are simple lab-oriented implementations, intentionally kept easy to read.
- Run the Server/Receiver first, then the Client/Sender, except where a single program is provided.
- localhost is used, so both sides can run on the same computer in separate Command Prompt windows.
- UDP versions intentionally simulate packet/ACK loss where appropriate.
- FTP and SMTP are TCP protocols in real-world networking. Their UDP versions here are educational simulations of FTP/SMTP-style command exchanges over UDP.
- The ARQ implementations follow the Module 2 concepts: Stop-and-Wait uses sequence numbers, timers and retransmission; Go-Back-N uses a sender window, cumulative ACKs, receiver window 1 and retransmission from the oldest outstanding frame; Selective Repeat buffers valid out-of-order frames and retransmits only the missing/oldest unacknowledged frame.

Compile examples
----------------
javac SW_TCP_Server.java SW_TCP_Client.java
java SW_TCP_Server
java SW_TCP_Client

or compile all Java files in a folder:
javac *.java

Ports
-----
DVR TCP       6001     DVR UDP       6002
SW TCP        6101     SW UDP        6102
GBN TCP       6201     GBN UDP       6202
SR TCP        6301     SR UDP        6302
FTP TCP       6401     FTP UDP       6402
SMTP TCP      6501     SMTP UDP      6502
Leaky TCP     6601     Leaky UDP     6602
Chat TCP      6701     Chat UDP      6702
Calc TCP      6801     Calc UDP      6802

FTP folders
-----------
FTP_TCP/server
FTP_UDP/server
A sample.txt is included in both server folders.

