package globaz.helios.db.comptes;

public class CGEcritureMultiSocieteViewBean extends CGEnteteEcritureViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPeriodeComptable = null;

    /**
     * Returns the idPeriodeComptable.
     * 
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Sets the idPeriodeComptable.
     * 
     * @param idPeriodeComptable
     *            The idPeriodeComptable to set
     */
    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
    }

}
