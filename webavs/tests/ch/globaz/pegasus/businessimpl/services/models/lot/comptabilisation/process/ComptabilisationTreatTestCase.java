package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.db.comptes.CAJournal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;

public class ComptabilisationTreatTestCase {
    public static String DATE_DERNIER_PMT = "05.2013";
    public static String ID_REF_RUBRIQUE = "1000";

    private PegasusJournalConteneur execute(ComptabilisationData data) throws JadeApplicationException,
            ComptabiliserLotException, JAException {
        ComptabilisationTreat treat = newComptabilisationTreat(data);
        return treat.treat().getJournalConteneur();
    }

    private ComptabilisationData generateBaseData() throws JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setSimpleLot(generateLot());
        data.setDateDernierPmt(new JADate("01.2012"));
        data.setDateValeur(new JADate("01.2012"));
        data.setDateEchance(new JADate("01.2012"));
        data.setListOV(new ArrayList<OrdreVersementForList>());
        return data;
    }

    private SimpleLot generateLot() {
        SimpleLot lot = new SimpleLot();
        lot.setCsEtat(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);
        lot.setCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        lot.setCsTypeLot(IRELot.CS_TYP_LOT_DECISION);
        lot.setDateCreation("");
        lot.setDateEnvoi("");
        lot.setDescription("Lot de test ");
        lot.setIdLot("");
        return lot;
    }

    private ComptabilisationTreat newComptabilisationTreat(ComptabilisationData data) throws JadeApplicationException {
        ComptabilisationTreat treat = new ComptabilisationTreat(data);
        ComptabilisationTreat spy = Mockito.spy(treat);
        List<Ecriture> list = new ArrayList<Ecriture>();
        Mockito.doReturn(list).when(spy).generatePresationsOperations();
        Mockito.doReturn(false).when(spy).hasError();
        return spy;
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testDateProhaiPaiementNull() throws JadeApplicationException, JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setSimpleLot(generateLot());
        data.setDateValeur(new JADate("01.2012"));
        data.setListOV(new ArrayList<OrdreVersementForList>());
        ComptabilisationTreat treat = newComptabilisationTreat(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testDateValeurNull() throws JadeApplicationException, JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setSimpleLot(generateLot());
        data.setDateDernierPmt(new JADate("01.2012"));
        data.setListOV(new ArrayList<OrdreVersementForList>());
        ComptabilisationTreat treat = newComptabilisationTreat(data);
    }

    @Test
    public void testEtatLotValide() throws JadeApplicationException, JAException {
        ComptabilisationData data = generateBaseData();
        PegasusJournalConteneur conteneur = execute(data);
        Assert.assertEquals(IRELot.CS_ETAT_LOT_VALIDE, data.getSimpleLot().getCsEtat());
        Assert.assertEquals(IRELot.CS_ETAT_LOT_VALIDE, conteneur.getLot().getCsEtat());
    }

    @Test
    public void testJournal() throws JadeApplicationException, JAException {
        ComptabilisationData data = generateBaseData();
        data.setLibelleJournal(null);
        PegasusJournalConteneur conteneur = execute(data);
        Assert.assertNotNull(conteneur.getJournalModel());
        Assert.assertEquals(CAJournal.TYPE_AUTOMATIQUE, conteneur.getJournalModel().getTypeJournal());
        Assert.assertEquals(data.getDateValeur(), conteneur.getJournalModel().getDateValeurCG());
        Assert.assertEquals(data.getSimpleLot().getDescription(), conteneur.getJournalModel().getLibelle());
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testLotNull() throws JadeApplicationException, JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setListOV(new ArrayList<OrdreVersementForList>());
        data.setDateDernierPmt(new JADate("01.2012"));
        data.setDateValeur(new JADate("01.2012"));
        ComptabilisationTreat treat = newComptabilisationTreat(data);
    }

    @Test(expected = ComptabiliserLotException.class)
    public void testOvsNull() throws JadeApplicationException, JAException {
        ComptabilisationData data = new ComptabilisationData();
        data.setSimpleLot(generateLot());
        data.setDateDernierPmt(new JADate("01.2012"));
        data.setDateValeur(new JADate("01.2012"));
        ComptabilisationTreat treat = newComptabilisationTreat(data);
    }

    @Test
    public void testPresationEcritureNotNull() throws JadeApplicationException, JAException {
        ComptabilisationData data = generateBaseData();
        PegasusJournalConteneur conteneur = execute(data);
        Assert.assertNotNull(conteneur.getOperations());

    }

}
