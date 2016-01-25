package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface APICompteCourant extends BIEntity {
    public final static int AK_IDEXTERNE = 1;
    public final static int AK_IDRUBRIQUE = 2;

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    public Boolean getAccepterVentilation();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAlias();

    /**
     * Getter
     */
    public java.lang.String getIdCompteCourant();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterne();

    public java.lang.String getIdRubrique();

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPriorite();

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 10:36:57)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public APIRubrique getRubrique();

    public java.lang.String getSolde();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2002 15:07:39)
     * 
     * @return java.lang.String
     */
    public String getSoldeFormate();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setAccepterVentilation(Boolean newAccepterVentilation);

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @param newAlias
     *            java.lang.String
     */
    public void setAlias(java.lang.String newAlias);

    /**
     * Setter
     */
    public void setIdCompteCourant(java.lang.String newIdCompteCourant);

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @param newIdExterne
     *            java.lang.String
     */
    public void setIdExterne(java.lang.String newIdExterne);

    public void setIdRubrique(java.lang.String newIdRubrique);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @param newPriorite
     *            java.lang.String
     */
    public void setPriorite(java.lang.String newPriorite);

    public void setSolde(java.lang.String newSolde);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
