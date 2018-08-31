package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Deprecated
public class DecompteResponse {
    private boolean reponse;
    private String message;

    public DecompteResponse() {
    	// Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public DecompteResponse(boolean reponse, String message) {
        this.reponse = reponse;
        this.message = message;
    }

}
