package globaz.cygnus.process.soinAdomicile;

import static org.fest.assertions.api.Assertions.*;
import globaz.cygnus.process.financementSoin.NSS;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;

public class LigneFichierTest {

    @Test
    public void testLigneFichierCorrect() throws Exception {
        String l = "1;Courrendlin-Val Terbi;756.0000.0000.00;Tartempion Juliette;5.00;01.01.2017;06.01.2017;6;30.00;25.02.2017;;";
        String[] values = l.split(";");
        LigneFichier ligne = new LigneFichier(values, 0);
        assertThat(ligne.getDateDebut()).isEqualTo(new Date("01.01.2017"));
        assertThat(ligne.getDateFin()).isEqualTo(new Date("06.01.2017"));
        assertThat(ligne.getDateDecompte()).isEqualTo(new Date("25.02.2017"));
        assertThat(ligne.getFraisJournalier()).isEqualTo(new Montant("5.00"));
        assertThat(ligne.getNbJours()).isEqualTo(6);
        assertThat(ligne.getNomService()).isEqualTo("Courrendlin-Val Terbi");
        assertThat(ligne.getNumNss()).isEqualTo(new NSS("756.0000.0000.00"));
        assertThat(ligne.getTotal()).isEqualTo(new Montant("30"));
        assertThat(ligne.hasError()).isFalse();
    }

    @Test
    public void testLigneFichierDateError() throws Exception {
        String l = "1;Courrendlin-Val Terbi;756.0000.0000.00;Tartempion Juliette;5.00;01.01.2017;31.02.2017;6;30.00;25.02.2017;;";
        String[] values = l.split(";");
        LigneFichier ligne = new LigneFichier(values, 0);
        assertThat(ligne.getDateDebut()).isEqualTo(new Date("01.01.2017"));
        assertThat(ligne.getDateFin()).isNull();
        assertThat(ligne.getDateDecompte()).isEqualTo(new Date("25.02.2017"));
        assertThat(ligne.getFraisJournalier()).isEqualTo(new Montant("5.00"));
        assertThat(ligne.getNbJours()).isEqualTo(6);
        assertThat(ligne.getNomService()).isEqualTo("Courrendlin-Val Terbi");
        assertThat(ligne.getNumNss()).isEqualTo(new NSS("756.0000.0000.00"));
        assertThat(ligne.getTotal()).isEqualTo(new Montant("30"));
        assertThat(ligne.hasError()).isTrue();
    }

    @Test
    public void testLigneFichierNbJourError() throws Exception {
        String l = "1;Courrendlin-Val Terbi;756.0000.0000.00;Tartempion Juliette;5.00;01.01.2017;31.02.2017;6.5;30.00;25.02.2017;;";
        String[] values = l.split(";");
        LigneFichier ligne = new LigneFichier(values, 0);
        assertThat(ligne.getNbJours()).isNull();
        assertThat(ligne.getNomService()).isEqualTo("Courrendlin-Val Terbi");
        assertThat(ligne.getNumNss()).isEqualTo(new NSS("756.0000.0000.00"));
        assertThat(ligne.getTotal()).isEqualTo(new Montant("30"));
        assertThat(ligne.hasError()).isTrue();
    }

    @Test
    public void testLigneFichierFraisError() throws Exception {
        String l = "1;Courrendlin-Val Terbi;756.0000.0000.00;Tartempion Juliette;5.03;01.01.2017;31.02.2017;6;30.00;25.02.2017;;";
        String[] values = l.split(";");
        LigneFichier ligne = new LigneFichier(values, 0);
        assertThat(ligne.getFraisJournalier()).isEqualTo(new Montant("5.03"));
        assertThat(ligne.hasError()).isTrue();
    }
}
