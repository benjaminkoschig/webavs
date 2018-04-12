package ch.globaz.orion.ws;

public abstract class AbstractResultSearch {
    private long nbMatchingRows;
    private long nbAllRows;

    public AbstractResultSearch() {
    }

    public AbstractResultSearch(long nbMatchingRows, long nbAllRows) {
        super();
        this.nbMatchingRows = nbMatchingRows;
        this.nbAllRows = nbAllRows;
    }

    public long getNbMatchingRows() {
        return nbMatchingRows;
    }

    public void setNbMatchingRows(long nbMatchingRows) {
        this.nbMatchingRows = nbMatchingRows;
    }

    public long getNbAllRows() {
        return nbAllRows;
    }

    public void setNbAllRows(long nbAllRows) {
        this.nbAllRows = nbAllRows;
    }

}
