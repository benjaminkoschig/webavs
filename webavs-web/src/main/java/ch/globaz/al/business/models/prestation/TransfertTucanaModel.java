package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le simple d'un transfert Tucana
 * 
 * @author jts
 * 
 */
public class TransfertTucanaModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id de d�tail de la prestation � laquelle est li�e l'enregistrement
     */
    public String idDetailPrestation = null;
    /**
     * Id (cl� primaire)
     */
    public String idTucana = null;
    /**
     * num�ro de bouclement
     */
    public String numBouclement = null;
    /**
     * Rubrique repr�sentant le type d'allocation
     */
    public String rubriqueAllocation = null;
    /**
     * Rubrique du suppl�ment conventionnel
     */
    public String rubriqueSupplementConventionnel = null;
    /**
     * Rubrique du suppl�ment l�gal
     */
    public String rubriqueSupplementLegal = null;

    @Override
    public String getId() {
        return idTucana;
    }

    public String getIdDetailPrestation() {
        return idDetailPrestation;
    }

    public String getIdTucana() {
        return idTucana;
    }

    public String getNumBouclement() {
        return numBouclement;
    }

    public String getRubriqueAllocation() {
        return rubriqueAllocation;
    }

    public String getRubriqueSupplementConventionnel() {
        return rubriqueSupplementConventionnel;
    }

    public String getRubriqueSupplementLegal() {
        return rubriqueSupplementLegal;
    }

    @Override
    public void setId(String id) {
        idTucana = id;
    }

    public void setIdDetailPrestation(String idDetailPrestation) {
        this.idDetailPrestation = idDetailPrestation;
    }

    public void setIdTucana(String idTucana) {
        this.idTucana = idTucana;
    }

    public void setNumBouclement(String numBouclement) {
        this.numBouclement = numBouclement;
    }

    public void setRubriqueAllocation(String rubriqueAllocation) {
        this.rubriqueAllocation = rubriqueAllocation;
    }

    public void setRubriqueSupplementConventionnel(String rubriqueSupplementConventionnel) {
        this.rubriqueSupplementConventionnel = rubriqueSupplementConventionnel;
    }

    public void setRubriqueSupplementLegal(String rubriqueSupplementLegal) {
        this.rubriqueSupplementLegal = rubriqueSupplementLegal;
    }
}
