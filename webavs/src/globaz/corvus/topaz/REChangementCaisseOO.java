package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.application.REApplication;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REChangementCaisseOO {

    private static final String CDT_DATEENVOI = "{dateEnvoi}";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_TRANSFERT";

    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue;
    private DocumentData data;
    private String dateDocument;
    private String DateEnvoi;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String idDemandeRente;
    private String idTiers;
    private boolean isCopie;
    private String numAgence;
    private String numCaisse;
    private BSession session;
    private PRTiersWrapper tierAdresse;

    public REChangementCaisseOO() {
        super();

        caisseHelper = null;
        codeIsoLangue = "";
        data = null;
        dateDocument = "";
        DateEnvoi = "";
        document = null;
        documentData = null;
        documentHelper = null;
        idDemandeRente = "";
        idTiers = "";
        isCopie = false;
        numAgence = "";
        numCaisse = "";
        session = null;
        tierAdresse = null;
    }

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte

        setTierAdresse(PRTiersHelper.getTiersAdresseParId(getSession(), getIdTiers()));

        codeIsoLangue = getSession().getCode(getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        // creation du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_TRANSFERT_VALIDE);
        documentHelper.setNom("openOfficeChCaisse");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }
    }

    private void chargementDonneesLettre() throws Exception {
        try {

            remplirChampsStatiques();

            // Ajoute dans l'entête de la lettre qui a traité le dossier si nécessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                data.addData("TRAITE_PAR", document.getTextes(6).getTexte(1).getDescription() + " "
                        + getSession().getUserFullName() + "\n" + getSession().getUserInfo().getPhone());
            }

            // Lieu et date
            String LieuDate = PRStringUtils.replaceString(document.getTextes(1).getTexte(4).getDescription(),
                    REChangementCaisseOO.CDT_DATEENVOI, (JACalendar.format(getDateEnvoi(), codeIsoLangue)));
            data.addData("LieuDate", LieuDate);

            String code = null;
            if (JadeStringUtil.isIntegerEmpty(getNumAgence())) {
                code = getNumCaisse();
            } else {
                code = getNumCaisse() + "." + getNumAgence();
            }

            // adresse de la nouvelle caisse
            PRTiersWrapper[] caisseComp = PRTiersHelper.getCaisseCompensationForCode(getSession(), code);
            // Si retourne null, exception
            if (caisseComp == null) {
                System.out.println("Pas de caisse de compensation trouvée avec le No : " + code);
                throw new Exception();
            }

            if (isCopie()) {
                data.addData("TEXTE_COPIE", document.getTextes(1).getTexte(3).getDescription());
            } else {
                data.addData("TEXTE_COPIE", null);
            }

            // Nuermo NSS de l'assure
            data.addData("valeurNSS", getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            // Numero de la caisse
            data.addData("numeroNouvelleCaisse", code);

            // Adresse caisse formatée

            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            String AdresseCaisseCompFormate = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    caisseComp[0].getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                    JACalendar.todayJJsMMsAAAA());

            data.addData("adresseNouvelleCaisse", AdresseCaisseCompFormate);

            setDocumentData(data);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
        }
    }

    private void chargementSalutationLettre() throws Exception {
        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                codeIsoLangue);
        caisseHelper.setTemplateName(REChangementCaisseOO.FICHIER_MODELE_ENTETE_CORVUS);

        data = caisseHelper.addHeaderParameters(data, crBean, new Boolean(isCopie()));
        data = caisseHelper.addSignatureParameters(data, crBean);

        setDocumentData(data);
    }

    public void generationLettre() throws Exception {

        // catalogue texte
        chargementCatalogueTexte();

        // remplir les données
        data = new DocumentData();
        data.addData("idProcess", "REChangementCaisseOO");

        chargementDonneesLettre();
        chargementSalutationLettre();
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDateEnvoi() {
        return DateEnvoi;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNumAgence() {
        return numAgence;
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    public BSession getSession() {
        return session;
    }

    public PRTiersWrapper getTierAdresse() {
        return tierAdresse;
    }

    public boolean isCopie() {
        return isCopie;
    }

    private void remplirChampsStatiques() throws Exception {
        data.addData("ADRESSE_CAISSE", document.getTextes(1).getTexte(5).getDescription());
        data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(2).getDescription());
        data.addData("LETTRE_INFORMATION1", document.getTextes(2).getTexte(1).getDescription());
        data.addData("TEXTE_ASSURE", document.getTextes(3).getTexte(1).getDescription());
        data.addData("TEXTE_INDICATION", document.getTextes(3).getTexte(2).getDescription());
        data.addData("LETTRE_INFORMATION2", document.getTextes(2).getTexte(2).getDescription());
        data.addData("TEXTE_NUMERO", document.getTextes(3).getTexte(3).getDescription());
        data.addData("TEXTE_SIGLE", document.getTextes(3).getTexte(4).getDescription());
        data.addData("TEXTE_COPIES", document.getTextes(4).getTexte(1).getDescription());
        data.addData("POINTS", document.getTextes(4).getTexte(4).getDescription());
        data.addData("COPIE_ANCIENNE_CAISSE", document.getTextes(4).getTexte(2).getDescription());
        data.addData("COPIE_NOUVELLE_CAISSE", document.getTextes(4).getTexte(3).getDescription());
        data.addData("BAS_PAGE_1", document.getTextes(5).getTexte(1).getDescription());
        data.addData("BAS_PAGE_2", document.getTextes(5).getTexte(2).getDescription());
        data.addData("BAS_PAGE_3", document.getTextes(5).getTexte(3).getDescription());
    }

    public void setCopie(boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDateEnvoi(String dateEnvoi) {
        DateEnvoi = dateEnvoi;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumAgence(String numAgence) {
        this.numAgence = numAgence;
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }
}