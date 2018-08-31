package ch.globaz.vulpecula.business.models.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JPA
 *
 */
public class AssociationCotisationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 7865949937484423604L;

    private String forId;
    private String forIdEmployeur;
    private String forIdAssociationProfessionnelle;
    private List<String> forIdAssociationProfessionnelleIn;
    private String forDateDebut;
    private String forDateFin;
    public final static String WHERE_FACTURATION_AP = "FACTURATION";
    public final static String ORDERBY_PRINTORDER = "printOrder";

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

    public String getForIdAssociationProfessionnelle() {
        return forIdAssociationProfessionnelle;
    }

    public void setForIdAssociationProfessionnelle(String forIdAssociationProfessionnelle) {
        this.forIdAssociationProfessionnelle = forIdAssociationProfessionnelle;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateDebut(Date date) {
        setForDateDebut(date.getSwissValue());
    }

    public void setForDateFin(Date date) {
        setForDateFin(date.getSwissValue());
    }

    public List<String> getForIdAssociationProfessionnelleIn() {
        return forIdAssociationProfessionnelleIn;
    }

    public void setForIdAssociationProfessionnelleIn(List<String> forIdAssociationProfessionnelleIn) {
        this.forIdAssociationProfessionnelleIn = forIdAssociationProfessionnelleIn;
    }

    @Override
    public Class<AssociationCotisationComplexModel> whichModelClass() {
        return AssociationCotisationComplexModel.class;
    }
}
