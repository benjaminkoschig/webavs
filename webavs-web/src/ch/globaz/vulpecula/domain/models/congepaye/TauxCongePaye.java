package ch.globaz.vulpecula.domain.models.congepaye;

import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

public class TauxCongePaye implements DomainEntity {
    private String id;
    private String idCongePaye;
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

    public String getIdCongePaye() {
        return idCongePaye;
    }

    public void setIdCongePaye(String idCongePaye) {
        this.idCongePaye = idCongePaye;
    }

    /**
     * Retourne le type d'assurance lié à la cotisation du taux.
     * 
     * @return Type d'assurance
     */
    public TypeAssurance getTypeAssurance() {
        return assurance.getTypeAssurance();
    }

    public String getIdAssurance() {
        return assurance.getId();
    }

    public void setIdAssurance(String idAssurance) {
        if (assurance == null) {
            assurance = new Assurance();
        }
        assurance.setId(idAssurance);
    }

    public String getAssuranceLibelle(Locale locale) {
        return assurance.getLibelle(locale);
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }
}
