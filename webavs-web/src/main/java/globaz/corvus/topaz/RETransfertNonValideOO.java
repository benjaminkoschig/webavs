package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater06;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Création du document de transfert à la caisse compétente avec la solution Topaz
 * 
 * @author PCA
 */

public class RETransfertNonValideOO {

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_TRANSFERT";

    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue;
    private DocumentData data;
    private String dateDocument;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String idAgenceCommu;
    private String idDemandeRente;
    private String idTiers;
    private PRInfoCompl info;
    private boolean isCopie;
    private boolean isCopieAgenceAI;
    private boolean isCopieAgenceCommu;
    private boolean isHomme;
    private TIAdministrationViewBean officeAi;
    private String remarque;
    private BSession session;
    private PRTiersWrapper tierAdresse;

    public RETransfertNonValideOO() {
        super();

        caisseHelper = null;
        codeIsoLangue = "";
        data = null;
        dateDocument = "";
        document = null;
        documentData = null;
        documentHelper = null;
        idAgenceCommu = "";
        idDemandeRente = "";
        idTiers = "";
        info = null;
        isCopie = false;
        isCopieAgenceAI = false;
        isCopieAgenceCommu = false;
        isHomme = false;
        officeAi = null;
        remarque = "";
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
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_TRANSFERT);
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

    private void chargementDonneesLettre() throws Exception {

        // Chargement des données statiques de la lettre
        remplirChampsStatiques();

        // Connaitre le sexe du tiers afin de changer l'orthographe, ex assuré
        // ou assurée
        PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
        if (PRACORConst.CS_HOMME.equals(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
            setHomme(true);
        }

        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        // chargement de la ligne d'adresse avec le formater
        String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater06(), "");

        data.addData("adresseTiers", adresse);

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(getIdDemandeRente());
        demandeRente.retrieve();

        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())) {
            data.addData("LETTRE_POINT", document.getTextes(2).getTexte(3).getDescription());
            data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(1).getDescription());
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
            data.addData("LETTRE_POINT", document.getTextes(2).getTexte(4).getDescription());
            data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(3).getDescription());
        } else if ((IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente()))) {
            data.addData("LETTRE_POINT", document.getTextes(2).getTexte(5).getDescription());
            data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(2).getDescription());
        } else if ((IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API).equals(demandeRente.getCsTypeDemandeRente())) {
            data.addData("LETTRE_POINT", document.getTextes(2).getTexte(5).getDescription());
            data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(4).getDescription());
        }

        if (JadeStringUtil.isEmpty(getRemarque())) {
            data.addData("LETTRE_TEXTE_FIN", null);
        } else {
            data.addData("LETTRE_TEXTE_FIN", getRemarque());
        }

        String copie = "";
        if (isHomme()) {
            copie = document.getTextes(5).getTexte(4).getDescription() + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "\n";
        } else {
            copie = document.getTextes(5).getTexte(3).getDescription() + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "\n";
        }

        if (isCopieAgenceCommu()) {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            String agenceCom = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdAgenceCommu(),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                    JACalendar.todayJJsMMsAAAA());

            copie = copie + agenceCom;

            if (isCopieAgenceAI()) {
                copie = copie + "\n" + officeAi.getDesignation1();
            }
        } else if (isCopieAgenceAI()) {
            copie = copie + officeAi.getDesignation1();
        }

        if (isCopieAgenceCommu() || isCopieAgenceAI()) {
            data.addData("TEXTE_COPIES", document.getTextes(4).getTexte(2).getDescription());
        } else {
            data.addData("TEXTE_COPIES", document.getTextes(4).getTexte(3).getDescription());
        }

        data.addData("valeurCopies", copie);

        setDocumentData(data);
    }

    private void chargementEnTeteEtSalutationLettre() throws Exception {
        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            String adresse = "";

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            demandeRente.retrieve();

            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getInfo().getIdTiersCaisse(),
                    "519006", "", "", null, "");

            crBean.setAdresse(adresse);

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document

            if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Ajoute dans l'entête de la lettre qui a traité le dossier si
            // necessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                crBean.setNomCollaborateur(document.getTextes(6).getTexte(1).getDescription() + " "
                        + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            // Ajoute la date dans l'entête de la lettre sauf pour les documents IJ

            crBean.setDate(JACalendar.format(getInfo().getDateInfoCompl(), codeIsoLangue));

            // Ajout du numero NSS dans l'entête de la lettre

            crBean.setNoAvs(getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RETransfertNonValideOO.FICHIER_MODELE_ENTETE_CORVUS);

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
        data.addData("idProcess", "RETransfertOO");

        chargementDonneesLettre();
        chargementEnTeteEtSalutationLettre();

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

    public TIAdministrationViewBean getOfficeAi() {
        return officeAi;
    }

    public String getRemarque() {
        return remarque;
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

    public boolean isCopieAgenceAI() {
        return isCopieAgenceAI;
    }

    public boolean isCopieAgenceCommu() {
        return isCopieAgenceCommu;
    }

    private boolean isHomme() {
        return isHomme;
    }

    private void remplirChampsStatiques() throws Exception {
        data.addData("titreTiers", document.getTextes(2).getTexte(1).getDescription());
        data.addData("LETTRE_TEXTE_DEBUT", document.getTextes(2).getTexte(2).getDescription());
        data.addData("LETTRE_SALUTATIONS", document.getTextes(3).getTexte(1).getDescription());
        data.addData("TEXTE_ANNEXES", document.getTextes(4).getTexte(1).getDescription());
        data.addData("VALEUR_ANNEXES", document.getTextes(5).getTexte(1).getDescription());
        data.addData("POINTS", document.getTextes(5).getTexte(5).getDescription());
    }

    public void setCopieAgenceAI(boolean isCopieAgenceAI) {
        this.isCopieAgenceAI = isCopieAgenceAI;
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

    public void setOfficeAi(TIAdministrationViewBean officeAi) {
        this.officeAi = officeAi;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }
}
