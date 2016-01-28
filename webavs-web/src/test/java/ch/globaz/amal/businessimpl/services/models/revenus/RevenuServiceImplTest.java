package ch.globaz.amal.businessimpl.services.models.revenus;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuFullComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

public class RevenuServiceImplTest /* extends AMTestCase */{

    public RevenuServiceImplTest(String name) {
        // super(name);
    }

    private RevenuFullComplex initRevenuFullComplexSourcier() {
        RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
        revenuFullComplex.setSimpleRevenu(initRevenuGeneriqueForSourcier());
        revenuFullComplex.setSimpleRevenuSourcier(initRevenuSourcier());
        return revenuFullComplex;
    }

    private RevenuFullComplex initRevenuFullComplexStandard() {
        RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
        revenuFullComplex.setSimpleRevenu(initRevenuGeneriqueForContribuable());
        revenuFullComplex.setSimpleRevenuContribuable(initRevenuStandard());

        return revenuFullComplex;
    }

    private SimpleRevenu initRevenuGeneriqueForContribuable() {
        SimpleRevenu simpleRevenu = new SimpleRevenu();
        simpleRevenu.setIdContribuable("151");
        simpleRevenu.setAnneeTaxation("2009");
        simpleRevenu.setTypeSource("42002901");
        simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
        simpleRevenu.setRevDetUniqueOuiNon(true);
        simpleRevenu.setRevDetUnique("41900");
        simpleRevenu.setProfession("42001700");
        simpleRevenu.setDateTraitement("08.11.2010");
        simpleRevenu.setEtatCivil(IAMCodeSysteme.CS_ETAT_CIVIL_MARIED);
        simpleRevenu.setIsSourcier(false);
        simpleRevenu.setNbEnfants("3");
        simpleRevenu.setNbJours("365");
        simpleRevenu.setNoLotAvisTaxation("5");
        return simpleRevenu;
    }

    private SimpleRevenu initRevenuGeneriqueForSourcier() {
        SimpleRevenu simpleRevenu = new SimpleRevenu();
        simpleRevenu.setIdContribuable("169539");
        simpleRevenu.setAnneeTaxation("2010");
        simpleRevenu.setTypeRevenu(IAMCodeSysteme.CS_TYPE_SOURCIER);
        simpleRevenu.setRevDetUniqueOuiNon(false);
        simpleRevenu.setProfession("42001700");
        simpleRevenu.setDateTraitement("30.08.2011");
        simpleRevenu.setEtatCivil(IAMCodeSysteme.CS_ETAT_CIVIL_MARIED);
        simpleRevenu.setIsSourcier(false);
        simpleRevenu.setNbEnfants("3");
        return simpleRevenu;
    }

    /**
     * Création d'un revenuFullComplex SOURCIER avec : <br/>
     * Année : 2011 <br/>
     * Id contribuable : 71110 <br/>
     * Type de revenu : Sourcier <br/>
     * Nb d'enfants : 1 <br/>
     * Etat civil : Célibataire (42001600) <br/>
     * Nb de jours : 365 <br/>
     * Code profession : Salarié (42001700) <br/>
     * Code actif : Oui <br/>
     * Code suspendu : Non <br/>
     * Année de réf : 2010 <br/>
     * Revenu annuel époux : 86014.60 <br/>
     * Revenu annuel épouse : 36658.10<br/>
     * <br/>
     * Résultat attendu <br/>
     * Revenu impostable selon chiffre 690 : 87'825.00 <br/>
     * Contribuable avec enfant à charge : 10'000.00 <br/>
     * 1 enfant(s) : 4'000.00<br/>
     * Total revenu déterminant : 73'825.00<br/>
     * <br/>
     * Revenu pris en compte : 122'672.00 <br/>
     * Cotisations AVS/AI/APG : 6'195.00 <br/>
     * Cotisations AC : 1'227.00 <br/>
     * Primes AANP : 1'205.00 <br/>
     * Primes LPP : 4'731.00 <br/>
     * Déductions assurances : 2'700.00 <br/>
     * Déduction assurances enfants : 760.00 <br/>
     * Déductions enfants : 5'400.00 <br/>
     * Déduction frais d'obtention : 7'600.00 <br/>
     * Déduction double gain 2'500.00 <br/>
     * Total revenu imposable : 87'825.00<br/>
     * 
     * @return
     * 
     */
    private SimpleRevenuSourcier initRevenuSourcier() {
        SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();

        simpleRevenuSourcier.setRevenuEpouxAnnuel("25295.75");
        simpleRevenuSourcier.setRevenuEpouseAnnuel("24313.65");
        simpleRevenuSourcier.setRevenuEpouseMensuel("0");
        simpleRevenuSourcier.setRevenuEpouxMensuel("0");
        simpleRevenuSourcier.setNombreMois("0");

        return simpleRevenuSourcier;
    }

    /**
     * Création d'un revenuFullComplex STANDARD avec :<br/>
     * Année : 2011<br/>
     * Id contribuable : 1026<br/>
     * Type de revenu Contribuable standard<br/>
     * Nb d'enfants : 3<br/>
     * Etat civil : Marié (42001601)<br/>
     * Nb de jours : 365<br/>
     * Code profession : Salarié (42001700)<br/>
     * Code actif : Oui<br/>
     * Code suspendu : Non<br/>
     * 100 Revenu net provenant d'un emploi : 63'700.00<br/>
     * 300 Rend. de la fortune immob. privée : 7'131.00<br/>
     * 490 Totaux des revenus nets : 70'857.00<br/>
     * 530 Intérets passifs privés : 13'194.00<br/>
     * 620 Personnes à charge ou enfants : 15'600.00<br/>
     * 690 Revenu imposable : 49'063.00<br/>
     * 890 Fortune imposable : 23'500.00<br/>
     * <br/>
     * Résultat attendu<br/>
     * Revenu impostable selon chiffre 690 : 49'000.00<br/>
     * Rendement de la fortune immo selon... : 7'131.00<br/>
     * Intérêts passifs selon... : 13'194.00<br/>
     * Contribuable avec enfant à charge : 10'000.00<br/>
     * 3 enfant(s) : 14'000.00<br/>
     * Total revenu déterminant : 31'768.00<br/>
     * 
     * @return
     */
    private SimpleRevenuContribuable initRevenuStandard() {
        SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();
        simpleRevenuContribuable.setRevenuNetEmploi("63393");
        simpleRevenuContribuable.setRevenuNetEpouse("38107");
        simpleRevenuContribuable.setTotalRevenusNets("101500");
        simpleRevenuContribuable.setPersChargeEnf("18000");
        simpleRevenuContribuable.setRevenuImposable("65988");
        return simpleRevenuContribuable;
    }

    private SimpleRevenuDeterminant initSimpleRevenuDeterminant() {
        SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();
        simpleRevenuDeterminant.setDeductionContribAvecEnfantChargeCalcul("0");
        simpleRevenuDeterminant.setDeductionContribNonCelibSansEnfantChargeCalcul("0");
        simpleRevenuDeterminant.setDeductionSelonNbreEnfantCalcul("0");
        simpleRevenuDeterminant.setExcedentDepensesPropImmoCalcul("0");
        simpleRevenuDeterminant.setExcedentDepensesSuccNonPartageesCalcul("0");
        simpleRevenuDeterminant.setFortuneImposableCalcul("0");
        simpleRevenuDeterminant.setFortuneImposablePercentCalcul("0");
        simpleRevenuDeterminant.setInteretsPassifsCalcul("0");
        simpleRevenuDeterminant.setPartRendementImmobExedantIntPassifsCalcul("0");
        simpleRevenuDeterminant.setPerteLiquidationCalcul("0");
        simpleRevenuDeterminant.setPerteReporteeExercicesCommerciauxCalcul("0");
        simpleRevenuDeterminant.setRendementFortuneImmoCalcul("0");
        simpleRevenuDeterminant.setRevenuImposableCalcul("0");

        return simpleRevenuDeterminant;
    }

    private SimpleRevenuHistorique initSimpleRevenuHistorique() {
        SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();
        simpleRevenuHistorique.setAnneeHistorique("2011");
        simpleRevenuHistorique.setCodeActif(true);
        simpleRevenuHistorique.setDateCreation("13.10.2012");
        return simpleRevenuHistorique;
    }

    public void testCount() {
        try {
            // Count des revenu avec modèle null
            // Résultat attendu = nb resultat > 0
            boolean bCountRevenu = false;
            try {
                RevenuSearch revenuSearch = null;
                int nb = AmalServiceLocator.getRevenuService().count(revenuSearch);

                Assert.assertTrue(nb > 0);
            } catch (RevenuException re) {
                bCountRevenu = true;
            }
            Assert.assertTrue(bCountRevenu);

            // Count des revenu avec modèle ok
            // Résultat attendu = nb resultat > 0
            try {
                RevenuSearch revenuSearch = new RevenuSearch();
                revenuSearch.setForIdContribuable("151");
                int nb = AmalServiceLocator.getRevenuService().count(revenuSearch);

                Assert.assertTrue(nb > 0);
            } catch (RevenuException re) {
                Assert.fail("Erreur lors du count sur RevenuSearch ! ==> " + re.toString());
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du count du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testCreateRevenuFullComplex() {
        try {
            // Création d'un revenu full complex avec un modèle NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateRevenuNullModel = false;
            try {
                RevenuFullComplex revenuFullComplex = null;
                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);
            } catch (RevenuException re) {
                bCreateRevenuNullModel = true;
            }
            Assert.assertTrue(bCreateRevenuNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenu NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuNullModel = false;
            try {
                RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
                revenuFullComplex.setSimpleRevenu(null);
                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenuContribuable NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuContribuableNullModel = false;
            try {
                RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
                revenuFullComplex.setSimpleRevenuContribuable(null);
                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuContribuableNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuContribuableNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenuSourcier NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuSoucierNullModel = false;
            try {
                RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
                revenuFullComplex.setSimpleRevenuSourcier(null);
                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuSoucierNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuSoucierNullModel);

            // Création d'un revenu incorrect (pas de type de revenu)
            // Résultat attendu : RevenuException
            boolean bRevenuExceptionNoTaxationType = false;
            try {
                RevenuFullComplex revenuFullComplex = initRevenuFullComplexStandard();
                revenuFullComplex.getSimpleRevenu().setTypeRevenu(null);

                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuFullComplex.isNew());
                // Assert.assertEquals("31768", revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul());
            } catch (RevenuException re) {
                bRevenuExceptionNoTaxationType = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bRevenuExceptionNoTaxationType);

            // Création d'un revenu STANDARD correct
            // Résultat attendu : Pas d'erreur / IsNew=false
            try {
                RevenuFullComplex revenuFullComplexStandard = initRevenuFullComplexStandard();

                revenuFullComplexStandard = AmalServiceLocator.getRevenuService().create(revenuFullComplexStandard);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuFullComplexStandard.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Création d'un revenu SOURCIER correct
            // Résultat attendu : Pas d'erreur / IsNew=false
            try {
                RevenuFullComplex revenuFullComplexSourcier = AmalServiceLocator.getRevenuService().readFullComplex(
                        "400");
                AmalServiceLocator.getRevenuService().delete(revenuFullComplexSourcier);

                RevenuFullComplex revenuFullComplex = initRevenuFullComplexSourcier();

                revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuFullComplex.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex sourcier ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du Create du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testCreateRevenuHistoriqueComplex() {
        try {
            // Création d'un revenu historiqueComplex avec un modèle NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateRevenuHistoriqueNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = null;
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateRevenuHistoriqueNullModel = true;
            }
            Assert.assertTrue(bCreateRevenuHistoriqueNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenuContribuable NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuContribuableNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.getRevenuFullComplex().setSimpleRevenuContribuable(null);
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuContribuableNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuContribuableNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenuSourcier NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuSourcierNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.getRevenuFullComplex().setSimpleRevenuSourcier(null);
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuSourcierNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuSourcierNullModel);

            // Création d'un revenu historiqueComplex avec un modèle simpleRevenuDeterminant NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuDeterminantNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.setSimpleRevenuDeterminant(null);
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuDeterminantNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuDeterminantNullModel);

            // Création d'un revenu historiqueComplex avec un modèle simpleRevenuHistorique NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateSimpleRevenuHistoriqueNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.setSimpleRevenuHistorique(null);
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateSimpleRevenuHistoriqueNullModel = true;
            }
            Assert.assertTrue(bCreateSimpleRevenuHistoriqueNullModel);

            // Création d'un revenu full complex avec un modèle simpleRevenuSourcier NULL
            // Résultat attendu ==> RevenuException
            boolean bCreateRevenuFullComplexNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.setRevenuFullComplex(null);
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
            } catch (RevenuException re) {
                bCreateRevenuFullComplexNullModel = true;
            }
            Assert.assertTrue(bCreateRevenuFullComplexNullModel);

            // Création d'un revenu incorrect (pas de type de revenu)
            // Résultat attendu : RevenuException
            boolean bRevenuHistoriqueExceptionNoTaxationType = false;
            try {
                RevenuFullComplex revenuFullComplex = initRevenuFullComplexStandard();
                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().setTypeRevenu(null);

                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuHistoriqueComplex.isNew());
            } catch (RevenuException re) {
                bRevenuHistoriqueExceptionNoTaxationType = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bRevenuHistoriqueExceptionNoTaxationType);

            // Création d'un revenu STANDARD correct
            // Résultat attendu : Montant correspondent / Pas d'erreur / IsNew=false
            RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
            try {
                // On désactive d'abord le revenu
                SimpleRevenuHistoriqueSearch simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
                simpleRevenuHistoriqueSearch.setForIdContribuable("151");
                simpleRevenuHistoriqueSearch.setForAnneeHistorique("2011");
                simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(
                        simpleRevenuHistoriqueSearch);
                SimpleRevenuHistorique simpleRevenuHistoriqueToDisable = (SimpleRevenuHistorique) simpleRevenuHistoriqueSearch
                        .getSearchResults()[0];
                simpleRevenuHistoriqueToDisable.setCodeActif(false);
                simpleRevenuHistoriqueToDisable = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                        simpleRevenuHistoriqueToDisable);

                // On crée d'abord le revenu générique
                SimpleRevenu simpleRevenu = initRevenuGeneriqueForContribuable();
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
                // ensuite, création du revenu contribuable
                SimpleRevenuContribuable simpleRevenuContribuable = initRevenuStandard();
                simpleRevenuContribuable.setIdRevenu(simpleRevenu.getIdRevenu());
                // les 2 forment le revenuFullComplex
                RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
                revenuFullComplex.setSimpleRevenu(simpleRevenu);
                revenuFullComplex.setSimpleRevenuContribuable(simpleRevenuContribuable);
                // Création du simpleRevenuHistorique
                SimpleRevenuHistorique simpleRevenuHistorique = initSimpleRevenuHistorique();
                simpleRevenuHistorique.setIdRevenu(simpleRevenu.getIdRevenu());
                // Création du SimpleRevenuDeterminant
                SimpleRevenuDeterminant simpleRevenuDeterminant = initSimpleRevenuDeterminant();

                // et enfin création du revenuHistoriqueComplex
                revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
                revenuHistoriqueComplex.setSimpleRevenuHistorique(simpleRevenuHistorique);
                revenuHistoriqueComplex.setSimpleRevenuDeterminant(simpleRevenuDeterminant);

                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuHistoriqueComplex.isNew());
                Assert.assertEquals("41900", revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuDeterminantCalcul());
                Assert.assertEquals("65900", revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuImposableCalcul());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Création d'un revenu SOURCIER correct
            // Résultat attendu : Montant correspondent / Pas d'erreur / IsNew=false
            revenuHistoriqueComplex = new RevenuHistoriqueComplex();
            try {
                // On désactive d'abord le revenu
                SimpleRevenuHistoriqueSearch simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
                simpleRevenuHistoriqueSearch.setForIdContribuable("15341");
                simpleRevenuHistoriqueSearch.setForAnneeHistorique("2010");
                simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(
                        simpleRevenuHistoriqueSearch);
                SimpleRevenuHistorique simpleRevenuHistoriqueToDisable = (SimpleRevenuHistorique) simpleRevenuHistoriqueSearch
                        .getSearchResults()[0];
                simpleRevenuHistoriqueToDisable.setCodeActif(false);
                simpleRevenuHistoriqueToDisable = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                        simpleRevenuHistoriqueToDisable);

                // On crée d'abord le revenu générique
                SimpleRevenu simpleRevenu = initRevenuGeneriqueForSourcier();
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
                // ensuite, création du revenu contribuable
                SimpleRevenuSourcier SimpleRevenuSourcier = initRevenuSourcier();
                SimpleRevenuSourcier.setIdRevenu(simpleRevenu.getIdRevenu());
                // les 2 forment le revenuFullComplex
                RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
                revenuFullComplex.setSimpleRevenu(simpleRevenu);
                revenuFullComplex.setSimpleRevenuSourcier(SimpleRevenuSourcier);
                // Création du simpleRevenuHistorique
                SimpleRevenuHistorique simpleRevenuHistorique = initSimpleRevenuHistorique();
                simpleRevenuHistorique.setIdRevenu(simpleRevenu.getIdRevenu());
                // Création du SimpleRevenuDeterminant
                SimpleRevenuDeterminant simpleRevenuDeterminant = initSimpleRevenuDeterminant();

                // et enfin création du revenuHistoriqueComplex
                revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
                revenuHistoriqueComplex.setSimpleRevenuHistorique(simpleRevenuHistorique);
                revenuHistoriqueComplex.setSimpleRevenuDeterminant(simpleRevenuDeterminant);

                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);

                Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
                Assert.assertFalse(revenuHistoriqueComplex.isNew());
                Assert.assertEquals("-13700", revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuDeterminantCalcul());
                Assert.assertEquals("10300", revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuImposableCalcul());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la création d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

        } catch (Exception e) {
            Assert.fail("Erreur générale lors du Create du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testDelete() {
        try {
            // Suppression d'un revenuFullComplex avec modèle null
            // Résultat attendu : RevenuException
            boolean bDeleteNullModel = false;
            try {
                AmalServiceLocator.getRevenuService().delete((RevenuFullComplex) null);
            } catch (RevenuException re) {
                bDeleteNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la suppression d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bDeleteNullModel);

            // Suppression d'un revenuFullComplex
            // Résultat attendu : IsNew = false / codeActif=false
            RevenuFullComplex revenuFullComplexToDel = AmalServiceLocator.getRevenuService().readFullComplex("400");
            try {
                AmalServiceLocator.getRevenuService().delete(revenuFullComplexToDel);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la suppression d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertFalse(revenuFullComplexToDel.isNew());

        } catch (Exception e) {
            Assert.fail("Erreur générale lors du delete du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testDeleteHistoriqueComplex() {
        try {
            // Suppression d'un revenuHistoriqueComplex avec modèle null
            // Résultat attendu : RevenuException
            boolean bDeleteNullModel = false;
            try {
                AmalServiceLocator.getRevenuService().delete((RevenuHistoriqueComplex) null);
            } catch (RevenuException re) {
                bDeleteNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la suppression d'un revenu historique complex standard ! ==> "
                        + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bDeleteNullModel);

            // Suppression d'un revenuFullComplex
            // Résultat attendu : IsNew = false / codeActif=false
            RevenuHistoriqueComplex revenuHistoriqueComplexToDel = AmalServiceLocator.getRevenuService()
                    .readHistoriqueComplex("1706");
            try {
                AmalServiceLocator.getRevenuService().delete(revenuHistoriqueComplexToDel);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la suppression d'un revenu historique complex standard ! ==> "
                        + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertFalse(revenuHistoriqueComplexToDel.isNew());

        } catch (Exception e) {
            Assert.fail("Erreur générale lors du delete du Revenu Historique Complex--> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testReadFullComplex() {
        try {
            // Lecture d'un revenuFullComplex avec id NULL
            // Résultat attendu ==> OK
            boolean bReadNullId = false;
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex(null);
            } catch (RevenuException re) {
                bReadNullId = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            }
            Assert.assertTrue(bReadNullId);

            // Lecture d'un revenuFullComplex avec id qui n'existe pas
            // Résultat attendu ==> OK
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("-1");
                Assert.assertTrue(revenuFullComplex.isNew());
                Assert.assertTrue(revenuFullComplex.getSimpleRevenuContribuable().isNew());
                Assert.assertTrue(revenuFullComplex.getSimpleRevenuSourcier().isNew());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Lecture d'un revenuFullComplex
            // Résultat attendu ==> OK
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("400");
                Assert.assertEquals(revenuFullComplex.getId(), revenuFullComplex.getSimpleRevenu().getId());
                Assert.assertFalse(revenuFullComplex.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du Read du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testReadHistoriqueComplex() {
        try {
            // Lecture d'un revenuHistoriqueComplex avec id NULL
            // Résultat attendu ==> RevenuException
            boolean bReadNullId = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = AmalServiceLocator.getRevenuService()
                        .readHistoriqueComplex(null);
            } catch (RevenuException re) {
                bReadNullId = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu historique complex standard ! ==> " + e.toString());
            }
            Assert.assertTrue(bReadNullId);

            // Lecture d'un revenuFullComplex avec id qui n'existe pas
            // Résultat attendu ==> isNew == true
            boolean bReadIdNotExist = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = AmalServiceLocator.getRevenuService()
                        .readHistoriqueComplex("-1");
                Assert.assertTrue(revenuHistoriqueComplex.isNew());
            } catch (JadePersistenceException jpe) {
                bReadIdNotExist = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu historique complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bReadIdNotExist);

            // Lecture d'un revenuFullComplex
            // Résultat attendu ==> OK
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = AmalServiceLocator.getRevenuService()
                        .readHistoriqueComplex("1706");
                Assert.assertEquals(revenuHistoriqueComplex.getId(), revenuHistoriqueComplex
                        .getSimpleRevenuHistorique().getId());
                Assert.assertFalse(revenuHistoriqueComplex.isNew());
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu historique complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du Read du Revenu Historique Complex--> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearch() {
        try {
            // Recherche d'un revenuFullComplex avec modèle null
            // Résultat attendu ==> RevenuException
            boolean bSearchNullModel = false;
            try {
                RevenuFullComplexSearch revenuFullComplexSearch = (RevenuFullComplexSearch) AmalServiceLocator
                        .getRevenuService().search((RevenuFullComplexSearch) null);
            } catch (RevenuException re) {
                bSearchNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModel);

            // Recherche d'un revenuFullComplex avec un mauvais id
            // Résultat attendu ==> OK
            try {
                RevenuFullComplexSearch revenuFullComplexSearch = new RevenuFullComplexSearch();
                revenuFullComplexSearch.setForIdContribuable("-1");
                revenuFullComplexSearch = (RevenuFullComplexSearch) AmalServiceLocator.getRevenuService().search(
                        revenuFullComplexSearch);

                Assert.assertTrue(revenuFullComplexSearch.getSize() == 0);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Recherche d'un revenuFullComplex
            // Résultat attendu ==> OK
            try {
                RevenuFullComplexSearch revenuFullComplexSearch = new RevenuFullComplexSearch();
                revenuFullComplexSearch.setForIdContribuable("151");
                revenuFullComplexSearch = (RevenuFullComplexSearch) AmalServiceLocator.getRevenuService().search(
                        revenuFullComplexSearch);

                Assert.assertTrue(revenuFullComplexSearch.getSize() > 0);
                RevenuFullComplex revenuFullComplex = (RevenuFullComplex) revenuFullComplexSearch.getSearchResults()[0];
                Assert.assertFalse(revenuFullComplex.isNew());
                Assert.assertTrue("151".equals(revenuFullComplex.getSimpleRevenu().getIdContribuable()));
            } catch (Exception e) {
                Assert.fail("Erreur lors de la lecture d'un revenu full complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du search du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearchHistoriqueComplex() {
        try {
            // Recherche d'un revenHistoriqueComplex avec modèle null
            // Résultat attendu ==> RevenuException
            boolean bSearchNullModel = false;
            try {
                RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService()
                        .search((RevenuHistoriqueComplexSearch) null);
            } catch (RevenuException re) {
                bSearchNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur lors du search d'un revenu historique complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bSearchNullModel);

            // Recherche d'un revenuFullComplex avec un mauvais id
            // Résultat attendu ==> OK
            try {
                RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = new RevenuHistoriqueComplexSearch();
                revenuHistoriqueComplexSearch.setForIdContribuable("-1");
                revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService().search(
                        revenuHistoriqueComplexSearch);

                Assert.assertTrue(revenuHistoriqueComplexSearch.getSize() == 0);
            } catch (Exception e) {
                Assert.fail("Erreur lors de la recherche d'un revenu historique complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // Recherche d'un revenuHistoriqueComplex
            // Résultat attendu ==> OK
            try {
                RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = new RevenuHistoriqueComplexSearch();
                revenuHistoriqueComplexSearch.setForIdContribuable("151");
                revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService().search(
                        revenuHistoriqueComplexSearch);

                Assert.assertTrue(revenuHistoriqueComplexSearch.getSize() > 0);
                RevenuHistoriqueComplex revenuHistoriqueComplex = (RevenuHistoriqueComplex) revenuHistoriqueComplexSearch
                        .getSearchResults()[0];
                Assert.assertFalse(revenuHistoriqueComplex.isNew());
                Assert.assertTrue("151".equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                        .getIdContribuable()));
            } catch (Exception e) {
                Assert.fail("Erreur lors de la recherche d'un revenu historique complex standard ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors du search du Revenu historique Complex--> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testUpdate() {
        try {
            // Update d'un revenuFullComplex sourcier avec modèle null
            // Résultat attendu ==> RevenuException
            boolean bUpdateNullModel = false;
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().update(
                        (RevenuFullComplex) null);
            } catch (RevenuException re) {
                bUpdateNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bUpdateNullModel);

            // Update d'un revenuFullComplex sourcier avec modèle null
            // Résultat attendu ==> RevenuException
            boolean bUpdateModelNoTypeRevenu = false;
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().update(
                        (RevenuFullComplex) null);
            } catch (RevenuException re) {
                bUpdateModelNoTypeRevenu = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bUpdateModelNoTypeRevenu);

            // // Update d'un revenuFullComplex standard / modification nbre enfants / ajout d'une fortune de 100'000.00
            // // Résultat attendu ==> OK / Montants doivent correspondre
            // try {
            // RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("189513");
            // String revenuDet = revenuFullComplex.getSimpleRevenuContribuable().getRevenuDeterminantCalcul();
            // revenuFullComplex.getSimpleRevenu().setNbEnfants("1");
            // revenuFullComplex.getSimpleRevenuContribuable().setFortuneImposable("100000");
            // revenuFullComplex = AmalServiceLocator.getRevenuService().update(revenuFullComplex);
            // String revenuDet2 = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
            // Assert.assertFalse(revenuDet.equals(revenuDet2));
            // // Assert.assertTrue("63800".equals(revenuFullComplex.getSimpleRevenu().getRevenuImposableCalcul()));
            // // Assert.assertTrue("52800".equals(revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul()));
            // } catch (Exception e) {
            // Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            // } finally {
            // JadeThread.logClear();
            // }

            // // Update d'un revenuFullComplex standard /
            // // Résultat attendu ==> OK / Montants doivent correspondre
            // try {
            // RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("189513");
            // String revenuDet = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
            // revenuFullComplex.getSimpleRevenu().setNbEnfants("1");
            // revenuFullComplex.getSimpleRevenuContribuable().setFortuneImposable("100000");
            // revenuFullComplex = AmalServiceLocator.getRevenuService().update(revenuFullComplex);
            // String revenuDet2 = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
            // Assert.assertFalse(revenuDet.equals(revenuDet2));
            // // Assert.assertTrue("63800".equals(revenuFullComplex.getSimpleRevenu().getRevenuImposableCalcul()));
            // // Assert.assertTrue("52800".equals(revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul()));
            // } catch (Exception e) {
            // Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            // } finally {
            // JadeThread.logClear();
            // }

            // Update d'un revenuFullComplex sourcier / modification de l'état civil
            // Résultat attendu ==> OK / Montants doivent correspondre
            try {
                RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("400");
                // String revenuDet = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
                revenuFullComplex.getSimpleRevenu().setEtatCivil(IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE);
                revenuFullComplex = AmalServiceLocator.getRevenuService().update(revenuFullComplex);
                // String revenuDet2 = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
                Assert.assertTrue(IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE.equals(revenuFullComplex.getSimpleRevenu()
                        .getEtatCivil()));
                // Assert.assertFalse(revenuDet.equals(revenuDet2));
                // Assert.assertTrue("48332".equals(revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul()));
                // Assert.assertTrue("62332".equals(revenuFullComplex.getSimpleRevenu().getRevenuImposableCalcul()));
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            } finally {
                JadeThread.logClear();
            }

            // // Update d'un revenuFullComplex sourcier / modification du salaire annuel époux / ajout salaire mensuel
            // // épouse + nbmois
            // // Résultat attendu ==> Montant doivent correspondre : R. det = 80'205.00 / R. impo = 94205.00
            // try {
            // RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex("400");
            // String revenuDet = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
            // revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouxAnnuel("100000");
            // revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouseMensuel("2500");
            // revenuFullComplex.getSimpleRevenuSourcier().setNombreMois("12");
            // revenuFullComplex = AmalServiceLocator.getRevenuService().update(revenuFullComplex);
            // String revenuDet2 = "";// revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul();
            // Assert.assertFalse(revenuDet.equals(revenuDet2));
            // // Assert.assertTrue("80205".equals(revenuFullComplex.getSimpleRevenu().getRevenuDeterminantCalcul()));
            // // Assert.assertTrue("94205".equals(revenuFullComplex.getSimpleRevenu().getRevenuImposableCalcul()));
            // } catch (Exception e) {
            // Assert.fail("Erreur non gérée lors de l'update d'un revenu full complex sourcier ! ==> " + e.toString());
            // } finally {
            // JadeThread.logClear();
            // }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors de l'update du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testUpdateHistoriqueComplex() {
        try {
            // Update d'un revenuHistoriqueComplex sourcier avec modèle null
            // Résultat attendu ==> RevenuException
            boolean bUpdateNullModel = false;
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().update(
                        (RevenuHistoriqueComplex) null);
            } catch (RevenuException re) {
                bUpdateNullModel = true;
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu historique complex sourcier ! ==> "
                        + e.toString());
            } finally {
                JadeThread.logClear();
            }
            Assert.assertTrue(bUpdateNullModel);

            // Update d'un revenuFullComplex sourcier / Changement d'une taxation liée
            // Résultat attendu ==> OK / Montants doivent correspondre
            // Cas de test : Contribuable Trapletti Santo
            try {
                RevenuHistoriqueComplex revenuHistoriqueComplex = AmalServiceLocator.getRevenuService()
                        .readHistoriqueComplex("346216");
                String revenuDet = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().setId("615517");
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().setIdRevenu("615517");
                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().update(revenuHistoriqueComplex);
                String revenuDet2 = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();
                Assert.assertFalse(revenuDet.equals(revenuDet2));
                Assert.assertTrue("13321".equals(revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuDeterminantCalcul()));
                Assert.assertTrue("1700".equals(revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                        .getRevenuImposableCalcul()));
            } catch (Exception e) {
                Assert.fail("Erreur non gérée lors de l'update d'un revenu historique complex sourcier ! ==> "
                        + e.toString());
            } finally {
                JadeThread.logClear();
            }
        } catch (Exception e) {
            Assert.fail("Erreur générale lors de l'update du Revenu --> " + e.toString());
        } finally {
            // doFinally();
        }
    }
}