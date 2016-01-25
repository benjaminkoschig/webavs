package globaz.musca.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface IFAModulePassage extends BIEntity {
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

    public java.lang.String getIdAction();

    public java.lang.String getIdModuleFacturation();

    /**
     * Getter
     */
    public java.lang.String getIdPassage();

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    public String getNiveauAppel();

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    public String getNomClasse();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setEstGenere(java.lang.Boolean newEstGenere);

    public void setIdAction(java.lang.String newIdAction);

    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation);

    /**
     * Setter
     */
    public void setIdPassage(java.lang.String newIdPassage);

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    public void setNiveauAppel(String niveauAppel);

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    public void setNomClasse(String nomClasse);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
