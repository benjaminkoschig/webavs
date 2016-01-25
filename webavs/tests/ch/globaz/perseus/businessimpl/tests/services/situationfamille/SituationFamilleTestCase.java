package ch.globaz.perseus.businessimpl.tests.services.situationfamille;


public class SituationFamilleTestCase {

    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     * //
     */
    // @Override
    // public void tearDown() {
    // // genere erreur pour provoquer un rollback de la db
    // try {
    // // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    //
    // }

    public final void test01CreateRequerant() throws Exception {
    }

    // public final void test01CreateRequerant() throws Exception {
    // Requerant r = new Requerant();
    // r.getMembreFamille().getSimpleMembreFamille().setIdTiers("1300789");
    //
    // r = PerseusServiceLocator.getRequerantService().create(r);
    //
    // Assert.assertNotNull(r.getId());
    // }

    // public final void test02SearchReadAndDeleteRequerant() throws Exception {
    // RequerantSearchModel s = new RequerantSearchModel();
    // s.setForIdTiers("1300789");
    // s = PerseusServiceLocator.getRequerantService().search(s);
    // Requerant requerant = (Requerant) s.getSearchResults()[0];
    //
    // Requerant requerant2 = PerseusServiceLocator.getRequerantService().read(requerant.getId());
    //
    // Assert.assertNotNull(requerant);
    // Assert.assertNotNull(requerant2);
    // Assert.assertEquals(requerant.getId(), requerant2.getId());
    // Assert.assertNotNull(requerant.getMembreFamille());
    // Assert.assertNotNull(requerant2.getMembreFamille());
    //
    // // requerant = PerseusServiceLocator.getRequerantService().delete(requerant);
    //
    // s = new RequerantSearchModel();
    // s.setForIdRequerant(requerant2.getId());
    // s = PerseusServiceLocator.getRequerantService().search(s);
    //
    // Assert.assertEquals(0, s.getSize());
    //
    // }
    //
    // public final void test03SearchRequerant() throws Exception {
    // SimpleMembreFamilleSearchModel simpleMembreFamilleSearchModel = new SimpleMembreFamilleSearchModel();
    // simpleMembreFamilleSearchModel.setForIdTiers("1000544");
    //
    // simpleMembreFamilleSearchModel = (SimpleMembreFamilleSearchModel) JadePersistenceManager
    // .search(simpleMembreFamilleSearchModel);
    //
    // Assert.assertEquals(0, simpleMembreFamilleSearchModel.getSize());
    // }

}