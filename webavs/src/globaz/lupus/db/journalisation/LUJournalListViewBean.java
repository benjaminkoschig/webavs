/*
 * Créé le 6 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.JOCommonLoggedEntity;
import globaz.journalisation.db.journalisation.access.IJOComplementJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOGroupeJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.JOJournalisationManager;
import globaz.lupus.db.data.LUProvenanceDataSource;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUJournalListViewBean extends JOJournalisationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] argv) {
        try {
            LUJournalListViewBean manager = new LUJournalListViewBean();
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession("globazf", "ssiiadm");
            manager.setSession(session);
            manager.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String COMPLEMENTJOURNAL = "COMPLEMENTJOURNAL.";
    private String forCsTypeCodeSysteme = new String();
    private String forCsTypeJournal = new String();
    private String forDate = new String();
    private String forDateRappel = new String();
    private String forDateReception = new String();
    private String forIdInitial = new String();
    // ----------------------------------------------------------------------
    private String forIdJournalisation = new String();
    private String forIdLot = new String();
    private String forIdPrecedent = new String();
    private boolean forJournalInitial = false;
    private String forLibelle = new String();
    private boolean forNotJournalInitial = false;
    private String forTypeDocument = new String();
    private String forTypeReferenceProvenance = new String();
    private String forUser = new String();
    private String forUserName = new String();
    private String forValeurCodeSysteme = new String();
    private String fromDate = new String();
    private String fromIdJournal = new String();
    private final String GROUPEJOURNAL = "GROUPEJOURNAL.";
    private final String JCJOTY = IJOComplementJournalDefTable.CSTYPECODESYSTEME;
    private final String JGJOID = IJOJournalisationDefTable.IDGROUPEJOURNAL;

    private final String JGJORA = IJOGroupeJournalDefTable.DATE_RAPPEL;

    private final String JGJORE = IJOGroupeJournalDefTable.DATE_RECEPTION;
    private final String JJOUDA = IJOJournalisationDefTable.DATE;
    /*
     * Définition de nos table et de nos champs
     */
    private final String JJOUID = IJOJournalisationDefTable.IDJOURNALISATION;
    private final String JJOUIN = IJOJournalisationDefTable.IDINITIAL;
    private final String JJOULI = IJOJournalisationDefTable.LIBELLE;
    private final String JJOUPR = IJOJournalisationDefTable.IDPRECEDENT;
    private final String JJOUSU = IJOJournalisationDefTable.IDSUIVANT;
    private final String JJOUTJ = IJOJournalisationDefTable.CSTYPEJOURNAL;
    private final String JJOUUT = IJOJournalisationDefTable.IDUTILISATEUR;
    private final String JOJPCJO = IJOComplementJournalDefTable.TABLE_NAME;
    private final String JOJPGJO = IJOGroupeJournalDefTable.TABLE_NAME;
    private final String JOJPJOU = IJOJournalisationDefTable.TABLE_NAME;
    private final String JOJPREP = IJOReferenceProvenanceDefTable.TABLE_NAME;
    private final String JOURNALISATION = "JOURNALISATION.";
    private final String JREPIP = IJOReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE;
    private final String JREPTY = IJOReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE;
    private String orderby;
    private LUProvenanceDataSource provenance = new LUProvenanceDataSource();
    private final String sep = " , ";
    private String untilDate = new String();
    private boolean wantComplementJournal = true;
    private boolean wantGroupeJournal = true;

    /*
     * Séléction des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlSelect = new StringBuffer();

        // On séléctionne les champs qui nous intéresse
        sqlSelect.append(" ( SELECT " + JOURNALISATION + JJOUID + sep + JJOUDA + sep + JJOULI + sep + JJOUUT + sep
                + JJOUTJ + sep + JJOUIN + sep + JJOUPR + sep + JJOUSU + sep + JOURNALISATION + JGJOID);

        if (wantGroupeJournal) {
            sqlSelect.append(sep + JGJORA + sep + JGJORE + sep + " CASE WHEN " + GROUPEJOURNAL + JGJORE + " = 0 THEN "
                    + JOURNALISATION + JJOUDA + " WHEN " + GROUPEJOURNAL + JGJORE + " <>0 THEN " + GROUPEJOURNAL
                    + JGJORE + " END AS TRIE ");
            // On fait la jointure sur groupe journal et complément journal
        }
        sqlSelect.append(" FROM " + _getCollection() + JOJPJOU + " JOURNALISATION ");
        if (wantGroupeJournal) {
            sqlSelect.append(" LEFT OUTER JOIN " + _getCollection() + JOJPGJO + " GROUPEJOURNAL ON " + GROUPEJOURNAL
                    + JGJOID + " = " + JOURNALISATION + JGJOID);
        }
        if (wantComplementJournal) {
            sqlSelect.append(" LEFT OUTER JOIN " + _getCollection() + JOJPCJO + " COMPLEMENTJOURNAL ON "
                    + COMPLEMENTJOURNAL + JJOUID + " = " + JOURNALISATION + JJOUID);

        }

        // On fait la jointure si on a une liste de provenance
        if (getProvenance().size() > 0) {
            sqlSelect.append(" INNER JOIN " + " (SELECT " + JJOUID + " FROM " + _getCollection() + JOJPREP + " WHERE "
                    + JJOUID);
            for (int i = 0; i < getProvenance().size(); i++) {
                sqlSelect.append(" IN(SELECT PROV" + i + "." + JJOUID + " FROM " + _getCollection() + JOJPREP + " PROV"
                        + i + " WHERE " + JREPTY + " = '" + getProvenance().getProvenance(i).getCsType() + "'"
                        + " AND " + JREPIP + " = '" + getProvenance().getProvenance(i).getValeur() + "'");
                if (i != getProvenance().size() - 1) {
                    sqlSelect.append(" AND PROV" + i + "." + JJOUID);
                }
            }
            for (int i = 0; i < getProvenance().size(); i++) {
                sqlSelect.append(")");
            }
            sqlSelect.append(" GROUP BY " + JJOUID + ") AS T1 ON " + JOURNALISATION + JJOUID + " = " + " T1." + JJOUID);
        }

        return sqlSelect.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isBlank(getOrderby())) {
            return " CASE WHEN " + JGJORA + " = 0 THEN 99999999 ELSE " + JGJORA + " END " + sep + JGJOID + /*
                                                                                                            * sep +
                                                                                                            * JJOUDA +
                                                                                                            */sep
                    + JJOUID;
        } else {
            return getOrderby();
        }
    }

    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(_getFields(statement)).append(" FROM ").append(_getFrom(statement));
        if (_getWhere(statement).length() > 0) {
            sql.append(" WHERE ").append(_getWhere(statement));
        } else {
            sql.append(" ) AS JOURNALISATION ");
        }
        sql.append(" ORDER BY ").append(_getOrder(statement));
        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // sqlWhere.append(COMPLEMENTJOURNAL + JCJOTY + " = 16000007 ");

        // pour l id Lot
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" JOURNALISATION.JLOTID = " + _dbWriteNumeric(statement.getTransaction(), getForIdLot()));
        }
        if (!JadeStringUtil.isBlank(getForDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" JOURNALISATION.JJOUDA = " + _dbWriteDateAMJ(statement.getTransaction(), getForDate()));
        }

        if (isForJournalInitial()) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.IDINITIAL).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), "0"));
        }
        if (isForNotJournalInitial()) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.IDINITIAL).append(">")
                    .append(_dbWriteNumeric(statement.getTransaction(), "0"));
        }
        // Date de création
        if (!JadeStringUtil.isBlank(getForDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.DATE + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDate()));
        }
        // Date de rappel
        if (!JadeStringUtil.isBlank(getForDateRappel())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateRappel()));
        }
        // Date de réception
        if (!JadeStringUtil.isBlank(getForDateReception())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RECEPTION + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateReception()));
        }
        // User
        if (!JadeStringUtil.isBlank(getForUserName())) {
            /*
             * FWSecureUser user = new FWSecureUser(); try { user = FWSecureUser.retrieveByUser(getSession(),
             * getForUserName()); } catch (Exception e) { // FIXME !!!!!!! e.printStackTrace(); }
             */
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR + " like "
                    + _dbWriteString(statement.getTransaction(), getForUserName() + "%"));
        }
        // Libelle
        if (!JadeStringUtil.isBlank(getForLibelle())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.LIBELLE + " like "
                    + _dbWriteString(statement.getTransaction(), getForLibelle() + "%"));
        }
        // Type document
        if (!JadeStringUtil.isBlank(getForTypeDocument())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME + " = " + getForTypeDocument());
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdSuivant())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.IDSUIVANT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdSuivant()));
        }
        if (!JadeStringUtil.isBlank(getForDateRappel())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateRappel()));
        }
        if (!JadeStringUtil.isBlank(untilDate)) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL).append("<=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getUntilDate()));
        }
        if (!JadeStringUtil.isBlank(getFromIdJournal())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.IDJOURNALISATION).append(">")
                    .append(_dbWriteNumeric(statement.getTransaction(), getFromIdJournal()));
        }
        if (!JadeStringUtil.isBlank(getForIdGroupeJournal())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("JOURNALISATION." + IJOCommonJournalisationDefTable.IDGROUPEJOURNAL).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdGroupeJournal()));
        }
        // ---------------------------------------------------------------------------------
        if (!JadeStringUtil.isBlank(getForIdJournalisation())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.IDJOURNALISATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdJournalisation()));
        }
        if (!JadeStringUtil.isBlank(getForIdInitial())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.IDINITIAL).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdInitial()));
        }
        if (!JadeStringUtil.isBlank(getForIdPrecedent())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.IDPRECEDENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdPrecedent()));
        }
        if (!JadeStringUtil.isBlank(getForCsTypeJournal())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.CSTYPEJOURNAL).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsTypeJournal()));
        }
        if (!JadeStringUtil.isBlank(getFromDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.DATE).append(">=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getFromDate()));
        }
        if (!JadeStringUtil.isBlank(getForCsTypeCodeSysteme())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" COMPLEMENTJOURNAL." + IJOCommonComplementJournalDefTable.CSTYPECODESYSTEME).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsTypeCodeSysteme()));
        }
        if (!JadeStringUtil.isBlank(getForValeurCodeSysteme())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" COMPLEMENTJOURNAL." + IJOCommonComplementJournalDefTable.VALEURCODESYSTEME).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForValeurCodeSysteme()));
        }
        // if (!JadeStringUtil.isBlank(getForTypeReferenceProvenance())) {
        // if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
        // sqlWhere.append(" AND ");
        // }
        // sqlWhere.append(" PROVENANCE."+IJOReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE).append("=").append(_dbWriteNumeric(statement.getTransaction(),
        // getForCsTypeCodeSysteme()));
        // }
        if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
            sqlWhere.append(")AS JOURNALISATION ");
        }
        if (JadeStringUtil.isBlank(sqlWhere.toString())) {
            return "";
        } else {
            return sqlWhere.toString();
        }

    }

    public String getCollection() {
        return _getCollection();
    }

    /**
     * @return
     */
    public String getCOMPLEMENTJOURNAL() {
        return COMPLEMENTJOURNAL;
    }

    /**
     * @return
     */
    @Override
    public String getForCsTypeCodeSysteme() {
        return forCsTypeCodeSysteme;
    }

    /**
     * @return
     */
    @Override
    public String getForCsTypeJournal() {
        return forCsTypeJournal;
    }

    /**
     * critère de recherche sur la date de création
     */
    @Override
    public String getForDate() {
        return forDate;
    }

    /**
     * critère de recherche sur la date de rappel
     */
    @Override
    public String getForDateRappel() {
        return forDateRappel;
    }

    /**
     * critère de recherche sur la date de reception
     */
    @Override
    public String getForDateReception() {
        return forDateReception;
    }

    /**
     * @return
     */
    @Override
    public String getForIdInitial() {
        return forIdInitial;
    }

    /**
     * @return
     */
    @Override
    public String getForIdJournalisation() {
        return forIdJournalisation;
    }

    /**
     * @return
     */
    @Override
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * @return
     */
    @Override
    public String getForIdPrecedent() {
        return forIdPrecedent;
    }

    /**
     * critère de recherche sur le libelle
     */
    @Override
    public String getForLibelle() {
        return forLibelle;
    }

    /**
     * critère de recherche sur le type de document
     */
    public String getForTypeDocument() {
        return forTypeDocument;
    }

    /**
     * @return
     */
    @Override
    public String getForTypeReferenceProvenance() {
        return forTypeReferenceProvenance;
    }

    /**
     * critère de recherche sur l'utilisateur
     */
    public String getForUser() {
        return forUser;
    }

    /**
     * critère de recherche sur le user
     */
    public String getForUserName() {
        return forUserName;
    }

    /**
     * @return
     */
    @Override
    public String getForValeurCodeSysteme() {
        return forValeurCodeSysteme;
    }

    /**
     * @return
     */
    @Override
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    public String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * @return
     */
    public String getGROUPEJOURNAL() {
        return GROUPEJOURNAL;
    }

    /**
     * @return
     */
    public String getJCJOTY() {
        return JCJOTY;
    }

    /**
     * @return
     */
    public String getJGJOID() {
        return JGJOID;
    }

    /**
     * @return
     */
    public String getJGJORA() {
        return JGJORA;
    }

    /**
     * @return
     */
    public String getJGJORE() {
        return JGJORE;
    }

    /**
     * @return
     */
    public String getJJOUDA() {
        return JJOUDA;
    }

    /**
     * @return
     */
    public String getJJOUID() {
        return JJOUID;
    }

    /**
     * @return
     */
    public String getJJOUIN() {
        return JJOUIN;
    }

    /**
     * @return
     */
    public String getJJOULI() {
        return JJOULI;
    }

    /**
     * @return
     */
    public String getJJOUPR() {
        return JJOUPR;
    }

    /**
     * @return
     */
    public String getJJOUSU() {
        return JJOUSU;
    }

    /**
     * @return
     */
    public String getJJOUTJ() {
        return JJOUTJ;
    }

    /**
     * @return
     */
    public String getJJOUUT() {
        return JJOUUT;
    }

    /**
     * @return
     */
    public String getJOJPCJO() {
        return JOJPCJO;
    }

    /**
     * @return
     */
    public String getJOJPGJO() {
        return JOJPGJO;
    }

    /**
     * @return
     */
    public String getJOJPJOU() {
        return JOJPJOU;
    }

    /**
     * @return
     */
    public String getJOJPREP() {
        return JOJPREP;
    }

    /**
     * @return
     */
    public String getJOURNALISATION() {
        return JOURNALISATION;
    }

    /**
     * @return
     */
    public String getJREPIP() {
        return JREPIP;
    }

    /**
     * @return
     */
    public String getJREPTY() {
        return JREPTY;
    }

    /**
     * @return
     */
    public String getOrderby() {
        return orderby;
    }

    /**
     * retourne la liste des provenances
     */
    public LUProvenanceDataSource getProvenance() {
        return provenance;
    }

    /**
     * @return
     */
    public String getSep() {
        return sep;
    }

    /**
     * critère de recherche sur la date de création
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * @return
     */
    public boolean isForJournalInitial() {
        return forJournalInitial;
    }

    /**
     * @return
     */
    public boolean isForNotJournalInitial() {
        return forNotJournalInitial;
    }

    @Override
    protected JOCommonLoggedEntity newEntity() {
        return new LUJournalViewBean();
    }

    /**
     * @param string
     */
    @Override
    public void setForCsTypeCodeSysteme(String string) {
        forCsTypeCodeSysteme = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForCsTypeJournal(String string) {
        forCsTypeJournal = string;
    }

    /**
     * critère de recherche sur la date de création
     */
    @Override
    public void setForDate(String string) {
        forDate = string;
    }

    /**
     * critère de recherche sur la date de rappel
     */
    @Override
    public void setForDateRappel(String string) {
        forDateRappel = string;
    }

    /**
     * critère de recherche sur la date de réception
     */
    @Override
    public void setForDateReception(String string) {
        forDateReception = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForIdInitial(String string) {
        forIdInitial = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForIdJournalisation(String string) {
        forIdJournalisation = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForIdPrecedent(String string) {
        forIdPrecedent = string;
    }

    /**
     * @param b
     */
    public void setForJournalInitial(boolean b) {
        forJournalInitial = b;
    }

    /**
     * critère de recherche sur le libelle
     */
    @Override
    public void setForLibelle(String string) {
        forLibelle = string;
    }

    /**
     * @param b
     */
    public void setForNotJournalInitial(boolean b) {
        forNotJournalInitial = b;
    }

    /**
     * critère de recherche sur le type de document
     */
    public void setForTypeDocument(String string) {
        forTypeDocument = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForTypeReferenceProvenance(String string) {
        forTypeReferenceProvenance = string;
    }

    /**
     * critère de recherche sur le user
     */
    public void setForUser(String string) {
        forUser = string;
    }

    /**
     * critère de recherche sur le user
     */
    public void setForUserName(String string) {
        forUserName = string;
    }

    /**
     * @param string
     */
    @Override
    public void setForValeurCodeSysteme(String string) {
        forValeurCodeSysteme = string;
    }

    /**
     * @param string
     */
    @Override
    public void setFromDate(String string) {
        fromDate = string;
    }

    /**
     * @param string
     */
    public void setFromIdJournal(String string) {
        fromIdJournal = string;
    }

    /**
     * @param string
     */
    public void setOrderby(String string) {
        orderby = string;
    }

    /**
     * set la liste des provenances
     */
    public void setProvenance(LUProvenanceDataSource source) {
        provenance = source;
    }

    /**
     * @param string
     */
    public void setUntilDate(String string) {
        untilDate = string;
    }

    /**
     * @param b
     */
    public void wantComplementJournal(boolean b) {
        wantComplementJournal = b;

    }

    public void wantGroupeJournal(boolean b) {
        wantGroupeJournal = b;
    }

}
