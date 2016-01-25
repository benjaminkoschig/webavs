package ch.globaz.vulpecula.web.gson;

import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.servicemilitaire.TauxServiceMilitaire;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

public class TauxPrestationGSON {
    private String idAssurance;
    private String tauxAssurance;

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getTauxAssurance() {
        return tauxAssurance;
    }

    public void setTauxAssurance(String tauxAssurance) {
        this.tauxAssurance = tauxAssurance;
    }

    public TauxServiceMilitaire convertToDomain() {
        TauxServiceMilitaire tauxServiceMilitaire = new TauxServiceMilitaire();
        Assurance assurance = new Assurance();
        assurance.setId(idAssurance);
        tauxServiceMilitaire.setAssurance(assurance);
        tauxServiceMilitaire.setTaux(new Taux(tauxAssurance));
        return tauxServiceMilitaire;
    }
}
