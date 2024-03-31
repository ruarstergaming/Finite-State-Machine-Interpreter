//Imports
import java.util.ArrayList;

/**
 * Simple class that stores the details of states 
 *
 * Ruaraidh Nicolson, https://rdn1.host.cs.st-andrews.ac.uk/
 *
 * September 2021
 * 
 */
public class State {
    
    //Attributes
    public int stateNo; //The number of the state
    public ArrayList<String> acceptedInputs = new ArrayList<String>();  //A list of the possible accepted inputs
    public ArrayList<String> outputs = new ArrayList<String>(); //A list of outputs depending on the inputs
    public ArrayList<Integer> nextState = new ArrayList<Integer>(); //A list of the transition states

    /**
     * Basic constructor that instantiates the object with a state number
     * @param stateNo
     */
    public State(int stateNo){  

        this.stateNo = stateNo;

    }

    /**
     * A simple constuctor in case a state is instantiated with all of its inputs, outputs and transition states
     * @param stateNo
     * @param acceptedInputs
     * @param outputs
     * @param nextState
     */
    public State(int stateNo, ArrayList<String> acceptedInputs, ArrayList<String> outputs,  ArrayList<Integer> nextState){
        
        this.stateNo = stateNo;
        this.acceptedInputs = acceptedInputs;
        this.outputs = outputs;
        this.nextState = nextState;

    }

    /**
     * This takes in an input, output and transition state and adds them to their respective array lists
     * @param newInput
     * @param newOutput
     * @param nextState
     */
    public void addToState(String newInput, String newOutput, int nextState){

        this.acceptedInputs.add(newInput);
        this.outputs.add(newOutput);
        this.nextState.add(nextState);
    }


    //Basic Getter methods for the attributes
    public int getStateNo() {
        return stateNo;
    }
    
    public ArrayList<String> getAcceptedInputs() {
        return acceptedInputs;
    }

    public ArrayList<String> getOutputs() {
        return outputs;
    }

    public ArrayList<Integer> getNextState() {
        return nextState;
    }


}