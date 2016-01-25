package ch.globaz.pegasus.businessimpl.tests.pcAccordee;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.junit.Assert;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.tests.util.BaseTestCase;

public class AllocationNoelTestCase extends BaseTestCase {

    private SimpleInformationsComptabilite createInfoCompta(SimpleInformationsComptabilite infoCompta)
            throws RentesAccordeesException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(infoCompta);
    }

    private SimpleAllocationNoel generateSimpleAllocationNoelPOJO() {
        SimpleAllocationNoel alloc = new SimpleAllocationNoel();
        alloc.setAnneeAllocation("1999");
        alloc.setIdDemande("2");
        alloc.setIdPCAccordee("23432");
        alloc.setMontantAllocation("3800");
        alloc.setNbrePersonnes("3");
        alloc.setIdPrestationAccordee("23");
        alloc.setIdPrestationAccordeeConjoint("44");
        return alloc;
    }

    private SimpleInformationsComptabilite generateSimpleInfoComptaPOJO(String idTiersForTest) {
        SimpleInformationsComptabilite infoCompta = new SimpleInformationsComptabilite();
        ;
        infoCompta.setIdCompteAnnexe("1");
        infoCompta.setIdTiersAdressePmt(idTiersForTest);
        return infoCompta;
    }

    private SimplePrestationsAccordees generateSimplePrestationAccordeesPOJO(String idTiers, String montant,
            String idInfoCompta) {
        SimplePrestationsAccordees prestAcc = new SimplePrestationsAccordees();
        prestAcc.setIdTiersBeneficiaire(idTiers);
        prestAcc.setMontantPrestation(montant);

        String date = "12." + JACalendar.today().getYear();

        prestAcc.setDateDebutDroit(date);
        prestAcc.setDateFinDroit(date);

        prestAcc.setCsGenre(IREPrestationAccordee.CS_GENRE_PC);
        prestAcc.setIdInfoCompta(idInfoCompta);
        prestAcc.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        prestAcc.setIsRetenues(false);
        prestAcc.setIsPrestationBloquee(false);
        prestAcc.setIsAttenteMajBlocage(false);
        prestAcc.setIsAttenteMajRetenue(false);

        boolean isRepresentantLegalOuDansHome = true;

        // C'est du chenis ce binz @see AllocationDeNoelServiceImpl.getCodePrestationAllocationNoel(String, boolean)
        //
        // if (isRepresentantLegalOuDansHome) {
        // // LGA old code : prestAcc.setCodePrestation(REGenresPrestations.GENRE_198);
        // prestAcc.setCodePrestation(PRCodePrestationPC._158.getCodePrestationAsString());
        // } else {
        // // LGA old code : prestAcc.setCodePrestation(REGenresPrestations.GENRE_199);
        // prestAcc.setCodePrestation(PRCodePrestationPC._159.getCodePrestationAsString());
        // }

        return prestAcc;

    }

    public final void testInsertionUpdateComplexAllocationDeNoel() {
        // Creation infocompta req
        SimpleInformationsComptabilite infoComptaReq = generateSimpleInfoComptaPOJO("12432");
        // Creation infocompta con
        SimpleInformationsComptabilite infoComptaCon = generateSimpleInfoComptaPOJO("13456");

        try {
            infoComptaReq = createInfoCompta(infoComptaReq);
            infoComptaCon = createInfoCompta(infoComptaCon);
        } catch (Exception e) {
            System.out.println("************** Error during creation InfoCompta: " + e.getMessage());
        }

        // Creation SimplePrestation req et con
        SimplePrestationsAccordees prestreq = generateSimplePrestationAccordeesPOJO(
                infoComptaReq.getIdTiersAdressePmt(), "300", infoComptaReq.getIdInfoCompta());
        SimplePrestationsAccordees prestcon = generateSimplePrestationAccordeesPOJO(
                infoComptaCon.getIdTiersAdressePmt(), "300", infoComptaCon.getIdInfoCompta());

        // creation alloc noel

        try {
            prestreq = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(prestreq);
            prestcon = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(prestcon);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        SimpleAllocationNoel allocReq = new SimpleAllocationNoel();
        allocReq.setAnneeAllocation("1999");
        allocReq.setIdDemande("2");
        allocReq.setIdPCAccordee("23432");
        allocReq.setMontantAllocation(new Float((Float.parseFloat(prestreq.getMontantPrestation()) + Float
                .parseFloat(prestcon.getMontantPrestation()))).toString());
        allocReq.setNbrePersonnes("6");
        allocReq.setIdPrestationAccordee(prestreq.getIdPrestationAccordee());
        allocReq.setIdPrestationAccordeeConjoint(prestcon.getIdPrestationAccordee());

        try {
            PegasusImplServiceLocator.getSimpleAllocationDeNoelService().create(allocReq);
        } catch (Exception ex) {

        }
        try {
            JadeThread.commitSession();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue(true);

    }

    // public final void testInsertionUpdateSImpleAllocation() {
    //
    // System.out.println("******** Allocation noel, create entity test");
    //
    // SimpleAllocationNoel alloc = this.generateSimpleAllocationNoelPOJO();
    //
    // try {
    // alloc = PegasusImplServiceLocator.getSimpleAllocationDeNoelService().create(alloc);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // Assert.assertNotNull(alloc);
    //
    // try {
    // JadeThread.commitSession();
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // System.out.println("******** Allocation noel, read and update entity test");
    // try {
    // alloc = PegasusImplServiceLocator.getSimpleAllocationDeNoelService().read(alloc.getId());
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // Assert.assertNotNull(alloc);
    //
    // alloc.setMontantAllocation("1000");
    // alloc.setNbrePersonnes("3");
    //
    // try {
    // alloc = PegasusImplServiceLocator.getSimpleAllocationDeNoelService().update(alloc);
    // JadeThread.commitSession();
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // Assert.assertEquals("1000", alloc.getMontantAllocation());
    // Assert.assertEquals("3", alloc.getNbrePersonnes());
    //
    // }
}
