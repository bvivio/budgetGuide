package bg;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

class Month {

    /** My name. */
    private final String _name;
    /** The number of days in me. */
    private final int _days;
    /** My total amount. */
    private double _monTot = Double.MIN_VALUE;
    /** My map from names of budget categories to lists of their data. */
    private HashMap<String, ArrayList<Item>> _data;
    /** My map from names of budget categories to their total amount. */
    private HashMap<String, Double> _totals;

    /** Creates a new Month with name NAME and 30 days. */
    Month(String name) {
	_name = name;
	_days = 30;
	_data = new HashMap<String, ArrayList<Item>>();
	_totals = new HashMap<String, Double>();
    }

    /** Creates a new Month with name NAME and DAYS days. */
    Month(String name, int days) {
	_name = name;
	_days = days;
	_data = new HashMap<String, ArrayList<Item>>();
	_totals = new HashMap<String, Double>();
    }

    /** Returns my name. */
    String getName() {
	return _name;
    }

    /** Returns my number of days. */
    int getDays() {
	return _days;
    }

    /** Adds a category with name NAME. */
    void addCat(String name) {
	_data.put(name, new ArrayList<Item>());
    }

    /** Returns true if this month contains category CAT. */
    boolean containsCat(String cat) {
	return _data.containsKey(cat);
    }

    /** Adds Item ITEM to my category CAT. CAT must already be one of
     *  my categories. */
    void addItem(String cat, Item item) {
	assert _data.containsKey(cat);
	_data.get(cat).add(item);
    }

    /** Adds a new Item with name NAME, date DATE, and amount AMOUNT
     *  to my category CAT. CAT must already be one of my categories. */
    void addItem(String cat, String name, int date, double amount) {
	assert _data.containsKey(cat);
	_data.get(cat).add(new Item(name, date, amount));
    }

    /** Returns a list of all my category names. */
    List<String> getCats() {
	ArrayList<String> list = new ArrayList<String>();
	for (String cat : _data.keySet()) {
	    list.add(cat);
	}
	return list;
    }


    /** Prints out the names of all of my categories on one line. */
    void printCats() {
	for (String cat : _data.keySet()) {
	    System.out.print(cat + "   ");
	}
	System.out.println();
    }

    /** Returns the total amount of category CAT and adds this
     *  value to the _totals map if not already present. */
    double getTotal(String cat) {
	assert _data.containsKey(cat);
	if (_totals.containsKey(cat)) {
	    return _totals.get(cat);
	}
	double total = 0;
	for (Item item : _data.get(cat)) {
	    total += item.getAmount();
	}
	_totals.put(cat, total);
	return total;
    }

    /** Returns the total amount for the entire month and sets
     *  _monTot to this value if not already set. */
    double getTotal() {
	if (_monTot != Double.MIN_VALUE) {
	    return _monTot;
	}
	double total = 0;
	for (String cat : _data.keySet()) {
	    total += getTotal(cat);
	}
	_monTot = total;
	return total;
    }

    /** Returns the amount of money spent this month (as a positive value). */
    double getLosses() {
	return -(getTotal() - getTotal("Income"));
    }

}
