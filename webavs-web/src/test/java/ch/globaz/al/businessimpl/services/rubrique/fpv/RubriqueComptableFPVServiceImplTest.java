package ch.globaz.al.businessimpl.services.rubrique.fpv;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFPVService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableFPVServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesFPVService serviceFPV = null;

    /**
     * Remplace le code de la caf (variable) dans <code>rubriqueRecherchee</code> par le pattern code OFAS (fixe)
     * rubrique.multicaisse.125.salarie.adi.ju => rubrique.multicaisse.code_ofas.salarie.adi.ju
     * 
     * @param rubriqueRecherchee
     * @return rubriqueRecherchee avec code CAF générique (=>pattern fixe @see
     *         {@link ALConstRubriques#RUBRIQUE_MULTICAISSE_CODE_PATTERN})
     */
    protected String replaceVariableCodeCAFWithGenericPattern(String rubriqueRecherchee) {
        int positionCodeCaf = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_ADI
                .indexOf(ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN);

        String prefixRubriqueRecherchee = rubriqueRecherchee.substring(0, positionCodeCaf);
        String suffixRubriqueRecherchee = rubriqueRecherchee.substring(positionCodeCaf);
        suffixRubriqueRecherchee = suffixRubriqueRecherchee.substring(suffixRubriqueRecherchee.indexOf("."));
        return prefixRubriqueRecherchee.concat(ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN).concat(
                suffixRubriqueRecherchee);

    }

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableFPVServiceImplTest.serviceFPV = (RubriquesComptablesFPVService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesFPVService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriquesComptablesFPVService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
