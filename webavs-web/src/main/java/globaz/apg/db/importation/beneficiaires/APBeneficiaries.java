package globaz.apg.db.importation.beneficiaires;

import java.util.ArrayList;
import java.util.List;

public class APBeneficiaries {
    private String idDemande = "";
    private List<APBeneficiary> beneficiaries;

    public APBeneficiaries(String idDemande){
        this.idDemande = idDemande;
        beneficiaries = new ArrayList<>();
    }

    public void addBeneficiaries (APBeneficiary beneficiary) {
        beneficiaries.add(beneficiary);
    }

    public List<APBeneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<APBeneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }
}
