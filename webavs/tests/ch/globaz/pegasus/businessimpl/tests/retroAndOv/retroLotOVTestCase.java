package ch.globaz.pegasus.businessimpl.tests.retroAndOv;

import ch.globaz.pegasus.tests.util.BaseTestCase;

public class retroLotOVTestCase extends BaseTestCase {
    // select distinct tieravs.HXNAVS,tier.htldu1,tier.htldu2,tieravsConj.HXNAVS,tierConj.htldu1,tierConj.htldu2
    // from ccJuweb.SFMBRFAM as fam1
    // inner join ccjuweb.SFCONJOI as con
    // on fam1.WGIMEF = con.WJICO1
    // inner join ccjuweb.SFMBRFAM as fam2
    // on fam2.WGIMEF = con.WJICO2
    // inner join ccjuweb.SFRELCON as relationConjoint
    // on relationConjoint.WKICON = con.WJICON
    // inner join ccjuweb.TIPAVSP as tieravs
    // on tieravs.HTITIE = fam1.WGITIE
    // inner join ccjuweb.TITIERP as tier
    // on tier.HTITIE = fam1.WGITIE
    // inner join ccjuweb.TIPAVSP as tieravsConj
    // on tieravsConj.HTITIE = fam2.WGITIE
    // inner join ccjuweb.TITIERP as tierConj
    // on tierConj.HTITIE = fam2.WGITIE
    // inner join ccjuweb.TIAADRP as avoirAdresse
    // on tieravs.HTITIE = avoirAdresse.HTITIE
    // inner join ccjuweb.TIADREP as adresse
    // on avoirAdresse.HAIADR = adresse.HAIADR
    // inner join ccjuweb.TIAADRP as avoirAdresseConj
    // on tierConj.HTITIE = avoirAdresseConj.HTITIE
    // inner join ccjuweb.TIADREP as adresseConj
    // on avoirAdresseConj.HAIADR = adresseConj.HAIADR
    // inner join ccjuweb.TIAPAIP as avoirPaiment
    // on tieravs.HTITIE = avoirPaiment.HTITIE
    // inner join ccjuweb.TIADRPP as adressePaiment
    // on avoirPaiment.HIIAPA = adressePaiment.HIIAPA
    // inner join ccjuweb.TIAPAIP as avoirPaimentConj
    // on tieravsConj.HTITIE = avoirPaimentConj.HTITIE
    // inner join ccjuweb.TIADRPP as adressePaimentConj
    // on avoirPaimentConj.HIIAPA = adressePaimentConj.HIIAPA
    // where fam1.WGITIE not in (select WAITIE from ccjuweb.PRDEMAP)
    // and tierConj.htldu1 = tier.htldu1
    // and 0 = (select count(WIIENF) from CCJUWEB.SFENFANT where WIICON = con.WJICON )
    // order by tieravs.HXNAVS
    // String dateLot = null;
    // Map<String, DonneeForDonneeFinanciere> listDesDonneesCreer = new HashMap<String, DonneeForDonneeFinanciere>();
    // SimpleLot simpleLot = null;
    //
    // private DonneeForDonneeFinanciere createDemandDossierWithRenteAndExecuteCalculeAndVerifRetro(
    // double montantTotalRetro, String montantRente, String nss, String dateDepotDemande) throws Exception {
    //
    //
    //
    // DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(nss,
    // dateDepotDemande);
    //
    // this.listDesDonneesCreer.put(nss, data);
    //
    // DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
    // montantRente);
    //
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetro, data.getDroit());
    // return data;
    // }
    //
    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // this.simpleLot = UtilsLotForTestRetroAndOV.findLotPaiment();
    // this.dateLot = UtilsLotForTestRetroAndOV.findLotPaiment().getDateCreation();
    // UtilsLotForTestRetroAndOV.updateDateLot(this.simpleLot, "01.01.2012");
    // }
    //
    // @Override
    // public void tearDown() {
    // try {
    // this.simpleLot = CorvusServiceLocator.getLotService().read(this.simpleLot.getIdLot());
    // UtilsLotForTestRetroAndOV.updateDateLot(this.simpleLot, this.dateLot);
    // } catch (LotException e) {
    // e.printStackTrace();
    // } catch (JadeApplicationServiceNotAvailableException e) {
    // e.printStackTrace();
    // } catch (JadePersistenceException e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }
    //
    // /*
    // * Test un cas 2 rente et un créancier
    // * Une 3 personne dans la famille
    // * 756.0634.0081.15/01.07.1970
    // * Kottelat Eric
    // *
    // *
    // * Donneés d'entrées
    // * Creance: 200 CHF
    // * Rente initiale: 400 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancier)
    // */
    // public final void testCasSimpleAvecDeuxRentes() throws Exception {
    // String idVersionDroit;
    // double montantTotalRetro = 20566;
    // String montantRente = "400";
    // //String idTierRequerant = "150361";
    // String nss = "756.0634.0081.15";
    // String dateDepotDemande = "01.01.2011";
    // String dateDecision = "01.12.2011";
    // String idTierCreancier = "164289";
    //
    // DonneeForDonneeFinanciere data = this.createDemandDossierWithRenteAndExecuteCalculeAndVerifRetro(
    // montantTotalRetro, montantRente, nss, dateDepotDemande);
    //
    // idVersionDroit = data.getDroit().getSimpleVersionDroit().getIdVersionDroit();
    //
    // List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(idVersionDroit);
    //
    // DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "200", listPca
    // .get(0).getSimplePCAccordee().getId(), "200");
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(idVersionDroit, dateDecision);
    //
    // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
    // idVersionDroit, 5);
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetro, 4);
    //
    // List<OrdreVersement> listTiers = mapOv.get(IREOrdresVersements.CS_TYPE_TIERS);
    //
    // if (listTiers != null) {
    // Assert.assertEquals("Nombre d'ordres de versements de type dette: ", 1, listTiers.size());
    // Assert.assertEquals("Montant de l'ordre de versements de type dette: ", Float.valueOf(200),
    // Float.valueOf(listTiers.get(0).getSimpleOrdreVersement().getMontant()));
    // } else {
    // Assert.fail("Auncun ordre de versement de type dette n'a été trouvé");
    // }
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // /*
    // * Requérant
    // * 756.8762.3464.37
    // * SCHALLER MARC
    // *
    // * SCHALLER ELIANE 756.8015.6741.40
    // *
    // * Donneés d'entrées
    // * Rente initiale: 400 CHF
    // * Crance : 200 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancier)
    // * Ce cas a 1 des dettes en comptat
    // */
    // public final void testCasSimpleAvecDeuxRentesMaisSeparerParLaMaladie() throws Exception {
    // String idVersionDroit;
    // double montantTotalRetro = 70077;//70016;
    // String montantRente = "400";
    // //String idTierRequerant = "211399";
    // String nss = "756.8762.3464.37   ";
    // String dateDepotDemande = "01.01.2011";
    // String dateDecision = "01.12.2011";
    // String idHome = "309"; // 4 Fondation Clair-Logis 309 - Fondation Clair-Logis 309
    // String idTypeChembre = "40";
    // String idTierCreancier = "164289";
    //
    // DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(nss,
    // dateDepotDemande);
    // this.listDesDonneesCreer.put(nss, data);
    // idVersionDroit = data.getDroit().getSimpleVersionDroit().getIdVersionDroit();
    //
    // DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
    // montantRente);
    //
    // DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
    // .getDroit().getSimpleDroit().getIdDroit());
    // DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(idVersionDroit, droitMembreFamille, idHome, idTypeChembre,
    // "01.2011");
    //
    // // Execute le calcule et creer les descisions
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetro, data.getDroit());
    //
    // List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(idVersionDroit);
    //
    // DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "200", listPca
    // .get(0).getSimplePCAccordee().getId(), "200");
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(idVersionDroit, dateDecision);
    //
    // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
    // idVersionDroit, 9);
    //
    // List<OrdreVersement> listTiersCreancier = mapOv.get(IREOrdresVersements.CS_TYPE_TIERS);
    //
    // if (listTiersCreancier != null) {
    // Assert.assertEquals("Nombre d'ordres de versements de type dette: ", 1, listTiersCreancier.size());
    // Assert.assertEquals("Montant de l'ordre de versements de type dette: ", Float.valueOf(200),
    // Float.valueOf(listTiersCreancier.get(0).getSimpleOrdreVersement().getMontant()));
    // } else {
    // Assert.fail("Auncun ordre de versement de type dette(creancier) n'a été trouvé");
    // }
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetro, 4);
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // /*
    // * Test un cas un couple séparé par la maladie avec 2 versions de droits
    // * On rajoute un contrat d'entretien viager pour le conjoint dans la deuxième version
    // * 756.6835.5504.22
    // * Egli Hans /15.05.1940 / H
    // *
    // 756.4566.2924.13/03.06.1951
    // Egli Rosmarie
    // *
    // * Donneés d'entrées
    // * Rente initiale: 400 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancie
    // */
    // // @Test
    // // @Depends("#testCasSimpleAvecUneRente")
    // public final void testCasSimpleAvecDeuxRentesMaisSeparerParLaMaladieEtAvecUneNouvelleVersionDeDroit()
    // throws Exception {
    // double montantTotalRetroV1 = 70077;
    // double montantTotalRetroV2 = 720;
    // String montantRenteV1 = "400";
    // String montantRenteV2 = "300";
    // String dateNouvelleRente = "06.2011";
    // String dateAnnonce = "01.06.2011";
    // String dateDecisionV1 = "01.01.2011";
    // String dateDecisionV2 = "01.01.2012";
    // String nssRequerant = "756.6835.5504.22";
    // String dateDepotDemande = "01.01.2011";
    // String idHome = "309"; // 4 Fondation Clair-Logis 309 - Fondation Clair-Logis 309 String idVersionDroit;
    // String idTypeChembre = "40";
    // String idVersionDroit;
    // Map<String, List<OrdreVersement>> mapOv;
    //
    // DroitMembreFamille droitMembreFamille;
    //
    // /*************** Création du droit initial ************************/
    // DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
    // nssRequerant, dateDepotDemande);
    // this.listDesDonneesCreer.put(nssRequerant, data);
    // idVersionDroit = data.getDroit().getSimpleVersionDroit().getIdVersionDroit();
    //
    // DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
    // montantRenteV1);
    //
    // droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data.getDroit()
    // .getSimpleDroit().getIdDroit());
    // DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(idVersionDroit, droitMembreFamille, idHome, idTypeChembre,
    // "01.2011");
    //
    // // Execute le calcule et creer les descisions
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetroV1, data.getDroit());
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(idVersionDroit, dateDecisionV1);
    //
    // mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(idVersionDroit, 4);
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetroV1, 4);
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    //
    // /*************** Modification du droit initial et création du nouveau droit ************************/
    //
    // Droit droit = DonneeUtilsForTestRetroAndOV.findCurrentDroitByIdTiers(data.getIdTiers());
    //
    // //UtilsLotForTestRetroAndOV.findLotDecision();
    //
    // droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce,
    // IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
    //
    // droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit.getSimpleDroit()
    // .getIdDroit());
    //
    // DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, montantRenteV2,
    // dateNouvelleRente);
    //
    // DroitMembreFamille droitMembreFamilleConjoint = DonneeUtilsForTestRetroAndOV
    // .findDroitMembreFamilleRequerant(droit.getSimpleDroit().getIdDroit());
    //
    // DonneeUtilsForTestRetroAndOV.createContratEntretienViager(idVersionDroit, droitMembreFamilleConjoint, "120",
    // dateNouvelleRente);
    //
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetroV2, droit);
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(droit.getSimpleVersionDroit().getId(),
    // dateDecisionV2);
    //
    // mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit.getSimpleVersionDroit()
    // .getIdVersionDroit(), 8);
    //
    // this.verifiOVPourCasSimpleSansCreancierEtDette(montantTotalRetroV2, mapOv, 4);
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    //
    // }
    //
    // /*
    // * Test un cas simple avec une seule rente et un créancier
    // * Une seule personne dans la famille (le réquérant)
    // * 756.4561.0339.50
    // * Akal Dehab / 04.05.1973 / F / Erythrée
    // *
    // *
    // * Donneés d'entrées
    // * Creance: 200 CHF
    // * Rente initiale: 400 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancier)
    // * a une dette en comptat
    // */
    // public final void testCasSimpleAvecUneRente() throws Exception {
    //
    // String idVersionDroit;
    // double montantTotalRetro = 15444;
    // String montantRente = "400";
    // //String idTierRequerant = "163063";
    //
    // String nssRequerant = "756.4561.0339.50";
    // String dateDepotDemande = "01.01.2011";
    // String dateDecision = "01.12.2011";
    // String idTierCreancier = "164289";
    //
    // DonneeForDonneeFinanciere data = this.createDemandDossierWithRenteAndExecuteCalculeAndVerifRetro(
    // montantTotalRetro, montantRente, nssRequerant, dateDepotDemande);
    //
    // idVersionDroit = data.getDroit().getSimpleVersionDroit().getIdVersionDroit();
    //
    // List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(idVersionDroit);
    //
    // DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "200", listPca
    // .get(0).getSimplePCAccordee().getId(), "200");
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(idVersionDroit, dateDecision);
    //
    // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
    // idVersionDroit, 5);
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetro, 2);
    //
    // List<OrdreVersement> listTiers = mapOv.get(IREOrdresVersements.CS_TYPE_TIERS);
    //
    // if (listTiers != null) {
    // Assert.assertEquals("Nombre d'ordres de versements de type dette: ", 1, listTiers.size());
    // Assert.assertEquals("Montant de l'ordre de versements de type dette: ", Float.valueOf(200),
    // Float.valueOf(listTiers.get(0).getSimpleOrdreVersement().getMontant()));
    // } else {
    // Assert.fail("Auncun ordre de versement de type dette n'a été trouvé");
    // }
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // /*
    // * Test un cas simple avec une seule rente et un créancier
    // * Ce cas à déja une version de droit. On vas donc créer une 2 eme version
    // * On est dans un cas de retro positif car on à une diminution de rente
    // * Une seule personne dans la famille (le réquérant)
    // * 756.4561.0339.50
    // * Akal Dehab / 04.05.1973 / F / Erythrée
    // *
    // *
    // * Donneés d'entrées
    // * Rente initiale: 300 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancier)
    // */
    // // @Test
    // // @Depends("#testCasSimpleAvecUneRente")
    // public final void testCasSimpleAvecUneRenteEnAjoutantUneVersionDeDroitRetroPositif() throws Exception {
    // double montantTotalRetro = 800;
    // String montantRente = "300";
    // String dateAnnonce = "01.06.2011";
    // String dateDecision = "01.01.2012";
    // String nssRequerant = " 756.4561.0339.50";
    //
    // // DonneeForDonneeFinanciere data = this.listDesDonneesCreer.get("163063");
    //
    // Droit droit = DonneeUtilsForTestRetroAndOV.findCurrentDroitByIdTiers(nssRequerant);
    //
    // droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce,
    // IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
    //
    // // data.setDroit(droit);
    //
    // DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit
    // .getSimpleDroit().getIdDroit());
    //
    // DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, montantRente, "06.2011");
    //
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetro, droit);
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(droit.getSimpleVersionDroit().getId(),
    // dateDecision);
    //
    // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit
    // .getSimpleVersionDroit().getIdVersionDroit(), 6);
    //
    // this.verifiOVPourCasSimpleSansCreancierEtDette(montantTotalRetro, mapOv, 2);
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // /*
    // * Test un cas simple avec une seule rente et un créancier
    // * Ce cas à déja une version de droit. On vas donc créer une 2 eme version
    // * On est dans un cas de retro positif car on à une diminution de rente
    // * Une seule personne dans la famille (le réquérant)
    // * 756.6638.8778.01
    // * Barthe Denis / 09.07.1956 / H
    // *
    // *
    // * Donneés d'entrées
    // * Rente initiale: 300 CHF
    // *
    // * Sortie
    // * OV: 3 (2 pour le bénéficiaire et 1 pour le créancier)
    // */
    // // @Test
    // // @Depends("#testCasSimpleAvecUneRente")
    // public final void testCasSimpleAvecUneRenteEtUneVersionDeDroitRetroNegaif() throws Exception {
    // double montantTotalRetroV1 = 15444;
    // double montantTotalRetroV2 = -800;
    // String montantRenteV1 = "400";
    // String montantRenteV2 = "500";
    // String dateAnnonce = "01.06.2011";
    // String dateDecisionV1 = "01.01.2011";
    // String dateDecisionV2 = "01.01.2012";
    // String nssRequerant = "756.6638.8778.01";
    // String dateDepotDemande = "01.01.2011";
    //
    // String idVersionDroit;
    // Map<String, List<OrdreVersement>> mapOv;
    //
    // /*************** Création du droit initial ************************/
    // DonneeForDonneeFinanciere data = this.createDemandDossierWithRenteAndExecuteCalculeAndVerifRetro(
    // montantTotalRetroV1, montantRenteV1, nssRequerant, dateDepotDemande);
    //
    // idVersionDroit = data.getDroit().getSimpleVersionDroit().getIdVersionDroit();
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(idVersionDroit, dateDecisionV1);
    //
    // mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(idVersionDroit, 2);
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetroV1, 2);
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    //
    // /*************** Modification du droit initial et création du nouveau droit ************************/
    //
    // Droit droit = DonneeUtilsForTestRetroAndOV.findCurrentDroitByIdTiers(data.getIdTiers());
    //
    // droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce,
    // IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
    //
    // DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit
    // .getSimpleDroit().getIdDroit());
    //
    // DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, montantRenteV2, "06.2011");
    //
    // DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetroV2, droit);
    //
    // DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndValide(droit.getSimpleVersionDroit().getId(),
    // dateDecisionV2);
    //
    // mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit.getSimpleVersionDroit()
    // .getIdVersionDroit(), 4);
    //
    // this.verifiOVPourCasSimpleSansCreancierEtDette(montantTotalRetroV2, mapOv, 2);
    //
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    //
    // }
    //
    // private void verifiOVPourCasSimpleSansCreancierEtDette(double montantTotalRetroV2,
    // Map<String, List<OrdreVersement>> mapOv, int nbOVRestitution) {
    // List<OrdreVersement> listTiers = mapOv.get(IREOrdresVersements.CS_TYPE_TIERS);
    //
    // List<OrdreVersement> listRestitution = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
    //
    // List<OrdreVersement> listDetteComptat = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE);
    //
    // if (listTiers != null) {
    // Assert.fail("Une dette de type creancier existe! Mais cela ne devrait pas arriver");
    // }
    //
    // if (listDetteComptat != null) {
    // //Assert.fail("Une dette de type comptat auxilière existe! Mais cela ne devrait pas arriver");
    // }
    //
    // if (listRestitution != null) {
    // Assert.assertEquals("Nombre d'ordres de versements de type restitution: ", nbOVRestitution,
    // listRestitution.size());
    // } else {
    // Assert.fail("Auncun ordre de versement de type restitution n'a été trouvé");
    // }
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantTotalRetroV2, nbOVRestitution);
    // }

}
