package ch.globaz.hera.domaine.relationconjoint;

import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.EtatCivil;

class EtatCivilsResolver {

    public static EtatCivilsResolver build() {
        return new EtatCivilsResolver();
    }

    public EtatCivil resolveEtatCivil(RelationConjoint relationConjoint, Date date) {

        if (relationConjoint == null || relationConjoint.getRelation() == null
                || relationConjoint.getRelation().isNone()) {
            return EtatCivil.CELIBATAIRE;
        }
        TypeRelation relation = relationConjoint.getRelation();

        if (relation.isConjointMarie()) {
            // Vérifie s'il est pas veuf
            if (!relationConjoint.isConjointDecede()) {
                // Si le conjoint n'est pas mort, alors ils sont mariés
                return EtatCivil.MARIE;
            } else {
                // il est mort
                if (date == null || date.after(relationConjoint.getDateDecesConjoint())) {
                    // et aucune date n'est fournie, il est veuf
                    return EtatCivil.VEUF;
                }
                return EtatCivil.MARIE;
            }
        } else if (relation.isConjointLPart()) {
            // Vérifie s'il est pas veuf
            if (!relationConjoint.isConjointDecede()) {
                // Si le conjoint n'est pas mort, alors ils sont partenariat enregistre
                return EtatCivil.LPART;
            } else {
                // il est mort
                if (date == null || date.after(relationConjoint.getDateDecesConjoint())) {
                    // et aucune date n'est fournie, il est partenariat dissous par deces
                    return EtatCivil.LPART_DIS_DECES;
                }
                return EtatCivil.LPART;
            }
        } else if (relation.isConjointDivorce()) {
            return EtatCivil.DIVORCE;
        } else if (relation.isConjointLPartDissous()) {
            return EtatCivil.LPART_DISSOUT;
        } else if (relation.isSepareDeFait()) {
            return EtatCivil.SEPARE_DE_FAIT;
        } else if (relation.isLPartSepareDeFait()) {
            return EtatCivil.LPART_SEPARE_FAIT;
        } else if (relation.isSepare()) {
            return EtatCivil.SEPARE;
        } else if (relation.isLPartSepare()) {
            return EtatCivil.LPART_DISSOUT;
        }
        return null;
    }

}
