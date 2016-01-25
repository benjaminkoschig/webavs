package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;

//@formatter:off
public class GenerateEcrituresCompensationForDecisionAcTestCase {
	@BeforeClass
	public static void before() {
		CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
	}

	private void checkEcriture(SectionPegasus sectionPegasus,String codeDebitCredit, int montant,TypeEcriture typeEcriture, String idCompteAnnexe,Ecriture ecriture) {
		Assert.assertEquals("Type de l'ecriture",typeEcriture, ecriture.getTypeEcriture());
		Assert.assertEquals("Montant",new BigDecimal(montant), ecriture.getMontant());
		Assert.assertEquals("Section",sectionPegasus, ecriture.getSection());
		Assert.assertEquals("CodeDebitCredit",codeDebitCredit, ecriture.getCodeDebitCredit());
		Assert.assertEquals("idCompteAnnexe",idCompteAnnexe, ecriture.getIdCompteAnnexe());
	}

	/*
	 * Requerant:
	 * 	InterPeriode: 4 E
	 *  EntrePriode:  2 E
	 *  ConjReq:      2 E
	 * Conjoint:
	 *  InterPeriode: 4 E
	 *  EntrePriode:  2 E
	 */
	@Test
	public void testCompensationEntreConjointRequerant() throws ComptabiliserLotException {
		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequConj(250, 300, 1500, 1700)); //req: -50;  conj: -200 : total -250
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequConj(240, 230, 1300, 1000)); //req: +10;  conj: +300 : total +310
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, "10", "20");

		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 1500,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 1500,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(3));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(6));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(7));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 10,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(8));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 10,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(9));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 200,TypeEcriture.COMPENSATION_ENTRE_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(10));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 200,TypeEcriture.COMPENSATION_ENTRE_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(11));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 40,TypeEcriture.COMPENSATION_ENTRE_PERSONNE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(12));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 40,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(13));
		
		Assert.assertEquals(14, ecritures.size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(60), compensation.getMontantADispositionConjoint());
	}


	
	@Test
	public void testCompensationEntrePeriode() throws JadeApplicationException {		
		
		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequerant(230, 300)); //-50
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequerant(450, 400)); //+200
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, null);
		
		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 400,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 400,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		
		
		Assert.assertEquals(6, ecritures.size());
		Assert.assertEquals(1, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
	}
	
	@Test
	public void testCompensationEntrePeriodeAvecConjointCouplSep() throws JadeApplicationException {
	
		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequConj(250, 300, 1500, 1700)); //req: -50;  conj:-200 
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequConj(450, 260, 1300, 1000));  //req: +190; conj:+300
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);
	
		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 1500,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 1500,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(3));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 260,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 260,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(6));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(7));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(8));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(9));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 200,TypeEcriture.COMPENSATION_ENTRE_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(10));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 200,TypeEcriture.COMPENSATION_ENTRE_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(11));

		Assert.assertEquals(12, ecritures.size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(140), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(100), compensation.getMontantADispositionConjoint());
	}

	@Test
	public void testCompensationEntrePeriodeMontantNegatif() throws JadeApplicationException {	
		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequerant(250, 300)); //-50
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequerant(100, 250)); //+200

		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, null);

		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 250,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 100,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 100,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));
		
		
		Assert.assertEquals(4, ecritures.size());
		Assert.assertEquals(2, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
	}
	
	@Test
	public void testCompensationInterPeriode() throws JadeApplicationException {

		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequerant(250, 200)); //+50
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequerant(450, 450)); //+0
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, null);
		List<Ecriture> ecritures = compensation.generateEcritures();
		
		
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(50), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 200,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 200,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 450,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 450,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));
		Assert.assertEquals(4, ecritures.size());
	}

	
	@Test
	public void testCompensationInterPeriodeDom2R() throws JadeApplicationException {

		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDom2R(50, 25, 50, 25)); //+50
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, null);

		List<Ecriture> ecritures = compensation.generateEcritures();
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(25), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(25), compensation.getMontantADispositionConjoint());
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 25,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 25,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 25,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 25,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(3));
		
	}

	@Test
	public void testCompensationInterPeriodeSansRestiution() throws JadeApplicationException {
	
		Map<Integer,List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		List<Ecriture> ecritures1 = new ArrayList<Ecriture>();
		ecritures1.add(EcritureFactory.generateEcrituresBeneficiaire(250, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
		mapEcritures.put(1,ecritures1);
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, null);
		
		List<Ecriture> ecritures = compensation.generateEcritures();
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(new BigDecimal(250), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
		Assert.assertEquals(0, ecritures.size());

	}

	@Test
	public void testDom2RToDom() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDom2RToDom(100,46,45));         
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();

		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(9), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
		
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 46,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 46,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 45,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 45,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));

		Assert.assertEquals(4, ecritures.size());
	}

	
	/*
     * @formatter:off
	 * Requerant:
	 * 	InterPeriode: 4 E
	 *  EntrePriode:  2 E
	 *  ConjReq:      0 E
	 * Conjoint:
	 *  InterPeriode: 4 E
	 *  EntrePriode:  0 E
	 */
	@Test
	public void testDom2RToSepMal() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDom2R(50, 70, 50, 70));          //req: -20;  conj: -20  : total -40
		mapEcritures.put(2, EcritureFactory.generateListEcritureRequConj(250, 230, 1300, 1000)); //req: +20;  conj: +300 : total +320
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(3));
		// Periode 2 
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 230,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(6));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 1000,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(7));
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 20,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(8));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 20,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(9));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 20,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(8));

		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(280), compensation.getMontantADispositionConjoint());
		
		Assert.assertEquals(12, ecritures.size());

	}

	
	/*
     * @formatter:off
	 * Requerant:
	 * 	InterPeriode: 4 E
	 *  EntrePriode:  2 E
	 *  ConjReq:      0 E
	 * Conjoint:
	 *  InterPeriode: 4 E
	 *  EntrePriode:  0 E
	 */
	@Test
	public void testDom2RToSepMalCompenstationInterPeriode() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDom2RToSepMal(20, 50, 200, 50));     //req: ;  conj:   : total 
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 20,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 20,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));

		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 30,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 30,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));

		
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(120), compensation.getMontantADispositionConjoint());
		
		Assert.assertEquals(6, ecritures.size());

	}
	
	
	
	@Test
	public void testDomToDom2R() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDomToDom2R(50, 120, 50));        //req: -20;  conj: 0    : total -20
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		Assert.assertEquals(1, compensation.getRestitutionNonRembourserRequerant().size());
		for (BigDecimal reste : compensation.getRestitutionNonRembourserRequerant().values()) {
			Assert.assertEquals(new BigDecimal(20), reste);
		}
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
		
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));

		Assert.assertEquals(4, ecritures.size());
	}
	
	
	@Test
	public void testDomToDom2RNegatif() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDomToDom2R(50, 60, 50));        //req: -20;  conj: 0    : total -20
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(40), compensation.getMontantADispositionConjoint());
		
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 10,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 10,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));

		Assert.assertEquals(4, ecritures.size());
	}

	@Test
	public void testDomToSepMal() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureDomToSepMal(1000, 100, 150));  //req: 900;  conj: 150  : total 1050
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 100,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 100,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));

		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(900), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(150), compensation.getMontantADispositionConjoint());
		
		Assert.assertEquals(2, ecritures.size());
	}
	
	@Test
	public void testSepMalToDom() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureSepMalToDom(30, 600, 10)); //req: -570;  conj: -10 : total -580
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		
		// Periode 1
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 30,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 30,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		
		Assert.assertEquals(2, ecritures.size());
		Assert.assertEquals(1, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(1, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
	}
	
	@Test
	public void testSepMalToDom2R() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		mapEcritures.put(1, EcritureFactory.generateListEcritureSepMalToDom2R(30, 500, 101, 20)); //req: -398;  conj: +81 : total -317
		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 30,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 30,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 20,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 20,TypeEcriture.COMPENSATION_INTER_PERIODE,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(3));

		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 81,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 81,TypeEcriture.COMPENSATION_ENTRE_PERSONNE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		
		Assert.assertEquals(6, ecritures.size());
		Assert.assertEquals(1, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(0), compensation.getMontantADispositionConjoint());
	}
	
	@Test
	public void testXMultipassage() throws JadeApplicationException {

		Map<Integer, List<Ecriture>> mapEcritures = new HashMap<Integer, List<Ecriture>>();
		
		mapEcritures.put(1, EcritureFactory.generateListEcritureRequerant(150, 200));             //req: -50;   conj:   0 : total -50
		mapEcritures.put(2, EcritureFactory.generateListEcritureDomToDom2R(50, 150, 50));         //req: -100;  conj: +50 : total -50
		mapEcritures.put(3, EcritureFactory.generateListEcritureDom2RToSepMal(1000, 50, 80, 50)); //req: +950;  conj: +30 : total +980

		
		GenerateEcrituresCompensationForDecisionAc compensation = new GenerateEcrituresCompensationForDecisionAc(
				mapEcritures, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,  CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

		List<Ecriture> ecritures = compensation.generateEcritures();
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 150,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(0));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 150,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(1));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(2));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(3));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(4));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(5));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT,ecritures.get(6));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_INTER_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(7));
		
		
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(8));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 50,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(9));
		checkEcriture(SectionPegasus.DECISION_PC,APIEcriture.DEBIT, 100,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(10));
		checkEcriture(SectionPegasus.RESTIUTION,APIEcriture.CREDIT, 100,TypeEcriture.COMPENSATION_ENTRE_PERIODE, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT,ecritures.get(11));
		
		Assert.assertEquals(12, ecritures.size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserRequerant().size());
		Assert.assertEquals(0, compensation.getRestitutionNonRembourserConjoint().size());
		Assert.assertEquals(new BigDecimal(800), compensation.getMontantADispositionRequerant());
		Assert.assertEquals(new BigDecimal(80), compensation.getMontantADispositionConjoint());
	}
	
}
