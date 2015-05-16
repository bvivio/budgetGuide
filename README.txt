budgetGuide Application Specifications and User Guide:

______________________________________________________________________________
***INTRO***
______________________________________________________________________________

budgetGuide is an application that compiles budgeting information from
input .bgi files (the specifications for .bgi files are listed below) and
allows users to retreive information about many aspects of the budget via a
command line interface using a simple query language called bgSQL (the
specifications of which are listed below as well). Also, users can export
the summarized data as .txt files in order to save the information obtained
from the application's data processing.

The program can be initiated by navigating to the directory entitled "bgproj" in
the command line. This folder contains a folder titled "bg" holding all of the
.class files of the application (Main.class, CommandInterpreter.class, Budget.class,
Month.class,and Item.class), among other things. To begin running the application,
simply type into the terminal:

java bg.Main

Hit enter and the program should begin with a print statement welcoming you to a
specific version of the application.

______________________________________________________________________________
***FORMAT OF .bgi FILES***
______________________________________________________________________________

The input files for the budgetGuide application are expected to be complete
budget reports of specific time intervals (the expected interval is one month).
Each .bgi file has three main parts: a name and number of days that the .bgi
file has budgeting information about, a list of category names (called
CATEGORIES) which describe the sources of funds and spending, and a list of
purchases/deposits (called ITEMS). budgetGuide defines NAME as follows:

NAME: a string containing only letters, numbers, and symbols. No spaces are
      allowed in a valid NAME (underscores can be used to create a NAME with
      more than one "word").

The first line of a .bgi file must conatin a NAME (the name of the given time
period; often the name of the month/s is appropriate) and an integer
representing the number of days that the .bgi file has budgeting information
about, separated by a single space. For example, line 1 of a valid .bgi file
could read as follows:

March 31

The second line of a .bgi file must conatin a list of NAMES to be used as the
file's CATEGORIES of income/expenditures separated by a comma and a single
space. Every .bgi file must contain "Income" as one of its CATEGORIES. 
For example, line 2 of a valid .bgi file could read as follows:

Income, Food, Luxury_Purchases, Necessary_Expenses

The remainder of the .bgi file should be a list of ITEMS, one ITEM per line.
An ITEM consists of four parts, each separated by a single space. An ITEM must
first list a given CATEGORY from the .bgi file's list of CATEGORIES on line 2.
Then, the ITEM must list an integer representing the day number of the budgeting
period that the ITEM occured on. Then the ITEM must list a NAME for the ITEM.
Lastly, the ITEM must list a floating point or integer value for the ITEM
representing the amount of money lost or gained (negative values are accepted
and are to be expected for purchases). For example, a valid ITEM could read as
follows:

Necessary_Expenses 4 Weekly_load_of_laundry -3.75

______________________________________________________________________________
***USER INTERFACE AND SIMPLE QUERY LANGUAGE (bgSQL)***
______________________________________________________________________________

Upon starting the program, the user must input a series of commands to load
the desired data (in .bgi files), receive summaries of the compiled data, and
export the reports as text files. The details of this simple language of
commands (bgSQL) is as follows. First the syntax of the command is listed, and then
a description of the command's function is given.

  	 __________________________________________
  	 COMMAND     	       	     |  DESCRIPTION
	 __________________________________________

	 load	<filename>+	     :	loads the .bgi file(s) into the budget

	 report	<month NAME>	     :	reports on a specific month (.bgi file)
	 	       			that has previously been loaded
		<CATEGORY>	     :	reports on a specific CATEGORY for all
					months that have been loaded
		budget		     :	reports on every month in the budget

	 save <month Name> as <NAME> :	saves the report of a specific month
	     	    	     		as the text file called <NAME>.txt
	      <CATEGORY> as <NAME>   :	saves the report of a specific CATEGORY
	       		     		as the text file called <NAME>.txt
	      budget as <NAME>       :	saves the report of the entire budget
	      	     		    	as the text file called <NAME>.txt

	 print	months		     :  prints out the names of the .bgi files
	 			    	that have been loaded into the budget
		categories           : 	prints out all of the CATEGORIES
				    	currently loaded inot the budget from
					the loaded .bgi files

	 clear		             :  restarts the state of the program by
	 			     	clearing all the data currently loaded
					into the budget

	 exit			     :	exits the budgetGuide program; doesn't
	 			    	save the state of the budget

	 quit			     :	same as exit command

	 help			     :	prints out this list of commands
