package globaz.musca.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface IFAEnteteFacture extends BIEntity {
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

    public java.lang.String getDateReceptionDS();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:50:06)
     * 
     * @return java.lang.String
     */
    public String getDescriptionDecompte();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:24:17)
     * 
     * @return java.lang.String
     */
    public String getDescriptionTiers();

    public java.lang.String getEspion();

    public java.lang.String getIdAdresse();

    public java.lang.String getIdAdressePaiement();

    /**
     * Getter
     */
    public java.lang.String getIdEntete();

    public java.lang.String getIdExterneFacture();

    public java.lang.String getIdExterneRole();

    public java.lang.String getIdModeRecouvrement();

    public java.lang.String getIdPassage();

    public java.lang.String getIdRemarque();

    public java.lang.String getIdRole();

    public java.lang.String getIdSoumisInteretsMoratoires();

    public java.lang.String getIdSousType();

    public java.lang.String getIdTiers();

    public java.lang.String getIdTypeFacture();

    public java.lang.String getMotifInteretsMoratoires();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    public String getNomTiers();

    public java.lang.String getNumCommune();

    public java.lang.String getRemarque();

    public java.lang.String getRemarque(BITransaction transaction);

    public java.lang.String getTotalFacture();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setDateReceptionDS(java.lang.String newDateReceptionDS);

    public void setEspion(java.lang.String newEspion);

    public void setIdAdresse(java.lang.String newIdAdresse);

    public void setIdAdressePaiement(java.lang.String newIdAdressePaiement);

    /**
     * Setter
     */
    public void setIdEntete(java.lang.String newIdEntete);

    public void setIdExterneFacture(java.lang.String newIdExterneFacture);

    public void setIdExterneRole(java.lang.String newIdExterneRole);

    public void setIdModeRecouvrement(java.lang.String newIdModeRecouvrement);

    public void setIdPassage(java.lang.String newIdPassage);

    public void setIdRemarque(java.lang.String newIdRemarque);

    public void setIdRole(java.lang.String newIdRole);

    public void setIdSoumisInteretsMoratoires(java.lang.String newIdSoumisInteretsMoratoires);

    public void setIdSousType(java.lang.String newIdSousType);

    public void setIdTiers(java.lang.String newIdTiers);

    public void setIdTypeFacture(java.lang.String newIdTypeFacture);

    public void setMotifInteretsMoratoires(java.lang.String newMotifInteretsMoratoires);

    public void setNonImprimable(java.lang.Boolean newNonImprimable);

    public void setNumCommune(java.lang.String newNumCommune);

    public void setRemarque(java.lang.String newRemarque);

    public void setTotalFacture(java.lang.String newTotalFacture);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
