package globaz.musca.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface IFAPassage extends BIEntity {
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

    public java.lang.String getDateComptabilisation();

    public java.lang.String getDateCreation();

    public java.lang.String getDateEcheance();

    public java.lang.String getDateFacturation();

    public java.lang.String getDatePeriode();

    public java.lang.String getIdDernierPassage();

    public java.lang.String getIdJournal();

    public java.lang.String getIdPassage();

    public java.lang.String getIdPlanFacturation();

    public java.lang.String getIdRemarque();

    public java.lang.String getIdTypeFacturation();

    /**
     * Getter
     */

    public Boolean getIsAuto();

    public java.lang.String getLibelle();

    public java.lang.String getRemarque();

    public java.lang.String getStatus();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setDateComptabilisation(java.lang.String newDateComptabilisation);

    public void setDateCreation(java.lang.String newDateCreation);

    public void setDateEcheance(java.lang.String newDateEcheance);

    public void setDateFacturation(java.lang.String newDateFacturation);

    public void setDatePeriode(java.lang.String newDatePeriode);

    public void setEstVerrouille(java.lang.Boolean newEstVerrouille);

    public void setIdJournal(java.lang.String newIdJournal);

    public void setIdPassage(java.lang.String newIdPassage);

    public void setIdPlanFacturation(java.lang.String newIdPlanFacturation);

    public void setIdRemarque(java.lang.String newIdRemarque);

    public void setIdTypeFacturation(java.lang.String newIdTypeFacturation);

    /**
     * Setter
     */
    public void setIsAuto(Boolean newIsAuto);

    public void setLibelle(java.lang.String newLibelle);

    public void setRemarque(java.lang.String newRemarque);

    public void setStatus(java.lang.String newStatus);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
