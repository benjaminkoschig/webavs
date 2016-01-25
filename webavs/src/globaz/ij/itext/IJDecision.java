package globaz.ij.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.api.doc.ICTScalableDocumentGenerator;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.babel.api.doc.impl.CTScalableDocumentCopie;
import globaz.babel.api.helper.CTHtmlConverter;
import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAVector;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.api.prononces.IIJSituationProfessionnelle;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPetiteIJJointRevenu;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.helpers.process.IJDecisionACaisseReportHelper;
import globaz.ij.module.IJDecisionCotisationBuilder;
import globaz.ij.properties.IJProperties;
import globaz.ij.utils.IJGestionnaireHelper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntRole;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater03;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater04;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRCalcul;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIBanqueViewBean;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonProperties;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;

/**
 * <p>
 * MB = montant de base retourné par ACOR (c'est le montant reporté sur la décision).<br/>
 * RD = Revenu déterminant, soumis à plafond Les tests ci-dessous permettent uniquement de définir la base de calcul.
 * </p>
 * <p>
 * Si Petite IJ:
 * 
 * <pre>
 *     Si MB >= 88(2007,rev 4) ou 104(2008, rev 4) ou 103.80(2008,rev5)  
 *                |                               |
 *                |                               |
 *                oui                             non
 *                |                               |
 *                ctd 3.3                         ctd 3.7
 * </pre>
 * 
 * </p>
 * <p>
 * Si Grande IJ:
 * 
 * <pre>
 *    Si MB >= 235(2007,rev4) ou 277(2008,rev4,rev5)------------------  
 *                |                                                   |
 *                |                                                   |
 *                oui                                                 non
 *                |													  |
 *                |                                                   |
 *              Si rev4------                          Si MB < 104(2008,rev4) ou 88(2007,rev4)
 *              |            |                      et Si rev4                            |
 *              |            |                         |                                  |
 *              oui          non                       oui                                non
 *              |            |                         |                                  |
 *        RD=293(2007)     RD=346                   RD=88(2007)                         RD=MB 
 *        RD=346(2008)     cdt 3.2                  RD=104(2008)                        cdt 3.8
 *        cdt 3.2          cdt 3.6                  cdt 3.13                            cdt 3.4           
 *        cdt 3.6                                   cdt 3.17
 * </pre>
 * 
 * @author JJE
 */
public class IJDecision extends FWIDocumentManager implements ICTScalableDocumentGenerator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CDT_DATEDEBUTPRONONCE = "{DateDebutPrononce}";
    private static final String CDT_DATEDECISION = "{DateSaisie}";
    private static final String CDT_DATEFINPRONONCE = "{DateFinPrononce}";
    private static final String CDT_DEVISE = "{Devise}";
    private static final String CDT_INDEMNITEDEBASE = "{IndemniteDeBase}";
    private static final String CDT_LIEUDATE = "{Lieu}";
    private static final String CDT_MNT_JOUR = "{MntJour}";
    private static final String CDT_MONTANTRENTEAI = "{MontantRenteAI}";
    private static final String CDT_NBENFANT = "{NbEnfant}";
    private static final String CDT_NOAVS = "{NoAvs}";
    private static final String CDT_NODECISIONIJ = "{NoDecisionIJ}";
    private static final String CDT_NOMPRENOM = "{NomPrenom}";
    private static final String CDT_REVENUANNUEL = "{RevenuAnnuel}";
    private static final String CDT_REVENUDETERMINANT = "{RevenuDeterminant}";
    private static final String CDT_REVENUDM = "{RevenuDM}";
    private static final String CDT_REVENUDROIT = "{RevenuDroit}";
    private static final String CDT_REVENUJOURNALIER = "{RevenuJournalier}";
    private static final String CDT_REVENUMENSUEL = "{RevenuMensuel}";
    private static final String CDT_SIGNECOTI = "{SigneCoti}";
    private static final String CDT_TAUXCOTI = "{TauxCoti}";
    private static final String CDT_TAUXIMP = "{TauxImp}";
    private static final String CDT_TITRE = "{Titre}";
    private static final String CDT_TITREPERIODE_DATEDEBUT = "{DateDebutIJ}";
    private static final String CDT_TITREPERIODE_DATEFIN = "{DateFinIJ}";
    private static final String CDT_TYPECOTI = "{TypeCoti}";

    private static final String FICHIER_MODELE_DECISION = "IJ_DECISION";
    private static final String NO5EMEREVISION = "5";
    private static final String PREFIX_HEADER_DECISION = "Header_Dec_";
    private static final String SUFIX_HEADER_DECISION = ".jasper";

    /**
     * Donne le montant journalier arrondi au franc supp. correspondant a la situation professionnelle
     * 
     * @param repartition
     * @return
     */
    static public FWCurrency getSalaireAnnuelVerse(IJSituationProfessionnelle sitPro) {

        BigDecimal montantAnnuelVerse = null;
        BigDecimal montant = new BigDecimal("0");

        // pour un indépendant
        if (sitPro.getIsIndependant().booleanValue()) {
            BigDecimal mv = new BigDecimal(sitPro.getSalaire());
            montantAnnuelVerse = new BigDecimal(mv.doubleValue());

        }
        // pour les employés
        else {
            BigDecimal revenuIntermediaire = null;
            BigDecimal mv = new BigDecimal(sitPro.getSalaire());

            // si périodicité horaire
            if (sitPro.getCsPeriodiciteSalaire().equals(IIJSituationProfessionnelle.CS_HORAIRE)) {
                BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getNbHeuresSemaine());
                revenuIntermediaire = mv.multiply(nbHeuresSemaine);
                revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal("7"), 10, BigDecimal.ROUND_DOWN);

            }

            // si périodicité mois
            else if (sitPro.getCsPeriodiciteSalaire().equals(IIJSituationProfessionnelle.CS_MENSUEL)) {
                revenuIntermediaire = mv.divide(new BigDecimal("30"), 10, BigDecimal.ROUND_DOWN);
            }

            // si périodicité 4 semaines
            else if (sitPro.getCsPeriodiciteSalaire().equals(IIJSituationProfessionnelle.CS_4_SEMAINES)) {
                revenuIntermediaire = mv.divide(new BigDecimal("28"), 10, BigDecimal.ROUND_DOWN);
            }

            // si périodicité année
            else if (sitPro.getCsPeriodiciteSalaire().equals(IIJSituationProfessionnelle.CS_ANNUEL)) {
                revenuIntermediaire = mv.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
            }

            // si autre rémunération
            if (Float.parseFloat(sitPro.getAutreRemuneration()) > 0) {

                if (!sitPro.getPourcentAutreRemuneration().booleanValue()) {

                    montant = new BigDecimal(sitPro.getAutreRemuneration());

                    // si périodicité horaire
                    if (sitPro.getCsPeriodiciteAutreRemuneration().equals(IIJSituationProfessionnelle.CS_HORAIRE)) {
                        BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getNbHeuresSemaine());
                        montant = montant.multiply(nbHeuresSemaine);
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("7"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité mois
                    else if (sitPro.getCsPeriodiciteAutreRemuneration().equals(IIJSituationProfessionnelle.CS_MENSUEL)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("30"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité 4 semaines
                    else if (sitPro.getCsPeriodiciteAutreRemuneration().equals(
                            IIJSituationProfessionnelle.CS_4_SEMAINES)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("28"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité année
                    else if (sitPro.getCsPeriodiciteAutreRemuneration().equals(IIJSituationProfessionnelle.CS_ANNUEL)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("360"), 10,
                                BigDecimal.ROUND_DOWN));
                    }
                } else {

                    montant = new BigDecimal(revenuIntermediaire.toString());

                    revenuIntermediaire = revenuIntermediaire.add(new BigDecimal(JANumberFormatter.format(
                            PRCalcul.pourcentage100(montant.toString(), sitPro.getAutreRemuneration()), 0.05, 2,
                            JANumberFormatter.NEAR)));
                }
            }

            // si un salaire nature
            if (Float.parseFloat(sitPro.getSalaireNature()) > 0) {

                montant = new BigDecimal(sitPro.getSalaireNature());

                // si périodicité horaire
                if (sitPro.getCsPeriodiciteSalaireNature().equals(IIJSituationProfessionnelle.CS_HORAIRE)) {
                    BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getNbHeuresSemaine());
                    montant = montant.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("7"), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si périodicité mois
                else if (sitPro.getCsPeriodiciteSalaireNature().equals(IIJSituationProfessionnelle.CS_MENSUEL)) {
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("30"), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si périodicité 4 semaines
                else if (sitPro.getCsPeriodiciteSalaireNature().equals(IIJSituationProfessionnelle.CS_4_SEMAINES)) {
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("28"), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si périodicité année
                else if (sitPro.getCsPeriodiciteSalaireNature().equals(IIJSituationProfessionnelle.CS_ANNUEL)) {
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("360"), 10,
                            BigDecimal.ROUND_DOWN));
                }

            }
            montantAnnuelVerse = revenuIntermediaire.multiply(new BigDecimal("360"));
        }

        FWCurrency montantAnnuelVerseFw = new FWCurrency(JANumberFormatter.format(montantAnnuelVerse.toString(), 1, 2,
                JANumberFormatter.SUP));

        return montantAnnuelVerseFw;
    }

    private IJDecisionACaisseReportHelper caisseHelper;
    private HashMap champs;
    private String codeIsoLangue = "";
    private String csDestinataire = "";
    private String csDomaine = "";
    private String csType = "";
    private String dateDecision = "";
    private ICTDocument document;
    private ICTDocument documentHelper;
    private ICTScalableDocumentProperties documentProperties;
    private String generatorImplClassName = "";
    private boolean hasBCRenteAISupprimée3emeMois = false;
    private boolean hasNext;
    private boolean hasRenteAIDurantReadaptation = false;
    private String idPrononce = "";
    private String idTiersBeneficiaire = "";
    private String idTiersCopieCourante = "";
    private ICTScalableDocumentCopie intervenantCopie;
    private ICTScalableDocumentCopie intervenantCopieEnTete;
    private boolean isCopieEnTete = false;
    private boolean isDocumentPrincipal = false;
    private Iterator iteratorDestinataireCopie;
    private Iterator iteratorDestinataireCopieEnTete;
    private LinkedList lignes = new LinkedList();
    private int nbDocument = 0;
    private int nbDocumentCopie = 0;
    private int nbTrue = 0;
    private String numeroDeDecisionAI = "";
    private IJPrononce prononce = new IJPrononce();
    private String revenuJournalierMoyen = "";
    private ICTScalableDocumentProperties scalableDocumentProperties;
    private String specificHeader = "";
    private PRTiersWrapper tiers;
    private PRTiersWrapper tiersCopie;
    private PRTiersWrapper tiersCopieEnTete;
    private PRTiersWrapper tiersCopieImpression;

    private String adresseCourrierMiseEnForme(TIAdressePaiementData adressePaiementData) {

        if ((adressePaiementData == null) || adressePaiementData.isNew()) {
            return "";
        }

        TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
        source.load(adressePaiementData);
        return new TIAdressePaiementBeneficiaireFormater().format(source);
    }

    private String adressePaiementMiseEnForme(TIAdressePaiementData adressePaiementData) throws Exception {

        StringBuilder adresse = new StringBuilder();
        TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

        // formatter les infos de l'adresse pour l'affichage correct
        // dans l'ecran
        if ((adressePaiementData != null) && !adressePaiementData.isNew()) {
            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adressePaiementData.getCcp())) {
                TIBanqueViewBean banque = new TIBanqueViewBean();
                banque.setSession(getSession());
                banque.setIdTiers(adressePaiementData.getIdTiersBanque());

                if (!JadeStringUtil.isEmpty(adressePaiementData.getCompte())) {
                    adresse.append(document.getTextes(10).getTexte(4).getDescription()).append(" ")
                            .append(adressePaiementData.getCompte()).append("\n");
                }
                if (!JadeStringUtil.isEmpty(adressePaiementData.getClearing())) {
                    adresse.append(getSession().getLabel("IJDECISION_CLEARING"))
                            .append(adressePaiementData.getClearing()).append("\n");
                }
                adresse.append(banque.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER));
            } else {
                source.load(adressePaiementData);
                adresse.append(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            adresse.append("\n").append(document.getTextes(4).getTexte(2).getDescription());
            return PRStringUtils.replaceString(adresse.toString(), IJDecision.CDT_NOMPRENOM,
                    (tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " " + tiers
                            .getProperty(PRTiersWrapper.PROPERTY_PRENOM)));
        } else {
            return getSession().getLabel("IJDECISION_ADRESSE_NON_TROUVE");
        }
    }

    private String afficheMntJour(StringBuffer buffer, boolean isBold) {

        String mntJourCdt = "";

        if (!isBold) {
            mntJourCdt = document.getTextes(2).getTexte(22).getDescription();
        } else {
            mntJourCdt = document.getTextes(2).getTexte(24).getDescription();
        }

        String mntJourFinal = PRStringUtils.replaceString(mntJourCdt, IJDecision.CDT_MNT_JOUR, buffer.toString());

        return mntJourFinal;
    }

    @Override
    public void afterBuildReport() {
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.DECISION_IJAI);
        super.afterBuildReport();
    }

    @Override
    public void beforeBuildReport() throws FWIException {

        try {

            // Reprise ou remise à zéro des autres paramètres
            Map parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            // creation des paramètres pour l'en-tête
            // ---------------------------------------------------------------------

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            String adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                    documentProperties.getParameter("idTierAdresseCourrier"), null,
                    IJApplication.CS_DOMAINE_ADRESSE_IJAI);

            String date = JACalendar.todayJJsMMsAAAA();

            crBean.setDate(JACalendar.format(date, codeIsoLangue));
            crBean.setAdresse(adresse);

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(PRAbstractApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                    IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Recherche de l'adresse selon code Administration
            String adresseOfficeAi = "";

            TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
            tiAdministrationMgr.setSession(getSession());
            tiAdministrationMgr.setForCodeAdministration(prononce.getOfficeAI());
            tiAdministrationMgr.setForGenreAdministration("509004");

            tiAdministrationMgr.find();

            TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();

            if (tiAdministration != null) {

                adresseOfficeAi = tiAdministration.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IJApplication.CS_DOMAINE_ADRESSE_IJAI, JACalendar.todayJJsMMsAAAA(),
                        new PRTiersAdresseCopyFormater03());

            }

            TIAdresseDataManager tiAdresseDatamgr = new TIAdresseDataManager();
            tiAdresseDatamgr.setSession(getSession());
            tiAdresseDatamgr.setForIdTiers(tiAdministration.getIdTiers());
            tiAdresseDatamgr.changeManagerSize(0);
            tiAdresseDatamgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            tiAdresseDatamgr.find();

            TIAdresseDataSource dataSource = new TIAdresseDataSource();
            dataSource.setLangue(codeIsoLangue);
            dataSource.load((TIAbstractAdresseData) tiAdresseDatamgr.getFirstEntity(), "");

            StringBuffer buffer = new StringBuffer();
            buffer.setLength(0);
            String localiteNom = "";

            if (dataSource != null) {
                localiteNom = dataSource.localiteNom.replaceAll("\\d", "").trim();
            }

            buffer.append(PRStringUtils.replaceString(document.getTextes(1).getTexte(9).getDescription(),
                    IJDecision.CDT_LIEUDATE, localiteNom));

            caisseHelper.addHeaderParameters(getImporter(), crBean, adresseOfficeAi, codeIsoLangue, buffer.toString());

            setDocumentTitle(getSession().getLabel("ORIGINAL_POUR") + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            if (isCopieEnTete) {
                parametres.put("P_HEADER_ISCOPIE", "ok");
            }

            // Pré-Création du document
            // ------------------------------------------------------------------------------------------------------

            // Remplissage du title PARAM_ZONE_1) qui contient les informations
            // "standard"
            buffer = new StringBuffer();

            buffer.append(document.getTextes(1).getTexte(1).getDescription());

            String cdtNomPrenomNoAvs = document.getTextes(1).getTexte(2).getDescription();

            cdtNomPrenomNoAvs = PRStringUtils.replaceString(cdtNomPrenomNoAvs, IJDecision.CDT_NOMPRENOM,
                    (tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " " + tiers
                            .getProperty(PRTiersWrapper.PROPERTY_PRENOM)));

            String nip = "";
            if (!JadeStringUtil.isBlankOrZero(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                BIApplication ijApp = GlobazSystem.getApplication(IJApplication.DEFAULT_APPLICATION_IJ);
                if ("true".equals(ijApp.getProperty(IJApplication.PROPERTY_DISPLAY_NIP))) {
                    nip = " / NIP " + tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
            }
            cdtNomPrenomNoAvs = PRStringUtils.replaceString(cdtNomPrenomNoAvs, IJDecision.CDT_NOAVS,
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + nip);

            buffer.append(cdtNomPrenomNoAvs);

            String cdtDateDecision = document.getTextes(1).getTexte(3).getDescription();

            dateDecision = documentProperties.getParameter("dateSurDocument");

            cdtDateDecision = PRStringUtils.replaceString(cdtDateDecision, IJDecision.CDT_DATEDECISION,
                    JACalendar.format(dateDecision, codeIsoLangue));

            buffer.append(cdtDateDecision);

            // Insertion du numéro de décision AI si il existe

            if (!JadeStringUtil.isBlankOrZero(prononce.getNoDecisionAI())) {

                String cdtNumeroDeDecisionAI = document.getTextes(1).getTexte(8).getDescription();

                numeroDeDecisionAI = prononce.getOfficeAI() + "/" + prononce.getNoDecisionAI().substring(0, 4) + "/"
                        + prononce.getNoDecisionAI().substring(4, 10) + "/"
                        + prononce.getNoDecisionAI().substring(10, 11);

                cdtNumeroDeDecisionAI = PRStringUtils.replaceString(cdtNumeroDeDecisionAI, IJDecision.CDT_NODECISIONIJ,
                        numeroDeDecisionAI);

                buffer.append(cdtNumeroDeDecisionAI);

            } else {
                buffer.append("\n\n");
            }

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable params = new Hashtable();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }

            String cdtTitre = document.getTextes(1).getTexte(4).getDescription();

            cdtTitre = PRStringUtils.replaceString(cdtTitre, IJDecision.CDT_TITRE,
                    tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            buffer.append(cdtTitre);

            buffer.append(document.getTextes(1).getTexte(5).getDescription());

            prononce.setIdPrononce(documentProperties.getParameter("idPrononce"));
            prononce.setSession(getSession());
            prononce.retrieve();

            String cdtDateDebutFinPrononce = "";

            if (!JadeStringUtil.isBlankOrZero(prononce.getDateDebutPrononce())
                    && !JadeStringUtil.isBlankOrZero(prononce.getDateFinPrononce())) {
                cdtDateDebutFinPrononce = document.getTextes(1).getTexte(6).getDescription();
            } else {
                cdtDateDebutFinPrononce = document.getTextes(1).getTexte(7).getDescription();
            }

            cdtDateDebutFinPrononce = PRStringUtils.replaceString(cdtDateDebutFinPrononce,
                    IJDecision.CDT_DATEDEBUTPRONONCE, prononce.getDateDebutPrononce());
            cdtDateDebutFinPrononce = PRStringUtils.replaceString(cdtDateDebutFinPrononce,
                    IJDecision.CDT_DATEFINPRONONCE, prononce.getDateFinPrononce());

            buffer.append(cdtDateDebutFinPrononce);

            parametres.put("PARAM_ZONE_1", buffer.toString());

            // Remise à zéro du buffer
            buffer.setLength(0);

            // Remplissage du titre de l'adresse de paiement

            buffer.append(document.getTextes(4).getTexte(1).getDescription());

            parametres.put("PARAM_TITRE_PAIEMENT", buffer.toString());

            // Recherche des adresses courrier/Paiement de l'assuré
            String adressePaiement = "";
            String adresseCourrier = "";

            if ("assure".equals(documentProperties.getParameter("beneficiaire"))) {

                TIAdressePaiementData adressePmtAssure;

                if ("standard".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                    adressePmtAssure = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                            .getCurrentThreadTransaction(), tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, "", JACalendar.todayJJsMMsAAAA());

                    // formatter les infos de l'adresse pour l'affichage correct dans l'écran
                    if ((adressePmtAssure != null) && !adressePmtAssure.isNew()) {
                        adressePaiement = adressePaiementMiseEnForme(adressePmtAssure);
                        adresseCourrier = adresseCourrierMiseEnForme(adressePmtAssure);
                    } else {
                        adressePaiement = getSession().getLabel("IJDECISION_ADRESSE_NON_TROUVE");
                        adresseCourrier = "";
                    }
                } else if ("personnalise".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {

                    adressePmtAssure = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                            .getCurrentThreadTransaction(), documentProperties
                            .getParameter("idTiersAdressePaiementPersonnalisee"), documentProperties
                            .getParameter("idDomaineApplicationAdressePaiementPersonnalisee"), "", JACalendar
                            .todayJJsMMsAAAA());

                    adressePaiement = adressePaiementMiseEnForme(adressePmtAssure);
                    adresseCourrier = adresseCourrierMiseEnForme(adressePmtAssure);
                } else {
                    // lorsqu'aucune adresse ne doit être affichée
                    adressePaiement = "";
                    adresseCourrier = document.getTextes(4).getTexte(9).getDescription() + "\n";
                }
            } else if ("employeur".equals(documentProperties.getParameter("beneficiaire"))) {
                // Gestion des adresses courrier/Paiement de l'employeur
                TIAdressePaiementData adressePaiementEmployeur;
                String idTierEmployeur = "";

                if ("standard".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                    // Tout d'abord, retrouver la situation professionnelle (on
                    // prendra le premier employeur)
                    IJSituationProfessionnelleManager sitProMgr = new IJSituationProfessionnelleManager();
                    sitProMgr.setSession(getSession());
                    sitProMgr.setForIdPrononce(prononce.getIdPrononce());
                    sitProMgr.find(1);

                    if (sitProMgr.size() > 0) {
                        IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) sitProMgr.get(0);
                        idTierEmployeur = sitPro.getIdEmployeur();
                    }

                    if (!JadeStringUtil.isEmpty(idTierEmployeur)) {

                        IJEmployeur employeur = new IJEmployeur();
                        employeur.setSession(getSession());
                        employeur.setIdEmployeur(idTierEmployeur);
                        employeur.retrieve();

                        idTierEmployeur = employeur.getIdTiers();

                        adressePaiementEmployeur = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                                .getCurrentThreadTransaction(), idTierEmployeur,
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, employeur.getIdAffilie(),
                                JACalendar.todayJJsMMsAAAA());

                        // formatter les infos de l'adresse pour l'affichage correct
                        // dans l'ecran
                        if ((adressePaiementEmployeur != null) && !adressePaiementEmployeur.isNew()) {
                            adressePaiement = adressePaiementMiseEnForme(adressePaiementEmployeur);
                            adresseCourrier = adresseCourrierMiseEnForme(adressePaiementEmployeur);
                        } else {
                            adressePaiement = getSession().getLabel("IJDECISION_ADRESSE_NON_TROUVE");
                            adresseCourrier = "";
                        }
                    }
                } else if ("personnalise".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {

                    adressePaiementEmployeur = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                            .getCurrentThreadTransaction(), documentProperties
                            .getParameter("idTiersAdressePaiementPersonnalisee"), documentProperties
                            .getParameter("idDomaineApplicationAdressePaiementPersonnalisee"), "", JACalendar
                            .todayJJsMMsAAAA());

                    adressePaiement = adressePaiementMiseEnForme(adressePaiementEmployeur);
                    adresseCourrier = adresseCourrierMiseEnForme(adressePaiementEmployeur);
                } else {
                    // lorsqu'aucune adresse ne doit être affichée
                    adressePaiement = "";
                    adresseCourrier = document.getTextes(4).getTexte(9).getDescription() + "\n";
                }
            }

            // Remise à zéro du buffer
            buffer.setLength(0);

            // Remplissage des adresses de courrier/paiement

            buffer.append(PRStringUtils.replaceString(document.getTextes(4).getTexte(7).getDescription(),
                    "{adresseCourrier}", adresseCourrier + " "));

            parametres.put("PARAM_PMT_COL1", buffer.toString());

            buffer.setLength(0);

            buffer.append(PRStringUtils.replaceString(document.getTextes(4).getTexte(8).getDescription(),
                    "{adressePmt}", adressePaiement));

            parametres.put("PARAM_PMT_COL2", buffer.toString());

            // Remplissage des renseignements de la caisse de compensation

            buffer.setLength(0);

            buffer.append(document.getTextes(4).getTexte(3).getDescription());

            buffer.append(document.getTextes(4).getTexte(4).getDescription());

            buffer.append(document.getTextes(4).getTexte(5).getDescription());

            String cdtPersonneDeReference = document.getTextes(4).getTexte(6).getDescription();

            Vector responsables = IJGestionnaireHelper.getResponsableData(getSession());
            String[] s = new String[2];

            s[0] = getSession().getUserId();
            s[1] = getSession().getUserId() + " - " + getSession().getUserFullName();

            boolean cont = false;

            for (int nbPersRef = 0; nbPersRef < responsables.size(); nbPersRef++) {
                String[] c = (String[]) responsables.get(nbPersRef);

                if (c[0].equals(s[0]) && c[1].equals(s[1])) {
                    cont = true;
                }

            }
            if (!cont) {
                responsables.add(s);
            }

            String responsableNomComplet = "";

            for (int nbResp = 0; nbResp < responsables.size(); nbResp++) {

                String[] respCourant = (String[]) responsables.get(nbResp);

                if (documentProperties.getParameter("idPersonneReference").equals(respCourant[0])) {
                    responsableNomComplet = respCourant[1];
                }

            }

            String[] responsableNomComplets = responsableNomComplet.split(" - ");

            cdtPersonneDeReference = PRStringUtils.replaceString(cdtPersonneDeReference, IJDecision.CDT_TITRE, "");// tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            if (responsableNomComplets.length == 2) {
                cdtPersonneDeReference = PRStringUtils.replaceString(cdtPersonneDeReference, IJDecision.CDT_NOMPRENOM,
                        responsableNomComplets[1].trim());
            } else {
                cdtPersonneDeReference = PRStringUtils.replaceString(cdtPersonneDeReference, IJDecision.CDT_NOMPRENOM,
                        "-");
            }

            buffer.append(cdtPersonneDeReference);

            parametres.put("PARAM_ZONE_3", buffer.toString());

            // Remplissage de la remarque (PARAM_ZONE_4)

            buffer.setLength(0);

            buffer.append(document.getTextes(5).getTexte(1).getDescription());

            buffer.append(CTHtmlConverter.htmlToIText(documentProperties.getParameter("remarque"), getSession()
                    .getApplication().getProperty(CommonProperties.KEY_NO_CAISSE)));

            parametres.put("PARAM_ZONE_4", buffer.toString());

            // Remplissage du texte de fin et des salutations (PARAM_ZONE_5)

            buffer.setLength(0);

            buffer.append(document.getTextes(6).getTexte(1).getDescription());

            parametres.put("PARAM_ZONE_5", buffer.toString());

            buffer.setLength(0);

            if (tiAdministration != null) {
                buffer.append(PRStringUtils.replaceString(document.getTextes(6).getTexte(2).getDescription(),
                        "{signature}", tiAdministration.getNomPrenom()));
            } else {
                buffer.append(PRStringUtils.replaceString(document.getTextes(6).getTexte(2).getDescription(),
                        "{signature}", getSession().getLabel("ADR_OAI_NON_TROUVEE")));
            }

            parametres.put("PARAM_SALUTATIONS", buffer.toString());

            // Création et remplissage de la signature
            // -----------------------------------------------------------------------------------
            try {

                caisseHelper.addSignatureParameters(getImporter());
            } catch (Exception e) {
                throw new FWIException("Impossible de charger le pied de page", e);
            }

            // Remplissage de la zone (Copies et annexes)
            // --------------------------------------------------------------------------------

            // On inscrit les copies

            buffer.setLength(0);
            Iterator iteratorCopieImpression = documentProperties.getCopiesIterator();

            String copies = "";
            boolean isCopieA = false;

            while (iteratorCopieImpression.hasNext()) {

                ICTScalableDocumentCopie copie = (ICTScalableDocumentCopie) iteratorCopieImpression.next();

                tiersCopieImpression = PRTiersHelper.getTiersParId(getSession(), copie.getIdTiers());

                if (null == tiersCopieImpression) {
                    tiersCopieImpression = PRTiersHelper.getAdministrationParId(getSession(), copie.getIdTiers());
                }

                if ((null != tiersCopieImpression) && !((CTScalableDocumentCopie) copie).isCopieOAI()) {

                    if (!isCopieA) {
                        isCopieA = true;
                        buffer.append(document.getTextes(7).getTexte(1).getDescription());
                    }

                    TITiers tiTierCopie = new TITiers();
                    tiTierCopie.setSession(getSession());
                    tiTierCopie.setIdTiers(tiersCopieImpression.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    tiTierCopie.retrieve();

                    copies += tiTierCopie.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IJApplication.CS_DOMAINE_ADRESSE_IJAI, JACalendar.todayJJsMMsAAAA(),
                            new PRTiersAdresseCopyFormater04());

                    copies += "\n";
                }

            }

            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(), "{valeurCopies}", copies));

            // Et on insère les annexes
            Iterator iteratorAnnexeImpression = documentProperties.getAnnexesIterator();

            if (iteratorAnnexeImpression.hasNext()) {
                buffer.append(document.getTextes(7).getTexte(2).getDescription());
            }

            String annexes = "";

            while (iteratorAnnexeImpression.hasNext()) {
                ICTScalableDocumentAnnexe annexe = (ICTScalableDocumentAnnexe) iteratorAnnexeImpression.next();

                annexes += annexe.getLibelle();
                annexes += "\n";

            }

            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(), "{valeurAnnexe}", annexes));

            // Si le buffer contenant les copies et annexes contient du texte
            // stylé, on remplace le ou les " & " par " et "
            if (isStyledText(buffer.toString())) {
                String strBuffer = buffer.toString();
                buffer.setLength(0);
                buffer.append(strBuffer.replaceAll(" & ", " et "));
            }

            // Finalement on insère tous dans les paramètres
            parametres.put("PARAM_ZONE_COPIE_ANNEXE", buffer.toString());

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJDecision");
            abort();
        }

    }

    @Override
    public void beforeExecuteReport() throws FWIException {

        // Effacer les pdf a la fin
        setDeleteOnExit(true);

        try {
            // Chargement du prononcé
            prononce = new IJPrononce();
            prononce.setIdPrononce(getIdPrononce());
            prononce.setSession(getSession());
            prononce.retrieve();

            try {
                // le modele
                String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
                if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                    setTemplateFile(IJDecision.FICHIER_MODELE_DECISION + extensionModelCaisse);
                    FWIImportManager im = getImporter();
                    File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                            + FWITemplateType.TEMPLATE_JASPER.toString());
                    if ((sourceFile != null) && sourceFile.exists()) {
                        ;
                    } else {
                        setTemplateFile(IJDecision.FICHIER_MODELE_DECISION);
                    }
                } else {
                    setTemplateFile(IJDecision.FICHIER_MODELE_DECISION);
                }
            } catch (Exception e) {
                setTemplateFile(IJDecision.FICHIER_MODELE_DECISION);
            }

            documentProperties = getScalableDocumentProperties();

            tiers = PRTiersHelper.getTiersParId(getSession(), documentProperties.getIdTiersPrincipal());
            codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            // chargement du catalogue de texte
            if (documentHelper == null) {
                documentHelper = PRBabelHelper.getDocumentHelper(getISession());
                documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
                documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_DECISION);
                documentHelper.setDefault(Boolean.TRUE);
                documentHelper.setActif(Boolean.TRUE);
            }

            documentHelper.setCodeIsoLangue(codeIsoLangue);

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                getMemoryLog()
                        .logMessage("impossible de charger le catalogue de texte", FWMessage.ERREUR, "IJDécision");
                abort();
            } else {
                document = documents[0];
            }

        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte pour la décision IJAI", e);
        }
    }

    @Override
    public void createDataSource() throws Exception {

        lignes = new LinkedList();
        champs = new HashMap();
        HashMap headerChamps = new HashMap();

        StringBuffer buffer = new StringBuffer();

        try {
            BSessionUtil.initContext(getSession(), this);

            // Initialisation du caisseHelper.
            // Ne peut pas être fait avant, car le docInfo est null !!
            // Comme on y passe qu'une seule fois, ne pose pas de problème .

            // creation du helper pour les entêtes et pieds de page
            caisseHelper = new IJDecisionACaisseReportHelper(getDocumentInfo());
            caisseHelper.init(getSession().getApplication(), codeIsoLangue);

            // Si l'office AI correspond à la caisse qui génère la décision, on
            // utilise une entête spécifique si elle existe, sinon on prend
            // celle par défaut
            if (PRAbstractApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ)
                    .getProperty(IJApplication.HAS_SPECIFIC_HEADER).equals("true")) {
                if (prononce.getOfficeAI().equals(
                        PRAbstractApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                IJApplication.PROPERTY_NO_OFFICEAI))) {
                    specificHeader = IJDecision.PREFIX_HEADER_DECISION + prononce.getOfficeAI()
                            + IJDecision.SUFIX_HEADER_DECISION;
                    caisseHelper.setHeaderName(specificHeader);
                } else {
                    caisseHelper.setHeaderName(IJDecision.PREFIX_HEADER_DECISION + "defaut"
                            + IJDecision.SUFIX_HEADER_DECISION);
                }
            } else {
                try {
                    String enteteOfficeAiSpec = getImporter().getTemplateProperty(getDocumentInfo(),
                            "header.specific.filename");
                    if (!JadeStringUtil.isEmpty(enteteOfficeAiSpec)) {
                        caisseHelper.setHeaderName(enteteOfficeAiSpec);
                    } else {
                        caisseHelper.setHeaderName(IJDecision.PREFIX_HEADER_DECISION + "defaut"
                                + IJDecision.SUFIX_HEADER_DECISION);
                    }
                } catch (Exception e) {
                    // Si une exception est levée, ont utilise le modèle par défaut
                    caisseHelper.setHeaderName(IJDecision.PREFIX_HEADER_DECISION + "defaut"
                            + IJDecision.SUFIX_HEADER_DECISION);
                }

            }

            createDocInfoIjDecision();

            // Initialisation du prononce

            // Récupération des IJCalculees pour le prononce courant
            IJIJCalculeeManager ijijCalculeeManager = new IJIJCalculeeManager();
            ijijCalculeeManager.setSession(getSession());
            ijijCalculeeManager.setForIdPrononce(documentProperties.getParameter("idPrononce"));
            ijijCalculeeManager.find(BManager.SIZE_NOLIMIT);

            // Tri des ijCalculees selon la date de début
            JAVector ijijCalculees = ijijCalculeeManager.getContainer();

            Collections.sort(ijijCalculees, new Comparator() {
                @Override
                public int compare(Object ijij1, Object ijij2) {
                    String dateijij1 = "";
                    String dateijij2 = "";
                    try {
                        dateijij1 = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(((IJIJCalculee) ijij1)
                                .getDateDebutDroit());
                        dateijij2 = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(((IJIJCalculee) ijij2)
                                .getDateDebutDroit());
                    } catch (Exception e) {
                    }
                    return new Integer(dateijij1).intValue() - new Integer(dateijij2).intValue();
                }
            });

            // Recherche du no de la revision à garantir
            String noRev = documentProperties.getParameter("garantitRevision");

            // Suppression des ij ne correspondant pas au no de rev. à garantir
            int i = 0;
            while (i < ijijCalculees.size()) {

                IJIJCalculee ijijCalculee1 = (IJIJCalculee) ijijCalculees.get(i);

                int j = 0;
                for (j = i + 1; j < ijijCalculees.size(); j++) {

                    IJIJCalculee ijijCalculee2 = (IJIJCalculee) ijijCalculees.get(j);

                    if (ijijCalculee1.getDateDebutDroit().equals(ijijCalculee2.getDateDebutDroit())) {

                        if (ijijCalculee1.getNoRevision().equals(noRev)) {
                            ijijCalculees.remove(j);
                        } else {
                            ijijCalculees.remove(i);
                        }
                    }
                }

                i++;
            }

            // Recherche de la situation professionnelle et calcul du revenu
            // déterminant annuel
            IJSituationProfessionnelleManager ijSituationProfessionnelleManager = new IJSituationProfessionnelleManager();
            ijSituationProfessionnelleManager.setSession(getSession());
            ijSituationProfessionnelleManager.setForIdPrononce(documentProperties.getParameter("idPrononce"));
            ijSituationProfessionnelleManager.find(BManager.SIZE_NOLIMIT);

            Iterator ijSituationProfessionnelleIter = ijSituationProfessionnelleManager.iterator();
            FWCurrency revenuDeterminantAnnuelReel = new FWCurrency("0");

            while (ijSituationProfessionnelleIter.hasNext()) {
                IJSituationProfessionnelle ijSituationProfessionnelle = (IJSituationProfessionnelle) ijSituationProfessionnelleIter
                        .next();
                revenuDeterminantAnnuelReel.add(IJDecision.getSalaireAnnuelVerse(ijSituationProfessionnelle));
            }

            // Affichage des IJCalculees pour le prononce courant
            for (int k = 0; k < ijijCalculees.size(); k++) {

                IJIJCalculee ijijCalculee = (IJIJCalculee) ijijCalculees.get(k);

                // Recherche des valeurs des champs header
                headerChamps = new HashMap();

                String cdtTitrePeriode = "";

                // Insertion du titre + période
                if (!JadeStringUtil.isBlank(ijijCalculee.getDateDebutDroit())
                        && !JadeStringUtil.isBlank(ijijCalculee.getDateFinDroit())) {
                    cdtTitrePeriode = document.getTextes(2).getTexte(1).getDescription();
                } else {
                    cdtTitrePeriode = document.getTextes(2).getTexte(2).getDescription();
                }

                cdtTitrePeriode = PRStringUtils.replaceString(cdtTitrePeriode, IJDecision.CDT_TITREPERIODE_DATEDEBUT,
                        ijijCalculee.getDateDebutDroit());
                cdtTitrePeriode = PRStringUtils.replaceString(cdtTitrePeriode, IJDecision.CDT_TITREPERIODE_DATEFIN,
                        ijijCalculee.getDateFinDroit());

                buffer.setLength(0);
                buffer.append(cdtTitrePeriode);
                headerChamps.put("PARAM_TITRE_PERIODE", buffer.toString());

                // Insertion du motif
                buffer.setLength(0);
                buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                        "{valeurMotif}", getLibelleGenreReadaptation(prononce.getCsGenre())));

                headerChamps.put("PARAM_TITRE_MOTIF", buffer.toString());

                // Recherche de l'indemnite journalière pour l'IJ courante
                IJIndemniteJournaliereManager ijIndemniteJournaliereManager = new IJIndemniteJournaliereManager();
                ijIndemniteJournaliereManager.setSession(getSession());
                ijIndemniteJournaliereManager.setForIdIJCalculee(ijijCalculee.getIdIJCalculee());
                ijIndemniteJournaliereManager.find();

                // Récupérartion des indemnités jour. interne et externe
                int nbIndemniteJour = 0;

                IJIndemniteJournaliere ijIndemniteJournaliereExterne = null;
                IJIndemniteJournaliere ijIndemniteJournaliereInterne = null;

                for (Iterator iterIdemniteJournaliere = ijIndemniteJournaliereManager.iterator(); iterIdemniteJournaliere
                        .hasNext(); nbIndemniteJour++) {
                    IJIndemniteJournaliere ijIndemniteJournaliere = (IJIndemniteJournaliere) iterIdemniteJournaliere
                            .next();
                    if (ijIndemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_EXTERNE)) {
                        ijIndemniteJournaliereExterne = ijIndemniteJournaliere;
                    }
                    if (ijIndemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE)) {
                        ijIndemniteJournaliereInterne = ijIndemniteJournaliere;
                    }
                }

                // Si l'indemnité est de type externe, on affiche le libellé du
                // montant journalier réduit et du montant journalier
                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                    buffer.setLength(0);
                    buffer.append(document.getTextes(2).getTexte(5).getDescription());// Mont
                    // red.
                    // Interne
                    headerChamps.put("PARAM_TITRE_MONTRED", buffer.toString());

                    buffer.setLength(0);
                    buffer.append(document.getTextes(2).getTexte(4).getDescription());// Mont
                    // jour.
                    // externe
                    headerChamps.put("PARAM_TITRE_MONT", buffer.toString());
                } else {
                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                        // Si l'ijcalculee est de type interne on affiche le
                        // libelle montant red.
                        buffer.setLength(0);
                        buffer.append(document.getTextes(2).getTexte(5).getDescription());
                        headerChamps.put("PARAM_TITRE_MONT_2", buffer.toString());
                    } else {
                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_EXTERNE)) {
                            // Si l'ijcalculee est de type externe on affiche le
                            // libelle montant jour.
                            buffer.setLength(0);
                            buffer.append(document.getTextes(2).getTexte(4).getDescription());
                            headerChamps.put("PARAM_TITRE_MONT_2", buffer.toString());
                        }
                    }
                }

                // Recherche de la devise
                StringBuffer bufferDevise = new StringBuffer();
                bufferDevise.setLength(0);
                bufferDevise.append(document.getTextes(2).getTexte(19).getDescription());

                StringBuffer bufferDeviseBold = new StringBuffer();
                bufferDeviseBold.setLength(0);
                bufferDeviseBold.append(document.getTextes(2).getTexte(23).getDescription());

                // Puis on renseigne les champs de la partie détail du rapport

                // Déclaration d'un tableau contenant les lignes à ajouter
                JAVector lignesAajouter = new JAVector();

                boolean isIndemnitePlafonnee = false;
                boolean isPrestationEnfantPlafonnee = false;
                boolean isPrestationEnfant = false;
                boolean isIndemniteMinimumGarantit = false;
                boolean isMaxPetiteIJ = false;
                BigDecimal montantTotalPlafonne = new BigDecimal(0);

                // Déclaration d'un buffer Base de calcul, dédié à la bande
                // Group2footer du rapport
                StringBuffer bufferBaseCalcul = new StringBuffer();
                // Insertion du titre
                bufferBaseCalcul.append(document.getTextes(3).getTexte(1).getDescription());

                // Recherche de la grande ou petite IJCalculee pour l'IJCalculee
                // courante
                IJIJCalculee ijGrandePetiteIjCalculee = null;

                if (ijijCalculee.getCsTypeIJ().equals(IIJIJCalculee.CS_TYPE_GRANDE_IJ)) {
                    ijGrandePetiteIjCalculee = new IJGrandeIJCalculee();
                    ijGrandePetiteIjCalculee.setSession(getSession());
                    ijGrandePetiteIjCalculee.setIdIJCalculee(ijijCalculee.getIdIJCalculee());
                    ijGrandePetiteIjCalculee.retrieve();
                } else {
                    if (ijijCalculee.getCsTypeIJ().equals(IIJIJCalculee.CS_TYPE_PETITE_IJ)) {
                        ijGrandePetiteIjCalculee = new IJPetiteIJCalculee();
                        ijGrandePetiteIjCalculee.setSession(getSession());
                        ijGrandePetiteIjCalculee.setIdIJCalculee(ijijCalculee.getIdIJCalculee());
                        ijGrandePetiteIjCalculee.retrieve();
                    }
                }

                // Recherche si prestations enfant
                if ((ijGrandePetiteIjCalculee != null) && (ijGrandePetiteIjCalculee instanceof IJGrandeIJCalculee)) {
                    if (!JadeStringUtil.isBlankOrZero(((IJGrandeIJCalculee) ijGrandePetiteIjCalculee)
                            .getMontantIndemniteEnfant())) {
                        isPrestationEnfant = true;
                    }
                }

                // Determine la periode de l'IJ
                boolean is2007 = false;
                if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(ijijCalculee.getDateDebutDroit()))
                        .intValue() < 20080101) {
                    is2007 = true;
                }

                boolean isDes2016 = (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(ijijCalculee
                        .getDateDebutDroit())).intValue() > 20151231);

                // Indemnité de base, plafonnée ou minimum garantit
                if (!JadeStringUtil.isBlankOrZero(ijijCalculee.getMontantBase())) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    String montantPlafonneeRev4 = "";

                    if (is2007) {
                        montantPlafonneeRev4 = PRAbstractApplication.getApplication(
                                IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                IJApplication.PROPERTY_MONTANT_INDEMNITE_PLAFONNEE_REV_4);
                    } else {
                        montantPlafonneeRev4 = PRAbstractApplication.getApplication(
                                IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                IJApplication.PROPERTY_MONTANT_INDEMNITE_PLAFONNEE_REV_4_DES2008);
                    }

                    String montantBase = ijijCalculee.getMontantBase();

                    if (ijijCalculee.getCsTypeIJ().equals(IIJIJCalculee.CS_TYPE_GRANDE_IJ)) {

                        // Si indemnité de base >= Montant plafonne, on prend en
                        // compte le montant plafonne
                        if (new BigDecimal(montantBase).compareTo(new BigDecimal(montantPlafonneeRev4)) >= 0) {

                            isIndemnitePlafonnee = true;

                            buffer.setLength(0);
                            buffer.append(document.getTextes(2).getTexte(7).getDescription());
                            champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                            buffer.setLength(0);
                            buffer.append(montantBase);

                            if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                                champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                                champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                            } else {
                                champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                            }

                            // Insertion du revenu déterminant plafonné dans la
                            // Base de calcul

                            String montantRevenuMaximumGarantitRev = "";

                            // Avant 5ème révision
                            if (!IJDecision.NO5EMEREVISION.equals(noRev)) {
                                if (is2007) {
                                    montantRevenuMaximumGarantitRev = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_4);
                                } else {
                                    montantRevenuMaximumGarantitRev = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_4_DES2008);
                                }
                            }
                            // 5ème révision
                            else {
                                // 5ème révision dès 2016
                                if (isDes2016) {
                                    montantRevenuMaximumGarantitRev = IJProperties.MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_5_DES2016
                                            .getValue();
                                }
                                // 5ème révision jusqu'à fin 2015
                                else {
                                    montantRevenuMaximumGarantitRev = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_5);
                                }
                            }

                            String cdtRevenuCalculeBc = document.getTextes(3).getTexte(2).getDescription();
                            BigDecimal revenuDeterminantAnnuel = new BigDecimal(montantRevenuMaximumGarantitRev)
                                    .multiply(new BigDecimal(365));
                            cdtRevenuCalculeBc = PRStringUtils.replaceString(cdtRevenuCalculeBc,
                                    IJDecision.CDT_REVENUANNUEL,
                                    new FWCurrency(revenuDeterminantAnnuel.doubleValue()).toStringFormat());
                            cdtRevenuCalculeBc = PRStringUtils.replaceString(cdtRevenuCalculeBc,
                                    IJDecision.CDT_REVENUJOURNALIER, montantRevenuMaximumGarantitRev);
                            cdtRevenuCalculeBc = PRStringUtils.replaceString(cdtRevenuCalculeBc, IJDecision.CDT_DEVISE,
                                    bufferDevise.toString());
                            bufferBaseCalcul.append(cdtRevenuCalculeBc);

                            String cdtRevenuTexteCalculeBc = document.getTextes(3).getTexte(6).getDescription();
                            cdtRevenuTexteCalculeBc = PRStringUtils.replaceString(cdtRevenuTexteCalculeBc,
                                    IJDecision.CDT_INDEMNITEDEBASE, montantBase);
                            cdtRevenuTexteCalculeBc = PRStringUtils.replaceString(cdtRevenuTexteCalculeBc,
                                    IJDecision.CDT_REVENUDETERMINANT, revenuDeterminantAnnuelReel.toStringFormat());
                            cdtRevenuTexteCalculeBc = PRStringUtils.replaceString(cdtRevenuTexteCalculeBc,
                                    IJDecision.CDT_REVENUDM,
                                    (new FWCurrency(revenuDeterminantAnnuel.doubleValue())).toStringFormat());
                            cdtRevenuTexteCalculeBc = PRStringUtils.replaceString(cdtRevenuTexteCalculeBc,
                                    IJDecision.CDT_DEVISE, bufferDevise.toString());
                            bufferBaseCalcul.append(cdtRevenuTexteCalculeBc);

                        } else {
                            String montantMinimumGarantitRev4 = "";
                            if (is2007) {
                                montantMinimumGarantitRev4 = PRAbstractApplication.getApplication(
                                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                        IJApplication.PROPERTY_MONTANT_INDEMNITE_MINIMUM_GARANTI_REV_4);
                            } else {
                                montantMinimumGarantitRev4 = PRAbstractApplication.getApplication(
                                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                        IJApplication.PROPERTY_MONTANT_INDEMNITE_MINIMUM_GARANTI_REV_4_DES2008);
                            }
                            // Si indemnité de base <= Montant garantit, on
                            // prend en compte le montant minimum garantit et si
                            // l'IJ concerne la 5ème révision, le minimum
                            // garantit n'est plus applicable
                            if ((new BigDecimal(montantBase).compareTo(new BigDecimal(montantMinimumGarantitRev4)) <= 0)
                                    && !IJDecision.NO5EMEREVISION.equals(noRev)) {

                                isIndemniteMinimumGarantit = true;
                                buffer.setLength(0);
                                buffer.append(document.getTextes(2).getTexte(9).getDescription());
                                champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                                buffer.setLength(0);
                                buffer.append(montantBase);

                                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                                    champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                                    champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                                } else {
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }

                                // Insertion du revenu déterminant minimum
                                // garantit dans la base de calcul selon
                                // l'existance de prestations pour enfants
                                String revenuDeterminantMin = "";
                                if (is2007) {
                                    revenuDeterminantMin = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4);
                                } else {
                                    revenuDeterminantMin = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4_DES2008);
                                }

                                if (!isPrestationEnfant) {

                                    String cdtRevenuDeterminantMG = document.getTextes(3).getTexte(13).getDescription();

                                    BigDecimal revenuDeterminantAnnuel = new BigDecimal(revenuDeterminantMin)
                                            .multiply(new BigDecimal(365));
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_REVENUANNUEL,
                                            new FWCurrency(revenuDeterminantAnnuel.doubleValue()).toStringFormat());
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_REVENUJOURNALIER, revenuDeterminantMin);
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_DEVISE, bufferDevise.toString());

                                    bufferBaseCalcul.append(cdtRevenuDeterminantMG);

                                    String cdtRevenuTexteDeterminantMG = document.getTextes(3).getTexte(14)
                                            .getDescription();
                                    cdtRevenuTexteDeterminantMG = PRStringUtils.replaceString(
                                            cdtRevenuTexteDeterminantMG, IJDecision.CDT_INDEMNITEDEBASE,
                                            ijijCalculee.getMontantBase());
                                    cdtRevenuTexteDeterminantMG = PRStringUtils.replaceString(
                                            cdtRevenuTexteDeterminantMG, IJDecision.CDT_REVENUDETERMINANT,
                                            revenuDeterminantAnnuelReel.toStringFormat());

                                    cdtRevenuTexteDeterminantMG = PRStringUtils
                                            .replaceString(cdtRevenuTexteDeterminantMG, IJDecision.CDT_DEVISE,
                                                    bufferDevise.toString());

                                    bufferBaseCalcul.append(cdtRevenuTexteDeterminantMG);

                                } else {

                                    String cdtRevenuDeterminantMG = document.getTextes(3).getTexte(13).getDescription();

                                    BigDecimal revenuDeterminantAnnuel = new BigDecimal(revenuDeterminantMin)
                                            .multiply(new BigDecimal(365));
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_REVENUANNUEL,
                                            new FWCurrency(revenuDeterminantAnnuel.doubleValue()).toStringFormat());
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_REVENUJOURNALIER, revenuDeterminantMin);
                                    cdtRevenuDeterminantMG = PRStringUtils.replaceString(cdtRevenuDeterminantMG,
                                            IJDecision.CDT_DEVISE, bufferDevise.toString());

                                    bufferBaseCalcul.append(cdtRevenuDeterminantMG);

                                    String cdtRevenuTexteDeterminantMG = document.getTextes(3).getTexte(17)
                                            .getDescription();
                                    cdtRevenuTexteDeterminantMG = PRStringUtils.replaceString(
                                            cdtRevenuTexteDeterminantMG, IJDecision.CDT_INDEMNITEDEBASE,
                                            ijijCalculee.getMontantBase());
                                    cdtRevenuTexteDeterminantMG = PRStringUtils.replaceString(
                                            cdtRevenuTexteDeterminantMG, IJDecision.CDT_REVENUDETERMINANT,
                                            revenuDeterminantAnnuelReel.toStringFormat());
                                    cdtRevenuTexteDeterminantMG = PRStringUtils
                                            .replaceString(cdtRevenuTexteDeterminantMG, IJDecision.CDT_DEVISE,
                                                    bufferDevise.toString());

                                    bufferBaseCalcul.append(cdtRevenuTexteDeterminantMG);

                                }
                            } else {

                                // Sinon on prend en compte l'indemnité de base
                                buffer.setLength(0);
                                buffer.append(document.getTextes(2).getTexte(6).getDescription());
                                champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                                buffer.setLength(0);
                                buffer.append(montantBase);

                                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                                    champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                                    champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                                } else {
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }

                                // Insertion du revenu déterminant de base dans
                                // la base de calcul
                                String cdtRevenuDeterminantDebase = document.getTextes(3).getTexte(8).getDescription();

                                cdtRevenuDeterminantDebase = PRStringUtils.replaceString(cdtRevenuDeterminantDebase,
                                        IJDecision.CDT_REVENUANNUEL, revenuDeterminantAnnuelReel.toStringFormat());
                                cdtRevenuDeterminantDebase = PRStringUtils.replaceString(cdtRevenuDeterminantDebase,
                                        IJDecision.CDT_REVENUJOURNALIER, ijijCalculee.getRevenuDeterminant());
                                cdtRevenuDeterminantDebase = PRStringUtils.replaceString(cdtRevenuDeterminantDebase,
                                        IJDecision.CDT_DEVISE, bufferDevise.toString());
                                bufferBaseCalcul.append(cdtRevenuDeterminantDebase);

                                String cdtRevenuTexteDeterminantDebase = document.getTextes(3).getTexte(4)
                                        .getDescription();
                                cdtRevenuTexteDeterminantDebase = PRStringUtils.replaceString(
                                        cdtRevenuTexteDeterminantDebase, IJDecision.CDT_INDEMNITEDEBASE, montantBase);
                                cdtRevenuTexteDeterminantDebase = PRStringUtils
                                        .replaceString(cdtRevenuTexteDeterminantDebase, IJDecision.CDT_DEVISE,
                                                bufferDevise.toString());

                                bufferBaseCalcul.append(cdtRevenuTexteDeterminantDebase);

                                // Insertion de la difference de revenu dans la
                                // base de calcul
                                if (!JadeStringUtil.isBlankOrZero(ijijCalculee.getDifferenceRevenu())) {
                                    if (new Float(ijijCalculee.getDifferenceRevenu()).floatValue() <= 0) {

                                        BigDecimal revenuDurantReadaptation = null;
                                        String cdtDifferenceRevenu = document.getTextes(3).getTexte(12)
                                                .getDescription();

                                        IJRevenu ijRevenuDurantReadaptation = new IJRevenu();
                                        ijRevenuDurantReadaptation.setSession(getSession());
                                        ijRevenuDurantReadaptation.setId(prononce.getIdRevenuReadaptation());
                                        ijRevenuDurantReadaptation.retrieve();

                                        if (!ijRevenuDurantReadaptation.isNew()
                                                && !(new BigDecimal(ijRevenuDurantReadaptation.getRevenu())
                                                        .equals(new BigDecimal("0.000")))
                                                && !(new BigDecimal(ijRevenuDurantReadaptation.getRevenu())
                                                        .equals(new BigDecimal("0.00")))
                                                && !(new BigDecimal(ijRevenuDurantReadaptation.getRevenu())
                                                        .equals(new BigDecimal("0.0")))
                                                && !(new BigDecimal(ijRevenuDurantReadaptation.getRevenu())
                                                        .equals(new BigDecimal("0")))) {

                                            if (IIJSituationProfessionnelle.CS_MENSUEL
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString());
                                            }
                                            if (IIJSituationProfessionnelle.CS_ANNUEL.equals(ijRevenuDurantReadaptation
                                                    .getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString()).divide(new BigDecimal(12), 10,
                                                        BigDecimal.ROUND_DOWN);
                                            }
                                            if (IIJSituationProfessionnelle.CS_HEBDOMADAIRE
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString()).multiply(new BigDecimal(4));
                                            }
                                            if (IIJSituationProfessionnelle.CS_JOURNALIER
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString()).multiply(new BigDecimal(30));
                                            }
                                            if (IIJSituationProfessionnelle.CS_4_SEMAINES
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString());
                                            }
                                            if (IIJSituationProfessionnelle.CS_HORAIRE
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString()).multiply(new BigDecimal(4));
                                            }
                                            if (IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE
                                                    .equals(ijRevenuDurantReadaptation.getCsPeriodiciteRevenu())) {
                                                revenuDurantReadaptation = new BigDecimal(ijRevenuDurantReadaptation
                                                        .getRevenu().toString());
                                            }
                                        } else {
                                            revenuDurantReadaptation = new BigDecimal(
                                                    ijijCalculee.getRevenuJournalierReadaptation())
                                                    .multiply(new BigDecimal(30));
                                        }

                                        if (null != revenuDurantReadaptation) {
                                            cdtDifferenceRevenu = PRStringUtils.replaceString(cdtDifferenceRevenu,
                                                    IJDecision.CDT_REVENUDROIT, revenuDurantReadaptation.toString());
                                            cdtDifferenceRevenu = PRStringUtils.replaceString(cdtDifferenceRevenu,
                                                    IJDecision.CDT_DEVISE, bufferDevise.toString());
                                            bufferBaseCalcul.append(cdtDifferenceRevenu);
                                        }

                                    }
                                }
                            }
                        }
                    }// Finalement on traite le cas pour la petite IJ
                    else {

                        buffer.setLength(0);
                        buffer.append(document.getTextes(2).getTexte(6).getDescription());
                        champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                        buffer.setLength(0);
                        buffer.append(montantBase);

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                            champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                            champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                            champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                            champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                        } else {
                            champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                            champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                        }

                        String montantMaxPetiteIj = "";
                        if (is2007) {
                            montantMaxPetiteIj = PRAbstractApplication.getApplication(
                                    IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                    IJApplication.PROPERTY_MONTANT_MAX_PETITE_IJ);
                        } else {
                            // Si 5ème révision
                            if (IJDecision.NO5EMEREVISION.equals(noRev)) {
                                // 5ème révision à partir de 2016
                                if (isDes2016) {
                                    montantMaxPetiteIj = IJProperties.MONTANT_PETITE_IJ_MAXIMUM_REV_5_DES2016
                                            .getValue();
                                }
                                // 5ème révision avant 2016
                                else {
                                    montantMaxPetiteIj = PRAbstractApplication.getApplication(
                                            IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                            IJApplication.PROPERTY_MONTANT_MAX_PETITE_IJ_REV_5_DES2008);
                                }
                            }
                            // 4ème révision
                            else {
                                montantMaxPetiteIj = PRAbstractApplication.getApplication(
                                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                        IJApplication.PROPERTY_MONTANT_MAX_PETITE_IJ_REV_4_DES2008);
                            }
                        }

                        if (new BigDecimal(montantBase).compareTo(new BigDecimal(montantMaxPetiteIj)) >= 0) {
                            isMaxPetiteIJ = true;
                            // Insertion du maximum de la petite indemnité
                            // journalière dans la base de calcul
                            String cdtIndemnitePetiteIj = document.getTextes(3).getTexte(3).getDescription();
                            cdtIndemnitePetiteIj = PRStringUtils.replaceString(cdtIndemnitePetiteIj,
                                    IJDecision.CDT_INDEMNITEDEBASE, montantBase);
                            cdtIndemnitePetiteIj = PRStringUtils.replaceString(cdtIndemnitePetiteIj,
                                    IJDecision.CDT_DEVISE, bufferDevise.toString());

                            bufferBaseCalcul.append(cdtIndemnitePetiteIj);
                        } else {
                            // Insertion de la petite indemnité journalière dans
                            // la base de calcul
                            String cdtIndemnitePetiteIj = document.getTextes(3).getTexte(7).getDescription();
                            cdtIndemnitePetiteIj = PRStringUtils.replaceString(cdtIndemnitePetiteIj,
                                    IJDecision.CDT_INDEMNITEDEBASE, montantBase);
                            cdtIndemnitePetiteIj = PRStringUtils.replaceString(cdtIndemnitePetiteIj,
                                    IJDecision.CDT_DEVISE, bufferDevise.toString());

                            bufferBaseCalcul.append(cdtIndemnitePetiteIj);
                        }

                        // Insertion du dernier revenu avant l'interruption de
                        // formation dans la base de calcul
                        IJPetiteIJJointRevenu ijPetiteIjJointRevenu = new IJPetiteIJJointRevenu();
                        ijPetiteIjJointRevenu.setSession(getSession());
                        ijPetiteIjJointRevenu.setIdPrononce(prononce.getIdPrononce());
                        ijPetiteIjJointRevenu.retrieve();

                        if (!ijPetiteIjJointRevenu.isNew()
                                && IIJPetiteIJCalculee.CS_NOUVELLE_FORME_APRES_INTERRUPTION
                                        .equals(((IJPetiteIJCalculee) ijGrandePetiteIjCalculee).getCsModeCalcul())
                                && !JadeStringUtil.isBlankOrZero(ijPetiteIjJointRevenu.getRevenu())) {

                            bufferBaseCalcul.append(document.getTextes(3).getTexte(21).getDescription());
                        }

                    }// Fin test si petite IJ

                    montantTotalPlafonne = montantTotalPlafonne.add(new BigDecimal(montantBase));

                    lignesAajouter.add(champs);
                }// Fin test si mnt base null

                // Prestations pour n enfants
                if (isPrestationEnfant) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    String cdtMontantIndemniteEnfant = "";

                    // Contrôle si la prestation pour n enfants est plafonnée
                    if (new BigDecimal(ijijCalculee.getMontantBase())
                            .add(new BigDecimal(((IJGrandeIJCalculee) ijGrandePetiteIjCalculee)
                                    .getMontantIndemniteEnfant())).compareTo(
                                    new BigDecimal(ijijCalculee.getRevenuDeterminant())) >= 0) {
                        cdtMontantIndemniteEnfant = document.getTextes(2).getTexte(20).getDescription();
                        isPrestationEnfantPlafonnee = true;
                    } else {
                        cdtMontantIndemniteEnfant = document.getTextes(2).getTexte(11).getDescription();
                    }

                    cdtMontantIndemniteEnfant = PRStringUtils.replaceString(cdtMontantIndemniteEnfant,
                            IJDecision.CDT_NBENFANT, ((IJGrandeIJCalculee) ijGrandePetiteIjCalculee).getNbEnfants());
                    buffer.setLength(0);
                    buffer.append(cdtMontantIndemniteEnfant);
                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    buffer.setLength(0);
                    buffer.append(((IJGrandeIJCalculee) ijGrandePetiteIjCalculee).getMontantIndemniteEnfant());

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {
                        champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                        champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                    } else {
                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                    }

                    lignesAajouter.add(champs);

                    // Insertion de la prestation pour enfants dans la base de
                    // calcul à condition que l'indemn. ne soit pas plafonnée
                    if (!isIndemnitePlafonnee) {

                        String cdtPrestationEnfant = "";

                        if (isIndemniteMinimumGarantit) {

                            cdtPrestationEnfant = document.getTextes(3).getTexte(15).getDescription();
                            cdtPrestationEnfant = PRStringUtils.replaceString(cdtPrestationEnfant,
                                    IJDecision.CDT_NBENFANT,
                                    ((IJGrandeIJCalculee) ijGrandePetiteIjCalculee).getNbEnfants());

                            String revenuDeterminantMin = "";
                            if (is2007) {
                                revenuDeterminantMin = PRAbstractApplication.getApplication(
                                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                        IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4);
                            } else {
                                revenuDeterminantMin = PRAbstractApplication.getApplication(
                                        IJApplication.DEFAULT_APPLICATION_IJ).getProperty(
                                        IJApplication.PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4_DES2008);
                            }
                            cdtPrestationEnfant = cdtPrestationEnfant
                                    + document.getTextes(3).getTexte(18).getDescription();
                            cdtPrestationEnfant = PRStringUtils.replaceString(cdtPrestationEnfant,
                                    IJDecision.CDT_REVENUDM, revenuDeterminantMin);
                            cdtPrestationEnfant = PRStringUtils.replaceString(cdtPrestationEnfant,
                                    IJDecision.CDT_DEVISE, bufferDevise.toString());
                        } else {
                            // Insertion dans la base de calcul si indemnité de
                            // base et prestation enfant plafonnée
                            if (isPrestationEnfantPlafonnee) {
                                cdtPrestationEnfant = document.getTextes(3).getTexte(16).getDescription();
                            }
                        }

                        bufferBaseCalcul.append(cdtPrestationEnfant);
                    }

                    montantTotalPlafonne = montantTotalPlafonne.add(new BigDecimal(
                            ((IJGrandeIJCalculee) ijGrandePetiteIjCalculee).getMontantIndemniteEnfant()));

                }

                // Indemnité totale plafonnée
                if ((ijIndemniteJournaliereInterne != null)
                        && (isPrestationEnfantPlafonnee || (isPrestationEnfant && isIndemnitePlafonnee))) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    buffer.setLength(0);
                    buffer.append(document.getTextes(2).getTexte(8).getDescription());
                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                        buffer.setLength(0);
                        buffer.append(new FWCurrency(montantTotalPlafonne.doubleValue()).toStringFormat());

                        champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());

                        buffer.setLength(0);
                        buffer.append(new FWCurrency(montantTotalPlafonne.doubleValue()).toStringFormat());

                        champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                    } else {

                        buffer.setLength(0);
                        buffer.append(new FWCurrency(montantTotalPlafonne.doubleValue()).toStringFormat());

                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                    }

                    lignesAajouter.add(champs);

                }

                // Indemnité de l'assurance accident
                String ijIndemniteJournaliereExterneMontantGarantiAA = "";
                String ijIndemniteJournaliereInterneMontantGarantiAA = "";

                if (prononce.getMontantGarantiAAReduit().booleanValue()) {
                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getMontantGarantiAAReduit())
                            || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getMontantGarantiAAReduit())) {

                        ijIndemniteJournaliereExterneMontantGarantiAA = ijIndemniteJournaliereExterne
                                .getMontantGarantiAAReduit();
                        ijIndemniteJournaliereInterneMontantGarantiAA = ijIndemniteJournaliereInterne
                                .getMontantGarantiAAReduit();
                    }
                } else {
                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getMontantGarantiAANonReduit())
                            || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                    .getMontantGarantiAANonReduit())) {

                        ijIndemniteJournaliereExterneMontantGarantiAA = ijIndemniteJournaliereExterne
                                .getMontantGarantiAANonReduit();
                        ijIndemniteJournaliereInterneMontantGarantiAA = ijIndemniteJournaliereInterne
                                .getMontantGarantiAANonReduit();
                    }
                }
                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterneMontantGarantiAA)
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterneMontantGarantiAA)) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    buffer.setLength(0);
                    buffer.append(document.getTextes(2).getTexte(10).getDescription());
                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                        buffer.setLength(0);
                        buffer.append(ijIndemniteJournaliereExterneMontantGarantiAA);

                        champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());

                        buffer.setLength(0);
                        buffer.append(ijIndemniteJournaliereInterneMontantGarantiAA);

                        champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                    } else {

                        buffer.setLength(0);

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                            buffer.append(ijIndemniteJournaliereInterneMontantGarantiAA);
                        } else {
                            buffer.append(ijIndemniteJournaliereExterneMontantGarantiAA);
                        }

                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                    }

                    lignesAajouter.add(champs);

                    // Si montant AA > Indemnité journalier, on ajoute le texte
                    // AA favorable dans la partie base de calcul

                    if (new BigDecimal(ijijCalculee.getMontantBase()).compareTo(new BigDecimal(
                            ijIndemniteJournaliereInterneMontantGarantiAA)) < 0) {
                        bufferBaseCalcul.append(document.getTextes(3).getTexte(9).getDescription());
                    }
                }

                // Déductions
                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                        .getMontantReductionSiRevenuAvantReadaptation())
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                .getMontantReductionSiRevenuAvantReadaptation())
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getDeductionRenteAI())
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getDeductionRenteAI())
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                .getMontantSupplementaireReadaptation())) {

                    boolean isLibelleAffiche = false;

                    // Rente AI durant réadaptation
                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getDeductionRenteAI())
                            || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getDeductionRenteAI())) {

                        hasRenteAIDurantReadaptation = true;

                        if (!isLibelleAffiche) {
                            // Ajout du libelle "Déductions:"
                            champs = new HashMap();
                            champs.putAll(headerChamps);

                            buffer.setLength(0);
                            buffer.append("\n");
                            buffer.append(document.getTextes(2).getTexte(12).getDescription());
                            champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                            lignesAajouter.add(champs);

                            isLibelleAffiche = true;
                        }

                        champs = new HashMap();
                        champs.putAll(headerChamps);

                        buffer.setLength(0);
                        buffer.append(document.getTextes(2).getTexte(16).getDescription());
                        champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getDeductionRenteAI())) {

                                buffer.setLength(0);
                                buffer.append(ijIndemniteJournaliereInterne.getDeductionRenteAI());

                                champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                            }

                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getDeductionRenteAI())) {

                                buffer.setLength(0);
                                buffer.append(ijIndemniteJournaliereExterne.getDeductionRenteAI());

                                champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                            }
                        } else {
                            buffer.setLength(0);

                            if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getDeductionRenteAI())) {
                                    buffer.append(ijIndemniteJournaliereInterne.getDeductionRenteAI());
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }
                            } else {
                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getDeductionRenteAI())) {
                                    buffer.append(ijIndemniteJournaliereExterne.getDeductionRenteAI());
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }
                            }
                        }

                        lignesAajouter.add(champs);

                        // Ajout de l'AI dans la base de calcul
                        String cdtRenteAiDurantReadaptation = document.getTextes(3).getTexte(10).getDescription();
                        cdtRenteAiDurantReadaptation = PRStringUtils.replaceString(cdtRenteAiDurantReadaptation,
                                IJDecision.CDT_MONTANTRENTEAI, prononce.getMontantRenteEnCours());
                        cdtRenteAiDurantReadaptation = PRStringUtils.replaceString(cdtRenteAiDurantReadaptation,
                                IJDecision.CDT_DEVISE, bufferDevise.toString());

                        bufferBaseCalcul.append(cdtRenteAiDurantReadaptation);

                    }

                    // Activité lucrative durant la réadaptation
                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                            .getMontantReductionSiRevenuAvantReadaptation())
                            || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                    .getMontantReductionSiRevenuAvantReadaptation())) {

                        if (!isLibelleAffiche) {
                            // Ajout du libelle "Déductions:"
                            champs = new HashMap();
                            champs.putAll(headerChamps);

                            buffer.setLength(0);
                            buffer.append("\n");
                            buffer.append(document.getTextes(2).getTexte(12).getDescription());
                            champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                            lignesAajouter.add(champs);

                            isLibelleAffiche = true;
                        }

                        champs = new HashMap();
                        champs.putAll(headerChamps);

                        buffer.setLength(0);
                        buffer.append(document.getTextes(2).getTexte(17).getDescription());
                        champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                    .getMontantReductionSiRevenuAvantReadaptation())) {

                                buffer.setLength(0);
                                buffer.append(ijIndemniteJournaliereInterne
                                        .getMontantReductionSiRevenuAvantReadaptation());

                                champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                            }

                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                    .getMontantReductionSiRevenuAvantReadaptation())) {

                                buffer.setLength(0);
                                buffer.append(ijIndemniteJournaliereExterne
                                        .getMontantReductionSiRevenuAvantReadaptation());

                                champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                            }

                        } else {
                            buffer.setLength(0);

                            if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                        .getMontantReductionSiRevenuAvantReadaptation())) {
                                    buffer.append(ijIndemniteJournaliereInterne
                                            .getMontantReductionSiRevenuAvantReadaptation());
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }
                            } else {
                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                        .getMontantReductionSiRevenuAvantReadaptation())) {
                                    buffer.append(ijIndemniteJournaliereExterne
                                            .getMontantReductionSiRevenuAvantReadaptation());
                                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }
                            }

                        }

                        lignesAajouter.add(champs);

                        // Insertion de la difference de revenu dans la base de
                        // calcul
                        if ((new Float(ijijCalculee.getDifferenceRevenu()).floatValue() > 0)
                                || ijijCalculee.getCsTypeIJ().equals(IIJIJCalculee.CS_TYPE_PETITE_IJ)) {

                            String cdtDifferenceRevenuActiviteLucrative = document.getTextes(3).getTexte(11)
                                    .getDescription();

                            IJRevenu ijRevenuReadaptation = new IJRevenu();
                            ijRevenuReadaptation.setSession(getSession());
                            ijRevenuReadaptation.setId(prononce.getIdRevenuReadaptation());
                            ijRevenuReadaptation.retrieve();

                            BigDecimal revenuMensuelDurantReadaptation = new BigDecimal("0");

                            if (IIJSituationProfessionnelle.CS_MENSUEL.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString());
                            }
                            if (IIJSituationProfessionnelle.CS_ANNUEL.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString()).divide(new BigDecimal(12), 10, BigDecimal.ROUND_DOWN);
                            }
                            if (IIJSituationProfessionnelle.CS_HEBDOMADAIRE.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString()).multiply(new BigDecimal(4));
                            }
                            if (IIJSituationProfessionnelle.CS_JOURNALIER.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString()).multiply(new BigDecimal(30));
                            }
                            if (IIJSituationProfessionnelle.CS_4_SEMAINES.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString());
                            }
                            if (IIJSituationProfessionnelle.CS_HORAIRE.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString()).multiply(new BigDecimal(4));
                            }
                            if (IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE.equals(ijRevenuReadaptation
                                    .getCsPeriodiciteRevenu())) {
                                revenuMensuelDurantReadaptation = new BigDecimal(ijRevenuReadaptation.getRevenu()
                                        .toString());
                            }

                            cdtDifferenceRevenuActiviteLucrative = PRStringUtils.replaceString(
                                    cdtDifferenceRevenuActiviteLucrative, IJDecision.CDT_REVENUMENSUEL,
                                    revenuMensuelDurantReadaptation.toString());
                            cdtDifferenceRevenuActiviteLucrative = PRStringUtils.replaceString(
                                    cdtDifferenceRevenuActiviteLucrative, IJDecision.CDT_DEVISE,
                                    bufferDevise.toString());
                            bufferBaseCalcul.append(cdtDifferenceRevenuActiviteLucrative);

                        }

                    }

                    // Frais de repas pris en charge par l'AI
                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                            .getMontantSupplementaireReadaptation())
                            || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                    .getMontantSupplementaireReadaptation())) {
                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)
                                || prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {

                            if (!isLibelleAffiche) {
                                // Ajout du libelle "Déductions:"
                                champs = new HashMap();
                                champs.putAll(headerChamps);

                                buffer.setLength(0);
                                buffer.append("\n");
                                buffer.append(document.getTextes(2).getTexte(12).getDescription());
                                champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                                lignesAajouter.add(champs);

                                isLibelleAffiche = true;
                            }

                            champs = new HashMap();
                            champs.putAll(headerChamps);

                            buffer.setLength(0);
                            buffer.append(document.getTextes(2).getTexte(15).getDescription());
                            champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                            if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                        .getMontantSupplementaireReadaptation())) {

                                    buffer.setLength(0);
                                    buffer.append(ijIndemniteJournaliereInterne.getMontantSupplementaireReadaptation());

                                    champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                }
                                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                        .getMontantSupplementaireReadaptation())) {

                                    buffer.setLength(0);
                                    buffer.append(ijIndemniteJournaliereExterne.getMontantSupplementaireReadaptation());

                                    champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                                    champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());
                                }
                            } else {
                                buffer.setLength(0);

                                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                            .getMontantSupplementaireReadaptation())) {
                                        buffer.append(ijIndemniteJournaliereInterne
                                                .getMontantSupplementaireReadaptation());
                                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                    }
                                } else {
                                    if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                            .getMontantSupplementaireReadaptation())) {
                                        buffer.append(ijIndemniteJournaliereExterne
                                                .getMontantSupplementaireReadaptation());
                                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());
                                    }
                                }

                            }

                            lignesAajouter.add(champs);

                            String cdtAI = "";
                            // Ajout de l'AI dans la base de calcul
                            if (ijijCalculee.getCsTypeIJ().equals(IIJIJCalculee.CS_TYPE_PETITE_IJ)) {
                                if (isMaxPetiteIJ) {
                                    cdtAI = document.getTextes(3).getTexte(19).getDescription();
                                } else {
                                    cdtAI = document.getTextes(3).getTexte(5).getDescription();
                                }

                                bufferBaseCalcul.append(cdtAI);
                            }
                        }
                    }
                }

                // Montant brut de l'indemnité journalière
                if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getMontantJournalierIndemnite())
                        || !JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne.getMontantJournalierIndemnite())) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    buffer.setLength(0);
                    buffer.append("\n");
                    buffer.append(document.getTextes(2).getTexte(13).getDescription());
                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                        buffer.setLength(0);
                        buffer.append(ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());
                        champs.put("PARAM_MONT_JOUR", "\n" + afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_JOUR", "\n" + bufferDevise.toString());

                        buffer.setLength(0);
                        buffer.append(ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());
                        champs.put("PARAM_MONT_RED", "\n" + afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", "\n" + bufferDevise.toString());

                    } else {

                        buffer.setLength(0);

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne
                                    .getMontantJournalierIndemnite())) {
                                buffer.append(ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());
                            }
                        } else {
                            if (!JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereExterne
                                    .getMontantJournalierIndemnite())) {
                                buffer.append(ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());
                            }
                        }

                        champs.put("PARAM_MONT_JOUR_2", "\n" + afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", "\n" + bufferDevise.toString());

                    }

                    lignesAajouter.add(champs);
                }

                // Cotisations AVS/AI/APG/AC/LFA
                champs = new HashMap();
                champs.putAll(headerChamps);

                String ac = "/AC";

                String typeCoti = "AVS/AI/APG";

                // si page de cotisations AC
                if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())
                        || IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {

                } else {

                    typeCoti += ac;

                }

                buffer.setLength(0);

                IJDecisionCotisationBuilder ijDecisionCotisationBuilder = new IJDecisionCotisationBuilder();

                CatalogueText ct = new CatalogueText();
                ct.setCsTypeDocument(IIJCatalogueTexte.CS_DECISION);
                ct.setCsDomaine(IIJCatalogueTexte.CS_IJ);
                ct.setCodeIsoLangue(TITiers.toLangueIso(tiers.getLangue()));
                ct.setNomCatalogue("normal");

                BabelContainer babelContainer = new BabelContainer();
                babelContainer.addCatalogueText(ct);
                babelContainer.setSession(getSession());
                babelContainer.load();

                prononce.setIdPrononce(documentProperties.getParameter("idPrononce"));
                prononce.setSession(getSession());
                prononce.retrieve();

                double cotisation_AVS_AI_APG_AC_interne = 0;
                double cotisation_AVS_AI_APG_AC_externe = 0;

                double taux_coti_interne = 0;
                double taux_coti_externe = 0;

                String libelle_type_coti_interne = "";
                String libelle_type_coti_externe = "";

                if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                    cotisation_AVS_AI_APG_AC_interne = ijDecisionCotisationBuilder.buildCotisationsAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());

                    taux_coti_interne = ijDecisionCotisationBuilder.buildCotisationsTauxAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());

                    libelle_type_coti_interne = ijDecisionCotisationBuilder.buildCotisationsLibelleAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereInterne.getMontantJournalierIndemnite(),
                            babelContainer, ct);

                } else {
                    cotisation_AVS_AI_APG_AC_interne = ijDecisionCotisationBuilder.buildCotisationsEmployeur(
                            getSession(), getTransaction(), prononce,
                            ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());

                    taux_coti_interne = ijDecisionCotisationBuilder.buildCotisationsTauxEmployeur(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereInterne.getMontantJournalierIndemnite());

                    libelle_type_coti_interne = ijDecisionCotisationBuilder.buildCotisationsLibelleEmployeur(
                            getSession(), getTransaction(), prononce,
                            ijIndemniteJournaliereInterne.getMontantJournalierIndemnite(), babelContainer, ct);

                }

                if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                    cotisation_AVS_AI_APG_AC_externe = ijDecisionCotisationBuilder.buildCotisationsAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());

                    taux_coti_externe = ijDecisionCotisationBuilder.buildCotisationsTauxAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());

                    libelle_type_coti_externe = ijDecisionCotisationBuilder.buildCotisationsLibelleAssure(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereInterne.getMontantJournalierIndemnite(),
                            babelContainer, ct);

                } else {
                    cotisation_AVS_AI_APG_AC_externe = ijDecisionCotisationBuilder.buildCotisationsEmployeur(
                            getSession(), getTransaction(), prononce,
                            ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());

                    taux_coti_externe = ijDecisionCotisationBuilder.buildCotisationsTauxEmployeur(getSession(),
                            getTransaction(), prononce, ijIndemniteJournaliereExterne.getMontantJournalierIndemnite());

                    libelle_type_coti_externe = ijDecisionCotisationBuilder.buildCotisationsLibelleEmployeur(
                            getSession(), getTransaction(), prononce,
                            ijIndemniteJournaliereInterne.getMontantJournalierIndemnite(), babelContainer, ct);

                }

                // Retrieve du taux de cotisations
                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                    buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(14).getDescription(),
                            IJDecision.CDT_TAUXCOTI, String.valueOf(taux_coti_externe)));
                    buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(), IJDecision.CDT_TYPECOTI,
                            libelle_type_coti_externe));
                    if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                        buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                IJDecision.CDT_SIGNECOTI, "./."));
                    } else {
                        buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                IJDecision.CDT_SIGNECOTI, "+"));
                    }

                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    buffer.setLength(0);
                    buffer.append(new FWCurrency(cotisation_AVS_AI_APG_AC_externe).toStringFormat());

                    champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                    champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());

                    buffer.setLength(0);
                    buffer.append(new FWCurrency(cotisation_AVS_AI_APG_AC_interne).toStringFormat());

                    champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                } else {

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {

                        buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(14).getDescription(),
                                IJDecision.CDT_TAUXCOTI, String.valueOf(taux_coti_interne)));
                        buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                IJDecision.CDT_TYPECOTI, libelle_type_coti_interne));
                        if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                    IJDecision.CDT_SIGNECOTI, "./."));
                        } else {
                            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                    IJDecision.CDT_SIGNECOTI, "+"));
                        }

                        champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                        buffer.setLength(0);

                        buffer.append(new FWCurrency(cotisation_AVS_AI_APG_AC_interne).toStringFormat());
                    } else {

                        buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(14).getDescription(),
                                IJDecision.CDT_TAUXCOTI, String.valueOf(taux_coti_externe)));
                        buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                IJDecision.CDT_TYPECOTI, libelle_type_coti_externe));
                        if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                    IJDecision.CDT_SIGNECOTI, "./."));
                        } else {
                            buffer = new StringBuffer(PRStringUtils.replaceString(buffer.toString(),
                                    IJDecision.CDT_SIGNECOTI, "+"));
                        }

                        champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                        buffer.setLength(0);

                        buffer.append(new FWCurrency(cotisation_AVS_AI_APG_AC_externe).toStringFormat());
                    }

                    champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                    champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                }

                lignesAajouter.add(champs);

                // Impôts à la source
                String impotSource = "0";
                if (prononce.getSoumisImpotSource().booleanValue()

                        &&

                        (!JadeStringUtil.isBlankOrZero(documentProperties.getParameter("cantonTauxImposition")) || !JadeStringUtil
                                .isBlankOrZero(documentProperties.getParameter("tauxImposition")))

                        &&

                        (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE) || prononce
                                .getCsTypeHebergement().equals(IIJPrononce.CS_EXTERNE))

                ) {

                    champs = new HashMap();
                    champs.putAll(headerChamps);

                    String[] res = ijDecisionCotisationBuilder.soustraireImpots(getSession(), getTransaction(),
                            ijIndemniteJournaliereExterne.getMontantJournalierIndemnite(),
                            documentProperties.getParameter("cantonTauxImposition"),
                            documentProperties.getParameter("tauxImposition"));

                    impotSource = res[0];

                    String cdtImpotSource = document.getTextes(2).getTexte(21).getDescription();
                    cdtImpotSource = PRStringUtils.replaceString(cdtImpotSource, IJDecision.CDT_TAUXIMP,
                            new FWCurrency(res[1]).toStringFormat());

                    buffer.setLength(0);
                    buffer.append(cdtImpotSource);
                    champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                        buffer.setLength(0);
                        buffer.append(impotSource);

                        champs.put("PARAM_MONT_JOUR", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_JOUR", bufferDevise.toString());

                        champs.put("PARAM_MONT_RED", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                    } else {

                        buffer.setLength(0);

                        if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {

                            buffer.append(new FWCurrency(impotSource).toStringFormat());
                        } else {
                            buffer.append(new FWCurrency(impotSource).toStringFormat());
                        }

                        champs.put("PARAM_MONT_JOUR_2", afficheMntJour(buffer, false));
                        champs.put("PARAM_DEVISE_RED", bufferDevise.toString());

                    }

                    lignesAajouter.add(champs);
                }

                // Montant net de l'indemnité journalière
                champs = new HashMap();
                champs.putAll(headerChamps);

                double montantNetIJext;
                double montantNetIJint;

                if (documentProperties.getParameter("beneficiaire").equals("assure")) {
                    montantNetIJext = new BigDecimal(ijIndemniteJournaliereExterne.getMontantJournalierIndemnite())
                            .doubleValue() - new FWCurrency(cotisation_AVS_AI_APG_AC_externe).doubleValue();
                    montantNetIJint = new BigDecimal(ijIndemniteJournaliereInterne.getMontantJournalierIndemnite())
                            .doubleValue() - new FWCurrency(cotisation_AVS_AI_APG_AC_interne).doubleValue();
                } else {
                    montantNetIJext = new BigDecimal(ijIndemniteJournaliereExterne.getMontantJournalierIndemnite())
                            .doubleValue() + new FWCurrency(cotisation_AVS_AI_APG_AC_externe).doubleValue();
                    montantNetIJint = new BigDecimal(ijIndemniteJournaliereInterne.getMontantJournalierIndemnite())
                            .doubleValue() + new FWCurrency(cotisation_AVS_AI_APG_AC_interne).doubleValue();
                }

                montantNetIJext = montantNetIJext - new Double(impotSource).doubleValue();
                montantNetIJint = montantNetIJint - new Double(impotSource).doubleValue();

                buffer.setLength(0);
                buffer.append("\n");
                buffer.append(document.getTextes(2).getTexte(18).getDescription());
                champs.put("PARAM_MONT_LIBELLE", buffer.toString());

                if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE_EXTERNE)) {

                    buffer.setLength(0);
                    buffer.append(new FWCurrency(montantNetIJext).toStringFormat());
                    champs.put("PARAM_MONT_JOUR", "\n" + afficheMntJour(buffer, true));
                    champs.put("PARAM_DEVISE_JOUR", "\n" + bufferDeviseBold.toString());

                    buffer.setLength(0);
                    buffer.append(new FWCurrency(montantNetIJint).toStringFormat());
                    champs.put("PARAM_MONT_RED", "\n" + afficheMntJour(buffer, true));
                    champs.put("PARAM_DEVISE_RED", "\n" + bufferDeviseBold.toString());

                } else {

                    buffer.setLength(0);

                    if (prononce.getCsTypeHebergement().equals(IIJPrononce.CS_INTERNE)) {
                        buffer.append(new FWCurrency(montantNetIJint).toStringFormat());
                    } else {
                        buffer.append(new FWCurrency(montantNetIJext).toStringFormat());
                    }

                    champs.put("PARAM_MONT_JOUR_2", "\n" + afficheMntJour(buffer, true));
                    champs.put("PARAM_DEVISE_RED", "\n" + bufferDeviseBold.toString());

                }

                // Après le 3ème mois ENTIER qui suit le début du droit à l'IJ,
                // la rente AI est supprimée.L'IJ est donc
                // versée en totalité d'où l'ajout d'un texte supplémentaire
                // dans la base de calcul
                if (hasRenteAIDurantReadaptation
                        && !hasBCRenteAISupprimée3emeMois
                        && (JadeStringUtil.isBlankOrZero(ijIndemniteJournaliereInterne.getDeductionRenteAI()) || JadeStringUtil
                                .isBlankOrZero(ijIndemniteJournaliereExterne.getDeductionRenteAI()))) {

                    hasBCRenteAISupprimée3emeMois = true;
                    bufferBaseCalcul.append(document.getTextes(3).getTexte(20).getDescription());
                }

                lignesAajouter.add(champs);

                // Pour chaque ligne qui se répète, on ajoute le texte base de
                // calcul
                for (int j = 0; j < lignesAajouter.size(); j++) {
                    HashMap champsTmp = (HashMap) lignesAajouter.get(j);
                    champsTmp.put("PARAM_BASECALCUL", bufferBaseCalcul.toString());
                }

                // Finalement on ajoute les lignes dans le rapport
                lignes.addAll(lignesAajouter);
                BSessionUtil.stopUsingContext(this);
            }// Fin du for ijcalculees

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
        } finally {
            BSessionUtil.stopUsingContext(this);
        }

        // On envoie toutes les lignes pour la création du détail
        this.setDataSource(lignes);

    }

    /**
     * Set les propriétés du JadePublishDocumentInfo pour archivage du document dans la GED
     */
    public void createDocInfoIjDecision() throws Exception {

        JadePublishDocumentInfo docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        String isSendToGed = documentProperties.getParameter("isSendToGed");
        String anneeDecision = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
        // String displaySendToGed =
        // documentProperties.getParameter("displaySendToGed");

        // on ajoute au doc info le numéro de référence inforom
        docInfo.setDocumentType(IPRConstantesExternes.DECISION_IJAI);
        docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECISION_IJAI);
        docInfo.setDocumentTitle(getSession().getLabel("DOC_DECISION_IJ_TITLE"));
        docInfo.setDocumentProperty("annee", anneeDecision);

        // Correction pour la mise en GED FPV (ou autre qui nécessitent la date), la date ne peut pas être vide !!!!
        String date = null;
        if (documentProperties != null) {
            date = documentProperties.getParameter("dateSurDocument");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            docInfo.setDocumentDate(JACalendar.todayJJsMMsAAAA());
        } else {
            docInfo.setDocumentDate(date);
        }

        try {

            PRDemande demande = new PRDemande();
            demande.setSession(getSession());
            demande.setIdDemande(prononce.getIdDemande());
            demande.retrieve();

            docInfo.setDocumentProperty("allocataire.tiers.id", demande.getIdTiers());
            docInfo.setDocumentProperty("numero.decision", numeroDeDecisionAI);

            TIDocumentInfoHelper.fill(docInfo, demande.getIdTiers(), getSession(), IntRole.ROLE_IJAI, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog()
                    .logMessage("IJDecision.afterPrintDocument():" + e.toString(), FWMessage.ERREUR, "IJDecision");
        }

        if (!isCopieEnTete() && "TRUE".equals(isSendToGed)) {
            docInfo.setArchiveDocument(true);
        } else {
            docInfo.setArchiveDocument(false);
        }

        super.afterExecuteReport();
    }

    public ICaisseReportHelper getCaisseHelper() {
        return caisseHelper;
    }

    public HashMap getChamps() {
        return champs;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getCsDestinataire() {
        return csDestinataire;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsType() {
        return csType;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public ICTDocument getDocument() {
        return document;
    }

    public ICTDocument getDocumentHelper() {
        return documentHelper;
    }

    public ICTScalableDocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public String getGeneratorImplClassName() {
        return generatorImplClassName;
    }

    public String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersCopieCourante() {
        return idTiersCopieCourante;
    }

    public ICTScalableDocumentCopie getIntervenantCopie() {
        return intervenantCopie;
    }

    public ICTScalableDocumentCopie getIntervenantCopieEnTete() {
        return intervenantCopieEnTete;
    }

    public Iterator getIteratorDestinataireCopie() {
        return iteratorDestinataireCopie;
    }

    public Iterator getIteratorDestinataireCopieEnTete() {
        return iteratorDestinataireCopieEnTete;
    }

    public String getLibelleGenreReadaptation(String csGenreReadaptation) throws Exception {

        JadeCodeSystemeService cs = JadeBusinessServiceLocator.getCodeSystemeService();

        JadeCodeSysteme codeSysteme = cs.getCodeSysteme(csGenreReadaptation);

        String myCs = codeSysteme.getTraduction(Langues.getLangueDepuisCodeIso(TITiers.toLangueIso(tiers.getLangue())));

        return myCs;
    }

    public LinkedList getLignes() {
        return lignes;
    }

    public int getNbDocument() {
        return nbDocument;
    }

    public int getNbDocumentCopie() {
        return nbDocumentCopie;
    }

    public String getNumeroDeDecisionAI() {
        return numeroDeDecisionAI;
    }

    public IJPrononce getPrononce() {
        return prononce;
    }

    public String getRevenuJournalierMoyen() {
        return revenuJournalierMoyen;
    }

    @Override
    public ICTScalableDocumentProperties getScalableDocumentProperties() {
        return scalableDocumentProperties;
    }

    public String getSpecificHeader() {
        return specificHeader;
    }

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public PRTiersWrapper getTiersCopie() {
        return tiersCopie;
    }

    public PRTiersWrapper getTiersCopieEnTete() {
        return tiersCopieEnTete;
    }

    public PRTiersWrapper getTiersCopieImpression() {
        return tiersCopieImpression;
    }

    public boolean isCopieEnTete() {
        return isCopieEnTete;
    }

    public boolean isDocumentPrincipal() {
        return isDocumentPrincipal;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    private boolean isStyledText(String text) {

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(".*<style.*", Pattern.DOTALL);
        matcher = pattern.matcher(text);

        return matcher.matches();

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {

        if (nbTrue == 0) {
            nbTrue++;
            return true;
        } else {
            return false;
        }
    }

    public void setCaisseHelper(ICaisseReportHelper caisseHelper) {
        this.caisseHelper = (IJDecisionACaisseReportHelper) caisseHelper;
    }

    public void setChamps(HashMap champs) {
        this.champs = champs;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setCopieEnTete(boolean isCopieEnTete) {
        this.isCopieEnTete = isCopieEnTete;
    }

    public void setCsDestinataire(String csDestinataire) {
        this.csDestinataire = csDestinataire;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDocument(ICTDocument document) {
        this.document = document;
    }

    public void setDocumentHelper(ICTDocument documentHelper) {
        this.documentHelper = documentHelper;
    }

    public void setDocumentPrincipal(boolean isDocumentPrincipal) {
        this.isDocumentPrincipal = isDocumentPrincipal;
    }

    public void setDocumentProperties(ICTScalableDocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    public void setGeneratorImplClassName(String generatorImplClassName) {
        this.generatorImplClassName = generatorImplClassName;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdTiersCopieCourante(String idTiersCopieCourante) {
        this.idTiersCopieCourante = idTiersCopieCourante;
    }

    public void setIntervenantCopie(ICTScalableDocumentCopie intervenantCopie) {
        this.intervenantCopie = intervenantCopie;
    }

    public void setIntervenantCopieEnTete(ICTScalableDocumentCopie intervenantCopieEnTete) {
        this.intervenantCopieEnTete = intervenantCopieEnTete;
    }

    public void setIteratorDestinataireCopie(Iterator iteratorDestinataireCopie) {
        this.iteratorDestinataireCopie = iteratorDestinataireCopie;
    }

    public void setIteratorDestinataireCopieEnTete(Iterator iteratorDestinataireCopieEnTete) {
        this.iteratorDestinataireCopieEnTete = iteratorDestinataireCopieEnTete;
    }

    public void setLignes(LinkedList lignes) {
        this.lignes = lignes;
    }

    public void setNbDocument(int nbDocument) {
        this.nbDocument = nbDocument;
    }

    public void setNbDocumentCopie(int nbDocumentCopie) {
        this.nbDocumentCopie = nbDocumentCopie;
    }

    public void setNumeroDeDecisionAI(String numeroDeDecisionAI) {
        this.numeroDeDecisionAI = numeroDeDecisionAI;
    }

    public void setPrononce(IJPrononce prononce) {
        this.prononce = prononce;
    }

    public void setRevenuJournalierMoyen(String revenuJournalierMoyen) {
        this.revenuJournalierMoyen = revenuJournalierMoyen;
    }

    @Override
    public void setScalableDocumentProperties(ICTScalableDocumentProperties scalableDocumentProperties) {
        this.scalableDocumentProperties = scalableDocumentProperties;
    }

    public void setSpecificHeader(String specificHeader) {
        this.specificHeader = specificHeader;
    }

    public void setTiers(PRTiersWrapper tiers) {
        this.tiers = tiers;
    }

    public void setTiersCopie(PRTiersWrapper tiersCopie) {
        this.tiersCopie = tiersCopie;
    }

    public void setTiersCopieEnTete(PRTiersWrapper tiersCopieEnTete) {
        this.tiersCopieEnTete = tiersCopieEnTete;
    }

    public void setTiersCopieImpression(PRTiersWrapper tiersCopieImpression) {
        this.tiersCopieImpression = tiersCopieImpression;
    }
}