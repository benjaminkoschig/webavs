package globaz.aquila.db.rdp.cashin.model;

import java.util.ArrayList;
import java.util.List;

public class Litige {

    private String numExterneDossier;
    private String numExterneDebiteur;
    private String dateCreationLitige;
    private String numeroExterneLitige;
    private String codeGestionnaire;
    private List<Creance> creances = new ArrayList<Creance>();
    private List<Ecriture> ecritures = new ArrayList<Ecriture>();

    public String getDateCreationLitige() {
        return dateCreationLitige;
    }

    public String getNumeroExterneLitige() {
        return numeroExterneLitige;
    }

    public void setNumeroExterneLitige(String numeroExterneLitige) {
        this.numeroExterneLitige = numeroExterneLitige;
    }

    public void setDateCreationLitige(String dateCreationLitige) {
        this.dateCreationLitige = dateCreationLitige;
    }

    public String getNumExterneDossier() {
        return numExterneDossier;
    }

    public void setNumExterneDossier(String numExterneDossier) {
        this.numExterneDossier = numExterneDossier;
    }

    public String getNumExterneDebiteur() {
        return numExterneDebiteur;
    }

    public void setNumExterneDebiteur(String numExterneDebiteur) {
        this.numExterneDebiteur = numExterneDebiteur;
    }

    public void addCreance(Creance creance) {
        creances.add(creance);
    }

    public List<Creance> getCreances() {
        return creances;
    }

    public void setCreances(List<Creance> creances) {
        this.creances = creances;
    }

    public String getCodeGestionnaire() {
        return codeGestionnaire;
    }

    public void setCodeGestionnaire(String codeGestionnaire) {
        this.codeGestionnaire = codeGestionnaire;
    }

    public List<Ecriture> getEcritures() {
        return ecritures;
    }

    public void setEcritures(List<Ecriture> ecritures) {
        this.ecritures = ecritures;
    }
}
