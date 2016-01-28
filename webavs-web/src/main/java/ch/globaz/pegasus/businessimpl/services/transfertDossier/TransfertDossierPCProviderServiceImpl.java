package ch.globaz.pegasus.businessimpl.services.transfertDossier;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BApplication;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.process.annonce.transfertdossier.PCDemandeTransfertDossierProcess;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCTransfert;
import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppressionSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertDossierPCProviderService;
import ch.globaz.pegasus.businessimpl.checkers.decision.SimpleDecisionSuppressionChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.SingleTransfertPCAbstractBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.TransfertDossierAbstractBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.TransfertDossierSuppressionPCBuilder;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.Adresse;
import ch.globaz.pyxis.business.model.AdresseSearch;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class TransfertDossierPCProviderServiceImpl extends AbstractPegasusBuilder implements
        TransfertDossierPCProviderService {

    @Override
    public void checkProcessArguments(String idDernierDomicileLegal, String idNouvelleCaisse, String[] copies)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, TransfertDossierException {
        // Verification du dernier domicile légal
        try {
            LocaliteSimpleModel derniereLocalite = TIBusinessServiceLocator.getAdresseService().readLocalite(
                    idDernierDomicileLegal);
            if ((derniereLocalite == null) || JadeStringUtil.isEmpty(derniereLocalite.getLocalite())) {
                this.logError("pegasus.process.transfertDossier.derniereLocalite.integrity");
            }
        } catch (JadeApplicationException e) {
            this.logError("pegasus.process.transfertDossier.derniereLocalite.integrity");
        }

        // verification de la nouvelle caisse
        try {
            AdresseTiersDetail nouvelleCaisseDetail = PegasusUtil.getAdresseCascadeByType(idNouvelleCaisse,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);
            if ((nouvelleCaisseDetail == null) || JadeStringUtil.isEmpty(nouvelleCaisseDetail.getAdresseFormate())) {
                this.logError("pegasus.process.transfertDossier.nouvelleCaisse.integrity");
            }
        } catch (JadeApplicationException e) {
            this.logError("pegasus.process.transfertDossier.nouvelleCaisse.integrity");
        }

        // verification des copies et de leur adresse
        for (String idTiers : copies) {

            try {
                AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(idTiers,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

                if ((adresseTiersDetail == null) || JadeStringUtil.isEmpty(adresseTiersDetail.getAdresseFormate())) {

                    // tente de récupérer la designation du idtiers
                    PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(
                            idTiers);
                    if ((personne == null) || JadeStringUtil.isEmpty(personne.getTiers().getDesignation1())) {
                        AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService().read(
                                idTiers);
                        if ((admin == null) || JadeStringUtil.isEmpty(admin.getTiers().getDesignation1())) {
                            this.logError("pegasus.process.transfertDossier.copieTiers.integrity", idTiers);
                        }
                        this.logError("pegasus.process.transfertDossier.copieTiers.adresseAdmin.integrity",
                                PegasusUtil.formatNomPrenom(admin.getTiers()));
                    } else {
                        this.logError("pegasus.process.transfertDossier.copieTiers.adressePersonne.integrity",
                                PegasusUtil.formatNomPrenom(personne.getTiers()));
                    }
                }

            } catch (JadeApplicationException e) {
                this.logError("pegasus.process.transfertDossier.copieTiers.integrity", idTiers);
            }
        }
    }

    @Override
    public SimpleTransfertDossierSuppression createTransfertDossierSuppression(DecisionSuppression decisionSuppression,
            String idNouvelleCaisse, String csMotifTransfert) throws TransfertDossierException,
            JadePersistenceException {

        // Check si params transfert ok
        SimpleDecisionSuppressionChecker.checkParametersForTransfert(csMotifTransfert, idNouvelleCaisse);

        try {
            SimpleTransfertDossierSuppression transfert = new SimpleTransfertDossierSuppression();
            transfert.setIdDecisionHeader(decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdDecisionHeader());
            transfert.setIdNouvelleCaisse(idNouvelleCaisse);

            // prégénération des motifs
            // // get catalogue Babel
            final ICTDocument babelDoc;
            Map<Langues, CTDocumentImpl> documentsBabel;
            try {
                documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTransfertDossierPC(
                        TransfertDossierBuilderType.DEMANDE_EN_COURS);
                // récupérer l'énuméré de la langue en fonction du codeISO
                Langues langueTiers = Langues.getLangueDepuisCodeIso(decisionSuppression.getDecisionHeader()
                        .getPersonneEtendue().getTiers().getLangue());

                if (null == langueTiers) {
                    langueTiers = Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext()
                            .getUserInfo().getLanguage());
                }

                babelDoc = documentsBabel.get(langueTiers);

            } catch (Exception e) {
                throw new TransfertDossierException("Error while loading catalogue Babel!", e);
            }

            String idTiersBeneficiaire = decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdTiersBeneficiaire();

            String dateNouvelleCaisse = decisionSuppression.getSimpleDecisionSuppression().getDateSuppression();

            dateNouvelleCaisse = JadeDateUtil.addMonths("01." + dateNouvelleCaisse, 1).substring(3);

            String motifContact = getMotifContactDecisionSuppression(idNouvelleCaisse, babelDoc, dateNouvelleCaisse);
            String motifTransfert = getMotifTransfertDecisionSuppression(csMotifTransfert, babelDoc,
                    idTiersBeneficiaire, idNouvelleCaisse);
            transfert.setTextMotifTransfert(motifTransfert);
            transfert.setTextMotifContact(motifContact);

            transfert = PegasusImplServiceLocator.getSimpleTransfertDossierSuppression().create(transfert);

            // ajout des copies et annexes par défaut de la décision *******************************************

            Boolean isCopieAnnexeAssures;
            try {
                isCopieAnnexeAssures = Boolean.parseBoolean(BSessionUtil.getSessionFromThreadContext().getApplication()
                        .getProperty(EPCProperties.DECISION_TRANSFERT_ANNEXES_ASSURES.getPropertyName()));
            } catch (Exception e) {
                throw new TransfertDossierException("Couldn't read property!", e);
            }
            if (isCopieAnnexeAssures) {
                List<String> annexes = new ArrayList<String>();

                if (isVDIntercommunal(idNouvelleCaisse)) {
                    annexes.add("PREP_DECISION_SUPPR_P_TRANSFERT_FICHE_VD_LAUSANNE");
                } else {
                    annexes.add("PREP_DECISION_SUPPR_P_TRANSFERT_ANNEXE_FEDERALE");
                }

                for (String labelAnnexe : annexes) {
                    SimpleAnnexesDecision annexe = new SimpleAnnexesDecision();
                    annexe.setIdDecisionHeader(decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                            .getIdDecisionHeader());
                    annexe.setCsType(IPCDecision.ANNEXE_COPIE_NON_EDITABLE);
                    annexe.setValeur(BSessionUtil.getSessionFromThreadContext().getLabel(labelAnnexe));
                    PegasusImplServiceLocator.getSimpleAnnexesDecisionService().create(annexe);
                }
            }

            // copies
            List<String> copies = new ArrayList<String>();
            copies.add(idNouvelleCaisse);

            for (String copie : copies) {
                SimpleCopiesDecision simpleCopiesDecision = new SimpleCopiesDecision();
                simpleCopiesDecision.setIdTiersCopie(copie);
                simpleCopiesDecision.setIdDecisionHeader(decisionSuppression.getDecisionHeader()
                        .getSimpleDecisionHeader().getIdDecisionHeader());

                simpleCopiesDecision.setAnnexes(false);
                simpleCopiesDecision.setCopies(false);
                simpleCopiesDecision.setMoyensDeDroit(false);
                simpleCopiesDecision.setPageDeGarde(false);
                simpleCopiesDecision.setPlandeCalcul(false);
                simpleCopiesDecision.setRecapitulatif(false);
                simpleCopiesDecision.setRemarque(false);
                simpleCopiesDecision.setSignature(false);
                simpleCopiesDecision.setVersementA(false);

                PegasusImplServiceLocator.getSimpleCopiesDecisionsService().create(simpleCopiesDecision);

            }

            return transfert;
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        } catch (DecisionException e) {
            throw new TransfertDossierException("An error happened while creating copies and annexes!", e);
        } catch (JadeApplicationException e) {
            throw new TransfertDossierException("An exception was raised while generating motif's texts!", e);
        }

    }

    @Override
    public void genereDocument(TransfertDossierSuppression transfert, DecisionSuppression decision, String mailGest)
            throws TransfertDossierException {

        try {
            if ((decision.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar() == null)
                    || JadeStringUtil.isBlankOrZero(decision.getDecisionHeader().getSimpleDecisionHeader()
                            .getPreparationPar())) {
                JadeThread.logError(TransfertDossierPCProviderServiceImpl.class.toString(),
                        "pegasus.simpleDecisionHeader.preparationPar.integrity");
            }

            PCDemandeTransfertDossierProcess process = new PCDemandeTransfertDossierProcess();
            process.setSession(getSession());

            // configure les champs
            String idDemandePc = decision.getVersionDroit().getDemande().getSimpleDemande().getIdDemande();
            process.setIdDemandePc(idDemandePc);
            process.setTypeBuilder(TransfertDossierBuilderType.DEMANDE_EN_COURS);

            Map<String, String> params = process.getParametres();
            params.put(TransfertDossierAbstractBuilder.ID_DEMANDE_PC, idDemandePc);
            params.put(TransfertDossierAbstractBuilder.ID_GESTIONNAIRE, decision.getDecisionHeader()
                    .getSimpleDecisionHeader().getPreparationPar());
            params.put(TransfertDossierAbstractBuilder.MAIL_GEST, mailGest);
            params.put(TransfertDossierAbstractBuilder.ID_NOUVELLE_CAISSE, transfert
                    .getSimpleTransfertDossierSuppression().getIdNouvelleCaisse());
            params.put(TransfertDossierSuppressionPCBuilder.MOTIF_TRANSFERT, transfert
                    .getSimpleTransfertDossierSuppression().getTextMotifTransfert());
            params.put(TransfertDossierSuppressionPCBuilder.ID_DROIT, decision.getVersionDroit().getSimpleDroit()
                    .getIdDroit());
            params.put(TransfertDossierSuppressionPCBuilder.NO_VERSION, decision.getVersionDroit()
                    .getSimpleVersionDroit().getNoVersion());

            params.put(TransfertDossierSuppressionPCBuilder.MOTIF_CONTACT, transfert
                    .getSimpleTransfertDossierSuppression().getTextMotifContact());
            params.put(TransfertDossierSuppressionPCBuilder.DATE_DECISION, decision.getDecisionHeader()
                    .getSimpleDecisionHeader().getDateDecision());
            params.put(TransfertDossierSuppressionPCBuilder.DATE_SUPPRESSION, decision.getSimpleDecisionSuppression()
                    .getDateSuppression());

            // annexes
            List<String> annexes = new ArrayList<String>();
            for (SimpleAnnexesDecision annexe : decision.getDecisionHeader().getListeAnnexes()) {
                annexes.add(annexe.getValeur());
            }
            process.setAnnexes(annexes);

            // copies
            List<String> copies = new ArrayList<String>();
            for (CopiesDecision copie : decision.getDecisionHeader().getListeCopies()) {
                copies.add(copie.getSimpleCopiesDecision().getIdTiersCopie());
            }
            process.setCopies(copies);

            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            throw new TransfertDossierException("Unable to start process!", e);
        }
    }

    private Adresse getAncienneAdresse(String idTiers) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException, TransfertDossierException {
        AdresseSearch ancienneAdresseSearchModel = new AdresseSearch();
        ancienneAdresseSearchModel.setOrderKey(AdresseSearch.ORDER_KEY_ANTICHRONOLOGIQUE);
        ancienneAdresseSearchModel.setWhereKey(AdresseSearch.WHERE_KEY_FOR_TIERS);
        ancienneAdresseSearchModel.setDefinedSearchSize(2);
        ancienneAdresseSearchModel.setForIdTiers(idTiers);
        ancienneAdresseSearchModel = TIBusinessServiceLocator.getAdresseService().searchAdresseWithSimpleTiers(
                ancienneAdresseSearchModel);
        if (ancienneAdresseSearchModel.getSize() < 2) {
            return null;
        }
        return (Adresse) ancienneAdresseSearchModel.getSearchResults()[1];
    }

    private BApplication getApplication() throws TransfertDossierException {
        try {
            return BSessionUtil.getSessionFromThreadContext().getApplication();
        } catch (Exception e) {
            throw new TransfertDossierException("Couldn't get application!", e);
        }
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

    private String getMotifContactDecisionSuppression(String idNouvelleCaisse, final ICTDocument babelDoc,
            CharSequence dateNouvelleCaisse) throws JadeApplicationException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        int niveauBabelMotifContact;
        if (isVDIntercommunal(idNouvelleCaisse)) {
            niveauBabelMotifContact = 10;
        } else {
            niveauBabelMotifContact = 1;
        }

        // // recherche de l'agence
        AdministrationComplexModel nouvelleAgence = TIBusinessServiceLocator.getAdministrationService().read(
                idNouvelleCaisse);
        String nomAgence = (nouvelleAgence.getTiers().getDesignation1() + " " + nouvelleAgence.getTiers()
                .getDesignation2()).trim();
        String canton = getSession().getCodeLibelle(nouvelleAgence.getAdmin().getCanton());

        return babelDoc.getTextes(niveauBabelMotifContact).getTexte(4).getDescription().replace("{CONTACT}", nomAgence)
                .replace("{CANTON}", canton).replace("{DATE_NOUVELLE_CAISSE}", dateNouvelleCaisse);

    }

    private String getMotifTransfertDecisionSuppression(String csMotifTransfert, final ICTDocument babelDoc,
            String idTiersBeneficiaire, String idNouvelleCaisse) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException, TransfertDossierException {
        AdresseTiersDetail derniereAdresse = PegasusUtil.getAdresseCascadeByType(idTiersBeneficiaire,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        int niveauBabelMotif;
        if (isVDIntercommunal(idNouvelleCaisse)) {
            niveauBabelMotif = 12;
        } else {
            niveauBabelMotif = 3;
        }

        String motifTransfert = null;
        if (IPCDecision.CS_MOTIF_TRANSFERT_SUPPRESSION_MAINLEVEE_TUTELLE.equals(csMotifTransfert)) {
            motifTransfert = babelDoc.getTextes(niveauBabelMotif).getTexte(1).getDescription();
        } else if (IPCDecision.CS_MOTIF_TRANSFERT_SUPPRESSION_NOMINATION_NOUVEAU_TUTEUR.equals(csMotifTransfert)) {
            motifTransfert = babelDoc.getTextes(niveauBabelMotif).getTexte(2).getDescription();
        } else {
            motifTransfert = babelDoc.getTextes(niveauBabelMotif).getTexte(3).getDescription();

            // recherche de la localité du nouveau domicile
            String localite = derniereAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
            if (localite == null) {
                localite = "";
            }
            Adresse ancienneAdresse = getAncienneAdresse(idTiersBeneficiaire);
            String ancienneLocalite = "";
            if (ancienneAdresse != null) {
                ancienneLocalite = ancienneAdresse.getLocalite().getLocalite();
            }

            motifTransfert = motifTransfert.replace("{ANCIEN_DOMICILE}", ancienneLocalite).replace(
                    "{NOUVEAU_DOMICILE}", localite);
        }
        return motifTransfert;
    }

    private boolean isVDIntercommunal(String idNouvelleCaisse) throws TransfertDossierException {
        try {
            String codeAgenceActuelle;
            codeAgenceActuelle = BSessionUtil.getSessionFromThreadContext().getApplication()
                    .getProperty(EPCProperties.DECISION_TRANSFERT_AGENCESAVS_CODE.getPropertyName());

            return (IPCTransfert.listeCodeVdLausanne.contains(codeAgenceActuelle) && IPCTransfert.listeCodeVdLausanne
                    .contains(getCodeAdmin(idNouvelleCaisse)));
        } catch (Exception e) {
            throw new TransfertDossierException("Couldn't get property!", e);
        }
    }

    private void logError(String message) {
        JadeThread.logError(TransfertDossierPCProviderServiceImpl.class.getName(), message);
    }

    private void logError(String message, String... parametres) {
        JadeThread.logError(TransfertDossierPCProviderServiceImpl.class.getName(), message, parametres);
    }

    @Override
    public TransfertDossierSuppression readTransfertByIdDecisionHeader(String idDecisionHeader)
            throws TransfertDossierException, JadePersistenceException {
        try {
            TransfertDossierSuppressionSearch searchModel = new TransfertDossierSuppressionSearch();
            searchModel.setForIdDecisionHeader(idDecisionHeader);
            searchModel = PegasusServiceLocator.getTransfertDossierSuppressionService().search(searchModel);
            if (searchModel.getSize() == 1) {
                return (TransfertDossierSuppression) searchModel.getSearchResults()[0];
            } else if (searchModel.getSize() == 0) {
                throw new TransfertDossierException("TransfertDossierSuppression not found!");
            } else {
                throw new TransfertDossierException(searchModel.getSize()
                        + " TransfertDossierSuppression duplicates found! Expected only one!");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TransfertDossierException("Service not available!", e);
        }
    }

}
