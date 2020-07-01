package globaz.cygnus.topaz.recapitulationImportationTmr;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.cygnus.RFCodeTraitementDemandeTmrCleEnum;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFCodesIsoLangue;
import globaz.cygnus.api.recapTmr.IRFRecapImportTmr;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

public class RFGenererRecapImportationTmrServiceOO {

    private static final Logger LOG = LoggerFactory.getLogger(RFGenererRecapImportationTmrServiceOO.class);

    private static final String CS_DOCUMENT_RECAPITULATION_TMR = IRFCatalogueTexte.CS_LISTE_RECAPITULATIVE_TMR;
    public static final String FICHIER_MODELE_SIGNATURE_CYGNUS = "RF_LISTE_RECAPITULATIVE_TMR";
    protected ICaisseReportHelperOO caisseHelper;
    private Hashtable catalogueTexteMultiLangues = new Hashtable();
    private DocumentData data = null;
    private String dateDeTraitement = "";
    private boolean demandesEnErreur = false;
    private JadePublishDocumentInfo docInfo = null;
    protected ICTDocument[] document;
    protected ICTDocument documentHelper;
    private ICTScalableDocumentProperties documentProperties;
    private String email = "";
    private ICTDocument mainDocument;
    BigDecimal montantTotalDemandesRejetees = new BigDecimal(0);
    private String numAf = "";
    private String referenceCms = "";
    private Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap = new HashMap<String, ArrayList<RFImportDemandesCmsData>>();
    private BSession session;
    // private JadeProcessStep step = null;
    private BTransaction transaction = null;

    /**
     * Methode pour charger le libellé de la page recapitulative, selon le code de traitement
     * 
     * @param key
     * @return
     */
    private String ajouterLibelleParCodeTraitement(String key) {
        String libelle = "";
        if (key.equals(RFCodeTraitementDemandeTmrCleEnum.NSS_PAS_TROUVE.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(10).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.REFUS_PC.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(11).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.REFUS_ASV.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(12).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.QD_DANS_HOME.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(13).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(14).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(15).getDescription();
        } else if (key.equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_TOTAL.getCode())) {
            libelle = mainDocument.getTextes(1).getTexte(16).getDescription();
        } else {
            libelle = mainDocument.getTextes(1).getTexte(17).getDescription();
        }

        return libelle;
    }

    private void ajouterSignature() {

        try {
            // Chargement de la signature
            data.addData("idSignature", "SIGNATURE_RFM_STANDART");

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    getCodeIsoLangueSession());
            caisseHelper.setTemplateName(RFGenererRecapImportationTmrServiceOO.FICHIER_MODELE_SIGNATURE_CYGNUS);

            data = caisseHelper.addSignatureParameters(data, crBean);

        } catch (Exception e) {
            throw new IllegalArgumentException(e + " : ajouterSignature - erreur dans le chargement de la signature");
        }
    }

    private void chargerCatalogueDeTextes() throws Exception {

        // Set info au documentHelper
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCsTypeDocument(RFGenererRecapImportationTmrServiceOO.CS_DOCUMENT_RECAPITULATION_TMR);

        document = documentHelper.load();

        String codeIsoLangue = getCodeIsoLangueSession();

        if ((document == null) || (document.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            if (codeIsoLangue.equals(IRFCodesIsoLangue.LANGUE_ISO_DE)) {
                catalogueTexteMultiLangues.put(IRFCodesIsoLangue.LANGUE_ISO_DE, document[0]);
            } else if (codeIsoLangue.equals(IRFCodesIsoLangue.LANGUE_ISO_IT)) {
                catalogueTexteMultiLangues.put(IRFCodesIsoLangue.LANGUE_ISO_IT, document[0]);
            } else {
                catalogueTexteMultiLangues.put(IRFCodesIsoLangue.LANGUE_ISO_FR, document[0]);
            }
        }
        if (codeIsoLangue.equals(IRFCodesIsoLangue.LANGUE_ISO_DE)) {
            mainDocument = (ICTDocument) catalogueTexteMultiLangues.get(IRFCodesIsoLangue.LANGUE_ISO_DE);
        } else if (codeIsoLangue.equals(IRFCodesIsoLangue.LANGUE_ISO_IT)) {
            mainDocument = (ICTDocument) catalogueTexteMultiLangues.get(IRFCodesIsoLangue.LANGUE_ISO_IT);
        } else {
            mainDocument = (ICTDocument) catalogueTexteMultiLangues.get(IRFCodesIsoLangue.LANGUE_ISO_FR);

        }
    }

    /**
     * Methode pour controler la présence de donnée dans la string passée en paramètre
     * 
     * @param donnee
     * @return String
     */
    private String checkData(String donnee) {
        if (JadeStringUtil.isEmpty(donnee)) {
            donnee = " - ";
        }
        return donnee;
    }

    /**
     * Methode pour créer la seconde page du détail des cas rejetés
     */
    private void creerPageListeDetailleeCasRejetes() throws Exception {

        // Insert de l'entête de la caisse
        data.addData("idEntete", "HEADER_CAISSE");

        // Insertion du CMS référence
        data.addData(IRFRecapImportTmr.ADRESSE_CMS, loadCmsReference(numAf));

        // Insert le titre du document
        data.addData(IRFRecapImportTmr.TITRE_DOCUMENT_DEMANDES_REJETEES_RECAP_TMR, mainDocument.getTextes(3)
                .getTexte(1).getDescription());

        // Tableau de récapitulation des demandes traitées.
        Collection tabCasRejetesRecapTmr = new Collection(IRFRecapImportTmr.TABLEAU_CAS_REJETES_RECAPITULATIF);

        // Insertion des titre du tableau
        data.addData(IRFRecapImportTmr.TITRE_NUMERO_LIGNE, mainDocument.getTextes(3).getTexte(8).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_NSS, mainDocument.getTextes(3).getTexte(2).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_NOM_PRENOM, mainDocument.getTextes(3).getTexte(3).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_PERIODE_TRAITEMENT_REJETE_RECAPITULATION, mainDocument.getTextes(3)
                .getTexte(4).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_MONTANT, mainDocument.getTextes(3).getTexte(5).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_MESSAGE_ERREUR, mainDocument.getTextes(3).getTexte(6).getDescription());
        // Code
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_CODE, mainDocument.getTextes(1).getTexte(7).getDescription());

        // Chargement des demandes rejetées
        loadDemandesRejetees(tabCasRejetesRecapTmr);

        // Insertion de la collection au data
        data.add(tabCasRejetesRecapTmr);

    }

    /**
     * Methode pour créer la seconde page du détail des cas traités
     */
    private void creerPageListeDetailleeCasTraites() throws Exception {

        // Insert de l'entête de la caisse
        data.addData("idEntete", "HEADER_CAISSE");

        // Insertion du CMS référence
        data.addData(IRFRecapImportTmr.ADRESSE_CMS, loadCmsReference(numAf));

        // Insert le titre du document
        data.addData(IRFRecapImportTmr.TITRE_DOCUMENT_DEMANDES_TRAITEES_RECAP_TMR, mainDocument.getTextes(2)
                .getTexte(1).getDescription());

        // Tableau de récapitulation des demandes traitées.
        Collection tabCasTraiteRecapTmr = new Collection(IRFRecapImportTmr.TABLEAU_CAS_TRAITES_RECAPITULATIF);

        // Insertion des titre du tableau
        data.addData(IRFRecapImportTmr.TITRE_NSS, mainDocument.getTextes(2).getTexte(2).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_NOM_PRENOM, mainDocument.getTextes(2).getTexte(3).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_ANNEE_QD, mainDocument.getTextes(2).getTexte(4).getDescription());
        data.addData(IRFRecapImportTmr.TITRE_MONTANT, mainDocument.getTextes(2).getTexte(5).getDescription());

        // ******************************************************************* // Chargement du détail des cas traités
        loadDemandesTraitees(tabCasTraiteRecapTmr);

        // Insertion de la collection au data
        data.add(tabCasTraiteRecapTmr);

    }

    /**
     * Methode pour créer la première page de recapitulation
     */
    private void creerPageRecapitulatif() throws Exception {

        // Insert de l'entête de la caisse
        data.addData("idEntete", "HEADER_CAISSE");

        // Insert le CMS référence
        data.addData(IRFRecapImportTmr.TITRE_CONCERNE_CMS_REFERENCE, mainDocument.getTextes(1).getTexte(1)
                .getDescription());
        data.addData(IRFRecapImportTmr.CMS_REFERENCE, loadCmsReference(numAf));

        // Insert l'adresse TMR
        data.addData(IRFRecapImportTmr.ADRESSE_TMR, loadAdresseTmr());

        // Insert le titre du document
        data.addData(IRFRecapImportTmr.TITRE_DOCUMENT_RECAP_TMR, mainDocument.getTextes(1).getTexte(3)
                .getDescription());

        // Insérer libellé des factures traitées
        data.addData(IRFRecapImportTmr.LIBELLE_TABLEAU_FACTURES_TRAITEES, mainDocument.getTextes(1).getTexte(4)
                .getDescription());

        // Tableau de récapitulation
        Collection tabRecapTmr = new Collection(IRFRecapImportTmr.TABLEAU_RECAPITULATIF);

        // Insertion des titres du tableau
        // Nombre de cas
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_NOMBRE_DE_CAS, mainDocument.getTextes(1).getTexte(5)
                .getDescription());
        // Libellé traitement
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_LIBELLE_TRAITEMENT, mainDocument.getTextes(1).getTexte(6)
                .getDescription());
        // Code
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_CODE, mainDocument.getTextes(1).getTexte(7).getDescription());
        // Montant en erreur
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_MONTANT_REFUSE, mainDocument.getTextes(1).getTexte(8)
                .getDescription());
        // Montant payé
        data.addData(IRFRecapImportTmr.TITRE_TABLEAU_MONTANT_PAYE, mainDocument.getTextes(1).getTexte(9)
                .getDescription());

        // ########################################################################
        // Chargement des données présentent dans la map
        loadDemandesParCodeTraitement(tabRecapTmr);

        // Insertion de la collection au data
        data.add(tabRecapTmr);

        // Insertion des textes après tableau
        data.addData(IRFRecapImportTmr.TEXTE_INFO_CORRECTION_RECAPITULATIF, mainDocument.getTextes(1).getTexte(19)
                .getDescription());
        data.addData(IRFRecapImportTmr.TEXTE_INFO_DEPASSEMENT_QD_RECAPITULATIF,
                mainDocument.getTextes(1).getTexte(20).getDescription());

        // Insertion de la signature
        ajouterSignature();
        // this.data.addData("idSignature", "SIGNATURE_RFM_STANDART");

        // Insertion de l'annexe en bas de page
        data.addData(IRFRecapImportTmr.TITRE_ANNEXE_RECAPITULATIF, mainDocument.getTextes(1).getTexte(21)
                .getDescription());
        data.addData(IRFRecapImportTmr.TEXTE_ANNEXE_CAS_PAYES_RECAPITULATIF, mainDocument.getTextes(1).getTexte(22)
                .getDescription());
        // Si la liste contient des demandes en erreurs, on affiche le texte "cas rejetés" dans l'annexe
        if (demandesEnErreur) {
            data.addData(IRFRecapImportTmr.TEXTE_ANNEXE_CAS_REJETES_RECAPITULATIF, mainDocument.getTextes(1)
                    .getTexte(23).getDescription());
        }
    }

    /**
     * Methode de création de l'ensemble de la récapitulation
     */
    private DocumentData creerRecapTmrDocumentOO() {
        try {

            data = new DocumentData();

            chargerCatalogueDeTextes();

            // Insert la date de traitement (date du jour) dans le document (CaisseHeaderReportBean)
            loadDateDeTraitement();

            // Création de la page principale (récapitulatif)
            data.addData("idProcess", "RFRecapitulationImportationTmrOO");
            creerPageRecapitulatif();

            // Création de la liste détaillée des cas traités
            data.addData("isTableauListeCasTraitesTmr", "STANDARD");
            creerPageListeDetailleeCasTraites();

            // Création de la liste détaillée des cas rejetés, si il y en a
            if (demandesEnErreur) {
                data.addData("isTableauListeCasRejetesTmr", "STANDARD");
                creerPageListeDetailleeCasRejetes();
            } else {
                data.addData("isTableauListeCasRejetesTmr", "NONE");
            }

        } catch (Exception e) {
            try {
                sendMailError(e.getMessage());
            } catch (Exception e1) {
                LOG.error(e1.getMessage(), e1);
            }
            throw new IllegalArgumentException(e
                    + " : RFGenererRecapImportationTmrServiceOO - creerRecapTmrDocumentOO");
        }
        return data;
    }

    /**
     * Methode pour construire le docInfo final
     * 
     * @return
     */
    private JadePublishDocumentInfo docInfoFinal() {
        JadePublishDocumentInfo docInfoFinal = new JadePublishDocumentInfo();

        docInfoFinal.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmail());
        docInfoFinal.setDocumentTitle("Recapitulation d'importation TMR");
        docInfoFinal.setDocumentSubject("Ci-joint la liste récapitulative de l'importation du fichier TMR");

        return docInfoFinal;
    }

    /**
     * Methode pour formatter la date AAMMJJ en jj.mm.aaaa
     */
    private String formatDateDemandeRejetee(String dateDebut, String dateFin) {

        String dateDebutPeriode;
        String dateFinPeriode;
        try {
            dateDebutPeriode = PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA(dateDebut);
            dateFinPeriode = PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA(dateFin);
        } catch (JAException e) {

            return "";
        }

        return dateDebutPeriode + " - " + dateFinPeriode;
    }

    /**
     * Methode pour formatter le numAVS en numNSS des demandes rejetées
     * 
     */
    private StringBuffer formatNss(RFImportDemandesCmsData demande) {

        StringBuffer nssBeneficiaireFormatte = new StringBuffer();

        if (!demande.getNssBeneficiaire().startsWith("756.")) {
            if (!JadeStringUtil.isBlankOrZero(demande.getNssBeneficiaire())) {
                nssBeneficiaireFormatte.append("756");
                nssBeneficiaireFormatte.append(".");
                nssBeneficiaireFormatte.append(demande.getNssBeneficiaire().substring(1, 5));
                nssBeneficiaireFormatte.append(".");
                nssBeneficiaireFormatte.append(demande.getNssBeneficiaire().substring(5, 9));
                nssBeneficiaireFormatte.append(".");
                nssBeneficiaireFormatte.append(demande.getNssBeneficiaire().substring(9, 11));
            }
            return nssBeneficiaireFormatte;
        } else {
            return new StringBuffer(demande.getNssBeneficiaire());
        }

    }

    private String getCodeIsoLangueSession() {

        String codeIsoLangue = "";

        if (getSession().getIdLangueISO().equals(IRFCodesIsoLangue.LANGUE_ISO_DE)) {
            codeIsoLangue = IRFCodesIsoLangue.LANGUE_ISO_DE;
        } else if (getSession().getIdLangueISO().equals(IRFCodesIsoLangue.LANGUE_ISO_IT)) {
            codeIsoLangue = IRFCodesIsoLangue.LANGUE_ISO_IT;
        } else {
            codeIsoLangue = IRFCodesIsoLangue.LANGUE_ISO_FR;
        }

        return codeIsoLangue;
    }

    public String getDescription() {
        return null;
    }

    public DocumentData getDocData() {
        return data;
    }

    public ICTDocument getDocumentHelper() {
        return documentHelper;
    }

    public ICTScalableDocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public String getEmail() {
        return email;
    }

    private String getMontantDemandeRejetee(RFImportDemandesCmsData demande) {

        BigDecimal montantDemande = new BigDecimal(0);
        BigDecimal montantPaye = new BigDecimal(0);

        if (!JadeStringUtil.isBlankOrZero(demande.getMontantDemande())) {
            montantDemande = new BigDecimal(demande.getMontantDemande());
        }
        if (!JadeStringUtil.isBlankOrZero(demande.getMontantPaye())) {
            montantPaye = new BigDecimal(demande.getMontantPaye());
        }

        // Si montantDemande > montantPaye, on retourne la différence
        if (montantDemande.compareTo(montantPaye) > 0) {
            montantTotalDemandesRejetees = montantTotalDemandesRejetees.add(new BigDecimal(montantDemande.subtract(
                    montantPaye).toString()));

            return new FWCurrency(montantDemande.subtract(montantPaye).toString()).toStringFormat();
        }
        // Sinon, on retourne le montant payé
        else {
            if (!JadeStringUtil.isBlankOrZero(demande.getMontantPaye())) {
                montantTotalDemandesRejetees = montantTotalDemandesRejetees
                        .add(new BigDecimal(demande.getMontantPaye()));
                return new FWCurrency(demande.getMontantPaye()).toStringFormat();
            } else {
                return "-";
            }
        }

    }

    public String getName() {
        return "null - RFGenererREcapImportationTmrServiceOO";
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    /**
     * Methode pour remonter l'adresse TMR du catalogue de texte
     * 
     * @return
     */
    private String loadAdresseTmr() {
        String adresseTmr = mainDocument.getTextes(1).getTexte(2).getDescription();
        return adresseTmr;
    }

    /**
     * Methode pour charger le descriptif de la caisse expéditrice du document
     * 
     * @return
     */
    private String loadCmsReference(String codeAdministration) throws Exception {

        if (JadeStringUtil.isBlank(referenceCms)) {
            try {

                // Récupération du genre d'administration dans les propriétés DB
                String csGenreAdministration = RFPropertiesUtils.getCsGenreAdminstrationCMS();

                // Recherche de l'administration par genre et par code
                PRTiersWrapper[] tiersW = PRTiersHelper.getAdministrationActiveForGenreAndCode(PRSession
                        .connectSession(BSessionUtil.getSessionFromThreadContext(),
                                RFApplication.DEFAULT_APPLICATION_CYGNUS), csGenreAdministration, codeAdministration);

                // Récupération du nom de l'administration
                if (tiersW != null) {
                    referenceCms = tiersW[0].getProperty(PRTiersWrapper.PROPERTY_NOM);
                } else {
                    referenceCms = "this CMS code " + codeAdministration + " is undifined  in administration";
                }

            } catch (Exception e) {
                throw new Exception(e);
            }
        }

        return referenceCms;
    }

    /**
     * Methode pour insérer la date du jour dans le document
     */
    private void loadDateDeTraitement() {
        try {
            // si la date n'a pas déjà été setté
            if (JadeStringUtil.isEmpty(dateDeTraitement)) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
                Date d = JadeDateUtil.getGlobazDate(JACalendar.todayJJsMMsAAAA());

                dateDeTraitement = df.format(d);

                CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

                caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                        getCodeIsoLangueSession());
                caisseHelper.setTemplateName(RFGenererRecapImportationTmrServiceOO.FICHIER_MODELE_SIGNATURE_CYGNUS);

                crBean.setDate(dateDeTraitement);

                data = caisseHelper.addHeaderParameters(data, crBean, false);
            }

        } catch (Exception e) {
            throw new NumberFormatException(e + " : RFGenererRecapImportationTmrServiceOO - loadDateDeTraitement");
        }
    }

    /**
     * Methode pour charger les demandes par codes de traitement
     * 
     * @param tabRecapTmr
     *
     */
    private void loadDemandesParCodeTraitement(Collection tabRecapTmr) {

        BigDecimal montantTotalPaye = new BigDecimal(0);
        BigDecimal montantTotalRefuse = new BigDecimal(0);
        int nbTotalDemandes = 0;

        for (Map.Entry<String, ArrayList<RFImportDemandesCmsData>> entry : regroupementDemandesParCodeTraitementMap
                .entrySet()) {

            BigDecimal montantPaye = new BigDecimal(0);
            BigDecimal montantRefuse = new BigDecimal(0);
            int nbDemandes = 0;
            String codeTraitement = "";

            for (RFImportDemandesCmsData demande : regroupementDemandesParCodeTraitementMap.get(entry.getKey())) {

                // Ajout des montants refusés
                if (!entry.getKey().equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_TOTAL.getCode())) {
                    if (!JadeStringUtil.isBlankOrZero(demande.getMontantDemande())) {
                        // Si paiement partiel on ajout le montant payé
                        if (demande.getCodeTraitement().equals(
                                RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())) {
                            montantPaye = montantPaye.add(new BigDecimal(demande.getMontantPaye()));
                            montantTotalPaye = montantTotalPaye.add(new BigDecimal(demande.getMontantPaye()));
                        }

                        if (demande.getCodeTraitement().equals(
                                RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())) {
                            montantRefuse = montantRefuse.add(new BigDecimal(demande.getMontantDemande())
                                    .subtract(new BigDecimal(demande.getMontantPaye())));
                            montantTotalRefuse = montantTotalRefuse.add(new BigDecimal(demande.getMontantDemande())
                                    .subtract(new BigDecimal(demande.getMontantPaye())));
                        } else {
                            montantRefuse = montantRefuse.add(new BigDecimal(demande.getMontantDemande()));
                            montantTotalRefuse = montantTotalRefuse.add(new BigDecimal(demande.getMontantDemande()));
                        }

                    }
                }
                // Sinon, ajout des montants payés
                else {
                    montantPaye = montantPaye.add(new BigDecimal(demande.getMontantDemande()));
                    montantTotalPaye = montantTotalPaye.add(new BigDecimal(demande.getMontantDemande()));
                }

                // Incrémentation du nombre de demande
                nbDemandes = nbDemandes + 1;

            }

            // Incrémentation du nombre total de demandes
            nbTotalDemandes = nbTotalDemandes + nbDemandes;

            // Ajout du code de traitement
            codeTraitement = entry.getKey().toString();

            // ###########################################################
            // création de la ligne du document
            DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_RECAPITULATIF);
            ligne.addData(IRFRecapImportTmr.NOMBRE_CAS_PAR_TYPE_RECAPITULATIF, String.valueOf(nbDemandes));

            if (entry.getKey().equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())) {
                ligne.addData(IRFRecapImportTmr.LIBELLE_RECAPITULATIF, ajouterLibelleParCodeTraitement(entry
                        .getKey().toString()) + "  #");
                ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_ACCEPTEE_RECAPITULATIF,
                        new FWCurrency(montantPaye.toString()).toStringFormat());
            } else {
                ligne.addData(IRFRecapImportTmr.LIBELLE_RECAPITULATIF, ajouterLibelleParCodeTraitement(entry
                        .getKey().toString()));
            }

            ligne.addData(IRFRecapImportTmr.CODE_PAR_TYPE_ERREUR_RECAPITULATIF, codeTraitement);
            if (!entry.getKey().equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_TOTAL.getCode())) {
                ligne.addData(IRFRecapImportTmr.MONTANT__REFUSE_PAR_TYPE_ERREUR_RECAPITULATIF, new FWCurrency(
                        montantRefuse.toString()).toStringFormat());
            } else {
                ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_ACCEPTEE_RECAPITULATIF,
                        new FWCurrency(montantPaye.toString()).toStringFormat());
            }

            // Insertion du dataList à la collection
            tabRecapTmr.add(ligne);

        }

        // #####################################################
        // Insertion de la ligne final du tableau (totaux)
        DataList ligneTotal = new DataList(IRFRecapImportTmr.LIGNE_TOTALE_RECAPITULATIF);
        ligneTotal.addData(IRFRecapImportTmr.NB_CAS_TOTAL_RECAPITULATIF, String.valueOf(nbTotalDemandes));
        ligneTotal.addData(IRFRecapImportTmr.TEXTE_TOTAL_RECAPITULATIF, mainDocument.getTextes(1).getTexte(18)
                .getDescription());
        ligneTotal.addData(IRFRecapImportTmr.MONTANT_TOTAL_REFUSE_RECAPITULATIF,
                new FWCurrency(montantTotalRefuse.toString()).toStringFormat());
        ligneTotal.addData(IRFRecapImportTmr.MONTANT_TOTAL_ACCEPTE_RECAPITULATIF,
                new FWCurrency(montantTotalPaye.toString()).toStringFormat());

        // Insertion du dataList à la collection
        tabRecapTmr.add(ligneTotal);

        // Si données en erreurs présente, passage à TRUE du boolean, pour afficher texte dans "annexe" en bas de page
        if (regroupementDemandesParCodeTraitementMap
                .containsKey(RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode())
                || regroupementDemandesParCodeTraitementMap
                        .containsKey(RFCodeTraitementDemandeTmrCleEnum.NSS_PAS_TROUVE.getCode())
                || regroupementDemandesParCodeTraitementMap
                        .containsKey(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())
                || regroupementDemandesParCodeTraitementMap
                        .containsKey(RFCodeTraitementDemandeTmrCleEnum.QD_DANS_HOME.getCode())
                || regroupementDemandesParCodeTraitementMap.containsKey(RFCodeTraitementDemandeTmrCleEnum.REFUS_ASV
                        .getCode())
                || regroupementDemandesParCodeTraitementMap.containsKey(RFCodeTraitementDemandeTmrCleEnum.REFUS_PC
                        .getCode())) {
            demandesEnErreur = true;
        }
    }

    /**
     * Methode pour charger les demandes rejetées
     * 
     * @param tabCasRejeteRecapTmr
     */
    private void loadDemandesRejetees(Collection tabCasRejeteRecapTmr) {

        for (Map.Entry<String, ArrayList<RFImportDemandesCmsData>> entry : regroupementDemandesParCodeTraitementMap
                .entrySet()) {
            for (RFImportDemandesCmsData demande : regroupementDemandesParCodeTraitementMap.get(entry.getKey())) {

                if (!demande.getCodeTraitement().equals(RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_TOTAL.getCode())) {

                    BigDecimal montantDemande = new BigDecimal(0);
                    BigDecimal montantPaye = new BigDecimal(0);

                    // Recupération des montants de la demande
                    if (!JadeStringUtil.isBlankOrZero(demande.getMontantDemande())) {
                        montantDemande = new BigDecimal(demande.getMontantDemande());
                    }
                    if (!JadeStringUtil.isBlankOrZero(demande.getMontantPaye())) {
                        montantPaye = new BigDecimal(demande.getMontantPaye());
                    }

                    // Traitement des demandes :
                    // - si le montant payé est à 0.-
                    // - si c'est un paiement partiel
                    // - si idDemande existe pas
                    if ((montantPaye.equals(0)) || ((montantDemande.compareTo(montantPaye)) == 1)
                            || (JadeStringUtil.isEmpty(demande.getIdDemandeValiderDecisionStep()))) {
                        // Si la demande est créée, il n'y a de messages d'importations
                        if (!JadeStringUtil.isEmpty(demande.getIdDemandeValiderDecisionStep())) {
                            // Parcours de chaque motif de refus
                            loadMotifsRefusDemande(demande, tabCasRejeteRecapTmr);
                        } else {
                            // Parcours de chaque messages d'erreurs
                            if (demande.getMessagesErreursImportationList() != null) {
                                loadMessagesErreursDemande(demande, tabCasRejeteRecapTmr);
                            }
                        }
                    }
                }
            }
        }

        // Insertion de la dernière ligne total
        DataList ligneTotale = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_TOTAL_CAS_REJETES_RECAPITULATIF);
        ligneTotale.addData(IRFRecapImportTmr.TEXTE_TOTAL_DEMANDES_REJETEES_RECAPITULATIF, mainDocument.getTextes(3)
                .getTexte(7).getDescription());
        ligneTotale.addData(IRFRecapImportTmr.MONTANT_TOTAL_DEMANDES_REJETEES, new FWCurrency(
                montantTotalDemandesRejetees.toString()).toStringFormat());

        // Insertion de la ligne à la collection
        tabCasRejeteRecapTmr.add(ligneTotale);

    }

    /**
     * Methode pour traiter les demandes acceptées/partielle/refusPC_compensées
     * 
     * @param tabCasTraiteRecapTmr
     */
    private void loadDemandesTraitees(Collection tabCasTraiteRecapTmr) {

        BigDecimal montantTotalCasTraites = new BigDecimal(0);

        for (Map.Entry<String, ArrayList<RFImportDemandesCmsData>> entry : regroupementDemandesParCodeTraitementMap
                .entrySet()) {
            for (RFImportDemandesCmsData demande : regroupementDemandesParCodeTraitementMap.get(entry.getKey())) {

                if (!JadeStringUtil.isEmpty(demande.getIdDemandeValiderDecisionStep())) {

                    // Insertion si la demande contient un montant payé
                    // if (!JadeStringUtil.isBlankOrZero(demande.getMontantPaye())) {
                    if (demande.getCodeTraitement().equals(
                            RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_TOTAL.getCode())
                            || demande.getCodeTraitement().equals(
                                    RFCodeTraitementDemandeTmrCleEnum.PAIEMENT_PARTIEL.getCode())) {

                        DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_TRAITES_RECAPITULATIF);
                        ligne.addData(IRFRecapImportTmr.NSS_DEMANDE_TRAITEE, demande.getNssBeneficiaire());
                        if (JadeStringUtil.isEmpty(demande.getNomTiers())
                                || JadeStringUtil.isEmpty(demande.getPrenomTiers())) {
                            ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_TRAITEE,
                                    demande.getNomPrenomTiersFichierSource());
                        } else {
                            ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_TRAITEE, demande.getNomTiers() + " "
                                    + demande.getPrenomTiers());
                        }
                        ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_TRAITEE, demande.getNomTiers() + " "
                                + demande.getPrenomTiers());
                        ligne.addData(IRFRecapImportTmr.ANNEE_QD_DEMANDE_TRAITEE, demande.getAnneeQd());
                        ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_TRAITE,
                                new FWCurrency(demande.getMontantPaye()).toStringFormat());

                        tabCasTraiteRecapTmr.add(ligne);

                        montantTotalCasTraites = montantTotalCasTraites.add(new BigDecimal(demande.getMontantPaye()));
                    }
                }
            }
        }

        // Ajout de la dernière ligne des montants totaux
        DataList ligneTotalCasTraites = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_TOTAL_CAS_TRAITES_RECAPITULATIF);
        ligneTotalCasTraites.addData(IRFRecapImportTmr.TEXTE_TOTAL_DEMANDES_TRAITEES_RECAPITULATIF, mainDocument
                .getTextes(2).getTexte(6).getDescription());
        ligneTotalCasTraites.addData(IRFRecapImportTmr.MONTANT_TOTAL_DEMANDES_TRAITEES, new FWCurrency(
                montantTotalCasTraites.toString()).toStringFormat());

        // Insertion de la ligne à la collection
        tabCasTraiteRecapTmr.add(ligneTotalCasTraites);
    }

    /**
     * Methode pour charger les message d'erreurs d'une demande
     * 
     * @param demande
     * @param tabCasRejeteRecapTmr
     */
    private void loadMessagesErreursDemande(RFImportDemandesCmsData demande, Collection tabCasRejeteRecapTmr) {
        // Si la demande comporte plusieurs messages d'erreurs, on itère pour créer une ligne à chaque
        // message d'erreur
        if (demande.getMessagesErreursImportationList().size() > 1) {

            DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
            ligne.addData(IRFRecapImportTmr.NUMERO_LIGNE_CAS_REJETES, demande.getNumeroLigne());
            ligne.addData(IRFRecapImportTmr.NSS_DEMANDE_REJETEE, checkData(formatNss(demande).toString()));

            if (JadeStringUtil.isEmpty(demande.getNomTiers()) || JadeStringUtil.isEmpty(demande.getPrenomTiers())) {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR,
                        checkData(demande.getNomPrenomTiersFichierSource()));
            } else {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR, checkData(demande.getNomTiers()) + " "
                        + checkData(demande.getPrenomTiers()));
            }

            ligne.addData(
                    IRFRecapImportTmr.ANNEE_QD_DEMANDE_REJETEE,
                    checkData(formatDateDemandeRejetee(demande.getDateDeDebutTraitement(),
                            demande.getDateDeFinTraitement())));
            ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_REJETEE, getMontantDemandeRejetee(demande));
            ligne.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                    .getMessagesErreursImportationList().get(0)[0]);
            ligne.addData(IRFRecapImportTmr.CODE_RECAP_REJETE, demande.getCodeTraitement());
            tabCasRejeteRecapTmr.add(ligne);

            // Ajout d'une ligne comportant uniquement le message d'erreur, sans répéter les infos sur
            // le
            // tiers
            for (int i = 1; i < demande.getMessagesErreursImportationList().size(); i++) {

                DataList ligneMessageErreur = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
                ligneMessageErreur.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                        .getMessagesErreursImportationList().get(i)[0]);
                // Insertion de la ligne à la collection
                tabCasRejeteRecapTmr.add(ligneMessageErreur);
            }
        }

        // Sinon, on insère une seule ligne.
        else {
            DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
            ligne.addData(IRFRecapImportTmr.NUMERO_LIGNE_CAS_REJETES, demande.getNumeroLigne());
            ligne.addData(IRFRecapImportTmr.NSS_DEMANDE_REJETEE, checkData(formatNss(demande).toString()));

            if (JadeStringUtil.isEmpty(demande.getNomTiers()) || JadeStringUtil.isEmpty(demande.getPrenomTiers())) {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR,
                        checkData(demande.getNomPrenomTiersFichierSource()));
            } else {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR, checkData(demande.getNomTiers()) + " "
                        + checkData(demande.getPrenomTiers()));
            }

            ligne.addData(
                    IRFRecapImportTmr.ANNEE_QD_DEMANDE_REJETEE,
                    checkData(formatDateDemandeRejetee(demande.getDateDeDebutTraitement(),
                            demande.getDateDeFinTraitement())));
            ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_REJETEE, getMontantDemandeRejetee(demande));

            if (demande.getMessagesErreursImportationList() != null) {
                ligne.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                        .getMessagesErreursImportationList().get(0)[0]);
            }
            ligne.addData(IRFRecapImportTmr.CODE_RECAP_REJETE, demande.getCodeTraitement());

            // Insertion de la ligne à la collection
            tabCasRejeteRecapTmr.add(ligne);
        }
    }

    /**
     * Methode pour charger la demandes par motifs de refus
     * 
     * @param demande
     */
    private void loadMotifsRefusDemande(RFImportDemandesCmsData demande, Collection tabCasRejeteRecapTmr) {

        // Si la demande comporte plusieurs motif de refus, on itère pour créer une ligne à chaque motif
        if ((demande.getMotifsRefusList() != null) && (demande.getMotifsRefusList().size() > 1)) {

            DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
            ligne.addData(IRFRecapImportTmr.NUMERO_LIGNE_CAS_REJETES, demande.getNumeroLigne());
            ligne.addData(IRFRecapImportTmr.NSS_DEMANDE_REJETEE, checkData(formatNss(demande).toString()));

            if (JadeStringUtil.isEmpty(demande.getNomTiers()) || JadeStringUtil.isEmpty(demande.getPrenomTiers())) {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR,
                        checkData(demande.getNomPrenomTiersFichierSource()));
            } else {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR, checkData(demande.getNomTiers()) + " "
                        + checkData(demande.getPrenomTiers()));
            }

            ligne.addData(
                    IRFRecapImportTmr.ANNEE_QD_DEMANDE_REJETEE,
                    checkData(formatDateDemandeRejetee(demande.getDateDeDebutTraitement(),
                            demande.getDateDeFinTraitement())));

            ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_REJETEE, getMontantDemandeRejetee(demande));
            ligne.addData(IRFRecapImportTmr.CODE_RECAP_REJETE, demande.getCodeTraitement());
            ligne.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                    .getMotifsRefusList().get(0));
            tabCasRejeteRecapTmr.add(ligne);

            // Ajout d'une ligne comportant uniquement le message d'erreur, sans répéter les infos sur
            // le
            // tiers
            for (int i = 1; i < demande.getMotifsRefusList().size(); i++) {

                DataList ligneMessageErreur = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
                ligneMessageErreur.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                        .getMotifsRefusList().get(i));

                // Insertion de la ligne à la collection
                tabCasRejeteRecapTmr.add(ligneMessageErreur);
            }
        }

        // Sinon, on insère une seule ligne.
        else {
            DataList ligne = new DataList(IRFRecapImportTmr.LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF);
            ligne.addData(IRFRecapImportTmr.NUMERO_LIGNE_CAS_REJETES, demande.getNumeroLigne());
            ligne.addData(IRFRecapImportTmr.NSS_DEMANDE_REJETEE, checkData(formatNss(demande).toString()));

            if (JadeStringUtil.isEmpty(demande.getNomTiers()) || JadeStringUtil.isEmpty(demande.getPrenomTiers())) {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR,
                        checkData(demande.getNomPrenomTiersFichierSource()));
            } else {
                ligne.addData(IRFRecapImportTmr.NOM_PRENOM_DEMANDE_ERREUR, checkData(demande.getNomTiers()) + " "
                        + checkData(demande.getPrenomTiers()));
            }

            ligne.addData(
                    IRFRecapImportTmr.ANNEE_QD_DEMANDE_REJETEE,
                    checkData(formatDateDemandeRejetee(demande.getDateDeDebutTraitement(),
                            demande.getDateDeFinTraitement())));
            ligne.addData(IRFRecapImportTmr.MONTANT_DEMANDE_REJETEE, getMontantDemandeRejetee(demande));
            ligne.addData(IRFRecapImportTmr.CODE_RECAP_REJETE, demande.getCodeTraitement());

            if (demande.getMotifsRefusList() != null) {
                ligne.addData(IRFRecapImportTmr.MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF, demande
                        .getMotifsRefusList().get(0));
            }

            // Insertion de la ligne à la collection
            tabCasRejeteRecapTmr.add(ligne);
        }

    }

    /**
     * Méthode de point d'entrée pour la construction du document
     * 
     * @param groupementDemandesParCodeTraitement
     * @param memoryLog
     * @param isMiseEnGed
     * @return
     * @throws Exception
     */
    public JadePrintDocumentContainer preparerContainerRecapTmr(
            Map<String, ArrayList<RFImportDemandesCmsData>> groupementDemandesParCodeTraitement, FWMemoryLog memoryLog,
            boolean isMiseEnGed, BSession session, JadeProcessStep step, String eMailAdress, String numAf)
            throws Exception {

        regroupementDemandesParCodeTraitementMap = groupementDemandesParCodeTraitement;
        this.session = session;
        email = eMailAdress;
        this.numAf = numAf;

        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        container.setMergedDocDestination(docInfoFinal());
        container.addDocument(creerRecapTmrDocumentOO(), docInfo);

        return container;
    }

    private void sendMailError(String message) throws Exception {
        JadeSmtpClient.getInstance().sendMail(
                email,
                BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_NAME"),
                BSessionUtil.getSessionFromThreadContext().getLabel(
                        "RF_IMPORT_TMR_PROCESS_VALIDATION_DECISION_FAILED")
                        + "\n \n" + message, null);
        session.addError(message);
    }

    public void setDocData(DocumentData docData) {
        data = docData;
    }

    public void setDocumentHelper(ICTDocument documentHelper) {
        this.documentHelper = documentHelper;
    }

    public void setDocumentProperties(ICTScalableDocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMainDocument(ICTDocument mainDocument) {
        this.mainDocument = mainDocument;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

}
