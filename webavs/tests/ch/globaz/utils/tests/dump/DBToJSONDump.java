package ch.globaz.utils.tests.dump;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import ch.globaz.pegasus.tests.util.Init;
import ch.globaz.utils.tests.DBToJSONDumpConfig;

/**
 * Classe permettant de dumper des mod�les de bases de donn�es. Dans un fichier JSON. <br/>
 * <b>Arguments:</b> 0 --> nom pleinement qualifi� du mod�le (obligatoire), doit h�riter de <b>BEntity</b> ou de
 * <b>JadeAbstractModel</b> <br/>
 * 1 --> nom pleinement qualifi� du mod�le de recherche (optionnel), dans le cas ou le mod�le de recherche n'est pas
 * sp�cifi�, le nom du mod�le de rechechre sera deduit de cette mani�re: modelName+Search, ou modelName + Manager<br/>
 * 2 --> nombre d'entit�es d�sir�es (obligatoire), plafonn� � 500<br/>
 * 
 * <b>Exemple d'utilisation:</b> Recherche sur le modele complexe VersionDroit
 * [ch.globaz.pegasus.business.models.droit.VersionDroit . 100] --> recherche limit� � 100, via le mod�le de recherche
 * ch.globaz.pegasus.business.models.droit.VersionDroitSearch<br/>
 * <br/>
 * Recherche sur le mod�le de recheche VersionDroit en sp�cifiant un modele de recherche sp�cifique
 * [ch.globaz.pegasus.business.models.droit.VersionDroit ch.globaz.pegasus.business.models.droit.VersionDroitSearch 100]
 * --> recherche limit� � 100, via le mod�le de recherche sp�cifi�<br/>
 * <b>Configuration</b><br/>
 * La configration se fait via l'enum DBToJSONDumpConfig
 * 
 * <br/>
 * <b>Fichiers json</b><br/>
 * Par d�faut les fichiers json sont d�pos�es dans le r�pertoire (source du projet): jsonFiles.<br/>
 * Un fichier par mod�le.
 * 
 * @see ch.globaz.utils.tests.DBToJSONDumpConfig
 * @author sce
 * 
 */
public class DBToJSONDump {

    private static DBToJSONDump instance = null;

    /**
     * Traitement des param�tres
     * 
     * @param args
     *            le tableau des param�tres, 3 param�tres
     */
    private static void checkParameters(String[] args) {

        if ((args == null) || (args.length == 0)) {
            logProblemAndShutdown(new IllegalArgumentException("The arguments cannot be empty!"));
        }

        if (args.length < 3) {
            logProblemAndShutdown(new IllegalArgumentException(
                    "The number of arguments must be 3. See the javadoc for usage"));
        }

        String modelName = args[0];
        int modelSearchSize = Integer.parseInt(args[2]);

        if ((modelName == null) || (modelSearchSize == 0)
                || (modelSearchSize > Integer.parseInt(DBToJSONDumpConfig.MAXIMUM_NUMBER_ENTITIES.value))) {
            logProblemAndShutdown(new IllegalArgumentException("Problem detected with argments: modelName[" + modelName
                    + "], modelSeachSize[" + modelSearchSize + "]"));
        }

    }

    /**
     * Retourne l'instance
     * 
     * @return
     */
    private static DBToJSONDump getInstance() {
        if (DBToJSONDump.instance == null) {
            DBToJSONDump.instance = new DBToJSONDump();
        }

        return DBToJSONDump.instance;
    }

    /**
     * Methode loggant une exception et terminant l'application Utilise JadeLogger
     * 
     * @param e
     *            l'exception a logger
     */
    private static void logProblemAndShutdown(Exception e) {

        JadeLogger
                .info(getInstance(),
                        "**************************************************************************************************************************************************\n"
                                + "**************************************************************************************************************************************************\n");

        JadeLogger.info(getInstance(), "########### Problem during process:" + e.toString() + "\n " + e.getMessage()
                + "\n");
        JadeLogger.info(getInstance(), "########### Application will exit!\n");
        JadeLogger
                .info(getInstance(),
                        "**************************************************************************************************************************************************\n"
                                + "**************************************************************************************************************************************************\n");

        getInstance().stopJade();
        System.exit(1);
    }

    /**
     * 
     * @param args
     */
    public static void main(String args[]) {

        try {
            checkParameters(args);
            // Lancement de jade
            getInstance().runJade();
            // g�n�ration des fichiers json
            new GenerateJsonFilesWithModel(args[0], args[1], args[2]);
        } catch (Exception e) {
            logProblemAndShutdown(e);
        }

        JadeLogger.info(getInstance(), "########### Process terminate ok!");
        getInstance().stopJade();
        System.exit(1);
    }

    /**
     * Lancement de JADE
     * 
     * @throws Exception
     */
    void runJade() throws Exception {
        Jade.getInstance();
        BSession session = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(DBToJSONDumpConfig.APPLICATION.value)
                .newSession(DBToJSONDumpConfig.USER.value, DBToJSONDumpConfig.PASS.value);
        JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
        JadeThread.currentContext().storeTemporaryObject("bsession", session);
    }

    /**
     * On stoppe JADE
     */
    void stopJade() {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThreadActivator.stopUsingContext(this);
    }
}
