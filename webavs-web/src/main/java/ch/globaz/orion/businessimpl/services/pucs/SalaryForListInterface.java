package ch.globaz.orion.businessimpl.services.pucs;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.DateSwissFormatConverter;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.common.listoutput.converterImplemented.NssConverter;
import ch.globaz.common.listoutput.converterImplemented.SexeHFConverter;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalaryConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.NullValue;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ PeriodeSalaryConverter.class, DateSwissFormatConverter.class, MontantConverter.class })
public interface SalaryForListInterface {

    @ColumnStyle(width = "9.7%")
    @ColumnValueConverter({ NssConverter.class })
    @Column(name = "Nss", order = 1, nullValue = NullValue.EMPTY)
    public String getNss();

    @Column(name = "NomPrenom", order = 2)
    public String getNomPrenom();

    @ColumnStyle(width = "6.7%", align = Align.CENTER)
    @Column(name = "DateNaissance", order = 3)
    public Date getDateNaissance();

    @ColumnValueConverter({ SexeHFConverter.class })
    @ColumnStyle(width = "3.7%", align = Align.CENTER)
    @Column(name = "Sexe", order = 4)
    public String getSexe();

    @ColumnStyle(width = "13%", align = Align.CENTER)
    @Column(name = "Periode", order = 5)
    public PeriodeSalary getPeriode();

    @ColumnStyle(width = "5%", align = Align.CENTER)
    @Column(name = "Canton", order = 6)
    public String getCanton();

    @ColumnStyle(width = "8%", align = Align.RIGHT)
    @Column(name = "Salaire", order = 7)
    public Montant getSlaire();

    @ColumnStyle(width = "8%", align = Align.RIGHT)
    @Column(name = "Ac1", order = 8)
    public Montant getAc1();

    @ColumnStyle(width = "8%", align = Align.RIGHT)
    @Column(name = "Ac2", order = 9)
    public Montant getAc2();

    @ColumnStyle(width = "8%", align = Align.RIGHT)
    @Column(name = "Af", order = 10)
    public Montant getAf();

    @ColumnStyle(width = "5%", align = Align.CENTER)
    @Column(name = "CantonAf", order = 11)
    public String getCantonAf();

}
