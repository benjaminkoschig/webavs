package ch.globaz.cygnus.businessimpl.services;

import static org.junit.Assert.*;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.Regimes;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RegimeLoaderImplTest {
    RegimeLoaderImpl rgl = null;
    List<RFPrestationAccordeeJointREPrestationAccordee> listRegimes = null;
    RFPrestationAccordeeJointREPrestationAccordee prestAcc1 = null;
    RFPrestationAccordeeJointREPrestationAccordee prestAcc2 = null;
    RFPrestationAccordeeJointREPrestationAccordee prestAcc3 = null;
    DonneeFinanciere dfReq = null;
    DonneeFinanciere dfConj = null;
    DonneeFinanciere dfEnf = null;
    Regime regime01 = null;
    Regime regime02 = null;
    Regime regime03 = null;
    MembresFamilles membresFamille1 = null;
    MembresFamilles membresFamille2 = null;
    MembresFamilles membresFamille3 = null;
    Map<String, MembresFamilles> mapMembresFamilles = null;
    Regimes regimesByIdTiers = null;
    Map<String, Map<String, Regime>> mapFinaleToTest = null;
    Regimes regimesForReq1 = null;
    Regimes regimesForReq2 = null;
    Regimes regimesForReq3 = null;
    MembreFamille mf1 = null;
    MembreFamille mf2 = null;
    MembreFamille mf3 = null;
    MembreFamille mf10 = null;
    MembreFamille mf20 = null;
    MembreFamille mf30 = null;
    PersonneAVS p1 = null;
    PersonneAVS p2 = null;
    PersonneAVS p3 = null;
    PersonneAVS p10 = null;
    PersonneAVS p20 = null;
    PersonneAVS p30 = null;

    public RegimeLoaderImplTest() {

        rgl = new RegimeLoaderImpl();

        dfReq = new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, new Date(), null, "1", "4");
        dfConj = new DonneeFinanciereHeader(RoleMembreFamille.CONJOINT, new Date(), null, "2", "4");
        dfEnf = new DonneeFinanciereHeader(RoleMembreFamille.ENFANT, new Date(), null, "3", "4");

        regime01 = new Regime(new Montant(100), IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE, dfReq);
        regime02 = new Regime(new Montant(250), IRFCodeTypesDeSoins.SOUS_TYPE_2_2_REGIME_DIABETIQUE, dfConj);
        regime03 = new Regime(new Montant(250), IRFCodeTypesDeSoins.SOUS_TYPE_2_2_REGIME_DIABETIQUE, dfEnf);

        prestAcc1 = new RFPrestationAccordeeJointREPrestationAccordee();
        prestAcc1.setIdTiers("1");
        prestAcc1.setCs_source(IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE);
        prestAcc1.setDateDebutDroit("20140101");
        prestAcc1.setDateFinDroit("20141231");
        prestAcc1.setMontantPrestation("100");

        prestAcc2 = new RFPrestationAccordeeJointREPrestationAccordee();
        prestAcc2.setIdTiers("2");
        prestAcc2.setCs_source(IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE);
        prestAcc2.setDateDebutDroit("20140101");
        prestAcc2.setDateFinDroit("20141231");
        prestAcc2.setMontantPrestation("100");

        prestAcc3 = new RFPrestationAccordeeJointREPrestationAccordee();
        prestAcc3.setIdTiers("3");
        prestAcc3.setCs_source(IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE);
        prestAcc3.setDateDebutDroit("20140101");
        prestAcc3.setDateFinDroit("20141231");
        prestAcc3.setMontantPrestation("100");

        listRegimes = new ArrayList<RFPrestationAccordeeJointREPrestationAccordee>();
        listRegimes.add(prestAcc1);
        listRegimes.add(prestAcc2);
        listRegimes.add(prestAcc3);

        p1 = new PersonneAVS();
        p1.setId(1L);

        p10 = new PersonneAVS();
        p10.setId(10L);

        p2 = new PersonneAVS();
        p2.setId(2L);

        p20 = new PersonneAVS();
        p20.setId(20L);

        p3 = new PersonneAVS();
        p3.setId(3L);

        p30 = new PersonneAVS();
        p30.setId(30L);

        mf1 = new MembreFamille();
        mf1.setPersonne(p1);
        mf1.setRoleMembreFamille(RoleMembreFamille.REQUERANT);
        mf2 = new MembreFamille();
        mf2.setPersonne(p2);
        mf2.setRoleMembreFamille(RoleMembreFamille.CONJOINT);
        mf3 = new MembreFamille();
        mf3.setPersonne(p3);
        mf3.setRoleMembreFamille(RoleMembreFamille.REQUERANT);

        mf10 = new MembreFamille();
        mf10.setPersonne(p10);
        mf10.setRoleMembreFamille(RoleMembreFamille.CONJOINT);
        mf20 = new MembreFamille();
        mf20.setPersonne(p20);
        mf20.setRoleMembreFamille(RoleMembreFamille.REQUERANT);
        mf30 = new MembreFamille();
        mf30.setPersonne(p30);
        mf30.setRoleMembreFamille(RoleMembreFamille.ENFANT);

        membresFamille1 = new MembresFamilles();
        membresFamille1.add(mf1);
        membresFamille1.add(mf10);

        membresFamille2 = new MembresFamilles();
        membresFamille2.add(mf2);
        membresFamille2.add(mf20);

        membresFamille3 = new MembresFamilles();
        membresFamille3.add(mf3);
        membresFamille3.add(mf30);

        mapMembresFamilles = new HashMap<String, MembresFamilles>();
        mapMembresFamilles.put("1", membresFamille1);
        mapMembresFamilles.put("2", membresFamille2);
        mapMembresFamilles.put("3", membresFamille3);

        regimesByIdTiers = new Regimes();
        regimesByIdTiers.put("1", regime01);
        regimesByIdTiers.put("2", regime02);
        regimesByIdTiers.put("3", regime01);
        regimesByIdTiers.put("10", regime02);
        regimesByIdTiers.put("20", regime01);

    }

    @Test
    public void testGroupRegimeByReq() throws Exception {
        Map<String, Regimes> mapFinale = rgl.groupRegimeByIdDroit(regimesByIdTiers, mapMembresFamilles);

        assertEquals(Montant.newAnnuel(1200), mapFinale.get("1").getDonneesForRequerant().sumRevenuAnnuel());
        assertEquals(Montant.newAnnuel(3000), mapFinale.get("1").getDonneesForConjointEnfant().sumRevenuAnnuel());
        assertEquals(Montant.newAnnuel(1200), mapFinale.get("2").getDonneesForRequerant().sumRevenuAnnuel());
        assertEquals(Montant.newAnnuel(3000), mapFinale.get("2").getDonneesForConjointEnfant().sumRevenuAnnuel());
        assertEquals(Montant.newAnnuel(1200), mapFinale.get("3").getDonneesForRequerant().sumRevenuAnnuel());
        assertEquals(Montant.newAnnuel(0), mapFinale.get("3").getDonneesForConjointEnfant().sumRevenuAnnuel());
    }

    @Test
    public void testCreateRFRegimePrestationAccordee() throws Exception {

        assertEquals(regime01.getMontant(), rgl.createRFRegimePrestationAccordee(prestAcc1, dfReq).getMontant());
        assertEquals(regime01.getDebut(), rgl.createRFRegimePrestationAccordee(prestAcc1, dfConj).getDebut());
        assertEquals(regime01.getFin(), rgl.createRFRegimePrestationAccordee(prestAcc1, dfEnf).getFin());
        assertEquals(regime01.getSousTypeRegime(), rgl.createRFRegimePrestationAccordee(prestAcc1, dfReq)
                .getSousTypeRegime());
    }

    @Ignore
    @Test
    public void testGroupRegimeByIdTiers() throws Exception {
        Map<String, Regime> map1 = new HashMap<String, Regime>();
        map1.put("1", rgl.createRFRegimePrestationAccordee(prestAcc1, dfReq));
        map1.put("2", rgl.createRFRegimePrestationAccordee(prestAcc2, dfConj));
        map1.put("3", rgl.createRFRegimePrestationAccordee(prestAcc3, dfEnf));

        Regimes map2 = rgl.groupRegimeByIdTiers(listRegimes, mapMembresFamilles);

        assertEquals(map1.size(), map2.getList().size());
    }

    @Test
    public void testGetListIdsTiers() throws Exception {
        assertEquals(6, rgl.getListIdsTiers(mapMembresFamilles).size());

    }
}
