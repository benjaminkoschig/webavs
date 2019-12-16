package ch.globaz.al.businessimpl.services.paiement;

import ch.globaz.al.properties.ALProperties;
import ch.globaz.common.domaine.Montant;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.prestations.ALPaiementPrestationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.paiement.PaiementPrestationComplexModel;
import ch.globaz.al.business.models.prestation.paiement.PaiementPrestationComplexSearchModel;
import ch.globaz.al.business.paiement.CompabilisationPrestationContainer;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.paiement.PaiementContainer;
import ch.globaz.al.business.paiement.PaiementRecapitulatifBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService;
import ch.globaz.al.business.services.paiement.PaiementDirectService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.compensation.CompensationFactureServiceImpl;
import ch.globaz.al.prestations.DoublonPrestationsChecker;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.OrdreVersementComplexModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Implémentation du service permettant la gestion des paiements direct de prestations
 * 
 * @author jts
 */
public class PaiementDirectServiceImpl extends ALAbstractBusinessServiceImpl implements PaiementDirectService {

    private void ajouterEcrituresAuJournal(HashMap<String, EcritureSimpleModel> ecritures, JournalConteneur jc) {
        for (EcritureSimpleModel ecriture : ecritures.values()) {
            if ((new BigDecimal(ecriture.getMontant())).compareTo(new BigDecimal("0")) != 0) {
                jc.addEcriture(ecriture);
            }
        }
    }

    private void ajouterEcrituresRestitutionAuJournal(HashMap<String, EcritureSimpleModel> ecrituresRestitution,
            JournalConteneur jc) {
        for (EcritureSimpleModel ecriture : ecrituresRestitution.values()) {
            if ((new BigDecimal(ecriture.getMontant())).compareTo(new BigDecimal("0")) != 0) {
                jc.addEcriture(ecriture);
            }
        }
    }

    private void ajouterOrdresAuJournal(HashMap<String, OrdreVersementComplexModel> ordres, JournalConteneur jc) {

        for (OrdreVersementComplexModel ordre : ordres.values()) {
            if ((new BigDecimal(ordre.getOperation().getMontant())).compareTo(new BigDecimal("0")) > 0) {
                jc.addOrdreVersement(ordre);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService#
     * annulerPreparationPaiementDirect(java.lang.String)
     */
    @Override
    public void annulerPreparationPaiementDirect(String periodeA) throws JadePersistenceException,
            JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#annulerPreparationPaiementDirect : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        ALServiceLocator.getPrestationBusinessService().updateEtat(
                searchPrestations(periodeA, ALCSPrestation.ETAT_TR, true), ALCSPrestation.ETAT_SA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.paiement.PaiementDirectService#annulerVersement(java.lang.String)
     */
    @Override
    public void annulerVersement(String idJournal) throws JadeApplicationException, JadePersistenceException {

        HashSet<String> listeRecap = new HashSet<String>();

        EntetePrestationSearchModel search = new EntetePrestationSearchModel();
        search.setForIdJournal(idJournal);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = ALImplServiceLocator.getEntetePrestationModelService().search(search);

        for (int i = 0; i < search.getSearchResults().length; i++) {
            EntetePrestationModel prest = (EntetePrestationModel) search.getSearchResults()[i];

            prest.setDateVersComp("");
            prest.setEtatPrestation(ALCSPrestation.ETAT_SA);
            prest.setIdJournal("0");
            ALImplServiceLocator.getEntetePrestationModelService().update(prest);

            listeRecap.add(prest.getIdRecap());
        }

        // mise à jour des récap
        RecapitulatifEntrepriseModelService sRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService();

        for (String idRecap : listeRecap) {
            RecapitulatifEntrepriseModel recap = sRecap.read(idRecap);
            recap.setEtatRecap(ALCSPrestation.ETAT_SA);
            sRecap.update(recap);
        }
    }

    /**
     * Prépare la liste des prestations en fonction du résultat d'une recherche de prestations
     * 
     * @param search
     *            Résultat de la recherche
     * @return {@link ch.globaz.al.businessimpl.paiement.PaiementContainer} contenant la liste des prestations et
     *         rubriques
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private PaiementContainer buildListPrestations(PaiementPrestationComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException {

        if (search == null) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#buildListPrestations : search is null");
        }

        HashMap<String, PaiementBusinessModel> prestations = new HashMap<String, PaiementBusinessModel>();
        HashMap<String, PaiementRecapitulatifBusinessModel> rubriques = new HashMap<String, PaiementRecapitulatifBusinessModel>();
        String currentNSS = null;
        BigDecimal solde = null;
        PaiementBusinessModel prest = null;

        // boucle sur les montants (détails de prestation)
        for (int i = 0; i < search.getSize(); i++) {
            PaiementPrestationComplexModel line = (PaiementPrestationComplexModel) (search.getSearchResults()[i]);

            String key = line.getNssAllocataire() + "_" + line.getIdTiersBeneficiaire() + "_" + line.getNumeroCompte();

            prest = prestations.get(key);

            // si NSS différent que lors du passage précédent on récupère le
            // solde
            if (!line.getNssAllocataire().equals(currentNSS)) {
                CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                        line.getIdTiersAllocataire(), IntRole.ROLE_AF, line.getNssAllocataire(), true);

                solde = JadeNumericUtil.isEmptyOrZero(ca.getSolde()) ? new BigDecimal("0.00") : (new BigDecimal(
                        ca.getSolde())).negate();
            } else {
                if ((prest == null) && (solde.compareTo(new BigDecimal("0")) > 0)) {
                    solde = new BigDecimal("0.00");
                }
            }

            // compte
            if (prest == null) {

                prest = new PaiementBusinessModel(line.getIdDossier(), line.getIdRecap(),
                        line.getIdTiersBeneficiaire(), solde, line.getIdTiersAllocataire(), line.getNomAllocataire()
                                + " " + line.getPrenomAllocataire(), line.getNssAllocataire(), line.getNumeroFacture(),
                        line.getNumeroCompte(), ALCSPrestation.BONI_RESTITUTION.equals(line.getBonificationEntete()), i);

                prest.setEtatDossier(line.getEtatDossier());
                prest.setFinValidite(line.getFinValidite());

            }

            // rubrique
            PaiementRecapitulatifBusinessModel rubr = rubriques.get(line.getNumeroCompte());
            if (rubr == null) {
                rubr = new PaiementRecapitulatifBusinessModel(line.getNumeroCompte());
            }

            // mise à jour compte
            prest.addIdEntete(line.getIdEntete());
            prest.addMontant(new BigDecimal(line.getMontantDetail()));

            // si la periode de fin prestation n'est pas encore défini OU si la période définie est plus ancienne que
            // celle actuelle (line)
            if (JadeStringUtil.isEmpty(prest.getPresPeriodeA())
                    || (!JadeStringUtil.isEmpty(prest.getPresPeriodeA()) && JadeDateUtil.isDateAfter(
                            "01." + line.getPresPeriodeA(), "01." + prest.getPresPeriodeA()))) {
                prest.setPresPeriodeA(line.getPresPeriodeA());
            }

            // mise à jour rubrique
            if (JadeNumericUtil.isNumericPositif(line.getMontantDetail())) {
                rubr.addCredit(new BigDecimal(line.getMontantDetail()));
            } else {
                rubr.addDebit(new BigDecimal(line.getMontantDetail()));
            }
            rubr.addOrdreVersement(prest.getOrdreVersement());

            solde = prest.getNouveauSolde();

            // mise à jour listes
            rubriques.put(rubr.getNumeroCompte(), rubr);

            // impôt à la source

            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                && !JadeStringUtil.isBlank(line.getMontantIS())
                && !"0.00".equals(line.getMontantIS()) ) {
                BigDecimal montantIS = new BigDecimal(line.getMontantIS()).negate();
                prest.addMontantIS(montantIS);
                prest.setRubriqueIS(line.getNumeroCompteIS());
                solde = prest.getNouveauSolde();

                // rubrique
                PaiementRecapitulatifBusinessModel rubrIS = rubriques.get(line.getNumeroCompteIS());
                if (rubrIS == null) {
                    rubrIS = new PaiementRecapitulatifBusinessModel(line.getNumeroCompteIS());
                    rubriques.put(rubrIS.getNumeroCompte(), rubrIS);
                }
                // mise à jour rubrique
                if (montantIS.compareTo(BigDecimal.ZERO) > 0) {
                    rubrIS.addCredit(montantIS);
                } else {
                    rubrIS.addDebit(montantIS);
                }
                rubr.addOrdreVersement(montantIS);
            }

            prestations.put(key, prest);
            currentNSS = line.getNssAllocataire();
        }

        ArrayList<PaiementBusinessModel> listPrest = new ArrayList<PaiementBusinessModel>(prestations.values());
        Collections.sort(listPrest);

        PaiementContainer container = new PaiementContainer();
        container.setPaiementBusinessList(listPrest);
        container.setPaiementRecapitulatifBusinessList(rubriques.values());
        return container;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService# checkPrestations(java.util.Collection,
     * java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public ProtocoleLogger checkPrestations(Collection<PaiementBusinessModel> prestations, String date,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException {

        Iterator<PaiementBusinessModel> prestationIterator = prestations.iterator();

        while (prestationIterator.hasNext()) {

            PaiementBusinessModel paiement = prestationIterator.next();

            AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    paiement.getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF, date, "");

            if (adress.getFields() == null) {
                PersonneEtendueComplexModel tiersBeneficiaire = TIBusinessServiceLocator.getPersonneEtendueService()
                        .read(paiement.getIdTiersBeneficiaire());

                logger.getErrorsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, PaiementDirectServiceImpl.class
                                .getName(), "al.protocoles.erreurs.tiers.adressePaiementAbsente",
                                new String[] { tiersBeneficiaire.getTiers().getDesignation1() + " "
                                        + tiersBeneficiaire.getTiers().getDesignation2() }));
            } else {
                logger.getInfosLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.INFO, PaiementDirectServiceImpl.class
                                .getName(), "ok"));
            }

            if (ALCSDossier.ETAT_RADIE.equals(paiement.getEtatDossier())) {
                try {
                    JACalendarGregorian dateUtil = new JACalendarGregorian();
                    if (dateUtil.compare(new JADate("30." + paiement.getPresPeriodeA()),
                            new JADate(paiement.getFinValidite())) == JACalendar.COMPARE_FIRSTUPPER) {

                        // si dossier radier voir le montant total des prestations postérieures à fin de validité du
                        // dossier
                        String montantPresta = ALImplServiceLocator.getGenerationService()
                                .totalMontantPrestaGenereDossierPeriode(paiement.getIdDossier(), paiement.getIdRecap(),
                                        JadeStringUtil.substring(paiement.getFinValidite(), 3, 7));
                        if (!JadeStringUtil.isBlankOrZero(montantPresta)) {

                            logger.getWarningsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                            CompensationFactureServiceImpl.class.getName(),
                                            "al.protocoles.compensation.dossier.radie", new String[] { paiement
                                                    .getIdDossier() }));
                        }
                    }
                } catch (JAException e) {
                    throw new ALPaiementPrestationException("JADate error", e);
                }
            }

            try {
                if (DoublonPrestationsChecker.hasDoubleDetailPrestation(paiement.getIdsEntete())) {
                    logger.getWarningsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                    CompensationFactureServiceImpl.class.getName(),
                                    "al.protocoles.paiementDirect.doubleDetailsPrestation"));
                }
            } catch (SQLException ex) {
                logger.getWarningsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                                .getName(), "al.protocoles.paiementDirect.checkDoublePrestation.error"));
            }

            try {
                if (DoublonPrestationsChecker.hasDoubleEntetePrestation(paiement.getIdsEntete())) {
                    logger.getWarningsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                    CompensationFactureServiceImpl.class.getName(),
                                    "al.protocoles.paiementDirect.doubleEntetePrestation"));
                }
            } catch (SQLException ex) {
                logger.getWarningsLogger(paiement.getIdDossier(), paiement.getNomAllocataire()).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                                .getName(), "al.protocoles.paiementDirect.checkDoublePrestation.error"));
            }

        }

        return logger;
    }

    private void comptabiliserRecaps(HashSet<String> listeRecap) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {
        // mise à jour des récap
        RecapitulatifEntrepriseModelService sRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService();

        for (String idRecap : listeRecap) {
            RecapitulatifEntrepriseModel recap = sRecap.read(idRecap);
            recap.setEtatRecap(ALCSPrestation.ETAT_CO);
            sRecap.update(recap);
        }
    }

    /*
     * Effectue le traitement pour un compte annexe (après contrôle des adresses de tous les bénéficiaires)
     * et retourne le nouveau total AF
     */
    private BigDecimal genererEcrituresDuCompteAnnexe(CompteAnnexeSimpleModel ca,
            ArrayList<PaiementPrestationComplexModel> prest, JournalConteneur jc, BigDecimal totalAF,
            HashSet<String> listeRecap, HashMap<String, OrdreVersementComplexModel> ordres,
            HashMap<String, EcritureSimpleModel> ecritures, HashMap<String, EcritureSimpleModel> ecrituresRestitution)
            throws JadeApplicationException, JadePersistenceException {

        HashSet<String> idsEntete = new HashSet<String>();

        BigDecimal solde = JadeNumericUtil.isEmptyOrZero(ca.getSolde()) ? new BigDecimal("0.00") : (new BigDecimal(
                ca.getSolde())).negate();

        HashMap<String, ArrayList<PaiementPrestationComplexModel>> prestDebitCredit = groupByDebitCredit(prest);

        // Contrôle des bénéficiaires saisies avant de faire quoi que ce soit, si un des bénéficiaires n'a pas d'adresse
        // de paiement => exception remontée et traitement s'arrête
        for (PaiementPrestationComplexModel credit : prestDebitCredit.get(ALCSPrestation.BONI_DIRECT)) {
            // si il y a des versements à faire, on vérifie l'adresse de paiement
            getAdressePaiementTiers(credit.getIdTiersBeneficiaire(), jc.getJournalModel().getDateValeurCG());
        }

        // ////////////////////////////////////////////////////////////////////////////////////////////////
        // traitement des demandes de restitution
        // ////////////////////////////////////////////////////////////////////////////////////////////////

        for (PaiementPrestationComplexModel restit : prestDebitCredit.get(ALCSPrestation.BONI_RESTITUTION)) {

            String numeroFacture = restit.getNumeroFacture().substring(0, 4)
                    + APISection.CATEGORIE_SECTION_RESTITUTIONS + "000";

            BigDecimal montant = (new BigDecimal(restit.getMontantDetail()));

            totalAF = totalAF.add(montant);
            solde = solde.add(montant);
            montant = montant.negate();

            String codeDebitCredit = null;
            if (montant.compareTo(new BigDecimal("0")) < 0) {
                codeDebitCredit = APIEcriture.EXTOURNE_DEBIT;
            } else {
                codeDebitCredit = APIEcriture.DEBIT;
            }

            SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                    ca.getIdCompteAnnexe(), APISection.CATEGORIE_SECTION_RESTITUTIONS, numeroFacture,
                    jc.getJournalModel());

            String keyEcriture = codeDebitCredit + "_" + ca.getIdCompteAnnexe() + "_" + section.getId() + "_"
                    + restit.getNumeroCompte();

            EcritureSimpleModel ecriture = null;
            if (ecrituresRestitution.containsKey(keyEcriture)) {
                ecriture = ecrituresRestitution.get(keyEcriture);

                ecriture.setMontant(((new BigDecimal(ecriture.getMontant())).add(montant)).toPlainString());
                ecrituresRestitution.put(keyEcriture, ecriture);
            } else {
                ecriture = new EcritureSimpleModel();

                ecriture.setLibelle(restit.getNomAllocataire() + " " + restit.getPrenomAllocataire());
                ecriture.setCodeDebitCredit(codeDebitCredit);
                ecriture.setIdCompteAnnexe(ca.getIdCompteAnnexe());
                ecriture.setIdSection(section.getId());
                ecriture.setDate(jc.getJournalModel().getDateValeurCG());
                ecriture.setIdCompte(restit.getNumeroCompte());
                ecriture.setMontant(montant.toPlainString());

                ecrituresRestitution.put(keyEcriture, ecriture);
            }

            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                && !JadeStringUtil.isBlankOrZero(restit.getMontantIS())) {
                totalAF = totalAF.add(new BigDecimal(restit.getMontantIS()).negate());
                addEcritureImpotSource(jc, restit, ca, section, jc.getJournalModel().getDateValeurCG(), ecritures);
            }

            idsEntete.add(restit.getIdEntete());
        }

        // ////////////////////////////////////////////////////////////////////////////////////////////////
        // traitement des versements
        // ////////////////////////////////////////////////////////////////////////////////////////////////
        for (PaiementPrestationComplexModel credit : prestDebitCredit.get(ALCSPrestation.BONI_DIRECT)) {

            // si il y a des versements à faire, on vérifie l'adresse de paiement
            AdresseTiersDetail adr = getAdressePaiementTiers(credit.getIdTiersBeneficiaire(), jc.getJournalModel()
                    .getDateValeurCG());

            String numeroFacture = credit.getNumeroFacture().substring(0, 4) + APISection.CATEGORIE_SECTION_AF + "000";

            BigDecimal montant = (new BigDecimal(credit.getMontantDetail()));
            totalAF = totalAF.add(montant);
            BigDecimal montantNeg = montant.negate();

            SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                    ca.getIdCompteAnnexe(), APISection.CATEGORIE_SECTION_AF, numeroFacture, jc.getJournalModel());

            // débit/credit
            String codeDebitCredit = null;
            if (montantNeg.compareTo(new BigDecimal("0")) < 0) {
                codeDebitCredit = APIEcriture.CREDIT;
            } else {
                codeDebitCredit = APIEcriture.DEBIT;
            }

            String keyEcriture = codeDebitCredit + "_" + ca.getIdCompteAnnexe() + "_" + section.getId() + "_"
                    + credit.getNumeroCompte();
            EcritureSimpleModel ecriture = null;
            if (ecritures.containsKey(keyEcriture)) {
                ecriture = ecritures.get(keyEcriture);

                ecriture.setMontant(((new BigDecimal(ecriture.getMontant())).add(montantNeg)).toPlainString());
                ecritures.put(keyEcriture, ecriture);
            } else {
                ecriture = new EcritureSimpleModel();

                ecriture.setLibelle(credit.getNomAllocataire() + " " + credit.getPrenomAllocataire());
                ecriture.setCodeDebitCredit(codeDebitCredit);
                ecriture.setIdCompteAnnexe(ca.getIdCompteAnnexe());
                ecriture.setIdSection(section.getId());
                ecriture.setDate(jc.getJournalModel().getDateValeurCG());
                ecriture.setIdCompte(credit.getNumeroCompte());
                ecriture.setMontant(montantNeg.toPlainString());

                ecritures.put(keyEcriture, ecriture);
            }

            BigDecimal montantOrdre = new BigDecimal("0.00");
            if (solde.compareTo(new BigDecimal("0")) < 0) {
                solde = solde.add(montant);

                if (solde.compareTo(new BigDecimal("0")) > 0) {
                    montantOrdre = solde;
                }
            } else {
                montantOrdre = montant;
            }

            String keyOrdre = ca.getId() + "_" + section.getId() + "_" + credit.getIdTiersBeneficiaire();
            OrdreVersementComplexModel ordre = null;
            if (ordres.containsKey(keyOrdre)) {
                ordre = ordres.get(keyOrdre);

                ordre.getOperation().setMontant(
                        ((new BigDecimal(ordre.getOperation().getMontant())).add(montantOrdre)).toPlainString());

                ordres.put(keyOrdre, ordre);
            } else {
                ordre = new OrdreVersementComplexModel();
                ordre.getOperation().setIdJournal(jc.getJournalModel().getId());
                ordre.getOperation().setIdCompteAnnexe(ca.getId());
                ordre.getOperation().setIdSection(section.getId());
                ordre.getOperation().setDate(jc.getJournalModel().getDateValeurCG());
                ordre.getOperation().setMontant(montantOrdre.toString());

                ordre.getOrdre().setIdAdressePaiement(
                        adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE));
                ordre.getOrdre().setCodeISOMonnaieBonification("CHF");
                ordre.getOrdre().setCodeISOMonnaieDepot("CHF");
                ordre.getOrdre().setTypeVirement(APIOperationOrdreVersement.VIREMENT);
                ordre.getOrdre().setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_AF);
                ordre.getOrdre().setMotif(
                        "Allocation familiale du " + jc.getJournalModel().getDateValeurCG() + " Allocataire : "
                                + ca.getDescription());
                ordres.put(keyOrdre, ordre);
            }

            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                    && !JadeStringUtil.isBlankOrZero(credit.getMontantIS())) {
                totalAF = totalAF.add(new BigDecimal(credit.getMontantIS()).negate());
                addEcritureImpotSource(jc, credit, ca, section, jc.getJournalModel().getDateValeurCG(), ecritures);
                addOvImpotSource(ordre, credit.getMontantIS());
            }

            idsEntete.add(credit.getIdEntete());
        }

        // Traitement des en-têtes
        for (String idEntete : idsEntete) {
            // mise à jour de la prestation
            EntetePrestationModel entete = ALImplServiceLocator.getEntetePrestationModelService().read(idEntete);
            if (entete.isNew()) {
                throw new ALPaiementPrestationException(
                        "PaiementDirectServiceImpl#verserPrestations : unable to read prestation #" + idEntete
                                + ", it doesn't exist");
            }

            entete.setDateVersComp(jc.getJournalModel().getDateValeurCG());
            entete.setEtatPrestation(ALCSPrestation.ETAT_CO);
            entete.setIdJournal(jc.getJournalModel().getIdJournal());
            ALImplServiceLocator.getEntetePrestationModelService().update(entete);

            listeRecap.add(entete.getIdRecap());

            // mis en CO du décompte lié à la prestation
            ALServiceLocator.getDecompteAdiBusinessService().comptabiliserDecompteLie(entete.getIdEntete());
            // suppression des prestations de travail
            ALServiceLocator.getDecompteAdiBusinessService().supprimerPrestationTravailDossier(entete.getIdDossier(),
                    entete.getPeriodeDe(), entete.getPeriodeA());
        }

        return totalAF;

    }

    private CompabilisationPrestationContainer genererPaiement(String dateComptable,
            PaiementPrestationComplexSearchModel prestations, ProtocoleLogger logger) throws JadePersistenceException,
            JadeApplicationException {

        BigDecimal totalAF = new BigDecimal("0");
        HashSet<String> listeRecap = new HashSet<String>();
        HashMap<String, OrdreVersementComplexModel> ordres = new HashMap<String, OrdreVersementComplexModel>();
        HashMap<String, EcritureSimpleModel> ecritures = new HashMap<String, EcritureSimpleModel>();
        HashMap<String, EcritureSimpleModel> ecrituresRestitution = new HashMap<String, EcritureSimpleModel>();

        JournalConteneur journalContainer = createJournal(dateComptable);

        totalAF = genererPourChaqueCompteAnnexe(prestations, logger, totalAF, listeRecap, ordres, ecritures,
                ecrituresRestitution, journalContainer);

        comptabiliserRecaps(listeRecap);

        ajouterEcrituresRestitutionAuJournal(ecrituresRestitution, journalContainer);
        ajouterEcrituresAuJournal(ecritures, journalContainer);
        ajouterOrdresAuJournal(ordres, journalContainer);

        JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournalAndOperations(
                journalContainer);

        // vérification du montant du journal
        journal = checkMontantJournal(logger, totalAF, journal);

        return new CompabilisationPrestationContainer(journal.getIdJournal(), logger);
    }

    private JournalSimpleModel checkMontantJournal(ProtocoleLogger logger, BigDecimal totalAF,
            JournalSimpleModel journal) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        if (totalAF.compareTo((new BigDecimal(JANumberFormatter.deQuote(CABusinessServiceLocator.getJournalService()
                .getSommeEcritures(journal)))).negate()) != 0) {
            logger.getErrorsLogger("0", "Journal AF").addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, PaiementDirectServiceImpl.class.getName(),
                            "al.protocoles.erreurs.osiris.differenceMontant"));
            JadeThread.logError(PaiementDirectServiceImpl.class.getName(),
                    "al.protocoles.erreurs.osiris.differenceMontant");
        } else if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            journal = CABusinessServiceLocator.getJournalService().comptabilise(journal);

        } else {
            JadeThread.logError(PaiementDirectServiceImpl.class.getName(), "al.paiement.direct.erreurs");
        }
        return journal;
    }

    private JournalConteneur createJournal(String dateComptable) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException, ALPaiementPrestationException {
        JournalSimpleModel journal;
        journal = CABusinessServiceLocator.getJournalService().createJournal("AF ", dateComptable);
        JournalConteneur journalContainer = new JournalConteneur();
        journalContainer.AddJournal(journal);
        // Commit avant de traiter par compte annexe, car si le premier compte annexe foire, le journal ne sera pas créé
        try {
            JadeThread.commitSession();
        } catch (Exception e2) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#genererPaiement : unable to commit before processing by compte annexe",
                    e2);
        }
        return journalContainer;
    }

    private BigDecimal genererPourChaqueCompteAnnexe(PaiementPrestationComplexSearchModel prestations,
            ProtocoleLogger logger, BigDecimal totalAF, HashSet<String> listeRecap,
            HashMap<String, OrdreVersementComplexModel> ordres, HashMap<String, EcritureSimpleModel> ecritures,
            HashMap<String, EcritureSimpleModel> ecrituresRestitution, JournalConteneur jc)
            throws JadeApplicationException, ALPaiementPrestationException {

        // boucle sur les prestations regroupées par compte annexe (NSS allocataire)
        for (Entry<String, ArrayList<PaiementPrestationComplexModel>> compteAnnexe : groupByCompteAnnexe(prestations)
                .entrySet()) {
            ArrayList<PaiementPrestationComplexModel> prest = compteAnnexe.getValue();
            String idDossier = prest.get(0).getIdDossier();
            String allocDescription = prest.get(0).getNomAllocataire() + " " + prest.get(0).getPrenomAllocataire();

            try {

                // récupération du compte annexe de l'allocataire
                CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                        prest.get(0).getIdTiersAllocataire(), IntRole.ROLE_AF, prest.get(0).getNssAllocataire(), true);

                totalAF = genererEcrituresDuCompteAnnexe(ca, prest, jc, totalAF, listeRecap, ordres, ecritures,
                        ecrituresRestitution);

                JadeThread.commitSession();

            } catch (Exception e) {

                if (JadeThread.logHasMessages()) {
                    // Rollback des éventuelles opérations faites relatives au compte annexe en cours
                    try {
                        JadeThread.rollbackSession();
                    } catch (Exception e1) {
                        throw new ALPaiementPrestationException(
                                "PaiementDirectServiceImpl#genererPourChaqueCompteAnnexe : unable to rollback", e);

                    }

                    // Log de l'erreur et déplacement des prestations
                    JadeBusinessMessage[] allMessages = JadeThread.logMessages();
                    // après avoir récupérer les messages, on les efface de la transaction, car création récap ensuite
                    JadeThread.logClear();
                    for (int iMessage = 0; iMessage < allMessages.length; iMessage++) {

                        List<String> enteteMoved = new ArrayList<String>();
                        for (PaiementPrestationComplexModel prestation : prest) {
                            // si l'entete a déjà été déplacée plus besoin de le faire
                            if (!enteteMoved.contains(prestation.getIdEntete())) {
                                EntetePrestationModel enteteToMove = null;

                                try {
                                    enteteToMove = ALServiceLocator.getEntetePrestationModelService().read(
                                            prestation.getIdEntete());
                                    ALServiceLocator.getPrestationBusinessService().deplaceDansRecapOuverte(
                                            enteteToMove, true);
                                    enteteMoved.add(enteteToMove.getIdEntete());
                                    JadeBusinessMessage message = new JadeBusinessMessage(
                                            JadeBusinessMessageLevels.ERROR, PaiementDirectServiceImpl.class.getName(),
                                            allMessages[iMessage].getContents(JadeThread.currentLanguage()).concat(
                                                    JadeI18n.getInstance().getMessage(
                                                            JadeThread.currentLanguage(),
                                                            "al.protocoles.paiementDirect.prestationReportee",
                                                            new String[] { enteteToMove.getPeriodeDe(),
                                                                    enteteToMove.getPeriodeA() })));

                                    logger.getWarningsLogger(idDossier, allocDescription).addMessage(message);
                                } catch (JadePersistenceException e1) {

                                    if (enteteToMove != null) {
                                        logger.getErrorsLogger(idDossier, "Journal AF").addMessage(
                                                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                                        PaiementDirectServiceImpl.class.getName(),
                                                        "Impossible de déplacer la prestation ("
                                                                + enteteToMove.getIdEntete() + ")"));
                                    } else {
                                        logger.getErrorsLogger(idDossier, "Journal AF").addMessage(
                                                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                                        PaiementDirectServiceImpl.class.getName(),
                                                        "Impossible de déplacer la prestation concernée"));
                                    }
                                }
                            }
                        }
                    }
                    // commit pour valider le déplacement des prestations dans la récap suivante
                    try {
                        JadeThread.commitSession();
                    } catch (Exception e1) {
                        throw new ALPaiementPrestationException(
                                "PaiementDirectServiceImpl#genererPourChaqueCompteAnnexe : unable to commit (deplaceDansRecapOuverte)",
                                e);

                    }

                }

            }

        }
        return totalAF;
    }

    private AdresseTiersDetail getAdressePaiementTiers(String idTiers, String date) throws JadePersistenceException,
            JadeApplicationException, JadeNoBusinessLogSessionError {
        AdresseTiersDetail adr = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                ALCSTiers.DOMAINE_AF, date, null);
        if ((adr == null) || (adr.getFields() == null)) {

            PersonneEtendueComplexModel tiersBeneficiaire = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    idTiers);

            JadeThread.logError(PaiementDirectServiceImpl.class.getName(),
                    "al.protocoles.erreurs.tiers.adressePaiementAbsente.protocoleComptabilisation", new String[] {
                            idTiers,
                            tiersBeneficiaire.getTiers().getDesignation1() + " "
                                    + tiersBeneficiaire.getTiers().getDesignation2() });

            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#verserPrestations : impossible de créer l'ordre de versement sans adresse de paiement, veuillez consulter le protocole d'erreur de la simulation");
        }
        return adr;
    }

    /**
     * 
     * @param prestations
     * @return
     */
    private HashMap<String, ArrayList<PaiementPrestationComplexModel>> groupByCompteAnnexe(
            PaiementPrestationComplexSearchModel prestations) {

        String currentNSS = null;
        HashMap<String, ArrayList<PaiementPrestationComplexModel>> list = new HashMap<String, ArrayList<PaiementPrestationComplexModel>>();
        ArrayList<PaiementPrestationComplexModel> prestCompteAnnexe = new ArrayList<PaiementPrestationComplexModel>();

        for (JadeAbstractModel prestation : prestations.getSearchResults()) {
            PaiementPrestationComplexModel prest = (PaiementPrestationComplexModel) prestation;

            // si le NSS a changé
            if (!prest.getNssAllocataire().equals(currentNSS)) {
                if (!prestCompteAnnexe.isEmpty()) {
                    list.put(currentNSS, prestCompteAnnexe);
                    prestCompteAnnexe = new ArrayList<PaiementPrestationComplexModel>();
                }

                currentNSS = prest.getNssAllocataire();
            }

            prestCompteAnnexe.add((PaiementPrestationComplexModel) prestation);
        }

        if (!prestCompteAnnexe.isEmpty()) {
            list.put(currentNSS, prestCompteAnnexe);
        }

        return list;
    }

    private HashMap<String, ArrayList<PaiementPrestationComplexModel>> groupByDebitCredit(
            ArrayList<PaiementPrestationComplexModel> prestations) throws JadeApplicationException {

        HashMap<String, ArrayList<PaiementPrestationComplexModel>> list = new HashMap<String, ArrayList<PaiementPrestationComplexModel>>();
        list.put(ALCSPrestation.BONI_DIRECT, new ArrayList<PaiementPrestationComplexModel>());
        list.put(ALCSPrestation.BONI_RESTITUTION, new ArrayList<PaiementPrestationComplexModel>());

        for (JadeAbstractModel prestation : prestations) {
            PaiementPrestationComplexModel prest = (PaiementPrestationComplexModel) prestation;

            if (prest.getBonificationEntete().equals(ALCSPrestation.BONI_DIRECT)) {
                list.get(ALCSPrestation.BONI_DIRECT).add(prest);
            } else if (prest.getBonificationEntete().equals(ALCSPrestation.BONI_RESTITUTION)) {
                list.get(ALCSPrestation.BONI_RESTITUTION).add(prest);
            } else {
                throw new ALPaiementPrestationException(
                        "PaiementDirectServiceImpl#groupByDebitCredit : Le type de bonification "
                                + prest.getBonificationEntete() + " n'est pas supporté");
            }
        }

        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService#
     * loadPrestationsComptabilitees(java.lang.String)
     */
    @Override
    public PaiementContainer loadPrestationsComptabilisees(String idJournal) throws JadePersistenceException,
            JadeApplicationException {

        if (!JadeNumericUtil.isNumericPositif(idJournal)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#loadPrestationsComptabilitees : "
                    + idJournal + " is not valid value");
        }

        PaiementPrestationComplexSearchModel search = new PaiementPrestationComplexSearchModel();
        search.setForIdJournal(idJournal);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey("paiementDirect");
        search = (PaiementPrestationComplexSearchModel) JadePersistenceManager.search(search);

        return buildListPrestations(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService# loadPrestationsPreparees(java.lang.String)
     */
    @Override
    public PaiementPrestationComplexSearchModel loadPrestationsPreparees(String periodeA)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#loadPrestationsPreparees : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }
        // recherche les prestations a 0.-
        PaiementPrestationComplexSearchModel searchPrestationsZero = searchPrestations(periodeA,
                ALCSPrestation.ETAT_TR, true);

        ArrayList<String> listIdEnteteTraites = new ArrayList<String>();

        // chaque prestation à 0.- (non-ADI) sont mises dans une nouvelle récap
        for (int i = 0; i < searchPrestationsZero.getSize(); i++) {
            EntetePrestationModel currentEntete = ALServiceLocator.getEntetePrestationModelService().read(
                    ((PaiementPrestationComplexModel) searchPrestationsZero.getSearchResults()[i]).getIdEntete());
            if (!listIdEnteteTraites.contains(currentEntete.getId())) {
                listIdEnteteTraites.add(currentEntete.getId());
                ALServiceLocator.getPrestationBusinessService().deplaceDansRecapOuverte(currentEntete);
            }
        }
        // les autres prestations (<> 0.-) sont retournées
        return searchPrestations(periodeA, ALCSPrestation.ETAT_TR, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService# loadPrestationsPreparees(java.lang.String)
     */
    @Override
    public PaiementPrestationComplexSearchModel loadPrestationsPrepareesByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#loadPrestationsPrepareesByNumProcessus : " + idProcessus
                            + " is not a valid numProcessus");
        }

        // recherche les prestations a 0.-
        PaiementPrestationComplexSearchModel searchPrestationsZero = searchPrestationsByNumProcessus(
                ALCSPrestation.ETAT_TR, idProcessus, true);

        ArrayList<String> listIdEnteteTraites = new ArrayList<String>();

        // chaque prestation à 0.- (non-ADI) sont mises dans une nouvelle récap
        for (int i = 0; i < searchPrestationsZero.getSize(); i++) {
            EntetePrestationModel currentEntete = ALServiceLocator.getEntetePrestationModelService().read(
                    ((PaiementPrestationComplexModel) searchPrestationsZero.getSearchResults()[i]).getIdEntete());
            if (!listIdEnteteTraites.contains(currentEntete.getId())) {
                listIdEnteteTraites.add(currentEntete.getId());
                ALServiceLocator.getPrestationBusinessService().deplaceDansRecapOuverte(currentEntete);
            }
        }

        // les autres prestations (<> 0.-) sont retournées
        return searchPrestationsByNumProcessus(ALCSPrestation.ETAT_TR, idProcessus, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService# loadPrestationsSimulation(java.lang.String)
     */
    @Override
    public PaiementContainer loadPrestationsSimulation(String periodeA) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#loadPrestationsSimulation : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        return buildListPrestations(searchPrestations(periodeA, ALCSPrestation.ETAT_SA, false));
    }

    @Override
    public PaiementContainer loadPrestationsSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#loadPrestationsSimulationByNumProcessus : " + idProcessus
                            + " is not a valid number of processus");
        }

        return buildListPrestations(searchPrestationsByNumProcessus(ALCSPrestation.ETAT_SA, idProcessus, false));
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.paiement.PaiementDirectService# preparerPaiementDirect(java.lang.String)
     */
    @Override
    public void preparerPaiementDirect(String periodeA) throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#preparerPaiementDirect : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        PaiementPrestationComplexSearchModel searchNonZero = searchPrestations(periodeA, ALCSPrestation.ETAT_SA, false);
        PaiementPrestationComplexSearchModel searchZero = searchPrestations(periodeA, ALCSPrestation.ETAT_SA, true);

        ALServiceLocator.getPrestationBusinessService().updateEtat(searchNonZero, ALCSPrestation.ETAT_TR);
        ALServiceLocator.getPrestationBusinessService().updateEtat(searchZero, ALCSPrestation.ETAT_TR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.paiement.PaiementDirectService#preparerPaiementDirect(ch.globaz.al.business.models
     * .prestation.RecapitulatifEntrepriseSearchModel)
     */
    @Override
    public void preparerPaiementDirectByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException {
        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#preparerPaiementDirectByNumProcessus : "
                    + idProcessus + " is not a valid num processus");
        }

        PaiementPrestationComplexSearchModel searchNonZero = searchPrestationsByNumProcessus(ALCSPrestation.ETAT_SA,
                idProcessus, false);
        PaiementPrestationComplexSearchModel searchZero = searchPrestationsByNumProcessus(ALCSPrestation.ETAT_SA,
                idProcessus, true);

        ALServiceLocator.getPrestationBusinessService().updateEtat(searchNonZero, ALCSPrestation.ETAT_TR);
        ALServiceLocator.getPrestationBusinessService().updateEtat(searchZero, ALCSPrestation.ETAT_TR);

    }

    /**
     * Effectue une recherche de prestation pour la période passée en paramètre.
     * 
     * Les autres critères de recherche sont :
     * <ul>
     * <li>état SA</li>
     * <li>bonification de type Direct ou Restitution</li>
     * </ul>
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA). Toutes les prestations ayant une fin de période inférieur ou égale
     *            au paramètre seront récupérées
     * @param etat
     *            Etat des prestations à traiter. {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * 
     * @return Résultat de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private PaiementPrestationComplexSearchModel searchPrestations(String periodeA, String etat, boolean montantZero)
            throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#searchPrestations : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALPaiementPrestationException("PaiementDirectServiceImpl#searchPrestations : etat " + etat
                        + " is not a valid value");
            }
        } catch (Exception e) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#searchPrestations : unable to check code system etat", e);
        }

        PaiementPrestationComplexSearchModel search = new PaiementPrestationComplexSearchModel();
        search.setForPeriodeA(periodeA);
        search.setForEtat(etat);
        search.setForIdProcessusPeriodique("0");
        HashSet<String> setBoni = new HashSet<String>();
        setBoni.add(ALCSPrestation.BONI_DIRECT);
        setBoni.add(ALCSPrestation.BONI_RESTITUTION);
        search.setInBonifications(setBoni);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (montantZero) {
            search.setWhereKey("montantZeroNonAdi");
        } else {
            search.setWhereKey("montantNonZeroAndAdiZero");
        }
        search.setOrderKey("paiementDirect");
        search = (PaiementPrestationComplexSearchModel) JadePersistenceManager.search(search);

        return search;
    }

    /**
     * Effectue une recherche de prestation pour la période passée en paramètre.
     * 
     * Les autres critères de recherche sont :
     * <ul>
     * <li>état SA</li>
     * <li>bonification de type Direct ou Restitution</li>
     * </ul>
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA). Toutes les prestations ayant une fin de période inférieur ou égale
     *            au paramètre seront récupérées
     * @param etat
     *            Etat des prestations à traiter. {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * 
     * @return Résultat de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private PaiementPrestationComplexSearchModel searchPrestationsByNumProcessus(String etat, String idProcessus,
            boolean montantZero) throws JadePersistenceException, JadeApplicationException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#searchPrestationsByNumProcessus : "
                    + idProcessus + " is not a valid id of processus");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALPaiementPrestationException(
                        "PaiementDirectServiceImpl#searchPrestationsByNumProcessus : etat " + etat
                                + " is not a valid value");
            }
        } catch (Exception e) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#searchPrestationsByNumProcessus : unable to check code system etat", e);
        }

        PaiementPrestationComplexSearchModel search = new PaiementPrestationComplexSearchModel();
        search.setForEtat(etat);
        search.setForIdProcessusPeriodique(idProcessus);
        HashSet<String> setBoni = new HashSet<String>();
        setBoni.add(ALCSPrestation.BONI_DIRECT);
        setBoni.add(ALCSPrestation.BONI_RESTITUTION);
        search.setInBonifications(setBoni);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (montantZero) {
            search.setWhereKey("montantZeroNonAdi");
        } else {
            search.setWhereKey("montantNonZeroAndAdiZero");
        }
        search.setOrderKey("paiementDirect");
        search = (PaiementPrestationComplexSearchModel) JadePersistenceManager.search(search);

        return search;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.paiement.PaiementDirectService#verserPrestations(java.lang.String,
     * java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public CompabilisationPrestationContainer verserPrestations(String periodeA, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestations : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDate(dateComptable)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestations : " + dateComptable
                    + " is not a valid date");
        }

        if (logger == null) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestations : logger is null");
        }

        return genererPaiement(dateComptable, loadPrestationsPreparees(periodeA), logger);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.paiement.PaiementDirectService#verserPrestations(ch.globaz.al.business.models.
     * prestation.RecapitulatifEntrepriseSearchModel, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public CompabilisationPrestationContainer verserPrestationsByNumProcessus(String idProcessus, String dateComptable,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestationsByNumProcessus : "
                    + idProcessus + " is not a valid num processus");
        }

        if (!JadeDateUtil.isGlobazDate(dateComptable)) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestations : " + dateComptable
                    + " is not a valid date");
        }

        if (logger == null) {
            throw new ALPaiementPrestationException("PaiementDirectServiceImpl#verserPrestations : logger is null");
        }

        return genererPaiement(dateComptable, loadPrestationsPrepareesByNumProcessus(idProcessus), logger);
    }

    private void addEcritureImpotSource(JournalConteneur jc, PaiementPrestationComplexModel prest, CompteAnnexeSimpleModel compteAnnexe,
                                        SectionSimpleModel section, String date, HashMap<String,EcritureSimpleModel> ecritures) {

        String codeDebitCredit;
        if (new Montant(prest.getMontantIS()).isPositive()) {
            codeDebitCredit = APIEcriture.DEBIT;
        } else {
            codeDebitCredit = APIEcriture.CREDIT;
        }

        String keyEcriture = codeDebitCredit + "_" + compteAnnexe.getIdCompteAnnexe() + "_" + section.getId() + "_"
                + prest.getNumeroCompteIS();
        EcritureSimpleModel ecriture = null;
        if (ecritures.containsKey(keyEcriture)) {
            ecriture = ecritures.get(keyEcriture);
            BigDecimal montantIS = new BigDecimal(prest.getMontantIS());
            ecriture.setMontant(((new BigDecimal(ecriture.getMontant())).add(montantIS)).toPlainString());
            ecritures.put(keyEcriture, ecriture);
        } else {
            ecriture = new EcritureSimpleModel();
            ecriture.setCodeDebitCredit(codeDebitCredit);
            ecriture.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            ecriture.setIdSection(section.getIdSection());
            ecriture.setDate(date);
            ecriture.setIdCompte(prest.getNumeroCompteIS());
            ecriture.setMontant(prest.getMontantIS());
            ecritures.put(keyEcriture, ecriture);
        }

    }

    private void addOvImpotSource(OrdreVersementComplexModel ordre, String montant) {
        BigDecimal montantIS = new BigDecimal(montant).negate();
        BigDecimal montantOv = new BigDecimal(ordre.getOperation().getMontant()).add(montantIS);
        ordre.getOperation().setMontant(montantOv.toPlainString());
    }
}