package ch.globaz.corvus.process.dnra;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.loader.PaysLoader;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MutationParserTest {

    @Mock
    PaysLoader paysLoader;

    @Test
    public void testParse() throws Exception {

        Mutation mutation = MutationParser
                .parse("7560050911013;7560050911013;1;1;\"NOM,PRENOM\";1;03021977;100;965000;10102016;5;3;05052001");
        assertThat(mutation.getNss()).isEqualTo("756.0050.9110.13");
        assertThat(mutation.getNewNss()).isEqualTo("756.0050.9110.13");
        assertThat(mutation.getTypeMutation()).isEqualTo(TypeMutation.QUOTIDIEN);
        assertThat(mutation.isValide()).isTrue();
        assertThat(mutation.getNom()).isEqualTo("NOM");
        assertThat(mutation.getPrenom()).isEqualTo("PRENOM");
        assertThat(mutation.getSexe()).isEqualTo(Sexe.HOMME);
        assertThat(mutation.getDateNaissance()).isEqualTo(new DateRente("03.02.1977", Date.DATE_PATTERN_SWISS));
        assertThat(mutation.getCodeNationalite()).isEqualTo("100");
        assertThat(mutation.getDateDece()).isEqualTo(new DateRente("10.10.2016", Date.DATE_PATTERN_SWISS));
        // assertThat(mutation.getCodeEtatCivil()).isEqualTo("5");
        assertThat(mutation.getRaisonDuPartenariatDissous()).isEqualTo("3");
        assertThat(mutation.getDateChangementEtatCivil()).isEqualTo(
                new DateRente("05.05.2001", Date.DATE_PATTERN_SWISS));
    }

    @Test
    public void testParseNomPrenomEmpty() throws Exception {

        Mutation mutation = MutationParser
                .parse("7560050911013;7560050911013;1;1;\",\";1;03021977;100;965000;10102016;5;3;05052001");
        assertThat(mutation.getNss()).isEqualTo("756.0050.9110.13");
        assertThat(mutation.getNewNss()).isEqualTo("756.0050.9110.13");
        assertThat(mutation.getTypeMutation()).isEqualTo(TypeMutation.QUOTIDIEN);
        assertThat(mutation.isValide()).isTrue();
        assertThat(mutation.getNom()).isNull();
        assertThat(mutation.getPrenom()).isNull();
        assertThat(mutation.getSexe()).isEqualTo(Sexe.HOMME);
        assertThat(mutation.getDateNaissance()).isEqualTo(new DateRente("03.02.1977", Date.DATE_PATTERN_SWISS));
        assertThat(mutation.getCodeNationalite()).isEqualTo("100");
        assertThat(mutation.getDateDece()).isEqualTo(new Date("10.10.2016"));
        // assertThat(mutation.getCodeEtatCivil()).isEqualTo("5");
        assertThat(mutation.getRaisonDuPartenariatDissous()).isEqualTo("3");
        assertThat(mutation.getDateChangementEtatCivil()).isEqualTo(
                new DateRente("05.05.2001", Date.DATE_PATTERN_SWISS));
    }

    @Test
    public void testParsFile() throws Exception {
       // when(paysLoader.resolveById(any(String.class))).thenReturn(new Pays());
        // List<String> files = Arrays
        // .asList(new String[] { "pucs1", "swissDec1", "swissDec2", "swissDecMix", "swissDec3" });
        final File folder = new File("src/test/resources/ch/globaz/corvus/dnra");
        MutationParser mutationParser = new MutationParser();
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                mutationParser.parsFile(file.getAbsolutePath(), paysLoader);
            }
        }

    }

}
