package ch.globaz.corvus.business.models.echeances;

import globaz.jade.client.util.JadePeriodWrapper;

public interface IREPeriodeEcheances extends Comparable<IREPeriodeEcheances> {

    public abstract String getCsTypePeriode();

    public abstract String getDateDebut();

    public abstract String getDateFin();

    public abstract String getIdPeriode();

    public JadePeriodWrapper getPeriode();
}