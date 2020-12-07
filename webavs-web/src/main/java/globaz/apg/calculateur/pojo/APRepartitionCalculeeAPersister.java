package globaz.apg.calculateur.pojo;

import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import java.util.ArrayList;
import java.util.List;

public class APRepartitionCalculeeAPersister {

    private List<APCotisation> cotisations = new ArrayList<APCotisation>();
    private APRepartitionPaiements repartitionPaiements;
    private Integer nombreInitialDeSituationsProfessionelles = 0;

    public List<APCotisation> getCotisations() {
        return cotisations;
    }

    public APRepartitionPaiements getRepartitionPaiements() {
        return repartitionPaiements;
    }

    public void setCotisations(final List<APCotisation> cotisations) {
        this.cotisations = cotisations;
    }

    public void setRepartitionPaiements(final APRepartitionPaiements repartitionPaiements) {
        this.repartitionPaiements = repartitionPaiements;
    }

    public Integer getNombreInitialDeSituationsProfessionelles() {
        return nombreInitialDeSituationsProfessionelles;
    }

    public void setNombreInitialDeSituationsProfessionelles(Integer nombreInitialDeSituationsProfessionelles) {
        this.nombreInitialDeSituationsProfessionelles = nombreInitialDeSituationsProfessionelles;
    }
}
