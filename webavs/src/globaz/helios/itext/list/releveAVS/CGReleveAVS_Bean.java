package globaz.helios.itext.list.releveAVS;

import globaz.globall.db.BSession;
import globaz.helios.process.helper.CGHelperReleveAVS;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public abstract class CGReleveAVS_Bean implements CGIReleveAVS_Bean {
    private static final String IDEXETERNE_END_WITH_5999 = "5999";
    private static final String IDEXETERNE_END_WITH_6999 = "6999";
    private static final String IDEXETERNE_END_WITH_7999 = "7999";
    private static final String IDEXETERNE_END_WITH_8999 = "8999";
    private static final String IDEXETERNE_END_WITH_999 = "999";

    protected String col0 = null;
    protected String col1 = null;
    protected Double col2 = null;
    protected Double col3 = null;
    protected Double col4 = null;

    String idExerciceComptable = null;
    String idMandat = null;
    String idPeriodeComptable = null;
    boolean isProvisoire = false;
    CGHelperReleveAVS traitementReleveAvsHelper = new CGHelperReleveAVS();

    /**
     * Constructor for CGGrandLivre_Bean.
     */
    public CGReleveAVS_Bean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#COL_0()
     */
    @Override
    public String getCOL_0() {
        return col0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#COL_1()
     */
    @Override
    public String getCOL_1() {
        return col1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#COL_2()
     */
    @Override
    public Double getCOL_2() {
        return col2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#COL_3()
     */
    @Override
    public Double getCOL_3() {
        return col3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#COL_4()
     */
    @Override
    public Double getCOL_4() {
        return col4;
    }

    /**
     * Format le libelle. Si libelle est vide et si l'idexterne se termine pas IDEXETERNE_END_WITH_xxxx alors retourne
     * label.
     * 
     * @param session
     * @param idExterne
     * @param libelle
     * @return
     */
    protected String getCol1Libelle(BSession session, String idExterne, String libelle) {
        if (JadeStringUtil.isBlank(libelle)
                && !JadeStringUtil.isBlank(idExterne)
                && idExterne.substring(idExterne.length() - IDEXETERNE_END_WITH_999.length()).equals(
                        IDEXETERNE_END_WITH_999)) {
            if (idExterne.substring(idExterne.length() - IDEXETERNE_END_WITH_5999.length()).equals(
                    IDEXETERNE_END_WITH_5999)) {
                return session.getLabel("RELEVE_AVS_CHARGES_ADMINISTRATION");
            } else if (idExterne.substring(idExterne.length() - IDEXETERNE_END_WITH_6999.length()).equals(
                    IDEXETERNE_END_WITH_6999)) {
                return session.getLabel("RELEVE_AVS_PRODUITS_ADMINISTRATION");
            } else if (idExterne.substring(idExterne.length() - IDEXETERNE_END_WITH_7999.length()).equals(
                    IDEXETERNE_END_WITH_7999)) {
                return session.getLabel("RELEVE_AVS_CHARGES_INVESTISSEMENT");
            } else if (idExterne.substring(idExterne.length() - IDEXETERNE_END_WITH_8999.length()).equals(
                    IDEXETERNE_END_WITH_8999)) {
                return session.getLabel("RELEVE_AVS_PRODUITS_INVESTISSEMENT");
            } else {
                return libelle;
            }
        } else {
            return libelle;
        }
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    @Override
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    @Override
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idPeriodeComptable.
     * 
     * @return String
     */
    @Override
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Returns the isProvisoire.
     * 
     * @return boolean
     */
    @Override
    public boolean isProvisoire() {
        return isProvisoire;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    @Override
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    @Override
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idPeriodeComptable.
     * 
     * @param idPeriodeComptable
     *            The idPeriodeComptable to set
     */
    @Override
    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
    }

    /**
     * Sets the isProvisoire.
     * 
     * @param isProvisoire
     *            The isProvisoire to set
     */
    @Override
    public void setIsProvisoire(boolean isProvisoire) {
        this.isProvisoire = isProvisoire;
    }
}
