package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;

public class CIAnomalieCI extends BEntity {

    private static final long serialVersionUID = -1363522850896696767L;
    public static final String CS_A_TRAITER = "326000";
    public static final String CS_ANNEE_OUVERTURE = "325001";
    public static final String CS_CI_ABSENT_A_LA_CAISSE = "325009";
    public static final String CS_CI_ABSENT_A_ZAS = "325010";
    public static final String CS_CI_PRESENT_CLOTURE = "325005";
    // public static final String CS_MOTIF_CLOTURE = "325006";
    public static final String CS_CLOTURE = "325007";

    public static final String CS_MOTIF_OUVERTURE = "325000";
    public static final String CS_NATIONNALITE = "325004";
    public static final String CS_NE_PAS_TRAITER = "326001";
    public static final String CS_NOM = "325002";
    // public static final String CS_DERNIERE_CAISSE ="325008";
    public static final String CS_NUMERO_AVS_ANCIEN = "325003";
    public static final String CS_TRAITE = "326002";

    private String anneeOuverture = new String();
    private String anomalieId = new String();
    private String compteIndividuelId = new String();
    private String dateCloture = new String();
    private String derniereCaisse = new String();
    private String dernierMotif = new String();
    private String etat = new String();
    private String motifOuverture = new String();
    private String nomPrenom = new String();
    private String numeroAvs = new String();
    private String numeroAvsPrecedent = new String();
    private String pays = new String();

    private String typeAnomalie = new String();

    public CIAnomalieCI() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JAUtil.isIntegerEmpty(anomalieId)) {
            setAnomalieId(_incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "CIANOMP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        anomalieId = statement.dbReadNumeric("KTID");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        numeroAvs = statement.dbReadString("KTNAVS");
        numeroAvsPrecedent = statement.dbReadString("KTNAVP");
        nomPrenom = statement.dbReadString("KTLNOM");
        pays = statement.dbReadNumeric("KTIPAY");
        motifOuverture = statement.dbReadNumeric("KTOARC");
        anneeOuverture = statement.dbReadNumeric("KTNOUV");
        dernierMotif = statement.dbReadNumeric("KTIARC");
        dateCloture = statement.dbReadDateAMJ("KTDCLO");
        derniereCaisse = statement.dbReadNumeric("KTICAI");
        etat = statement.dbReadNumeric("KTIETA");
        typeAnomalie = statement.dbReadNumeric("KTITYP");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (!JAUtil.isIntegerEmpty(derniereCaisse)) {
            if (derniereCaisse.trim().length() <= 7) {
                // recherche de l'id tiers
                int sep = derniereCaisse.indexOf('.');
                String caisse = null;
                String agence = null;
                if (sep > 0) {
                    caisse = derniereCaisse.substring(0, sep);
                    agence = derniereCaisse.substring(sep + 1);
                } else {
                    caisse = derniereCaisse;
                    agence = "";
                }
                try {
                    CIApplication application = (CIApplication) getSession().getApplication();
                    derniereCaisse = application.getAdministration(getSession(), caisse, agence,
                            new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();

                } catch (Exception ex) {
                    // impossible d'appliquer le changement
                }
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KTID", _dbWriteNumeric(statement.getTransaction(), getAnomalieId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KTID", _dbWriteNumeric(statement.getTransaction(), getAnomalieId(), "anomalieId"));
        statement.writeField("KTNAVS", _dbWriteString(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("KTITYP", _dbWriteNumeric(statement.getTransaction(), getTypeAnomalie(), "typeAnomalie"));
        statement.writeField("KAIIND",
                _dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KTNAVP",
                _dbWriteString(statement.getTransaction(), getNumeroAvsPrecedent(), "numeroAvsPrecedent"));
        statement.writeField("KTLNOM", _dbWriteString(statement.getTransaction(), getNomPrenom(), "nomPrenom"));
        statement.writeField("KTIPAY", _dbWriteNumeric(statement.getTransaction(), getPays(), "paysOrigine"));
        statement.writeField("KTOARC",
                _dbWriteNumeric(statement.getTransaction(), getMotifOuverture(), "motifOuverture"));
        statement.writeField("KTNOUV",
                _dbWriteNumeric(statement.getTransaction(), getAnneeOuverture(), "anneeOuverture"));
        statement.writeField("KTIARC", _dbWriteNumeric(statement.getTransaction(), getDernierMotif(), "dernierMotif"));
        statement.writeField("KTDCLO", _dbWriteDateAMJ(statement.getTransaction(), getDateCloture(), "dateCloture"));
        statement.writeField("KTICAI",
                _dbWriteNumeric(statement.getTransaction(), getDerniereCaisse(), "derniereCaisse"));
        statement.writeField("KTIETA", _dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));

    }

    public String getAnneeOuverture() {
        return anneeOuverture;
    }

    public String getAnomalieId() {
        return anomalieId;
    }

    public String getCaisseAgenceFormatee() {
        String lastCaisseAgence = "";
        try {

            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            if (CIApplication.DEFAULT_APPLICATION_PAVO.equalsIgnoreCase(getSession().getApplicationId())) {
                lastCaisseAgence = application.getAdministration(getSession(), derniereCaisse,
                        new String[] { "getCodeAdministration" }).getCodeAdministration();

            }
        } catch (Exception e) {
            return "";
        }
        String lastDateCloture = "";
        try {
            lastDateCloture = getDateCloture().substring(3, 10);
        } catch (Exception e) {
            dateCloture = "";
        }
        String lastMotif = "";
        if (!JAUtil.isIntegerEmpty(getDernierMotif())) {
            lastMotif = getDernierMotif();
        }
        if (JAUtil.isIntegerEmpty(lastDateCloture) && JAUtil.isIntegerEmpty(lastCaisseAgence)
                && JAUtil.isIntegerEmpty(lastMotif)) {
            return "";
        } else {
            return lastDateCloture + " - " + lastCaisseAgence + " - " + lastMotif;
        }
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
    public String getDateCloture() {
        return dateCloture;
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
    public String getDernierMotif() {
        return dernierMotif;
    }

    /**
     * @return
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @return
     */
    public String getMotifOuverture() {
        return motifOuverture;
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
    public String getNumeroAvsPrecedent() {
        return numeroAvsPrecedent;
    }

    /**
     * @return
     */
    public String getPays() {
        return pays;
    }

    public String getPaysFormate() {
        return getSession().getCode(getPays()) + " - " + getSession().getCodeLibelle(getPays());
    }

    /**
     * @return
     */
    public String getTypeAnomalie() {
        return typeAnomalie;
    }

    /**
     * @param string
     */
    public void setAnneeOuverture(String string) {
        anneeOuverture = string;
    }

    /**
     * @param string
     */
    public void setAnomalieId(String string) {
        anomalieId = string;
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
    public void setDateCloture(String string) {
        dateCloture = string;
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
    public void setDernierMotif(String string) {
        dernierMotif = string;
    }

    /**
     * @param string
     */
    public void setEtat(String string) {
        etat = string;
    }

    /**
     * @param string
     */
    public void setMotifOuverture(String string) {
        motifOuverture = string;
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
    public void setNumeroAvsPrecedent(String string) {
        numeroAvsPrecedent = string;
    }

    /**
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    /**
     * @param string
     */
    public void setTypeAnomalie(String string) {
        typeAnomalie = string;
    }
}
