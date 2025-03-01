/**
* File Name: cm.flex
* JFlex specification for C- files.
*/
   
import java_cup.runtime.*;
      
%%
   
%class Lexer

%eofval{
  return null;
%eofval};

%line
%column
%cup            // Use CUP compatibility mode.
   
%{   
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   
// LINE_TERMINATOR is a \r (carriage return), \n (line feed), or \r\n. */
LINE_TERMINATOR = \r|\n|\r\n
   
/* WHITESPACE is a line terminator, space, tab, or form feed. */
WHITESPACE = {LINE_TERMINATOR} | [ \t\f]
   
// DIGIT is any numeric ascii character.
DIGIT = [0-9]

// NUMBER is all integers.
NUMBER = {DIGIT}+

COMMENT = "/*" [^*] ~"*/" | "/*" "*"+ "/"

TRUTH = "true"|"false"

// LETTER is any alphabet character, of any case.
LETTER = [a-zA-Z]

ID = ("_" | {LETTER})("_" | {LETTER} | {DIGIT})*
   
%%
   
"if" {
    return symbol(sym.IF); 
}

"int" { 
    return symbol(sym.INT); 
}

"bool" { 
    return symbol(sym.BOOL); 
}

"void" { 
    return symbol(sym.VOID); 
}

"else" { 
    return symbol(sym.ELSE); 
}

"while" { 
    return symbol(sym.WHILE); 
}

"return" { 
    return symbol(sym.RETURN); 
}

"||" {
    return symbol(sym.OR);
}

"&&" {
    return symbol(sym.AND);
}

"==" { 
    return symbol(sym.EQ); 
}

"!=" {
    return symbol(sym.NE);
}

"<=" {
    return symbol(sym.LE);
}

">=" {
    return symbol(sym.GE);
}

"," {
    return symbol(sym.COMMA);
}
"~" {
    return symbol(sym.UNARY);
}

"=" { 
    return symbol(sym.ASSIGN); 
}

"<" { 
    return symbol(sym.LT); 
}

">" { 
    return symbol(sym.GT); 
}

"+" { 
    return symbol(sym.PLUS); 
}

"-" { 
    return symbol(sym.MINUS); 
}

"*" { 
    return symbol(sym.MULTIPLY); 
}

"/" { 
    return symbol(sym.DIVIDE); 
}

"(" { 
    return symbol(sym.LEFT_PARENTHESES); 
}

")" { 
    return symbol(sym.RIGHT_PARENTHESES); 
}

"{" {
    return symbol(sym.LEFT_CURLY_BRACKET);
}

"}" {
    return symbol(sym.RIGHT_CURLY_BRACKET);
}

"[" {
    return symbol(sym.LEFT_SQUARE_BRACKET);
}

"]" {
    return symbol(sym.RIGHT_SQUARE_BRACKET);
}

";" { 
    return symbol(sym.SEMI); 
}

{NUMBER} { 
    return symbol(sym.NUM, yytext()); 
}

{TRUTH} { 
    return symbol(sym.TRUTH, yytext()); 
}

{ID} { 
    return symbol(sym.ID, yytext()); 
}

// Skip all whitespace. 
{WHITESPACE}+ {}   

// Skip all comments.
{COMMENT} {}

// Matches all unmatched input.
. { 
    System.out.println("ERROR");
}
