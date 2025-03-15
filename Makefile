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
	#$(CUP) -dump -expect 3 cm.cup
	$(CUP) -expect 3 cm.cup

run:
	java $(CLASSPATH) CM -a $(file)

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class tests/*.sym tests/*.abs *~
