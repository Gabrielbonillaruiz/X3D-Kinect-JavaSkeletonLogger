X3D-Kinect-JavaSkeletonLogger
=============================

Simple motion tracking in a CSV-file via InstantReality, Kinect, OpenNI and Java

Data is saved in user.home as kinectLog.csv

Needs instantreality.jar to build.
Data is extracted from a running NI-Node in InstantReality.
(http://www.instantreality.org/)

Security issue:
The Class-file has to get extended execution rights or it won't save anything. Add those with the policytool in java\bin.
Detailed instructions offered on the InstantReality homepage:
http://doc.instantreality.org/tutorial/breaking-out-of-the-java-sandbox/