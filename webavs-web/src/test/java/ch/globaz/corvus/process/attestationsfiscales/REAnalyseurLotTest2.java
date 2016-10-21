package ch.globaz.corvus.process.attestationsfiscales;

import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @formatter:off
/**
 * Test pour les analyseurs de lots dans la génération des attestations fiscales
 * 
 *  N°  |Type rente | seulement avec        |avec retro     |retro dans     |
 *      |           |décision dans l'année  |               |année fiscale  |
 *  1   | AI - AVS  |                       |               |               |
 *  2   |   SUR     |                       |               |               |
 *  3   | AI - AVS  |       X               |               |               |
 *  4   |   SUR     |       X               |               |               |
 *  5   | AI - AVS  |       X               |       X       |               |
 *  6   | AI - AVS  |       X               |       X       |       X       |
 *  7   |   SUR     |       X               |       X       |               |
 *  8   |   SUR     |       X               |       X       |       X       |
 * 
 * 
 * 
 * 
 * @author LGA
 */
// @formatter:on
public class REAnalyseurLotTest2 {

    private static final String ANNEE_FISCALE = "2011";

    private static final Logger logger = LoggerFactory.getLogger(REAnalyseurLotTest2.class);

    private REAbstractAnalyseurLot analyseurLot1;
    private REAbstractAnalyseurLot analyseurLot2;
    private REAbstractAnalyseurLot analyseurLot3;
    private REAbstractAnalyseurLot analyseurLot4;
    private REAbstractAnalyseurLot analyseurLot5;
    private REAbstractAnalyseurLot analyseurLot6;
    private REAbstractAnalyseurLot analyseurLot7;
    private REAbstractAnalyseurLot analyseurLot8;

    private Map<Integer, REAbstractAnalyseurLot> mapAnalyseur;

    private List<String> codePrestAVS_AI;
    private List<String> codePrestSUR;
    private List<String> codePrestAPI;

    private REFamillePourAttestationsFiscales famille;
    private RERentePourAttestationsFiscales rente1;
    private RETiersPourAttestationsFiscales tiers1;

    @Before
    public void setUp() {
        analyseurLot1 = new REAnalyseurLot1(ANNEE_FISCALE);
        analyseurLot2 = new REAnalyseurLot2(ANNEE_FISCALE);
        analyseurLot3 = new REAnalyseurLot3(ANNEE_FISCALE);
        analyseurLot4 = new REAnalyseurLot4(ANNEE_FISCALE);
        analyseurLot5 = new REAnalyseurLot5(ANNEE_FISCALE);
        analyseurLot6 = new REAnalyseurLot6(ANNEE_FISCALE);
        analyseurLot7 = new REAnalyseurLot7(ANNEE_FISCALE);
        analyseurLot8 = new REAnalyseurLot8(ANNEE_FISCALE);

        //
        mapAnalyseur = new HashMap<Integer, REAbstractAnalyseurLot>();
        mapAnalyseur.put(1, analyseurLot1);
        mapAnalyseur.put(2, analyseurLot2);
        mapAnalyseur.put(3, analyseurLot3);
        mapAnalyseur.put(4, analyseurLot4);
        mapAnalyseur.put(5, analyseurLot5);
        mapAnalyseur.put(6, analyseurLot6);
        mapAnalyseur.put(7, analyseurLot7);
        mapAnalyseur.put(8, analyseurLot8);

        // Code prestation des rentes principales AVS et AI
        codePrestAVS_AI = new ArrayList<String>();
        codePrestAVS_AI.add("10");
        codePrestAVS_AI.add("12");
        codePrestAVS_AI.add("20");
        codePrestAVS_AI.add("22");
        codePrestAVS_AI.add("50");
        codePrestAVS_AI.add("52");
        codePrestAVS_AI.add("70");
        codePrestAVS_AI.add("72");

        // Code prestation des rentes principales SUR
        codePrestSUR = new ArrayList<String>();
        codePrestSUR.add("13");
        codePrestSUR.add("23");

        // Code prestation des rentes principales SUR
        codePrestAPI = new ArrayList<String>();
        for (IPRCodePrestationEnum e : PRCodePrestationAPI.values()) {
            codePrestAPI.add(e.getCodePrestationAsString());
        }

        famille = new REFamillePourAttestationsFiscales();

        tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        tiers1.setPrenom("Daniel");
        tiers1.setNom("Hubler");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);
    }

    /**
     * Type de rente : AVS/AI</br>
     * Avec décision dans l'année : NON</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur1() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);

        int numeroAnalyseur = 1;
        REAbstractAnalyseurLot analyseur = analyseurLot1;

        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2010");

        // Rente AVS/AI - décision pas dans année, pas de rétro -> OK
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        // Rente API - décision pas dans année, pas de rétro -> KO
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision pas dans année, pas de rétro -> KO
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, pas de rétro -> KO (décision dans l'année)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.01.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, rétro sur une année -> KO (décision dans l'année, rétro)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, pas de rétro -> KO (décision dans l'année)
        rente1.setDateDebutDroit("03.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // rente1.setDateDebutDroit("02.2010");
        // rente1.setDateDecision("11.02.2010");
        // for (String e : codePrestAVS_AI) {
        // rente1.setCodePrestation(e);
        // Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
        // analyseur.isFamilleDansLot(famille));
        // }
    }

    /**
     * Type de rente : SUR</br>
     * Avec décision dans l'année : NON</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur2() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 2;
        REAbstractAnalyseurLot analyseur = analyseurLot2;

        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2010");

        // Rente SUR - décision pas dans année, pas de rétro -> OK
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        // Rente API - décision pas dans année, pas de rétro -> KO
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision pas dans année, pas de rétro -> KO
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, pas de rétro -> KO (décision dans l'année)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.01.2011");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, rétro sur une année -> KO (décision dans l'année, rétro)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, pas de rétro -> KO (décision dans l'année)
        rente1.setDateDebutDroit("03.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }
        // // Rente SUR - décision pas dans année, rétro sur une année -> KO (rétro)
        // rente1.setDateDebutDroit("02.2010");
        // rente1.setDateDecision("11.02.2010");
        // for (String e : codePrestSUR) {
        // rente1.setCodePrestation(e);
        // Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
        // analyseur.isFamilleDansLot(famille));
        // }

    }

    /**
     * Type de rente : AVS/AI</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur3() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 3;
        REAbstractAnalyseurLot analyseur = analyseurLot3;

        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("10.01.2011");

        // Rente AVS - décision dans année, pas de rétro -> OK
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        // Rente API - décision dans année, pas de rétro -> KO
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, pas de rétro -> KO
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision pas dans année, pas de rétro -> KO (décision pas dans année)
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("11.12.2010");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, AVEC de rétro -> KO (rétro)
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }
    }

    /**
     * Type de rente : SUR</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur4() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 4;
        REAbstractAnalyseurLot analyseur = analyseurLot4;

        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("10.01.2011");

        // Rente AVS - décision dans année, pas de rétro -> OK
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        // Rente API - décision dans année, pas de rétro -> KO (API)
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, pas de rétro -> KO (AVS)
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision pas dans année, pas de rétro -> KO (décision pas dans année)
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("11.12.2010");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, AVEC de rétro -> KO (rétro)
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, AVEC de rétro -> KO (rétro)
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }
    }

    /**
     * Type de rente : AVS/AI</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : OUI</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur5() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 5;
        REAbstractAnalyseurLot analyseur = analyseurLot5;

        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("31.02.2011");

        // Rente AVS - décision dans année, avec rétro -> OK
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                message = "Analyseur numéro [" + numero + "] avec code prestation [" + cp + "]";
                Assert.assertFalse(message, mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        rente2.setDateDebutDroit("12.2011");
        rente2.setDateDecision("15.12.2011");
        rente2.setCodePrestation("10");
        tiers1.getMapRentes().put("2", rente2);

        // Rente AVS/AI - décision dans année, avec rétro MAIS décision en décembre -> KO (décision en décembre)
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertFalse(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }
        tiers1.getMapRentes().remove("2");

        // Rente API - décision dans année, pas de rétro -> KO (API)
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, pas de rétro -> KO (SUR)
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision pas dans année, avec rétro -> KO (décision pas dans année)
        rente1.setDateDebutDroit("02.2010");
        rente1.setDateDecision("11.02.2010");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

    }

    /**
     * Type de rente : AVS/AI</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : OUI</br>
     * 
     */
    @Test
    public void testAnalyseur6() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(7);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 6;
        REAbstractAnalyseurLot analyseur = analyseurLot6;

        // TODO cas à éclaircir avec RJE
        // rente1.setDateDebutDroit("02.2010");
        // rente1.setDateDecision("31.02.2010");

        rente1.setDateDebutDroit("02.2010");
        rente1.setDateDecision("31.02.2011");

        // Rente AVS - décision pas dans année, avec rétro -> OK
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                message = "Analyseur numéro [" + numero + "] avec code prestation [" + cp + "]";
                Assert.assertFalse(message, mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        rente2.setDateDebutDroit("12.2011");
        rente2.setDateDecision("15.12.2011");
        rente2.setCodePrestation("10");
        tiers1.getMapRentes().put("2", rente2);

        // Rente AVS/AI - décision dans année, avec rétro MAIS décision en décembre -> KO (décision en décembre)
        for (String cp : codePrestAVS_AI) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertFalse(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }
        tiers1.getMapRentes().remove("2");

        // Rente API - décision pas dans année, pas de rétro -> KO (API)
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision pas dans année, pas de rétro -> KO (SUR)
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, avec rétro -> KO (décision dans année)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

    }

    // --------------------------------------------------------------------------------
    /**
     * Type de rente : SUR</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : OUI</br>
     * Avec rétro sur plusieurs années : NON</br>
     * 
     */
    @Test
    public void testAnalyseur7() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(8);
        int numeroAnalyseur = 7;
        REAbstractAnalyseurLot analyseur = analyseurLot7;

        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("31.02.2011");

        // Rente SUR - décision dans année, avec rétro -> OK
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                message = "Analyseur numéro [" + numero + "] avec code prestation [" + cp + "]";
                Assert.assertFalse(message, mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        rente2.setDateDebutDroit("12.2011");
        rente2.setDateDecision("15.12.2011");
        rente2.setCodePrestation("13");
        tiers1.getMapRentes().put("2", rente2);

        // Rente AVS/AI - décision dans année, avec rétro MAIS décision en décembre -> KO (décision en décembre)
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertFalse(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }
        tiers1.getMapRentes().remove("2");

        // Rente API - décision dans année, pas de rétro -> KO (API)
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision dans année, pas de rétro -> KO (AVS/AI)
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision pas dans année, avec rétro -> KO (décision pas dans année)
        rente1.setDateDebutDroit("02.2010");
        rente1.setDateDecision("11.02.2010");
        for (String e : codePrestSUR) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

    }

    /**
     * Type de rente : SUR</br>
     * Avec décision dans l'année : OUI</br>
     * Avec rétro dans année fiscale : NON</br>
     * Avec rétro sur plusieurs années : OUI</br>
     * 
     */
    @Test
    public void testAnalyseur8() {
        List<Integer> analyseurEnErreur = new ArrayList<Integer>();
        analyseurEnErreur.add(1);
        analyseurEnErreur.add(2);
        analyseurEnErreur.add(3);
        analyseurEnErreur.add(4);
        analyseurEnErreur.add(5);
        analyseurEnErreur.add(6);
        analyseurEnErreur.add(7);
        int numeroAnalyseur = 8;
        REAbstractAnalyseurLot analyseur = analyseurLot8;

        // TODO cas à éclaircir avec RJE
        // rente1.setDateDebutDroit("02.2010");
        // rente1.setDateDecision("31.02.2010");

        rente1.setDateDebutDroit("02.2010");
        rente1.setDateDecision("31.02.2011");

        // Rente AVS - décision pas dans année, avec rétro -> OK
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertTrue(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                message = "Analyseur numéro [" + numero + "] avec code prestation [" + cp + "]";
                Assert.assertFalse(message, mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }

        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        rente2.setDateDebutDroit("12.2011");
        rente2.setDateDecision("15.12.2011");
        rente2.setCodePrestation("13");
        tiers1.getMapRentes().put("2", rente2);

        // Rente AVS/AI - décision dans année, avec rétro MAIS décision en décembre -> KO (décision en décembre)
        for (String cp : codePrestSUR) {
            logger.debug("Analyseur " + numeroAnalyseur + " : test du code prestation numéro [" + cp + "]");
            rente1.setCodePrestation(cp);
            String message = "Analyseur numéro [" + numeroAnalyseur + "] en erreur avec code prestation [" + cp + "]";
            Assert.assertFalse(message, analyseur.isFamilleDansLot(famille));
            for (int numero : analyseurEnErreur) {
                Assert.assertFalse(mapAnalyseur.get(numero).isFamilleDansLot(famille));
            }
        }
        tiers1.getMapRentes().remove("2");

        // Rente API - décision pas dans année, pas de rétro -> KO (API)
        for (String e : codePrestAPI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente SUR - décision pas dans année, pas de rétro -> KO (SUR)
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

        // Rente AVS/AI - décision dans année, avec rétro -> KO (décision dans année)
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("11.02.2011");
        for (String e : codePrestAVS_AI) {
            rente1.setCodePrestation(e);
            Assert.assertFalse("Analyseur numéro [" + numeroAnalyseur + "] en erreur",
                    analyseur.isFamilleDansLot(famille));
        }

    }

}
