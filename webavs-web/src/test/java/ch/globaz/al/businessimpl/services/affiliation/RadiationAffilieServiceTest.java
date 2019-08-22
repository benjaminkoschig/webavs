package ch.globaz.al.businessimpl.services.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.affiliation.RadiationAffilieService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class RadiationAffilieServiceTest extends ALTestCaseJU4 {

    @Ignore
    @Test
    public void testCopierDossier() {

        try {
            String dateDebutActivite = "01.01.2013";
            String numAffilie = "124.1150";
            String idDossier = "58";

            DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);
            HashMap<ALEnumProtocoleRadiationAffilie, Object> res = ALServiceLocator.getRadiationAffilieService()
                    .copierDossier(dossier, numAffilie, dateDebutActivite);
            DossierComplexModel newDossier = (DossierComplexModel) res.get(ALEnumProtocoleRadiationAffilie.DOSSIER);

            // date de début d'activité
            Assert.assertEquals(dateDebutActivite, newDossier.getDossierModel().getDebutActivite());

            // date de début de validité
            Assert.assertEquals(dateDebutActivite, newDossier.getDossierModel().getDebutValidite());

            // état du dossier
            Assert.assertEquals(ALCSDossier.ETAT_ACTIF, newDossier.getDossierModel().getEtatDossier());

            // date de fin de validité
            Assert.assertEquals(true, JadeStringUtil.isBlankOrZero(newDossier.getDossierModel().getFinValidite()));

            // nombre de prestations
            EntetePrestationSearchModel searchPrest = new EntetePrestationSearchModel();
            searchPrest.setForIdDossier(newDossier.getId());
            Assert.assertEquals(0, ALImplServiceLocator.getEntetePrestationModelService().count(searchPrest));

            /*
             * Test d'un droit ne devant pas être actif après la copie du dossier
             */
            DroitComplexSearchModel searchDroit = new DroitComplexSearchModel();
            searchDroit.setForIdTiersEnfant("165733");
            searchDroit.setForIdDossier(newDossier.getId());
            searchDroit.setForTypeDroit(ALCSDroit.TYPE_ENF);
            searchDroit = ALServiceLocator.getDroitComplexModelService().search(searchDroit);
            Assert.assertEquals(1, searchDroit.getSize());
            DroitComplexModel droit = (DroitComplexModel) searchDroit.getSearchResults()[0];

            // etat
            Assert.assertEquals(ALCSDroit.ETAT_S, droit.getDroitModel().getEtatDroit());

            // nombre d'annonces
            AnnonceRafamSearchModel searchRAFam = new AnnonceRafamSearchModel();
            searchRAFam.setForIdDroit(droit.getId());
            searchRAFam = ALServiceLocator.getAnnonceRafamModelService().search(searchRAFam);
            Assert.assertEquals(0, searchRAFam.getSize());

            /*
             * Test d'un droit devant être actif après la copie du dossier
             */
            searchDroit = new DroitComplexSearchModel();
            searchDroit.setForIdTiersEnfant("165734");
            searchDroit.setForIdDossier(newDossier.getId());
            searchDroit.setForTypeDroit(ALCSDroit.TYPE_ENF);
            searchDroit = ALServiceLocator.getDroitComplexModelService().search(searchDroit);
            Assert.assertEquals(1, searchDroit.getSize());
            droit = (DroitComplexModel) searchDroit.getSearchResults()[0];

            // etat
            Assert.assertEquals(ALCSDroit.ETAT_A, droit.getDroitModel().getEtatDroit());

            // date de début
            Assert.assertEquals(dateDebutActivite, droit.getDroitModel().getDebutDroit());

            // nombre d'annonces
            searchRAFam = new AnnonceRafamSearchModel();
            searchRAFam.setForIdDroit(droit.getId());
            searchRAFam = ALServiceLocator.getAnnonceRafamModelService().search(searchRAFam);
            Assert.assertEquals(1, searchRAFam.getSize());

            // vérification des copies
            Assert.assertEquals(3,
                    ALServiceLocator.getCopiesBusinessService().searchForDossier(newDossier.getId(), null).getSize());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testGenererPrestationForDossier() {
        DossierComplexModel dossier = null;

        try {
            RadiationAffilieService s = ALServiceLocator.getRadiationAffilieService();

            dossier = (DossierComplexModel) (s.radierDossier(ALServiceLocator.getDossierComplexModelService()
                    .read("58"), "30.11.2011", "Toto")).get(ALEnumProtocoleRadiationAffilie.DOSSIER);

            DetailPrestationComplexSearchModel prestations = ALServiceLocator.getRadiationAffilieService()
                    .genererPrestationForDossier(dossier, false, false);

            // nombre de détail de prestations
            Assert.assertEquals(3, prestations.getSize());

            // montant total
            BigDecimal total = new BigDecimal("0.00");
            for (JadeAbstractModel prest : prestations.getSearchResults()) {
                total = total.add(new BigDecimal(((DetailPrestationComplexModel) prest).getDetailPrestationModel()
                        .getMontant()));
            }

            Assert.assertEquals(new BigDecimal("-750.00"), total);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();

            try {

                JadeThread.logClear();
                EntetePrestationSearchModel search = new EntetePrestationSearchModel();
                search.setForEtat(ALCSPrestation.ETAT_SA);
                search.setForIdDossier("58");
                search = ALImplServiceLocator.getEntetePrestationModelService().search(search);

                for (JadeAbstractModel prest : search.getSearchResults()) {
                    try {
                        ALImplServiceLocator.getEntetePrestationModelService().delete((EntetePrestationModel) prest);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                JadeThread.commitSession();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Ignore
    @Test
    public void testGetDossiersForAffilie() {
        try {
            DossierComplexSearchModel res = ALServiceLocator.getRadiationAffilieService().getDossiersForAffilie(
                    "261.1205", "31.12.2011");
            Assert.assertEquals(4, res.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    @Ignore
    @Test
    public void testRadierDossier() {
        try {
            String dateRadiation = "31.12.2012";

            HashMap<ALEnumProtocoleRadiationAffilie, Object> res = ALServiceLocator.getRadiationAffilieService()
                    .radierDossier(ALServiceLocator.getDossierComplexModelService().read("58"), dateRadiation, "Toto");

            DossierComplexModel dossier = (DossierComplexModel) res.get(ALEnumProtocoleRadiationAffilie.DOSSIER);

            // date de fin d'activité
            Assert.assertEquals("31.12.2012", dossier.getDossierModel().getFinActivite());

            // date de fin de validité
            Assert.assertEquals("31.12.2012", dossier.getDossierModel().getFinValidite());

            // état du dossier
            Assert.assertEquals(ALCSDossier.ETAT_RADIE, dossier.getDossierModel().getEtatDossier());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }
}
