package ch.globaz.al.businessimpl.ctrlexport;

import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.pyxis.business.model.PaysSimpleModel;

public class PrestationExportableControllerTest {
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

    private AllocataireComplexModel alloc;
    private String ctrlDate;
    private DossierComplexModel dossierModel;
    private DroitComplexModel droitModel;
    private PrestationExportableController sut;

    @Before
    public void setup() {
        sut = Mockito.spy(new PrestationExportableController());
    }

    /*
     * Saisie d'une date qui n'est pas sous le régime LAFAM (avant la mise en application de la loi en 01.01.2009)
     */
    @Test
    public void testControlDateNonSousRegimeLAFAM() {
        dossierModel = new DossierComplexModel();
        droitModel = new DroitComplexModel();
        ctrlDate = "04.01.1990";

        boolean expected = true;
        boolean actual;
        try {
            actual = sut.control(dossierModel, droitModel, ctrlDate);
            Assert.assertEquals(expected, actual);
        } catch (JadeApplicationException e) {
            Assert.fail(e.getMessage());
        }
    }

    /*
     * Salariés : CH Pays de résidence des enfants : CH Employé non actif
     */
    @Test
    public void testControlDateSousRegimeLAFamEmployeNonActifCH() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        PaysSimpleModel origine = new PaysSimpleModel();
        origine.setCodeIso("CH");

        alloc = new AllocataireComplexModel();
        alloc.setPaysModel(origine);

        dossierModel = new DossierComplexModel();
        dossierModel.setAllocataireComplexModel(alloc);
        dossierModel.getDossierModel().setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        droitModel = new DroitComplexModel();
        ctrlDate = "04.01.2009";
        Mockito.doReturn("CH").when(sut).findCodeISOEnfant(Matchers.anyString());
        Mockito.doReturn(false).when(sut).isAgricole(Matchers.any(DossierComplexModel.class));

        boolean expected = true;
        boolean actual;
        try {
            actual = sut.control(dossierModel, droitModel, ctrlDate);
            Assert.assertEquals(expected, actual);
        } catch (JadeApplicationException e) {
            Assert.fail(e.getCause().getMessage());
        }
    }

    /*
     * Salariés : CH Pays de résidence des enfants : IT Employé non actif
     */
    @Test
    public void testControlDateSousRegimeLAFamEmployeNonActifEntranger()
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        PaysSimpleModel origine = new PaysSimpleModel();
        origine.setCodeIso("CH");

        alloc = new AllocataireComplexModel();
        alloc.setPaysModel(origine);

        dossierModel = new DossierComplexModel();
        dossierModel.setAllocataireComplexModel(alloc);
        dossierModel.getDossierModel().setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        droitModel = new DroitComplexModel();
        ctrlDate = "04.01.2009";
        Mockito.doReturn("IT").when(sut).findCodeISOEnfant(Matchers.anyString());
        Mockito.doReturn(false).when(sut).isAgricole(Matchers.any(DossierComplexModel.class));

        boolean expected = false;
        boolean actual;
        try {
            actual = sut.control(dossierModel, droitModel, ctrlDate);
            Assert.assertEquals(expected, actual);
        } catch (JadeApplicationException e) {
            Assert.fail(e.getCause().getMessage());
        }
    }
}
