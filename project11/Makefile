###############################################################################
#
# Makefile for Java project
#
# Students:
# Yuval Shavit, ID 200659332, yuval.shavit@cs.huji.ac.il
# Eric Gofman, ID 307705111, eric.gofman@cs.huji.ac.il
#
###############################################################################

JAVAC=javac
JAVACFLAGS=

SRCS=*.java
EXEC=JackCompiler

TAR=tar
TARFLAGS=cvf
TARNAME=project11.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

