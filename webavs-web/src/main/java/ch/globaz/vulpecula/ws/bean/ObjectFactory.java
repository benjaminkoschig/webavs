package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public EmployeurEbu createEmployeur() {
        return new EmployeurEbu();
    }

    public AdresseEbu createAdresse() {
        return new AdresseEbu();
    }

}
