package globaz.apg.itext.decompte;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.safehaus.uuid.Logger;
import globaz.apg.ApgServiceLocator;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APCotisationManager;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.groupdoc.ccju.GroupdocPropagateUtil;
import globaz.apg.itext.decompte.utils.APDecompte;
import globaz.apg.itext.decompte.utils.APPrestationLibelleCodeSystem;
import globaz.apg.itext.decompte.utils.APTypeDeDecompte;
import globaz.apg.properties.APProperties;
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
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.external.IntRole;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.enums.ged.PRGedProperties;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRBlankBNumberFormater;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.webavs.common.CommonProperties;

/**
 * Cette classe abstraite s'ocupe de la génération des documents de décomptes</br>
 * La préparation des données
 * (regroupement, ventilations, catalogue de textes, etc) doit être réalisé par la classe mère.</br>
 * Les différentes
 * informations sont récupérées depuis la classe mère via les différentes méthodes abstraites définies dans cette classe
 * </br>
 *
 * @author lga
 */
public abstract class APAbstractDecomptesGenerationProcess extends FWIDocumentManager {

    private static final String DETAIL_COTISATIONS_FNE = "FIELD_DETAIL_COTISATIONS_FNE";
    private static final String FICHIER_MODELE = "AP_DECOMPTE";
    private static final String MONTANT_COTISATIONS_FNE = "FIELD_MONTANT_COTISATIONS_FNE";
    private static final String ORDER_PRINTING_BY = "orderPrintingBy";
    private static final String PARAMETER_PRESTATION_COMPLEMENTAIRE = "FIELD_PREST_COMPL";
    private static final long serialVersionUID = -6053653641306384554L;

    private static final String NB_JOURS_DECES = "0.5";

    private ICaisseReportHelper caisseHelper;
    private APDecompte decompteCourant;
    private JadePublishDocumentInfo docInfo = getDocumentInfo();
    private ICTDocument document;
    private String domaineDePaiement = "";
    private boolean hasNextDocument = false;
    private boolean restitution = false;

    /**
     * Retourne le code système du type de prestation contenue dans le lot
     *
     * @return le code système du type de prestation contenue dans le lot
     */
    public abstract String getCSTypePrestationsLot();

    /**
     * Retourne le code ISO de la langue à utiliser pour le décompte courant
     *
     * @return le code ISO de la langue à utiliser pour le décompte courant
     */
    public abstract String getCodeIsoLangue();

    /**
     * Retourne la date comptable
     *
     * @return la date comptable
     */
    public abstract JADate getDateComptable();

    /**
     * Retourne la date de publication du document
     *
     * @return la date de publication du document
     */
    public abstract JADate getDateDocument();

    /**
     * Défini les propriétés du JadePublishDocumentInfo et génère le fichier PDF pour l'impression
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void afterExecuteReport() {
        if (!hasNextDocument) {

            if (getAttachedDocuments().size() > 0) {

                try {
                    // si CVCI et impression definitive
                    if (APApplication.NO_CAISSE_CVCI
                            .equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                                    .getProperty(CommonProperties.KEY_NO_CAISSE))
                            && (getIsSendToGED())) {

                        // on enlève de la liste les éléments qui ne sont pas archivé, ni publié
                        final Iterator<JadePublishDocument> iter = getAttachedDocuments().iterator();
                        while (iter.hasNext()) {
                            final JadePublishDocument doc = iter.next();
                            final JadePublishDocumentInfo dInf = doc.getPublishJobDefinition().getDocumentInfo();

                            if (!dInf.getArchiveDocument() && !dInf.getPublishDocument()) {
                                iter.remove();
                            }
                        }
                    }

                    // on défini les propriétés du DocInfo pour envoyer le mail
                    setSendCompletionMail(false);
                    final JadePublishDocumentInfo docInfo = createDocumentInfo();
                    docInfo.setPublishDocument(true);
                    docInfo.setArchiveDocument(false);
                    docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
                    docInfo.setDocumentTitle("L'impression des documents de décompte s'est terminée avec succès");

                    String annee = "";
                    if (!JadeStringUtil.isEmpty(getDateDocument().toString())) {
                        annee = JADate.getYear(getDateDocument().toString()).toString();
                    } else {
                        annee = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
                    }
                    docInfo.setDocumentProperty(PRGedProperties.ANNEE.getPropertyName(), annee);
                    docInfo.setDocumentProperty(PRGedProperties.PYXIS_NO_AVS_NON_FORMATTE.getPropertyName(),
                            "7561111111111");

                    // Pour les décomptes définitifs et les client qui possèdent une GED
                    if (getIsSendToGED()) {
                        // on génère le doc pour impression (mail) et on défini les propriétés DocInfo
                        // on ne supprime pas les documents individuels car on doit les envoyer à la GED
                        // on trie les documents sur le critère "orderPrintBy"
                        this.mergePDF(docInfo, false, 0, false, APAbstractDecomptesGenerationProcess.ORDER_PRINTING_BY);
                    } else {
                        // on génère le doc pour impression (mail) et on défini les propriétés DocInfo
                        // on supprime les documents individuels car on ne les envoie pas à la GED
                        // on trie les documents sur le critère "orderPrintBy"
                        this.mergePDF(docInfo, true, 0, false, APAbstractDecomptesGenerationProcess.ORDER_PRINTING_BY);
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public final void afterPrintDocument() throws FWIException {
        docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        try {
            // on défini les propriétés du DocInfo pour l'archivage uniquement
            // - pour les client qui possèdent une GED
            // - pour les décomptes définitifs ou les décomptes non-définitif
            // pour lesquels on force l'envoi à la GED

            docInfo.setArchiveDocument(getIsSendToGED());

            // SI APG
            if (IPRDemande.CS_TYPE_APG.equals(getCSTypePrestationsLot())) {
                docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_APG_TITLE"));
                // SI MATERNITE
            } else {
                docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_MAT_TITLE"));
            }

            docInfo.setDocumentDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDateDocument().toStrAMJ()));

            final String yy = String.valueOf(getDateComptable().getYear()).substring(2, 4);
            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "A" + yy);

            String annee = "";
            if (!JadeStringUtil.isEmpty(getDateDocument().toString())) {
                annee = JADate.getYear(getDateDocument().toString()).toString();
            }
            if (!JadeStringUtil.isEmpty(annee)) {
                docInfo.setDocumentProperty("annee", annee);
            } else if (!JadeStringUtil.isEmpty(yy)) {
                docInfo.setDocumentProperty("annee", yy);
            } else {
                docInfo.setDocumentProperty("annee", JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString());
            }

            final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), decompteCourant.getIdTiers());
            PRTiersWrapper tiers = null;

            String idTiersPourLaGED = "";
            String rolePourLaGed = "";
            String noAffiliePourLaGED = "";

            if (decompteCourant.getIsPaiementEmployeur()) {

                // 2 noms de documents différents pour permettre une configuration différente si le document traite d'un
                // affilié ou d'un assuré
                docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Aff");

                // tiers pour la GED = employeur
                idTiersPourLaGED = decompteCourant.getIdTiers();

                if (!JadeStringUtil.isIntegerEmpty(decompteCourant.getIdAffilie())) {

                    // Employeur affilié --> rôle AFFILIE
                    rolePourLaGed = ITIRole.CS_AFFILIE;

                    if (affilie != null) {
                        noAffiliePourLaGED = affilie.getNumAffilie();
                    } else {
                        noAffiliePourLaGED = "";
                    }
                } else {
                    // Employeur non affilié --> rôle NON_AFFILIE
                    rolePourLaGed = IPRConstantesExternes.OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE;
                    noAffiliePourLaGED = "";

                    // Pour la CICICAM, on va rechercher le premier assuré dans ce décompte si employeur non affilié
                    if (isCaisse(APApplication.NO_CAISSE_CICICAM)) {
                        tiers = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());
                        idTiersPourLaGED = chercherIdTiersPourLigneTechinqueVersementATier(tiers);
                    }
                }
            } else {
                // Assure --> rôle APG

                if (isCaisse(APApplication.NO_CAISSE_CVCI)) {

                    JadePublishDocumentInfo docInfoCvci = docInfo.createCopy();

                    // pour la caisse CVCI les documents envoyé directement à l'assuré doivent être indexés sur le n°
                    // d'affilié du/des employeurs
                    docInfoCvci = remplirDocInfoPourAssureAPGSpecifiqueCVCI(docInfoCvci);

                } else {
                    // autres caisses que la CVCI

                    // Assure --> rôle APG
                    rolePourLaGed = IntRole.ROLE_APG;

                    tiers = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

                    if (affilie != null) {
                        docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Aff");

                        // Correction BZ 10007
                        // afin de se prémunir contre 22, 022 on transforme les valeurs en integer
                        int caisseCourante = Integer
                                .parseInt(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                                        .getProperty(CommonProperties.KEY_NO_CAISSE));

                        int ccvdConstante = Integer.parseInt(APApplication.NO_CAISSE_CCVD);
                        int agrivitConstante = Integer.parseInt(APApplication.NO_CAISSE_AGRIVIT);

                        boolean isCCVD = ccvdConstante == caisseCourante;
                        boolean isAGRIVIT = agrivitConstante == caisseCourante;

                        /*
                         * Si on génère les décomptes pour la CCVD ou AGRIVIT il faut renseigner l'id tiers
                         */
                        if (isCCVD || isAGRIVIT) {
                            idTiersPourLaGED = tiers.getIdTiers();
                        } else {
                            idTiersPourLaGED = "";
                        }

                        noAffiliePourLaGED = affilie.getNumAffilie();
                    } else {
                        docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Ass");

                        idTiersPourLaGED = chercherIdTiersPourLigneTechinqueVersementATier(tiers);
                        noAffiliePourLaGED = "";
                    }
                }
            }

            // pour la CCJU OU CICICAM, les documents envoyés aux employeurs doivent être indexés uniquement avec le n°
            // d'affilié
            if ((isCaisse(APApplication.NO_CAISSE_CCJU) || isCaisse(APApplication.NO_CAISSE_CICICAM))
                    && IntRole.ROLE_AFFILIE.equals(rolePourLaGed)) {
                docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
            }

            if (!isCaisse(APApplication.NO_CAISSE_CVCI)) {
                // on ajoute au doc info le critère de tri pour les impressions ORDER_PRINTING_BY
                docInfo.setDocumentProperty(APAbstractDecomptesGenerationProcess.ORDER_PRINTING_BY,
                        buildOrderPrintingByKey(decompteCourant.getIdAffilie(), decompteCourant.getIdTiers()));

                final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem()
                        .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                docInfo.setDocumentProperty("numero.affilie.formatte", noAffiliePourLaGED);

                TIDocumentInfoHelper.fill(docInfo, idTiersPourLaGED, getSession(), rolePourLaGed, noAffiliePourLaGED,
                        affilieFormatter.unformat(noAffiliePourLaGED));

                if (getIsSendToGED()) {
                    // création / mise à jour du dossier GroupDoc
                    GroupdocPropagateUtil.propagateData(affilie, tiers, null);
                }
            }

            // La gestion du NSS est différente selon la caisse, pour la mise en GED.
            // Création d'une propriété pour les caisses qui veulent le NSS vide lors de la mise en GED
            // Par défaut, l'absence de propriété ou si la propriété est à FALSE, le NSS sera remplacé par
            // 000.00.000.000

            if (!getIsBlankIndexGedNssAZero()) {
                final String avsnf = PRAbstractApplication.getAffileFormater()
                        .unformat(docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE));
                final String avsf = docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

                if (JadeStringUtil.isBlank(avsnf) || JadeStringUtil.isBlank(avsf) || (avsnf.equals("00000000000"))) {
                    // Si n°AVS est vide, le remplacer par des '0'
                    docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
                }
            }

            if (getIsNumeroAffiliePourGEDForceAZeroSiVide()) {
                final String noAffilie = docInfo.getDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE);

                if (JadeStringUtil.isBlank(noAffilie)) {
                    docInfo = PRBlankBNumberFormater.fillEmptyNoAffilie(docInfo);
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("APDecompte afterPrintDocument() : " + e.getMessage(), FWMessage.ERREUR,
                    this.getClass().getName());
        }
    }

    /** Remplit les différents corps du document avec le catalogue de texte. */
    @SuppressWarnings("unchecked")
    @Override
    public void beforeBuildReport() {

        try {
            Map<String, String> parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap<String, String>();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            /**
             * Récupération du nom du modèle à utiliser pour l'affichage des détails du décompte
             */
            String nomModeleDetailDecompte = decompteCourant.getTypeDeDecompte().getNomModeleDetailDecompte();
            if (JadeStringUtil.isEmpty(nomModeleDetailDecompte)) {
                throw new IllegalArgumentException("The name of the sub-report 'détail' is empty");
            }

            parametres.put("PARAM_AP_DECOMPTE_DETAIL",
                    JadeStringUtil
                            .change(getSession().getApplication().getExternalModelPath()
                                    + APApplication.APPLICATION_APG_REP, '\\', '/')
                            + "/" + "model" + "/" + nomModeleDetailDecompte);

            parametres.put("PARAM_AP_DECOMPTE_DETAIL2",
                    JadeStringUtil
                            .change(getSession().getApplication().getExternalModelPath()
                                    + APApplication.APPLICATION_APG_REP, '\\', '/')
                            + "/" + "model" + "/" + "AP_DECOMPTE_DETAIL2.jasper");

            // remplissage de l'entête
            final CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            String noAffilie = "";

            if (JadeStringUtil.isIntegerEmpty(decompteCourant.getIdAffilie())) {
                PRTiersWrapper tiers;
                String adresse;

                try {

                    tiers = PRTiersHelper.getTiersParId(getISession(), decompteCourant.getIdTiers());

                    if (tiers == null) {
                        tiers = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
                    }

                    adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), decompteCourant.getIdTiers(),
                            decompteCourant.getIdAffilie(),
                            IPRDemande.CS_TYPE_APG.equals(getCSTypePrestationsLot())
                                    ? IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG
                                    : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);

                } catch (final Exception e) {
                    throw new FWIException("impossible de charger le tiers", e);
                }

                if (isTraitementDesVentilations()) {
                    crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                }

                crBean.setAdresse(adresse);
                crBean.setNoAvs(tiers.getNSS());
                crBean.setDate(JACalendar.format(JACalendar.format(getDateDocument()), getCodeIsoLangue()));
                if (APProperties.PROPERTY_AFFICHER_TRAITE_PAR.getBooleanValue()) {
                    crBean.setNomCollaborateur(getSession().getUserFullName());
                }
                // nom du document
                setDocumentTitle(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            } else {

                String adresse;
                IPRAffilie affilie;
                try {
                    affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getISession(),
                            getSession().getCurrentThreadTransaction(), decompteCourant.getIdAffilie(),
                            decompteCourant.getIdTiers());
                    adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), decompteCourant.getIdTiers(),
                            decompteCourant.getIdAffilie(),
                            IPRDemande.CS_TYPE_APG.equals(getCSTypePrestationsLot())
                                    ? IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG
                                    : IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);

                    noAffilie = affilie.getNumAffilie();

                    // Renseignement du numéro ide
                    AFIDEUtil.addNumeroIDEInDoc(crBean, affilie.getNumeroIDE(), affilie.getIdeStatut());

                } catch (final Exception e) {
                    throw new FWIException("impossible de charger le tiers", e);
                }

                crBean.setNoAffilie(affilie.getNumAffilie());
                crBean.setAdresse(adresse);
                if (APProperties.PROPERTY_AFFICHER_TRAITE_PAR.getBooleanValue()) {
                    crBean.setNomCollaborateur(getSession().getUserFullName());
                }
                crBean.setDate(JACalendar.format(JACalendar.format(getDateDocument()), getCodeIsoLangue()));

                // nom du document
                setDocumentTitle(affilie.getNumAffilie() + " - " + affilie.getNom());
            }

            createDocInfo();

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(docInfo,
                    getSession().getApplication(), getCodeIsoLangue());

            // creation des paramètres pour l'en-tête
            // ---------------------------------------------------------------------
            try {
                // on change la langue du caisse helper en fonction de la langue tiers
                caisseHelper.init(getSession().getApplication(), getCodeIsoLangue());

                // Ajoute le libellé CONFIDENTIEL dans l'adresse de l'entête du document
                if (getIsAfficherConfidentielSurDocument()) {
                    crBean.setConfidentiel(true);
                } else {
                    crBean.setConfidentiel(false);
                }

                caisseHelper.addHeaderParameters(getImporter(), crBean);
            } catch (final Exception e) {
                throw new FWIException("Impossible de renseigner l'en-tete", e);
            }

            // ajout du nom du département si nécessaire
            if (decompteCourant.getDepartement() != null) {
                parametres.put("P_HEADER_DEPARTEMENT", decompteCourant.getDepartement().getDepartement());
            }

            // le titre
            if (APTypeDeDecompte.JOUR_ISOLE.equals(decompteCourant.getTypeDeDecompte())) {
                parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(2).getDescription());
            } else {
                parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(1).getDescription());
            }

            // Les données de l'entête dès la deuxième page
            // Les autres paramètres sont repris de l'entête normal

            String type_decompte = "";

            if (isTraitementDesVentilations()) {
                type_decompte = document.getTextes(5).getTexte(9).getDescription();
            } else {
                switch (decompteCourant.getTypeDeDecompte()) {

                    case NORMAL:
                        type_decompte = document.getTextes(5).getTexte(6).getDescription();
                        break;

                    case ACM_GE:
                        type_decompte = document.getTextes(5).getTexte(7).getDescription();
                        break;

                    case AMAT_GE:
                        type_decompte = document.getTextes(5).getTexte(8).getDescription();
                        break;

                    case NORMAL_ACM_NE:
                        type_decompte = document.getTextes(5).getTexte(8).getDescription();
                        break;

                    case JOUR_ISOLE:
                        type_decompte = document.getTextes(5).getTexte(6).getDescription();
                        break;

                    case COMPCIAB:
                        type_decompte = document.getTextes(5).getTexte(6).getDescription();
                        break;

                    default:
                        throw new Exception("Impossible de résoudre le type de décompte actuellement généré !");
                }
            }

            String texteTitre2 = PRStringUtils.replaceString(document.getTextes(5).getTexte(1).getDescription(),
                    "{type}", type_decompte);

            texteTitre2 = PRStringUtils.replaceString(texteTitre2, "{dateDec}", JACalendar.format(getDateDocument()));

            parametres.put("PARAM_TITRE2", texteTitre2);

            PRTiersWrapper tiers;
            tiers = PRTiersHelper.getTiersParId(getISession(), decompteCourant.getIdTiers());

            if (tiers == null) {
                tiers = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
            }

            parametres.put("P_HEADER_NOM_PAGE2",
                    PRStringUtils.replaceString(document.getTextes(5).getTexte(4).getDescription(), "{nomPrenom}",
                            tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)));

            if (JadeStringUtil.isEmpty((noAffilie))) {

                final String nAvs = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                final String idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                if (!JadeStringUtil.isEmpty(nAvs)) {
                    parametres.put("P_HEADER_NO_AVS_PAGE2", PRStringUtils
                            .replaceString(document.getTextes(5).getTexte(3).getDescription(), "{noAVS}", nAvs));
                }

                if (!JadeStringUtil.isBlankOrZero(idTiers)) {

                    if (getIsAfficherNIPSurDocument()) {
                        parametres.put("P_HEADER_NIP_LIB", getSession().getLabel("NIP") + " :");
                        parametres.put("P_HEADER_NIP", idTiers);
                    }
                }

            } else {

                parametres.put("P_HEADER_NO_AFFILIE_PAGE2", PRStringUtils
                        .replaceString(document.getTextes(5).getTexte(2).getDescription(), "{noAffilie}", noAffilie));
            }

            parametres.put("PARAM_PAGE", document.getTextes(5).getTexte(5).getDescription());

            // le corps du document
            StringBuffer buffer = new StringBuffer();

            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            final Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            final ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            final String titre = tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            for (final Iterator<ICTTexte> textes = document.getTextes(2).iterator(); textes.hasNext();) {
                final ICTTexte texte = textes.next();

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

            // cette méthode est exécutée après createDataSource donc nous connaissons le total des prestations et donc
            // nous savons s'il s'agit d'un document de restitution.

            if (restitution) {
                buffer.append(document.getTextes(2).getTexte(102).getDescription());
            } else if (APTypeDeDecompte.JOUR_ISOLE.equals(decompteCourant.getTypeDeDecompte())) {
                buffer.append(document.getTextes(2).getTexte(201).getDescription());
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

            for (final Iterator<ICTTexte> textes = document.getTextes(4).iterator(); textes.hasNext();) {
                final ICTTexte texte = textes.next();

                // ne pas traiter le contenu optionnel
                if (Integer.parseInt(texte.getPosition()) > 100) {
                    break;
                }

                if ((Integer.parseInt(texte.getPosition()) >= 3) && (Integer.parseInt(texte.getPosition()) <= 9)) {
                    break;
                }

                // Si ventilation, ne pas mettre le texte 4.1
                if (texte.getPosition().equals("1")) {
                    if (!isTraitementDesVentilations()) {
                        buffer.append(texte.getDescription());
                        if (buffer.length() > 0) {
                            buffer.append("\n");
                        }
                    }
                } else {
                    buffer.append(texte.getDescription());
                    if (buffer.length() > 0) {
                        buffer.append("\n");
                    }
                }

            }

            parametres.put("PARAM_PIED", buffer.toString());

            buffer.setLength(0);
            buffer.append(document.getTextes(6).getTexte(1).getDescription());
            buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));
            buffer.append("\n");
            parametres.put("PARAM_SALUTATIONS", buffer.toString());

            try {
                // ajouter la signature
                caisseHelper.addSignatureParameters(getImporter());

                // Test on écrase les signatures standard si celle du CT ne sont pas vide.
                // Permet de différencier le nom du service entre LAMAT et APG

                // Le nom du service sera stocké au même niveau/position entre les doc APG et Maternité

                // WorkAround pour différencier la signature pour chaque type de document
                String caisse = null;
                try {
                    caisse = document.getTextes(20).getTexte(1).getDescription();
                } catch (final Exception e) {
                    caisse = "";
                }

                String service = null;
                try {
                    service = document.getTextes(20).getTexte(2).getDescription();
                } catch (final Exception e) {
                    service = "";
                }

                if (!JadeStringUtil.isBlankOrZero(service)) {
                    parametres.put(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE, caisse);
                }

                if (!JadeStringUtil.isBlankOrZero(caisse)) {
                    parametres.put(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE, service);
                }

            } catch (final Exception e) {
                throw new FWIException("Impossible de charger le pied de page", e);
            }
        } catch (final Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, APDecompteGenerationProcess.class.getName());
            abort();
        }
    }

    /** Initialise l'etat de ce process. */
    @Override
    public final void beforeExecuteReport() {
        // Effacer les pdf a la fin
        setDeleteOnExit(true);

        try {
            preparerDonneesPourDecomptes();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Erreur fatale lors due la prépartation et du regroupement des données. Message : " + e.toString(),
                    e);
        }

        definitLeTemplateEnFonctionCaisse();
    }

    protected final String buildOrderPrintingByKey(final String idAffilie, final String idTiers) throws Exception {

        String noAffilieFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
        String noAvsFormatte = PRBlankBNumberFormater.getEmptyNssFormatte(getSession().getApplication());

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
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
     * Methode pour formatter le caractère '&'
     *
     * @param texte
     * @return
     */
    private String checkFormatCaracter(String texte) {
        texte = texte.replace("&", "&amp;amp;");
        return texte;
    }

    private String chercherIdTiersPourLigneTechinqueVersementATier(final PRTiersWrapper tiers) throws Exception {
        String idTiersPourLigneTechnique = decompteCourant.getIdTiers();

        if ((tiers == null) || ((tiers != null) && JadeStringUtil.isBlankOrZero(tiers.getNSS()))) {

            class TiersNomPrenom {
                String idTiers = null;
                String nom = null;
                String prenom = null;
            }

            // versement à tiers, recherche du premier assuré dans ce décompte. Son NSS sera utilisé
            // dans la ligne technique afin de ne pas perdre trace de ce document dans la GED.
            final List<TiersNomPrenom> tiersDeCeDecommpte = new ArrayList<TiersNomPrenom>();

            for (final APRepartitionJointPrestation uneRepartition : decompteCourant.getRepartitionsPeres()) {

                final APPrestationJointLotTiersDroitManager prestationJointTiersManager = new APPrestationJointLotTiersDroitManager();
                prestationJointTiersManager.setSession(getSession());
                prestationJointTiersManager.setForIdDroit(uneRepartition.getIdDroit());
                prestationJointTiersManager.find();

                if (prestationJointTiersManager.size() > 0) {
                    final APPrestationJointLotTiersDroit unePrestation = (APPrestationJointLotTiersDroit) prestationJointTiersManager
                            .get(0);

                    final TiersNomPrenom unTiers = new TiersNomPrenom();
                    unTiers.idTiers = unePrestation.getIdTiers();
                    unTiers.nom = unePrestation.getNom();
                    unTiers.prenom = unePrestation.getPrenom();

                    tiersDeCeDecommpte.add(unTiers);
                }
            }

            if (tiersDeCeDecommpte.size() > 0) {

                Collections.sort(tiersDeCeDecommpte, new Comparator<TiersNomPrenom>() {

                    @Override
                    public int compare(final TiersNomPrenom o1, final TiersNomPrenom o2) {
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
     * Crée les lignes de la boucle de détail du document.
     * <p>
     * Ces données sont, pour chaque répartition de paiement:
     * </p>
     * <ol>
     * <li>Les infos sur le droit (no AVS, noms, etc.).</li>
     * <li>Les infos sur la prestation (dates, nombre jours, montants, ...)</li>
     * <li>Les infos sur les cotisations à payer sur les montants.</li>
     * <li>Les infos sur les ventilations de ce montant.</li>
     * <li>Une ligne qui totalise le montant avant compensation.</li>
     * <li>Les factures à compenser.</li>
     * <li>Une ligne qui totalise le montant après compensation.</li>
     * <li>Les remarques sur la prestation.</li>
     * </ol>
     */
    @Override
    public final void createDataSource() {

        final List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();
        FWCurrency total = new FWCurrency(0);
        final FWCurrency grandTotal = new FWCurrency(0);
        APDroitLAPG droit;
        PRTiersWrapper tiers;
        Map<String, String> champs = new HashMap<String, String>();
        final FWCurrency totalAPG = new FWCurrency(0);
        final FWCurrency totalCotisations = new FWCurrency(0);
        final FWCurrency totalImpotSource = new FWCurrency(0);
        final FWCurrency totalMontantVentile = new FWCurrency(0);
        final FWCurrency totalCompensations = new FWCurrency(0);
        int nbDecompte = 0;

        try {

            // On déclare un TreeSet, triant les éléments par nom et prénom
            final Set<APRepartitionJointPrestation> repartitionsTreeSet = new TreeSet<APRepartitionJointPrestation>(
                    new Comparator<APRepartitionJointPrestation>() {
                        @Override
                        public int compare(final APRepartitionJointPrestation objRepartition1,
                                final APRepartitionJointPrestation objRepartition2) {

                            final String nom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_NOM);
                            final String nom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_NOM);
                            if (nom1 != null) {
                                final int nomComp = nom1.compareTo(nom2);

                                if (nomComp != 0) {
                                    return nomComp;
                                } else {
                                    String prenom1 = null;
                                    try {
                                        prenom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_PRENOM)
                                                + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(
                                                        objRepartition1.getDateDebut())
                                                + objRepartition1.getIdRepartitionBeneficiairePaiement();
                                    } catch (final JAException e) {
                                        prenom1 = getNom(objRepartition1, PRTiersWrapper.PROPERTY_PRENOM)
                                                + objRepartition1.getIdRepartitionBeneficiairePaiement();

                                    }

                                    String prenom2;
                                    try {
                                        prenom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_PRENOM)
                                                + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(
                                                        objRepartition2.getDateDebut())
                                                + objRepartition2.getIdRepartitionBeneficiairePaiement();
                                    } catch (final JAException e) {
                                        prenom2 = getNom(objRepartition2, PRTiersWrapper.PROPERTY_PRENOM)
                                                + objRepartition2.getIdRepartitionBeneficiairePaiement();

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

                        private String getNom(final APRepartitionJointPrestation objRepartition,
                                final String propriete) {
                            try {
                                if (objRepartition != null) {
                                    final PRDemande demande = ApgServiceLocator.getEntityService().getDemandeDuDroit(
                                            APAbstractDecomptesGenerationProcess.this.getSession(),
                                            APAbstractDecomptesGenerationProcess.this.getTransaction(),
                                            objRepartition.getIdDroit());
                                    final PRTiersWrapper tiers = PRTiersHelper.getTiersParId(
                                            APAbstractDecomptesGenerationProcess.this.getSession(),
                                            demande.getIdTiers());
                                    return tiers.getProperty(propriete);
                                } else {
                                    return null;
                                }

                            } catch (final Exception e) {
                                return null;
                            }
                        }
                    });

            repartitionsTreeSet.addAll(decompteCourant.getRepartitionsPeres());

            Boolean hasPrestationAPGFederale = false;
            for (final APRepartitionJointPrestation repartition : repartitionsTreeSet) {
                setTailleLot(1);
                setImpressionParLot(true);

                champs = new HashMap<String, String>();

                nbDecompte += 1;

                // les lignes pour la répartition elle-même
                // ré-initialisation du total pour chaque répartition
                if (!total.isZero()) {
                    total = new FWCurrency(0);
                }

                // Récupération de l'idDomaine pour sélectionner la bonne adresse de paiement dans
                // getAdressePaiementAffilie()
                domaineDePaiement = repartition.getIdDomaineAdressePaiement();

                // 1. le n°AVS et le nom de l'assure
                droit = ApgServiceLocator.getEntityService().getDroitLAPG(getSession(), getTransaction(),
                        repartition.getIdDroit());
                final PRDemande demande = droit.loadDemande();
                tiers = PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());

                String infoSup = "";
                if (APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(repartition.getGenrePrestationPrestation())) {
                    infoSup = "\n" + APPrestationLibelleCodeSystem.getLibelleJourIsole(getSession(),
                            repartition.getGenreService(), getCodeIsoLangue());
                } else if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(repartition.getGenrePrestationPrestation())) {
                    infoSup = "\n"
                            + APPrestationLibelleCodeSystem.getLibelleComplement(getSession(), getCodeIsoLangue());
                }

                champs.put("FIELD_ASSURE",
                        PRStringUtils.replaceString(document.getTextes(3).getTexte(13).getDescription(), "{nomAVS}",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " "
                                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                        + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + infoSup));

                // 2. les infos sur la prestation
                champs.put("FIELD_DETAIL_PERIODE",
                        PRStringUtils.replaceString(document.getTextes(3).getTexte(14).getDescription(), "{periode}",
                                JACalendar.format(repartition.getDateDebut(), getCodeIsoLangue()) + " - "
                                        + JACalendar.format(repartition.getDateFin(), getCodeIsoLangue())));

                // 3. détail sur la prestation journalière (nbr de jours + montant journalier), si non ventilé
                if (!isTraitementDesVentilations()) {
                    // CHANGES
                    // On n'ajoute pas ce détail si c'est un décompte mixte NORMAL_ACM_NE
                    if (!APTypeDeDecompte.NORMAL_ACM_NE.equals(decompteCourant.getTypeDeDecompte())) {
                        champs.put("FIELD_DETAIL_JOURNALIER", getDetailJournalier(repartition));
                    }
                }

                // 4. le montant de la répartition
                if (isTraitementDesVentilations()) {
                    champs.put("FIELD_MONTANT_APG",
                            PRStringUtils.replaceString(document.getTextes(3).getTexte(16).getDescription(),
                                    "{montantPeriode}",
                                    JANumberFormatter.formatNoRound(repartition.getMontantVentile())));

                } else {
                    champs.put("FIELD_MONTANT_APG",
                            PRStringUtils.replaceString(document.getTextes(3).getTexte(16).getDescription(),
                                    "{montantPeriode}", JANumberFormatter.formatNoRound(repartition.getMontantBrut())));

                }

                if ("true".equals(JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB))
                        && APTypeDePrestation.STANDARD.isCodeSystemEqual(repartition.getGenrePrestationPrestation())) {
                    champs.put("FIELD_APG_FED", "*");
                    hasPrestationAPGFederale = true;
                }
                // Type de prestation complémentaire
                // Dans le cas des décomptes 'normal-acmne' (les 2 types de prestations sont présentes sur le
                // décomptes)
                // une remarque prestation complémentaire est insérée
                // 1 - le décompte doit être un décompte standard-acmne
                if ((decompteCourant != null)
                        && APTypeDeDecompte.NORMAL_ACM_NE.equals(decompteCourant.getTypeDeDecompte())) {
                    // 2 - la prestation doit être de type ACMNE
                    if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(repartition.getGenrePrestationPrestation())) {
                        champs.put(APAbstractDecomptesGenerationProcess.PARAMETER_PRESTATION_COMPLEMENTAIRE,
                                document.getTextes(3).getTexte(50).getDescription());
                    }
                }

                // Remarques
                if (!JadeStringUtil.isEmpty(repartition.getRemarque())) {
                    champs.put("FIELD_REMARQUE_PRESTATION",
                            PRStringUtils.replaceString(document.getTextes(3).getTexte(19).getDescription(),
                                    "{remarque}", repartition.getRemarque()));
                }

                if (isTraitementDesVentilations()) {
                    totalAPG.add(repartition.getMontantVentile());
                } else {
                    totalAPG.add(repartition.getMontantBrut());
                }

                // 5. les cotisations & l'impôt à la source
                final APCotisationManager apCotMan = new APCotisationManager();
                apCotMan.setForIdRepartitionBeneficiairePaiement(repartition.getIdRepartitionBeneficiairePaiement());
                apCotMan.setSession(getSession());
                apCotMan.find(getTransaction(), BManager.SIZE_NOLIMIT);

                final FWCurrency totalMontantCotisation = new FWCurrency(0);
                final FWCurrency totalMontantImpotSource = new FWCurrency(0);
                final FWCurrency cotisationsFNE = new FWCurrency(0);

                final String libelleCot = document.getTextes(3).getTexte(15).getDescription();
                String libelleAVS = "";
                String libelleAC = "";
                String libelleLFA = "";

                for (int i = 0; i < apCotMan.size(); i++) {
                    final APCotisation apCot = (APCotisation) apCotMan.getEntity(i);

                    if (APCotisation.TYPE_IMPOT.equals(apCot.getType())) {
                        totalMontantImpotSource.add((apCot.getMontant()));
                    }

                    else {
                        final int idExterneCoti = Integer.parseInt(apCot.getIdExterne());

                        if (idExterneCoti == getIdAssuranceFneParitaire()) {
                            // Le montant des coti FNE n'est pas cumulé avec les autres cotis
                            cotisationsFNE.add(apCot.getMontant());
                        } else {
                            totalMontantCotisation.add((apCot.getMontant()));

                            if ((idExterneCoti == getIdAssuranceAvsParitaire())
                                    || (idExterneCoti == getIdAssuranceAvsPersonnelle())) {
                                libelleAVS = document.getTextes(3).getTexte(40).getDescription();
                            }

                            else if (idExterneCoti == getIdAssuranceAcParitaire()) {
                                libelleAC = document.getTextes(3).getTexte(41).getDescription();
                            }

                            else if ((idExterneCoti == getIdAssuranceLfaParitaire())
                                    || (idExterneCoti == getIdAssuranceLfaPersonnelle())) {
                                libelleLFA = document.getTextes(3).getTexte(42).getDescription();
                            }
                        }
                    }
                }

                String fieldRestitution = "";

                if (!JadeStringUtil.isBlankOrZero(decompteCourant.getIdAffilie()) && getIsAfficherNIPSurDocument()) {
                    fieldRestitution = getSession().getLabel("NIP") + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }

                // Si c'est une restitution, on affiche la mention "restitution"
                final double montant = Double.parseDouble(repartition.getMontantBrut());
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
                // CHANGES
                if (!totalMontantCotisation.equals(new FWCurrency(0))) {
                    champs.put("FIELD_DETAIL_COTISATIONS", libelleCot + libelleAVS + libelleAC + libelleLFA);

                    champs.put("FIELD_MONTANT_COTISATIONS",
                            PRStringUtils.replaceString(document.getTextes(3).getTexte(17).getDescription(),
                                    "{montantCoti}",
                                    JANumberFormatter.formatNoRound(totalMontantCotisation.toString())));

                    totalCotisations.add(totalMontantCotisation.toString());
                }

                // afficher Cotisations FNE sur une nouvelle ligne si le montant est supérieur à 0
                if (!cotisationsFNE.isZero()) {
                    champs.put(APAbstractDecomptesGenerationProcess.DETAIL_COTISATIONS_FNE,
                            document.getTextes(3).getTexte(51).getDescription());
                    champs.put(APAbstractDecomptesGenerationProcess.MONTANT_COTISATIONS_FNE, cotisationsFNE.toString());
                }

                // Affichage de l'impôt à la source
                if (!totalMontantImpotSource.equals(new FWCurrency(0))) {
                    champs.put("FIELD_DETAIL_IMPOT", document.getTextes(3).getTexte(12).getDescription());

                    champs.put("FIELD_MONTANT_IMPOT",
                            PRStringUtils.replaceString(document.getTextes(3).getTexte(22).getDescription(),
                                    "{montantImpot}",
                                    JANumberFormatter.formatNoRound(totalMontantImpotSource.toString())));

                    totalImpotSource.add(totalMontantImpotSource.toString());
                }

                if (isTraitementDesVentilations()) {
                    total.add(repartition.getMontantVentile());
                } else {
                    // ajout du montant brut moins les cotisations au total
                    total.add(repartition.getMontantNet());
                }

                // les lignes pour les montants ventilés (si nécessaire)
                final List<APRepartitionJointPrestation> repartitionsEnfants = decompteCourant
                        .getRepartitionsEnfants(repartition);

                if (repartitionsEnfants != null) {

                    int nbVentilations = 0;

                    for (final APRepartitionJointPrestation uneRepartitionEnfant : repartitionsEnfants) {

                        nbVentilations++;

                        ITITiers tiersNom = (ITITiers) getSession().getAPIFor(ITITiers.class);
                        final Hashtable<String, String> params = new Hashtable<String, String>();
                        params.put(ITITiers.FIND_FOR_IDTIERS, uneRepartitionEnfant.getIdTiersAdressePaiement());
                        final ITITiers[] t = tiersNom.findTiers(params);
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

                        // Verifie et format les caratères spéciaux
                        nom = checkFormatCaracter(nom);

                        champs.put("FIELD_DETAIL_VENTILATIONS_" + nbVentilations, PRStringUtils.replaceString(
                                document.getTextes(3).getTexte(6).getDescription(), "{ventilation}", nom));

                        champs.put("FIELD_MONTANT_VENTILATIONS_" + nbVentilations, PRStringUtils.replaceString(
                                document.getTextes(3).getTexte(24).getDescription(), "{montantVentilation}",
                                "-" + JANumberFormatter.formatNoRound(uneRepartitionEnfant.getMontantVentile())));

                        total.sub(uneRepartitionEnfant.getMontantVentile());
                        totalMontantVentile.sub(uneRepartitionEnfant.getMontantVentile());

                    }
                }

                // le total
                champs.put("FIELD_TOTAL_REPARTITION", document.getTextes(3).getTexte(7).getDescription());

                champs.put("FIELD_MONTANT_REPARTITION",
                        PRStringUtils.replaceString(document.getTextes(3).getTexte(18).getDescription(),
                                "{montantTotal}", JANumberFormatter.formatNoRound(total.toString())));

                lignes.add(champs);
                grandTotal.add(total);

            }

            champs = new HashMap<String, String>();

            // les factures à compenser
            final List<APFactureACompenser> facturesACompenser = decompteCourant.getFacturesACompenser();

            if ((facturesACompenser != null) && !facturesACompenser.isEmpty()) {

                // BZ 6306
                // Liste des compensations avec numéro de facture
                final ArrayList<APFactureACompenser> listeFacturesAvecNumero = new ArrayList<APFactureACompenser>();
                // Liste des compensation sans numéro de facture
                final ArrayList<APFactureACompenser> listeFacturesSansNumero = new ArrayList<APFactureACompenser>();
                // Buffer utilisé pour mémoriser le texte de la dernière ligne des compensations avec num de facture
                // Dans le cas où elles ne peuvent pas toutes êtres affichées de manière standard
                final StringBuffer bufferDernierLigneFactureAvecNum = new StringBuffer();
                // Même que ci-dessus mais pour le montant à afficher
                final FWCurrency totalDernierLigneFactureAvecNum = new FWCurrency();
                // Utilisé pour déterminer si j'ai atteint la place maximum d'affichage de la dernière ligne des
                // compensations avec num de facture
                boolean isTropFacture = false;

                // BZ 6306
                // Je parcours une première fois la liste des factures à compenser pour les séparer dans deux listes :
                // La première contenant les compensations avec un numéro de facture et la deuxième pour les
                // compensation sans numéro de facture
                for (final APFactureACompenser facture : facturesACompenser) {
                    if (JadeStringUtil.isEmpty(facture.getNoFacture()) || facture.getNoFacture().equals("0")) {
                        listeFacturesSansNumero.add(facture);
                    } else {
                        listeFacturesAvecNumero.add(facture);
                    }
                }

                // BZ 6306
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
                // BZ 6306
                // Je commence par traiter le compensation avec num de facture
                for (final APFactureACompenser facture : listeFacturesAvecNumero) {
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
                        final StringBuffer buffer = new StringBuffer();
                        buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(25).getDescription(),
                                "{compSurFacture}", facture.getNoFacture()));

                        champs.put("FIELD_COMPENSATION_" + numLigneFactureAvecNum, buffer.toString());
                        if (Double.parseDouble(facture.getMontant()) < 0) {

                            champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecNum,
                                    PRStringUtils.replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                            "{montantCompensation}",
                                            JANumberFormatter.formatNoRound(facture.getMontant())));

                        } else {

                            champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecNum,
                                    PRStringUtils.replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                            "{montantCompensation}",
                                            "-" + JANumberFormatter.formatNoRound(facture.getMontant())));

                        }

                    } else {
                        // Affichage concaténé
                        // Si j'ai atteint le nombre max de ligne
                        if (numLigneFactureAvecNum == nbLignesFactureAvecNum) {
                            // Je récupère une seul fois le texte à afficher
                            bufferDernierLigneFactureAvecNum.append(
                                    PRStringUtils.replaceString(document.getTextes(3).getTexte(43).getDescription(),
                                            "{compSurFacture}", facture.getNoFacture()));
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

                // BZ 6306 Dans le cas d'un affichage non standard, j'insère la dernière ligne
                if (!JadeStringUtil.isEmpty(bufferDernierLigneFactureAvecNum.toString())) {
                    champs.put("FIELD_COMPENSATION_" + nbLignesFactureAvecNum,
                            bufferDernierLigneFactureAvecNum.toString());
                    if (Double.parseDouble(totalDernierLigneFactureAvecNum.toString()) < 0) {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + nbLignesFactureAvecNum,
                                PRStringUtils.replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                        "{montantCompensation}",
                                        JANumberFormatter.formatNoRound(totalDernierLigneFactureAvecNum.toString())));

                    } else {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + nbLignesFactureAvecNum, PRStringUtils.replaceString(
                                document.getTextes(3).getTexte(26).getDescription(), "{montantCompensation}",
                                "-" + JANumberFormatter.formatNoRound(totalDernierLigneFactureAvecNum.toString())));

                    }
                }

                // BZ 6306
                // Traitement de la liste des compensations sans num de facture
                if (!listeFacturesSansNumero.isEmpty()) {
                    // Dans le cas où il y a des compensations sans num de factures, une seule ligne sera affichée et le
                    // montant sera la résultat de l'addition de toutes les compensations sans num de factures.
                    final FWCurrency totalFactureNumZero = new FWCurrency(0);
                    for (final APFactureACompenser facture : listeFacturesSansNumero) {
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

                    champs.put("FIELD_COMPENSATION_" + numLigneFactureAvecZero,
                            document.getTextes(3).getTexte(8).getDescription());

                    // On doit inverser le signe des factures à compenser.
                    if (Double.parseDouble(totalFactureNumZero.toString()) < 0) {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecZero,
                                PRStringUtils.replaceString(document.getTextes(3).getTexte(26).getDescription(),
                                        "{montantCompensation}",
                                        JANumberFormatter.formatNoRound(totalFactureNumZero.toString())));

                    } else {

                        champs.put("FIELD_MONTANT_COMPENSATION_" + numLigneFactureAvecZero,
                                PRStringUtils.replaceString(document.getTextes(3).getTexte(26).getDescription(),
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

            champs.put("FIELD_MONTANT_FINAL",
                    PRStringUtils.replaceString(document.getTextes(3).getTexte(27).getDescription(), "{montantFinal}",
                            JANumberFormatter.formatNoRound(grandTotal.toString())));

            // Insertion de l'addresse de paiement, si non ventilé
            if ((grandTotal.compareTo(new FWCurrency(0)) > 0)) {
                // Récupération de l'addresse de paiement
                final String adressePaiement = getAdressePaiementAffilie(getSession(), decompteCourant.getIdTiers(),
                        decompteCourant.getIdAffilie());
                // Si pas vide, insertion dans le document
                if (!JadeStringUtil.isBlank(adressePaiement)) {
                    // Texte d'information selon le type d'adresse de paiement
                    // Si adresse contient une virgule, il s'agit d'une adresse de paiement bancaire
                    if (adressePaiement.contains(",")) {
                        champs.put("FIELD_TEXTE_ADRESSE_PAIEMENT", document.getTextes(3).getTexte(45).getDescription());
                        // Adresse de paiement
                        champs.put("FIELD_ADRESSE_PAIEMENT",
                                PRStringUtils.replaceString(document.getTextes(3).getTexte(48).getDescription(),
                                        "{adressePaiement}", adressePaiement));

                    }
                    // Sinon, si l'adresse ne contient ni virgule, ni retour à la ligne, c'est une adresse postale
                    else if (!adressePaiement.contains(",") && !adressePaiement.contains("\n")) {
                        champs.put("FIELD_TEXTE_ADRESSE_PAIEMENT",
                                document.getTextes(3).getTexte(46).getDescription() + " "
                                        + PRStringUtils.replaceString(
                                                document.getTextes(3).getTexte(48).getDescription(),
                                                "{adressePaiement}", adressePaiement));

                    }
                    // Sinon, c'est une adresse de mandat postale
                    else {
                        try {
                            champs.put("FIELD_TEXTE_ADRESSE_PAIEMENT",
                                    document.getTextes(3).getTexte(47).getDescription());
                            // Adresse de paiement
                            champs.put("FIELD_ADRESSE_PAIEMENT",
                                    PRStringUtils.replaceString(document.getTextes(3).getTexte(48).getDescription(),
                                            "{adressePaiement}", adressePaiement.trim()));
                        } catch (Exception e) {
                            JadeLogger.error(this.getClass().getName(), e);
                        }
                    }
                }
            }
            try {
                if (hasPrestationAPGFederale) {
                    champs.put("FIELD_TEXTE_PAS_SOUMIS_LAA", document.getTextes(3).getTexte(49).getDescription());
                }
            } catch (IndexOutOfBoundsException e) {
                Logger.logInfo("Pas de texte pour maternité : " + e.getMessage());
            }

            // Anciennement champs de classe
            restitution = grandTotal.isNegative();

            // Récapitulatif si plusieurs décomptes et UNIQUEMENT pour certains caisses
            if (!JadeStringUtil.isBlankOrZero(decompteCourant.getIdAffilie())) {

                if (getIsDecompteRecapitulatif()) {

                    // Pas de recap pour les décomptes mixtes NORMAL_ACM_NE
                    if (!APTypeDeDecompte.NORMAL_ACM_NE.equals(decompteCourant.getTypeDeDecompte())) {

                        if (nbDecompte > 1) {

                            if (!totalAPG.isZero()) {

                                champs.put("FIELD_RECAP", document.getTextes(3).getTexte(28).getDescription());
                                champs.put("FIELD_RECAP_APG", document.getTextes(3).getTexte(29).getDescription());
                                champs.put("FIELD_MONTANT_RECAP_APG",
                                        PRStringUtils.replaceString(document.getTextes(3).getTexte(30).getDescription(),
                                                "{montantAPGbrut}", totalAPG.toString()));
                            }

                            if (!totalCotisations.isZero()) {
                                champs.put("FIELD_RECAP_COTISATIONS",
                                        document.getTextes(3).getTexte(31).getDescription());
                                champs.put("FIELD_MONTANT_RECAP_COTISATIONS",
                                        PRStringUtils.replaceString(document.getTextes(3).getTexte(32).getDescription(),
                                                "{montantCotisations}", totalCotisations.toString()));
                            }

                            if (!totalImpotSource.isZero()) {
                                champs.put("FIELD_RECAP_IMPOT", document.getTextes(3).getTexte(35).getDescription());
                                champs.put("FIELD_MONTANT_RECAP_IMPOT",
                                        PRStringUtils.replaceString(document.getTextes(3).getTexte(36).getDescription(),
                                                "{impotMontant}", totalImpotSource.toString()));
                            }

                            if (!totalMontantVentile.isZero()) {
                                champs.put("FIELD_RECAP_VENTILATION",
                                        document.getTextes(3).getTexte(37).getDescription());
                                champs.put("FIELD_MONTANT_RECAP_VENTILATION",
                                        PRStringUtils.replaceString(document.getTextes(3).getTexte(38).getDescription(),
                                                "{ventilationsMontant}", totalMontantVentile.toString()));
                            }

                            if (!totalCompensations.isZero()) {
                                champs.put("FIELD_RECAP_COMPENSATION",
                                        document.getTextes(3).getTexte(33).getDescription());
                                champs.put("FIELD_MONTANT_RECAP_COMPENSATION",
                                        PRStringUtils.replaceString(document.getTextes(3).getTexte(34).getDescription(),
                                                "{compensationsMontant}", totalCompensations.toString()));
                            }

                            if (grandTotal.isNegative() || grandTotal.isZero()) {
                                champs.put("FIELD_TOTAL_FINAL_RECAP",
                                        document.getTextes(3).getTexte(11).getDescription());
                            } else {
                                champs.put("FIELD_TOTAL_FINAL_RECAP",
                                        document.getTextes(3).getTexte(9).getDescription());
                            }

                            champs.put("FIELD_MONTANT_FINAL_RECAP",
                                    PRStringUtils.replaceString(document.getTextes(3).getTexte(39).getDescription(),
                                            "{montantFinalRecap}",
                                            JANumberFormatter.formatNoRound(grandTotal.toString())));
                        }
                    }
                }
            }
            lignes.add(champs);
        } catch (final Exception e) {
            e.printStackTrace();
            setSendMailOnError(true);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "APAbstractDecomptesGenerationProcess");
            abort();
        }
        if (!lignes.isEmpty()) {
            this.setDataSource(lignes);
        }

    }

    /**
     * Défini les propriétés du JadePublishDocumentInfo pour archivage du document dans la GED
     */
    public void createDocInfo() {

        docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        try {
            try {
                if (IPRDemande.CS_TYPE_APG.equals(getCSTypePrestationsLot())) {

                    // SI APG
                    docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_APG_TITLE"));
                    docInfo.setDocumentProperty("apg.typeDecompte", IPRDemande.CS_TYPE_APG);

                    if (isTraitementDesVentilations()) {
                        docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_VENTILATION);
                    } else {
                        switch (decompteCourant.getTypeDeDecompte()) {

                            case NORMAL:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_NORMAL);
                                break;
                            case ACM_GE:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_ACM);
                                break;
                            case AMAT_GE:
                                throw new Exception(
                                        "Incohérence de donnée. Impossible de générer un décompte de type APG avec des prestations de type AMAT_GE");
                            case NORMAL_ACM_NE:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_NORMAL_ACMNE);
                                break;
                            case JOUR_ISOLE:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_NORMAL);
                                break;
                            case COMPCIAB:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_NORMAL);
                                break;
                            default:
                                throw new Exception(
                                        "Impossible de résoudre le type de décompte APG pour la génération du doc info");
                        }
                    }

                } else {
                    // SI MATERNITE
                    docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_MAT_TITLE"));
                    docInfo.setDocumentProperty("apg.typeDecompte", IPRDemande.CS_TYPE_MATERNITE);
                    if (isTraitementDesVentilations()) {
                        docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_APG_VENTILATION);
                    } else {
                        switch (decompteCourant.getTypeDeDecompte()) {

                            case NORMAL:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_MAT_NORMAL);
                                break;
                            case ACM_GE:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_MAT_ACM);
                                break;
                            case AMAT_GE:
                                docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECOMPTE_MAT_LAMAT);
                                break;
                            case NORMAL_ACM_NE:
                                throw new Exception(
                                        "Incohérence de données. Impossible de générer un décompte de type Maternité avec des prestations de type ACM_NE");
                            default:
                                throw new Exception(
                                        "Impossible de résoudre le type de décompte maternité pour la génération du doc info");
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }

            docInfo.setDocumentDate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDateDocument().toStrAMJ()));

            String yy = String.valueOf(getDateComptable().getYear());
            yy = yy.substring(2, 4);
            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "A" + yy);

            String annee = "";
            if (!JadeStringUtil.isEmpty(getDateDocument().toString())) {
                annee = JADate.getYear(getDateDocument().toString()).toString();
            } else if (!JadeStringUtil.isEmpty(yy)) {
                annee = yy;
            } else {
                annee = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
            }
            docInfo.setDocumentProperty("annee", annee);

            final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), decompteCourant.getIdTiers());
            // on défini les propriétés du DocInfo pour l'archivage
            if (decompteCourant.getIsPaiementEmployeur()) {

                // Dans JadeClientServiceLocator, globaz.apg.itext.APDecomptesAss ou
                // globaz.apg.itext.APDecomptesAff pour respectivement
                // L'assure ou l'affilie. 2 entrées différentes pour différencier le type de document (DTID)
                docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Aff");

                if (!JadeStringUtil.isIntegerEmpty(decompteCourant.getIdAffilie())) {

                    // Employeur affilie --> rôle AFFILIE
                    final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem()
                            .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                    if (affilie != null) {

                        TIDocumentInfoHelper.fill(docInfo, affilie.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                                affilie.getNumAffilie(), affilieFormatter.unformat(affilie.getNumAffilie()));
                    } else {

                        final String naffFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                        final String naffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(naffFormatte);

                        TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(),
                                ITIRole.CS_AFFILIE, naffFormatte, naffNonFormatte);
                    }

                    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    // pour la CCJU OU CICICAM, les documents envoyés aux employeurs doivent être indexés uniquement
                    // avec le n° d'affilié
                    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    if (isCaisse(APApplication.NO_CAISSE_CCJU) || isCaisse(APApplication.NO_CAISSE_CICICAM)) {
                        docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
                    }

                } else {
                    final String naffFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                    final String naffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(naffFormatte);

                    // Employeur non affilie --> rôle NON_AFFILIE
                    TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(),
                            IPRConstantesExternes.OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE, naffFormatte, naffNonFormatte);
                }

            } else {
                // Assuré --> rôle APG

                String noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                String noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);

                final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem()
                        .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                if (affilie != null) {
                    docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Aff");
                    noAff = affilie.getNumAffilie();
                    noAffNonFormatte = affilieFormatter.unformat((affilie.getNumAffilie()));

                    if (JadeStringUtil.isBlank(noAff)) {
                        noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                        noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);
                    }

                    docInfo.setDocumentProperty("numero.affilie.formatte", noAff);
                } else {
                    docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Ass");
                }

                TIDocumentInfoHelper.fill(docInfo, decompteCourant.getIdTiers(), getSession(), IntRole.ROLE_APG, noAff,
                        noAffNonFormatte);

            }

        } catch (final RemoteException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Détermine et récupère le bon template de document en fonction de la caisse
     */
    private void definitLeTemplateEnFonctionCaisse() {
        try {
            // le modèle
            final String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
            if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                setTemplateFile(APAbstractDecomptesGenerationProcess.FICHIER_MODELE + extensionModelCaisse);
                final FWIImportManager im = getImporter();
                final File sourceFile = new File(
                        im.getImportPath() + im.getDocumentTemplate() + FWITemplateType.TEMPLATE_JASPER.toString());
                if (sourceFile != null && sourceFile.exists()) {
                    // NOTHING TO DO
                } else {
                    setTemplateFile(APAbstractDecomptesGenerationProcess.FICHIER_MODELE);
                }
            } else {
                setTemplateFile(APAbstractDecomptesGenerationProcess.FICHIER_MODELE);
            }
        } catch (final Exception e) {
            setTemplateFile(APAbstractDecomptesGenerationProcess.FICHIER_MODELE);
        }
    }

    /**
     * Methode pour retourner l'adresse de paiement formatée
     *
     * @return
     */
    public String getAdressePaiementAffilie(final BSession session, final String idTiers, String idAffilie)
            throws Exception {
        String descAdressePaiement = "";
        try {
            if (!JadeStringUtil.isBlankOrZero(idTiers)) {

                // Recherche du nom et prénom du tiers
                PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);

                // Si null, recherche d'une administration
                if (tiersWrapper == null) {
                    tiersWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
                }

                if (null != tiersWrapper) {
                    if (JadeStringUtil.isBlankOrZero(idAffilie)) {
                        idAffilie = "";
                    }

                    // recherche de l'adresse de paiement
                    final TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(getSession(),
                            (getSession()).getCurrentThreadTransaction(), idTiers, domaineDePaiement, idAffilie,
                            JACalendar.todayJJsMMsAAAA());

                    if ((adressePaiement != null) && !adressePaiement.isNew()) {
                        final TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                        source.load(adressePaiement);

                        ITIAdresseFormater tiAdrPaiBanFor;

                        // Selection du type de compte CCP
                        if (!JadeStringUtil.isEmpty(adressePaiement.getCcp())) {
                            descAdressePaiement = adressePaiement.getCcp();
                        }
                        // Selection du type de compte BANQUE
                        else if (!JadeStringUtil.isEmpty(adressePaiement.getCompte())) {
                            descAdressePaiement = adressePaiement.getDesignation1_banque();
                            if (!JadeStringUtil.isEmpty(adressePaiement.getDesignation2_banque())) {
                                descAdressePaiement = descAdressePaiement + " "
                                        + adressePaiement.getDesignation2_banque();
                            }
                            descAdressePaiement = descAdressePaiement + ", " + adressePaiement.getCompte();
                        }
                        // Selection du type de compte MANDAT POSTAL
                        else {
                            tiAdrPaiBanFor = new TIAdressePaiementBeneficiaireFormater();
                            descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), '\n', '\n');
                        }
                    }
                }
            }
            return descAdressePaiement;

        } catch (final Exception e) {
            throw new Exception(
                    e + ": " + "Erreur dans le chargement de la'dresse de paiement. APDecompte/getDescAdressePaiement");
        }
    }

    /**
     * @return the decompteCourant
     */
    public final APDecompte getDecompteCourant() {
        return decompteCourant;
    }

    /**
     * Methode pour retourner le détail journalier du décompte : nb de jours + montant journalier
     *
     * @return String
     * @param APRepartitionJointPrestation
     */
    private String getDetailJournalier(final APRepartitionJointPrestation repartition) throws Exception {

        String texteDetailJournalier = "";

        try {

            // Recupération du nombre de jours soldés
            final int nbJours = Integer.parseInt(repartition.getNbJoursSoldes());

            if (nbJours != 0) {
                // Récupération du montant brut
                final Double montantBrut = Double.valueOf(repartition.getMontantBrut());
                // Division du montant brut par le nombre de jours
                BigDecimal montantJournalier = BigDecimal.valueOf(montantBrut / nbJours);
                // Arrondi du montant brut à 2 décimal
                montantJournalier = montantJournalier.setScale(2, BigDecimal.ROUND_HALF_UP);

                // Récupération du texte dans le catalogue
                texteDetailJournalier = document.getTextes(3).getTexte(44).getDescription();
                // Insertion du nombre de jours dans le texte
                if (repartition.getGenreService().equals(APGenreServiceAPG.DecesDemiJour.getCodeSysteme())) {
                    texteDetailJournalier = PRStringUtils.replaceString(texteDetailJournalier, "{nbJours}",
                            NB_JOURS_DECES);
                } else {
                    texteDetailJournalier = PRStringUtils.replaceString(texteDetailJournalier, "{nbJours}",
                            Integer.toString(nbJours));
                }
                // Insertion du montant journalier dans le texte
                texteDetailJournalier = PRStringUtils.replaceString(texteDetailJournalier, "{mntJournalier}",
                        String.valueOf(montantJournalier));
            }

        } catch (final Exception e) {
            String message = getSession().getLabel("GENERATION_DECOMPTE_ERREUR_CHARGEMENT_DETAILS_JOURNALIER");
            message = message.replace("{0}", repartition.getIdTiers());
            message += " : ";
            message += e.toString();
            throw new Exception(message, e);
        }

        return texteDetailJournalier;
    }

    @Override
    protected String getEMailObject() {
        final StringBuilder builder = new StringBuilder("L'impression du document");

        if (isOnError()) {
            builder.append(" s'est terminée en erreur");
        } else {
            builder.append(" s'est terminée avec succès");
        }
        return builder.toString();
    }

    public abstract int getIdAssuranceAcParitaire();

    public abstract int getIdAssuranceAvsParitaire();

    public abstract int getIdAssuranceAvsPersonnelle();

    public abstract int getIdAssuranceFneParitaire();

    public abstract int getIdAssuranceLfaParitaire();

    public abstract int getIdAssuranceLfaPersonnelle();

    public abstract String getIdLot();

    public abstract boolean getIsAfficherConfidentielSurDocument();

    public abstract boolean getIsAfficherNIPSurDocument();

    public abstract boolean getIsBlankIndexGedNssAZero();

    public abstract boolean getIsDecompteRecapitulatif();

    public abstract boolean getIsNumeroAffiliePourGEDForceAZeroSiVide();

    public abstract boolean getIsSendToGED();

    public abstract boolean hasNextDocument() throws FWIException;

    private boolean isCaisse(final String noCaisse) throws Exception {
        return noCaisse.equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                .getProperty(CommonProperties.KEY_NO_CAISSE));
    }

    /**
     * Renseigne si on traite les ventilations par opposition au répartitions Return true si l'on traite les
     * ventilations Return false si on traite les répartitions
     *
     * @return <code>true</code> si on est dans la phase de traitement des ventilations
     */
    public abstract boolean isTraitementDesVentilations();

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public final boolean next() throws FWIException {
        hasNextDocument = hasNextDocument();
        return hasNextDocument;
    }

    /**
     * Récupère les données depuis la DB, réalise le regroupement. A la fin de cette méthode, on doit être prêt à itérer
     * via la méthode next()
     */
    public abstract void preparerDonneesPourDecomptes() throws Exception;

    private JadePublishDocumentInfo remplirDocInfoPourAssureAPGSpecifiqueCVCI(JadePublishDocumentInfo docInfo)
            throws Exception {
        if (getIsSendToGED()) {
            docInfo.setArchiveDocument(true);
            docInfo.setPublishDocument(false);

            // on cherche tous les employeurs de ce bénéficiaire
            // pour cela on utilise les situation prof. du droit des prestations du décompte courant
            final Map<String, APEmployeur> employeurs = new HashMap<String, APEmployeur>();

            for (final APRepartitionJointPrestation prest : decompteCourant.getRepartitionsPeres()) {

                // on cherche les sit. prof. de ce droit
                final APSituationProfessionnelleManager sitProMan = new APSituationProfessionnelleManager();
                sitProMan.setSession(getSession());
                sitProMan.setForIdDroit(prest.getIdDroit());
                sitProMan.find();

                for (int i = 0; i < sitProMan.size(); i++) {

                    final APSituationProfessionnelle sitPro = (APSituationProfessionnelle) sitProMan.get(i);

                    final APEmployeur employeur = new APEmployeur();
                    employeur.setSession(getSession());
                    employeur.setIdEmployeur(sitPro.getIdEmployeur());
                    employeur.retrieve();

                    // on ne considère que les employeurs qui ont un n° affilié
                    if (!JadeStringUtil.isIntegerEmpty(employeur.getIdAffilie())) {

                        // on ne considère que les employeur qui ont droit à des prestations correspondantes
                        // au type de document que l'on génère
                        if ((APTypeDeDecompte.NORMAL.equals(decompteCourant.getTypeDeDecompte()))
                                || (isTraitementDesVentilations())) {

                            if (!employeurs.containsKey(employeur.getIdAffilie())) {
                                employeurs.put(employeur.getIdAffilie(), employeur);
                            }
                        } else if ((APTypeDeDecompte.ACM_GE.equals(decompteCourant.getTypeDeDecompte()))) {
                            // la case ACM est cochée dans la sit. pro. normalement pas de paiement assuré
                            // pour les ACM (même cas pour les ACM 2, du coup on se base sur ACM1
                            if (sitPro.getHasAcmAlphaPrestations().booleanValue()) {
                                if (!employeurs.containsKey(employeur.getIdAffilie())) {
                                    employeurs.put(employeur.getIdAffilie(), employeur);
                                }
                            }

                        } else if (APTypeDeDecompte.AMAT_GE.equals(decompteCourant.getTypeDeDecompte())) {
                            // la case LAMat est cochée dans la sit. pro.
                            if (sitPro.getHasLaMatPrestations().booleanValue()) {
                                if (!employeurs.containsKey(employeur.getIdAffilie())) {
                                    employeurs.put(employeur.getIdAffilie(), employeur);
                                }
                            }
                        } else {
                            // Pas d'autres type de décompte pour la CVCI
                        }
                    }
                }

                // on envoie une copie pour chacun des employeurs sélectionnés
                for (final APEmployeur affiliecourant : employeurs.values()) {

                    // on défini les propriétés du DocInfo pour l'archivage
                    if (affiliecourant != null) {
                        final IPRAffilie aff = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(),
                                getTransaction(), affiliecourant.getIdAffilie(), affiliecourant.getIdTiers());

                        // Dans JadeClientServiceLocator, globaz.apg.itext.APDecomptesAss ou
                        // globaz.apg.itext.APDecomptesAff pour respectivement l'assuré ou l'affilié.
                        // 2 entrées différentes pour différencier le type de document (DTID)
                        docInfo.setDocumentType("globaz.apg.itext.APAbstractDecomptesGenerationProcess" + "Aff");

                        try {
                            // SI APG
                            if (IPRDemande.CS_TYPE_APG.equals(getCSTypePrestationsLot())) {
                                docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_APG_TITLE"));
                                // SI MATERNITE
                            } else {
                                docInfo.setDocumentTitle(getSession().getLabel("DOC_DECOMPTE_MAT_TITLE"));
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        docInfo.setDocumentDate(
                                PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(getDateDocument().toStrAMJ()));

                        if (!JadeStringUtil.isIntegerEmpty(affiliecourant.getIdAffilie())) {
                            // Employeur affilié --> rôle AFFILIE
                            if (aff != null) {

                                final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem()
                                        .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

                                TIDocumentInfoHelper.fill(docInfo, affiliecourant.getIdTiers(), getSession(),
                                        ITIRole.CS_AFFILIE, aff.getNumAffilie(),
                                        affilieFormatter.unformat(aff.getNumAffilie()));

                                if (getIsSendToGED()) {
                                    // création / mise à jour du dossier GroupDoc
                                    GroupdocPropagateUtil.propagateData(aff, null, null);
                                }

                                // Si n°AVS est vide, le remplacer par des '0'
                                final String avsnf = docInfo
                                        .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                                final String avsf = docInfo
                                        .getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

                                if (JadeStringUtil.isBlankOrZero(avsnf) || JadeStringUtil.isBlankOrZero(avsf)) {
                                    docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(),
                                            docInfo);
                                }

                                // on ajoute au doc info le critère de tri pour les impressions
                                // ORDER_PRINTING_BY toujours selon le décompte courant (Assuré)
                                docInfo.setDocumentProperty(APAbstractDecomptesGenerationProcess.ORDER_PRINTING_BY,
                                        buildOrderPrintingByKey(decompteCourant.getIdAffilie(),
                                                decompteCourant.getIdTiers()));

                                // on ajoute le document à la liste des documents attachés
                                docInfo.setPublishDocument(false);
                                super.registerAttachedDocument(docInfo, getExporter().getExportNewFilePath());
                            }
                        }
                    }
                }
            }

            getDocumentInfo().setArchiveDocument(false);
            getDocumentInfo().setPublishDocument(false);
        }

        return docInfo;
    }

    public void setCatalogueTextesCourant(final ICTDocument document) {
        this.document = document;
    }

    public abstract void setDateComptable(final JADate string);

    public abstract void setDateDocument(final JADate date);

    /**
     * @param decompteCourant
     *                            the decompteCourant to set
     */
    public final void setDecompteCourant(final APDecompte decompteCourant) {
        this.decompteCourant = decompteCourant;
    }

    public abstract void setIdLot(final String idLot);

    public abstract void setIsSendToGED(final boolean sentToGed);

}
