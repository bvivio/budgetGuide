package bg;

/** A PAIR represents a 2-tuple of data of generic types.
 *  @author Brodie Vivio
 */
class Pair<L, R> {

    /** My first/left value. */
    private L _left;
    /** My second/right value. */
    private R _right;

    Pair(L leftVal, R rightVal) {
	_left = leftVal;
	_right = rightVal;
    }

    L getLeft() {
	return _left;
    }

    R getRight() {
	return _right;
    }
}
