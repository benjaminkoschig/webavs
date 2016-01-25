package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.draco.application.DSApplication;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

public class DSContMandatAgentAvsRappel2_Doc extends DSAbstractPrincipaleGenererEtape {

    private static final long serialVersionUID = 1L;
    public static final String CS_DOMAINE_DOC = "124001";
    public static final String CS_TYPE_DOC = "1250010";
    private static final String NUM_INFOROM = "0217CDS";
    private static final String TEMPLATE_RAPPEL_AGENT_2 = "DRACO_RAPPEL_AGENT_2";
    private String langueIsoDest;

    public DSContMandatAgentAvsRappel2_Doc() throws Exception {
        super();
    }

    public DSContMandatAgentAvsRappel2_Doc(BSession session) throws Exception {
        super(session, session.getLabel(DSPreImpressionContentieux_Param.L_NOMDOCCONTENTIEUX));
    }

    public DSContMandatAgentAvsRappel2_Doc(BSession session, String nomDocument) throws Exception {
        super(session, nomDocument);
    }

    @Override
    protected String getTemplate() {
        return TEMPLATE_RAPPEL_AGENT_2;
    }

    @Override
    public void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        TITiers tiers = getTiers(getIdDestinataire());
        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA()));
        String dateJJssMMssAAAA = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA()
                : getDateImpression();
        bean.setDate(JACalendar.format(dateJJssMMssAAAA, tiers.getLangueIso().toLowerCase()));
        bean.setNoAffilie(getNumAff());
        // bean.setNoAvs(" ");
        bean.setEmailCollaborateur(" ");
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setDocumentTitle(getSession().getLabel("AGENTAVS_RAPPEL2"));
        setFileTitle(getSession().getLabel("AGENTAVS_RAPPEL2"));
    }

    @Override
    protected String format(String paragraphe) throws Exception {
        String res = "";
        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                res += getDateEnvoiDocPrecedent(langueIsoDest);
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '1') {
                res += getSession().getApplication().getLabel("MADAME_MONSIEUR", langueIsoDest);
                i = i + 2;
            }
        }
        return res;
    }

    @Override
    public String getDocInforomNum() {
        return DSContMandatAgentAvsRappel2_Doc.NUM_INFOROM;
    }

    @Override
    protected String getEMailObject() {
        String documentFileTitle = (JadeStringUtil.isBlank(getFileTitle()) ? getDocumentTitle() : getFileTitle());
        StringBuffer buffer = new StringBuffer("L'impression du document '");
        buffer.append(documentFileTitle);
        if (isOnError()) {
            buffer.append("' s'est terminée en erreur");
        } else {
            buffer.append("' s'est terminée avec succès");
        }
        return buffer.toString();
    }

    @Override
    protected ICTDocument[] getICTDocument() {
        ICTDocument[] candidats = null;
        try {

            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            langueIsoDest = getTiers(getIdDestinataire()).getLangueIso();
            helper.setCsDomaine(DSContMandatAgentAvsRappel2_Doc.CS_DOMAINE_DOC); // domaine avs
            helper.setCsTypeDocument(DSContMandatAgentAvsRappel2_Doc.CS_TYPE_DOC); // pour le type de catalogue
            helper.setCodeIsoLangue(langueIsoDest);
            helper.setActif(Boolean.TRUE); // actif
            helper.setDefault(Boolean.TRUE); // et par défaut

            // charger le catalogue de texte
            candidats = helper.load();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while getting document");
            candidats = null;
        }

        return candidats;
    }

    private TITiers getTiers(String idTiers) {
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        if (!JadeStringUtil.isEmpty(idTiers)) {
            tiers.setIdTiers(idTiers);
        } else {
            tiers.setIdTiers(getIdDestinataire());
        }
        try {
            tiers.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERREUR_GETTING_TIERS"));
        }
        return tiers;
    }

    @Override
    protected void initDocument(String isoLangue) throws Exception {
        setDocumentTitle(getSession().getLabel("AGENTAVS_RAPPEL2"));
        document = getICTDocument();
        try {
            nbNiveaux = document[0].size();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("DOC_VIDE"));
        }

        this.setParametres(DSPreImpressionContentieux_Param.P_CONCERNE, getTexte(1));
        this.setParametres("P_TIERS_LABEL", getSession().getApplication().getLabel("CONCERNE_TIERS", langueIsoDest));
        this.setParametres(
                "P_TIERS",
                getTiers(getIdTiers()).getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA()));
        this.setParametres("P_OBJET_LABEL", getSession().getApplication().getLabel("OBJET_AGENT", langueIsoDest));
        this.setParametres("P_OBJET", getSession().getApplication().getLabel("DECOMPTE_EMPLOYEUR", langueIsoDest) + " "
                + getPeriode());
        this.setParametres(DSPreImpressionContentieux_Param.P_CORPS, getTexte(2));
        this.setParametres(DSPreImpressionContentieux_Param.P_SIGNATURE, getTexte(3));
    }
}
