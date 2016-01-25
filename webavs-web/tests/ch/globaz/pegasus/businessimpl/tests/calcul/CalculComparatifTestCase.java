package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroit;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PersonnePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class CalculComparatifTestCase {

    private Map<String, JadeAbstractSearchModel> cacheDonneesBD;
    private Droit droit;
    private List<PeriodePCAccordee> listePCAccordes;
    private Map<String, CalculDonneesDroit> listePersonnes;

    /**
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CalculException
     * @throws DroitException
     * @throws PeriodeException
     * @throws MonnaieEtrangereException
     * @throws AutreRenteException
     * @throws ForfaitsPrimesAssuranceMaladieException
     */
    private void chargeDonnees() throws JadePersistenceException, JadeApplicationServiceNotAvailableException,
            CalculException, DroitException, PeriodeException, MonnaieEtrangereException, AutreRenteException,
            ForfaitsPrimesAssuranceMaladieException {
        // String debutPlage = PegasusImplServiceLocator.getPeriodesService()
        // .recherchePlageCalcul(this.droit);
        // this.listePersonnes = new HashMap<String, CalculDonneesDroit>();
        //
        // Assert.assertEquals("01.01.2005", debutPlage);
        //
        // this.cacheDonneesBD = PegasusImplServiceLocator.getPeriodesService()
        // .getDonneesCalculDroit(this.droit, debutPlage);
        //
        // this.listePCAccordes = PegasusImplServiceLocator.getPeriodesService()
        // .recherchePeriodesCalcul(this.droit, debutPlage,
        // this.cacheDonneesBD);
        //
        // PegasusImplServiceLocator.getCalculComparatifService()
        // .loadDonneesCalculComparatif(this.droit, this.cacheDonneesBD,
        // this.listePCAccordes, this.listePersonnes);

    }

    private Float getValeur(String annee, String code) {
        for (PeriodePCAccordee periode : listePCAccordes) {
            if (periode.getStrDateDebut().endsWith(annee)) {
                for (PersonnePCAccordee personne : periode.getPersonnes().values()) {
                    TupleDonneeRapport tuple = personne.getRootDonneesConsolidees().getEnfants().get(code);
                    if (tuple == null) {
                        return null;
                    } else {
                        return tuple.getValeur();
                    }
                }
            }
        }
        return null;
    }

    // public final void testCalculePlageDroit() {
    // Assert.fail("Not yet implemented");
    // }

    private boolean hasDonneeFinanciere(String typeDF) {
        Assert.assertNotNull(listePCAccordes == null);
        for (PeriodePCAccordee periode : listePCAccordes) {
            for (PersonnePCAccordee personne : periode.getPersonnes().values()) {
                TupleDonneeRapport root = personne.getRootDonneesConsolidees();
                for (TupleDonneeRapport node : root.getEnfants().values()) {
                    if ((node != null) && typeDF.equals(node.getLabel())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Before
    @Ignore
    public void setUp() throws Exception {
        // super.setUp();

        listePCAccordes = null;
        cacheDonneesBD = null;
        droit = PegasusServiceLocator.getDroitService().readDroit("15");
    }

    @After
    @Ignore
    public void tearDown() throws Exception {
        // genere erreur pour provoquer un rollback de la db
        try {
            JadeThread.rollbackSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // super.tearDown();
    }

    @Test
    @Ignore
    public final void testALoadDonneesCalculComparatif() throws Exception {
        // String debutPlage = PegasusImplServiceLocator.getPeriodesService()
        // .recherchePlageCalcul(this.droit);
        //
        // Map<String, JadeAbstractSearchModel> cacheDonneesBD = PegasusImplServiceLocator
        // .getPeriodesService().getDonneesCalculDroit(this.droit,
        // debutPlage);
        //
        // List<PeriodePCAccordee> listePCAccordes = PegasusImplServiceLocator
        // .getPeriodesService().recherchePeriodesCalcul(this.droit,
        // debutPlage, cacheDonneesBD);
        //
        // PegasusImplServiceLocator.getCalculComparatifService()
        // .loadDonneesCalculComparatif(this.droit, cacheDonneesBD,
        // listePCAccordes, this.listePersonnes);
        //
        // JadeAbstractSearchModel donneesAConsolider = cacheDonneesBD
        // .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT_CC);
        // Assert.assertNotNull(donneesAConsolider);
        // Assert.assertTrue(donneesAConsolider.getSize() > 0);
    }

    @Test
    @Ignore
    public final void testConsolideCacheDonneesPersonnes() throws Exception {

        chargeDonnees();

        // PegasusImplServiceLocator.getCalculComparatifService()
        // .consolideCacheDonneesPersonnes(this.listePCAccordes,
        // this.cacheDonneesBD, this.listePersonnes);
        //
        // Assert.assertTrue("le nombre de personnes attendu ne peut être zero",
        // this.listePCAccordes.get(0).getPersonnes().size() > 0);
        //
        // // check if the container contains the following keys
        // final String[] clefs = {
        // IPCValeursPlanCalcul.CLE_REVENU_RENTE_AVS_AI,
        // IPCValeursPlanCalcul.CLE_FORTUNE_MARCHANDES_STOCK,
        // IPCValeursPlanCalcul.CLE_FORTUNE_BIENS_IMMO_PRINCIPAL,
        // IPCValeursPlanCalcul.CLE_REVENU_BIENS_IMMO_VALEURS_LOCATIVES,
        // IPCValeursPlanCalcul.CLE_DEPENSE_FRAIS_IMMO_VALEUR_LOCATIVE,
        // IPCValeursPlanCalcul.CLE_FORTUNE_BETAIL,
        // IPCValeursPlanCalcul.CLE_FORTUNE_VEHICULE,
        // IPCValeursPlanCalcul.CLE_FORTUNE_NUMERAIRE,
        // IPCValeursPlanCalcul.CLE_REVENU_FOR_MOB_INT_SUR_NUMERAIRES,
        // IPCValeursPlanCalcul.CLE_FORTUNE_PRET_ENVERS_TIERS,
        // IPCValeursPlanCalcul.CLE_REVENU_AUTRES_RENTES_ASS_RENTE_VIAGERE,
        // IPCValeursPlanCalcul.CLE_REVENU_AUTRES_IJ_AI,
        // IPCValeursPlanCalcul.CLE_REVENU_IJ_APG,
        // IPCValeursPlanCalcul.CLE_REVENU_AUTRES_API,
        // IPCValeursPlanCalcul.CLE_REVENU_RENTE_AVS_AI,
        // IPCValeursPlanCalcul.CLE_REVENU_AUTRES_ALLOCATION_FAMILIALE,
        // IPCValeursPlanCalcul.CLE_FORTUNE_TITRE,
        // IPCValeursPlanCalcul.CLE_REVENU_TITRE,
        // IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES,
        // IPCValeursPlanCalcul.CLE_FORTUNE_BIENS_IMMO_ANNEXE,
        // IPCValeursPlanCalcul.CLE_FORTUNE_BIENS_IMMO_NON_HABITABLE };
        //
        // for (String cle : clefs) {
        // Assert.assertTrue("could not find the key " + cle,
        // this.hasDonneeFinanciere(cle));
        // }
        //
        // // check some values
        // Assert.assertEquals(500f, this.getValeur("06",
        // IPCValeursPlanCalcul.CLE_FORTUNE_PRET_ENVERS_TIERS));
        // Assert.assertEquals(800f, this.getValeur("07",
        // IPCValeursPlanCalcul.CLE_FORTUNE_PRET_ENVERS_TIERS));
        // Assert.assertEquals(333f, this.getValeur("06",
        // IPCValeursPlanCalcul.CLE_FORTUNE_NUMERAIRE));
        // Assert.assertEquals(333f * 1, this.getValeur("06",
        // IPCValeursPlanCalcul.CLE_REVENU_FOR_MOB_INT_SUR_NUMERAIRES));
        // Assert.assertNull(this.getValeur("09",
        // IPCValeursPlanCalcul.CLE_REVENU_FOR_MOB_INT_SUR_NUMERAIRES));
        // Assert.assertNull(this.getValeur("05",
        // IPCValeursPlanCalcul.CLE_REVENU_TITRE));
        // Assert.assertEquals(100f,
        // this.getValeur("07", IPCValeursPlanCalcul.CLE_REVENU_TITRE));
        // Assert.assertEquals(100f,
        // this.getValeur("09", IPCValeursPlanCalcul.CLE_REVENU_TITRE));
        //
        // Assert.assertEquals(
        // 80f,
        // this.getValeur(
        // "09",
        // IPCValeursPlanCalcul.CLE_REVENU_AUTRES_RENTES_ASS_RENTE_VIAGERE));

    }

    @Test
    @Ignore
    public final void testYCalculePCAccordes() throws Exception {
        chargeDonnees();

        // PegasusImplServiceLocator.getCalculComparatifService()
        // .consolideCacheDonneesPersonnes(this.listePCAccordes,
        // this.cacheDonneesBD, this.listePersonnes);

        PegasusImplServiceLocator.getCalculComparatifService().calculePCAccordes(droit, listePCAccordes);

        // crée temp dir si besoin
        File tempDir = new File("c://temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        for (PeriodePCAccordee periode : listePCAccordes) {
            String result = PegasusImplServiceLocator.getCalculPersistanceService().serialiseDonneesCcXML(
                    periode.getCalculsComparatifs().get(0));

            // écrit resultats du test dans un fichier xml
            FileWriter writer = new FileWriter("c://temp//periode" + periode.getStrDateDebut() + "_"
                    + periode.getStrDateFin() + ".xml");
            writer.write(result);
            writer.close();
        }
    }

    @Test
    @Ignore
    public final void testZPersistePCAccordes() throws Exception {
        chargeDonnees();

        // PegasusImplServiceLocator.getCalculComparatifService()
        // .consolideCacheDonneesPersonnes(this.listePCAccordes,
        // this.cacheDonneesBD, this.listePersonnes);
        //
        // PegasusImplServiceLocator.getCalculComparatifService()
        // .calculePCAccordes(this.droit, this.listePCAccordes);
        //
        // PeriodePCAccordee periode = this.listePCAccordes.get(0);
        //
        // PegasusImplServiceLocator.getCalculPersistanceService()
        // .sauvePCAccordee(this.droit, periode);
        //
        // // test récupération des données
        // SimplePCAccordeeSearch pcaSearch = new SimplePCAccordeeSearch();
        // pcaSearch.setForIdVersionDroit(this.droit.getSimpleVersionDroit()
        // .getId());
        // pcaSearch = PegasusImplServiceLocator.getSimplePCAccordeeService()
        // .search(pcaSearch);
        // Assert.assertTrue("resultset should be not empty",
        // pcaSearch.getSize() > 0);
        //
        // for (JadeAbstractModel abstractPCA : pcaSearch.getSearchResults()) {
        // SimplePCAccordee pca = (SimplePCAccordee) abstractPCA;
        //
        // SimplePlanDeCalculSearch planCalculSearch = new SimplePlanDeCalculSearch();
        // planCalculSearch.setForIdPCAccordee(pca.getId());
        // planCalculSearch = PegasusImplServiceLocator
        // .getSimplePlanDeCalculService().search(planCalculSearch);
        //
        // for (JadeAbstractModel d : planCalculSearch.getSearchResults()) {
        // SimplePlanDeCalcul plan = (SimplePlanDeCalcul) d;
        //
        // plan = PegasusImplServiceLocator.getSimplePlanDeCalculService()
        // .read(plan.getId());
        // TupleDonneeRapport tupleRoot = PegasusImplServiceLocator
        // .getCalculPersistanceService().deserialiseDonneesCcXML(
        // new String(plan.getResultatCalcul()));
        // System.out.println(tupleRoot);
        // }
        //
        // }
    }

}
