package ch.globaz.al.test.businessimpl.service.echeances;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe permettant d'effectuer des tests sur les méthodes utilisées dans le
 * services DatesEcheancesServiceImpl
 * 
 * @author PTA
 * 
 */

public class EcheanceValiditesDroitImplTest {
    /**
     * identifiant du dossier
     */
    private String idDossier = "14707";

    /**
     * Test sur la méthode qui calcule l'âge de l'enfant à une date donnée
     * {@link ch.globaz.al.business.services.echeances.DatesEcheanceServiceImpl#getAgeEnfant(String, String)}
     * 
     */
    @Ignore
    @Test
    public void testAgeEnfant() {
        String dateNaissanceEnfant = null;
        String date = null;
        String ageEnfant = null;

        try {
            // test avec une exception, date de naissance non valide
            // dateNaissanceEnfant = "";
            date = "31.01.2008";

            dateNaissanceEnfant = "31.01.2004";

            ageEnfant = ALImplServiceLocator.getDatesEcheancePrivateService().getAgeEnfant(dateNaissanceEnfant, date);
            assertEquals("4", ageEnfant);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }

    }

    /**
     * Test sur la méthode String calculFinValiditeEcheance(String
     * dateDebutValidite, EcheanceComplexModel EcheanceCriter, String
     * dateNaissance)
     */
    @Ignore
    @Test
    public void testCalculFinValiditeEcheance() {

        String dateDebutValidite = null;
        EcheanceComplexModel echeanceCriter = new EcheanceComplexModel();
        String dateNaissance = null;
        try {

            dateDebutValidite = "15.09.2003";
            echeanceCriter.setAgeDebut("8");
            echeanceCriter.setAgeFin("12");
            dateNaissance = "23.10.2000";

            String dateFinValidite = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    dateDebutValidite, echeanceCriter, dateNaissance);

            assertEquals("31.10.2012", dateFinValidite);

            dateNaissance = "23.10.1982";

            dateFinValidite = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    dateDebutValidite, echeanceCriter, dateNaissance);

            assertEquals(null, dateFinValidite);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * Test sur la méthode qui calcule la fin de la validité d'un droit String
     * calculFinValiditeEcheance(DroitComplexModel droitComplex, String
     * dateDebutValidite, ArrayList EcheanceCriter)
     */
    @Ignore
    @Test
    public void testCalculFinValiditeEcheanceDroitComple() {
        try {

            String finValiditeEcheance = null;
            DroitComplexModel droitComplex = new DroitComplexModel();
            String dateDebutValidite = null;
            ArrayList echeanceCriter = new ArrayList();
            EcheanceComplexModel echeance1 = new EcheanceComplexModel();
            EcheanceComplexModel echeance2 = new EcheanceComplexModel();
            EcheanceComplexModel echeance3 = new EcheanceComplexModel();
            echeance1.setAgeDebut("0");
            echeance1.setAgeFin("12");
            echeance2.setAgeDebut("12");
            echeance2.setAgeFin("16");
            echeance3.setAgeDebut("16");
            echeance3.setAgeFin("25");

            echeanceCriter.add(echeance1);
            echeanceCriter.add(echeance2);
            echeanceCriter.add(echeance3);

            dateDebutValidite = "23.06.2001";

            droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                    .setDateNaissance("20.12.1996");

            finValiditeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    droitComplex, dateDebutValidite, echeanceCriter);

            assertEquals("31.12.2008", finValiditeEcheance);

            /**********/
            dateDebutValidite = "23.06.2010";

            finValiditeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    droitComplex, dateDebutValidite, echeanceCriter);

            assertEquals("31.12.2012", finValiditeEcheance);
            /*********/
            dateDebutValidite = "15.05.2018";

            finValiditeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    droitComplex, dateDebutValidite, echeanceCriter);

            assertEquals("31.12.2021", finValiditeEcheance);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * Test sur la méthode qui retourne le début de validité d'un droit à
     * laquelle on passe en paramètre un droit complexe modèle et une date
     * (début Activité ou début dossier)
     * {@link ch.globaz.al.business.services.echeances.DatesEcheanceServiceImpl#getDateDebutValiditeDroit(ch.globaz.al.business.models.droit.DroitComplexModel, String)}
     * 
     */
    @Ignore
    @Test
    public void testDebutValiditeDroit() {
        DroitComplexModel testDroitDebutValidite = new DroitComplexModel();
        DroitComplexSearchModel rechercheDroitEnfant = new DroitComplexSearchModel();
        rechercheDroitEnfant.setWhereKey("echeancesDroit");
        DossierModel dossierModel = new DossierModel();
        DossierSearchModel rechercheDossier = new DossierSearchModel();

        try {
            // test sur le début de validité d'un droit enfant

            // test sur le début de validité d'un droit de formation
            rechercheDossier.setForIdDossier(idDossier);
            ALServiceLocator.getDossierModelService().search(rechercheDossier);
            if (rechercheDossier.getSize() == 1) {
                dossierModel = (DossierModel) rechercheDossier.getSearchResults()[0];

                // recherche d'un droit Foramtion lié à ce dossier
                rechercheDroitEnfant.setForIdDossier(dossierModel.getIdDossier());
                rechercheDroitEnfant.setForTypeDroit(ALCSDroit.TYPE_FORM);
                ALServiceLocator.getDroitComplexModelService().search(rechercheDroitEnfant);

                // si retrouve un droit type Enfant, je prends le premier
                if (rechercheDroitEnfant.getSize() > 1) {

                    testDroitDebutValidite = (DroitComplexModel) rechercheDroitEnfant.getSearchResults()[0];

                    testDroitDebutValidite.getDroitModel().setDebutDroit("");

                    // méthode qui renvoie le début de la validitée du droit
                    String debutDroit = ALServiceLocator.getDatesEcheanceService().getDateDebutValiditeDroit(
                            testDroitDebutValidite, dossierModel.getDebutActivite());

                    System.out.println("Début de Validité: " + testDroitDebutValidite.getDroitModel().getDebutDroit());

                    assertEquals("01.12.2005", debutDroit);
                } else {
                    System.out.println("Aucun droit enfant ne correspond au numero de dossier : " + idDossier);

                }

                // ALServiceLocator.getDroitComplexModelService().search(rechercheDroitEnfant);

                // recherche d'un droit ménage lié à ce dossier

            } else {
                System.out.println("Aucun dossier ne correspond au numero : " + idDossier);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * test sur le début d'un droit en fonction de deux dates (date naissance et
     * date débutActivite)
     */
    @Ignore
    @Test
    public void testDebutValiditeDroitDebutActivite() {

        String debutActivite = null;
        String dateNaissance = null;
        String debutDroit = null;

        try {

            // date de naissance antérieure à date de début activité, date début
            // activité fait référence pour le début de validité du droit

            dateNaissance = "20.12.2005";
            debutActivite = "29.07.2008";
            debutDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(dateNaissance,
                    debutActivite);

            assertEquals(debutActivite, debutDroit);

            // date de naissance postérieure à la date de début validité mais du
            // même
            // mois et même année, début activité est la référence pour le début
            // de
            // validité du droit
            dateNaissance = "20.12.2005";
            debutActivite = "15.12.2005";
            debutDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(dateNaissance,
                    debutActivite);

            assertEquals(debutActivite, debutDroit);

            // dans les autres cas (si date naissance est postérieure à la date
            // du
            // début d'activité , la date de validité correspond au début du
            // mois de
            // la date de naissance

            dateNaissance = "20.12.2005";
            debutActivite = "15.12.2003";
            debutDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(dateNaissance,
                    debutActivite);

            assertEquals("01.12.2005", debutDroit);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * test sur le début de validité d'un droit faisant suite à la fin d'un
     * droit précédent
     */

    @Ignore
    @Test
    public void testDebutValiditeDroitSuiteAUnDroit() {
        try {
            String finDroit = null;

            finDroit = "31.12.2009";

            String debutNouveauDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                    finDroit);
            assertEquals("01.01.2010", debutNouveauDroit);

            finDroit = "28.02.2010";

            debutNouveauDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                    finDroit);
            assertEquals("01.03.2010", debutNouveauDroit);

            finDroit = "29.02.2012";
            debutNouveauDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                    finDroit);
            assertEquals("01.03.2012", debutNouveauDroit);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * test sur la fin validité échéance calculé sur la loi LAFAM seulement
     * 
     */
    @Ignore
    @Test
    public void testFinValiditeDroitCalculeLoiLafam() {
        DroitComplexModel testDroitFinValiditeEcheance = new DroitComplexModel();
        DossierComplexModel dossierComple = new DossierComplexModel();
        DossierSearchModel dossierSearchModel = new DossierSearchModel();
        DossierModel dossierModel = new DossierModel();
        AllocataireComplexSearchModel allocSearc = new AllocataireComplexSearchModel();
        AllocataireComplexModel allocComplex = new AllocataireComplexModel();
        String debutActivite = null;
        DroitComplexSearchModel rechercheDroitEnfant = new DroitComplexSearchModel();
        rechercheDroitEnfant.setWhereKey("echeancesDroit");
        String dateFinEcheance = null;

        try {
            // recherche d'un dossier
            dossierSearchModel.setForIdDossier("30371");
            ALServiceLocator.getDossierModelService().search(dossierSearchModel);
            if (dossierSearchModel.getSize() == 1) {
                dossierModel = (DossierModel) dossierSearchModel.getSearchResults()[0];

                dossierComple.setDossierModel(dossierModel);

                allocSearc.setForIdTiers("127067");

                allocSearc = ALServiceLocator.getAllocataireComplexModelService().search(allocSearc);

                if (allocSearc.getSize() == 1) {
                    allocComplex = (AllocataireComplexModel) allocSearc.getSearchResults()[0];

                    dossierComple.setAllocataireComplexModel(allocComplex);
                }

                // recherche d'un droit Enfant lié à ce dossier

                rechercheDroitEnfant.setForIdDossier(dossierModel.getIdDossier());
                // rechercheDroitEnfant.setForTypeDroit(ALCSDroit.TYPE_FORM);
                // rechercheDroitEnfant.setForIdDroit("1758");
                ALServiceLocator.getDroitComplexModelService().search(rechercheDroitEnfant);

                testDroitFinValiditeEcheance = (DroitComplexModel) rechercheDroitEnfant.getSearchResults()[0];
                dateFinEcheance = ALServiceLocator.getDatesEcheanceService().getDateFinValiditeDroitCalculee(
                        testDroitFinValiditeEcheance);

                System.out.println("Fin de Validité: " + dateFinEcheance);

                // vérification de la valeur terminée
                assertEquals(dateFinEcheance, "30.04.2015");

            }
        }

        catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }
    }

    /**
     * test sur la fin validité échéance calculé
     */
    @Ignore
    @Test
    public void testFinValiditeDroitEcheance() {
        DroitComplexModel testDroitFinValiditeEcheance = new DroitComplexModel();
        DossierComplexModel dossierComple = new DossierComplexModel();
        DossierSearchModel dossierSearchModel = new DossierSearchModel();
        DossierModel dossierModel = new DossierModel();
        AllocataireComplexSearchModel allocSearc = new AllocataireComplexSearchModel();
        AllocataireComplexModel allocComplex = new AllocataireComplexModel();

        // String debutActivite = null;
        DroitComplexSearchModel rechercheDroitEnfant = new DroitComplexSearchModel();
        rechercheDroitEnfant.setWhereKey("echeancesDroit");
        String dateFinEcheance = null;

        try {
            // recherche d'un dossier
            dossierSearchModel.setForIdDossier("30371");
            ALServiceLocator.getDossierModelService().search(dossierSearchModel);
            if (dossierSearchModel.getSize() == 1) {
                dossierModel = (DossierModel) dossierSearchModel.getSearchResults()[0];

                dossierComple.setDossierModel(dossierModel);

                allocSearc.setForIdTiers("127067");

                allocSearc = ALServiceLocator.getAllocataireComplexModelService().search(allocSearc);

                if (allocSearc.getSize() == 1) {
                    allocComplex = (AllocataireComplexModel) allocSearc.getSearchResults()[0];

                    dossierComple.setAllocataireComplexModel(allocComplex);
                }

                // recherche d'un droit Enfant lié à ce dossier

                rechercheDroitEnfant.setForIdDossier(dossierModel.getIdDossier());

                ALServiceLocator.getDroitComplexModelService().search(rechercheDroitEnfant);

                testDroitFinValiditeEcheance = (DroitComplexModel) rechercheDroitEnfant.getSearchResults()[0];

                dateFinEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().getFinValiditeEcheance(
                        testDroitFinValiditeEcheance, testDroitFinValiditeEcheance.getDroitModel().getDebutDroit());

                System.out.println(" date de fin echeance calculée : " + dateFinEcheance);
                System.out.println(" date de naissance : "
                        + testDroitFinValiditeEcheance.getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getPersonne().getDateNaissance());

            }
            assertEquals(dateFinEcheance, "30.04.2015");
        }

        catch (Exception e) {

            e.printStackTrace();
            fail(e.toString());

        }

    }

}
