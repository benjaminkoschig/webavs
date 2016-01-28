package globaz.corvus.compta;

import globaz.corvus.TestConfig;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.utils.AbstractTestCaseWithContext;
import globaz.globall.api.BISession;
import globaz.jade.common.Jade;
import globaz.osiris.api.APIRubrique;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationPC;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationRFM;
import globaz.prestation.tools.PRSession;
import globaz.utils.CommonPropertiesUtilsTest;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.properties.CommonProperties;

public class AREModuleComptableTest extends AbstractTestCaseWithContext {

    private boolean errorFounded = false;

    private String sousTypeGenrePrestationActif = "true";
    private boolean stopOnError = false;

    @Override
    protected String getApplicationName() {
        return TestConfig.getDefaultConfig().getApplicationName();
    }

    @Override
    protected String getUserName() {
        return TestConfig.getDefaultConfig().getUserName();
    }

    @Override
    protected String getUserPassword() {
        return TestConfig.getDefaultConfig().getUserPassword();
    }

    @Test
    @Ignore
    public void mainTest() {
        try {

            Jade.getInstance();
            Jade.getInstance().setVerbose(false);

            BISession corvusSession = getSession();
            BISession sessionOsiris = PRSession.connectSession(corvusSession, "OSIRIS");

            int typeRubriqueComptable = AREModuleComptable.TYPE_RUBRIQUE_NORMAL;

            CommonPropertiesUtilsTest.setPropertyValue(CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF,
                    sousTypeGenrePrestationActif);

            System.out.println("Tests du genre de prestation PC 110");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.getSousTypeGenrePrestationPCAVS()) {
                String genrePrestation = PRCodePrestationPC._110.getCodePrestationAsString();

                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

            System.out.println("Tests du genre de prestation PC 113");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.getSousTypeGenrePrestationPCAVS()) {
                String genrePrestation = PRCodePrestationPC._113.getCodePrestationAsString();
                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

            System.out.println("Tests du genre de prestation PC 150");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.getSousTypeGenrePrestationPCAI()) {
                String genrePrestation = PRCodePrestationPC._150.getCodePrestationAsString();
                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

            System.out.println("Tests du genre de prestation RFM 210");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.getSousTypeGenrePrestationRFMAVS()) {
                String genrePrestation = PRCodePrestationRFM._210.getCodePrestationAsString();
                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

            System.out.println("Tests du genre de prestation RFM 213");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.getSousTypeGenrePrestationRFMAVS()) {
                String genrePrestation = PRCodePrestationRFM._213.getCodePrestationAsString();
                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

            System.out.println("Tests du genre de prestation RFM 250");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.getSousTypeGenrePrestationRFMAI()) {
                String genrePrestation = PRCodePrestationRFM._250.getCodePrestationAsString();
                APIRubrique rubrique = AREModuleComptable.getRubriqueWithInit(sessionOsiris, genrePrestation,
                        stcp.getSousTypeCodePrestationAsString(), typeRubriqueComptable);
                print(genrePrestation, stcp.getSousTypeCodePrestationAsString(), rubrique);
            }

        } catch (Exception e) {
            errorFounded = true;
            e.printStackTrace();
            Assert.assertFalse("Des exceptions sont apparues lors du test", true);
        }

        Assert.assertFalse("Des erreurs sont apparues lors du test", errorFounded);
    }

    private void print(String codePrestation, String sousTypeGenrePrestation, APIRubrique rubrique) {
        String idExterne = "not founded";
        String idRubrique = "empty";
        if (rubrique != null) {
            idRubrique = rubrique.getIdRubrique();
            idExterne = rubrique.getIdExterne();
        } else {
            errorFounded = true;
        }
        if (stopOnError) {
            Assert.assertTrue(rubrique != null);
        }
        System.out.println("Genre de prestation [" + codePrestation + "], sous-type genre prestation = ["
                + sousTypeGenrePrestation + "], id rubrique comptable = [" + idRubrique + "], rubrique = [" + idExterne
                + "]");
    }

}
