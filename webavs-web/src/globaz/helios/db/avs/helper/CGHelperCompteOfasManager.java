package globaz.helios.db.avs.helper;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.comptes.CGCompte;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": NON FICTIF SELECT
 *         WEBAVSI.CGCOMTP.* FROM WEBAVSI.CGCOMTP INNER JOIN WEBAVSI.CGCPCOP ON
 *         (WEBAVSI.CGCPCOP.IDCOMPTE=WEBAVSI.CGCOMTP.IDCOMPTE) WHERE WEBAVSI.CGCPCOP.IDCOMPTEOFAS=31; FICTIF SELECT C.*
 *         FROM WEBAVSI.CGCOMTP AS C INNER JOIN WEBAVSI.CGOFCPP AS A ON (A.IDMANDAT=900 AND A.IDEXTERNE LIKE '910.5%')
 *         INNER JOIN WEBAVSI.CGCPCOP AS B ON (A.IDCOMPTEOFAS=B.IDCOMPTEOFAS AND B.IDCOMPTE=C.IDCOMPTE)
 */
public class CGHelperCompteOfasManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String forIdCompteOfas = "";
    String forIdMandat = "";
    String fromIdExterne = "";
    String idExterne = "";
    String idNature = "";

    /**
     * Constructor for CGHelperCompteOfasManager.
     */
    public CGHelperCompteOfasManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "C.*";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";
        if (!CGCompteOfas.CS_NATURE_FICTIF.equals(getIdNature())) {
            from = _getCollection() + "CGCOMTP AS C " + "INNER JOIN " + _getCollection() + "CGCPCOP AS B "
                    + "ON (B.IDCOMPTE=C.IDCOMPTE) ";
        } else {
            from = _getCollection() + "CGCOMTP AS C " + "INNER JOIN " + _getCollection() + "CGOFCPP AS A "
                    + "ON (A.IDMANDAT=" + getForIdMandat() + " " + "AND A.IDEXTERNE LIKE '" + getFromIdExterne()
                    + "%') " + "INNER JOIN " + _getCollection() + "CGCPCOP AS B "
                    + "ON (A.IDCOMPTEOFAS=B.IDCOMPTEOFAS AND B.IDCOMPTE=C.IDCOMPTE)";
        }
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        if (!CGCompteOfas.CS_NATURE_FICTIF.equals(getIdNature())) {
            return "B.IDCOMPTEOFAS=" + getForIdCompteOfas();
        } else {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGCompte();
    }

    /**
     * Returns the forIdCompteOfas.
     * 
     * @return String
     */
    public String getForIdCompteOfas() {
        return forIdCompteOfas;
    }

    /**
     * Returns the forIdMandat.
     * 
     * @return String
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the fromIdExterne.
     * 
     * @return String
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * Returns the idExterne.
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * Returns the idNature.
     * 
     * @return String
     */
    public String getIdNature() {
        return idNature;
    }

    /**
     * Sets the forIdCompteOfas.
     * 
     * @param forIdCompteOfas
     *            The forIdCompteOfas to set
     */
    public void setForIdCompteOfas(String forIdCompteOfas) {
        this.forIdCompteOfas = forIdCompteOfas;
    }

    /**
     * Sets the forIdMandat.
     * 
     * @param forIdMandat
     *            The forIdMandat to set
     */
    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * Sets the fromIdExterne.
     * 
     * @param fromIdExterne
     *            The fromIdExterne to set
     */
    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    /**
     * Sets the idExterne.
     * 
     * @param idExterne
     *            The idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * Sets the idNature.
     * 
     * @param idNature
     *            The idNature to set
     */
    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

}
