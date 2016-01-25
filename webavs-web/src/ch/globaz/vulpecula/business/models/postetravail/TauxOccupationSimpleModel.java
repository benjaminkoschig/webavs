package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JPA
 * 
 */
public class TauxOccupationSimpleModel extends JadeSimpleModel {
    private String id;
    private String idPosteTravail;
    private String dateValidite;
    private String taux;

    public TauxOccupationSimpleModel() {
        super();
    }

    public String getDateValidite() {
        return dateValidite;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public String getTaux() {
        return taux;
    }

    public void setDateValidite(String dateValidite) {
        this.dateValidite = dateValidite;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }
}
