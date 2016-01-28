/**
 *
 */
package globaz.helios.db.journaldebits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author sel
 * 
 */
public class CGExtractionOperationOsiris extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_ANNEE = "annee";
    public static final String FIELD_COMPTECOURANTDEST = "comptecourantdest";
    public static final String FIELD_COMPTECOURANTSRC = "comptecourantsrc";
    public static final String FIELD_CONTREPARTIEDEST = "contrepartiedest";
    public static final String FIELD_IDMANDAT = "idmandat";
    public static final String FIELD_MONTANT = "montant";
    public static final String FIELD_RUBRIQUESRC = "rubriquesrc";

    private String annee = "";
    private String compteCourantDest = "";
    private String compteCourantSrc = "";
    private String contrePartieDest = "";
    private String idMandat = "";
    private String montant = "";
    private String rubriqueSrc = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMandat = statement.dbReadNumeric(CGExtractionOperationOsiris.FIELD_IDMANDAT);
        annee = statement.dbReadNumeric(CGExtractionOperationOsiris.FIELD_ANNEE);
        compteCourantDest = statement.dbReadString(CGExtractionOperationOsiris.FIELD_COMPTECOURANTDEST);
        compteCourantSrc = statement.dbReadString(CGExtractionOperationOsiris.FIELD_COMPTECOURANTSRC);
        contrePartieDest = statement.dbReadString(CGExtractionOperationOsiris.FIELD_CONTREPARTIEDEST);
        montant = statement.dbReadNumeric(CGExtractionOperationOsiris.FIELD_MONTANT);
        rubriqueSrc = statement.dbReadString(CGExtractionOperationOsiris.FIELD_RUBRIQUESRC);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the compteCourantDest
     */
    public String getCompteCourantDest() {
        return compteCourantDest;
    }

    /**
     * @return the compteCourantSrc
     */
    public String getCompteCourantSrc() {
        return compteCourantSrc;
    }

    /**
     * @return the contrePartieDest
     */
    public String getContrePartieDest() {
        return contrePartieDest;
    }

    /**
     * @return the idMandat
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the rubriqueSrc
     */
    public String getRubriqueSrc() {
        return rubriqueSrc;
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param compteCourantDest
     *            the compteCourantDest to set
     */
    public void setCompteCourantDest(String compteCourantDest) {
        this.compteCourantDest = compteCourantDest;
    }

    /**
     * @param compteCourantSrc
     *            the compteCourantSrc to set
     */
    public void setCompteCourantSrc(String compteCourantSrc) {
        this.compteCourantSrc = compteCourantSrc;
    }

    /**
     * @param contrePartieDest
     *            the contrePartieDest to set
     */
    public void setContrePartieDest(String contrePartieDest) {
        this.contrePartieDest = contrePartieDest;
    }

    /**
     * @param idMandat
     *            the idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param rubriqueSrc
     *            the rubriqueSrc to set
     */
    public void setRubriqueSrc(String rubriqueSrc) {
        this.rubriqueSrc = rubriqueSrc;
    }

}
