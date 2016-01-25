package globaz.corvus.api.arc.downloader;

import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.RECompteIndividuelManager;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.common.Jade;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe fournissant la vérification qu'il y ait une seule entrée dans RECI par tiers. Les éventuels doublons sont
 * ajoutés à un log fourni en paramètre
 * 
 * @author AME
 */
public class RETiersDuplicationDansReciReporter {
    private static final String DoublonExtractionSqlQuery = "SELECT YNITIE as ID_TIERS FROM %s.RECI GROUP BY YNITIE HAVING COUNT(YNITIE) > 1";

    /**
     * @param session
     *            La session considérée
     * @param transaction
     *            La transaction considérée
     * @param log
     *            Le log à alimenter en cas de problème
     * @throws Exception
     */
    public static void report(BSession session, BTransaction transaction, FWMemoryLog log) throws Exception {
        RETiersDuplicationDansReciReporter reporter = new RETiersDuplicationDansReciReporter(session, transaction, log);

        reporter._report();
    }

    private final FWMemoryLog _log;

    private final BSession _session;

    private final BTransaction _transaction;

    /**
     * Constructeur de la vérification. Celui-ci reste protected car le traitement est initié via la méthode statique
     * report.
     * 
     * @see #report(BSession,BTransaction,FWMemoryLog)
     * 
     * @param session
     *            La session considérée
     * @param transaction
     *            La transaction considérée
     * @param log
     *            Le log à alimenter en cas de problème
     */
    protected RETiersDuplicationDansReciReporter(BSession session, BTransaction transaction, FWMemoryLog log) {
        _log = log;
        _session = session;
        _transaction = transaction;
    }

    /**
     * Execution de la vérification
     * 
     * @throws Exception
     */
    private void _report() throws Exception {

        for (String idTiers : getDuplicatedTiersInReci()) {

            logInformation(FWMessageFormat.format(_session.getLabel("INFO_RECI_CONTIENT_PLUSIEURS_FOIS_MEME_TIERS"),
                    idTiers));

            for (RECompteIndividuel ciLoop : getReciEntriesByTiers(idTiers)) {

                String occurence = FWMessageFormat.format(_session.getLabel("INFO_OCCURENCE_TIERS_DANS_RECI"),
                        ciLoop.getIdCi(), ciLoop.getCreationDate(), ciLoop.getCreationTime(), ciLoop.getCreationUser());

                logInformation(occurence);
            }
        }
    }

    /**
     * Recherche les tiers ayant plusieurs comptes individuels (plusieurs entrées dans la table RECI)
     * 
     * @return La liste des id tiers ayant plus d'une entrée dans la table RECI
     * @throws Exception
     */
    private Iterable<String> getDuplicatedTiersInReci() throws Exception {
        ArrayList<String> output = new ArrayList<String>();
        String schema = Jade.getInstance().getDefaultJdbcSchema();
        BStatement statement = new BStatement(_transaction);
        statement.createStatement();

        String query = String.format(RETiersDuplicationDansReciReporter.DoublonExtractionSqlQuery, schema);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String idTiers = resultSet.getString("ID_TIERS");

            output.add(idTiers);
        }

        statement.closeStatement();

        return output;
    }

    /**
     * Recherche tous les comptes individuels associés à un tiers donné. Le fait d'en avoir plusieurs constitue une
     * erreur.
     * 
     * @param idTiers
     *            L'id du tiers considéré
     * @return La liste des comptes individuels associés au tiers
     * @throws Exception
     *             Si la recherche a échouée
     */
    private Iterable<RECompteIndividuel> getReciEntriesByTiers(String idTiers) throws Exception {
        RECompteIndividuelManager manager = new RECompteIndividuelManager();
        manager.setSession(_session);
        manager.setForIdTiers(idTiers);
        manager.find(_transaction, 10);

        return manager;
    }

    /**
     * Ajoute le texte en argument dans le log en tant qu'information
     * 
     * @param information
     *            Le texte à logger
     */
    private void logInformation(String information) {
        if (_log != null) {
            _log.logMessage(information, FWMessage.INFORMATION, this.getClass().getName());
        }
    }
}
