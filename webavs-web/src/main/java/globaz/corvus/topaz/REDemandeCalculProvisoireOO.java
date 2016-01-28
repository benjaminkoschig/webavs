package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.vb.process.REGenererTransfertDossierViewBean;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater06;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REDemandeCalculProvisoireOO {

    private static final String CDT_NOMTIERS = "{NomtTiers}";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_TRANSFERT";

    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue = "";
    private DocumentData data;
    private String dateDocument = "";
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String idAgenceCommu = "";
    private String idDemandeRente = "";
    private String idTiers = "";
    private PRInfoCompl info = null;
    private boolean isCopie = false;
    private boolean isCopieAgenceCommu = false;
    private boolean isHomme = false;
    private String motif = "";
    private String nomAssure = "";
    private BSession session;
    private PRTiersWrapper tierAdresse;

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte
        setTierAdresse(PRTiersHelper.getTiersAdresseParId(getSession(), getIdTiers()));

        codeIsoLangue = getSession().getCode(getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        // creation du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_DEMANDE_CALCUL_PROV);
        documentHelper.setNom("openOffice");
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

    private void ChargementDonneesLettre() throws Exception {

        remplirChampsStatiques();

        // Connaître le sexe du tier afin de changer l'orthographe, ex assuré ou assurée
        PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
        if (PRACORConst.CS_HOMME.equals(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
            setHomme(true);
        }

        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        // chargement de la ligne d'adresse avec le formater
        String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater06(),
                JACalendar.todayJJsMMsAAAA());

        data.addData("adresseTiers", adresse);

        if (getMotif().equals(REGenererTransfertDossierViewBean.MOTIF_RENTE_AVS_AI)) {

            String paraAvecNomTiers = PRStringUtils.replaceString(document.getTextes(3).getTexte(1).getDescription(),
                    REDemandeCalculProvisoireOO.CDT_NOMTIERS, getNomAssure());
            data.addData("LETTRE_MOTIF", paraAvecNomTiers);

        } else if (getMotif().equals(REGenererTransfertDossierViewBean.MOTIF_PERCEPTION_DERNIERES_COTI)) {

            if (isHomme()) {
                data.addData("LETTRE_MOTIF", document.getTextes(3).getTexte(3).getDescription());
            } else {
                data.addData("LETTRE_MOTIF", document.getTextes(3).getTexte(4).getDescription());
            }
        } else if (REGenererTransfertDossierViewBean.MOTIF_DOMICILE_ETRANGER.equals(getMotif())) {
            if (isHomme()) {
                String paraAvecNomTiers = PRStringUtils.replaceString(document.getTextes(3).getTexte(5)
                        .getDescription(), REDemandeCalculProvisoireOO.CDT_NOMTIERS,
                        getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM));
                data.addData("LETTRE_MOTIF", paraAvecNomTiers);
            } else {
                String paraAvecNomTiers = PRStringUtils.replaceString(document.getTextes(3).getTexte(6)
                        .getDescription(), REDemandeCalculProvisoireOO.CDT_NOMTIERS,
                        getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM));
                data.addData("LETTRE_MOTIF", paraAvecNomTiers);
            }
        }

        String copie = "";
        if (isHomme()) {
            copie = document.getTextes(6).getTexte(2).getDescription() + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "\n";
        } else {
            copie = document.getTextes(6).getTexte(3).getDescription() + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "\n";
        }

        if (isCopieAgenceCommu()) {
            // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            String agenceCom = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdAgenceCommu(),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                    JACalendar.todayJJsMMsAAAA());

            copie = copie + agenceCom;
        }

        if (isCopieAgenceCommu()) {
            data.addData("TEXTE_COPIES", document.getTextes(5).getTexte(2).getDescription());
        } else {
            data.addData("TEXTE_COPIES", document.getTextes(5).getTexte(4).getDescription());
        }

        data.addData("valeurCopies", copie);

        setDocumentData(data);
    }

    private void ChargementEnTeteEtSalutationLettre() throws Exception {

        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            demandeRente.retrieve();

            // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getInfo().getIdTiersCaisse(),
                    "519006", "", "", null, "");

            crBean.setAdresse(adresse);

            crBean.setDate(JACalendar.format(getInfo().getDateInfoCompl(), codeIsoLangue));

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Ajoute dans l'entête de la lettre qui a traité le dossier si nécessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                crBean.setNomCollaborateur(document.getTextes(7).getTexte(1).getDescription() + " "
                        + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            // Ajout du numero NSS dans l'entête de la lettre
            crBean.setNoAvs(getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(REDemandeCalculProvisoireOO.FICHIER_MODELE_ENTETE_CORVUS);

            data.addData("idEntete", "CAISSE");

            data = caisseHelper.addHeaderParameters(data, crBean, new Boolean(isCopie()));

            data = caisseHelper.addSignatureParameters(data, crBean);

            setDocumentData(data);

        } catch (Exception e) {
            throw new Exception(getSession().getLabel("ERREUR_ENTETE_SIGNATURE"));

        }

    }

    public void generationLettre() throws Exception {

        // catalogue texte
        chargementCatalogueTexte();

        // remplir les données
        data = new DocumentData();

        data.addData("idProcess", "REDemandeCalculProvisoireOO");

        ChargementDonneesLettre();

        ChargementEnTeteEtSalutationLettre();

    }

    public String getDateDocument() {
        return dateDocument;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getIdAgenceCommu() {
        return idAgenceCommu;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public PRInfoCompl getInfo() {
        return info;
    }

    public String getMotif() {
        return motif;
    }

    public String getNomAssure() {
        return nomAssure;
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

    public boolean isCopieAgenceCommu() {
        return isCopieAgenceCommu;
    }

    private boolean isHomme() {
        return isHomme;
    }

    private void remplirChampsStatiques() throws Exception {

        data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(1).getDescription());
        data.addData("titreTiers", document.getTextes(2).getTexte(1).getDescription());
        data.addData("LETTRE_TEXTE_DEBUT", document.getTextes(2).getTexte(2).getDescription());
        data.addData("LETTRE_DEMANDE", document.getTextes(2).getTexte(3).getDescription());
        data.addData("LETTRE_SALUTATIONS", document.getTextes(4).getTexte(1).getDescription());
        data.addData("TEXTE_ANNEXES", document.getTextes(5).getTexte(1).getDescription());
        data.addData("VALEUR_ANNEXES", document.getTextes(6).getTexte(1).getDescription());
        data.addData("POINTS", document.getTextes(5).getTexte(3).getDescription());
    }

    public void setCopieAgenceCommu(boolean isCopieAgenceCommu) {
        this.isCopieAgenceCommu = isCopieAgenceCommu;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    private void setHomme(boolean isHomme) {
        this.isHomme = isHomme;
    }

    public void setIdAgenceCommu(String idAgenceCommu) {
        this.idAgenceCommu = idAgenceCommu;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setInfo(PRInfoCompl info) {
        this.info = info;
    }

    public void setIsCopie(boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }
}