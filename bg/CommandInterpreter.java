package bg;

import java.util.Scanner;
import java.io.PrintStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.lang.RuntimeException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

/** The COMMANDINTERPRETER is the object for the CLI that the user
 *  interacts with by typing valid bgSQL commands. The input and output streams
 *  are by default set to the Standard Input and the Standard Output, but
 *  there is a constructor that can be called to change the input and output
 *  streams.
 *  @author Brodie Vivio
*/
class CommandInterpreter {

    /** My input. */
    private Scanner _input;
    /** My output. */
    private PrintStream _output;
    /** True iff this session of budgetGuide should end. */
    private boolean _end;
    /** The Budget holding all of my data. */
    private Budget _budget;
    /** The set of the names of all the categories in my _budget. */
    private HashSet<String> _cats;
    /** The set of the names of all my months. */
    private HashSet<String> _monthNames;

    /** Creates a new CommandInterpreter object with an empty Budget.
     *  All input is from the Standard Input and all output is to the
     *  Standard Output. */
    CommandInterpreter() {
	this(new Scanner(System.in), System.out);
    }

    /** Creates a new CommandInterpreter object with an empty Budget.
     *  Input comes from Scanner INP and output is to the PrintStream OUT. */
    CommandInterpreter(Scanner inp, PrintStream out) {
	_input = inp;
	_output = out;
	_end = false;
	_budget = new Budget();
	_cats = new HashSet<String>();
	_monthNames = new HashSet<String>();
    }

    /** Closes the output PrintStream. */
    void close() {
	_output.close();
    }

    /** Returns true iff the session of budgetGuide should end. */
    boolean end() {
	return _end;
    }

    /** Returns my Budget object. */
    Budget getBudget() {
	return _budget;
    }

    /** Adds any new categories that have been added to any of the months
     *  in _budget to _cats. */
    private void collectCats() {
	for (Month month : _budget.getMonths()) {
	    for (String cat : month.getCats()) {
		_cats.add(cat);
	    }
	}
    }

    /** Reads and executes one statement. */
    void statement() {
	_output.print(">> ");
	String line = _input.nextLine();
	String[] coms = line.split(" ");
	switch (coms[0]) {
	case "quit":
	    quitCommand();
	    return;
	case "exit":
	    quitCommand();
	    return;
	case "load":
	    loadCommand(coms);
	    return;
	case "print":
	    printCommand(coms);
	    return;
	case "report":
	    reportCommand(coms);
	    return;
	case "save":
	    saveCommand(coms);
	    return;
	case "select":
	    selectCommand(coms, _output);
	    return;
	case "remove":
	    removeCommand(coms);
	    return;
	case "help":
	    helpCommand();
	    return;
	case "clear":
	    clearCommand();
	    return;
	default:
	    _output.printf("ERROR: unknown command%n");
	    statement();
	}
    }

    /** Prints an ending message and quits budgetGuide. */
    private void quitCommand() {
	_output.print("closing budgetGuide...");
	_end = true;
    }

    /** Prints out the list of all commands. */
    private void helpCommand() {
	try {
	    Scanner helpFile = new Scanner(new FileReader("help.txt"));
	    String line = null;
	    _output.println();
	    while (helpFile.hasNextLine()) {
		line = helpFile.nextLine();
		_output.println(line);
	    }
	    helpFile.close();
	    _output.println();
	} catch (FileNotFoundException e) {
	    _output.println("ERROR: help.txt not found");
	}
    }

    /** Restarts the program by clearing all loaded data. */
    private void clearCommand() {
	_budget = new Budget();
	_cats = new HashSet<String>();
	_monthNames = new HashSet<String>();
	_output.println("cleared all data");
    }

    /** Performs a remove command by removing a Month from the budget. */
    private void removeCommand(String[] args) {
	if (args.length != 2) {
	    _output.println("ERROR: invalid remove command");
	    return;
	}
	if (!_monthNames.contains(args[1])) {
	    _output.printf("ERROR: %s is not a loaded month%n", args[1]);
	    return;
	}
	_budget.removeMonth(_budget.getMonth(args[1]));
	_monthNames.remove(args[1]);
        _output.printf("removed %s from budget%n", args[1]);
    }

    /** Reads and executes a load command, which reads in the .bgi files
     *  FILENAMES[1...] and stores it as a Month in my Budget. */
    private void loadCommand(String[] fileNames) {
	if (fileNames.length < 2) {
	    _output.println("ERROR: invalid load command");
	}
	for (int i = 1; i < fileNames.length; i++) { 
	    try {
		Scanner in = new Scanner(new FileReader(fileNames[i]));
		Month month = processFile(in);
		if (month == null) {
		    continue;
		}
		_budget.addMonth(month);
		_output.printf("loaded %s%n", month.getName());
	    } catch (FileNotFoundException e) {
		_output.printf("ERROR: cannot find file %s%n",
			       fileNames[i]);
	    } catch (RuntimeException e) {
		_output.printf("ERROR: file %s could not be interpreted;%n       %s",
			       fileNames[i], e.getMessage());
	    }
	}
    }

    /** Reads the file IN and returns all the data as a Month object
     *  if _budget doesn't already contain a Month with this file's
     *  month name, or null otherwise. Also returns null if the file
     *  IN does not include an 'Income' category. */
    private Month processFile(Scanner in) {
	int lineNum = 1;
	String monthName;
	Month month;
	boolean containsIncome = false;
	try {
	    monthName = in.next();
	    if (_monthNames.contains(monthName)) {
		_output.printf("ERROR: budget already contains month %s%n",
			       monthName);
		return null;
	    }
	    month = new Month(monthName, in.nextInt());
	    in.nextLine();
	    lineNum++;
	    String[] cats = in.nextLine().split(", ");
	    for (String cat : cats) {
		if (cat.equals("Income")) {
		    containsIncome = true;
		}
		month.addCat(cat);
	    }
	    if (!containsIncome) {
		_output.printf("ERROR: month %s has no 'Income' category%n",
			       monthName);
		return null;
	    }
	while (in.hasNextLine()) {
	    lineNum++;
	    String cat = in.next();
	    int date = in.nextInt();
	    String name = in.next();
	    double amount = in.nextDouble();
	    month.addItem(cat, name, date, amount);
	}
	} catch (Exception e) {
	    String eMessage = String.format("line number %d has incorrect formatting%n",
					    lineNum);
	    throw new RuntimeException(eMessage);
	}
	_monthNames.add(monthName);
	return month;
    }

    /** Reads and executes a print command. */
    private void printCommand(String[] args) {
	if (args.length != 2) {
	    _output.println("ERROR: invalid print command");
	    return;
	}
	switch (args[1]) {
	case "months":
	    if (_monthNames.size() == 0) {
		_output.println("currently no loaded months");
	    } else {
		for (Month month : _budget.getMonths()) {
		    _output.println(month.getName());
		}
	    }
	    return;
	case "categories":
	    if (_monthNames.size() == 0) {
		_output.println("currently no loaded months");
	    } else {
		collectCats();
		for (String cat : _cats) {
		    _output.println(cat);
		}
	    }
	    return;
	default:
	    _output.println("ERROR: invalid print command");
	    return;
	}
    }

    /** Reads and executes a report command. */
    private void reportCommand(String[] coms) {
	if (coms.length != 2) {
	    _output.println("ERROR: invalid report command");
	    return;
	}
	if (coms[1].equals("budget")) {
	    reportBudget(_output);
	    return;
	} else if (_monthNames.contains(coms[1])) {
	    reportMonth(coms[1], _output);
	    return;
	}
	collectCats();
	if (_cats.contains(coms[1])) {
	    reportCat(coms[1], _output);
	    return;
	}
	_output.println("ERROR: invalid report command");
	return;
    }

    /** Reports on the entire budget, outputting to OUTPUT. */
    private void reportBudget(PrintStream output) {
	output.println();
	double res = _budget.getTotal();
	if (res < 0) {
	    output.printf("  Your budget total is: -$%.2f%n", -res);
	} else {
	    output.printf("  Your budget total is: $%.2f%n", res);
	}
	double totInc = _budget.getTotal("Income");
	output.printf("  Your total income is: $%.2f%n", totInc);
	double expend = res - totInc;
	if (expend < 0) {
	    output.printf("  Your total expenditures amounted to: $%.2f%n", -expend);
	} else {
	    output.printf("  Your total expenditures amounted to: $%.2f%n", expend);
	}
	reportMonthlyTotals(output);
	output.println();
	for (Month month : _budget.getMonths()) {
	    double dailyLoss = month.getLosses() / month.getDays();
	    double inc = month.getTotal("Income");
	    double dailyInc = inc / month.getDays();
	    double dailyTot = dailyInc - dailyLoss;
	    output.printf("   -Each day in %s you spent $%.2f ",
			      month.getName(), dailyLoss);
	    output.printf("and earned $%.2f,%n", dailyInc);
	    if (dailyTot < 0) {
		output.printf("    which is a daily net total of -$%.2f.%n",
				  -dailyTot);
	    } else {
		output.printf("    which is a daily net total of $%.2f.%n",
				  dailyTot);
	    }
	    if (inc > 0) {
		for (String cat : month.getCats()) {
		    if (!cat.equals("Income")) {
			double tot = month.getTotal(cat);
			if (tot < 0) {
			    double perc = -100 * tot / inc;
			    output.printf("      %.0f", perc);
			    output.print("% of your income was spent on ");
			    output.printf("%s.%n", cat);
			}
		    }
		}
		if (month.getTotal() > 0) {
		    double extra = 100 * month.getTotal() / inc;
		    output.printf("      %.0f", extra);
		    output.println("% of your income was unused.");
		}
	    }
	    output.println();
	}
    }

    /** Outputs the names of all the Months in _budget with their
     *  respective totals and 'Income' totals to OUTPUT. */
    private void reportMonthlyTotals(PrintStream output) {
	if (_budget.getMonths().size() > 0) {
	    output.println();
	    for (Month month : _budget.getMonths()) {
		String name = month.getName();
		double total = month.getTotal();
		double income = month.getTotal("Income");
		double expend = total - income;
		if (total < 0) {
		    output.printf("  %s total: -$%.2f%n", name, -total);
		} else {
		    output.printf("  %s total: $%.2f%n", name, total);
		}
		output.printf("   *Income: $%.2f%n", income);
		if (expend < 0) {
		    output.printf("   *Expenditures: $%.2f%n%n", -expend);
		} else {
		    output.printf("   *Expenditures: $%.2f%n%n", expend);
		}
	    }
	}
    }


    /** Reports only on Month MONTH, which must be included in _budget.
     *  Output goes to OUTPUT. */
    private void reportMonth(String month, PrintStream output) {
        output.println();
	Month m  = _budget.getMonth(month);
	double tot = m.getTotal();
	if (tot < 0) {
	    output.printf("  Your total for %s is: -$%.2f%n", month, -tot);
	} else {
	    output.printf("  Your total for %s is: $%.2f%n", month, tot);
	}
	double inc = m.getTotal("Income");
	output.printf("  Your total income for %s is: $%.2f%n", month, inc);
	double expend = tot - inc;
	if (expend < 0) {
	    output.printf("  Your total expenditures for %s amounted to: $%.2f%n%n", month, -expend);
	} else {
	    output.printf("  Your total expenditures for %s amounted to: $%.2f%n%n", month, expend);
	}
	for (String cat : m.getCats()) {
	    if (cat.equals("Income")) {
		continue;
	    }
	    double amount = -m.getTotal(cat);
	    if (amount == 0) {
		continue;
	    }
	    double perc = 100 * amount / inc;
	    if (inc == 0) {
		output.printf("   -You spent $%.2f on %s.%n", amount, cat);
	    } else {
		output.printf("   -You spent $%.2f on %s, which is %.0f",
			      amount, cat, perc);
		output.println("% of your income.");
	    }
	}
	output.println();
	double dailyLoss = m.getLosses() / m.getDays();
	double dailyInc = inc / m.getDays();
	double dailyTot = dailyInc - dailyLoss;
	output.printf("   -Each day in %s you spent $%.2f ",
		      month, dailyLoss);
	output.printf("and earned $%.2f,%n", dailyInc);
	if (dailyTot < 0) {
	    output.printf("    which is a daily net total of -$%.2f.%n",
			  -dailyTot);
	} else {
	    output.printf("    which is a daily net total of $%.2f.%n",
			  dailyTot);
	}
	output.println();
    }

    /** Reports only on category CAT, which must be included in _cats.
     *  Output goes to OUTPUT. */
    private void reportCat(String cat, PrintStream output) {
	if (cat.equals("Income")) {
	    reportIncome(output);
	    return;
	}
	output.println();
	double total = 0;
	double income = 0;
	for (Month month : _budget.getMonths()) {
	    if (month.containsCat(cat)) {
		double monthTot = month.getTotal(cat);
		double monthInc = month.getTotal("Income");
		double monthPerc = 100 * monthTot / monthInc;
		income += monthInc;
		if (monthTot < 0) {
		    total -= monthTot;
		    output.printf("  -In %s you spent $%.2f on %s",
				  month.getName(), -monthTot, cat);
		} else {
		    total += monthTot;
		    output.printf("  -In %s you spent $%.2f on %s",
				  month.getName(), monthTot, cat);
		}
		if (monthInc > 0 && monthTot < 0) {
		    output.printf("%n   which was %.0f", -monthPerc);
		    output.print("% of your income that month");
		} else if (monthInc > 0) {
		    output.printf("%n   which was %.0f", monthPerc);
		    output.print("% of your income that month");
		}
		output.printf("%n%n");
	    }
	}
	double percent = 100 * total / income;
	if (income > 0) {
	    output.printf("  * Overall you spent $%.2f on %s,%n    using %.0f",
			  total, cat, percent);
	    output.print("% of your total income.");
	} else {
	    output.printf("  * Overall you spent $%.2f on %s.",
			  total, cat);
	}
	output.printf("%n%n");
    }

    /** Reports on Income only to PrintStream OUTPUT. */
    private void reportIncome(PrintStream output) {
	output.println();
	double income = 0;
	double losses = 0;
	for (Month month : _budget.getMonths()) {
	    double monthInc = month.getTotal("Income");
	    double monthLoss = month.getLosses();
	    income += monthInc;
	    losses += monthLoss;
	    output.printf("  In %s you made $%.2f%n",
			  month.getName(), monthInc);
	    if (monthInc > 0) {
		double perc = 100 * monthLoss / monthInc;
		output.printf("   -You spent $%.2f, which was %.0f",
			      monthLoss, perc);
		output.println("% of your income");
	    } else {
		output.printf("   -You spent $%.2f%n", monthLoss);
	    }
	}
	output.printf("%n  Overall you made $%.2f%n", income);
	if (income > 0 && losses > 0) {
	    output.printf("   -Overall you spent $%.2f,%n    which was %.0f",
			  losses, 100 * losses / income);
	    output.print("% of your total income.");
	    output.printf("%n%n");
	} else {
	    output.printf("   -Overall you spent $%.2f,%n%n", losses);
	}
    }

    /** Reads and executes a save command. */
    private void saveCommand(String[] args) {
	if (_monthNames.size() == 0) {
	    _output.println("currently no loaded months");
	    return;
	}
	if (args[1].equals("select")) {
	    saveSelectCommand(args);
	    return;
	}
	if (args.length != 4 || !args[2].equals("as")) {
	    _output.println("ERROR: invalid save command");
	    return;
	}
	collectCats();
	PrintStream out = null;
	try {
	    if (args[1].equals("budget")) {
		out = new PrintStream(args[3] + ".txt");
		reportBudget(out);
		_output.printf("saved budget report as %s.txt%n", args[3]);
	    } else if (_monthNames.contains(args[1])) {
		out = new PrintStream(args[3] + ".txt");
		reportMonth(args[1], out);
		_output.printf("saved %s report as %s.txt%n",
			       args[1], args[3]);
	    } else if (args[1].equals("Income")) {
		out = new PrintStream(args[3] + ".txt");
		reportIncome(out);
		_output.printf("saved Income report as %s.txt%n", args[3]);
	    } else if (_cats.contains(args[1])) {
		out = new PrintStream(args[3] + ".txt");
		reportCat(args[1], out);
		_output.printf("saved %s report as %s.txt%n",
			       args[1], args[3]);
	    } else {
		_output.printf("ERROR: cannot report on %s%n", args[1]);
	    }
	} catch (FileNotFoundException e) {
	    _output.printf("ERROR: trouble writing to %s.txt%n", args[3]);
	} finally {
	    if (out != null) {
		out.close();
	    }
	}
    }

    /** Performs a save select command in which the results of a select query
	are saved as a text file. */
    private void saveSelectCommand(String[] args) {
        if (!args[args.length - 2].equals("as")) {
	    _output.printf("ERROR: invalid save select command%n");
	    return;
	}
	PrintStream out = null;
	try {
	    out = new PrintStream(args[args.length - 1] + ".txt");
	    selectCommand(Arrays.copyOfRange(args, 1, args.length - 2), out);
	    _output.printf("saved query as %s.txt%n", args[args.length - 1]);
	} catch (FileNotFoundException e) {
	    _output.printf("ERROR: trouble writing to %s.txt%n", args[args.length - 1]);
	} finally {
	    if (out != null) {
		out.close();
	    }
	}
    }

    /** Performs a select operation by only outputting the months
	that meet the given conditions.*/
    private void selectCommand(String[] args, PrintStream output) {
	if (_monthNames.size() == 0) {
	    _output.println("currently no loaded months");
	    return;
	}
	if (!(args[1].equals("months") && args[2].equals("where"))) {
	    _output.println("ERROR: invalid select command");
	    return;
	}
	ArrayList<Condition> conds = parseConditions(Arrays.copyOfRange(args, 3,
									args.length));
	if (conds == null) {
	    return;
	}
	ArrayList<Pair<String, ArrayList<Pair<String, Double>>>> dataList =
	    Condition.filter(conds, _budget);
	output.printf("query results:%n%n");
	for (Pair item : dataList) {
	    output.printf("  %s:%n", ((Month)item.getLeft()).getName());
	    for (Pair result : (ArrayList<Pair<String, Double>>)item.getRight()) {
		double catVal = (double)result.getRight();
		if (catVal < 0) {
		    output.printf("   -%s: -$%.2f%n", result.getLeft(), -catVal);
		} else {
		    output.printf("   -%s: $%.2f%n", result.getLeft(), catVal);
		}
	    }
	    output.println();
	}
    }

    /** Parses input array ARGS as a list of condition statements and returns a list
     *  of corresponding Condition objects. Returns NULL if any error occurs. */
    ArrayList<Condition> parseConditions(String[] args) {
	collectCats();
	if ((args.length % 4) != 3) {
	    _output.println("ERROR: invalid select command");
	    return null;
	}
	ArrayList<Condition> conds = new ArrayList<Condition>();
	for (int i = 0; i < args.length; i += 4) {
	    if (!(_cats.contains(args[i]) || args[i].equals("Total") ||
		  args[i].equals("Expenditures"))) {
		_output.printf("ERROR: %s is not a loaded category%n", args[i]);
		return null;
	    }
	    if (!(args[i+1].equals("==") || args[i+1].equals("!=") || args[i+1].equals("<")
		  || args[i+1].equals("<=") || args[i+1].equals(">=") ||
		  args[i+1].equals(">"))) {
		_output.printf("ERROR: %s is not a valid comparator%n", args[i+1]);
		return null;
	    }
	    double value;
	    try {
		value = Double.parseDouble(args[i+2]);
	    } catch (Exception e) {
		_output.printf("ERROR: %s is an invalid value%n", args[i+2]);
		return null;
	    }
	    conds.add(new Condition(args[i], new DoubleComparator(args[i+1]), value));
	    if ((i + 3) == args.length) {
		break;
	    } else if (!args[i+3].equals("and")) {
		_output.println("ERROR: invalid select command");
		return null;
	    }
	}
	return conds;
    }
}
