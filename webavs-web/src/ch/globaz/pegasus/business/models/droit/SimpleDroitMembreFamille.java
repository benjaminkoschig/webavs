package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDroitMembreFamille extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRoleFamillePC = null;
    private String idDonneesPersonnelles = null;

    private String idDroit = null;

    private String idDroitMembreFamille = null;
    private String idMembreFamilleSF = null;

    /**
     * @return the csRoleFamillePC
     */
    public String getCsRoleFamillePC() {
        return csRoleFamillePC;
    }

    @Override
    public String getId() {
        return idDroitMembreFamille;
    }

    /**
     * @return the idDonneesPersonnelles
     */
    public String getIdDonneesPersonnelles() {
        return idDonneesPersonnelles;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idDroitMembreFamille
     */
    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * @return the idMembreFamilleSF
     */
    public String getIdMembreFamilleSF() {
        return idMembreFamilleSF;
    }

    /**
     * @param csRoleFamillePC
     *            the csRoleFamillePC to set
     */
    public void setCsRoleFamillePC(String csRoleFamillePC) {
        this.csRoleFamillePC = csRoleFamillePC;
    }

    @Override
    public void setId(String id) {
        idDroitMembreFamille = id;
    }

    /**
     * @param idDonneesPersonnelles
     *            the idDonneesPersonnelles to set
     */
    public void setIdDonneesPersonnelles(String idDonneesPersonnelles) {
        this.idDonneesPersonnelles = idDonneesPersonnelles;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idDroitMembreFamille
     *            the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    /**
     * @param idMembreFamilleSF
     *            the idMembreFamilleSF to set
     */
    public void setIdMembreFamilleSF(String idMembreFamilleSF) {
        this.idMembreFamilleSF = idMembreFamilleSF;
    }

}
