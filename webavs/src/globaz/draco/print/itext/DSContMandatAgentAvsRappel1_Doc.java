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

public class DSContMandatAgentAvsRappel1_Doc extends DSAbstractPrincipaleGenererEtape {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_DOMAINE_DOC = "124001";
    public static final String CS_TYPE_DOC = "125009";
    private final static String NUM_INFOROM = "0216CDS";
    public static final String TEMPLATE_RAPPEL_AGENT_1 = "DRACO_RAPPEL_AGENT_1";
    private String langueIsoDest;

    public DSContMandatAgentAvsRappel1_Doc() throws Exception {
        super();
    }

    public DSContMandatAgentAvsRappel1_Doc(BSession session) throws Exception {
        super(session, session.getLabel(DSPreImpressionContentieux_Param.L_NOMDOCCONTENTIEUX));
    }

    public DSContMandatAgentAvsRappel1_Doc(BSession session, String nomDocument) throws Exception {
        super(session, nomDocument);
    }

    @Override
    public void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        TITiers tiersDest = getTiers(getIdDestinataire());
        bean.setAdresse(tiersDest.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA()));
        String dateJJssMMssAAAA = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA()
                : getDateImpression();
        bean.setDate(JACalendar.format(dateJJssMMssAAAA, tiersDest.getLangueIso().toLowerCase()));
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
        setDocumentTitle(getSession().getLabel("AGENTAVS_RAPPEL1"));
        setFileTitle(getSession().getLabel("AGENTAVS_RAPPEL1"));
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
        return DSContMandatAgentAvsRappel1_Doc.NUM_INFOROM;
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
            helper.setCsDomaine(DSContMandatAgentAvsRappel1_Doc.CS_DOMAINE_DOC); // domaine avs
            helper.setCsTypeDocument(DSContMandatAgentAvsRappel1_Doc.CS_TYPE_DOC); // pour le type de catalogue
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

    @Override
    protected String getTemplate() {
        return DSContMandatAgentAvsRappel1_Doc.TEMPLATE_RAPPEL_AGENT_1;
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
        setDocumentTitle(getSession().getLabel("AGENTAVS_RAPPEL1"));
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
