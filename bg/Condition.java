package bg;

import java.util.List;
import java.util.ArrayList;

/** A CONDITION represents  condition statement which is a statement that
 *  is either true or false. A CONDITION simply tests a value from
 *  a month (like the total for a certain category) against another value
 *  in the form of a double that is input by the user.
 *  @author Brodie Vivio
 */
class Condition {

    /** My category. */
    private String _cat;
    /** My comparator. */
    private DoubleComparator _comparator;
    /** My value to compare against. */
    private double _value;

    Condition(String cat, DoubleComparator comp,
	      double val) {
	_cat = cat;
	_comparator = comp;
	_value = val;
    }

    String getCat() {
	return _cat;
    }

    DoubleComparator getComp() {
	return _comparator;
    }

    double getValue() {
	return _value;
    }

    static ArrayList<Pair<String, ArrayList<Pair<String, Double>>>> filter(List<Condition> conds,
									   Budget budget) {
	ArrayList<Pair<String, ArrayList<Pair<String, Double>>>> retList =
	    new ArrayList<Pair<String, ArrayList<Pair<String, Double>>>>();
	for (Month month : budget.getMonths()) {
	    ArrayList<Pair<String, Double>> monthData = new ArrayList<Pair<String, Double>>();
	    boolean monthPass = true;
	    
	    for (Condition cond : conds) {
		double monthVal;
		if (cond.getCat().equals("Total")) {
		    monthVal = month.getTotal();
		} else if (cond.getCat().equals("Expenditures")) {
		    monthVal = month.getLosses();
		} else {
		    monthVal = month.getTotal(cond.getCat());
		}
		if (cond.getComp().compare(monthVal, cond.getValue())) {
		    monthData.add(new Pair(cond.getCat(), monthVal));
		} else {
		    monthPass = false;
		    break;
		}
	    }
	    if (monthPass) {
		retList.add(new Pair(month, monthData));
	    }
	}
	return retList;
    }
}
