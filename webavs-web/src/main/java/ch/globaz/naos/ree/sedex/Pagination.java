package ch.globaz.naos.ree.sedex;

public class Pagination {

    private int tailleLot;
    private int nombreLot;
    private int lotCourant;

    /**
     * @param tailleLot
     * @param nombreLot
     * @param lotCourant
     */
    public Pagination(int tailleLot, int nombreLot, int lotCourant) {
        this.tailleLot = tailleLot;
        this.nombreLot = nombreLot;
        this.lotCourant = lotCourant;
    }

    public int getTailleLot() {
        return tailleLot;
    }

    public int getNombreLot() {
        return nombreLot;
    }

    public int getLotCourant() {
        return lotCourant;
    }

    /**
     * Retourne <code>true</code> s'il n'y a qu'un seul lot
     * 
     * @return <code>true</code> s'il n'y a qu'un seul lot
     */
    public boolean hasOnlyOneLot() {
        return nombreLot == 1;
    }

    /**
     * Retourne la valeur pour le champ 'comment' en cas de partial delivery
     * 
     * @return
     */
    public String getCommentForPartialDelivery(String ourBusinessReferenceId) {
        return "Partial Delivery: " + ourBusinessReferenceId + " (" + lotCourant + "/" + nombreLot + ")";
    }
}
