package ch.globaz.al.businessimpl.services.generation;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.models.dossier.AffilieListComplexModel;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.generation.prestations.GenerationService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.models.dossier.AffilieListComplexModelServiceImpl;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class GenerationServiceImplTest {

    public String valeur_obtenue = null;

    @Test
    @Ignore
    public void testGenerationAffilieStringString() {

        try {
            // exécution du service, méthode, ... à tester

            // ...

            // comparaison du résultat obtenu et celui qui est attendu
            Assert.assertEquals("valeur_attendue", valeur_obtenue);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testGenerationDossier() {

        try {
            ArrayList listOrder = new ArrayList();
            // exploitation
            // TODO: Ajouter constante AdresseService.CS_TYPE_EXPLOITATION dans service pyxis
            listOrder.add(new String("508021"));
            // domicile
            listOrder.add(AdresseService.CS_TYPE_DOMICILE);// 508008
            // courrier
            listOrder.add(new String(AdresseService.CS_TYPE_COURRIER));

            AdresseTiersDetail adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(
                    "126205", "01.01.1999", ALCSTiers.DOMAINE_AF, listOrder, 2);

            AdresseTiersDetail adressTiers2 = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiersCustomCascade("126205", "01.01.2010", ALCSTiers.DOMAINE_AF, listOrder, 2);

        } catch (Exception e) {
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testGenerationFictive() {
        try {
            ArrayList<String> dossier = new ArrayList<String>();

            // dossier trimestriel avec 2 droits dont un droit enf prenant fin durant le trimestre
            dossier.add("32452");

            RecapitulatifEntrepriseImpressionComplexSearchModel prestRecap = ALServiceLocator
                    .getRecapitulatifEntrepriseImpressionService().calculPrestationsStoreInRecapsDocs(dossier,
                            "09.2012", ALCSPrestation.BONI_INDIRECT);

            RecapitulatifEntrepriseImpressionComplexModel ligne1 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap
                    .getSearchResults()[0]);
            RecapitulatifEntrepriseImpressionComplexModel ligne2 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap
                    .getSearchResults()[1]);

            // 2 lignes affichées sur la récap
            Assert.assertEquals(2, prestRecap.getSize());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_MOIS, ligne1.getTypeUnite());
            Assert.assertEquals("2", ligne1.getNbreUnite());
            Assert.assertEquals("2", ligne1.getNbrEnfant());
            Assert.assertEquals("08.2012", ligne1.getPeriodeAEntete());
            Assert.assertEquals("07.2012", ligne1.getPeriodeDeEntete());
            Assert.assertEquals("1000.0", ligne1.getMontant());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_MOIS, ligne2.getTypeUnite());
            Assert.assertEquals("1", ligne2.getNbreUnite());
            Assert.assertEquals("1", ligne2.getNbrEnfant());
            Assert.assertEquals("09.2012", ligne2.getPeriodeAEntete());
            Assert.assertEquals("09.2012", ligne2.getPeriodeDeEntete());
            Assert.assertEquals("250.0", ligne2.getMontant());

            // cas dossier mensuel avec 2 enfants + NAIS
            dossier.clear();
            dossier.add("29958");
            prestRecap = ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                    .calculPrestationsStoreInRecapsDocs(dossier, "12.2011", ALCSPrestation.BONI_INDIRECT);

            ligne1 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap.getSearchResults()[0]);
            ligne2 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap.getSearchResults()[1]);

            // 2 lignes affichées sur la récap
            Assert.assertEquals(2, prestRecap.getSize());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_SPECIAL, ligne1.getTypeUnite());
            Assert.assertEquals("1", ligne1.getNbreUnite());
            Assert.assertEquals("1", ligne1.getNbrEnfant());
            Assert.assertEquals("12.2011", ligne1.getPeriodeAEntete());
            Assert.assertEquals("12.2011", ligne1.getPeriodeDeEntete());
            Assert.assertEquals("850.0", ligne1.getMontant());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_MOIS, ligne2.getTypeUnite());
            Assert.assertEquals("1", ligne2.getNbreUnite());
            Assert.assertEquals("2", ligne2.getNbrEnfant());
            Assert.assertEquals("12.2011", ligne2.getPeriodeAEntete());
            Assert.assertEquals("12.2011", ligne2.getPeriodeDeEntete());
            Assert.assertEquals("500.0", ligne2.getMontant());

            // cas dossier trimestriel valide dès courant novembre => J, puis M
            dossier.clear();
            dossier.add("33616");
            prestRecap = ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                    .calculPrestationsStoreInRecapsDocs(dossier, "12.2011", ALCSPrestation.BONI_INDIRECT);

            ligne1 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap.getSearchResults()[0]);
            ligne2 = ((RecapitulatifEntrepriseImpressionComplexModel) prestRecap.getSearchResults()[1]);

            // 2 lignes affichées sur la récap
            Assert.assertEquals(2, prestRecap.getSize());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_JOUR, ligne1.getTypeUnite());
            Assert.assertEquals("16", ligne1.getNbreUnite());
            Assert.assertEquals("1", ligne1.getNbrEnfant());
            Assert.assertEquals("11.2011", ligne1.getPeriodeAEntete());
            Assert.assertEquals("11.2011", ligne1.getPeriodeDeEntete());
            Assert.assertEquals("160.0", ligne1.getMontant());

            Assert.assertEquals(ALCSDossier.UNITE_CALCUL_MOIS, ligne2.getTypeUnite());
            Assert.assertEquals("1", ligne2.getNbreUnite());
            Assert.assertEquals("1", ligne2.getNbrEnfant());
            Assert.assertEquals("12.2011", ligne2.getPeriodeAEntete());
            Assert.assertEquals("12.2011", ligne2.getPeriodeDeEntete());
            Assert.assertEquals("300.0", ligne2.getMontant());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }

    }

    @Test
    @Ignore
    public void testGenerationGlobale() {

        try {
            // génération
            // ProtocoleLogger logger = ALImplServiceLocator.getGenerationService().generationGlobale("06.2012",
            // ALConstPrestations.TYPE_INDIRECT_GROUPE, "1", new ProtocoleLogger());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }

    }

    @Test
    @Ignore
    public void testGetPeriodeTraitementDossier() {

        try {
            // 61240001=> direct
            // 61240002=> indirect

            // 802001 => mensuelle
            // indirect / trimestriel
            String periode = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(
                    ALCSAffilie.PERIODICITE_TRI, ALCSPrestation.BONI_INDIRECT, ALConstPrestations.TYPE_INDIRECT_GROUPE);

            Assert.assertEquals("03.2012", periode);
            // indirect / mensuel
            periode = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(
                    ALCSAffilie.PERIODICITE_MEN, ALCSPrestation.BONI_INDIRECT, ALConstPrestations.TYPE_INDIRECT_GROUPE);

            Assert.assertEquals("02.2012", periode);

            // direct / trimestriel
            periode = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(
                    ALCSAffilie.PERIODICITE_TRI, ALCSPrestation.BONI_DIRECT, ALConstPrestations.TYPE_DIRECT);

            Assert.assertEquals("02.2012", periode);
            // direct / mensuel
            periode = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(
                    ALCSAffilie.PERIODICITE_MEN, ALCSPrestation.BONI_DIRECT, ALConstPrestations.TYPE_DIRECT);

            Assert.assertEquals("02.2012", periode);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }
    }

    // TODO:vérifier / préparer les données
    @Test
    @Ignore
    public void testNumFacture() {
        try {
            // 61240001=> direct
            // 61240002=> indirect

            // 802001 => mensuelle

            // n° pour récap direct si direct et indirect existent déjà et CO
            String numFacture = ALServiceLocator.getNumeroFactureService().getNumFacture("11.2011", "61240001",
                    "802001", "654.1107");
            Assert.assertEquals("201111100", numFacture);
            // n° pour récap indirect si direct et indirect existent déjà et CO
            numFacture = ALServiceLocator.getNumeroFactureService().getNumFacture("11.2011", "61240002", "802001",
                    "654.1107");
            Assert.assertEquals("201111100", numFacture);
            // n° pour récap direct si direct et indirect n'existent pas
            numFacture = ALServiceLocator.getNumeroFactureService().getNumFacture("12.2011", "61240001", "802001",
                    "654.1107");
            Assert.assertEquals("201112000", numFacture);
            // n° pour récap indirect si direct et indirect n'existent pas
            numFacture = ALServiceLocator.getNumeroFactureService().getNumFacture("12.2011", "61240002", "802001",
                    "654.1107");
            Assert.assertEquals("201112000", numFacture);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testSelectionAffilieGenerationGlobale() {
        try {
            GenerationService gen = ALImplServiceLocator.getGenerationService();
            AffilieListComplexModelServiceImpl aff = (AffilieListComplexModelServiceImpl) ALImplServiceLocator
                    .getAffilieListComplexModelService();
            // modèles de recherche
            AffilieListComplexSearchModel affilies = gen.initSearchAffilies("12.2011",
                    ALConstPrestations.TYPE_INDIRECT_GROUPE);
            affilies.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            // recherche des affiliés
            affilies = aff.search(affilies);
            // affilie 524.1001 actif au 12.2011
            boolean mensuelActifFound = false;
            // affilie 214.1136 radié au 12.2011
            boolean mensuelRadie122011Found = false;
            // affilie 290.1080 radié avant 12.2011
            boolean mensuelRadieAvant122011Found = false;

            // affilie trimestriel 171.1020 actif au 12.2011
            boolean trimestrielActifFound = false;
            // affilie trimestriel 172.1001 radié au 12.2011
            boolean trimestrielRadie122011Found = false;
            // affilie trimestriel 561.1005 radié au 10.2011
            boolean trimestrielRadie102011Found = false;
            // affilie trimestriel 686.1040 radié avant 10.2011
            boolean trimestrielRadieAvant102011Found = false;

            for (int i = 0; i < affilies.getSize(); i++) {
                AffilieListComplexModel currentAffilie = (AffilieListComplexModel) affilies.getSearchResults()[i];
                if ("524.1001".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelActifFound = true;
                }
                if ("214.1136".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelRadie122011Found = true;
                }
                if ("290.1080".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelRadieAvant122011Found = true;
                }

                if ("171.1020".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielActifFound = true;
                }
                if ("172.1001".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadie122011Found = true;
                }
                if ("561.1005".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadie102011Found = true;
                }
                if ("686.1040".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadieAvant102011Found = true;
                }

            }

            Assert.assertEquals(true, mensuelActifFound);
            Assert.assertEquals(true, mensuelRadie122011Found);
            Assert.assertEquals(false, mensuelRadieAvant122011Found);
            Assert.assertEquals(true, trimestrielActifFound);
            Assert.assertEquals(true, trimestrielRadie122011Found);
            Assert.assertEquals(true, trimestrielRadie102011Found);
            Assert.assertEquals(false, trimestrielRadieAvant102011Found);

            // période 01.2012
            // modèles de recherche
            affilies = gen.initSearchAffilies("01.2012", ALConstPrestations.TYPE_INDIRECT_GROUPE);
            affilies.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            // recherche des affiliés
            affilies = aff.search(affilies);

            // affilie 524.1001 actif au 12.2011
            mensuelActifFound = false;
            // affilie 214.1136 radié au 12.2011
            mensuelRadie122011Found = false;
            // affilie 290.1080 radié avant 12.2011
            mensuelRadieAvant122011Found = false;
            // affilie trimestriel 171.1020 actif au 12.2011
            trimestrielActifFound = false;
            // affilie trimestriel 172.1001 radié au 12.2011
            trimestrielRadie122011Found = false;
            // affilie trimestriel 561.1005 radié au 10.2011
            trimestrielRadie102011Found = false;
            // affilie trimestriel 686.1040 radié avant 10.2011
            trimestrielRadieAvant102011Found = false;

            for (int i = 0; i < affilies.getSize(); i++) {
                AffilieListComplexModel currentAffilie = (AffilieListComplexModel) affilies.getSearchResults()[i];
                if ("524.1001".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelActifFound = true;
                }
                if ("214.1136".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelRadie122011Found = true;
                }
                if ("290.1080".equals(currentAffilie.getNumeroAffilie())) {
                    mensuelRadieAvant122011Found = true;
                }

                if ("171.1020".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielActifFound = true;
                }
                if ("172.1001".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadie122011Found = true;
                }
                if ("561.1005".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadie102011Found = true;
                }
                if ("686.1040".equals(currentAffilie.getNumeroAffilie())) {
                    trimestrielRadieAvant102011Found = true;
                }
            }

            Assert.assertEquals(true, mensuelActifFound);
            Assert.assertEquals(false, mensuelRadie122011Found);
            Assert.assertEquals(false, mensuelRadieAvant122011Found);
            Assert.assertEquals(false, trimestrielActifFound);
            Assert.assertEquals(false, trimestrielRadie122011Found);
            Assert.assertEquals(false, trimestrielRadie102011Found);
            Assert.assertEquals(false, trimestrielRadieAvant102011Found);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }
    }

    @Test
    @Ignore
    public void testSelectionDossierGenerationGlobale() {
        try {

            // ALConstPrestations.TYPE_DIRECT.equals(this.typeCot)

            DossierComplexSearchModel dossiers = ALImplServiceLocator.getGenerationService().initSearchDossiers(
                    "01.2012", ALConstPrestations.TYPE_INDIRECT_GROUPE);
            dossiers.setForNumeroAffilie("204.1007");
            dossiers.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            dossiers = ALServiceLocator.getDossierComplexModelService().search(dossiers);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());

        } finally {
            // doFinally();
        }
    }

}
