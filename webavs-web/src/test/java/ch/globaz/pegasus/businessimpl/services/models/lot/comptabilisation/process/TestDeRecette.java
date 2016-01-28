package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.business.service.JournalService;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

//@formatter:off
public class TestDeRecette {
	//@formatter:off
	public final static String idCompteAnnexeConjoint = "48695"; // 33782
	public final static String idCompteAnnexeRequerant = "33872";
	public final static String idTiersConjoint = "205802";
	public final static String idTiersCreancier = "103612";
	public final static String idTiersRequerant = "114215";
	public static final String NSS_CONJOINT = "756.3277.3574.59"; // Anita Blaser
	public final static String NSS_requerant = "756.0000.1615.50"; // b 7756.2945.6342.81/07.02.1927 Richert Denise
	public static final String NSS_REQUERANT = "756.6916.5971.17";

	
	
	private void comptabilise(SimpleLot lot) throws PmtMensuelException, JadeApplicationServiceNotAvailableException,
			JadePersistenceException, ComptabiliserLotException, JAException, Exception {
		String date = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
		JadeThread.commitSession();
		
		ComptabilisationHandler.execute(lot.getIdLot(), "1", "1", "Test journal PC Decision Ac",
				date, date, createJournalService());

		JadeThread.commitSession();

		System.out.println(ComptabilisationHandler.displayTime() + "\r");
		// Thread.sleep(1000);
		Assert.assertFalse(JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR));

	}
	
	private JournalService createJournalService() throws JadePersistenceException, JadeApplicationException{
		JournalService spy = Mockito.spy(CABusinessServiceLocator.getJournalService());
		Mockito.doReturn(new JournalSimpleModel()).when(spy).comptabilise(Matchers.any(JournalSimpleModel.class));
		return spy ;
	}

	private SimpleLot createLot() throws JadePersistenceException, LotException,
			JadeApplicationServiceNotAvailableException {
		SimpleLot simpleLot = new SimpleLot();
		simpleLot.setCsTypeLot(IRELot.CS_TYP_LOT_DECISION);
		simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
		simpleLot.setDateCreation(JACalendar.todayJJsMMsAAAA());
		simpleLot.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
		simpleLot.setDescription("Test pour la comtablisisation");
		simpleLot.setCsProprietaire(IRELot.CS_LOT_OWNER_PC);
		return CorvusServiceLocator.getLotService().create(simpleLot);
	}

	private void createOvBeneficiaire(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleBeneficiaire(montant,
				TestDeRecette.idTiersRequerant, noGroupe, prestation.getId());

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvBeneficiaireConjoint(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleBeneficiaire(montant,
				TestDeRecette.idTiersConjoint, noGroupe, prestation.getId());
		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvBeneficiaireDom2R(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleBeneficiaireDom2R(montant,
				TestDeRecette.idTiersRequerant, TestDeRecette.idTiersConjoint, noGroupe, prestation.getId());

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvCreancier(SimplePrestation prestation, String montant) throws OrdreVersementException,
			JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleCreancier(montant,
				TestDeRecette.idTiersCreancier, TestDeRecette.idTiersRequerant, prestation.getId());
		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvDette(SimplePrestation prestation, String montant, String idSection)
			throws OrdreVersementException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleDetteCompta(montant, idSection,
				TestDeRecette.idTiersRequerant);

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvRestitution(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleRestitution(montant,
				TestDeRecette.idTiersRequerant, noGroupe, prestation.getId());

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvRestitutionConjoint(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleRestitution(montant,
				TestDeRecette.idTiersConjoint, noGroupe, prestation.getId());

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private void createOvRestitutionDom2R(SimplePrestation prestation, String montant, String noGroupe)
			throws OrdreVersementException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleRestitutionDom2R(montant,
				TestDeRecette.idTiersRequerant, TestDeRecette.idTiersRequerant, noGroupe, prestation.getId());

		ov = PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
	}

	private SimplePrestation createPresation(SimpleLot lot, String montant) throws PrestationException,
			JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimplePrestation prestation = new SimplePrestation();
		prestation.setDateDebut("01.2012");
		prestation.setDateFin("12.2012");
		prestation.setIdCompteAnnexeConjoint(null);
		prestation.setIdCompteAnnexeRequerant(TestDeRecette.idCompteAnnexeRequerant);
		prestation.setIdTiersBeneficiaire(TestDeRecette.idTiersRequerant);
		prestation.setIdLot(lot.getIdLot());
		prestation.setCsTypePrestation(IPCPresation.CS_TYPE_DE_PRESTATION_DECISION);
		prestation.setIdVersionDroit("45465456464214");
		prestation.setMontantTotal(montant);
		return PegasusImplServiceLocator.getSimplePrestationService().create(prestation);
	}

	private SimplePrestation createPresationWithConjoint(SimpleLot lot, String montant) throws PrestationException,
			JadePersistenceException, JadeApplicationServiceNotAvailableException {
		SimplePrestation prestation = new SimplePrestation();
		prestation.setDateDebut("01.2012");
		prestation.setDateFin("12.2012");
		prestation.setIdCompteAnnexeConjoint(TestDeRecette.idCompteAnnexeConjoint);
		prestation.setIdCompteAnnexeRequerant(TestDeRecette.idCompteAnnexeRequerant);
		prestation.setIdTiersBeneficiaire(TestDeRecette.idTiersRequerant);
		prestation.setIdLot(lot.getIdLot());
		prestation.setCsTypePrestation(IPCPresation.CS_TYPE_DE_PRESTATION_DECISION);
		prestation.setIdVersionDroit("45465456464214");
		prestation.setMontantTotal(montant);
		return PegasusImplServiceLocator.getSimplePrestationService().create(prestation);
	}

	 @Test
	    @Ignore
	public void testAvecCreancier() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "200");
		// "200
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "350", "1");
		createOvCreancier(prestation, "150");

		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testCorrectionAvecConjointPlusieursPeriode() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresationWithConjoint(lot, "250");

		// 220
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "330", "1");
		// 50
		createOvBeneficiaireConjoint(prestation, "200", "1");
		createOvRestitutionConjoint(prestation, "150", "1");

		// 50
		createOvBeneficiaire(prestation, "800", "2");
		createOvRestitution(prestation, "750", "2");
		// -70
		createOvBeneficiaireConjoint(prestation, "150", "2");
		createOvRestitutionConjoint(prestation, "220", "2");

		comptabilise(lot);
	}
	
	 @Test
	    @Ignore
	public void testCorrectionAvecConjointUnePeriode() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresationWithConjoint(lot, "270");

		// 220
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "330", "1");
		// 50
		createOvBeneficiaireConjoint(prestation, "200", "1");
		createOvRestitutionConjoint(prestation, "150", "1");

		comptabilise(lot);
	}

	
	 @Test
	    @Ignore
	public void testCorrectionDom2R() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "120");
		// "220
		createOvBeneficiaireDom2R(prestation, "550", "1");
		createOvRestitutionDom2R(prestation, "330", "1");
		// +50
		createOvBeneficiaireDom2R(prestation, "800", "2");
		createOvRestitutionDom2R(prestation, "750", "2");
		// -150
		createOvBeneficiaireDom2R(prestation, "450", "3");
		createOvRestitutionDom2R(prestation, "600", "3");

		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testCorrectionPlusieursPeriodesPrendArgentreEntrePeriode() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "170");
		// "220
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "330", "1");
		// +50
		createOvBeneficiaire(prestation, "800", "2");
		createOvRestitution(prestation, "750", "2");
		// -100
		createOvBeneficiaire(prestation, "450", "3");
		createOvRestitution(prestation, "550", "3");

		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testDeuxOvCorrectionDroit() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "200");
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "350", "1");
		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testDom2RToDom() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "220");
		// 220
		createOvBeneficiaireDom2R(prestation, "550", "1");
		createOvRestitutionDom2R(prestation, "330", "1");
		// 50
		createOvBeneficiaire(prestation, "800", "2");
		createOvRestitutionDom2R(prestation, "750", "2");
		// -50
		createOvBeneficiaire(prestation, "150", "3");
		createOvRestitution(prestation, "200", "3");

		comptabilise(lot);
	}
	
	 @Test
	    @Ignore
	public void testDom2RToSepMal() throws PmtMensuelException, ComptabiliserLotException, JAException, Exception{
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresationWithConjoint(lot, "2820");
		// 220
		createOvBeneficiaireDom2R(prestation, "550", "1");
		createOvRestitutionDom2R(prestation, "330", "1");
		
		//2600
		createOvBeneficiaire(prestation, "100", "2");
		createOvBeneficiaireConjoint(prestation, "3000", "2");
		createOvRestitutionDom2R(prestation, "500", "2");
		
		comptabilise(lot);
	}
	
	
	 @Test
	    @Ignore
	public void testDomToDom2R() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "240");
		// 220
		createOvBeneficiaire(prestation, "550", "1");
		createOvRestitution(prestation, "330", "1");
		// 50
		createOvBeneficiaireDom2R(prestation, "800", "2");
		createOvRestitution(prestation, "750", "2");
		// -30
		createOvBeneficiaireDom2R(prestation, "170", "3");
		createOvRestitutionDom2R(prestation, "200", "3");

		comptabilise(lot);
	}
	
	
	 @Test
	    @Ignore
	public void testMontantTotalNotEquals() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "2500");
		createOvBeneficiaire(prestation, "550", "1");

		String date = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
		JadeThread.commitSession();
		PegasusServiceLocator.getLotService().comptabiliserLot(lot.getIdLot(), "1", "1", "Test journal PC Decision Ac",
				date, date);
		JadeThread.commitSession();

		Thread.sleep(1000);
		Assert.assertTrue(JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR));
	}
	
	
	@Test
	@Ignore
	public void testSepMalToDom2r() throws PmtMensuelException, ComptabiliserLotException, JAException, Exception{
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresationWithConjoint(lot, "50");

		//50
		createOvBeneficiaire(prestation, "150", "1");
		createOvRestitution(prestation, "100", "1");
		//2500
		createOvBeneficiaireConjoint(prestation, "3000", "1");
		createOvRestitutionConjoint(prestation, "500", "1");

		// -2500
		createOvBeneficiaireDom2R(prestation, "550", "2");
		createOvRestitution(prestation, "50", "2");
		createOvRestitutionConjoint(prestation, "3000", "2");		
		
		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testSepMalToDom2rNegatif() throws PmtMensuelException, ComptabiliserLotException, JAException, Exception{
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresationWithConjoint(lot, "-3000");

		// -3000
		createOvBeneficiaireDom2R(prestation, "550", "2");
		createOvRestitution(prestation, "50", "2");
		createOvRestitutionConjoint(prestation, "3500", "2");		
		
		comptabilise(lot);
	}

	 @Test
	    @Ignore
	public void testUnOvDroitInitial() throws Exception {
		SimpleLot lot = createLot();
		SimplePrestation prestation = createPresation(lot, "550");
		createOvBeneficiaire(prestation, "550", "1");
		comptabilise(lot);
	}
}
