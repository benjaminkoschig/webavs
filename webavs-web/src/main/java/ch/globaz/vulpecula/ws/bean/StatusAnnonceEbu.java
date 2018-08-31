package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="statusAnnonce")
public class StatusAnnonceEbu {
    private String message;
    private StatusReponse reponse;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusReponse getReponse() {
        return reponse;
    }

    public void setReponse(StatusReponse reponse) {
        this.reponse = reponse;
    }
}
