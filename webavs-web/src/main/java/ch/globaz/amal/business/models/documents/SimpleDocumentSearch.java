package ch.globaz.amal.business.models.documents;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;

public class SimpleDocumentSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDetailFamille = null;
    private String forIdDocument = null;
    private ArrayList<String> inIdDetailFamille = null;
    private ArrayList<String> inNumModele = null;

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    public String getForIdDocument() {
        return forIdDocument;
    }

    public ArrayList<String> getInIdDetailFamille() {
        return inIdDetailFamille;
    }

    public ArrayList<String> getInNumModele() {
        return inNumModele;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    public void setForIdDocument(String forIdDocument) {
        this.forIdDocument = forIdDocument;
    }

    public void setInIdDetailFamille(ArrayList<String> inIdDetailFamille) {
        this.inIdDetailFamille = inIdDetailFamille;
    }

    public void setInNumModele(ArrayList<String> inNumModele) {
        this.inNumModele = inNumModele;
    }

    @Override
    public Class whichModelClass() {
        return SimpleDocument.class;
    }

}
