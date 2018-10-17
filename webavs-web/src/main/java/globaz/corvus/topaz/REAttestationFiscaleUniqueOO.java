package globaz.corvus.topaz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.corvus.process.attestationsfiscales.REAgregateurDonneesPourAttestationsFiscales;
import ch.globaz.corvus.process.attestationsfiscales.REFamillePourAttestationsFiscales;
import ch.globaz.corvus.process.attestationsfiscales.RERentePourAttestationsFiscales;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import ch.globaz.topaz.mixer.postprocessor.PostProcessor;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscales;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscalesManager;
import globaz.corvus.utils.REGedUtils;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.tools.PRStringUtils;

public class REAttestationFiscaleUniqueOO extends REAbstractJobOO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String NUM_CAISSE_FERCIAM = "106";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ATTESTATION_FISCALE";

    private String adresse;
    private JadePrintDocumentContainer allDoc;
    private String anneeAttestations;
    private String assure;
    private ICaisseReportHelperOO caisseHelper;
    private String chf;
    private String codeIsoLangue;
    private String concerne;
    private DocumentData data;
    private String dateImpressionAttJJMMAAA;
    private DocumentData documentData;
    private String idTiers;
    private String idTiersBaseCalcul;
    private boolean isSendToGed;
    private List<String> listeAssure;
    private List<String> listeattAssure;
    private List<String> listeattMontant;
    private List<String> listeattPeriode;
    private List<String> listeMontant;
    private List<String> listePeriode;
    private String montant;
    private String NSS;
    private String para1;
    private String paraAPI;
    private String periode;
    private String salutation;
    private String siganture;
    private String sousConcerne;
    private String titre;
    private String titreAPI;
    private String total;
    private String traiterPar;
    private CatalogueText catalogueTextesAttestationsFiscales;

    public REAttestationFiscaleUniqueOO() {
        super(false);

        adresse = "";
        allDoc = null;
        anneeAttestations = "";
        assure = "";
        caisseHelper = null;
        chf = "";
        codeIsoLangue = "";
        concerne = "";
        data = null;
        dateImpressionAttJJMMAAA = "";
        documentData = null;
        idTiers = "";
        idTiersBaseCalcul = "";
        isSendToGed = false;
        listeAssure = null;
        listeattAssure = null;
        listeattMontant = null;
        listeattPeriode = null;
        listeMontant = null;
        listePeriode = null;
        montant = "";
        NSS = "";
        para1 = "";
        paraAPI = "";
        periode = "";
        salutation = "";
        siganture = "";
        sousConcerne = "";
        titre = "";
        titreAPI = "";
        total = "";
        traiterPar = "";
    }

    @Override
    protected List<CatalogueText> definirCataloguesDeTextes() {
        List<CatalogueText> catalogues = new ArrayList<CatalogueText>();

        catalogueTextesAttestationsFiscales = new CatalogueText();
        catalogueTextesAttestationsFiscales.setCodeIsoLangue(getCodeIsoLangue());
        catalogueTextesAttestationsFiscales.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueTextesAttestationsFiscales.setCsTypeDocument(IRECatalogueTexte.CS_ATTESTATION_FISCALE);
        catalogueTextesAttestationsFiscales.setNomCatalogue("openOffice");
        catalogues.add(catalogueTextesAttestationsFiscales);

        return catalogues;
    }

    /**
     * Recherche la valeur à l'<code>index</code> demandé. Retourne une chaîne vide si aucun valeur n'est présente
     *
     * @param list La liste de valeur
     * @param index L'index à rechercher
     * @return la valeur à l'<code>index</code> demandé. Retourne une chaîne vide si aucun valeur n'est présente
     */
    private String getListValue(List<String> list, int index) {
        String result = "";
        if (list.size() > index) {
            result = list.get(index);
        }
        return result;
    }

    @Override
    protected void genererDocument() throws Exception {
        data = new DocumentData();
        allDoc = new JadePrintDocumentContainer();
        data.addData("idProcess", "REAttestationFiscaleOO");

        JadePublishDocumentInfo pubInfosDestination = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfosDestination.setOwnerEmail(getAdresseEmail());
        pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
        pubInfosDestination.setDocumentTitle(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfosDestination.setDocumentSubject(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfosDestination.setArchiveDocument(false);
        pubInfosDestination.setPublishDocument(true);
        pubInfosDestination.setDocumentType(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES);
        pubInfosDestination.setDocumentTypeNumber(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES);

        allDoc.setMergedDocDestination(pubInfosDestination);

        // Définition des paramètres

        // Entête
        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
        crBean.setAdresse(getAdresse());
        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                getCodeIsoLangue());
        caisseHelper.setTemplateName(REAttestationFiscaleUniqueOO.FICHIER_MODELE_ENTETE_CORVUS);

        if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            // Uniquement pour la FERCIAM
            if ((NUM_CAISSE_FERCIAM).equals(CommonPropertiesUtils.getValue(CommonProperties.KEY_NO_CAISSE))) {
                CatalogueText catalogue = definirCataloguesDeTextes().get(0);
                crBean.setNomCollaborateur(getTraiterPar() + " " + getTexte(catalogue, 6, 2));
                crBean.setTelCollaborateur(getTexte(catalogue, 6, 3));
            } else {
                crBean.setNomCollaborateur(getTraiterPar() + " " + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

        }

        // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
        if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
            crBean.setConfidentiel(true);
        }

        if (!JadeStringUtil.isEmpty(getDateImpressionAttJJMMAAA())) {
            if ("FR".equals(codeIsoLangue)) {
                crBean.setDate(" le " + JACalendar.format(getDateImpressionAttJJMMAAA(), codeIsoLangue));
            } else if ("IT".equals(codeIsoLangue)) {
                crBean.setDate(" il " + JACalendar.format(getDateImpressionAttJJMMAAA(), codeIsoLangue));
            } else {
                crBean.setDate(" " + JACalendar.format(getDateImpressionAttJJMMAAA(), codeIsoLangue));
            }
        }

        data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);
        data = caisseHelper.addSignatureParameters(data, crBean);

        data.addData("idEntete", "CAISSE");

        // Corps du document
        concerne = PRStringUtils.replaceString(getConcerne(), "\n", "");
        data.addData("TITRE_ATTESTATION", concerne);
        sousConcerne = PRStringUtils.replaceString(getSousConcerne(), "\n", "");
        data.addData("nss_requerant", sousConcerne);
        data.addData("titre_tiers", getTitre());
        para1 = PRStringUtils.replaceString(getPara1(), "\n", "");
        data.addData("PARAGRAPHE1_ATTESTATION", para1);

        data.addData("ENTETE_TAB_ASSURE", getAssure());
        data.addData("ENTETE_TAB_PERIODE", getPeriode());
        data.addData("ENTETE_TAB_MONTANT", getMontant());
        data.addData("TOTAL", getTotal());
        data.addData("CHF", getChf());

        // Données du premier tableau
        Collection newTable = new Collection("TableauRenteAccordee");
        FWCurrency montantTotal = new FWCurrency("0.00");
        int i = 0;

        // Recherche de la liste la plus longue
        int maxSize = getListeattAssure().size();
        if (getListeattPeriode().size() > maxSize) {
            maxSize = getListeattPeriode().size();
        }
        if (getListeattMontant().size() > maxSize) {
            maxSize = getListeattMontant().size();
        }

        for (int ctr = 0; ctr < maxSize; ctr++) {
            String rente = getListValue(getListeattAssure(), ctr);
            String periode = getListValue(getListeattPeriode(), ctr);
            String montant = getListValue(getListeattMontant(), ctr);

            if (!JadeStringUtil.isEmpty(rente) || !JadeStringUtil.isEmpty(periode)
                    || !JadeStringUtil.isEmpty(montant)) {
                FWCurrency mt = new FWCurrency("0.00");
                mt.add(montant);

                DataList line1 = new DataList("ligneInfoRente");
                line1.addData("genre_rente", rente);
                line1.addData("periode_rente", "\n" + periode);
                line1.addData("CHF", "\n" + getChf());
                line1.addData("montant_rente", "\n" + mt.toStringFormat());
                montantTotal.add(montant);
                newTable.add(line1);
            }
        }
        data.add(newTable);
        data.addData("montant_total", montantTotal.toStringFormat());

        // Données du deuxième tableau
        Collection autreTable = new Collection("TableauAutre");
        // --------------------------------------------------------
        maxSize = getListeAssure().size();
        if (getListePeriode().size() > maxSize) {
            maxSize = getListePeriode().size();
        }
        if (getListeMontant().size() > maxSize) {
            maxSize = getListeMontant().size();
        }

        for (int ctr = 0; ctr < maxSize; ctr++) {
            String rente = getListValue(getListeAssure(), ctr);
            String periode = getListValue(getListePeriode(), ctr);
            String montant = getListValue(getListeMontant(), ctr);

            if (!JadeStringUtil.isEmpty(rente) || !JadeStringUtil.isEmpty(periode)
                    || !JadeStringUtil.isEmpty(montant)) {
                FWCurrency mt = new FWCurrency("0.00");
                mt.add(montant);

                DataList line1 = new DataList("ligneInfoRenteAutre");
                line1.addData("Autre_genre_rente", rente);
                line1.addData("Autre_periode_rente", "\n" + periode);
                line1.addData("CHF", "\n" + getChf());
                line1.addData("Autre_montant_rente", "\n" + mt.toStringFormat());
                autreTable.add(line1);
            }
        }

        data.add(autreTable);

        if (!JadeStringUtil.isEmpty(getTitreAPI())) {
            titreAPI = PRStringUtils.replaceString(getTitreAPI(), "\n", "");
            data.addData("TITRE_API", titreAPI);
        }
        if (!JadeStringUtil.isEmpty(getParaAPI())) {
            paraAPI = PRStringUtils.replaceString(getParaAPI(), "\n", "");
            data.addData("TEXTE_API", paraAPI + PostProcessor.GLUE_PARAGRAPHS);
        }
        salutation = PRStringUtils.replaceString(getSalutation(), "\n", "");
        data.addData("SALUTATION_ATTESTATION", salutation);
        data.addData("SIGNATURE", getSiganture());
        JadePublishDocumentInfo pubInfoAttestation = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfoAttestation.setDocumentTitle(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfoAttestation.setDocumentSubject(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfoAttestation.setOwnerEmail(getAdresseEmail());
        pubInfoAttestation.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
        pubInfoAttestation.setArchiveDocument(isSendToGed);
        try {
            if (isSendToGed) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concernée (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfoAttestation = h.setNssExtraFolderToDocInfo(getSession(), pubInfoAttestation, getIdTiers());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pubInfoAttestation.setPublishDocument(false);
        pubInfoAttestation.setDocumentType(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES);
        pubInfoAttestation.setDocumentTypeNumber(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES);
        pubInfoAttestation.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        // Mandat InfoRom 533, on recherche les rentes de la famille pour définir quelle type de rente doit figurer dans
        // les données pour la GED
        REDonneesPourAttestationsFiscalesManager manager = new REDonneesPourAttestationsFiscalesManager();
        manager.setSession(getSession());
        manager.setForAnnee(anneeAttestations);
        manager.setForIdTiersBaseCalcul(idTiersBaseCalcul);
        manager.setFiltrerParDateDeDecision(false);
        manager.setIsTiersBeneficiaire(true);
        manager.find();

        List<REDonneesPourAttestationsFiscales> donnees = new ArrayList<REDonneesPourAttestationsFiscales>();
        for (int k = 0; k < manager.size(); k++) {
            REDonneesPourAttestationsFiscales uneDonnee = (REDonneesPourAttestationsFiscales) manager.get(k);
            donnees.add(uneDonnee);
        }
        REAgregateurDonneesPourAttestationsFiscales agreagateur = new REAgregateurDonneesPourAttestationsFiscales(
                getSession());
        List<REFamillePourAttestationsFiscales> donneesTraitees = agreagateur.transformer(donnees);

        Set<CodePrestation> codesPrestationFamille = new HashSet<CodePrestation>();
        for (REFamillePourAttestationsFiscales uneAttestation : donneesTraitees) {
            for (RERentePourAttestationsFiscales uneRenteDeLaFamille : uneAttestation.getRentesDeLaFamille()) {
                if (!JadeStringUtil.isBlankOrZero(uneRenteDeLaFamille.getCodePrestation())
                        && JadeNumericUtil.isInteger(uneRenteDeLaFamille.getCodePrestation())) {
                    CodePrestation codePrestation = CodePrestation
                            .getCodePrestation(Integer.parseInt(uneRenteDeLaFamille.getCodePrestation()));
                    codesPrestationFamille.add(codePrestation);
                }
            }
        }

        pubInfoAttestation.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                REGedUtils.getCleGedPourTypeRente(getSession(), REGedUtils
                        .getTypeRentePourListeCodesPrestation(getSession(), codesPrestationFamille, false, true)));

        TIDocumentInfoHelper.fill(pubInfoAttestation, getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfoAttestation);

        this.createDocuments(allDoc);
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAnneeAttestations() {
        return anneeAttestations;
    }

    public String getAssure() {
        return assure;
    }

    public String getChf() {
        return chf;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getConcerne() {
        return concerne;
    }

    public String getDateImpressionAttJJMMAAA() {
        return dateImpressionAttJJMMAAA;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("ATTESTATION_FISCALE_RENTE");
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public boolean getIsSendToGed() {
        return isSendToGed;
    }

    public List<String> getListeAssure() {
        return listeAssure;
    }

    public List<String> getListeattAssure() {
        return listeattAssure;
    }

    public List<String> getListeattMontant() {
        return listeattMontant;
    }

    public List<String> getListeattPeriode() {
        return listeattPeriode;
    }

    public List<String> getListeMontant() {
        return listeMontant;
    }

    public List<String> getListePeriode() {
        return listePeriode;
    }

    public String getMontant() {
        return montant;
    }

    @Override
    public String getName() {
        return getSession().getLabel("ATTESTATION_FISCALE_RENTE");
    }

    public String getNSS() {
        return NSS;
    }

    public String getPara1() {
        return para1;
    }

    public String getParaAPI() {
        return paraAPI;
    }

    public String getPeriode() {
        return periode;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getSiganture() {
        return siganture;
    }

    public String getSousConcerne() {
        return sousConcerne;
    }

    public String getTitre() {
        return titre;
    }

    public String getTitreAPI() {
        return titreAPI;
    }

    public String getTotal() {
        return total;
    }

    public String getTraiterPar() {
        return traiterPar;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeAttestations(String anneeAttestations) {
        this.anneeAttestations = anneeAttestations;
    }

    public void setAssure(String assure) {
        this.assure = assure;
    }

    public void setChf(String chf) {
        this.chf = chf;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setConcerne(String concerne) {
        this.concerne = concerne;
    }

    public void setDateImpressionAttJJMMAAA(String dateImpressionAttJJMMAAA) {
        this.dateImpressionAttJJMMAAA = dateImpressionAttJJMMAAA;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    public void setIsSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setListeAssure(List<String> listeAssure) {
        this.listeAssure = listeAssure;
    }

    public void setListeattAssure(List<String> listeattAssure) {
        this.listeattAssure = listeattAssure;
    }

    public void setListeattMontant(List<String> listeattMontant) {
        this.listeattMontant = listeattMontant;
    }

    public void setListeattPeriode(List<String> listeattPeriode) {
        this.listeattPeriode = listeattPeriode;
    }

    public void setListeMontant(List<String> listeMontant) {
        this.listeMontant = listeMontant;
    }

    public void setListePeriode(List<String> listePeriode) {
        this.listePeriode = listePeriode;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

    public void setPara1(String para1) {
        this.para1 = para1;
    }

    public void setParaAPI(String paraAPI) {
        this.paraAPI = paraAPI;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public void setSiganture(String siganture) {
        this.siganture = siganture;
    }

    public void setSousConcerne(String sousConcerne) {
        this.sousConcerne = sousConcerne;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTitreAPI(String titreAPI) {
        this.titreAPI = titreAPI;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTraiterPar(String traiterPar) {
        this.traiterPar = traiterPar;
    }
}