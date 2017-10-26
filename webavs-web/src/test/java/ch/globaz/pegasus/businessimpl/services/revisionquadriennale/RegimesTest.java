package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import static org.junit.Assert.*;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class RegimesTest {

    private Map<String, Regime> mapRegimesFamille;
    private Regimes reg = null;
    private Regime regime = null;
    private Regime regime2 = null;
    private DonneeFinanciere df = null;

    public RegimesTest() {
        df = new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, new Date(), new Date(), "1", "2");

        regime = new Regime(new Montant(100), IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE, df);

        regime2 = new Regime(new Montant(200), IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE, df);

        mapRegimesFamille = new HashMap<String, Regime>();
        mapRegimesFamille.put("1", regime);

        reg = new Regimes();
        reg.setMapRegimesFamille(mapRegimesFamille);
        reg.put("2", regime2);
    }

    @Test
    public void testGetMapRegimesFamille() throws Exception {
        assertEquals(Montant.newMensuel("100"), reg.get("1").getMontant());
    }

    @Test
    public void testContainsKey() throws Exception {
        assertTrue(mapRegimesFamille.containsKey("1"));
        assertFalse(mapRegimesFamille.containsKey("3"));
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(regime, mapRegimesFamille.get("1"));
        assertNull(mapRegimesFamille.get("3"));
    }

    @Test
    public void testPut() throws Exception {
        assertTrue(mapRegimesFamille.containsKey("2"));
    }

}
