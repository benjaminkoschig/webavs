package ch.globaz.corvus.process.attestationsfiscales;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

public class REAbstractAnalyseurLotTest {

    @Test
    public void test() {
        REAbstractAnalyseurLot analyseur = new REAbstractAnalyseurLot1a4("2011", true, DomaineCodePrestation.VIEILLESSE) {

            @Override
            public int getNumeroAnalyseur() {
                return 1;
            }

            @Override
            public boolean controllerDateDecisionEtTypeDeRente(REFamillePourAttestationsFiscales famille) {
                return super.controllerDateDecisionEtTypeDeRente(famille);
            }

        };

        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        tiers1.setPrenom("Daniel");
        tiers1.setNom("Hubler");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        rente1.setCodePrestation("10");
        rente1.setDateDebutDroit("01.2011");

        rente1.setDateDecision("31.12.2011");
        // KO car aucune rente
        assertFalse(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        // Ajout de la rente
        tiers1.getMapRentes().put("1", rente1);
        assertTrue(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        // KO car la date de décision n'est pas dans l'année
        rente1.setDateDecision("31.12.2010");
        assertFalse(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        // KO car le genre de rente n'est pas bon
        rente1.setCodePrestation("50");
        assertFalse(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        // Plusieurs rentes
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        rente2.setCodePrestation("81");
        rente2.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2011");
        tiers1.getMapRentes().put("2", rente2);

        // KO car toujours pas le bon genre de rente
        assertFalse(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        RERentePourAttestationsFiscales rente3 = new RERentePourAttestationsFiscales();
        rente3.setIdRenteAccordee("3");
        rente3.setCodePrestation("10");
        rente3.setDateDebutDroit("01.2010");
        rente3.setDateDecision("31.12.2010");
        tiers1.getMapRentes().put("3", rente3);

        // KO car le bon genre de rente n'est pas dans l'année
        // FIXME
        // assertFalse(analyseur.controllerDateDecisionEtTypeDeRente(famille));

        RERentePourAttestationsFiscales rente4 = new RERentePourAttestationsFiscales();
        rente4.setIdRenteAccordee("4");
        rente4.setCodePrestation("10");
        rente4.setDateDebutDroit("08.2011");
        rente4.setDateDecision("11.02.2011");
        tiers1.getMapRentes().put("4", rente4);

        // KO car le bon genre de rente n'est pas dans l'année
        assertTrue(analyseur.controllerDateDecisionEtTypeDeRente(famille));

    }
}
