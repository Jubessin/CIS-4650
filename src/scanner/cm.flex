/**
* File Name: cm.flex
* JFlex specification for C- files.
*/
   
import java_cup.runtime.*;
      
%%
   
%class Lexer
%line
%column
%cup            // Use CUP compatibility mode.

%eofval{
  return null;
%eofval};
   
%{   
    private Symbol newSymbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol newSymbol(int type, Object value) {
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

// This may need to be changed to avoid nested comments
COMMENT = "/\*.*\*/"

TRUTH = "true"|"false"

// LETTER is any alphabet character, of any case.
LETTER = [a-zA-Z]

ID = ("_" | {LETTER})("_" | {LETTER} | {DIGIT})*
   
%%
   
"if" {
    return newSymbol(sym.IF); 
}

"int" { 
    return newSymbol(sym.INT); 
}

"bool" { 
    return newSymbol(sym.BOOL); 
}

"void" { 
    return newSymbol(sym.VOID); 
}

"else" { 
    return newSymbol(sym.ELSE); 
}

"while" { 
    return newSymbol(sym.WHILE); 
}

"return" { 
    return newSymbol(sym.RETURN); 
}

"||" {
    return newSymbol(sym.OR);
}

"&&" {
    return newSymbol(sym.AND);
}

"==" { 
    return newSymbol(sym.EQUAL); 
}

"!=" {
    return newSymbol(sym.NOT_EQUAL);
}

"<=" {
    return newSymbol(sym.LESS_THAN_OR_EQUAL);
}

">=" {
    return newSymbol(sym.GREATER_THAN_OR_EQUAL);
}

"," {
    return newSymbol(sym.COMMA);
}
"~" {
    return newSymbol(sym.UNARY);
}

"=" { 
    return newSymbol(sym.ASSIGN); 
}

"<" { 
    return newSymbol(sym.LESS_THAN); 
}

">" { 
    return newSymbol(sym.GREATER_THAN); 
}

"+" { 
    return newSymbol(sym.PLUS); 
}

"-" { 
    return newSymbol(sym.MINUS); 
}

"*" { 
    return newSymbol(sym.MULTIPLY); 
}

"/" { 
    return newSymbol(sym.DIVIDE); 
}

"(" { 
    return newSymbol(sym.LEFT_PARENTHESES); 
}

")" { 
    return newSymbol(sym.RIGHT_PARENTHESES); 
}

"{" {
    return newSymbol(sym.LEFT_ANGLE_BRACKET);
}

"}" {
    return newSymbol(sym.RIGHT_ANGLE_BRACKET);
}

"[" {
    return newSymbol(sym.LEFT_SQUARE_BRACKET);
}

"]" {
    return newSymbol(sym.RIGHT_SQUARE_BRACKET);
}

";" { 
    return newSymbol(sym.SEMICOLON); 
}

{NUMBER} { 
    return newSymbol(sym.NUM, yytext()); 
}

{TRUTH} { 
    return newSymbol(sym.TRUTH, yytext()); 
}

{ID} { 
    return newSymbol(sym.ID, yytext()); 
}

// Skip all whitespace. 
{WHITESPACE}+ {}   

// Skip all comments.
{COMMENT} {}

// Matches all unmatched input.
. { 
    return newSymbol(sym.ERROR); 
}
