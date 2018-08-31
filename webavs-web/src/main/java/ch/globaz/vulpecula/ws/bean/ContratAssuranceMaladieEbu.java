package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contratAssuranceMaladie")
public class ContratAssuranceMaladieEbu {
    private AssuranceMaladieEbu assureur;
    private boolean assuranceCombinee;

    public AssuranceMaladieEbu getAssureur() {
        return assureur;
    }

    public void setAssureur(AssuranceMaladieEbu assureur) {
        this.assureur = assureur;
    }

    public boolean isAssuranceCombinee() {
        return assuranceCombinee;
    }

    public void setAssuranceCombinee(boolean assuranceCombinee) {
        this.assuranceCombinee = assuranceCombinee;
    }
}
