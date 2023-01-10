import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** A class that holds the alphabet of the FA */
class Alphabet
{
    String[] alphabet; // Array for storing user-inputted symbols
    int numberOfSymbols = 0; // Number of symbols that is in the array

    /** Constructor with a given size */
    Alphabet(int alphabetSize)
    {
        alphabet = new String[alphabetSize];
    }

    /** Copy constuctor */
    Alphabet(Alphabet alpha)
    {
        this.alphabet = Arrays.copyOf(alpha.alphabet, alpha.getNumberOfSymbols());
        this.numberOfSymbols = alpha.getNumberOfSymbols();
    }

    /** Adds a symbol into the alphabet array.
        @param s  A symbol to be added. */
    public void addSymbol(String s)
    {
        if (isFull())
        {
            increaseCapacity();
        }
        alphabet[numberOfSymbols] = s;
        numberOfSymbols++;
    }

    /** Checks if the alphabet array is full.
        @return  True if array is full. */
    private boolean isFull()
    {
        return numberOfSymbols == alphabet.length;
    }

    /** Increases the size of the alphabet array to add more symbols if needed. */
    private void increaseCapacity()
    {
        int newLength = 1 + alphabet.length;
        alphabet = Arrays.copyOf(alphabet, newLength);
    }

    /** Retrieves the number of symbols in the alphabet array.
        @return  Number of symbols. */
    public int getNumberOfSymbols()
    {
        return alphabet.length;
    }

    /** Retrieves the alphabet array.
        @return  Alphabet array. */
    public String[] getArray()
    {
        return alphabet;
    }
}

/** A class that holds the set of states for the FA */
class States
{
    Boolean[] states; // Array for storing states from 0 to n-1, n is the number of states that the user inputted.
                      // True if final state, false otherwise.

    /** Constructor with a given number of states */
    States(int numberOfStates)
    {
        states = new Boolean[numberOfStates];
        for (int i = 0; i < states.length; i++)
        {
            states[i] = false;
        }
    }

    /** Copy constructor */
    States(States sta)
    {
        this.states = Arrays.copyOf(sta.states, sta.getNumberOfStates());
    }

    /** Retrieves the number of states in the states array.
        @return  Number of states. */
    public int getNumberOfStates()
    {
        return states.length;
    }

    /** Retrieves the states array.
        @return  States array. */
    public Boolean[] getArray()
    {
        return states;
    }

    /** Sets an existing state to be a final state.
        @param stateIndex  A number of the state. */
    public void setFinalState(int stateIndex)
    {
        states[stateIndex] = true;
    }
}

/** A class that simulates the universal FA machine */
class FiniteAutomataMachine
{
    TreeSet<String> alphabet; // Set of alphabet symbols
    States states; // Set of states
    String[][] transitionTable; // Set of transitions
    int numberOfTransitions = 0; // Number of transitions in the transition table
    int maxTableSize; // Max size of transition table, which is size of alphabet * number of states

    /** Constructor with a given alphabet and set of states */
    FiniteAutomataMachine(Alphabet alpha, States states)
    {
        this.alphabet = new TreeSet<String>(Arrays.asList(alpha.getArray())); // Removes any duplicate symbols
        this.states = new States(states);
        maxTableSize = states.getNumberOfStates() * alphabet.size();
        transitionTable = new String[maxTableSize][3];
    }

    /** Adds a transition with a given source state, alphabet symbol, and destination state.
        @param fromState  A source state. 
        @param symbol  An alphabet symbol. 
        @param toState  A destination state. */
    public void addTransition(String fromState, String symbol, String toState)
    {
        if (checkState(fromState) == false) // Checks if source state exists
        {
            System.out.println("State '" + fromState + "' does not exist");
        }
        if (checkSymbol(symbol) == false) // Checks if alphabet symbol exists
        {
            System.out.println("Symbol '" + symbol + "' does not exist");
        }
        if (checkState(toState) == false) // Checks if destination state exists
        {
            System.out.println("State '" + toState + "' does not exist");
        }
        if (checkState(fromState) && checkSymbol(symbol) && checkState(toState)) // If all exists, transition is added
        {
            transitionTable[numberOfTransitions][0] = fromState;
            transitionTable[numberOfTransitions][1] = symbol;
            transitionTable[numberOfTransitions][2] = toState;
            numberOfTransitions++;
        }
    }

    /** Checks if state exists.
        @param state  The given state to check.
        @return  True if state exists. */
    public boolean checkState(String state)
    {
        try 
        { 
            return (Integer.parseInt(state) < states.getNumberOfStates() && Integer.parseInt(state) >= 0);
        }
        catch (Exception e) // Avoids crashes if state is not an integer
        {
            return false;
        }
    }

    /** Checks if alphabet symbol exists.
        @param symbol  The given symbol to check.
        @return  True if symbol exists. */
    public boolean checkSymbol(String symbol)
    {
        String[] tempArray = new String[alphabet.size()];
        int index = 0; // Index of temporary array
        for (String ele: alphabet) // Copies alphabet from set into a temporary array
        {
            tempArray[index++] = ele;
        }
        boolean found = false;
        for (int i = 0; i < alphabet.size(); i++)
        {
            if (tempArray[i].equals(symbol)) // If given symbol is found in temporary array, return true
            {
                found = true;
                break;
            }
        }
        return found;
    }

    /** Displays the set of final states of the FA. */
    public void displayFinalStates()
    {
        Boolean[] tempArray = states.getArray();
        int numberOfFinalStates = 0;
        for (int index = 0; index < tempArray.length; index++) // Iterates through the states set
        {
            if (tempArray[index] == true)
            {
                numberOfFinalStates++;
                int count = 0;
                for (int j = index + 1; j < tempArray.length; j++) // Checks if there are anymore final states after the current state
                {
                    if (tempArray[j] == true)
                    {
                        count++;
                    }
                }
                if (count > 0) // Avoids any extra commas after last final state is printed
                {
                    System.out.print(index + ", ");
                }
                else
                {
                    System.out.print(index);
                }
            }
        }
        if (numberOfFinalStates == 0) // If there are no final states, print none
        {
            System.out.print("none");
        }
        System.out.println();
    }

    /** Displays the alphabet of the FA. */
    public void displayAlphabet()
    {
        Iterator<String> iterator = alphabet.iterator();
        int alphabetIndex = 1;
        while (iterator.hasNext()) // Iterates through the alphabet set
        {
            if (alphabetIndex++ == alphabet.size()) // Avoids any extra commas after last alphabet symbol is printed
            {
                System.out.print(iterator.next());
                break;
            }
            System.out.print(iterator.next() + ", ");
        }
        System.out.println();
    }

    /** Displays the transition table of the FA. */
    public void displayTransitionTable()
    {
        sortTransitionTable(); // Sorts the table by sequential order, also removes any duplicate transitions
        String[][] simpleTable = new String[maxTableSize][3]; // Simplified transition table
        boolean endCopy = false; // Signals the end of copying transition table
        for (int i = 0; i < maxTableSize; i++) // Copies transition table
        {
            for (int j = 0; j < 3; j++)
            {
                if (transitionTable[i][j] == null)
                {
                    endCopy = true;
                    break;
                }
                simpleTable[i][j] = transitionTable[i][j];
            }
            if (endCopy)
            {
                break;
            }
        }
        boolean endOfFullTable = false; // Signals the end of the full transition table
        boolean endOfSimplifiedTable = false; // Signals the end of the simplified table
        int letterCount = 0; // Counts the letters in a sequential order
        int digitCount = 0; // Counts the digits in a sequential order
        int indexHolder = 0; // Placeholder to remember a previous index position
        int simpleIndex = 0; // Index of the simplified table
        for (int i = 0; i < maxTableSize; i++)
        {
            if (simpleTable[i][0] == null && simpleTable[i][1] == null && simpleTable[i][2] == null)
            {
                continue;
            }
            if (Character.isDigit(simpleTable[i][1].charAt(0))) // Checks if symbol is a digit
            { 
                int from = Integer.parseInt(simpleTable[i][1]);
                int to = from + 1;
                indexHolder = i;
                i++;
                while (simpleTable[i][1] != null) // Checks for a range of digits
                {
                    if (Character.isDigit(simpleTable[i][1].charAt(0)) && simpleTable[i][0].equals(simpleTable[indexHolder][0]) && Integer.parseInt(simpleTable[i][1]) == to && simpleTable[i][2].equals(simpleTable[indexHolder][2]))
                    {
                        digitCount++;
                        to++;
                        i++;
                        if (i == maxTableSize)
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                if (digitCount == 0)
                {
                    simpleTable[simpleIndex][0] = simpleTable[indexHolder][0];
                    simpleTable[simpleIndex][1] = simpleTable[indexHolder][1];
                    simpleTable[simpleIndex][2] = simpleTable[indexHolder][2];
                    if (indexHolder != simpleIndex)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                    }
                    i--;
                    simpleIndex++;
                }
                else
                {
                    simpleTable[simpleIndex][0] = simpleTable[indexHolder][0];
                    simpleTable[simpleIndex][1] = Integer.toString(from) + "-" + Integer.toString(to-1);
                    simpleTable[simpleIndex][2] = simpleTable[indexHolder][2];
                    if (indexHolder != simpleIndex)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                    }
                    indexHolder++;
                    while (indexHolder < i)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                        indexHolder++;
                    }
                    i--;
                    digitCount = 0;
                    simpleIndex++;
                }
            }
            else if (Character.isLetter(simpleTable[i][1].charAt(0))) // Checks if symbol is a letter
            {
                char from = simpleTable[i][1].charAt(0);
                char temp = from;
                char to = ++temp;
                indexHolder = i;
                i++;
                while (simpleTable[i][1] != null) // Checks for a range of letters
                {
                    if (Character.isLetter(simpleTable[i][1].charAt(0)) && simpleTable[i][0].equals(simpleTable[indexHolder][0]) && simpleTable[i][1].charAt(0) == to && simpleTable[i][2].equals(simpleTable[indexHolder][2]))
                    {
                        letterCount++;
                        to++;
                        i++;
                        if (i == maxTableSize)
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                if (letterCount == 0)
                {
                    simpleTable[simpleIndex][0] = simpleTable[indexHolder][0];
                    simpleTable[simpleIndex][1] = simpleTable[indexHolder][1];
                    simpleTable[simpleIndex][2] = simpleTable[indexHolder][2];
                    if (indexHolder != simpleIndex)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                    }
                    i--;
                    simpleIndex++;
                }
                else
                {
                    simpleTable[simpleIndex][0] = simpleTable[indexHolder][0];
                    simpleTable[simpleIndex][1] = Character.toString(from) + "-" + Character.toString(--to);
                    simpleTable[simpleIndex][2] = simpleTable[indexHolder][2];
                    if (indexHolder != simpleIndex)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                    }
                    indexHolder++;
                    while (indexHolder < i)
                    {
                        simpleTable[indexHolder][0] = null;
                        simpleTable[indexHolder][1] = null;
                        simpleTable[indexHolder][2] = null;
                        indexHolder++;
                    }
                    i--;
                    letterCount = 0;
                    simpleIndex++;
                }
            }
            else // Stores any other symbols that aren't alphanumerical
            {
                simpleTable[simpleIndex][0] = simpleTable[i][0];
                simpleTable[simpleIndex][1] = simpleTable[i][1];
                simpleTable[simpleIndex][2] = simpleTable[i][2];
                if (i != simpleIndex)
                {
                    simpleTable[i][0] = null;
                    simpleTable[i][1] = null;
                    simpleTable[i][2] = null;
                }
                simpleIndex++;
            }
        }
        
        Boolean needSimpleTable = false; // Signals if displaying a simplified transition table is necessary
        for (int i = 0; i < maxTableSize; i++)
        {
            if (simpleTable[i][1] == null)
            {
                break;
            }
            // If table contains a range, simplified table will be displayed
            if (simpleTable[i][1].matches(".+-.+"))
            {
                needSimpleTable = true;
                break;
            }
        }

        if (needSimpleTable)
        {
            System.out.println("Full Table");
        }
        for (int i = 0; i < maxTableSize; i++) // Displays full/original transition table
        {
            System.out.print("\t");
            if (numberOfTransitions == 0) // If there are no transitions, display message
            {
                System.out.println("No transitions");
                break;
            }
            for (int j = 0; j < 3; j++)
            {
                if (transitionTable[i][j] == null) // If table reaches null, stop displaying
                {
                    endOfFullTable = true;
                    break;
                }
                System.out.print(transitionTable[i][j] + " ");
            }
            System.out.println();
            if (endOfFullTable)
            {
                break;
            }
        }
        if (needSimpleTable)
        {
            System.out.println("Simplified Table");
            for (int i = 0; i < maxTableSize; i++) // Displays simplified transition table
            {
                System.out.print("\t");
                if (numberOfTransitions == 0) // If there are no transitions, display message
                {
                    System.out.println("No transitions");
                    break;
                }
                for (int j = 0; j < 3; j++)
                {
                    if (simpleTable[i][j] == null) // If table reaches null, stop displaying
                    {
                        endOfSimplifiedTable = true;
                        break;
                    }
                    System.out.print(simpleTable[i][j] + " ");
                }
                System.out.println();
                if (endOfSimplifiedTable)
                {
                    break;
                }
            }
        }
    }

    /** Sorts the transition table by sequential order. */
    private void sortTransitionTable()
    {
        // Removes any duplicate transitions
        String[][] tempTable = new String[maxTableSize][3]; // Temporary table to hold transitions
        AtomicInteger ai = new AtomicInteger(0); // Used to change index number in forEach loop
        Arrays.stream(transitionTable).map(Arrays::asList).distinct()
        .forEach( row -> 
        {
            int index = ai.intValue();
            tempTable[index][0] = row.get(0);
            tempTable[index][1] = row.get(1);
            tempTable[index][2] = row.get(2);
            ai.getAndIncrement();
        });
        transitionTable = new String[maxTableSize][3]; // Resets table
        boolean endCopy = false; // Signals the end of copying table
        for (int i = 0; i < maxTableSize; i++) // Refills transition table
        {
            for (int j = 0; j < 3; j++)
            {
                if (tempTable[i][j] == null) // If null, stop copying
                {
                    endCopy = true;
                    break;
                }
                transitionTable[i][j] = tempTable[i][j];
            }
            if (endCopy)
            {
                break;
            }
        }

        // Sorts table by columns
        java.util.Arrays.sort(transitionTable, new java.util.Comparator<String[]>()
        {
            public int compare(String[] a, String[] b)
            {
                return String.valueOf(a[2]).compareTo(String.valueOf(b[2]));
            }
        });
        java.util.Arrays.sort(transitionTable, new java.util.Comparator<String[]>()
        {
            public int compare(String[] a, String[] b)
            {
                return String.valueOf(a[1]).compareTo(String.valueOf(b[1]));
            }
        });
        java.util.Arrays.sort(transitionTable, new java.util.Comparator<String[]>()
        {
            public int compare(String[] a, String[] b)
            {
                return String.valueOf(a[0]).compareTo(String.valueOf(b[0]));
            }
        });
    }

    /** Formats and displays the test string table results.
        @param testStrings  An array of test strings. */
    public void test(String[] testStrings)
    {
        for (int i = 0; i < testStrings.length; i++)
        {
            if (testStrings[i] == null || testStrings[0].equals("....."))
            {
                System.out.println("\tNo strings to test\n");
                System.out.println(".....");
                System.exit(0);
            }
            else if (testStrings[i].equals(".....")) // Signals the end of the table results
            {
                break;
            }
            else if (testStrings[i].equals("")) // Denotes an empty string
            {
                System.out.print("\t(empty)\t\t\t");
            }
            else
            {
                // Some formatting to give the test string table results a cleaner look
                if (testStrings[i].length() >= 16)
                {
                    System.out.print("\t" + testStrings[i] + "\t");
                }
                else if (testStrings[i].length() >= 8)
                {
                    System.out.print("\t" + testStrings[i] + "\t\t");
                }
                else
                {
                    System.out.print("\t" + testStrings[i] + "\t\t\t");
                }
            }
            test(testStrings[i]); // Calls the simulation method
        }
    }

    /** Performs the simulation of an FA.
        @param testString  A test string. */
    public void test(String testString)
    {
        Boolean[] stateArray = states.getArray(); // Set of given states, true if final state
        int state = 0; // Initial state
        int charIndex = 0; // Character index of the test string
        String symbol; // Symbol at character index
        for (charIndex = 0; charIndex < testString.length(); charIndex++)
        {
            symbol = Character.toString(testString.charAt(charIndex));
            // Checks if symbol is in the alphabet
            if (checkSymbol(symbol))
            {
                // Retrieves the next state with the given current state and symbol
                state = NextState(state, symbol);
                if (state == -1) // State is -1 if destination state is nonexistent, meaning that the string goes nowhere
                {
                    System.out.println("Reject");
                    break;
                }
            }
            else
            {
                // Rejects if symbol is not in the alphabet
                System.out.println("Reject");
                break;
            }
        }
        // Checks if the end of string is met, then either accept or reject depending on ending state
        if (charIndex == testString.length())
        {
            if (stateArray[state] == true)
            {
                System.out.println("Accept");
            }
            else
            {
                System.out.println("Reject");
            }
        }
    }

    /** Retrieves the next state with a given current state and symbol.
        @param state  The current state.
        @param symbol  The symbol.
        @return  The next state. */
    public int NextState(int state, String symbol)
    {
        if (numberOfTransitions == 0)
        {
            return -1;
        }
        int i;
        for (i = 0; i < maxTableSize; i++) // Searches through transition table
        {
            if (transitionTable[i][0] == null && transitionTable[i][1] == null && transitionTable[i][2] == null)
            {
                continue;
            }
            if (transitionTable[i][0].equals(Integer.toString(state)) && transitionTable[i][1].equals(symbol))
            {
                state = Integer.parseInt(transitionTable[i][2]);
                break;
            }
        }
        if (i == maxTableSize) // If table reaches end and there is no destination state found
        {
            state = -1;
        }
        return state;
    }

    /** Retrieves the number of transitions of the transition table.
        @return  The number of transitions. */
    public int getNumberOfTransitions()
    {
        return numberOfTransitions;
    }

    /** Retrieves the max size of the transition table.
        @return  The max table size. */
    public int getTableSize()
    {
        return maxTableSize;
    }
}

/** Driver class for running a simulation of an FA machine */
public class UniversalFA
{
    public static void main(String[] args) 
    {
        Scanner keyboard = new Scanner(System.in);

        // Title
        System.out.println("Universal FA");

        // Prompts for number of states
        int numberOfStates = 0;
        try
        {
            System.out.println("Enter number of states:");
            numberOfStates = keyboard.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("You must enter an integer");
            System.exit(0);
        }
        States FAstates = new States(numberOfStates);
        keyboard.nextLine();

        // Prompts for declaring final states
        System.out.println("Enter final states:");
        String finalStates = keyboard.nextLine(); // May enter as '0 1' or '0,1' or '0, 1' 
        finalStates = finalStates.replaceAll(",", "\s").replaceAll("\\s+", "\s"); // Handles commas
        String[] finalStatesModified = finalStates.split("\s"); // Handles spaces
        for (String state: finalStatesModified)
        {
            // Checks if the state exists or is a valid state
            try
            {
                if (Integer.parseInt(state) >= numberOfStates || Integer.parseInt(state) < 0)
                {
                    System.out.println("State '" + state + "' does not exist");
                }
                else
                {
                    FAstates.setFinalState(Integer.parseInt(state));
                }
            }
            catch (Exception e) // Avoids crashes if the state is not an integer
            {
                System.out.println("State '" + state + "' does not exist");
            }
        }
        
        // Prompts for the alphabet
        System.out.println("Enter alphabet (may include 'letters', 'numbers', and/or ranges i.e. '2-7', 'a-z', 'G-M'):");
        String alphabet = keyboard.nextLine(); // May enter as 'a b' or 'a,b' or 'a, b' 
        alphabet = alphabet.replaceAll(",", "\s").replaceAll("\\s+", "\s"); // Handles commas
        String[] alphabetModified = alphabet.split("\s"); // Handles spaces
        Alphabet FAsymbols = new Alphabet(alphabetModified.length);
        for (String s: alphabetModified)
        {
            if (s.equals("letters")) // This keyword adds all letters
            {
                for (char letter = 'a'; letter <= 'z'; letter++)
                {
                    FAsymbols.addSymbol(Character.toString(letter));
                    FAsymbols.addSymbol(Character.toString(Character.toUpperCase(letter)));
                }
            }
            else if (s.equals("numbers")) // This keyword adds all digits
            {
                for (int number = 0; number <= 9; number++)
                {
                    FAsymbols.addSymbol(Integer.toString(number));
                }
            }
            else if (s.matches(".+-.+")) // This keyword adds all characters within a given range
            {
                String[] t = s.split("-");
                // Checks if a digit range is given
                if ((Character.isDigit(t[0].charAt(0)) && Character.isDigit(t[1].charAt(0))) && (t[0].length() == 1 && t[1].length() == 1))
                {
                    int from = Integer.parseInt(t[0]);
                    int to = Integer.parseInt(t[1]);
                    if ((from >= 0 && from <= 9) && (to >= 0 && to <= 9))
                    {
                        for (int i = from; i <= to; i++)
                        {
                            // Adds symbols with given range
                            FAsymbols.addSymbol(Integer.toString(i));
                        }
                    }
                    else
                    {
                        // Ignore range
                        continue;
                    }
                }
                // Checks if a letter range is given
                else if ((Character.isLetter(t[0].charAt(0)) && Character.isLetter(t[1].charAt(0))) && (t[0].length() == 1 && t[1].length() == 1))
                {
                    char from = t[0].charAt(0);
                    char to = t[1].charAt(0);
                    if ((from >= 'A' && from <= 'Z' && to >= 'A' && to <= 'Z') || (from >= 'a' && from <= 'z' && to >= 'a' && to <= 'z'))
                    {
                        for (char c = from; c <= to; c++)
                        {
                            // Adds symbols with given range
                            FAsymbols.addSymbol(Character.toString(c));
                        }
                    }
                    else
                    {
                        // Ignore range
                        continue;
                    }
                }
                else
                {
                    // Ignore range
                    continue;
                }
            }
            else // Adds any individual digits, letters, or non-alphanumerical characters
            {
                FAsymbols.addSymbol(s);
            }
        }

        // Loads the given alphabet and states into an FA
        FiniteAutomataMachine FA_Machine = new FiniteAutomataMachine(FAsymbols, FAstates);

        // Prompts for the transitions
        System.out.println("Enter transitions in the format 'p a q' first (may also put 'letters', 'numbers', or ranges for the symbol)");
        System.out.println("Then up to 20 test strings (enter '.....' to finish): ");
        String transition;
        while (true)
        {
            transition = keyboard.nextLine();
            // Checks if string is in the format '(p a q)' or 'p a q'
            // If not, assume as a test string
            if (!transition.matches("\\(.+ .+ .+\\)") && !transition.matches(".+ .+ .+"))
            {
                break;
            }
            // Checks if string is in the format '(p letters q)' or 'p letters q'
            else if (transition.matches("\\(.+ letters .+\\)") || transition.matches(".+ letters .+"))
            {
                // Removes parentheses, if there exists any
                if (transition.charAt(0) == '(' && transition.charAt(transition.length() - 1) == ')')
                {
                    transition = transition.replaceAll("\\(", "").replaceAll("\\)", "");
                }
                String[] t = transition.split("\s"); // Handles spaces
                for (char letter = 'a'; letter <= 'z'; letter++) // Adds all letters to the transition
                {
                    // Adds lowercase letter to the transition
                    FA_Machine.addTransition(t[0], Character.toString(letter), t[2]);
                    // Checks if transition table is full
                    if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                    {
                        System.out.println("Enter test strings:");
                        break;
                    }
                    // Adds uppercase letter to the transition
                    FA_Machine.addTransition(t[0], Character.toString(Character.toUpperCase(letter)), t[2]);
                    // Checks if transition table is full
                    if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                    {
                        System.out.println("Enter test strings:");
                        break;
                    }
                }
                // Checks if transition table is full
                if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                {
                    break;
                }
            }
            // Checks if string is in the format '(p numbers q)' or 'p numbers q'
            else if (transition.matches("\\(.+ numbers .+\\)") || transition.matches(".+ numbers .+"))
            {
                // Removes parentheses, if there exists any
                if (transition.charAt(0) == '(' && transition.charAt(transition.length() - 1) == ')')
                {
                    transition = transition.replaceAll("\\(", "").replaceAll("\\)", "");
                }
                String[] t = transition.split("\s"); // Handles spaces
                for (int number = 0; number <= 9; number++) // Adds all digits to the transition
                {
                    // Adds digit to the transition
                    FA_Machine.addTransition(t[0], Integer.toString(number), t[2]);
                    // Checks if transition table is full
                    if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                    {
                        System.out.println("Enter test strings:");
                        break;
                    }
                }
                // Checks if transition table is full
                if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                {
                    break;
                }
            }
            // Checks if string is given as a range in the format '(p a q)' or 'p a q'
            else if (transition.matches("\\(.+ .+-.+ .+\\)") || transition.matches(".+ .+-.+ .+"))
            {
                // Removes parentheses, if there exists any
                if (transition.charAt(0) == '(' && transition.charAt(transition.length() - 1) == ')')
                {
                    transition = transition.replaceAll("\\(", "").replaceAll("\\)", "");
                }
                String[] t = transition.split("[-\s]");
                // Checks if a digit range is given
                if ((Character.isDigit(t[1].charAt(0)) && Character.isDigit(t[2].charAt(0))) && (t[1].length() == 1 && t[2].length() == 1))
                {
                    int from = Integer.parseInt(t[1]);
                    int to = Integer.parseInt(t[2]);
                    if ((from >= 0 && from <= 9) && (to >= 0 && to <= 9))
                    {
                        for (int i = from; i <= to; i++)
                        {
                            // Adds digit to the transition
                            FA_Machine.addTransition(t[0], Integer.toString(i), t[3]);
                            // Checks if transition table is full
                            if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                            {
                                System.out.println("Enter test strings:");
                                break;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Range not accepted");
                    }
                }
                // Checks if a letter range is given
                else if ((Character.isLetter(t[1].charAt(0)) && Character.isLetter(t[2].charAt(0))) && (t[1].length() == 1 && t[2].length() == 1))
                {
                    char from = t[1].charAt(0);
                    char to = t[2].charAt(0);
                    if ((from >= 'A' && from <= 'Z' && to >= 'A' && to <= 'Z') || (from >= 'a' && from <= 'z' && to >= 'a' && to <= 'z'))
                    {
                        for (char c = from; c <= to; c++)
                        {
                            // Adds digit to the transition
                            FA_Machine.addTransition(t[0], Character.toString(c), t[3]);
                            // Checks if transition table is full
                            if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                            {
                                System.out.println("Enter test strings:");
                                break;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Range not accepted");
                    }
                }
                else
                {
                    System.out.println("Range not accepted");
                }
                // Checks if transition table is full
                if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                {
                    break;
                }
            }
            // Adds any symbol to the transition
            else
            {
                // Removes parentheses, if there exists any
                if (transition.charAt(0) == '(' && transition.charAt(transition.length() - 1) == ')')
                {
                    transition = transition.replaceAll("\\(", "").replaceAll("\\)", "");
                }
                String[] t = transition.split("\s"); // Handles spaces
                // Adds symbol to the transition
                FA_Machine.addTransition(t[0], t[1], t[2]);
                // Checks if transition table is full
                if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
                {
                    System.out.println("Enter test strings:");
                    break;
                }
            }
        }

        String[] testStringsSet = new String[20]; // Set of 20 test strings
        if (!transition.equals(".....")) // Skips adding any test strings if "....." is entered
        {
            int i;
            if (FA_Machine.getNumberOfTransitions() == FA_Machine.getTableSize())
            {
                for (i = 0; i < testStringsSet.length; i++)
                {
                    String testString = keyboard.nextLine();
                    testString = testString.replaceAll("\s", ""); // Removes spaces, if there exists any
                    if (testString.equals(".....")) // Ends the list of test strings
                    {
                        testStringsSet[i] = testString;
                        break;
                    }
                    testStringsSet[i] = testString;
                }
            }
            else
            {
                System.out.println("Read as a test string, enter up to 19 test strings, ('.....' to finish): ");
                testStringsSet[0] = transition; // If transition table isn't full and the user did not input in the transition format
                                                // Program assumes it as a test string
                for (i = 1; i < testStringsSet.length; i++)
                {
                    String testString = keyboard.nextLine();
                    testString = testString.replaceAll("\s", ""); // Removes spaces, if there exists any
                    if (testString.equals(".....")) // Ends the list of test strings
                    {
                        testStringsSet[i] = testString;
                        break;
                    }
                    testStringsSet[i] = testString;
                }
            }
            if (i == testStringsSet.length)
            {
                System.out.println(".....");
            }
        }

        System.out.println();

        // Output
        System.out.println("number of states: " + numberOfStates);

        System.out.print("final states: ");
        FA_Machine.displayFinalStates();

        System.out.print("alphabet: ");
        FA_Machine.displayAlphabet();

        System.out.println("transitions: ");
        FA_Machine.displayTransitionTable();

        System.out.println("strings: ");
        FA_Machine.test(testStringsSet);
        System.out.println("\n.....");
        keyboard.close();
    }
}