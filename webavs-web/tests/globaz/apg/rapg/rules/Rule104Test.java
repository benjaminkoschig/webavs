package globaz.apg.rapg.rules;

import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class Rule104Test extends RuleTest {

    private final static String TS1 = "1374069137767";
    private final static String TS2 = "1374069144008";
    private final static String TS3 = "1374069171682";
    private final static String TS4 = "1374069180802";
    private final static String TS5 = "1374069189713";
    private final static String TS6 = "1374069199888";
    private List<String> timeStamps = new ArrayList<String>();

    public Rule104Test() {
        timeStamps.add(Rule104Test.TS1);
        timeStamps.add(Rule104Test.TS2);
        timeStamps.add(Rule104Test.TS3);
        timeStamps.add(Rule104Test.TS4);
        timeStamps.add(Rule104Test.TS5);
    }

    @Test
    public void test() {

        try {

            // Si l'annonce est de type 1, pas de test effectué sur le timeStamp
            Rule104 rule = getRule104();
            APChampsAnnonce champsAnnonce = new APChampsAnnonce();
            champsAnnonce.setTimeStamp(Rule104Test.TS1);
            champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType1);
            assertTrue(rule.check(champsAnnonce));

            // Annonce type 3, le timeStamp n'existe pas
            champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType3);
            champsAnnonce.setTimeStamp(Rule104Test.TS6);
            assertTrue(rule.check(champsAnnonce));

            // Annonce type 3, le timeStamp existe
            champsAnnonce.setSubMessageType(APAnnoncesRapgService.subMessageType3);
            champsAnnonce.setTimeStamp(Rule104Test.TS5);
            assertFalse(rule.check(champsAnnonce));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (APRuleExecutionException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private Rule104 getRule104() {
        Rule104 rule = new Rule104("104");
        rule.setDataBaseDataProvider(new APRuleDBDataProvider() {
            @Override
            public boolean isTimeStampUnique(String timeStamp, BSession session) throws APRuleExecutionException {
                return !timeStamps.contains(timeStamp);
            }

            @Override
            public boolean isCodePaysExistant(String insurantDomicileCountry, BSession session)
                    throws APRuleExecutionException {
                throw new RuntimeException("Not implemented yet...");
            }
        });
        return rule;
    }
}
