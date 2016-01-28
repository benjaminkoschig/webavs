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
 * Classe de base pour les processus m�tier
 * 
 * @author GMO
 * 
 */
public abstract class BusinessProcessus {

    /**
     * Les crit�res pour filter les donn�es trait�es par les traitements du processus
     */
    private ProcessusDatasCriteria dataCriterias = null;
    /**
     * Indique si le processus est partiel ou non ( ne contient pas la g�n�ration)
     */
    private boolean isPartiel = false;
    /**
     * La liste ordonn�e des traitements � ex�cuter
     */
    private ArrayList<BusinessTraitement> listeTraitements = new ArrayList<BusinessTraitement>();

    /**
     * Le mod�le repr�sentant le processus p�riodique sur lequel la logique m�tier de la classe est appliqu�e
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;

    /**
     * Constructeur BusinessProcessus
     */
    public BusinessProcessus() {
        super();

    }

    /**
     * Ajoute un traitement � la file des traitements du processus
     * 
     * @param newTraitement
     *            le traitement � ajouter
     * @param withCriteriaProcessus
     *            indique si la s�lection des donn�es trait�es par le traitement ajout� sont pr�filtr�es par le
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
     * La structure repr�sentant les "crit�res filtre" des donn�es trait�es par les traitements du processus
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
        // Parmi les traitements processus, on lance celui qui correspond � l'id
        // pass� en param�tre
        for (int i = 0; i < getListeTraitements().size(); i++) {
            if (idTraitementPeriodique.equals((getListeTraitements().get(i)).getTraitementPeriodiqueModel().getId())) {
                return getListeTraitements().get(i);
            }

        }
        return null;
    }

    /**
     * Charge les donn�es processus p�riodique et traitements p�riodiques depuis la DB
     * 
     * @param idProcessusPeriodique
     *            id du processus p�riodique
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public final void init(ProcessusPeriodiqueModel processusPeriodique) throws JadeApplicationException,
            JadePersistenceException {
        processusPeriodiqueModel = processusPeriodique;
        initialize();
        // On charge tous les traitementsPeriodiqueModel depuis la DB pour
        // remplir les traitements m�tier du processus avec les donn�es DB
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
     * Initialisation du processus (filtre des donn�es, traitements, ...) => la structure, mais les donn�es (fait par
     * this.init)
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Retire le traitement se trouvant � la position donn�e
     * 
     * @param idxTraitement
     *            la position du traitement � retirer
     */
    public void removeTraitement(int idxTraitement) {
        listeTraitements.remove(idxTraitement);
    }

    /**
     * @param dataCriterias
     *            les crit�res pour filtrer les donn�es que les traitements du processus
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
     *            d�finit si il s'agit d'un processus partiel ou non
     */
    public void setPartiel(boolean isPartiel) {
        this.isPartiel = isPartiel;
    }

    /**
     * @param processusPeriodiqueModel
     *            le processus p�riodique soumis � la logique m�tier
     */
    public void setProcessusPeriodiqueModel(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        this.processusPeriodiqueModel = processusPeriodiqueModel;
    }

}
