package ch.globaz.al.businessimpl.services.rubrique.ccvd;

import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCCVDSalarieTest extends RubriqueComptableCCVDServiceImplTest {

    // @Override
    // @Before
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    // TODO
    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieVDTarifCCVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: CCVD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCVD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);

    }

    @Ignore
    @Test
    public void testDossierADISalarieIndirectAffilieVDTarifVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieVDTarifCCVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: CCVD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCVD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);

    }

    @Ignore
    @Test
    public void testDossierADISalarieRestitutionAffilieVDTarifVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieVDTarifCCVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCVD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieIndirectAffilieVDTarifVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);
    }

    // TODO
    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieVDTarifCCVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_CCVD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);

    }

    @Ignore
    @Test
    public void testDossierNormalSalarieRestitutionAffilieVDTarifVD() {
        // on modifie ces objets selon le test voulu : Dossier salarié, N, affilié: VD, tarif: VD
        RubriqueComptableServiceImplTest.dossierTest.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        RubriqueComptableServiceImplTest.dossierTest.setStatut(ALCSDossier.STATUT_IS);
        RubriqueComptableServiceImplTest.enteteTest.setBonification(ALCSPrestation.BONI_INDIRECT);
        RubriqueComptableServiceImplTest.enteteTest.setCantonAffilie(ALCSCantons.VD);
        RubriqueComptableServiceImplTest.detailTest.setCategorieTarif(ALCSTarif.CATEGORIE_VD);
        // on déclenche le test effectif
        getRubriqueKeyRecherchee(RubriqueComptableCCVDServiceImplTest.serviceCCVD);
    }
}
