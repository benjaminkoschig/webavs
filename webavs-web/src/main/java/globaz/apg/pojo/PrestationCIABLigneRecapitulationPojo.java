package globaz.apg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PrestationCIABLigneRecapitulationPojo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String libelleService;
    private String codeService;
    private int totalNbCasService;
    private double totalMontantBrutService;
    private ArrayList<PrestationCIABAssuranceComplPojo> listPrestationCIABAssuranceCompl;


    public PrestationCIABLigneRecapitulationPojo() {
        super();
        libelleService = "";
        codeService = "";
        totalMontantBrutService = 0;
        totalNbCasService = 0;
        listPrestationCIABAssuranceCompl = new ArrayList<PrestationCIABAssuranceComplPojo>();
    }

    public String getLibelleService() {
        return libelleService;
    }

    public void setLibelleService(String libelleService) {
        this.libelleService = libelleService;
    }

    public String getCodeService() {
        return codeService;
    }

    public void setCodeService(String codeService) {
        this.codeService = codeService;
    }

    public int getTotalNbCasService() {
        return totalNbCasService;
    }

    public void setTotalNbCasService(int totalNbCasService) {
        this.totalNbCasService = totalNbCasService;
    }

    public double getTotalMontantBrutService() {
        return totalMontantBrutService;
    }

    public void setTotalMontantBrutService(double totalMontantBrutService) {
        this.totalMontantBrutService = totalMontantBrutService;
    }

    public ArrayList<PrestationCIABAssuranceComplPojo> getListPrestationCIABAssuranceCompl() {
        return listPrestationCIABAssuranceCompl;
    }

    public void setListPrestationCIABAssuranceCompl(ArrayList<PrestationCIABAssuranceComplPojo> listPrestationCIABAssuranceCompl) {
        this.listPrestationCIABAssuranceCompl = listPrestationCIABAssuranceCompl;
    }
}
