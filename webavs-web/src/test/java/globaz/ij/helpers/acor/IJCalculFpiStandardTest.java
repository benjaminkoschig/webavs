package globaz.ij.helpers.acor;

import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJFpiCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.helpers.prononces.IJCorrigeDepuisPrononceTest;
import globaz.ij.helpers.prononces.IJCorrigerDepuisPrononce;
import junit.framework.Assert;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class IJCalculFpiStandardTest {

    @Parameterized.Parameters
    public static ArrayList<List<String>> getPrestationsOk() {

        ArrayList<List<String>> params = new ArrayList<List<String>>();
        // date de début, date de fin,
        // nombre de jour non compté (JNC), montant journalier(RJM), montant enfant(RJME), salaire mensuel(SAL),
        // prestation attendue(PRMB), montant journalier calculé(PRMJ)
//        params.add(Arrays.asList("05.02.2024","28.02.2024","18","33.4","0","1000","198.40",""));
//        params.add(Arrays.asList("05.02.2024","28.02.2024","18","33.4","0","1000","198.40",""));
//        params.add(Arrays.asList("05.02.2024","28.02.2024","18","33.4","0","1000","198.40",""));
        params.add(Arrays.asList("05.02.2021","14.02.2021","0","16.70","0","500","166.00",""));

        return params;
    }

    /**
     * Test CalculMontantsPrestation
     */
    @Test
    public void testCalculMontantsPrestationOk() {
        for (List<String> params : getPrestationsOk()) {
            IJBaseIndemnisation baseIndemnisation = initIJBaseIndemnisation(params);
            IJPrestation prestation = new IJPrestation();
            IJFpiCalculee ijFpiCalculee = initIJFpiCalculee(params);
            IJCalculFpiStandard.calculMontantsPrestation(baseIndemnisation, ijFpiCalculee, prestation);
            Assert.assertEquals(params.get(6), prestation.getMontantBrut());
        }

    }

    private IJBaseIndemnisation initIJBaseIndemnisation(List<String> params) {
        IJBaseIndemnisation baseIndemnisation = new IJBaseIndemnisation();
        baseIndemnisation.setDateDebutPeriode(params.get(0));
        baseIndemnisation.setDateFinPeriode(params.get(1));
        baseIndemnisation.setNombreJoursNonCouverts(params.get(2));
        return  baseIndemnisation;
    }

    private IJFpiCalculee initIJFpiCalculee(List<String> params) {
        IJFpiCalculee ijFpiCalculee = new IJFpiCalculee();
        ijFpiCalculee.setMontantBase(params.get(3));
        ijFpiCalculee.setMontantEnfants(params.get(4));
        ijFpiCalculee.setSalaireMensuel(params.get(5));
        return ijFpiCalculee;
    }

}
