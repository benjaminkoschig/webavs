package ch.globaz.vulpecula.repositoriesjade;

public class PaginationContainer {

    private int definedSearchSize;
    private boolean hasMoreElements = false;
    private int nbOfResultMatchingQuery = 0;
    private String orderKey = "";

    public PaginationContainer(int definedSearchSize, boolean hasMoreElements, int nbOfResultMatchingQuery, int offset,
            String orderKey) {
        super();
        isNull(definedSearchSize, "definedSearchSize");
        isNull(hasMoreElements, "hasMoreElements");
        isNull(nbOfResultMatchingQuery, "nbOfResultMatchingQuery");
        isNull(offset, "offset");
        this.definedSearchSize = definedSearchSize;
        this.hasMoreElements = hasMoreElements;
        this.nbOfResultMatchingQuery = nbOfResultMatchingQuery;
        this.offset = offset;
        this.orderKey = orderKey;
    }

    private void isNull(Object value, String name) {
        if (value == null) {
            throw new java.lang.IllegalArgumentException("The " + name + " cannot be null ");
        }
    }

    @Override
    public String toString() {
        return "PaginationContainer [definedSearchSize=" + definedSearchSize + ", hasMoreElements=" + hasMoreElements
                + ", nbOfResultMatchingQuery=" + nbOfResultMatchingQuery + ", offset=" + offset + ", orderKey="
                + orderKey + "]";
    }

    private int offset = 0;

    public int getDefinedSearchSize() {
        return definedSearchSize;
    }

    public boolean isHasMoreElements() {
        return hasMoreElements;
    }

    public void setHasMoreElements(boolean hasMoreElements) {
        this.hasMoreElements = hasMoreElements;
    }

    public int getNbOfResultMatchingQuery() {
        return nbOfResultMatchingQuery;
    }

    public int getOffset() {
        return offset;
    }

    public void setNbOfResultMatchingQuery(int nbOfResultMatchingQuery) {
        this.nbOfResultMatchingQuery = nbOfResultMatchingQuery;
    }

    public boolean hasNbOfResultMatchingQuery() {
        return nbOfResultMatchingQuery != 0;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
}
