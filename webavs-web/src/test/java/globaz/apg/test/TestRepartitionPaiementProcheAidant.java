/*
 * Créé le 9 août 05
 */
package globaz.apg.test;

import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Builder;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class TestRepartitionPaiementProcheAidant {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static BSession session = null;

    private static Map<String, String> procheAidant = new HashMap();

    static {
        try {
            session = (BSession) ((APApplication) GlobazSystem.getApplication("APG")).newSession("ccjuglo", "glob4az");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        procheAidant.put("3760", "1068.95 2691.05");
    }

    @Test
    @Ignore
    public void testSimple() throws Exception {
        BTransaction transaction = null;
        APDroitProcheAidant droit = null;

        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            droit = createDroit(transaction);
            String idDroit = droit.getIdDroit();

            APSituationFamilialeAPG situationFamilialeAPG = createSituationFamiliale("1", transaction);

            APPeriodeAPG periodeAPG = createPeriode("01.01.2021", "20.01.2021", idDroit, transaction) ;

            updateDroit(situationFamilialeAPG.getIdSitFamAPG(),"01.01.2021", "20", droit, transaction);

            APEmployeur employeur = createEmployeur("11", "3811", "0", transaction);
            APEmployeur employeur2 = createEmployeur("12", "3811", "0", transaction);

            APSituationProfessionnelle situationProfessionnelle =
                    createSituationProfessionnelle(employeur.getIdEmployeur(),"5000.00", idDroit, transaction);
            APSituationProfessionnelle situationProfessionnelle2 =
                    createSituationProfessionnelle(employeur.getIdEmployeur(),"2000.00", idDroit, transaction);

            Set<String> salaires = procheAidant.keySet();

            for (String salaire : salaires) {
//                situationProfessionnelle.setSalaireMensuel(salaire);
//                situationProfessionnelle.update(transaction);

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List<APBaseCalcul> baseCalculs = APBasesCalculBuilder.of(session, droit).createBasesCalcul();
                calculateur.genererPrestations(session, droit,
                        new FWCurrency(situationFamilialeAPG.getFraisGarde()), baseCalculs);

                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(idDroit);
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

                String montantAttendu1 = JadeStringUtil.split((String) (procheAidant.get(salaire)))[0];
                String montantAttendu2 = JadeStringUtil.split((String) (procheAidant.get(salaire)))[1];

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

    private APSituationProfessionnelle createSituationProfessionnelle(String idEmployeur, String salaire, String idDroit, BITransaction transaction) throws Exception {
        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
        situationProfessionnelle.setSession(session);
        situationProfessionnelle.setIdEmployeur(idEmployeur);
        situationProfessionnelle.setSalaireMensuel(salaire);
        situationProfessionnelle.setIdDroit(idDroit);
        situationProfessionnelle.add(transaction);
        return situationProfessionnelle;
    }

    private APEmployeur createEmployeur(String idAffilie, String idTiers, String IdParticularite, BTransaction transaction) throws Exception {
        APEmployeur employeur = new APEmployeur();
        employeur.setIdAffilie(idAffilie);
        employeur.setIdTiers(idTiers);
        employeur.setIdParticularite(IdParticularite);
        employeur.setSession(session);
        employeur.add(transaction);
        return employeur;
    }

    private APPeriodeAPG createPeriode(String dateDebut, String dateFin, String idDroit, BTransaction transaction) throws Exception {
        APPeriodeAPG periodeAPG = new APPeriodeAPG();
        periodeAPG.setSession(session);
        periodeAPG.setDateDebutPeriode(dateDebut);
        periodeAPG.setDateFinPeriode(dateFin);
        periodeAPG.setIdDroit(idDroit);
        periodeAPG.add(transaction);
        return periodeAPG;
    }

    private APDroitProcheAidant createDroit(BTransaction transaction) throws Exception {
        APDroitProcheAidant droitAPG = new APDroitProcheAidant();
        droitAPG.wantCallValidate(false);
        droitAPG.add(transaction);
        droitAPG.wantCallValidate(true);
        return droitAPG;
    }

    private void updateDroit(String idSituationFam, String dateDebut, String nbJour, APDroitProcheAidant droit, BTransaction transaction) throws Exception {
        droit.setIdSituationFam(idSituationFam);
        droit.setDateDebutDroit(dateDebut);
        droit.setDateDepot(dateDebut);
        droit.setDateReception(dateDebut);
        droit.setDuplicata(Boolean.FALSE);
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT);
        droit.setNbrJourSoldes(nbJour);
        droit.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande("10");
        droit.setSession(session);
        droit.wantCallValidate(false);
        droit.update(transaction);
    }


    private APSituationFamilialeAPG createSituationFamiliale(String nbEnfant, BTransaction transaction) throws Exception {
        APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
        situationFamilialeAPG.setSession(session);
        // situationFamilialeAPG.setFraisGarde("300");
        situationFamilialeAPG.setNbrEnfantsDebutDroit("1");
        situationFamilialeAPG.add(transaction);
        return situationFamilialeAPG;
    }


}


