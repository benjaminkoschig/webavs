package ch.globaz.al.businessimpl.services.rubrique.ccju;

import org.junit.Test;

/**
 * @author gmo
 * 
 */
public class RubriqueComptableCCJUNonActifTest /* extends RubriqueComptableCCJUServiceImplTest */{

    /**
     * plutot RUBRIQUE_STANDARD_ADI ? ou non actif ou moins ? 02.12.2013 gmo
     */
    @Test
    public void testDossierADINonActifIndirectAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    /**
     * plutôt RUBRIQUE_STANDARD_ADI ? ou non actif au moins? 02.12.2013 gmo
     */
    @Test
    public void testDossierADINonActifIndirectAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    /**
     * plutôt RUBRIQUE_STANDARD_ADI ? ou non actif au moins ? 02.12.2013 gmo
     */
    @Test
    public void testDossierADINonActifIndirectAffilieJUTarifVaudAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);

    }

    @Test
    public void testDossierADINonActifRestitutionAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF, rubriqueRecherchee);
    }

    @Test
    public void testDossierADINonActifRestitutionAffilieJUTarifLFM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Test
    public void testDossierADINonActifRestitutionAffilieJUTarifVaudAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF, rubriqueRecherchee);

    }

    @Test
    public void testDossierNormalNonActifIndirectAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalNonActifIndirectAffilieJUTarifLFM() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFM);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalNonActifIndirectAffilieJUTarifVaudAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF, rubriqueRecherchee);

    }

    @Test
    public void testDossierNormalNonActifRestitutionAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalNonActifRestitutionAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalNonActifRestitutionAffilieJUTarifVaudAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_NONACTIF);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF, rubriqueRecherchee);

    }
}
