/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.lots;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.tools.PRHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureJointCompensation extends APFactureACompenser implements PRHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** alias */
    public static final String FIELDNAME_ISLIGNECOMPENSATION = "VOBLCO";

    /**
     */
    public static final String FIELDNAME_NOMTIERS = "HTLDE1";

    /**
     */
    public static final String FIELDNAME_PRENOMTIERS = "HTLDE2";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dettes = "";
    private String idAffilieCompensation = "";
    private String idCompensation = "";
    private String idLot = "";
    private String idTiersCompensation = "";
    private Boolean isLigneCompensation = Boolean.FALSE;

    private String montantTotal = "";
    private String nomTiers = "";

    private String prenomTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne faux.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * retourne faux
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return APFactureACompenser.TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);

        try {
            idCompensation = statement.dbReadNumeric(APCompensation.FIELDNAME_IDCOMPENSATION);
            idLot = statement.dbReadNumeric(APCompensation.FIELDNAME_IDLOT);
            idTiersCompensation = statement.dbReadNumeric(APCompensation.FIELDNAME_IDTIERS);
            idAffilieCompensation = statement.dbReadNumeric(APCompensation.FIELDNAME_IDAFFILIE);
            montantTotal = statement.dbReadNumeric(APCompensation.FIELDNAME_MONTANTTOTAL, 2);
            dettes = statement.dbReadNumeric(APCompensation.FIELDNAME_DETTES, 2);
            prenomTiers = statement.dbReadString(APFactureJointCompensation.FIELDNAME_PRENOMTIERS);
            nomTiers = statement.dbReadString(APFactureJointCompensation.FIELDNAME_NOMTIERS);
            isLigneCompensation = statement.dbReadBoolean(APFactureJointCompensation.FIELDNAME_ISLIGNECOMPENSATION);
        } catch (Exception e) {
            e.printStackTrace();
            // on ne veut pas que le truc lance des exceptions si un retrieve
            // est effectue par erreur.
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * getter pour l'attribut dettes
     * 
     * @return la valeur courante de l'attribut dettes
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String getDettes() throws Exception {
        return dettes;
    }

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilieCompensation() {
        return idAffilieCompensation;
    }

    /**
     * getter pour l'attribut id compensation
     * 
     * @return la valeur courante de l'attribut id compensation
     */
    public String getIdCompensation() {
        return idCompensation;
    }

    /**
     * getter pour l'attribut id lot
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     */
    @Override
    public String getIdMajeur() {
        return idCompensation + getIdFactureACompenser();
    }

    /**
     * @see globaz.prestation.tools.PRHierarchique#getIdParent()
     */
    @Override
    public String getIdParent() {
        return getIdCompensationParente() + "0";
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiersCompensation() {
        return idTiersCompensation;
    }

    /**
     * getter pour l'attribut is ligne compensation
     * 
     * @return la valeur courante de l'attribut is ligne compensation
     */
    public Boolean getIsLigneCompensation() {
        return isLigneCompensation;
    }

    /**
     * getter pour l'attribut montant total
     * 
     * @return la valeur courante de l'attribut montant total
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * getter pour l'attribut prenom nom tiers
     * 
     * @return la valeur courante de l'attribut prenom nom tiers
     */
    public String getNomPrenomTiers() {
        if (!JadeStringUtil.isEmpty(prenomTiers)) {
            return nomTiers + " " + prenomTiers;
        } else {
            return nomTiers;
        }
    }

    /**
     * getter pour l'attribut nom tiers
     * 
     * @return la valeur courante de l'attribut nom tiers
     */
    public String getNomTiers() {
        return nomTiers;
    }

    @Override
    public String getNumAffilie() {
        String numAffilie = "";

        if (!JadeStringUtil.isIntegerEmpty(idCompensation)) {
            try {
                APCompensation compensation = new APCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(idCompensation);
                compensation.retrieve();

                if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie())) {
                    IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getSession()
                            .newTransaction(), compensation.getIdAffilie(), compensation.getIdTiers());
                    numAffilie = affilie.getNumAffilie();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numAffilie;
    }

    public String getNumEtTypeAffiliation() {
        String numAffilie = "";

        if (!JadeStringUtil.isIntegerEmpty(idCompensation)) {
            try {
                APCompensation compensation = new APCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(idCompensation);
                compensation.retrieve();

                if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie())) {
                    IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getSession()
                            .newTransaction(), compensation.getIdAffilie(), compensation.getIdTiers());
                    numAffilie = affilie.getNumAffilie();

                    // InfoRom531
                    try {
                        String tAff = getSession().getCodeLibelle(affilie.getTypeAffiliation());
                        numAffilie += " " + tAff;
                    } catch (Exception e) {
                        numAffilie = affilie.getNumAffilie();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numAffilie;
    }

    /**
     * getter pour l'attribut prenom tiers
     * 
     * @return la valeur courante de l'attribut prenom tiers
     */
    public String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilieCompensation(String idAffilie) {
        idAffilieCompensation = idAffilie;
    }

    /**
     * setter pour l'attribut id compensation
     * 
     * @param idCompensation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensation(String idCompensation) {
        this.idCompensation = idCompensation;
    }

    /**
     * setter pour l'attribut id lot
     * 
     * @param idLot
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersCompensation(String idTiers) {
        idTiersCompensation = idTiers;
    }

    /**
     * setter pour l'attribut is ligne compensation
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsLigneCompensation(Boolean boolean1) {
        isLigneCompensation = boolean1;
    }

    /**
     * setter pour l'attribut is compenser
     * 
     * @param montantTotal
     *            une nouvelle valeur pour cet attribut
     */
    /**
     * setter pour l'attribut montant total
     * 
     * @param montantTotal
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

}
