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

public class RubriqueComptableCCJUAgriculteurTest extends RubriqueComptableCCJUServiceImplTest {
    @Ignore
    @Test
    public void testDossierADIAgriculteurIndirectAffilieJUTarifJU() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADIAgriculteurIndirectAffilieJUTarifLFM() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFM);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_AGRICULTEUR, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADIAgriculteurIndirectAffilieJUTarifVDAcquis() {
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierADIAgriculteurRestitutionAffilieJUTarifJU() {
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
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
    public void testDossierADIAgriculteurRestitutionAffilieJUTarifLFM() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setStatut(ALCSPrestation.STATUT_ADI);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFM13);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierADIAgriculteurRestitutionAffilieJUTarifVDAcquis() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
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
    public void testDossierNormalAgriculteurfIndirectAffilieJUTarifJU() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalAgriculteurIndirectAffilieJUTarifLFP() {
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_AGRICULTEUR, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalAgriculteurIndirectAffilieJUTarifVDAcquis() {

        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR, rubriqueRecherchee);

    }

    @Ignore
    @Test
    public void testDossierNormalAgriculteurRestitutionAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
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
    public void testDossierNormalAgriculteurRestitutionAffilieJUTarifLFP() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_LFP23);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE, rubriqueRecherchee);
    }

    @Ignore
    @Test
    public void testDossierNormalAgriculteurRestitutionAffilieJUTarifVDAcquis() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_AGRICULTEUR);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_N);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_RESTITUTION);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
        // on déclenche le test effectif
        String rubriqueRecherchee = getRubriqueKeyRecherchee(RubriqueComptableCCJUServiceImplTest.serviceCCJU);
        Assert.assertEquals(ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION, rubriqueRecherchee);

    }
}
