package ch.globaz.al.businessimpl.services.rubrique.ccju;

import org.junit.Test;

public class RubriqueComptableCCJUIndependantTest /* extends RubriqueComptableCCJUServiceImplTest */{
    @Test
    /*
     * Ne devrait pas retourner la rubrique ADI ? ou au moins independant ?? mais standard_salarié....02.12.2013 GMO)
     */
    public void testDossierADIIndependantIndirectAffilieJUTarifJU() {
        //
        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Test
    /*
     * Ne devrait pas retourner la rubrique ADI ? ou au moins independant ?? mais standard_salarié....02.12.2013 GMO)
     */
    public void testDossierADIIndependantIndirectAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Test
    /*
     * Ne devrait pas retourner la rubrique ADI ? ou au moins independant ?? mais standard_salarié....02.12.2013 GMO)
     */
    public void testDossierADIIndependantIndirectAffilieJUTarifVDAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
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
    public void testDossierADIIndependantRestitutionAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }

    @Test
    public void testDossierADIIndependantRestitutionAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Test
    public void testDossierADIIndependantRestitutionAffilieJUTarifVDAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        // RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantIndirectAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantIndirectAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantIndirectAffilieJUTarifVDAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantRestitutionAffilieJUTarifJU() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantRestitutionAffilieJUTarifLFP() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Test
    public void testDossierNormalIndependantRestitutionAffilieJUTarifVDAcquis() {

        // RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_INDEPENDANT);
        // RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        // RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        // RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        // RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // // on déclenche le test effectif
        // String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        // Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }
}
