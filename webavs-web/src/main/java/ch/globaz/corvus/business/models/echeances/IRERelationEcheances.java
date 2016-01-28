package ch.globaz.corvus.business.models.echeances;

import java.util.Set;

public interface IRERelationEcheances {

    public abstract String getCsSexeConjoint();

    public abstract String getCsTypeRelation();

    public abstract String getDateDebut();

    public abstract String getDateDecesConjoint();

    public abstract String getDateFin();

    public abstract String getDateNaissanceConjoint();

    public abstract String getIdRelation();

    public abstract String getIdTiersConjoint();

    public abstract String getNomConjoint();

    public abstract String getPrenomConjoint();

    public abstract Set<IRERenteEcheances> getRentesDuConjoint();

}