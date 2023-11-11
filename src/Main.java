import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Expression {

    String getRepresentation();

    void setRepresentation(String representation);
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

interface InferenceRule {
    boolean matches(Expression exp1, Expression exp2);
    Expression apply(Expression exp1, Expression exp2);
}

// Define the InferenceEngine interface
interface InferenceEngine {
    void addRule(InferenceRule rule);
    void addExpression(Expression exp);
    Expression applyRules();
}

// Implement the InferenceEngine
class SimpleInferenceEngine implements InferenceEngine {
    private List<InferenceRule> rules;
    private List<Expression> expressions;

    public SimpleInferenceEngine() {
        rules = new ArrayList<>();
        expressions = new ArrayList<>();
    }

    @Override
    public void addRule(InferenceRule rule) {
        rules.add(rule);
    }

    @Override
    public void addExpression(Expression exp) {
        String exp_string = exp.getRepresentation();
        exp_string = exp_string.replaceAll(" ", "");
        exp_string = simplifyNegation(exp_string);
        exp.setRepresentation(exp_string);
        expressions.add(exp);
    }
    private String simplifyNegation(String exp)
    {
        for(int i=0;i<exp.length();i++)
        {
            if(exp.charAt(i)=='~')
            {
                if(exp.charAt(i+1)=='~')
                {
                    exp = exp.substring(0,i) + exp.substring(i+2);
                    i--;
                }
            }
        }
        return exp;
    }
    @Override
    public Expression applyRules() {
        Expression exp1 = expressions.get(0);
        Expression exp2 = expressions.get(1);

        for (InferenceRule rule : rules) {
            if (rule.matches(exp1, exp2)) {
                Expression result = rule.apply(exp1, exp2);
                expressions.add(result);
                System.out.println(simplifyNegation(result.getRepresentation()) + " (" + rule.getClass().getSimpleName() + ")");
                expressions.clear();
                return result;
            }
        }
        expressions.clear();
        System.out.println("The input expressions cannot be inferred");
        return null;
    }
}

class ModusPonens implements InferenceRule {
    //  Modus ponens. Given expressions of the form "P > Q" and "P", the rule
    //allows inferring "Q"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] exp1_string = new String[2];
        String exp2_string = new String();
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length()) {
            if (!exp1.getRepresentation().contains(">"))
                return false;
            exp1_string = exp1.getRepresentation().replace(" ", "").split(">");
            exp2_string = exp2.getRepresentation();
        } else if(exp1.getRepresentation().length() == exp2.getRepresentation().length())
            return false;
        else
        {
            if (!exp2.getRepresentation().contains(">"))
                return false;
            exp1_string = exp2.getRepresentation().replace(" ", "").split(">");
            exp2_string = exp1.getRepresentation();
        }
        if (exp1_string[0].equals(exp2_string))
            return true;

        return false;
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length()) {
            String[] exp_string = exp1.getRepresentation().replace(" ", "").split(">");
//            System.out.println(new LogicalExpression(exp_string[1]).getRepresentation());
            return new LogicalExpression(exp_string[1]);
        }
        String[] exp_string = exp2.getRepresentation().replace(" ", "").split(">");
//        System.out.println(new LogicalExpression(exp_string[1]).getRepresentation());
        return new LogicalExpression(exp_string[1]);
    }
}
class ModusTollens implements InferenceRule {
    //    Modus tollens. Given expressions of the form "P > Q" and "~Q", the rule
    //    allows inferring "~P"
    String negation (String exp)
    {
        if(exp.length()>1 &&  (exp.contains("v") || exp.contains("^"))){
            for(int i=0;i<exp.length();i++)
            {
                if(exp.charAt(i)!='^' && exp.charAt(i)!='v')
                {
                    if(exp.charAt(i)=='~')
                        exp= exp.substring(0,i)+(exp.substring(i+1));
                    else {
                        exp = exp.substring(0, i) + "~" + (exp.substring(i));
                        i++;
                    }
                }
                else if(exp.charAt(i)=='^')
                    exp=exp.substring(0,i)+"v"+exp.substring(i+1);
                else
                    exp=exp.substring(0,i)+"^"+exp.substring(i+1);

            }
            if(exp.charAt(0)!='~')
                return "~("+exp+")";
            return "~"+exp.substring(1);
        }
        if(exp.length()>1 && exp.contains(">"))
            return "~("+exp+")";
        if(exp.charAt(0)!='~')
            return "~" + exp;
        return exp.substring(1);
    }
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] exp1_string = new String[2];
        String exp2_string = new String();
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length() || (exp1.getRepresentation().length() <= exp2.getRepresentation().length()) &&(exp1.getRepresentation().contains(">"))) {
            if (!exp1.getRepresentation().contains(">")) {return false;}
            exp1_string = exp1.getRepresentation().replace(" ", "").split(">");
            exp2_string = exp2.getRepresentation();
        } else if(exp1.getRepresentation().length() < exp2.getRepresentation().length() || (exp1.getRepresentation().length() >= exp2.getRepresentation().length() && ( exp2.getRepresentation().contains(">" )))){
            if (!exp2.getRepresentation().contains(">")) {return false;}

            exp1_string = exp2.getRepresentation().replace(" ", "").split(">");
            exp2_string = exp1.getRepresentation();
        }
        else{
            return false;
        }

        if (negation(exp1_string[0]).equals(exp2_string) || negation(exp1_string[1]).equals(exp2_string)) {
            return true;
        }
        return false;
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length() || (exp1.getRepresentation().length() == exp2.getRepresentation().length() && (exp1.getRepresentation().contains(">") ))) {
            String[] exp_string = exp1.getRepresentation().replace(" ", "").split(">");
            return new LogicalExpression(negation(exp_string[0]));
        }
        String[] exp_string = exp2.getRepresentation().replace(" ", "").split(">");
        return new LogicalExpression(negation(exp_string[1]));
    }

}
class HypotheticalSyllogism implements InferenceRule {
    //    Hypothetical syllogism. Given expressions of the form "P > Q" and "Q > R",
//    the rule allows inferring "P > R"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        if(!exp1.getRepresentation().contains(">") || !exp2.getRepresentation().contains(">"))
            return false;
        String[] exp1_string = exp1.getRepresentation().replace(" ", "").split(">");
        String[] exp2_string = exp2.getRepresentation().replace(" ", "").split(">");
        if (exp1_string[1].equals(exp2_string[0]) || exp1_string[1].equals(exp2_string[1])) {
            return true;
        }

        return false;
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {

        String[] exp1_string = exp1.getRepresentation().replace(" ", "").split(">");
        String[] exp2_string = exp2.getRepresentation().replace(" ", "").split(">");
        if (exp1_string[1].equals(exp2_string[0])) {
            return new LogicalExpression(exp1_string[0] + " > " + exp2_string[1]);
        }
        return new LogicalExpression(exp2_string[0] + " > " + exp1_string[1]);
    }

}

class DisjunctiveSyllogism implements InferenceRule {
    //    Disjunctive syllogism. Given expressions of the form "P v Q" and "~P", the
//    rule allows inferring "Q"
    String negation (String exp)
    {
        if(exp.length()>1 &&  (exp.contains("v") || exp.contains("^"))){
            for(int i=0;i<exp.length();i++)
            {
                if(exp.charAt(i)!='^' && exp.charAt(i)!='v')
                {
                    if(exp.charAt(i)=='~')
                        exp= exp.substring(0,i)+(exp.substring(i+1));
                    else {
                        exp = exp.substring(0, i) + "~" + (exp.substring(i));
                        i++;
                    }
                }
                else if(exp.charAt(i)=='^')
                    exp=exp.substring(0,i)+"v"+exp.substring(i+1);
                else
                    exp=exp.substring(0,i)+"^"+exp.substring(i+1);

            }
            if(exp.charAt(0)!='~')
                return "~("+exp+")";
            return "~"+exp.substring(1);
        }
        if(exp.length()>1 && exp.contains(">"))
            return "~("+exp+")";
        if(exp.charAt(0)!='~')
            return "~" + exp;
        return exp.substring(1);
    }
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        String[] exp1_string = new String[2];
        String exp2_string = new String();
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length()) {
            if (!exp1.getRepresentation().contains("v"))
                return false;
            exp1_string = exp1.getRepresentation().replace(" ", "").split("v");
            exp2_string = exp2.getRepresentation();
        } else {
            if (!exp2.getRepresentation().contains("v"))
                return false;
            exp1_string = exp2.getRepresentation().replace(" ", "").split("v");
            exp2_string = exp1.getRepresentation();
        }
        if (negation(exp1_string[0]).equals(exp2_string) || negation(exp1_string[1]).equals(exp2_string)) {
            return true;
        }
        return false;
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {

        String[] exp1_string = new String[2];
        String exp2_string = new String();
        if (exp1.getRepresentation().length() > exp2.getRepresentation().length()) {
            exp1_string = exp1.getRepresentation().replace(" ", "").split("v");
            exp2_string = exp2.getRepresentation();
        } else {
            exp1_string = exp2.getRepresentation().replace(" ", "").split("v");
            exp2_string = exp1.getRepresentation();
        }
        if (negation(exp1_string[0]).equals(exp2_string))
            return new LogicalExpression(exp1_string[1]);
        return new LogicalExpression(exp1_string[0]);

    }
}
class Resolution implements InferenceRule {
    //  Resolution. Given expressions of the form "P v Q" and "~P v R", the rule
    //allows inferring "Q v R"
    String negation (String exp)
    {
        if(exp.length()>1 &&  (exp.contains("v") || exp.contains("^"))){
            for(int i=0;i<exp.length();i++)
            {
                if(exp.charAt(i)!='^' && exp.charAt(i)!='v')
                {
                    if(exp.charAt(i)=='~')
                        exp= exp.substring(0,i)+(exp.substring(i+1));
                    else {
                        exp = exp.substring(0, i) + "~" + (exp.substring(i));
                        i++;
                    }
                }
                else if(exp.charAt(i)=='^')
                    exp=exp.substring(0,i)+"v"+exp.substring(i+1);
                else
                    exp=exp.substring(0,i)+"^"+exp.substring(i+1);

            }
            if(exp.charAt(0)!='~')
                return "~("+exp+")";
            return "~"+exp.substring(1);
        }
        if(exp.length()>1 && exp.contains(">"))
            return "~("+exp+")";
        if(exp.charAt(0)!='~')
            return "~" + exp;
        return exp.substring(1);
    }
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        if (!exp1.getRepresentation().contains("v") || !exp2.getRepresentation().contains("v"))
            return false;
        String[] exp1_string = exp1.getRepresentation().replace(" ", "").split("v");
        String[] exp2_string = exp2.getRepresentation().replace(" ", "").split("v");

        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                if (exp1_string[i].equals(negation(exp2_string[j])))
                    return true;
        return false;
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {

        String[] exp1_string = exp1.getRepresentation().replace(" ", "").split("v");
        String[] exp2_string = exp2.getRepresentation().replace(" ", "").split("v");
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                if (exp1_string[i].equals(negation(exp2_string[j])))
                    return new LogicalExpression(exp1_string[1 - i] + " v " + exp2_string[1 - j]);
        return null;
    }
}
public class Main {
    public static void main(String[] args) {
        InferenceEngine engine = new SimpleInferenceEngine();
        Scanner sc = new Scanner(System.in);
        // Add inference rules
        engine.addRule(new ModusPonens());
        engine.addRule(new ModusTollens());
        engine.addRule(new HypotheticalSyllogism());
        engine.addRule(new DisjunctiveSyllogism());
        engine.addRule(new Resolution());
        System.out.println("Hello to our program :)");
        System.out.println("Here are some basic examples");
        System.out.println("Expression is");
        System.out.println("P > Q");
        System.out.println("P");
        // Add input expressions
        engine.addExpression(new LogicalExpression("P > Q"));
        engine.addExpression(new LogicalExpression("P"));
        engine.applyRules();
        System.out.println("Expression is");
        System.out.println("P > Q");
        System.out.println("~Q");
        // Add input expressions
        engine.addExpression(new LogicalExpression("P > Q"));
        engine.addExpression(new LogicalExpression("~Q"));
        engine.applyRules();
        System.out.println("Expression is");
        System.out.println("P > Q");
        System.out.println("Q > R");
        // Add input expressions
        engine.addExpression(new LogicalExpression("P > Q"));
        engine.addExpression(new LogicalExpression("Q > R"));
        engine.applyRules();
        System.out.println("Expression is");
        System.out.println("~P v Q");
        System.out.println("~Q v R");
        // Add input expressions
        engine.addExpression(new LogicalExpression("~P v Q"));
        engine.addExpression(new LogicalExpression("~Q v R"));
        // Apply inference rules
        engine.applyRules();
        while(true)
        {
            System.out.println("please enter your expressions in separate lines and without parentheses");
            String exp1 = sc.nextLine();
            String exp2 = sc.nextLine();
            System.out.println("Expression is");
            System.out.println(exp1);
            System.out.println(exp2);
            // Add input expressions3
            System.out.println("------------");
            engine.addExpression(new LogicalExpression(exp1));
            engine.addExpression(new LogicalExpression(exp2));
            // Apply inference rules
            engine.applyRules();
        }
    }
}

