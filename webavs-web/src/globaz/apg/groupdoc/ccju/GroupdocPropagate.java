package globaz.apg.groupdoc.ccju;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.stat.JadeStatistics;
import globaz.jade.stat.JadeStatisticsRecord;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * <code>GroupdocPropagate</code> est un service externe qui crée/met à jour les dossiers des tiers dans Groupdoc pour
 * la CCJU.
 * 
 * @author Emmanuel Fleury
 */
public class GroupdocPropagate extends BAbstractEntityExternalService {
    // commonProperties
    public final static String PROPERTY_ADAPTER_NAME = "adapterName";
    public final static String PROPERTY_VERBOSE = "verbose";

    /**
     * Parcourt tous les tiers et lance la création/mise à jour des dossiers GroupDoc
     * 
     * @param args
     *            les paramètres suivants: user password first_id
     */
    private static int _propagateList(String user, String pwd) throws Throwable {
        String[] affilies = new String[] { "103.1002", "123.1108", "123.1163", "124.1099", "135.1018", "145.1037",
                "153.1086", "155.1008", "163.1247", "245.1002", "251.1201", "259.1006", "260.1161", "260.1418",
                "261.1199", "261.1255", "267.1043", "268.1002", "268.1004", "268.1005", "268.1091", "268.1111",
                "270.1082", "272.1067", "272.1284", "280.1018", "288.1075", "289.1003", "307.1001", "317.1004",
                "327.1001", "351.1004", "351.1025", "351.1051", "351.1108", "354.1006", "354.1016", "354.1021",
                "385.1030", "395.1106", "417.1012", "448.1001", "478.1002", "482.1011", "482.1018", "495.1006",
                "502.1001", "502.1037", "503.1041", "516.1094", "524.1001", "592.1001", "646.1048", "677.1007",
                "679.1071", "696.1030", "711.1103", "722.1300", "726.1020", "729.1011", "737.1073", "739.1004",
                "784.1080", "784.1082", "784.1087", "789.1001", "877.1023", "878.1076", "881.1206", "890.1036",
                "919.1011", "962.1003" };

        BSession session = new BSession(TIApplication.DEFAULT_APPLICATION_PYXIS);
        System.out.println("-> connexion...");
        session.connect(user, pwd);
        System.out.println("-> connexion OK");
        for (int i = 0; i < affilies.length; i++) {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session, affilies[i]);
            GroupdocPropagateUtil.propagateData(affilie, null, null);
            System.out.println(affilies[i]);
        }
        return affilies.length;
    }

    /**
     * Parcourt tous les tiers et lance la création/mise à jour des dossiers GroupDoc
     * 
     * @param args
     *            les paramètres suivants: user password first_id
     */
    private static int _propagateTiers(String user, String pwd, String first_id) throws Throwable {
        TIPersonneAvsManager manager = new TIPersonneAvsManager();
        BSession session = new BSession(TIApplication.DEFAULT_APPLICATION_PYXIS);
        System.out.println("-> connexion...");
        session.connect(user, pwd);
        System.out.println("-> connexion OK");
        BTransaction transaction = null;
        BStatement statement = null;
        int count = 0;
        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            manager.setFromIdTiers(first_id);
            statement = manager.cursorOpen(transaction);
            GroupdocPropagate groupdoc = new GroupdocPropagate();
            TITiersViewBean entity = null;
            System.out.println("-> démarrage de la propagation");
            long start = System.currentTimeMillis();
            while ((entity = (TITiersViewBean) manager.cursorReadNext(statement)) != null) {
                groupdoc._propagateData(entity);
                count++;
                if ((count % 100) == 0) {
                    System.out.println(count + " entités en " + (System.currentTimeMillis() - start) / 1000
                            + " secondes");
                }
            }
            manager.cursorClose(statement);
        } catch (Throwable t) {
            throw t;
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            }
        }

        return count;
    }

    /**
     * Parcourt tous les tiers et lance la création/mise à jour des dossiers GroupDoc
     * 
     * @param args
     *            les paramètres suivants: user password first_id
     */
    public static void main(String[] args) {
        int count = 0;
        try {
            if ((args.length != 2) && (args.length != 3)) {
                System.out.println("Ce programme attend 2 ou 3 arguments: user password [first_id]");
                System.exit(-1);
            }
            Jade.getInstance();
            JadeStatistics.startTracker();
            if (args.length == 3) {
                count = _propagateTiers(args[0], args[1], args[2]);
            } else {
                count = _propagateList(args[0], args[1]);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("-> " + count + " entités traitées");
            System.out.println("****************************************");
            System.out.println("* STATISTIQUES                         *");
            System.out.println("****************************************");
            System.out.println("TOKEN\tTOTAL\tCOUNT\tAVG");
            Collection records = JadeStatistics.getInstance().getRecords();
            for (Iterator iter = records.iterator(); iter.hasNext();) {
                JadeStatisticsRecord record = (JadeStatisticsRecord) iter.next();
                System.out.println(record.getToken() + "\t" + record.getTotalTime() + "\t" + record.getCount() + "\t"
                        + record.getAverageTime());
            }
        }
        System.exit(0);
    }

    /**
     * Exécute un service externe après ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    protected void _propagateData(BEntity entity) throws Throwable {
        try {
            // TODO supprimer setVerbose(true);
            setVerbose(true);
            if (APSituationProfessionnelle.class.isAssignableFrom(entity.getClass())) {
                // on cree un dossier xxx.xxxx-xxx.xx.xxx.xxx-A07 car c'est un
                // employé
                APSituationProfessionnelle situationProfessionnelle = (APSituationProfessionnelle) entity;
                APEmployeur employeur = situationProfessionnelle.loadEmployeur();
                if (employeur != null) {
                    IPRAffilie affilie = employeur.loadAffilie();
                    if (affilie != null) {
                        APDroitLAPG droit = new APDroitLAPG();
                        droit.setSession(entity.getSession());
                        droit.setIdDroit(situationProfessionnelle.getIdDroit());
                        droit.retrieve();
                        if (!JadeStringUtil.isBlank(droit.getIdDemande())) {
                            PRDemande demande = droit.loadDemande();
                            if (!JadeStringUtil.isBlank(demande.getIdTiers())) {
                                PRTiersWrapper allocataire = demande.loadTiers();
                                if (allocataire != null) {
                                    GroupdocPropagateUtil.propagateData(affilie, allocataire, droit.getDateReception());
                                } else {
                                    if (isVerbose()) {
                                        JadeLogger.warn(this, "Allocataire(" + demande.getIdTiers() + ") for Demande("
                                                + demande.getId() + ") not found");
                                    }
                                }
                            } else {
                                if (isVerbose()) {
                                    JadeLogger.warn(this,
                                            "Demande(" + droit.getIdDemande() + ") for Droit(" + droit.getId()
                                                    + ") not found");
                                }
                            }
                        } else {
                            if (isVerbose()) {
                                JadeLogger.warn(this, "Droit(" + situationProfessionnelle.getIdDroit()
                                        + ") for SituationProfessionnelle(" + situationProfessionnelle.getId()
                                        + ") not found");
                            }
                        }
                    } else {
                        if (isVerbose()) {
                            JadeLogger.warn(this, "Affilie(" + employeur.getIdAffilie() + ") for Employeur("
                                    + employeur.getId() + ") not found");
                        }
                    }
                } else {
                    if (isVerbose()) {
                        JadeLogger.warn(this, "Employeur(" + situationProfessionnelle.getIdEmployeur()
                                + ") for SituationProfessionnelle(" + situationProfessionnelle.getId() + ") not found");
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, "Could not propagate data for " + entity + ": " + e.toString());
        }
    }

    /**
     * Exécute un service externe après ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if (_isAfterAdd(entity)) {
            if (isVerbose()) {
                JadeLogger.info(this, "afterAdd(" + entity.getClass().getName() + ")");
            }
            _propagateData(entity);
        }
    }

    /**
     * Exécute un service externe après suppression d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après chargement d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après mise à jour d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        if (_isAfterUpdate(entity)) {
            if (isVerbose()) {
                JadeLogger.info(this, "afterUpdate(" + entity.getClass().getName() + ")");
            }
            _propagateData(entity);
        }
    }

    /**
     * Exécute un service externe avant ajout d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant suppression d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant chargement d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant mise à jour d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe pour initialiser une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity entity) throws Throwable {
    }

    /**
     * Définit les propriétés générales du service.
     * 
     * @param properties
     *            les propriétés générales du service
     */
    public final void setCommonProperties(Properties properties) {
        GroupdocPropagateUtil.setShouldPropagate(true);
        GroupdocPropagateUtil.setVerbose(false);
        if (properties == null) {
            return;
        }
        GroupdocPropagateUtil.setVerbose(JadeStringUtil.parseBoolean(properties.getProperty(PROPERTY_VERBOSE), false));
        GroupdocPropagateUtil.setAdapterName(properties.getProperty(PROPERTY_ADAPTER_NAME));
    }

    /**
     * Exécute un service externe pour valider le contenu d'une entité.
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
