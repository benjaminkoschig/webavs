package globaz.prestation.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.application.REApplication;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.ij.application.IJApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater03;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Hashtable;
import java.util.LinkedList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class PRLettreEnTeteOO {

    public static final String DOMAINE_APG = "APG";
    public static final String DOMAINE_CORVUS = "CORVUS";
    public static final String DOMAINE_IJAI = "IJAI";
    public static final String DOMAINE_MAT = "MAT";
    public static final String FICHIER_MODELE_ENTETE_APG = "AP_LETTRE_ENTETE_OO";
    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ENTETE";
    public static final String FICHIER_MODELE_ENTETE_IJ = "IJ_LETTRE_ENTETE_OO";

    private ICaisseReportHelperOO caisseHelper = null;
    private String codeIsoLangue = "";
    private DocumentData data = null;
    private String dateDecision = "";
    private String dateDocument = "";
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String domaineLettreEnTete = "";
    private String idAffilie = "";
    private boolean isDemandeCompensation = false;
    private Boolean isEnteteOAI = Boolean.FALSE;
    private LinkedList lignes = new LinkedList();
    private int nbTrue = 0;
    private String noOfficeAI = "";
    private String numeroDeDecisionAI = "";
    private String periodeDecision = "";
    private String reference = "";
    private BSession session = null;
    private String specificHeader = "";
    private PRTiersWrapper tierAdresse = null;

    private void chargementCatalogueTexte() throws Exception {

        codeIsoLangue = getSession().getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        // chargement du catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());

        if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_APG)
                || getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_MAT)) {
            documentHelper.setCsDomaine("52018001");
            documentHelper.setCsTypeDocument("52019008");
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_IJAI)) {
            documentHelper.setCsDomaine("52425001");
            documentHelper.setCsTypeDocument("52426008");
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_CORVUS)) {
            documentHelper.setCsDomaine("52843001");
            documentHelper.setCsTypeDocument("52844002");
        }

        documentHelper.setDefault(Boolean.TRUE);
        documentHelper.setActif(Boolean.TRUE);

        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception("Impossible de charger le catalogue de texte");
        } else {
            document = documents[0];
        }

    }

    public void generationLettre() throws Exception {

        chargementCatalogueTexte();

        data = new DocumentData();
        data.addData("idProcess", "PRLettreEnTeteOO");

        remplissageDocument();

        gestionEnTeteEtSignature();

    }

    private void gestionEnTeteEtSignature() throws Exception {

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        String adresse = "";

        if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_APG)
                || getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_MAT)) {
            adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519002");
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_IJAI)) {
            adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519009");
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_CORVUS)) {
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), "519006", getIdAffilie(), "", null, "");

            // Ajoute dans l'entête de la lettre qui a traité le dossier si
            // necessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                crBean.setNomCollaborateur(document.getTextes(1).getTexte(5).getDescription() + " "
                        + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            if ("true".equals(getSession().getApplication().getProperty("documents.is.confidentiel"))) {
                crBean.setConfidentiel(true);
            }

        }

        crBean.setAdresse(adresse);

        // Ajoute la date dans l'entête de la lettre sauf si la date est
        // renseignée
        if (JadeStringUtil.isBlankOrZero(getDateDocument())) {
            crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));
        } else {

            crBean.setDate(JACalendar.format(getDateDocument(), codeIsoLangue));
        }

        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                codeIsoLangue);

        if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_APG)
                || getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_MAT)) {
            caisseHelper.setTemplateName(PRLettreEnTeteOO.FICHIER_MODELE_ENTETE_APG);
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_IJAI)) {
            caisseHelper.setTemplateName(PRLettreEnTeteOO.FICHIER_MODELE_ENTETE_IJ);
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_CORVUS)) {
            caisseHelper.setTemplateName(PRLettreEnTeteOO.FICHIER_MODELE_ENTETE_CORVUS);
        }

        data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

        // 1) EnTete et signature OAI
        if (getIsEnteteOAI().booleanValue()) {

            // En-tête
            data.addData("idEntete", "AVS_AI");
            data.addData("idFooter", "AVS_AI");

            // Recherche de l'adresse selon code Administration
            String adresseOfficeAi = "";

            TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
            tiAdministrationMgr.setSession(getSession());
            tiAdministrationMgr.setForCodeAdministration(getNoOfficeAI());
            tiAdministrationMgr.setForGenreAdministration("509004");
            tiAdministrationMgr.find();

            TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();

            if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_IJAI)) {
                adresseOfficeAi = tiAdministration.getAdresseAsString(TIAvoirAdresse.CS_COURRIER,
                        IJApplication.CS_DOMAINE_ADRESSE_IJAI, JACalendar.todayJJsMMsAAAA(),
                        new PRTiersAdresseCopyFormater03());
            } else if (getDomaineLettreEnTete().equals(PRLettreEnTeteOO.DOMAINE_CORVUS)) {
                // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                // se trouvant dans le fichier corvus.properties
                adresseOfficeAi = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                        tiAdministration.getIdTiers(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "",
                        new PRTiersAdresseCopyFormater03(), JACalendar.todayJJsMMsAAAA());
            }

            data.addData("HEADER_ADRESSE_CAISSE", adresseOfficeAi);

            // Signature
            if (null != tiAdministration) {
                data.addData("SIGNATURE_NOM_CAISSE", tiAdministration.getNomPrenom());
            } else {
                data.addData("SIGNATURE_NOM_CAISSE", "Office AI introuvable");
            }

            // 2) EnTete et signature Caisse
        } else {

            // En-tête
            data.addData("idEntete", "CAISSE");
            data.addData("idFooter", "CAISSE");

            // Signature
            data = caisseHelper.addSignatureParameters(data, crBean);

        }

    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public Boolean getIsEnteteOAI() {
        return isEnteteOAI;
    }

    public LinkedList getLignes() {
        return lignes;
    }

    public int getNbTrue() {
        return nbTrue;
    }

    public String getNoOfficeAI() {
        return noOfficeAI;
    }

    public String getNumeroDeDecisionAI() {
        return numeroDeDecisionAI;
    }

    public String getPeriodeDecision() {
        return periodeDecision;
    }

    public BSession getSession() {
        return session;
    }

    public String getSpecificHeader() {
        return specificHeader;
    }

    public PRTiersWrapper getTierAdresse() {
        return tierAdresse;
    }

    public boolean isDemandeCompensation() {
        return isDemandeCompensation;
    }

    private void remplissageDocument() throws Exception {

        try {

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }

            StringBuffer buffer = new StringBuffer();

            buffer.append(tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            data.addData("referenceProcedureComunication", reference);
            data.addData("titreTiers", buffer.toString());

            if (isDemandeCompensation()) {
                data.addData("TEXTE_LETTRE", document.getTextes(1).getTexte(4).getDescription());
            } else {
                data.addData("TEXTE_LETTRE", document.getTextes(1).getTexte(2).getDescription());
            }

            data.addData("SALUTATIONS", PRStringUtils.replaceString(document.getTextes(1).getTexte(3).getDescription(),
                    "{titreTiers}",
                    tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
            data.addData("annexe", document.getTextes(2).getTexte(1).getDescription());

            setDocumentData(data);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDemandeCompensation(boolean isDemandeCompensation) {
        this.isDemandeCompensation = isDemandeCompensation;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIsEnteteOAI(Boolean isEnteteOAI) {
        this.isEnteteOAI = isEnteteOAI;
    }

    public void setLignes(LinkedList lignes) {
        this.lignes = lignes;
    }

    public void setNbTrue(int nbTrue) {
        this.nbTrue = nbTrue;
    }

    public void setNoOfficeAI(String noOfficeAI) {
        this.noOfficeAI = noOfficeAI;
    }

    public void setNumeroDeDecisionAI(String numeroDeDecisionAI) {
        this.numeroDeDecisionAI = numeroDeDecisionAI;
    }

    public void setPeriodeDecision(String periodeDecision) {
        this.periodeDecision = periodeDecision;
    }

    public void setReferenceProcedureComunication(String reference) {
        this.reference = reference;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSpecificHeader(String specificHeader) {
        this.specificHeader = specificHeader;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }
}
