package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

/**
 * @author JPA
 * 
 */
public class SuiviDocumentCaisseMaladieGSON implements Serializable {
    private static final long serialVersionUID = 3837248260596799868L;
    public String id;
    public String typeDocument;
    public String envoye;
    public String dateEnvoi;

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getEnvoye() {
        return envoye;
    }

    public void setEnvoye(String envoye) {
        this.envoye = envoye;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
