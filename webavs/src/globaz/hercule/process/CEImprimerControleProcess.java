package globaz.hercule.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.itext.controleEmployeur.CERapport_P1_Doc;
import globaz.hercule.itext.controleEmployeur.CERapport_P2_Doc;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * <H1>Description</H1>
 * <p>
 * Cree les debuts de suivis pour les documents d'annonces de salaires.
 * </p>
 * <p>
 * Genere les documents pour tous les affilies non radies qui ont la case 'envois automatiques des annonces de salaires'
 * ou pour l'affilie dont l'identifiant est renseigne.
 * </p>
 * 
 * @author vre
 * @since Créé le 8 déc. 05
 */
public class CEImprimerControleProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String controleId;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String dateImpression = "";
    private String numAffilie = "";
    private Boolean preRapport = new Boolean(false);

    public CEImprimerControleProcess() {
        super();
    }

    public CEImprimerControleProcess(BProcess parent) {
        super(parent);
    }

    public CEImprimerControleProcess(BSession session) {
        super(session);
    }

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
    protected boolean _executeProcess() throws Exception {

        boolean status = true;

        try {
            CERapport_P1_Doc documentP1 = new CERapport_P1_Doc();
            CERapport_P2_Doc documentP2 = new CERapport_P2_Doc();

            CEControleEmployeurManager manager = new CEControleEmployeurManager();

            documentP1.setSession(getSession());
            documentP1.setPreRapport(getPreRapport().booleanValue());
            documentP2.setSession(getSession());
            manager.setSession(getSession());

            // charger les plans d'affiliations pour cet affilie
            manager.setForControleEmployeurId(getControleId());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int idPlan = 0; idPlan < manager.size(); ++idPlan) {

                CEControleEmployeur ce = (CEControleEmployeur) manager.getEntity(idPlan);
                numAffilie = ce.getNumAffilie();

                creerDocumentP1(documentP1, getControleId());
                creerDocumentP2(documentP2, getControleId());

                if (JadeStringUtil.isEmpty(ce.getDateImpression())) {
                    ce.setDateImpression(dateImpression);

                    if (!getPreRapport()) {
                        ce.update(getTransaction());
                    }
                }
            }

            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber(CERapport_P1_Doc.DOC_NO);
            this.mergePDF(docInfo, true, 0, false, null);

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_RAPPORT_CONTROLE_ERREUR"));

            String messageInformation = "\n";
            messageInformation += "NumAffilie : " + numAffilie + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    /**
     * @param document
     * @param controleId
     * @throws Exception
     */
    private void creerDocumentP1(CERapport_P1_Doc document, String controleId) throws Exception {
        document.setParent(this);
        document.setIdControle(controleId);
        document.setDateImpression(dateImpression);
        document.setPreRapport(getPreRapport());

        document.executeProcess();
    }

    /**
     * @param document
     * @param controleId
     * @throws Exception
     */
    private void creerDocumentP2(CERapport_P2_Doc document, String controleId) throws Exception {
        document.setParent(this);
        document.setIdControle(controleId);

        document.executeProcess();
    }

    public String getControleId() {
        return controleId;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("IMPRESSION_RAPPORT_ERREUR");
        } else {
            return getSession().getLabel("IMPRESSION_RAPPORT") + " - " + numAffilie;
        }
    }

    public Boolean getPreRapport() {
        return preRapport;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setControleId(String string) {
        controleId = string;
    }

    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setPreRapport(Boolean preRapport) {
        this.preRapport = preRapport;
    }
}
