package bg;

import java.util.Scanner;

public class Main {

    private static final String VERSION = "1.2";

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
