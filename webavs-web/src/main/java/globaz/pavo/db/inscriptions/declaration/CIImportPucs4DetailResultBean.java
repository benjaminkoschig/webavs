package globaz.pavo.db.inscriptions.declaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CIImportPucs4DetailResultBean {

    private String numeroAffilie = "";
    private boolean isAFSeul = false;
    private boolean isSimulation = false;
    private String designationAffilie = "";
    private Map<String, List<CIImportPucs4DetailResultInscriptionBean>> mapAnneeListInscriptions = new HashMap<String, List<CIImportPucs4DetailResultInscriptionBean>>();
    private Map<String, CIImportPucs4ResumeBean> mapAnneeResume = new HashMap<String, CIImportPucs4ResumeBean>();

    public Map<String, CIImportPucs4ResumeBean> getMapAnneeResume() {
        return mapAnneeResume;
    }

    public void setMapAnneeResume(Map<String, CIImportPucs4ResumeBean> mapAnneeResume) {
        this.mapAnneeResume = mapAnneeResume;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getDesignationAffilie() {
        return designationAffilie;
    }

    public void setDesignationAffilie(String designationAffilie) {
        this.designationAffilie = designationAffilie;
    }

    public Map<String, List<CIImportPucs4DetailResultInscriptionBean>> getMapAnneeListInscriptions() {
        return mapAnneeListInscriptions;
    }

    public void setMapAnneeListInscriptions(
            Map<String, List<CIImportPucs4DetailResultInscriptionBean>> mapAnneeListInscriptions) {
        this.mapAnneeListInscriptions = mapAnneeListInscriptions;
    }

    public boolean isAFSeul() {
        return isAFSeul;
    }

    public void setAFSeul(boolean isAFSeul) {
        this.isAFSeul = isAFSeul;
    }

    public boolean isSimulation() {
        return isSimulation;
    }

    public void setSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

}