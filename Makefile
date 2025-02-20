CUP=cup
JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main

SRC=src

MAIN_SRC=$(SRC)/Main.java
MAIN_OUT=$(SRC)/Main.class

SCANNER_SRC=$(SRC)/scanner
LEXER_SRC=$(SCANNER_SRC)/*.flex
LEXER_OUT=$(SCANNER_SRC)/Lexer.java

all: $(MAIN_OUT)

# Uncomment once CUP (sym.java) is ready.
# %.class: %.java
# 	$(JAVAC) $(CLASSPATH) $^

Main.java: $(MAIN_OUT)
Lexer.java: $(LEXER_OUT)

$(MAIN_OUT): $(LEXER_OUT) $(MAIN_SRC)

$(LEXER_OUT): $(LEXER_SRC)
	$(JFLEX) $(LEXER_SRC)

clean:
	rm -f $(LEXER_OUT) $(MAIN_OUT) $(SRC)**/*.class *~
