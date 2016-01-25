/*
 * Créé le 13 févr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.print.itext.HEExtraitAnnonce_Doc;
import globaz.hermes.utils.HEBatchUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEExtraitCIAdditionnel extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            if (args.length < 5) {
                System.out
                        .println(HEBatchUtils.getCurrentTime()
                                + "java globaz.hermes.zas.HEExtraitCIAdditionnel <date> <dateref(AAAAMMJJ)> <email> <uid> <pwd>");
                System.out.println(HEBatchUtils.getCurrentTime()
                        + "java globaz.hermes.zas.HEExtraitCIAdditionnel <lot> <idLot> <email> <uid> <pwd>");
                throw new Exception("Wrong number of arguments");
            }

            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[3], args[4]);

            HEExtraitCIAdditionnel myProcess = new HEExtraitCIAdditionnel();
            myProcess.setSession(session);
            myProcess.setEmail(args[2]);
            if ("date".equals(args[0])) {
                myProcess.setFromDate(args[1]);
            } else if ("lot".equals(args[0])) {
                myProcess.setIdLot(args[1]);
            } else {
                throw new Exception("Please use date or lot in the command line ! ");
            }
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
    private String fromDate = new String();
    private String idLot = new String();
    private boolean isArchivage = false;

    private String service = new String();

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
        HEExtraitAnnonce_Doc extrait = new HEExtraitAnnonce_Doc();
        extrait.setSession(getSession());
        extrait.setEMailAddress(email);
        //
        HELotListViewBean listeLot = new HELotListViewBean();
        listeLot.setSession(getSession());
        listeLot.setForIdLot(idLot);
        listeLot.setFromDateEnvoi(fromDate);
        listeLot.setForType(HELotViewBean.CS_TYPE_RECEPTION);
        listeLot.setIsArchivage(isArchivage() ? "true" : "false");
        listeLot.find(getTransaction(), BManager.SIZE_NOLIMIT);
        // Ajout des références des CI additionnels
        for (int i = 0; i < listeLot.size(); i++) {
            HELotViewBean lot = (HELotViewBean) listeLot.getEntity(i);
            HEOutputAnnonceListViewBean listCiadd = new HEOutputAnnonceListViewBean();
            listCiadd.setSession(getSession());
            listCiadd.setForCodeApplication("39");
            listCiadd.setForCodeEnr01Or001(true);
            listCiadd.setForIdLot(lot.getIdLot());
            listCiadd.setForCIAdditionnel(true);
            listCiadd.setIsArchivage(isArchivage());
            BStatement statement = listCiadd.cursorOpen(getTransaction());
            HEOutputAnnonceViewBean crtCIAdd;
            while ((crtCIAdd = (HEOutputAnnonceViewBean) listCiadd.cursorReadNext(statement)) != null) {
                extrait.addReferenceUnique(crtCIAdd.getRefUnique());
            }
            listCiadd.cursorClose(statement);
        }
        extrait.setEmailSubjectOK(getSession().getLabel("HERMES_10032") + "/" + JACalendar.todayJJsMMsAAAA()
                + (JadeStringUtil.isBlank(idLot) ? "" : "/" + idLot));
        extrait.setEmailSubjectError(getSession().getLabel("HERMES_10033") + "/" + JACalendar.todayJJsMMsAAAA()
                + (JadeStringUtil.isBlank(idLot) ? "" : "/" + idLot));
        extrait.setParent(this);
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
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    public String getService() {
        return service;
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
    public void setFromDate(String string) {
        fromDate = string;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    public void setService(String newReferenceInterne) {
        service = newReferenceInterne;
    }

}
