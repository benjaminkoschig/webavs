package ch.globaz.al.test.businessimpl.service.models.prestation;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author PTA
 * 
 */
public class EntetePrestationModelServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.EntetePrestationModelServiceImpl#create(ch.globaz.al.business.models.prestation.EntetePrestationModel)}
     */
    @Ignore
    @Test
    public void testCreate() {
        try {
            EntetePrestationModel enTetePrestaModel = new EntetePrestationModel();
            /*
             * MANDATORY
             */

            ALImplServiceLocator.getEntetePrestationModelService().create(enTetePrestaModel);
            // encore aucun ajout donc 14 messages d'erreur le(s) élément(s)
            // obligatoires

            assertEquals(14, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // DATABASEINTEGRITY
            enTetePrestaModel.setTypeGeneration("-5");
            enTetePrestaModel.setTauxVersement("-10");
            enTetePrestaModel.setIdDossier("-1");
            enTetePrestaModel.setCantonAffilie("-1");
            enTetePrestaModel.setIdRecap("-1");
            enTetePrestaModel.setIdTiersCaisseAF("-1");
            enTetePrestaModel.setEtatPrestation("-1");
            enTetePrestaModel.setUnite("-1");
            enTetePrestaModel.setNombreUnite("-2");
            enTetePrestaModel.setNombreEnfants("-2.5");
            enTetePrestaModel.setNumPsgGeneration("-1");
            enTetePrestaModel.setMontantTotal("sept");
            enTetePrestaModel.setBonification("-2");
            enTetePrestaModel.setStatut("-3");
            enTetePrestaModel.setDateVersComp("3janvier2009");
            enTetePrestaModel.setPeriodeDe("13.2009");
            enTetePrestaModel.setPeriodeA("14.2009");
            enTetePrestaModel.setJourDebutMut("-2");
            enTetePrestaModel.setJourFinMut("-3");
            enTetePrestaModel.setMoisDebutMut("-2");
            enTetePrestaModel.setMoisFinMut("-14");
            enTetePrestaModel.setIdCotisation("un");

            ALImplServiceLocator.getEntetePrestationModelService().create(enTetePrestaModel);

            assertEquals(22, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // ajout de données en respectant l'obligation et l'intégrité des
            // données avec aux codes systémes erronés
            enTetePrestaModel.setIdDossier("1");
            enTetePrestaModel.setIdRecap("1");
            enTetePrestaModel.setIdTiersCaisseAF("2");
            enTetePrestaModel.setUnite("61000127");
            enTetePrestaModel.setNombreUnite("4");
            enTetePrestaModel.setNombreEnfants("2");
            enTetePrestaModel.setNumPsgGeneration("34");
            enTetePrestaModel.setMontantTotal("45.2");
            enTetePrestaModel.setTauxVersement("10");
            enTetePrestaModel.setPeriodeDe("12.2009");
            enTetePrestaModel.setPeriodeA("11.2009");
            enTetePrestaModel.setJourDebutMut("01");
            enTetePrestaModel.setJourFinMut("31");
            enTetePrestaModel.setMoisDebutMut("12");
            enTetePrestaModel.setMoisFinMut("10");
            enTetePrestaModel.setDateVersComp("12.12.2009");
            enTetePrestaModel.setIdCotisation("1");

            // CODESYSTEMINTEGRITY
            enTetePrestaModel.setCantonAffilie("1");
            enTetePrestaModel.setUnite("1");
            enTetePrestaModel.setBonification("123");
            enTetePrestaModel.setStatut("22222");
            enTetePrestaModel.setTypeGeneration("61000078");
            enTetePrestaModel.setEtatPrestation("60000020");

            ALImplServiceLocator.getEntetePrestationModelService().create(enTetePrestaModel);

            assertEquals(6, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // code système correct
            enTetePrestaModel.setCantonAffilie(ALCSCantons.JU);
            enTetePrestaModel.setUnite(ALCSDossier.UNITE_CALCUL_MOIS);
            enTetePrestaModel.setBonification(ALCSPrestation.BONI_INDIRECT);
            enTetePrestaModel.setStatut(ALCSPrestation.STATUT_CH);
            enTetePrestaModel.setTypeGeneration(ALCSPrestation.GENERATION_TYPE_GEN_AFFILIE);
            enTetePrestaModel.setEtatPrestation(ALCSPrestation.ETAT_SA);

            // BUSINESSINTEGRITY
            enTetePrestaModel.setIdDossier("3");
            enTetePrestaModel.setIdRecap("1");
            enTetePrestaModel.setEtatPrestation(ALCSPrestation.ETAT_PR);
            enTetePrestaModel.setUnite(ALCSDossier.UNITE_CALCUL_HEURE);

            ALImplServiceLocator.getEntetePrestationModelService().create(enTetePrestaModel);

            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            enTetePrestaModel.setIdDossier("30856");
            enTetePrestaModel.setIdRecap("68354");
            enTetePrestaModel.setIdTiersCaisseAF("1");
            enTetePrestaModel.setPeriodeDe("11.2008");
            enTetePrestaModel.setPeriodeA("12.2008");

            enTetePrestaModel.setEtatPrestation(ALCSPrestation.ETAT_CO);
            enTetePrestaModel.setDateVersComp(null);
            enTetePrestaModel.setIdJournal(null);
            enTetePrestaModel.setIdPassage(null);
            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            // tout les éléments sont valide
            enTetePrestaModel.setEtatPrestation(ALCSPrestation.ETAT_CO);
            enTetePrestaModel.setDateVersComp("01.06.2010");
            enTetePrestaModel.setIdJournal("10");

            ALImplServiceLocator.getEntetePrestationModelService().create(enTetePrestaModel);
            // 0 message d'erreur, en réalité un message d'erreur sur
            // l'identifiant cat. Tarif, table en cours de modification (PTA
            // 15.04.09)
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.EntetePrestationModelServiceImpl#delete(ch.globaz.al.business.models.prestation.EntetePrestationModel)}
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // lecture de l'id EntetePrestation à modifier
            EntetePrestationModel enTetePrestaModel = ALImplServiceLocator.getEntetePrestationModelService().read(
                    "353725");
            ALImplServiceLocator.getEntetePrestationModelService().delete(enTetePrestaModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.EntetePrestationModelServiceImpl#read(String)}
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            ALImplServiceLocator.getEntetePrestationModelService().read("61170003");

            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.EntetePrestationModelServiceImpl#update(ch.globaz.al.business.models.prestation.EntetePrestationModel)}
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement à modifier par l'identifiant
            EntetePrestationModel enTetePrestaModel = ALImplServiceLocator.getEntetePrestationModelService().read(
                    "353725");
            // modification à faire
            enTetePrestaModel.setEtatPrestation(ALCSPrestation.ETAT_CO);

            // mise à jour de l'enregistrement
            ALImplServiceLocator.getEntetePrestationModelService().update(enTetePrestaModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
