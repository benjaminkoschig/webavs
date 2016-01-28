package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import java.util.ArrayList;

public class AnnonceBuilderReturnInfosContainer {
    private ArrayList<String> erreurCreationList = new ArrayList<String>();
    private ArrayList<String> erreurEnvoiList = new ArrayList<String>();
    private String idCaisse = "";
    private int nbCreation = 0;
    private int nbElements = 0;
    private int nbEnvoi = 0;
    private String pathToXmlFile = "";

    public int addOneElementCreated() {
        nbCreation++;
        return nbCreation;
    }

    public int addOneElementSended() {
        nbEnvoi++;
        return nbEnvoi;
    }

    public ArrayList<String> getErreurCreationList() {
        return erreurCreationList;
    }

    public ArrayList<String> getErreurEnvoiList() {
        return erreurEnvoiList;
    }

    public String getIdCaisse() {
        return idCaisse;
    }

    public int getNbCreation() {
        return nbCreation;
    }

    public int getNbElements() {
        return nbElements;
    }

    public int getNbEnvoi() {
        return nbEnvoi;
    }

    public String getPathToXmlFile() {
        return pathToXmlFile;
    }

    public void setErreurCreationList(ArrayList<String> erreurCreationList) {
        this.erreurCreationList = erreurCreationList;
    }

    public void setErreurEnvoiList(ArrayList<String> erreurEnvoiList) {
        this.erreurEnvoiList = erreurEnvoiList;
    }

    public void setIdCaisse(String idCaisse) {
        this.idCaisse = idCaisse;
    }

    public void setNbCreation(int nbCreation) {
        this.nbCreation = nbCreation;
    }

    public void setNbElements(int nbElements) {
        this.nbElements = nbElements;
    }

    public void setNbEnvoi(int nbEnvoi) {
        this.nbEnvoi = nbEnvoi;
    }

    public void setPathToXmlFile(String pathToXmlFile) {
        this.pathToXmlFile = pathToXmlFile;
    }

}
