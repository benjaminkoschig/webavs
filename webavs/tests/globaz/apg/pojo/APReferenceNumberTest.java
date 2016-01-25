package globaz.apg.pojo;

import globaz.apg.enums.APCentreRegionauxServiceCivil;
import globaz.prestation.enums.PRCanton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe de test du POJO APReferenceNumber. </br> Le but de la classe APReferenceNumber est d'voir un object
 * responsable et capable de décoder toutes les formes de code 'numéro de référence'
 * 
 * @author lga
 */
public class APReferenceNumberTest {

    @Test
    public void testReferenceNumberCas1() {
        APReferenceNumber refNumber = new APReferenceNumber();
        try {
            List<String> values = new ArrayList<String>();
            values.add("1111");
            values.add("1121");
            values.add("1131");
            values.add("2111");
            values.add("2121");
            values.add("2131");
            values.add("2141");
            values.add("2151");
            values.add("2161");
            values.add("3111");
            values.add("4111");
            values.add("5111");
            values.add("6111");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 1);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 1)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(1, 3)));
            }

            values.clear();
            values.add("13.11.1");
            values.add("25.12.1");
            values.add("50.11.1");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 1);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(3, 5)));
            }

            values.clear();
            values.add("25111");
            values.add("25111");
            values.add("50111");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 1);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(2, 4)));
            }

        } catch (Exception e) {
            Assert.assertFalse(true);
            e.printStackTrace();
        }

        try {
            refNumber.setReferenceNumber("51111");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReferenceNumberCas2() {
        APReferenceNumber refNumber = new APReferenceNumber();
        try {
            refNumber.setReferenceNumber("26001");
            assertTrue(refNumber.getCas() == 2);
            assertTrue(refNumber.getCodeCanton() == null);
            assertTrue(refNumber.getCodeCentreInstruction() == null);

            List<String> values = new ArrayList<String>();
            values.add("7112");
            values.add("8112");
            values.add("9112");
            values.add("2162");
            values.add("1112");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 2);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 1)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(1, 3)));
            }

            values.clear();
            values.add("25.11.2");
            values.add("25.12.2");
            values.add("50.11.2");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 2);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(3, 5)));
            }

            values.clear();
            values.add("25112");
            values.add("25112");
            values.add("50112");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 2);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getCodeCentreInstruction().getCodeAsString().equals(value.subSequence(2, 4)));
            }

        } catch (Exception e) {
            Assert.assertFalse(true);
            e.printStackTrace();
        }

        try {
            refNumber.setReferenceNumber("51112");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReferenceNumberCas3() {
        APReferenceNumber refNumber = new APReferenceNumber();
        try {
            refNumber.setReferenceNumber("26002");
            assertTrue(refNumber.getCas() == 3);
            assertTrue(refNumber.getCodeCanton() == null);
            assertTrue(refNumber.getCodeCentreInstruction() == null);

            List<String> values = new ArrayList<String>();
            values.add("1700001");
            values.add("2500001");
            values.add("5099991");
            values.add("1712341");
            values.add("2298761");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 3);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getNpa().equals(value.subSequence(2, 6)));
            }

            values.clear();
            values.add("10.2255.1");
            values.add("50.2545.1");
            values.add("16.5412.1");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 3);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 2)));
                assertTrue(refNumber.getNpa().equals(value.subSequence(3, 7)));
            }

            values.clear();
            values.add("1.5478.1");
            values.add("8.1562.1");
            values.add("1.0000.1");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 3);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 1)));
                assertTrue(refNumber.getNpa().equals(value.subSequence(2, 6)));
            }

        } catch (Exception e) {
            Assert.assertFalse(true);
            e.printStackTrace();
        }

        try {
            refNumber.setReferenceNumber("51.1111.1");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            refNumber.setReferenceNumber("50.1111.2");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReferenceNumberCas4() {
        APReferenceNumber refNumber = new APReferenceNumber();
        try {

            List<String> values = new ArrayList<String>();
            values.add("7991");
            values.add("8991");
            values.add("1991");
            values.add("6991");
            values.add("5991");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 4);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 1)));
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

            values.clear();
            values.add("25.99.1");
            values.add("20.99.1");
            values.add("50.99.1");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 4);
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

            values.clear();
            values.add("1.99.1");
            values.add("5.99.1");
            values.add("9.99.1");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 4);
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

        } catch (Exception e) {
            Assert.assertFalse(true);
            e.printStackTrace();
        }

        try {
            refNumber.setReferenceNumber("51.99.1");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            refNumber.setReferenceNumber("50.89.1");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            refNumber.setReferenceNumber("50.99.2");
            assertTrue(refNumber.getCas() == 5);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testReferenceNumberCas5() {
        APReferenceNumber refNumber = new APReferenceNumber();
        try {

            List<String> values = new ArrayList<String>();
            values.add("7992");
            values.add("8992");
            values.add("1992");
            values.add("6992");
            values.add("5992");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 5);
                assertTrue(refNumber.getCodeCanton().getCodeAsString().equals(value.substring(0, 1)));
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

            values.clear();
            values.add("25.99.2");
            values.add("20.99.2");
            values.add("50.99.2");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 5);
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

            values.clear();
            values.add("1.99.2");
            values.add("5.99.2");
            values.add("9.99.2");
            for (String value : values) {
                refNumber.setReferenceNumber(value);
                assertTrue(refNumber.getCas() == 5);
                assertTrue(refNumber.getCodeCentreInstruction().equals(APCentreRegionauxServiceCivil.FEDERAL));
                assertTrue(refNumber.getNpa() == null);
            }

        } catch (Exception e) {
            Assert.assertFalse(true);
            e.printStackTrace();
        }

        try {
            refNumber.setReferenceNumber("51.99.2");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            refNumber.setReferenceNumber("50.89.2");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            refNumber.setReferenceNumber("50.99.1");
            assertTrue(refNumber.getCas() == 4);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    private class Result {
        public Result(Integer cas, Integer canton, Integer centreInstruction, String npa) {
            this.cas = cas;
            this.canton = canton;
            this.centreInstruction = centreInstruction;
            this.npa = npa;
        }

        private Integer cas;
        private Integer canton;
        private Integer centreInstruction;
        private String npa;

        public final Integer getCanton() {
            return canton;
        }

        public final Integer getCentreInstruction() {
            return centreInstruction;
        }

        public final String getNpa() {
            return npa;
        }

        public final Integer getCas() {
            return cas;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Result [cas=");
            builder.append(cas);
            builder.append(", canton=");
            builder.append(canton);
            builder.append(", centreInstruction=");
            builder.append(centreInstruction);
            builder.append(", npa=");
            builder.append(npa);
            builder.append("]");
            return builder.toString();
        }

    }

    @Test
    public void testReferenceNumber() {
        Map<String, Result> values = new HashMap<String, APReferenceNumberTest.Result>();
        APReferenceNumber referenceNumber = new APReferenceNumber();

        int cas = 1;
        for (PRCanton canton : PRCanton.values()) {
            for (APCentreRegionauxServiceCivil centre : APCentreRegionauxServiceCivil.values()) {
                if (!centre.equals(APCentreRegionauxServiceCivil.FEDERAL)) {
                    String key = canton.getCodeAsString() + centre.getCodeAsString() + cas;
                    values.put(key, new Result(cas, canton.getCodeCanton(), centre.getCode(), null));
                }
            }
        }
        cas = 2;
        for (PRCanton canton : PRCanton.values()) {
            for (APCentreRegionauxServiceCivil centre : APCentreRegionauxServiceCivil.values()) {
                if (!centre.equals(APCentreRegionauxServiceCivil.FEDERAL)) {
                    String key = canton.getCodeAsString() + centre.getCodeAsString() + cas;
                    values.put(key, new Result(cas, canton.getCodeCanton(), centre.getCode(), null));
                }
            }
        }

        cas = 4;
        for (PRCanton canton : PRCanton.values()) {
            String key = canton.getCodeAsString() + APCentreRegionauxServiceCivil.FEDERAL.getCode() + "1";
            values.put(key, new Result(cas, canton.getCodeCanton(), APCentreRegionauxServiceCivil.FEDERAL.getCode(),
                    null));
        }

        cas = 5;
        for (PRCanton canton : PRCanton.values()) {
            String key = canton.getCodeAsString() + APCentreRegionauxServiceCivil.FEDERAL.getCode() + "2";
            values.put(key, new Result(cas, canton.getCodeCanton(), APCentreRegionauxServiceCivil.FEDERAL.getCode(),
                    null));
        }

        testCas_1_2_4_5(values, referenceNumber);

        values.clear();
        List<String> npas = new ArrayList<String>();
        npas.add("0000");
        npas.add("1256");
        npas.add("2800");
        npas.add("9999");
        npas.add("5454");
        cas = 3;
        for (PRCanton canton : PRCanton.values()) {
            for (String npa : npas) {
                String key = canton.getCodeAsString() + npa + "1";
                values.put(key, new Result(cas, canton.getCodeCanton(), null, npa));
            }
        }

        testCas_3(values, referenceNumber);
    }

    /**
     * Test les cas 1, 2, 4 et 5
     * 
     * @param values
     * @param referenceNumber
     */
    private void testCas_1_2_4_5(Map<String, Result> values, APReferenceNumber referenceNumber) {
        for (String key : values.keySet()) {
            try {
                System.out.print(key + " : ");
                referenceNumber.setReferenceNumber(key);
            } catch (Exception e) {
                System.err.println("Exception for key : " + key + ". Result = " + values.get(key).toString());
                e.printStackTrace();
            }
            Result result = values.get(key);
            assertTrue(result.getCas() == referenceNumber.getCas());
            assertTrue(result.getCanton() == referenceNumber.getCodeCanton().getCodeCanton());
            assertTrue(result.getCentreInstruction() == referenceNumber.getCodeCentreInstruction().getCode());
            assertTrue(referenceNumber.getNpa() == null);
            System.out.println(result.toString());
        }
    }

    /**
     * Test les cas 3
     * 
     * @param values
     * @param referenceNumber
     */
    private void testCas_3(Map<String, Result> values, APReferenceNumber referenceNumber) {
        for (String key : values.keySet()) {
            try {
                System.out.print(key + " : ");
                referenceNumber.setReferenceNumber(key);
            } catch (Exception e) {
                System.err.println("Exception for key : " + key + ". Result = " + values.get(key).toString());
                e.printStackTrace();
            }
            Result result = values.get(key);
            assertTrue(result.getCas() == referenceNumber.getCas());
            assertTrue(result.getCanton() == referenceNumber.getCodeCanton().getCodeCanton());
            assertTrue(result.getCentreInstruction() == null);
            assertTrue(referenceNumber.getNpa().equals(result.getNpa()));
            System.out.println(result.toString());
        }
    }

    private void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

}
