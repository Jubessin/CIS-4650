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
    return newSymbol(Symbol.IF); 
}

"int" { 
    return newSymbol(Symbol.INT); 
}

"bool" { 
    return newSymbol(Symbol.BOOL); 
}

"void" { 
    return newSymbol(Symbol.VOID); 
}

"else" { 
    return newSymbol(Symbol.ELSE); 
}

"while" { 
    return newSymbol(Symbol.WHILE); 
}

"return" { 
    return newSymbol(Symbol.RETURN); 
}

"||" {
    return newSymbol(Symbol.OR);
}

"&&" {
    return newSymbol(Symbol.AND);
}

"==" { 
    return newSymbol(Symbol.EQUAL); 
}

"!=" {
    return newSymbol(Symbol.NOT_EQUAL);
}

"<=" {
    return newSymbol(Symbol.LESS_THAN_OR_EQUAL);
}

">=" {
    return newSymbol(Symbol.GREATER_THAN_OR_EQUAL);
}

"," {
    return newSymbol(Symbol.COMMA);
}
"~" {
    return newSymbol(Symbol.UNARY);
}

"=" { 
    return newSymbol(Symbol.ASSIGN); 
}

"<" { 
    return newSymbol(Symbol.LESS_THAN); 
}

">" { 
    return newSymbol(Symbol.GREATER_THAN); 
}

"+" { 
    return newSymbol(Symbol.PLUS); 
}

"-" { 
    return newSymbol(Symbol.MINUS); 
}

"*" { 
    return newSymbol(Symbol.MULTIPLY); 
}

"/" { 
    return newSymbol(Symbol.DIVIDE); 
}

"(" { 
    return newSymbol(Symbol.LEFT_PARENTHESES); 
}

")" { 
    return newSymbol(Symbol.RIGHT_PARENTHESES); 
}

"{" {
    return newSymbol(Symbol.LEFT_ANGLE_BRACKET);
}

"}" {
    return newSymbol(Symbol.RIGHT_ANGLE_BRACKET);
}

"[" {
    return newSymbol(Symbol.LEFT_SQUARE_BRACKET);
}

"]" {
    return newSymbol(Symbol.RIGHT_SQUARE_BRACKET);
}

";" { 
    return newSymbol(sym.SEMI); 
}

{NUMBER} { 
    return newSymbol(Symbol.NUM, yytext()); 
}

{TRUTH} { 
    return newSymbol(Symbol.TRUTH, yytext()); 
}

{ID} { 
    return newSymbol(Symbol.ID, yytext()); 
}

// Skip all whitespace. 
{WHITESPACE}+ {}   

// Skip all comments.
{COMMENT} {}

// Matches all unmatched input.
. { 
    return newSymbol(Symbol.ERROR); 
}
