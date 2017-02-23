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
import globaz.leo.process.generation.ILEGeneration;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.api.ITIRole;
import java.util.Iterator;

public class AFNouvelleAffiliation_Doc extends AFAbstractTiersDocument implements ILEGeneration {

    private static final long serialVersionUID = 7253288804904230038L;
    private static final String DOC_NO_IND = "0174CAF";

    private static final String DOC_NO_NA = "0175CAF";
    private static Boolean isAnneeCourrante = Boolean.TRUE;
    private static final String NOM_DOC = "NOUVELLE_AFFILIATION";
    private static final String P_CAISSE = "P_CAISSE";
    private static final String P_CHOIX1 = "P_CHOIX1";
    private static final String P_CHOIX2 = "P_CHOIX2";
    private static final String P_ESTIMATION = "P_ESTIMATION";
    private static final String P_ISINDEPENDANT = "P_ISINDEPENDANT";
    private static final String P_RAPPEL = "P_RAPPEL";
    private static final String P_REMARQUE = "P_REMARQUE";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_TEXTE = "P_TEXTE";
    private static final String P_TIMBRE = "P_TIMBRE";
    private static final String P_TITRE = "P_TITRE";

    private static final String TEMPLATE_FILE_NAME = "NAOS_NOUVELLE_AFFILIATION";
    private AFAffiliation affiliation;
    private String annee = "";

    private boolean hasNext = true;

    public AFNouvelleAffiliation_Doc() throws Exception {
        super();
    }

    public AFNouvelleAffiliation_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFNouvelleAffiliation_Doc.NOM_DOC));
    }

    public static Boolean getIsAnneeCourrante() {
        return AFNouvelleAffiliation_Doc.isAnneeCourrante;
    }

    public static void setIsAnneeCourrante(Boolean isAnneeCourrante) {
        AFNouvelleAffiliation_Doc.isAnneeCourrante = isAnneeCourrante;
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

            setDocumentTitle(getSession().getLabel("NOUVELLE_AFFILIATION") + " " + loadAffiliation().getAffilieNumero());
            getExporter().setExportFileName(
                    getSession().getLabel("NOUVELLE_AFFILIATION") + " " + loadAffiliation().getAffilieNumero());

            // renseigner le texte
            ICTDocument document = loadCatalogue();
            StringBuffer buffer = new StringBuffer();

            // ***************
            if (isIndependant()) {
                setParametresIND(buffer, document);
            } else {
                setParametresNA(buffer, document);
            }

            // ***************
            if (!JadeStringUtil.isEmpty(getNumeroRappel())) {
                buffer = new StringBuffer();
                for (Iterator textesIter = document.getTextes(10).iterator(); textesIter.hasNext();) {
                    if (buffer.length() > 0) {
                        buffer.append("\n\n");
                    }

                    buffer.append(((ICTTexte) textesIter.next()).getDescription());
                }

                if (!JadeStringUtil.isEmpty(buffer.toString())) {
                    this.setParametres(AFNouvelleAffiliation_Doc.P_RAPPEL, "<style isBold=\"true\">"
                            + getNumeroRappel() + " </style>" + buffer.toString());
                }
            } else {
                this.setParametres(AFNouvelleAffiliation_Doc.P_RAPPEL, " ");
            }

        } catch (Exception e) {
            abort();
            throw new FWIException("Erreur: " + e.getMessage(), e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFNouvelleAffiliation_Doc.TEMPLATE_FILE_NAME);
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
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    affiliation.getAffilieNumero(), affilieFormater.unformat(getNumAff()));
            getDocumentInfo().setDocumentTypeNumber(giveNumeroInforom());

        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAff());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(getPeriode()));
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateImpression());
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

    private String getAnneeCourrante() {
        if (JadeStringUtil.isEmpty(annee)) {
            annee = String.valueOf((JACalendar.today().getYear()));
        }
        return annee;
    }

    @Override
    public String getCategorie() {
        return ILEConstantes.CS_CATEGORIES_NOUVELLE_AFFILIATION;
    }

    @Override
    public String getDomaine() {
        return null;
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
        return getSession().getLabel(AFNouvelleAffiliation_Doc.NOM_DOC);
    }

    public String getNumeroRappel() {
        return "";
    }

    @Override
    public LEEnvoiDataSource getResult() {
        return documentDataSource;
    }

    @Override
    protected String getTemplate() {
        return AFNouvelleAffiliation_Doc.TEMPLATE_FILE_NAME;
    }

    protected String giveNumeroInforom() throws Exception {
        if (isIndependant()) {
            return AFNouvelleAffiliation_Doc.DOC_NO_IND;
        }

        return AFNouvelleAffiliation_Doc.DOC_NO_NA;
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
            loader.setIdDocument(CodeSystem.ID_DOCUMENT_NOUVELLE_AFFILIATION_INDEPENDANT);
        } else {
            loader.setIdDocument(CodeSystem.ID_DOCUMENT_NOUVELLE_AFFILIATION_NONACTIF);
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
        }

        return retValue;
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

    private void setParametresIND(StringBuffer buffer, ICTDocument document) throws Exception {

        this.setParametres(AFNouvelleAffiliation_Doc.P_ISINDEPENDANT, Boolean.TRUE);

        // -- LE TITRE
        // -----------------------------------------------------------
        for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_TITRE, buffer.toString());

        // -- LE TEXTE
        // -------------------------------------------------------------
        buffer.setLength(0);

        if (getPeriode().equals(getAnneeCourrante())) {
            AFNouvelleAffiliation_Doc.setIsAnneeCourrante(Boolean.TRUE);
            for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }

                buffer.append(((ICTTexte) textesIter.next()).getDescription());
            }
        } else {
            AFNouvelleAffiliation_Doc.setIsAnneeCourrante(Boolean.FALSE);
            for (Iterator textesIter = document.getTextes(11).iterator(); textesIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }

                buffer.append(((ICTTexte) textesIter.next()).getDescription());
            }
        }
        this.setParametres(AFNouvelleAffiliation_Doc.P_ISINDEPENDANT, AFNouvelleAffiliation_Doc.getIsAnneeCourrante());

        int i = 0;
        String[] valParams = new String[4];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        valParams[1] = getPeriode();
        valParams[2] = getPeriode();
        valParams[3] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_TEXTE, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(3).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_CAISSE, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(4).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_ESTIMATION, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(5).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_REMARQUE, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(6).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_CHOIX1, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(7).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_CHOIX2, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(8).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_SIGNATURE, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(9).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_TIMBRE, buffer.toString());

    }

    private void setParametresNA(StringBuffer buffer, ICTDocument document) throws Exception {

        this.setParametres(AFNouvelleAffiliation_Doc.P_ISINDEPENDANT, Boolean.FALSE);

        // -- LE TITRE
        // -----------------------------------------------------------
        for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_TITRE, buffer.toString());

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
        String[] valParams = new String[4];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        valParams[1] = getPeriode();
        valParams[2] = getPeriode();
        valParams[3] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFNouvelleAffiliation_Doc.P_TEXTE, buffer.toString());

        buffer = new StringBuffer();
        for (Iterator titresIter = document.getTextes(3).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }
        this.setParametres(AFNouvelleAffiliation_Doc.P_CAISSE, buffer.toString());

        this.setParametres(AFNouvelleAffiliation_Doc.P_ESTIMATION, " ");

        this.setParametres(AFNouvelleAffiliation_Doc.P_REMARQUE, " ");

        this.setParametres(AFNouvelleAffiliation_Doc.P_CHOIX1, " ");

        this.setParametres(AFNouvelleAffiliation_Doc.P_CHOIX2, " ");

        this.setParametres(AFNouvelleAffiliation_Doc.P_SIGNATURE, " ");

        this.setParametres(AFNouvelleAffiliation_Doc.P_TIMBRE, " ");
    }

}
