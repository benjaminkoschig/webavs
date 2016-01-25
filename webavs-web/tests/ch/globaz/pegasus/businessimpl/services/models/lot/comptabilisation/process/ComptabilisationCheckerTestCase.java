package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;

public class ComptabilisationCheckerTestCase {

    private void check(ComptabilisationData data) throws ComptabiliserLotException, JAException {

        ComptabilisationChecker checker = new ComptabilisationChecker(data);
        ComptabilisationChecker spy = Mockito.spy(checker);
        Mockito.doReturn("").when(spy).getMessage(Matchers.anyString());
        // Mockito.when(spy.resolveIdRefRubrique(Matchers.any(OrdreVersementForList.class))).thenReturn("10000");
        //
        // ComptabilisationChecker checker = Mockito.spy(new ComptabilisationChecker(data));
        // Mockito.doReturn("Error").when(checker).getMessage("");
        spy.check();
    }

    private ComptabilisationData generateData() throws JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setDateDernierPmt(new JADate("01.2012"));
        data.setDateEchance(new JADate("02.2012"));
        data.setDateValeur(new JADate("01.2012"));
        data.setListOV(new ArrayList<OrdreVersementForList>());
        data.setComptesAnnexes(new ArrayList<CompteAnnexeSimpleModel>());
        data.setSections(new ArrayList<SectionSimpleModel>());
        SimpleLot lot = new SimpleLot();
        lot.setCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
        lot.setCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        lot.setCsTypeLot(IRELot.CS_TYP_LOT_DECISION);
        data.setSimpleLot(lot);
        return data;
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckDateEcheanceKoBefore() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.setDateEchance(new JADate("12.2011"));
        check(data);
    }

    @Test
    public void testCheckDateEcheanceOkEquals() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.setDateEchance(new JADate("02.2012"));
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckDateValeurKoApres() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.setDateValeur(new JADate("01.2013"));
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckDateValeurKoAvant() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.setDateValeur(new JADate("01.2011"));
        check(data);
    }

    @Test
    public void testCheckDateValeurOK() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckEtatLotValideeKo() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckIsLotComptabilisableKoEtat() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckIsLotComptabilisableKoOwner() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.getSimpleLot().setCsProprietaire(IRELot.CS_LOT_OWNER_RENTES);
        check(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testCheckIsLotComptabilisableKoType() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        data.getSimpleLot().setCsTypeLot(IRELot.CS_TYP_LOT_MENSUEL);
        check(data);
    }

    @Test
    public void testCheckOk() throws JAException, ComptabiliserLotException {
        ComptabilisationData data = generateData();
        check(data);
    }

    @Test
    public void testComptabilisationChecker() throws JAException {
        ComptabilisationData data = generateData();
        ComptabilisationChecker checker = new ComptabilisationChecker(data);
    }

}
