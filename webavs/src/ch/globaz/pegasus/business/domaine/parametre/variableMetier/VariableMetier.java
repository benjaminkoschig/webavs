package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;

public class VariableMetier implements Parametre<VariableMetierType> {
    private VariableMetierType type;
    private VariableMetierTypeDeDonnee typeDeDonnee = null;
    private Date dateDebut;
    private Date dateFin;
    private Part part;
    private Montant montant;
    private Taux taux;
    private String id;

    public VariableMetierTypeDeDonnee getTypeDeDonnee() {
        return typeDeDonnee;
    }

    @Override
    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Part getPart() {
        if (part == null) {
            throw new RuntimeException("La part est null pour la variable métier suivante: " + type + " - > "
                    + toString());
        }
        return part;
    }

    public Taux getTaux() {
        if (taux == null) {
            throw new RuntimeException("Le taux est null pour la variable métier suivante: " + type + " - > "
                    + toString());
        }
        return taux;
    }

    public Montant getMontant() {
        if (montant == null) {
            throw new RuntimeException("Le montant est null pour la variable métier suivante: " + type + " - > "
                    + toString());
        }
        return montant;
    }

    public void setTypeDeDonnee(VariableMetierTypeDeDonnee variableMetierTypeDeDonnee) {
        typeDeDonnee = variableMetierTypeDeDonnee;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    @Override
    public VariableMetierType getType() {
        return type;
    }

    public void setType(VariableMetierType variableMetierType) {
        type = variableMetierType;
    }

    @Override
    public String toString() {
        return "VariableMetier [type=" + type + ", typeDeDonnee=" + typeDeDonnee + ", dateDebut=" + dateDebut
                + ", dateFin=" + dateFin + ", part=" + part + ", id=" + id + ", montant=" + montant + ", taux=" + taux
                + "]";
    }

}
