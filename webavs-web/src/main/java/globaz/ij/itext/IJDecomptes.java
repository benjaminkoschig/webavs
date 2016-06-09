package globaz.ij.itext;

import globaz.apg.groupdoc.ccju.GroupdocPropagateUtil;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.application.IJApplication;
import globaz.ij.db.lots.IJFactureACompenser;
import globaz.ij.db.lots.IJFactureACompenserManager;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJCotisationManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestationManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceJointDemande;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.pojo.AdressePaiementDecompteIJPojo;
import globaz.ij.properties.IJProperties;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.external.IntRole;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.employeurs.PRDepartement;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRBlankBNumberFormater;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.util.TIIbanFormater;
import globaz.webavs.common.CommonProperties;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.common.properties.PropertiesException;

/**
 * @author VRE
 */
public class IJDecomptes extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Une classe d'aggrégation de toutes les informations relatives a un décompte.
     * 
     * Une instance de cette classe est créée pour chaque bénéficiaire de paiement. Elle est ensuite renseignée au moyen
     * de toutes les prestations et factures a compenser qui existent pour ce bénéficiaire.
     */
    private static class Decompte {

        private PRDepartement departement;
        private boolean employeur;
        private List<IJFactureACompenser> facturesACompenser;
        private final String idAffilie;
        private final String idTiers;
        private Map<String, List<IJRepartitionJointPrestation>> repartitionsEnfants;
        private final Set<IJRepartitionJointPrestation> repartitionsPeres = new HashSet<IJRepartitionJointPrestation>();

        public Decompte(IJRepartitionJointPrestation repartition) throws Exception {
            idTiers = repartition.getIdTiers();
            idAffilie = repartition.getIdAffilie();
            employeur = repartition.isBeneficiaireEmployeur();

            if (employeur) {
                // Dans les cas ou les bénéficiaires des paiements de type employeurs ne sont pas les mêmes que ceux
                // saisi dans la situation professionnelle, on ne pourra pas la charger.
                // Le département ne sera pas pris en compte.
                if (repartition.loadSituationProfessionnelle() != null) {
                    departement = repartition.loadSituationProfessionnelle().loadEmployeur().loadDepartement();
                }
            }
        }

        public void addFactureACompenser(IJFactureACompenser factureACompenser) {

            // On ajoute que les factures à compenser
            if (!factureACompenser.getIsCompense().booleanValue()) {
                return;
            }

            if (facturesACompenser == null) {
                facturesACompenser = new ArrayList<IJFactureACompenser>();
            }
            if (!facturesACompenser.contains(factureACompenser)) {
                facturesACompenser.add(factureACompenser);
            }
        }

        /**
         * ajoute une ventilation de montant.
         */
        public void addRepartitionEnfant(IJRepartitionJointPrestation repartition) {
            if (repartitionsEnfants == null) {
                repartitionsEnfants = new HashMap<String, List<IJRepartitionJointPrestation>>();
            }

            List<IJRepartitionJointPrestation> enfants = repartitionsEnfants.get(repartition.getIdParent());

            if (enfants == null) {
                enfants = new ArrayList<IJRepartitionJointPrestation>();
                repartitionsEnfants.put(repartition.getIdParent(), enfants);
            }

            enfants.add(repartition);
        }

        /**
         * Ajouter une repartition au décompte de ce bénéficiaire, il doit forcement s'agir d'une repartition parente.<br />
         * Ajoute également les ventilations de montant de cette repartition.
         */
        public void addRepartitionPere(IJRepartitionJointPrestation repartition) {
            repartitionsPeres.add(repartition);
        }

        public PRDepartement getDepartement() {
            return departement;
        }

        public List<IJFactureACompenser> getFacturesACompenser() {
            return facturesACompenser;
        }

        public String getIdAffilie() {
            return idAffilie;
        }

        public String getIdTiers() {
            return idTiers;
        }

        public List<IJRepartitionJointPrestation> getRepartitionsEnfants(IJRepartitionJointPrestation repartition) {
            if (repartitionsEnfants == null) {
                return null;
            } else {
                return repartitionsEnfants.get(repartition.getIdRepartitionPaiement());
            }
        }

        public Set<IJRepartitionJointPrestation> getRepartitionsPeres() {
            return repartitionsPeres;
        }

        public boolean isEmployeur() {
            return employeur;
        }
    }

    private static final String FICHIER_MODELE = "IJ_DECOMPTE";
    private static final String FICHIER_RESULTAT = "decompte";
    private static final String FIELD_ADRESSE_PAIEMENT = "FIELD_ADRESSE_PAIEMENT";

    private static final String FIELD_DETAIL_NBR_JOUR_EXTERNE = "FIELD_DETAIL_NBR_JOUR_EXTERNE";

    private static final String FIELD_DETAIL_NBR_JOUR_INTERNE = "FIELD_DETAIL_NBR_JOUR_INTERNE";
    private static final String FIELD_TEXTE_ADRESSE_PAIEMENT = "FIELD_TEXTE_ADRESSE_PAIEMENT";

    private static final String ORDER_PRINTING_BY = "orderPrintingBy";

    private static final int STATE_DEBUT = -1;
    private static final int STATE_FIN = -2;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_VENTILATION = 4;

    private ICaisseReportHelper caisseHelper;
    private String codeIsoLangue = "fr";
    private JADate date;
    private Decompte decompteCourant;
    private Collection<Decompte> decomptesCollection;
    private ICTDocument document;
    private ICTDocument documentHelper;
    private Map<String, ICTDocument> documents = new HashMap<String, ICTDocument>();
    private boolean grandTotalEgalZero = false;
    private boolean hasNext = false;
    private String idLot = "";
    private Boolean isDecompteDefinitif = Boolean.FALSE;
    private Boolean isSendToGED = Boolean.FALSE;
    private Iterator<Decompte> iterateurDecomptes;
    private String noAffilie = "";
    private boolean restitution;
    private int state = IJDecomptes.STATE_DEBUT;

    public IJDecomptes() throws FWIException {
        super();
        setSendCompletionMail(false);
    }

    public IJDecomptes(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, IJDecomptes.FICHIER_RESULTAT);
        setSendCompletionMail(false);
    }

    public IJDecomptes(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
        setSendCompletionMail(false);
    }

    public IJDecomptes(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, IJDecomptes.FICHIER_RESULTAT);
        setSendCompletionMail(false);
    }

    public IJDecomptes(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
        setSendCompletionMail(false);
    }

    /**
     * Défini les propriétés du JadePublishDocumentInfo et génère le fichier PDF pour l'impression
     */
    @Override
    public void afterExecuteReport() {
        if (!hasNext) {
            if (getAttachedDocuments().size() > 0) {

                try {
                    // si CVCI et impression definitive
                    if (IJApplication.NO_CAISSE_CVCI.equals(PRAbstractApplication.getApplication(
                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(CommonProperties.KEY_NO_CAISSE))
                            && (getIsSendToGED().booleanValue())) {

                        // on enlève de la liste les éléments qui ne sont pas archivés, ni publiés
                        Iterator<JadePublishDocument> iter = getAttachedDocuments().iterator();
                        while (iter.hasNext()) {
                            JadePublishDocument doc = iter.next();
                            JadePublishDocumentInfo dInf = doc.getPublishJobDefinition().getDocumentInfo();

                            if (!dInf.getArchiveDocument() && !dInf.getPublishDocument()) {
                                iter.remove();
                            }
                        }
                    }

                    String anneeDecompte = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();

                    // on défini les propriétés du DocInfo pour envoyer le mail
                    setSendCompletionMail(false);
                    JadePublishDocumentInfo docInfo = createDocumentInfo();
                    docInfo.setPublishDocument(true);
                    docInfo.setArchiveDocument(false);
                    docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
                    docInfo.setDocumentTitle(getSession().getLabel("IMPR_DECOMPTE_SUCCES"));
                    docInfo.setDocumentProperty("annee", anneeDecompte);

                    // Pour les décomptes définitifs et les client qui possèdent une GED
                    if (getIsSendToGED().booleanValue()) {
                        // on ne supprime pas les documents individuels car on doit les envoies à la GED on trie les
                        // documents sur le critère "orderPrintBy"
                        this.mergePDF(docInfo, false, 0, false, IJDecomptes.ORDER_PRINTING_BY);
                    } else {
                        // on supprime les documents individuels car on ne les envoies pas à la GED on trie les
                        // documents sur le critère "orderPrintBy"
                        this.mergePDF(docInfo, true, 0, false, IJDecomptes.ORDER_PRINTING_BY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getMemoryLog().logMessage("IJDecompte afterExecuteReport():" + e.getMessage(), FWMessage.ERREUR,
                            "IJDecomptes");
                }
            }
        }
    }

    @Override
    public void afterPrintDocument() throws FWIException {

        JadePublishDocumentInfo docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        try {

            String anneeDecompte = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();

            // on défini les propriétés du DocInfo pour l'archivage uniquement
            // - pour les client qui possèdent une GED
            // - pour les décomptes définitifs ou les décomptes non-définitif pour lesquels on force l'envoi a la GED
            docInfo.setDocumentProperty("annee", anneeDecompte);
            docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_IJ_TITLE"));
            docInfo.setDocumentDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDate().toStrAMJ()));

            if (getIsSendToGED().booleanValue()) {
                docInfo.setArchiveDocument(true);
            } else {
                docInfo.setArchiveDocument(false);
            }

            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), decompteCourant.getIdTiers());

            PRTiersWrapper tiers = null;

            String rolePourLaGed = "";
            String idTiersPourLaGED = "";
            String noAffiliePourLaGED = "";

            if (decompteCourant.isEmployeur()) {

                docInfo.setDocumentType(docInfo.getDocumentType() + "Aff");
                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);

                if (affilie != null) {
                    // Employeur affilié --> rôle AFFILIE
                    rolePourLaGed = ITIRole.CS_AFFILIE;
                    idTiersPourLaGED = decompteCourant.getIdTiers();
                    noAffiliePourLaGED = affilie.getNumAffilie();
                } else {
                    // Employeur non affilié --> rôle NON_AFFILIE
                    rolePourLaGed = IPRConstantesExternes.OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE;
                    idTiersPourLaGED = decompteCourant.getIdTiers();
                    noAffiliePourLaGED = "";
                }
            } else {
                // Assuré --> rôle IJAI

                if (isCaisse(IJApplication.NO_CAISSE_CVCI)) {

                    JadePublishDocumentInfo docInfoCvci = docInfo.createCopy();

                    // pour la caisse CVCI les documents envoyé directement à l'assuré doivent être indexés sur le n°
                    // d'affilié du/des employeurs
                    docInfoCvci = remplirDocInfoPourAssureIJAISpecifiqueCVCI(docInfoCvci);

                } else {

                    rolePourLaGed = IntRole.ROLE_IJAI;

                    docInfo.setDocumentType(docInfo.getDocumentType() + "Aff");
                    docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);

                    tiers = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

                    if (affilie != null) {

                        idTiersPourLaGED = decompteCourant.getIdTiers();

                        if (JadeStringUtil.isBlank(affilie.getNumAffilie())) {
                            noAffiliePourLaGED = "";
                        } else {
                            noAffiliePourLaGED = affilie.getNumAffilie();
                        }

                    } else {
                        // versement à tiers
                        idTiersPourLaGED = chercherIdTiersPourLigneTechinqueVersementATier(tiers);
                        noAffiliePourLaGED = "";
                    }
                }
            }

            // pour la CCJU, les documents envoyés aux employeurs doivent être indexés uniquement avec le n° d'affilié
            if (isCaisse(IJApplication.NO_CAISSE_CCJU) && IntRole.ROLE_AFFILIE.equals(rolePourLaGed)) {
                docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
            }

            if (!isCaisse(IJApplication.NO_CAISSE_CVCI)) {
                // on ajoute au doc info le critère de tri pour les impressions ORDER_PRINTING_BY
                docInfo.setDocumentProperty(IJDecomptes.ORDER_PRINTING_BY,
                        buildOrderPrintingByKey(decompteCourant.getIdAffilie(), decompteCourant.getIdTiers()));

                IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                        AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                docInfo.setDocumentProperty("numero.affilie.formatte", noAffiliePourLaGED);

                TIDocumentInfoHelper.fill(docInfo, idTiersPourLaGED, getSession(), rolePourLaGed, noAffiliePourLaGED,
                        affilieFormatter.unformat(noAffiliePourLaGED));

                if (getIsSendToGED().booleanValue()) {
                    // création / mise à jour du dossier GroupDoc
                    GroupdocPropagateUtil.propagateData(affilie, tiers, null);
                }
            }

            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, documentHelper.getCsTypeDocument());

            // on ajoute au doc info le critère de tri pour les impressions ORDER_PRINTING_BY
            docInfo.setDocumentProperty(IJDecomptes.ORDER_PRINTING_BY,
                    buildOrderPrintingByKey(decompteCourant.getIdAffilie(), decompteCourant.getIdTiers()));

            // La gestion du NSS est différente selon la caisse, pour la mise en GED.
            // Création d'une propriété pour les caisses qui veulent le NSS vide lors de la mise en GED
            // Par défaut, l'absence de propriété ou si la propriété est à FALSE, le NSS sera remplacé par
            // 000.00.000.000
            boolean propertyNssBlank = false;

            try {
                propertyNssBlank = IJProperties.BLANK_INDEX_GED_NSS_A_ZERO.getBooleanValue();
            } catch (PropertiesException e) {
                // Résultat de l'absence de propriété.
                JadeSmtpClient.getInstance().sendMail(getEMailAddress(),
                        getSession().getLabel("SUBJECT_MAIL_PARAM_GED_PROPERTY_INCOMPLETE"),
                        getSession().getLabel("BODY_MAIL_PARAM_GED_PROPERTY_NSS_MANQUANTE"), null);
                throw new PropertiesException(e
                        + " : "
                        + FWMessageFormat.format(getSession().getLabel("ERREUR_PROPRIETE_INEXISTANTE"),
                                IJProperties.BLANK_INDEX_GED_NSS_A_ZERO.getPropertyName()));
            }

            if (!propertyNssBlank) {
                String avsnf = docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                String avsf = docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

                if (JadeStringUtil.isBlank(avsnf) || JadeStringUtil.isBlank(avsf) || (avsnf == "00000000000")) {
                    // Si n°AVS est vide, le remplacer par des '0'
                    docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
                }
            }

            boolean proprieteNumeroAffilieForceAZeroSiVide = false;
            try {
                proprieteNumeroAffilieForceAZeroSiVide = IJProperties.NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE
                        .getBooleanValue();
            } catch (PropertiesException e) {
                JadeSmtpClient.getInstance().sendMail(getEMailAddress(),
                        getSession().getLabel("SUBJECT_MAIL_PARAM_GED_PROPERTY_INCOMPLETE"),
                        getSession().getLabel("BODY_MAIL_PARAM_GED_PROPERTY_NAFF_MANQUANTE"), null);
                throw new PropertiesException(e
                        + " : "
                        + FWMessageFormat.format(getSession().getLabel("ERREUR_PROPRIETE_INEXISTANTE"),
                                IJProperties.NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE.getPropertyName()));
            }

            if (proprieteNumeroAffilieForceAZeroSiVide) {
                String noAffilie = docInfo.getDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE);

                if (JadeStringUtil.isBlank(noAffilie)) {
                    docInfo = PRBlankBNumberFormater.fillEmptyNoAffilie(docInfo);
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            Map<String, String> parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap<String, String>();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            parametres.put(
                    "PARAM_IJ_DECOMPTE_DETAIL",
                    JadeStringUtil.change(getSession().getApplication().getExternalModelPath()
                            + IJApplication.APPLICATION_IJ_REP, '\\', '/')
                            + "/" + "model" + "/" + "IJ_DECOMPTE_DETAIL.jasper");

            parametres.put(
                    "PARAM_IJ_DECOMPTE_DETAIL2",
                    JadeStringUtil.change(getSession().getApplication().getExternalModelPath()
                            + IJApplication.APPLICATION_IJ_REP, '\\', '/')
                            + "/" + "model" + "/" + "IJ_DECOMPTE_DETAIL2.jasper");

            // remplissage de l'entête
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            if (JadeStringUtil.isIntegerEmpty(decompteCourant.getIdAffilie())) {
                PRTiersWrapper tiers;
                String adresse;

                noAffilie = "";

                try {
                    tiers = PRTiersHelper.getTiersParId(getISession(), decompteCourant.getIdTiers());

                    if (tiers == null) {
                        tiers = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
                    }

                    adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), decompteCourant.getIdTiers(),
                            decompteCourant.getIdAffilie(), IJApplication.CS_DOMAINE_ADRESSE_IJAI);
                } catch (Exception e) {
                    throw new FWIException("impossible de charger le tiers", e);
                }

                crBean.setDate(JACalendar.format(JACalendar.format(getDate()), codeIsoLangue));

                if (state != IJDecomptes.STATE_VENTILATION) {
                    crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                }
                crBean.setAdresse(adresse);

                // nom du document
                setDocumentTitle(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            } else {
                IPRAffilie affilie;
                String adresse;

                try {
                    affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getISession(), getSession()
                            .getCurrentThreadTransaction(), decompteCourant.getIdAffilie(), decompteCourant
                            .getIdTiers());
                    adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), decompteCourant.getIdTiers(),
                            decompteCourant.getIdAffilie(), IJApplication.CS_DOMAINE_ADRESSE_IJAI);

                    noAffilie = affilie.getNumAffilie();

                    // Renseignement de l'IDE
                    AFIDEUtil.addNumeroIDEInDoc(crBean, affilie.getNumeroIDE(), affilie.getIdeStatut());

                } catch (Exception e) {
                    throw new FWIException("impossible de charger le tiers", e);
                }

                if (state != IJDecomptes.STATE_VENTILATION) {
                    crBean.setNoAffilie(affilie.getNumAffilie());
                }
                crBean.setDate(JACalendar.format(JACalendar.format(getDate()), codeIsoLangue));
                crBean.setAdresse(adresse);

                // nom du document
                setDocumentTitle(affilie.getNumAffilie() + " - " + affilie.getNom());
            }
            if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_NOMCOLABO))) {
                // nom du collaborateur
                crBean.setNomCollaborateur(getSession().getUserFullName());
            }
            // création des paramètres pour l'en-tête
            try {
                // Ajoute le libellé CONFIDENTIEL dans l'adresse de l'entête du document
                if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {

                    crBean.setConfidentiel(true);
                }

                caisseHelper.addHeaderParameters(getImporter(), crBean);
            } catch (Exception e) {
                throw new FWIException("Impossible de renseigner l'en-tete", e);
            }

            // ajout du nom du département si nécessaire
            if (decompteCourant.getDepartement() != null) {
                parametres.put("P_HEADER_DEPARTEMENT", decompteCourant.getDepartement().getDepartement());
            }

            // le titre
            parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(1).getDescription());

            String type_decompte = "";

            switch (state) {

                case STATE_NORMAL:
                    type_decompte = document.getTextes(5).getTexte(6).getDescription();
                    break;

                case STATE_VENTILATION:
                    type_decompte = document.getTextes(5).getTexte(7).getDescription();
                    break;
            }

            String texteTitre2 = PRStringUtils.replaceString(document.getTextes(5).getTexte(1).getDescription(),
                    "{type}", type_decompte);

            texteTitre2 = PRStringUtils.replaceString(texteTitre2, "{dateDec}", JACalendar.format(getDate()));

            parametres.put("PARAM_TITRE2", texteTitre2);

            PRTiersWrapper tiers;
            tiers = PRTiersHelper.getTiersParId(getISession(), decompteCourant.getIdTiers());

            if (tiers == null) {
                tiers = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
            }

            parametres.put("P_HEADER_NOM_PAGE2", PRStringUtils.replaceString(
                    document.getTextes(5).getTexte(4).getDescription(),
                    "{nomPrenom}",
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)));

            if (JadeStringUtil.isEmpty((noAffilie))) {

                String nAvs = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                String idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

                if (!JadeStringUtil.isEmpty(nAvs)) {
                    parametres.put("P_HEADER_NO_AVS_PAGE2", PRStringUtils.replaceString(
                            document.getTextes(5).getTexte(3).getDescription(), "{noAVS}", nAvs));
                }

                if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                    if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DISPLAY_NIP))) {
                        parametres.put("P_HEADER_NIP_LIB", getSession().getLabel("NIP") + " :");
                        parametres.put("P_HEADER_NIP", idTiers);
                    }
                }
            } else {

                parametres.put("P_HEADER_NO_AFFILIE_PAGE2", PRStringUtils.replaceString(
                        document.getTextes(5).getTexte(2).getDescription(), "{noAffilie}", noAffilie));
            }

            parametres.put("PARAM_PAGE", document.getTextes(5).getTexte(5).getDescription());

            // le corps du document
            StringBuffer buffer = new StringBuffer();

            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            String titre = tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            for (Iterator<ICTTexte> textes = document.getTextes(2).iterator(); textes.hasNext();) {
                ICTTexte texte = textes.next();

                // ne pas traiter le contenu optionnel
                if (Integer.parseInt(texte.getPosition()) > 100) {
                    break;
                }

                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }
                buffer.append(texte.getDescription());
            }
            buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));

            buffer.append("\n\n");

            // cette méthode est exécutée après createDataSource donc nous connaissons le total des prestations, et donc
            // nous savons s'il s'agit d'un document de restitution.
            if (restitution) {
                buffer.append(document.getTextes(2).getTexte(102).getDescription());
            } else {
                buffer.append(document.getTextes(2).getTexte(101).getDescription());
            }

            parametres.put("PARAM_CORPS", buffer.toString());

            // le détail
            parametres.put("PARAM_ASSURE", document.getTextes(3).getTexte(1).getDescription());
            parametres.put("PARAM_DETAIL", document.getTextes(3).getTexte(2).getDescription());
            parametres.put("PARAM_MONTANT", document.getTextes(3).getTexte(3).getDescription());
            parametres.put("PARAM_DEVISE", document.getTextes(3).getTexte(4).getDescription());

            // le pied de page
            buffer.setLength(0);

            for (Iterator<ICTTexte> textes = document.getTextes(4).iterator(); textes.hasNext();) {
                ICTTexte texte = textes.next();

                // ne pas traiter le contenu optionnel
                if (Integer.parseInt(texte.getPosition()) > 100) {
                    break;
                }

                if (Integer.parseInt(texte.getPosition()) == 1) {
                    if (buffer.length() > 0) {
                        buffer.append("\n");
                    }
                    buffer.append(texte.getDescription());
                }

            }

            parametres.put("PARAM_PIED", buffer.toString());

            // ajouter les signatures
            buffer.setLength(0);

            buffer.append(document.getTextes(6).getTexte(1).getDescription());

            buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));
            buffer.append("\n");
            parametres.put("PARAM_SALUTATIONS", buffer.toString());

            try {
                caisseHelper.addSignatureParameters(getImporter());
            } catch (Exception e) {
                throw new FWIException("Impossible de charger le pied de page", e);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, IJDecomptes.class.getSimpleName());
            abort();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Effacer les pdf à la fin
        setDeleteOnExit(true);

        // le modèle
        try {
            String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelCaisse");
            if ((null != extensionModelCaisse) && "true".equals(extensionModelCaisse)) {
                setTemplateFile(IJDecomptes.FICHIER_MODELE + extensionModelCaisse);
                FWIImportManager im = getImporter();
                File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                        + FWITemplateType.TEMPLATE_JASPER.toString());
                if ((sourceFile != null) && sourceFile.exists()) {
                    ;
                } else {
                    setTemplateFile(IJDecomptes.FICHIER_MODELE);
                }
            } else {
                setTemplateFile(IJDecomptes.FICHIER_MODELE);
            }
        } catch (Exception e) {
            setTemplateFile(IJDecomptes.FICHIER_MODELE);
        }
    }

    /**
     * On n'imprime que les décomptes avec un grand total sup. à zéro
     */
    @Override
    public boolean beforePrintDocument() {

        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);

        if (grandTotalEgalZero) {
            grandTotalEgalZero = false;
            // possible car on imprime un PDF par décompte
            deleteAllDocument();
            return false;
        } else {
            return true && super.beforePrintDocument();
        }
    }

    private String buildOrderPrintingByKey(String idAffilie, String idTiers) throws Exception {

        String noAffilieFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
        String noAvsFormatte = PRBlankBNumberFormater.getEmptyNssFormatte(getSession().getApplication());

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    idAffilie, idTiers);
            if (affilie != null) {
                noAffilieFormatte = affilie.getNumAffilie();
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if ((tierWrapper != null)
                    && !JadeStringUtil.isEmpty(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                noAvsFormatte = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }
        }

        return noAffilieFormatte + "_" + noAvsFormatte;
    }

    /**
     * <p>
     * Charge le catalogue de texte pour le décompte courant.
     * </p>
     * <p>
     * Cette méthode utilise la variable documentHelper qui est initialisée dans {@link #beforeExecuteReport()}.
     * </p>
     * <p>
     * Les catalogues ne sont chargés qu'un fois, ils sont ensuite mis en cache.
     * </p>
     * 
     * @throws FWIException
     */
    private void chargerCatalogue() throws FWIException {
        // charger le catalogue de texte pour le décompte decompteCourant
        try {

            String cle = cleCatalogue();

            if (JadeStringUtil.isEmpty(codeIsoLangue)) {
                codeIsoLangue = "fr";
            }

            // création du helper pour les entêtes et pieds de page
            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), codeIsoLangue);

            document = documents.get(cle);

            if (document == null) {
                documentHelper.setDefault(Boolean.TRUE);
                documentHelper.setCsDestinataire(decompteCourant.isEmployeur() ? ICTDocument.CS_EMPLOYEUR
                        : ICTDocument.CS_ASSURE);

                // on cherche la langue du destinataire
                if (!JadeStringUtil.isIntegerEmpty(decompteCourant.getIdAffilie())) {
                    IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                            decompteCourant.getIdAffilie(), "");
                    codeIsoLangue = getSession().getCode(affilie.getLangue());
                    codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                    documentHelper.setCodeIsoLangue(codeIsoLangue);

                } else {
                    PRTiersWrapper tierWrapper = PRTiersHelper
                            .getTiersParId(getSession(), decompteCourant.getIdTiers());

                    if (tierWrapper == null) {
                        tierWrapper = PRTiersHelper.getAdministrationParId(getSession(), decompteCourant.getIdTiers());
                    }
                    if (tierWrapper == null) {
                        System.err.println("IJDecompte-0002) ERROR !!! Tiers is null, cas impossible en théorie.");
                        codeIsoLangue = "fr";
                    } else {
                        codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                    }
                    documentHelper.setCodeIsoLangue(codeIsoLangue);
                }

                ICTDocument[] candidats = documentHelper.load();
                if (candidats == null) {
                    throw new FWIException("impossible de charger le catalogue de texte");
                }

                if ((candidats != null) && (candidats.length > 0)) {
                    document = candidats[0];
                    documents.put(cle, document);
                }
            }
        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte");
        }
    }

    private String chercherIdTiersPourLigneTechinqueVersementATier(PRTiersWrapper tiers) throws Exception {

        String idTiersPourLigneTechnique = decompteCourant.getIdTiers();

        if ((tiers == null) || ((tiers != null) && JadeStringUtil.isBlankOrZero(tiers.getNSS()))) {

            class TiersNomPrenom {
                String idTiers = null;
                String nom = null;
                String prenom = null;
            }

            // versement à tiers, recherche du premier assuré dans ce décompte. Son NSS sera utilisé
            // dans la ligne technique afin de ne pas perdre trace de ce document dans la GED.
            List<TiersNomPrenom> tiersDeCeDecommpte = new ArrayList<TiersNomPrenom>();

            for (IJRepartitionJointPrestation uneRepartition : decompteCourant.getRepartitionsPeres()) {

                IJPrononceJointDemandeManager prestationJointTiersManager = new IJPrononceJointDemandeManager();
                prestationJointTiersManager.setSession(getSession());
                prestationJointTiersManager.setForIdPrononce(uneRepartition.getIdPrononce());
                prestationJointTiersManager.find();

                if (prestationJointTiersManager.size() > 0) {
                    IJPrononceJointDemande unePrestation = (IJPrononceJointDemande) prestationJointTiersManager.get(0);

                    TiersNomPrenom unTiers = new TiersNomPrenom();
                    unTiers.idTiers = unePrestation.getIdTiers();
                    unTiers.nom = unePrestation.getNom();
                    unTiers.prenom = unePrestation.getPrenom();

                    tiersDeCeDecommpte.add(unTiers);
                }
            }

            if (tiersDeCeDecommpte.size() > 0) {

                Collections.sort(tiersDeCeDecommpte, new Comparator<TiersNomPrenom>() {

                    @Override
                    public int compare(TiersNomPrenom o1, TiersNomPrenom o2) {
                        if (!JadeStringUtil.isBlank(o1.nom) && !JadeStringUtil.isBlank(o2.nom)
                                && !JadeStringUtil.equals(o1.nom, o2.nom, true)) {
                            return o1.nom.toUpperCase().compareTo(o2.nom.toUpperCase());
                        }
                        if (!JadeStringUtil.isBlank(o1.prenom) && !JadeStringUtil.isBlank(o2.prenom)
                                && !JadeStringUtil.equals(o1.prenom, o2.prenom, true)) {
                            return o1.prenom.toUpperCase().compareTo(o2.prenom.toUpperCase());
                        }
                        return 0;
                    }
                });

                idTiersPourLigneTechnique = tiersDeCeDecommpte.get(0).idTiers;
            }
        }

        return idTiersPourLigneTechnique;
    }

    /**
     * Crée une clé qui identifie de manière unique un catalogue.
     */
    private String cleCatalogue() throws Exception {

        // on cherche la langue du destinataire
        if (decompteCourant.isEmployeur()) {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), "");

            if (affilie == null) {
                // Cas des employeurs non affilié
                PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

                if (tierWrapper == null) {
                    tierWrapper = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
                }

                codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            } else {
                codeIsoLangue = getSession().getCode(affilie.getLangue());
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            }

        } else {
            PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
            }
            if (tierWrapper == null) {
                System.err.println("IJDecompte-00001) ERROR !!! Ne doit jamais arriver ici !!!!!!");
                codeIsoLangue = "fr";
            } else {
                codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            }
        }

        return String.valueOf(decompteCourant.isEmployeur()) + "_" + codeIsoLangue;
    }

    private AdressePaiementDecompteIJPojo construitAdressePaiementDecompteIJPojo(String idDomaineAdressePaiement)
            throws Exception {

        AdressePaiementDecompteIJPojo adressePaiementDecompteIJPojo = new AdressePaiementDecompteIJPojo();

        // recherche de l'adresse de paiement
        TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(getSession(),
                (getSession()).getCurrentThreadTransaction(), decompteCourant.getIdTiers(), idDomaineAdressePaiement,
                "", date.toStr("."));

        String libelle = "";
        String adressePaiementString = "";

        // Selection du type de compte CCP
        if (!JadeStringUtil.isEmpty(adressePaiement.getCcp())) {

            libelle = document.getTextes(3).getTexte(47).getDescription() + " " + adressePaiement.getCcp();
            ;
            adressePaiementString = "";
        }
        // Selection du type de compte BANQUE
        else if (!JadeStringUtil.isEmpty(adressePaiement.getCompte())) {
            adressePaiementString = adressePaiement.getDesignation1_banque();
            if (!JadeStringUtil.isEmpty(adressePaiement.getDesignation2_banque())) {
                adressePaiementString = adressePaiementString + " " + adressePaiement.getDesignation2_banque();
            }

            String compte = adressePaiement.getCompte();
            try {
                TIIbanFormater ibanFormater = new TIIbanFormater();
                ibanFormater.check(compte);
                compte = ibanFormater.format(compte);
            } catch (Exception e) {
                // le compte ne peut pas être formaté (pas un compte iban)
                compte = adressePaiement.getCompte();
            }
            libelle = document.getTextes(3).getTexte(46).getDescription();
            adressePaiementString = adressePaiementString + ", " + compte;
        }
        // Selection du type de compte MANDAT POSTAL
        else {
            TIAdressePaiementDataSource adressePaiementDataSource = new TIAdressePaiementDataSource();
            adressePaiementDataSource.load(adressePaiement);

            ITIAdresseFormater adressePaiementBeneficiaireFormater = new TIAdressePaiementBeneficiaireFormater();

            libelle = document.getTextes(3).getTexte(48).getDescription();
            adressePaiementString = adressePaiementBeneficiaireFormater.format(adressePaiementDataSource);
        }

        if (!JadeStringUtil.isBlankOrZero(adressePaiementString)) {
            adressePaiementString = PRStringUtils.replaceString(document.getTextes(3).getTexte(49).getDescription(),
                    "{adressePaiement}", adressePaiementString.trim());
        }

        adressePaiementDecompteIJPojo.setLibelle(libelle);
        adressePaiementDecompteIJPojo.setAdressePaiement(adressePaiementString);

        return adressePaiementDecompteIJPojo;

    }

    @Override
    public void createDataSource() throws Exception {
        List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();
        FWCurrency total = new FWCurrency(0);
        FWCurrency grandTotal = new FWCurrency(0);
        IJPrononce prononce;
        PRTiersWrapper tiers;
        Map<String, String> champs = new HashMap<String, String>();
        FWCurrency totalIJ = new FWCurrency(0);
        FWCurrency totalCotisations = new FWCurrency(0);
        FWCurrency totalImpotSource = new FWCurrency(0);
        FWCurrency totalMontantVentile = new FWCurrency(0);
        FWCurrency totalCompensations = new FWCurrency(0);
        int nbDecompte = 0;

        // création du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getISession());
        documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
        documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_DECOMPTE);
        documentHelper.setActif(Boolean.TRUE);

        // on charge le catalogue de texte pour le nouveau décompte.
        createDocInfoIjDecomptes();
        chargerCatalogue();

        try {
            Set<IJRepartitionJointPrestation> repartitionsList = new TreeSet<IJRepartitionJointPrestation>(
                    new Comparator<IJRepartitionJointPrestation>() {
                        @Override
                        public int compare(IJRepartitionJointPrestation objRepartition1,
                                IJRepartitionJointPrestation objRepartition2) {

                            String nom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_NOM);
                            String nom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_NOM);

                            if (nom1 != null) {
                                int nomComp = nom1.compareTo(nom2);
                                if (nomComp != 0) {
                                    return nomComp;
                                } else {
                                    String prenom1;
                                    try {
                                        prenom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_PRENOM)
                                                + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(objRepartition1
                                                        .getDateDebutPrestation())
                                                + objRepartition1.getIdRepartitionPaiement();
                                    } catch (JAException e) {
                                        prenom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_PRENOM)
                                                + objRepartition1.getIdRepartitionPaiement();
                                    }
                                    String prenom2;
                                    try {
                                        prenom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_PRENOM)
                                                + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(objRepartition2
                                                        .getDateDebutPrestation())
                                                + objRepartition2.getIdRepartitionPaiement();
                                    } catch (JAException e) {
                                        prenom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_PRENOM)
                                                + objRepartition2.getIdRepartitionPaiement();
                                    }

                                    if (prenom1 != null) {
                                        return prenom1.compareTo(prenom2);
                                    } else {
                                        return 0;
                                    }
                                }
                            } else {
                                return 0;
                            }
                        }

                        private String getNom(IJRepartitionJointPrestation objRepartition, String propriete) {
                            try {
                                if (objRepartition != null) {
                                    IJPrononce prononce = IJPrononce.loadPrononce(IJDecomptes.this.getSession(),
                                            IJDecomptes.this.getTransaction(), objRepartition.getIdPrononce(),
                                            objRepartition.getCsTypeIJ());
                                    PRDemande demande = prononce.loadDemande(IJDecomptes.this.getTransaction());
                                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(IJDecomptes.this.getSession(),
                                            demande.getIdTiers());

                                    return tiers.getProperty(propriete);

                                } else {
                                    return "";
                                }
                            } catch (Exception e) {
                                return "";
                            }
                        }
                    });

            repartitionsList.addAll(decompteCourant.getRepartitionsPeres());

            String idDomaineAdressePaiement = "";
            for (IJRepartitionJointPrestation repartition : repartitionsList) {

                idDomaineAdressePaiement = repartition.getIdDomaineAdressePaiement();
                setTailleLot(1);
                setImpressionParLot(true);

                champs = new HashMap<String, String>();

                // --------------------------------------------------------------------------------------------------------
                // les lignes pour la répartition elle-même
                // ---------------------------------------------------------------

                nbDecompte += 1;

                // ré-initialisation du total pour chaque répartition
                if (!total.isZero()) {
                    total = new FWCurrency(0);
                }

                // 1. le n°AVS et le nom de l'assuré
                prononce = IJPrononce.loadPrononce(getSession(), getTransaction(), repartition.getIdPrononce(),
                        repartition.getCsTypeIJ());
                PRDemande demande = prononce.loadDemande(getTransaction());
                tiers = PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());

                champs.put("FIELD_ASSURE", PRStringUtils.replaceString(
                        document.getTextes(3).getTexte(13).getDescription(),
                        "{nomAVS}",
                        tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " "
                                + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)));

                // 2. Les détails
                champs.put("FIELD_DETAIL_PERIODE", PRStringUtils.replaceString(
                        document.getTextes(3).getTexte(14).getDescription(),
                        "{periode}",
                        JACalendar.format(repartition.getDateDebutPrestation(), codeIsoLangue) + " - "
                                + JACalendar.format(repartition.getDateFinPrestation(), codeIsoLangue)));

                if (state == IJDecomptes.STATE_VENTILATION) {

                    champs.put("FIELD_MONTANT_APG", PRStringUtils.replaceString(document.getTextes(3).getTexte(16)
                            .getDescription(), "{montantPeriode}",
                            JANumberFormatter.formatNoRound(repartition.getMontantVentile())));

                } else {

                    IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();
                    repartitionPaiementsManager.setSession(getSession());
                    repartitionPaiementsManager.setForParentOnly("true");
                    repartitionPaiementsManager.setForIdPrestation(repartition.getIdPrestation());
                    boolean isDetailJournalierToPrint = repartitionPaiementsManager.getCount() == 1;

                    if (!JadeStringUtil.isBlankOrZero(repartition.getNombreJourInternePrestation())
                            && isDetailJournalierToPrint) {

                        String montantJournalierInterne = new FWCurrency(
                                repartition.getMontantJournalierInternePrestation()).toStringFormat();
                        String texteNombreJourMontantJournalierInterne = document.getTextes(3).getTexte(44)
                                .getDescription();
                        texteNombreJourMontantJournalierInterne = PRStringUtils.replaceString(
                                texteNombreJourMontantJournalierInterne, "{nombreJourInterne}",
                                repartition.getNombreJourInternePrestation());
                        texteNombreJourMontantJournalierInterne = PRStringUtils.replaceString(
                                texteNombreJourMontantJournalierInterne, "{montantJournalierInterne}",
                                montantJournalierInterne);

                        champs.put(IJDecomptes.FIELD_DETAIL_NBR_JOUR_INTERNE, texteNombreJourMontantJournalierInterne);
                    }

                    if (!JadeStringUtil.isBlankOrZero(repartition.getNombreJourExternePrestation())
                            && isDetailJournalierToPrint) {

                        String montantJournalierExterne = new FWCurrency(
                                repartition.getMontantJournalierExternePrestation()).toStringFormat();
                        String texteNombreJourMontantJournalierExterne = document.getTextes(3).getTexte(45)
                                .getDescription();
                        texteNombreJourMontantJournalierExterne = PRStringUtils.replaceString(
                                texteNombreJourMontantJournalierExterne, "{nombreJourExterne}",
                                repartition.getNombreJourExternePrestation());
                        texteNombreJourMontantJournalierExterne = PRStringUtils.replaceString(
                                texteNombreJourMontantJournalierExterne, "{montantJournalierExterne}",
                                montantJournalierExterne);

                        champs.put(IJDecomptes.FIELD_DETAIL_NBR_JOUR_EXTERNE, texteNombreJourMontantJournalierExterne);
                    }

                    champs.put("FIELD_MONTANT_APG", PRStringUtils.replaceString(document.getTextes(3).getTexte(16)
                            .getDescription(), "{montantPeriode}",
                            JANumberFormatter.formatNoRound(repartition.getMontantBrut())));
                }

                if (state == IJDecomptes.STATE_VENTILATION) {
                    totalIJ.add(repartition.getMontantVentile());
                } else {
                    totalIJ.add(repartition.getMontantBrut());
                }

                // 3. les cotisations & l'impôt à la source
                IJCotisationManager ijCotMan = new IJCotisationManager();
                ijCotMan.setForIdRepartitionPaiements(repartition.getIdRepartitionPaiement());
                ijCotMan.setSession(getSession());
                ijCotMan.find(getTransaction(), BManager.SIZE_NOLIMIT);

                FWCurrency totalMontantCotisation = new FWCurrency(0);
                FWCurrency totalMontantImpotSource = new FWCurrency(0);

                String libelleCot = document.getTextes(3).getTexte(15).getDescription();
                String libelleAVS = "";
                String libelleAC = "";
                String libelleLFA = "";

                for (int i = 0; i < ijCotMan.size(); i++) {
                    IJCotisation ijCot = (IJCotisation) ijCotMan.getEntity(i);

                    if (ijCot.getIsImpotSource().booleanValue() == true) {
                        totalMontantImpotSource.add((ijCot.getMontant()));
                    }

                    else {
                        totalMontantCotisation.add((ijCot.getMontant()));

                        String AVSPAR = PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE);
                        String AVSPER = PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL);

                        String ACPAR = PRAffiliationHelper.GENRE_AC.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE);
                        String ACPER = PRAffiliationHelper.GENRE_AC.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL);

                        String LFAPAR = PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE);
                        String LFAPER = PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL);

                        if (AVSPAR.equals(ijCot.getIdExterne()) || AVSPER.equals(ijCot.getIdExterne())) {
                            libelleAVS = document.getTextes(3).getTexte(40).getDescription();
                        }
                        if (ACPAR.equals(ijCot.getIdExterne()) || ACPAR.equals(ijCot.getIdExterne())) {
                            libelleAC = document.getTextes(3).getTexte(41).getDescription();
                        }
                        if (LFAPAR.equals(ijCot.getIdExterne()) || LFAPER.equals(ijCot.getIdExterne())) {
                            libelleAC = document.getTextes(3).getTexte(42).getDescription();
                        }

                    }
                }

                String fieldRestitution = "";

                if (!JadeStringUtil.isBlankOrZero(decompteCourant.getIdAffilie())
                        && "true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DISPLAY_NIP))) {
                    fieldRestitution = getSession().getLabel("NIP") + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }

                // Si c'est une restitution, on affiche la mention "restitution"
                double montant = Double.parseDouble(repartition.getMontantBrut());
                if (montant < 0) {
                    if (!JadeStringUtil.isBlankOrZero(fieldRestitution)) {
                        fieldRestitution += " / ";
                    }
                    fieldRestitution += document.getTextes(3).getTexte(20).getDescription();
                }

                // Ce champ indique s'il s'agit d'une restitution, et peut également contenir le NIP.
                if (!JadeStringUtil.isBlankOrZero(fieldRestitution)) {
                    champs.put("FIELD_RESTITUTION", fieldRestitution);
                }

                // Affichage des cotisations
                if (!totalMontantCotisation.equals(new FWCurrency(0))) {
                    champs.put("FIELD_DETAIL_COTISATIONS", libelleCot + libelleAVS + libelleAC + libelleLFA);

                    champs.put("FIELD_MONTANT_COTISATIONS", PRStringUtils.replaceString(
                            document.getTextes(3).getTexte(17).getDescription(), "{montantCoti}",
                            JANumberFormatter.formatNoRound(totalMontantCotisation.toString())));

                    totalCotisations.add(totalMontantCotisation.toString());
                }

                // Affichage de l'impôt à la source
                if (!totalMontantImpotSource.equals(new FWCurrency(0))) {
                    champs.put("FIELD_DETAIL_IMPOT", document.getTextes(3).getTexte(12).getDescription());

                    champs.put("FIELD_MONTANT_IMPOT", PRStringUtils.replaceString(document.getTextes(3).getTexte(22)
                            .getDescription(), "{montantImpot}",
                            JANumberFormatter.formatNoRound(totalMontantImpotSource.toString())));

                    totalImpotSource.add(totalMontantImpotSource.toString());
                }

                if (state == IJDecomptes.STATE_VENTILATION) {
                    total.add(repartition.getMontantVentile());
                } else {
                    // ajout du montant brut moins les cotisations au total
                    total.add(repartition.getMontantNet());
                }

                // --------------------------------------------------------------------------------------------------------
                // les lignes pour les montants ventilés (si nécessaire)
                // --------------------------------------------------
                List<IJRepartitionJointPrestation> repartitionsEnfants = decompteCourant
                        .getRepartitionsEnfants(repartition);

                if (repartitionsEnfants != null) {

                    int nbVentilations = 0;

                    for (IJRepartitionJointPrestation uneRepartitionEnfant : repartitionsEnfants) {

                        nbVentilations++;

                        ITITiers tiersNom = (ITITiers) getSession().getAPIFor(ITITiers.class);
                        Hashtable<String, String> params = new Hashtable<String, String>();
                        params.put(ITITiers.FIND_FOR_IDTIERS, uneRepartitionEnfant.getIdTiersAdressePaiement());
                        ITITiers[] t = tiersNom.findTiers(params);
                        if ((t != null) && (t.length > 0)) {
                            tiersNom = t[0];
                        }

                        String nom = "";

                        if (!JadeStringUtil.isEmpty(tiersNom.getDesignation1())) {
                            nom = tiersNom.getDesignation1();
                        }
                        if (!JadeStringUtil.isEmpty(tiersNom.getDesignation2())) {
                            if (!JadeStringUtil.isEmpty(nom)) {
                                nom = nom + " " + tiersNom.getDesignation2();
                            } else {
                                nom = tiersNom.getDesignation2();
                            }
                        }
                        if (!JadeStringUtil.isEmpty(tiersNom.getDesignation3())) {
                            if (!JadeStringUtil.isEmpty(nom)) {
                                nom = nom + " " + tiersNom.getDesignation3();
                            } else {
                                nom = tiersNom.getDesignation3();
                            }
                        }
                        if (!JadeStringUtil.isEmpty(tiersNom.getDesignation4())) {
                            if (!JadeStringUtil.isEmpty(nom)) {
                                nom = nom + " " + tiersNom.getDesignation4();
                            } else {
                                nom = tiersNom.getDesignation4();
                            }
                        }

                        champs.put("FIELD_DETAIL_VENTILATIONS_" + nbVentilations, PRStringUtils.replaceString(document
                                .getTextes(3).getTexte(6).getDescription(), "{ventilation}", nom));

                        champs.put("FIELD_MONTANT_VENTILATIONS_" + nbVentilations, PRStringUtils.replaceString(document
                                .getTextes(3).getTexte(24).getDescription(), "{montantVentilation}", "-"
                                + JANumberFormatter.formatNoRound(uneRepartitionEnfant.getMontantVentile())));
                        total.sub(uneRepartitionEnfant.getMontantVentile());
                        totalMontantVentile.sub(uneRepartitionEnfant.getMontantVentile());
                    }
                }

                // --------------------------------------------------------------------------------------------------------
                // le total
                // -----------------------------------------------------------------------------------------------
                champs.put("FIELD_TOTAL_REPARTITION", document.getTextes(3).getTexte(7).getDescription());

                champs.put("FIELD_MONTANT_REPARTITION", PRStringUtils.replaceString(document.getTextes(3).getTexte(18)
                        .getDescription(), "{montantTotal}", JANumberFormatter.formatNoRound(total.toString())));

                // --------------------------------------------------------------------------------------------------------
                // les remarques
                // -------------------------------------------------------------------------------
                if (!JadeStringUtil.isEmpty(repartition.getRemarque())) {

                    champs.put("FIELD_REMARQUE_VAL", PRStringUtils.replaceString(document.getTextes(3).getTexte(19)
                            .getDescription(), "{remarque}", repartition.getRemarque()));
                }

                lignes.add(champs);
                grandTotal.add(total);

            }

            champs = new HashMap<String, String>();

            // --------------------------------------------------------------------------------------------------------
            // les factures à compenser
            // -------------------------------------------------------------------------------
            List<IJFactureACompenser> facturesACommpenser = decompteCourant.getFacturesACompenser();

            if ((facturesACommpenser != null) && !facturesACommpenser.isEmpty()) {
                // BZ 6307
                // Liste des compensations avec numéro de facture
                ArrayList<IJFactureACompenser> listeFacturesAvecNumero = new ArrayList<IJFactureACompenser>();
                // Liste des compensation sans numéro de facture
                ArrayList<IJFactureACompenser> listeFacturesSansNumero = new ArrayList<IJFactureACompenser>();
                // Buffer utilisé pour mémoriser le texte de la dernière ligne des compensations avec num de facture
                // Dans le cas ou elles ne peuvent pas toutes êtres affichées de manière standart
                StringBuffer bufferDernierLigneFactureAvecNum = new StringBuffer();
                // Même que ci-dessus mais pour le montant à afficher
                FWCurrency totalDernierLigneFactureAvecNum = new FWCurrency();
                // Utiliser pour déterminer si j'ai atteint la place maximum d'affichage de la dernière ligne des
                // compensations avec num de facture
                boolean isTropFacture = false;

                // BZ 6307
                // Je parcours une première fois la liste des factures à compenser pour les séparer dans deux listes :
                // La première contenant les compensations avec un numéro de facture et la deuxième pour les
                // compensation sans numéro de facture
                for (IJFactureACompenser facture : facturesACommpenser) {
                    if (JadeStringUtil.isEmpty(facture.getNoFacture()) || facture.getNoFacture().equals("0")) {
                        listeFacturesSansNumero.add(facture);
                    } else {
                        listeFacturesAvecNumero.add(facture);
                    }
                }

                // BZ 6307
                // Comme le modèle nous limite à 5 lignes pour les compensations, je détermine le nombre à disposition
                // pour les compensations avec un numéro de facture en fonction de la présence ou non de compensation
                // sans num de facture

                // Nombre max de ligne à disposition des compensations avec num de facture
                int nbLignesFactureAvecNum = 0;

                if (!listeFacturesSansNumero.isEmpty()) {
                    if (listeFacturesAvecNumero.size() >= 4) {
                        nbLignesFactureAvecNum = 4;
                    } else {
                        nbLignesFactureAvecNum = listeFacturesAvecNumero.size();
                    }
                } else {
                    nbLignesFactureAvecNum = 5;
                }

                // Num de la ligne que je suis en train de traiter
                int numLigneFactureAvecNum = 0;
                // BZ 6307
                // Je commence par traiter le compensation avec num de facture
                for (IJFactureACompenser facture : listeFacturesAvecNumero) {
                    numLigneFactureAvecNum++;

                    // si le numéro de ligne est égale au nombre max de ligne à disposition pour les compensations avec
                    // num de facture, je détermine si je dois l'afficher de manière standard ou concaténer plusieurs
                    // compensation sur la même ligne
                    boolean isTraitementStandart = true;
                    if ((numLigneFactureAvecNum == nbLignesFactureAvecNum)
                            && (nbLignesFactureAvecNum < listeFacturesAvecNumero.size())) {
                        isTraitementStandart = false;
                    }

                    if ((numLigneFactureAvecNum <= nbLignesFactureAvecNum) && isTraitementStandart) {
                        // Affichage standard
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(25).getDescription(),
                                "{compSurFacture}", facture.getNoFacture()));

                        champs.put("FIELD_COMPENSATION_" + numLigneFactureAvecNum, buffer.toString());
                        if (Double.parseDouble(facture.getMontant()) < 0) {

                            champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecNum, PRStringUtils
                                    .replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                            "{montantCompensation}",
                                            JANumberFormatter.formatNoRound(facture.getMontant())));

                        } else {

                            champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecNum, PRStringUtils
                                    .replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                            "{montantCompensation}",
                                            "-" + JANumberFormatter.formatNoRound(facture.getMontant())));

                        }

                    } else {
                        // Affichage concaténé
                        // Si j'ai atteint le nombre max de ligne
                        if (numLigneFactureAvecNum == nbLignesFactureAvecNum) {
                            // Je récupère une seul fois le texte à afficher
                            bufferDernierLigneFactureAvecNum.append(PRStringUtils.replaceString(document.getTextes(3)
                                    .getTexte(43).getDescription(), "{compSurFacture}", facture.getNoFacture()));
                        } else if (numLigneFactureAvecNum <= (nbLignesFactureAvecNum + 4)) {
                            // comme le texte a été récupéré ci-dessus, j'y ajoute le numéro de facture (max 4 num de
                            // facture)
                            bufferDernierLigneFactureAvecNum.append("," + facture.getNoFacture());
                        } else {
                            // je détermine si j'ai déjà atteint ce point
                            if (!isTropFacture) {
                                // Si ce n'est pas le cas, et qu'il n'y a plus la place d'afficher des num de facture,
                                // j'affiche ",..."
                                bufferDernierLigneFactureAvecNum.append(",...");
                                isTropFacture = true;
                            }
                        }
                        // Je calcul le total de la dernière ligne des compensations à num de facture dans le cas d'un
                        // affichage non standard
                        totalDernierLigneFactureAvecNum.sub(facture.getMontant());

                    }
                    // Je calcul le total des compensations
                    totalCompensations.sub(facture.getMontant());
                }

                // BZ 6307 Dans le cas d'un affichage non standard, j'insere la dernière ligne
                if (!JadeStringUtil.isEmpty(bufferDernierLigneFactureAvecNum.toString())) {
                    champs.put("FIELD_COMPENSATION_" + nbLignesFactureAvecNum,
                            bufferDernierLigneFactureAvecNum.toString());
                    if (Double.parseDouble(totalDernierLigneFactureAvecNum.toString()) < 0) {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + nbLignesFactureAvecNum, PRStringUtils.replaceString(
                                document.getTextes(3).getTexte(26).getDescription(), "{montantCompensation}",
                                JANumberFormatter.formatNoRound(totalDernierLigneFactureAvecNum.toString())));

                    } else {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + nbLignesFactureAvecNum, PRStringUtils.replaceString(
                                document.getTextes(3).getTexte(26).getDescription(), "{montantCompensation}", "-"
                                        + JANumberFormatter.formatNoRound(totalDernierLigneFactureAvecNum.toString())));

                    }
                }

                // BZ 6307
                // Traitement de la liste des compensations sans num de facture
                if (!listeFacturesSansNumero.isEmpty()) {
                    // Dans le cas ou il y a des compensations sans num de factures, une seule ligne sera affichée et le
                    // montant sera la résultat de l'addition de toutes les compensations sans num de factures.
                    FWCurrency totalFactureNumZero = new FWCurrency(0);
                    for (IJFactureACompenser facture : listeFacturesSansNumero) {
                        // Je calcul le total des compensations sans num de facture
                        totalFactureNumZero.sub(facture.getMontant());
                        // Je calcul le total des compensations
                        totalCompensations.sub(facture.getMontant());
                    }

                    // Je détermine le num de la ligne en fonction du nombre de ligne utilisé par les compensations avec
                    // num de facture
                    int numLigneFactureAvecZero = 0;
                    if (listeFacturesAvecNumero.size() >= 4) {
                        numLigneFactureAvecZero = 5;
                    } else {
                        numLigneFactureAvecZero = listeFacturesAvecNumero.size() + 1;
                    }

                    champs.put("FIELD_COMPENSATION_" + numLigneFactureAvecZero, document.getTextes(3).getTexte(8)
                            .getDescription());

                    // On doit inverser le signe des factures à compenser.
                    if (Double.parseDouble(totalFactureNumZero.toString()) < 0) {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecZero, PRStringUtils
                                .replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                        "{montantCompensation}",
                                        JANumberFormatter.formatNoRound(totalFactureNumZero.toString())));

                    } else {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecZero, PRStringUtils
                                .replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                        "{montantCompensation}",
                                        "-" + JANumberFormatter.formatNoRound(totalFactureNumZero.toString())));

                    }
                }

                grandTotal.add(totalCompensations);

            }
            if (grandTotal.isNegative() || grandTotal.isZero()) {
                champs.put("FIELD_TOTAL_FINAL", document.getTextes(3).getTexte(11).getDescription());
            } else {
                champs.put("FIELD_TOTAL_FINAL", document.getTextes(3).getTexte(9).getDescription());
            }

            champs.put("FIELD_MONTANT_FINAL", PRStringUtils.replaceString(document.getTextes(3).getTexte(27)
                    .getDescription(), "{montantFinal}", JANumberFormatter.formatNoRound(grandTotal.toString())));

            restitution = grandTotal.isNegative();

            if (grandTotal.isPositive()) {

                AdressePaiementDecompteIJPojo adressePaiementDecompteIJPojo = construitAdressePaiementDecompteIJPojo(idDomaineAdressePaiement);

                champs.put("FIELD_TEXTE_ADRESSE_PAIEMENT", adressePaiementDecompteIJPojo.getLibelle());
                champs.put("FIELD_ADRESSE_PAIEMENT", adressePaiementDecompteIJPojo.getAdressePaiement());

            }

            // Récapitulatif si plusieurs décomptes et UNIQUEMENT pour certains caisses
            if (!JadeStringUtil.isBlankOrZero(decompteCourant.getIdAffilie())) {

                if ("true".equals(getSession().getApplication().getProperty(
                        IJApplication.PROPERTY_IS_RECAPITULATIF_DECOMPTE))) {

                    if (nbDecompte > 1) {

                        if (!totalIJ.isZero()) {

                            champs.put("FIELD_RECAP", document.getTextes(3).getTexte(28).getDescription());
                            champs.put("FIELD_RECAP_APG", document.getTextes(3).getTexte(29).getDescription());
                            champs.put("FIELD_MONTANT_RECAP_APG", PRStringUtils.replaceString(document.getTextes(3)
                                    .getTexte(30).getDescription(), "{montantIJAIBrut}", totalIJ.toString()));
                        }

                        if (!totalCotisations.isZero()) {
                            champs.put("FIELD_RECAP_COTISATIONS", document.getTextes(3).getTexte(31).getDescription());
                            champs.put("FIELD_MONTANT_RECAP_COTISATIONS", PRStringUtils.replaceString(document
                                    .getTextes(3).getTexte(32).getDescription(), "{montantCotisations}",
                                    totalCotisations.toString()));
                        }

                        if (!totalImpotSource.isZero()) {
                            champs.put("FIELD_RECAP_IMPOT", document.getTextes(3).getTexte(35).getDescription());
                            champs.put("FIELD_MONTANT_RECAP_IMPOT", PRStringUtils.replaceString(document.getTextes(3)
                                    .getTexte(36).getDescription(), "{impotMontant}", totalImpotSource.toString()));
                        }

                        if (!totalMontantVentile.isZero()) {
                            champs.put("FIELD_RECAP_VENTILATION", document.getTextes(3).getTexte(37).getDescription());
                            champs.put("FIELD_MONTANT_RECAP_VENTILATION", PRStringUtils.replaceString(document
                                    .getTextes(3).getTexte(38).getDescription(), "{ventilationsMontant}",
                                    totalMontantVentile.toString()));
                        }

                        if (!totalCompensations.isZero()) {
                            champs.put("FIELD_RECAP_COMPENSATION", document.getTextes(3).getTexte(33).getDescription());
                            champs.put("FIELD_MONTANT_RECAP_COMPENSATION", PRStringUtils.replaceString(document
                                    .getTextes(3).getTexte(34).getDescription(), "{compensationsMontant}",
                                    totalCompensations.toString()));
                        }

                        if (grandTotal.isNegative() || grandTotal.isZero()) {
                            champs.put("FIELD_TOTAL_FINAL_RECAP", document.getTextes(3).getTexte(11).getDescription());
                        } else {
                            champs.put("FIELD_TOTAL_FINAL_RECAP", document.getTextes(3).getTexte(9).getDescription());
                        }

                        champs.put("FIELD_MONTANT_FINAL_RECAP", PRStringUtils.replaceString(document.getTextes(3)
                                .getTexte(39).getDescription(), "{montantFinalRecap}",
                                JANumberFormatter.formatNoRound(grandTotal.toString())));

                    }
                }

            }

            lignes.add(champs);
            this.setDataSource(lignes);

            // on imprime tous les décomptes
            grandTotalEgalZero = false;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJDecomptes");
            abort();
        }
    }

    /**
     * Défini les propriétés du JadePublishDocumentInfo pour archivage du document dans la GED
     */
    public void createDocInfoIjDecomptes() {
        JadePublishDocumentInfo docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        try {
            String anneeDecompte = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
            // on défini les propriétés du DocInfo pour l'archivage uniquement
            // - pour les client qui possèdent une GED
            // - pour les décomptes définitifs ou les décomptes non-définitif pour lesquels on force l'envoi a la GED
            docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_IJ_TITLE"));
            docInfo.setDocumentDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDate().toStrAMJ()));
            docInfo.setDocumentProperty("annee", anneeDecompte);

            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), decompteCourant.getIdTiers());

            if (decompteCourant.isEmployeur()) {

                docInfo.setDocumentType(docInfo.getDocumentType() + "Aff");
                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);

                if (affilie != null) {
                    // Employeur affilié --> rôle AFFILIE
                    TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            affilie.getNumAffilie(), JadeStringUtil.removeChar(affilie.getNumAffilie(), '.'));

                } else {
                    // Employeur non affilié --> rôle NON_AFFILIE
                    String naffFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                    String naffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(naffFormatte);

                    TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(),
                            IPRConstantesExternes.OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE, naffFormatte, naffNonFormatte);

                }

                // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // pour la CCJU, les documents envoyés aux employeurs doivent être indexés uniquement avec le n°
                // d'affilié
                // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (IJApplication.NO_CAISSE_CCJU.equals(PRAbstractApplication.getApplication(
                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(CommonProperties.KEY_NO_CAISSE))) {
                    docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
                }

            } else {
                // Assure --> rôle IJAI

                String noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                String noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);

                if (affilie != null) {
                    docInfo.setDocumentType(docInfo.getDocumentType() + "Aff");
                    docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);
                    noAff = affilie.getNumAffilie();
                    noAffNonFormatte = JadeStringUtil.removeChar(affilie.getNumAffilie(), '.');

                    if (JadeStringUtil.isBlank(noAff)) {
                        noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                        noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);
                    }

                    docInfo.setDocumentProperty("numero.affilie.formatte", noAff);
                } else {
                    docInfo.setDocumentType(docInfo.getDocumentType() + "Ass");
                    docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);
                }
                TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(), IntRole.ROLE_IJAI,
                        noAff, noAffNonFormatte);

            }

            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, documentHelper.getCsTypeDocument());

        } catch (RemoteException e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        }
    }

    /**
     * Crée tous les décomptes en chargeant toutes les repartitions pour l'état courant.<br />
     * Cette méthode sert juste à re-diriger sur les méthodes spécifiques à chaque type de document.
     * 
     * @throws FWIException
     * 
     * @see #creerDecomptesNormal()
     * @see #creerDecomptesVentilations()
     */
    private void creerDecomptes() throws FWIException {
        switch (state) {
            case STATE_NORMAL:
                creerDecomptesNormal();
                break;

            case STATE_VENTILATION:
                creerDecomptesVentilations();
                break;

            default:
                throw new FWIException("erreur dans le cycle de vie de la génération des décomptes");
        }
    }

    /**
     * Crée les décomptes en chargeant toutes les repartitions pour les prestations d'un genre donné.
     * 
     * @throws FWIException
     */
    private void creerDecomptesNormal() throws FWIException {
        try {
            // chargement des repartitions pour ce lot
            IJRepartitionJointPrestationManager repartitionsMgr = new IJRepartitionJointPrestationManager();
            IJRepartitionJointPrestationManager ventilations = new IJRepartitionJointPrestationManager();
            IJFactureACompenserManager factures = new IJFactureACompenserManager();

            factures.setSession(getSession());
            ventilations.setSession(getSession());
            repartitionsMgr.setSession(getSession());

            repartitionsMgr.setForIdLot(getIdLot());
            repartitionsMgr.setForParentOnly(Boolean.TRUE.toString());
            repartitionsMgr.setOrderBy(IJRepartitionPaiements.FIELDNAME_IDTIERS + ","
                    + IJRepartitionPaiements.FIELDNAME_IDAFFILIE + "," + IJPrestation.FIELDNAME_DATEDEBUT + ","
                    + IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
            repartitionsMgr.find(BManager.SIZE_NOLIMIT);

            // classer les repartitions par bénéficiaire
            Map<String, Decompte> repartitions = new HashMap<String, IJDecomptes.Decompte>();

            for (int idRepartition = 0; idRepartition < repartitionsMgr.size(); ++idRepartition) {
                IJRepartitionJointPrestation repartition = (IJRepartitionJointPrestation) repartitionsMgr
                        .get(idRepartition);

                // Il est possible que le montant brut à verser au bénéficiaire principal soit égal à zéro. Dans ce cas,
                // on ne l'imprime pas sur le décompte, et on passe à l'itération suivante.
                if (JadeStringUtil.isIntegerEmpty(repartition.getMontantBrut())
                        && JadeStringUtil.isIntegerEmpty(repartition.getMontantVentile())) {
                    continue;
                }

                Decompte decompte = getDecompte(repartitions, repartition);

                // ajouter la repartition courante.
                decompte.addRepartitionPere(repartition);

                // ajouter les ventilations de montant
                ventilations.setForIdParent(repartition.getIdRepartitionPaiement());
                ventilations.find(BManager.SIZE_NOLIMIT);

                for (int idVentilation = 0; idVentilation < ventilations.size(); ++idVentilation) {
                    decompte.addRepartitionEnfant((IJRepartitionJointPrestation) ventilations.get(idVentilation));
                }

                // ajouter les factures à compenser pour cette repartition
                if (!JadeStringUtil.isIntegerEmpty(repartition.getIdCompensation())) {
                    factures.setForIdCompensation(repartition.getIdCompensation());
                    factures.find(BManager.SIZE_NOLIMIT);

                    for (int idCompensation = 0; idCompensation < factures.size(); ++idCompensation) {
                        decompte.addFactureACompenser((IJFactureACompenser) factures.get(idCompensation));
                    }
                }

            }

            decomptesCollection = repartitions.values();
        } catch (Exception e) {
            throw new FWIException("impossible de créer les décomptes", e);
        }
    }

    /**
     * Crée les décomptes en chargeant tous les montants ventilés pour les prestations.
     * 
     * @throws FWIException
     */
    private void creerDecomptesVentilations() throws FWIException {
        try {
            // chargement des repartitions pour ce lot
            IJRepartitionJointPrestationManager repartitionsMgr = new IJRepartitionJointPrestationManager();
            IJRepartitionJointPrestationManager ventilations = new IJRepartitionJointPrestationManager();
            IJFactureACompenserManager factures = new IJFactureACompenserManager();

            factures.setSession(getSession());
            ventilations.setSession(getSession());
            repartitionsMgr.setSession(getSession());

            repartitionsMgr.setForIdLot(getIdLot());
            repartitionsMgr.setForParentOnly(Boolean.TRUE.toString());
            repartitionsMgr.setOrderBy(IJRepartitionPaiements.FIELDNAME_IDTIERS + ","
                    + IJRepartitionPaiements.FIELDNAME_IDAFFILIE + "," + IJPrestation.FIELDNAME_DATEDEBUT + ","
                    + IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
            repartitionsMgr.find(BManager.SIZE_NOLIMIT);

            // classer les montants ventilés par bénéficiaire des ventilations
            Map<String, Decompte> repartitions = new HashMap<String, IJDecomptes.Decompte>();

            for (int idRepartition = 0; idRepartition < repartitionsMgr.size(); ++idRepartition) {
                IJRepartitionJointPrestation repartition = (IJRepartitionJointPrestation) repartitionsMgr
                        .get(idRepartition);

                // ajouter les ventilations de montant
                ventilations.setForIdParent(repartition.getIdRepartitionPaiement());
                ventilations.find(BManager.SIZE_NOLIMIT);

                for (int idVentilation = 0; idVentilation < ventilations.size(); ++idVentilation) {
                    IJRepartitionJointPrestation ventilation = (IJRepartitionJointPrestation) ventilations
                            .get(idVentilation);
                    Decompte decompte = getDecompte(repartitions, ventilation);
                    decompte.addRepartitionPere((IJRepartitionJointPrestation) ventilations.get(idVentilation));
                }
            }

            decomptesCollection = repartitions.values();
        } catch (Exception e) {
            throw new FWIException("impossible de créer les décomptes", e);
        }
    }

    public JADate getDate() {
        return date;
    }

    /**
     * Renvoie le décompte pour le bénéficiaire de la prestation donnée.
     * 
     * @param decomptes
     *            une map contenant les décomptes déjà créés.
     * @param repartition
     *            la repartition dont on veut trouver le décompte
     */
    private Decompte getDecompte(Map<String, Decompte> decomptes, IJRepartitionJointPrestation repartition)
            throws Exception {
        // identifiant du bénéficiaire
        String cle = repartition.getIdTiers() + "_" + repartition.getIdAffilie() + "_"
                + !JadeStringUtil.isIntegerEmpty(repartition.getIdSituationProfessionnelle());

        // retourne décompte
        Decompte retValue = decomptes.get(cle);

        if (retValue == null) {
            retValue = new Decompte(repartition);
            decomptes.put(cle, retValue);
        }

        return retValue;
    }

    public String getIdLot() {
        return idLot;
    }

    public Boolean getIsDecompteDefinitif() {
        return isDecompteDefinitif;
    }

    public Boolean getIsSendToGED() {
        return isSendToGED;
    }

    private boolean isCaisse(String noCaisse) throws Exception {
        return noCaisse.equals(PRAbstractApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                CommonProperties.KEY_NO_CAISSE));
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            hasNext = false;

            if (state == IJDecomptes.STATE_DEBUT) {
                // au début, on charge les décomptes normaux
                state = IJDecomptes.STATE_NORMAL;
                creerDecomptes();
                iterateurDecomptes = decomptesCollection.iterator();
            }

            if (iterateurDecomptes.hasNext()) {
                // il y a encore des bénéficiaires de paiement
                hasNext = true;
            } else {
                // il n'y a plus de bénéficiaire, on passe au type de document suivant
                do {
                    nextState();

                    if (state != IJDecomptes.STATE_FIN) {
                        creerDecomptes();
                        iterateurDecomptes = decomptesCollection.iterator();
                        hasNext = iterateurDecomptes.hasNext();
                    }
                    // tant qu'il n'y a pas de bénéficiaire et qu'on a pas fini.
                } while (!hasNext && (state != IJDecomptes.STATE_FIN));
            }

            if (hasNext) {

                // on charge le document courant
                decompteCourant = iterateurDecomptes.next();
            }

            return hasNext;
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJDecomptes");
            abort();

            return false;
        }
    }

    /** passe a l'etat suivant. */
    private void nextState() {
        switch (state) {
            case STATE_DEBUT:
                state = IJDecomptes.STATE_NORMAL;

                break;

            case STATE_NORMAL:
                state = IJDecomptes.STATE_VENTILATION;

                break;

            default:
                state = IJDecomptes.STATE_FIN;

                break;
        }
    }

    private JadePublishDocumentInfo remplirDocInfoPourAssureIJAISpecifiqueCVCI(JadePublishDocumentInfo docInfoUnitaire)
            throws Exception {

        if (getIsSendToGED().booleanValue()) {
            docInfoUnitaire.setArchiveDocument(true);
            docInfoUnitaire.setPublishDocument(false);
        }
        docInfoUnitaire.setDocumentType(docInfoUnitaire.getDocumentType() + "Aff");
        docInfoUnitaire.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_IJ);
        docInfoUnitaire.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_IJ_TITLE"));
        docInfoUnitaire.setDocumentDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDate().toStrAMJ()));
        docInfoUnitaire.setDocumentProperty("annee", JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString());

        // on cherche tous les employeurs de ce bénéficiaire pour cela on utilise les situation prof. du
        // droit des prestations du décompte courant
        Map<String, IJEmployeur> employeurs = new HashMap<String, IJEmployeur>();

        for (IJRepartitionJointPrestation prest : decompteCourant.getRepartitionsPeres()) {
            // on cherche les sit. prof. de ce droit
            IJSituationProfessionnelleManager sitProMan = new IJSituationProfessionnelleManager();
            sitProMan.setSession(getSession());
            sitProMan.setForIdPrononce(prest.getIdPrononce());
            sitProMan.find();

            for (int i = 0; i < sitProMan.size(); i++) {

                IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) sitProMan.get(i);

                IJEmployeur employeur = new IJEmployeur();
                employeur.setSession(getSession());
                employeur.setIdEmployeur(sitPro.getIdEmployeur());
                employeur.retrieve();

                // on ne considère que les employeurs qui on un n°affilie
                if (!JadeStringUtil.isIntegerEmpty(employeur.getIdAffilie())) {

                    // on ne considère que les employeur qui ont droit aux prestations qui correspondent au
                    // type de document que l'on génère
                    if (state == IJDecomptes.STATE_NORMAL) {

                        if (!employeurs.containsKey(employeur.getIdAffilie())) {
                            employeurs.put(employeur.getIdAffilie(), employeur);
                        }
                    }
                } else {
                    if (isSendToGED) {

                        // Si n°AVS est vide, le remplacer par des '0'
                        String avsnf = docInfoUnitaire
                                .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                        String avsf = docInfoUnitaire
                                .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

                        if (JadeStringUtil.isBlankOrZero(avsnf) || JadeStringUtil.isBlankOrZero(avsf)) {
                            docInfoUnitaire = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(),
                                    docInfoUnitaire);
                        }

                        // on ajoute au doc info le critère de tri pour les impressions ORDER_PRINTING_BY
                        // toujours selon le décompte courant (Assuré)
                        docInfoUnitaire.setDocumentProperty(IJDecomptes.ORDER_PRINTING_BY,
                                buildOrderPrintingByKey(decompteCourant.getIdAffilie(), decompteCourant.getIdTiers()));

                        docInfoUnitaire.setPublishDocument(false);
                        super.registerAttachedDocument(docInfoUnitaire, getExporter().getExportNewFilePath());
                    }
                }
            }

            // on envoie un copie pour chacun des employeurs sélectionnés
            for (IJEmployeur affiliecourant : employeurs.values()) {

                if (affiliecourant != null) {
                    IPRAffilie aff = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                            affiliecourant.getIdAffilie(), affiliecourant.getIdTiers());

                    if (!JadeStringUtil.isIntegerEmpty(affiliecourant.getIdAffilie())) {

                        // Employeur affilié --> rôle AFFILIE

                        if (aff != null) {

                            IFormatData affilieFormatte = ((AFApplication) GlobazServer.getCurrentSystem()
                                    .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                            // BZ8975 : correction null pointer
                            TIDocumentInfoHelper.fill(docInfoUnitaire, affiliecourant.getIdTiers(), getSession(),
                                    ITIRole.CS_AFFILIE, aff.getNumAffilie(),
                                    affilieFormatte.unformat(aff.getNumAffilie()));

                            // Si n°AVS est vide, le remplacer par des '0'
                            String avsnf = docInfoUnitaire
                                    .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                            String avsf = docInfoUnitaire
                                    .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

                            if (JadeStringUtil.isBlankOrZero(avsnf) || JadeStringUtil.isBlankOrZero(avsf)) {
                                docInfoUnitaire = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(),
                                        docInfoUnitaire);
                            }

                            // on ajoute au doc info le critère de tri pour les impressions
                            // ORDER_PRINTING_BY toujours selon le décompte courant (Assuré)
                            docInfoUnitaire.setDocumentProperty(
                                    IJDecomptes.ORDER_PRINTING_BY,
                                    buildOrderPrintingByKey(decompteCourant.getIdAffilie(),
                                            decompteCourant.getIdTiers()));

                            // on ajoute le document à la liste des documents attachés
                            docInfoUnitaire.setPublishDocument(false);
                            super.registerAttachedDocument(docInfoUnitaire, getExporter().getExportNewFilePath());
                        }
                    }
                }
            }
        }
        getDocumentInfo().setArchiveDocument(false);
        getDocumentInfo().setPublishDocument(false);

        return docInfoUnitaire;
    }

    public void setDate(JADate date) {
        this.date = date;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsDecompteDefinitif(Boolean boolean1) {
        isDecompteDefinitif = boolean1;
    }

    public void setIsSendToGED(Boolean boolean1) {
        isSendToGED = boolean1;
    }
}
