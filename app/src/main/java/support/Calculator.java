package support;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

public class Calculator {

	private Stack theStack;

	private String infixExpr;

	private String postfixExpr;

	List<SymbolTable> symbolTable;

	String tempString = "";

	public static final String ERROR_STRING = "Syntax Error!";

	public Calculator(String in) {

		infixExpr = in;

		postfixExpr = "";

		int stackSize = infixExpr.length();

		theStack = new Stack(stackSize);

		symbolTable = new ArrayList<SymbolTable>();

	}

	public void infixToPostfix() {

		char ch;
		String excl;

		for (int j = 0; j < infixExpr.length(); j++) {

			ch = infixExpr.charAt(j);

			switch (ch) {

			case '+':
			case '-':
				excl = "E×^√+-(÷%";
				if (j > 0 && excl.contains(String.valueOf(infixExpr
								.charAt(j - 1))))
					tempString = tempString + ch;
				else
					gotOper(ch, 1);
				break;

			case '×':
			case '÷':
				excl = "E";
				if (ch == '×' && 
						(j < infixExpr.length() - 1 	&&
								excl.contains(String.valueOf(infixExpr.charAt(j + 1))))){
					
				}
				else gotOper(ch, 2);
				break;

			case '√':
				excl = "×(^√+-÷E";
				if (j > 0 && !excl.contains(String.valueOf(infixExpr.charAt(j - 1)))){
					
					gotOper('×', 2);
					System.out.println(infixExpr.charAt(j-1) + infixExpr.charAt(j));
					
				}
					
			case '^':
				gotOper(ch, 3);
				break;

			case '%':
				gotOper(ch, 4);
				excl = "E×^√+-()÷%";
				if (j < infixExpr.length() - 1
						&& !excl.contains(String.valueOf(infixExpr
								.charAt(j + 1))))
					gotOper('×', 2);
				break;

			case 'E':
//				if (j > 0 && infixExpr.charAt(j - 1) != '×'
//						&& infixExpr.charAt(j - 1) != '(')
//					gotOper('×', 2);
				gotOper(ch, 5);
				break;

			case '(':
				if ((tempString != null && !tempString.equals(""))
						|| (j > 0 && (infixExpr.charAt(j - 1) == '%' || infixExpr
								.charAt(j - 1) == ')')))
					gotOper('×', 2);
				theStack.push(ch);
				break;

			case ')':
				gotParen(ch);
				excl = "E×^√+-()÷%";
				if (j < infixExpr.length() - 1
						&& !excl.contains(String.valueOf(infixExpr
								.charAt(j + 1))))
					gotOper('×', 2);
				break;
			// CASE '√':
			// CASE '^':
			// GOTOPER(CH, 3);
			default:

				tempString = tempString + ch;
				// System.out.println(output);
				break;
			}

			printLog();
		}

		if (tempString != "") {

			addToSymbolTable(tempString, SymbolTable.SYMBOL_OPERAND, null);

			tempString = "";
		}
		while (!theStack.isEmpty()) {

			char opTop = (char) theStack.pop();

			System.out.println("poped from stack " + opTop);

			addOperator(String.valueOf(opTop));

			printLog();

		}

		for (SymbolTable symbol : symbolTable) {
			System.out.println("value of " + symbol.getSymbol() + " is "
					+ symbol.getValue());
		}

	}

	private void printLog() {

		System.out.println("out put :" + postfixExpr);

		System.out.print("Stack contents : ");

		theStack.printContents();

	}

	public void gotOper(char opThis, int prec1) {

		pushTemp();
		
		System.out.println("gotOper("+opThis+", "+prec1+")");

		while (!theStack.isEmpty()) {

			char opTop = (char) theStack.pop();

			if (opTop == '(') {

				theStack.push(opTop);

				break;

			} else {
				
				String rightPrecedenceOperators = "√^";

				int prec2 = 1;

				if (opTop == '+' || opTop == '-')
					prec2 = 1;

				else if (opTop == '×' || opTop == '÷')
					prec2 = 2;

				else if (rightPrecedenceOperators.contains(String.valueOf(opTop)))
					prec2 = 3;

				else if (opTop == '%')
					prec2 = 4;

				else if (opTop == 'E')
					prec2 = 5;

				if (prec2 < prec1) {

					theStack.push(opTop);

					break;

				}
				
				else if (prec2 == prec1 && rightPrecedenceOperators.contains(String.valueOf(opTop)) && rightPrecedenceOperators.contains(String.valueOf(opThis))){
					theStack.push(opTop);
					System.out.println("opTop : " + opTop + " and opThis = " + opThis);
					break;
				}
				
				else {

					addOperator(String.valueOf(opTop));

				}

			}

		}

		theStack.push(opThis);

	}

	public void addOperator(String operator) {

		String percentOp = "+-";

		boolean isPercentBinary = !theStack.isEmpty()
				&& percentOp.contains(String.valueOf((char) theStack.peek()));

		if (new String("√").contains(operator)
				|| (operator.equals("%") && !isPercentBinary)) {

			addToSymbolTable(String.valueOf(operator),
					SymbolTable.SYMBOL_OPERATOR_UNARY, null);
		}

		else if (new String("+-^%×÷E").contains(operator)) {

			addToSymbolTable(String.valueOf(operator),
					SymbolTable.SYMBOL_OPERATOR_BINARY, null);
		}

	}

	public void gotParen(char ch) {

		pushTemp();

		char chx;

		while (!theStack.isEmpty()) {

			chx = (char) theStack.pop();

			if (chx == '(')
				break;

			else
				addOperator(String.valueOf(chx));

		}
	}

	public void pushTemp() {

		if (tempString != null && !tempString.equals("")) {

			addToSymbolTable(tempString, SymbolTable.SYMBOL_OPERAND, null);

			tempString = "";

		}

	}

	public char addToSymbolTable(String symbolValue, int symboltype,
			Bundle additional) {

		SymbolTable symbolElement = new SymbolTable(symbolValue, symboltype,
				additional);

		symbolTable.add(symbolElement);

		postfixExpr = postfixExpr + symbolElement.getSymbol();

		return symbolElement.getSymbol();

	}

	class Stack {
		private int maxSize;
		private int[] stackArray;
		private int top;

		public Stack(int max) {
			maxSize = max;
			stackArray = new int[maxSize];
			top = -1;
		}

		public void printContents() {

			for (int i = 0; i <= top; i++)
				System.out.print((char) stackArray[i] + ", ");

			System.out.println();

		}

		public void push(int j) {

			stackArray[++top] = j;
		}

		public int pop() {
			if (top < 0)
				return -1;
			return stackArray[top--];
		}

		public int peek() {
			if (top < 0)
				return -1;
			return stackArray[top];
		}

		public boolean isEmpty() {
			return (top == -1);
		}
	}

	public String calculate() {

		infixToPostfix();

		System.out.println("post fix expression is : " + postfixExpr);

		int n = postfixExpr.length();

		double result = 0;

		Stack calculatorStack = new Stack(n);

		try {

			for (int i = 0; i < n; i++) {

				char ch = postfixExpr.charAt(i);

				if (SymbolTable.getTypeBySymbol(symbolTable, ch) == SymbolTable.SYMBOL_OPERAND) {

					calculatorStack.push((int) (ch));

					System.out.println("to push : " + (ch));

				}

				else if (SymbolTable.getTypeBySymbol(symbolTable, ch) == SymbolTable.SYMBOL_OPERATOR_BINARY) {

					char xchar = (char) calculatorStack.pop();

					char ychar = (char) calculatorStack.pop();

					double x = Double.parseDouble(SymbolTable.getValueBySymbol(
							symbolTable, xchar));

					double y = Double.parseDouble(SymbolTable.getValueBySymbol(
							symbolTable, ychar));

					System.out.println("x : " + x + " y : " + y + " symbol : "
							+ SymbolTable.getValueBySymbol(symbolTable, ch));

					switch (SymbolTable.getValueBySymbol(symbolTable, ch)) {

					case "+":
						result = x + y;
						break;

					case "-":
						result = y - x;
						break;

					case "×":
						result = x * y;
						break;

					case "÷":
						result = y / x;
						break;

					case "^":
						result = Math.pow(y, x);
						break;

					case "%":
						result = x / 100 * y;
						calculatorStack.push(ychar);
						break;
						
					case "E":
						result = Math.pow(10, x) * y;
						break;

					// case"%":
					// r=x/100;

					// case "^2":
					// r = x^2;
					// case "√":
					// r = √x;
					default:
						result = 0;
					}

					System.out.println("r : " + result);

					calculatorStack.push((int) addToSymbolTable(
							String.valueOf(result), SymbolTable.SYMBOL_OPERAND,
							null));

				}

				else if (SymbolTable.getTypeBySymbol(symbolTable, ch) == SymbolTable.SYMBOL_OPERATOR_UNARY) {

					double x = Double.parseDouble(SymbolTable.getValueBySymbol(
							symbolTable, (char) calculatorStack.pop()));

					System.out.println("x : " + x + " symbol : "
							+ SymbolTable.getValueBySymbol(symbolTable, ch));

					switch (SymbolTable.getValueBySymbol(symbolTable, ch)) {
					

					case "√":
						result = Math.sqrt(x);
						break;

					case "%":
						result = x / 100 * 1;
						break;

					default:
						result = 0;

					}

					System.out.println("r : " + result);

					calculatorStack.push((int) addToSymbolTable(
							String.valueOf(result), SymbolTable.SYMBOL_OPERAND,
							null));
				}
			}
			result = calculatorStack.pop();

			System.out.println("result symbol : " + (char) result);

			System.out.println("value of : " + (char) result + " is "
					+ SymbolTable.getValueBySymbol(symbolTable, (char) result));

			return (SymbolTable.getValueBySymbol(symbolTable, (char) result));

		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return ERROR_STRING;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ERROR_STRING;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return ERROR_STRING;
		}

	}

}