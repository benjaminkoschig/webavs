/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;

/**
 * 
 * @author jje
 */
public class RFQdSaisiePeriodeValiditeQdPrincipaleViewBean extends RFPeriodeValiditeQdPrincipale {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeQd = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    public String getAnneeQd() {
        return anneeQd;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
