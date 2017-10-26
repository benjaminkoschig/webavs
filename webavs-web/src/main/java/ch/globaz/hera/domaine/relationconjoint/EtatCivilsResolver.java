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
                // Si le conjoint n'est pas mort, alors ils sont mariés ou partenariat enregistre
                return resolveMarieOrLpart(relationConjoint);
            } else {
                // il est mort
                if (date == null || date.after(relationConjoint.getDateDecesConjoint())) {
                    // et aucune date n'est fournie, il est veuf ou partenariat dissous par deces
                    if (relationConjoint.hasSameSexe()) {
                        return EtatCivil.LPART_DIS_DECES;
                    }
                    return EtatCivil.VEUF;
                }
                return resolveMarieOrLpart(relationConjoint);
            }
        } else if (relation.isConjointDivorce()) {
            if (relationConjoint.hasSameSexe()) {
                return EtatCivil.LPART_DISSOUT;
            }
            return EtatCivil.DIVORCE;
        } else if (relation.isSepareDeFait()) {
            if (relationConjoint.hasSameSexe()) {
                return EtatCivil.LPART_SEPARE_FAIT;
            }
            return EtatCivil.SEPARE_DE_FAIT;
        } else if (relation.isSePare()) {
            if (relationConjoint.hasSameSexe()) {
                return EtatCivil.LPART_DISSOUT;
            }
            return EtatCivil.SEPARE;
        }
        return null;
    }

    private EtatCivil resolveMarieOrLpart(RelationConjoint relationConjoint) {
        if (relationConjoint.hasSameSexe()) {
            return EtatCivil.LPART;
        }
        return EtatCivil.MARIE;
    }
}
