package ch.globaz.al.businessimpl.services.processus;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.processus.ALProcessusCtrlException;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ConfigProcessusSearchModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueSearchModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.processus.BusinessProcessusService;
import ch.globaz.al.businessimpl.processus.BusinessProcessus;
import ch.globaz.al.businessimpl.processus.BusinessProcessusFactory;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation du service de gestion des processus métier
 * 
 * @author GMO
 * 
 */
public class BusinessProcessusServiceImpl extends ALAbstractBusinessServiceImpl implements BusinessProcessusService {

    /**
     * 
     * @param model
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void addHistoriqueTraitement(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#addHistoriqueTraitement :unable to add historique - model is null");
        }

        // TODO (lot 2) à terminer
    }

    /**
	 * 
	 */
    @Override
    public void annulerTraitementPeriodique(String idProcessusPeriodique, String idTraitementPeriodique)
            throws JadeApplicationException, JadePersistenceException {
        // TODO (lot 2) à terminer
    }

    /**
     * Créer les entités en DB qui représente les processus périodiques et leurs traitements périodiques en fonction du
     * contenu du searchModel
     * 
     * @param searchModel
     *            le searchModel selon lequel sont créés les entités
     * @param datePeriode
     *            la période à laquelle lier ces entités périodiques
     * @param isPartielProcessus
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void createPeriodiqueItems(ConfigProcessusSearchModel searchModel, String datePeriode,
            boolean isPartielProcessus) throws JadeApplicationException, JadePersistenceException {

        PeriodeAFModel periode = ALServiceLocator.getPeriodeAFBusinessService().openPeriode(datePeriode);
        // pour chaque config, on créé le processus périodique
        for (int i = 0; i < searchModel.getSize(); i++) {
            ConfigProcessusModel currentConfigProcessus = (ConfigProcessusModel) searchModel.getSearchResults()[i];
            // Créé les processus périodique dans la base de données
            ProcessusPeriodiqueModel processusPeriodique = new ProcessusPeriodiqueModel();
            processusPeriodique.setIdConfig(currentConfigProcessus.getIdConfig());
            processusPeriodique.setEtat(ALCSProcessus.ETAT_OUVERT);
            processusPeriodique.setIdPeriode(periode.getIdPeriodeAF());
            processusPeriodique.setIsPartiel(isPartielProcessus);
            processusPeriodique = ALServiceLocator.getProcessusPeriodiqueModelService().create(processusPeriodique);

            // On créé une instance du processus métier implémentée pour le code
            // CS donné par la config pour pouvoir créer les traitements
            // périodique en DB

            BusinessProcessus businessProcessus = BusinessProcessusFactory.getProcessus(
                    currentConfigProcessus.getBusinessProcessus(), isPartielProcessus);
            // On lui lie le processus périodique qu'on vient de créér
            businessProcessus.setProcessusPeriodiqueModel(processusPeriodique);
            // on initialise le processus ( ajout des traitements)
            businessProcessus.initialize();

            // Pour chaque traitement métier lié au processus métier, on crée un
            // traitement périodique
            for (int j = 0; j < businessProcessus.getListeTraitements().size(); j++) {
                BusinessTraitement currentTraitement = businessProcessus.getListeTraitements().get(j);
                TraitementPeriodiqueModel traitementPeriodiqueToAdd = new TraitementPeriodiqueModel();
                traitementPeriodiqueToAdd.setIdProcessusPeriodique(businessProcessus.getProcessusPeriodiqueModel()
                        .getId());
                traitementPeriodiqueToAdd.setEtat(ALCSProcessus.ETAT_OUVERT);
                traitementPeriodiqueToAdd.setTraitementLibelle(currentTraitement.getCSTraitement());
                traitementPeriodiqueToAdd.setReadOnly(currentTraitement.isReadOnly());
                // ajout dans la base
                ALImplServiceLocator.getTraitementPeriodiqueModelService().create(traitementPeriodiqueToAdd);

            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.processus.BusinessProcessusService# deleteProcessusPartiel(java.lang.String)
     */
    @Override
    public void deleteProcessusPartiel(String idProcessusPartiel) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idProcessusPartiel)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#deleteProcessusPartiel: impossible de supprimer le processus, idProcessusPartiel is empty");
        }

        ProcessusPeriodiqueModel processusToDelete = ALServiceLocator.getProcessusPeriodiqueModelService().read(
                idProcessusPartiel);

        TraitementPeriodiqueSearchModel traitementsLies = new TraitementPeriodiqueSearchModel();
        traitementsLies.setForIdProcessusPeriodique(idProcessusPartiel);
        traitementsLies = ALServiceLocator.getTraitementPeriodiqueModelService().search(traitementsLies);
        // suppression des traitements périodiques liés au processus
        for (int i = 0; i < traitementsLies.getSize(); i++) {
            ALServiceLocator.getTraitementPeriodiqueModelService().delete(
                    (TraitementPeriodiqueModel) traitementsLies.getSearchResults()[i]);
        }

        // suppression du processus partiel
        ALServiceLocator.getProcessusPeriodiqueModelService().delete(processusToDelete);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.processus.BusinessProcessusService#
     * executeTraitementPeriodique(java.lang.String, java.lang.String)
     */
    @Override
    public HashMap<ALConstProtocoles.TypeProtocole, Object> executeTraitementPeriodique(String idProcessusPeriodique,
            String idTraitementPeriodique) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idProcessusPeriodique)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#executeTraitementPeriodique: impossible d'exécuter le traitement, idProcessusPeriodique is empty");
        }
        if (JadeStringUtil.isEmpty(idTraitementPeriodique)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#executeTraitementPeriodique: impossible d'exécuter le traitement, idTraitementPeriodique is empty");
        }

        // on recherche le CS processus lié au traitement périodique
        TemplateTraitementListComplexSearchModel searchModel = new TemplateTraitementListComplexSearchModel();
        searchModel.setForIdTraitementPeriodique(idTraitementPeriodique);
        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);

        if (searchModel.getSize() > 1) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#executeTraitementPeriodique: le traitement périodique n'est pas unique");
        }

        if (searchModel.getSize() == 0) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#executeTraitementPeriodique: le traitement périodique n'existe pas");
        }

        // On récupère l'instance du processus conteneur lié au traitement
        // périodique, pour avoir la séquence complète et l'ordre d'exécution
        // des traitements
        ProcessusPeriodiqueModel processusPeriodique = ALServiceLocator.getProcessusPeriodiqueModelService().read(
                idProcessusPeriodique);

        BusinessProcessus businessProcessus = BusinessProcessusFactory.getProcessus(
                ((TemplateTraitementListComplexModel) searchModel.getSearchResults()[0]).getConfigProcessusModel()
                        .getBusinessProcessus(), processusPeriodique.getIsPartiel());

        businessProcessus.init(processusPeriodique);
        try {
            businessProcessus.getTraitementFromList(idTraitementPeriodique).start();
        } catch (ALProcessusCtrlException e) {
            // on ne met pas à jour l'état, car erreur métier (conditions pour
            // lancement traitement non réunies)
            // mais on remonte l'exception pour que ça apparaisse dans le mail
            // résultat du process
            throw e;
        } catch (Exception e) {
            TraitementPeriodiqueModel traitementToUpdate = ALServiceLocator.getTraitementPeriodiqueModelService().read(
                    idTraitementPeriodique);
            traitementToUpdate.setEtat(ALCSProcessus.ETAT_ERREUR);
            ALServiceLocator.getTraitementPeriodiqueModelService().update(traitementToUpdate);

            throw new ALProcessusException(e.getMessage());

        }

        // on laisse remonter toutes les exceptions car
        // il faut les infos pour le mail de status du job

        // si on arrive au bout sans erreur technique, on sort le protocole, qui
        // peut lui contenir des erreurs métier

        HashMap<ALConstProtocoles.TypeProtocole, Object> protocoles = new HashMap<ALConstProtocoles.TypeProtocole, Object>();
        protocoles.put(ALConstProtocoles.TypeProtocole.CSV,
                businessProcessus.getTraitementFromList(idTraitementPeriodique).getProtocolesCSV());
        protocoles.put(ALConstProtocoles.TypeProtocole.PDF,
                businessProcessus.getTraitementFromList(idTraitementPeriodique).getProtocole());
        protocoles.put(ALConstProtocoles.TypeProtocole.INFO,
                businessProcessus.getTraitementFromList(idTraitementPeriodique).getPubInfo());

        return protocoles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.processus.BusinessProcessusService#getAppliedTemplate()
     */
    @Override
    public String getAppliedTemplate(String periode) throws JadeApplicationException, JadePersistenceException {

        ParameterModel prm = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.TEMPLATE_PROCESSUS, "01.".concat(periode));
        return prm.getValeurAlphaParametre();
    }

    @Override
    public String getNumProcessusForNumFacture(String numFacture) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.processus.BusinessProcessusService#
     * getTheProcessusForPeriode(java.lang.String, java.lang.String)
     */
    @Override
    public List<ProcessusPeriodiqueModel> getTheProcessusForPeriode(String CSProcessus, String periode)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(CSProcessus)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#getTheProcessusForPeriode: unable to search the processus, param CSProcessus is empty");
        }
        if (JadeStringUtil.isEmpty(periode)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#getTheProcessusForPeriode: unable to search the processus, param periode is empty");
        }

        List<ProcessusPeriodiqueModel> processusPeriodiqueList = new ArrayList<ProcessusPeriodiqueModel>();

        TemplateTraitementListComplexSearchModel searchModel = new TemplateTraitementListComplexSearchModel();
        searchModel.setForCSBusinessProcessus(CSProcessus);
        searchModel.setForDatePeriode(periode);

        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);
        // si il n'y a aucun processus, on retourne null et pas d'exception
        // remontée car possible si première période de l'application
        if (searchModel.getSize() == 0) {
            return processusPeriodiqueList;

        }

        for (int i = 0; i < searchModel.getSize(); i++) {
            if (!processusPeriodiqueList
                    .contains(((TemplateTraitementListComplexModel) searchModel.getSearchResults()[i])
                            .getProcessusPeriodiqueModel())) {
                processusPeriodiqueList.add(((TemplateTraitementListComplexModel) searchModel.getSearchResults()[i])
                        .getProcessusPeriodiqueModel());

            }
        }

        return processusPeriodiqueList;
    }

    @Override
    public List<ProcessusPeriodiqueModel> getUnlockProcessusPaiementForPeriode(String bonification,
            String genreAssurance) throws JadeApplicationException, JadePersistenceException {

        if (!ALCSPrestation.BONI_DIRECT.equals(bonification) && !ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#getUnlockProcessusPaiementForPeriode: bonification is not a valid for this method");

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

        // processus ignorés selon le typeCoti passé en paramètre
        // FIXME: gmo voir si ok car pas que ces processus en paritaire / pers mais peut être que les autres plus
        // utilisé
        // (h51x)
        HashSet notProcessus = new HashSet();
        if (ALCSAffilie.GENRE_ASSURANCE_PARITAIRE.equals(genreAssurance)) {
            notProcessus.add(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS);
        } else if (!ALCSAffilie.GENRE_ASSURANCE_PARITAIRE.equals(genreAssurance)) {
            notProcessus.add(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR);
        } else {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#getUnlockProcessusPaiementForPeriode: unable to filter processus according to genreAssurance:"
                            + genreAssurance);
        }
        searchModel.setNotInCSProcessus(notProcessus);

        searchModel.setDefinedSearchSize(0);
        searchModel.setWhereKey("enCours");
        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);

        List<ProcessusPeriodiqueModel> listProcessus = new ArrayList<ProcessusPeriodiqueModel>();
        // boucle pour récupérer que les processus ouverts dont la simulation n'a pas encore été faite
        for (int i = 0; i < searchModel.getSize(); i++) {
            TemplateTraitementListComplexModel currentTraitement = (TemplateTraitementListComplexModel) searchModel
                    .getSearchResults()[i];
            // si le traitement est en erreur ou encore ouvert, il est toujours sélectionnable
            if ((ALCSProcessus.ETAT_ERREUR.equals(currentTraitement.getTraitementPeriodiqueModel().getEtat()) || ALCSProcessus.ETAT_OUVERT
                    .equals(currentTraitement.getTraitementPeriodiqueModel().getEtat()))
                    && !listProcessus.contains(currentTraitement.getProcessusPeriodiqueModel())) {
                listProcessus.add(currentTraitement.getProcessusPeriodiqueModel());
            }
        }

        return listProcessus;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.processus.BusinessProcessusService#
     * initAllBusinessProcessusForPeriode(java.lang.String, java.lang.String)
     */
    @Override
    public void initAllBusinessProcessusForPeriode(String CSTemplate, String datePeriode)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(CSTemplate)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#initAllBusinessProcessusForPeriode: unable to init all processus, param CSTemplate is empty");
        }

        if (JadeStringUtil.isEmpty(datePeriode)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#initAllBusinessProcessusForPeriode: unable to init all processus, param datePeriode is empty");
        }

        // on récupère les configuration processus qui correspondent au template
        ConfigProcessusSearchModel searchModel = new ConfigProcessusSearchModel();
        searchModel.setForTemplate(CSTemplate);
        searchModel = ALServiceLocator.getConfigProcessusModelService().search(searchModel);

        for (int i = 0; i < searchModel.getSize(); i++) {
            ConfigProcessusModel currentConfig = (ConfigProcessusModel) searchModel.getSearchResults()[i];
            ALServiceLocator.getBusinessProcessusService().initBusinessProcessusForPeriode(CSTemplate,
                    currentConfig.getBusinessProcessus(), datePeriode, false);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.processus.BusinessProcessusService#
     * initBusinessProcessusForPeriode(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean initBusinessProcessusForPeriode(String CSTemplate, String CSBusinessProcessus, String datePeriode,
            boolean isProcessusPartiel) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(CSTemplate)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#initBusinessProcessusForPeriode: unable to init processus, param CSTemplate is empty");
        }

        if (JadeStringUtil.isEmpty(CSBusinessProcessus)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#initBusinessProcessusForPeriode: unable to init processus, param CSBusinessProcessus is empty");
        }

        if (JadeStringUtil.isEmpty(datePeriode)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#initBusinessProcessusForPeriode: unable to init processus, param datePeriode is empty");
        }

        // on récupère les mêmes processus métier (principal et partiel) de la
        // période en cours

        List<ProcessusPeriodiqueModel> samePeriodSameProcessus = ALServiceLocator.getBusinessProcessusService()
                .getTheProcessusForPeriode(CSBusinessProcessus, datePeriode);

        boolean isOtherPartielClosed = true;
        boolean alreadyMainCreated = false;
        for (ProcessusPeriodiqueModel oneSamePeriodSameProcessus : samePeriodSameProcessus) {
            if (oneSamePeriodSameProcessus != null) {
                // On ne peut pas créer un partiel, si deja un partiel ouvert.
                if (oneSamePeriodSameProcessus.getIsPartiel()
                        && !ALCSProcessus.ETAT_TERMINE.equals(oneSamePeriodSameProcessus.getEtat())) {
                    isOtherPartielClosed = false;
                }
                // si déjà principal, on ne peut plus initialiser un même principal, on fera rien
                if (!oneSamePeriodSameProcessus.getIsPartiel()) {
                    alreadyMainCreated = true;
                }
            }
        }
        // on créé les items en DB seulement si on est dans le cas d'un partiel et que pas encore ouvert ou si pas de
        // principal déjà créé

        // FIXME : bugzilla 5827 : normalement on peut ouvrir un principal si pas encore créé ?? meme si partiel déjà
        // existant...

        /**
         * - Un partiel s'ouvre si il n'y a pas d'autres partiel ouvert pour la période, même si pas de principal - Un
         * principal s'ouvre si il n'y a pas déjà un principal ouvert pour le période, même si un partiel est déjà
         * ouvert if ((isOtherPartielClosed && isProcessusPartiel) || (!alreadyMainCreated && !isProcessusPartiel )
         * 
         * 
         * * - Un partiel s'ouvre si il n'y a pas d'autres partiel ouvert pour la période, ET qu'il y a déjà un
         * principal - Un principal s'ouvre si il n'y a pas déjà un principal ouvert pour le période, même si un partiel
         * est déjà ouvert if ((isOtherPartielClosed && isProcessusPartiel && alreadyMainCreated ) ||
         * (!alreadyMainCreated && !isProcessusPartiel )
         */

        if ((isOtherPartielClosed && isProcessusPartiel && alreadyMainCreated)
                || (!alreadyMainCreated && !isProcessusPartiel)) {
            // on récupère les configuration processus qui correspondent au template
            ConfigProcessusSearchModel searchModel = new ConfigProcessusSearchModel();
            searchModel.setForTemplate(CSTemplate);
            searchModel.setForBusinessProcessus(CSBusinessProcessus);
            searchModel = ALServiceLocator.getConfigProcessusModelService().search(searchModel);
            createPeriodiqueItems(searchModel, datePeriode, isProcessusPartiel);
            return true;
        }
        return false;

    }

    @Override
    public boolean isFacturationSeparee(String csTemplate) throws JadeApplicationException, JadePersistenceException {

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_NAME_TEMPLATE_CONFIG, csTemplate)) {
                throw new ALProcessusException("BusinessProcessusServiceImpl#isFacturationSeparee : " + csTemplate
                        + " is not a valid type");
            }
        } catch (ALProcessusException e) {
            throw e;
        }

        catch (Exception e) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#isFacturationSeparee : unable to check csTemplate validity");
        }

        if (ALCSProcessus.NAME_TEMPLATE_AGLS.equals(csTemplate) || ALCSProcessus.NAME_TEMPLATE_CCVD.equals(csTemplate)
                || ALCSProcessus.NAME_TEMPLATE_H51X.equals(csTemplate)
                || ALCSProcessus.NAME_TEMPLATE_FPV.equals(csTemplate)) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.processus.BusinessProcessusService#
     * removeHistoriqueTraitement(java.lang.String)
     */
    @Override
    public void removeHistoriqueTraitement(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idTraitementPeriodique)) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#removeHistoriqueTraitement : unable to delete historique - idTraitementPeriodique is empty");
        }
        TraitementHistoriqueSearchModel searchModel = new TraitementHistoriqueSearchModel();
        searchModel.setForIdTraitementPeriodique(idTraitementPeriodique);
        searchModel = ALImplServiceLocator.getTraitementHistoriqueModelService().search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new ALProcessusException(
                    "BusinessProcessusServiceImpl#removeHistoriqueTraitement : unable to delete historique - no historic datas found");
        }
        for (int i = 0; i < searchModel.getSize(); i++) {
            ALImplServiceLocator.getTraitementHistoriqueModelService().delete(
                    (TraitementHistoriqueModel) searchModel.getSearchResults()[i]);
        }

    }
}
