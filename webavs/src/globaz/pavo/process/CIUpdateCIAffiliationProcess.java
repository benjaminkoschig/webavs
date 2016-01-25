package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * Traitement journalier des comptes individules (ca 21 et 23). Date de création : (25.11.2002 11:52:37)
 * 
 * @author: Administrator
 */
public class CIUpdateCIAffiliationProcess extends BProcess {

    private static final long serialVersionUID = -3300424617816443062L;

    private boolean echoToConsole = false;

    public int nbrToRead = 1000000;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIUpdateCIAffiliationProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIUpdateCIAffiliationProcess(globaz.globall.db.BSession session) {
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
        try {

            CICompteIndividuelManager mgr = new CICompteIndividuelManager();
            mgr.setSession(getSession());
            mgr.changeManagerSize(nbrToRead);
            mgr.orderByAvs(false);
            mgr.wantCallMethodAfter(false);
            mgr.wantCallMethodBefore(false);
            mgr.setFromNumeroAvs("100");
            mgr.setUntilNumeroAvs("12011102113");
            BStatement statement = mgr.cursorOpen(getTransaction());
            if (echoToConsole) {
                System.out.println("starting execution");
            }
            // Boucle pour la lecture
            CICompteIndividuel ci;
            int counter = 1;
            long time = System.currentTimeMillis();
            while ((ci = (CICompteIndividuel) mgr.cursorReadNext(statement)) != null) {
                CIEcritureFastManager ecrMgr = new CIEcritureFastManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForIdTypeCompteCompta("true");
                ecrMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                // ecrMgr.setFromEmployeur("1");
                // ecrMgr.setForAnnee("2002");
                ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
                // ecrMgr.changeManagerSize(1);
                ecrMgr.find(getTransaction());
                for (int i = 0; i < ecrMgr.size(); i++) {
                    // mise à jour avec la dernière écriture de l'assuré
                    String employeur = ((CIEcritureFast) ecrMgr.getEntity(i)).getEmployeur();

                    if (!JAUtil.isIntegerEmpty(employeur)) {
                        if (!employeur.equals(ci.getDernierEmployeur())) {
                            ci.setDernierEmployeur(employeur);
                            ci.wantCallValidate(false);
                            ci.wantCallMethodBefore(false);
                            ci.wantCallMethodAfter(false);
                            getTransaction().disableSpy();
                            ci.update(getTransaction());
                        }
                        if (getTransaction().hasErrors()) {
                            System.out.println("Erreur pour " + ci.getNumeroAvs() + " " + ci.getNomPrenom() + ": "
                                    + getTransaction().getErrors());
                            getTransaction().clearErrorBuffer();
                        }
                        break;

                    } else {
                        // System.out.println("pas d'affilié pour "+ci.getNumeroAvs()+" "+ci.getNomPrenom());
                    }
                }
                if (counter % 1000 == 0) {
                    if (echoToConsole) {
                        System.out.println(counter + " ci updated in " + (System.currentTimeMillis() - time) / 1000
                                + "sec.");
                        getTransaction().commit();
                    }

                }
                counter++;
            }
            if (echoToConsole) {
                System.out.println("Process done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !isAborted();
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement de mise à jour des comptes individuels a echoué!";
        } else {
            return "Le traitement de mise à jour des comptes individuels s'est effectué avec succès.";
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
