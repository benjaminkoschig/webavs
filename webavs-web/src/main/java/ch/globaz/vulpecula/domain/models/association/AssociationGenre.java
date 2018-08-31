package ch.globaz.vulpecula.domain.models.association;

import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AssociationGenre {
    private final Administration association;
    private final GenreCotisationAssociationProfessionnelle genre;
    private final AssociationEmployeur associationEmployeur;

    public AssociationGenre(Administration association, GenreCotisationAssociationProfessionnelle genre,
            AssociationEmployeur associationEmployeur) {
        super();
        this.association = association;
        this.genre = genre;
        this.associationEmployeur = associationEmployeur;
    }

    public AssociationGenre(Administration association, GenreCotisationAssociationProfessionnelle genre) {
        this(association, genre, null);
    }

    public Administration getAssociation() {
        return association;
    }

    public String getLibelleAssociation() {
        return association.getDesignation1();
    }

    public GenreCotisationAssociationProfessionnelle getGenre() {
        return genre;
    }

    public AssociationEmployeur getAssociationEmployeur() {
        return associationEmployeur;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((association == null) ? 0 : association.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AssociationGenre other = (AssociationGenre) obj;
        if (association == null) {
            if (other.association != null) {
                return false;
            }
        } else if (!association.equals(other.association)) {
            return false;
        }
        if (genre != other.genre) {
            return false;
        }
        return true;
    }
}
