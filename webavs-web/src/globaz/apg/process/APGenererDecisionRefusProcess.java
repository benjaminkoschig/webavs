package globaz.apg.process;

import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.topaz.APDecisionRefusOO;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.process.CTAbstractJadeJob;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import java.util.Iterator;

/**
 * @author JJE
 */
public class APGenererDecisionRefusProcess extends CTAbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean isOnlySendGED = Boolean.FALSE;

    private Boolean isSendToGed = null;

    public APGenererDecisionRefusProcess() {
        super();
    }

    private APDecisionRefusOO createDecisionRefus(boolean isCopie) throws Exception {

        APDecisionRefusOO apDecisionRefusOo = new APDecisionRefusOO();
        apDecisionRefusOo.setSession(getSession());
        apDecisionRefusOo.setIdDroit(getScalableDocumentProperties().getParameter("idDroit"));
        apDecisionRefusOo.setDateSurDocument(getScalableDocumentProperties().getParameter("dateSurDocument"));
        apDecisionRefusOo.setCopies(getScalableDocumentProperties().getCopiesIterator());
        apDecisionRefusOo.setAnnexes(getScalableDocumentProperties().getAnnexesIterator());
        apDecisionRefusOo.setIsCopie(isCopie);

        // Recherche du tiers en fct du droit
        APDroitLAPGJointDemandeManager apDroitLAPGJointDemandeMgr = new APDroitLAPGJointDemandeManager();
        apDroitLAPGJointDemandeMgr.setSession(getSession());
        apDroitLAPGJointDemandeMgr.setForIdDroit(getScalableDocumentProperties().getParameter("idDroit"));
        apDroitLAPGJointDemandeMgr.find();
        APDroitLAPGJointDemande apDroitLAPGJointDem = null;

        if (!apDroitLAPGJointDemandeMgr.isEmpty()) {
            apDroitLAPGJointDem = (APDroitLAPGJointDemande) apDroitLAPGJointDemandeMgr.getFirstEntity();
            apDecisionRefusOo.setIdTiers(apDroitLAPGJointDem.getIdTiers());
            apDecisionRefusOo.generationLettre();

        } else {
            JadeLogger.error(this, "Erreur: Pas de tiers lié au droit (APGenererDecisionRefusProcess.run())");
        }

        return apDecisionRefusOo;
    }

    private PRLettreEnTeteOO createLettreEnTete(String idTiers) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());

        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);

        if (null == tier) {
            tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tier) {
            lettreEnTete.setTierAdresse(tier);
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(PRLettreEnTeteOO.DOMAINE_MAT);
            lettreEnTete.generationLettre();
        }

        return lettreEnTete;
    }

    @Override
    public String getDescription() {
        return "Décision de refus";
    }

    protected String getEMailObject() {
        return getEMailObject();
    }

    public Boolean getIsOnlySendGED() {
        return isOnlySendGED;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    @Override
    public String getName() {
        return "Décision de refus";
    }

    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    public void run() {

        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfos.setOwnerEmail(getScalableDocumentProperties().getParameter("eMailAddress"));
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO,
                getScalableDocumentProperties().getParameter("eMailAddress"));
        pubInfos.setDocumentTitle("Décision de refus");
        pubInfos.setArchiveDocument(false);
        pubInfos.setPublishDocument(false);
        pubInfos.setDocumentSubject("Décision de refus");

        JadePublishDocumentInfo pubInfosGen = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfosGen.setOwnerEmail(getScalableDocumentProperties().getParameter("eMailAddress"));
        pubInfosGen.setPublishProperty(JadePublishDocumentInfo.MAIL_TO,
                getScalableDocumentProperties().getParameter("eMailAddress"));
        pubInfosGen.setDocumentTitle("Décision de refus");
        pubInfosGen.setArchiveDocument(false);
        pubInfosGen.setPublishDocument(true);
        pubInfosGen.setDocumentSubject("Décision de refus");

        allDoc.setMergedDocDestination(pubInfosGen);

        try {
            APDecisionRefusOO apDecisionRefusOoOriginal = createDecisionRefus(false);
            allDoc.addDocument(apDecisionRefusOoOriginal.getDocumentData(), pubInfos);

            // Création des copies
            Iterator iteratorDestinataireCopie = getScalableDocumentProperties().getCopiesIterator();
            while (iteratorDestinataireCopie.hasNext()) {
                ICTScalableDocumentCopie intervenantCopie = (ICTScalableDocumentCopie) iteratorDestinataireCopie.next();

                // Création lettre entête
                PRLettreEnTeteOO lettreEnTete = createLettreEnTete(intervenantCopie.getIdTiers());
                allDoc.addDocument(lettreEnTete.getDocumentData(), pubInfos);

                // Création copie lettre refus
                APDecisionRefusOO apDecisionRefusOo = createDecisionRefus(true);
                allDoc.addDocument(apDecisionRefusOo.getDocumentData(), pubInfos);
            }

            createDocuments(allDoc);

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }
    }

    public void setIsOnlySendGED(Boolean isOnlySendGED) {
        this.isOnlySendGED = isOnlySendGED;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

}
