/*
 * Créé le 4 août 05
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <H1>Tests Simple sur une période de 1 mois (du 01.01.2001 au 31.01.2001)</H1>
 * 
 * @author dvh
 */
public class TestCalculPrestationAPGRevision99ServiceNormal /* extends TestCase implements Test */{

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;
    private static Map correspondanceAllocJournaliereSalaireMensuel99SansEnfant = new HashMap();

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        correspondanceAllocJournaliereSalaireMensuel99SansEnfant.put("3180", "68.90");
        correspondanceAllocJournaliereSalaireMensuel99SansEnfant.put("3180.01", "69.60");
        correspondanceAllocJournaliereSalaireMensuel99SansEnfant.put("3720", "80.60");
        correspondanceAllocJournaliereSalaireMensuel99SansEnfant.put("3749.99", "81.30");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
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
            periodeAPG.setDateDebutPeriode("01.01.2001");
            periodeAPG.setDateFinPeriode("31.01.2001");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.01.2001");
            droitAPG.setDateDepot("01.01.2001");
            droitAPG.setDateReception("01.01.2001");
            droitAPG.setDateFinDroit("31.01.2001");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_1999);
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

            // assertTrue(prestationManager.size() == 1);

            APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

            // assertTrue("dates trouvées : du " + prestation.getDateDebut() + " au " + prestation.getDateFin()
            // + " ___ dates attendues : du 01.01.2001 au 31.01.2001",
            // prestation.getDateDebut().equals("01.01.2001") && prestation.getDateFin().equals("31.01.2001"));
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
    public void testPrestationJournaliereSansEnfant() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(session);

            // situationFamilialeAPG.setFraisGarde("300");
            // situationFamilialeAPG.setNbrEnfantsDebutDroit("1");
            situationFamilialeAPG.add(transaction);

            droitAPG = new APDroitAPG();
            droitAPG.wantCallValidate(false);
            droitAPG.add(transaction);
            droitAPG.wantCallValidate(true);

            APPeriodeAPG periodeAPG = new APPeriodeAPG();
            periodeAPG.setSession(session);
            periodeAPG.setDateDebutPeriode("01.01.2001");
            periodeAPG.setDateFinPeriode("31.01.2001");
            periodeAPG.setIdDroit(droitAPG.getIdDroit());
            periodeAPG.add(transaction);

            droitAPG.setIdSituationFam(situationFamilialeAPG.getIdSitFamAPG());
            droitAPG.setDateDebutDroit("01.01.2001");
            droitAPG.setDateDepot("01.01.2001");
            droitAPG.setDateReception("01.01.2001");
            droitAPG.setDateFinDroit("31.01.2001");
            droitAPG.setDuplicata(Boolean.FALSE);
            droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitAPG.setGenreService(IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_1999);
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
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel99SansEnfant.keySet();
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

                // assertTrue("plus d'une prestation a été calculée...", prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                // assertTrue(
                // "montant journalier trouvé : " + prestation.getMontantJournalier() + " ___ Montant attendu : "
                // + correspondanceAllocJournaliereSalaireMensuel99SansEnfant.get(salaire)
                // + " (salaireMensuel = " + salaire + ")",
                // Double.parseDouble((String) correspondanceAllocJournaliereSalaireMensuel99SansEnfant
                // .get(salaire)) == Double.parseDouble(prestation.getMontantJournalier()));
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
