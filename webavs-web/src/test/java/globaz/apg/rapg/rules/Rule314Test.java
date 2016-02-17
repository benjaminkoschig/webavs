package globaz.apg.rapg.rules;

import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.pyxis.constantes.IConstantes;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class Rule314Test {

    @Test
    public void testCheckHomme65() throws Exception {

        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("12.05.2010");
        champsAnnonce.setInsurantBirthDate("12.05.1945");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);

        Assert.assertTrue(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckHomme66() throws Exception {
        // Rule ne doit pas passer.
        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("12.05.2011");
        champsAnnonce.setInsurantBirthDate("12.05.1945");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);

        Assert.assertFalse(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckHomme64() throws Exception {

        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("03.05.2010");
        champsAnnonce.setInsurantBirthDate("12.05.1945");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);

        Assert.assertTrue(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckFemme63() throws Exception {

        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("03.05.2010");
        champsAnnonce.setInsurantBirthDate("12.05.1946");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);

        Assert.assertTrue(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckFemme64() throws Exception {

        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("12.05.2010");
        champsAnnonce.setInsurantBirthDate("12.05.1946");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);

        Assert.assertTrue(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckFemme65() throws Exception {
        // Rule ne doit pas passer.
        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setServiceType("10");
        champsAnnonce.setStartOfPeriod("12.05.2011");
        champsAnnonce.setInsurantBirthDate("12.05.1946");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);

        Assert.assertFalse(rule.check(champsAnnonce, 65, 64));
    }

    @Test
    public void testCheckServiceAutorise() throws Exception {
        Rule314 rule = new Rule314("314");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);

        champsAnnonce.setStartOfPeriod("12.05.2011");
        champsAnnonce.setInsurantBirthDate("12.05.1945");
        champsAnnonce.setInsurantSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);

        List<String> serviceAutorise = Arrays.asList("10", "11", "12", "13", "14", "20", "21", "22", "23", "30", "40",
                "41", "50");

        for (String service : serviceAutorise) {
            champsAnnonce.setServiceType(service);
            Assert.assertFalse(rule.check(champsAnnonce, 65, 64));
        }

    }
}
