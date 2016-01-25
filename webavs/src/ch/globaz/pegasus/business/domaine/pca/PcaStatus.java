package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;

public enum PcaStatus implements CodeSystemEnum<PcaStatus> {

    REFUS(IPCValeursPlanCalcul.STATUS_REFUS),
    OCTROI_PARTIEL(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL),
    OCTROI(IPCValeursPlanCalcul.STATUS_OCTROI);

    private String id;

    PcaStatus(String idCodeSysteme) {
        id = idCodeSysteme;
    }

    public boolean isRefus() {
        return equals(REFUS);
    }

    public boolean isOctroi() {
        return equals(OCTROI);
    }

    public boolean isOctroiPartiel() {
        return equals(OCTROI_PARTIEL);
    }

    @Override
    public String getValue() {
        return id;
    }

    public static PcaStatus fromValue(String id) {
        return CodeSystemEnumUtils.valueOfById(id, PcaStatus.class);
    }

}
