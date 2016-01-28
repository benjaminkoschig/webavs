package ch.globaz.pegasus.businessimpl.utils.decision;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class AbstractValiderDecision {

    private static boolean isDateProchainPaimemtBeforeDateDecision(DecisionApresCalculSearch search, String datePP)
            throws PmtMensuelException, JadeApplicationServiceNotAvailableException {

        Integer dateMin = null;
        Integer dateTemp = 0;

        for (JadeAbstractModel model : search.getSearchResults()) {
            DecisionApresCalcul decisionApresCalcul2 = (DecisionApresCalcul) model;
            String dateDecision = decisionApresCalcul2.getDecisionHeader().getSimpleDecisionHeader().getDateDecision();
            dateTemp = Integer.valueOf(JadePersistenceUtil.parseDateToSql(dateDecision));
            if (((dateMin == null) || (dateTemp < dateMin))) {
                dateMin = dateTemp;
            }
        }

        if (dateMin > Integer.valueOf(Integer.valueOf(JadePersistenceUtil.parseDateToSql("01." + datePP)))) {
            return true;
        }
        return false;
    }

    /* lot pour la creation des prestations */
    protected SimpleLot simpleLot = null;

    protected void checkDecision(SimpleDecisionHeader simpleDecisionHeader) throws DecisionException {
        isDateDecisionCompriseEntreDernierEtProchainPaiement(simpleDecisionHeader.getDateDecision());
    }

    /**
     * Création d'un ordre de versement, gestion du modèle simple
     * 
     * @param simplePrestation
     * @param idTier
     * @param idIiersAdressePaiment
     * @param montant
     * @param csTypeDomainePcAccordee
     * @param csType
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    protected void createOrdreVersementForBen(SimplePrestation simplePrestation, String idTier,
            String idIiersAdressePaiment, BigDecimal montant, String csTypeDomainePcAccordee, String csType,
            String sousTypeGenrePrestation) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DecisionException {

        this.createOrdreVersementForBen(simplePrestation, idTier, idIiersAdressePaiment, montant.toString(),
                csTypeDomainePcAccordee, csType, sousTypeGenrePrestation);

    }

    protected void createOrdreVersementForBen(SimplePrestation simplePrestation, String idTier,
            String idIiersAdressePaiment, String montant, String csTypeDomainePcAccordee, String csType,
            String sousTypeGenrePrestation) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DecisionException {
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();

        simpleOrdreVersement.setIdPrestation(simplePrestation.getIdPrestation());
        simpleOrdreVersement.setCsTypeDomaine(csTypeDomainePcAccordee);
        simpleOrdreVersement.setCsType(csType);
        simpleOrdreVersement.setIdTiers(idTier);
        simpleOrdreVersement.setIdTiersAdressePaiement(idIiersAdressePaiment);
        simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        simpleOrdreVersement.setMontant(montant.toString());
        simpleOrdreVersement.setSousTypeGenrePrestation(sousTypeGenrePrestation);

        try {
            PegasusImplServiceLocator.getSimpleOrdreVersementService().create(simpleOrdreVersement);
        } catch (OrdreVersementException e) {
            throw new DecisionException("Unable to create ordreVersement for the beneficiare ", e);
        }
    }

    /**
     * Création d'un ordre de versement poour le bénéficiaire, ordre de versment standard
     * 
     * @param simplePrestation
     * @param decisionApresCalcul
     * @param montantDisponible
     * @param csTypePcAccordee
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    protected void createOrdreVersementForBeneficiaire(SimplePrestation simplePrestation,
            DecisionApresCalcul decisionApresCalcul, BigDecimal montantDisponible, String csTypePcAccordee)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {
        // On regarde si on est dans la cas d'un couple avec 2 rentes
        if (!JadeStringUtil.isBlankOrZero(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionConjoint())) {
            montantDisponible.setScale(0);
            BigDecimal[] montant = SplitMontant(montantDisponible);
            BigDecimal montantBeneficiaire = montant[0];
            BigDecimal montantBeneficiaireConjoint = montant[1];

            // On créer l'ov pour le bénéficiaire et on paye le dividende au bénéficiaire de la pcAccordée
            this.createOrdreVersementForBen(simplePrestation, decisionApresCalcul.getPcAccordee().getPersonneEtendue()
                    .getPersonne().getIdTiers(), decisionApresCalcul.getPcAccordee().getSimplePrestationsAccordees()
                    .getIdTiersBeneficiaire(), montantBeneficiaire, csTypePcAccordee,
                    IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, decisionApresCalcul.getPcAccordee()
                            .getSimplePrestationsAccordees().getSousCodePrestation());
            // On creér l'ov pour le conjoint
            this.createOrdreVersementForBen(simplePrestation, decisionApresCalcul.getPcAccordee()
                    .getPersonneEtendueConjoint().getPersonne().getIdTiers(), decisionApresCalcul.getPcAccordee()
                    .getSimplePrestationsAccordeesConjoint().getIdTiersBeneficiaire(), montantBeneficiaireConjoint,
                    csTypePcAccordee, IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, decisionApresCalcul
                            .getPcAccordee().getSimplePrestationsAccordees().getSousCodePrestation());

        } else {
            this.createOrdreVersementForBen(simplePrestation, decisionApresCalcul.getPcAccordee().getPersonneEtendue()
                    .getPersonne().getIdTiers(), decisionApresCalcul.getPcAccordee().getSimplePrestationsAccordees()
                    .getIdTiersBeneficiaire(), montantDisponible, csTypePcAccordee,
                    IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, decisionApresCalcul.getPcAccordee()
                            .getSimplePrestationsAccordees().getSousCodePrestation());
        }

    }

    /**
     * Check si la date de decision est comprise entre la date de dernier paiement (egal ou plus grand) et la date de
     * prochain paiement (egal ou plus petite)
     * 
     * @param dateDecision
     * @return l'état du test, comprise dans la période ddp -- date -- dpp
     * @throws DecisionException
     */
    private void isDateDecisionCompriseEntreDernierEtProchainPaiement(String dateDecision) throws DecisionException {

        String datePP;// prochain paiement
        String dateDP;// Dernier paiement
        try {
            datePP = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            dateDP = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        } catch (PmtMensuelException e) {
            throw new DecisionException("Unable to optain the date prochainPmt", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("The service for prochainPmt is not available", e);
        }

        // date après prochain paiement
        if (JadeDateUtil.isDateAfter(dateDecision, "01." + datePP)) {
            String[] date = { "01." + datePP };
            JadeThread.logError(this.getClass().getName(), "pegasus.validationDecision.dateProchainPaimentAfter", date);
        }
        if (JadeDateUtil.isDateBefore(dateDecision, "01." + dateDP)
                || JadeDateUtil.isDateBefore(dateDecision, "01." + dateDP)) {
            String[] date = { "01." + dateDP };
            JadeThread.logError(this.getClass().getName(), "pegasus.validationDecision.dateDernierPaimentBefore", date);
        }

    }

    /**
     * Set le lot ouvert, ou le cré si il ne l'est pas
     * 
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    protected void setTheOpenLot() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DecisionException {
        simpleLot = PegasusServiceLocator.getLotService().findCurrentDecisionLotOrCreate();
    }

    /**
     * Set un lot spécifique pour la décision
     * 
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    protected void setTheCreatedLot(String csTypeLot, String description)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {
        simpleLot = PegasusServiceLocator.getLotService().createLot(csTypeLot, description);
    }

    /**
     * Permet de spliter un montant en 2. Si le montant est impaire la première valeur aura un franc de plus
     * 
     * @param montantToSplit
     * @return
     */
    protected BigDecimal[] SplitMontant(BigDecimal montantToSplit) {
        BigDecimal[] list = new BigDecimal[2];
        montantToSplit.setScale(0);
        BigDecimal montantBeneficiaire = montantToSplit.divide(new BigDecimal(2), 0, RoundingMode.CEILING);
        BigDecimal montantBeneficiaireConjoint = montantToSplit.subtract(montantBeneficiaire);
        list[0] = montantBeneficiaire;
        list[1] = montantBeneficiaireConjoint;
        return list;
    }
}
