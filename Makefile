CUP=cup
JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main

# TODO: cleanup

SRC=src

MAIN_SRC=$(SRC)/Main.java
MAIN_OUT=$(SRC)/Main.class

PARSER_DIR=$(SRC)/parser
PARSER_SRC=$(PARSER_DIR)/*.cup
PARSER_OUT=$(PARSER_DIR)/Parser
SYMBOL_OUT=$(SCANNER_DIR)/Symbol

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

$(SCANNER_OUT): $(SCANNER_SRC) #$(PARSER_OUT)
	$(JFLEX) $(SCANNER_SRC)
	#$(JAVAC) $(SCANNER_OUT)

$(PARSER_OUT): $(PARSER_SRC)
	$(CUP) -expect 3 -parser Parser -symbols Symbol $(PARSER_SRC)
	mv Parser.java -t $(PARSER_DIR)
	mv Symbol.java -t $(SCANNER_DIR)

$(ABSYN_OUT): $(ABSYN_SRC)
	$(JAVAC) $(ABSYN_SRC)

clean:
	rm -f $(SCANNER_OUT)* $(MAIN_OUT) $(PARSER_OUT).java $(SYMBOL_OUT).java $(ABSYN_OUT) $(SRC)**/*.class *~
