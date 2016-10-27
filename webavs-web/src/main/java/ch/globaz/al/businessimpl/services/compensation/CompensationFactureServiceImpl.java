package ch.globaz.al.businessimpl.services.compensation;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.compensation.CompensationRecapitulatifBusinessModel;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.prestations.ALCompensationPrestationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierFkModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.paiement.CheckAffiliationComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CheckAffiliationSearchComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationFullComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationFullComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.compensation.CompensationFactureService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.prestations.DoublonPrestationsChecker;
import ch.globaz.naos.business.data.AssuranceInfo;

/**
 * Implémentation du service permettant la gestion des compensations sur facture
 * 
 * @author jts
 * 
 */
public class CompensationFactureServiceImpl extends ALAbstractBusinessServiceImpl implements CompensationFactureService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationFactureServiceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService #annulerPreparation(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void annulerPreparation(String periodeA, String typeCoti) throws JadePersistenceException,
            JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#annulerPreparation : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#annulerPreparation : "
                    + typeCoti + " is not valid");
        }

        ALServiceLocator.getPrestationBusinessService().updateEtat(
                searchPrestations(periodeA, typeCoti, ALCSPrestation.ETAT_TR), ALCSPrestation.ETAT_SA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService #annulerPreparation(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void annulerPreparationByNumProcessus(String idProcessus) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#annulerPreparationByNumProcessus : " + idProcessus
                            + " is not a valid num processus");
        }

        ALServiceLocator.getPrestationBusinessService().updateEtat(
                searchPrestationsByNumProcessus(idProcessus, ALCSPrestation.ETAT_TR), ALCSPrestation.ETAT_SA);
    }

    /**
     * Prépare les récaps pour l'impression des protocoles
     * 
     * @param search Modèle de recherche. la recherche doit avoir été exécutée
     * @param rubrSep Indique si les enregistrement dans la liste doivent être séparé par rubrique comptable en plus des
     *            critères standard (affilié et genre d'assurance)
     * @return Liste des récap préparées ( <code>CompensationBusinessModel</code>)
     * @throws JadeApplicationException Exception levée si <code>search</code> est <code>null</code>
     */
    @Override
    public Collection<CompensationBusinessModel> buildRecaps(CompensationPrestationFullComplexSearchModel search,
            boolean rubrSep) throws JadeApplicationException {

        if (search == null) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecaps : search is null");
        }
        // il faut un model par numcpt et pas par récap,
        // la clé est idrecap-numcpt, car 1 ligne par récap et par compte
        HashMap<String, CompensationBusinessModel> recaps = new HashMap<String, CompensationBusinessModel>();

        for (int i = 0; i < search.getSize(); i++) {

            CompensationPrestationFullComplexModel line = (CompensationPrestationFullComplexModel) (search
                    .getSearchResults()[i]);
            CompensationBusinessModel model = null;

            if (rubrSep) {
                model = recaps.get(line.getIdRecap() + "-" + line.getNumeroCompte());
            } else {
                model = recaps.get(line.getIdRecap());
            }

            if (model == null) {
                model = new CompensationBusinessModel(line.getIdRecap(), line.getNomAffilie(), line.getNumeroAffilie(),
                        line.getNumeroFacture(), line.getNumeroCompte(), line.getPeriodeRecapDe(),
                        line.getPeriodeRecapA(), line.getGenreAssurance());
                // Inforom409
                // model.setEtatDossier(line.getEtatDossier());
            }

            model.addMontant(line.getMontantDetail());

            if (rubrSep) {
                model = recaps.put(line.getIdRecap() + "-" + line.getNumeroCompte(), model);
            } else {
                model = recaps.put(line.getIdRecap(), model);
            }
        }

        ArrayList<CompensationBusinessModel> list = new ArrayList<CompensationBusinessModel>(recaps.values());
        Collections.sort(list);
        return list;
    }

    /**
     * Prépare les rubriques correspondant au résultat de la recherche passé en paramètre. Elle se charge de calculer la
     * somme des prestations (au crédit et au débit) pour chaque rubrique comptable
     * 
     * @param search Modèle de recherche. la recherche doit avoir été exécutée
     * @return Liste des récap préparées ( <code>CompensationBusinessModel</code>)
     * @throws JadeApplicationException Exception levée si <code>search</code> est <code>null</code>
     */
    private Collection<CompensationRecapitulatifBusinessModel> buildRubriques(
            CompensationPrestationComplexSearchModel search) throws JadeApplicationException {

        if (search == null) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRubriques : search is null");
        }

        HashMap<String, CompensationRecapitulatifBusinessModel> recaps = new HashMap<String, CompensationRecapitulatifBusinessModel>();

        for (int i = 0; i < search.getSize(); i++) {

            CompensationPrestationComplexModel line = (CompensationPrestationComplexModel) (search.getSearchResults()[i]);

            CompensationRecapitulatifBusinessModel model = recaps.get(line.getNumeroCompte());

            if (model == null) {
                model = new CompensationRecapitulatifBusinessModel(line.getNumeroCompte());
            }

            if (JadeNumericUtil.isNumericPositif(line.getMontantDetail())) {
                model.addCredit(line.getMontantDetail());
            } else {
                model.addDebit(line.getMontantDetail());
            }

            recaps.put(line.getNumeroCompte(), model);
        }

        ArrayList<CompensationRecapitulatifBusinessModel> list = new ArrayList<CompensationRecapitulatifBusinessModel>(
                recaps.values());
        Collections.sort(list);
        return list;
    }
	
    // JIRA IN-2732 et WEBAVS-3155: Traitement de simulation de compensation du processus paritaire principale en erreur
    // à cause d'un IN SQL trop grand, on découpe donc notre liste en sous-listes de N éléments
    private static final int IN_MAX = 1000;

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService #checkRecaps(java.util.ArrayList,
     * ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public ProtocoleLogger checkRecaps(Collection<CompensationBusinessModel> recaps, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException {

        if (recaps == null) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#checkRecaps : recaps is null");
        }

        if (logger == null) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#checkRecaps : logger is null");
        }

        // préparation de la liste des récaps
        HashSet<String> idsRecap = new HashSet<String>();
        Iterator<CompensationBusinessModel> it = recaps.iterator();
        while (it.hasNext()) {

            CompensationBusinessModel recap = it.next();

            idsRecap.add(recap.getIdRecap());

            logger.getInfosLogger(recap.getNumeroAffilie(), recap.getNomAffilie()).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.INFO, CompensationFactureServiceImpl.class
                            .getName(), "al.protocoles.compensation.diagnostique.affilie.compensee", new String[] {
                            recap.getPeriodeRecapDe(), recap.getPeriodeRecapA() }));
        }

        checkDoublons(recaps, logger);

		
        /*
         * JIRA IN-2732 et WEBAVS-3155: On découpe le travail en lots de max IN_MAX éléments, pour ne pas faire sauter
         * la requête SQL.
         * TODO : ça mériterait d'être supprimé lorsque le framework saura gérer correctement les limites de tailles
         * pour un champ de recherche utilisant l'opérateur "IN".
         */
        List<String> listIdsRecap = new ArrayList<String>(idsRecap);

        double lots = Math.ceil(((double) listIdsRecap.size()) / IN_MAX);
        LOG.debug("will iterate {} times to cover {} elements in the IN criteria", lots, listIdsRecap.size());

        for (int lotIndex = 0; lotIndex < lots; lotIndex++) {
            List<String> lotElements = listIdsRecap.subList(lotIndex * IN_MAX,
                    Math.min((lotIndex + 1) * IN_MAX, listIdsRecap.size()));
            LOG.debug("  searching lot {} with {} elements in the IN criteria", lotIndex, lotElements.size());
		
            CheckAffiliationSearchComplexModel search = new CheckAffiliationSearchComplexModel();
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            // on lance que si il y a des récaps
            search.setInIdRecap(idsRecap);
            search = (CheckAffiliationSearchComplexModel) JadePersistenceManager.search(search);        

            // Map contenant les avertissement (key) en cours de traitement dans la boucle for
            Map<String, Object> avertissementEnTraitement = new HashMap<String, Object>();
            // Map contenant pour chaque affilié (key) leurs avertissements (value)
            Map<String, Map<String, Object>> affilieTraiter = new HashMap<String, Map<String, Object>>();

            for (int i = 0; i < search.getSize(); i++) {

                CheckAffiliationComplexModel model = ((CheckAffiliationComplexModel) search.getSearchResults()[i]);

                // Si un affilié réapparait pour une récap, nous prenons sa map d'avertissement
                if (affilieTraiter.containsKey(model.getNumeroAffilie())) {
                    avertissementEnTraitement = affilieTraiter.get(model.getNumeroAffilie());
                } else { // autrement nous créons une nouvelle map d'avertissement pour l'affilié pas encore été traité
                    avertissementEnTraitement = new HashMap<String, Object>();
                    affilieTraiter.put(model.getNumeroAffilie(), avertissementEnTraitement);
                }

                AssuranceInfo assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                        model.getNumeroAffilie(), model.getActiviteAllocataire(), "01." + model.getPeriodeDe());

                if (ALCSDossier.ETAT_RADIE.equals(model.getEtatDossier())) {
                    // pour le dossier chercher toute les prestation égale ou psostérieure à période A: faire le totales de
                    // ces prstations
                    // si le total est égal à 0 pas d'avertissement, sinon avertissement (totale négatif ou positif)

                    try {
                        JACalendarGregorian dateUtil = new JACalendarGregorian();
                        if (dateUtil.compare(new JADate("30." + model.getPresPeriodeA()),
                                new JADate(model.getFinValidite())) == JACalendar.COMPARE_FIRSTUPPER) {

                            // si dossier radier voir le montant total des prestations posterieures à fin de validité du
                            // dossie
                            String montantPresta = ALImplServiceLocator.getGenerationService()
                                    .totalMontantPrestaGenereDossierPeriode(model.getIdDossier(), model.getIdRecap(),
                                            JadeStringUtil.substring(model.getFinValidite(), 3, 7));
                            if (!JadeStringUtil.isBlankOrZero(montantPresta)) {

                                logger.getWarningsLogger(model.getNumeroAffilie(), assuranceInfo.getDesignation())
                                        .addMessage(
                                                new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                                        CompensationFactureServiceImpl.class.getName(),
                                                        "al.protocoles.compensation.dossier.radie", new String[] { model
                                                                .getIdDossier() }));
                            }
                        }
                    } catch (JAException e) {
                        throw new ALCompensationPrestationException("JADate error", e);
                    }
                }

                String[] params = { model.getPeriodeDe(), model.getPeriodeA() };
                if (!assuranceInfo.getWarningsContainer().isEmpty()) {
                    for (String warn : assuranceInfo.getWarningsContainer()) {
                        // Nous insérons dans le mail/liste qu'une seule fois un type d'avertissement pour un affilié
                        if (!avertissementEnTraitement.containsKey(warn)) {
                            logger.getWarningsLogger(model.getNumeroAffilie(), assuranceInfo.getDesignation()).addMessage(
                                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                            CompensationFactureServiceImpl.class.getName(), warn, params));

                            avertissementEnTraitement.put(warn, assuranceInfo);
                        }
                    }
                } else if (!assuranceInfo.getCouvert()) {

                    logger.getWarningsLogger(model.getNumeroAffilie(), assuranceInfo.getDesignation()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                                    .getName(), "al.protocoles.compensation.diagnostique.affilie.inactif", params));
                }
			}	
        }

        return logger;
    }

    private void checkDoublons(Collection<CompensationBusinessModel> recaps, ProtocoleLogger logger)
            throws JadeApplicationException {

        Map<String, List<String>> listeIdEnteteParDossier = new HashMap<String, List<String>>();
        Map<String, String> nomsAffilies = new HashMap<String, String>();

        for (CompensationBusinessModel compensationBusinessModel : recaps) {
            populateListeIdEnteteParDossier(logger, listeIdEnteteParDossier, nomsAffilies, compensationBusinessModel);
        }

        for (Entry<String, List<String>> entry : listeIdEnteteParDossier.entrySet()) {
            checkDoublonsEntetesDossier(logger, nomsAffilies, entry);
        }
    }

    private void checkDoublonsEntetesDossier(ProtocoleLogger logger, Map<String, String> nomsAffilies,
            Entry<String, List<String>> entry) throws JadeApplicationException {
        String idDossier = null;
        String numAffilie = "?";
        String nomAffilie = "-";

        try {

            List<String> idEnteteDossier = entry.getValue();
            idDossier = entry.getKey();

            boolean hasDoubleEntetePrestation = DoublonPrestationsChecker.hasDoubleEntetePrestation(idEnteteDossier);
            boolean hasDoubleDetailPrestation = DoublonPrestationsChecker.hasDoubleDetailPrestation(idEnteteDossier);

            if (hasDoubleEntetePrestation || hasDoubleDetailPrestation) {
                DossierSearchModel dossierSearch = new DossierSearchModel();
                dossierSearch.setForIdDossier(idDossier);
                dossierSearch = ALServiceLocator.getDossierModelService().search(dossierSearch);

                if (dossierSearch.getSize() == 1) {
                    numAffilie = ((DossierFkModel) dossierSearch.getSearchResults()[0]).getNumeroAffilie();
                    nomAffilie = nomsAffilies.get(numAffilie);
                }
            }

            if (hasDoubleEntetePrestation) {

                logger.getWarningsLogger(numAffilie, nomAffilie).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                                .getName(), "al.protocoles.compensation.doubleEntetePrestation",
                                new String[] { idDossier }));
            }

            if (hasDoubleDetailPrestation) {
                logger.getWarningsLogger(numAffilie, nomAffilie).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                                .getName(), "al.protocoles.compensation.doubleDetailsPrestation",
                                new String[] { idDossier }));
            }
        } catch (Exception e) {
            logger.getWarningsLogger("?", "?").addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                            .getName(), "al.protocoles.compensation.checkDoublePrestation.error",
                            new String[] { idDossier }));

        }
    }

    private void populateListeIdEnteteParDossier(ProtocoleLogger logger,
            Map<String, List<String>> listeIdEnteteParDossier, Map<String, String> nomsAffilies,
            CompensationBusinessModel compensationBusinessModel) throws JadeApplicationException {
        try {

            nomsAffilies.put(compensationBusinessModel.getNumeroAffilie(), compensationBusinessModel.getNomAffilie());

            EntetePrestationSearchModel search = new EntetePrestationSearchModel();
            search.setForIdRecap(compensationBusinessModel.getIdRecap());
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            search = ALImplServiceLocator.getEntetePrestationModelService().search(search);

            for (JadeAbstractModel item : search.getSearchResults()) {
                EntetePrestationModel entete = (EntetePrestationModel) item;
                if (!listeIdEnteteParDossier.containsKey(entete.getIdDossier())) {
                    listeIdEnteteParDossier.put(entete.getIdDossier(), new ArrayList<String>());
                }

                listeIdEnteteParDossier.get(entete.getIdDossier()).add(entete.getIdEntete());
            }
        } catch (Exception e) {
            logger.getWarningsLogger(compensationBusinessModel.getNumeroAffilie(),
                    compensationBusinessModel.getNomAffilie()).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, CompensationFactureServiceImpl.class
                            .getName(), "al.protocoles.compensation.checkDoublePrestation.affilie.error"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService
     * #loadRecapsDefinitif(java.lang.String, java.util.Collection, boolean)
     */
    @Override
    public Collection<CompensationBusinessModel> loadRecapsDefinitif(String idPassage,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecapsDefinitif : "
                    + idPassage + " is not a positif integer");
        }

        if ((activitesAlloc == null) || (activitesAlloc.size() == 0)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#loadRecapsDefinitif : activitesAlloc is null or empty ");
        }

        // activitesAlloc peut être vide ou null

        CompensationPrestationFullComplexSearchModel search = new CompensationPrestationFullComplexSearchModel();
        search.setForIdPassage(idPassage);
        search.setInActivites(activitesAlloc);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (CompensationPrestationFullComplexSearchModel) JadePersistenceManager.search(search);

        return buildRecaps(search, rubrSep);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService
     * #loadRecapsSimulation(java.lang.String, java.util.Collection, boolean)
     */
    @Override
    public Collection<CompensationBusinessModel> loadRecapsSimulation(String periodeA,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecapsSimulation : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        if ((activitesAlloc == null) || (activitesAlloc.size() == 0)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#loadRecapsSimulation : activitesAlloc is null or empty ");
        }

        // activitesAlloc peut être vide ou null
        CompensationPrestationFullComplexSearchModel search = new CompensationPrestationFullComplexSearchModel();
        search.setForPeriodeA(periodeA);
        search.setForEtat(ALCSPrestation.ETAT_SA);
        search.setForBonification(ALCSPrestation.BONI_INDIRECT);
        search.setInActivites(activitesAlloc);
        search.setForIdProcessusPeriodique("0");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (CompensationPrestationFullComplexSearchModel) JadePersistenceManager.search(search);

        return buildRecaps(search, rubrSep);
    }

    @Override
    public Collection<CompensationBusinessModel> loadRecapsSimulationByNumProcessus(String idProcessus,
            Collection<String> activitesAlloc, boolean rubrSep) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecapsSimulation : "
                    + idProcessus + " is not a valid number of processus");
        }

        // activitesAlloc peut être vide ou null
        CompensationPrestationFullComplexSearchModel search = new CompensationPrestationFullComplexSearchModel();
        search.setForEtat(ALCSPrestation.ETAT_SA);
        search.setInActivites(activitesAlloc);
        search.setForIdProcessusPeriodique(idProcessus);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (CompensationPrestationFullComplexSearchModel) JadePersistenceManager.search(search);

        return buildRecaps(search, rubrSep);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService
     * #loadRubriquesDefinitif(java.lang.String)
     */
    @Override
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesDefinitif(String idPassage)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecapsDefinitif : "
                    + idPassage + " is not a positif integer");
        }

        CompensationPrestationComplexSearchModel search = new CompensationPrestationComplexSearchModel();
        search.setForIdPassage(idPassage);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (CompensationPrestationComplexSearchModel) JadePersistenceManager.search(search);

        return buildRubriques(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService
     * #loadRubriquesSimulation(java.lang.String, java.lang.String)
     */
    @Override
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulation(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecaps : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#loadRecaps : " + typeCoti
                    + " is not valid");
        }

        CompensationPrestationComplexSearchModel search = searchPrestations(periodeA, typeCoti, ALCSPrestation.ETAT_SA);

        return buildRubriques(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.compensation.CompensationFactureService
     * #loadRubriquesSimulation(java.lang.String, java.lang.String)
     */
    @Override
    public Collection<CompensationRecapitulatifBusinessModel> loadRubriquesSimulationByNumProcessus(String idProcessus)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#loadRubriquesSimulationByNumProcessus : " + idProcessus
                            + " is not a valid num processus");
        }

        CompensationPrestationComplexSearchModel search = searchPrestationsByNumProcessus(idProcessus,
                ALCSPrestation.ETAT_SA);

        return buildRubriques(search);
    }

    @Override
    public void preparerCompensation(String periodeA, String typeCoti) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#preparerCompensation : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#preparerCompensation : "
                    + typeCoti + " is not valid");
        }

        ALServiceLocator.getPrestationBusinessService().updateEtat(
                searchPrestations(periodeA, typeCoti, ALCSPrestation.ETAT_SA), ALCSPrestation.ETAT_TR);

    }

    @Override
    public void preparerCompensationByNumProcessus(String idProcessus) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#preparerCompensationByNumProcessus : " + idProcessus
                            + " is not a valid num processus");
        }

        ALServiceLocator.getPrestationBusinessService().updateEtat(
                searchPrestationsByNumProcessus(idProcessus, ALCSPrestation.ETAT_SA), ALCSPrestation.ETAT_TR);

    }

    /**
     * Recherche les prestations correspondant aux critères passés en paramètre
     * 
     * Les critères sont :
     * <ul>
     * <li>période (paramètre)</li>
     * <li>état (paramètre)</li>
     * <li>bonification indirecte</li>
     * <li>type de coti (paramètre)</li>
     * </ul>
     * 
     * @param periodeA Période traitée (format MM.AAAA)
     * @param typeCoti type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param etat état des prestation/récap à rechercher
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * @return Résultat de la recherche
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    private CompensationPrestationComplexSearchModel searchPrestations(String periodeA, String typeCoti, String etat)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#searchPrestations : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALCompensationPrestationException("CompensationFactureServiceImpl#searchPrestations : "
                    + typeCoti + " is not valid");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALCompensationPrestationException("CompensationFactureServiceImpl#searchPrestations : "
                        + etat + " is not valid");
            }
        } catch (Exception e) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#searchPrestations : unable to check code system", e);
        }

        CompensationPrestationComplexSearchModel search = new CompensationPrestationComplexSearchModel();
        search.setForPeriodeA(periodeA);
        search.setForEtat(etat);
        search.setForIdProcessusPeriodique("0");
        search.setForBonification(ALCSPrestation.BONI_INDIRECT);
        search.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti));
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return (CompensationPrestationComplexSearchModel) JadePersistenceManager.search(search);
    }

    /**
     * Recherche les prestations correspondant aux critères passés en paramètre
     * 
     * Les critères sont :
     * <ul>
     * <li>période (paramètre)</li>
     * <li>état (paramètre)</li>
     * <li>bonification indirecte</li>
     * <li>type de coti (paramètre)</li>
     * </ul>
     * 
     * @param periodeA Période traitée (format MM.AAAA)
     * @param typeCoti type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param etat état des prestation/récap à rechercher
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * @return Résultat de la recherche
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    private CompensationPrestationComplexSearchModel searchPrestationsByNumProcessus(String idProcessus, String etat)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#searchPrestationsByNumProcessus : " + idProcessus
                            + " is not a valid num processus");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALCompensationPrestationException(
                        "CompensationFactureServiceImpl#searchPrestationsByNumProcessus : " + etat + " is not valid");
            }
        } catch (Exception e) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureServiceImpl#searchPrestations : unable to check code system", e);
        }

        CompensationPrestationComplexSearchModel search = new CompensationPrestationComplexSearchModel();
        search.setForIdProcessusPeriodique(idProcessus);
        search.setForEtat(etat);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return (CompensationPrestationComplexSearchModel) JadePersistenceManager.search(search);
    }
}