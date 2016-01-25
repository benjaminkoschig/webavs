package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * Efface tous les ci de genre 6,7 ou provisoire si ceux-ci sont vide Date de création : (25.11.2002 11:52:37)
 * Périodicité: annuelle ou sur demande
 * 
 * @author: dgi
 */
public class CIClearEmptyCIProcess extends BProcess {

    private static final long serialVersionUID = -7451296368098773928L;
    public static String[] typeList = { CICompteIndividuel.CS_REGISTRE_PROVISOIRE };
    private boolean echoToConsole = false;

    protected boolean maj = false;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIClearEmptyCIProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIClearEmptyCIProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        int counter = 1;
        long time = System.currentTimeMillis();
        try {
            if (echoToConsole) {
                System.out.println("starting process...");
                if (maj) {
                    System.out.println("MODE DELETE!");
                }
            }
            // init
            BStatement statement;
            CICompteIndividuelManager mgr = new CICompteIndividuelManager();
            mgr.setSession(getSession());
            mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            mgr.orderByAvs(false);
            mgr.wantCallMethodAfter(false);
            mgr.wantCallMethodBefore(false);
            CICompteIndividuel ci;

            for (int type = 0; type < typeList.length; type++) {
                mgr.setForRegistre(typeList[type]);
                statement = mgr.cursorOpen(getTransaction());
                if (echoToConsole) {
                    System.out.println("processing '" + typeList[type] + "'...");
                }
                getMemoryLog().logMessage("processing '" + typeList[type] + "'", FWMessage.INFORMATION, "Clear CI");
                // Boucle pour la lecture
                while ((ci = (CICompteIndividuel) mgr.cursorReadNext(statement)) != null) {
                    if (ci.deleteIfEmpty(getTransaction(), maj)) {
                        if (getTransaction().hasErrors()) {
                            StringBuffer errors = getTransaction().getErrors();
                            getMemoryLog().logStringBuffer(errors, getClass().getName());
                            getMemoryLog().logMessage(
                                    "  CI " + ci.getNumeroAvs() + "/" + ci.getNomPrenom() + " not deleted",
                                    FWMessage.FATAL, "Clear CI");
                            if (echoToConsole) {
                                System.out.println("  CI " + ci.getNumeroAvs() + "/" + ci.getNomPrenom()
                                        + " not deleted [" + errors.toString() + "]");
                            }
                            getTransaction().clearErrorBuffer();
                        } else {
                            if (echoToConsole) {
                                System.out.println("  CI " + ci.getNumeroAvs() + "/" + ci.getNomPrenom() + " deleted");
                            }
                            getMemoryLog().logMessage("CI " + ci.getNumeroAvs() + "/" + ci.getNomPrenom() + " deleted",
                                    FWMessage.INFORMATION, "Clear CI");
                        }
                        // ci vide et effacé
                        if (counter % 1000 == 0) {
                            if (echoToConsole) {
                                System.out.println(counter + " ci deleted in " + (System.currentTimeMillis() - time)
                                        / 1000 + "sec.");
                                getTransaction().commit();
                            }
                        }
                        counter++;
                    }

                }
                mgr.cursorClose(statement);
            }
        } catch (Exception e) {
            if (echoToConsole) {
                e.printStackTrace();
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, getClass().getName());
        }
        if (echoToConsole) {
            System.out.println("Process done in " + (System.currentTimeMillis() - time) / 60000 + "min.");
        }
        return !isAborted();
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le nettoyage des CI a echoué!";
        } else {
            return "Le nettoyage des CI s'est effectué avec succès.";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

}
