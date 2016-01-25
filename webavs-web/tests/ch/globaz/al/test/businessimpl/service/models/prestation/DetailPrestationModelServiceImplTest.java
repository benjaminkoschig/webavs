package ch.globaz.al.test.businessimpl.service.models.prestation;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.Constantes;

/**
 * @author PTA
 * 
 */
public class DetailPrestationModelServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.DetailPrestationModelServiceImpl#create(ch.globaz.al.business.models.prestation.DetailPrestationModel)}
     * .
     */

    @Ignore
    @Test
    public void testCreate() {
        try {
            DetailPrestationModel detPrestaModel = new DetailPrestationModel();
            ALImplServiceLocator.getDetailPrestationModelService().create(detPrestaModel);

            /*
             * MANDATORY
             */
            assertEquals(6, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            /*
             * DATABASEINTGRITY
             */
            detPrestaModel.setRang("un");
            detPrestaModel.setIdEntete("Ava");
            detPrestaModel.setIdDroit("tout");
            detPrestaModel.setMontant("tout");
            detPrestaModel.setMontantCaisse("tout");
            detPrestaModel.setMontantCanton("tout");
            detPrestaModel.setPeriodeValidite("3janvier05");
            detPrestaModel.setTypePrestation("simple");
            detPrestaModel.setAgeEnfant("trois");
            detPrestaModel.setCategorieTarif("ju");
            detPrestaModel.setIdTiersBeneficiaire("-20");
            detPrestaModel.setNumeroCompte("dddfdsafafafasdfsdfsdafsdaf");
            detPrestaModel.setTarifForce(new Boolean(false));
            detPrestaModel.setCategorieTarifCaisse("dfgsd");
            detPrestaModel.setCategorieTarifCanton("dgdf");

            ALImplServiceLocator.getDetailPrestationModelService().create(detPrestaModel);

            assertEquals(14, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // ajout d'éléments respectant l'obligation, l'intégrité mais pas
            // l'intégrité des codes systemes

            /*
             * CODESYSTEMINTEGRITY
             */
            detPrestaModel.setIdEntete("1");
            detPrestaModel.setIdDroit("1");
            detPrestaModel.setMontant("4500");
            detPrestaModel.setMontantCaisse("4500");
            detPrestaModel.setMontantCanton("4500");
            detPrestaModel.setPeriodeValidite("03.2005");
            detPrestaModel.setAgeEnfant("3");
            detPrestaModel.setIdTiersBeneficiaire("20");
            detPrestaModel.setNumeroCompte("avsd12121");
            detPrestaModel.setCategorieTarifCaisse(Constantes.WRONG_CS);
            detPrestaModel.setCategorieTarifCanton(Constantes.WRONG_CS);
            detPrestaModel.setTypePrestation(Constantes.WRONG_CS);
            detPrestaModel.setCategorieTarif(Constantes.WRONG_CS);
            ALImplServiceLocator.getDetailPrestationModelService().create(detPrestaModel);

            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            /*
             * BUSINESSINTEGRITY
             */
            detPrestaModel.setTypePrestation(ALCSDroit.TYPE_ENF);
            detPrestaModel.setCategorieTarif(null);
            detPrestaModel.setTarifForce(new Boolean(true));
            detPrestaModel.setCategorieTarifCaisse(ALCSTarif.CATEGORIE_CCJU);
            detPrestaModel.setCategorieTarifCanton(ALCSTarif.CATEGORIE_JU);
            detPrestaModel.setRang(null);
            detPrestaModel.setIdDroit("1");
            detPrestaModel.setIdEntete("1");
            detPrestaModel.setIdTiersBeneficiaire("2");
            detPrestaModel.setAgeEnfant(null);
            detPrestaModel.setMontant(null);

            ALImplServiceLocator.getDetailPrestationModelService().create(detPrestaModel);

            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // normalement plus aucun message d'erreur si données indiquées
            // présentes dans la base
            detPrestaModel.setCategorieTarif(ALCSTarif.CATEGORIE_JU);
            detPrestaModel.setIdDroit("3");
            detPrestaModel.setIdEntete("2");
            detPrestaModel.setIdTiersBeneficiaire("1");
            detPrestaModel.setAgeEnfant("3");
            detPrestaModel.setMontant("200.0");
            detPrestaModel.setRang("2");
            ALImplServiceLocator.getDetailPrestationModelService().create(detPrestaModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.DetailPrestationModelServiceImpl#delete(ch.globaz.al.business.models.prestation.DetailPrestationModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // lecture de l'id Detail Prestation
            DetailPrestationModel detPrestaModel = ALImplServiceLocator.getDetailPrestationModelService().read("1");
            ALImplServiceLocator.getDetailPrestationModelService().delete(detPrestaModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.DetailPrestationModelServiceImpl#read(java.lang.String)}
     * .
     */
    // lecture par l'id du détail de la prestation
    @Ignore
    @Test
    public void testRead() {
        try {
            ALImplServiceLocator.getDetailPrestationModelService().read("1");
            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.DetailPrestationModelServiceImpl#update(ch.globaz.al.business.models.prestation.DetailPrestationModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement à modifier par l'identifiant
            DetailPrestationModel detPrestaModel = ALImplServiceLocator.getDetailPrestationModelService().read("1");
            // modification à faire
            detPrestaModel.setMontant("567");
            detPrestaModel.setTypePrestation(ALCSDroit.TYPE_ENF);
            detPrestaModel.setIdDroit("3");

            // mise à jour de l'enregistrement
            ALImplServiceLocator.getDetailPrestationModelService().update(detPrestaModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
