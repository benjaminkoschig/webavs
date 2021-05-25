package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.babel.api.ICTDocument;
import globaz.cygnus.utils.PCFraisMaladieAnnuelUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.security.FWSecurityLoginException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextGiverBabel;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCTransfert;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.decision.ValidationDecision;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtendu;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtenduSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.IPageDeGardeDefinition;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeDefinition;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.Adresse;
import ch.globaz.pyxis.business.model.AdresseSearch;
import ch.globaz.pyxis.business.model.AdresseSimpleModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleTransfertSuppressionPCBuilder extends SingleTransfertPCAbstractBuilder {

    private class MontantsRFM {
        String fraisInvalidite = null;
        String fraisMaladie = null;
        String fraisNonRembourses = null;
        String fraisRegimes = null;
    }

    private static final String CHAMP_NOM_COLLABORATEUR = "NOM_COLLABO";
    private static final String CHAMP_PERSONNE_REF = "PERSONNE_REF";

    private static final String CHAMP_TEL_COLLABORATEUR = "TEL_COLLABO";

    private static final int FICHE_TRANSFERT_SIZE_MONTANT = 15;

    public static final ArrayList<String> listOrderAdresseTiers = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(AdresseService.CS_TYPE_DOMICILE);
            this.add(AdresseService.CS_TYPE_COURRIER);
        }
    };
    private List<String> annexes;
    private ICTDocument babelDoc = null;
    private ICTDocument babelPageGardeDoc = null;
    private List<TITiers> copies = null;
    private String dateDecision = null;
    private String dateSuppression = null;
    private Map<String, AdresseTiersDetail> dernieresAdresses = new HashMap<String, AdresseTiersDetail>();
    private Map<String, PCAccordee> dernieresPcAccordees = new HashMap<String, PCAccordee>();
    private JadeUser gestionnaire = null;
    private String idDroit = null;
    private String idNouvelleCaisse = null;
    private Map<String, PersonneEtendueComplexModel> membres = new HashMap<String, PersonneEtendueComplexModel>();
    private Map<String, MontantsRFM> montantsRFM = new HashMap<String, MontantsRFM>();
    private String motifContact = null;
    private String motifTransfert;
    private Map<Langues, CTDocumentImpl> documentsBabelTransfertDossier = null;

    private List<PCAccordee> pcAccordees;

    private void addAnnexe(JadePrintDocumentContainer allDoc, String csRoleMbrFam, JadePublishDocumentInfo pubInfo)
            throws TransfertDossierException {

        if (isVDIntercommunal()) {
            addFicheVDLausanne(allDoc, csRoleMbrFam, pubInfo);
        } else {
            addAnnexeFederale(allDoc, csRoleMbrFam, pubInfo);
        }

    }

    private void addAnnexeFederale(JadePrintDocumentContainer allDoc, String csRoleMbrFam,
            JadePublishDocumentInfo pubInfo) throws TransfertDossierException {
        try {
            DocumentData dataAnnexeFederal = new DocumentData();
            dataAnnexeFederal = buildAnnexeFederale(dataAnnexeFederal, csRoleMbrFam);
            dataAnnexeFederal.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                    IPCCatalogueTextes.PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_ANNEXE_PC);
            allDoc.addDocument(dataAnnexeFederal, pubInfo);
        } catch (Exception e) {
            throw new TransfertDossierException("An error happenend while generating annexe federale!", e);
        }
    }

    private void addFicheVDLausanne(JadePrintDocumentContainer allDoc, String csRoleMbrFam,
            JadePublishDocumentInfo pubInfo) throws TransfertDossierException {
        try {
            DocumentData dataFiche = new DocumentData();
            buildHeader(dataFiche, false);
            dataFiche = this.buildFicheTransfertVDLausanne(dataFiche);
            dataFiche.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                    IPCCatalogueTextes.PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_FICHE_VD_LAUSANNE);
            buildSignatures(dataFiche);
            allDoc.addDocument(dataFiche, pubInfo);
        } catch (Exception e) {
            throw new TransfertDossierException("An error happenend while generating fiche de transfert dossier PC!", e);
        }
    }

    private void addPlanCalcul(JadePrintDocumentContainer allDoc, String csRoleMembreFam,
            JadePublishDocumentInfo pubInfo) throws TransfertDossierException {
        try {
            // récupération du id de la décision AC lié à la PCAccordée
            String idDecisionApresCalcul = getIdDecisionApresCalcul(csRoleMembreFam);

            // génération du plan de calcul
            DocumentData dataPlanCalcul = PegasusImplServiceLocator.getDecisionApresCalculService()
                    .buildPlanCalculDocumentData(idDecisionApresCalcul, true, true,"");
            // uniquement si la PCA contient un BLOB (pas dans les cas de reprise)
            if (dataPlanCalcul != null) {
                allDoc.addDocument(dataPlanCalcul, pubInfo);
            }
        } catch (Exception e) {
            throw new TransfertDossierException("An exception happened while retrieving plan de calcul!", e);
        }
    }

    public JadePrintDocumentContainer build(Map<Langues, CTDocumentImpl> documentsBabelTransfertDossier,
            Map<Langues, CTDocumentImpl> babelPageGardeDocuments, JadePrintDocumentContainer allDoc,
            String idGestionnaire, PersonneEtendueComplexModel requerant, String idNouvelleCaisse,
            String motifTransfert, String dateDecision, String dateSuppression, AdresseTiersDetail derniereAdresse,
            String motifContact, List<String> annexes, List<String> copies, List<PCAccordee> listPcAccordees,
            String idDroit) throws TransfertDossierException {
        try {
            this.idDroit = idDroit;
            this.dateDecision = dateDecision;
            this.dateSuppression = dateSuppression;
            dernieresAdresses.put(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, derniereAdresse);
            this.motifContact = motifContact;
            pcAccordees = listPcAccordees;
            // récupère l'utilisateur
            gestionnaire = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), idGestionnaire);
            membres.put(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, requerant);
            this.idNouvelleCaisse = idNouvelleCaisse;
            this.motifTransfert = motifTransfert;
            this.annexes = annexes;
            this.copies = getTiersCopies(copies);
            Langues langueTiersRequerant = LanguageResolver.resolveISOCode(requerant.getTiers().getLangue());
            babelDoc = documentsBabelTransfertDossier.get(langueTiersRequerant);
            this.documentsBabelTransfertDossier = documentsBabelTransfertDossier;
            // traite les pc accordees
            dernieresPcAccordees.put(IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                    getDernierePcAccordee(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
            PCAccordee pcaConjoint = getDernierePcAccordee(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);

            if (pcaConjoint != null) {
                dernieresPcAccordees.put(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, pcaConjoint);
                String idTiersConjoint = pcaConjoint.getPersonneEtendue().getTiers().getIdTiers();
                AdresseTiersDetail adresseConjoint = PegasusUtil.getAdresseCascadeByType(idTiersConjoint,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        SingleTransfertSuppressionPCBuilder.listOrderAdresseTiers);
                dernieresAdresses.put(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, adresseConjoint);
                membres.put(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, pcaConjoint.getPersonneEtendue());
            }

            // récupère le ou les info RFM
            chargeMontantsRFM();

            JadePublishDocumentInfo pubInfo = new PegasusPubInfoBuilder().ged().rectoVersoFirst().getPubInfo();
            // prepare pubInfos
            buildOriginal(allDoc, false, pubInfo);

            /*********************** Copies ************/

            this.buildCopies(allDoc, babelPageGardeDocuments);

            return allDoc;
        } catch (FWSecurityLoginException e) {
            throw new TransfertDossierException("Couldn't login when trying to get gestionnaire!", e);
        } catch (Exception e) {
            throw new TransfertDossierException("An error happened while building the document!", e);
        }
    }

    /**
     * Creation de l'annexe fédérale
     * 
     * @param data
     * @param csRoleMbrFam
     *            TODO
     * @param tiers
     * @param document
     * @return data, instance de DocumentData mis à jour
     * @throws Exception
     */
    private DocumentData buildAnnexeFederale(DocumentData data, String csRoleMbrFam) throws Exception {

        AdresseTiersDetail adresseNouvelleCaisse = PegasusUtil.getAdresseCascadeByType(idNouvelleCaisse,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);
        data.addData("ORGANE_EXECUTION", adresseNouvelleCaisse.getAdresseFormate());

        // ancienne adresse
        Adresse ancienneAdresse = getAncienneAdresse(csRoleMbrFam);
        data.addData("ANCIENNE_ADRESSE", formateAdresse(ancienneAdresse));

        PersonneEtendueComplexModel membre = membres.get(csRoleMbrFam);
        data.addData("NO_ASSURE", membre.getPersonneEtendue().getNumAvsActuel());
        data.addData("NOM_PRENOM", PegasusUtil.formatNomPrenom(membre.getTiers()));
        AdresseTiersDetail derniereAdresse = dernieresAdresses.get(csRoleMbrFam);
        data.addData("RUE", derniereAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                + derniereAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
        data.addData("DOMICILE", derniereAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                + derniereAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));

        PCAccordee dernierePCAccordee = dernieresPcAccordees.get(csRoleMbrFam);
        data.addData("MONTANT_PC",
                new FWCurrency(dernierePCAccordee.getPlanRetenu().getMontantPCMensuelle()).toStringFormat());
        data.addData("DATE_DECISION", dateDecision + "/" + gestionnaire.getVisa());

        String dateFinVersement = PegasusDateUtil.getLastDayOfMonth(dateSuppression) + "." + dateSuppression;
        dateFinVersement = JadeDateUtil.getLastDateOfMonth(dateFinVersement);
        data.addData("DATE_FIN_VERSEMENT", dateFinVersement);

        MontantsRFM montants = montantsRFM.get(csRoleMbrFam);

        data.addData("FRAIS_MALADIE", new FWCurrency(montants.fraisMaladie).toString());
        data.addData("FRAIS_INVALIDITE", new FWCurrency(montants.fraisInvalidite).toString());
        data.addData("FRAIS_NON_REMBOURSES", new FWCurrency(montants.fraisNonRembourses).toString());

        return data;

    }

    /**
     * Permet de charger un tiers de type TITiers.
     * On charge ce type de tiers afin de recherche la formule de politesse du document pour les traductions.
     * 
     * @param idTiers
     * @return
     * @throws Exception
     */
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

    /**
     * Création du bloc annexes
     * 
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     */
    private DocumentData buildAnnexes(DocumentData data) {

        data.addData("ANNEXES", PegasusUtil.joinArray(annexes, AbstractPegasusBuilder.NEW_LINE));
        return data;
    }

    private void buildContent(DocumentData data) throws TransfertDossierException {

        try {
            PersonneEtendueComplexModel membre = membres.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

            int niveauBabelPrestation;
            int niveauBabelGeneral;

            if (isVDIntercommunal()) {
                niveauBabelPrestation = 11;
                niveauBabelGeneral = 10;
            } else {
                niveauBabelPrestation = 2;
                niveauBabelGeneral = 1;
            }

            // champs à remplir
            data.addData("TITRE", babelDoc.getTextes(niveauBabelGeneral).getTexte(1).getDescription());
            String formulePolitesseTiers = "";
            String formulePolitesseTiersWithComa = "";
            try {
                TITiers tiers = loadTiers(membre.getTiers().getIdTiers());
                formulePolitesseTiers = tiers.getFormulePolitesse(LanguageResolver.resolveCodeSystemFromLanguage(tiers
                        .getLangue()));

                formulePolitesseTiersWithComa = addCommaIFFrench(formulePolitesseTiers,
                        LanguageResolver.resolveISOCodeToString(tiers.getLangue()));

            } catch (Exception e) {
                throw new TransfertDossierException("An error happened while trying to load the tiers", e);
            }
            data.addData("TITRE_POLITESSE", formulePolitesseTiersWithComa);

            // prestations
            data.addData("CONTENU0", babelDoc.getTextes(niveauBabelGeneral).getTexte(2).getDescription());

            PCAccordee pcaRequerant = dernieresPcAccordees.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            PCAccordee pcaConjoint = dernieresPcAccordees.get(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);

            if (pcaRequerant != null) {
                String montantPC = new FWCurrency(pcaRequerant.getPlanRetenu().getMontantPCMensuelle())
                        .toStringFormat();
                data.addData("PRESTATION0", babelDoc.getTextes(niveauBabelPrestation).getTexte(1).getDescription()
                        .replace("{MONTANT_PC}", montantPC));
            }

            if (pcaConjoint != null) {
                String montantPC = new FWCurrency(pcaConjoint.getPlanRetenu().getMontantPCMensuelle()).toStringFormat();
                data.addData("PRESTATION1", babelDoc.getTextes(niveauBabelPrestation).getTexte(2).getDescription()
                        .replace("{MONTANT_PC}", montantPC));
            }

            data.addData("PRESTATION2", babelDoc.getTextes(niveauBabelPrestation).getTexte(3).getDescription());

            data.addData(
                    "CONTENU1",
                    babelDoc.getTextes(niveauBabelGeneral).getTexte(3).getDescription()
                            .replace("{DATE}", JadeDateUtil.addMonths("01." + dateSuppression, 1)));

            data.addData("MOTIF", motifTransfert);

            data.addData("CONTENU3", motifContact);

            String salutations = babelDoc.getTextes(niveauBabelGeneral).getTexte(5).getDescription();

            data.addData("SALUTATIONS", salutations.replace("{POLITESSE}", formulePolitesseTiers));
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("A Jade error happened!", e);
        }
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
    private DocumentData buildCopies(DocumentData data) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Liste des copies
        String[] names = new String[copies.size()];
        int i = 0;
        for (TITiers tiers : copies) {
            names[i++] = tiers.getDesignation1() + " " + tiers.getDesignation2();
        }

        data.addData("COPIE", babelDoc.getTextes(4).getTexte(2).getDescription());
        data.addData("COPIES", PegasusUtil.joinArray(names, AbstractPegasusBuilder.NEW_LINE));

        return data;
    }

    /**
     * Les copies doivent contenir un catalogue de texte spécifique en fonction de la langue de la caisse à qui on
     * effectue le transfert.
     * Les chargements spécifiques de catalogues de texte en fonction des langues doivent être faits dans cette boucle.
     * 
     * @param allDoc
     * @throws Exception
     */
    private void buildCopies(JadePrintDocumentContainer allDoc, Map<Langues, CTDocumentImpl> babelPageGardeDocuments)
            throws Exception {
        for (TITiers tiers : copies) {
            JadePublishDocumentInfo pubInfo = new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo();
            Langues tiersLangue = LanguageResolver.resolveISOCode(tiers.getLangue());
            // récupération du catalogues de textes dans la langue du tiers.
            babelPageGardeDoc = babelPageGardeDocuments.get(tiersLangue);
            // page de garde
            TextGiver<BabelTextDefinition> textGiver = new TextGiverBabel(babelPageGardeDoc);
            IPageDeGardeDefinition pageDeGardeDefinition = new PageDeGardeDefinition(textGiver);
            String nssReference = membres.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).getPersonneEtendue()
                    .getNumAvsActuel();
            PageDeGardeBuilder pageDeGardeBuilder = new PageDeGardeBuilder(babelPageGardeDoc, pageDeGardeDefinition,
                    tiers, gestionnaire, null, nssReference, dateDecision);
            DocumentData dataPageGarde = pageDeGardeBuilder.buildPageDeGarde();
            dataPageGarde
                    .addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_PG);
            allDoc.addDocument(dataPageGarde, pubInfo);
            // ajout de l'original
            buildOriginal(allDoc, true, pubInfo);
        }
    }

    private DocumentData buildFicheTransfertVDLausanne(DocumentData data) throws Exception {
        // la fiche de transfert est toujours en français
        ICTDocument babelDocForFicheTransfert = documentsBabelTransfertDossier.get(Langues.Francais);
        String designationCaisse = babelDocForFicheTransfert.getTextes(13).getTexte(1).getDescription();

        data.addData("DESIGNATION_CAISSE", designationCaisse);
        String dateFinVersement = JadeDateUtil.addMonths("01." + dateSuppression, -1);
        dateFinVersement = JadeDateUtil.getLastDateOfMonth(dateFinVersement);
        data.addData("DATE_FIN_PRESTATIONS", dateFinVersement);

        // adresses
        Adresse ancienneAdresse = getAncienneAdresse(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        data.addData("ANCIENNE_ADRESSE", formateAdresse(ancienneAdresse));

        AdresseTiersDetail adresseTiersDetail = dernieresAdresses.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        data.addData("NOUVELLE_ADRESSE", adresseTiersDetail.getAdresseFormate());

        data = this.buildFicheTransfertVDLausanne(data, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "REQUERANT");
        data = this.buildFicheTransfertVDLausanne(data, IPCDroits.CS_ROLE_FAMILLE_CONJOINT, "CONJOINT");

        return data;
    }

    private DocumentData buildFicheTransfertVDLausanne(DocumentData data, String csRoleMbrFam, String suffix)
            throws Exception {

        // noms
        PersonneEtendueComplexModel membre = membres.get(csRoleMbrFam);
        if ((membre == null) && IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(csRoleMbrFam)) {
            return data; // si le conjoint n'est pas trouvé, il n'existe pas
        }

        data.addData("NSS_" + suffix, membre.getPersonneEtendue().getNumAvsActuel());
        data.addData("NOM_PRENOM_" + suffix, PegasusUtil.formatNomPrenom(membre.getTiers()));

        // PC mensuelle
        PCAccordee dernierePCAccordee = dernieresPcAccordees.get(csRoleMbrFam);
        data.addData("PC_MENSUELLE_" + suffix, JadeStringUtil.rightJustify(new FWCurrency(dernierePCAccordee
                .getPlanRetenu().getMontantPCMensuelle()).toStringFormat(),
                SingleTransfertSuppressionPCBuilder.FICHE_TRANSFERT_SIZE_MONTANT));

        MontantsRFM montants = montantsRFM.get(csRoleMbrFam);

        data.addData("FRAIS_MALADIE_" + suffix, new FWCurrency(montants.fraisMaladie).toStringFormat());
        data.addData("FRAIS_INVALIDITE_" + suffix, new FWCurrency(montants.fraisInvalidite).toStringFormat());
        data.addData("REGIME_" + suffix, new FWCurrency(montants.fraisRegimes).toStringFormat());

        data.addData("DATE_ENTREE_EMS_" + suffix, getDateEntreeEMS(csRoleMbrFam));

        return data;
    }

    private void buildHeader(DocumentData data, Boolean isCopie) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();
        data.addData(SingleTransfertSuppressionPCBuilder.CHAMP_PERSONNE_REF, babelDoc.getTextes(1).getTexte(23)
                .getDescription());
        // Infos collaborateur
        data.addData(SingleTransfertSuppressionPCBuilder.CHAMP_NOM_COLLABORATEUR, nomCollabo);
        data.addData(SingleTransfertSuppressionPCBuilder.CHAMP_TEL_COLLABORATEUR, babelDoc.getTextes(1).getTexte(24)
                .getDescription()
                + " " + tel);

        data.addData("NREF", babelDoc.getTextes(1).getTexte(21).getDescription() + " " + gestionnaire.getVisa());
        data.addData(
                "VREF",
                babelDoc.getTextes(1).getTexte(22).getDescription() + " "
                        + membres.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).getPersonneEtendue().getNumAvsActuel());

        // Date et lieu
        data.addData(
                "DATE_ET_LIEU",
                babelDoc.getTextes(1).getTexte(10).getDescription() + " "
                        + PegasusDateUtil.getLitteralDateByTiersLanguage(dateDecision, babelDoc.getCodeIsoLangue()));

        if (isCopie) {
            data.addData("IS_COPIE", babelDoc.getTextes(19).getTexte(5).getDescription());
        }

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(
                membres.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).getTiers().getIdTiers(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        data.addData("ADRESSE", adresseTiersDetail.getAdresseFormate());
    }

    private DocumentData buildMainPage(boolean isCopy) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        DocumentData mainPage = new DocumentData();

        buildHeader(mainPage, isCopy);

        buildContent(mainPage);

        buildSignatures(mainPage);

        // Annexes et copies
        mainPage.addData("ANNEXE", babelDoc.getTextes(4).getTexte(1).getDescription());
        if (isCopy || isAnnexeAssures()) {
            buildAnnexes(mainPage);
        }
        this.buildCopies(mainPage);

        return mainPage;
    }

    private void buildOriginal(JadePrintDocumentContainer allDoc, boolean isCopy, JadePublishDocumentInfo pubInfo)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        DocumentData mainPage = buildMainPage(isCopy);

        mainPage.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_PC);
        allDoc.addDocument(mainPage, pubInfo);

        JadePublishDocumentInfo pubInfoAnnexes = new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo();

        if (isCopy || isAnnexeAssures()) {
            addAnnexe(allDoc, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, pubInfoAnnexes);
            addPlanCalcul(allDoc, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, pubInfoAnnexes);
            if (dernieresPcAccordees.containsKey(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                addAnnexe(allDoc, IPCDroits.CS_ROLE_FAMILLE_CONJOINT, pubInfoAnnexes);
                addPlanCalcul(allDoc, IPCDroits.CS_ROLE_FAMILLE_CONJOINT, pubInfoAnnexes);
            }
        }
    }

    private DocumentData buildSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        data.addData("SIGNATURE_NOM_CAISSE", babelDoc.getTextes(19).getTexte(2).getDescription());
        return data;
    }

    private void calculeMontantsRFMBeneficiaire(String csRoleMembreFamille, List<String> listIdTiersFamille,
            String annee) throws Exception {

        MontantsRFM montantsMembre = montantsRFM.get(csRoleMembreFamille);
        String strListIdTiers = PegasusUtil.joinArray(listIdTiersFamille, ",");

        // recherche des montants RFM communs
        String[] montantsCalculesCommun = PCFraisMaladieAnnuelUtils.calculeMontantsRFMBeneficiaire(getSession(),
                strListIdTiers, annee);

        montantsMembre.fraisMaladie = montantsCalculesCommun[0];
        montantsMembre.fraisInvalidite = montantsCalculesCommun[1];
        montantsMembre.fraisNonRembourses = "0";

        // recherche de montants RFM spécific Vaud-Lausanne
        if (isVDIntercommunal()) {
            String[] montantsCalculesFiche = PCFraisMaladieAnnuelUtils.calculeRFMontantsFicheTransfert(getSession(),
                    strListIdTiers, annee);

            montantsMembre.fraisRegimes = montantsCalculesFiche[0];
        }
    }

    private void calculeMontantsRFMBeneficiaire(String csRoleMembreFamille, String annee) throws Exception {
        final String idTiers = membres.get(csRoleMembreFamille).getTiers().getIdTiers();
        this.calculeMontantsRFMBeneficiaire(csRoleMembreFamille, new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(idTiers);
            }
        }, annee);
    }

    private void chargeMontantsRFM() throws TransfertDossierException {
        try {

            // cherche la liste des tiers à prendre en compte
            boolean coupleSepare = membres.size() > 1;

            List<String> listIdTiersFamille = new ArrayList<String>();

            if (!coupleSepare) {
                DroitMembreFamilleSearch familleSearch = new DroitMembreFamilleSearch();
                familleSearch.setForIdDroit(idDroit);
                familleSearch = PegasusImplServiceLocator.getDroitMembreFamilleService().search(familleSearch);

                for (JadeAbstractModel absDonnee : familleSearch.getSearchResults()) {
                    DroitMembreFamille mbr = (DroitMembreFamille) absDonnee;
                    listIdTiersFamille.add(mbr.getMembreFamille().getPersonneEtendue().getTiers().getIdTiers());
                }
            } else {
                for (PersonneEtendueComplexModel mbr : membres.values()) {
                    listIdTiersFamille.add(mbr.getTiers().getIdTiers());
                }
            }

            // prépare la recherche les demandes
            String annee = dateSuppression.substring(3);

            // construit les conteneurs des montants RFM
            for (String csRole : membres.keySet()) {
                montantsRFM.put(csRole, new MontantsRFM());
            }

            if (coupleSepare) {
                this.calculeMontantsRFMBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, annee);
                this.calculeMontantsRFMBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, annee);
            } else {
                this.calculeMontantsRFMBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, listIdTiersFamille, annee);
            }

        } catch (Exception e) {
            throw new TransfertDossierException("An error happened while getting RFM data!", e);
        }

    }

    private String formateAdresse(Adresse ancienneAdresse) {
        if (ancienneAdresse == null) {
            return "";
        }
        AdresseSimpleModel adresse = ancienneAdresse.getAdresse();
        StringBuilder result = new StringBuilder();
        result.append(adresse.getRue()).append(" ").append(adresse.getNumeroRue());
        result.append(AbstractPegasusBuilder.NEW_LINE)
                .append(ancienneAdresse.getLocalite().getNumPostal().substring(0, 4)).append(" ")
                .append(ancienneAdresse.getLocalite().getLocalite());
        result.append(AbstractPegasusBuilder.NEW_LINE).append(adresse.getCasePostale());

        return result.toString();
    }

    private Adresse getAncienneAdresse(String csRoleMbrFam) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException, TransfertDossierException {

        AdresseSearch ancienneAdresseSearchModel = new AdresseSearch();
        ancienneAdresseSearchModel.setOrderKey(AdresseSearch.ORDER_KEY_ANTICHRONOLOGIQUE);
        ancienneAdresseSearchModel.setWhereKey(AdresseSearch.WHERE_KEY_FOR_TIERS);
        // ancienneAdresseSearchModel.setDefinedSearchSize(2);
        ancienneAdresseSearchModel.setForIdTiers(membres.get(csRoleMbrFam).getTiers().getIdTiers());
        ancienneAdresseSearchModel = TIBusinessServiceLocator.getAdresseService().searchAdresseWithSimpleTiers(
                ancienneAdresseSearchModel);

        boolean isAdresseActuelleTrouvee = false;
        for (JadeAbstractModel absdonnee : ancienneAdresseSearchModel.getSearchResults()) {
            Adresse adresse = (Adresse) absdonnee;
            if (AdresseService.CS_TYPE_DOMICILE.equals(adresse.getAvoirAdresse().getTypeAdresse())) {
                if (isAdresseActuelleTrouvee) {
                    return adresse;
                } else {
                    isAdresseActuelleTrouvee = true;
                }
            }

        }

        return null;
    }

    private String getCodeAdmin(String idTiers) throws TransfertDossierException {
        AdministrationComplexModel agence;
        try {
            agence = TIBusinessServiceLocator.getAdministrationService().read(idTiers);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("An exception happened!", e);
        } catch (JadePersistenceException e) {
            throw new TransfertDossierException("A presistence exception happened!", e);
        }
        return agence.getAdmin().getCodeAdministration();
    }

    private String getDateEntreeEMS(String csRoleMbrFam) throws TransfertDossierException {

        try {
            PersonneEtendueComplexModel membre = membres.get(csRoleMbrFam);

            TaxeJournaliereHomeEtenduSearch searchModel = new TaxeJournaliereHomeEtenduSearch();
            searchModel.setForIdTiers(membre.getTiers().getIdTiers());
            searchModel.setWhereKey("forTransfertDossierDecisionSuppression");
            searchModel.setOrderKey("orderByDateDesc");
            searchModel.setDefinedSearchSize(1);
            searchModel = PegasusImplServiceLocator.getTaxeJournaliereHomeEtenduService().search(searchModel);
            if (searchModel.getSize() == 1) {
                TaxeJournaliereHomeEtendu taxeJourn = (TaxeJournaliereHomeEtendu) searchModel.getSearchResults()[0];
                return taxeJourn.getSimpleTaxeJournaliereHome().getDateEntreeHome();
            } else {
                return "";
            }
        } catch (PegasusException e) {
            throw new TransfertDossierException("An error happened while searching taxe journaliere!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (JadePersistenceException e) {
            throw new TransfertDossierException("A persistence error happened!", e);
        }
    }

    private PCAccordee getDernierePcAccordee(String csRoleMembreFam) throws PCAccordeeException {
        for (PCAccordee pcAccordee : pcAccordees) {
            // date de suppression de la decision egale a la date de fin de la pca
            if (dateSuppression.equals(pcAccordee.getSimplePCAccordee().getDateFin())) {
                // uniquement pc octroyé (octroi et partiel)
                if (IPCValeursPlanCalcul.STATUS_OCTROI.equals(pcAccordee.getPlanRetenu().getEtatPC())
                        || IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL.equals(pcAccordee.getPlanRetenu().getEtatPC())) {
                    // Le role bénéficiaire doit etre le même que le role membre famille
                    final String csRoleBeneficiaire = pcAccordee.getSimplePCAccordee().getCsRoleBeneficiaire();
                    if (csRoleMembreFam.equals(csRoleBeneficiaire)) {
                        return pcAccordee;
                    }
                }
            }
        }
        return null;
    }

    public String getIdDecisionApresCalcul(String csRoleMembreFam) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException, TransfertDossierException {
        PCAccordee pcAccordee = dernieresPcAccordees.get(csRoleMembreFam);

        ValidationDecisionSearch searchModel = new ValidationDecisionSearch();
        // c'est la pca parent qui a ete validee par une decision apres calcul et qui contient un plan de calcul
        searchModel.setForIdPca(pcAccordee.getSimplePCAccordee().getIdPcaParent());
        searchModel = PegasusImplServiceLocator.getValidationDecisionService().search(searchModel);

        if (searchModel.getSize() == 0) {
            throw new TransfertDossierException("No ValidationDecision found for this PCAccordee!");
        }
        ValidationDecision validationDecision = (ValidationDecision) searchModel.getSearchResults()[0];
        return validationDecision.getSimpleDecisionApresCalcul().getIdDecisionApresCalcul();
    }

    private boolean isAnnexeAssures() throws TransfertDossierException {
        try {
            return "true".equals(BSessionUtil.getSessionFromThreadContext().getApplication()
                    .getProperty(EPCProperties.DECISION_TRANSFERT_ANNEXES_ASSURES.getProperty()));
        } catch (Exception e) {
            throw new TransfertDossierException("Couldn't read property!", e);
        }
    }

    private boolean isVDIntercommunal() throws TransfertDossierException {
        try {
            String codeAgenceActuelle;
            codeAgenceActuelle = BSessionUtil.getSessionFromThreadContext().getApplication()
                    .getProperty(EPCProperties.DECISION_TRANSFERT_AGENCESAVS_CODE.getProperty());

            return (IPCTransfert.listeCodeVdLausanne.contains(codeAgenceActuelle) && IPCTransfert.listeCodeVdLausanne
                    .contains(getCodeAdmin(idNouvelleCaisse)));
        } catch (Exception e) {
            throw new TransfertDossierException("Couldn't get property!", e);
        }
    }

    private String addCommaIFFrench(String formulePolitesse, String codeIsoLangue) {
        Langues langue = LanguageResolver.resolveISOCode(codeIsoLangue);
        if (Langues.Francais.equals(langue)) {
            return formulePolitesse + ",";
        } else {
            return formulePolitesse;
        }
    }

}
