package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import org.junit.Test;

public class Rule402Test extends RuleTest {
    @Test
    public void testRule402Avant2021() {
        try {
            Rule402 rule = new Rule402("302");
            APChampsAnnonce champsAnnonce = new APChampsAnnonce();
            // Le reférence number doit être de longueur > 5 pour que le type de protection civile soit cours de répétition
            champsAnnonce.setReferenceNumber("11111");
            champsAnnonce.setStartOfPeriod("01.01.2020");
            champsAnnonce.setServiceType("20");
            assertTrue(rule.check(champsAnnonce));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (APRuleExecutionException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testRule402Après2020ServiceCivilNormal() {
        try {
            Rule402 rule = new Rule402("302");
            APChampsAnnonce champsAnnonce = new APChampsAnnonce();
            // Le reférence number doit être de longueur > 5 pour que le type de protection civile soit cours de répétition
            champsAnnonce.setReferenceNumber("11111");
            champsAnnonce.setStartOfPeriod("01.01.2021");
            champsAnnonce.setServiceType("20");
            assertTrue(rule.check(champsAnnonce));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (APRuleExecutionException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testRule402Après2020ServiceCivilSpéciale() {
        try {
            Rule402 rule = new Rule402("302");
            APChampsAnnonce champsAnnonce = new APChampsAnnonce();
            // Le reférence number doit être de longueur > 5 pour que le type de protection civile soit cours de répétition
            champsAnnonce.setReferenceNumber("11111");
            champsAnnonce.setStartOfPeriod("01.01.2021");
            champsAnnonce.setServiceType("22");
            assertTrue(rule.check(champsAnnonce));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (APRuleExecutionException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
