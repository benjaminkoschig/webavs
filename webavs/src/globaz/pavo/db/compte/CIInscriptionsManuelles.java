package globaz.pavo.db.compte;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;

/**
 * Objet permettant la saisie de correction manuelle de la masse salariale
 * 
 * @author sda Date de création: (04.03.2005 09:18:35)
 */

public class CIInscriptionsManuelles extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (MALNAF) */
    private String affilie = new String();
    /** (KSNANN) */
    private String annee = new String();
    /** (HTLDE1) */
    private String employeurNom = new String();
    /** (HTLDE2) */
    private String employeurPrenom = new String();
    /** (KSIAFF) */
    private String idAffiliation = new String();
    /** (KSID) */
    private String idCorrection = new String();
    /** (KSLIB) */
    private String libelle = new String();
    /** (KSMMON) */
    private String montant = new String();

    public CIInscriptionsManuelles() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_alwaysAfterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws Exception {
        // TODO Raccord de méthode auto-généré
        if (transaction.hasErrors()) {
            setIdCorrection("");

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) {
        // On incrémente le compteur de la clé primaire
        if (JAUtil.isIntegerEmpty(idCorrection)) {
            try {
                setIdCorrection(this._incCounter(transaction, "0"));
            } catch (Exception e) {
                // TODO Bloc catch auto-généré
                e.printStackTrace();
            }
        }

    }

    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + _getTableName() + ".*, " + _getCollection() + "AFAFFIP.MALNAF, " + _getCollection()
                + "TITIERP.HTLDE1, " + _getCollection() + "TITIERP.HTLDE2";

    }

    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();

        joinStr = " inner join " + _getCollection() + "AFAFFIP on " + _getCollection() + _getTableName() + ".KSIAFF="
                + _getCollection() + "AFAFFIP.MAIAFF " + "left outer join " + _getCollection() + "TITIERP on "
                + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE";

        return _getCollection() + _getTableName() + joinStr;
    }

    @Override
    protected String _getTableName() {

        return "CICORRP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCorrection = statement.dbReadNumeric("KSID");
        idAffiliation = statement.dbReadNumeric("KSIAFF");
        annee = statement.dbReadNumeric("KSNANN");
        libelle = statement.dbReadString("KSLIB");
        montant = statement.dbReadNumeric("KSMMON");
        affilie = statement.dbReadString("MALNAF");
        employeurNom = statement.dbReadString("HTLDE1");
        employeurPrenom = statement.dbReadString("HTLDE2");
    }

    @Override
    protected void _validate(BStatement statement) {

        // Empêche l'affilié d'être null
        if (JAUtil.isIntegerEmpty(getAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AFFILIE"));

        }

        // Set l'id de l'affilie dans le champ et teste que l'année entrée soit
        // correcte par rapport à l'affiliation
        CIApplication mgrAffilie;
        try {
            if (!JAUtil.isIntegerEmpty(getAffilie())) {
                mgrAffilie = new CIApplication();
                AFAffiliation numeroAffiliation = new AFAffiliation();

                numeroAffiliation = mgrAffilie
                        .getAffilieByNo(getSession(), affilie, true, false, "", "", annee, "", "");
                // Si numéro affiliation est null l'année entrée n'est pas
                // valide
                if (numeroAffiliation == null) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AFFILIE"));
                } else {

                    // on set l'id de l'affilié dans le champs
                    setIdAffiliation(numeroAffiliation.getAffiliationId());
                }
            }
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        if (JAUtil.isIntegerEmpty(getAnnee())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ANNEE_OBLIGATOIRE"));
        } else {
            if (!JAUtil.isStringEmpty(getAnnee())) {
                if (new Integer(getAnnee().trim()).intValue() < 1948) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ANNEE_SUPERIEUR_1948"));
                }
            }
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KSID", this._dbWriteNumeric(statement.getTransaction(), getIdCorrection(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KSID",
                this._dbWriteNumeric(statement.getTransaction(), getIdCorrection(), "idCorrection"));
        statement.writeField("KSIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("KSNANN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("KSLIB", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("KSMMON", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
    }

    /**
     * @return le numéro d'affilié
     */
    public String getAffilie() {

        return affilie;
    }

    /**
     * @return une String contenant l'annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getEmployeurNom() {
        return employeurNom;
    }

    public String getEmployeurNomPrenom() {
        try {
            return employeurNom + " " + employeurPrenom;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * @return
     */
    public String getEmployeurNoNom() {
        return affilie + "  " + employeurNom + " " + employeurPrenom;
    }

    /**
     * @return une String contenant l'id d'affiliation
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @return une String contenant l'id de la table
     */
    public String getIdCorrection() {
        return idCorrection;
    }

    /**
     * @return une String contenant le libellé
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return une String contenant le montant
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantFormate() {
        return new FWCurrency(getMontant()).toStringFormat();
    }

    /**
     * @param string
     *            le numéro d'affilié à setter
     */
    public void setAffilie(String string) {
        affilie = string;
    }

    /**
     * @param string
     *            l'année à setter
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setEmployeurNom(String string) {
        employeurNom = string;
    }

    /**
     * @param string
     *            l'id à setter
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    /**
     * @param string
     *            l'id à setter
     */
    public void setIdCorrection(String string) {
        idCorrection = string;
    }

    /**
     * @param string
     *            le libellé à setter
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    /**
     * @param string
     *            le montant à setter
     */
    public void setMontant(String string) {
        montant = string;
    }

}
