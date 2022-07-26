package globaz.musca.api.musca;

import java.util.Objects;

public class PaireIdEcheanceIdPlanRecouvrementEBill {

    private String idEcheance;
    private String idPlanRecouvrement;

    public PaireIdEcheanceIdPlanRecouvrementEBill(String idEcheance, String idPlanRecouvrement) {
        this.idEcheance = idEcheance;
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaireIdEcheanceIdPlanRecouvrementEBill that = (PaireIdEcheanceIdPlanRecouvrementEBill) o;
        return Objects.equals(idEcheance, that.idEcheance) && Objects.equals(idPlanRecouvrement, that.idPlanRecouvrement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEcheance, idPlanRecouvrement);
    }
}