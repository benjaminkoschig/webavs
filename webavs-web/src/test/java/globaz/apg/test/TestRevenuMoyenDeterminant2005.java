/*
 * Créé le 9 août 05
 */
package globaz.apg.test;

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
import globaz.prestation.api.IPRSituationProfessionnelle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class TestRevenuMoyenDeterminant2005 /* extends TestCase */{

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static Map correspondanceSalaireMensuelRMD2005 = new HashMap();
    private static Map correspondanceSalaire4SemainesRMD2005 = new HashMap();
    private static Map correspondanceSalaireAnnuelRMD2005 = new HashMap();
    private static Map correspondanceSalaireHorairePour10HeuresRMD2005 = new HashMap();
    private static BSession session = null;

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        correspondanceSalaireMensuelRMD2005.put("3180", "106");
        correspondanceSalaireMensuelRMD2005.put("3180.01", "107");
        correspondanceSalaireMensuelRMD2005.put("3720", "124");
        correspondanceSalaireMensuelRMD2005.put("3749.99", "125");

        correspondanceSalaire4SemainesRMD2005.put("2968", "106");
        correspondanceSalaire4SemainesRMD2005.put("2968.01", "107");
        correspondanceSalaire4SemainesRMD2005.put("3472", "124");
        correspondanceSalaire4SemainesRMD2005.put("3499.99", "125");

        correspondanceSalaireAnnuelRMD2005.put("38160", "106");
        correspondanceSalaireAnnuelRMD2005.put("38160.01", "107");
        correspondanceSalaireAnnuelRMD2005.put("44640", "124");
        correspondanceSalaireAnnuelRMD2005.put("44999.99", "125");

        correspondanceSalaireHorairePour10HeuresRMD2005.put("74.2", "106");
        correspondanceSalaireHorairePour10HeuresRMD2005.put("74.21", "107");
        correspondanceSalaireHorairePour10HeuresRMD2005.put("86.8", "124");
        correspondanceSalaireHorairePour10HeuresRMD2005.put("87.49", "125");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * exactitude entre le salaire mensuel et le revenu moyen determinant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testRMDSalaire4Semaines() throws Exception {
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
            situationProfessionnelle.setAutreSalaire("1");
            situationProfessionnelle.setPeriodiciteAutreSalaire(IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES);
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que le RMD correspond aux tables
            Set salairesMensuels = correspondanceSalaire4SemainesRMD2005.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setAutreSalaire(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                // assertTrue(prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                // assertTrue("RMD calculé : " + prestation.getRevenuMoyenDeterminant() + " ____ RMD attendu : "
                // + correspondanceSalaire4SemainesRMD2005.get(salaire),
                // Double.parseDouble((String) correspondanceSalaire4SemainesRMD2005.get(salaire)) == Double
                // .parseDouble(prestation.getRevenuMoyenDeterminant()));
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
     * exactitude entre le salaire annuel et le revenu moyen determinant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testRMDSalaireAnnuel() throws Exception {
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
            situationProfessionnelle.setAutreSalaire("1");
            situationProfessionnelle.setPeriodiciteAutreSalaire(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que le RMD correspond aux tables
            Set salairesMensuels = correspondanceSalaireAnnuelRMD2005.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setAutreSalaire(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                // assertTrue(prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                // assertTrue("RMD calculé : " + prestation.getRevenuMoyenDeterminant() + " ____ RMD attendu : "
                // + correspondanceSalaireAnnuelRMD2005.get(salaire) + " (Salaire = " + salaire + ")",
                // Double.parseDouble((String) correspondanceSalaireAnnuelRMD2005.get(salaire)) == Double
                // .parseDouble(prestation.getRevenuMoyenDeterminant()));
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
     * exactitude entre le salaire hebdo et le revenu moyen determinant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testRMDSalaireHebdo() throws Exception {
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
            situationProfessionnelle.setSalaireHoraire("1");
            situationProfessionnelle.setHeuresSemaine("10");

            situationProfessionnelle.setPeriodiciteAutreSalaire(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que le RMD correspond aux tables
            Set salairesMensuels = correspondanceSalaireHorairePour10HeuresRMD2005.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireHoraire(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs =APBasesCalculBuilder.of(session, droitAPG).createBasesCalcul();
                calculateur.genererPrestations(session, droitAPG,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitAPG.getIdDroit());
                prestationManager.find(transaction);

                // assertTrue(prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                // assertTrue(
                // "RMD calculé : " + prestation.getRevenuMoyenDeterminant() + " ____ RMD attendu : "
                // + correspondanceSalaireHorairePour10HeuresRMD2005.get(salaire) + " (Salaire = "
                // + salaire + ")",
                // Double.parseDouble((String) correspondanceSalaireHorairePour10HeuresRMD2005.get(salaire)) == Double
                // .parseDouble(prestation.getRevenuMoyenDeterminant()));
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
     * exactitude entre le salaire mensuel et le revenu moyen determinant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testRMDSalaireMensuel() throws Exception {
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

            // vérifie que le RMD correspond aux tables
            Set salairesMensuels = correspondanceSalaireMensuelRMD2005.keySet();
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

                // assertTrue(prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                // assertTrue(Double.parseDouble((String) correspondanceSalaireMensuelRMD2005.get(salaire)) == Double
                // .parseDouble(prestation.getRevenuMoyenDeterminant()));
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
}
