package globaz.hermes.api;

import globaz.globall.api.BITransaction;

/**
 * Interface d'API � utiliser pour lire une annonce stock�e
 * 
 * @author EFLCreateAPITool
 */
public interface IHEOutputAnnonce extends IHEAnnoncesViewBean {
    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    @Override
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a �chou�e
     */
    @Override
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /*
     * V�rifie si toutes les annonces attendues pour cette annonce sont arriv�es
     * 
     * @return true si oui, false si non
     */
    public Boolean getAllAnnonceRetour();

    public String getChampEnregistrement();

    /*
     * V�rifie si toutes les annonces attendues pour cette annonce sont arriv�es
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
     *                si le chargement a �chou�
     */
    @Override
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Ajoute la liste des m�thodes � charger depuis l'entity. Date de cr�ation : (30.01.2003 07:47:14)
     * 
     * @param methodsName
     *            la liste des m�thodes
     */
    void setMethodsToLoad(String[] methodsName);

    /**
     * Met � jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise � jour a �chou�e
     */
    @Override
    public void update(BITransaction transaction) throws java.lang.Exception;
}
