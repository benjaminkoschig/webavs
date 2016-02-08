package ch.globaz.corvus.process.dnra;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ch.globaz.common.codesystem.CodeSystemeResolver;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;

@RunWith(MockitoJUnitRunner.class)
public class DifferenceFinderTest {

    private Locale locale = Locale.FRANCE;

    @Mock
    private CodeSystemeResolver codeSystemeResolver;

    @Test
    public void testFindDifferenceEmpty() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(new Mutation(), new InfoTiers());
        assertThat(list).isEmpty();
    }

    @Test
    public void testFindDifferenceNomPrenom() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        Mutation mutation = new Mutation();
        mutation.setNom("Nom");
        mutation.setPrenom("prenom");
        mutation.setNewNss("756.123");
        mutation.setDateDece(new Date());
        mutation.setDateNaissance(new DateRente());
        mutation.setSexe(Sexe.FEMME);

        mutation.setEtatCivil(EtatCivil.CELIBATAIRE);
        mutation.setCodeNationalite("4545");
        Pays pays = new Pays();
        pays.setTraductionParLangue(new HashMap<Langues, String>());
        mutation.setPays(pays);
        InfoTiers infoTiers = new InfoTiers();
        infoTiers.setPays(pays);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(8);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.NOM);
    }

    @Test
    public void testFindDifferenceEtatCivil() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        Mutation mutation = new Mutation();
        mutation.setEtatCivil(EtatCivil.CELIBATAIRE);
        mutation.setDateChangementEtatCivil(new DateRente());
        InfoTiers infoTiers = new InfoTiers();
        when(codeSystemeResolver.resovleTraduction(any(Integer.class))).thenReturn(EtatCivil.CELIBATAIRE.toString());

        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.ETAT_CIVIL);
        assertThat(list.get(0).getDateChangement()).isEqualTo(mutation.getDateChangementEtatCivil());
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getEtatCivil().toString());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Test
    public void testFindDifferenceDateDece() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        Mutation mutation = new Mutation();
        mutation.setDateDece(new DateRente("10.10.2015", Date.DATE_PATTERN_SWISS));
        mutation.setDateChangementEtatCivil(new DateRente("11.12.2000", Date.DATE_PATTERN_SWISS));
        mutation.setEtatCivil(EtatCivil.UNDEFINED);

        InfoTiers infoTiers = new InfoTiers();
        infoTiers.setDateDeces(null);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.DATE_DECES);
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getDateDece().getSwissValue());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Test
    public void testFindDifferenceDateNaissance() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        Mutation mutation = new Mutation();
        mutation.setDateNaissance(new DateRente("10.10.2015", Date.DATE_PATTERN_SWISS));
        mutation.setDateChangementEtatCivil(new DateRente("11.12.2000", Date.DATE_PATTERN_SWISS));
        mutation.setEtatCivil(EtatCivil.UNDEFINED);
        InfoTiers infoTiers = new InfoTiers();
        infoTiers.setDateNaissance(null);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.DATE_NAISSANCE);
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getDateNaissance().getSwissValue());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Test
    public void testIsNomSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isTrue();
        mutation.setNom("Nom");
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isFalse();
        infoTiers.setNom("Nom");
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isTrue();
        infoTiers.setNom("NoM tttt");
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsNomSameUpercase() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        mutation.setNom("NOM");
        infoTiers.setNom("Nom");
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isTrue();
    }

    @Test
    public void testIsPrenomSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isTrue();
        mutation.setPrenom("Prenom");
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isFalse();
        infoTiers.setPrenom("Prenom");
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isTrue();
        infoTiers.setPrenom("Prenom2");
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsPrenomSameUpercase() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        mutation.setPrenom("Prenom");
        infoTiers.setPrenom("PRENOM");
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isTrue();
    }

    @Test
    public void testIsNssSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isNssSame(mutation, infoTiers)).isTrue();
        mutation.setNewNss("nss");
        assertThat(DifferenceFinder.isNssSame(mutation, infoTiers)).isFalse();
        infoTiers.setNss("nss");
        assertThat(DifferenceFinder.isNssSame(mutation, infoTiers)).isTrue();
        infoTiers.setNss("nsS");
        assertThat(DifferenceFinder.isNssSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsSexeSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isSexeSame(mutation, infoTiers)).isTrue();
        mutation.setSexe(Sexe.FEMME);
        assertThat(DifferenceFinder.isSexeSame(mutation, infoTiers)).isFalse();
        infoTiers.setSexe(Sexe.FEMME);
        assertThat(DifferenceFinder.isSexeSame(mutation, infoTiers)).isTrue();
        infoTiers.setSexe(Sexe.HOMME);
        assertThat(DifferenceFinder.isSexeSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsDateNaissanceSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isTrue();
        mutation.setDateNaissance(new DateRente("10.10.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isFalse();
        infoTiers.setDateNaissance(new DateRente("10.10.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isTrue();
        infoTiers.setDateNaissance(new DateRente("10.11.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsDateDecesSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isTrue();
        mutation.setDateDece(new DateRente("10.10.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isFalse();
        infoTiers.setDateDeces(new DateRente("10.10.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isTrue();
        infoTiers.setDateDeces(new DateRente("10.11.2000", Date.DATE_PATTERN_SWISS));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isFalse();
        mutation.setDateDece(null);
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testIsNationaliteSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isNationaliteSame(mutation, infoTiers)).isTrue();
        mutation.setCodeNationalite("3");
        assertThat(DifferenceFinder.isNationaliteSame(mutation, infoTiers)).isFalse();
        infoTiers.setCodeNationalite("3");
        assertThat(DifferenceFinder.isNationaliteSame(mutation, infoTiers)).isTrue();
        infoTiers.setCodeNationalite("34");
        assertThat(DifferenceFinder.isNationaliteSame(mutation, infoTiers)).isFalse();
    }

    @Test
    public void testFindAllDifference() throws Exception {

        List<Mutation> mutations = new ArrayList<Mutation>();
        Set<InfoTiers> infosTiers = new HashSet<InfoTiers>();
        for (int i = 0; i < 2; i++) {

            Pays pays = new Pays();
            pays.setTraductionParLangue(new HashMap<Langues, String>());

            Mutation mutation = new Mutation();
            mutation.setNom("Nom_mutation");
            mutation.setPrenom("Prenom_mutation");
            mutation.setNss(i + "");
            mutation.setNewNss(i + "");
            mutation.setDateDece(new Date());
            mutation.setDateNaissance(new DateRente());
            mutation.setDateChangementEtatCivil(new DateRente());
            mutation.setSexe(Sexe.FEMME);
            mutation.setEtatCivil(EtatCivil.CELIBATAIRE);
            mutation.setCodeNationalite("4545");
            mutation.setTypeMutation(TypeMutation.QUOTIDIEN);
            mutations.add(mutation);
            mutation.setPays(pays);
            InfoTiers tiers = new InfoTiers();
            tiers.setNss(mutation.getNss());
            tiers.setPays(pays);

            infosTiers.add(tiers);
        }
        DifferenceFinder differenceFinder = new DifferenceFinder(locale, codeSystemeResolver);
        Set<InfoTiers> infosTiersNssInvalide = new HashSet<InfoTiers>();
        List<DifferenceTrouvee> diffs = differenceFinder
                .findAllDifference(mutations, infosTiers, infosTiersNssInvalide);
        assertThat(diffs).hasSize(14);
    }
}
