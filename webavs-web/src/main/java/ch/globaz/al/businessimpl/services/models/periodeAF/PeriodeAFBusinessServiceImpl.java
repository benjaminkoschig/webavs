package ch.globaz.al.businessimpl.services.models.periodeAF;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.model.periodeAF.ALPeriodeAFException;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFSearchModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.BusinessProcessusFactory;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Impl�mentation des services m�tier li�s � la p�riode AF
 * 
 * @author GMO
 * 
 */
public class PeriodeAFBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements PeriodeAFBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService #closePeriode(java.lang.String)
     */
    @Override
    public PeriodeAFModel closePeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlank(datePeriode)) {
            throw new ALPeriodeAFException(this.getClass()
                    + "#closePeriode - unable to close periode, datePeriode is empty");
        }

        PeriodeAFSearchModel searchPeriode = new PeriodeAFSearchModel();
        searchPeriode.setForDatePeriode(datePeriode);
        searchPeriode = ALServiceLocator.getPeriodeAFModelService().search(searchPeriode);
        if (searchPeriode.getSize() > 1) {
            throw new ALPeriodeAFException(this.getClass()
                    + "- unable to close periode - too many periodeAF in database");
        }

        PeriodeAFModel periodeAF = new PeriodeAFModel();

        // si la p�riode existe d�j�, on la retourne si elle est ouverte, sinon
        // on remonte une exception
        if (searchPeriode.getSize() == 1) {

            if (ALCSProcessus.ETAT_OUVERT.equals(((PeriodeAFModel) searchPeriode.getSearchResults()[0]).getEtat())) {
                // on la ferme et on l'update
                periodeAF = (PeriodeAFModel) searchPeriode.getSearchResults()[0];
                periodeAF.setEtat(ALCSProcessus.ETAT_TERMINE);
                periodeAF = ALServiceLocator.getPeriodeAFModelService().upate(periodeAF);

            } // par contre si elle n'est pas ouverte, on ne fait rien, elle est
              // d�j� ferm�e
        } else {

            // si la p�riode d�sir�e n'existe pas, on la cr�� et on la ferme
            // direct
            periodeAF.setDatePeriode(datePeriode);
            periodeAF.setEtat(ALCSProcessus.ETAT_TERMINE);
            periodeAF = (PeriodeAFModel) JadePersistenceManager.add(periodeAF);

        }
        return periodeAF;
    }

    @Override
    public PeriodeAFModel getNextPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(datePeriode)) {
            throw new ALPeriodeAFException("PeriodAFBusinessServiceImpl#getNextPeriode : " + datePeriode
                    + " is not a valid period");
        }
        return getPeriode(datePeriode, 1);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService #getPeriodeEnCours()
     */
    /**
     * Retourne la p�riode correspondant � <code>datePeriode</code> plus ou moins <code>moisToAdd</code>
     * 
     * @param datePeriode
     *            p�riode initiale
     * @param moisToAdd
     *            nombre de mois � ajouter ou � supprimer � la p�riode
     * @return la nouvelle p�riode
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private PeriodeAFModel getPeriode(String datePeriode, int moisToAdd) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(datePeriode)) {
            throw new ALPeriodeAFException("PeriodAFBusinessServiceImpl#getPeriode : " + datePeriode
                    + " is not a valid period");
        }

        String resultDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(moisToAdd,
                ALDateUtils.getCalendarDate("01.".concat(datePeriode))).getTime());

        PeriodeAFSearchModel searchPeriodeModel = new PeriodeAFSearchModel();
        searchPeriodeModel.setForDatePeriode(resultDate.substring(3));
        searchPeriodeModel = ALServiceLocator.getPeriodeAFModelService().search(searchPeriodeModel);

        if (searchPeriodeModel.getSize() > 1) {
            throw new ALPeriodeAFException(
                    "PeriodAFBusinessServiceImpl#getPeriode: unable to get period - too many periods found");
        }

        if (searchPeriodeModel.getSize() == 0) {

            if (moisToAdd == 1) {
                return openPeriode(resultDate.substring(3));
            }
            if (moisToAdd == -1) {
                return closePeriode(resultDate.substring(3));
            }
        }

        return (PeriodeAFModel) searchPeriodeModel.getSearchResults()[0];
    }

    @Override
    public String getPeriodeDebutTrimestre(String datePeriode) throws JadeApplicationException,
            JadePersistenceException {
        if (!JadeDateUtil.isGlobazDateMonthYear(datePeriode)) {
            throw new ALPeriodeAFException(
                    "PeriodeAFBusinessServiceImpl#getPeriodeDebutTrimestre: unable to determine fin trimestre, datePeriod must be MM.yyyy");
        }

        String anneeOnly = datePeriode.substring(3);
        String monthOnly = datePeriode.substring(0, 2);
        String result = "";

        if (Integer.parseInt(monthOnly) > 9) {
            result = "10." + anneeOnly;
        } else if (Integer.parseInt(monthOnly) > 6) {
            result = "07." + anneeOnly;
        } else if (Integer.parseInt(monthOnly) > 3) {
            result = "04." + anneeOnly;
        } else {
            result = "01." + anneeOnly;
        }
        return result;
    }

    @Override
    public PeriodeAFModel getPeriodeEnCours(String bonification, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException {

        PeriodeAFModel thePeriode = null;
        // Recherche sur tous les processus ouvert...
        TemplateTraitementListComplexSearchModel searchModel = new TemplateTraitementListComplexSearchModel();
        searchModel.setForEtatProcessusPeriodique(ALCSProcessus.ETAT_OUVERT);
        HashSet traitements = new HashSet();
        // traitements pris en compte pour la recherche
        if (ALCSPrestation.BONI_DIRECT.equals(bonification)) {
            traitements.add(ALCSProcessus.NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS);
        } else if (ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            traitements.add(ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION);
        }

        searchModel.setInCSTraitement(traitements);
        searchModel.setDefinedSearchSize(0);
        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);

        for (int i = 0; i < searchModel.getSize(); i++) {
            TemplateTraitementListComplexModel currentResult = (TemplateTraitementListComplexModel) searchModel
                    .getSearchResults()[i];

            if (ALCSProcessus.ETAT_OUVERT.equals(currentResult.getProcessusPeriodiqueModel().getEtat())
                    && (ALCSProcessus.ETAT_OUVERT.equals(currentResult.getTraitementPeriodiqueModel().getEtat()) || ALCSProcessus.ETAT_ERREUR
                            .equals(currentResult.getTraitementPeriodiqueModel().getEtat()))) {
                // on r�cup�re le template pour voir si il s'agit d'une horlog�re, si oui, on v�rifie inclut de toute
                // facon les partiels
                ParameterModel ParamAppliedTemplate = ParamServiceLocator.getParameterModelService()
                        .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.TEMPLATE_PROCESSUS,
                                "01." + currentResult.getPeriodeAFModel().getDatePeriode());
                String appliedTemplate = ParamAppliedTemplate.getValeurAlphaParametre();

                if ((includePartiel || ALCSProcessus.NAME_TEMPLATE_HORLO.equals(appliedTemplate))
                        && currentResult.getProcessusPeriodiqueModel().getIsPartiel()) {
                    thePeriode = currentResult.getPeriodeAFModel();

                } else if (!currentResult.getProcessusPeriodiqueModel().getIsPartiel()) {
                    thePeriode = currentResult.getPeriodeAFModel();
                }
            }

        }

        if (thePeriode == null) {
            throw new ALPeriodeAFException(
                    "PeriodeAFBusinessServiceImpl#getPeriodeEnCours : unable to get the last open periode - no open period found");
        }

        return thePeriode;
    }

    @Override
    public PeriodeAFModel getPeriodeEnCours(String bonification, String typeCotisation, boolean includePartiel)
            throws JadeApplicationException, JadePersistenceException {
        PeriodeAFModel thePeriode = null;

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCotisation)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCotisation)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCotisation)
                && !ALConstPrestations.TYPE_DIRECT.equals(typeCotisation)) {
            throw new ALPeriodeAFException("PeriodeAFBusinessServiceImpl#getPeriodeEnCours : " + typeCotisation
                    + " is not valid");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_BONI, bonification)) {
                throw new ALPeriodeAFException("PeriodeAFBusinessServiceImpl#getPeriodeEnCours:" + bonification
                        + " is not a valid bonification type");
            }

        } catch (Exception e) {
            throw new ALPeriodeAFException(
                    "PeriodeAFBusinessServiceImpl#getPeriodeEnCours: unable to check bonification type");
        }

        // Recherche sur tous les processus ouvert...
        TemplateTraitementListComplexSearchModel searchModel = new TemplateTraitementListComplexSearchModel();
        searchModel.setForEtatProcessusPeriodique(ALCSProcessus.ETAT_OUVERT);

        HashSet traitements = new HashSet();
        // traitements pris en compte pour la recherche
        if (ALCSPrestation.BONI_DIRECT.equals(bonification)) {
            traitements.add(ALCSProcessus.NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS);
        } else if (ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            traitements.add(ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION);
        }

        searchModel.setInCSTraitement(traitements);
        searchModel.setDefinedSearchSize(0);
        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);

        for (int i = 0; i < searchModel.getSize(); i++) {
            TemplateTraitementListComplexModel currentResult = (TemplateTraitementListComplexModel) searchModel
                    .getSearchResults()[i];
            // on doit initialiser un instance du businessProcessus pour avoir le type de cotisations g�r�e par les
            // processus trouv�s
            BusinessProcessus businessProcessus = BusinessProcessusFactory.getProcessus(currentResult
                    .getConfigProcessusModel().getBusinessProcessus(), currentResult.getProcessusPeriodiqueModel()
                    .getIsPartiel());

            businessProcessus.setProcessusPeriodiqueModel(currentResult.getProcessusPeriodiqueModel());

            businessProcessus.initialize();
            // on prend en compte que si le traitement est dans une processus avec le type de cotisation voulu
            if (typeCotisation.equals(businessProcessus.getDataCriterias().cotisationCriteria)) {

                if (ALCSProcessus.ETAT_OUVERT.equals(currentResult.getProcessusPeriodiqueModel().getEtat())
                        && (ALCSProcessus.ETAT_OUVERT.equals(currentResult.getTraitementPeriodiqueModel().getEtat()) || ALCSProcessus.ETAT_ERREUR
                                .equals(currentResult.getTraitementPeriodiqueModel().getEtat()))) {
                    // on r�cup�re le template pour voir si il s'agit d'une horlog�re, si oui, on v�rifie inclut de
                    // toute
                    // facon les partiels
                    ParameterModel ParamAppliedTemplate = ParamServiceLocator.getParameterModelService()
                            .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.TEMPLATE_PROCESSUS,
                                    "01." + currentResult.getPeriodeAFModel().getDatePeriode());
                    String appliedTemplate = ParamAppliedTemplate.getValeurAlphaParametre();

                    if ((includePartiel || ALCSProcessus.NAME_TEMPLATE_HORLO.equals(appliedTemplate))
                            && currentResult.getProcessusPeriodiqueModel().getIsPartiel()) {
                        thePeriode = currentResult.getPeriodeAFModel();

                    } else if (!currentResult.getProcessusPeriodiqueModel().getIsPartiel()) {
                        thePeriode = currentResult.getPeriodeAFModel();
                    }
                }
            }

        }

        if (thePeriode == null) {
            throw new ALPeriodeAFException(
                    "PeriodeAFBusinessServiceImpl#getPeriodeEnCours : unable to get the last open periode - no open period found");
        }

        return thePeriode;
    }

    @Override
    public String getPeriodeFinTrimestre(String datePeriode) throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(datePeriode)) {
            throw new ALPeriodeAFException(
                    "PeriodeAFBusinessServiceImpl#getPeriodeFinTrimestre: unable to determine fin trimestre, datePeriod must be MM.yyyy");
        }

        String anneeOnly = datePeriode.substring(3);
        String monthOnly = datePeriode.substring(0, 2);
        String result = "";

        if (Integer.parseInt(monthOnly) > 9) {
            result = "12." + anneeOnly;
        } else if (Integer.parseInt(monthOnly) > 6) {
            result = "09." + anneeOnly;
        } else if (Integer.parseInt(monthOnly) > 3) {
            result = "06." + anneeOnly;
        } else {
            result = "03." + anneeOnly;
        }
        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService
     * #getPeriodeToGenerateForDossier(java.lang.String)
     */
    @Override
    public String getPeriodeToGenerateForDossier(String periodicite, String bonification, String typeCotisation)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(periodicite)) {
            throw new ALPeriodeAFException(this.getClass()
                    + "#getPeriodeToGenerateForDossier : unable to get the periode - periodicite is null");
        }

        if (JadeStringUtil.isEmpty(bonification)) {
            throw new ALPeriodeAFException(this.getClass()
                    + "#getPeriodeToGenerateForDossier : unable to get the periode - bonification is null");
        }
        String periodeEnCours = this.getPeriodeEnCours(bonification, typeCotisation, false).getDatePeriode();

        if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            return getPeriodeFinTrimestre(periodeEnCours);
        }

        if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            return "12." + periodeEnCours.substring(3);
        }

        return periodeEnCours;
    }

    @Override
    public PeriodeAFModel getPreviousPeriode(String datePeriode) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(datePeriode)) {
            throw new ALPeriodeAFException("PeriodAFBusinessServiceImpl#getPreviousPeriode : " + datePeriode
                    + " is not a valid period");
        }

        return getPeriode(datePeriode, -1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService #openPeriode(java.lang.String)
     */
    @Override
    public PeriodeAFModel openPeriode(String datePeriode) throws JadeApplicationException, JadePersistenceException {

        PeriodeAFSearchModel searchPeriode = new PeriodeAFSearchModel();
        searchPeriode.setForDatePeriode(datePeriode);
        searchPeriode = ALServiceLocator.getPeriodeAFModelService().search(searchPeriode);
        if (searchPeriode.getSize() > 1) {
            throw new ALPeriodeAFException(this.getClass()
                    + "- unable to open periode - too many periodeAF in database");
        }
        // si la p�riode existe d�j�, on la retourne si elle est ouverte, sinon
        // on remonte une exception
        if (searchPeriode.getSize() == 1) {

            if (ALCSProcessus.ETAT_OUVERT.equals(((PeriodeAFModel) searchPeriode.getSearchResults()[0]).getEtat())) {
                return (PeriodeAFModel) searchPeriode.getSearchResults()[0];

            } else {
                throw new ALPeriodeAFException(this.getClass() + "#openPeriode : unable to open a closed period");
            }
        }

        PeriodeAFModel newPeriodeAF = new PeriodeAFModel();
        // si la p�riode d�sir�e n'existe pas, on la cr��
        if (searchPeriode.getSize() == 0) {

            newPeriodeAF.setDatePeriode(datePeriode);
            newPeriodeAF.setEtat(ALCSProcessus.ETAT_OUVERT);
            newPeriodeAF = (PeriodeAFModel) JadePersistenceManager.add(newPeriodeAF);

        }
        return newPeriodeAF;

    }

}
