package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mapping du compteur pour les congés payés
 * 
 * @since WebBMS 0.01.04
 */
public class CompteurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 5023351253133640633L;

    private String id;
    private String idPosteTravail;
    private String annee;
    private String cumulCotisation;
    private String montantRestant;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String arg0) {
        id = arg0;
    }

    /**
     * @return the idPosteTravail
     */
    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the cumulCotisation
     */
    public String getCumulCotisation() {
        return cumulCotisation;
    }

    /**
     * @param idPosteTravail the idPosteTravail to set
     */
    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param cumulCotisation the cumulCotisation to set
     */
    public void setCumulCotisation(String cumulCotisation) {
        this.cumulCotisation = cumulCotisation;
    }

    public String getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(String montantRestant) {
        this.montantRestant = montantRestant;
    }
}
