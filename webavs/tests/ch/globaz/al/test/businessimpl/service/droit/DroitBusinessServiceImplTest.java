package ch.globaz.al.test.businessimpl.service.droit;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.ctrlexport.PrestationExportableController;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * * Classe permettant d'effectuer des tests sur les méthodes utilisées dans le
 * services DroitBusiness
 * 
 * @author PTA/JTS
 * 
 */
public class DroitBusinessServiceImplTest {

    /**
     * identifiant du dossier
     */
    private String idDossier = "14587";
    /**
     * identifiant du droit
     */
    private String idDroit = null;

    /**
     * Test sur la méthode de contrôle d'exportabilité du droit
     * 
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * 
     */
    @Ignore
    @Test
    public void testCtrlDroitExportabilite() throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        try {
            // allocataire CH, enfant vivant : FR
            // this.idDossier= "16885";
            idDroit = "70594";
            boolean result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(true, result);
            Assert.assertEquals(1, PrestationExportableController.getUsedRule().getNum());

            // allocataire CH, enfant vivant : MU
            // this.idDossier= "26574";
            idDroit = "84588";
            result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(false, result);
            Assert.assertEquals(3, PrestationExportableController.getUsedRule().getNum());

            // allocataire PT, enfant vivant : PT
            // this.idDossier= "10120";
            idDroit = "64462";
            result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(true, result);
            Assert.assertEquals(6, PrestationExportableController.getUsedRule().getNum());

            // allocataire HR, enfant vivant : CH
            // this.idDossier= "10670";
            idDroit = "64841";
            result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(true, result);
            Assert.assertEquals(8, PrestationExportableController.getUsedRule().getNum());

            // allocataire BA, enfant vivant : CH
            // this.idDossier= "13101";
            idDroit = "66734";
            result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(true, result);
            Assert.assertEquals(12, PrestationExportableController.getUsedRule().getNum());

            // allocataire CM, enfant vivant : MN
            // this.idDossier= "29481";
            idDroit = "89814";
            result = ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(
                    ALServiceLocator.getDroitComplexModelService().read(idDroit), "01.06.2010");

            Assert.assertEquals(false, result);
            Assert.assertEquals(15, PrestationExportableController.getUsedRule().getNum());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        }

    }

    /**
     * test sur la méthode définition du début du droit
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    @Ignore
    @Test
    public void testDefineDebutDroit() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        try {

            idDroit = "14124";
            DroitComplexModel droitComplex = ALServiceLocator.getDroitComplexModelService().read(idDroit);
            String debutActivite = null;

            debutActivite = "01.05.2004";
            droitComplex = ALServiceLocator.getDroitBusinessService().defineDebutDroit(droitComplex, debutActivite);

            Assert.assertEquals("01.05.2004", droitComplex.getDroitModel().getDebutDroit());

            // type droit de formation faisant suite au droit enfant précédent
            droitComplex.getDroitModel().setTypeDroit(ALCSDroit.TYPE_FORM);

            droitComplex = ALServiceLocator.getDroitBusinessService().defineDebutDroit(droitComplex, debutActivite);

            Assert.assertEquals("01.01.2009", droitComplex.getDroitModel().getDebutDroit());

            // type de droit enfant avec date de naissance postérieure à la dat
            // du début activite

            idDroit = "14129";

            droitComplex = ALServiceLocator.getDroitComplexModelService().read(idDroit);

            droitComplex = ALServiceLocator.getDroitBusinessService().defineDebutDroit(droitComplex, debutActivite);

            Assert.assertEquals("01.04.2005", droitComplex.getDroitModel().getDebutDroit());

            // type de droit ménage
            DroitComplexModel droitComplexMenage = new DroitComplexModel();
            droitComplexMenage.getDroitModel().setTypeDroit(ALCSDroit.TYPE_MEN);

            droitComplexMenage = ALServiceLocator.getDroitBusinessService().defineDebutDroit(droitComplexMenage,
                    debutActivite);

            Assert.assertEquals("01.05.2004", droitComplexMenage.getDroitModel().getDebutDroit());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        }

    }

    /**
     * test sur la méthode qui ne permet pas d'allouer une allocation de
     * formation pour un enfant de plus de 25 ans
     */
    @Ignore
    @Test
    public void testDroitForm() {
        DroitComplexModel droitComplex = new DroitComplexModel();
        try {

            // lecture d'un droit type formation
            droitComplex = ALServiceLocator.getDroitComplexModelService().read("62720");

            // modification de la date de naissance, enfant a plus de 25 ans
            droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                    .setDateNaissance("01.01.1970");

            droitComplex = ALServiceLocator.getDroitComplexModelService().update(droitComplex);

            // Permet de contrôler qu'il n'y a un message d'erreur
            Assert.assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // remise de la bonne date de naissance
            droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                    .setDateNaissance("07.02.1987");

            droitComplex = ALServiceLocator.getDroitComplexModelService().update(droitComplex);

            Assert.assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        }
    }

    /**
     * test sur la méthode idDroitActif
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */

    @Ignore
    @Test
    public void testIsDroitActif() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        boolean isActif;

        DroitModel droitModelActif = new DroitModel();

        try {
            // contrôle pour un droit non actif
            droitModelActif = ALImplServiceLocator.getDroitModelService().read("14124");

            isActif = ALServiceLocator.getDroitBusinessService().isDroitActif(droitModelActif, "05.02.2010");
            Assert.assertEquals(false, isActif);
            // contrôle pour un droit actif

            droitModelActif = ALImplServiceLocator.getDroitModelService().read("41166");
            isActif = ALServiceLocator.getDroitBusinessService().isDroitActif(droitModelActif, "05.02.2010");
            Assert.assertEquals(true, isActif);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.business.services.models.droit.DroitBusinessService#isFormationAnticipee(DroitComplexModel)}
     */
    @Ignore
    @Test
    public void testIsFormationAnticipee() {

        try {

            // Droit ENF
            Assert.assertEquals(
                    false,
                    ALServiceLocator.getDroitBusinessService().isFormationAnticipee(
                            ALServiceLocator.getDroitComplexModelService().read("21600")));

            // Droit FORM
            Assert.assertEquals(
                    false,
                    ALServiceLocator.getDroitBusinessService().isFormationAnticipee(
                            ALServiceLocator.getDroitComplexModelService().read("21605")));

            // Droit FORM anticipée
            Assert.assertEquals(
                    true,
                    ALServiceLocator.getDroitBusinessService().isFormationAnticipee(
                            ALServiceLocator.getDroitComplexModelService().read("21606")));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * test sur la méthode d'ajout d'un droit le cas échéant (ajout d'un droit
     * supplémentaire lorsqu'il y a changement de tarif pour un même droit)
     */
    @Ignore
    @Test
    public void testNewDroit() {
        DroitComplexModel droitComplex = new DroitComplexModel();
        int nbreDroitDossier;
        idDossier = "14587";
        // DroitComplexSearchModel droitComplexSearchModel = new
        // DroitComplexSearchModel();
        idDroit = "7536";

        try {
            // recherche d'un droitComplex
            DroitComplexSearchModel searchDroitComplex = new DroitComplexSearchModel();
            searchDroitComplex.setForIdDroit(idDroit);
            // modification

            searchDroitComplex = ALServiceLocator.getDroitComplexModelService().search(searchDroitComplex);

            droitComplex = (DroitComplexModel) searchDroitComplex.getSearchResults()[0];
            // sans modification du tarif (reste à JU), donc un seul tarif
            // enfant
            ALServiceLocator.getDroitBusinessService().ajoutDroitMemeType(droitComplex);

            // recherche du nombre de droits pour le dossier
            searchDroitComplex.setForIdDroit(null);
            searchDroitComplex.setForIdDossier(idDossier);
            searchDroitComplex = ALServiceLocator.getDroitComplexModelService().search(searchDroitComplex);

            // nombre de droi lié à ce dossier
            nbreDroitDossier = 4;
            // aucun nouveau droit, le nombre de droit reste à 4 pour ce dossier
            assertEquals(nbreDroitDossier, searchDroitComplex.getSize());

            JadeThread.logClear();

            // modification du tarif force à LU pour ajouter un nouveau droit
            // effectif
            droitComplex.getDroitModel().setTarifForce("61190012");
            ALServiceLocator.getDroitBusinessService().ajoutDroitMemeType(droitComplex);
            // rechercher le nouveau droit créé
            searchDroitComplex.setForIdDroit(null);
            searchDroitComplex.setForIdDossier("14587");
            searchDroitComplex = ALServiceLocator.getDroitComplexModelService().search(searchDroitComplex);
            nbreDroitDossier = 5;
            // le nombre de droit est à 5 (ajout automatique d'un droit enfant
            // pour Lucerne, car changement de tarif)
            assertEquals(nbreDroitDossier, searchDroitComplex.getSize());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }
}
