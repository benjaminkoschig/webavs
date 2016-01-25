/*
 * Créé le 4 août 05
 */
package globaz.apg.test;

import static org.junit.Assert.*;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationFamilialeMat;
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
public class TestCalculPrestationMaternite {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;
    private static Map correspondanceAllocJournaliereSalaireMensuel = new HashMap();

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        correspondanceAllocJournaliereSalaireMensuel.put("2000", "53.60");
        correspondanceAllocJournaliereSalaireMensuel.put("3630", "96.80");
        correspondanceAllocJournaliereSalaireMensuel.put("4019.99", "107.20");
        correspondanceAllocJournaliereSalaireMensuel.put("4020.01", "108.00");
        correspondanceAllocJournaliereSalaireMensuel.put("6500", "172.00");
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
        APDroitMaternite droitMaternite = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            droitMaternite = new APDroitMaternite();
            droitMaternite.wantCallValidate(false);
            droitMaternite.add(transaction);
            droitMaternite.wantCallValidate(true);

            APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();
            situationFamilialeMat.setSession(session);
            situationFamilialeMat.setIdDroitMaternite(droitMaternite.getIdDroit());
            situationFamilialeMat.setNom("truc");
            situationFamilialeMat.setPrenom("machin");
            situationFamilialeMat.setNoAVS("111.11.111.121");
            situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
            situationFamilialeMat.add(transaction);

            droitMaternite.setDateDebutDroit("01.07.2005");
            droitMaternite.setDateDepot("01.07.2005");
            droitMaternite.setDateReception("01.07.2005");
            droitMaternite.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitMaternite.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            droitMaternite.setNpa("1008");
            droitMaternite.setPays("100");
            droitMaternite.setIdDemande("8");
            droitMaternite.setSession(session);

            droitMaternite.update(transaction);

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
            situationProfessionnelle.setIdDroit(droitMaternite.getIdDroit());
            situationProfessionnelle.add(transaction);

            APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
            List baseCalculs = new APBasesCalculBuilder(session, droitMaternite).createBasesCalcul();
            calculateur.genererPrestations(session, droitMaternite, new FWCurrency(0), baseCalculs);

            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdDroit(droitMaternite.getIdDroit());
            prestationManager.find(transaction);

            assertTrue(prestationManager.size() == 4);

            APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

            assertTrue("dates trouvées : du " + prestation.getDateDebut() + " au " + prestation.getDateFin()
                    + " ___ dates attendues : du 01.07.2005 au 31.07.2005",
                    prestation.getDateDebut().equals("01.07.2005") && prestation.getDateFin().equals("31.07.2005"));

            prestation = (APPrestation) prestationManager.getEntity(3);

            assertTrue("dates trouvées : du " + prestation.getDateDebut() + " au " + prestation.getDateFin()
                    + " ___ dates attendues : du 01.10.2005 au 06.10.2005",
                    prestation.getDateDebut().equals("01.10.2005") && prestation.getDateFin().equals("06.10.2005"));
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
    public void testPrestationJournaliere() throws Exception {
        BTransaction transaction = null;
        APDroitMaternite droitMaternite = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            droitMaternite = new APDroitMaternite();
            droitMaternite.wantCallValidate(false);
            droitMaternite.add(transaction);
            droitMaternite.wantCallValidate(true);

            APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();
            situationFamilialeMat.setSession(session);
            situationFamilialeMat.setIdDroitMaternite(droitMaternite.getIdDroit());
            situationFamilialeMat.setNom("truc");
            situationFamilialeMat.setPrenom("machin");
            situationFamilialeMat.setNoAVS("111.11.111.121");
            situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
            situationFamilialeMat.add(transaction);

            droitMaternite.setDateDebutDroit("01.07.2005");
            droitMaternite.setDateDepot("01.07.2005");
            droitMaternite.setDateReception("01.07.2005");
            droitMaternite.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            droitMaternite.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            droitMaternite.setNpa("1008");
            droitMaternite.setPays("100");
            droitMaternite.setIdDemande("8");
            droitMaternite.setSession(session);

            droitMaternite.update(transaction);

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
            situationProfessionnelle.setIdDroit(droitMaternite.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                situationProfessionnelle.setSalaireMensuel(salaire);
                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List baseCalculs = new APBasesCalculBuilder(session, droitMaternite).createBasesCalcul();
                calculateur.genererPrestations(session, droitMaternite, new FWCurrency(0), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droitMaternite.getIdDroit());
                prestationManager.find(transaction);

                assertTrue(prestationManager.size() == 4);

                for (int i = 0; i < 4; i++) {
                    APPrestation prestation = (APPrestation) prestationManager.getEntity(i);

                    assertTrue("montant journalier trouvé : " + prestation.getMontantJournalier()
                            + " ___ Montant attendu : " + correspondanceAllocJournaliereSalaireMensuel.get(salaire)
                            + " (salaireMensuel = " + salaire + ")", Double
                            .parseDouble((String) correspondanceAllocJournaliereSalaireMensuel.get(salaire)) == Double
                            .parseDouble(prestation.getMontantJournalier()));
                }
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
