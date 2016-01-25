package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleTaux extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeTaux = "";
    private String idBareme = "";
    private String idTaux = "";
    private String idTrancheSalaire = "";
    private String valeurTaux = "";

    /**
     * @return the anneeTaux
     */
    public String getAnneeTaux() {
        return anneeTaux;
    }

    @Override
    public String getId() {
        return idTaux;
    }

    /**
     * @return the idBareme
     */
    public String getIdBareme() {
        return idBareme;
    }

    /**
     * @return the idTaux
     */
    public String getIdTaux() {
        return idTaux;
    }

    /**
     * @return the idTrancheSalaire
     */
    public String getIdTrancheSalaire() {
        return idTrancheSalaire;
    }

    /**
     * @return the valeurTaux
     */
    public String getValeurTaux() {
        return valeurTaux;
    }

    /**
     * @param anneeTaux
     *            the anneeTaux to set
     */
    public void setAnneeTaux(String anneeTaux) {
        this.anneeTaux = anneeTaux;
    }

    @Override
    public void setId(String id) {
        idTaux = id;

    }

    /**
     * @param idBareme
     *            the idBareme to set
     */
    public void setIdBareme(String idBareme) {
        this.idBareme = idBareme;
    }

    /**
     * @param idTaux
     *            the idTaux to set
     */
    public void setIdTaux(String idTaux) {
        this.idTaux = idTaux;
    }

    /**
     * @param idTrancheSalaire
     *            the idTrancheSalaire to set
     */
    public void setIdTrancheSalaire(String idTrancheSalaire) {
        this.idTrancheSalaire = idTrancheSalaire;
    }

    /**
     * @param valeurTaux
     *            the valeurTaux to set
     */
    public void setValeurTaux(String valeurTaux) {
        this.valeurTaux = valeurTaux;
    }

}
