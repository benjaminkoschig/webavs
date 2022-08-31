package globaz.pavo.db.inscriptions.declaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CIImportPucsDetailResultBean {

    private String numeroAffilie = "";
    private boolean isAFSeul = false;
    private boolean isSimulation = false;
    private String designationAffilie = "";
    private Map<String, List<CIImportPucsDetailResultInscriptionBean>> mapAnneeListInscriptions = new HashMap<String, List<CIImportPucsDetailResultInscriptionBean>>();
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

    public Map<String, List<CIImportPucsDetailResultInscriptionBean>> getMapAnneeListInscriptions() {
        return mapAnneeListInscriptions;
    }

    public void setMapAnneeListInscriptions(
            Map<String, List<CIImportPucsDetailResultInscriptionBean>> mapAnneeListInscriptions) {
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