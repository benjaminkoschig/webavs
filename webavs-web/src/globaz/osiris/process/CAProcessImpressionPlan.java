package globaz.osiris.process;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvBVR4;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvDecision;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvEcheancier;
import globaz.osiris.utils.CASursisPaiement;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;

/**
 * @author kurkus, 27 mai 05
 * @author sel, 18 juin 2007
 */
public class CAProcessImpressionPlan extends BProcess {

    private static final long serialVersionUID = -7417065395365529318L;

    private static final String PROPERTIES_LIGNE_TECH = "common.use.ligneTechnique";

    private String dateRef = "";

    private CAILettrePlanRecouvDecision document = null;
    private String idDocument = "";
    private String idPlanRecouvrement = "";
    private Boolean impAvecBVR = new Boolean(false);
    private String modele = "";
    private String observation = "";

    /**
     * Constructor for CAProcessImpressionPlan.
     */
    public CAProcessImpressionPlan() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param parent
     */
    public CAProcessImpressionPlan(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param session
     */
    public CAProcessImpressionPlan(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Création des documents
            document = createDecision();
            // CAILettrePlanRecouvVoiesDroit documentVD = this.createVoiesDroit();
            CAPlanRecouvrement plan = (CAPlanRecouvrement) document.currentEntity();
            CAILettrePlanRecouvEcheancier documentE = CASursisPaiement.createEcheancier(this, getTransaction(), plan);
            // Fusionne les documents ci-dessus (Décision, voies de droit et échéancier)
            fusionneDocuments(plan);

            List echeances = (ArrayList) documentE.currentEntity();
            if (!JadeStringUtil.isBlank(documentE.getPlanRecouvrement().getId()) && getImpAvecBVR().booleanValue()
                    && (echeances != null)) {

                createBVR(plan, echeances, documentE);
            }

            // Tester si abort
            if (isAborted()) {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
    }

    /**
     * Prépare et retourne le document "BVR"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @param plan
     * @param echeances
     * @return le document "BVR"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvBVR4 createBVR(CAPlanRecouvrement plan, List echeances,
            CAILettrePlanRecouvEcheancier echeancier) throws FWIException, Exception {
        // Instancie le document : BVR
        CAILettrePlanRecouvBVR4 documentBVR = new CAILettrePlanRecouvBVR4(this);
        documentBVR.setSession(getSession());
        documentBVR.setDateRef(getDateRef());
        documentBVR.addAllEntities(echeances);
        documentBVR.setPlanRecouvrement(plan);
        documentBVR.setCumulSolde(echeancier.getCumulSolde());
        documentBVR.setImpressionParLot(true);
        documentBVR.setTailleLot(500);

        // Demander le traitement du document
        documentBVR.setEMailAddress(getEMailAddress());
        documentBVR.executeProcess();

        return documentBVR;
    }

    /**
     * Prépare et retourne le document "Décision"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @return le document "Décision"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvDecision createDecision() throws FWIException, Exception {
        // Instancie le document du plan de recouvrement : Décision
        CAILettrePlanRecouvDecision document = new CAILettrePlanRecouvDecision(this);
        document.setSession(getSession());
        document.setDateRef(getDateRef());
        document.setIdDocument(getIdDocument());
        document.setJoindreBVR(getImpAvecBVR());
        document.setObservation(getObservation());
        // Demander le traitement du document
        document.setEMailAddress(getEMailAddress());

        if (JadeStringUtil.isBlank(getIdPlanRecouvrement())) {
            super._addError(getSession().getLabel("OSIRIS_ERR_PLAN_ID_IS_NULL"));
            throw new Exception(getSession().getLabel("OSIRIS_ERR_PLAN_ID_IS_NULL"));
        }
        document.setIdPlanRecouvrement(getIdPlanRecouvrement());
        document.executeProcess();

        if (document.getDocumentList().size() <= 0) {
            throw new Exception(this.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " can not be created !");
        }

        return document;
    }

    /**
     * Fusionne les documents (Décision, voies de droit et échéancier). <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments(CAPlanRecouvrement plan) throws Exception {
        // Fusionne les documents (Décision, voies de droit et échéancier)
        // Les documents fusionnés sont effacés

        // GED
        JadePublishDocumentInfo info = null;
        if (getAttachedDocuments().size() > 0) {

            JadePublishDocument doc = (JadePublishDocument) getAttachedDocuments().get(0);
            info = doc.getPublishJobDefinition().getDocumentInfo().createCopy();

        } else {
            info = super.createDocumentInfo();
            info.setDocumentTypeNumber(CAILettrePlanRecouvDecision.NUMERO_REFERENCE_INFOROM);
            IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                    TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
            TIDocumentInfoHelper.fill(info, plan.getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE, plan
                    .getCompteAnnexe().getIdExterneRole(), affilieFormater.unformat(plan.getCompteAnnexe()
                    .getIdExterneRole()));
        }

        if (isUseLigneTechnique()) {
            // Envoie un e-mail avec les pdf fusionnés
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            this.mergePDF(info, false, 500, false, null);

        } else {
            // Envoie un e-mail avec les pdf fusionnés
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            info.setDuplex(true);
            this.mergePDF(info, true, 500, false, null);

            // On refait le docinfo du document généré
            if (getAttachedDocuments().size() > 0) {
                JadePublishDocument doc = (JadePublishDocument) getAttachedDocuments().get(0);
                JadePublishDocumentInfo infoDoc = doc.getPublishJobDefinition().getDocumentInfo();
                infoDoc.setChildren(null);
            }
        }
    }

    /**
     * Récupération de la propriété pour savoir si on est en fonctionnement ligne technique
     * 
     * @return la valeur <code>boolean</code> de la propriété.
     * @throws PropertiesException Si la propriété est absente ou incorrecte
     */
    private boolean isUseLigneTechnique() throws PropertiesException {

        String value = JadePropertiesService.getInstance().getProperty(PROPERTIES_LIGNE_TECH);
        if (value == null) {
            throw new PropertiesException("The properties [" + PROPERTIES_LIGNE_TECH + "] doesn't exist.");
        }

        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new PropertiesException("The value (" + value + ") for the properties " + PROPERTIES_LIGNE_TECH
                    + " is not a boolean value");
        }
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    @Override
    protected String getEMailObject() {
        // Sujet du mail
        return document.getDocumentTitle();
    }

    /**
     * @return the idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    public Boolean getImpAvecBVR() {
        return impAvecBVR;
    }

    /**
     * @return the modele
     */
    public String getModele() {
        return modele;
    }

    /**
     * @return the observation
     */
    public String getObservation() {
        return observation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param dateRef
     *            the dateRef to set
     */
    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    /**
     * @param idDocument
     *            the idDocument to set
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    public void setImpAvecBVR(Boolean newImpAvecBVR) {
        impAvecBVR = newImpAvecBVR;
    }

    /**
     * @param modele
     *            the modele to set
     */
    public void setModele(String modele) {
        this.modele = modele;
    }

    /**
     * @param observation
     *            the observation to set
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }
}
