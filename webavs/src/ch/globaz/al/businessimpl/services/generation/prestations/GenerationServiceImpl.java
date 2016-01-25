package ch.globaz.al.businessimpl.services.generation.prestations;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.math.BigDecimal;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexModel;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.generation.prestations.GenerationService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;
import ch.globaz.al.businessimpl.generation.prestations.managers.GenerationDossierHandler;
import ch.globaz.al.businessimpl.generation.prestations.managers.MainGenerationHandler;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Implémentation du service de génération de prestations
 * 
 * @author jts
 */
public class GenerationServiceImpl extends ALAbstractBusinessServiceImpl implements GenerationService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.generation.prestations.GenerationService#generationAffilie(ch.globaz.al.businessimpl
     * .generation.prestations.context.ContextGlobal)
     */
    @Override
    public void generationAffilie(ContextGlobal context) throws JadeApplicationException {

        if (context == null) {
            throw new ALGenerationException("GenerationServiceImpl#generationAffilie : context is null");
        }

        ContextAffilie contextAffilie = context.getContextAffilie();

        // exécution de la génération pour chaque dossier de l'affilié
        for (int i = 0; i < context.getDossiers().length; i++) {

            contextAffilie.initContextDossier((DossierComplexModel) context.getDossiers()[i],
                    contextAffilie.getDebutRecap(), contextAffilie.getFinRecap(), "0", Bonification.AUTO, "1",
                    ALConstPrestations.TypeGeneration.STANDARD);

            GenerationDossierHandler thread = new GenerationDossierHandler(contextAffilie);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                // DO NOTHING
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.prestations.GenerationService#generationAffilie(java.lang.String,
     * java.lang.String, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public ProtocoleLogger generationAffilie(String numAffilie, String periode, String numGeneration,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALGenerationException("GenerationServiceImpl#generationAffilie : numAffilie is null or empty");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#generationAffilie : " + periode
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeNumericUtil.isInteger(numGeneration)) {
            throw new ALGenerationException("GenerationServiceImpl#generationGlobale : " + numGeneration
                    + "is not valid");
        }

        // récupération des dossiers de l'affilié
        DossierComplexSearchModel dsm = initSearchDossiers(periode, null);
        dsm.setForNumeroAffilie(numAffilie);
        dsm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        dsm = ALServiceLocator.getDossierComplexModelService().search(dsm);

        // récupération des informations de l'affilié
        AffilieListComplexSearchModel affilie = new AffilieListComplexSearchModel();
        affilie.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        affilie.setForNumeroAffilie(numAffilie);
        affilie = ALImplServiceLocator.getAffilieListComplexModelService().search(affilie);

        if (affilie.getSize() != 1) {
            throw new ALGenerationException("GenerationServiceImpl#generationAffilie : unable to load data for "
                    + numAffilie);
        }

        String periodicite = ((AffilieListComplexModel) affilie.getSearchResults()[0]).getPeriodicite();

        // Initialisation du context
        ContextAffilie contextAffilie = ContextAffilie.getContextAffilie(
                ALDateUtils.getDebutPeriode(periode, periodicite), periode, ALCSPrestation.GENERATION_TYPE_GEN_AFFILIE,
                numAffilie, periodicite, "0", logger, JadeThread.currentContext().getContext());

        // exécution
        this.generationAffilie(ContextGlobal.getInstance(dsm.getSearchResults(), contextAffilie));

        return logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.prestations.GenerationService#generationGlobale(java.lang.String,
     * java.lang.String, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)
     */
    @Override
    public ProtocoleLogger generationGlobale(String periode, String typeCot, String numGeneration,
            ProtocoleLogger logger) throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#generationGlobale : " + periode
                    + "is not a valid period");
        }

        if (!ALConstPrestations.TYPE_COT_PAR.equals(typeCot) && !ALConstPrestations.TYPE_COT_PERS.equals(typeCot)
                && !ALConstPrestations.TYPE_DIRECT.equals(typeCot)
                && !ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCot)) {
            throw new ALGenerationException("GenerationServiceImpl#generationGlobale : " + typeCot
                    + "is not a valid cotisation type");
        }

        if (JadeNumericUtil.isEmptyOrZero(numGeneration)) {
            throw new ALGenerationException("GenerationServiceImpl#generationGlobale : " + numGeneration
                    + "is not valid");
        }

        ContextTucana.initContext("01." + periode);

        MainGenerationHandler.newInstance().handle(periode, typeCot, numGeneration, logger);

        return logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.prestations.GenerationService#getPeriodicites(java.lang.String)
     */
    @Override
    public HashSet<String> getPeriodicites(String periode) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#getPeriodicites : " + periode
                    + " is not a valid period");
        }

        HashSet<String> set = new HashSet<String>();

        int mois = Integer.parseInt(periode.substring(0, 2));

        switch (mois) {
            case 1:
            case 2:
            case 4:
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
                set.add(ALCSAffilie.PERIODICITE_MEN);
                break;

            case 3:
            case 6:
            case 9:
                set.add(ALCSAffilie.PERIODICITE_TRI);
                set.add(ALCSAffilie.PERIODICITE_MEN);
                break;

            case 12:
                set.add(ALCSAffilie.PERIODICITE_TRI);
                set.add(ALCSAffilie.PERIODICITE_MEN);
                set.add(ALCSAffilie.PERIODICITE_ANN);
                break;

            default:
                throw new ALGenerationException("GenerationServiceImpl#getPeriodicites : " + mois
                        + " case is not supported");
        }

        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.prestations.GenerationService#initSearchAffilies(java.lang.String,
     * java.lang.String)
     */
    @Override
    public AffilieListComplexSearchModel initSearchAffilies(String periode, String typeCoti)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#initSearchAffilies : " + periode
                    + " is not a valid period");
        }

        AffilieListComplexSearchModel affilies = new AffilieListComplexSearchModel();

        // en cas de paiement direct on traite tous les dossiers mensuellement
        // quel que soit la périodicité pour l'activité de l'allocataire
        if (ALConstPrestations.TYPE_DIRECT.equals(typeCoti)) {
            affilies.setWhereKey("listForGenerationPaiementDirect");
        } else {

            // pour les paiements indirect on tient compte de la périodicité et
            // du type de cotisation (déterminé par l'activité de
            // l'allocataire). Les deux types peuvent aussi être traités
            // ensemble
            affilies.setInPeriodicites(getPeriodicites(periode));

            if (ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)) {
                affilies.setWhereKey("listForGeneration");
            } else if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
                affilies.setWhereKey("listForGenerationCotPar");
                // NOT IN définit au niveau de la description du modèle (fichier
                // ALAffilieListComplexModel.xml)
                affilies.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesCategorieCotPers());
            } else if (ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
                affilies.setWhereKey("listForGenerationCotPers");
                affilies.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesCategorieCotPers());
            } else {
                throw new ALGenerationException("GenerationServiceImpl#initSearchAffilies : " + typeCoti
                        + " is not a valid cotisation type");
            }
        }

        affilies.setForDate("01." + periode);
        // nécessaire que pour les indirects, car en direct, les trimestriels et annuels sont générés comme les mensuels
        affilies.setForDateDebutTrimestre("01."
                + ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(periode));
        affilies.setForDateDebutAnnee("01.01." + periode.substring(3));

        return affilies;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.prestations.GenerationService#initSearchDossiers(java.lang.String,
     * java.lang.String)
     */
    @Override
    public DossierComplexSearchModel initSearchDossiers(String debutPeriode, String typeCoti)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(debutPeriode)) {
            throw new ALGenerationException("GenerationServiceImpl#initSearchAffilies : " + debutPeriode
                    + " is not a valid period");
        }

        if ((typeCoti != null)
                && (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                        && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                        && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti) && !ALConstPrestations.TYPE_DIRECT
                            .equals(typeCoti))) {
            throw new ALGenerationException("GenerationServiceImpl#initSearchDossiers : " + typeCoti
                    + " is not a valid cotisation type");
        }

        DossierComplexSearchModel searchDossiers = new DossierComplexSearchModel();
        searchDossiers.setForFinValidite("01." + debutPeriode);

        if (typeCoti == null) {
            searchDossiers.setWhereKey(ALConstPrestations.SEARCH_DEFINITION_GENERATION);
        } else if (ALConstPrestations.TYPE_DIRECT.equals(typeCoti)) {
            searchDossiers.setWhereKey(ALConstPrestations.SEARCH_DEFINITION_GENERATION_DIRECT);
        } else if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
            searchDossiers.setWhereKey(ALConstPrestations.SEARCH_DEFINITION_GENERATION_INDIRECT_PAR);
            searchDossiers.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesCategorieCotPers());
        } else if (ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            searchDossiers.setWhereKey(ALConstPrestations.SEARCH_DEFINITION_GENERATION_INDIRECT_PERS);
            searchDossiers.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesCategorieCotPers());
        } else if (ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)) {
            searchDossiers.setWhereKey(ALConstPrestations.SEARCH_DEFINITION_GENERATION_INDIRECT);
        } else {
            throw new ALGenerationException("GenerationServiceImpl#initSearchDossiers : " + typeCoti
                    + " is not a valid cotisation type");
        }

        // on génère pour les dossier qui ne sont pas de type IS
        HashSet<String> setStatut = new HashSet<String>();
        setStatut.add(ALCSDossier.STATUT_IS);
        searchDossiers.setInStatut(setStatut);

        searchDossiers.setInActivites(ALServiceLocator.getDossierBusinessService().getActivitesCategorieCotPers());

        // on calcul des dossiers radiés après la date de génération
        searchDossiers.setForEtat(ALCSDossier.ETAT_RADIE);

        HashSet<String> setEtats = (HashSet<String>) ALServiceLocator.getDossierBusinessService()
                .listerEtatsOkGenerationGlobale();

        searchDossiers.setInEtat(setEtats);

        return searchDossiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.generation.prestations.GenerationService#totalMontantPrestaGenereDossierPeriode
     * (java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String totalMontantPrestaGenereDossierPeriode(String idDossier, String idRecap, String periodeValidite)
            throws JadeApplicationException, JadePersistenceException {
        // cntrôle des paramètres
        if (JadeStringUtil.isBlankOrZero(idDossier)) {
            throw new ALGenerationException(
                    "GenerationServiceImpl#totalMontantPrestaGenereDossierPeriode :idDossier is empty ");
        }
        if (JadeStringUtil.isBlankOrZero(idRecap)) {
            throw new ALGenerationException(
                    "GenerationServiceImpl#totalMontantPrestaGenereDossierPeriode :idRecap is empty ");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeValidite)) {
            throw new ALGenerationException("GenerationServiceImpl#totalMontantPrestaGenereDossierPeriode : periode: "
                    + periodeValidite + " is not a valid period");

        }
        // recherche de toutes les prestations du dossier lié à la récap avec période de validité postérieure à la
        // période

        DetailPrestationGenComplexSearchModel detailPrest = new DetailPrestationGenComplexSearchModel();
        detailPrest.setForIdDossier(idDossier);
        detailPrest.setForDateDebut(periodeValidite);
        detailPrest.setForIdRecap(idRecap);
        detailPrest.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        detailPrest.setWhereKey("TotalPrestatationsPeriode");

        detailPrest = ALImplServiceLocator.getDetailPrestationGenComplexModelService().search(detailPrest);

        BigDecimal total = new BigDecimal(0.00);

        // calcul le montant total
        for (int i = 0; i < detailPrest.getSize(); i++) {
            DetailPrestationGenComplexModel presta = (DetailPrestationGenComplexModel) detailPrest.getSearchResults()[i];

            total = total.add(new BigDecimal(presta.getMontant().toString()));
        }

        String montant = total.toString();

        return montant;
    }
}