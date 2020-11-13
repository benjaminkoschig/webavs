package globaz.apg.db.importation.turnover;

import java.util.ArrayList;
import java.util.List;

public class APTurnovers {
    private String idDemande;
    private List<APTurnover> turnovers;

    public APTurnovers(String idDemande){
        turnovers = new ArrayList<>();
        this.idDemande = idDemande;
    }

    public void addTurnover(APTurnover turnover){
        turnovers.add(turnover);
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public List<APTurnover> getTurnovers() {
        return turnovers;
    }

    public void setTurnovers(List<APTurnover> turnovers) {
        this.turnovers = turnovers;
    }
}
