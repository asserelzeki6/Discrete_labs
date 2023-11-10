import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

interface Expression {
    String getRepresentation();
    void setRepresentation(String representation);
}

interface LogicalExpressionSolver {
    boolean evaluateExpression(Expression expression);
}

class LogicalExpression implements Expression {
    private String representation;

    public LogicalExpression(String representation) {
        this.representation = representation;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }

    @Override
    public void setRepresentation(String representation) {
        this.representation = representation;
    }
}

class LogicalExpressionEvaluator implements LogicalExpressionSolver {
    private Map<String, Boolean> variableValues;

    public LogicalExpressionEvaluator() {
        variableValues = new HashMap<>();
    }

    public void setVariableValue(String variable, boolean value) {
        variableValues.put(variable, value);
    }

    public void evaluateVariablesValues(String expression)
    {
        expression = expression.replaceAll(" ", ""); // Remove spaces
        String[] assignments = expression.split(",");

        for (String assignment : assignments) {
            String[] parts = assignment.trim().split("=");
            if (parts.length == 2) {
                String variableName = parts[0].trim();
                String value = parts[1].trim();

                if (value.equalsIgnoreCase("true")) {
                    this.setVariableValue(variableName, true);
                } else if (value.equalsIgnoreCase("false")) {
                    this.setVariableValue(variableName, false);
                } else {
                    System.out.println("Invalid value for variable: " + variableName);
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid assignment format: " + assignment);
                System.exit(0);
            }
        }
    }
    public boolean evaluateExpression(Expression expression) {
        String exp = expression.getRepresentation();
        exp = exp.replaceAll(" ", ""); // Remove spaces
        for (int i = 0; i < exp.length(); i++) {
        if(Character.isAlphabetic(exp.charAt(i)) && Character.isUpperCase(exp.charAt(i))) {
            if (variableValues.containsKey(exp.charAt(i)+"")) {
                if(variableValues.get(exp.charAt(i)+""))
                    exp = exp.replace(exp.charAt(i), 't');
                else
                    exp = exp.replace(exp.charAt(i), 'f');
            } else {
                exp = exp.replace(exp.charAt(i), 'n');
            }
            }
        }
        return evaluateExpressionInternal(exp);
    }

    private boolean evaluateExpressionInternal(String expression) {
        Stack<Character> operatorStack = new Stack<>();
        Stack<Integer> valueStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == 't' || c == 'f' || c=='n') {
                valueStack.push(c == 't'?1:(c=='f'?0:-1));
                //printStacks(operatorStack, valueStack);
            } else if (c == '(') {
                operatorStack.push(c);
                //printStacks(operatorStack, valueStack);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    applyOperator(operatorStack, valueStack);
                    //printStacks(operatorStack, valueStack);
                }
                operatorStack.pop(); // Pop '('
            } else if (c == '~' || c == '^' || c == 'v' || c == '>') {
                while (!operatorStack.isEmpty() && hasHigherPrecedence(c, operatorStack.peek())) {
                    applyOperator(operatorStack, valueStack);
                    //printStacks(operatorStack, valueStack);
                }
                operatorStack.push(c);
                //printStacks(operatorStack, valueStack);
            } else {
                // Invalid character
                System.out.println("Wrong expression");
                System.exit(0);
                return false;
            }

            //printStacks(operatorStack, valueStack);
        }

        while (!operatorStack.isEmpty()) {
            applyOperator(operatorStack, valueStack);
            //printStacks(operatorStack, valueStack);
        }

        if (valueStack.size() == 1) {
            if(valueStack.peek()==-1)
            {
                System.out.println("Insufficient information");
                System.exit(0);
            }
            return (valueStack.pop() == 1);
        } else {
            System.out.println("Wrong expression");
            System.exit(0);
            return false;
        }
    }

    private void applyOperator(Stack<Character> operatorStack, Stack<Integer> valueStack) {
        char operator = operatorStack.pop();
        boolean operand1BoolValue;
        boolean operand2BoolValue;
        if (operator == '~') {
            int operand = valueStack.pop();
            boolean boolValue = (operand == 1);
            if(operand==-1)
            {
              valueStack.push(-1);
            }
            else {
            valueStack.push(boolValue ? 0 : 1);}
        } else {
            int operand2 = valueStack.pop();
            if(valueStack.isEmpty()) {
                System.out.println("Wrong expression");
                System.exit(0);
            }
            int operand1 = valueStack.pop();
            switch (operator) {
                case '^':
                    if((operand1 == -1 && operand2==1) ||(operand2 == -1 && operand1==1) ) {
                        valueStack.push(-1);
                        break;
                    }
                    operand1BoolValue = (operand1 == 1);
                    operand2BoolValue = (operand2 == 1);
                    if(operand1 == -1 && operand2 == -1)
                    { valueStack.push(-1);}
                    else{
                    valueStack.push((operand1BoolValue && operand2BoolValue)?1:0);}
                    break;
                case 'v':
                    if((operand1 == -1 && operand2==0) ||(operand2 == -1 && operand1==0) ) {
                        valueStack.push(-1);
                        break;
                    }
                    operand1BoolValue = (operand1 == 1);
                    operand2BoolValue = (operand2 == 1);
                    if(operand1 == -1 && operand2 == -1)
                    { valueStack.push(-1);}
                    else
                    {valueStack.push((operand1BoolValue || operand2BoolValue)?1:0);}
                    break;
                case '>':
                    if(operand1==0)
                    {
                        valueStack.push(1);
                        break;
                    }
                    if(operand2==1)
                    {
                        valueStack.push(1);
                        break;
                    }
                    if(operand1==1 && operand2==0)
                    {
                        valueStack.push(0);
                        break;
                    }
                    else {
                        valueStack.push(-1);
                        break;
                    }
            }
        }
    }

    private boolean hasHigherPrecedence(char op1, char op2) {
        if (op2 == '(') {
            return false;
        }
        if ((op1 == '~' && (op2 == '^' || op2 == 'v' || op2 == '>' || op2=='~')) || (op1 == '^' && (op2 == 'v' || op2 == '>')) || (op1 == 'v' && (op2 == '>'))) {
            return false;
        }
        return true;
    }

    private void printStacks(Stack<Character> operatorStack, Stack<Boolean> valueStack) {
        System.out.println("Operator Stack: " + operatorStack);
        System.out.println("Value Stack: " + valueStack);
    }
}

public class Main {
    public static void main(String[] args) {
        LogicalExpressionEvaluator evaluator = new LogicalExpressionEvaluator();
        Scanner sc = new Scanner(System.in);
        String expression = sc.nextLine();
        System.out.println();
        String VariableValuesExpression = sc.nextLine();

        LogicalExpression exp = new LogicalExpression(expression);

        evaluator.evaluateVariablesValues(VariableValuesExpression);
        boolean result = evaluator.evaluateExpression(exp);
        System.out.println(result);
    }
}

//((~~~PvQ)>R>FvD)
//
// P=false,R=false,F=true