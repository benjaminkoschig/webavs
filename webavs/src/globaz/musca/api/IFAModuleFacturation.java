package globaz.musca.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface IFAModuleFacturation extends BIEntity {
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

    /**
     * Getter
     */
    public java.lang.String getIdModuleFacturation();

    public java.lang.String getIdTypeModule();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:20:10)
     * 
     * @return int
     */
    public java.lang.String getLibelle();

    public java.lang.String getLibelleDe();

    public java.lang.String getLibelleFr();

    public java.lang.String getLibelleIt();

    public java.lang.String getNiveauAppel();

    public java.lang.String getNomClasse();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Setter
     */
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation);

    public void setIdTypeModule(java.lang.String newIdTypeModule);

    public void setLibelleDe(java.lang.String newLibelleDe);

    public void setLibelleFr(java.lang.String newLibelleFr);

    public void setLibelleIt(java.lang.String newLibelleIt);

    public void setModifierAfact(java.lang.Boolean newModifierAfact);

    public void setNiveauAppel(java.lang.String newNiveauAppel);

    public void setNomClasse(java.lang.String newNomClasse);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
