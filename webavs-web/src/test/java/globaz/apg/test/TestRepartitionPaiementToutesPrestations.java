/*
 * Créé le 9 août 05
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
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class TestRepartitionPaiementToutesPrestations {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;

    private static Map serviceNormal2005 = new HashMap();

    static {
        try {
            session = TestAll.createSession();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        serviceNormal2005.put("3000", "3100 1240");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Test
    @Ignore
    public void testSimple() throws Exception {
        BTransaction transaction = null;
        APDroitAPG droitAPG = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(session);
            // situationFamilialeAPG.setFraisGarde("300");
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
            droitAPG.setGenreService(IAPDroitLAPG.CS_SERVICE_AVANCEMENT);
            droitAPG.setNbrJourSoldes("31");
            droitAPG.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
            droitAPG.setNpa("1008");
            droitAPG.setPays("100");
            droitAPG.setIdDemande("10");
            droitAPG.setSession(session);
            droitAPG.wantCallValidate(false);
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

            Set salaires = serviceNormal2005.keySet();

            Iterator iterator = salaires.iterator();

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

                assertTrue(prestationManager.size() == 1);

                APPrestation prestation = (APPrestation) prestationManager.getEntity(0);

                APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
                repartitionPaiementsManager.setSession(session);
                repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                repartitionPaiementsManager.find(transaction);

                assertTrue("Il devrait y avoir 2 répartitions, il y en a : " + repartitionPaiementsManager.size(),
                        repartitionPaiementsManager.size() == 2);

                APRepartitionPaiements repartitionPaiements1 = (APRepartitionPaiements) repartitionPaiementsManager
                        .getEntity(0);
                APRepartitionPaiements repartitionPaiements2 = (APRepartitionPaiements) repartitionPaiementsManager
                        .getEntity(1);

                assertTrue("la somme des répartitions de paiements (" + repartitionPaiements1.getMontantBrut() + ", "
                        + repartitionPaiements2.getMontantBrut() + ") n'est pas égal au montant de la prestation ("
                        + prestation.getMontantBrut(),
                        (Double.parseDouble(repartitionPaiements1.getMontantBrut()) + Double
                                .parseDouble(repartitionPaiements2.getMontantBrut())) == Double.parseDouble(prestation
                                .getMontantBrut()));

                String montantAttendu1 = JadeStringUtil.split((String) (serviceNormal2005.get(salaire)))[0];
                String montantAttendu2 = JadeStringUtil.split((String) (serviceNormal2005.get(salaire)))[1];

                assertTrue(
                        "le montant d'une répartition de paiement (" + repartitionPaiements1.getMontantBrut()
                                + ") est différent de la valeur attendue (" + montantAttendu1 + " ou "
                                + montantAttendu2 + ")",
                        (Double.parseDouble(repartitionPaiements1.getMontantBrut()) == Double
                                .parseDouble(montantAttendu1))
                                || (Double.parseDouble(repartitionPaiements1.getMontantBrut()) == Double
                                        .parseDouble(montantAttendu2)));

                assertTrue(
                        "le montant d'une répartition de paiement (" + repartitionPaiements2.getMontantBrut()
                                + ") est différent de la valeur attendue (" + montantAttendu1 + " ou "
                                + montantAttendu2 + ")",
                        (Double.parseDouble(repartitionPaiements2.getMontantBrut()) == Double
                                .parseDouble(montantAttendu1))
                                || (Double.parseDouble(repartitionPaiements2.getMontantBrut()) == Double
                                        .parseDouble(montantAttendu2)));
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
