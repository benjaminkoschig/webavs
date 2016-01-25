package ch.globaz.amal.businessimpl.services.models.contribuable;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ContribuableServiceImplTest {
    private Contribuable contribuable = null;
    private PersonneEtendueComplexModel personneEtendueComplexModel = null;
    private SimpleContribuable simpleContribuable = null;
    private SimpleFamille simpleFamille = null;

    private void init() {
        simpleContribuable = initSimpleContribuable();
        simpleFamille = initSimpleFamille();
        personneEtendueComplexModel = initPersonneEtendue("138862");

        contribuable = new Contribuable();
        contribuable.setIsContribuableHistorique(false);
        contribuable.setContribuable(simpleContribuable);
        contribuable.setFamille(simpleFamille);
        contribuable.setPersonneEtendueComplexModel(personneEtendueComplexModel);
    }

    private PersonneEtendueComplexModel initPersonneEtendue(String idTiers) {
        try {
            return TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);
        } catch (Exception e) {
            Assert.fail("Erreur, l'id tiers n'existe pas ! --> " + e.getMessage());
        }

        return null;
    }

    private SimpleContribuable initSimpleContribuable() {
        SimpleContribuable simpleContribuable = new SimpleContribuable();
        simpleContribuable.setNoContribuable("11111111111");
        simpleContribuable.setIsContribuableActif(true);
        simpleContribuable.setIdTier("1");
        return simpleContribuable;
    }

    private SimpleFamille initSimpleFamille() {
        SimpleFamille simpleFamille = new SimpleFamille();
        simpleFamille.setIdContribuable("151");
        simpleFamille.setNomPrenom("AcGlobaz TacimGlobaz");
        simpleFamille.setDateNaissance("19820103");
        simpleFamille.setIsContribuable(true);
        return simpleFamille;
    }

    protected void reset() {
        simpleContribuable = null;
        simpleFamille = null;
        personneEtendueComplexModel = null;
        contribuable = null;
        JadeThread.logClear();
    }

    @Ignore
    @Test
    public void testCreate() {
        try {
            // Création d'un contribuable avec tiers existant (contribuable null)
            // Résultat attendu : Erreur (ContribuableException)
            boolean bCreateContribException = false;
            try {
                contribuable = AmalImplServiceLocator.getContribuableService().create(contribuable);

                Assert.assertNotNull(contribuable.getSpy());
                Assert.assertNotNull(contribuable.getId());
            } catch (ContribuableException ce) {
                bCreateContribException = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            } finally {
                reset();
            }
            Assert.assertTrue(bCreateContribException);

            // Création d'un contribuable avec tiers existant (SimpleContribuable null)
            // Résultat attendu : Erreur (NullPointerException)
            boolean bCreateSimpleContribException = false;
            try {
                init();
                contribuable.setContribuable(null);
                contribuable = AmalImplServiceLocator.getContribuableService().create(contribuable);
            } catch (NullPointerException npe) {
                bCreateSimpleContribException = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            } finally {
                reset();
            }
            Assert.assertTrue(bCreateSimpleContribException);

            // Création d'un contribuable avec tiers existant (SimpleFamille null)
            // Résultat attendu : Erreur (NullPointerException)
            boolean bCreateSimpleFamillebException = false;
            try {
                init();
                contribuable.setFamille(null);
                contribuable = AmalImplServiceLocator.getContribuableService().create(contribuable);
            } catch (NullPointerException npe) {
                bCreateSimpleFamillebException = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            } finally {
                reset();
            }
            Assert.assertTrue(bCreateSimpleFamillebException);

            // Création d'un contribuable avec tiers qui n'exise pas (données correctes)
            // Résultat attendu : Contribuable crée + Tiers crée
            try {
                init();
                contribuable.setPersonneEtendueComplexModel(new PersonneEtendueComplexModel());
                contribuable.getPersonneEtendue().getTiers().setDesignation1("AcGlobaz");
                contribuable.getPersonneEtendue().getTiers().setDesignation2("TacimGlobaz");
                contribuable.getPersonneEtendue().getPersonneEtendue().setNumAvsActuel("");
                contribuable.getPersonneEtendue().getPersonne().setDateNaissance("03.01.1982");
                contribuable = AmalImplServiceLocator.getContribuableService().create(contribuable);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(contribuable.isNew());
                Assert.assertNotNull(contribuable.getId());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            } finally {
                reset();
            }

            // Création d'un contribuable avec tiers existant (données correctes)
            // Résultat attendu : Contribuable crée
            try {
                init();
                contribuable = AmalImplServiceLocator.getContribuableService().create(contribuable);
                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertNotNull(contribuable.getSpy());
                Assert.assertNotNull(contribuable.getId());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            } finally {
                reset();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du create du Contribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testDelete() {
        try {
            // Pas de delete de contribuable
            boolean bDeleteException = false;
            try {
                Contribuable contribuable = new Contribuable();
                contribuable = AmalImplServiceLocator.getContribuableService().delete(contribuable);
            } catch (ContribuableException ce) {
                bDeleteException = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du delete du Contribuable --> " + e.toString());
            }
            Assert.assertTrue(bDeleteException);
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du delete du Contribuable --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'un contribuable sans passer d'id
            // Résultat attendu ==> ContribuableException
            boolean bReadNullId = false;
            try {
                AmalImplServiceLocator.getContribuableService().read(null);
            } catch (ContribuableException ce) {
                bReadNullId = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du read avec un id NULL ==> " + e.toString());
            }
            Assert.assertTrue(bReadNullId);

            // Lecture d'un contribuable avec un id correct
            // Résultat attendu ==> contribuable.isNew == false
            try {
                Contribuable contribuable = AmalImplServiceLocator.getContribuableService().read("151");
                Assert.assertFalse(contribuable.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du read avec un id NULL ==> " + e.toString());
            }

        } catch (Exception e) {
            Assert.fail("ContribuableServiceImplTest : Erreur général lors du testRead sur Contribuable ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }

    }

    @Ignore
    @Test
    public void testSearch() {
        try {
            // Search d'un contribuable avec infos incorrect (modèl null)
            // Résultat attendu ==> Erreur
            boolean bSearchNullModel = false;
            try {
                ContribuableSearch contribuableSearch = AmalImplServiceLocator.getContribuableService().search(null);
            } catch (ContribuableException ce) {
                bSearchNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            }
            Assert.assertTrue(bSearchNullModel);

            // Search d'un contribuable avec infos corrects
            // Résultat attendu ==> Résultat length == 6
            try {
                ContribuableSearch contribuableSearch = new ContribuableSearch();
                contribuableSearch.setForIdContribuable("151");
                contribuableSearch = AmalImplServiceLocator.getContribuableService().search(contribuableSearch);

                Contribuable contribuable = (Contribuable) contribuableSearch.getSearchResults()[0];

                Assert.assertTrue(contribuableSearch.getSize() == 6);
                Assert.assertFalse(contribuable.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            }

        } catch (Exception e) {
            Assert.fail("ContribuableServiceImplTest : Erreur général lors du testSearch sur Contribuable ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testSearchRcList() {
        try {
            // Search d'un contribuable avec infos incorrect (modèl null)
            // Résultat attendu ==> Erreur
            boolean bSearchNullModel = false;
            try {
                ContribuableRCListeSearch contribuableRCListeSearch = AmalImplServiceLocator.getContribuableService()
                        .searchRCListe(null);
            } catch (ContribuableException ce) {
                bSearchNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            }
            Assert.assertTrue(bSearchNullModel);

            // Search d'un contribuable avec infos corrects
            // Résultat attendu ==> Résultat length == 6
            try {
                ContribuableRCListeSearch contribuableRCListeSearch = new ContribuableRCListeSearch();
                contribuableRCListeSearch.setForIdContribuable("151");
                contribuableRCListeSearch = AmalImplServiceLocator.getContribuableService().searchRCListe(
                        contribuableRCListeSearch);

                ContribuableRCListe contribuableRCListe = (ContribuableRCListe) contribuableRCListeSearch
                        .getSearchResults()[0];

                Assert.assertTrue(contribuableRCListeSearch.getSize() == 6);
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors du create du Contribuable --> " + e.toString());
            }

        } catch (Exception e) {
            Assert.fail("ContribuableServiceImplTest : Erreur général lors du testSearch sur Contribuable ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }

    @Ignore
    @Test
    public void testUpdate() {
        // Pas d'update sur le modèle Contribuable
        Assert.assertTrue(true);
    }
}
