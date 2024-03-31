// Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Simple program that interprets finite state machines and executes them 
 *
 * Ruaraidh Nicolson, https://rdn1.host.cs.st-andrews.ac.uk/
 *
 * September 2021
 * 
 */
public class fsminterpreter{

    //Attributes
    private static String fsmFile;  //The string path of the file of the FSM
    private static String testFile; //The string path of the file with the test inputs in them
    private static ArrayList<State> fsm = new ArrayList<State>();   //The array list of states that make up the FSM
    private static Boolean testFileExists;  //This boolean checks if the input is being done by a test file or not

    /**
     * MAin method that reads in and runs the FSM
     * @param args the arguments passed in by the command line
     */
    public static void main(String[] args) {

        //Checks if theres an incorrect number of arguments
        if (args.length > 2 && args.length < 1) { 
            //Display error message and quit the program
            System.out.println("\n Incorrect Argument Passed. Correct Formats: java fsminterpreter <Finite state machine file path> <test.txt>\n");
            System.out.println("\n java fsminterpreter <Finite state machine file path> \n");
            System.out.println("\n java fsminterpreter <Finite state machine file path> <test.txt> \n");
            System.exit(0);
        }

        fsmFile = args[0];
        //If there was only one argument i.e. no test file given
        if(args.length == 1){
            //There is no test file 
            testFileExists = false;
        }

        else{

            //Store the path of the test file
            testFile = args[1];
            testFileExists = true;
        }

        //Read in the FSM from the file and store the states in an array list
        fsm = readFSM(fsmFile);

        //Run the FSM
        runFSM(fsm);

    }

    /**
     * Reads in and interprets the input of a file translating it into states
     * @param fsmName, the file name of the FSM 
     * @return ArrayList<State> FSM, the states that make up the FSM
     */
    public static ArrayList<State> readFSM(String fsmName){

        //Instantiate the array list that'll be returned
        ArrayList<State> fsm = new ArrayList<State>();

        //Create a scanner object
        Scanner fsmScanner;

        try {
        
            //Try to read in the file
            File fsmFile = new File(fsmName);
            fsmScanner = new Scanner(fsmFile);

            //Set the index and the previous state to -1
            int previousState = -1;
            int index = -1;

            //Loop through each line of the file
            while(fsmScanner.hasNext()){

                //Split the current line into a string array seperate by a space
                String[] currentTuple = fsmScanner.nextLine().split(" ");

                //If the input is not formatted correctly, display a bad description error message and quit the program 
                if(currentTuple.length != 4 || fsmFile.length() == 0){
                    System.out.println("Bad description");
                    System.exit(0);
                }

                //Instantiate all the information from the tuple into variables for readability
                int stateNo = Integer.parseInt(currentTuple[0]);
                String newInput = currentTuple[1];
                String output = currentTuple[2];
                int nextState = Integer.parseInt(currentTuple[3]);

                //If the state no is new 
                if(previousState != stateNo){

                    //Add to the index
                    index++;

                    //Create a new state objects and add it to the FSM
                    State newstate = new State(stateNo);
                    fsm.add(newstate);
                
                    //Get the new state object just created and add the current input to that state
                    fsm.get(index).addToState(newInput, output, nextState);
                
                    //Set the previous state to the current state number
                    previousState = stateNo;
                    
                }

                //Otherwise just add the inputs to the current state
                else{
                    fsm.get(index).addToState(newInput, output, nextState);
                }  
            }
        }
        
        //If the file isnt found print an error message
        catch (FileNotFoundException e) {
            System.err.println("File Not Found Exception: " + e.getMessage());
        } 


        //Return the array list created
        return fsm;
    }


    /**
     * This runs through a given input and outputs the appropriate thing according to the FSM
     * @param fsm, the FSM created earlier in the program
     */
    public static void runFSM(ArrayList<State> fsm){

        
        String[] userInput; //A string array of the user input
        Scanner userScanner;    //A scanner object to read in the user input either from a file or the command line
        int initialStateIndex = 0; //Just the index of the initial state
        int nextStateNo = 1;    //The state number that will be next
        State currentState = fsm.get(initialStateIndex);    //The state the FSM is in currently

        //If the FSM is using input from a test file 
        if(testFileExists){

            
            try {
                //Scan in the input from the file and split the input into a string array
                userScanner = new Scanner(new File(testFile));
                userInput = userScanner.nextLine().split("");

                //Loop through each input from the user
                for(int i = 0; i < userInput.length; i++){
        
                    //Find the index of the where the input among the accepted inputs
                    int indexOfMatch = checkInput(currentState, userInput[i]);
                    
                    //A string of the input thats being used
                    String matchedInput;
    
                    //If the index is negative i.e. the method didnt find the user input among the accepted input
                    if(indexOfMatch < 0){
                        //Make the input an empty string
                        matchedInput = "";  
                    }

                    //Otherwise set the matched input to the accepted input that was found 
                    else{
                        matchedInput = currentState.getAcceptedInputs().get(indexOfMatch);
                    }
                    
                    //If the user input wasn't among the accepted input
                    if (!matchedInput.equals(userInput[i])){
                        
                        //Display bad input and exit the program
                        System.out.println("Bad input");
                        System.exit(0);
                    }
                    
                    //Otherwise
                    else{
                        
                        //Find the matching output and display it
                        String output = currentState.getOutputs().get(indexOfMatch);
                        System.out.print(output);

                        //Find the transition state
                        nextStateNo = currentState.getNextState().get(indexOfMatch);
        
                    }
    
                    //Get the next state
                    currentState = findNextState(fsm, currentState, nextStateNo);
                }

            } 
            
            //Catch a file not found exception
            catch (FileNotFoundException e) {
                System.err.println("File Not Found Exception: " + e.getMessage());
            } 
        }

        //Otherwise the input is from the command line
        else{

            //Instantiate the scanner to read in from the command line
            userScanner = new Scanner(System.in);

            //This is the same as the code above 

            userInput = userScanner.nextLine().split("");

            
            for(int i = 0; i < userInput.length; i++){

                int indexOfMatch = checkInput(currentState, userInput[i]);
                String matchedInput;


                if(indexOfMatch < 0){
                  matchedInput = "";  
                }
                else{
                    matchedInput = currentState.getAcceptedInputs().get(indexOfMatch);
                }
                

                if (!matchedInput.equals(userInput[i])){
                    System.out.println("Bad input");
                    System.exit(0);
                }
                else{
                    
                    String output = currentState.getOutputs().get(indexOfMatch);
                    System.out.print(output);
                    nextStateNo = currentState.getNextState().get(indexOfMatch);
    
                }

                currentState = findNextState(fsm, currentState, nextStateNo);
            }
        }
    }

    /**
     * Check to see if the user input is among the accepted input of the given state
     * @param state, the state the FSM is in currently
     * @param input, The user input
     * @return int, the index of the the matching input
     */
    public static int checkInput(State state, String input){
        
        //Set the index of the match to negative (so if it doesnt find the input, the above method can interpret it as not being found)
        int indexOfMatch = -1; 

        //Declare an array list of strings from the states list of accepted inputs
        ArrayList<String> acceptedInputs = state.getAcceptedInputs();

        //Loop through the accpeted inputs
        for(int i = 0; i < acceptedInputs.size(); i++){

            //If the current input eqauls the user input, 
            if(acceptedInputs.get(i).equals(input)){
                
                //Set the current index of the loop to the matching index and return it
                indexOfMatch = i;
                return indexOfMatch;
            }

        }

        return indexOfMatch;
    }

    /**
     * Finds the next state being transitioned to
     * @param fsm, the states it vould move to
     * @param currentState, the current state
     * @param nextStateNo, the next state being moved to
     * @return State, the next state being moved too
     */
    public static State findNextState(ArrayList<State> fsm, State currentState, int nextStateNo){
        
        //Looop througha ll the states
        for(int i = 0; i < fsm.size(); i++){

            //If the curren states number is the next state
            if(fsm.get(i).getStateNo() == nextStateNo){
                
                //Set the current state to the found state and return it
                currentState = fsm.get(i);
                
                return currentState;
            }
        }
        return currentState;
    }



}