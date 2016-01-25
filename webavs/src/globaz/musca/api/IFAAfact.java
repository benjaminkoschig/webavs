package globaz.musca.api;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API
 * 
 * @author EFLCreateAPITool
 */
public interface IFAAfact extends BIEntity {
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

    public java.lang.String getAnneeCotisation();

    /**
     * @return le code système du canton de l'assurance.
     */
    public String getCanton();

    public java.lang.String getDebutPeriode();

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

    public java.lang.String getFinPeriode();

    /**
     * Getter
     */
    public java.lang.String getIdAfact();

    /**
     * @return l'identifiant de l'assurance.
     */
    public String getIdAssurance();

    public java.lang.String getIdEnteteFacture();

    public java.lang.String getIdExterneDebiteurCompensation();

    public java.lang.String getIdExterneFactureCompensation();

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 17:48:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterneRubrique();

    public java.lang.String getIdModuleFacturation();

    /**
     * Getter
     */
    public java.lang.String getIdPassage();

    public java.lang.String getIdRemarque();

    public java.lang.String getIdRoleDebiteurCompensation();

    public java.lang.String getIdRubrique();

    /**
     * @return l'id de la section.
     */
    public String getIdSection();

    public java.lang.String getIdTiersDebiteurCompensation();

    public java.lang.String getIdTypeAfact();

    public java.lang.String getIdTypeFactureCompensation();

    public java.lang.String getLibelle();

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:31:20)
     * 
     * @return java.lang.String
     */
    public String getLibelleRubrique();

    /**
     * Récupère le libellé sur facture (libellé forcé ou description du compte Date de création : (12.12.2002 14:32:37)
     * 
     * @return java.lang.String Le libellé sur facture
     */
    public String getLibelleSurFacture();

    public java.lang.String getMasseDejaFacturee();

    public java.lang.String getMasseFacture();

    public java.lang.String getMasseInitiale();

    public java.lang.String getMontantDejaFacture();

    public java.lang.String getMontantFacture();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 16:24:56)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantFactureToCurrency();

    public java.lang.String getMontantInitial();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    public String getNomTiers();

    public java.lang.String getReferenceExterne();

    public java.lang.String getRemarque();

    public java.lang.String getTauxDejaFacture();

    public java.lang.String getTauxFacture();

    public java.lang.String getTauxInitial();

    public java.lang.String getUser();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setAnneeCotisation(java.lang.String newAnneeCotisation);

    public void setAQuittancer(java.lang.Boolean newAQuittancer);

    /**
     * Modifie le code système du canton de l'assurance.
     * 
     * @param canton
     */
    public void setCanton(String canton);

    public void setDebutPeriode(java.lang.String newDebutPeriode);

    public void setFinPeriode(java.lang.String newFinPeriode);

    /**
     * Setter
     */
    public void setIdAfact(java.lang.String newIdAfact);

    /**
     * Modifie l'id de l'assurance.
     * 
     * @param idAssurance
     */
    public void setIdAssurance(String idAssurance);

    public void setIdEnteteFacture(java.lang.String newIdEnteteFacture);

    public void setIdExterneDebiteurCompensation(java.lang.String newIdExterneDebiteurCompensation);

    public void setIdExterneFactureCompensation(java.lang.String newIdExterneFactureCompensation);

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 17:48:07)
     * 
     * @param newIdExterneRubrique
     *            java.lang.String
     */
    public void setIdExterneRubrique(java.lang.String newIdExterneRubrique);

    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation);

    /**
     * Setter
     */
    public void setIdPassage(java.lang.String newIdPassage);

    public void setIdRemarque(java.lang.String newIdRemarque);

    public void setIdRoleDebiteurCompensation(java.lang.String newIdRoleDebiteurCompensation);

    public void setIdRubrique(java.lang.String newIdRubrique);

    /**
     * Modifie l'id de la section.
     * 
     * @param idSection
     */
    public void setIdSection(String idSection);

    public void setIdTiersDebiteurCompensation(java.lang.String newIdTiersDebiteurCompensation);

    public void setIdTypeAfact(java.lang.String newIdTypeAfact);

    public void setIdTypeFactureCompensation(java.lang.String newIdTypeFactureCompensation);

    public void setLibelle(java.lang.String newLibelle);

    public void setMasseDejaFacturee(java.lang.String newMasseDejaFacturee);

    public void setMasseFacture(java.lang.String newMasseFacture);

    public void setMasseInitiale(java.lang.String newMasseInitiale);

    public void setMontantDejaFacture(java.lang.String newMontantDejaFacture);

    public void setMontantFacture(java.lang.String newMontantFacture);

    public void setMontantInitial(java.lang.String newMontantInitial);

    public void setNonComptabilisable(java.lang.Boolean newNonComptabilisable);

    public void setNonImprimable(java.lang.Boolean newNonImprimable);

    public void setReferenceExterne(java.lang.String newReferenceExterne);

    public void setRemarque(java.lang.String newRemarque);

    public void setTauxDejaFacture(java.lang.String newTauxDejaFacture);

    public void setTauxFacture(java.lang.String newTauxFacture);

    public void setTauxInitial(java.lang.String newTauxInitial);

    public void setUser(java.lang.String newUser);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
