package bg;

/** A DOUBLECOMPARATOR simply represents a binary function on
 *  to doubles using one of the standard comparators.
 *  @author Brodie Vivio
 */
class DoubleComparator {

    /** The string representation of my compare function. */
    String _compareString;

    DoubleComparator(String compareString) {
	_compareString = compareString;
    }

    boolean compare(double val1, double val2) {
	switch (_compareString) {
	case "==":
	    return val1 == val2;
	case "!=":
	    return val1 != val2;
	case "<":
	    return val1 < val2;
	case "<=":
	    return val1 <= val2;
	case ">=":
	    return val1 >= val2;
	case ">":
	    return val1 > val2;
	default:
	    return false;
	}
    }
}
