package athena.groups.paging;

/**
 * Abstract class providing paging support
 */
public abstract class Pageable {

    /**
     * The paging
     */
    private Paging paging;

    /**
     * @return count of groups.
     */
    public int count() {
        return paging.count;
    }

    /**
     * @return start index?
     */
    public int start() {
        return paging.start;
    }

    /**
     * @return total number
     */
    public int total() {
        return paging.total;
    }

    private static final class Paging {

        /**
         * Count
         * Starting point
         * Total
         */
        private int count, start, total;

    }

}
