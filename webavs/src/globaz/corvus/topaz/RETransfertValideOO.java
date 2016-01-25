package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamille;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.webavs.common.CommonProperties;
import java.util.LinkedList;
import java.util.List;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Création du document de transfert à de dossier avec la solution Topaz
 * 
 * @author PCA
 */
public class RETransfertValideOO {

    private static final String CDT_ADRESSECAISSE = "{adresseCaisse}";
    private static final String CDT_DATEENVOI = "{dateEnvoi}";
    private static final String CDT_MOISDERNIERVERSEMENT = "{moisDernierVersement}";
    private static final String CDT_MOTIFTRANSMISSION = "{texteMotif}";
    private static final String CDT_NUMEROAVSASSURE = "{numeroAssure}";
    private static final String CDT_NUMEROCAISSE = "{numeroCaisse}";
    private static final String CDT_PROCHAINMOIS = "{dateProchainMois}";
    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_TRANSFERT";

    private ICaisseReportHelperOO caisseHelper;
    private String CessationPaiement;
    private String codeIsoLangue;
    private DocumentData data;
    private String dateDocument;
    private String dateEnvoi;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String idAgenceCommu;
    private String idDemandeRente;
    private String idTiers;
    private boolean isCopie;
    private boolean isCopieAgenceAI;
    private boolean isCopieAgenceCommu;
    private boolean isHomme;
    private boolean isRaUnique;
    private List<RERenteAccordeeFamille> list;
    private String motifTransmission;
    private String numAgence;
    private String numCaisse;
    private TIAdministrationViewBean officeAi;
    private String remarque;
    private BSession session;
    private PRTiersWrapper tierAdresse;

    public RETransfertValideOO() {
        super();

        caisseHelper = null;
        CessationPaiement = "";
        codeIsoLangue = "";
        data = null;
        dateDocument = "";
        dateEnvoi = "";
        document = null;
        documentData = null;
        documentHelper = null;
        idAgenceCommu = "";
        idDemandeRente = "";
        idTiers = "";
        isCopie = false;
        isCopieAgenceAI = false;
        isCopieAgenceCommu = false;
        isHomme = false;
        isRaUnique = false;
        list = null;
        motifTransmission = "";
        numAgence = "";
        numCaisse = "";
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
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_TRANSFERT_VALIDE);
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
        try {

            remplirChampsStatiques();

            // Ajoute dans l'entête de la lettre qui a traité le dossier si
            // necessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                data.addData("TRAITE_PAR", document.getTextes(9).getTexte(1).getDescription() + " "
                        + getSession().getUserFullName() + "\n" + getSession().getUserInfo().getPhone());
            }

            // Connaitre le sex du tier afin de changer l'orthographe, ex assuré
            // ou assurée
            PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
            if (PRACORConst.CS_HOMME.equals(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                setHomme(true);
            }

            // Affichage ou non du mot copie en haut du document
            if (isCopie()) {
                data.addData("TITRE_COPIE", document.getTextes(1).getTexte(2).getDescription());
            } else {
                data.addData("TITRE_COPIE", null);
            }

            // Lieu et de la date
            String LieuDate = PRStringUtils.replaceString(document.getTextes(1).getTexte(4).getDescription(),
                    RETransfertValideOO.CDT_DATEENVOI, (JACalendar.format(getDateEnvoi(), codeIsoLangue)));
            data.addData("LieuDate", LieuDate);

            // Adresse de l'ancienne caisse (caisse qui envoi le document)
            TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
            tiAdminCaisseMgr.setSession(session);
            tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                    getSession().getApplication()));
            tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdminCaisseMgr.find();

            TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

            String idTiersCaisse = "";
            if (tiAdminCaisse != null) {
                idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
            }

            String adresse = "";
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), idTiersCaisse,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

            data.addData("adresseAncienneCaisse", adresse);

            // Remplacement de la variable {NumeroCaisse} par le numero d'une
            // caisse
            String code = getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
            String numCaisse = PRStringUtils.replaceString(document.getTextes(2).getTexte(2).getDescription(),
                    RETransfertValideOO.CDT_NUMEROCAISSE, code);
            data.addData("TITRE_NUM_ANCIENNE_CAISSE", numCaisse);

            // Recuperation du numéro de la nouvelle caisse
            code = null;
            if (JadeStringUtil.isIntegerEmpty(getNumAgence())) {
                code = getNumCaisse();
            } else {
                code = getNumCaisse() + "." + getNumAgence();
            }

            PRTiersWrapper[] caisseComp = PRTiersHelper.getCaisseCompensationForCode(getSession(), code);

            // Test pour savoir si à partir du numéro, j'ai obtenu une caisse
            if (caisseComp == null) {
                throw new Exception(getSession().getLabel("ERREUR_NUMERO_CAISSE") + " " + code);
            }
            // adresse de la nouvelle caisse
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    caisseComp[0].getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

            data.addData("adresseNouvelleCaisse", adresse);

            // Remplacement de la variable {NumeroCaisse} par le numero d'une
            // caisse
            numCaisse = PRStringUtils.replaceString(document.getTextes(2).getTexte(2).getDescription(),
                    RETransfertValideOO.CDT_NUMEROCAISSE, code);
            data.addData("TITRE_NUM_NOUVELLE_CAISSE", numCaisse);

            // Information sur l'assuré
            if (isHomme) {
                data.addData("TITRE_ASSURE", document.getTextes(2).getTexte(4).getDescription());
            } else {
                data.addData("TITRE_ASSURE", document.getTextes(2).getTexte(6).getDescription());
            }
            // Adresse de l'assuré
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            data.addData("adresseAssure", PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdTiers(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, ""));

            // Numéro AVS de l'assuré
            String NumAVS = PRStringUtils.replaceString(document.getTextes(2).getTexte(5).getDescription(),
                    RETransfertValideOO.CDT_NUMEROAVSASSURE,
                    getTierAdresse().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            // traiter le remplacement de la variable {NumeroAssure} par le
            // numero de l'assuré(e)
            data.addData("TITRE_NUM_ASSURE", NumAVS);

            if (isHomme) {
                data.addData("LETTRE_TEXTE", document.getTextes(3).getTexte(5).getDescription());
            } else {
                data.addData("LETTRE_TEXTE", document.getTextes(3).getTexte(6).getDescription());
            }

            // traiter le remplacement de la variable {TexteMotif} par le motif
            // insérer par l'utilisateur dans la jsp
            String motif = PRStringUtils.replaceString(document.getTextes(3).getTexte(3).getDescription(),
                    RETransfertValideOO.CDT_MOTIFTRANSMISSION, getMotifTransmission());
            data.addData("LETTRE_MOTIF", motif);

            // traiter le remplacement de la variable {MoisDernierVersement} par
            // la date
            JADate d = new JADate(getCessationPaiement());
            String texte = PRStringUtils.replaceString(document.getTextes(4).getTexte(5).getDescription(),
                    RETransfertValideOO.CDT_MOISDERNIERVERSEMENT, PRDateFormater.format_MMMYYYY(d, codeIsoLangue));
            data.addData("TEXTE_RENTE_ENCOURS", texte);

            LinkedList<String> chiffreMontant = new LinkedList<String>();

            // création d'un tableau pour afficher les valeurs des prestations
            // sur plusieurs lignes
            Collection newTable = new Collection("Tableau5");

            for (RERenteAccordeeFamille renteac : getList()) {
                DataList line1 = new DataList("lignePrestMontant");
                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), renteac.getIdTiersBeneficiaire());
                line1.addData("numeroPrestation",
                        renteac.getCodePrestation() + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                line1.addData("montantLigne", FormatMontant(renteac.getMontantPrestation()));
                line1.addData("CHF", document.getTextes(5).getTexte(6).getDescription());
                newTable.add(line1);
                chiffreMontant.add(renteac.getMontantPrestation());
            }

            // Calcul du montant total
            FWCurrency montant = new FWCurrency("0.00");
            while (!chiffreMontant.isEmpty()) {
                montant.add(new FWCurrency(chiffreMontant.getFirst()));
                chiffreMontant.removeFirst();
            }

            // Insertion du montant total
            data.addData("montantTotal", montant.toStringFormat());
            data.addData("CHF", document.getTextes(5).getTexte(6).getDescription());
            data.add(newTable);

            // traitement de la remarque
            if (getRemarque().toString().length() > 1) {
                // insertion de la remarque dans le document
                data.addData("TEXTE_REMARQUE", getRemarque());
            } else {
                data.addData("TEXTE_REMARQUE", null);
            }

            // La variable {DateProchainMois} doit être remplacer par le mois +
            // 1
            JACalendarGregorian ja = new JACalendarGregorian();

            String texteCopie = "";
            d = ja.addMonths(d, 1);

            // BZ 4950, changement des textes des copies en fonction du sexe et du nombre de prestations
            if (isHomme) {
                if (isRaUnique()) {
                    texteCopie = PRStringUtils.replaceString(document.getTextes(7).getTexte(4).getDescription(),
                            RETransfertValideOO.CDT_PROCHAINMOIS, PRDateFormater.format_MMMYYYY(d, codeIsoLangue));
                } else {
                    texteCopie = PRStringUtils.replaceString(document.getTextes(7).getTexte(7).getDescription(),
                            RETransfertValideOO.CDT_PROCHAINMOIS, PRDateFormater.format_MMMYYYY(d, codeIsoLangue));
                }
            } else {
                if (isRaUnique()) {
                    texteCopie = PRStringUtils.replaceString(document.getTextes(7).getTexte(5).getDescription(),
                            RETransfertValideOO.CDT_PROCHAINMOIS, PRDateFormater.format_MMMYYYY(d, codeIsoLangue));
                } else {
                    texteCopie = PRStringUtils.replaceString(document.getTextes(7).getTexte(8).getDescription(),
                            RETransfertValideOO.CDT_PROCHAINMOIS, PRDateFormater.format_MMMYYYY(d, codeIsoLangue));
                }
            }

            // Adresse de la nouvelle caisse formatée
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            String AdresseCaisseCompFormate = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    caisseComp[0].getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                    JACalendar.todayJJsMMsAAAA());

            texteCopie = PRStringUtils.replaceString(texteCopie, RETransfertValideOO.CDT_ADRESSECAISSE,
                    AdresseCaisseCompFormate) + "\n";

            // Adresse de l'agence communale
            if (isCopieAgenceCommu()) {
                // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                // se trouvant dans le fichier corvus.properties
                String agenceCom = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdAgenceCommu(),
                        REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                        JACalendar.todayJJsMMsAAAA());

                texteCopie = texteCopie + agenceCom;

                if (isCopieAgenceAI) {
                    texteCopie = texteCopie + "\n" + officeAi.getDesignation1();
                }
            } else if (isCopieAgenceAI) {
                texteCopie = texteCopie + officeAi.getDesignation1();
            }

            data.addData("valeurCopies", texteCopie);

            if (isCopieAgenceCommu()) {
                data.addData("TEXTE_COPIES", document.getTextes(7).getTexte(1).getDescription());
            } else {
                data.addData("TEXTE_COPIES", document.getTextes(1).getTexte(2).getDescription());
            }

            setDocumentData(data);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
        }
    }

    private void ChargementSalutationLettre() throws Exception {
        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RETransfertValideOO.FICHIER_MODELE_ENTETE_CORVUS);

            data = caisseHelper.addSignatureParameters(data, crBean);

            setDocumentData(data);

        } catch (Exception e) {
            throw new Exception(getSession().getLabel("ERREUR_ENTETE_SIGNATURE"));

        }

    }

    private String FormatMontant(String montant) {
        FWCurrency montantMensuel = new FWCurrency("0.00");
        montantMensuel.add(new FWCurrency(null));
        montantMensuel.add(new FWCurrency(montant));
        return montantMensuel.toStringFormat();
    }

    public void generationLettre() throws Exception {

        // catalogue texte
        chargementCatalogueTexte();

        // remplir les données

        data = new DocumentData();

        data.addData("idProcess", "RETransfertValideOO");

        ChargementDonneesLettre();

        ChargementSalutationLettre();
    }

    public String getCessationPaiement() {
        return CessationPaiement;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
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

    public List<RERenteAccordeeFamille> getList() {
        return list;
    }

    public String getMotifTransmission() {
        return motifTransmission;
    }

    public String getNumAgence() {
        return numAgence;
    }

    public String getNumCaisse() {
        return numCaisse;
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

    public boolean isHomme() {
        return isHomme;
    }

    public boolean isRaUnique() {
        return isRaUnique;
    }

    private void remplirChampsStatiques() throws Exception {

        data.addData("TITRE_DOCUMENT", document.getTextes(1).getTexte(1).getDescription());
        data.addData("TITRE_ANCIENNE_CAISSE", document.getTextes(2).getTexte(1).getDescription());
        data.addData("TITRE_NOUVELLE_CAISSE", document.getTextes(2).getTexte(3).getDescription());
        data.addData("LETTRE_CONCERNE", document.getTextes(3).getTexte(4).getDescription());
        data.addData("TEXTE_PRESTATION", document.getTextes(5).getTexte(1).getDescription());
        data.addData("TEXTE_MONTANT_MENSUEL", document.getTextes(5).getTexte(3).getDescription());
        data.addData("TEXTE_SOMME_TOTAL", document.getTextes(5).getTexte(5).getDescription());
        data.addData("TEXTE_ANNEXES", document.getTextes(7).getTexte(2).getDescription());
        data.addData("VALEUR_ANNEXES", document.getTextes(7).getTexte(3).getDescription());
        data.addData("POINTS", document.getTextes(7).getTexte(6).getDescription());
    }

    public void setCessationPaiement(String cessationPaiement) {
        CessationPaiement = cessationPaiement;
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

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setHomme(boolean isHomme) {
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

    public void setIsCopie(boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setList(List<RERenteAccordeeFamille> listeRenteAccordee) {
        list = listeRenteAccordee;
    }

    public void setMotifTransmission(String motifTransmission) {
        this.motifTransmission = motifTransmission;
    }

    public void setNumAgence(String numAgence) {
        this.numAgence = numAgence;
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    public void setOfficeAi(TIAdministrationViewBean officeAi) {
        this.officeAi = officeAi;
    }

    public void setRaUnique(boolean isRaUnique) {
        this.isRaUnique = isRaUnique;
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
