package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class TravailleurInfoGSON implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4279855361008177407L;
    String idPortail;
    String idTravailleurExistant;
    String idTiersExistant;
    String correlationId;
    String permisTravail;
    String referencePermis;

    public String getIdTravailleurExistant() {
        return idTravailleurExistant;
    }

    public String getIdTiersExistant() {
        return idTiersExistant;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getPermisTravail() {
        return permisTravail;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public String getIdPortail() {
        return idPortail;
    }

}
