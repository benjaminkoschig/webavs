package globaz.apg.pojo;

import java.io.Serializable;

public class PrestationCIABAssuranceComplPojo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String codeAssuranceCompl;
    private String libelleAssuranceCompl;
    private int nbCas;
    private double montantBrut;

    public PrestationCIABAssuranceComplPojo() {
        super();
        codeAssuranceCompl = "";
        libelleAssuranceCompl = "";
        nbCas = 0;
        montantBrut = 0;
    }
    public String getCodeAssuranceCompl() {
        return codeAssuranceCompl;
    }

    public void setCodeAssuranceCompl(String codeAssuranceCompl) {
        this.codeAssuranceCompl = codeAssuranceCompl;
    }

    public String getLibelleAssuranceCompl() {
        return libelleAssuranceCompl;
    }

    public void setLibelleAssuranceCompl(String libelleAssuranceCompl) {
        this.libelleAssuranceCompl = libelleAssuranceCompl;
    }

    public int getNbCas() {
        return nbCas;
    }

    public void setNbCas(int nbCas) {
        this.nbCas = nbCas;
    }

    public double getMontantBrut() {
        return montantBrut;
    }

    public void setMontantBrut(double montantBrut) {
        this.montantBrut = montantBrut;
    }

}
