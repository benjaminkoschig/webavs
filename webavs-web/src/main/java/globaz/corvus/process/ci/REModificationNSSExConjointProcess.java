package globaz.corvus.process.ci;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.naos.process.statOfas.AFStatistiquesOfasProcess;
import globaz.pyxis.util.TIToolBox;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mmo
 * 
 *         Ce processus met à jour le nss de l'ex-conjoint dans les écritures de splitting (8-18) pour un rassemblement
 *         donné et une période donnée
 * 
 */

public class REModificationNSSExConjointProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String REQUETE_SELECT_INSCRIPTION_CI_NSS_FAUX_COL_NAME_ID_INSCRIPTION_CI = "ID_INSCRIPTION_CI";

    private String anneeDebut;
    private String anneeFin;
    private String eMailAddress;
    private String idRassemblement;
    private List<String> listIdInscriptionCINSSFaux;
    private String nouveauNSS;
    private String schemaDBWithTablePrefix;

    public REModificationNSSExConjointProcess() {
        super();
        schemaDBWithTablePrefix = TIToolBox.getCollection();
        anneeDebut = "";
        anneeFin = "";
        eMailAddress = "";
        idRassemblement = "";
        nouveauNSS = "";
        listIdInscriptionCINSSFaux = new ArrayList<String>();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean success = true;
        String errorMessage = "";

        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            setSendCompletionMail(false);
            extraireIdInscriptionCINSSFaux();
            updateInscriptionCINSSFaux();

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

            // Pas modifier sans voir REModificationNSSExConjointHelper._start
            if (!success || isAborted() || isOnError() || getSession().hasErrors()) {

                success = false;
                this._addError(getTransaction(), errorMessage);
                getSession().addError(errorMessage);
                abort();

            }
        }

        return success;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlankOrZero(idRassemblement)) {
            getSession().addError(
                    getSession().getLabel("PROCESS_MODIFICATION_NSS_EXCONJOINT_ID_RASSEMBLEMENT_MANDATORY"));
        }

        boolean isAnneeDebutValide = true;
        if (!JadeDateUtil.isGlobazDateYear(anneeDebut)) {
            isAnneeDebutValide = false;
            getSession().addError(getSession().getLabel("PROCESS_MODIFICATION_NSS_EXCONJOINT_ANNEE_DEBUT_MANDATORY"));
        }

        boolean isAnneeFinValide = true;
        if (!JadeDateUtil.isGlobazDateYear(anneeFin)) {
            isAnneeFinValide = false;
            getSession().addError(getSession().getLabel("PROCESS_MODIFICATION_NSS_EXCONJOINT_ANNEE_FIN_MANDATORY"));
        }

        if (isAnneeDebutValide && isAnneeFinValide
                && (Integer.valueOf(anneeDebut).intValue() > Integer.valueOf(anneeFin).intValue())) {
            getSession().addError(
                    getSession().getLabel("PROCESS_MODIFICATION_NSS_EXCONJOINT_ANNEE_DEBUT_APRES_ANNEE_FIN"));
        }

        if (!NSUtil.nssCheckDigit(nouveauNSS) || (nouveauNSS.indexOf(".") != -1)) {
            getSession().addError(getSession().getLabel("PROCESS_MODIFICATION_NSS_EXCONJOINT_NSS_MANDATORY"));
        }

        // Pas modifier sans voir REModificationNSSExConjointHelper._start
        if (getSession().hasErrors()) {
            abort();
        }

    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException {

        Statement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<String, String>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getString(i));
                }

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(AFStatistiquesOfasProcess.class,
                            "Problem to close statement in AFStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    private void extraireIdInscriptionCINSSFaux() {

        String sqlQueryInscriptionCINSSFaux = getSqlInscriptionCINSSFaux();

        List<Map<String, String>> listMapResultQueryInscriptionCINSSFaux = null;

        try {
            listMapResultQueryInscriptionCINSSFaux = executeQuery(sqlQueryInscriptionCINSSFaux);
        } catch (JadePersistenceException e) {
            throw new RETechnicalException("Unabled to find inscription CI with false NSS", e);
        }

        // Parcours de chaque ligne retournée par la requête sql
        for (Map<String, String> aMapRowResultQueryInscriptionCINSSFaux : listMapResultQueryInscriptionCINSSFaux) {
            listIdInscriptionCINSSFaux
                    .add(aMapRowResultQueryInscriptionCINSSFaux
                            .get(REModificationNSSExConjointProcess.REQUETE_SELECT_INSCRIPTION_CI_NSS_FAUX_COL_NAME_ID_INSCRIPTION_CI));
        }

    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    protected String getEMailObject() {

        // pas de mail envoyé
        return "";

    }

    public String getIdRassemblement() {
        return idRassemblement;
    }

    public String getNouveauNSS() {
        return nouveauNSS;
    }

    private String getSqlInscriptionCINSSFaux() {

        String sql = " select distinct anno.ZBIAIN as "
                + REModificationNSSExConjointProcess.REQUETE_SELECT_INSCRIPTION_CI_NSS_FAUX_COL_NAME_ID_INSCRIPTION_CI
                + " from schema.REINSCI insc inner join schema.REANICI anno on insc.YPIARC = anno.ZBIAIN "
                + " where  anno.ZBAGCO = '8' and insc.YPIRCI = " + idRassemblement + " and anno.ZBLACO <= '" + anneeFin
                + "' and anno.ZBLACO >= '" + anneeDebut + "' ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {

        sqlQuery = sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);

        return sqlQuery;
    }

    private REAnnonceInscriptionCI retrieveAnnonceInscriptionCI(String idInscriptionCINSSFaux) {

        REAnnonceInscriptionCI annonceInscriptionCI = new REAnnonceInscriptionCI();
        annonceInscriptionCI.setSession(getSession());
        annonceInscriptionCI.setId(idInscriptionCINSSFaux);

        try {
            annonceInscriptionCI.retrieve(getTransaction());
        } catch (Exception e) {
            throw new RETechnicalException("Unable to retrieve annonce inscription CI (id : " + idInscriptionCINSSFaux
                    + ")", e);
        }
        if (!annonceInscriptionCI.isNew()) {
            return annonceInscriptionCI;
        }

        return null;

    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setIdRassemblement(String idRassemblement) {
        this.idRassemblement = idRassemblement;
    }

    public void setNouveauNSS(String nouveauNSS) {
        this.nouveauNSS = nouveauNSS;
    }

    private void updateInscriptionCINSSFaux() {

        for (String idInscriptionCINSSFaux : listIdInscriptionCINSSFaux) {

            REAnnonceInscriptionCI annonceInscriptionCI = retrieveAnnonceInscriptionCI(idInscriptionCINSSFaux);

            if (annonceInscriptionCI != null) {

                annonceInscriptionCI.setNoAffilie(nouveauNSS);
                try {
                    annonceInscriptionCI.update(getTransaction());
                } catch (Exception e) {
                    throw new RETechnicalException("Unabled to update annonce inscription CI (id : "
                            + idInscriptionCINSSFaux + ")", e);
                }
            }
        }

    }
}
