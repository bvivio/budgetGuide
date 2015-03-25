package bg;

import java.util.ArrayList;
import java.util.List;

class Budget {

    /** The list of my Months. */
    private ArrayList<Month> _months;

    /** Creates a new empty Budget. */
    Budget() {
	_months = new ArrayList<Month>();
    }

    /** Adds Month MONTH to my list of months. */
    void addMonth(Month month) {
	_months.add(month);
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
