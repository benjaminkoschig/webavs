package globaz.osiris.process.ebill;

import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;

public class CAProcessGenererListeAffiliesEBill extends BProcess {

    public CAProcessGenererListeAffiliesEBill() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     *
     * @param parent
     */
    public CAProcessGenererListeAffiliesEBill(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     *
     * @param session
     */
    public CAProcessGenererListeAffiliesEBill(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForIdGenreCompte("0");
        manager.find();
        if (manager.size() <= 0) {
            getMemoryLog().logMessage(getSession().getLabel("MAIL_LISTE_AFFILIATION_MODE_FACTURATION_NO_DATA"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        createDoc(manager);

        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

    private void createDoc(CACompteAnnexeManager compteAnnexeManager) {
        /*SimpleOutputListBuilderJade docBuilder = SimpleOutputListBuilderJade.newInstance();
        docBuilder.asCsv();
        docBuilder.initializeContext(getSession());*/

        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            //writeLigneToDoc(docBuilder, (CACompteAnnexe) compteAnnexeManager.getEntity(i));
        }
    }

    private void writeLigneToDoc(SimpleOutputListBuilderJade doc, CACompteAnnexe compte){

    }

    @Override
    protected String getEMailObject() {
        return "Gereration Liste Affilies eBill";
    }


    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
