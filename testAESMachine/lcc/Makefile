# Wedit Makefile for project testAESMachine
SRCDIR="c:\users\david dias\desktop\testaesmachine"
CFLAGS=-I"C:\lcc\include"  -g2 
CC=$(LCCROOT)\bin\lcc.exe
LINKER=$(LCCROOT)\bin\lcclnk.exe
OBJS=\
	util.obj \
	protocol.obj \
	main.obj \
	ethcom.obj \
	counter.obj \
	com.obj

LIBS=
EXE="testaesmachine.exe"

$(EXE):	$(OBJS) Makefile
	$(LINKER)  -subsystem console -o $(SRCDIR)\"lcc\testaesmachine.exe" $(OBJS) $(LIBS)

# Build util.c
UTIL_C=\
	$(SRCDIR)\util.h\
	c:\lcc\include\_syslist.h\
	$(SRCDIR)\ethcom.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\stdarg.h\
	c:\lcc\include\ctype.h\
	c:\lcc\include\basetsd.h\

util.obj: $(UTIL_C) $(SRCDIR)\"util.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"util.c"

# Build protocol.c
PROTOCOL_C=\
	$(SRCDIR)\protocol.h\
	c:\lcc\include\safelib.h\
	$(SRCDIR)\com.h\
	c:\lcc\include\_syslist.h\

protocol.obj: $(PROTOCOL_C) $(SRCDIR)\"protocol.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"protocol.c"

# Build main.c
MAIN_C=\
	$(SRCDIR)\util.h\
	c:\lcc\include\_syslist.h\
	$(SRCDIR)\ethcom.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\stdarg.h\
	c:\lcc\include\ctype.h\
	c:\lcc\include\basetsd.h\
	$(SRCDIR)\counter.h\
	$(SRCDIR)\com.h\
	$(SRCDIR)\protocol.h\

main.obj: $(MAIN_C) $(SRCDIR)\"main.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"main.c"

# Build ethcom.c
ETHCOM_C=\
	$(SRCDIR)\util.h\
	c:\lcc\include\_syslist.h\
	$(SRCDIR)\ethcom.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\stdarg.h\
	c:\lcc\include\ctype.h\
	c:\lcc\include\basetsd.h\

ethcom.obj: $(ETHCOM_C) $(SRCDIR)\"ethcom.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"ethcom.c"

# Build counter.c
COUNTER_C=\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\string.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\win.h\
	c:\lcc\include\limits.h\
	c:\lcc\include\stdarg.h\
	c:\lcc\include\ctype.h\
	c:\lcc\include\basetsd.h\

counter.obj: $(COUNTER_C) $(SRCDIR)\"counter.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"counter.c"

# Build com.c
COM_C=\
	$(SRCDIR)\com.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\safelib.h\
	c:\lcc\include\_syslist.h\
	c:\lcc\include\stdarg.h\
	c:\lcc\include\ctype.h\
	c:\lcc\include\basetsd.h\

com.obj: $(COM_C) $(SRCDIR)\"com.c"
	$(CC) -c $(CFLAGS) $(SRCDIR)\"com.c"

link:
	$(LINKER)  -subsystem console -o $(SRCDIR)\"lcc\testaesmachine.exe" $(OBJS) $(LIBS)

clean:
	del $(OBJS) testaesmachine.exe"
