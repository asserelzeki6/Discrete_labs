import java.util.*;
public class Main {
    static Vector<Vector<String>> computePowerSet_Iterative(Vector<String> Set)
    {

        Vector<Vector<String>> powerSet = new Vector<Vector<String>>();
        int setSize = Set.size();
        int powerSetSize = (int)Math.pow(2, setSize);
        for (int i = 0; i < powerSetSize; i++) {
            Vector<String> subset = new Vector<String>();
            for (int j = 0; j < setSize; j++) {
                if ((i & (1 << j)) != 0)
                    subset.add(Set.get(j));
            }
            powerSet.add(subset);
        }
        return powerSet;
    }
     static Vector<Vector<String>> powerSet = new Vector<Vector<String>>();

    public static void computePowerSet_Recursive(int index, Vector<String> SubSet,Vector<String> Set)
    {
        if(index == Set.size())
        {
            return;
        }
        Vector<String> SubSetCopy = new Vector<String>(SubSet);
        powerSet.add(SubSetCopy);
        for(int i = index+1; i < Set.size(); i++)
        {
            SubSet.add(Set.get(i));
            computePowerSet_Recursive(i, SubSet,Set);
            SubSet.remove(SubSet.size()-1);
        }
    }


    public static void main(String[] args) {

        System.out.println("Hello to the program!");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the elements in the universal set");
        String[] s = sc.nextLine().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", "").replaceAll("\"","").split(" ");
        Vector<String> Set = new Vector<String>(Arrays.asList(s));
        Vector<String> SubSet =new Vector<String>();
        computePowerSet_Recursive(-1,SubSet,Set);
        System.out.println("All subsets of the universal set are: "+ powerSet);

    }
}