package ch.globaz.auriga.businessimpl.services;

import globaz.auriga.print.AUDecisionCap_Doc;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.properties.AFProperties;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaDateUtils;
import ch.globaz.auriga.business.beans.decisioncap.DecisionCAPBean;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.constantes.AUDecisionType;
import ch.globaz.auriga.business.constantes.AUParametrePlageValeur;
import ch.globaz.auriga.business.constantes.AUSortieEtat;
import ch.globaz.auriga.business.exceptions.AurigaException;
import ch.globaz.auriga.business.models.ComplexDecisionCAPAffiliationCotisation;
import ch.globaz.auriga.business.models.ComplexDecisionCAPAffiliationCotisationSearchModel;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiers;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCapWidgetSearchModel;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliationSearchModel;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.models.SimpleEnfantDecisionCAP;
import ch.globaz.auriga.business.models.SimpleSortieCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.auriga.business.services.DecisionCAPService;
import ch.globaz.auriga.businessimpl.checkers.RenouvellementDecisionCAPChecker;
import ch.globaz.auriga.businessimpl.checkers.SimpleDecisionCapChecker;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;
import ch.globaz.common.businessimpl.models.UnitTaskResultState;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;
import ch.globaz.naos.business.model.AdhesionCotisationAssuranceSearchComplexModel;
import ch.globaz.naos.business.model.AffiliationAssuranceComplexModel;
import ch.globaz.naos.business.model.AffiliationAssuranceSearchComplexModel;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class DecisionCAPServiceImpl implements DecisionCAPService {

    private void calculerCotisation(SimpleDecisionCAP decisionCap) throws Exception, AurigaException {
        // calcul de la cotisation en fonction du type d'assurance
        // On utilise l'idAssurance sélectionné par l'utilisateur
        AssuranceSimpleModel assurance = AFBusinessServiceLocator.getAssuranceService().read(
                decisionCap.getIdAssurance());
        AFTauxAssurance taux = findTauxAssurance(assurance.getAssuranceId(), decisionCap.getAnnee());
        FWCurrency cotisationAnnuelle;

        // si assurance de type CAP10 (tient compte des allocations familiales)
        if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_10)) {
            FWCurrency revenu;

            // set du taux
            decisionCap.setTauxAssurance(taux.getValeurEmployeur());

            // si on a un revenu FRV on le prend en priorité sinon on prend le revenu IFD
            if (!JadeStringUtil.isBlankOrZero(decisionCap.getRevenuFRV())) {
                revenu = new FWCurrency(decisionCap.getRevenuFRV());
            } else {
                revenu = new FWCurrency(decisionCap.getRevenuIFD());
            }

            cotisationAnnuelle = new FWCurrency(revenu.doubleValue()
                    * (Double.valueOf(taux.getValeurEmployeur()) / Double.valueOf(taux.getFraction())));

            // recherche des plafonds et des planchers de l'assurance
            AFParametreAssurance paramAssurancePlancher = findParamAssurance(assurance.getAssuranceId(),
                    CodeSystem.GEN_PARAM_ASS_PLANCHER, decisionCap.getAnnee());
            AFParametreAssurance paramAssurancePlafond = findParamAssurance(assurance.getAssuranceId(),
                    CodeSystem.GEN_PARAM_ASS_PLAFOND, decisionCap.getAnnee());
            FWCurrency plancher = new FWCurrency(paramAssurancePlancher.getValeur());
            FWCurrency plafond = new FWCurrency(paramAssurancePlafond.getValeur());

            if (cotisationAnnuelle.doubleValue() < plancher.doubleValue()) {
                cotisationAnnuelle = new FWCurrency(plancher.doubleValue());
            } else if (cotisationAnnuelle.doubleValue() > plafond.doubleValue()) {
                cotisationAnnuelle = new FWCurrency(plafond.doubleValue());
            }

            // set de la cotisation brute (avant prise en compte des prestations)
            decisionCap.setCotisationBrute(cotisationAnnuelle.toStringFormat());

            // set des prestations
            FWCurrency montantAfTotal = calculerMontantAfTotal(decisionCap.getIdDecision());
            decisionCap.setPrestation(montantAfTotal.toStringFormat());

        }

        // si assurance de type CAP20 (tient compte des allocations familiales)
        else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_20)) {
            // set du forfait
            decisionCap.setForfait(taux.getValeurEmployeur());

            // calcul cotisation annuelle
            cotisationAnnuelle = new FWCurrency(taux.getValeurEmployeur());

            // set de la cotisation brute (avant prise en compte des prestations)
            decisionCap.setCotisationBrute(cotisationAnnuelle.toStringFormat());

            // set des prestations
            FWCurrency montantAfTotal = calculerMontantAfTotal(decisionCap.getIdDecision());
            decisionCap.setPrestation(montantAfTotal.toStringFormat());
        }

        // si assurance de type CAP30->50
        else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_30)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_31)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_40)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_41)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CAP_50)) {
            // set du forfait
            decisionCap.setForfait(taux.getValeurEmployeur());

            // set des prestations et de la cotisation brute à 0
            decisionCap.setPrestation("0");
            decisionCap.setCotisationBrute(taux.getValeurEmployeur());

            // calcul cotisation annuelle
            cotisationAnnuelle = new FWCurrency(taux.getValeurEmployeur());
        }

        // si aucune assurance ne correspond on lève une exception
        else {
            throw new AurigaException("le type d'assurance ne correspond à aucun type connu");
        }

        FWCurrency montantAFMensuelArrondi = getMontantAFMensuelArrondiDecisionCAP(decisionCap);

        // calcul de la cotisationAnnuelle arrondie
        FWCurrency cotisationAnnuelleArrondie = new FWCurrency(JANumberFormatter.round(
                cotisationAnnuelle.doubleValue(), 0.05, 2, JANumberFormatter.INF));
        FWCurrency cotisationAnnuelleArrondieWithAF = new FWCurrency(cotisationAnnuelleArrondie.doubleValue()
                - (montantAFMensuelArrondi.doubleValue() * 12));

        // calcul de la cotisationTrimestrielle arrondie
        FWCurrency cotisationTrimestrielleArrondie = new FWCurrency(JANumberFormatter.round(
                cotisationAnnuelle.doubleValue() / 4, 0.05, 2, JANumberFormatter.INF));
        FWCurrency cotisationTrimestrielleArrondieWithAF = new FWCurrency(cotisationTrimestrielleArrondie.doubleValue()
                - (montantAFMensuelArrondi.doubleValue() * 3));

        FWCurrency cotisationMensuelleArrondie = new FWCurrency(JANumberFormatter.round(
                cotisationAnnuelle.doubleValue() / 12, 0.05, 2, JANumberFormatter.INF));
        FWCurrency cotisationMensuelleArrondieWithAF = new FWCurrency(cotisationMensuelleArrondie.doubleValue()
                - montantAFMensuelArrondi.doubleValue());

        // calcul de la cotisationPeriode arrondie et calcul de la cotisation brute
        // calculer le nombre de mois que représente la période saisie
        int nbMoisPeriode = AriesAurigaDateUtils.getNombreMoisEntreDates(decisionCap.getDateDebut(),
                decisionCap.getDateFin());

        FWCurrency cotisationBrute = null;
        // Attention ! Les affectations ci-dessous font que cotisationBrute et respectivement
        // cotisationAnnuelleArrondie ou cotisationTrimestrielleArrondie ou cotisationMensuelleArrondie ont la
        // mêmme référence mémoire
        if (nbMoisPeriode == 12) {
            cotisationBrute = cotisationAnnuelleArrondie;
        } else if (nbMoisPeriode == 3) {
            cotisationBrute = cotisationTrimestrielleArrondie;
        } else if (nbMoisPeriode == 1) {
            cotisationBrute = cotisationMensuelleArrondie;
        } else {
            cotisationBrute = new FWCurrency(cotisationMensuelleArrondie.doubleValue() * nbMoisPeriode);

        }

        FWCurrency cotisationPeriodeArrondieWithAF = new FWCurrency(cotisationBrute.doubleValue()
                - new FWCurrency(decisionCap.getPrestation()).doubleValue());

        // set des cotisations dans la décision
        decisionCap.setCotisationAnnuelle(cotisationAnnuelleArrondieWithAF.toStringFormat());
        decisionCap.setCotisationTrimestrielle(cotisationTrimestrielleArrondieWithAF.toStringFormat());
        decisionCap.setCotisationMensuelle(cotisationMensuelleArrondieWithAF.toStringFormat());
        decisionCap.setCotisationPeriode(cotisationPeriodeArrondieWithAF.toStringFormat());
        decisionCap.setCotisationBrute(cotisationBrute.toStringFormat());
        decisionCap.setCategorie(assurance.getTypeAssurance());

    }

    /**
     * Calcul le montant AF en fonction de l'année de naissance de l'enfant et de l'année de la décision. Proratise en
     * fonction de la date de début et la date de fin
     * 
     * @param anneeDecision
     * @param anneeNaissance
     * @return
     * @throws Exception
     * @throws
     */
    private FWCurrency calculerMontantAf(int anneeDecision, String dateNaissance, String dateDeces,
            String dateDebutPeriode, String dateFinPeriode) throws Exception {

        int anneeNaissance = JACalendar.getYear(dateNaissance);

        String dateDebutDroit = dateDebutPeriode;
        if (JadeDateUtil.isDateAfter(dateNaissance, dateDebutDroit)) {
            dateDebutDroit = dateNaissance;
        }

        String dateFinDroit = dateFinPeriode;
        if (!JadeStringUtil.isBlankOrZero(dateDeces) && JadeDateUtil.isGlobazDate(dateDeces)
                && JadeDateUtil.isDateBefore(dateDeces, dateFinDroit)) {
            dateFinDroit = dateDeces;
        }

        FWCurrency montantAf_00_16 = AurigaServiceLocator.getParametreService().getValeurNumeriqueFWCurrency(
                AUParametrePlageValeur.ALLOCATION_00_16, dateDebutPeriode);
        FWCurrency montantAf_17_20 = AurigaServiceLocator.getParametreService().getValeurNumeriqueFWCurrency(
                AUParametrePlageValeur.ALLOCATION_17_20, dateDebutPeriode);

        FWCurrency montantAf;
        int age = anneeDecision - anneeNaissance;

        int nbMoisDroit = AriesAurigaDateUtils.getNombreMoisEntreDates(dateDebutDroit, dateFinDroit);

        // suite à un changement de spécifications tardif
        // le test a été modifié de 0 à 15 et 16 à 20 mais le nom des variables et des propriétés
        // n'a pas été changé
        // si age entre 0-15
        if ((age >= 0) && (age <= 15)) {
            montantAf = new FWCurrency(JANumberFormatter.round((montantAf_00_16.doubleValue() / 12) * nbMoisDroit,
                    0.05, 2, JANumberFormatter.SUP));
        }
        // si age entre 16-20
        else if ((age >= 16) && (age <= 20)) {
            montantAf = new FWCurrency(JANumberFormatter.round((montantAf_17_20.doubleValue() / 12) * nbMoisDroit,
                    0.05, 2, JANumberFormatter.SUP));
        } else {
            montantAf = new FWCurrency(0);
        }

        return montantAf;
    }

    /**
     * Calcul le montant AF total en prenant en compte tous les enfants enregistrés
     * 
     * @param idDecision
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private FWCurrency calculerMontantAfTotal(String idDecision) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        FWCurrency montantAfTotal = new FWCurrency("0");
        List<SimpleEnfantDecisionCAP> listEnfantDecisionCap = AurigaServiceLocator.getEnfantDecisionCAPService()
                .getListEnfantDecisionCap(idDecision);

        // parcours de tous les enfants et addition du montant
        for (SimpleEnfantDecisionCAP enfant : listEnfantDecisionCap) {
            montantAfTotal.add(enfant.getMontant());
        }

        return montantAfTotal;
    }

    @Override
    public SimpleDecisionCAP changeEtatDecision(String idDecision, AUDecisionEtat nouvelEtat)
            throws JadePersistenceException {
        SimpleDecisionCAP decision = read(idDecision);
        decision.setEtat(nouvelEtat.getCodeSystem());
        decision = (SimpleDecisionCAP) JadePersistenceManager.update(decision);
        return decision;
    }

    private DecisionCAPBean clone(DecisionCAPBean decisionCAPBeanOriginale) {

        // Les attributs du bean ne sont jamais null car ils sont instanciés dans le constructeur de ce dernier
        DecisionCAPBean decisionCAPBeanClone = new DecisionCAPBean();

        SimpleDecisionCAP decisionCAPOriginale = decisionCAPBeanOriginale.getDecisionCAP();
        SimpleDecisionCAP decisionCAPClone = decisionCAPBeanClone.getDecisionCAP();

        decisionCAPClone.setAnnee(decisionCAPOriginale.getAnnee());
        decisionCAPClone.setCategorie(decisionCAPOriginale.getCategorie());
        decisionCAPClone.setForfait(decisionCAPOriginale.getForfait());
        decisionCAPClone.setRevenuFRV(decisionCAPOriginale.getRevenuFRV());
        decisionCAPClone.setRevenuIFD(decisionCAPOriginale.getRevenuIFD());
        decisionCAPClone.setTauxAssurance(decisionCAPOriginale.getTauxAssurance());
        decisionCAPClone.setCotisationAnnuelle(decisionCAPOriginale.getCotisationAnnuelle());
        decisionCAPClone.setCotisationMensuelle(decisionCAPOriginale.getCotisationMensuelle());
        decisionCAPClone.setCotisationPeriode(decisionCAPOriginale.getCotisationPeriode());
        decisionCAPClone.setCotisationTrimestrielle(decisionCAPOriginale.getCotisationTrimestrielle());
        decisionCAPClone.setDateDebut(decisionCAPOriginale.getDateDebut());
        decisionCAPClone.setDateDonnees(decisionCAPOriginale.getDateDonnees());
        decisionCAPClone.setDateFin(decisionCAPOriginale.getDateFin());
        decisionCAPClone.setEtat(decisionCAPOriginale.getEtat());
        decisionCAPClone.setIdAffiliation(decisionCAPOriginale.getIdAffiliation());
        decisionCAPClone.setIdPassageFacturation(decisionCAPOriginale.getIdPassageFacturation());
        decisionCAPClone.setIdResponsable(decisionCAPOriginale.getIdResponsable());
        decisionCAPClone.setType(decisionCAPOriginale.getType());
        decisionCAPClone.setCotisationBrute(decisionCAPOriginale.getCotisationBrute());
        decisionCAPClone.setPrestation(decisionCAPOriginale.getPrestation());
        decisionCAPClone.setIdAssurance(decisionCAPOriginale.getIdAssurance());
        decisionCAPClone.setIdDecisionRectifiee(decisionCAPOriginale.getIdDecisionRectifiee());

        List<String> listIdEnfantsOriginale = decisionCAPBeanOriginale.getListIdEnfants();

        List<String> listIdEnfantsClone = new ArrayList<String>();
        listIdEnfantsClone.addAll(listIdEnfantsOriginale);

        decisionCAPBeanClone.setListIdEnfants(listIdEnfantsClone);

        return decisionCAPBeanClone;
    }

    @Override
    public SimpleDecisionCAP create(SimpleDecisionCAP decisionCap, List<String> listIdEnfants)
            throws JadePersistenceException, Exception {

        if (!CodeSystem.isTypeAssuranceCAPWithEnfant(decisionCap.getCategorie())) {
            listIdEnfants.clear();
        }

        // recherche de l'affiliation liée
        AffiliationSimpleModel affiliation = AFBusinessServiceLocator.getAffiliationService().read(
                decisionCap.getIdAffiliation());

        List<String> listExceptedIdDecisionActive = new ArrayList<String>();

        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
            listExceptedIdDecisionActive.add(decisionCap.getIdDecisionRectifiee());
        }

        // validation des données
        SimpleDecisionCapChecker.checkForCreate(
                decisionCap,
                affiliation,
                isAlreadyADecisionActive(affiliation.getId(), decisionCap.getDateDebut(), decisionCap.getDateFin(),
                        listExceptedIdDecisionActive));

        // on persiste directement afin d'obtenir un id (obligatoire pour enregistrer les enfants ensuite)
        decisionCap = (SimpleDecisionCAP) JadePersistenceManager.add(decisionCap);

        int anneeDecision = Integer.valueOf(decisionCap.getAnnee());
        // ajout des enfants
        // parcours de la nouvelle liste et ajout
        for (String idEnfant : listIdEnfants) {
            PersonneEtendueComplexModel tiersComplex = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    idEnfant);
            String dateNaissance = tiersComplex.getPersonne().getDateNaissance();
            String dateDeces = tiersComplex.getPersonne().getDateDeces();

            if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
                throw new Exception("la date de naissance est vide pour le tiers "
                        + tiersComplex.getTiers().getIdTiers());
            }

            // vérification de l'enfant
            SimpleDecisionCapChecker.checkEnfant(tiersComplex, decisionCap.getAnnee());

            // calcul de montant AF
            FWCurrency montantAf = calculerMontantAf(anneeDecision, dateNaissance, dateDeces,
                    decisionCap.getDateDebut(), decisionCap.getDateFin());
            SimpleDecisionCapChecker.checkMontantAf(montantAf, tiersComplex.getTiers().getDesignation1() + " "
                    + tiersComplex.getTiers().getDesignation2());

            // création de l'enfant
            SimpleEnfantDecisionCAP enfantDecisionCap;
            enfantDecisionCap = new SimpleEnfantDecisionCAP();
            enfantDecisionCap.setIdDecision(decisionCap.getIdDecision());
            enfantDecisionCap.setIdTiers(idEnfant);
            enfantDecisionCap.setMontant(montantAf.toStringFormat());
            enfantDecisionCap.setDateNaissance(dateNaissance);
            enfantDecisionCap.setDateRadiation(dateDeces);

            AurigaServiceLocator.getEnfantDecisionCAPService().create(enfantDecisionCap);
        }

        calculerCotisation(decisionCap);
        decisionCap.setEtat(AUDecisionEtat.VALIDEE.getCodeSystem());
        decisionCap = (SimpleDecisionCAP) JadePersistenceManager.update(decisionCap);

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(decisionCap.getAnnee()).intValue()) {
            updateCotisationAffiliation(decisionCap);
        }

        // si il s'agit d'une décision réctificative
        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {

            // vérification de la décision rectifiée
            SimpleDecisionCAP decisionRectifiee = read(decisionCap.getIdDecisionRectifiee());
            SimpleDecisionCapChecker.checkDecisionRectifieeForCreate(decisionRectifiee);

            // check spécifique au décision réctificative
            SimpleDecisionCapChecker.checkForRectificative(decisionCap, decisionRectifiee);

            // mettre la décision réctifiée en état rectfiée
            decisionRectifiee.setEtat(AUDecisionEtat.RECTIFIEE.getCodeSystem());
            decisionRectifiee.setIdPassageFacturation("0");
            JadePersistenceManager.update(decisionRectifiee);

            // créer une sortie CAP
            // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
            String idPassageFacturation = findPassageFacturation();
            if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
                // calcul du montant de l'extourne (cotiRectificative - cotiRectifiee)
                FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifiee.getCotisationPeriode());
                FWCurrency cotisationPeriodeRectificative = new FWCurrency(decisionCap.getCotisationPeriode());
                FWCurrency montantExtourne = new FWCurrency(JANumberFormatter.round(
                        cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05,
                        2, JANumberFormatter.INF));

                // création de la sortie
                SimpleSortieCAP sortie = new SimpleSortieCAP();
                sortie.setIdDecision(decisionCap.getIdDecision());
                sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                sortie.setMontantExtourne(montantExtourne.toStringFormat());
                sortie.setEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                sortie.setIdPassageFacturation(idPassageFacturation);
                AurigaServiceLocator.getSortieCAPService().create(sortie);
            } else {
                throw new Exception("idPassage facturation is null or zero");
            }
        }

        return decisionCap;
    }

    @Override
    public SimpleDecisionCAP delete(SimpleDecisionCAP decisionCap) throws Exception {
        SimpleDecisionCapChecker.checkForDelete(decisionCap);

        // si la décision est de type réctificative et à l'état validée on supprime la sortie liée et on réactive la
        // décision rectifiée
        if (SimpleDecisionCapChecker.isTypeRectif(decisionCap.getType())
                && AUDecisionEtat.VALIDEE.getCodeSystem().equals(decisionCap.getEtat())) {
            deleteSortieForDecision(decisionCap.getIdDecision());

            // réactivation de la décision rectifiée
            reactivateDecisionRectifiee(decisionCap);
        }

        // suppression de la décision
        decisionCap.setEtat(AUDecisionEtat.SUPPRIMEE.getCodeSystem());
        decisionCap.setIdPassageFacturation("0");
        return (SimpleDecisionCAP) JadePersistenceManager.update(decisionCap);
    }

    /**
     * Permet de supprimer les décisions lors de l'extourne (radiation d'un affilié). Cette méthode supprime également
     * les sorties non-comptablisée liées aux décisions qui sont à l'état "validée". Les sorties à l'état "comptablisée"
     * sont conservées mais la décisions liée est mise à l'état "supprimée".
     * 
     * 
     * @param decisionCap
     * @return
     * @throws Exception
     */
    private SimpleDecisionCAP deleteDecisionForExtourne(SimpleDecisionCAP decisionCap) throws Exception {

        // suppression de la décision
        decisionCap.setEtat(AUDecisionEtat.SUPPRIMEE.getCodeSystem());
        decisionCap.setIdPassageFacturation("0");
        return (SimpleDecisionCAP) JadePersistenceManager.update(decisionCap);
    }

    /**
     * Supprime les sorties à l'état "non-comptablisée" pour une décision donnée. Il ne doit y avoir qu'une seule sortie
     * par décision
     * 
     * @param idDecisionCap
     * @throws JadeNoBusinessLogSessionError
     * @throws Exception
     */
    private void deleteSortieForDecision(String idDecisionCap) throws JadeNoBusinessLogSessionError, Exception {
        ComplexSortieCAPDecisionCAPAffiliationSearchModel searchComplexSortie = new ComplexSortieCAPDecisionCAPAffiliationSearchModel();
        searchComplexSortie.setForIdDecision(idDecisionCap);
        searchComplexSortie.setForEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        searchComplexSortie = AurigaServiceLocator.getSortieCAPService().search(searchComplexSortie);
        // il ne doit y avoir qu'une seule sortie non-comptabilisée pour une décision validée
        if (searchComplexSortie.getSize() == 1) {
            ComplexSortieCAPDecisionCAPAffiliation complexSortie = (ComplexSortieCAPDecisionCAPAffiliation) searchComplexSortie
                    .getSearchResults()[0];
            AurigaServiceLocator.getSortieCAPService().delete(complexSortie.getSortieCap());
        } else if (searchComplexSortie.getSize() > 1) {
            throw new Exception("unable to delete sortie, a lot of sortie found for idDecision : " + idDecisionCap);
        }
    }

    @Override
    public void extournerDecisions(String idAffiliation, String fromDate) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idAffiliation)) {
            throw new Exception("idAffiliation can not be null or zero !");
        }
        if (JadeStringUtil.isBlankOrZero(fromDate)) {
            throw new Exception("fromDate can not be null or zero !");
        }

        // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
        String idPassageFacturation = findPassageFacturation();
        if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            // récupération des décisions dont l'année est >= à l'année de fromDate
            // sauf si la radiation est au 31.12
            // dans ce cas particulier il suffit de récupérer les décisions dont l'année est > à l'année de fromDate
            String fromAnnee = String.valueOf(JACalendar.getYear(fromDate));
            if (("31.12." + fromAnnee).equalsIgnoreCase(fromDate)) {
                fromAnnee = String.valueOf(JACalendar.getYear(fromDate) + 1);
            }
            DecisionCAPSearchModel decisionCapSearchModel = new DecisionCAPSearchModel();
            decisionCapSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            decisionCapSearchModel.setForIdAffiliation(idAffiliation);
            decisionCapSearchModel.setFromAnnee(fromAnnee);
            decisionCapSearchModel.setForNotEtat(AUDecisionEtat.SUPPRIMEE.getCodeSystem());
            decisionCapSearchModel = this.search(decisionCapSearchModel);
            for (JadeAbstractModel abstractModel : decisionCapSearchModel.getSearchResults()) {
                SimpleDecisionCAP decision = (SimpleDecisionCAP) abstractModel;

                // si la décision est à l'état comptablilisé on crée une sortie
                boolean wantCreateSortie = false;
                if (AUDecisionEtat.COMPTABILISEE.getCodeSystem().equals(decision.getEtat())
                        || AUDecisionEtat.REPRISE.getCodeSystem().equals(decision.getEtat())) {
                    wantCreateSortie = true;
                }

                // on supprime la décision et crée une sortie si nécessaire
                deleteDecisionForExtourne(decision);
                if (wantCreateSortie) {
                    // création de la sortie correspondante
                    FWCurrency montantExtourne = new FWCurrency(decision.getCotisationPeriode());
                    montantExtourne.negate();

                    SimpleSortieCAP sortie = new SimpleSortieCAP();
                    sortie.setIdDecision(decision.getIdDecision());
                    sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                    sortie.setMontantExtourne(montantExtourne.toStringFormat());
                    sortie.setEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                    sortie.setIdPassageFacturation(idPassageFacturation);
                    AurigaServiceLocator.getSortieCAPService().create(sortie);
                }
            }
        } else {
            throw new Exception("idPassage facturation is null or zero");
        }
    }

    @Override
    public void extournerDecisionsRollback(String idAffiliation) throws Exception {

        ComplexSortieCAPDecisionCAPAffiliationSearchModel searchComplexSortie = new ComplexSortieCAPDecisionCAPAffiliationSearchModel();
        searchComplexSortie.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchComplexSortie.setForIdAffiliation(idAffiliation);
        searchComplexSortie.setForEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        searchComplexSortie = AurigaServiceLocator.getSortieCAPService().search(searchComplexSortie);

        for (JadeAbstractModel abstractModel : searchComplexSortie.getSearchResults()) {
            ComplexSortieCAPDecisionCAPAffiliation complexSortieCap = (ComplexSortieCAPDecisionCAPAffiliation) abstractModel;

            SimpleDecisionCAP decisionCAP = complexSortieCap.getDecisionCap();

            decisionCAP.setEtat(AUDecisionEtat.COMPTABILISEE.getCodeSystem());

            decisionCAP = updateWithoutCalculCotis(decisionCAP);

            String idDecisionRectifiee = decisionCAP.getIdDecisionRectifiee();
            while (!JadeStringUtil.isBlankOrZero(idDecisionRectifiee)) {
                SimpleDecisionCAP decisionCAPRectifiee = read(idDecisionRectifiee);
                decisionCAPRectifiee.setEtat(AUDecisionEtat.RECTIFIEE.getCodeSystem());
                decisionCAPRectifiee = updateWithoutCalculCotis(decisionCAPRectifiee);

                idDecisionRectifiee = decisionCAPRectifiee.getIdDecisionRectifiee();
            }

            AurigaServiceLocator.getSortieCAPService().delete(complexSortieCap.getSortieCap());

        }

    }

    @Override
    public AFAssurance findAssurance(String idAffiliation) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // recherche de l'adhesion pour l'idAffiliation
        AFAdhesionManager adhesionManager = new AFAdhesionManager();
        adhesionManager.setSession(session);
        adhesionManager.setForAffiliationId(idAffiliation);
        adhesionManager.find();
        if (adhesionManager.size() > 0) {
            AFAdhesion adhesion = (AFAdhesion) adhesionManager.getFirstEntity();

            // recherche de la cotisation pour l'idAdhesion
            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setSession(session);
            cotisationManager.setForAdhesionId(adhesion.getAdhesionId());
            cotisationManager.find();
            if (cotisationManager.size() > 0) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getFirstEntity();
                return cotisation.getAssurance();
            } else {
                throw new AurigaException("unable to find cotisation");
            }
        } else {
            throw new AurigaException("unable to find adhesion");
        }
    }

    @Override
    public AdhesionCotisationAssuranceSearchComplexModel findAssuranceWidget(
            AdhesionCotisationAssuranceSearchComplexModel searchModel) throws Exception {
        searchModel.setForDateCotisationDebutLessOrEqual("31.12." + searchModel.getForWidgetAnnee());
        searchModel.setForDateCotisationFinNullGreaterOrEqual("01.01." + searchModel.getForWidgetAnnee());
        searchModel = (AdhesionCotisationAssuranceSearchComplexModel) JadePersistenceManager.search(searchModel);
        return searchModel;
    }

    @Override
    public AFParametreAssurance findParamAssurance(String idAssurance, String genre, String annee) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // recherche des paramètres pour l'idAssurance
        AFParametreAssuranceManager paramAssuranceManager = new AFParametreAssuranceManager();
        paramAssuranceManager.setSession(session);
        paramAssuranceManager.setForIdAssurance(idAssurance);
        paramAssuranceManager.setForGenre(genre);
        paramAssuranceManager.setForDate("01.01." + annee);
        paramAssuranceManager.setOrderByDateDebutDesc();
        paramAssuranceManager.find();
        if (paramAssuranceManager.size() > 0) {
            return (AFParametreAssurance) paramAssuranceManager.getFirstEntity();
        } else {
            throw new AurigaException("unable to find paramAssurance");
        }
    }

    private String findPassageFacturation() throws Exception {
        FAPassageModuleManager modulePassageManager = new FAPassageModuleManager();
        modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
        modulePassageManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
        modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
        modulePassageManager.find();

        if (modulePassageManager.size() > 0) {
            FAPassageModule passageFacturation = (FAPassageModule) modulePassageManager.getFirstEntity();
            return passageFacturation.getIdPassage();
        } else {
            // erreur metier
            throw new AurigaException("auriga.passage.facturation.notfound");
        }
    }

    @Override
    public AFTauxAssurance findTauxAssurance(String idAssurance, String annee) throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // recherche du tauxAssurance pour l'idAssurance et l'année de la décision
        AFTauxAssuranceManager tauxAssuranceManager = new AFTauxAssuranceManager();
        tauxAssuranceManager.setSession(session);
        tauxAssuranceManager.setForIdAssurance(idAssurance);
        tauxAssuranceManager.setForDate("01.01." + annee);
        tauxAssuranceManager.setOrderByDebutDescRang();
        tauxAssuranceManager.find();
        if (tauxAssuranceManager.size() > 0) {
            AFTauxAssurance tauxAssurance = (AFTauxAssurance) tauxAssuranceManager.getFirstEntity();
            return tauxAssurance;
        } else {
            throw new AurigaException("unable to find tauxAssurance");
        }
    }

    @Override
    public double getMontantAFAFacturer(SimpleDecisionCAP decisionCap, int nbMoisAFacturer) {

        return new FWCurrency(getMontantAFMensuelArrondiDecisionCAP(decisionCap).doubleValue() * nbMoisAFacturer)
                .doubleValue();

    }

    @Override
    public double getMontantAFAFacturerForSortieRectificative(SimpleDecisionCAP decisionCapRectificative,
            SimpleDecisionCAP decisionCapRectifiee) {

        return Double.valueOf(decisionCapRectificative.getPrestation()).doubleValue()
                - Double.valueOf(decisionCapRectifiee.getPrestation()).doubleValue();

    }

    private FWCurrency getMontantAFMensuelArrondiDecisionCAP(SimpleDecisionCAP decisionCAP) {
        // Les prestation sont déjà adaptées au nombre de mois
        FWCurrency montPeriode = new FWCurrency(decisionCAP.getPrestation());
        int nbMoisPeriode = AriesAurigaDateUtils.getNombreMoisEntreDates(decisionCAP.getDateDebut(),
                decisionCAP.getDateFin());
        FWCurrency montantAFMensuelArrondi = new FWCurrency(JANumberFormatter.round(montPeriode.doubleValue()
                / nbMoisPeriode, 0.05, 2, JANumberFormatter.SUP));

        return montantAFMensuelArrondi;
    }

    private boolean isAlreadyADecisionActive(String theIdAffiliation, String theDateDebut, String theDateFin,
            List<String> listExceptedIdDecision) throws JadePersistenceException {

        DecisionCAPSearchModel decisionCAPSearchModel = new DecisionCAPSearchModel();

        decisionCAPSearchModel.setForIdAffiliation(theIdAffiliation);
        decisionCAPSearchModel.setForDateDebutLessEqual(theDateFin);
        decisionCAPSearchModel.setForDateFinGreaterEqual(theDateDebut);
        decisionCAPSearchModel.setInEtat(AUDecisionEtat.getListEtatActif());
        decisionCAPSearchModel.setNotInIdDecision(listExceptedIdDecision);

        return JadePersistenceManager.count(decisionCAPSearchModel) >= 1;
    }

    @Override
    public String printDecision(String idDecisionCap) throws Exception {

        SimpleDecisionCAP decisionCap = AurigaServiceLocator.getDecisionCAPService().read(idDecisionCap);

        String theDateFacturation = JACalendar.todayJJsMMsAAAA();
        String theIdPassage = decisionCap.getIdPassageFacturation();

        if (!JadeStringUtil.isBlankOrZero(theIdPassage)) {
            PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(theIdPassage);
            theDateFacturation = thePassage.getDateFacturation();
        }

        return printUnitaire(decisionCap, theDateFacturation).getDocumentLocation().replace("\\", "/");

    }

    private List<String> getListCategorieToPrintForRenouvellement() throws PropertiesException {

        String categorieToPrintSeparatedByVirgule = AFProperties.CATEGORIE_DECISION_CAP_TO_PRINT_DURING_RENOUVELLEMENT
                .getValue();

        List<String> listCategorieToPrint = new ArrayList<String>(Arrays.asList(categorieToPrintSeparatedByVirgule
                .split(",")));

        for (String aCategorie : listCategorieToPrint) {
            aCategorie = aCategorie.trim();
        }

        return listCategorieToPrint;

    }

    @Override
    public String getLibelleCategorieDecisionPrintedInRenouvellement() throws Exception {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        String categorieToPrintSeparatedByVirgule = AFProperties.CATEGORIE_DECISION_CAP_TO_PRINT_DURING_RENOUVELLEMENT
                .getValue();

        List<String> listCategorieToPrint = new ArrayList<String>(Arrays.asList(categorieToPrintSeparatedByVirgule
                .split(",")));

        String categorieDecisionPrintedResult = "";
        for (String aCategorie : listCategorieToPrint) {

            aCategorie = aCategorie.trim();

            FWParametersSystemCode csEntity = new FWParametersSystemCode();
            csEntity.setSession(session);
            csEntity.setIdCode(aCategorie);
            csEntity.retrieve();

            if (!JadeStringUtil.isBlankOrZero(categorieDecisionPrintedResult)) {
                categorieDecisionPrintedResult += ", ";
            }
            categorieDecisionPrintedResult += csEntity.getCurrentCodeUtilisateur().getLibelle();

        }

        return categorieDecisionPrintedResult;

    }

    @Override
    public List<JadePublishDocument> printDecisionPassage(String idPassage) throws Exception {
        List<JadePublishDocument> listPrintedDocument = new ArrayList<JadePublishDocument>();

        DecisionCAPSearchModel decisionCAPSearchModel = new DecisionCAPSearchModel();
        decisionCAPSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCAPSearchModel.setForIdPassageFacturation(idPassage);
        decisionCAPSearchModel.setInCategorie(getListCategorieToPrintForRenouvellement());
        decisionCAPSearchModel = this.search(decisionCAPSearchModel);

        PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(idPassage);
        String theDateFacturation = thePassage.getDateFacturation();

        if (decisionCAPSearchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : decisionCAPSearchModel.getSearchResults()) {
                SimpleDecisionCAP aDecisionCAP = (SimpleDecisionCAP) abstractModel;
                listPrintedDocument.add(printUnitaire(aDecisionCAP, theDateFacturation));
            }
        }

        return listPrintedDocument;
    }

    /**
     * Imprime de manière unitaire un document décision en fonction de la décision reçue en paramètre et retourne le
     * premier JadePublishDocument
     * 
     * @param decisionCap
     * @return
     * @throws Exception
     */
    private JadePublishDocument printUnitaire(SimpleDecisionCAP decisionCap, String dateFacturation) throws Exception {

        AffiliationComplexModel affiliationComplex = AFBusinessServiceLocator.getAffiliationComplexService().read(
                decisionCap.getIdAffiliation());
        List<ComplexEnfantDecisionCAPTiers> listEnfantDecisionCap = AurigaServiceLocator.getEnfantDecisionCAPService()
                .getListComplexEnfantDecisionCapTiers(decisionCap.getIdDecision());

        // si catégorie 10 on renseigne le plancher et le plafond
        String plancher = "";
        String plafond = "";
        if (decisionCap.getCategorie().equals(CodeSystem.TYPE_ASS_CAP_10)) {
            AFParametreAssurance paramPlancher = findParamAssurance(decisionCap.getIdAssurance(),
                    CodeSystem.GEN_PARAM_ASS_PLANCHER, decisionCap.getAnnee());
            AFParametreAssurance paramPlafond = findParamAssurance(decisionCap.getIdAssurance(),
                    CodeSystem.GEN_PARAM_ASS_PLAFOND, decisionCap.getAnnee());
            plancher = new FWCurrency(paramPlancher.getValeur()).toString();
            plafond = new FWCurrency(paramPlafond.getValeur()).toString();
        }

        // si il s'agit d'une décision rectificative on calcul le solde en notre ou votre faveur
        String montantCotisationRectifiee = "";
        String montantTotalNotreVotreFaveur = "";
        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
            SimpleDecisionCAP decisionRectifiee = read(decisionCap.getIdDecisionRectifiee());

            FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifiee.getCotisationPeriode());
            FWCurrency cotisationPeriodeRectificative = new FWCurrency(decisionCap.getCotisationPeriode());
            FWCurrency totalNotreVotreFaveur = new FWCurrency(JANumberFormatter.round(
                    cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05, 2,
                    JANumberFormatter.INF));
            montantCotisationRectifiee = cotisationPeriodeRectifiee.toStringFormat();
            montantTotalNotreVotreFaveur = totalNotreVotreFaveur.toStringFormat();
        }

        // impression du document
        AUDecisionCap_Doc decisionCapDoc = new AUDecisionCap_Doc();
        decisionCapDoc.setDateFacturation(dateFacturation);
        decisionCapDoc.setDecisionCap(decisionCap);
        decisionCapDoc.setAffiliationComplex(affiliationComplex);
        decisionCapDoc.setListEnfant(listEnfantDecisionCap);
        decisionCapDoc.setPlancherCotiBrute(plancher);
        decisionCapDoc.setPlafondCotiBrute(plafond);
        decisionCapDoc.setMontantCotisationRectifiee(montantCotisationRectifiee);
        decisionCapDoc.setMontantTotalNotreVotreFaveur(montantTotalNotreVotreFaveur);
        decisionCapDoc.executeProcess();

        // récupération du document
        List<JadePublishDocument> attachedDocuments = decisionCapDoc.getAttachedDocuments();
        if ((attachedDocuments.size() > 0) && (attachedDocuments.get(0) != null)) {
            return attachedDocuments.get(0);
        } else {
            throw new Exception("no attached documents found for decision cap !");
        }
    }

    private void reactivateDecisionRectifiee(SimpleDecisionCAP decisionCap) throws JadePersistenceException, Exception {
        if (JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
            throw new IllegalStateException(
                    "unable to reactive decision, idDecisionRectifiee can not be blank or zero !");
        }

        SimpleDecisionCAP decisionRectifiee = read(decisionCap.getIdDecisionRectifiee());
        decisionRectifiee.setEtat(AUDecisionEtat.COMPTABILISEE.getCodeSystem());
        updateWithoutCalculCotis(decisionRectifiee);

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(decisionRectifiee.getAnnee())
                .intValue()) {
            // mise à jour des cotisations dans l'affiliation
            updateCotisationAffiliation(decisionRectifiee);
        }

    }

    @Override
    public SimpleDecisionCAP read(String idDecisionCap) throws JadePersistenceException {
        SimpleDecisionCAP decisionCap = new SimpleDecisionCAP();
        decisionCap.setIdDecision(idDecisionCap);
        return (SimpleDecisionCAP) JadePersistenceManager.read(decisionCap);
    }

    private List<String> removeEnfantsAyantPlusDroitAuxAF(List<String> theListIdEnfants, int anneeDecisionInt)
            throws Exception {

        List<String> listIdEnfantsAyantDroitAF = new ArrayList<String>();

        for (String idEnfant : theListIdEnfants) {

            boolean hasEnfantDroitAuxAF = true;

            PersonneEtendueComplexModel tiersComplex = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    idEnfant);
            String dateNaissance = tiersComplex.getPersonne().getDateNaissance();
            String dateDeces = tiersComplex.getPersonne().getDateDeces();

            if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
                int anneeNaissanceInt = JACalendar.getYear(dateNaissance);
                int age = anneeDecisionInt - anneeNaissanceInt;
                if (age > 20) {
                    hasEnfantDroitAuxAF = false;
                }
            }

            if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
                int anneeDecesInt = JACalendar.getYear(dateDeces);
                if (anneeDecesInt < anneeDecisionInt) {
                    hasEnfantDroitAuxAF = false;
                }
            }

            if (hasEnfantDroitAuxAF) {
                listIdEnfantsAyantDroitAF.add(idEnfant);
            }
        }

        return listIdEnfantsAyantDroitAF;

    }

    @Override
    public DecisionCAPBean renouvelerDecisionCAP(DecisionCAPBean decisionCAPBeanARenouveler,
            PassageModel thePassageFacturation, boolean hasPassageModuleFacturationDecisionCAP,
            String theTypeAssurance, List<SimpleDecisionCAP> theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement, String theIdAssurance)
            throws Exception {

        RenouvellementDecisionCAPChecker.checkDecisionCAPARenouveler(decisionCAPBeanARenouveler, theTypeAssurance,
                theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
                theListAffiliePlusieursCotisationDansAnneeDuRenouvellement);
        RenouvellementDecisionCAPChecker.checkPassageFacturation(thePassageFacturation,
                hasPassageModuleFacturationDecisionCAP);

        DecisionCAPBean nouveauDecisionCAPBean = this.clone(decisionCAPBeanARenouveler);

        int anneeNouvelleDecisionCAP = Integer.valueOf(decisionCAPBeanARenouveler.getDecisionCAP().getAnnee())
                .intValue() + 1;

        nouveauDecisionCAPBean.setListIdEnfants(removeEnfantsAyantPlusDroitAuxAF(
                nouveauDecisionCAPBean.getListIdEnfants(), anneeNouvelleDecisionCAP));

        SimpleDecisionCAP nouvelleDecisionCAP = nouveauDecisionCAPBean.getDecisionCAP();
        nouvelleDecisionCAP.setAnnee(String.valueOf(anneeNouvelleDecisionCAP));
        nouvelleDecisionCAP.setDateDebut("01.01." + anneeNouvelleDecisionCAP);
        nouvelleDecisionCAP.setDateFin("31.12." + anneeNouvelleDecisionCAP);
        nouvelleDecisionCAP.setDateDonnees(JACalendar.todayJJsMMsAAAA());
        nouvelleDecisionCAP.setIdPassageFacturation(thePassageFacturation.getIdPassage());
        nouvelleDecisionCAP.setType(AUDecisionType.DEFINITIVE.getCodeSystem());
        nouvelleDecisionCAP.setIdAssurance(theIdAssurance);
        nouvelleDecisionCAP.setIdDecisionRectifiee("0");
        if (CodeSystem.TYPE_ASS_CAP_10.equalsIgnoreCase(theTypeAssurance)) {
            nouvelleDecisionCAP.setType(AUDecisionType.PROVISOIRE.getCodeSystem());
        }

        create(nouveauDecisionCAPBean.getDecisionCAP(), nouveauDecisionCAPBean.getListIdEnfants());

        return nouveauDecisionCAPBean;
    }

    @Override
    public ResultatTraitementMasseCsvJournal renouvelerDecisionCAPMasse(JadeProgressBarModel jadeProgressBarModel,
            boolean simulation, String annee, String numeroAffilieDebut, String numeroAffilieFin,
            String numeroPassageFacturation) throws Exception {

        PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(numeroPassageFacturation);

        FAModulePassageManager modulePassageManager = new FAModulePassageManager();
        modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
        modulePassageManager.setForIdPassage(numeroPassageFacturation);
        modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
        boolean hasPassageModuleFacturationDecisionCAP = modulePassageManager.getCount() >= 1;

        int anneeReferenceRenouvellement = Integer.valueOf(annee).intValue() - 1;

        ComplexDecisionCAPAffiliationCotisationSearchModel theSearchModel = new ComplexDecisionCAPAffiliationCotisationSearchModel();
        theSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        theSearchModel.setInEtatDecision(AUDecisionEtat.getListEtatRenouvelable());
        theSearchModel.setForDateFinDecision("31.12." + anneeReferenceRenouvellement);

        if (!JadeStringUtil.isBlankOrZero(numeroAffilieDebut)) {
            theSearchModel.setForNumeroAffilieGreaterEqual(numeroAffilieDebut);
        }

        if (!JadeStringUtil.isBlankOrZero(numeroAffilieFin)) {
            theSearchModel.setForNumeroAffilieLessEqual(numeroAffilieFin);
        }

        theSearchModel.setForDateFinAffiliationGreater("31.12." + anneeReferenceRenouvellement);
        theSearchModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        theSearchModel.setInTypeAssurance(CodeSystem.getListTypeAssuranceCAP());
        theSearchModel.setForDateFinCotisationGreater("31.12." + anneeReferenceRenouvellement);

        theSearchModel = this.search(theSearchModel);

        // Le tag <order-by> par défaut du ComplexDecisionCAPAffiliationCotisationSearchModel
        // retourne les données triées par numéro d'affilié ascendant puis par date de décision descendante
        List<ComplexDecisionCAPAffiliationCotisation> listDecisionCAPAffiliationCotisation = new ArrayList<ComplexDecisionCAPAffiliationCotisation>();
        List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement = new ArrayList<String>();
        if (theSearchModel.getSearchResults() != null) {
            String theNumeroAffilieCourant = "";
            String theIdCotisationCourante = "";
            for (JadeAbstractModel abstractModel : theSearchModel.getSearchResults()) {
                ComplexDecisionCAPAffiliationCotisation theModel = (ComplexDecisionCAPAffiliationCotisation) abstractModel;
                if (!theNumeroAffilieCourant.equalsIgnoreCase(theModel.getAffiliation().getAffilieNumero())) {
                    listDecisionCAPAffiliationCotisation.add(theModel);
                    theNumeroAffilieCourant = theModel.getAffiliation().getAffilieNumero();
                    theIdCotisationCourante = theModel.getCotisation().getCotisationId();
                } else {
                    if (!theIdCotisationCourante.equalsIgnoreCase(theModel.getCotisation().getCotisationId())
                            && !theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.contains(theModel
                                    .getAffiliation().getAffiliationId())) {
                        theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.add(theModel.getAffiliation()
                                .getAffiliationId());
                    }
                }

            }
        }

        List<SimpleDecisionCAP> listDecisionCAPDejaExistanteDansAnneeDuRenouvellement = new ArrayList<SimpleDecisionCAP>();

        DecisionCAPSearchModel decisionCAPSearchModel = new DecisionCAPSearchModel();
        decisionCAPSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCAPSearchModel.setForAnnee(annee);
        decisionCAPSearchModel.setInEtat(AUDecisionEtat.getListEtatActif());

        decisionCAPSearchModel = this.search(decisionCAPSearchModel);

        if (decisionCAPSearchModel.getSearchResults() != null) {

            for (JadeAbstractModel abstractModel : decisionCAPSearchModel.getSearchResults()) {
                SimpleDecisionCAP aDecisionCAP = (SimpleDecisionCAP) abstractModel;
                listDecisionCAPDejaExistanteDansAnneeDuRenouvellement.add(aDecisionCAP);
            }
        }

        List<RenouvellementDecisionCAPUnitTask> listUnitTask = new ArrayList<RenouvellementDecisionCAPUnitTask>();
        for (ComplexDecisionCAPAffiliationCotisation aDecisionCAPAffiliationCotisation : listDecisionCAPAffiliationCotisation) {
            DecisionCAPBean theDecisionCAPBeanInput = new DecisionCAPBean();
            theDecisionCAPBeanInput.setDecisionCAP(aDecisionCAPAffiliationCotisation.getDecisionCAP());

            theDecisionCAPBeanInput.setListIdEnfants(AurigaServiceLocator.getEnfantDecisionCAPService()
                    .getListIdEnfantDecisionCap(theDecisionCAPBeanInput.getDecisionCAP().getIdDecision()));

            listUnitTask.add(new RenouvellementDecisionCAPUnitTask(theDecisionCAPBeanInput,
                    aDecisionCAPAffiliationCotisation.getAffiliation().getAffilieNumero(),
                    aDecisionCAPAffiliationCotisation.getAffiliation().getRaisonSocialeCourt(), thePassage,
                    hasPassageModuleFacturationDecisionCAP, aDecisionCAPAffiliationCotisation.getAssurance()
                            .getTypeAssurance(), listDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
                    theListAffiliePlusieursCotisationDansAnneeDuRenouvellement, aDecisionCAPAffiliationCotisation
                            .getAssurance().getAssuranceId()));
        }

        AriesServiceLocator.getTraitementMasseService().traiter(jadeProgressBarModel, listUnitTask);

        List<String> listCsvLine = new ArrayList<String>();

        StringBuffer csvHeader = new StringBuffer();
        csvHeader.append(JadeThread.getMessage("auriga.printing.numero.affilie"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("auriga.printing.affilie"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("auriga.printing.periode"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("auriga.printing.date.decision"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("auriga.printing.statut"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("auriga.printing.remarque"));
        csvHeader.append(";");
        csvHeader.append(IOUtils.LINE_SEPARATOR);

        listCsvLine.add(csvHeader.toString());

        boolean hasErreurTechnique = false;
        for (RenouvellementDecisionCAPUnitTask aUnitTask : listUnitTask) {

            if (UnitTaskResultState.ERREUR_TECHNNIQUE.equals(aUnitTask.getResultState())) {
                hasErreurTechnique = true;
            }

            StringBuffer csvLine = new StringBuffer();

            csvLine.append(aUnitTask.getNumeroAffilie());
            csvLine.append(";");
            csvLine.append(aUnitTask.getNomPrenomAffilie());
            csvLine.append(";");
            csvLine.append(aUnitTask.getDecisionCAPBeanInput().getDecisionCAP().getDateDebut() + " - "
                    + aUnitTask.getDecisionCAPBeanInput().getDecisionCAP().getDateFin());
            csvLine.append(";");
            csvLine.append(aUnitTask.getDecisionCAPBeanInput().getDecisionCAP().getDateDonnees());
            csvLine.append(";");
            csvLine.append(aUnitTask.getResultStateLibelle());
            csvLine.append(";");
            csvLine.append(aUnitTask.getErrorMessage() != null ? aUnitTask.getErrorMessage() : "");
            csvLine.append(";");
            csvLine.append(IOUtils.LINE_SEPARATOR);

            listCsvLine.add(csvLine.toString());
        }

        return new ResultatTraitementMasseCsvJournal(hasErreurTechnique, listCsvLine);

    }

    public ComplexDecisionCAPAffiliationCotisationSearchModel search(
            ComplexDecisionCAPAffiliationCotisationSearchModel searchModel) throws JadePersistenceException {
        return (ComplexDecisionCAPAffiliationCotisationSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DecisionCAPSearchModel search(DecisionCAPSearchModel searchModel) throws JadePersistenceException {
        return (DecisionCAPSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public ComplexEnfantDecisionCapWidgetSearchModel searchEnfantDecisionCapWidget(
            ComplexEnfantDecisionCapWidgetSearchModel searchModel) throws Exception {

        if (!JadeStringUtil.isEmpty(searchModel.getForDesignation1Like())) {
            searchModel.setForDesignation1Like(JadeStringUtil.convertSpecialChars(searchModel.getForDesignation1Like()
                    .toUpperCase()));
        }

        if (!JadeStringUtil.isEmpty(searchModel.getForDesignation2Like())) {
            searchModel.setForDesignation2Like(JadeStringUtil.convertSpecialChars(searchModel.getForDesignation2Like()
                    .toUpperCase()));
        }

        return (ComplexEnfantDecisionCapWidgetSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleDecisionCAP update(SimpleDecisionCAP decisionCap, List<String> listIdEnfants)
            throws JadePersistenceException, Exception {

        if (!CodeSystem.isTypeAssuranceCAPWithEnfant(decisionCap.getCategorie())) {
            listIdEnfants.clear();
        }

        // recherche de l'affiliation liée
        AffiliationSimpleModel affiliation = AFBusinessServiceLocator.getAffiliationService().read(
                decisionCap.getIdAffiliation());

        List<String> listExceptedIdDecisionActive = new ArrayList<String>();
        listExceptedIdDecisionActive.add(decisionCap.getIdDecision());

        // validation des données
        SimpleDecisionCapChecker.checkForUpdate(
                decisionCap,
                affiliation,
                isAlreadyADecisionActive(affiliation.getId(), decisionCap.getDateDebut(), decisionCap.getDateFin(),
                        listExceptedIdDecisionActive));

        int anneeDecision = Integer.valueOf(decisionCap.getAnnee());

        // récupération des enfants déjà enregistrés
        List<SimpleEnfantDecisionCAP> listEnfantsActuels = AurigaServiceLocator.getEnfantDecisionCAPService()
                .getListEnfantDecisionCap(decisionCap.getIdDecision());

        // suppression des enfants effacés
        // parcours de la liste actuel, si l'enfant n'est pas contenu dans la nouvelle liste on le supprime
        for (SimpleEnfantDecisionCAP enfantActuel : listEnfantsActuels) {
            if (!listIdEnfants.contains(enfantActuel.getIdTiers())) {
                // suppression de l'enfant
                AurigaServiceLocator.getEnfantDecisionCAPService().delete(enfantActuel);
            }
        }

        // ajout et mise à jour des enfants
        // parcours de la nouvelle liste, si l'enfant n'est pas contenu dans la liste actuel on l'ajoute sinon on
        // le met à jour
        for (String idEnfant : listIdEnfants) {
            boolean isContains = false;

            // récupération des infos sur l'enfant (pour calculer le montant)
            PersonneEtendueComplexModel tiersComplex = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    idEnfant);
            String dateNaissance = tiersComplex.getPersonne().getDateNaissance();
            String dateDeces = tiersComplex.getPersonne().getDateDeces();
            if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
                throw new Exception("la date de naissance est vide pour le tiers "
                        + tiersComplex.getTiers().getIdTiers());
            }

            // vérification de l'enfant
            SimpleDecisionCapChecker.checkEnfant(tiersComplex, decisionCap.getAnnee());

            // calcul de montant AF
            FWCurrency montantAf = calculerMontantAf(anneeDecision, dateNaissance, dateDeces,
                    decisionCap.getDateDebut(), decisionCap.getDateFin());
            SimpleDecisionCapChecker.checkMontantAf(montantAf, tiersComplex.getTiers().getDesignation1() + " "
                    + tiersComplex.getTiers().getDesignation2());

            // udpate : si l'enfant est déjà contenu on le met simplement à jour
            for (SimpleEnfantDecisionCAP enfantActuel : listEnfantsActuels) {
                if (enfantActuel.getIdTiers().equals(idEnfant)) {
                    isContains = true;
                    enfantActuel.setDateNaissance(dateNaissance);
                    enfantActuel.setDateRadiation(dateDeces);
                    enfantActuel.setMontant(montantAf.toStringFormat());
                    AurigaServiceLocator.getEnfantDecisionCAPService().update(enfantActuel);
                    break;
                }
            }

            // add
            if (!isContains) {
                // création de l'enfant
                SimpleEnfantDecisionCAP enfantDecisionCap;
                enfantDecisionCap = new SimpleEnfantDecisionCAP();
                enfantDecisionCap.setIdDecision(decisionCap.getIdDecision());
                enfantDecisionCap.setIdTiers(idEnfant);
                enfantDecisionCap.setDateNaissance(dateNaissance);
                enfantDecisionCap.setDateRadiation(dateDeces);
                enfantDecisionCap.setMontant(montantAf.toStringFormat());
                AurigaServiceLocator.getEnfantDecisionCAPService().create(enfantDecisionCap);
            }
        }

        calculerCotisation(decisionCap);

        decisionCap = (SimpleDecisionCAP) JadePersistenceManager.update(decisionCap);

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(decisionCap.getAnnee()).intValue()) {
            updateCotisationAffiliation(decisionCap);
        }

        // si il s'agit d'une décision réctificative
        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {

            // lecture de la décision rectifiée
            SimpleDecisionCAP decisionRectifiee = read(decisionCap.getIdDecisionRectifiee());

            // check spécifique au décision réctificative
            SimpleDecisionCapChecker.checkForRectificative(decisionCap, decisionRectifiee);

            // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
            String idPassageFacturation = findPassageFacturation();
            if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
                // calcul du montant de l'extourne (cotiRectificative - cotiRectifiee)
                FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifiee.getCotisationPeriode());
                FWCurrency cotisationPeriodeRectificative = new FWCurrency(decisionCap.getCotisationPeriode());
                FWCurrency montantExtourne = new FWCurrency(JANumberFormatter.round(
                        cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05,
                        2, JANumberFormatter.INF));

                // lecture et mise à jour de la sortie
                ComplexSortieCAPDecisionCAPAffiliationSearchModel searchComplexSortie = new ComplexSortieCAPDecisionCAPAffiliationSearchModel();
                searchComplexSortie.setForIdDecision(decisionCap.getIdDecision());
                searchComplexSortie.setForEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                searchComplexSortie = AurigaServiceLocator.getSortieCAPService().search(searchComplexSortie);
                // il ne doit y avoir qu'une seule sortie non-comptabilisée pour une décision
                if (searchComplexSortie.getSize() == 1) {
                    ComplexSortieCAPDecisionCAPAffiliation complexSortie = (ComplexSortieCAPDecisionCAPAffiliation) searchComplexSortie
                            .getSearchResults()[0];
                    SimpleSortieCAP sortie = complexSortie.getSortieCap();
                    sortie.setIdDecision(decisionCap.getIdDecision());
                    sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                    sortie.setMontantExtourne(montantExtourne.toStringFormat());
                    sortie.setEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                    sortie.setIdPassageFacturation(idPassageFacturation);
                    AurigaServiceLocator.getSortieCAPService().update(sortie);
                } else if (searchComplexSortie.getSize() == 0) {
                    throw new Exception("no sortieCap found for idDecision : " + decisionCap.getIdDecision());
                } else {
                    throw new Exception("a lot of sortieCap found for idDecision : " + decisionCap.getIdDecision());
                }

            } else {
                throw new Exception("idPassage facturation is null or zero");
            }
        }

        return decisionCap;
    }

    private AffiliationAssuranceSearchComplexModel searchCotisation(String whereKey, String dateReference,
            String idAffiliation) throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchModel = new AffiliationAssuranceSearchComplexModel();

        affiliationAssuranceSearchModel.setWhereKey(whereKey);
        affiliationAssuranceSearchModel.setForIdAffiliation(idAffiliation);
        affiliationAssuranceSearchModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);

        affiliationAssuranceSearchModel.setInTypeAssurance(CodeSystem.getListTypeAssuranceCAP());

        affiliationAssuranceSearchModel.setForDateCotisationDebutLessEqualFinGreaterNull(dateReference);
        affiliationAssuranceSearchModel = AFBusinessServiceLocator.getAffiliationService().searchAffiliationAssurance(
                affiliationAssuranceSearchModel);

        return affiliationAssuranceSearchModel;

    }

    private void updateCotisationAffiliation(SimpleDecisionCAP decisionCap) throws Exception {

        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchModel = searchCotisation(
                "searchCotisationCAPCGASToUpdateAfterDecision", decisionCap.getDateDebut(),
                decisionCap.getIdAffiliation());

        if (affiliationAssuranceSearchModel.getSize() <= 0) {
            throw new AurigaException("auriga.update.cotisation.affiliation.aucune.cotisation.active",
                    decisionCap.getDateDebut());
        }

        // Le modèle complexe AffiliationAssuranceSearchComplexModel retourne plusieurs lignes dans le cas où
        // l'assurance a plusieurs paramètres
        // mais toutes les lignes retournées concernent la même cotisation
        // C'est pourquoi il suffit de prendre la première
        CotisationSimpleModel cotisationToUpdate = ((AffiliationAssuranceComplexModel) affiliationAssuranceSearchModel
                .getSearchResults()[0]).getCotisation();

        AffiliationSimpleModel affiliation = ((AffiliationAssuranceComplexModel) affiliationAssuranceSearchModel
                .getSearchResults()[0]).getAffiliation();

        int anneeCotisationToUpdate = JACalendar.getYear(cotisationToUpdate.getDateDebut());
        int anneeDecisionCap = Integer.valueOf(decisionCap.getAnnee());

        boolean isDecisionPartielleForAnneeCouranteWithoutRadiation = anneeDecisionCap == JACalendar.getYear(JACalendar
                .todayJJsMMsAAAA());
        isDecisionPartielleForAnneeCouranteWithoutRadiation = isDecisionPartielleForAnneeCouranteWithoutRadiation
                && !decisionCap.getDateFin().startsWith("31.12");
        isDecisionPartielleForAnneeCouranteWithoutRadiation = isDecisionPartielleForAnneeCouranteWithoutRadiation
                && JadeStringUtil.isBlankOrZero(affiliation.getDateFin());

        // Cas décision pour une année future
        if (anneeDecisionCap > anneeCotisationToUpdate
                && anneeDecisionCap > JACalendar.getYear(JACalendar.todayJJsMMsAAAA())) {

            if (JadeStringUtil.isBlankOrZero(cotisationToUpdate.getDateFin())) {

                cotisationToUpdate.setDateFin(new JACalendarGregorian().addDays(decisionCap.getDateDebut(), -1));
                cotisationToUpdate.setMotifFin(CodeSystem.MOTIF_FIN_MASSE);

                cotisationToUpdate = (CotisationSimpleModel) JadePersistenceManager.update(cotisationToUpdate);

            }

            AffiliationAssuranceSearchComplexModel affiliationAssuranceChevauchementSearchModel = searchCotisation(
                    "searchCotisationCAPCGASChevauchement", decisionCap.getDateDebut(), decisionCap.getIdAffiliation());

            if (affiliationAssuranceChevauchementSearchModel.getSize() >= 1) {
                throw new AurigaException(
                        "auriga.update.cotisation.affiliation.creation.cotisation.impossible.car.deja.cotisation.active",
                        decisionCap.getDateDebut());
            }

            createNewCotisationForFacturationPeriodique(cotisationToUpdate, decisionCap, decisionCap.getDateDebut(), "");

        } else if (isDecisionPartielleForAnneeCouranteWithoutRadiation) {

            AffiliationAssuranceSearchComplexModel cotisationSuivanteSearchModel = searchCotisation(
                    "searchCotisationCAPCGASToUpdateAfterDecision", decisionCap.getDateFin(),
                    decisionCap.getIdAffiliation());

            if (cotisationSuivanteSearchModel.getSize() <= 0) {

                cotisationToUpdate.setDateFin(decisionCap.getDateFin());
                cotisationToUpdate.setMotifFin(CodeSystem.MOTIF_FIN_MASSE);
                cotisationToUpdate = setNewMontantForFacturationPeriodiqueInCotisation(cotisationToUpdate, decisionCap);

                cotisationToUpdate = (CotisationSimpleModel) JadePersistenceManager.update(cotisationToUpdate);

            } else {

                CotisationSimpleModel cotisationSuivante = ((AffiliationAssuranceComplexModel) affiliationAssuranceSearchModel
                        .getSearchResults()[0]).getCotisation();

                if (cotisationToUpdate.getCotisationId().equalsIgnoreCase(cotisationSuivante.getCotisationId())) {

                    String dateDebutForNewCotisation = cotisationToUpdate.getDateDebut();

                    cotisationToUpdate.setDateDebut(new JACalendarGregorian().addDays(decisionCap.getDateFin(), 1));

                    cotisationToUpdate = (CotisationSimpleModel) JadePersistenceManager.update(cotisationToUpdate);

                    createNewCotisationForFacturationPeriodique(cotisationToUpdate, decisionCap,
                            dateDebutForNewCotisation, decisionCap.getDateFin());

                } else {

                    cotisationSuivante.setDateDebut(new JACalendarGregorian().addDays(decisionCap.getDateFin(), 1));

                    cotisationSuivante = (CotisationSimpleModel) JadePersistenceManager.update(cotisationSuivante);

                    cotisationToUpdate.setDateFin(decisionCap.getDateFin());
                    cotisationToUpdate.setMotifFin(CodeSystem.MOTIF_FIN_MASSE);
                    cotisationToUpdate = setNewMontantForFacturationPeriodiqueInCotisation(cotisationToUpdate,
                            decisionCap);

                    cotisationToUpdate = (CotisationSimpleModel) JadePersistenceManager.update(cotisationToUpdate);

                }

            }

        } else {

            cotisationToUpdate = setNewMontantForFacturationPeriodiqueInCotisation(cotisationToUpdate, decisionCap);

            JadePersistenceManager.update(cotisationToUpdate);
        }

    }

    private CotisationSimpleModel setNewMontantForFacturationPeriodiqueInCotisation(
            CotisationSimpleModel cotisationToUpdate, SimpleDecisionCAP decisionCap) {

        cotisationToUpdate.setMontantAnnuel(decisionCap.getCotisationAnnuelle());
        cotisationToUpdate.setMontantTrimestriel(decisionCap.getCotisationTrimestrielle());
        cotisationToUpdate.setMontantMensuel(decisionCap.getCotisationMensuelle());

        return cotisationToUpdate;

    }

    private void createNewCotisationForFacturationPeriodique(CotisationSimpleModel cotisationModele,
            SimpleDecisionCAP decisionCap, String dateDebut, String dateFin) throws Exception {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        Boolean isExceptionPeriodicite = BConstants.DB_BOOLEAN_TRUE.equalsIgnoreCase(cotisationModele
                .getExceptionPeriodicite());
        Boolean isMaisonMere = BConstants.DB_BOOLEAN_TRUE.equalsIgnoreCase(cotisationModele.getMaisonMere());

        AFCotisation newCotisation = new AFCotisation();
        newCotisation.setSession(session);
        newCotisation.setAdhesionId(cotisationModele.getAdhesionId());
        newCotisation.setAnneeDecision(cotisationModele.getAnneeDecision());
        newCotisation.setAssuranceId(cotisationModele.getAssuranceId());
        newCotisation.setCategorieTauxId(cotisationModele.getCategorieTauxId());
        newCotisation.setDateDebut(dateDebut);

        if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            newCotisation.setMotifFin(CodeSystem.MOTIF_FIN_MASSE);
            newCotisation.setDateFin(dateFin);
        }

        newCotisation.setExceptionPeriodicite(isExceptionPeriodicite);
        newCotisation.setMaisonMere(isMaisonMere);
        newCotisation.setMasseAnnuelle(cotisationModele.getMasseAnnuelle());
        newCotisation.setMontantAnnuel(decisionCap.getCotisationAnnuelle());
        newCotisation.setMontantTrimestriel(decisionCap.getCotisationTrimestrielle());
        newCotisation.setMontantMensuel(decisionCap.getCotisationMensuelle());
        newCotisation.setMontantSemestriel(cotisationModele.getMontantSemestriel());
        newCotisation.setPeriodicite(cotisationModele.getPeriodicite());
        newCotisation.setPlanAffiliationId(cotisationModele.getPlanAffiliationId());
        newCotisation.setPlanCaisseId(cotisationModele.getPlanCaisseId());
        newCotisation.setTauxAssuranceId(cotisationModele.getTauxAssuranceId());
        newCotisation.setTraitementMoisAnnee(cotisationModele.getTraitementMoisAnnee());

        newCotisation.add();

    }

    @Override
    public SimpleDecisionCAP updateWithoutCalculCotis(SimpleDecisionCAP decisionCap) throws JadePersistenceException,
            Exception {
        return (SimpleDecisionCAP) JadePersistenceManager.update(decisionCap);
    }
}
