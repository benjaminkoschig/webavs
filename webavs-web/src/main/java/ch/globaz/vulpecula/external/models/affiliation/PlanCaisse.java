package ch.globaz.vulpecula.external.models.affiliation;

import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * @author Arnaud Geiser (AGE) | Créé le 6 févr. 2014
 *
 */
public class PlanCaisse {
    private String id;
    private Administration administration;
    private String libelle;
    private String typeAffiliation;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Administration getAdministration() {
        return administration;
    }

    public void setAdministration(final Administration administration) {
        this.administration = administration;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(final String libelle) {
        this.libelle = libelle;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setTypeAffiliation(final String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public String getIdTiersAdministration() {
        if (administration == null) {
            return null;
        }
        return administration.getIdTiers();
    }

    public String getCodeAdministration() {
        if (administration == null) {
            return null;
        }
        return administration.getCodeAdministration();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PlanCaisse) {
            PlanCaisse planCaisse = (PlanCaisse) obj;
            return planCaisse.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }
}
