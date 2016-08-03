package globaz.pavo.db.inscriptions.declaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CIImportPucs4DetailResultBean {

    private String numeroAffilie = "";
    private String designationAffilie = "";
    private Map<String, List<CIImportPucs4DetailResultInscriptionBean>> mapAnneeListInscriptions = new HashMap<String, List<CIImportPucs4DetailResultInscriptionBean>>();

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

}