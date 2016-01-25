package ch.globaz.al.businessimpl.services.rubrique.cvci;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Classe de tests pour les rubriques comptables des dossiers salarié CVCI. Attention, si le paramètre n'est pas en base
 * pour les non restitutions, le service retourne: rubrique.standard.salarie ou rubrique.standard.adi => pas (encore)
 * testé
 * 
 * @author gmo
 */

public class RubriqueComptableCVCISalarieTest {
    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieBETarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.BE);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        //
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendueFinale = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendueFinale, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieVDTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        //
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendueFinale = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendueFinale, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieVDTarifVD_ENF() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_ENF);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendueFinale = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendueFinale, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieVDTarifZH_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_ZH);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendueFinale = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.ZH).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendueFinale, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieVDTarifSZ_FORM() {
        //
        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_SZ);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.SZ).toLowerCase());
        //
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.SZ).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }

    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieVDTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }
    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieVDTarifVD_ENF() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_ENF);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }
    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieZHTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.ZH);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieVDTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieVDTarifVD_NAIS() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_NAIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieVDTarifZH_ENF() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_ZH);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_ENF);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.ZH).toLowerCase());
        //
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieVDTarifVaudAcquis_FORM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_FORM);
        // // on déclenche le test effectif
        //
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieVDTarifVD_ENF() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_ENF);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.VD).toLowerCase());
        //
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieVDTarifZH_NAIS() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_ZH);
        // RubriqueComptableServiceImplTest.detailTest.setTypePrestation(ALCSDroit.TYPE_NAIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCVCIServiceImplTest.serviceCVCI);
        //
        // String rubriqueAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.ZH).toLowerCase());
        //
        // String rubriqueAlternativeAttendue = ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION.concat(".").concat(
        // JadeCodesSystemsUtil.getCode(ALCSCantons.ZH).toLowerCase());
        // try {
        // Assert.assertEquals(rubriqueAttendue, rubriqueRecherchee);
        // } catch (AssertionError e) {
        // Assert.assertEquals(rubriqueAlternativeAttendue, rubriqueRecherchee);
        // }

    }

}
