/*
 * Créé le 26 févr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.HEBatchUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEExtraitTermine extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println(HEBatchUtils.getCurrentTime()
                        + "java globaz.hermes.zas.HEExtraitCIAdditionnel <idLot> <email> <uid> <pwd>");
                throw new Exception("Wrong number of arguments");
            }

            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[2], args[3]);

            HEExtraitTermine myProcess = new HEExtraitTermine();
            myProcess.setSession(session);
            myProcess.setEmail(args[1]);
            myProcess.setIdLot(args[0]);
            myProcess.setSendCompletionMail(true);
            myProcess.setSendMailOnError(true);
            myProcess.setControleTransaction(true);
            JadeJobInfo job = BProcessLauncher.start(myProcess);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String email = new String();
    private String idLot = new String();
    private boolean isArchivage = false;

    private String referenceInterne = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // TODO Raccord de méthode auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        setSendCompletionMail(false);
        // préparation de l'impression
        HEExtraitAnnonceProcess extrait = new HEExtraitAnnonceProcess();
        extrait.setSession(getSession());
        extrait.setEMailAddress(email);
        if (JadeStringUtil.isEmpty(idLot)) {
            throw new Exception("Erreur : idLot n'est pas renseigné !");
        }
        // rechercher tous les 39001 qui sont terminé pour ce lot
        HEOutputAnnonceListViewBean listCiTermine = new HEOutputAnnonceListViewBean();
        listCiTermine.setSession(getSession());
        listCiTermine.setForCodeApplication("39");
        listCiTermine.setForCodeEnr01Or001(true);
        listCiTermine.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        listCiTermine.setForIdLot(idLot);
        listCiTermine.setForNotCiaddtionnel(true);
        listCiTermine.setIsArchivage(isArchivage());
        BStatement statement = listCiTermine.cursorOpen(getTransaction());
        HEOutputAnnonceViewBean crtExtraitCI;
        while ((crtExtraitCI = (HEOutputAnnonceViewBean) listCiTermine.cursorReadNext(statement)) != null) {
            if ((!extrait.getReferenceUniqueVector().contains(crtExtraitCI.getRefUnique()))
                    && (!crtExtraitCI.isCiSecure())) {
                extrait.addReferenceUnique(crtExtraitCI.getRefUnique());
            }
        }
        listCiTermine.cursorClose(statement);

        extrait.setEmailSubjectOK(getSession().getLabel("HERMES_10038") + " " + idLot + "/"
                + JACalendar.todayJJsMMsAAAA() + "/" + getReferenceInterne());
        extrait.setParent(this);
        extrait.setService(getReferenceInterne());
        extrait.executeProcess();
        return false;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * @return
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param b
     */
    public void setArchivage(boolean b) {
        isArchivage = b;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    public void setReferenceInterne(String newReferenceInterne) {
        referenceInterne = newReferenceInterne;
    }

}
