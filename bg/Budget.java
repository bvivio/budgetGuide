package bg;

import java.util.ArrayList;
import java.util.List;

/** A BUDGET represents a collection of MONTH objects, which each represent
 *  one .bgi file. So a BUDGET holds all of the current .bgi files loaded into
 *  the program and can thus be used to calulate totals and such.
 *  @author Brodie Vivio
*/
class Budget {

    /** The list of my Months. */
    private ArrayList<Month> _months;

    /** Creates a new empty Budget. */
    Budget() {
	_months = new ArrayList<Month>();
    }

    /** Adds Month MONTH to my list of Months. */
    void addMonth(Month month) {
	_months.add(month);
    }

    /** Removes Month MONTH from my list of Months. */
    void removeMonth(Month month) {
	_months.remove(month);
    }

    /** Returns my list of months. */
    List<Month> getMonths() {
	return _months;
    }

    /** Returns the total for the entire budget. */
    double getTotal() {
	double total = 0;
	for (Month month : _months) {
	    total += month.getTotal();
	}
	return total;
    }

    /** Returns the Month object with name NAME if it is in my month list
     *  or null otherwise. */
    Month getMonth(String name) {
	for (Month month: _months) {
	    if (month.getName().equals(name)) {
		return month;
	    }
	}
	return null;
    }

    /** Returns the total for the Category CAT. */
    double getTotal(String cat) {
	double total = 0;
	for (Month month : _months) {
	    if (month.getCats().contains(cat)) {
		total += month.getTotal(cat);
	    }
	}
	return total;
    }

}
