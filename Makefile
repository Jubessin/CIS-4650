CUP=cup
JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main

SRC=src

MAIN_SRC=$(SRC)/Main.java
MAIN_OUT=$(SRC)/Main.class

PARSER_DIR=$(SRC)/parser
PARSER_SRC=$(PARSER_DIR)/*.cup
PARSER_OUT=$(PARSER_DIR)/Parser
SYMBOLS_OUT=$(PARSER_DIR)/Symbols

ABSYN_DIR = $(PARSER_DIR)/absyn
ABSYN_SRC = $(ABSYN_DIR)/*.java
ABSYN_OUT = $(ABSYN_DIR)/*.class

SCANNER_DIR=$(SRC)/scanner
SCANNER_SRC=$(SCANNER_DIR)/*.flex
SCANNER_OUT=$(SCANNER_DIR)/Lexer.java

all: $(MAIN_OUT)

# %.class: %.java
# 	$(JAVAC) $(CLASSPATH) $^

Main.java: $(MAIN_OUT)
Lexer.java: $(SCANNER_OUT)
Parser.java: $(PARSER_OUT)

$(MAIN_OUT): $(SCANNER_OUT) $(MAIN_SRC) $(PARSER_OUT) $(ABSYN_OUT)
	$(JAVAC) $(MAIN_SRC)

$(SCANNER_OUT): $(SCANNER_SRC)
	$(JFLEX) $(SCANNER_SRC)

$(PARSER_OUT): $(PARSER_SRC)
	$(CUP) -expect 3 -parser $(PARSER_OUT) -symbols $(SYMBOLS_OUT) $(PARSER_SRC)

$(ABSYN_OUT): $(ABSYN_SRC)
	$(JAVAC) $(ABSYN_SRC)

clean:
	rm -f $(SCANNER_OUT) $(MAIN_OUT) $(PARSER_OUT).java $(SYMBOLS_OUT).java $(ABSYN_OUT) $(SRC)**/*.class *~
