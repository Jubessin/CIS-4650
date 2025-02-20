CUP=cup
JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main

SRC=src
SCANNER_SRC=$(SRC)/scanner
LEXER_SRC=$(SCANNER_SRC)/*.flex
LEXER_OUT=$(SCANNER_SRC)/Lexer.java

all: $(LEXER_OUT)

$(LEXER_OUT): $(LEXER_SRC)
	$(JFLEX) $(LEXER_SRC)

Lexer.java: $(LEXER_OUT)

clean:
	rm -f $(LEXER_OUT) *.class *~
