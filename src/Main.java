import java.util.ArrayList;
import java.util.List;

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
        exp.setRepresentation(exp_string);
        expressions.add(exp);
    }

    @Override
    public Expression applyRules() {
        Expression exp1 = expressions.get(0);
        Expression exp2 = expressions.get(1);

        for (InferenceRule rule : rules) {
            if (rule.matches(exp1, exp2)) {
                Expression result = rule.apply(exp1, exp2);
                expressions.add(result);
                System.out.println(result + " (" + rule.getClass().getSimpleName() + ")");
                return result;
            }
            else if (rule.matches(exp2, exp1)) {
                Expression result = rule.apply(exp2, exp1);
                expressions.add(result);
                System.out.println(result + " (" + rule.getClass().getSimpleName() + ")");
                return result;
            }
        }
        System.out.println("The input expressions cannot be inferred");
        return null;
    }
}

class Modes_Pollens implements InferenceRule {
    //  Modus ponens. Given expressions of the form "P > Q" and "P", the rule
    //allows inferring "Q"

    @Override
    public boolean matches(Expression exp1, Expression exp2) {
        
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
    }
}
class Modus_tollens implements InferenceRule {
    //    Modus tollens. Given expressions of the form "P > Q" and "~Q", the rule
    //    allows inferring "~P"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
    }

}
class Hypothetical_syllogism implements InferenceRule {
    //    Hypothetical syllogism. Given expressions of the form "P > Q" and "Q > R",
//    the rule allows inferring "P > R"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
    }

}

class Disjunctive_syllogism implements InferenceRule {
    //    Disjunctive syllogism. Given expressions of the form "P v Q" and "~P", the
//    rule allows inferring "Q"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
    }
}
class Resolution implements InferenceRule {
    //  Resolution. Given expressions of the form "P v Q" and "~P v R", the rule
    //allows inferring "Q v R"
    @Override
    public boolean matches(Expression exp1, Expression exp2) {
    }

    @Override
    public Expression apply(Expression exp1, Expression exp2) {
    }

}

public class Main {
    public static void main(String[] args) {
        InferenceEngine engine = new SimpleInferenceEngine();

        // Add inference rules
        engine.addRule(new ModusPonensRule());
        // Add other inference rules here (Modus Tollens, Hypothetical Syllogism, Disjunctive Syllogism, Resolution)

        // Add input expressions
        engine.addExpression(new LogicalExpression("~X v Y"));
        engine.addExpression(new LogicalExpression("X v Z"));

        // Apply inference rules
        engine.applyRules();
    }
}
