/*
 * Créé le 12 août 05
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
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class TestDroitAcquis /* extends TestCase */{

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;

    private static Map correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal = new HashMap();

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3180.01", "85.60");
        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3720", "99.20");
        correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.put("3749.99", "100.00");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
            situationProfessionnelle.setSalaireMensuel("3180");
            situationProfessionnelle.setIdDroit(droitAPG.getIdDroit());
            situationProfessionnelle.add(transaction);

            // vérifie que la prestation journaliere correspond aux tables
            Set salairesMensuels = correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.keySet();
            Iterator iterator = salairesMensuels.iterator();

            while (iterator.hasNext()) {
                String salaire = (String) iterator.next();
                String droitAcquis = (String) correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal
                        .get(salaire);
                droitAPG.setDroitAcquis(droitAcquis);

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

                // assertTrue("montant journalier trouvé : " + prestation.getMontantJournalier()
                // + " ___ Montant attendu : "
                // + correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal.get("3180"),
                // 84.8 == Double.parseDouble(prestation.getMontantJournalier()));
                //
                // assertTrue(
                // "(droitAcquis dans la prestation = "
                // + prestation.getDroitAcquis()
                // + ")   ___   montant brut attendu = "
                // + (new BigDecimal(
                // (String) correspondanceAllocJournaliereSalaireMensuel2005SansEnfantServiceNormal
                // .get(salaire)).multiply(new BigDecimal(31)))
                // + " montant brut trouvé : " + prestation.getMontantBrut(),
                // new BigDecimal(prestation.getMontantBrut()).compareTo((new BigDecimal(prestation
                // .getDroitAcquis()).multiply(new BigDecimal(31)))) == 0);
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
