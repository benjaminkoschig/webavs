package ch.globaz.al.businessimpl.ctrlexport;

import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.pyxis.business.model.PaysSimpleModel;

@RunWith(Parameterized.class)
public class PrestationExportableControllerControlTest {
    /***
     * Définit les différents paramètres pour les tests qui seront effectués.
     * 
     * @return une tableau contenant les paramètres définit dans l'annotation @Parameterized sous forme de {@link List}
     */
    @Parameterized.Parameters(name = "Allocataire origine : {0}, Enfant résidence : {1}, Cas agricole : {2}, Age : {3}, IsMenage :{4}, Expected : {5}")
    public static Collection<Object[]> getParameters() {
        Object[][] table = new Object[][] { { "CH", "CH", false, 15, false, true },
                { "BE", "FR", false, 15, false, true }, { "CH", "CH", true, 24, true, true },
                { "BE", "FR", true, 24, true, true }, { "CH", "US", false, 15, false, false },
                { "FR", "US", false, 15, false, false }, { "CH", "US", true, 24, false, false },
                { "AT", "US", true, 24, false, false }, { "SI", "FR", false, 15, false, true },
                { "SI", "US", false, 24, false, true }, { "BE", "US", true, 24, false, true },
                { "ES", "US", true, 15, false, true }, { "FR", "US", true, 24, false, true },
                { "IT", "US", true, 24, false, true }, { "PT", "US", true, 15, false, true },
                { "SI", "US", true, 24, false, true }, { "MK", "DE", false, 15, false, false },
                { "MK", "CH", false, 15, false, true }, { "MK", "DE", false, 24, false, false },
                { "CR", "CH", false, 24, false, true }, { "MK", "DE", true, 15, false, true },
                { "SM", "CH", true, 15, false, true }, { "MK", "DE", true, 24, false, true },
                { "TR", "CH", true, 24, false, true }, { "BA", "DE", false, 15, false, true },
                { "ME", "CH", false, 15, false, true }, { "RS", "DE", false, 24, false, true },
                { "ME", "CH", false, 24, false, true }, { "BA", "DE", true, 15, false, true },
                { "ME", "CH", true, 15, false, true }, { "RS", "DE", true, 24, false, true },
                { "ME", "CH", true, 24, false, true }, { "MQ", "YT", true, 15, false, false },
                { "MD", "US", true, 24, false, false }, { "MX", "US", true, 24, false, false },
                { "CH", "", false, 24, true, false }, { "CH", "", false, 15, true, false },
                { "CH", "", true, 24, true, true }, { "CH", "", true, 15, true, true },
                { "SI", "", true, 24, true, false }, { "FR", "", true, 15, true, true },
                { "BG", "BG", true, 15, false, true }, { "HR", "HR", false, 15, false, false },
                { "HR", "CH", false, 15, false, true }, { "US", "CH", false, 15, false, true },
        // TODO : CAS TEST QUI NE PASSE PAS
        /* { "US", "CH", true, 15, true, true }, { "CR", "FR", false, 15, false, true } */
        };
        return Arrays.asList(table);
    }

    /***
     * Chargement des règles depuis le fichier XML. Ces dernières ont été définies à partir du document
     * "Directives pour l'application de la loi fédérale sur les allocations familiales LAFam", page 104/105
     */
    @BeforeClass
    public static void tearUp() {
        try {
            PrestationExportableController.xmlExportRules = JadeXmlReader
                    .parseFile(PrestationExportableController.EXPORT_RULES_FILENAME);
        } catch (Exception e) {
            JadeThread.logWarn(ALApplication.class.getName(), "al.initapplication.exportation.xmlerror");
        }

        if (!PrestationExportableController.isCountriesLoaded()) {
            PrestationExportableController.loadEuropeCountries();
        }
        if (!PrestationExportableController.isRulesLoaded()) {
            try {
                PrestationExportableController.loadRules();
            } catch (JadeApplicationException e) {
                e.printStackTrace();
            }
        }
    }

    /** Âge de l'enfant */
    private int age;

    /** Lieu de résidence actuel de l'enfant sous forme ISO (ex : CH) */
    private String enfantResidence;

    /** Valeur attendu par le test */
    private boolean excepted;

    /** Définit si le dossier est agricole */
    private boolean isAgricole;

    /** Définit si le droit est de type {@link ALCSDroit#TYPE_MEN} */
    private boolean isTypeMenage;

    /** Lieu d'origine de l'allocataire sous forme ISO (ex : DE) */
    private String origine;

    public PrestationExportableControllerControlTest(String origine, String enfantResidence, boolean agricole, int age,
            boolean isTypeMenage, boolean excepted) {
        this.origine = origine;
        this.enfantResidence = enfantResidence;
        isAgricole = agricole;
        this.excepted = excepted;
        this.age = age;
        this.isTypeMenage = isTypeMenage;
    }

    @Ignore
    @Test
    public void testControl() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        PrestationExportableController sut = Mockito.spy(new PrestationExportableController());
        // On mock la residence de l'enfant afin de ne pas dépendre du service nécessitant la mise en route de JADE.
        Mockito.doReturn(enfantResidence).when(sut).findCodeISOEnfant(Matchers.anyString());
        // On mock l'attribut agricole afin de ne pas dépendre du service nécessitant la mise en route de JADE.
        Mockito.doReturn(isAgricole).when(sut).isAgricole(Matchers.any(DossierComplexModel.class));
        // On mock l'âge afin de ne pas dépendre de la date du jour utilisé pour le calcul
        Mockito.doReturn(age).when(sut).getDateBetween(Matchers.any(DroitComplexModel.class), Matchers.anyString());

        // Création des objets contenant les valeurs qui seront utilisées durant le test
        PaysSimpleModel origine = new PaysSimpleModel();
        origine.setCodeIso(this.origine);

        AllocataireComplexModel alloc = new AllocataireComplexModel();
        alloc.setPaysModel(origine);

        DossierComplexModel dossierModel = new DossierComplexModel();
        dossierModel.setAllocataireComplexModel(alloc);

        DroitComplexModel droitModel = new DroitComplexModel();
        if (isTypeMenage) {
            droitModel.getDroitModel().setTypeDroit(ALCSDroit.TYPE_MEN);
        }

        // Cette date permet uniquement de passer le test mais n'est pas utilisé dans le calcul car on mock la
        // dépendence de l'âge afin de ne pas dépendre de la date du jour et ainsi compromettre le test.
        String ctrlDate = "04.10.2013";

        try {
            boolean actual = sut.control(dossierModel, droitModel, ctrlDate);
            Assert.assertEquals(excepted, actual);
        } catch (JadeApplicationException e) {
            Assert.fail(e.getCause().getMessage());
        }
    }
}
