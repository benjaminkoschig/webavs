package ch.globaz.al.businessimpl.documents;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération de protocole pour la génération de prestations
 * 
 * @author jts
 * 
 */
public abstract class AbstractProtocoleErreurs extends AbstractProtocole {

    /**
     * Permet de définir un label qui s'affichera en face de chaque élément de la liste d'erreur.
     * 
     * Exemple : Dans le protocole de génération cela permet d'afficher "Dossier : " avant le numéro du dossier
     * 
     * @return id du label à utiliser, Peut retourner <code>null</code> si aucun label ne doit être utilisé
     */
    protected String getErrorListItemLabelId() {
        // Méthode à surcharger dans les classes enfants si nécessaire
        return null;
    }

    /**
     * Initialise le document en appelant les méthodes suivantes :
     * 
     * <ul>
     * <li>super.init(params)</li>
     * <li>setTitles</li>
     * <li>setStatsLabels</li>
     * <li>setStatsValues</li>
     * <li>setTable</li>
     * <li>setTableFatal</li>
     * </ul>
     * 
     * @param logger
     *            Instance du logger contenant les messages à afficher sur le protocole
     * @param params
     *            Paramètres tels que le numéro de passage, périodes, etc.
     * @return Le document initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DocumentData init(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException {
        DocumentData document = super.init(params);
        setTitles(document);
        setStatsLabels(document);
        setStatsValues(document, logger);

        setTable(document, ALConstProtocoles.TABLE_ERRORS, logger.getErrorsContainer());
        setTable(document, ALConstProtocoles.TABLE_WARNINGS, logger.getWarningsContainer());
        setTableFatal(document, logger.getFatalErrorsContainer());

        return document;
    }

    /**
     * Définir les libellé apparaissant devant les valeurs statistiques. Peut être surchargée pour tenir compte des
     * labels spécifiques aux protocoles
     * 
     * La variables de template suivantes doivent être définies :
     * 
     * <ul>
     * <li>protocole_stats_nbinfo_label</li>
     * <li>protocole_stats_nberr_label</li>
     * <li>protocole_stats_nbwarn_label</li>
     * </ul>
     * 
     * @param document
     *            Document auquel ajouter les labels
     * @throws JadeApplicationException
     *             Exception levée si <code>document</code> et <code>null</code>
     */
    protected void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleGenerationServiceImpl#setStatsLabels : document is null");
        }

        document.addData("protocole_stats_nbinfo_label", this.getText("al.protocoles.compensation.stats.nbinfo.label"));
        document.addData("protocole_stats_nberr_label", this.getText("al.protocoles.commun.stats.nberr.label"));
        document.addData("protocole_stats_nbwarn_label", this.getText("al.protocoles.commun.stats.nbwarn.label"));
    }

    /**
     * Ajoute les statistique de génération dans le document
     * 
     * @param document
     *            document dans lequel placer les données
     * @param logger
     *            Instance de <code>ProtocoleLogger</code> contenant les messages à afficher sur le protocole
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    protected void setStatsValues(DocumentData document, ProtocoleLogger logger) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#setStatsValues : document is null");
        }

        document.addData("protocole_stats_nbinfo_value",
                JANumberFormatter.fmt(String.valueOf(logger.getInfosContainer().size()), true, false, false, 0));
        document.addData("protocole_stats_nberr_value",
                JANumberFormatter.fmt(String.valueOf(logger.getErrorsContainer().size()), true, false, false, 0));
        document.addData("protocole_stats_nbwarn_value",
                JANumberFormatter.fmt(String.valueOf(logger.getWarningsContainer().size()), true, false, false, 0));
    }

    /**
     * @param document
     *            document dans lequel placer les données
     * @param tableName
     *            nom du tableau (tableAvertissements ou tableErreurs)
     * @param errorsLogs
     *            Log provenant de <code>GenerationLogger</code>
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     * 
     * @see ch.globaz.al.business.loggers.ProtocoleLogger
     */
    protected void setTable(DocumentData document, String tableName, HashMap<String, ProtocoleLogger> errorsLogs)
            throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#setTable : document is null");
        }

        if (JadeStringUtil.isEmpty(tableName)) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#setTable : tableName is empty");
        }

        Collection tabErreurs = new Collection(tableName);

        JadeBusinessMessageRenderer renderer = JadeBusinessMessageRenderer.getInstance();

        for (ProtocoleLogger errorsLog : errorsLogs.values()) {

            StringBuffer sb = new StringBuffer();

            if (!JadeStringUtil.isEmpty(getErrorListItemLabelId())) {
                sb.append(this.getText(getErrorListItemLabelId())).append(" : ");
            }

            sb.append(errorsLog.getIdItem()).append(" ").append(errorsLog.getNomItem());

            DataList items = new DataList("element");
            items.addData("protocole_diagnostique_element", sb.toString());
            tabErreurs.add(items);

            Collection listErr = new Collection("erreurs");

            for (JadeBusinessMessage message : errorsLog.getMessages()) {

                DataList err = new DataList("erreur");

                err.addData("protocole_message",
                        renderer.getDefaultAdapter().renderMessage(message, JadeThread.currentContext().getLanguage()));
                listErr.add(err);
            }

            tabErreurs.add(listErr);
        }

        document.add(tabErreurs);
    }

    /**
     * Défini le tableau d'erreurs
     * 
     * @param document
     *            document dans lequel placer les données
     * @param errorsLog
     *            Listes des messages d'erreur
     * @throws JadeApplicationException
     *             Exception levée si le log d'erreurs ne peut être récupéré
     */
    protected void setTableFatal(DocumentData document, ArrayList<JadeBusinessMessage> errorsLog)
            throws JadeApplicationException {
        Collection tabErreurs = new Collection("tableFatal");

        JadeBusinessMessageRenderer renderer = JadeBusinessMessageRenderer.getInstance();

        if (errorsLog.size() > 0) {
            document.addData("fatal_error_info", this.getText("al.protocoles.erreurs.titre.diagnostique.fatal.info"));
        }

        for (JadeBusinessMessage message : errorsLog) {
            DataList dossier = new DataList("fatalerror");
            dossier.addData("protocole_fatalerror_message",
                    renderer.getDefaultAdapter().renderMessage(message, JadeThread.currentContext().getLanguage()));
            tabErreurs.add(dossier);
        }

        document.add(tabErreurs);
    }

    /**
     * Ajoute les titres du protocole
     * 
     * @param document
     *            document dans lequel placer les données
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    protected void setTitles(DocumentData document) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#setTitles : document is null");
        }

        document.addData("protocole_titre_stats", this.getText("al.protocoles.erreurs.titre.stats"));

        document.addData("protocole_titre_diagnostique", this.getText("al.protocoles.erreurs.titre.diagnostique"));

        document.addData("protocole_fatal_titre", this.getText("al.protocoles.erreurs.titre.diagnostique.fatal"));

        document.addData("protocole_erreurs_titre", this.getText("al.protocoles.erreurs.titre.diagnostique.erreurs"));

        document.addData("protocole_avertissements_titre",
                this.getText("al.protocoles.erreurs.titre.diagnostique.avertissements"));

    }
}
