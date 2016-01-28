package ch.globaz.al.businessimpl.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * 
 * Classe de base pour les processus métier
 * 
 * @author GMO
 * 
 */
public abstract class BusinessProcessus {

    /**
     * Les critères pour filter les données traitées par les traitements du processus
     */
    private ProcessusDatasCriteria dataCriterias = null;
    /**
     * Indique si le processus est partiel ou non ( ne contient pas la génération)
     */
    private boolean isPartiel = false;
    /**
     * La liste ordonnée des traitements à exécuter
     */
    private ArrayList<BusinessTraitement> listeTraitements = new ArrayList<BusinessTraitement>();

    /**
     * Le modèle représentant le processus périodique sur lequel la logique métier de la classe est appliquée
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;

    /**
     * Constructeur BusinessProcessus
     */
    public BusinessProcessus() {
        super();

    }

    /**
     * Ajoute un traitement à la file des traitements du processus
     * 
     * @param newTraitement
     *            le traitement à ajouter
     * @param withCriteriaProcessus
     *            indique si la sélection des données traitées par le traitement ajouté sont préfiltrées par le
     *            processus
     */
    public void addTraitement(BusinessTraitement newTraitement, boolean withCriteriaProcessus) {
        newTraitement.setProcessusConteneur(this);
        listeTraitements.add(newTraitement);
    }

    /**
     * Retourne le label du nom processus
     * 
     * @return label du nom processus
     */
    public abstract String getCSProcessus();

    /**
     * La structure représentant les "critères filtre" des données traitées par les traitements du processus
     * 
     * @return dataCriterias
     */
    public ProcessusDatasCriteria getDataCriterias() {
        return dataCriterias;
    }

    /**
     * @return listeTraitements
     */
    public ArrayList<BusinessTraitement> getListeTraitements() {
        return listeTraitements;
    }

    /**
     * @return processusPeriodiqueModel
     */
    public ProcessusPeriodiqueModel getProcessusPeriodiqueModel() {
        return processusPeriodiqueModel;
    }

    /**
     * @param idTraitementPeriodique
     *            l'id du traitement
     * @return le traitement parmi la liste des traitements des processus
     */
    public BusinessTraitement getTraitementFromList(String idTraitementPeriodique) {
        // Parmi les traitements processus, on lance celui qui correspond à l'id
        // passé en paramètre
        for (int i = 0; i < getListeTraitements().size(); i++) {
            if (idTraitementPeriodique.equals((getListeTraitements().get(i)).getTraitementPeriodiqueModel().getId())) {
                return getListeTraitements().get(i);
            }

        }
        return null;
    }

    /**
     * Charge les données processus périodique et traitements périodiques depuis la DB
     * 
     * @param idProcessusPeriodique
     *            id du processus périodique
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public final void init(ProcessusPeriodiqueModel processusPeriodique) throws JadeApplicationException,
            JadePersistenceException {
        processusPeriodiqueModel = processusPeriodique;
        initialize();
        // On charge tous les traitementsPeriodiqueModel depuis la DB pour
        // remplir les traitements métier du processus avec les données DB
        for (int i = 0; i < listeTraitements.size(); i++) {

            TraitementPeriodiqueSearchModel searchTraitementPeriodique = new TraitementPeriodiqueSearchModel();
            searchTraitementPeriodique.setForCSTraitement((listeTraitements.get(i)).getCSTraitement());
            searchTraitementPeriodique.setForIdProcessusPeriodique(processusPeriodique.getId());
            searchTraitementPeriodique = ALImplServiceLocator.getTraitementPeriodiqueModelService().search(
                    searchTraitementPeriodique);
            if (searchTraitementPeriodique.getSize() == 0) {
                throw new ALProcessusException("BusinessProcessus#init: no traitement found with "
                        + searchTraitementPeriodique.getForCSTraitement());
            }
            if (searchTraitementPeriodique.getSize() > 1) {
                throw new ALProcessusException("BusinessProcessus#init: too many traitements found with "
                        + searchTraitementPeriodique.getForCSTraitement());
            }
            (listeTraitements.get(i))
                    .setTraitementPeriodiqueModel((TraitementPeriodiqueModel) searchTraitementPeriodique
                            .getSearchResults()[0]);
        }

    }

    /**
     * Initialisation du processus (filtre des données, traitements, ...) => la structure, mais les données (fait par
     * this.init)
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public abstract void initialize() throws JadeApplicationException, JadePersistenceException;

    /**
     * @return isPartiel
     */
    public boolean isPartiel() {
        return isPartiel;
    }

    /**
     * Retire le traitement se trouvant à la position donnée
     * 
     * @param idxTraitement
     *            la position du traitement à retirer
     */
    public void removeTraitement(int idxTraitement) {
        listeTraitements.remove(idxTraitement);
    }

    /**
     * @param dataCriterias
     *            les critères pour filtrer les données que les traitements du processus
     */
    public void setDataCriterias(ProcessusDatasCriteria dataCriterias) {
        this.dataCriterias = dataCriterias;
    }

    /**
     * @param listeTraitements
     *            la liste des traitements contenus dans le processus
     */
    public void setListeTraitements(ArrayList<BusinessTraitement> listeTraitements) {
        this.listeTraitements = listeTraitements;
    }

    /**
     * 
     * @param isPartiel
     *            définit si il s'agit d'un processus partiel ou non
     */
    public void setPartiel(boolean isPartiel) {
        this.isPartiel = isPartiel;
    }

    /**
     * @param processusPeriodiqueModel
     *            le processus périodique soumis à la logique métier
     */
    public void setProcessusPeriodiqueModel(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        this.processusPeriodiqueModel = processusPeriodiqueModel;
    }

}
