package globaz.naos.itext.affiliation;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.api.ITIRole;
import java.util.Iterator;

public class AFRadiation_Doc extends AFAbstractTiersDocument implements ILEGeneration {

    private static final long serialVersionUID = 4112464317017687689L;
    private static final String DOC_NO_IND = "0176CAF";
    private static final String DOC_NO_NA = "0177CAF";
    private static final String NOM_DOC = "RADIATION";
    private static final String P_CAISSE = "P_CAISSE";
    private static final String P_RADIATION = "P_RADIATION";
    private static final String P_RAPPEL = "P_RAPPEL";
    private static final String P_TEXTE = "P_TEXTE";
    private static final String P_TITRE = "P_TITRE";

    private static final String TEMPLATE_FILE_NAME = "NAOS_RADIATION";
    private AFAffiliation affiliation;
    private String csDocument;
    private String csTypeAffiliation;
    private String dateImpression;
    protected LEParamEnvoiDataSource docCourant;
    protected LEEnvoiDataSource documentDataSource;
    private boolean hasNext = true;
    private String idDestinataire;
    private String idEnvoiParent;

    private String idTiers;

    private String numAff = "";
    private String periode;

    public AFRadiation_Doc() throws Exception {
        super();
    }

    public AFRadiation_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFRadiation_Doc.NOM_DOC));
    }

    @Override
    public void addPropriete(String csTypeProp, String valeur) {
        if (ILEConstantes.CS_PARAM_GEN_ID_TIERS.equals(csTypeProp)) {
            setIdTiers(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_NUMERO.equals(csTypeProp)) {
            setNumAff(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ROLE.equals(csTypeProp)) {
            setCsTypeAffiliation(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(csTypeProp)) {
            setIdEnvoiParent(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(csTypeProp)) {
            setIdDestinataire(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_PERIODE.equals(csTypeProp)) {
            setPeriode(valeur);
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // remplir l'en-tête et la signature
            ICaisseReportHelper crh = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getLangueDestinataire());
            CaisseHeaderReportBean hb = new CaisseHeaderReportBean();
            hb.setAdresse(getAdresse().getAdresseDestinataire(getIdDestinataire(), getNumAff(), "519005"));
            hb.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), loadAffiliation().getTiers().getLangueIso()));
            hb.setEmailCollaborateur(getSession().getUserEMail());
            hb.setNoAffilie(loadAffiliation().getAffilieNumero());
            hb.setNoAvs(loadAffiliation().getTiers().getNumAvsActuel());
            hb.setNomCollaborateur(getSession().getUserFullName());
            hb.setTelCollaborateur(getSession().getUserInfo().getPhone());
            hb.setUser(getSession().getUserInfo());
            crh.addHeaderParameters(this, hb);
            crh.addSignatureParameters(this);

            setDocumentTitle(getSession().getLabel("RADIATION") + " " + loadAffiliation().getAffilieNumero());
            getExporter().setExportFileName(
                    getSession().getLabel("RADIATION") + " " + loadAffiliation().getAffilieNumero());

            // renseigner le texte
            ICTDocument document = loadCatalogue();
            StringBuffer buffer = new StringBuffer();

            setParametresGeneraux(buffer, document);

        } catch (Exception e) {
            abort();
            throw new FWIException("Erreur: " + e.getMessage(), e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFRadiation_Doc.TEMPLATE_FILE_NAME);
        for (int i = 0; i < getDocumentDataSource().getParamEnvoi().size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = getDocumentDataSource().getParamEnvoi().getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }
    }

    @Override
    public void createDataSource() throws Exception {
        super.createDataSource();
        fillDocInfo();
    }

    /**
     * Après l'impression d'un document
     */
    @Override
    public void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAff());
        try {
            loadAffiliation();
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(getNumAff()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    affiliation.getAffilieNumero(), affilieFormater.unformat(getNumAff()));
            if (isIndependant()) {
                getDocumentInfo().setDocumentTypeNumber(AFRadiation_Doc.DOC_NO_IND);
            } else {
                getDocumentInfo().setDocumentTypeNumber(AFRadiation_Doc.DOC_NO_NA);
            }
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAff());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());

    }

    public StringBuffer format(StringBuffer paragraphe, String varTemp) {
        StringBuffer res = new StringBuffer("");
        String chaineModifiee = paragraphe.toString();
        ;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        if ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // remplacement de la variable par sa valeur (varTemp)
            if (varTemp == "") {
                varTemp = " ";
            }
            res.append(CPToolBox.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
    }

    @Override
    public String getCategorie() {
        return ILEConstantes.CS_CATEGORIE_RADIATION;
    }

    public String getCsDocument() {
        return csDocument;
    }

    @Override
    public String getCsTypeAffiliation() {
        return csTypeAffiliation;
    }

    @Override
    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    public String getDomaine() {
        return null;
    }

    @Override
    public String getIdDestinataire() {
        return idDestinataire;
    }

    @Override
    public String getIdEnvoiParent() {
        return idEnvoiParent;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * retourne la langue de l'affilie (doit être appellé ap.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private String getLangueDestinataire() throws Exception {
        return getIsoLangueDestinataire();
    }

    @Override
    public int getNbLevel() {
        return 0;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFRadiation_Doc.NOM_DOC);
    }

    @Override
    public String getNumAff() {
        return numAff;
    }

    public String getNumeroRappel() {
        return "";
    }

    @Override
    public String getPeriode() {
        return periode;
    }

    @Override
    public LEEnvoiDataSource getResult() {

        return documentDataSource;
    }

    @Override
    protected String getTemplate() {
        return AFRadiation_Doc.TEMPLATE_FILE_NAME;
    }

    @Override
    protected void initDocument(String isoLangue) throws Exception {
    }

    public boolean isIndependant() throws Exception {
        String type = loadAffiliation().getTypeAffiliation();
        if (CodeSystem.TYPE_AFFILI_INDEP.equals(type) || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private AFAffiliation loadAffiliation() throws Exception {
        if (affiliation == null) {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForAffilieNumero(getNumAff());
            manager.setForTypesAffPersonelles();
            manager.find();
            affiliation = (AFAffiliation) manager.getFirstEntity();
        }

        return affiliation;
    }

    private ICTDocument loadCatalogue() throws Exception {
        // préparer le chargement
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        loader.setDefault(Boolean.TRUE);
        loader.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
        if (isIndependant()) {
            loader.setIdDocument(CodeSystem.ID_DOCUMENT_RADIATION_INDEPENDANT);
        } else {
            loader.setIdDocument(CodeSystem.ID_DOCUMENT_RADIATION_NONACTIF);
        }
        loader.setCodeIsoLangue(getLangueDestinataire());

        // trouver le catalogue
        ICTDocument[] candidats = loader.load();

        if ((candidats == null) || (candidats.length == 0)) {
            throw new Exception("Impossible de trouver le catalogue de texte");
        }

        return candidats[0];
    }

    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = !hasNext;
            docCourant = getDocumentDataSource().getParamEnvoi();
        }

        return retValue;
    }

    public void setCsDocument(String csDocument) {
        this.csDocument = csDocument;
    }

    @Override
    public void setCsTypeAffiliation(String csTypeAffiliation) {
        this.csTypeAffiliation = csTypeAffiliation;
    }

    @Override
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
    }

    @Override
    public void setHeader(CaisseHeaderReportBean bean, String isoLangueTiers) throws Exception {
        bean.setAdresse(getAdresse().getAdresseDestinataire(getIdDestinataire(), getNumAff()));
        getAdresse().getAdresseDestinataire(getIdDestinataire(), getNumAff(), "519005");
        String dateJJssMMssAAAA = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA()
                : getDateImpression();
        bean.setDate(JACalendar.format(dateJJssMMssAAAA, isoLangueTiers.toLowerCase()));
        bean.setNoAffilie(getNumAff());
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setUser(getSession().getUserInfo());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setNoAvs(" ");
    }

    @Override
    public void setIdDestinataire(String idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    @Override
    public void setIdEnvoiParent(String idEnvoiParent) {
        this.idEnvoiParent = idEnvoiParent;
    }

    @Override
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public void setNumAff(String numAff) {
        this.numAff = numAff;
    }

    private void setParametresGeneraux(StringBuffer buffer, ICTDocument document) throws Exception {
        // -- LE TITRE
        // -----------------------------------------------------------
        for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFRadiation_Doc.P_TITRE, buffer.toString());

        // -- LE TEXTE
        // -------------------------------------------------------------
        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        int i = 0;
        String[] valParams = new String[5];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        valParams[1] = loadAffiliation().getDateFin();
        valParams[2] = getPeriode();
        valParams[3] = getPeriode();
        valParams[4] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFRadiation_Doc.P_TEXTE, buffer.toString());

        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(3).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        this.setParametres(AFRadiation_Doc.P_CAISSE, buffer.toString());

        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(4).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        i = 0;
        valParams = new String[1];
        // {debutAnnee}
        valParams[0] = loadAffiliation().getDateFin();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFRadiation_Doc.P_RADIATION, buffer.toString());

        // ***************

        if (!JadeStringUtil.isEmpty(getNumeroRappel())) {
            buffer = new StringBuffer();
            for (Iterator textesIter = document.getTextes(5).iterator(); textesIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }

                buffer.append(((ICTTexte) textesIter.next()).getDescription());
            }

            if (!JadeStringUtil.isEmpty(buffer.toString())) {
                this.setParametres(AFRadiation_Doc.P_RAPPEL, "<style isBold=\"true\">" + getNumeroRappel()
                        + " </style>" + buffer.toString());
            }
        } else {
            this.setParametres(AFRadiation_Doc.P_RAPPEL, " ");
        }

    }

    @Override
    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
