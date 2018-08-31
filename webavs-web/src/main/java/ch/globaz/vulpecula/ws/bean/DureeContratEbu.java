package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dureeContrat")
public class DureeContratEbu {
    private boolean determinee;
    private String dateFin;

    public DureeContratEbu(boolean determinee, String dateFin) {
        this.determinee = determinee;
        this.dateFin = dateFin;
    }

    public DureeContratEbu() {
    };

    public DureeContratEbu(boolean determinee) {
        this.determinee = determinee;
    }

    public boolean isDeterminee() {
        return determinee;
    }

    public void setDeterminee(boolean determinee) {
        this.determinee = determinee;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
}
