package ch.globaz.corvus.process.dnra;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Sexe;

public class REGenererListeDiffDnraEtRentesProcessTest {

    @Ignore
    @Test
    public void testGenerateXls() throws Exception {
        List<Mutation> mutations = new ArrayList<Mutation>();
        List<InfoTiers> infosTiers = new ArrayList<InfoTiers>();
        for (int i = 0; i < 2; i++) {
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
            InfoTiers tiers = new InfoTiers();
            tiers.setNss(mutation.getNss());
            tiers.setNom("Nom" + i);
            tiers.setPrenom("Prenom" + i);
            tiers.setDateNaissance(new Date());
            infosTiers.add(tiers);
        }
        DifferenceFinder differenceFinder = new DifferenceFinder(Locale.FRANCE);
        List<DifferenceTrouvee> diffs = differenceFinder.findAllDifference(mutations, infosTiers);
        String path = REGenererListeDiffDnraEtRentesProcess.generateXls(diffs, Locale.FRENCH);
        System.out.println(path);
    }

}
