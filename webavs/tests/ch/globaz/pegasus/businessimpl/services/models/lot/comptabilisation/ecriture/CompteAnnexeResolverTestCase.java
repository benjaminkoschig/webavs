package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.osiris.external.IntRole;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;

public class CompteAnnexeResolverTestCase {

    private List<CompteAnnexeSimpleModel> generateListComptesAnnexe() {
        List<CompteAnnexeSimpleModel> list = new ArrayList<CompteAnnexeSimpleModel>();
        list.add(CompteAnnexeFactory.generateCompteAnnexe("10", "1"));
        list.add(CompteAnnexeFactory.generateCompteAnnexe("20", "2"));
        list.add(CompteAnnexeFactory.generateCompteAnnexe("30", "3"));
        list.add(CompteAnnexeFactory.generateCompteAnnexe("40", "4"));
        return list;
    }

    @Test
    public void testAddCompteAnnexes() {
        CompteAnnexeResolver.addComptesAnnexes(new ArrayList<CompteAnnexeSimpleModel>());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCompteAnnexesError() {
        CompteAnnexeResolver.addComptesAnnexes(null);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testClear() throws ComptabiliserLotException {
        CompteAnnexeResolver.addComptesAnnexes(generateListComptesAnnexe());
        CompteAnnexeResolver.clear();
        CompteAnnexeResolver.resolveByIdTiers("2", IntRole.ROLE_RENTIER);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testInstanceNull() throws ComptabiliserLotException {
        CompteAnnexeResolver.resolveByIdTiers("2", IntRole.ROLE_RENTIER);
    }

    @Test
    public void testResoveByIdCompteAnnexe() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdCompteAnnexe("20"));
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testResoveByIdCompteAnnexeWithErrorNoFounded() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdCompteAnnexe("75"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResoveByIdCompteAnnexeWithErrorNull() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdCompteAnnexe(null));
    }

    @Test
    public void testResoveByIdTiers() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdTiers("2", IntRole.ROLE_RENTIER));
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testResoveByIdTiersWithErrorNoAddedComptesAnnexe() throws ComptabiliserLotException {
        CompteAnnexeResolver.resolveByIdTiers("10", IntRole.ROLE_RENTIER);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testResoveByIdTiersWithErrorNonFounded() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdTiers("15", IntRole.ROLE_RENTIER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResoveByIdTiersWithErrorNull() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdTiers(null, IntRole.ROLE_RENTIER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResoveByIdTiersWithErrorRoleNull() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdTiers("15", null));
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testResoveByIdTiersWithErrorTooMany() throws ComptabiliserLotException {
        List<CompteAnnexeSimpleModel> list = generateListComptesAnnexe();
        list.add(CompteAnnexeFactory.generateCompteAnnexe("10", "1"));
        list.add(CompteAnnexeFactory.generateCompteAnnexe("20", "2"));
        CompteAnnexeResolver.addComptesAnnexes(list);
        Assert.assertEquals(list.get(1), CompteAnnexeResolver.resolveByIdTiers("2", IntRole.ROLE_RENTIER));
    }
}
