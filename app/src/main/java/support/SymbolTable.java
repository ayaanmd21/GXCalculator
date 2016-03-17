package support;

import java.util.List;

import android.os.Bundle;

public class SymbolTable {

 public static final byte SYMBOL_OPERATOR_BINARY = 2;
 
 public static final byte SYMBOL_OPERATOR_UNARY = 3;

 public static final byte SYMBOL_OPERAND = 1;

 private int type;
 private char symbol;
 private String value;
 

 
 private static int topSymbolInt = 64;
 
 public SymbolTable(String value, int type, Bundle additional){
  
  this.type = type;
  this.value = value;
  this.symbol = (char)++topSymbolInt;

  
 }

 int getType() {
  return type;
 }

 char getSymbol() {
  return symbol;
 }

 String getValue() {
  return value;
 }

 public static int getTypeBySymbol(List<SymbolTable> symbolTable, char symbol) {

  if (symbolTable != null && !symbolTable.isEmpty()) {
   for (SymbolTable currentSymbol : symbolTable) {
    if (currentSymbol.getSymbol() == symbol) {
     return currentSymbol.getType();
    }
   }
  }
  return 0;
 }

 public static String getValueBySymbol(List<SymbolTable> symbolTable, char symbol) {

  if (symbolTable != null && !symbolTable.isEmpty()) {
   for (SymbolTable currentSymbol : symbolTable) {
    if (currentSymbol.getSymbol() == symbol) {
     return currentSymbol.getValue();
    }
   }
  }
  return null;
 }

}