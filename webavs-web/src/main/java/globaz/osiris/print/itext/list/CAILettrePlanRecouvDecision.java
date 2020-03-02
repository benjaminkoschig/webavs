package globaz.osiris.print.itext.list;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.external.IntTiers;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alexandre Cuva, 13-mai-2005
 */
public class CAILettrePlanRecouvDecision extends CADocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0041GCA";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIEcheancierLettreDecision";
    private String compteCADesc = "";
    private String dateRef = "";

    private String idDocument = "";
    /** Données du formulaire */
    private String idPlanRecouvrement = "";
    private Boolean joindreBVR = new Boolean(false);
    private String observation = "";
    private CAPlanRecouvrement plan = null;

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvDecision(BProcess parent) throws FWIException {
        super(parent, CAILettrePlanRecouvDecision.TEMPLATE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvDecision(BSession parent) throws FWIException {
        super(parent, CAILettrePlanRecouvDecision.TEMPLATE_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterPrintDocument()
     */
    @Override
    public void afterExecuteReport() {
        try {
            super.afterExecuteReport();

            createVoiesDroit();

        } catch (FWIException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();

        if ((getPlanRecouvrement() != null) && (getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank((getPlanRecouvrement()).getCompteAnnexe().getId())) {
                String numAff = (getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
                getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(),
                            (getPlanRecouvrement()).getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            numAff, affilieFormater.unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
                }
            }
        }
        // this.getDocumentInfo().setDocumentType(CAILettrePlanRecouvDecision.NUMERO_REFERENCE_INFOROM);
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        try {
            imprimerPlansRecouvDecision();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }
        setNumeroReferenceInforom(CAILettrePlanRecouvDecision.NUMERO_REFERENCE_INFOROM);

        // if ((this.getPlanRecouvrement() != null) && (this.getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
        // if (!JadeStringUtil.isBlank((this.getPlanRecouvrement()).getCompteAnnexe().getId())) {
        // String numAff = (this.getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
        // this.getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
        // try {
        // IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
        // TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
        // this.getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
        // affilieFormater.unformat(numAff));
        // TIDocumentInfoHelper.fill(this.getDocumentInfo(), (this.getPlanRecouvrement()).getCompteAnnexe()
        // .getIdTiers(), this.getSession(), ITIRole.CS_AFFILIE, numAff, affilieFormater
        // .unformat(numAff));
        // } catch (Exception e) {
        // this.getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        // }
        // this.getDocumentInfo().setArchiveDocument(true);
        // }
        // }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        IntTiers affilie = null;

        // Récupération des données
        setPlanRecouvrement((CAPlanRecouvrement) currentEntity());

        // On récupère les documents du catalogue de textes nécessaires
        setTypeDocument(CACodeSystem.CS_TYPE_SURSIS_DECISION);
        setIdDocument(getIdDocument());

        if (!JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getId())) {
            compteCADesc = getPlanRecouvrement().getCompteAnnexe().getDescription();
            affilie = getPlanRecouvrement().getCompteAnnexe().getTiers();
        }
        _setLangueFromTiers(affilie);
        // Gestion du modèle et du titre
        setTemplateFile(CAILettrePlanRecouvDecision.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("OSIRIS_LETTRE_PLAN_RECOUV_DECISION") + " " + compteCADesc);
        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(getPlanRecouvrement(), true, false, true, getDateRef());

        // Renseigne les paramètres du document
        this.setParametres(CAILettrePlanRecouvParam.P_CONCERNE, format(getTexte(1, 1)));
        StringBuilder corps = new StringBuilder("");
        dumpNiveau(2, corps, "");
        if (getJoindreBVR().booleanValue()) {
            corps.append(getTexte(3, 1)); // Texte : Veuillez trouvez ci-join l'échéancier accompagné des BVR.
        } else {
            corps.append(getTexte(3, 2)); // Texte : Veuillez trouvez ci-join l'échéancier.
        }

        dumpNiveau(4, corps, "");
        dumpNiveau(5, corps, "");
        dumpNiveau(6, corps, "");
        dumpNiveau(7, corps, "");

        String texteCorps = format(corps);
        // Si le buffer contient du texte stylé, on remplace le ou les " & " par " et "
        if (isStyledText(texteCorps)) {
            String strBuffer = texteCorps;
            texteCorps = strBuffer.replaceAll(" & ", " et ");
        }

        this.setParametres(CAILettrePlanRecouvParam.P_TEXT_CORPS, texteCorps);
        this.setParametres(CAILettrePlanRecouvParam.P_ANNEXE, getTexte(8, 1).toString());

        if (getJoindreBVR().booleanValue()) {
            this.setParametres(CAILettrePlanRecouvParam.T10, getJoindreBVR());
        }
    }

    /**
     * Prépare et retourne le document "Voies de droit"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @return le document "Voies de droit"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvVoiesDroit createVoiesDroit() throws FWIException, Exception {
        // Instancie le document : Voies de droit
        CAILettrePlanRecouvVoiesDroit documentVD = new CAILettrePlanRecouvVoiesDroit(this);
        documentVD.setSession(getSession());
        // Demander le traitement du document
        documentVD.setEMailAddress(getEMailAddress());
        documentVD.setIdPlanRecouvrement(getIdPlanRecouvrement());
        documentVD.setSendCompletionMail(false);
        documentVD.executeProcess();

        List<?> versoGenere = documentVD.getDocumentList();
        if (versoGenere.isEmpty() != false) {
            getDocumentList().add(versoGenere.get(0));
        }

        return documentVD;
    }

    /**
     * Formate le texte. Remplace un {0} par la date d'échéance
     * 
     * @param paragraphe
     * @return
     */
    private String format(StringBuilder paragraphe) {
        StringBuilder res = new StringBuilder("");
        String titre = "";

        titre = getPlanRecouvrement().getCompteAnnexe().getTiers().getPolitesse();

        try {
            for (int i = 0; i < paragraphe.length(); i++) {
                if (paragraphe.charAt(i) != '{') {
                    res.append(paragraphe.charAt(i));
                } else if (paragraphe.charAt(i + 1) == '1') {
                    res.append(compteCADesc);
                    i += 2;
                } else if (paragraphe.charAt(i + 1) == '2') {
                    res.append(titre);
                    i += 2;
                } else if (paragraphe.charAt(i + 1) == '3') {
                    res.append(JACalendar.format(getPlanRecouvrement().getDateEcheance(), _getLangue()));
                    i += 2;
                } else if (paragraphe.charAt(i + 1) == '4') {
                    res.append(JANumberFormatter.format(getPlanRecouvrement().getCumulSoldeSections()));
                    i += 2;
                } else if (paragraphe.charAt(i + 1) == '5') {
                    if (getPlanRecouvrement().getPartPenale().booleanValue()) {
                        res.append(getTexte(9, 90).toString());
                    } else {
                        res.append("  ");
                    }
                    i += 2;
                } else if (paragraphe.charAt(i + 1) == '6') {
                    res.append(getObservation());
                    i += 2;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return res.toString();
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    /**
     * @return the idDocument
     */
    @Override
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Boolean getJoindreBVR() {
        return joindreBVR;
    }

    /**
     * @return the observation
     */
    public String getObservation() {
        return observation;
    }

    /**
     * @return the plan
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        return plan;
    }

    /**
     * Imprimer la lettre de decision pour le plan de recouvrement
     */
    private void imprimerPlansRecouvDecision() throws JAException {
        try {
            CAPlanRecouvrement planRecouvrement = new CAPlanRecouvrement();
            planRecouvrement.setSession(getSession());
            planRecouvrement.setId(getIdPlanRecouvrement());
            planRecouvrement.retrieve(getTransaction());
            if (!JadeStringUtil.isBlank(planRecouvrement.getId())) {
                addEntity(planRecouvrement);
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
        }
    }

    /**
     * @param text
     * @return
     */
    private boolean isStyledText(String text) {
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(".*<style.*", Pattern.DOTALL);
        matcher = pattern.matcher(text);

        return matcher.matches();
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
    @Override
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setJoindreBVR(Boolean newJoindreBVR) {
        joindreBVR = newJoindreBVR;
    }

    /**
     * @param observation
     *            the observation to set
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * @param plan
     *            the plan to set
     */
    public void setPlanRecouvrement(CAPlanRecouvrement plan) {
        this.plan = plan;
    }
}
