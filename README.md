aiac-ist
========


##How to Run

Import PROJECT_AIAC to eclipse
In "Run->Run Configurations->Arguments", select the desired configuration

#####<-s/r> [-c] [-z] [-t] <-p sandboxFolder/<path of file to send>>



<-s/r> s-send r-receive

[-c] c-cypher

[-z] z-zip

[-t] t-timestamp

<-p sandboxFolder/<path of file to send>> 

To use AESBOX change the flag in line30 of Main.java "boolean useAESBox = false;" to true
