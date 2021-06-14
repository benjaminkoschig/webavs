package globaz.apg.rapg.rules;

import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APPeriodeAPG;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class Rule418Test {
    @Test
    public void getNombreJoursAAjouter_avecChevauchementMemeMois_5(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,3, 1);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2021,3, 5);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(5);
    }

    @Test
    public void getNombreJoursAAjouter_sansChevauchementMemeMois_0(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,3, 1);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2021,3, 21);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(0);
    }

    @Test
    public void getNombreJoursAAjouter_avecChevauchementDifferentMois_5(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,3, 27);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2021,4, 1);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(5);
    }

    @Test
    public void getNombreJoursAAjouter_sansChevauchementDifferentMois_5(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,3, 1);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2021,4, 5);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(0);
    }

    @Test
    public void getNombreJoursAAjouter_sansChevauchementDifferentMoisAnnee_5(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,12, 1);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2022,1, 5);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(0);
    }

    @Test
    public void getNombreJoursAAjouter_avecChevauchementDifferentMoisAnnee_5(){
        Rule418 rule = new Rule418("err");
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        LocalDate dateDebutPeriodeApg = LocalDate.of(2021,12, 27);
        LocalDate dateDebutPeriodeAComparer = LocalDate.of(2022,1, 1);
        periodeAPG.setDateDebutPeriode(Dates.formatSwiss(dateDebutPeriodeApg));
        periodeAPG.setDateFinPeriode(Dates.formatSwiss(dateDebutPeriodeApg.plusDays(10)));

        assertThat(rule.getNombreJoursAAjouter(periodeAPG, Dates.formatSwiss(dateDebutPeriodeAComparer), Dates.formatSwiss(dateDebutPeriodeAComparer.plusDays(4)))).isEqualTo(5);
    }

}