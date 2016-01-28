package globaz.cygnus.db.recapitulation;

import java.util.ArrayList;

public class MutationsMensuellesData {

    private String idTiers = null;
    private String montantAugmentationAi = null;
    private String montantAugmentationAvs = null;
    private String montantDiminutionAi = null;
    private String montantDiminutionAvs = null;
    private String montantRetroAi = null;
    private String montantRetroAvs = null;
    private ArrayList<String> typePrestation = new ArrayList();

    public MutationsMensuellesData(ArrayList typePrestation, String idTiers, String montantAugmentationAvs,
            String montantDiminutionAvs, String montantRetroAvs, String montantAugmentationAi,
            String montantDiminutionAi, String montantRetroAi) {
        super();
        this.typePrestation = typePrestation;
        this.idTiers = idTiers;
        this.montantAugmentationAi = montantAugmentationAi;
        this.montantAugmentationAvs = montantAugmentationAvs;
        this.montantDiminutionAi = montantDiminutionAi;
        this.montantDiminutionAvs = montantDiminutionAvs;
        this.montantRetroAi = montantRetroAi;
        this.montantRetroAvs = montantRetroAvs;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantAugmentationAi() {
        return montantAugmentationAi;
    }

    public String getMontantAugmentationAvs() {
        return montantAugmentationAvs;
    }

    public String getMontantDiminutionAi() {
        return montantDiminutionAi;
    }

    public String getMontantDiminutionAvs() {
        return montantDiminutionAvs;
    }

    public String getMontantRetroAi() {
        return montantRetroAi;
    }

    public String getMontantRetroAvs() {
        return montantRetroAvs;
    }

    public ArrayList<String> getTypePrestation() {
        return typePrestation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantAugmentationAi(String montantAugmentationAi) {
        this.montantAugmentationAi = montantAugmentationAi;
    }

    public void setMontantAugmentationAvs(String montantAugmentationAvs) {
        this.montantAugmentationAvs = montantAugmentationAvs;
    }

    public void setMontantDiminutionAi(String montantDiminutionAi) {
        this.montantDiminutionAi = montantDiminutionAi;
    }

    public void setMontantDiminutionAvs(String montantDiminutionAvs) {
        this.montantDiminutionAvs = montantDiminutionAvs;
    }

    public void setMontantRetroAi(String montantRetroAi) {
        this.montantRetroAi = montantRetroAi;
    }

    public void setMontantRetroAvs(String montantRetroAvs) {
        this.montantRetroAvs = montantRetroAvs;
    }

    public void setTypePrestation(ArrayList<String> typePrestation) {
        this.typePrestation = typePrestation;
    }

}
