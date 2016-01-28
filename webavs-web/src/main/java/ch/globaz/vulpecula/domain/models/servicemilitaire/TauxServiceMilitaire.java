package ch.globaz.vulpecula.domain.models.servicemilitaire;

import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

public class TauxServiceMilitaire implements DomainEntity {
    private String id;
    private String idServiceMilitaire;
    private Assurance assurance;
    private Taux taux;
    private String spy;

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdServiceMilitaire() {
        return idServiceMilitaire;
    }

    public void setIdServiceMilitaire(String idServiceMilitaire) {
        this.idServiceMilitaire = idServiceMilitaire;
    }

    public String getLibelleAssurance(Locale locale) {
        return assurance.getLibelle(locale);
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    /**
     * Retourne la valeur du taux lié au taux service militaire.
     * 
     * @return String représentant le taux ou null si inexistant
     */
    public String getTauxValue() {
        if (taux == null) {
            return null;
        }
        return taux.getValue();
    }

    public TypeAssurance getTypeAssurance() {
        return assurance.getTypeAssurance();
    }

}
