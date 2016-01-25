package ch.globaz.al.test.businessimpl.service.paiement;

import static org.junit.Assert.*;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.paiement.PaiementPrestationComplexSearchModel;
import ch.globaz.al.business.paiement.CompabilisationPrestationContainer;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.paiement.PaiementContainer;
import ch.globaz.al.business.paiement.PaiementRecapitulatifBusinessModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;

/**
 * Nom du service de paiement direct
 * 
 * @author jts
 * 
 */
public class PaiementDirectServiceTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#annulerPreparationPaiementDirect(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testAnnulerPreparationPaiementDirect() {
        try {

            String periode = "06.2010";

            ALImplServiceLocator.getPaiementDirectService().annulerPreparationPaiementDirect(periode);

            PaiementPrestationComplexSearchModel search = new PaiementPrestationComplexSearchModel();
            search.setForPeriodeA(periode);
            search.setForEtat(ALCSPrestation.ETAT_TR);
            HashSet setBoni = new HashSet();
            setBoni.add(ALCSPrestation.BONI_DIRECT);
            setBoni.add(ALCSPrestation.BONI_RESTITUTION);
            search.setInBonifications(setBoni);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = (PaiementPrestationComplexSearchModel) JadePersistenceManager.search(search);

            assertEquals(0, search.getSearchResults().length);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#checkPrestations(java.util.Collection, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)}
     * .
     */
    @Ignore
    @Test
    public void testCheckPrestations() {
        try {
            // TODO : à terminer
            ProtocoleLogger logger = ALImplServiceLocator.getPaiementDirectService().checkPrestations(
                    ALImplServiceLocator.getPaiementDirectService().loadPrestationsSimulation("06.2010")
                            .getPaiementBusinessList(), "01.06.2010", new ProtocoleLogger());
            assertEquals(1, logger.getErrorsContainer().size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#loadPrestationsComptabilisees(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testLoadPrestationsComptabilitees() {
        try {
            // TODO : à terminer
            PaiementContainer col = ALImplServiceLocator.getPaiementDirectService().loadPrestationsComptabilisees("1");

            // vérification du nombre d'enregistrement
            assertEquals(10, col.getPaiementBusinessList().size());

            // vérification des montants
            BigDecimal totalCredit = new BigDecimal("0");
            BigDecimal totalDebit = new BigDecimal("0");
            BigDecimal totalOrdres = new BigDecimal("0");

            Iterator it = col.getPaiementBusinessList().iterator();
            while (it.hasNext()) {
                PaiementRecapitulatifBusinessModel paiement = (PaiementRecapitulatifBusinessModel) it.next();
                totalCredit = totalCredit.add(paiement.getCredit());
                totalDebit = totalDebit.add(paiement.getDebit());
                totalOrdres = totalOrdres.add(paiement.getOrdreVersement());
            }

            assertEquals(new BigDecimal("1000"), totalCredit);
            assertEquals(new BigDecimal("1000"), totalDebit);
            assertEquals(new BigDecimal("1000"), totalOrdres);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#loadPrestationsPreparees(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testLoadPrestationsPreparees() {
        try {
            // TODO : à terminer
            Collection col = null;// ALImplServiceLocator.getPaiementDirectService().loadPrestationsPreparees("06.2010");

            // vérification du nombre d'enregistrement
            assertEquals(10, col.size());

            // vérification des montants
            BigDecimal totalCredit = new BigDecimal("0");
            BigDecimal totalDebit = new BigDecimal("0");
            BigDecimal totalOrdres = new BigDecimal("0");

            Iterator it = col.iterator();
            while (it.hasNext()) {
                PaiementRecapitulatifBusinessModel paiement = (PaiementRecapitulatifBusinessModel) it.next();
                totalCredit = totalCredit.add(paiement.getCredit());
                totalDebit = totalDebit.add(paiement.getDebit());
                totalOrdres = totalOrdres.add(paiement.getOrdreVersement());
            }

            assertEquals(new BigDecimal("1000"), totalCredit);
            assertEquals(new BigDecimal("1000"), totalDebit);
            assertEquals(new BigDecimal("1000"), totalOrdres);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#loadPrestationsRecapitualifComptabilisees(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testLoadPrestationsRecapitualifComptabilisees() {
        try {

            // TODO : à terminer
            Collection col = ALImplServiceLocator.getPaiementDirectService().loadPrestationsComptabilisees("1")
                    .getPaiementRecapitulatifBusinessList();

            // vérification du nombre d'enregistrement
            assertEquals(10, col.size());

            // vérification des montants
            BigDecimal totalCredit = new BigDecimal("0");
            BigDecimal totalDebit = new BigDecimal("0");
            BigDecimal totalOrdres = new BigDecimal("0");

            Iterator it = col.iterator();
            while (it.hasNext()) {
                PaiementRecapitulatifBusinessModel paiement = (PaiementRecapitulatifBusinessModel) it.next();
                totalCredit = totalCredit.add(paiement.getCredit());
                totalDebit = totalDebit.add(paiement.getDebit());
                totalOrdres = totalOrdres.add(paiement.getOrdreVersement());
            }

            assertEquals(new BigDecimal("1000"), totalCredit);
            assertEquals(new BigDecimal("1000"), totalDebit);
            assertEquals(new BigDecimal("1000"), totalOrdres);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#loadPrestationsRecapitualifSimulation(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testLoadPrestationsRecapitualifSimulation() {
        try {
            // TODO : à terminer
            Collection col = ALImplServiceLocator.getPaiementDirectService().loadPrestationsSimulation("06.2010")
                    .getPaiementRecapitulatifBusinessList();

            // vérification du nombre d'enregistrement
            assertEquals(10, col.size());

            // vérification des montants
            BigDecimal totalCredit = new BigDecimal("0");
            BigDecimal totalDebit = new BigDecimal("0");
            BigDecimal totalOrdres = new BigDecimal("0");

            Iterator it = col.iterator();
            while (it.hasNext()) {
                PaiementRecapitulatifBusinessModel paiement = (PaiementRecapitulatifBusinessModel) it.next();
                totalCredit = totalCredit.add(paiement.getCredit());
                totalDebit = totalDebit.add(paiement.getDebit());
                totalOrdres = totalOrdres.add(paiement.getOrdreVersement());
            }

            assertEquals(new BigDecimal("1000"), totalCredit);
            assertEquals(new BigDecimal("1000"), totalDebit);
            assertEquals(new BigDecimal("1000"), totalOrdres);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#loadPrestationsSimulation(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testLoadPrestationsSimulation() {
        try {

            Collection col = ALImplServiceLocator.getPaiementDirectService().loadPrestationsSimulation("06.2010")
                    .getPaiementBusinessList();

            // vérification du nombre d'enregistrement
            assertEquals(10, col.size());

            // vérification des montants
            BigDecimal totalCredit = new BigDecimal("0");
            BigDecimal totalDebit = new BigDecimal("0");
            BigDecimal totalOrdres = new BigDecimal("0");
            BigDecimal totalSoldeInitial = new BigDecimal("0");

            Iterator it = col.iterator();
            while (it.hasNext()) {
                PaiementBusinessModel paiement = (PaiementBusinessModel) it.next();

                totalCredit = totalCredit.add(paiement.getCredit());
                totalDebit = totalDebit.add(paiement.getDebit());
                totalOrdres = totalOrdres.add(paiement.getOrdreVersement());
                totalSoldeInitial = totalSoldeInitial.add(paiement.getSoldeInitial());
            }

            assertEquals(new BigDecimal("1000"), totalSoldeInitial);
            assertEquals(new BigDecimal("1000"), totalCredit);
            assertEquals(new BigDecimal("1000"), totalDebit);
            assertEquals(new BigDecimal("1000"), totalOrdres);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#preparerPaiementDirect(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testPreparerPaiementDirect() {
        try {
            String periode = "06.2010";

            ALImplServiceLocator.getPaiementDirectService().preparerPaiementDirect(periode);

            PaiementPrestationComplexSearchModel search = new PaiementPrestationComplexSearchModel();
            search.setForPeriodeA(periode);
            search.setForEtat(ALCSPrestation.ETAT_SA);
            HashSet setBoni = new HashSet();
            setBoni.add(ALCSPrestation.BONI_DIRECT);
            setBoni.add(ALCSPrestation.BONI_RESTITUTION);
            search.setInBonifications(setBoni);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = (PaiementPrestationComplexSearchModel) JadePersistenceManager.search(search);

            assertEquals(0, search.getSearchResults().length);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.paiement.PaiementDirectServiceImpl#verserPrestations(java.lang.String, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger)}
     * .
     */
    @Ignore
    @Test
    public void testVerserPrestations() {
        try {
            // TODO à compléter

            // cas 1: versement allocataire, compte existant
            // dossier 31488, prestation 12.2011 ,alloc tiers = 152975,756.0047.8325.81

            // cas 2 : versement allocataire, pas de compte annexe existant
            // dossier 58, prestation 12.2011 , alloc tiers = 165732,756.0730.2701.40

            // cas 3 : versement tiers, compte annexe existant
            // dossier 31623, prestation 12.2011 , alloc tiers = 163689,756.8244.6276.58

            // cas 4 : versement tiers, pas de compte annexe existant
            // dossier 32403, prestation 12.2011 alloc tiers = 189212,756.5079.4602.52

            CompteAnnexeSimpleModel compte1 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                    "152975", IntRole.ROLE_AF, "756.0047.8325.81", false);
            CompteAnnexeSimpleModel compte2 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                    "165732", IntRole.ROLE_AF, "756.0730.2701.40", false);
            CompteAnnexeSimpleModel compte3 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                    "163689", IntRole.ROLE_AF, "756.8244.6276.58", false);
            CompteAnnexeSimpleModel compte4 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                    "189212", IntRole.ROLE_AF, "756.5079.4602.52", false);

            // Vérification des comptes avant versement
            assertEquals("152975", compte1.getIdTiers());
            assertEquals(null, compte2.getIdTiers());
            assertEquals("163689", compte3.getIdTiers());
            assertEquals(null, compte4.getIdTiers());

            CompabilisationPrestationContainer containerPaiement = ALImplServiceLocator.getPaiementDirectService()
                    .verserPrestationsByNumProcessus("106", "01.12.2011", new ProtocoleLogger());

            // CABusinessServiceLocator.getJournalService().containerPaiement.getIdJournal()

            compte1 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, "152975",
                    IntRole.ROLE_AF, "756.0047.8325.81", false);
            compte2 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, "165732",
                    IntRole.ROLE_AF, "756.0730.2701.40", false);
            compte3 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, "163689",
                    IntRole.ROLE_AF, "756.8244.6276.58", false);
            compte4 = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, "189212",
                    IntRole.ROLE_AF, "756.5079.4602.52", false);

            // Vérification des comptes créés
            assertEquals("152975", compte1.getIdTiers());
            assertEquals("165732", compte2.getIdTiers());
            assertEquals("163689", compte3.getIdTiers());
            assertEquals("189212", compte4.getIdTiers());

            // Vérification ordre de versements créé pour compte 1
            CAOperationOrdreVersementManager searchOp2 = new CAOperationOrdreVersementManager();
            searchOp2.setForIdCompteAnnexe(compte1.getIdCompteAnnexe());
            searchOp2.setForIdJournal(containerPaiement.getIdJournal());

            searchOp2.find();

            for (int i = 0; i < searchOp2.getSize(); i++) {
                CAOperationOrdreVersement currentOp = (CAOperationOrdreVersement) searchOp2.getEntity(i);
                assertEquals("300.00", currentOp.getMontant());
                assertEquals("152975", currentOp.getAdressePaiement().getIdTiers());
            }

            // Vérification ordre de versements créé pour compte 2
            searchOp2.setForIdCompteAnnexe(compte2.getIdCompteAnnexe());
            searchOp2.setForIdJournal(containerPaiement.getIdJournal());

            searchOp2.find();

            for (int i = 0; i < searchOp2.getSize(); i++) {
                CAOperationOrdreVersement currentOp = (CAOperationOrdreVersement) searchOp2.getEntity(i);
                assertEquals("500.00", currentOp.getMontant());
                assertEquals("165732", currentOp.getAdressePaiement().getIdTiers());
            }

            // Vérification ordre de versements créé pour compte 3
            searchOp2.setForIdCompteAnnexe(compte3.getIdCompteAnnexe());
            searchOp2.setForIdJournal(containerPaiement.getIdJournal());

            searchOp2.find();

            for (int i = 0; i < searchOp2.getSize(); i++) {
                CAOperationOrdreVersement currentOp = (CAOperationOrdreVersement) searchOp2.getEntity(i);
                assertEquals("600.00", currentOp.getMontant());
                assertEquals("118256", currentOp.getAdressePaiement().getIdTiers());
            }

            // Vérification ordre de versements créé pour compte 4
            searchOp2.setForIdCompteAnnexe(compte4.getIdCompteAnnexe());
            searchOp2.setForIdJournal(containerPaiement.getIdJournal());

            searchOp2.find();

            for (int i = 0; i < searchOp2.getSize(); i++) {
                CAOperationOrdreVersement currentOp = (CAOperationOrdreVersement) searchOp2.getEntity(i);
                assertEquals("250.00", currentOp.getMontant());
                assertEquals("133874", currentOp.getAdressePaiement().getIdTiers());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
