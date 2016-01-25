package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationTreatTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementPeriodeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.PrestationPeriode;

public class GenerateEcrituresResitutionBeneficiareForDecisionAcTestCase {

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    private List<Ecriture> generateEcritureBenRest(List<PrestationPeriode> periodes) throws JadeApplicationException {
        GenerateEcrituresResitutionBeneficiareForDecisionAc generate = newGenerateEcritures();
        List<Ecriture> ecritures = generate.generateEcritures(periodes);
        return ecritures;
    }

    private GenerateEcrituresResitutionBeneficiareForDecisionAc newGenerateEcritures() throws JadeApplicationException {
        GenerateEcrituresResitutionBeneficiareForDecisionAc generate = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        GenerateEcrituresResitutionBeneficiareForDecisionAc spy = Mockito.spy(generate);
        Mockito.doReturn(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE).when(spy)
                .resolveIdRefRubrique(Matchers.any(OrdreVersement.class));
        // Mockito.when(spy.resolveIdRefRubrique(Matchers.any(OrdreVersementForList.class))).thenReturn("10000");
        return spy;
    }

    @Test
    public void testEcritureAvecPlusieursPeriode() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("1"));
        periode.setConjoint(OrdreVersementPeriodeFactory.generateOvPeriodeConjoint("1", "220", "120"));
        periodes.add(periode);
        periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("2"));
        periode.setConjoint(OrdreVersementPeriodeFactory.generateOvPeriodeConjoint("2", "140", "100"));
        periodes.add(periode);
        periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("3"));
        periode.setConjoint(OrdreVersementPeriodeFactory.generateOvPeriodeConjoint("3", "140", "100"));
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);
        Assert.assertEquals(12, ecritures.size());

    }

    @Test
    public void testEcritureConjoint() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setConjoint(OrdreVersementPeriodeFactory.generateOvPeriodeConjoint("1", "200", "180"));
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);
        Assert.assertEquals(2, ecritures.size());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, ecritures.get(0).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, ecritures.get(1).getCsTypeRoleFamille());
        verifEcritureBeneficiaire(periodes.get(0).getConjoint().getBeneficiaire(), ecritures.get(0));

        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT, ecritures.get(1).getIdCompteAnnexe());
        verifEcritureRestiution(periodes.get(0).getConjoint().getRestitution(), ecritures.get(1));
    }

    @Test
    public void testEcritureSansBeneficiaire() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRequerantWithOutBeneficiaire("1"));
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);
        Assert.assertEquals(1, ecritures.size());

        verifEcritureRestiution(periodes.get(0).getRequerant().getRestitution(), ecritures.get(0));
    }

    @Test
    public void testEcritureSansRestitution() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRequerantWithOutRestitution("1"));
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);
        Assert.assertEquals(1, ecritures.size());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ecritures.get(0).getIdCompteAnnexe());

        verifEcritureBeneficiaire(periodes.get(0).getRequerant().getBeneficiaire(), ecritures.get(0));
    }

    @Test
    public void testEcrituresDom2R() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRequerant("1", "205", "123"));
        periode.getRequerant().getBeneficiaire().setIdTiersAdressePaiementConjoint("20");
        periode.getRequerant().getRestitution().setIdTiersAdressePaiementConjoint("20");

        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);
        Set<BigDecimal> list = new HashSet<BigDecimal>();
        for (Ecriture ecriture : ecritures) {
            if (SectionPegasus.DECISION_PC.equals(ecriture.getSection())) {
                if (ecriture.getMontant().equals(new BigDecimal(102))) {
                    Assert.assertTrue(true);
                    list.add(ecriture.getMontant());
                } else if (ecriture.getMontant().equals(new BigDecimal(103))) {
                    Assert.assertTrue(true);
                    list.add(ecriture.getMontant());
                } else {
                    Assert.fail();
                }
            } else if (SectionPegasus.RESTIUTION.equals(ecriture.getSection())) {
                if (ecriture.getMontant().equals(new BigDecimal(62))) {
                    Assert.assertTrue(true);
                    list.add(ecriture.getMontant());
                } else if (ecriture.getMontant().equals(new BigDecimal(61))) {
                    Assert.assertTrue(true);
                    list.add(ecriture.getMontant());
                } else {
                    Assert.fail();
                }
            } else {
                Assert.fail("No section was definded");
            }
        }
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, ecritures.get(0).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, ecritures.get(0).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, ecritures.get(1).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, ecritures.get(2).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT, ecritures.get(3).getCsTypeRoleFamille());
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(4, ecritures.size());

        // Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
        // @Override
        // public String exec(Ecriture e) {
        // return e.getCsTypeRoleFamille();
        // }
        // });
        //
        // Assert.assertEquals(2, map.size());
    }

    @Test
    public void testEcrituresDom2RToSepMal() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("1"));
        periode.getRequerant().getRestitution().setIdTiersAdressePaiementConjoint("20");
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);

        Assert.assertEquals(3, ecritures.size());
    }

    @Test
    public void testEcrituresRequerant() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("1"));
        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);

        Assert.assertEquals(2, ecritures.size());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, ecritures.get(0).getCsTypeRoleFamille());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, ecritures.get(1).getCsTypeRoleFamille());
        verifEcritureBeneficiaire(periodes.get(0).getRequerant().getBeneficiaire(), ecritures.get(0));
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ecritures.get(1).getIdCompteAnnexe());

        verifEcritureRestiution(periodes.get(0).getRequerant().getRestitution(), ecritures.get(1));

    }

    @Test
    public void testEcrituresSepMalToDom2R() throws JadeApplicationException {
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();

        PrestationPeriode periode = new PrestationPeriode();
        periode.setNoGroupePeriode(1);
        periode.setRequerant(OrdreVersementPeriodeFactory.generateOvPeriodeRquerant("1"));
        periode.getRequerant().getBeneficiaire().setIdTiersAdressePaiementConjoint("20");
        periode.setConjoint(OrdreVersementPeriodeFactory.generateOvPeriodeConjoint("1", "200", "180"));

        periodes.add(periode);

        List<Ecriture> ecritures = generateEcritureBenRest(periodes);

        Assert.assertEquals(5, ecritures.size());

        // Assert.assertEquals(ovbeneficiaire, beneficiare.getOrdreVersement());
        // Assert.assertEquals(new BigDecimal(ovbeneficiaire.getSimpleOrdreVersement().getMontant()),
        // beneficiare.getMontant());
        // Assert.assertEquals(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE, beneficiare.getIdRefRubrique());
        // Assert.assertEquals(APIEcriture.CREDIT, beneficiare.getCodeDebitCredit());
        // Assert.assertNull(beneficiare.getSectionDette());

    }

    private void verifEcritureBeneficiaire(OrdreVersement ovbeneficiaire, Ecriture beneficiare) {
        Assert.assertEquals(ovbeneficiaire, beneficiare.getOrdreVersement());
        Assert.assertEquals(ovbeneficiaire.getMontant(), beneficiare.getMontant());
        Assert.assertEquals(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE, beneficiare.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.CREDIT, beneficiare.getCodeDebitCredit());
        Assert.assertNull(beneficiare.getSectionSimple());
    }

    private void verifEcritureRestiution(OrdreVersement ovRestiution, Ecriture restiution) {
        Assert.assertEquals(ovRestiution, restiution.getOrdreVersement());
        Assert.assertEquals(ovRestiution.getMontant(), restiution.getMontant());
        Assert.assertEquals(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE, restiution.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.DEBIT, restiution.getCodeDebitCredit());
        Assert.assertNull(restiution.getSectionSimple());
    }

    private void verifiBeneficiaire(Ecriture ecriture, Integer montant) {
        Assert.assertEquals(new BigDecimal(montant), ecriture.getMontant());
        Assert.assertEquals(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE, ecriture.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.CREDIT, ecriture.getCodeDebitCredit());
        Assert.assertNull(ecriture.getSectionSimple());
    }

    private void verifiRestitution(Ecriture ecriture, Integer montant) {
        Assert.assertEquals(new BigDecimal(montant), ecriture.getMontant());
        Assert.assertEquals(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE, ecriture.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.DEBIT, ecriture.getCodeDebitCredit());
        Assert.assertNull(ecriture.getSectionSimple());
    }

}
