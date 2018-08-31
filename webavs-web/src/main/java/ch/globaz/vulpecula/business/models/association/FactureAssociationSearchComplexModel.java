package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import ch.globaz.vulpecula.domain.models.common.Annee;

public class FactureAssociationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -7250324456305339892L;

    private String forId;
    private String forIdEmployeur;
    private String forAnneeFacture;
    private String forEtat;
    private String forIdPassage;
    private String forGenre;

    private Collection<String> forIdIn;
    private Collection<String> forIdAssociationIn;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForAnneeFacture() {
        return forAnneeFacture;
    }

    public void setForAnneeFacture(String forAnneeFacture) {
        this.forAnneeFacture = forAnneeFacture;
    }

    public void setForAnneeFacture(Annee annee) {
        setForAnneeFacture(String.valueOf(annee.getValue()));
    }

    public Collection<String> getForIdIn() {
        return forIdIn;
    }

    public void setForIdIn(Collection<String> forIdIn) {
        this.forIdIn = forIdIn;
    }

    public Collection<String> getForIdAssociationIn() {
        return forIdAssociationIn;
    }

    public void setForIdAssociationIn(Collection<String> forIdAssociationIn) {
        this.forIdAssociationIn = forIdAssociationIn;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @return the forIdPassage
     */
    public String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * @param forIdPassage the forIdPassage to set
     */
    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * @return the forGenre
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * @param forGenre the forGenre to set
     */
    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    @Override
    public Class<FactureAssociationComplexModel> whichModelClass() {
        return FactureAssociationComplexModel.class;
    }

}
