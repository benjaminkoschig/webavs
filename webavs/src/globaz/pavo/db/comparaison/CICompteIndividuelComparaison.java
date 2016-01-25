package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;

public class CICompteIndividuelComparaison extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KSTSEC) */
    private String accesSecurite = new String();
    /** (KSNOUV) */
    private String anneeOuverture = new String();
    /** (KSBOUV) */
    private Boolean ciOuvert = new Boolean(false);
    private String compteIndividuelId = new String();
    /** (KSIINR) */
    private String compteIndividuelIdReference = new String();
    /** Interne */
    private String compteIndividuelIdReferenceSave = new String();
    /** (KSDCRE) */
    private String dateCreation = new String();
    /** (KSDNAI) */
    private String dateNaissance = new String();
    /** (KSICAI) */
    private String derniereCaisse = new String();
    /** (KSDCLO) */
    private String derniereCloture = new String();
    /** (KSIEMP) */
    private String dernierEmployeur = new String();
    /** (KSIARC) */
    private String dernierMotifCloture = new String();
    private String dernierMotifOuverture = new String();
    /** (KSLNOM) */
    private String nomPrenom = new String();
    /** (KSNAVS) */
    private String numeroAvs = new String();
    /** (KSNAVA) */
    private String numeroAvsAncien = new String();
    /** (KSNAVP) */
    private String numeroAvsPrecedant = new String();
    /** (KSIPAY) */
    private String paysOrigineId = new String();
    /** (KSBFUS) */
    private Boolean provenanceFusion = new Boolean(false);
    /** (KSLREF) */
    private String referenceInterne = new String();
    /** (KSIREG) */
    private String registre = new String();
    /** (KSTSEX) */
    private String sexe = new String();

    /** (KSBTRA) */
    // Indique si le CI a été traité, pour définir les CIs ouvert à la caisse et
    // pas ds le fichier
    private Boolean traite = new Boolean(false);

    /**
	 * 
	 */
    public CICompteIndividuelComparaison() {
        super();
    }

    public String _getSqlUpdateTraite() {
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(_getCollection());
        sql.append("CIIABGP ");
        sql.append("set KSBTRA = '1'");
        sql.append(" where KSIIND = ? ");
        return sql.toString();
    };

    @Override
    protected String _getTableName() {
        return "CIIABGP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compteIndividuelId = statement.dbReadNumeric("KSIIND");
        compteIndividuelIdReference = statement.dbReadNumeric("KSIINR");
        numeroAvs = statement.dbReadString("KSNAVS");
        nomPrenom = statement.dbReadString("KSLNOM");
        paysOrigineId = statement.dbReadNumeric("KSIPAY");
        dateNaissance = CIUtil.formatDate(statement.dbReadNumeric("KSDNAI"));
        sexe = statement.dbReadNumeric("KSTSEX");
        anneeOuverture = statement.dbReadNumeric("KSNOUV");
        referenceInterne = statement.dbReadString("KSLREF");
        accesSecurite = statement.dbReadNumeric("KSTSEC");
        ciOuvert = statement.dbReadBoolean("KSBOUV");
        dernierMotifCloture = statement.dbReadNumeric("KSIARC");
        dernierMotifOuverture = statement.dbReadNumeric("KSOARC");
        derniereCaisse = statement.dbReadNumeric("KSICAI");
        derniereCloture = statement.dbReadDateAMJ("KSDCLO");
        provenanceFusion = statement.dbReadBoolean("KSBFUS");
        dernierEmployeur = statement.dbReadNumeric("KSIEMP");
        numeroAvsAncien = statement.dbReadString("KSNAVA");
        numeroAvsPrecedant = statement.dbReadString("KSNAVP");
        registre = statement.dbReadNumeric("KSIREG");
        dateCreation = statement.dbReadDateAMJ("KSDCRE");
        traite = statement.dbReadBoolean("KSBTRA");

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KSIIND", this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * @return
     */
    public String getAccesSecurite() {
        return accesSecurite;
    }

    /**
     * @return
     */
    public String getAnneeOuverture() {
        if (!JAUtil.isIntegerEmpty(anneeOuverture)) {
            return anneeOuverture;
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCaisseAgenceFormatee() {
        try {

            String caisseAgence = "";
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            if (CIApplication.DEFAULT_APPLICATION_PAVO.equalsIgnoreCase(getSession().getApplicationId())) {
                caisseAgence = application.getAdministration(getSession(), derniereCaisse,
                        new String[] { "getCodeAdministration" }).getCodeAdministration();
            }
            return caisseAgence;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public Boolean getCiOuvert() {
        return ciOuvert;
    }

    /**
     * @return
     */
    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    /**
     * @return
     */
    public String getCompteIndividuelIdReference() {
        return compteIndividuelIdReference;
    }

    /**
     * @return
     */
    public String getCompteIndividuelIdReferenceSave() {
        return compteIndividuelIdReferenceSave;
    }

    /**
     * @return
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    public CIRassemblementOuverture getDerniereBCloture(BTransaction transaction, String compteIndividuelId) {
        try {
            // force à regarder dans les annonces
            CIRassemblementOuvertureManager mgr = new CIRassemblementOuvertureManager();
            mgr.setSession(getSession());
            mgr.setForCompteIndividuelId(compteIndividuelId);
            mgr.setOrderByDateCloture(false);
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                CIRassemblementOuverture ras = (CIRassemblementOuverture) mgr.getEntity(i);
                if (JAUtil.isStringEmpty(ras.getDateRevocation()) && ras.isCloture()) {
                    return ras;
                }
            }
        } catch (Exception ex) {
            // laisser tel quel
        }
        return null;
    }

    /**
     * @return
     */
    public String getDerniereCaisse() {
        return derniereCaisse;
    }

    /**
     * @return
     */
    public String getDerniereCloture() {
        return derniereCloture;
    }

    /**
     * @return
     */
    public String getDernierEmployeur() {
        return dernierEmployeur;
    }

    /**
     * @return
     */
    public String getDernierMotifCloture() {
        return dernierMotifCloture;
    }

    /**
     * @return
     */
    public String getDernierMotifOuverture() {
        if (!JAUtil.isIntegerEmpty(dernierMotifOuverture)) {
            return dernierMotifOuverture;
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return
     */
    public String getNumeroAvs() {
        return numeroAvs;
    }

    /**
     * @return
     */
    public String getNumeroAvsAncien() {
        return numeroAvsAncien;
    }

    /**
     * @return
     */
    public String getNumeroAvsPrecedant() {
        return numeroAvsPrecedant;
    }

    public String getPays() {
        if (new Long(Long.parseLong(paysOrigineId)).doubleValue() > 300000) {
            return CodeSystem.getCodeUtilisateur(paysOrigineId, getSession());
        } else {
            return String.valueOf(paysOrigineId);
        }

    }

    /**
     * @return
     */
    public String getPaysOrigineId() {
        return paysOrigineId;
    }

    /**
     * @return
     */
    public Boolean getProvenanceFusion() {
        return provenanceFusion;
    }

    /**
     * @return
     */
    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * @return
     */
    public String getRegistre() {
        return registre;
    }

    /**
     * @return
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @return
     */
    public Boolean getTraite() {
        return traite;
    }

    /**
     * @param string
     */
    public void setAccesSecurite(String string) {
        accesSecurite = string;
    }

    /**
     * @param string
     */
    public void setAnneeOuverture(String string) {
        anneeOuverture = string;
    }

    /**
     * @param boolean1
     */
    public void setCiOuvert(Boolean boolean1) {
        ciOuvert = boolean1;
    }

    /**
     * @param string
     */
    public void setCompteIndividuelId(String string) {
        compteIndividuelId = string;
    }

    /**
     * @param string
     */
    public void setCompteIndividuelIdReference(String string) {
        compteIndividuelIdReference = string;
    }

    /**
     * @param string
     */
    public void setCompteIndividuelIdReferenceSave(String string) {
        compteIndividuelIdReferenceSave = string;
    }

    /**
     * @param string
     */
    public void setDateCreation(String string) {
        dateCreation = string;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     */
    public void setDerniereCaisse(String string) {
        derniereCaisse = string;
    }

    /**
     * @param string
     */
    public void setDerniereCloture(String string) {
        derniereCloture = string;
    }

    /**
     * @param string
     */
    public void setDernierEmployeur(String string) {
        dernierEmployeur = string;
    }

    /**
     * @param string
     */
    public void setDernierMotifCloture(String string) {
        dernierMotifCloture = string;
    }

    /**
     * @param string
     */
    public void setDernierMotifOuverture(String string) {
        dernierMotifOuverture = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * @param string
     */
    public void setNumeroAvs(String string) {
        numeroAvs = string;
    }

    /**
     * @param string
     */
    public void setNumeroAvsAncien(String string) {
        numeroAvsAncien = string;
    }

    /**
     * @param string
     */
    public void setNumeroAvsPrecedant(String string) {
        numeroAvsPrecedant = string;
    }

    /**
     * @param string
     */
    public void setPaysOrigineId(String string) {
        paysOrigineId = string;
    }

    /**
     * @param boolean1
     */
    public void setProvenanceFusion(Boolean boolean1) {
        provenanceFusion = boolean1;
    }

    /**
     * @param string
     */
    public void setReferenceInterne(String string) {
        referenceInterne = string;
    }

    /**
     * @param string
     */
    public void setRegistre(String string) {
        registre = string;
    }

    /**
     * @param string
     */
    public void setSexe(String string) {
        sexe = string;
    }

    /**
     * @param boolean1
     */
    public void setTraite(Boolean boolean1) {
        traite = boolean1;
    }

    public void traiteCi(BTransaction transaction) throws Exception {
        BPreparedStatement ciPrepared = new BPreparedStatement(transaction);
        try {
            ciPrepared.prepareStatement(_getSqlUpdateTraite());
            ciPrepared.setBigDecimal(1, new BigDecimal(compteIndividuelId));
            ciPrepared.execute();
        } finally {
            ciPrepared.closePreparedStatement();
        }
    }

}
