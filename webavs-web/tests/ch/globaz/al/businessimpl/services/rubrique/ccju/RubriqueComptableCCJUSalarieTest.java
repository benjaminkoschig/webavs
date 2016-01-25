package ch.globaz.al.businessimpl.services.rubrique.ccju;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCCJUSalarieTest extends RubriqueComptableCCJUServiceImplTest {

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieJUTarifJU() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieJUTarifLFP() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieJUTarifVaudAcquis() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieJUTarifJU() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieJUTarifLFP() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieJUTarifVaudAcquis() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifJU() {
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifLFM() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFM);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifVaudAcquis() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_SALARIE, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieJUTarifLFP() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieJUTarifVDAcquis() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);

    }
}
