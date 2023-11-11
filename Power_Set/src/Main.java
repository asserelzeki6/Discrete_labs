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

    public static Vector<Vector<String>> computePowerSet_Recursive(int index, Vector<String> SubSet,Vector<String> Set,Vector<Vector<String>> powerSet)
    {
        if(index == Set.size())
        {
            return powerSet;
        }
        Vector<String> SubSetCopy = new Vector<String>(SubSet);
        System.out.println(SubSetCopy);
        powerSet.add(SubSetCopy);
        for(int i = index+1; i < Set.size(); i++)
        {
            SubSet.add(Set.get(i));
            computePowerSet_Recursive(i, SubSet,Set,powerSet);
            SubSet.remove(SubSet.size()-1);
        }
        return powerSet;
    }

    public static Vector<String> remove_duplicates (Vector<String> s)
    {
        Map<String, Integer> frequencyMap = new HashMap<String, Integer>();

        for (int i = 0; i < s.size(); i++) {
            String element = s.elementAt(i);
            if (frequencyMap.containsKey(element)) {
                s.remove(i);
                i--;
            } else {
                frequencyMap.put(element, i);
            }
        }
        frequencyMap.clear();
        return s;
    }

    public static void main(String[] args) {

        System.out.println("Hello to the program!");
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the elements in the universal set");
            String[] s = sc.nextLine().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", "").replaceAll("\"", "").split(" ");
            Vector<String> Set = new Vector<String>(Arrays.asList(s));
            Set = remove_duplicates(Set);
            System.out.println("The universal set is: " + Set);
            System.out.println("All subsets of the universal set by iteration are: " + computePowerSet_Iterative(Set));
            System.out.println("All subsets of the universal set by recursion are: " + computePowerSet_Recursive(-1, new Vector<String>(), Set, new Vector<Vector<String>>()));
        }
    }
}