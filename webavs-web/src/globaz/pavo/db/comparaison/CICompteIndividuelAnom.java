package globaz.pavo.db.comparaison;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;

public class CICompteIndividuelAnom extends BEntity {

    private static final long serialVersionUID = -6673996538967935617L;
    /** (KTNOUV) */
    private String anneeOuverture = new String();
    private String caisseTenantCI = new String();
    /** (KTBOUV) */
    private Boolean ciOuvert = new Boolean(false);
    private String compteIndividuelId = new String();
    /** (KTDCRE) */
    private String dateCreation = new String();
    /** (KTICAI) */
    private String derniereCaisse = new String();
    /** (KTDCLO) */
    private String derniereCloture = new String();
    /** (KTIARC) */
    private String dernierMotifCloture = new String();
    private String dernierMotifOuverture = new String();
    /** (KTLNOM) */
    private String nomPrenom = new String();
    /** (KTIINR) */
    private String numeroAvs = new String();
    /** (KTNAVP) */
    private String numeroAvsPrecedant = new String();

    /** (KTIPAY) */
    private String paysOrigineId = new String();

    /** (KTBTRA) */
    // Indique si le CI a été traité, pour définir les CIs ouvert à la caisse et
    // pas ds le fichier
    private Boolean traite = new Boolean(false);

    public CICompteIndividuelAnom() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JAUtil.isIntegerEmpty(compteIndividuelId)) {
            setCompteIndividuelId(_incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "CIIOUVP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compteIndividuelId = statement.dbReadNumeric("KTIIND");
        numeroAvs = statement.dbReadString("KTNAVS");
        nomPrenom = statement.dbReadString("KTLNOM");
        paysOrigineId = statement.dbReadNumeric("KTIPAY");
        anneeOuverture = statement.dbReadNumeric("KTNOUV");
        ciOuvert = statement.dbReadBoolean("KTBOUV");
        dernierMotifCloture = statement.dbReadNumeric("KTIARC");
        dernierMotifOuverture = statement.dbReadNumeric("KTOARC");
        derniereCaisse = statement.dbReadNumeric("KTICAI");
        derniereCloture = statement.dbReadDateAMJ("KTDCLO");
        numeroAvsPrecedant = statement.dbReadString("KTNAVP");
        caisseTenantCI = statement.dbReadString("KTCAIT");
        traite = statement.dbReadBoolean("KTBTRA");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KTIIND",
                _dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KTNAVS", _dbWriteString(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("KTLNOM", _dbWriteString(statement.getTransaction(), getNomPrenom(), "nomPrenom"));
        statement
                .writeField("KTIPAY", _dbWriteNumeric(statement.getTransaction(), getPaysOrigineId(), "paysOrigineId"));
        statement.writeField("KTNOUV",
                _dbWriteNumeric(statement.getTransaction(), getAnneeOuverture(), "anneeOuverture"));
        statement.writeField("KTBOUV",
                _dbWriteBoolean(statement.getTransaction(), isCiOuvert(), BConstants.DB_TYPE_BOOLEAN_CHAR, "ciOuvert"));
        statement.writeField("KTIARC",
                _dbWriteNumeric(statement.getTransaction(), getDernierMotifCloture(), "dernierMotifCloture"));
        statement.writeField("KTOARC",
                _dbWriteNumeric(statement.getTransaction(), getDernierMotifOuverture(), "dernierMotifOuverture"));
        statement.writeField("KTICAI",
                _dbWriteNumeric(statement.getTransaction(), getDerniereCaisse(), "derniereCaisse"));
        statement
                .writeField(
                        "KTDCLO",
                        _dbWriteDateAMJ(statement.getTransaction(), getDateBidouillee(getDerniereCloture()),
                                "derniereCloture"));
        statement.writeField("KTNAVP",
                _dbWriteString(statement.getTransaction(), getNumeroAvsPrecedant(), "numeroAvsPrecedant"));
        statement.writeField("KTCAIT",
                _dbWriteNumeric(statement.getTransaction(), getCaisseTenantCI(), "caisseTenantCI"));
    }

    /**
     * @return
     */
    public String getAnneeOuverture() {
        return anneeOuverture;
    }

    /**
     * @return
     */
    public String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    /**
     * @return
     */
    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    /**
     * Bidouille la date donnée du type MMAA ou JJMMAA pour générer une date du type JJMMAAAA.<br>
     * Régle: si AA >= 48, retourner JJMM19AA(01MM19AA), sinon retourner JJMM20AA(01MM20AA) Note: cette méthode va bien
     * évidemment fonctionner que jusqu'en 2047 :-(. Date de création : (13.12.2002 14:11:12)
     * 
     * @return la date bidouillée
     * @param date
     *            la date à bidouiller
     */
    protected String getDateBidouillee(String date) {
        if (date == null) {
            return "";
        }
        int posAnnee = 0;
        String prefix = "";
        if (date.trim().length() == 4) {
            posAnnee = 2;
            prefix = "01";
        } else if (date.trim().length() == 6) {
            posAnnee = 4;
        } else {
            return "";
        }
        int annee = Integer.parseInt(date.substring(posAnnee));
        if (annee >= 48) {
            return prefix + date.substring(0, posAnnee) + "19" + date.substring(posAnnee);
        } else {
            return prefix + date.substring(0, posAnnee) + "20" + date.substring(posAnnee);
        }
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
    public String getDernierMotifCloture() {
        return dernierMotifCloture;
    }

    /**
     * @return
     */
    public String getDernierMotifOuverture() {
        return dernierMotifOuverture;
    }

    /**
     * * @return
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
    public String getNumeroAvsPrecedant() {
        return numeroAvsPrecedant;
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
    public Boolean getTraite() {
        return traite;
    }

    /**
     * @return
     */
    public Boolean isCiOuvert() {
        return ciOuvert;
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
    public void setCaisseTenantCI(String string) {
        caisseTenantCI = string;
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
    public void setDateCreation(String string) {
        dateCreation = string;
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
    public void setTraite(Boolean boolean1) {
        traite = boolean1;
    }

}
