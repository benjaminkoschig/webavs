package ch.globaz.hera.domaine.relationconjoint;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RelationConjoint implements Parametre<TypeRelation> {
    private final PersonneAVS requerant;
    private final PersonneAVS conjoint;
    private final TypeRelation relation;
    private final Date debut;
    private final Date fin;

    public RelationConjoint() {
        requerant = null;
        conjoint = null;
        relation = TypeRelation.NONE;
        debut = null;
        fin = null;
    }

    public RelationConjoint(PersonneAVS requerant, PersonneAVS conjoint, TypeRelation relation, Date debut, Date fin) {
        this.requerant = requerant;
        this.conjoint = conjoint;
        this.relation = relation;
        this.debut = debut;
        this.fin = fin;
    }

    public boolean hasSameSexe() {
        if (conjoint == null) {
            return false;
        }
        return requerant.getSexe().equals(conjoint.getSexe());
    }

    public PersonneAVS getRequerant() {
        return requerant;
    }

    public PersonneAVS getConjoint() {
        return conjoint;
    }

    public TypeRelation getRelation() {
        return relation;
    }

    public Date getDateDecesConjoint() {
        if (hasConjoint()) {
            return new Date(conjoint.getDateDeces());
        }
        return null;
    }

    public boolean isConjointDecede() {
        if (hasConjoint()) {
            return conjoint.isDecede();
        }
        return false;
    }

    public boolean hasConjoint() {
        return conjoint != null;
    }

    public boolean isEmpty() {
        return debut == null && fin == null && relation.isNone();
    }

    /**
     * Permet de résoudre l'état civils du requérant
     * 
     * @param date Date utilisé pour définir l'état civil peu être null
     * @return L'etat civils du requérant
     */
    public EtatCivil resolveEtatCivils(Date date) {
        return EtatCivilsResolver.build().resolveEtatCivil(this, date);
    }

    @Override
    public Date getDateDebut() {
        return debut;
    }

    @Override
    public Date getDateFin() {
        return fin;
    }

    @Override
    public TypeRelation getType() {
        return relation;
    }

    @Override
    public String toString() {
        return "RelationConjoint [debut=" + debut + ", fin=" + fin + ",relation=" + relation + "]";
    }
}
