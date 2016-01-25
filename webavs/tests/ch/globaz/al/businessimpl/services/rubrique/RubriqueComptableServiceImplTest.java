package ch.globaz.al.businessimpl.services.rubrique;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.rubriques.ALRubriquesException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesService;
import ch.globaz.al.utils.ALTestCaseJU4;
import ch.globaz.param.business.exceptions.models.ParameterModelException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;

public class RubriqueComptableServiceImplTest extends ALTestCaseJU4 {

    protected static DetailPrestationModel detailTest = null;
    protected static DossierModel dossierTest = null;
    protected static EntetePrestationModel enteteTest = null;
    protected static List<JadeAbstractModel> excludeDossiersList = new ArrayList<JadeAbstractModel>();
    // Constante pour indique que le plan comptable ne gère pas le cas soumis lors du test
    protected static String NO_RUBRIQUE_MAPPING_DEFINED = "NO_RUBRIQUE_DEFINED";

    private static boolean loadDataToTestWithDossierSearchModel(DossierSearchModel searchDossier)
            throws JadeApplicationException, JadePersistenceException {

        if (searchDossier == null) {
            throw new IllegalArgumentException("loadDataToTestWithDossierSearchModel: searchDossier is null");
        }

        if (RubriqueComptableServiceImplTest.excludeDossiersList == null) {
            throw new IllegalArgumentException("loadDataToTestWithDossierSearchModel: excludeDossiersList is null");
        }

        searchDossier.setDefinedSearchSize(100);
        searchDossier = ALServiceLocator.getDossierModelService().search(searchDossier);
        List<JadeAbstractModel> searchDossiersResult = Arrays.asList(searchDossier.getSearchResults());

        searchDossiersResult.removeAll(RubriqueComptableServiceImplTest.excludeDossiersList);

        boolean errorLoad = true;
        for (int i = 0; i < searchDossiersResult.size(); i++) {
            RubriqueComptableServiceImplTest.dossierTest = (DossierModel) searchDossiersResult.get(i);
            if (!RubriqueComptableServiceImplTest.excludeDossiersList
                    .contains(RubriqueComptableServiceImplTest.dossierTest.getIdDossier())) {
                DetailPrestationComplexSearchModel searchDetailsComplex = new DetailPrestationComplexSearchModel();
                searchDetailsComplex.setForIdDossier(RubriqueComptableServiceImplTest.dossierTest.getIdDossier());
                searchDetailsComplex = ALServiceLocator.getDetailPrestationComplexModelService().search(
                        searchDetailsComplex);

                if (searchDetailsComplex.getSize() > 0) {
                    RubriqueComptableServiceImplTest.enteteTest = ((DetailPrestationComplexModel) searchDetailsComplex
                            .getSearchResults()[0]).getEntetePrestationModel();

                    RubriqueComptableServiceImplTest.detailTest = ((DetailPrestationComplexModel) searchDetailsComplex
                            .getSearchResults()[0]).getDetailPrestationModel();
                    errorLoad = false;
                    break;
                }
            }
        }

        return errorLoad;

    }

    protected static void loadDossierBasic() {
        DossierSearchModel dossierSearch = new DossierSearchModel();
        dossierSearch.setForActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        dossierSearch.setForStatut(ALCSDossier.STATUT_N);
        try {
            boolean resultFound = RubriqueComptableServiceImplTest.loadDataToTestWithDossierSearchModel(dossierSearch);
            if (resultFound) {
                Assert.fail("Unable to loadDossierBasic, reason: data not found");
            }
        } catch (Exception e) {
            Assert.fail("Unable to loadDataToTestWithActiviteAllocataire, reason:" + e.getMessage());
        }
    }

    /**
     * 
     * @param service
     * @return TODO
     * @throws ALRubriquesException
     */

    protected String getRubriqueKeyRecherchee(RubriquesComptablesService service) {
        String rubrique = null;
        try {
            if (service != null) {
                rubrique = service.getRubriqueComptable(RubriqueComptableServiceImplTest.dossierTest,
                        RubriqueComptableServiceImplTest.enteteTest, RubriqueComptableServiceImplTest.detailTest,
                        "01.01.2010");
            }
        } catch (ParameterModelException e) {
            return e.getKeyParameter().toString();
        } catch (ALRubriquesException e) {
            return RubriqueComptableServiceImplTest.NO_RUBRIQUE_MAPPING_DEFINED;
        } catch (Exception e) {
            Assert.fail("Problème lors de la récupération de la rubrique, reason:" + e.getMessage());
        }

        if (rubrique != null) {
            ParameterSearchModel searchParam = new ParameterSearchModel();
            searchParam.setForValeurAlphaParametre(rubrique);
            searchParam.setForDateDebutValidite("01.01.2010");
            try {
                ParamServiceLocator.getParameterModelService().search(searchParam);
            } catch (Exception e) {
                Assert.fail("Impossible de rechercher le paramètre correspondant à la rubrique ??, reason:"
                        + e.getMessage());
            }
            if (searchParam.getSize() == 1) {
                ParameterModel param = (ParameterModel) searchParam.getSearchResults()[0];
                return param.getIdCleDiffere();

            } else if (searchParam.getSize() > 1) {

                StringBuffer rubriquesFound = new StringBuffer();
                for (int i = 0; i < searchParam.getSize(); i++) {
                    rubriquesFound.append(((ParameterModel) searchParam.getSearchResults()[i]).getIdCleDiffere())
                            .append(";");
                }
                Assert.fail("Impossible de vérifier le testm plusieurs paramètres avec la valeur de rubrique définie par le plan comptable:"
                        + rubriquesFound);
            }
        }
        return rubrique;
    }
}
