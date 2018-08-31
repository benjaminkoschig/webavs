/**
 *
 */
package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class ParametreCotisationAssociationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

    @Override
    public Class<ParametreCotisationAssociationComplexModel> whichModelClass() {
        return ParametreCotisationAssociationComplexModel.class;
    }

    private String forId;
    private String forIdCotisationAssociationProfessionnelle;
    private String forGenre;
    private String forGenreNot;
    private String forLibelleLike;
    private String forMontantFourchette;
    private String forTypeParametre;
    private String forFourchetteInferieure;
    private String forFourchetteSuperieure;
    private Collection<String> forFacturerDefautIn;

    public String getForFourchetteSuperieure() {
        return forFourchetteSuperieure;
    }

    public void setForFourchetteSuperieure(String forFourchetteSuperieure) {
        this.forFourchetteSuperieure = forFourchetteSuperieure;
    }

    public String getForTypeParametre() {
        return forTypeParametre;
    }

    public void setForTypeParametre(String forTypeParametre) {
        this.forTypeParametre = forTypeParametre;
    }

    public String getForFourchetteInferieure() {
        return forFourchetteInferieure;
    }

    public void setForFourchetteInferieure(String forFourchetteInferieure) {
        this.forFourchetteInferieure = forFourchetteInferieure;
    }

    public String getForMontantFourchette() {
        return forMontantFourchette;
    }

    public void setForMontantFourchette(String forMontantFourchette) {
        this.forMontantFourchette = forMontantFourchette;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdCotisationAssociationProfessionnelle() {
        return forIdCotisationAssociationProfessionnelle;
    }

    public void setForIdCotisationAssociationProfessionnelle(String forIdCotisationAssociationProfessionnelle) {
        this.forIdCotisationAssociationProfessionnelle = forIdCotisationAssociationProfessionnelle;
    }

    public String getForGenre() {
        return forGenre;
    }

    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public String getForGenreNot() {
        return forGenreNot;
    }

    public void setForGenreNot(String forGenreNot) {
        this.forGenreNot = forGenreNot;
    }

    public String getForLibelleLike() {
        return forLibelleLike;
    }

    public void setForLibelleLike(String forLibelleLike) {
        this.forLibelleLike = "%" + forLibelleLike;
    }

    /**
     * @return the forNotfacturerDefaut
     */
    public Collection<String> getForFacturerDefautIn() {
        return forFacturerDefautIn;
    }

    /**
     * @param forNotfacturerDefaut the forNotfacturerDefaut to set
     */
    public void setForFacturerDefautIn(Collection<String> forfacturerDefaut) {
        forFacturerDefautIn = forfacturerDefaut;
    }
}
