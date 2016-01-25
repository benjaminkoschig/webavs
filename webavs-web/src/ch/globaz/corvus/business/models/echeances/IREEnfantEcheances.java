package ch.globaz.corvus.business.models.echeances;

import java.util.Set;

public interface IREEnfantEcheances extends Comparable<IREEnfantEcheances> {

    public abstract String getDateDeces();

    public abstract String getDateNaissance();

    public abstract String getIdTiers();

    public abstract String getNom();

    public abstract String getPrenom();

    public abstract Set<IRERenteEcheances> getRentes();

}