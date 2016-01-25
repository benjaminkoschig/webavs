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
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception Exception
     *                si la suppression a �chou�e
     */
    public void delete(BITransaction transaction) throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.01.2002 09:33:33)
     * 
     * @return String
     */
    public String getDescription();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.12.2001 11:02:39)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:26:06)
     * 
     * @return globaz.osiris.api.APISectionDescriptor
     */
    public globaz.osiris.api.APISectionDescriptor getSectionDescriptor();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.01.2002 09:33:33)
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
     * Met � jour l'enregistrement dans la BD
     * 
     * @exception Exception
     *                si la mise � jour a �chou�e
     */
    public void update(BITransaction transaction) throws Exception;
}
