package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JPA
 * 
 */
public class AdhesionCotisationPosteTravailSimpleModel extends JadeSimpleModel {
    private String id;
    private String idPosteTravail;
    private String idCotisation;
    private String dateDebut;
    private String dateFin;

    public AdhesionCotisationPosteTravailSimpleModel() {
        super();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
}
