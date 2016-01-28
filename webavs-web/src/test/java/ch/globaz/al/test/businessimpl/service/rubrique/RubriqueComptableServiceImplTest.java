package ch.globaz.al.test.businessimpl.service.rubrique;

import static org.junit.Assert.*;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFPVService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Tests des rubriques comptables - se basent sur des prestations existantes
 * 
 * @author gmo
 * 
 */
public class RubriqueComptableServiceImplTest {
    private DetailPrestationModel detailTest = null;
    private DossierModel dossierTest = null;
    private EntetePrestationModel enteteTest = null;

    private void loadDataToTest(String idDossier, String idEntete, String idDetail) throws JadeApplicationException,
            JadePersistenceException {
        dossierTest = ALServiceLocator.getDossierModelService().read(idDossier);
        enteteTest = ALServiceLocator.getEntetePrestationModelService().read(idEntete);
        DetailPrestationComplexSearchModel searchDetails = new DetailPrestationComplexSearchModel();
        searchDetails.setForIdDossier(idDossier);
        searchDetails = ALServiceLocator.getDetailPrestationComplexModelService().search(searchDetails);

        for (int i = 0; i < searchDetails.getSize(); i++) {
            DetailPrestationComplexModel currentDetail = (DetailPrestationComplexModel) searchDetails
                    .getSearchResults()[i];
            if (currentDetail.getDetailPrestationModel().getIdDetailPrestation().equals(idDetail)) {
                detailTest = currentDetail.getDetailPrestationModel();
                if (!detailTest.getIdEntete().equals(idEntete)) {
                    fail("Unable to load testData : entete and detail don't correspond each other, please check your inputs");
                }
                break;
            }
        }
    }

    /**
     * Test le calcul pour un dossier vaudois (Famille nombreuse) avant la LAFam
     */
    @Ignore
    @Test
    public void testPlanFPV() {
        try {

            RubriquesComptablesFPVService serviceFPV = (RubriquesComptablesFPVService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesFPVService.class);
            // dossier 31755
            // salarié naissance ju
            String idDossier = "31755";
            String idEntete = "443637";
            String idDetail = "1418115";

            loadDataToTest(idDossier, idEntete, idDetail);

            String rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.3075.0250", rubrique);
            // dossier 31755
            // salarié enfant ju
            idDossier = "31755";
            idEntete = "520262";
            idDetail = "1614460";

            loadDataToTest(idDossier, idEntete, idDetail);

            rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.3075.0050", rubrique);

            // dossier 10120
            // salarié formation ju
            idDossier = "10120";
            idEntete = "496447";
            idDetail = "1559660";

            loadDataToTest(idDossier, idEntete, idDetail);

            rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.3075.0150", rubrique);

            // dossier 31002
            // salarié adi ju
            idDossier = "31002";
            idEntete = "363136";
            idDetail = "1161793";

            loadDataToTest(idDossier, idEntete, idDetail);

            rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.3075.0350", rubrique);

            // dossier 22302
            // salarié enfant ju
            idDossier = "22302";
            idEntete = "519646";
            idDetail = "1613346";

            loadDataToTest(idDossier, idEntete, idDetail);

            rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.3075.0050", rubrique);
            // dossier 22302
            // salarié restitution ju
            idDossier = "22302";
            idEntete = "242029";
            idDetail = "812128";

            loadDataToTest(idDossier, idEntete, idDetail);

            rubrique = serviceFPV.getRubriqueComptable(dossierTest, enteteTest, detailTest, "01.01.2010");
            assertEquals("5700.4609.1050", rubrique);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
