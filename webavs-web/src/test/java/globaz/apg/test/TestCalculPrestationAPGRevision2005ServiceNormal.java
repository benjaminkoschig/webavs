/*
 * Créé le 4 août 05
 */
package globaz.apg.test;

import static org.junit.Assert.*;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <H1>Tests Simple sur une période de 1 mois (du 01.07.2005 au 31.07.2005)</H1>
 * 
 * @author dvh
 */
public class TestCalculPrestationAPGRevision2005ServiceNormal {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;

    private static Map correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal = new HashMap();
    private static Map correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal = new HashMap();
    private static Map correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal = new HashMap();
    private static Map correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal = new HashMap();

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3180", "84.80");
        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3180.01", "85.60");
        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3720", "99.20");
        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3749.99", "100.00");

        correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.put("3180", "102.80");
        correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.put("3180.01", "103.60");
        correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.put("3720", "117.20");
        correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.put("3749.99", "118.00");

        correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.put("3180", "108.00");
        correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.put("3180.01", "108.00");
        correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.put("3720", "124.00");
        correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.put("3749.99", "125.00");
        correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.put("5430.00", "180.80");

        correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.put("3180", "108.00");
        correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.put("3180.01", "108.00");
        correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.put("3720", "124.00");
        correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.put("3749.99", "125.00");
        correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.put("5430.00", "181.00");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testDatesPrestation() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.setFraisGarde("300");
            situationFamilialeAPG.setNbrEnfantsDebutDroit("1");
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
            List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
            calculateur.genererPrestations(session, droitAPG, new FWCurrency(situationFamilialeAPG.getFraisGarde()),
                    baseCalculs);

            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdDroit(droitAPG.getIdDroit());
            prestationManager.find(transaction);

            assertTrue(prestationManager.size() == 1);

            APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

            assertTrue("dates trouvées : du " + prestation.getDateDebut() + " au " + prestation.getDateFin()
                    + " ___ dates attendues : du 01.07.2005 au 31.07.2005",
                    prestation.getDateDebut().equals("01.07.2005") && prestation.getDateFin().equals("31.07.2005"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testPrestationJournaliereDeuxEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setNbrEnfantsDebutDroit("2");
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                assertTrue(
                        "montant journalier trouvé : " + prestation.getMontantJournalier() + " ___ Montant attendu : "
                                + correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal.get(salaire)
                                + " (salaireMensuel = " + salaire + ")",
                        Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel2005DeuxEnfantServiceNormal
                                .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testPrestationJournaliereQuinzeEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setNbrEnfantsDebutDroit("15");
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal
                    .keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                assertTrue(
                        "montant journalier trouvé : "
                                + prestation.getMontantJournalier()
                                + " ___ Montant attendu : "
                                + correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.get(salaire)
                                + " (salaireMensuel = " + salaire + ")",
                        Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal
                                .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testPrestationJournaliereSansEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                assertTrue(
                        "montant journalier trouvé : " + prestation.getMontantJournalier() + " ___ Montant attendu : "
                                + correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.get(salaire)
                                + " (salaireMensuel = " + salaire + ")",
                        Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal
                                .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testPrestationJournaliereTroisEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setNbrEnfantsDebutDroit("3");
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal
                    .keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                assertTrue(
                        "montant journalier trouvé : "
                                + prestation.getMontantJournalier()
                                + " ___ Montant attendu : "
                                + correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal.get(salaire)
                                + " (salaireMensuel = " + salaire + ")",
                        Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel2005TroisEtPlusEnfantServiceNormal
                                .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testPrestationJournaliereUnEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setNbrEnfantsDebutDroit("1");
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                assertTrue(
                        "montant journalier trouvé : " + prestation.getMontantJournalier() + " ___ Montant attendu : "
                                + correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal.get(salaire)
                                + " (salaireMensuel = " + salaire + ")",
                        Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel2005UnEnfantServiceNormal
                                .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testSimpleTotalAPG() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(session);
            situationFamilialeAPG.setFraisGarde("300");
            situationFamilialeAPG.setNbrEnfantsDebutDroit("1");
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.07.2005");
            periodeAPG.setDateFinPeriode("31.07.2005");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.07.2005");
            droitAPG.setDateDepot("01.07.2005");
            droitAPG.setDateReception("01.07.2005");
            droitAPG.setDateFinDroit("31.07.2005");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);

            droitAPG.update(transaction);

            APEmployeur employeur = new APEmployeur();
            employeur.setIdAffilie("11");
            employeur.setIdTiers("3811");
            employeur.setIdParticularite("0");
            employeur.setSession(session);
            employeur.add(transaction);

            APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(session);
            situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
            situationProfessionnelle.setSalaireMensuel("3000.00");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
            List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
            calculateur.genererPrestations(session, droitAPG, new FWCurrency(situationFamilialeAPG.getFraisGarde()),
                    baseCalculs);

            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdDroit(droitAPG.getIdDroit());
            prestationManager.find(transaction);

            assertTrue(prestationManager.size() == 1);

            APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

            assertTrue("total trouvé : " + prestation.getMontantBrut() + " ___ montant total attendu : " + "3038",
                    Double.parseDouble(prestation.getMontantBrut()) == Double.parseDouble("3038"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                transaction.rollback();
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
        }
    }
}
