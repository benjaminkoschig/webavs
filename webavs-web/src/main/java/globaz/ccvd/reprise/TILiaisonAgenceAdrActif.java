package globaz.ccvd.reprise;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.externalservices.TIAvoirAdresseAgenceExternalService;

public class TILiaisonAgenceAdrActif extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ProgressBar {
        private int actual = 0;
        private boolean estimationDisplayed = false;
        private boolean headerDisplayed = false;
        private long initTime = 0;
        private long nbEnregistrements;

        public ProgressBar(long nombre) {
            nbEnregistrements = nombre;
        }

        public void terminate() {
            System.out.println("|");
            System.out.println("******************************************************");
        }

        public void updateBar(long actualLength, String idTiers) {
            if (initTime == 0) {
                initTime = System.currentTimeMillis();
            }
            int act = (int) ((actualLength * 100) / nbEnregistrements);
            if (act == 1 && !estimationDisplayed) {
                System.out.println("******************** PROGRESS BAR ********************");
                System.out.print("Estimated time: ");
                long part = (System.currentTimeMillis() - initTime) * 100;
                if (part < 60000) {
                    // affichage en sec
                    System.out.println(part / 1000 + "sec.");
                } else if (part < 3600000) {
                    // affichage en minute
                    System.out.println(part / 60000 + "min.");
                } else {
                    // afichage en heure
                    System.out.println(part / 3600000 + "h.");
                }
                estimationDisplayed = true;
            }
            if (!headerDisplayed && estimationDisplayed) {
                System.out.println("0%  10%  20%  30%  40%  50%  60%  70%  80%  90%  100%");
                System.out.println("|    |    |    |    |    |    |    |    |    |    |  ");
                System.out.print("|");
                headerDisplayed = true;
            }
            if (act > actual + 1) {
                System.out.print("|");
                System.out.println("idTiers :" + idTiers);
                actual = act;
            }
        }
    }

    public static void main(String[] args) {
        TILiaisonAgenceAdrActif process = null;
        String user = "";
        String pwd = "";
        String email = "ald@globaz.ch";
        try {
            user = args[0];
            pwd = args[1];
            email = args[2];
            System.out.println("User : " + user);
            System.out.println("Password : " + pwd);
            System.out.println("Email : " + email);
            System.out.println("Id Tiers de départ : " + args[3]);
            BSession session = (BSession) GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS)
                    .newSession(user, pwd);
            System.out.println("Traitement des agences started...");
            session.connect(user, pwd);
            process = new TILiaisonAgenceAdrActif();
            process.setSession(session);
            process.setEMailAddress(email);
            process.setFromIdTiers(args[3]);
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("" + args[3]);
        }
        System.exit(0);
    }

    private String fromIdTiers;

    private ProgressBar progressBar;

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean result = true;
        TIAvoirAdresse avoirAdresse = null;
        int counter = 0;
        long time = System.currentTimeMillis();

        try {
            TIAvoirAdresseManager setOfAvAdresses = new TIAvoirAdresseManager();
            setOfAvAdresses.setSession(getSession());
            setOfAvAdresses.setFromIdTiers(getFromIdTiers());
            setOfAvAdresses.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());

            int nbTotalAvAdresse = setOfAvAdresses.getCount();
            progressBar = new ProgressBar(nbTotalAvAdresse);
            System.out.println("Nombre total d'adresses à traiter " + nbTotalAvAdresse);

            BStatement statement = setOfAvAdresses.cursorOpen(getTransaction());

            TIAvoirAdresseAgenceExternalService handlerAgence = new TIAvoirAdresseAgenceExternalService();

            while ((avoirAdresse = (TIAvoirAdresse) setOfAvAdresses.cursorReadNext(statement)) != null) {

                if (JadeStringUtil.isEmpty(avoirAdresse.getDateFinRelation())) {
                    try {
                        // On a une adresse, on exécute la recherche des agences
                        // dessus:
                        handlerAgence.afterUpdate(avoirAdresse);
                    } catch (Throwable e) {
                        _addError(getTransaction(), e.getMessage());
                    }
                }
                counter++;

                // Commit la transaction si pas rencontré d'erreur
                // Rollback dans le cas contraire
                valideTransaction(getTransaction());
                if (counter % 100 == 0) {
                    progressBar.updateBar(counter, avoirAdresse.getIdTiers());
                }
            }
            setOfAvAdresses.cursorClose(statement);
            progressBar.updateBar(counter, avoirAdresse.getIdTiers());
            progressBar.terminate();
            System.out.println(counter + " adresses traitées en " + ((System.currentTimeMillis() - time) / 60000)
                    + "min.");
        } catch (Exception e) {
            e.printStackTrace();
            _addError(getTransaction(), e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "Le traitement des agences communales est terminé";
    }

    public String getFromIdTiers() {
        return fromIdTiers;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setFromIdTiers(String fromIdTiers) {
        this.fromIdTiers = fromIdTiers;
    }

    private void valideTransaction(BTransaction transaction) throws Exception {
        if (transaction.hasErrors()) {
            getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR, "PHENIX");
            transaction.rollback();
            transaction.clearErrorBuffer();
        } else {
            transaction.commit();
        }
    }

}
