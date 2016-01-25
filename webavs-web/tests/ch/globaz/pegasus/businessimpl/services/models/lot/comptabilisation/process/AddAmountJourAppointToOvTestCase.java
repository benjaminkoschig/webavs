package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

public class AddAmountJourAppointToOvTestCase {

    private SimpleOrdreVersement createOvBeneficiaire(String amount, String idPca, String noGroupe) {
        SimpleOrdreVersement ordreVersement = new SimpleOrdreVersement();
        ordreVersement.setMontant(amount);
        ordreVersement.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        ordreVersement.setIdPca(idPca);
        ordreVersement.setNoGroupePeriode(noGroupe);
        return ordreVersement;
    }

    private SimpleOrdreVersement createOvJoursAppoint(String amount, String idPca, String noGroupe) {
        SimpleOrdreVersement ordreVersement = new SimpleOrdreVersement();
        ordreVersement.setMontant(amount);
        ordreVersement.setCsType(IREOrdresVersements.CS_TYPE_JOURS_APPOINT);
        ordreVersement.setIdPca(idPca);
        ordreVersement.setNoGroupePeriode(noGroupe);
        return ordreVersement;
    }

    private SimpleOrdreVersement createOvRestitution(String amount, String idPca, String noGroupe) {
        SimpleOrdreVersement ordreVersement = new SimpleOrdreVersement();
        ordreVersement.setMontant(amount);
        ordreVersement.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
        ordreVersement.setIdPca(idPca);
        ordreVersement.setNoGroupePeriode(noGroupe);
        return ordreVersement;
    }

    @Test
    public void giveJoursAppoint120AddAmountShouldbe1120() {
        SimpleOrdreVersement ovJourAppoint = createOvJoursAppoint("120", "50", "1");
        SimpleOrdreVersement ovBeneficaire = createOvBeneficiaire("1000", "50", "1");
        AddAmountJourAppointToOv.addAmountJoursAppointToOrdreVersement(ovJourAppoint, ovBeneficaire);
        Assert.assertEquals("1120", ovBeneficaire.getMontant());
    }

    @Test(expected = ComptabiliserLotException.class)
    public void giveJoursAppointResolveOvShouldBeRaiseExecption() throws ComptabiliserLotException {

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        SimpleOrdreVersement ovJourAppoint = createOvJoursAppoint("350", "50", "1");
        ovs.add(ovJourAppoint);
        ovs.add(createOvBeneficiaire("1500", "51", "1"));
        ovs.add(createOvBeneficiaire("1000", "122", "1"));
        AddAmountJourAppointToOv AddAmountJourAppointToOv = new AddAmountJourAppointToOv(ovs);

        Assert.assertNotNull(AddAmountJourAppointToOv.findOrdreVersementBoundToJourAppoint(ovJourAppoint));
    }

    @Test
    public void giveJoursAppointResolveOvShouldBeReturnOv() throws ComptabiliserLotException {

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        SimpleOrdreVersement ovJourAppoint = createOvJoursAppoint("350", "50", "1");
        ovs.add(ovJourAppoint);
        ovs.add(createOvBeneficiaire("1500", "50", "1"));
        ovs.add(createOvBeneficiaire("1000", "122", "1"));
        AddAmountJourAppointToOv AddAmountJourAppointToOv = new AddAmountJourAppointToOv(ovs);

        Assert.assertNotNull(AddAmountJourAppointToOv.findOrdreVersementBoundToJourAppoint(ovJourAppoint));
    }

    @Test
    public void giveListOvAddAmountJourAppointOnAllOv() throws ComptabiliserLotException {

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        ovs.add(createOvBeneficiaire("1500", "110", "1"));
        ovs.add(createOvBeneficiaire("1000", "122", "2"));
        ovs.add(createOvBeneficiaire("1000", "1222", "3"));
        ovs.add(createOvJoursAppoint("350", "110", "1"));

        ovs.add(createOvJoursAppoint("25", "10", "1"));
        ovs.add(createOvRestitution("400", "10", "2"));
        ovs.add(createOvRestitution("800", "10", "1"));

        AddAmountJourAppointToOv AddAmountJourAppointToOv = new AddAmountJourAppointToOv(ovs);
        AddAmountJourAppointToOv.addAmountJourAppointOnMatchedOv();
        for (SimpleOrdreVersement simpleOrdreVersement : ovs) {
            System.out.println(simpleOrdreVersement.getNoGroupePeriode() + " " + simpleOrdreVersement.getIdPca() + " "
                    + simpleOrdreVersement.getMontant());
        }
        Assert.assertEquals("1850", ovs.get(0).getMontant());
        Assert.assertEquals("825", ovs.get(3).getMontant());
    }

    @Test
    public void giveListOvWithJoursAppointShouldBeReturnMapContainJoursAppoint() {

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        ovs.add(createOvJoursAppoint("350", "50", "1"));
        ovs.add(createOvBeneficiaire("1500", "50", "1"));
        ovs.add(createOvBeneficiaire("1000", "122", "1"));

        AddAmountJourAppointToOv AddAmountJourAppointToOv = new AddAmountJourAppointToOv(ovs);

        Assert.assertEquals(1, AddAmountJourAppointToOv.getMapJourAppoint().size());
        Assert.assertEquals(2, AddAmountJourAppointToOv.getMapOv().size());
    }
}
