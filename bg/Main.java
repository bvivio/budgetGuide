package bg;

import java.util.Scanner;

/** MAIN is simply the starting point of the program to initiate the
 *  COMMANDINTERPRETER which takes over until the program is exited.
 *  @author Brodie Vivio
*/
public class Main {

    private static final String VERSION = "3.6";

    public static void main(String[] args) {
	System.out.printf("Welcome to budgetGuide version %s!%n", VERSION);
	CommandInterpreter interpreter = new CommandInterpreter();
	while (!interpreter.end()) {
	    interpreter.statement();
	}
	interpreter.close();
	System.exit(0);
    }

}
