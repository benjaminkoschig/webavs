package ch.globaz.prestation.domaine;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test des différents codes prestations des rentes (n'inclue pas de test pour les codes prestations RFM et PC)
 */
public class CodePrestationTest {

    private static final CodePrestation[] codesPrestatationVieillesseOrdinaire = { CodePrestation.CODE_10,
            CodePrestation.CODE_12, CodePrestation.CODE_33, CodePrestation.CODE_34, CodePrestation.CODE_35,
            CodePrestation.CODE_36 };
    private static final CodePrestation[] codesPrestationAPI_AI = { CodePrestation.CODE_81, CodePrestation.CODE_82,
            CodePrestation.CODE_83, CodePrestation.CODE_84, CodePrestation.CODE_88, CodePrestation.CODE_91,
            CodePrestation.CODE_92, CodePrestation.CODE_93 };
    private static final CodePrestation[] codesPrestationAPI_AVS = { CodePrestation.CODE_85, CodePrestation.CODE_86,
            CodePrestation.CODE_87, CodePrestation.CODE_89, CodePrestation.CODE_95, CodePrestation.CODE_96,
            CodePrestation.CODE_97 };
    private static final CodePrestation[] codesPrestationInvaliditeExtraordinaire = { CodePrestation.CODE_70,
            CodePrestation.CODE_72, CodePrestation.CODE_73, CodePrestation.CODE_74, CodePrestation.CODE_75,
            CodePrestation.CODE_76 };
    private static final CodePrestation[] codesPrestationInvaliditeOrdinaire = { CodePrestation.CODE_50,
            CodePrestation.CODE_52, CodePrestation.CODE_53, CodePrestation.CODE_54, CodePrestation.CODE_55,
            CodePrestation.CODE_56 };
    private static final CodePrestation[] codesPrestationSurvivantExtraordinaire = { CodePrestation.CODE_23,
            CodePrestation.CODE_24, CodePrestation.CODE_25, CodePrestation.CODE_26 };
    private static final CodePrestation[] codesPrestationSurvivantOrdinaire = { CodePrestation.CODE_13,
            CodePrestation.CODE_14, CodePrestation.CODE_15, CodePrestation.CODE_16 };
    private static final CodePrestation[] codesPrestationVieillesseExtraordinaire = { CodePrestation.CODE_20,
            CodePrestation.CODE_22, CodePrestation.CODE_43, CodePrestation.CODE_44, CodePrestation.CODE_45,
            CodePrestation.CODE_46 };
    private static final CodePrestation[] codesPrestationPCStandard = { CodePrestation.CODE_110,
            CodePrestation.CODE_113, CodePrestation.CODE_150 };
    private static final CodePrestation[] codesPrestationPCAllocationNoel = { CodePrestation.CODE_118,
            CodePrestation.CODE_119, CodePrestation.CODE_158, CodePrestation.CODE_159 };
    private static final CodePrestation[] codesPrestationRFM = { CodePrestation.CODE_210, CodePrestation.CODE_213,
            CodePrestation.CODE_250 };

    private static void checkCodePrestationPasUneRenteAI(CodePrestation... codesPrestations) {
        for (CodePrestation unCodePrestation : codesPrestations) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente AI");
            Assert.assertFalse(message.toString(), unCodePrestation.isAI());
        }
    }

    private static void checkCodePrestationPasUneRenteAPI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente API");
            Assert.assertFalse(message.toString(), unCodePrestation.isAPI());
        }
    }

    private static void checkCodePrestationPasUneRenteAPIAI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente API AI");
            Assert.assertFalse(message.toString(), unCodePrestation.isAPIAI());
        }
    }

    private static void checkCodePrestationPasUneRenteAPIAVS(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente API AVS");
            Assert.assertFalse(message.toString(), unCodePrestation.isAPIAVS());
        }
    }

    private static void checkCodePrestationPasUneRenteExtraordinaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente extraordinaire");
            Assert.assertFalse(message.toString(), unCodePrestation.isRenteExtraordinaire());
        }
    }

    private static void checkCodePrestationPasUneRenteOrdinaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente ordinaire");
            Assert.assertFalse(message.toString(), unCodePrestation.isRenteOrdinaire());
        }
    }

    private static void checkCodePrestationPasUneRentePourConjoint(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente pour conjoint");
            Assert.assertFalse(message.toString(), unCodePrestation.isRenteComplementairePourConjoint());
        }
    }

    private static void checkCodePrestationPasUneRentePourEnfant(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente pour enfant");
            Assert.assertFalse(message.toString(), unCodePrestation.isRenteComplementairePourEnfant());
        }
    }

    private static void checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ")
                    .append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente complémentaire pour enfant liée à la rente de la mère");
            Assert.assertFalse(message.toString(),
                    unCodePrestation.isRentesComplementairePourEnfantsLieesRenteDeLaMere());
        }
    }

    private static void checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ")
                    .append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente complémentaire pour enfant liée à la rente du père");
            Assert.assertFalse(message.toString(), unCodePrestation.isRentesComplementairePourEnfantsLieesRenteDuPere());
        }
    }

    private static void checkCodePrestationPasUneRentePrincipale(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente principale");
            Assert.assertFalse(message.toString(), unCodePrestation.isRentePrincipale());
        }
    }

    private static void checkCodePrestationPasUneRenteSurvivant(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente survivant");
            Assert.assertFalse(message.toString(), unCodePrestation.isSurvivant());
        }
    }

    private static void checkCodePrestationPasUneRenteVieillesse(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de rente vieillesse");
            Assert.assertFalse(message.toString(), unCodePrestation.isVieillesse());
        }
    }

    private static void checkCodePrestationRenteAI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente AI");
            Assert.assertTrue(message.toString(), unCodePrestation.isAI());
        }
    }

    private static void checkCodePrestationRenteAPI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente API");
            Assert.assertTrue(message.toString(), unCodePrestation.isAPI());
        }
    }

    private static void checkCodePrestationRenteAPIAI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente API AI");
            Assert.assertTrue(message.toString(), unCodePrestation.isAPIAI());
        }
    }

    private static void checkCodePrestationRenteAPIAVS(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente API AVS");
            Assert.assertTrue(message.toString(), unCodePrestation.isAPIAVS());
        }
    }

    private static void checkCodePrestationRenteExtraordinaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente extraordinaire");
            Assert.assertTrue(message.toString(), unCodePrestation.isRenteExtraordinaire());
        }
    }

    private static void checkCodePrestationRenteOrdinaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente ordinaire");
            Assert.assertTrue(message.toString(), unCodePrestation.isRenteOrdinaire());
        }
    }

    private static void checkCodePrestationRentePourConjoint(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente pour conjoint");
            Assert.assertTrue(message.toString(), unCodePrestation.isRenteComplementairePourConjoint());
        }
    }

    private static void checkCodePrestationRentePourEnfant(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente pour enfant");
            Assert.assertTrue(message.toString(), unCodePrestation.isRenteComplementairePourEnfant());
        }
    }

    private static void checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente complémentaire pour enfant liée à la rente de la mère");
            Assert.assertTrue(message.toString(),
                    unCodePrestation.isRentesComplementairePourEnfantsLieesRenteDeLaMere());
        }
    }

    private static void checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente complémentaire pour enfant liée à la rente du père");
            Assert.assertTrue(message.toString(), unCodePrestation.isRentesComplementairePourEnfantsLieesRenteDuPere());
        }
    }

    private static void checkCodePrestationRentePrincipale(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente principale");
            Assert.assertTrue(message.toString(), unCodePrestation.isRentePrincipale());
        }
    }

    private static void checkCodePrestationRenteSurvivant(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente survivant");
            Assert.assertTrue(message.toString(), unCodePrestation.isSurvivant());
        }
    }

    private static void checkCodePrestationRenteVieillesse(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de rente vieillesse");
            Assert.assertTrue(message.toString(), unCodePrestation.isVieillesse());
        }
    }

    private static void checkCodePrestationPrestationComplementaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de PC");
            Assert.assertTrue(message.toString(), unCodePrestation.isPC());
        }
    }

    private static void checkCodePrestationPasUnePrestationComplementaire(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de PC");
            Assert.assertFalse(message.toString(), unCodePrestation.isPC());
        }
    }

    private static void checkCodePrestationPrestationComplementaireAllocatioNoel(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de PC Allocation de Noël");
            Assert.assertTrue(message.toString(), unCodePrestation.isPCAllocationNoel());
        }
    }

    private static void checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(
            CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de PC Allocation de Noël");
            Assert.assertFalse(message.toString(), unCodePrestation.isPCAllocationNoel());
        }
    }

    private static void checkCodePrestationRemboursementFraisMedicaux(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de RFM");
            Assert.assertTrue(message.toString(), unCodePrestation.isRFM());
        }
    }

    private static void checkCodePrestationPasRemboursementFraisMedicaux(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de remboursement de frais médicaux");
            Assert.assertFalse(message.toString(), unCodePrestation.isRFM());
        }
    }

    private static void checkCodePrestationPrestationComplementaireAUneRenteAI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de prestation complémentaire à une rente AI");
            Assert.assertTrue(message.toString(), unCodePrestation.isPrestationComplementaireAUneRenteAI());
        }
    }

    private static void checkCodePrestationPasPrestationComplementaireAUneRenteAI(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de prestation complémentaire à une rente AI");
            Assert.assertFalse(message.toString(), unCodePrestation.isPrestationComplementaireAUneRenteAI());
        }
    }

    private static void checkCodePrestationEstComplementaireAUneRenteAVS(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" est un code prestation de prestation complémentaire à une rente AVS");
            Assert.assertTrue(message.toString(), unCodePrestation.isPrestationComplementaireAUneRenteAVS());
        }
    }

    private static void checkCodePrestationNEstPasComplementaireAUneRenteAVS(CodePrestation... codesPrestation) {
        for (CodePrestation unCodePrestation : codesPrestation) {
            StringBuilder message = new StringBuilder();
            message.append("Erreur, le code prestation ").append(unCodePrestation.getCodePrestation())
                    .append(" n'est pas un code prestation de prestation complémentaire à une rente AVS");
            Assert.assertFalse(message.toString(), unCodePrestation.isPrestationComplementaireAUneRenteAVS());
        }
    }

    @Test
    public void testCodePrestationAPI() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteAPI(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationRenteAPI(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationRenteAPI(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteAPI(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteAPI(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteAPI(codesPrestationRFM);
    }

    @Test
    public void testCodePrestationAPIAI() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteAPIAI(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteAPIAI(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationRenteAPIAI(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteAPIAI(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteAPIAI(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteAPIAI(codesPrestationRFM);
    }

    @Test
    public void testCodePrestationAPIAVSAVS() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationRenteAPIAVS(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteAPIAVS(codesPrestationRFM);
    }

    @Test
    public void testCodePrestationRenteAi() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteAI(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteAI(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteAI(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteAI(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationRenteAI(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationRenteAI(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteAI(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteAI(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteAI(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteAI(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteAI(codesPrestationRFM);
    }

    @Test
    public void testCodePrestationRenteComplementairePourConjoint() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRentePourConjoint(CodePrestation.CODE_10, CodePrestation.CODE_34,
                CodePrestation.CODE_35, CodePrestation.CODE_36);
        checkCodePrestationRentePourConjoint(CodePrestation.CODE_12, CodePrestation.CODE_33);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRentePourConjoint(CodePrestation.CODE_20, CodePrestation.CODE_44,
                CodePrestation.CODE_45, CodePrestation.CODE_46);
        checkCodePrestationRentePourConjoint(CodePrestation.CODE_22, CodePrestation.CODE_43);

        // survivant ordinaire
        checkCodePrestationPasUneRentePourConjoint(codesPrestationSurvivantExtraordinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRentePourConjoint(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRentePourConjoint(CodePrestation.CODE_50, CodePrestation.CODE_54,
                CodePrestation.CODE_55, CodePrestation.CODE_56);
        checkCodePrestationRentePourConjoint(CodePrestation.CODE_52, CodePrestation.CODE_53);

        // AI extraordinaire
        checkCodePrestationPasUneRentePourConjoint(CodePrestation.CODE_70, CodePrestation.CODE_74,
                CodePrestation.CODE_75, CodePrestation.CODE_76);
        checkCodePrestationRentePourConjoint(CodePrestation.CODE_72, CodePrestation.CODE_73);

        // API-AVS
        checkCodePrestationPasUneRentePourConjoint(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRentePourConjoint(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRentePourConjoint(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRentePourConjoint(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRentePourConjoint(codesPrestationRFM);
    }

    @Test
    public void testCodePrestationRenteComplementairePourEnfant() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_10, CodePrestation.CODE_12, CodePrestation.CODE_33);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_34, CodePrestation.CODE_35, CodePrestation.CODE_36);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_20, CodePrestation.CODE_22, CodePrestation.CODE_43);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_44, CodePrestation.CODE_45, CodePrestation.CODE_46);

        // survivant ordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_13);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_14, CodePrestation.CODE_15, CodePrestation.CODE_16);

        // survivant extraordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_23);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_24, CodePrestation.CODE_25, CodePrestation.CODE_26);

        // AI ordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_50, CodePrestation.CODE_52, CodePrestation.CODE_53);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_54, CodePrestation.CODE_55, CodePrestation.CODE_56);

        // AI extraordinaire
        checkCodePrestationPasUneRentePourEnfant(CodePrestation.CODE_70, CodePrestation.CODE_72, CodePrestation.CODE_73);
        checkCodePrestationRentePourEnfant(CodePrestation.CODE_74, CodePrestation.CODE_75, CodePrestation.CODE_76);

        // API-AVS
        checkCodePrestationPasUneRentePourEnfant(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRentePourEnfant(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRentePourEnfant(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRentePourEnfant(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRentePourEnfant(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRenteExtraordinaire() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationRenteExtraordinaire(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationRenteExtraordinaire(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationRenteExtraordinaire(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteExtraordinaire(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRenteOrdinaire() {
        // vieillesse ordinaire
        checkCodePrestationRenteOrdinaire(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationRenteOrdinaire(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationRenteOrdinaire(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteOrdinaire(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRentePourEnfantLieeRenteDeLaMere() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_10, CodePrestation.CODE_12,
                CodePrestation.CODE_33, CodePrestation.CODE_34);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_35, CodePrestation.CODE_36);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_20, CodePrestation.CODE_22,
                CodePrestation.CODE_43, CodePrestation.CODE_44);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_45, CodePrestation.CODE_46);

        // survivant ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_13, CodePrestation.CODE_14);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_15, CodePrestation.CODE_16);

        // survivant extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_23, CodePrestation.CODE_24);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_25, CodePrestation.CODE_26);

        // AI ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_50, CodePrestation.CODE_52,
                CodePrestation.CODE_53, CodePrestation.CODE_54);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_55, CodePrestation.CODE_56);

        // AI extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_70, CodePrestation.CODE_72,
                CodePrestation.CODE_73, CodePrestation.CODE_74);
        checkCodePrestationRentePourEnfantLieeRenteDeLaMere(CodePrestation.CODE_75, CodePrestation.CODE_76);

        // API-AVS
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRentePourEnfantLieeRenteDeLaMere(codesPrestationAPI_AI);
    }

    @Test
    public void testIsCodePrestationRentePourEnfantLieeRenteDuPere() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_10, CodePrestation.CODE_12,
                CodePrestation.CODE_33, CodePrestation.CODE_35);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_34, CodePrestation.CODE_36);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_20, CodePrestation.CODE_22,
                CodePrestation.CODE_43, CodePrestation.CODE_45);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_44, CodePrestation.CODE_46);

        // survivant ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_13, CodePrestation.CODE_15);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_14, CodePrestation.CODE_16);

        // survivant extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_23, CodePrestation.CODE_25);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_24, CodePrestation.CODE_26);

        // AI ordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_50, CodePrestation.CODE_52,
                CodePrestation.CODE_53, CodePrestation.CODE_55);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_54, CodePrestation.CODE_56);

        // AI extraordinaire
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_70, CodePrestation.CODE_72,
                CodePrestation.CODE_73, CodePrestation.CODE_75);
        checkCodePrestationRentePourEnfantLieeRenteDuPere(CodePrestation.CODE_74, CodePrestation.CODE_76);

        // API-AVS
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRentePourEnfantLieeRenteDuPere(codesPrestationAPI_AI);
    }

    @Test
    public void testIsCodePrestationRentePrincipale() {
        // vieillesse ordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_10);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_12, CodePrestation.CODE_33,
                CodePrestation.CODE_34, CodePrestation.CODE_35, CodePrestation.CODE_36);

        // vieillesse extraordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_20);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_22, CodePrestation.CODE_43,
                CodePrestation.CODE_44, CodePrestation.CODE_45, CodePrestation.CODE_46);

        // survivant ordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_13);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_14, CodePrestation.CODE_15, CodePrestation.CODE_16);

        // survivant extraordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_23);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_24, CodePrestation.CODE_25, CodePrestation.CODE_26);

        // AI ordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_50);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_52, CodePrestation.CODE_53,
                CodePrestation.CODE_54, CodePrestation.CODE_55, CodePrestation.CODE_56);

        // AI extraordinaire
        checkCodePrestationRentePrincipale(CodePrestation.CODE_70);
        checkCodePrestationPasUneRentePrincipale(CodePrestation.CODE_72, CodePrestation.CODE_73,
                CodePrestation.CODE_74, CodePrestation.CODE_75, CodePrestation.CODE_76);

        // API-AVS
        checkCodePrestationPasUneRentePrincipale(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRentePrincipale(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRentePrincipale(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRentePrincipale(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRentePrincipale(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRenteSurvivant() {
        // vieillesse ordinaire
        checkCodePrestationPasUneRenteSurvivant(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUneRenteSurvivant(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationRenteSurvivant(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationRenteSurvivant(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteSurvivant(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteSurvivant(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteSurvivant(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteSurvivant(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteSurvivant(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteSurvivant(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteSurvivant(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRenteVieillesse() {
        // vieillesse ordinaire
        checkCodePrestationRenteVieillesse(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationRenteVieillesse(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUneRenteVieillesse(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUneRenteVieillesse(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUneRenteVieillesse(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUneRenteVieillesse(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUneRenteVieillesse(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUneRenteVieillesse(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUneRenteVieillesse(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasUneRenteVieillesse(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUneRenteVieillesse(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationPC() {
        // vieillesse ordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPrestationComplementaire(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPrestationComplementaire(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUnePrestationComplementaire(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationPCAllocationNoel() {
        // vieillesse ordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPrestationComplementaireAllocatioNoel(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationPasUnePrestationComplementaireAllocatioNoel(codesPrestationRFM);
    }

    @Test
    public void testIsCodePrestationRFM() {
        // vieillesse ordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationAPI_AI);

        // PC standards
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationPCStandard);

        // PC Allocation de Noël
        checkCodePrestationPasRemboursementFraisMedicaux(codesPrestationPCAllocationNoel);

        // RFM
        checkCodePrestationRemboursementFraisMedicaux(codesPrestationRFM);
    }

    @Test
    public void testIsPrestationComplementAUneRenteAI() {
        // vieillesse ordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(codesPrestationAPI_AI);

        // PC standards AVS
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(CodePrestation.CODE_110, CodePrestation.CODE_113);

        // PC standards AI
        checkCodePrestationPrestationComplementaireAUneRenteAI(CodePrestation.CODE_150);

        // PC Allocation de Noël AVS
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(CodePrestation.CODE_118, CodePrestation.CODE_119);

        // PC Allocation de Noël AI
        checkCodePrestationPrestationComplementaireAUneRenteAI(CodePrestation.CODE_158);

        // RFM AVS
        checkCodePrestationPasPrestationComplementaireAUneRenteAI(CodePrestation.CODE_210, CodePrestation.CODE_213);

        // RFM AI
        checkCodePrestationPrestationComplementaireAUneRenteAI(CodePrestation.CODE_250);
    }

    @Test
    public void testIsPrestationComplementAUneRenteAVS() {
        // vieillesse ordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestatationVieillesseOrdinaire);

        // vieillesse extraordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationVieillesseExtraordinaire);

        // survivant ordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationSurvivantOrdinaire);

        // survivant extraordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationSurvivantExtraordinaire);

        // AI ordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationInvaliditeOrdinaire);

        // AI extraordinaire
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationInvaliditeExtraordinaire);

        // API-AVS
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationAPI_AVS);

        // API-AI
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(codesPrestationAPI_AI);

        // PC standards AVS
        checkCodePrestationEstComplementaireAUneRenteAVS(CodePrestation.CODE_110, CodePrestation.CODE_113);

        // PC standards AI
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(CodePrestation.CODE_150);

        // PC Allocation de Noël AVS
        checkCodePrestationEstComplementaireAUneRenteAVS(CodePrestation.CODE_118, CodePrestation.CODE_119);

        // PC Allocation de Noël AI
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(CodePrestation.CODE_158, CodePrestation.CODE_159);

        // RFM AVS
        checkCodePrestationEstComplementaireAUneRenteAVS(CodePrestation.CODE_210, CodePrestation.CODE_213);

        // RFM AI
        checkCodePrestationNEstPasComplementaireAUneRenteAVS(CodePrestation.CODE_250);
    }

    @Test
    public void testValeursInvalides() {
        // valeurs invalides
        try {
            CodePrestation.getCodePrestation(0);
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            CodePrestation.getCodePrestation(-1);
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            CodePrestation.getCodePrestation(1);
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            CodePrestation.getCodePrestation(-10);
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }

    @Test
    public void testEstDeLaMemeFamilleDePrestationQue() throws Exception {

        /*
         * On vérifie ici que :
         * - (1) les deux codes prestations sont du même groupe de prestation : API ou non-API
         * - (2) dans le cas des non-API et que c'est une rente complémentaire pour enfant : que les deux rentes soient
         * issues du même donneur de droit (père ou mère)
         * - (3) que les rentes PC, RFM et AVS/AI ne sont pas considérées de la même famille
         */

        Assert.assertFalse("Une rente AI et API ne doivent pas être considérées de la même famille",
                CodePrestation.CODE_55.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_81));

        Assert.assertTrue("Deux rentes API doivent être considérées de la même famille",
                CodePrestation.CODE_81.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_82));

        Assert.assertTrue("Une rente AI et une rente AVS doivent être considérées de la même famille",
                CodePrestation.CODE_55.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_10));

        Assert.assertFalse(
                "Cas particulier : deux rentes complémentaires ne découlant pas du même droit (père ou mère) ne doivent pas être considérées comme de la même famille",
                CodePrestation.CODE_54.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_55));

        Assert.assertFalse("Une PC et une rente AVS ne doivent pas être considérées de la même famille",
                CodePrestation.CODE_10.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_110));

        Assert.assertTrue("deux PC doivent être considérées de la même famille",
                CodePrestation.CODE_150.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_110));

        Assert.assertFalse("Une PC et un RFM ne doivent pas être considérées de la même famille",
                CodePrestation.CODE_110.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_210));

        Assert.assertTrue("deux RFM doivent être considérées de la même famille",
                CodePrestation.CODE_210.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_250));

        Assert.assertFalse("Une rente AVS et un RFM ne doivent pas être considérées de la même famille",
                CodePrestation.CODE_10.estDeLaMemeFamilleDePrestationQue(CodePrestation.CODE_210));

    }
}
