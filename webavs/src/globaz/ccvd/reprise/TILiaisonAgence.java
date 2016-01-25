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
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.externalservices.TIAvoirAdresseAgenceExternalService;

public class TILiaisonAgence extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        TILiaisonAgence process = null;
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
            System.out.println("id Localité à traiter : " + args[4]);

            BSession session = (BSession) GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS)
                    .newSession(user, pwd);
            System.out.println("Traitement des agences started...");
            session.connect(user, pwd);
            process = new TILiaisonAgence();
            process.setSession(session);
            process.setEMailAddress(email);
            process.setFromIdTiers(args[3]);
            process.setIdlocalite(args[4]);
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

    private String idlocalite;

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

            System.out.println("Nombre total d'adresses à traiter " + nbTotalAvAdresse);

            BStatement statement = setOfAvAdresses.cursorOpen(getTransaction());

            TIAvoirAdresseAgenceExternalService handlerAgence = new TIAvoirAdresseAgenceExternalService();

            while ((avoirAdresse = (TIAvoirAdresse) setOfAvAdresses.cursorReadNext(statement)) != null) {

                if (JadeStringUtil.isEmpty(avoirAdresse.getDateFinRelation()) && adresseConcerne(avoirAdresse)) {
                    try {
                        // On a une adresse, on exécute la recherche des agences
                        // dessus:
                        handlerAgence.afterUpdate(avoirAdresse);
                    } catch (Throwable e) {
                        _addError(getTransaction(), e.getMessage());
                    }
                    counter++;
                }
                // Commit la transaction si pas rencontré d'erreur
                // Rollback dans le cas contraire
                valideTransaction(getTransaction());
            }
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

    private boolean adresseConcerne(TIAvoirAdresse avoirAdresse) throws Exception {
        if (JadeStringUtil.isEmpty(getIdlocalite())) {
            return true;
        } else {
            TIAdresse adr = new TIAdresse();
            adr.setSession(getSession());
            adr.setIdAdresseUnique(avoirAdresse.getIdAdresse());
            adr.retrieve();
            return adr.getIdLocalite().equals(getIdlocalite());
        }
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

    public String getIdlocalite() {
        return idlocalite;
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

    public void setIdlocalite(String idlocalite) {
        this.idlocalite = idlocalite;
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
