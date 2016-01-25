package ch.globaz.al.businessimpl.services.rubrique.fve;

import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableFVESalarieTest extends RubriqueComptableFVEServiceImplTest {

    // @Override
    // @Before
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    // TODO
    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieJUTarifCCJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCJU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);

    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieJUTarifCCJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCJU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);

    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifCCJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCJU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieJUTarifCCJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCJU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieJUTarifJU() {
        // on modifie ces objets selon le test voulu : Dossier salari�, N, affili�: JU, tarif: JU
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.JU);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
        // on d�clenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableFVEServiceImplTest.serviceFVE);
    }
}
