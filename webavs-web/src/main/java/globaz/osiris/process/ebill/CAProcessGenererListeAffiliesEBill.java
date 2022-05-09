package globaz.osiris.process.ebill;

import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFListeAffiliationModeFacturationProcess;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexePourListeAffiliesEBill;
import globaz.osiris.db.comptes.CACompteAnnexePourListeAffiliesEBillManager;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TITiers;

import java.io.File;
import java.io.IOException;

/**
 * Process to generate the eBill affiliés, the list is generated from the Compte Annexe.
 */
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
        CACompteAnnexePourListeAffiliesEBillManager manager = new CACompteAnnexePourListeAffiliesEBillManager();
        manager.setSession(getSession());
        manager.find(BManager.SIZE_NOLIMIT);
        createDoc(manager);

        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

    private void createDoc(CACompteAnnexePourListeAffiliesEBillManager compteAnnexeManager) {
        setProgressScaleValue(compteAnnexeManager.size());
        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            incProgressCounter();
            CACompteAnnexePourListeAffiliesEBill compte = (CACompteAnnexePourListeAffiliesEBill) compteAnnexeManager.getEntity(i);
            String today = JACalendar.today().toStr(".");
            try {
                TIAvoirAdresse adresse = TITiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER, IntTiers.PYXIS_DOMAINE_DEFAUT,
                        today, compte.getIdTiers(), getSession());
                TILocalite loc = new TILocalite();
                loc.setSession(getSession());
                loc.setIdLocalite(adresse.getIdLocalite());
                loc.retrieve();
                compte.setCodePostal(loc.getNumPostal());
                compte.setLieu(loc.getLocalite());
            } catch (Exception e) {
                JadeLogger.warn(e,"Problème lors de la recherche de données correspondantes au Tiers n° " + compte.getIdTiers());
            }
        }

        File file = SimpleOutputListBuilderJade.newInstance()
                .initializeContext(getSession())
                .outputNameAndAddPath("ListeAffiliesEBill_test")
                .addList(compteAnnexeManager.toList())
                .addTitle("Liste aff", Align.CENTER)
                .asCsv().build();

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeAffiliationModeFacturationProcess.NUMERO_INFOROM);
        try {
            this.registerAttachedDocument(docInfo, file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("MAIL_LISTE_AFFILIATION_EBILL_EMAIL_OBJECT");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }
}
