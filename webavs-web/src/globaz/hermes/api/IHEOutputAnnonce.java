package globaz.hermes.api;

import globaz.globall.api.BITransaction;

/**
 * Interface d'API à utiliser pour lire une annonce stockée
 * 
 * @author EFLCreateAPITool
 */
public interface IHEOutputAnnonce extends IHEAnnoncesViewBean {
    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    @Override
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    @Override
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /*
     * Vérifie si toutes les annonces attendues pour cette annonce sont arrivées
     * 
     * @return true si oui, false si non
     */
    public Boolean getAllAnnonceRetour();

    public String getChampEnregistrement();

    /*
     * Vérifie si toutes les annonces attendues pour cette annonce sont arrivées
     * 
     * @return true si oui, false si non
     */
    public Boolean getConfirmed();

    /*
     * retourne le statut de l'annonce
     */
    @Override
    public String getStatut();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    @Override
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Ajoute la liste des méthodes à charger depuis l'entity. Date de création : (30.01.2003 07:47:14)
     * 
     * @param methodsName
     *            la liste des méthodes
     */
    void setMethodsToLoad(String[] methodsName);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    @Override
    public void update(BITransaction transaction) throws java.lang.Exception;
}
