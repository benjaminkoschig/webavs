/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationTreatTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

/**
 * @author sce
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateOperationsBlocageTest {
    public static final String ID_SECTION_BLOCAGE = "1050";

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    /**
     * Retourne la liste des idTypesSectionsPegasus présent dans l'enum
     * 
     * @return
     */
    private static ArrayList<String> getIdSectionsListFromPegasusEnum() {
        ArrayList<String> listeIdTypeSection = new ArrayList<String>();

        for (SectionPegasus section : SectionPegasus.values()) {
            listeIdTypeSection.add(section.getType());
        }

        return listeIdTypeSection;
    }

    private Operations generateOperations(List<OrdreVersementForList> ovs) throws JadeApplicationException {
        List<SectionSimpleModel> sections = new ArrayList<SectionSimpleModel>();
        // dette
        sections.add(new SectionSimpleModel());
        sections.get(0).setIdSection("2020");
        sections.get(0).setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);

        // blocage
        sections.add(new SectionSimpleModel());
        sections.get(1).setIdSection(GenerateOperationsBlocageTest.ID_SECTION_BLOCAGE);
        sections.get(1).setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);

        GenerateOperations generateOperations = newGenerateOperations();
        Operations operations = generateOperations.generateAllOperations(ovs, sections, "01.01.2012", "01.03.2012");
        return operations;
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCsRoleFamilleNullParameterTest() {
        String idTiersReq = null;
        String idTiersToCompareOk = "12132";
        String idTiersToCompareFail = null;

        // test requerant idTiers egaux
        String csRoleReq = GenerateOperationsBlocage.getCsRoleFamille(idTiersReq, idTiersToCompareOk);
        Assert.assertEquals(csRoleReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        // test conjoint idTiers inegaux
        String csRoleCon = GenerateOperationsBlocage.getCsRoleFamille(idTiersReq, idTiersToCompareFail);
        Assert.assertEquals(csRoleCon, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    @Test
    public void getCsRoleFamilleStandardTest() {
        String idTiersReq = "12132";
        String idTiersToCompareOk = "12132";
        String idTiersToCompareFail = "222121";

        // test requerant idTiers egaux
        String csRoleReq = GenerateOperationsBlocage.getCsRoleFamille(idTiersReq, idTiersToCompareOk);
        Assert.assertEquals(csRoleReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        // test conjoint idTiers inegaux
        String csRoleCon = GenerateOperationsBlocage.getCsRoleFamille(idTiersReq, idTiersToCompareFail);
        Assert.assertEquals(csRoleCon, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    @Test
    public void getSectionMappingWithOvStandardTest() {

    }

    @Test
    public void getSectionPegasusByIdSectionStandardTest() {
        // iteration sur la liste des idTypeSection définis dans l'enum

        for (String idTypeSection : GenerateOperationsBlocageTest.getIdSectionsListFromPegasusEnum()) {
            // SectionPegasus sectionPegasus = GenerateOperationsBlocage.getSectionPegasusByIdSection(idTypeSection);

            // Assert.assertEquals(sectionPegasus.getType(), idTypeSection);
        }
    }

    // @Test(expected = IllegalArgumentException.class)
    public void getSectionPegasusByIdSectionWithFailParametersTest() {
        String idTypeSectionFail = "32323";
        // SectionPegasus sectionPegasus = GenerateOperationsBlocage.getSectionPegasusByIdSection(idTypeSectionFail);
    }

    protected GenerateEcrituresResitutionBeneficiareForDecisionAc newEcritureBasic() {
        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        return ac;
    }

    private GenerateEcrituresResitutionBeneficiareForDecisionAc newGenerateEcritures() throws JadeApplicationException {
        GenerateEcrituresResitutionBeneficiareForDecisionAc generate = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        GenerateEcrituresResitutionBeneficiareForDecisionAc spy = Mockito.spy(generate);
        Mockito.doReturn(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE).when(spy)
                .resolveIdRefRubrique(Matchers.any(OrdreVersement.class));
        return spy;
    }

    private GenerateOperations newGenerateOperations() throws JadeApplicationException {
        GenerateOperationsBlocage generate = new GenerateOperationsBlocage();
        GenerateOperationsBlocage spy = Mockito.spy(generate);
        doReturn("Mock RefPaiement").when(spy).formatDeblocage(any(OrdreVersementForList.class), any(String.class));
        Mockito.doReturn(newGenerateEcritures()).when(spy).newEcritureBasic();
        return spy;
    }

    @Test
    public void testGenerateAllOperationBasiqueCasSimpleWithConjoint() throws JadeApplicationException {

        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000",
                GenerateOperationsBlocageTest.ID_SECTION_BLOCAGE));

        ovs.add(OrdreVersementFactory.generateOvListConjointBeneficiaire("1", "500",
                GenerateOperationsBlocageTest.ID_SECTION_BLOCAGE));
        ovs.add(OrdreVersementFactory.generateOvListDette("1200", "2020", "32",
                GenerateOperationsBlocageTest.ID_SECTION_BLOCAGE));

        Operations operations = generateOperations(ovs);
        List<Ecriture> ecritures = operations.getEcritures();
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
            @Override
            public String exec(Ecriture e) {
                return e.getTypeEcriture().toString();
            }
        });
        Assert.assertEquals(2, ovsCompta.size());
        Assert.assertEquals(new BigDecimal(2000), ovsCompta.get(0).getMontant());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ovsCompta.get(0).getCompteAnnexe()
                .getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(500), ovsCompta.get(1).getMontant());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ovsCompta.get(1).getCompteAnnexe()
                .getIdCompteAnnexe());

        Assert.assertEquals(2, ecritures.size());

    }
}
