package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.business.service.JournalService;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersementCompta;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PCLotTypeOperationFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOperations;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

class ComptabilisationPersister {
    private PegasusJournalConteneur conteneur;
    private Map<String, OrdreversementTiers> mapTiers;
    private SectionRubriqueResolver sectionResolver;
    private SimpleLot simpleLot;

    public ComptabilisationPersister(PegasusJournalConteneur conteneur, SimpleLot simpleLot,
            Map<String, OrdreversementTiers> mapTiers) {
        this.conteneur = conteneur;
        this.simpleLot = simpleLot;
        this.mapTiers = mapTiers;
    }

    private void createAllOperations() throws JadeNoBusinessLogSessionError,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        for (PrestationOperations presation : conteneur.getOperations()) {
            JadeThread.logInfo(
                    this.getClass().getName(),
                    "\n\rOperations pour la prestation n° " + presation.getIdPrestation() + " - "
                            + presation.getNumAvsRequerant() + "/" + presation.getNom() + " / " + presation.getPrenom()
                            + " | " + new FWCurrency(presation.getMontantPresation().toString()).toStringFormat());

            createOperations(presation);
        }
    }

    private void createEcritures(List<Ecriture> ecritures) throws ComptabiliserLotException,
            JadeNoBusinessLogSessionError {
        for (Ecriture ecriture : ecritures) {

            SectionSimpleModel section = sectionResolver.resolveSection(ecriture);
            APIRubrique rubrique = sectionResolver.resloveRubrique(ecriture.getIdRefRubrique());

            JadeThread.logInfo(this.getClass().getName(), descEriture(ecriture, rubrique, section));

            conteneur.addEcriture("", ecriture.getCodeDebitCredit(), ecriture.getCompteAnnexe().getIdCompteAnnexe(),
                    section.getIdSection(), conteneur.getDateValeur(), rubrique.getIdRubrique(), ecriture.getMontant()
                            .toString());

        }
    }

    private void createJournal() throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournalAndOperations(conteneur);
        conteneur.AddJournal(journal);
    }

    private void createOperations(PrestationOperations presation) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        createEcritures(presation.getEcritures());
        createOv(presation.getOrdresVersements());
    }

    private void createOv(List<OrdreVersementCompta> ordresVersements)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        for (OrdreVersementCompta ov : ordresVersements) {

            SectionSimpleModel section = resolveSection(ov);

            String idAressePaimentUnique = findIdAdressePaimentUnique(ov);

            JadeThread.logInfo(this.getClass().getName(), "-  OV | " + descriptionCompteAnnexe(ov.getCompteAnnexe())
                    + " | " + section.getIdExterne() + " | montant: " + ov.getMontant() + " | "
                    + descriptionAdressePaiement(ov));

            if (idAressePaimentUnique != null) {

                String refPaiement = ov.getReferencePaiement();
                if (refPaiement != null && refPaiement.length() > 140) {
                    refPaiement = refPaiement.substring(0, 140);
                }

                conteneur.addOrdreVersement(conteneur.getJournalModel().getIdJournal(), ov.getCompteAnnexe()
                        .getIdCompteAnnexe(), section.getIdSection(), idAressePaimentUnique,
                        conteneur.getDateEchance(), ov.getMontant().toString(), "CHF", "CHF",
                        APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI, refPaiement);

            } else {
                PersonneEtendueComplexModel tiers = TIBusinessServiceLocator.getPersonneEtendueService().read(
                        ov.getIdTiers());
                throw new ComptabiliserLotException("Unable to find the adresse paiement for "
                        + tiers.getPersonneEtendue().getNumAvsActuel() + " " + tiers.getTiers().getDesignation1() + " "
                        + tiers.getTiers().getDesignation2() + "(id:" + ov.getIdTiersAdressePaiement() + ")");
            }

        }
    }

    private String descEriture(Ecriture ecriture, APIRubrique rubrique, SectionSimpleModel section) {
        String dc = "C ";
        if (APIEcriture.DEBIT.equals(ecriture.getCodeDebitCredit())) {
            dc = "D ";
        }
        String id = section.getIdExterne();
        if (JadeStringUtil.isEmpty(id)) {
            id = section.getId();
        }

        return "-  E  | " + ecriture.getTypeEcriture() + " | " + descriptionCompteAnnexe(ecriture.getCompteAnnexe())
                + "| " + id + " | " + rubrique.getDescription() + " | "
                + new FWCurrency(ecriture.getMontant().toString()).toStringFormat() + " | " + dc;
    }

    private String descriptionAdressePaiement(OrdreVersementCompta ov) {
        OrdreversementTiers tiers = mapTiers.get(ov.getIdTiersAdressePaiement());
        String descriptionAdressePaiement = "idTiersAdressePaiement: " + ov.getIdTiersAdressePaiement();
        if (tiers != null) {
            descriptionAdressePaiement = "paiement pour: " + tiers.getDesignation1() + " " + tiers.getDesignation2();
        }
        return descriptionAdressePaiement;
    }

    private String descriptionCompteAnnexe(CompteAnnexeSimpleModel compteAnnexeSimple) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(compteAnnexeSimple.getIdRole()) + " "
                + compteAnnexeSimple.getIdExterneRole() + " " + compteAnnexeSimple.getDescription() + "("
                + compteAnnexeSimple.getId() + ")";
    }

    private String findIdAdressePaimentUnique(OrdreVersementCompta ov) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        AdresseTiersDetail adr = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                ov.getIdTiersAdressePaiement(), true, ov.getIdDomaineApplication(),
                conteneur.getJournalModel().getDateValeurCG(), null);
        if (adr.getFields() != null) {
            return adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE);
        }

        return null;
    }

    public PegasusJournalConteneur getConteneur() {
        return conteneur;
    }

    public PegasusJournalConteneur persit(JournalService comptaService)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        try {
            if (!IRELot.CS_ETAT_LOT_ERREUR.equals(simpleLot.getCsEtat())) {

                simpleLot = CorvusServiceLocator.getLotService().update(simpleLot);
                createJournal();
                sectionResolver = new SectionRubriqueResolver(conteneur.getJournalModel(), conteneur.getDateValeur()
                        .substring(6));

                createAllOperations();

                // creer les operations en comptat
                comptaService.createJournalAndOperations(conteneur);

                verificationMontantJournal(comptaService);
                if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                    simpleLot.setIdJournalCA(conteneur.getJournalModel().getIdJournal());
                    simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_VALIDE);

                    // after persist
                    PCLotTypeOperationFactory operation = PCLotTypeOperationFactory.csTypeOf(simpleLot.getCsTypeLot());

                    operation.afterPersist(simpleLot);
                    simpleLot = CorvusServiceLocator.getLotService().update(simpleLot);

                    // Commit obligatoire car une nouvelle transaction est ouverte en compta
                    // Cela permet d'éviter un lock
                    JadeThread.commitSession();

                    comptaService.comptabiliseSynchrone(conteneur.getJournalModel());
                }
                return conteneur;
            } else {
                simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_ERREUR);
            }
        } catch (Exception e) {
            simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_ERREUR);
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            JadeThread.logError("", errors.toString());
        } finally {
            simpleLot = CorvusServiceLocator.getLotService().update(simpleLot);
        }
        return conteneur;
    }

    /**
     * Il est possible de définir une section existante dans les ovs. Si une section est définit dans l'ov on prend
     * cette section sinon on vas recherche la section qui est lié a cette ov
     * 
     * @param ov
     * @return
     * @throws ComptabiliserLotException
     */
    private SectionSimpleModel resolveSection(OrdreVersementCompta ov) throws ComptabiliserLotException {
        if ((ov.getSimpleSection() != null) && !ov.getSimpleSection().isNew()) {
            return ov.getSimpleSection();
        } else {
            return sectionResolver.resolveSection(ov.getSection(), ov.getCompteAnnexe());
        }
    }

    private void verificationMontantJournal(JournalService comptaService) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        BigDecimal totalJournal = new BigDecimal(JANumberFormatter.deQuote(comptaService.getSommeEcritures(conteneur
                .getJournalModel())));
        // vérification du montant du journal
        BigDecimal sum = new BigDecimal(0);
        for (PrestationOperations presation : conteneur.getOperations()) {
            sum = sum.add(presation.getControlAmount());
        }

        if (sum.compareTo(totalJournal.negate()) != 0) {
            String[] param = new String[2];
            param[0] = new FWCurrency(sum.abs().toString()).toStringFormat();
            param[1] = new FWCurrency(totalJournal.abs().toString()).toStringFormat();
            JadeThread.logError(this.getClass().getName(), "pegasus.lot.comptablisation.osiris.differenceMontant",
                    param);
        }
    }
}
