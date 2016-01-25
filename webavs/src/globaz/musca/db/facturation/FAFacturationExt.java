package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class FAFacturationExt extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String TABLE_FIELDS = "FAFAEXT.FAIDFE, FAFAEXT.FANCAI, FAFAEXT.FANAFF, "
            + "FAFAEXT.FANRUB, FAFAEXT.FANPER, FAFAEXT.FAMMAS, "
            + "FAFAEXT.FANTAU, FAFAEXT.FAMCOT, FAFAEXT.FATROL, "
            + "FAFAEXT.FADANN, FAFAEXT.FADPED, FAFAEXT.FADPEA, "
            + "FAFAEXT.FALIBE, FAFAEXT.FABSUS, FAFAEXT.FANPAS, "
            + "FAFAEXT.FAMCPA, FAFAEXT.FALTFA, FAFAEXT.FADVAL, FAFAEXT.PSPY, FAFAEXT.FABTAU, FAFAEXT.IDMODERECOUVREMENT ";
    private Boolean afficheTaux = new Boolean(true);
    private String anneeCotisation = new String();
    private String cotisation = new String();
    private String cotisationPaye = new String();
    private String datePaiement = new String();
    private String dateValeur = new String();
    private java.lang.Boolean enSuspens = new Boolean(false);
    FAEnteteFactureViewBean enteteFacture = new FAEnteteFactureViewBean();
    private String idFacturationExt = new String();
    private String idModeRecouvrement = new String();
    private String libelle = new String();
    private String masse = new String();
    private String numAffilie = new String();
    private String numCaisse = new String();
    private String numPassage = new String();
    private String numPeriode = new String();
    private String numRubrique = new String();
    private String periodeA = new String();
    private String periodeDe = new String();
    private Boolean quatreDeci = new Boolean(false);

    private String role = new String();

    private String taux = new String();

    private String typeFactu = new String();

    /**
     * Commentaire relatif au constructeur FAAfact
     */
    public FAFacturationExt() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAFacturationExt.TABLE_FIELDS;
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "FAFAEXT AS FAFAEXT ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAFAEXT";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idFacturationExt = statement.dbReadNumeric("FAIDFE");
        numCaisse = statement.dbReadNumeric("FANCAI");
        numAffilie = statement.dbReadString("FANAFF");
        numRubrique = statement.dbReadString("FANRUB");
        numPeriode = statement.dbReadString("FANPER");
        masse = statement.dbReadNumeric("FAMMAS", 2);
        taux = statement.dbReadNumeric("FANTAU", 5);
        cotisation = statement.dbReadNumeric("FAMCOT", 2);
        role = statement.dbReadNumeric("FATROL");
        anneeCotisation = statement.dbReadNumeric("FADANN");
        periodeDe = statement.dbReadDateAMJ("FADPED");
        periodeA = statement.dbReadDateAMJ("FADPEA");
        libelle = statement.dbReadString("FALIBE");
        enSuspens = statement.dbReadBoolean("FABSUS");
        numPassage = statement.dbReadNumeric("FANPAS");
        cotisationPaye = statement.dbReadNumeric("FAMCPA", 2);
        typeFactu = statement.dbReadString("FALTFA");
        dateValeur = statement.dbReadDateAMJ("FADVAL");
        datePaiement = statement.dbReadDateAMJ("FADPMT");
        afficheTaux = statement.dbReadBoolean("FABTAU");
        idModeRecouvrement = statement.dbReadNumeric("IDMODERECOUVREMENT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("FAIDFE", this._dbWriteNumeric(statement.getTransaction(), getIdFacturationExt(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("FAIDFE",
                this._dbWriteNumeric(statement.getTransaction(), getIdFacturationExt(), "idFacturationExt"));
        statement.writeField("FANCAI", this._dbWriteNumeric(statement.getTransaction(), getNumCaisse(), "numCaisse"));
        statement.writeField("FANAFF", this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
        statement
                .writeField("FANRUB", this._dbWriteString(statement.getTransaction(), getNumRubrique(), "numRubrique"));
        statement.writeField("FANPER", this._dbWriteString(statement.getTransaction(), getNumPeriode(), "numPeriode"));
        statement.writeField("FAMMAS", this._dbWriteNumeric(statement.getTransaction(), getMasse(), "masse"));
        statement.writeField("FANTAU", this._dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField("FAMCOT", this._dbWriteNumeric(statement.getTransaction(), getCotisation(), "cotisation"));
        statement.writeField("FATROL", this._dbWriteNumeric(statement.getTransaction(), getRole(), "role"));
        statement.writeField("FADANN",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField("FADPED", this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeDe(), "periodeDe"));
        statement.writeField("FADPEA", this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeA(), "periodeA"));
        statement.writeField("FALIBE", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("FABSUS", this._dbWriteBoolean(statement.getTransaction(), getEnSuspens(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "enSuspens"));
        statement.writeField("FANPAS", this._dbWriteNumeric(statement.getTransaction(), getNumPassage(), "numPassage"));
        statement.writeField("FAMCPA",
                this._dbWriteNumeric(statement.getTransaction(), getCotisationPaye(), "cotisationPaye"));
        statement.writeField("FALTFA", this._dbWriteString(statement.getTransaction(), getTypeFactu(), "typeFactu"));
        statement.writeField("FADVAL", this._dbWriteDateAMJ(statement.getTransaction(), getDateValeur(), "dateValeur"));
        statement.writeField("FADPMT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePaiement(), "datePaiement"));
        statement.writeField("FABTAU", this._dbWriteBoolean(statement.getTransaction(), getAfficheTaux(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "afficheTaux"));
    }

    public Boolean getAfficheTaux() {
        return afficheTaux;
    }

    public java.lang.String getAnneeCotisation() {
        if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            return "";
        } else {
            return anneeCotisation;
        }
    }

    /**
     * @return
     */
    public String getCotisation() {
        return JANumberFormatter.fmt(cotisation.toString(), true, true, false, 2);
    }

    /**
     * @return
     */
    public String getCotisationPaye() {
        return JANumberFormatter.fmt(cotisationPaye.toString(), true, true, false, 2);
    }

    /**
     * @return the datePaiement
     */
    public String getDatePaiement() {
        return datePaiement;
    }

    /**
     * @return
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return
     */
    public java.lang.Boolean getEnSuspens() {
        return enSuspens;
    }

    /**
     * @return
     */
    public FAEnteteFactureViewBean getEnteteFacture() {
        return enteteFacture;
    }

    /**
     * @return
     */
    public String getIdFacturationExt() {
        return idFacturationExt;
    }

    public String getIdModeRecouvrement() {
        return idModeRecouvrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    public String getLangueTiers(FAEnteteFactureViewBean factureViewBean) {
        // reprendre la méthode de l'entête
        return factureViewBean.getLangueTiers();
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * @return
     */
    public String getMasse() {
        return JANumberFormatter.fmt(masse.toString(), true, true, false, 2);
    }

    /**
     * @return
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return
     */
    public String getNumCaisse() {
        return numCaisse;
    }

    /**
     * @return
     */
    public String getNumPassage() {
        return numPassage;
    }

    /**
     * @return
     */
    public String getNumPeriode() {
        return numPeriode;
    }

    /**
     * @return
     */
    public String getNumRubrique() {
        return numRubrique;
    }

    /**
     * @return
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * @return
     */
    public Boolean getQuatreDeci() {
        return quatreDeci;
    }

    /**
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     * @return
     */
    public String getTaux() {
        if (!taux.equals("") && !taux.equals(null)) {
            if (taux.length() > 2) {
                int indexPoint = taux.indexOf('.');
                String decimales = taux.substring(indexPoint + 1);
                if (((decimales.length() > 2) && (decimales.charAt(2) != '0'))
                        || ((decimales.length() > 3) && (decimales.charAt(3) != '0'))) {
                    setQuatreDeci(Boolean.TRUE);
                    return JANumberFormatter.fmt(taux.toString(), false, false, true, 4);
                } else {
                    setQuatreDeci(Boolean.FALSE);
                    return JANumberFormatter.fmt(taux.toString(), false, false, true, 2);
                }
            } else {
                setQuatreDeci(Boolean.FALSE);
                return JANumberFormatter.fmt(taux.toString(), false, false, true, 2);
            }
        } else {
            setQuatreDeci(Boolean.FALSE);
            return taux;
        }
    }

    /**
     * @return
     */
    public String getTypeFactu() {
        return typeFactu;
    }

    public void setAfficheTaux(Boolean afficheTaux) {
        this.afficheTaux = afficheTaux;
    }

    /**
     * @param string
     */
    public void setAnneeCotisation(String string) {
        anneeCotisation = string;
    }

    /**
     * @param string
     */
    public void setCotisation(String string) {
        cotisation = string;
    }

    /**
     * @param string
     */
    public void setCotisationPaye(String string) {
        cotisationPaye = string;
    }

    /**
     * @param datePaiement
     *            the datePaiement to set
     */
    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    /**
     * @param string
     */
    public void setDateValeur(String string) {
        dateValeur = string;
    }

    /**
     * @param boolean1
     */
    public void setEnSuspens(java.lang.Boolean boolean1) {
        enSuspens = boolean1;
    }

    /**
     * @param bean
     */
    public void setEnteteFacture(FAEnteteFactureViewBean bean) {
        enteteFacture = bean;
    }

    /**
     * @param string
     */
    public void setIdFacturationExt(String string) {
        idFacturationExt = string;
    }

    public void setIdModeRecouvrement(String newIdModeRecouvrement) {
        idModeRecouvrement = newIdModeRecouvrement;
    }

    /**
     * @param string
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    /**
     * @param string
     */
    public void setMasse(String string) {
        masse = string;
    }

    /**
     * @param string
     */
    public void setNumAffilie(String string) {
        numAffilie = string;
    }

    /**
     * @param string
     */
    public void setNumCaisse(String string) {
        numCaisse = string;
    }

    /**
     * @param string
     */
    public void setNumPassage(String string) {
        numPassage = string;
    }

    /**
     * @param string
     */
    public void setNumPeriode(String string) {
        numPeriode = string;
    }

    /**
     * @param string
     */
    public void setNumRubrique(String string) {
        numRubrique = string;
    }

    /**
     * @param string
     */
    public void setPeriodeA(String string) {
        periodeA = string;
    }

    /**
     * @param string
     */
    public void setPeriodeDe(String string) {
        periodeDe = string;
    }

    /**
     * @param boolean1
     */
    public void setQuatreDeci(Boolean boolean1) {
        quatreDeci = boolean1;
    }

    /**
     * @param string
     */
    public void setRole(String string) {
        role = string;
    }

    /**
     * @param string
     */
    public void setTaux(String string) {
        taux = string;
    }

    /**
     * @param string
     */
    public void setTypeFactu(String string) {
        typeFactu = string;
    }

}
