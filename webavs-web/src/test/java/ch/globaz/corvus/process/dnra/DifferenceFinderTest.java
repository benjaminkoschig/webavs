package ch.globaz.corvus.process.dnra;

import static org.fest.assertions.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;

public class DifferenceFinderTest {

    private Locale locale = Locale.FRANCE;

    @Ignore
    @Test
    public void testFindDifferenceEmpty() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(new Mutation(), new InfoTiers());
        assertThat(list).isEmpty();
    }

    @Ignore
    @Test
    public void testFindDifferenceNomPrenom() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        Mutation mutation = new Mutation();
        mutation.setNom("Nom");
        mutation.setPrenom("prenom");
        mutation.setNewNss("756.123");
        mutation.setDateDece(new Date());
        mutation.setDateNaissance(new Date());
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

    @Ignore
    @Test
    public void testFindDifferenceEtatCivil() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        Mutation mutation = new Mutation();
        mutation.setEtatCivil(EtatCivil.CELIBATAIRE);
        mutation.setDateChangementEtatCivil(new Date());
        InfoTiers infoTiers = new InfoTiers();
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.ETAT_CIVIL);
        assertThat(list.get(0).getDateChangement()).isEqualTo(mutation.getDateChangementEtatCivil());
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getEtatCivil().toString());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Ignore
    @Test
    public void testFindDifferenceDateDece() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        Mutation mutation = new Mutation();
        mutation.setDateDece(new Date("10.10.2015"));
        mutation.setDateChangementEtatCivil(new Date());
        InfoTiers infoTiers = new InfoTiers();
        infoTiers.setDateDeces(null);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.DATE_DECES);
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getDateDece().getSwissValue());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Ignore
    @Test
    public void testFindDifferenceDateNaissance() throws Exception {
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        Mutation mutation = new Mutation();
        mutation.setDateNaissance(new Date("10.10.2015"));
        mutation.setDateChangementEtatCivil(new Date());
        InfoTiers infoTiers = new InfoTiers();
        infoTiers.setDateNaissance(null);
        List<DifferenceTrouvee> list = differenceFinder.findDifference(mutation, infoTiers);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDifference()).isEqualTo(TypeDifference.DATE_NAISSANCE);
        assertThat(list.get(0).getValeurNouvelle()).isEqualTo(mutation.getDateNaissance().getSwissValue());
        assertThat(list.get(0).getValeurActuelle()).isNull();
    }

    @Ignore
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

    @Ignore
    @Test
    public void testIsNomSameUpercase() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        mutation.setNom("NOM");
        infoTiers.setNom("Nom");
        assertThat(DifferenceFinder.isNomSame(mutation, infoTiers)).isTrue();
    }

    @Ignore
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

    @Ignore
    @Test
    public void testIsPrenomSameUpercase() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        mutation.setPrenom("Prenom");
        infoTiers.setPrenom("PRENOM");
        assertThat(DifferenceFinder.isPrenomSame(mutation, infoTiers)).isTrue();
    }

    @Ignore
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

    @Ignore
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

    @Ignore
    @Test
    public void testIsDateNaissanceSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isTrue();
        mutation.setDateNaissance(new Date("10.10.2000"));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isFalse();
        infoTiers.setDateNaissance(new Date("10.10.2000"));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isTrue();
        infoTiers.setDateNaissance(new Date("10.11.2000"));
        assertThat(DifferenceFinder.isDateNaissanceSame(mutation, infoTiers)).isFalse();
    }

    @Ignore
    @Test
    public void testIsDateDecesSame() throws Exception {
        Mutation mutation = new Mutation();
        InfoTiers infoTiers = new InfoTiers();
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isTrue();
        mutation.setDateDece(new Date("10.10.2000"));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isFalse();
        infoTiers.setDateDeces(new Date("10.10.2000"));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isTrue();
        infoTiers.setDateDeces(new Date("10.11.2000"));
        assertThat(DifferenceFinder.isDateDecesSame(mutation, infoTiers)).isFalse();
    }

    @Ignore
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

    @Ignore
    @Test
    public void testFindAllDifference() throws Exception {

        List<Mutation> mutations = new ArrayList<Mutation>();
        List<InfoTiers> infosTiers = new ArrayList<InfoTiers>();
        for (int i = 0; i < 2; i++) {

            Pays pays = new Pays();
            pays.setTraductionParLangue(new HashMap<Langues, String>());

            Mutation mutation = new Mutation();
            mutation.setNom("Nom_mutation");
            mutation.setPrenom("Prenom_mutation");
            mutation.setNss(i + "");
            mutation.setDateDece(new Date());
            mutation.setDateNaissance(new Date());
            mutation.setDateChangementEtatCivil(new Date());
            mutation.setSexe(Sexe.FEMME);
            mutation.setEtatCivil(EtatCivil.CELIBATAIRE);
            mutation.setCodeNationalite("4545");
            mutations.add(mutation);
            mutation.setPays(pays);
            InfoTiers tiers = new InfoTiers();
            tiers.setNss(mutation.getNss());
            tiers.setPays(pays);

            infosTiers.add(tiers);
        }
        DifferenceFinder differenceFinder = new DifferenceFinder(locale);
        List<DifferenceTrouvee> diffs = differenceFinder.findAllDifference(mutations, infosTiers);
        assertThat(diffs).hasSize(14);
    }
}
