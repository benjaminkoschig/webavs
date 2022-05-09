package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REListerEcheanceRenteJoinMembresFamille;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.REListerEcheancesProcess;
import globaz.corvus.utils.REGedUtils;
import globaz.corvus.utils.REGedUtils.TypeRente;
import globaz.corvus.utils.RENumberFormatter;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.topaz.datajuicer.DocumentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REEcheanceRenteOO extends AbstractJadeJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(REEcheanceRenteOO.class);

    private static final long serialVersionUID = 1L;
    private static final String CDT_AGEAVS = "{AgeAVS}";
    private static final String CDT_ANNEESALAIRE = "{AnneeSalaire}";
    private static final String CDT_BENEFICIAIRE = "{beneficiaire}";
    private static final String CDT_PRENOM_NOM_ENFANT = "{PrenomNomEnfant}";
    private static final String CDT_NSS_PRENOM_NOM_ENFANT = "{Nss-PrenomNomEnfant}";
    private static final String CDT_DATEECHEANCE = "{DateEcheance}";
    private static final String CDT_DATENAISSANCE = "{dateNaissance}";
    private static final String CDT_DERNIERJOURSMOIS = "{DernierJoursMois}";
    private static final String CDT_GENTREPREST = "{GenrePrestation}";
    private static final String CDT_MOISANNIVERSAIRE = "{MoisAnniversaire}";
    private static final String CDT_MOISECHEANCE = "{MoisEcheance}";
    private static final String CDT_MOISSUIVANTECHE = "{MoisSuivantEcheance}";
    private static final String CDT_MONTANTPREST = "{MontantPrestation}";
    private static final String CDT_TITRE = "{titre}";
    private static final String NUM_CAISSE_FERCIAM = "106";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ECHEANCE_RENTE";

    private TIAdressePaiementData adr;
    private String adresse;
    private String afficherDossierTraitePar;
    private boolean ajournementGED;
    private JadePrintDocumentContainer allDoc;
    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue;
    private DocumentData data;
    private ICTDocument document;
    private ICTDocument documentHelper;
    private REListerEcheanceRenteJoinMembresFamille echeanceCourrante;
    private boolean echeanceEtudeGED;
    private List<REListerEcheanceRenteJoinMembresFamille> echeances;
    private Iterator<REListerEcheanceRenteJoinMembresFamille> echeancesIterator;
    private String eMailAddress;
    private boolean enfantDe18ansGED;
    private boolean enfantDe25ansGED;
    private boolean femmeArrivantAgeVieillesseGED;
    private boolean hasNextEcheance;
    private boolean hommeArrivantAgeVieillesseGED;
    private String moisEcheance;
    private String nomBeneficiaire;
    private boolean renteDeVeufGED;
    private boolean echeanceEnfantRecueilliGratuitementGED;
    private PRTiersWrapper tiersAdresse;
    private PRTiersWrapper tiersBeneficiaire;
    private PRTiersWrapper tiersAdressePaiement;
    private String titre;

    public REEcheanceRenteOO() {
        super();

        adr = null;
        adresse = "";
        afficherDossierTraitePar = "";
        ajournementGED = false;
        allDoc = null;
        caisseHelper = null;
        codeIsoLangue = "";
        data = null;
        document = null;
        documentHelper = null;
        echeanceCourrante = null;
        echeanceEtudeGED = false;
        echeances = null;
        echeancesIterator = null;
        eMailAddress = "";
        enfantDe18ansGED = false;
        enfantDe25ansGED = false;
        femmeArrivantAgeVieillesseGED = false;
        hasNextEcheance = false;
        hommeArrivantAgeVieillesseGED = false;
        moisEcheance = "";
        nomBeneficiaire = "";
        renteDeVeufGED = false;
        echeanceEnfantRecueilliGratuitementGED = false;
        tiersAdresse = null;
        tiersBeneficiaire = null;
        titre = "";
    }

    private void addWarningMessageToMail(REListerEcheanceRenteJoinMembresFamille echeance, Exception exception) {
        StringBuilder message = new StringBuilder();
        message.append(echeance.getNss());
        message.append(" - ");
        message.append(getSession().getLabel("ERREUR_GENERATION_LETTRE_POUR_MOTIF"));
        message.append(" [");
        message.append(echeance.getMotifLettre());
        message.append("]. ");
        message.append(getSession().getLabel("MESSAGE_ERREUR"));
        message.append(" : ");
        message.append(exception.getMessage());
        getLogSession().addMessage(
                new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, this.getClass().getSimpleName(), message
                        .toString()));
    }

    private void cacherAnnexes() {
        data.addData("TEXTE_ANNEXES", null);
        data.addData("POINTS", null);
        data.addData("VALEUR_ANNEXES", null);
    }

    private void chargementCatalogueTexte() throws Exception {
        // Si j'ai un tiersAdresse, je s�lectionne le codeIsoLangue de ce tiers, dans le cas contraire ce sera celui du
        // tiers b�n�ficiaire
        if (tiersAdresse != null) {
            codeIsoLangue = getSession().getCode(tiersAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        } else if (tiersAdressePaiement != null) {
            codeIsoLangue = getSession().getCode(tiersAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        } else {
            codeIsoLangue = getSession().getCode(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        }

        // creation du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_ECHEANCE);
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

    private void chargementDonneesLettre(boolean hasDoubleRowObject) throws Exception {
        if (null != tiersBeneficiaire) {
            nomBeneficiaire = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            throw new Exception(getSession().getLabel("ERREUR_BENEFICIAIRE_RENTE"));
        }

        if (REMotifEcheance.Echeance18ans.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonneesMotif18ans(hasDoubleRowObject);
        } else if (REMotifEcheance.Echeance25ans.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonneesMotif25ans(hasDoubleRowObject);
        } else if (REMotifEcheance.EcheanceFinEtudes.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonneesMotifFinEtudes(hasDoubleRowObject);
        } else if (ifMotifAgeAVS(echeanceCourrante)) {
            chargementDonneesMotifAgeAVS(echeanceCourrante.getMotifLettre());
        } else if (REMotifEcheance.ConjointArrivantAgeAvs.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonneesMotifAgeAVSConjoint();
        } else if (REMotifEcheance.RenteDeVeuf.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonn�esMotifVeuf();
        } else if (REMotifEcheance.EcheanceEnfantRecueilliGratuitement.name().equals(echeanceCourrante.getMotifLettre())) {
            chargementDonneesMotifEcheanceEnfantRecueilliGratuitement();
        } else {
            // Pas de signalement de ces cas
            // String message = this.getSession().getLabel("AUCUNE_LETTRE_GENEREE_POUR_MOTIF_SUIVANT");
            // throw new Exception(message + " [" + this.echeanceCourrante.getMotifLettre() + "]");
        }
    }

    private void chargementDonneesMotifEcheanceEnfantRecueilliGratuitement() throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteEnfantOO");

        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        // Traitement du titre, avec le nom/pr�nom de l'enfant et la date de naissance
        String concerne = PRStringUtils.replaceString(document.getTextes(10).getTexte(1).getDescription(),
                REEcheanceRenteOO.CDT_PRENOM_NOM_ENFANT, nomBeneficiaire);

        concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_DATENAISSANCE,
                JACalendar.format(echeanceCourrante.getDateNaissance(), codeIsoLangue));

        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Traitement du paragraphe 1, avec le montant de la prestation
        String actuellement = PRStringUtils.replaceString(document.getTextes(10).getTexte(2).getDescription(),
                REEcheanceRenteOO.CDT_MONTANTPREST,
                echeanceCourrante.getMontantPrestation());
        data.addData("LETTRE_PARA1", actuellement);

        // Traitement du paragraphe 2
        data.addData("LETTRE_PARA2", document.getTextes(10).getTexte(3).getDescription());

        // Traitement du paragraphe 3
        data.addData("LETTRE_PARA3", document.getTextes(10).getTexte(4).getDescription());

        // Traitement du paragraphe 4, avec la valeur du dernier jour du mois de l'�ch�ance
        JACalendarGregorian jaCalGre = new JACalendarGregorian();
        // Calcul du dernier jours du mois
        JADate dernierJourMois = jaCalGre.addDays(jaCalGre.addMonths(new JADate("01." + getMoisEcheance()), 1), -1);
        // Insertion du dernier jours du mois dans le texte
        String paraAvecDateEcheance = PRStringUtils.replaceString(document.getTextes(10).getTexte(5).getDescription(),
                REEcheanceRenteOO.CDT_DERNIERJOURSMOIS,
                JACalendar.format(dernierJourMois, JACalendar.FORMAT_DDsMMsYYYY));
        // Insertion du texte dans le doc
        data.addData("LETTRE_PARA4", paraAvecDateEcheance);

        // Traitement du paragraphe 5, avec le nom du b�n�ficiare
        String vivezVous = PRStringUtils.replaceString(document.getTextes(10).getTexte(6).getDescription(),
                REEcheanceRenteOO.CDT_NSS_PRENOM_NOM_ENFANT, echeanceCourrante.getNss() + " - " + nomBeneficiaire);
        data.addData("LETTRE_PARA5", vivezVous);

        // Traitement du paragraphe 6, case � cocher OUI / NON
        String espaceAvant = " ";
        String espaceApres = "                              ";
        data.addData("LETTRE_PARA6_BOX", espaceAvant + document.getTextes(10).getTexte(19).getDescription() + espaceApres);
        data.addData("LETTRE_PARA6", document.getTextes(10).getTexte(7).getDescription());
        data.addData("LETTRE_PARA6_SUITE1", document.getTextes(10).getTexte(8).getDescription());

        // Traitement du paragraphe 7, r�ponse OUI
        data.addData("LETTRE_PARA7", document.getTextes(10).getTexte(9).getDescription());
        data.addData("LETTRE_PARA7_SUITE1", document.getTextes(10).getTexte(11).getDescription());

        // Traitement du paragraphe 8, rR�ponse NON
        data.addData("LETTRE_PARA8", document.getTextes(10).getTexte(10).getDescription());
        data.addData("LETTRE_PARA8_SUITE1",  document.getTextes(10).getTexte(11).getDescription());

        // Traitement du paragraphe 9, certifie, homme/femme en fonction du tiers de l'adresse
        String certifie = "";
        if (PRACORConst.CS_HOMME.equals(tiersAdressePaiement.getSexe())) {
            certifie = document.getTextes(10).getTexte(12).getDescription();
        } else {
            certifie = document.getTextes(10).getTexte(13).getDescription();
        }
        data.addData("LETTRE_PARA9", certifie);

        // Traitement paragraphe 10, Lieu date, signature
        data.addData("LETTRE_PARA10", document.getTextes(10).getTexte(14).getDescription() + "  " + document.getTextes(10).getTexte(15).getDescription());

        // Traitement paragraphe 11, Salutations
        String salutations = PRStringUtils.replaceString(document.getTextes(10).getTexte(16).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA11", salutations);

        chargementEnTeteEtSignatureLettre(data);

        JadePublishDocumentInfo pubInfoEnfantRecueilliGratuitement = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfoEnfantRecueilliGratuitement.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoEnfantRecueilliGratuitement.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoEnfantRecueilliGratuitement.setOwnerEmail(getEMailAddress());
        pubInfoEnfantRecueilliGratuitement.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        pubInfoEnfantRecueilliGratuitement.setArchiveDocument(getEcheanceEnfantRecueilliGratuitementGED());

        try {
            if (getEcheanceEnfantRecueilliGratuitementGED()) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concern�e (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfoEnfantRecueilliGratuitement = h.setNssExtraFolderToDocInfo(getSession(), pubInfoEnfantRecueilliGratuitement,
                            echeanceCourrante.getIdTiers());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Une erreur est intervenue lors du chargement en GED de la lettre d'�ch�ance pour enfant recueilli gratuitement.", e);
        }

        pubInfoEnfantRecueilliGratuitement.setPublishDocument(false);
        pubInfoEnfantRecueilliGratuitement.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_ENFANT_RECUEILLI_GRATUITEMENT);
        pubInfoEnfantRecueilliGratuitement.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_ENFANT_RECUEILLI_GRATUITEMENT);
        pubInfoEnfantRecueilliGratuitement.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation()) && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
            Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
            codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
            pubInfoEnfantRecueilliGratuitement.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
        }

        TIDocumentInfoHelper.fill(pubInfoEnfantRecueilliGratuitement, getEcheanceCourrante().getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfoEnfantRecueilliGratuitement);

    }

    private void chargementDonneesMotif18ans(boolean hasDoubleRowObject) throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");

        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        String concerne = resolveDocumentObject(rd, echeanceCourrante);

        if (hasDoubleRowObject && !echeanceCourrante.getListeEcheanceLiees().isEmpty()) {
            // il ne peut y avoir que 2 objets
            String concerne2 = resolveDocumentObject(rd, echeanceCourrante.getListeEcheanceLiees().get(0));
            concerne += " / " + concerne2;
        }

        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Traitement du paragraphe 1, ajout d'un exposant

        if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getCsSexe())) {
            data.addData("LETTRE_PARA1", document.getTextes(3).getTexte(1).getDescription());
        } else {
            data.addData("LETTRE_PARA1", document.getTextes(3).getTexte(13).getDescription());
        }

        data.addData("LETTRE_PARA1_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA1_SUITE1", document.getTextes(3).getTexte(2).getDescription());

        // Aucun traitement pour le paragraphe 2
        data.addData("LETTRE_PARA2", document.getTextes(3).getTexte(3).getDescription());

        // Traitement paragraphe 3, j'insere la valeur du mois d'anniversaire du tiers et l'exposant

        String nomMois = "";
        if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getCsSexe())) {
            nomMois = PRStringUtils.replaceString(document.getTextes(3).getTexte(4).getDescription(),
                    REEcheanceRenteOO.CDT_MOISANNIVERSAIRE,
                    JACalendar.getMonthName(Integer.parseInt(getMoisEcheance().substring(0, 2)), codeIsoLangue));
        } else {
            nomMois = PRStringUtils.replaceString(document.getTextes(3).getTexte(12).getDescription(),
                    REEcheanceRenteOO.CDT_MOISANNIVERSAIRE,
                    JACalendar.getMonthName(Integer.parseInt(getMoisEcheance().substring(0, 2)), codeIsoLangue));
        }
        data.addData("LETTRE_PARA3", nomMois);
        data.addData("LETTRE_PARA3_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA3_SUITE1", document.getTextes(3).getTexte(5).getDescription());

        // Traitement du paragraphe 4, j'insere la valeur du dernier jour du mois de l'�ch�ance
        JACalendarGregorian jaCalGre = new JACalendarGregorian();
        // Calcul du dernier jours du mois
        JADate dernierJourMois = jaCalGre.addDays(jaCalGre.addMonths(new JADate("01." + getMoisEcheance()), 1), -1);
        // Insertion du dernier jours du mois dans le texte
        String paraAvecDate = PRStringUtils.replaceString(document.getTextes(3).getTexte(6).getDescription(),
                REEcheanceRenteOO.CDT_DERNIERJOURSMOIS,
                JACalendar.format(dernierJourMois, JACalendar.FORMAT_DDsMMsYYYY));
        // Insertion du texte dans le doc
        data.addData("LETTRE_PARA4", paraAvecDate);

        // Traitement du pragraphe 5, j'insere un exposant
        data.addData("LETTRE_PARA5", document.getTextes(3).getTexte(7).getDescription());
        data.addData("LETTRE_PARA5_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA5_SUITE1", document.getTextes(3).getTexte(8).getDescription());

        // Aucuns traitement pour les paragraphes 6 et 7
        data.addData("LETTRE_PARA6", document.getTextes(3).getTexte(9).getDescription());
        data.addData("LETTRE_PARA7", document.getTextes(3).getTexte(10).getDescription());

        // Traitement du paragraphe 8, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(3).getTexte(11).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA8", titretier);

        chargementEnTeteEtSignatureLettre(data);

        JadePublishDocumentInfo pubInfo18ans = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfo18ans.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfo18ans.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfo18ans.setOwnerEmail(getEMailAddress());
        pubInfo18ans.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        pubInfo18ans.setArchiveDocument(getEnfantDe18ansGED());

        try {
            if (getEnfantDe18ansGED()) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concern�e (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfo18ans = h.setNssExtraFolderToDocInfo(getSession(), pubInfo18ans,
                            echeanceCourrante.getIdTiers());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pubInfo18ans.setPublishDocument(false);
        pubInfo18ans.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_ENFANT_18_ANS);
        pubInfo18ans.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_ENFANT_18_ANS);
        pubInfo18ans.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation()) && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
            Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
            codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
            pubInfo18ans.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
        }

        TIDocumentInfoHelper.fill(pubInfo18ans, getEcheanceCourrante().getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfo18ans);

    }

    private String resolveDocumentObject(RERenteAccordee rd, REListerEcheanceRenteJoinMembresFamille echeanceToResolve)
            throws Exception {
        String concerne = "";

        // Traitement du concerne, j'insere les valeurs type de rente, montant, prenom/nom et date anniversaire en
        // fonction du sexe de l'enfant
        if (PRACORConst.CS_HOMME.equals(echeanceToResolve.getCsSexe())) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(1).getDescription(),
                    REEcheanceRenteOO.CDT_BENEFICIAIRE, nomBeneficiaire);
        } else if (PRACORConst.CS_FEMME.equals(echeanceToResolve.getCsSexe())) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(2).getDescription(),
                    REEcheanceRenteOO.CDT_BENEFICIAIRE, nomBeneficiaire);
        } else {
            throw new Exception("Internal error : Le sexe du b�n�ficiaire n'a pas pu �tre trouv�");
        }

        String pourRechercheCodeSysteme = getRERechercheCodeSystem(echeanceToResolve);

        // R�cup�ration du code syst�me en fonction de codeIsoLangue et non en fonction de la langue de l'utilisateur
        String libelle = RENumberFormatter.codeSystemToLibelle(
                getSession().getSystemCode("REGENRPRST", pourRechercheCodeSysteme),
                codeIsoLangue, getSession());
        // Ajout du mot (AVS) ou (AI) � la fin du genre de prestation (utilis� pour le classement par les caisses)
        if (rd.isRAVieillesse().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_GENTREPREST, libelle
                    + " " + document.getTextes(9).getTexte(4));
        } else if (rd.isRAInvalidite().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_GENTREPREST, libelle
                    + " " + document.getTextes(9).getTexte(5));
        } else {
            concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_GENTREPREST, libelle);
        }

        concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_MONTANTPREST,
                echeanceToResolve.getMontantPrestation());
        concerne = PRStringUtils.replaceString(concerne.toString(), REEcheanceRenteOO.CDT_DATENAISSANCE,
                JACalendar.format(echeanceToResolve.getDateNaissance(), codeIsoLangue));

        return concerne;
    }

    private void chargementDonneesMotif25ans(boolean hasDoubleRowObject) throws Exception {

        data = new DocumentData();

        data.addData("idProcess", "REEcheanceRenteOO");

        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        String concerne = resolveDocumentObject(rd, echeanceCourrante);

        if (hasDoubleRowObject && !echeanceCourrante.getListeEcheanceLiees().isEmpty()) {
            // il ne peut y avoir que 2 objets
            String concerne2 = resolveDocumentObject(rd, echeanceCourrante.getListeEcheanceLiees().get(0));
            concerne += " / " + concerne2;
        }

        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Acuns traitemenets pour le paragraphe 1
        data.addData("LETTRE_PARA1", document.getTextes(4).getTexte(1).getDescription());

        // Traitement paragraphe 2, j'insere les valeurs du mois d'anniversaire, le dernier jour du mois et un exposant
        // insertion du mois d'anniversaire
        String paraAvecDateAnniversaire = "";
        if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getCsSexe())) {
            paraAvecDateAnniversaire = PRStringUtils.replaceString(document.getTextes(4).getTexte(2).getDescription(),
                    REEcheanceRenteOO.CDT_MOISANNIVERSAIRE,
                    JACalendar.getMonthName(Integer.parseInt(getMoisEcheance().substring(0, 2)), codeIsoLangue));
        } else {
            paraAvecDateAnniversaire = PRStringUtils.replaceString(document.getTextes(4).getTexte(6).getDescription(),
                    REEcheanceRenteOO.CDT_MOISANNIVERSAIRE,
                    JACalendar.getMonthName(Integer.parseInt(getMoisEcheance().substring(0, 2)), codeIsoLangue));
        }
        data.addData("LETTRE_PARA2", paraAvecDateAnniversaire);
        data.addData("LETTRE_PARA2_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        // Traitement du dernier jour du mois
        JACalendarGregorian jaCalGre = new JACalendarGregorian();
        // Calcul du dernier jours du mois
        JADate dernierJourMois = jaCalGre.addDays(jaCalGre.addMonths(new JADate("01." + getMoisEcheance()), 1), -1);
        // Insertion du dernier jours du mois dans le texte
        String paraAvecDate = PRStringUtils.replaceString(document.getTextes(4).getTexte(3).getDescription(),
                REEcheanceRenteOO.CDT_DERNIERJOURSMOIS,
                JACalendar.format(dernierJourMois, JACalendar.FORMAT_DDsMMsYYYY));
        // Insertion du texte dans le doc
        data.addData("LETTRE_PARA2_SUITE1", paraAvecDate);

        // Aucuns traitements pour le paragraphe 3
        data.addData("LETTRE_PARA3", document.getTextes(4).getTexte(4).getDescription());

        // Traitement du paragraphe 4, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(4).getTexte(5).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA4", titretier);

        chargementEnTeteEtSignatureLettre(data);

        JadePublishDocumentInfo pubInfo25ans = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfo25ans.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfo25ans.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfo25ans.setOwnerEmail(getEMailAddress());
        pubInfo25ans.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        pubInfo25ans.setArchiveDocument(getEnfantDe25ansGED());

        try {
            if (getEnfantDe25ansGED()) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concern�e (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfo25ans = h.setNssExtraFolderToDocInfo(getSession(), pubInfo25ans,
                            echeanceCourrante.getIdTiers());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pubInfo25ans.setPublishDocument(false);
        pubInfo25ans.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_ENFANT_25_ANS);
        pubInfo25ans.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_ENFANT_25_ANS);
        pubInfo25ans.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation()) && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
            Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
            codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
            pubInfo25ans.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
        }

        TIDocumentInfoHelper.fill(pubInfo25ans, getEcheanceCourrante().getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfo25ans);
    }

    /**
     * Pr�pare la cha�ne pour retrouver le code syst�me avec .0 ou .1 ou autres fractions selon les r�gles analys�es
     *
     * @param ra    Rente accord�
     * @return      La cha�ne permettant de chercher le code syst�me
     */
    private String getRERechercheCodeSystem(REListerEcheanceRenteJoinMembresFamille ra){
        String pourRechercheCodeSysteme = ra.getCodePrestation();

        if (Arrays.stream(REGenresPrestations.GENRE_PRESTATIONS_AI).anyMatch(genrePrestation -> genrePrestation.equals(ra.getCodePrestation()))) {
            if (!JadeStringUtil.isEmpty(ra.getFractionRente())) {
                pourRechercheCodeSysteme += "." + ra.getFractionRente();
            } else if (!JadeStringUtil.isEmpty(ra.getQuotiteRente())) {
                if (REGenresPrestations.GENRE_50.equals(ra.getCodePrestation()) || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())) {
                    if (Float.parseFloat(ra.getQuotiteRente()) >= 0.70) {
                        pourRechercheCodeSysteme += ".1";
                    } else {
                        pourRechercheCodeSysteme += ".0";
                    }
                } else {
                    pourRechercheCodeSysteme += ".1";
                }
            } else {
                pourRechercheCodeSysteme += ".0";
            }
        } else {
            pourRechercheCodeSysteme += ".0";
        }
        return pourRechercheCodeSysteme;
    }

    private void chargementDonneesMotifAgeAVS(String motif) throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");
        String concerne = "";

        // Traitement du concerne, j'insere les valeurs type de rente, montant, prenom/nom et date anniversaire en
        // fonction du sexe de l'enfant
        String pourRechercheCodeSysteme = getRERechercheCodeSystem(echeanceCourrante);

        // Recuperation du code syst�me en fonction de codeIsoLangue et non en fonction de la langue de l'utilisateur
        String libelle = RENumberFormatter.codeSystemToLibelle(
                getSession().getSystemCode("REGENRPRST", pourRechercheCodeSysteme),
                codeIsoLangue, getSession());
        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        // Ajout du mot (AVS) ou (AI) � la fin du genre de prestation (utilis� pour le classement par les caisses)
        if (rd.isRAVieillesse().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle + " " + document.getTextes(9).getTexte(4));
        } else if (rd.isRAInvalidite().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle + " " + document.getTextes(9).getTexte(5));
        } else {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle);
        }

        concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_MONTANTPREST,
                echeanceCourrante.getMontantPrestation());
        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Traitement du paragraphe 1 en fonction du motif, j'insere le mois suivante la date d'�ch�ance
        String paraAvecDateEcheance = "";
        JACalendarGregorian c = new JACalendarGregorian();
        JADate j = new JADate("01." + getMoisEcheance());
        String dateEcheance = JACalendar.format(c.addMonths(j, 1).toString(), codeIsoLangue);

        if (REMotifEcheance.FemmeArrivantAgeAvs.name().equals(motif)
                || REMotifEcheance.HommeArrivantAgeAvs.name().equals(motif)) {
            paraAvecDateEcheance = PRStringUtils.replaceString(document.getTextes(7).getTexte(1).getDescription(),
                    REEcheanceRenteOO.CDT_MOISSUIVANTECHE, dateEcheance);
        }

        else {
            paraAvecDateEcheance = PRStringUtils.replaceString(document.getTextes(7).getTexte(2).getDescription(),
                    REEcheanceRenteOO.CDT_MOISSUIVANTECHE, dateEcheance);
        }
        data.addData("LETTRE_PARA1", paraAvecDateEcheance);

        // Traitement du paragraphe 2, le texte change en fonction de la propri�t� du fichier corvus.properties
        String agComu = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).getProperty(
                "isCopieAgenceCommunale");

        if (agComu.equals("true")) {
            data.addData("LETTRE_PARA2", document.getTextes(7).getTexte(12).getDescription());
        } else {
            data.addData("LETTRE_PARA2", document.getTextes(7).getTexte(3).getDescription());
        }

        // Aucun traitement pour le paragraphe 3
        data.addData("LETTRE_PARA3", document.getTextes(7).getTexte(4).getDescription());

        // Traitement du paragraphe des puces, j'insere l'annee du salaire et les puces
        JACalendar j2 = new JACalendarGregorian();
        data.addData("LETTRE_PUCE1_A", document.getTextes(7).getTexte(5).getDescription());
        data.addData("LETTRE_PUCE1_B", document.getTextes(7).getTexte(9).getDescription());
        data.addData("LETTRE_PUCE1_C", document.getTextes(7).getTexte(10).getDescription());
        data.addData("LETTRE_PUCE1_D", PRStringUtils.replaceString(document.getTextes(7).getTexte(11).getDescription(),
                REEcheanceRenteOO.CDT_ANNEESALAIRE,
                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(j2.addYears(getMoisEcheance().substring(2), -1))));

        // Aucun traitement pour le paragraphe 4
        data.addData("LETTRE_PARA4", document.getTextes(7).getTexte(6).getDescription());

        // Aucun traitement pour le paragraphe 5
        data.addData("LETTRE_PARA5", document.getTextes(7).getTexte(7).getDescription());

        // Traitement du pragraphe 6, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(7).getTexte(8).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA6", titretier);

        // Taitement de l'annexe
        data.addData("TEXTE_ANNEXES", document.getTextes(9).getTexte(1).getDescription());
        data.addData("POINTS", document.getTextes(9).getTexte(2).getDescription());
        data.addData("VALEUR_ANNEXES", document.getTextes(9).getTexte(3).getDescription());

        if (PRACORConst.CA_FEMME.equals(echeanceCourrante.getCsSexe())) {

            chargementEnTeteEtSignatureLettre(data);

            JadePublishDocumentInfo pubInfoFAVSConjoint = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfoFAVSConjoint.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoFAVSConjoint.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoFAVSConjoint.setOwnerEmail(getEMailAddress());
            pubInfoFAVSConjoint.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfoFAVSConjoint.setArchiveDocument(getFemmeArrivantAgeVieillesseGED());

            try {
                if (getFemmeArrivantAgeVieillesseGED()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concern�e (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfoFAVSConjoint = h.setNssExtraFolderToDocInfo(getSession(), pubInfoFAVSConjoint,
                                echeanceCourrante.getIdTiers());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pubInfoFAVSConjoint.setPublishDocument(false);
            pubInfoFAVSConjoint.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_FEMME_VIEILLESSE);
            pubInfoFAVSConjoint.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_FEMME_VIEILLESSE);
            pubInfoFAVSConjoint.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

            if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation())
                    && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
                Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
                codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
                pubInfoFAVSConjoint.setDocumentProperty(
                        REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                        REGedUtils.getCleGedPourTypeRente(getSession(),
                                REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
            }

            TIDocumentInfoHelper.fill(pubInfoFAVSConjoint, getEcheanceCourrante().getIdTiers(), getSession(), null,
                    null, null);

            allDoc.addDocument(data, pubInfoFAVSConjoint);

        } else {
            chargementEnTeteEtSignatureLettre(data);

            JadePublishDocumentInfo pubInfoHAVSConjoint = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfoHAVSConjoint.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoHAVSConjoint.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoHAVSConjoint.setOwnerEmail(getEMailAddress());
            pubInfoHAVSConjoint.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfoHAVSConjoint.setArchiveDocument(getHommeArrivantAgeVieillesseGED());

            try {
                if (getHommeArrivantAgeVieillesseGED()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concern�e (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfoHAVSConjoint = h.setNssExtraFolderToDocInfo(getSession(), pubInfoHAVSConjoint,
                                echeanceCourrante.getIdTiers());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pubInfoHAVSConjoint.setPublishDocument(false);
            pubInfoHAVSConjoint.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_HOMME_VIEILLESSE);
            pubInfoHAVSConjoint.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_HOMME_VIEILLESSE);
            pubInfoHAVSConjoint.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

            if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation())
                    && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
                Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
                codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
                pubInfoHAVSConjoint.setDocumentProperty(
                        REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                        REGedUtils.getCleGedPourTypeRente(getSession(),
                                REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
            }

            TIDocumentInfoHelper.fill(pubInfoHAVSConjoint, getEcheanceCourrante().getIdTiers(), getSession(), null,
                    null, null);

            allDoc.addDocument(data, pubInfoHAVSConjoint);
        }
    }

    private void chargementDonneesMotifAgeAVSConjoint() throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");
        String concerne = "";

        // Traitement du concerne, j'insere les valeurs type de rente, montant, prenom/nom et date anniversaire en
        // fonction du sexe de l'enfant
        String pourRechercheCodeSysteme = getRERechercheCodeSystem(echeanceCourrante);

        String libelle = RENumberFormatter.codeSystemToLibelle(
                getSession().getSystemCode("REGENRPRST", pourRechercheCodeSysteme),
                codeIsoLangue, getSession());

        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        // Ajout du mot (AVS) ou (AI) � la fin du genre de prestation (utilis� pour le classement par les caisses)
        if (rd.isRAVieillesse().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle + " " + document.getTextes(9).getTexte(4));
        } else if (rd.isRAInvalidite().equals(Boolean.TRUE)) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle + " " + document.getTextes(9).getTexte(5));
        } else {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    REEcheanceRenteOO.CDT_GENTREPREST, libelle);
        }

        concerne = PRStringUtils.replaceString(concerne, REEcheanceRenteOO.CDT_MONTANTPREST,
                echeanceCourrante.getMontantPrestation());
        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Aucuns traitement pour le paragraphe 1
        data.addData("LETTRE_PARA1", document.getTextes(6).getTexte(1).getDescription());

        // Traitement du paragraphe 2, j'insere les deux exposants et la date age AVS
        String para2 = "";
        if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getSexeConjoint())) {
            para2 = PRStringUtils.replaceString(document.getTextes(6).getTexte(2).getDescription(),
                    REEcheanceRenteOO.CDT_AGEAVS, Integer.toString(REListerEcheancesProcess.AGE_AVS_HOMME));
        } else if (PRACORConst.CS_FEMME.equals(echeanceCourrante.getSexeConjoint())) {
            para2 = PRStringUtils.replaceString(document.getTextes(6).getTexte(15).getDescription(),
                    REEcheanceRenteOO.CDT_AGEAVS, Integer.toString(REListerEcheancesProcess.AGE_AVS_FEMME));
        } else {
            throw new Exception(getSession().getLabel("IMPOSSIBLE_RETROUVER_SEXE_CONJOINT") + " "
                    + echeanceCourrante.getNss());
        }

        data.addData("LETTRE_PARA2", para2);
        data.addData("LETTRE_PARA2_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA2_SUITE1", document.getTextes(6).getTexte(3).getDescription());
        data.addData("LETTRE_PARA2_EXPOSANT2", document.getTextes(2).getTexte(6).getDescription());
        data.addData("LETTRE_PARA2_SUITE2", document.getTextes(6).getTexte(4).getDescription());

        // Traitement paragraphe3, texte change en fonction de la properties agence communale
        String agComu = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).getProperty(
                "isCopieAgenceCommunale");
        if (agComu.equals("true")) {
            if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getSexeConjoint())) {
                data.addData("LETTRE_PARA3", document.getTextes(6).getTexte(14).getDescription());
            } else if (PRACORConst.CS_FEMME.equals(echeanceCourrante.getSexeConjoint())) {
                data.addData("LETTRE_PARA3", document.getTextes(6).getTexte(16).getDescription());
            } else {
                throw new Exception(getSession().getLabel("IMPOSSIBLE_RETROUVER_SEXE_CONJOINT") + " "
                        + echeanceCourrante.getNss());
            }
        } else {
            data.addData("LETTRE_PARA3", document.getTextes(6).getTexte(5).getDescription());
        }

        // Aucuns traitement pour le pragaraphe 4
        data.addData("LETTRE_PARA4", document.getTextes(6).getTexte(6).getDescription());

        // Traitement du paragraphe des puces, j'insere l'annee du salaire et les puces
        JACalendar j = new JACalendarGregorian();
        data.addData("LETTRE_PUCE2_A", document.getTextes(6).getTexte(7).getDescription());
        data.addData("LETTRE_PUCE2_B", document.getTextes(6).getTexte(11).getDescription());
        data.addData("LETTRE_PUCE2_C", document.getTextes(6).getTexte(12).getDescription());
        data.addData("LETTRE_PUCE2_D", PRStringUtils.replaceString(document.getTextes(6).getTexte(13).getDescription(),
                REEcheanceRenteOO.CDT_ANNEESALAIRE,
                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(j.addYears(getMoisEcheance().substring(2), -1))));

        // Aucns traitements pour le pragraphe 6
        data.addData("LETTRE_PARA5", document.getTextes(6).getTexte(8).getDescription());

        // Aucns traitements pour le pragraphe 7
        data.addData("LETTRE_PARA6", document.getTextes(6).getTexte(9).getDescription());

        // Traitement du pragraphe 8, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(6).getTexte(10).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA7", titretier);

        // Taitement de l'annexe
        data.addData("TEXTE_ANNEXES", document.getTextes(9).getTexte(1).getDescription());
        data.addData("POINTS", document.getTextes(9).getTexte(2).getDescription());
        data.addData("VALEUR_ANNEXES", document.getTextes(9).getTexte(3).getDescription());

        if (PRACORConst.CA_FEMME.equals(echeanceCourrante.getCsSexe())) {

            chargementEnTeteEtSignatureLettre(data);

            JadePublishDocumentInfo pubInfoFAVSConjoint = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfoFAVSConjoint.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoFAVSConjoint.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoFAVSConjoint.setOwnerEmail(getEMailAddress());
            pubInfoFAVSConjoint.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfoFAVSConjoint.setArchiveDocument(getFemmeArrivantAgeVieillesseGED());

            try {
                if (getFemmeArrivantAgeVieillesseGED()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concern�e (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfoFAVSConjoint = h.setNssExtraFolderToDocInfo(getSession(), pubInfoFAVSConjoint,
                                echeanceCourrante.getIdTiers());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pubInfoFAVSConjoint.setPublishDocument(false);
            pubInfoFAVSConjoint.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_FEMME_VIEILLESSE);
            pubInfoFAVSConjoint.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_FEMME_VIEILLESSE);
            pubInfoFAVSConjoint.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

            if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation())
                    && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
                Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
                codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
                pubInfoFAVSConjoint.setDocumentProperty(
                        REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                        REGedUtils.getCleGedPourTypeRente(getSession(),
                                REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
            }

            TIDocumentInfoHelper.fill(pubInfoFAVSConjoint, getEcheanceCourrante().getIdTiers(), getSession(), null,
                    null, null);

            allDoc.addDocument(data, pubInfoFAVSConjoint);

        } else {
            chargementEnTeteEtSignatureLettre(data);

            JadePublishDocumentInfo pubInfoHAVSConjoint = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfoHAVSConjoint.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoHAVSConjoint.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfoHAVSConjoint.setOwnerEmail(getEMailAddress());
            pubInfoHAVSConjoint.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfoHAVSConjoint.setArchiveDocument(getHommeArrivantAgeVieillesseGED());

            try {
                if (getHommeArrivantAgeVieillesseGED()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concern�e (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfoHAVSConjoint = h.setNssExtraFolderToDocInfo(getSession(), pubInfoHAVSConjoint,
                                echeanceCourrante.getIdTiers());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pubInfoHAVSConjoint.setPublishDocument(false);
            pubInfoHAVSConjoint.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_HOMME_VIEILLESSE);
            pubInfoHAVSConjoint.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_HOMME_VIEILLESSE);
            pubInfoHAVSConjoint.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

            if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation())
                    && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
                Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
                codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
                pubInfoHAVSConjoint.setDocumentProperty(
                        REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                        REGedUtils.getCleGedPourTypeRente(getSession(),
                                REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
            }

            TIDocumentInfoHelper.fill(pubInfoHAVSConjoint, getEcheanceCourrante().getIdTiers(), getSession(), null,
                    null, null);

            allDoc.addDocument(data, pubInfoHAVSConjoint);
        }
    }

    private void chargementDonneesMotifFinEtudes(boolean hasDoubleRowObject) throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");

        RERenteAccordee rd = new RERenteAccordee();
        rd.setSession(getSession());
        rd.setId(echeanceCourrante.getIdRenteAccordee());
        rd.retrieve();

        String concerne = resolveDocumentObject(rd, echeanceCourrante);

        if (hasDoubleRowObject && !echeanceCourrante.getListeEcheanceLiees().isEmpty()) {
            // il ne peut y avoir que 2 objets -> on prend donc la premi�re �ch�ance li�e
            String concerne2 = resolveDocumentObject(rd, echeanceCourrante.getListeEcheanceLiees().get(0));
            concerne += " / " + concerne2;
        }
        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Traitement du paragraphe 1, j'insere la date de fin de la periode d'�tude au format mmm YYYY
        String paraAvecDate = "";
        if (PRACORConst.CS_HOMME.equals(echeanceCourrante.getCsSexe())) {
            paraAvecDate = PRStringUtils.replaceString(document.getTextes(5).getTexte(1).getDescription(),
                    REEcheanceRenteOO.CDT_DATEECHEANCE, dateFinEtude(echeanceCourrante));
        } else {
            paraAvecDate = PRStringUtils.replaceString(document.getTextes(5).getTexte(7).getDescription(),
                    REEcheanceRenteOO.CDT_DATEECHEANCE, dateFinEtude(echeanceCourrante));
        }
        data.addData("LETTRE_PARA1", paraAvecDate);

        // Aucuns traitement pour le paragraphe 2
        data.addData("LETTRE_PARA2", document.getTextes(5).getTexte(2).getDescription());

        // Traitement paragraphe 3, j'insere la valeur du dernier jour du mois
        JACalendarGregorian jaCalGre = new JACalendarGregorian();
        // Calcul du dernier jours du mois
        JADate dernierJourMois = jaCalGre.addDays(jaCalGre.addMonths(new JADate("01." + getMoisEcheance()), 1), -1);
        // Insertion du dernier jours du mois dans le texte et dans le doc
        data.addData("LETTRE_PARA3", PRStringUtils.replaceString(document.getTextes(5).getTexte(3).getDescription(),
                REEcheanceRenteOO.CDT_DERNIERJOURSMOIS,
                JACalendar.format(dernierJourMois, JACalendar.FORMAT_DDsMMsYYYY)));

        // Aucuns traitement pour le paragraphe 4 et 5
        data.addData("LETTRE_PARA4", document.getTextes(5).getTexte(4).getDescription());
        data.addData("LETTRE_PARA5", document.getTextes(5).getTexte(5).getDescription());

        // Traitement du paragraphe 6, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(5).getTexte(6).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA6", titretier);

        cacherAnnexes();

        chargementEnTeteEtSignatureLettre(data);

        JadePublishDocumentInfo pubInfoEtude = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfoEtude.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoEtude.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoEtude.setOwnerEmail(getEMailAddress());
        pubInfoEtude.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        pubInfoEtude.setArchiveDocument(getEcheanceEtudeGED());

        try {
            if (getEcheanceEtudeGED()) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concern�e (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfoEtude = h.setNssExtraFolderToDocInfo(getSession(), pubInfoEtude,
                            echeanceCourrante.getIdTiers());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pubInfoEtude.setPublishDocument(false);
        pubInfoEtude.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_ETUDES);
        pubInfoEtude.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_ETUDES);
        pubInfoEtude.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        if (!JadeStringUtil.isBlankOrZero(rd.getCodePrestation()) && JadeNumericUtil.isInteger(rd.getCodePrestation())) {
            Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
            codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(rd.getCodePrestation())));
            pubInfoEtude.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourListeCodesPrestation(getSession(), codesPrestation)));
        }

        TIDocumentInfoHelper.fill(pubInfoEtude, getEcheanceCourrante().getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfoEtude);

    }

    private void chargementDonn�esMotifVeuf() throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");

        String concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(4).getDescription(),
                REEcheanceRenteOO.CDT_MONTANTPREST, echeanceCourrante.getMontantPrestation());
        data.addData("LETTRE_CONCERNE", concerne);

        // Traitement du titre, en fonction du tiers de l'adresse
        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titre);
        } else {
            data.addData("titreTiers", titre + ",");
        }

        // Traitement du paragraphe 1, ajout d'un exposant
        data.addData("LETTRE_PARA1", document.getTextes(8).getTexte(1).getDescription());
        data.addData("LETTRE_PARA1_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA1_SUITE1", document.getTextes(8).getTexte(2).getDescription());

        // Traitement du paragraphe 2, ajout d'un exposant et insertion du nom du mois
        String para2 = PRStringUtils.replaceString(document.getTextes(8).getTexte(3).getDescription(),
                REEcheanceRenteOO.CDT_MOISECHEANCE,
                JACalendar.getMonthName(Integer.parseInt(getMoisEcheance().substring(0, 2)), codeIsoLangue));
        data.addData("LETTRE_PARA2", para2);
        data.addData("LETTRE_PARA2_EXPOSANT1", document.getTextes(2).getTexte(5).getDescription());
        data.addData("LETTRE_PARA2_SUITE1", document.getTextes(8).getTexte(4).getDescription());

        // Traitement du paragraphe 3, ajout du dernier jours du mois
        JACalendarGregorian jaCalGre = new JACalendarGregorian();
        // Calcul du dernier jours du mois
        JADate dernierJourMois = jaCalGre.addDays(jaCalGre.addMonths(new JADate("01." + getMoisEcheance()), 1), -1);
        // Insertion du dernier jours du mois dans le texte
        String paraAvecDate = PRStringUtils.replaceString(document.getTextes(8).getTexte(5).getDescription(),
                REEcheanceRenteOO.CDT_DERNIERJOURSMOIS,
                JACalendar.format(dernierJourMois, JACalendar.FORMAT_DDsMMsYYYY));
        // Insertion du texte dans le doc
        data.addData("LETTRE_PARA3", paraAvecDate);

        // Aucun traitement pour le paragraphe 4
        data.addData("LETTRE_PARA4", document.getTextes(8).getTexte(6).getDescription());

        // Traitement du paragraphe 5, j'insere le titre dans les salutations
        String titretier = PRStringUtils.replaceString(document.getTextes(8).getTexte(7).getDescription(),
                REEcheanceRenteOO.CDT_TITRE, titre);
        data.addData("LETTRE_PARA5", titretier);

        chargementEnTeteEtSignatureLettre(data);

        JadePublishDocumentInfo pubInfoRenteVeuf = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfoRenteVeuf.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoRenteVeuf.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
        pubInfoRenteVeuf.setOwnerEmail(getEMailAddress());
        pubInfoRenteVeuf.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        pubInfoRenteVeuf.setArchiveDocument(getRenteDeVeufGED());

        try {
            if (getRenteDeVeufGED()) {
                // bz-5941
                PRGedHelper h = new PRGedHelper();
                // Traitement uniquement pour la caisse concern�e (CCB)
                if (h.isExtraNSS(getSession())) {
                    pubInfoRenteVeuf = h.setNssExtraFolderToDocInfo(getSession(), pubInfoRenteVeuf,
                            echeanceCourrante.getIdTiers());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pubInfoRenteVeuf.setPublishDocument(false);
        pubInfoRenteVeuf.setDocumentType(IRENoDocumentInfoRom.ECHEANCE_RENTE_DE_VEUF);
        pubInfoRenteVeuf.setDocumentTypeNumber(IRENoDocumentInfoRom.ECHEANCE_RENTE_DE_VEUF);
        pubInfoRenteVeuf.setDocumentDate(JACalendar.todayJJsMMsAAAA()); // ou la date du jours, selon les cas

        pubInfoRenteVeuf.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                REGedUtils.getCleGedPourTypeRente(getSession(), TypeRente.RenteAVS));

        TIDocumentInfoHelper
                .fill(pubInfoRenteVeuf, getEcheanceCourrante().getIdTiers(), getSession(), null, null, null);

        allDoc.addDocument(data, pubInfoRenteVeuf);

    }

    private void chargementEnTeteEtSignatureLettre(DocumentData data) throws Exception {
        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            crBean.setAdresse(adresse);
            JadeUser userDetails = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                    .loadForVisa(getSession().getUserId());

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Ajoute dans l'ent�te de la lettre qui a trait� le dossier si necessaire
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                if (userDetails != null) {
                    String user = userDetails.getFirstname() + " " + userDetails.getLastname();

                 // Uniquement pour la FERCIAM
                    if((NUM_CAISSE_FERCIAM).equals(CommonPropertiesUtils.getValue(CommonProperties.KEY_NO_CAISSE))) {
                        crBean.setNomCollaborateur(document.getTextes(1).getTexte(1).getDescription() + " " + document.getTextes(1).getTexte(2).getDescription());
                        crBean.setTelCollaborateur(document.getTextes(1).getTexte(3).getDescription());
                    }else {
                        crBean.setNomCollaborateur(document.getTextes(1).getTexte(1).getDescription() + " " + user);
                        crBean.setTelCollaborateur(userDetails.getPhone());
                    }
                }
            }

            // Ajoute la date dans l'ent�te de la lettre sauf pour les documents IJ
            crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));

            // Ajout du numero NSS dans l'ent�te de la lettre
            crBean.setNoAvs(echeanceCourrante.getNss());

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(REEcheanceRenteOO.FICHIER_MODELE_ENTETE_CORVUS);

            data.addData("idEntete", "CAISSE");

            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            data = caisseHelper.addSignatureParameters(data, crBean);

            try {
                data.addData("SIGNATURE", document.getTextes(9).getTexte(6).getDescription());
            } catch (IndexOutOfBoundsException e) {
                JadeLogger.warn(this, e.getMessage());
            }

        } catch (Exception e) {
            throw new Exception(getSession().getLabel("ERREUR_ENTETE_SIGNATURE"));

        }
    }

    private String dateFinEtude(REListerEcheanceRenteJoinMembresFamille entity) {
        try {
            String dateFinEtudeRetournee = JACalendar.format(
                    PRDateFormater.convertDate_AAAAMMJJ_to_JJMMAAAA(getEcheanceCourrante().getPeriodeDateFin()),
                    codeIsoLangue);
            return dateFinEtudeRetournee.substring(dateFinEtudeRetournee.indexOf(" ") + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAfficherDossierTraitePar() {
        return afficherDossierTraitePar;
    }

    public boolean getAjournementGED() {
        return ajournementGED;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("ECHEANCE_RENTE");
    }

    public REListerEcheanceRenteJoinMembresFamille getEcheanceCourrante() {
        return echeanceCourrante;
    }

    public boolean getEcheanceEtudeGED() {
        return echeanceEtudeGED;
    }

    public List<REListerEcheanceRenteJoinMembresFamille> getEcheances() {
        return echeances;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public boolean getEnfantDe18ansGED() {
        return enfantDe18ansGED;
    }

    public boolean getEnfantDe25ansGED() {
        return enfantDe25ansGED;
    }

    public boolean getFemmeArrivantAgeVieillesseGED() {
        return femmeArrivantAgeVieillesseGED;
    }

    public boolean getHommeArrivantAgeVieillesseGED() {
        return hommeArrivantAgeVieillesseGED;
    }

    public String getMoisEcheance() {
        return moisEcheance;
    }

    @Override
    public String getName() {
        return getSession().getLabel("ECHEANCE_RENTE");
    }

    public boolean getRenteDeVeufGED() {
        return renteDeVeufGED;
    }

    public boolean getEcheanceEnfantRecueilliGratuitementGED() {
        return echeanceEnfantRecueilliGratuitementGED;
    }

    public boolean ifMotifAgeAVS(REListerEcheanceRenteJoinMembresFamille echeance) {
        return REMotifEcheance.FemmeArrivantAgeAvs.name().equals(echeance.getMotifLettre())
                || REMotifEcheance.HommeArrivantAgeAvs.name().equals(echeance.getMotifLettre())
                || REMotifEcheance.FemmeArrivantAgeAvsAvecApiAi.name().equals(echeance.getMotifLettre())
                || REMotifEcheance.HommeArrivantAgeAvsAvecApiAi.name().equals(echeance.getMotifLettre());
    }

    public boolean isHasNextEcheance() {
        return hasNextEcheance;
    }

    @Override
    public void run() {
        try {
            allDoc = new JadePrintDocumentContainer();

            JadePublishDocumentInfo pubInfosDestination = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosDestination.setOwnerEmail(getEMailAddress());
            pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfosDestination.setDocumentTitle(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfosDestination.setDocumentSubject(getSession().getLabel("ECHEANCE_RENTE"));
            pubInfosDestination.setArchiveDocument(false);
            pubInfosDestination.setPublishDocument(true);

            allDoc.setMergedDocDestination(pubInfosDestination);

            if ((null != echeances) && (!echeances.isEmpty())) {
                echeancesIterator = echeances.iterator();
                while (echeancesIterator.hasNext()) {
                    try {
                        echeanceCourrante = echeancesIterator.next();
                        // S'il s'agit du type �ch�ance enfant, on va v�rifier s'il faut cr�er plusieurs documents ou
                        // plusieurs objets dans un document
                        if (isEnfantEcheance()) {
                            // Si on n'a pas la m�me adresse de paiement, on cr�er un document par entit�
                            if (!echeanceCourrante.isHasSameIdAdressePaiement()) {
                                // pr�paration d'un premier document pour l'�ch�ance courrante
                                prepareDocumentEcheanceFromEcheanceCourrante(false);
                                for (REListerEcheanceRenteJoinMembresFamille echeanceLiee : echeanceCourrante
                                        .getListeEcheanceLiees()) {
                                    // pr�paration des documents des �ch�ances li�es
                                    echeanceCourrante = echeanceLiee;
                                    prepareDocumentEcheanceFromEcheanceCourrante(false);
                                }
                            } else {
                                // Si l'adresse de paiement est la m�me, on change uniquement l'objet du document en
                                // fonction des entit�s li�es � l'�ch�ances courrante.
                                prepareDocumentEcheanceFromEcheanceCourrante(true);
                            }
                        } else {
                            prepareDocumentEcheanceFromEcheanceCourrante(false);

                        }

                    } catch (Exception exception) {
                        addWarningMessageToMail(echeanceCourrante, exception);
                    }

                }

                this.createDocuments(allDoc);

            }
            // Si la liste des �ch�ances est null ou vide, un mail d'avertisement est envoy� � l'utilisateur
            else {
                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "REEcheanceRenteOO", getSession()
                                .getLabel("ERREUR_AUCUNE_ECHEANCE_TROUVEE") + " " + getMoisEcheance()));
            }
        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, "REEcheanceRenteOO", e.toString()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    sendCompletionMail(Arrays.asList(getEMailAddress()));
                }
            } catch (Exception e) {
                System.out.println(getSession().getLabel("ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }
    }

    private boolean isEnfantEcheance() {

        if (REMotifEcheance.Echeance18ans.name().equals(echeanceCourrante.getMotifLettre())
                || REMotifEcheance.Echeance25ans.name().equals(echeanceCourrante.getMotifLettre())
                || REMotifEcheance.EcheanceFinEtudes.name().equals(echeanceCourrante.getMotifLettre())) {

            return true;
        }

        return false;
    }

    private void prepareDocumentEcheanceFromEcheanceCourrante(boolean hasDoubleRowObject) throws Exception {
        data = new DocumentData();
        data.addData("idProcess", "REEcheanceRenteOO");

        // Valeur � null pour ne pas garder en m�moire les valeurs du tiers precedent
        tiersAdresse = null;
        tiersBeneficiaire = null;
        tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getEcheanceCourrante().getIdTiers());
        if (tiersBeneficiaire == null) {
            tiersBeneficiaire = PRTiersHelper.getAdministrationParId(getSession(), getEcheanceCourrante().getIdTiers());
        }

        setTiersAdressePaiement(PRTiersHelper.getTiersParId(getSession(), getEcheanceCourrante()
                .getIdTiersAdressePaiement()));

        // Recherche d'une adresse pour le tiers b�n�ficiaire
        // BZ 5220, recherche de l'adresse en cascade en fonction du param�tre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        if (tiersAdressePaiement != null) {
            // On prend l'adresse du tiers adresse de paiement, s'il est renseign�.
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    tiersAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");
        } else {
            // Sinon, on va prendre celle du tiers b�n�ficiaire.
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");
        }

        // Si le tiers beneficiaire n'a pas d'adresse, je recherche toutes les rentes accord�es dans un
        // �tat
        // valide de la famille
        if (JadeStringUtil.isEmpty(adresse)) {
            RERenteAccJoinTblTiersJoinDemRenteManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
            renteAccManager.setSession(getSession());

            String navs = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            if (navs.length() > 14) {
                renteAccManager.setLikeNumeroAVSNNSS("true");
            } else {
                renteAccManager.setLikeNumeroAVSNNSS("false");
            }

            // ce manager qui cherche dans l'idTiers parent en boucle !
            renteAccManager.setLikeNumeroAVS(navs);
            renteAccManager.wantCallMethodBeforeFind(true);
            renteAccManager.setRechercheFamille(true);
            renteAccManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE);
            renteAccManager.find(BManager.SIZE_NOLIMIT);

            // Liste Pour les rentes accord�es non principale
            List<RERenteAccordee> listeMemeAdPmtNonPrincipale = new ArrayList<RERenteAccordee>();

            for (int i = 0; i < renteAccManager.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) renteAccManager
                        .get(i);

                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setSession(getSession());
                ic.setIdInfoCompta(echeanceCourrante.getIdInfoCompta());
                ic.retrieve();

                // Uniquement les rentes accord�es dont l'adresse de paiement est identique � l'�cheance
                // courante et sans date de fin de droit
                if (elm.getIdTierAdressePmt().equals(ic.getIdTiersAdressePmt())
                        && JadeStringUtil.isEmpty(elm.getDateFinDroit())) {

                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(getSession());
                    ra.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                    ra.retrieve();

                    // Si la rente est principale je l'utilise, sinon je l'insere dans la liste pour les
                    // rentes accord�es non principale
                    if (ra.getCodePrestation().equals("10") || ra.getCodePrestation().equals("20")
                            || ra.getCodePrestation().equals("13") || ra.getCodePrestation().equals("23")
                            || ra.getCodePrestation().equals("50") || ra.getCodePrestation().equals("70")
                            || ra.getCodePrestation().equals("72")) {

                        REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();
                        tiersAdresse = PRTiersHelper.getTiersParId(getSession(), infoCompt.getIdTiersAdressePmt());
                        if (tiersAdresse == null) {
                            tiersAdresse = PRTiersHelper.getAdministrationParId(getSession(),
                                    infoCompt.getIdTiersAdressePmt());
                        }
                        // je recherche une adresse de courrier pour le tiers de l'adresse de paiement
                        adr = PRTiersHelper.getAdressePaiementData(getSession(), getTransaction(),
                                infoCompt.getIdTiersAdressePmt(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                JACalendar.todayJJsMMsAAAA());

                        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre
                        // isWantAdresseCourrier
                        // se trouvant dans le fichier corvus.properties
                        adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), adr.getIdTiers(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                        // Si une adresse est trouv�e, je recherche le titre du tiers del'adresse de
                        // paiement
                        if (!JadeStringUtil.isEmpty(adresse)) {
                            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                            Hashtable<String, String> params = new Hashtable<String, String>();
                            params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                            ITITiers[] t = tiersTitre.findTiers(params);
                            if ((t != null) && (t.length > 0)) {
                                tiersTitre = t[0];
                            }

                            titre = tiersTitre.getFormulePolitesse(tiersAdresse
                                    .getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                            break;
                        }
                    } else {
                        listeMemeAdPmtNonPrincipale.add(ra);
                    }
                }
            }

            // Si aucune adresse n'est trouv�e, je recherche dans la liste des rentes accrod�es non
            // principale
            if (JadeStringUtil.isEmpty(adresse)) {
                if (!listeMemeAdPmtNonPrincipale.isEmpty()) {
                    for (Iterator<RERenteAccordee> iterator = listeMemeAdPmtNonPrincipale.iterator(); iterator
                            .hasNext();) {
                        RERenteAccordee ra = iterator.next();
                        REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();
                        tiersAdresse = PRTiersHelper.getTiersParId(getSession(), infoCompt.getIdTiersAdressePmt());
                        if (tiersAdresse == null) {
                            tiersAdresse = PRTiersHelper.getAdministrationParId(getSession(),
                                    infoCompt.getIdTiersAdressePmt());
                        }

                        // je recherche une adresse de courrier pour le tiers de l'adresse de paiement
                        adr = PRTiersHelper.getAdressePaiementData(getSession(), getTransaction(),
                                infoCompt.getIdTiersAdressePmt(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                JACalendar.todayJJsMMsAAAA());
                        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre
                        // isWantAdresseCourrier
                        // se trouvant dans le fichier corvus.properties
                        adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), adr.getIdTiers(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                        // Si une adresse est trouv�e, je recherche le titre du tiers de l'adresse de
                        // paiement
                        if (!JadeStringUtil.isEmpty(adresse)) {
                            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                            Hashtable<String, String> params = new Hashtable<String, String>();
                            params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                            ITITiers[] t = tiersTitre.findTiers(params);
                            if ((t != null) && (t.length > 0)) {
                                tiersTitre = t[0];
                            }

                            titre = tiersTitre.getFormulePolitesse(tiersAdresse
                                    .getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                            break;
                        }
                    }
                }
            }
        } else {
            // Comme une adresse a �t� trouv�e, je recherche le titre du tiers beneficiaire
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            PRTiersWrapper tiersForTitre = tiersBeneficiaire;
            if (tiersAdressePaiement != null) {
                tiersForTitre = tiersAdressePaiement;
            }
            params.put(ITITiers.FIND_FOR_IDTIERS, tiersForTitre.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            titre = tiersTitre.getFormulePolitesse(tiersForTitre.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            if (JadeStringUtil.isEmpty(titre)) {
                TITiers tiers = new TITiers();
                tiers.setSession(getSession());
                tiers.setIdTiers(echeanceCourrante.getIdTiers());
                tiers.retrieve();

                TIAdresseDataSource Ads = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);

                if (null != Ads) {
                    titre = Ads.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE);
                }
            }
        }

        // Si aucun titre n'est trouv�, je force un titre par d�faut (celui de la caisse)
        if (JadeStringUtil.isEmpty(titre)) {

            TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
            tiAdminCaisseMgr.setSession(getSession());
            tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                    getSession().getApplication()));
            tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdminCaisseMgr.find(BManager.SIZE_NOLIMIT);

            TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

            String idTiersCaisse = "";
            if (tiAdminCaisse != null) {
                idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
            }

            // retrieve du tiers
            PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiersCaisse);

            if (null == tier) {
                tier = PRTiersHelper.getAdministrationParId(getSession(), idTiersCaisse);
            }

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            titre = tiersTitre.getFormulePolitesse(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

        }

        // Si aucune adresse n'est trouv�e pour le tiers et dans les rentes accord�es, un mail d'erreur
        // est
        // envoy� � l'utilisateur
        if (JadeStringUtil.isEmpty(adresse)) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "REEcheanceRenteOO", getSession().getLabel(
                            "ERREUR_TIER_ADRESSE")
                            + " "
                            + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                            + " "
                            + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM)));
        } else {
            chargementCatalogueTexte();
            chargementDonneesLettre(hasDoubleRowObject);
        }
    }

    public void setAfficherDossierTraitePar(String afficherDossierTraitePar) {
        this.afficherDossierTraitePar = afficherDossierTraitePar;
    }

    public void setAjournementGED(boolean ajournementGED) {
        this.ajournementGED = ajournementGED;
    }

    public void setEcheanceCourrante(REListerEcheanceRenteJoinMembresFamille echeanceCourrante) {
        this.echeanceCourrante = echeanceCourrante;
    }

    public void setEcheanceEtudeGED(boolean echeanceEtudeGED) {
        this.echeanceEtudeGED = echeanceEtudeGED;
    }

    public void setEcheances(List<REListerEcheanceRenteJoinMembresFamille> echeances) {
        this.echeances = echeances;
    }

    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setEnfantDe18ansGED(boolean enfantDe18ansGED) {
        this.enfantDe18ansGED = enfantDe18ansGED;
    }

    public void setEnfantDe25ansGED(boolean enfantDe25ansGED) {
        this.enfantDe25ansGED = enfantDe25ansGED;
    }

    public void setFemmeArrivantAgeVieillesseGED(boolean femmeArrivantAgeVieillesseGED) {
        this.femmeArrivantAgeVieillesseGED = femmeArrivantAgeVieillesseGED;
    }

    public void setHasNextEcheance(boolean hasNextEcheance) {
        this.hasNextEcheance = hasNextEcheance;
    }

    public void setHommeArrivantAgeVieillesseGED(boolean hommeArrivantAgeVieillesseGED) {
        this.hommeArrivantAgeVieillesseGED = hommeArrivantAgeVieillesseGED;
    }

    public void setMoisEcheance(String moisEcheance) {
        this.moisEcheance = moisEcheance;
    }

    public void setRenteDeVeufGED(boolean renteDeVeufGED) {
        this.renteDeVeufGED = renteDeVeufGED;
    }

    public void setEcheanceEnfantRecueilliGratuitementGED(boolean echeanceEnfantRecueilliGratuitementGED) {
        this.echeanceEnfantRecueilliGratuitementGED = echeanceEnfantRecueilliGratuitementGED;
    }

    public PRTiersWrapper getTiersAdressePaiement() {
        return tiersAdressePaiement;
    }

    public void setTiersAdressePaiement(PRTiersWrapper tiersAdressePaiement) {
        this.tiersAdressePaiement = tiersAdressePaiement;
    }
}
