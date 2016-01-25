/**
 * 
 */
package ch.globaz.vulpecula.domain.models.registre;

import java.io.Serializable;
import java.util.List;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.specifications.parametrecotisationap.ParametreCotisationAPFourchetteValideSpecification;
import ch.globaz.vulpecula.domain.specifications.parametrecotisationap.ParametreCotisationAPMandatoriesSpecification;

/**
 * Un affilié peut être rattaché à une convention
 * 
 * @author Arnaud Geiser (AGE) | Créé le 18 déc. 2013
 * 
 */
public class ParametreCotisationAssociation implements DomainEntity, Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle;
    private Taux taux;
    private Montant fourchetteDebut;
    private Montant fourchetteFin;
    private String spy;

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    public void setTaux(String taux) {
        this.taux = new Taux(taux);
    }

    public Montant getFourchetteDebut() {
        return fourchetteDebut;
    }

    public void setFourchetteDebut(Montant fourchetteDebut) {
        this.fourchetteDebut = fourchetteDebut;
    }

    public Montant getFourchetteFin() {
        return fourchetteFin;
    }

    public void setFourchetteFin(Montant fourchetteFin) {
        this.fourchetteFin = fourchetteFin;
    }

    public String getIdCotisationAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle.getId();
    }

    public CotisationAssociationProfessionnelle getCotisationAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle;
    }

    public void setCotisationAssociationProfessionnelle(
            CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle) {
        this.cotisationAssociationProfessionnelle = cotisationAssociationProfessionnelle;
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

    public void validate(List<ParametreCotisationAssociation> cotisationsList) throws UnsatisfiedSpecificationException {
        final ParametreCotisationAPFourchetteValideSpecification fourchetteValideSpecification = new ParametreCotisationAPFourchetteValideSpecification(
                cotisationsList);
        final ParametreCotisationAPMandatoriesSpecification mandatoriesSpecification = new ParametreCotisationAPMandatoriesSpecification();

        final Specification<ParametreCotisationAssociation> cotisationsSpecification = mandatoriesSpecification
                .and(fourchetteValideSpecification);

        cotisationsSpecification.isSatisfiedBy(this);
    }

    public boolean mustBeFetched() {
        return id != null && spy == null;
    }
}
