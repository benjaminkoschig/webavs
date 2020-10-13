package ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions;

import globaz.babel.api.ICTDocument;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;

import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.constantes.CommonConstLangue;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextGiverBabel;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.sql.CodeSystemQueryExecutor;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.osiris.businessimpl.service.SectionServiceImpl;
import ch.globaz.osiris.exception.OsirisException;
import ch.globaz.pegasus.business.constantes.EPCCodeAmal;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.decision.EtatDecision;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculOO;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.CreancierVO;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.business.vo.decompte.DetteEnComptaVO;
import ch.globaz.pegasus.business.vo.decompte.PCAccordeeDecompteVO;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.demande.SingleBillagBuilder;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.IPageDeGardeDefinition;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeDefinitionForDAC;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import org.apache.commons.lang.StringUtils;

public class SingleDACBuilder extends AbstractDecisionBuilder {

    private static final String AYANT_DROIT = "{ayant_droit}";
    private static final String CR = "\n";
    private static final String CR_FROM_BABEL = "{br}";
    private static final String CRLF = "\r\n";
    private static final String DATE_DEBUT = "{date_debut}";

    private static final String DATE_DECISION_AMAL_REPLACE = "{date_decision_amal}";
    private static final String DATE_FIN = "{date_fin}";
    private static final String DECISION_DU = "{date_decision}";

    private static final String MONNAIE = "CHF";
    private static final String NO_DECISION = "{no_decision}";
    private static final String NSS = "{nss}";
    private static final String SEPARATOR_USER_REF = "/";
    private ICTDocument babelDoc = null;
    private ICTDocument babelPageDeGardeDoc = null;
    private DecisionApresCalculOO dacOO = null;

    private AdresseTiersDetail detailPaiement = null;

    private JadeUser gestionnaire = null;
    private JadeUser preprateurDecision = null;
    PlanDeCalculWitMembreFamilleSearch plaCalMembreFamSearch = null;
    private JadePublishDocumentInfo pubInfosPixisProperties = new JadePublishDocumentInfo();
    private JadePublishDocumentInfo pubInfosPixisPropertiesCopie = new JadePublishDocumentInfo();

    private TITiers loadTiers(String idTiers) throws Exception {

        if (idTiers == null) {
            throw new CommonTechnicalException("the idTiers can't be null");
        }

        TITiers tiersToReturn = new TITiers();
        tiersToReturn.setId(idTiers);
        tiersToReturn.setSession(getSession());
        tiersToReturn.retrieve();

        return tiersToReturn;
    }

    public void build(DecisionApresCalculOO dacOO, TupleDonneeRapport tuppleRoot,
                      Map<Langues, CTDocumentImpl> documentsBabel, JadePrintDocumentContainer allDoc, DACPublishHandler handler,
                      String dateDoc, String persRef, Boolean allowDecompte) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        Checkers.checkNotNull(dacOO, "dacOO");
        this.dacOO = dacOO;
        // chargement du tiers
        TITiers tiersBeneficiaire = loadTiers(dacOO.getDecisionHeader().getSimpleDecisionHeader()
                .getIdTiersBeneficiaire());
        // récupération du catalogue de texte en fonction de la langue du tiers
        babelDoc = documentsBabel.get(LanguageResolver.resolveISOCode(tiersBeneficiaire.getLangue()));
        // chargement des infos pour les propriétés pixis
        loadPixisInfoForPubInfo(tiersBeneficiaire.getIdTiers());

        JadePrintDocumentContainer containerGed = new JadePrintDocumentContainer();

        if (handler.getPersref() == null) {
            handler.setPersref(getSession().getUserId());
        }
        gestionnaire = getSession().getApplication()._getSecurityManager()
                .getUserForVisa(getSession(), handler.getPersref());
        preprateurDecision = getSession()
                .getApplication()
                ._getSecurityManager()
                .getUserForVisa(getSession(),
                        this.dacOO.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar());

        setAdressePaiement(tiersBeneficiaire);

        /************ Document principal, lettre ************/
        DocumentData dataOriginal = new DocumentData();
        dataOriginal = buildMainDoc(dataOriginal, tiersBeneficiaire);
        if (isReformePC()) {
            dataOriginal.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_REFORME);
        } else {
            dataOriginal.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL);
        }


        mergeDataAndPubInfosWithPixisFill(allDoc, dataOriginal, new PegasusPubInfoBuilder().ged().rectoVersoLast()
                        .getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(),
                AbstractDecisionBuilder.TYPE_DOCUMENT.ORIGINAL, dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

        mergeDataAndPubInfosWithPixisFill(containerGed, dataOriginal, new PegasusPubInfoBuilder().ged()
                        .rectoVersoLast().getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(),
                TYPE_DOCUMENT.ORIGINAL, dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

        /******************** Plan de calcul **************/
        DocumentData dataPCAL = addPlanCalcul(dacOO, allDoc, containerGed, true);

        /******************** Plan de calcul non retenu **************/
        DocumentData dataPCALnonRetenu = null;
        if (StringUtils.isNotEmpty(dacOO.getPlanCalculNonRetenu().getId())) {
            dataPCALnonRetenu = addPlanCalcul(dacOO, allDoc, containerGed, false);
        }

        /********************** Decomptes ****************/
        if (allowDecompte) {
            DocumentData dataDecompte = new DocumentData();
            dataDecompte = buildRecapitulatifsDoc(dataDecompte);
            dataDecompte.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_DECOMPTE);
            mergeDataAndPubInfosWithPixisFill(allDoc, dataDecompte, new PegasusPubInfoBuilder().ged().rectoVersoLast()
                            .getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(), TYPE_DOCUMENT.ORIGINAL,
                    dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

            mergeDataAndPubInfosWithPixisFill(containerGed, dataDecompte, new PegasusPubInfoBuilder().ged()
                            .rectoVersoLast().getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(),
                    TYPE_DOCUMENT.ORIGINAL, dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

        }
        /********************** Billag ****************/
        // uniquement dans le cas de la validation et
        if (isAnnexeBillag() && dacOO.isDecisionValidee() && isDecisionPasEnrefus()) {
            DocumentData dataBillag = new DocumentData();
            dataBillag = new SingleBillagBuilder().buildBillagDoc(this.dacOO.getDecisionHeader()
                            .getSimpleDecisionHeader().getDateDecision(), babelDoc, this.dacOO.getDecisionHeader()
                            .getSimpleDecisionHeader().getDateDebutDecision(), this.dacOO.getDecisionHeader()
                            .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(), this.dacOO.getVersionDroit()
                            .getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(),
                    preprateurDecision.getVisa());
            String prop = getSession().getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
            if ("BILLAG".equalsIgnoreCase(prop)) {
                dataBillag.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_BILLAG);
            } else {
                dataBillag.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_REDEVANCE);
            }
            mergeDataAndPubInfosWithPixisFill(allDoc, dataBillag, new PegasusPubInfoBuilder().ged().rectoVersoLast()
                            .getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(), TYPE_DOCUMENT.ORIGINAL,
                    dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

            mergeDataAndPubInfosWithPixisFill(containerGed, dataBillag, new PegasusPubInfoBuilder().ged()
                            .rectoVersoLast().getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(),
                    TYPE_DOCUMENT.ORIGINAL, dacOO.getIdTiersCourrier(), dacOO.getNoDecision());
        }

        // Pas de copies pour la ge son ajoute le container dans le handler des ged
        handler.addGedContainer(containerGed);

        /*********************** Copies ************/
        ArrayList<CopiesDecision> listeCopies = this.dacOO.getDecisionHeader().getListeCopies();
        DocumentData dataCopie;
        for (CopiesDecision copie : listeCopies) {

            String idTiersCopie = copie.getSimpleCopiesDecision().getIdTiersCopie();
            pubInfosPixisPropertiesCopie = new JadePublishDocumentInfo();
            // page de garde
            if (copie.getSimpleCopiesDecision().getPageDeGarde()) {
                TITiers tiers = loadTiers(idTiersCopie);
                babelPageDeGardeDoc = documentsBabel.get(LanguageResolver.resolveISOCode(tiers.getLangue()));
                DocumentData dataPageGarde = null;
                TextGiver<BabelTextDefinition> textGiver = new TextGiverBabel(babelPageDeGardeDoc);
                IPageDeGardeDefinition pageDeGardeText = new PageDeGardeDefinitionForDAC(textGiver);
                String dateDecision = dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDecision();
                String NSSReference = dacOO.getDecisionHeader().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel();
                PageDeGardeBuilder pageDeGardeBuilder = new PageDeGardeBuilder(babelPageDeGardeDoc, pageDeGardeText,
                        tiers, gestionnaire, preprateurDecision, NSSReference, dateDecision);
                dataPageGarde = pageDeGardeBuilder.buildPageDeGarde();
                dataPageGarde.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                        IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_PG);
                mergeDataAndPubInfosWithPixisFill(allDoc, dataPageGarde, new PegasusPubInfoBuilder().rectoVersoLast()
                                .getPubInfo(), pubInfosPixisPropertiesCopie, dacOO.getPersonneForDossier(),
                        TYPE_DOCUMENT.COPIE, idTiersCopie, dacOO.getNoDecision());
            }

            // Letrre de base
            if (copie.getSimpleCopiesDecision().getLettreBase()) {
                dataCopie = new DocumentData();
                dataCopie = buildCopiesDoc(dataCopie, copie.getSimpleCopiesDecision(), tiersBeneficiaire);
                dataCopie.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                        IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_COPIE);
                mergeDataAndPubInfosWithPixisFill(allDoc, dataCopie, new PegasusPubInfoBuilder().rectoVersoLast()
                                .getPubInfo(), pubInfosPixisPropertiesCopie, dacOO.getPersonneForDossier(),
                        TYPE_DOCUMENT.COPIE, idTiersCopie, dacOO.getNoDecision());
            }

            // ***** Plan calcul copie
            if (copie.getSimpleCopiesDecision().getPlandeCalcul()) {
                DocumentData dataPCALCopie;
                DocumentData dataPCALnonRetenuCopie;

                dataPCALCopie = dataPCAL;
                mergeDataAndPubInfosWithPixisFill(allDoc, dataPCALCopie, new PegasusPubInfoBuilder().rectoVersoLast()
                                .getPubInfo(), pubInfosPixisPropertiesCopie, dacOO.getPersonneForDossier(),
                        TYPE_DOCUMENT.COPIE, idTiersCopie, dacOO.getNoDecision());

                if (StringUtils.isNotEmpty(dacOO.getPlanCalculNonRetenu().getId())) {
                    dataPCALnonRetenuCopie = dataPCALnonRetenu;
                    mergeDataAndPubInfosWithPixisFill(allDoc, dataPCALnonRetenuCopie, new PegasusPubInfoBuilder().rectoVersoLast()
                                    .getPubInfo(), pubInfosPixisPropertiesCopie, dacOO.getPersonneForDossier(),
                            TYPE_DOCUMENT.COPIE, idTiersCopie, dacOO.getNoDecision());
                }
            }

            // ****** Decompte copie
            if (allowDecompte && copie.getSimpleCopiesDecision().getRecapitulatif()) {
                DocumentData dataDecompteCopie = new DocumentData();
                dataDecompteCopie = buildRecapitulatifsDoc(dataDecompteCopie);
                dataDecompteCopie.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                        IPCCatalogueTextes.PROCESS_DECISION_DECOMPTE);
                mergeDataAndPubInfosWithPixisFill(allDoc, dataDecompteCopie, new PegasusPubInfoBuilder()
                                .rectoVersoLast().getPubInfo(), pubInfosPixisPropertiesCopie, dacOO.getPersonneForDossier(),
                        TYPE_DOCUMENT.COPIE, idTiersCopie, dacOO.getNoDecision());
            }
        }
    }

    private DocumentData addPlanCalcul(DecisionApresCalculOO dacOO, JadePrintDocumentContainer allDoc, JadePrintDocumentContainer containerGed, boolean isRetenu) throws Exception {
        DocumentData dataPCAL;
        dataPCAL = PegasusImplServiceLocator.getDecisionApresCalculService().buildPlanCalculDocumentData(
                dacOO.getSimpleDecisionApresCalcul().getIdDecisionApresCalcul(), true, isRetenu);

        mergeDataAndPubInfosWithPixisFill(allDoc, dataPCAL, new PegasusPubInfoBuilder().ged().rectoVersoLast()
                        .getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(), TYPE_DOCUMENT.ORIGINAL,
                dacOO.getIdTiersCourrier(), dacOO.getNoDecision());

        mergeDataAndPubInfosWithPixisFill(containerGed, dataPCAL, new PegasusPubInfoBuilder().ged().rectoVersoLast()
                        .getPubInfo(), pubInfosPixisProperties, dacOO.getPersonneForDossier(), TYPE_DOCUMENT.ORIGINAL,
                dacOO.getIdTiersCourrier(), dacOO.getNoDecision());
        return dataPCAL;
    }

    private boolean isDecisionPasEnrefus() throws DecisionException {
        return getEtatDecision().equals(EtatDecision.OCTROI) || getEtatDecision().equals(EtatDecision.PARTIEL);
    }

    private DocumentData buildBlocAllocationNoel(DocumentData data, DecompteTotalPcVO decompte) {

        if (decompte.getSimpleAllocationNoels() != null && decompte.getSimpleAllocationNoels().size() > 0) {
            data.addData("ALLOCNOEL_TITRE", babelDoc.getTextes(16).getTexte(30).getDescription());
            data.addData("ALLOCNOEL_TEXTE", babelDoc.getTextes(16).getTexte(31).getDescription());

            StringBuilder allocaNoelDecompte = new StringBuilder();
            allocaNoelDecompte.append(decompte.getNombrePersonnesAllocationDeNoel());
            allocaNoelDecompte.append(" ");

            // gestions pluriels
            if (decompte.getNombrePersonnesAllocationDeNoel() > 1) {
                allocaNoelDecompte.append(babelDoc.getTextes(16).getTexte(40).getDescription());
            } else {
                allocaNoelDecompte.append(babelDoc.getTextes(16).getTexte(39).getDescription());
            }

            allocaNoelDecompte.append(" ").append(SingleDACBuilder.MONNAIE).append(" ")
                    .append(decompte.getMontantAllocationNoelParPersonne().toString());

            data.addData("ALLOCNOEL_DECOMPTE", allocaNoelDecompte.toString());
            data.addData("ALLOC_NOEL_TOTAL",
                    new FWCurrency(decompte.getMontantTotalAllocationNoel().intValue()).toStringFormat());
            data.addData("CHF_ALLN", SingleDACBuilder.MONNAIE);
        }

        return data;
    }

    /**
     * Création du bloc annexes
     *
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     */
    private DocumentData buildBlocAnnexes(DocumentData data) {
        // Liste des annexes
        ArrayList<SimpleAnnexesDecision> listeAnnexes = dacOO.getDecisionHeader().getListeAnnexes();
        StringBuilder annexes = new StringBuilder("");

        if (listeAnnexes.size() > 0) {
            for (SimpleAnnexesDecision annexe : listeAnnexes) {
                annexes.append(annexe.getValeur());
                annexes.append(SingleDACBuilder.CR);
            }
            data.addData("ANNEXE", babelDoc.getTextes(12).getTexte(10).getDescription());
            data.addData("ANNEXES", annexes.toString());

        }
        return data;
    }

    /**
     * Création du bloc copies
     *
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private DocumentData buildBlocCopies(DocumentData data) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Liste des copies
        ArrayList<CopiesDecision> listeCopies = dacOO.getDecisionHeader().getListeCopies();
        StringBuilder copies = new StringBuilder("");
        if (listeCopies.size() > 0) {
            for (CopiesDecision copie : listeCopies) {

                copies.append(copie.getDesignation1());
                copies.append(" ").append(copie.getDesignation2());
                copies.append(", ").append(getAdresseForCopies(copie.getSimpleCopiesDecision().getIdTiersCopie()));
                copies.append(SingleDACBuilder.CR);
            }
            data.addData("COPIE", babelDoc.getTextes(12).getTexte(20).getDescription());
            data.addData("COPIES", copies.toString());
        }

        return data;
    }

    private DocumentData buildBlocDecomptePC(DocumentData data, DecompteTotalPcVO decompte)
            throws JadePersistenceException, DecisionException, JadeApplicationException {

        // Titre recap
        // data.addData("DECOMPTE_TITRE", this.babelDoc.getTextes(16).getTexte(20).getDescription());

        /************************* Decompte de PCA **************************/
        buildRecapPcaForDecompte(data, decompte);

        buildPrestationsVersesForDecompte(data, decompte);

        buildCreanciersForDecompte(data, decompte);

        buildDetteEnComptaForDecompte(data, decompte);

        buildTotalSoldeForDecompte(data, decompte);
        return data;
    }

    /**
     * Construction du header
     *
     * @param data
     * @return data, l'objet alimenté avec les infos du header
     * @throws Exception
     */
    private DocumentData buildBlocHeader(DocumentData data, Boolean isCopie, TITiers tiersBeneficiaire)
            throws Exception {

        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = "Tél: " + gestionnaire.getPhone();

        // Infos collaborateur
        data.addData("PERSONNE_REF", babelDoc.getTextes(1).getTexte(2).getDescription());
        data.addData("NOM_COLLABO", nomCollabo);
        data.addData("TEL_COLLABO", tel);

        // Modification pour template Lausanne
        data.addData("TEL_GESTIONNAIRE", gestionnaire.getPhone());
        data.addData("GESTIONNAIRE", preprateurDecision.getFirstname() + " " + preprateurDecision.getLastname());
        data.addData("ID_USER", preprateurDecision.getIdUser());
        data.addData("NSS_BENEFICIAIRE", dacOO.getDecisionHeader().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel());

        // Références
        if (Boolean.parseBoolean(getSession().getApplication().getProperty("pegasus.pegasus.decision.ref_user"))) {
            data.addData("NREF", SingleDACBuilder.SEPARATOR_USER_REF + gestionnaire.getVisa());
        }
        String dateDecision = PegasusDateUtil.getLitteralDateByTiersLanguage(dacOO.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision(), tiersBeneficiaire.getLangue());
        data.addData("DATE_ET_LIEU", babelDoc.getTextes(1).getTexte(1).getDescription() + " " + dateDecision);

        // Infos générales header copie et autres
        if (isCopie) {
            data.addData("IS_COPIE", "COPIE");
        }

        data.addData("ADRESSE", PRTiersHelper.getAdresseCourrierFormatee(getSession(), tiersBeneficiaire.getIdTiers(),
                "", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE));

        return data;
    }

    /**
     * Génération du paragraphie informations
     *
     * @throws DecisionException
     */
    private DocumentData buildBlocInformations(DocumentData data) throws Exception {
        data.addData("INFORMATIONS", babelDoc.getTextes(9).getTexte(10).getDescription());
        data.addData("B_INFORMATIONS_1", babelDoc.getTextes(9).getTexte(20).getDescription());
        data.addData("B_INFORMATIONS_2", babelDoc.getTextes(9).getTexte(30).getDescription());

        if (isDecisionPasEnrefus()) {
            String texteBillag;
            texteBillag = isAnnexeBillag() ? babelDoc.getTextes(9).getTexte(40).getDescription() : babelDoc
                    .getTextes(9).getTexte(41).getDescription();
            data.addData("B_INFORMATIONS_3", texteBillag);
        }

        return data;
    }

    /**
     * Creation du bloc MOyens de droits
     *
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     */
    private DocumentData buildBlocMoyensDroits(DocumentData data) {
        data.addData("MOYENS_DROITS", babelDoc.getTextes(10).getTexte(10).getDescription());
        data.addData("B_MOYENS_DROITS", babelDoc.getTextes(10).getTexte(20).getDescription());

        return data;
    }

    /**
     * Génération du paragraphie obligation de renseigner
     *
     * @throws DecisionException
     */
    private DocumentData buildBlocObligationDeRenseigner(DocumentData data) throws DecisionException {
        if (getEtatDecision().equals(EtatDecision.OCTROI) || getEtatDecision().equals(EtatDecision.PARTIEL)) {
            data.addData("OBLIGATION_RENSEIGNER", babelDoc.getTextes(8).getTexte(10).getDescription());
            data.addData("B_OBLIGATION_RENSEIGNER", babelDoc.getTextes(8).getTexte(20).getDescription());
        }
        return data;
    }

    /**
     * Création du bloc remarques
     *
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     */
    private DocumentData buildBlocRemarques(DocumentData data) {
        // Si il y a une remrque, ou que la case à cocher réduction est coché...
        if (!"".equals(dacOO.getSimpleDecisionApresCalcul().getRemarqueGenerale())
                || dacOO.getSimpleDecisionApresCalcul().getDiminutionPc()
                || dacOO.getSimpleDecisionApresCalcul().getAllocNonActif()) {
            // titres
            data.addData("REMARQUES", babelDoc.getTextes(7).getTexte(10).getDescription());

            StringBuilder text = new StringBuilder(dacOO.getSimpleDecisionApresCalcul().getRemarqueGenerale());
            // ajout texte si diminution pc
            if (dacOO.getSimpleDecisionApresCalcul().getDiminutionPc()) {
                if (text.length() != 0) {
                    text.append(SingleDACBuilder.CRLF);
                }
                text.append(babelDoc.getTextes(7).getTexte(11).getDescription());

            }
            // ajout texte si diminution pc
            if (dacOO.getSimpleDecisionApresCalcul().getAllocNonActif()) {
                if (text.length() != 0) {
                    text.append(SingleDACBuilder.CRLF);
                }
                text.append(babelDoc.getTextes(7).getTexte(12).getDescription());

            }
            // ajout texte
            data.addData("TXT_REMARQUES", text.toString());
        }
        return data;
    }

    /**
     * Génération du salutations
     */
    private DocumentData buildBlocSalutations(DocumentData data, TITiers tiersBeneficiaire) {
        // les salutations doivent être gérées de manières différente qu'on soit en allemand ou en français
        if (tiersBeneficiaire.getLangueIso().equalsIgnoreCase(CommonConstLangue.LANGUE_ISO_ALLEMAND)) {
            data.addData("SALUTATIONS", babelDoc.getTextes(11).getTexte(10).getDescription());
        } else {
            data.addData("SALUTATIONS", babelDoc.getTextes(11).getTexte(10).getDescription() + " "
                    + resolveFormulePolitesse(tiersBeneficiaire) + " "
                    + babelDoc.getTextes(11).getTexte(11).getDescription());
        }
        return data;
    }

    /**
     * Génération du salutations
     */
    private DocumentData buildBlocSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        // Ajout d'un cr si description caisse sur deux lignes
        data.addData("SIGNATURE_NOM_CAISSE", PRStringUtils.replaceString(babelDoc.getTextes(19).getTexte(1)
                .getDescription(), SingleDACBuilder.CR_FROM_BABEL, SingleDACBuilder.CR));

        data.addData("SIGNATAIRE", babelDoc.getTextes(19).getTexte(3).getDescription());
        data.addData("SIGNATURE_NOM_SERVICE", babelDoc.getTextes(19).getTexte(4).getDescription());
        return data;
    }

    /**
     * Génération du paragraphie versement à
     *
     * @throws DecisionException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private DocumentData buildBlocVersementA(DocumentData data) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Collection table = new Collection("tabVersement");
        if (getEtatDecision().equals(EtatDecision.OCTROI)) {
            // recherche et set adresse
            // this.setAdressePaiement(this.dacOO.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire());
            data.addData("VERSEMENT_PC", babelDoc.getTextes(6).getTexte(10).getDescription());
            data.addData("B_VERSEMENT_PC", babelDoc.getTextes(6).getTexte(20).getDescription());

            data.addData("B_VERSEMENT_PC_REMARQUES", babelDoc.getTextes(6).getTexte(22).getDescription());

            DataList line = new DataList("ligne");
            line.addData("B_VERSEMENT_PC_A", babelDoc.getTextes(6).getTexte(21).getDescription());
            line.addData("B_VERSEMENT_PC_ADRESSE", getBlocVersementAdresse());
            line.addData("B_VERSEMENT_PC_IBAN", getBlocVersementPaiement());
            table.add(line);

        }
        data.add(table);
        return data;
    }

    /**
     * Création des copies de documents en fonction des paramètres de copies
     *
     * @param data
     * @param document
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PersonneDansPlanCalculException
     * @throws RemoteException
     */
    private DocumentData buildCopiesDoc(DocumentData data, SimpleCopiesDecision copie, TITiers tiersBeneficiaire)
            throws Exception {

        // Page de garde
        // header
        buildBlocHeader(data, true, tiersBeneficiaire);
        // contenu
        buildMainLetter(data, tiersBeneficiaire);
        // Versement pc uniquement pour octroi, p6
        if (copie.getVersementA()) {
            buildBlocVersementA(data);
        } else {
            Collection table = new Collection("tabVersement");
            DataList line = new DataList("ligne");
            table.add(line);
            data.add(table);

        }

        // Remarques, p7
        if (copie.getRemarque()) {
            buildBlocRemarques(data);
        }

        // OBligation de renseigner pour partiel et octroi, p8
        buildBlocObligationDeRenseigner(data);

        // Informations, p9
        buildBlocInformations(data);

        // Moyen de droits
        if (copie.getMoyensDeDroit()
                && !isProforma()
                && !isProvisoire()) {
            buildBlocMoyensDroits(data);
        }

        // Salutations
        buildBlocSalutations(data, tiersBeneficiaire);

        // Signatures
        if (copie.getSignature()) {
            buildBlocSignatures(data);
        } else {
            data.addData("signature", "NONE");
        }

        // Annexes et copies
        if (copie.getAnnexes() && !isProforma()) {
            buildBlocAnnexes(data);
        }
        if (copie.getCopies()) {
            buildBlocCopies(data);
        }

        return data;
    }

    private void buildFooter(DocumentData data) {
        data.addData("DECISION_COMPORTE", babelDoc.getTextes(13).getTexte(10).getDescription() + " ");
        data.addData("DECISION_PAGES", " " + babelDoc.getTextes(13).getTexte(11).getDescription());
    }

    private void buildCreanciersForDecompte(DocumentData data, DecompteTotalPcVO decompte) {
        // Si on a des créanciers on traite
        if (decompte.getCreanciers().getList().size() > 0) {
            // Recap pca ajout du sous template
            data.addData("hasBlocCreanciers", "TRUE");
            // Titre
            data.addData("TITRE_BLOC_CREANCIERS", babelDoc.getTextes(16).getTexte(15).getDescription());

            Collection table = new Collection("tabCreanciers");

            // Iteration sur les cranciers
            for (CreancierVO creancierVO : decompte.getCreanciers().getList()) {
                DataList ligne = new DataList("ligneStandard");
                ligne.addData("LIBELLE", creancierVO.getDescription());
                ligne.addData("CHF", "CHF");
                ligne.addData("TOTAL", new FWCurrency(creancierVO.getMontantVerse().toString()).toStringFormat());
                table.add(ligne);
            }
            // Total creances
            DataList ligneTotal = new DataList("ligneTotal");
            ligneTotal.addData("LIBELLE", babelDoc.getTextes(16).getTexte(21).getDescription());
            ligneTotal.addData("CHF", "CHF");
            ligneTotal.addData("TOTAL",
                    "- " + new FWCurrency(decompte.getCreanciers().getTotal().toString()).toStringFormat());
            table.add(ligneTotal);
            data.add(table);
        } else {
            // Recap pca ajout du sous template
            data.addData("hasBlocCreanciers", "FALSE");
        }
    }

    private void buildDetteEnComptaForDecompte(DocumentData data, DecompteTotalPcVO decompte) throws OsirisException {
        // Si dettes, on traite
        if (decompte.getDettesCompta().getList().size() > 0) {
            data.addData("hasBlocDettes", "TRUE");
            // Titre
            data.addData("TITRE_BLOC_DETTES", babelDoc.getTextes(16).getTexte(14).getDescription());

            Collection table = new Collection("tabDettes");

            String langueTiers = dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getLangue();
            String isoLangueTiers = PRUtil.getISOLangueTiers(langueTiers);

            // Iteration sur les Dettes
            for (DetteEnComptaVO detteVO : decompte.getDettesCompta().getList()) {
                DataList ligne = new DataList("ligneStandard");
                ligne.addData("LIBELLE", new SectionServiceImpl().findDescription(detteVO.getDette()
                        .getIdSectionDetteEnCompta(), isoLangueTiers));
                ligne.addData("CHF", "CHF");
                ligne.addData("TOTAL", new FWCurrency(detteVO.getDette().getMontant()).toStringFormat());
                table.add(ligne);

            }
            // Total dettes
            DataList ligneTotal = new DataList("ligneTotal");
            ligneTotal.addData("LIBELLE", babelDoc.getTextes(16).getTexte(21).getDescription());
            ligneTotal.addData("CHF", "CHF");
            ligneTotal.addData("TOTAL",
                    "- " + new FWCurrency(decompte.getDettesCompta().getTotal().toString()).toStringFormat());
            table.add(ligneTotal);
            data.add(table);
        } else {
            data.addData("hasBlocDettes", "FALSE");
        }
    }

    /**
     * Remplissage du document original
     *
     * @param data
     * @return DoumentData
     * @throws Exception
     */
    private DocumentData buildMainDoc(DocumentData data, TITiers tiersBeneficiaire) throws Exception {
        // header
        buildBlocHeader(data, false, tiersBeneficiaire);
        // Contennu général, commun au copies egalement
        buildMainLetter(data, tiersBeneficiaire);
        // Versement pc uniquement pour octroi, p6
        buildBlocVersementA(data);
        // Remarques, p7
        buildBlocRemarques(data);
        // OBligation de renseigner pour partiel et octroi, p8
        buildBlocObligationDeRenseigner(data);
        // Informations, p9
        buildBlocInformations(data);
        // Moyen de droits
        if (!isProforma() && !isProvisoire()) {
            buildBlocMoyensDroits(data);
        }
        // Salutations
        buildBlocSalutations(data, tiersBeneficiaire);
        // Signatures
        buildBlocSignatures(data);
        // Annexes et copies
        if (!isProforma()) {
            buildBlocAnnexes(data);
        }
        buildBlocCopies(data);

        buildFooter(data);

        return data;
    }

    /**
     * Construction du contenu
     *
     * @param data , l'objet data
     * @return data, l'objet alimenté avec les infos du contenu
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PersonneDansPlanCalculException
     * @throws DecisionException
     */
    private DocumentData buildMainLetter(DocumentData data, TITiers tiersBeneficiaire)
            throws PersonneDansPlanCalculException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {

        // header page
        String decisionDu;
        if (isProforma()) {
            decisionDu = babelDoc.getTextes(1).getTexte(21).getDescription();
        } else {
            decisionDu = babelDoc.getTextes(1).getTexte(20).getDescription();
        }
        data.addData(
                "HEADER_DECISION",
                PRStringUtils.replaceString(decisionDu, SingleDACBuilder.NO_DECISION, dacOO.getDecisionHeader()
                        .getSimpleDecisionHeader().getNoDecision()));
        // No decision
        data.addData("NO_DECISION", PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(20).getDescription(),
                SingleDACBuilder.NO_DECISION, dacOO.getDecisionHeader().getSimpleDecisionHeader().getNoDecision()));
        // NSS
        data.addData("NSS", PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(30).getDescription(),
                SingleDACBuilder.NSS, dacOO.getDecisionHeader().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel()));
        // PAGE
        data.addData("PAGE_NUMERO",
                LanguageResolver.resolveLibelleFromLabel(tiersBeneficiaire.getLangueIso(), "PAGE", getSession()));

        // Ayant droit
        data.addData("AYANT_DROIT", PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(40).getDescription(),
                SingleDACBuilder.AYANT_DROIT, dacOO.getDecisionHeader().getPersonneEtendue().getTiers()
                        .getDesignation2()
                        + " " + dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation1()));

        String dateDecision = PegasusDateUtil.getLitteralDateByTiersLanguage(dacOO.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision(), tiersBeneficiaire.getLangue());

        // Decision du
        if (isProforma()) {

            data.addData("DECISION_DU", PRStringUtils.replaceString(
                    babelDoc.getTextes(1).getTexte(52).getDescription(), SingleDACBuilder.DECISION_DU, dateDecision));
        } else {
            data.addData("DECISION_DU", PRStringUtils.replaceString(
                    babelDoc.getTextes(1).getTexte(50).getDescription(), SingleDACBuilder.DECISION_DU, dateDecision));
        }

        // Decision plan calcul
        data.addData("DECISION_D", dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDecision());
        // Annule et remplace prec
        if (!isProforma() && dacOO.getSimpleDecisionApresCalcul().getAnnuleEtRemplacePrec()) {
            data.addData("ANNULE_REMPLACE", babelDoc.getTextes(1).getTexte(60).getDescription());
        }
        // politesse
        data.addData("POLITESSE", resolveFormulePolitesse(tiersBeneficiaire));

        // intro
        data.addData("INTRO", dacOO.getSimpleDecisionApresCalcul().getIntroduction());

        // suite
        data.addData("CONFORM_LEGISLATION", babelDoc.getTextes(2).getTexte(20).getDescription());
        data.addData("VALABLE_DECISION", babelDoc.getTextes(2).getTexte(30).getDescription());

        // Bloc infos pc Accordées
        data.addData("B_PERIODE_CALCUL", babelDoc.getTextes(3).getTexte(10).getDescription());
        // gestion date
        if (JadeStringUtil.isEmpty(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision())) {
            data.addData("PERIODE_CALCUL", PRStringUtils.replaceString(babelDoc.getTextes(3).getTexte(20)
                    .getDescription(), SingleDACBuilder.DATE_DEBUT, "01."
                    + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision()));

        } else {
            int month = Integer.parseInt(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision()
                    .substring(0, 2));
            int year = Integer.parseInt(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision()
                    .substring(3));

            data.addData(
                    "PERIODE_CALCUL",
                    PRStringUtils.replaceString(babelDoc.getTextes(3).getTexte(21).getDescription(),
                            SingleDACBuilder.DATE_DEBUT, "01."
                                    + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision())
                            + " "
                            + PRStringUtils.replaceString(babelDoc.getTextes(3).getTexte(22).getDescription(),
                            SingleDACBuilder.DATE_FIN, PegasusDateUtil.getLastDayOfMonth(month - 1, year) + "."
                                    + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision()));
        }

        if (isReformePC()) {
            // TODO Changer label
            data.addData("B_MONTANT_VERS_ASSURE", babelDoc.getTextes(3).getTexte(31).getDescription());
            data.addData("MONTANT_VERS_ASSURE", SingleDACBuilder.MONNAIE + " " + new FWCurrency(getMontantPc()).toStringFormat());

            // TODO Changer label
            data.addData("B_MONTANT_VERSEE_ASS", babelDoc.getTextes(3).getTexte(32).getDescription());
            data.addData("B_MONTANT_VERSEE_ASS2", babelDoc.getTextes(3).getTexte(35).getDescription());
            data.addData("MONTANT_VERSEE_ASS", SingleDACBuilder.MONNAIE + " " + new FWCurrency(dacOO.getPlanCalcul().getPrimeVerseeAssMaladie()).toStringFormat());

            // TODO Changer label
            data.addData("B_MONTANT_HOME", babelDoc.getTextes(3).getTexte(33).getDescription());
            data.addData("MONTANT_HOME", SingleDACBuilder.MONNAIE + " " + new FWCurrency(Float.valueOf(dacOO.getPlanCalcul().getMontantPrixHome())/12).toStringFormat());
        }

        // gestion prestation
        if (isReformePC()) {
            data.addData("B_PRESTATION_MENS", babelDoc.getTextes(3).getTexte(34).getDescription());
        } else {
            data.addData("B_PRESTATION_MENS", babelDoc.getTextes(3).getTexte(30).getDescription());
        }

        switch (getEtatDecision()) {
            case REFUS:
                data.addData("PRESTATION_MENS", babelDoc.getTextes(3).getTexte(42).getDescription());
                break;

            case PARTIEL:
                data.addData("PRESTATION_MENS", babelDoc.getTextes(3).getTexte(43).getDescription());
                break;

            case OCTROI:
                data.addData("PRESTATION_MENS",
                        SingleDACBuilder.MONNAIE + " " + new FWCurrency(isReformePC() ? getMontantPCTotal() : getMontantPc()).toStringFormat());
                break;

            default:
                throw new IllegalArgumentException("The default case can't occurs: [" + getEtatDecision() + "]");

        }

        // Personnes comprises, p4
        if (!"".equals(getPersonnesComprises())) {
            data.addData("PERSONNES_COMPRISE", babelDoc.getTextes(4).getTexte(10).getDescription());
            data.addData("MEMBRES_FAMILLES", getPersonnesComprises());
        }

        buildBlocAmal(data);

        return data;
    }

    /**
     * Genération du traitement du texte amal sur la décision
     * Si propriété check amal à false --> traitement standard, code non définie lors de la préparation des décisions
     * Si propriété check amal à true --> traitement check amal
     *
     * @param data
     * @return
     * @throws DecisionException
     */
    private void buildBlocAmal(DocumentData data) throws DecisionException {

        String codeAmal = dacOO.getSimpleDecisionApresCalcul().getCodeAmal();

        // code non définit, donc fonctionnement stadard propriété à false
        if (codeAmal.equals(EPCCodeAmal.CODE_STANDARD.getProperty())) {
            generateAmalTextForStandardOperation(data);
        } else {
            generateAmalTextForCheckAmalOperation(data);
        }
    }

    /**
     * Generation du texte amal pour le traitement standard
     *
     * @param data le container de remplissage amal
     * @return le container de remplissage amal
     * @throws DecisionException
     */
    private void generateAmalTextForStandardOperation(DocumentData data) throws DecisionException {

        String amaltexte;
        String amalTexteReforme;

        switch (getEtatDecision()) {
            case OCTROI:
                amaltexte = babelDoc.getTextes(5).getTexte(27).getDescription();
                amalTexteReforme = babelDoc.getTextes(5).getTexte(30).getDescription();
                break;

            case PARTIEL:
                amaltexte = babelDoc.getTextes(5).getTexte(28).getDescription();
                amalTexteReforme = babelDoc.getTextes(5).getTexte(30).getDescription();
                break;

            case REFUS:
                amaltexte = babelDoc.getTextes(5).getTexte(29).getDescription();
                amalTexteReforme = babelDoc.getTextes(5).getTexte(30).getDescription();
                break;

            default:
                throw new IllegalArgumentException("The state of the decision is inconsistent ["
                        + this.getClass().getName() + "]");

        }

        data.addData("REDUCTION_PRIMES", babelDoc.getTextes(5).getTexte(10).getDescription());
        if (isReformePC()) {
            data.addData("B_REDUCTION_PRIMES", amalTexteReforme);
        } else {
            data.addData("B_REDUCTION_PRIMES", amaltexte);
        }


    }

    private void generateAmalTextForCheckAmalOperation(DocumentData data) {

        String amaltexte = null;
        String amaltexteReforme = null;
        Boolean displayAmal = true;
        String codeAmal = dacOO.getSimpleDecisionApresCalcul().getCodeAmal();

        // Reforme PC
        amaltexteReforme = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(30).getDescription());

        if (codeAmal.equals(EPCCodeAmal.CODE_A.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(23).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_C.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(26).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_F.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(21).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_H.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(24).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_D.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(25).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_J.getProperty())) {
            amaltexte = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(22).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_K.getProperty())
                || codeAmal.equals(EPCCodeAmal.CODE_UNDEFINED.getProperty()) || JadeStringUtil.isBlankOrZero(codeAmal)) {
            displayAmal = false;
        }

        if (displayAmal) {
            // Reduction primes, pour toutes les decisions, p5
            data.addData("REDUCTION_PRIMES", babelDoc.getTextes(5).getTexte(10).getDescription());
            if (isReformePC()) {
                data.addData("B_REDUCTION_PRIMES", amaltexteReforme);
            } else {
                data.addData("B_REDUCTION_PRIMES", amaltexte);
            }
        }

    }

    private void buildPrestationsVersesForDecompte(DocumentData data, DecompteTotalPcVO decompte)
            throws PmtMensuelException, NumberFormatException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException {
        // Si on a des prestations versées, on traite
        if (decompte.getPrestationsVerses().getList().size() > 0) {
            // Recap pca ajout du sous template
            data.addData("hasBlocPrestVerses", "TRUE");
            // Titre
            data.addData("TITRE_BLOC_PREST_VERSES", babelDoc.getTextes(16).getTexte(13).getDescription());

            Collection table = new Collection("tabPrestVerses");

            // iteration
            // Iteration sur les pca du decomte
            for (int cpt = 0; cpt < decompte.getPrestationsVerses().getList().size(); cpt++) {
                PCAccordeeDecompteVO pcaToDeal = decompte.getPrestationsVerses().getList().get(cpt);
                PCAccordeeDecompteVO nextPca = null;

                // test si 2 meme periode, cas de la separation
                if (cpt < ((decompte.getPrestationsVerses().getList().size()) - 1)) {
                    nextPca = decompte.getPrestationsVerses().getList().get(cpt + 1);

                    if (pcaToDeal.getDateDebutPeriode().equals(nextPca.getDateDebutPeriode())) {
                        table = fillLineDecompteByPeriode(table, nextPca, true);
                        table = fillLineDecompteByPeriode(table, pcaToDeal, true);
                        // on en traite deux, double incrément
                        cpt++;
                    } else {
                        table = fillLineDecompteByPeriode(table, pcaToDeal, false);
                    }

                } else {
                    table = fillLineDecompteByPeriode(table, pcaToDeal, false);
                }

            }

            // Montant total des pca
            DataList lineTotal = new DataList("ligneTotal");
            lineTotal.addData("LIBELLE", babelDoc.getTextes(16).getTexte(21).getDescription());
            lineTotal.addData("CHF", "CHF");
            lineTotal.addData("TOTAL",
                    "- " + new FWCurrency(decompte.getPrestationsVerses().getTotal().toString()).toStringFormat());

            table.add(lineTotal);

            data.add(table);
        } else {
            // Recap pca ajout du sous template
            data.addData("hasBlocPrestVerses", "FALSE");
        }

    }

    /**
     * Creation du document recapitulatif
     *
     * @param data
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private DocumentData buildRecapitulatifsDoc(DocumentData data) throws DecisionException, JadePersistenceException,
            JadeApplicationException {

        // VO Decompte
        DecompteTotalPcVO decompte = PegasusImplServiceLocator.getDecompteService().getDecompteTotalPCA(
                dacOO.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

        buildRecapitulatifsHeaderBloc(data);
        buildBlocDecomptePC(data, decompte);

        if (PCproperties.getBoolean(EPCProperties.ALLOCATION_NOEL)) {
            buildBlocAllocationNoel(data, decompte);
        }

        return data;
    }

    /**
     * Création du bloc récapitulatif
     *
     * @param data
     * @param document
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     * @throws PCAccordeeException
     */
    private DocumentData buildRecapitulatifsHeaderBloc(DocumentData data) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, PCAccordeeException {
        data.addData("RECAP_PC_TITRE", babelDoc.getTextes(16).getTexte(10).getDescription());
        data.addData("RECAP_PC_TEXTE", babelDoc.getTextes(16).getTexte(11).getDescription());

        // data.addData("REMARQUE_MONTANT", this.babelDoc.getTextes(16).getTexte(38).getDescription());

        // D0168
        String remarque = dacOO.getVersionDroit().getSimpleVersionDroit().getRemarqueDecompte();
        if (remarque != null && !remarque.trim().isEmpty()) {
            data.addData("REMARQUE_TITLE", babelDoc.getTextes(16).getTexte(41).getDescription());
            data.addData("REMARQUE_DECOMPTE", remarque);
        }
        data.addData("NB_PAGES_REMARQUE1", babelDoc.getTextes(16).getTexte(42).getDescription());
        data.addData("NB_PAGES_REMARQUE2", babelDoc.getTextes(16).getTexte(43).getDescription());

        // Ajout de l'ayant droit sur le recapitulatif
        Collection tabAyantDroit = new Collection("tabAyantDroit");
        DataList ayantDroit = new DataList("ayantDroit");
        ayantDroit.addData("AYANT_DROIT_LIBELLE", PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(40)
                .getDescription(), SingleDACBuilder.AYANT_DROIT, ""));

        ayantDroit.addData(
                "AYANT_DROIT",
                dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation1()
                        + " "
                        + dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation2()
                        + " - "
                        + dacOO.getDecisionHeader().getPersonneEtendue().getPersonne().getDateNaissance()
                        + " , "
                        + PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(30).getDescription(),
                        SingleDACBuilder.NSS, dacOO.getDecisionHeader().getPersonneEtendue()
                                .getPersonneEtendue().getNumAvsActuel()));

        tabAyantDroit.add(ayantDroit);
        data.add(tabAyantDroit);

        return data;
    }

    private void buildRecapPcaForDecompte(DocumentData data, DecompteTotalPcVO decompte) throws PmtMensuelException,
            NumberFormatException, PCAccordeeException, JadeApplicationServiceNotAvailableException {
        // Recap pca ajout du sous template
        data.addData("hasBlocRecapPca", "TRUE");
        // Titre
        data.addData("TITRE_BLOC_RECAP_PCA", babelDoc.getTextes(16).getTexte(12).getDescription());
        // tableau decompte
        Collection table = new Collection("tabDecomptePca");
        // Iteration sur les pca du decomte
        for (int cpt = 0; cpt < decompte.getPeriodesPca().getList().size(); cpt++) {
            PCAccordeeDecompteVO pcaToDeal = decompte.getPeriodesPca().getList().get(cpt);
            PCAccordeeDecompteVO nextPca = null;

            // test si 2 meme periode, cas de la separation
            if (cpt < ((decompte.getPeriodesPca().getList().size()) - 1)) {
                nextPca = decompte.getPeriodesPca().getList().get(cpt + 1);

                if (pcaToDeal.getDateDebutPeriode().equals(nextPca.getDateDebutPeriode())) {
                    table = fillLineDecompteByPeriode(table, nextPca, true);
                    table = fillLineDecompteByPeriode(table, pcaToDeal, true);
                    // on en traite deux, double incrément
                    cpt++;
                } else {
                    table = fillLineDecompteByPeriode(table, pcaToDeal, false);
                }

            } else {
                table = fillLineDecompteByPeriode(table, pcaToDeal, false);
            }

        }

        data.add(table);
        // Montant total des pca
        DataList lineTotal = new DataList("ligneTotal");
        lineTotal.addData("LIBELLE", babelDoc.getTextes(16).getTexte(21).getDescription());
        lineTotal.addData("CHF", "CHF");
        lineTotal.addData("TOTAL", new FWCurrency(decompte.getPeriodesPca().getTotal().toString()).toStringFormat());
        table.add(lineTotal);
        DataList lineVide = new DataList("ligneTotal");
        table.add(lineVide);
    }

    private void buildTotalSoldeForDecompte(DocumentData data, DecompteTotalPcVO decompte) {
        // Calcul solde
        BigDecimal solde = decompte.getTotal();
        // DataList ligneSolde = new DataList("ligneSolde");
        // Si solde négatif
        if (solde.intValue() < 0) {
            data.addData("LIBELLE_SOLDE", babelDoc.getTextes(16).getTexte(27).getDescription());
            solde = solde.abs();
        } else if (solde.intValue() == 0) {
            data.addData("LIBELLE_SOLDE", babelDoc.getTextes(16).getTexte(24).getDescription());
        } else {
            data.addData("LIBELLE_SOLDE", babelDoc.getTextes(16).getTexte(24).getDescription());
            data.addData("DECOMPTE_TEXTE", babelDoc.getTextes(16).getTexte(25).getDescription());
        }
        data.addData("CHF_SOLDE", "CHF");
        data.addData("TOTAL_SOLDE", new FWCurrency(solde.toString()).toStringFormat());

    }

    private Collection fillLineDecompteByPeriode(Collection table, PCAccordeeDecompteVO pcaForPeriode,
                                                 Boolean dealPcaCSType) throws PmtMensuelException, JadeApplicationServiceNotAvailableException,
            NumberFormatException, PCAccordeeException {

        String dateFinDecompte = pcaForPeriode.getDateFinPeriode();
        String dateDebutDecompte = pcaForPeriode.getDateDebutPeriode();
        String langueTiers = dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getLangue();

        Integer nbreMois = pcaForPeriode.getNbreMois();

        if (nbreMois > 0) {
            // Montant pc mensuelle
            BigDecimal mPca = pcaForPeriode.getMontantPcaMensuel();

            // montant total pour le decompte, ajout du montant de la peridoe
            // this.totalPcRetroForDecompte = this.totalPcRetroForDecompte.add(pcaForPeriode);

            BigDecimal totalForPeriode = pcaForPeriode.getMontantForPeriod();

            // recup des mois et annee
            String mDeb = dateDebutDecompte.substring(0, 2);
            String mFin = dateFinDecompte.substring(0, 2);
            String an = dateFinDecompte.substring(3);

            DataList line = new DataList("ligneStandard");
            // ligne standard decompte
            String libelleForLineStandard = getLibelleForLigneStandardForDecompte(mDeb, mFin, an);
            if (dealPcaCSType) {
                CodeSystem codeSystemGenrePca = CodeSystemQueryExecutor.searchCodeSystemTraduction(
                        pcaForPeriode.getCsGenrePca(), getSession(),
                        LanguageResolver.resolveCodeSystemFromLanguage(langueTiers));
                libelleForLineStandard += " (" + codeSystemGenrePca.getTraduction() + ")";

            }
            // Ajoute de la designation du ou des beneficiaires
            libelleForLineStandard += SingleDACBuilder.CRLF + pcaForPeriode.getDescTiers();

            StringBuilder strMoisDebut = new StringBuilder(getMonthName(Integer.parseInt(mDeb)));
            strMoisDebut.replace(0, 1, strMoisDebut.substring(0, 1).toUpperCase());
            line.addData("LIBELLE", libelleForLineStandard);
            line.addData("MOISA", nbreMois + " " + babelDoc.getTextes(16).getTexte(26).getDescription());
            line.addData("CHF", "CHF");
            line.addData("MONTANT", new FWCurrency(mPca.toString()).toStringFormat());
            line.addData("TOTAL", "" + new FWCurrency(totalForPeriode.toString()).toStringFormat());
            table.add(line);

            if (pcaForPeriode.hasJourAppoint()) {
                fillLineJoursAppoints(table, pcaForPeriode);
            }

        }

        return table;
    }

    private void fillLineJoursAppoints(Collection table, PCAccordeeDecompteVO pcaForPeriode) {

        String moisJoursAppoints = pcaForPeriode.getSimpleJoursAppoint().getDateEntreHome().substring(3, 5);
        String annee = pcaForPeriode.getSimpleJoursAppoint().getDateEntreHome().substring(6);
        String nbreJours = pcaForPeriode.getSimpleJoursAppoint().getNbrJoursAppoint();
        String montantJour = pcaForPeriode.getSimpleJoursAppoint().getMontantJournalier();

        // complément
        StringBuilder libelleForLineStandard = new StringBuilder(babelDoc.getTextes(16).getTexte(33).getDescription());
        libelleForLineStandard.append(" ").append(getLibelleForLigneStandardForJoursAppoint(moisJoursAppoints, annee));

        DataList line = new DataList("ligneStandard");
        // Jours d'appoints
        StringBuilder strMoisDebut = new StringBuilder(getMonthName(Integer.parseInt(moisJoursAppoints)));
        strMoisDebut.replace(0, 1, strMoisDebut.substring(0, 1).toUpperCase());
        line.addData("LIBELLE", libelleForLineStandard.toString());
        line.addData("MOISA", nbreJours + " " + babelDoc.getTextes(16).getTexte(32).getDescription());
        line.addData("CHF", "CHF");
        line.addData("MONTANT", new FWCurrency(montantJour.toString()).toStringFormat());
        line.addData(
                "TOTAL",
                ""
                        + new FWCurrency(pcaForPeriode.getSimpleJoursAppoint().getMontantTotal().toString())
                        .toStringFormat());
        table.add(line);
    }

    private String getAdresseForCopies(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers,
                Boolean.TRUE, JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        if (detailTiers.getFields() != null) {
            return detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
        } else {
            return "";
        }

    }

    private String getBlocVersementAdresse() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        if (detailPaiement.getFields() != null) {
            // return detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
            // + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
            String adressed1 = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) : "";
            String adressed2 = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2) : "";
            String rue = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) : "";
            String noRue = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) : "";

            String npa = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) : "";
            String localite = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE)).trim()
                    .length() != 0) ? detailPaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE) : "";

            StringBuilder blocVersementAdresse = new StringBuilder();
            blocVersementAdresse.append(adressed1).append(" ");
            blocVersementAdresse.append(adressed2).append(CR);
            blocVersementAdresse.append(rue).append(" ");
            blocVersementAdresse.append(noRue).append(CR);
            blocVersementAdresse.append(npa).append(" ");
            blocVersementAdresse.append(localite);

            return blocVersementAdresse.toString();

        } else {
            return "";
        }

    }

    private String getBlocVersementPaiement() {
        if (detailPaiement.getFields() != null) {

            String iban = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE)).trim().length() != 0) ? detailPaiement
                    .getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE) : "";

            String clearing = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING)).trim()
                    .length() != 0) ? detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING) : "";
            String banqued1 = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1)).trim()
                    .length() != 0) ? detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1) + " "
                    : "";
            String banqued2 = ((detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D2)).trim()
                    .length() != 0) ? detailPaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D2) : "";

            return (banqued1 + " " + banqued2 + SingleDACBuilder.CR + clearing + SingleDACBuilder.CR + iban);

        } else {
            return "";
        }
    }

    private String getDateAsStringFormatted(String jadeDate) {
        return PegasusDateUtil.getLitteralDate(jadeDate);

    }

    /**
     * Retourne l'état de la décision refus, octroi ou partiel
     *
     * @throws DecisionException
     * @return, contante enum de EtatDecision
     */
    private EtatDecision getEtatDecision() throws DecisionException {

        if (dacOO.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return EtatDecision.OCTROI;
        } else if (dacOO.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
            return EtatDecision.PARTIEL;
        } else {
            return EtatDecision.REFUS;
        }

    }

    private String getLibelleForLigneStandardForDecompte(String mDeb, String mFin, String an) {
        StringBuilder strMoisDebut = new StringBuilder(getMonthName(Integer.parseInt(mDeb)));
        strMoisDebut.replace(0, 1, strMoisDebut.substring(0, 1).toUpperCase());

        return strMoisDebut + " " + an + " " + babelDoc.getTextes(6).getTexte(21).getDescription() + " "
                + getMonthName(Integer.parseInt(mFin)) + " " + an;
    }

    private String getLibelleForLigneStandardForJoursAppoint(String mDeb, String an) {
        StringBuilder strMoisDebut = new StringBuilder(getMonthName(Integer.parseInt(mDeb)));

        return strMoisDebut + " " + an;
    }

    private String getMontantPc() {
        // Si tiers egal, c'est le requérant, on retourne le montant de la pc
        if (dacOO.getSimplePrestation().getIdTiersBeneficiaire()
                .equals(dacOO.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire())) {
            return dacOO.getSimplePrestation().getMontantPrestation();
        } else {
            Float montantConjoint = Float.parseFloat(dacOO.getPlanCalcul().getMontantPCMensuelle())
                    - Float.parseFloat(dacOO.getSimplePrestation().getMontantPrestation());
            return new FWCurrency(montantConjoint.toString()).toStringFormat();
        }
    }

    private String getMontantPCTotal() {
        Float montantPCTotal = Float.parseFloat(dacOO.getSimplePrestation().getMontantPrestation())
                + Float.parseFloat(dacOO.getPlanCalcul().getPrimeVerseeAssMaladie());
        return new FWCurrency(montantPCTotal.toString()).toStringFormat();
    }

    /**
     * Retourn le nom du mois en fonction du numéro passé en param
     *
     * @param month
     * @return
     */
    private String getMonthName(int month) {
        String langueTiers = dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getLangue();
        return JACalendar.getMonthName(month, PRUtil.getISOLangueTiers(langueTiers));
    }

    private Boolean isConjointDom2R() {
        // Si tiers egal, c'est le requérant, on retourne le montant de la pc
        if (dacOO.getSimplePrestation().getIdTiersBeneficiaire()
                .equals(dacOO.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire())) {
            return false;
        }
        return true;
    }

    private boolean isConjointPcaConjoint() {
        return dacOO.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire()
                .equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    /**
     * Retourne une chaine de caractère comprenant les membres de familles compris dans le calcul
     *
     * @return chaine de caractère
     * @throws PersonneDansPlanCalculException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private String getPersonnesComprises() throws PersonneDansPlanCalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // recherche des membres familles compris dans le calcul PC, avec id pcaccordées
        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setForIdPcal(dacOO.getPlanCalcul().getIdPlanDeCalcul());// (this.planDeCalcul.getIdPCAccordee());
        search.setForComprisPcal(Boolean.TRUE);
        search.setOrderKey("orderByNaissance");
        // search.setForCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_ENFANT);

        Boolean isConjoint = isConjointDom2R() || isConjointPcaConjoint();

        StringBuilder membreCompris = new StringBuilder("");
        plaCalMembreFamSearch = PegasusServiceLocator.getPCAccordeeService().search(search);

        for (JadeAbstractModel model : plaCalMembreFamSearch.getSearchResults()) {
            PlanDeCalculWitMembreFamille membreFamille = (PlanDeCalculWitMembreFamille) model;
            // si le mebre de famille est enfant on l'ajoute
            RoleMembreFamille roleFamille = RoleMembreFamille.fromValue(membreFamille.getDroitMembreFamille()
                    .getSimpleDroitMembreFamille().getCsRoleFamillePC());

            // si c est le conjoint, et que ce n'est pas une décision pour le conjoint, on l'ajoute
            // si c est le requerant, et que c'est une décision pour le conjoint, on l'ajoute
            if (roleFamille.isEnfant() || (roleFamille.isConjoint() && !isConjoint)
                    || (roleFamille.isRequerant() && isConjoint)) {
                membreCompris.append(membreFamille.getDroitMembreFamille().getMembreFamille().getNom());
                membreCompris.append(" ").append(membreFamille.getDroitMembreFamille().getMembreFamille().getPrenom());
                membreCompris.append(", ");
            }
        }
        String membreComprisStr = membreCompris.toString();

        if ((membreCompris != null) && (membreCompris.length() > 0)) {
            // on enleve la dernier virgule
            membreComprisStr = membreCompris.substring(0, membreCompris.length() - 2);

        }
        return membreComprisStr;
    }

    private Boolean isAnnexeBillag() throws Exception {
        for (SimpleAnnexesDecision annexe : dacOO.getDecisionHeader().getListeAnnexes()) {
            if (annexe.getCsType().equals(IPCDecision.ANNEXE_BILLAG_AUTO)) {
                Langues langueTiers = LanguageResolver.resolveISOCode(dacOO.getPcAccordee().getPersonneEtendue().getTiers()
                        .getLangue());
                String prop = getSession().getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
                String message =
                        MessageFormat.format(LanguageResolver.resolveLibelleFromLabel(
                                langueTiers.getCodeIso(), IPCDecision.BILLAG_ANNEXES_STRING, getSession()), prop);
                annexe.setValeur(message);
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne true su la decision est de type PROFORMA
     *
     * @return
     */
    private Boolean isProforma() {
        return dacOO.getSimpleDecisionApresCalcul().getCsTypePreparation().equals(IPCDecision.CS_GENRE_PROFORMA);
    }

    /**
     * Retourne true su la decision est provisoire
     *
     * @return
     */
    private Boolean isProvisoire() {
        return dacOO.getDecisionHeader().getSimpleDecisionHeader().isDecisionProvisoire() != null
                && dacOO.getDecisionHeader().getSimpleDecisionHeader().isDecisionProvisoire();
    }

    /**
     * Chargement du container de transfert pour le remplissage de pixis
     *
     * @param idTiers l'identifiant du tiers concerné pour les informations
     * @throws DecisionException si un problème survient lors du remplissge
     */
    private void loadPixisInfoForPubInfo(String idTiers) throws DecisionException {
        try {
            TIDocumentInfoHelper.fill(pubInfosPixisProperties, idTiers, getSession(), null, null, null);
            // fillForLigneTechnique(pubInfosPixisProperties, dacOO);
        } catch (Exception e) {
            throw new DecisionException(
                    "An error happened during filling the document with pyxis informations for the following idTiers:["
                            + idTiers + "]", e);
        }
    }

    private String replaceDateDecisionAmalInString(String baseString) {
        return PRStringUtils.replaceString(baseString, SingleDACBuilder.DATE_DECISION_AMAL_REPLACE, dacOO
                .getSimpleDecisionApresCalcul().getDateDecisionAmal());
    }

    private String resolveTitreTiersCopie(SimpleCopiesDecision copie) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        PersonneEtendueComplexModel personneEtendueCopie = TIBusinessServiceLocator.getPersonneEtendueService().read(
                copie.getIdTiersCopie());

        String csTitre = null;
        String titre = "";
        TiersSimpleModel tiersCopie = null;
        if (personneEtendueCopie.getPersonne().isNew()) {
            AdministrationComplexModel administration = TIBusinessServiceLocator.getAdministrationService().read(
                    copie.getIdTiersCopie());
            tiersCopie = administration.getTiers();
        } else {
            tiersCopie = personneEtendueCopie.getTiers();
        }

        csTitre = tiersCopie.getTitreTiers();
        if (JadeStringUtil.isBlankOrZero(csTitre)) {
            csTitre = "19120003"; // Madame Monsieur
        }
        if (tiersCopie != null) {
            JadeCodeSysteme codeSyteme = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(csTitre);
            if (codeSyteme != null) {
                titre = codeSyteme.getTraduction(Langues.getLangueDepuisCodeIso(PRUtil.getISOLangueTiers(tiersCopie
                        .getLangue())));
            }
        }
        return titre;
    }

    private void setAdressePaiement(TITiers tiersBeneficiaire) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        detailPaiement = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                tiersBeneficiaire.getIdTiers(), Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                JACalendar.todayJJsMMsAAAA(), null);

    }

    private String resolveFormulePolitesse(TITiers tiersBeneficiaire) {
        String formulePolitesse = "";
        try {
            formulePolitesse = tiersBeneficiaire.getFormulePolitesse(LanguageResolver
                    .resolveCodeSystemFromLanguage(tiersBeneficiaire.getLangue()));
        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "An error happened while trying to get the title of the following tiers  [id :"
                            + tiersBeneficiaire.getId() + "]");
        }

        return addCommaIFFrench(formulePolitesse,
                LanguageResolver.resolveISOCodeToString(tiersBeneficiaire.getLangue()));
    }

    private String addCommaIFFrench(String formulePolitesse, String codeIsoLangue) {
        Langues langue = LanguageResolver.resolveISOCode(codeIsoLangue);
        if (Langues.Francais.equals(langue)) {
            return formulePolitesse + ",";
        } else {
            return formulePolitesse;
        }
    }

    public boolean isReformePC() {
        return dacOO.getPlanCalcul().getReformePc();
    }
}
