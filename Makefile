JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp ./modules/java-cup-11b.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main

all: CM.class

CM.class: absyn/*.java parser.java sym.java Lexer.java Scanner.java CM.java

%.class: %.java
	$(JAVAC) $(CLASSPATH) $^

Lexer.java: cm.flex
	$(JFLEX) cm.flex

parser.java: cm.cup
	$(CUP) -expect 3 cm.cup

run:
	make
	java $(CLASSPATH) CM -a -c $(file)

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
	rm -f testSuite/*/*.sym testSuite/*/*.abs testSuite/*/*.tm
	rm -f *.abs *.sym *.tm
