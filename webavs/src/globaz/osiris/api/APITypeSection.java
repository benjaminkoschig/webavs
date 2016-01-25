package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface APITypeSection extends BIEntity {

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return String
     */
    public String getDescription();

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return String
     */
    public String getIdentificationSource();

    public String getIdSequenceContentieux();

    public String getIdTraduction();

    /**
     * Getter
     */
    public String getIdTypeSection();

    public String getNomClasse();

    public String getNomPageDetail();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:26:06)
     * 
     * @return globaz.osiris.api.APISectionDescriptor
     */
    public globaz.osiris.api.APISectionDescriptor getSectionDescriptor();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            String
     */
    public void setDescription(String newDescription) throws Exception;

    public void setDescriptionDe(String newDescription) throws Exception;

    public void setDescriptionFr(String newDescription) throws Exception;

    public void setDescriptionIt(String newDescription) throws Exception;

    public void setIdSequenceContentieux(String newIdSequenceContentieux);

    public void setIdTraduction(String newIdTraduction);

    /**
     * Setter
     */
    public void setIdTypeSection(String newIdTypeSection);

    public void setNomClasse(String newNomClasse);

    public void setNomPageDetail(String newNomPageDetail);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws Exception;
}
