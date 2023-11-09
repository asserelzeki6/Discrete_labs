import java.util.*;
interface InferenceRule {
    boolean matches(String exp1, String  exp2);
    String apply(String exp1, String exp2);
}
interface InferenceEngine {
    void addRule(InferenceRule rule);
    void addExpression(String exp);
    String applyRules();
}
class Modes_Pollens implements InferenceEngine {
    //  Modus ponens. Given expressions of the form "P > Q" and "P", the rule
    //allows inferring "Q"
    private List<InferenceRule> rules = new ArrayList<>();
    private List<String> expressions = new ArrayList<>();
    public void addRule(InferenceRule rule) {
        rules.add(rule);
    }
    public void addExpression(String exp) {
        expressions.add(exp);
    }
    public String applyRules() {
        String result = "";
        for (String exp : expressions) {
            for (InferenceRule rule : rules) {
                if (rule.matches(exp, result)) {
                    result = rule.apply(exp, result);
                }
            }
        }
        return result;
    }
}
class Modus_tollens implements InferenceRule {
//    Modus tollens. Given expressions of the form "P > Q" and "~Q", the rule
//    allows inferring "~P"
    public boolean matches(String exp1, String exp2) {
        return exp1.equals("p->q") && exp2.equals("¬q");
    }
    public String apply(String exp1, String exp2) {
        return "¬p";
    }
}
class Hypothetical_syllogism implements InferenceRule {
//    Hypothetical syllogism. Given expressions of the form "P > Q" and "Q > R",
//    the rule allows inferring "P > R"
    public boolean matches(String exp1, String exp2) {
        return exp1.equals("p->q") && exp2.equals("q->r");
    }
    public String apply(String exp1, String exp2) {
        return "p->r";
    }
}

class Disjunctive_syllogism implements InferenceRule {
//    Disjunctive syllogism. Given expressions of the form "P v Q" and "~P", the
//    rule allows inferring "Q"
    public boolean matches(String exp1, String exp2) {
        return exp1.equals("p∨q") && exp2.equals("¬p");
    }
    public String apply(String exp1, String exp2) {
        return "q";
    }
}
class Resolution implements InferenceRule {
    //  Resolution. Given expressions of the form "P v Q" and "~P v R", the rule
    //allows inferring "Q v R"
    public boolean matches(String exp1, String exp2) {
        return exp1.equals("p∨q") && exp2.equals("¬p∨r");
    }
    public String apply(String exp1, String exp2) {
        return "q∨r";
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}