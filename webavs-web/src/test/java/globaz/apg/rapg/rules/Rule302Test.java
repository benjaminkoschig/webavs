package globaz.apg.rapg.rules;

import globaz.apg.pojo.APChampsAnnonce;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class Rule302Test {

    private final static String TS1 = "1374069137767";
    private final static String TS2 = "1374069144008";
    private final static String TS3 = "1374069171682";
    private final static String TS4 = "1374069180802";
    private final static String TS5 = "1374069189713";
    private final static String TS6 = "1374069199888";
    private List<String> timeStamps = new ArrayList<String>();

    public Rule302Test() {
        timeStamps.add(Rule302Test.TS1);
        timeStamps.add(Rule302Test.TS2);
        timeStamps.add(Rule302Test.TS3);
        timeStamps.add(Rule302Test.TS4);
        timeStamps.add(Rule302Test.TS5);
    }

    @Test
    public void testCheckServiceNot20Or21() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();

        // Ne devrais pas passer car + de 30 ans
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.01.2016");
        champsAnnonce.setInsurantBirthDate("05.05.1985");

        // Rule passe quand même car pas service 20 ou 21
        champsAnnonce.setServiceType("19");
        Assert.assertTrue(rule.check(champsAnnonce));

        champsAnnonce.setServiceType("22");
        Assert.assertTrue(rule.check(champsAnnonce));
    }

    @Test
    public void testCheckAge30() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.01.2016");
        champsAnnonce.setInsurantBirthDate("05.05.1986");

        Assert.assertTrue(rule.check(champsAnnonce));

    }

    @Test
    public void testCheckAge29() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.01.2016");
        champsAnnonce.setInsurantBirthDate("05.05.1987");

        Assert.assertTrue(rule.check(champsAnnonce));

    }

    @Test
    public void testCheckAge31() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.01.2016");
        champsAnnonce.setInsurantBirthDate("05.05.1985");

        Assert.assertFalse(rule.check(champsAnnonce));

    }

    @Test
    public void testCheck1() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.11.2015");
        champsAnnonce.setInsurantBirthDate("02.06.1985");

        Assert.assertTrue(rule.check(champsAnnonce));

    }

    @Test
    public void testCheck2() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.05.2015");
        champsAnnonce.setInsurantBirthDate("02.06.1985");

        Assert.assertTrue(rule.check(champsAnnonce));

    }

    @Test
    public void testCheck3() throws Exception {

        Rule302 rule = new Rule302("302");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("20");
        champsAnnonce.setReferenceNumber("1111");
        champsAnnonce.setStartOfPeriod("01.05.2014");
        champsAnnonce.setInsurantBirthDate("02.06.1985");

        Assert.assertTrue(rule.check(champsAnnonce));

    }
}
