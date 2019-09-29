JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Score.java \
	WordApp.java \
	WordDictionary.java \
	WordPanel.java \
	WordRecord.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

doc:
	javadoc -d doc  *.java

