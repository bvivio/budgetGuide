package bg;

/** An ITEM is a single purchase or deposit which has a name, a date
 *  (an integer representing the day of the month), and an amount that
 *  can be either positive or negative.
 *  @author Brodie Vivio
*/
class Item {

    /** My name. */
    private String _name;
    /** My date. */
    private int _date;
    /** My amount. */
    private double _amount;

    Item(String name, int date, double amount) {
	_name = name;
	_date = date;
	_amount = amount;
    }

    String getName() {
	return _name;
    }

    int getDate() {
	return _date;
    }

    double getAmount() {
	return _amount;
    }

}
