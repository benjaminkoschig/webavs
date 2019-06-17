package globaz.corvus.helpers.acor;

import acor.FCalcul;
import acor.Rente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.exceptions.REBusinessException;

import java.util.*;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.BaseCalcul;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteInvalidite;
import ch.globaz.corvus.domaine.DemandeRenteSurvivant;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.DossierPrestation;
import ch.globaz.prestation.domaine.InformationsComplementaires;
import ch.globaz.pyxis.domaine.PersonneAVS;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RECalculACORDemandeRenteHelper.class, BSession.class, BTransaction.class, RERenteAccordeeManager.class, PRTiersHelper.class, REBasesCalcul.class})
public class RECalculACORDemandeRenteHelperTest extends TestUnitaireAvecGenerateurIDUnique {

    private void laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(final DemandeRente copie,
                                                                    final DemandeRente demandeInitiale) {
        Assert.assertEquals("La date de traitement doit être celle de la demande intiale",
                demandeInitiale.getDateTraitement(), copie.getDateTraitement());
    }

    private boolean laRenteEstDansCetteDemande(final RenteAccordee rente, final DemandeRente demande) {

        for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
            if (uneRenteDeLaDemande.equals(rente)) {
                return true;
            }
        }

        return false;
    }

    private void lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(final DemandeRente demande1,
                                                                               final DemandeRente demande2) {
        String message = "ce n'est pas la bonne demande qui a été copiée";

        Assert.assertEquals(message, demande1.getDateDepot(), demande2.getDateDepot());
        Assert.assertEquals(message, demande1.getDossier(), demande2.getDossier());
        Assert.assertEquals(message, demande1.getGestionnaire(), demande2.getGestionnaire());
        Assert.assertEquals(message, demande1.getTypeDemandeRente(), demande2.getTypeDemandeRente());

        Assert.assertNotEquals("Les informations complémentaires de la demande ne doivent pas être copiées",
                demande1.getInformationsComplementaires(), demande2.getInformationsComplementaires());
    }

    @Test
    public void testAjournementAvecDejaUneDemandeCalculeePourLeConjoint() throws Exception {

        DemandeRente demandeInitiale = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        demandeInitiale.setDossier(dossier);

        DemandeRente demandeConjoint = new DemandeRenteVieillesse();
        demandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setDateDebutDuDroitInitial("01.01.2012");
        demandeConjoint.setDateDepot("02.01.2012");
        demandeConjoint.setDateFinDuDroitInitial("");
        demandeConjoint.setDateTraitement("03.01.2012");
        demandeConjoint.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeConjoint = new InformationsComplementaires();
        infoComplPourDemandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setInformationsComplementaires(infoComplPourDemandeConjoint);

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointRequerant);
        demandeConjoint.setDossier(dossierConjoint);

        RenteAccordee renteAccorde01Requerant = new RenteAccordee();
        renteAccorde01Requerant.setId(genererUnIdUnique());
        renteAccorde01Requerant.setBeneficiaire(requerant);
        renteAccorde01Requerant.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01Requerant.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculRequerant = new BaseCalcul();
        baseCalculRequerant.setId(genererUnIdUnique());
        baseCalculRequerant.setDonneurDeDroit(requerant);
        renteAccorde01Requerant.setBaseCalcul(baseCalculRequerant);

        RenteAccordee renteAccordee02Conjoint = new RenteAccordee();
        renteAccordee02Conjoint.setId(genererUnIdUnique());
        renteAccordee02Conjoint.setBeneficiaire(conjointRequerant);
        renteAccordee02Conjoint.setCodePrestation(CodePrestation.CODE_10);
        renteAccordee02Conjoint.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        RenteAccordee ancienneRenteConjoint = new RenteAccordee();
        ancienneRenteConjoint.setId(genererUnIdUnique());
        ancienneRenteConjoint.setBeneficiaire(conjointRequerant);
        ancienneRenteConjoint.setCodePrestation(CodePrestation.CODE_10);
        ancienneRenteConjoint.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculConjoint = new BaseCalcul();
        baseCalculConjoint.setId(genererUnIdUnique());
        baseCalculConjoint.setDonneurDeDroit(conjointRequerant);
        renteAccordee02Conjoint.setBaseCalcul(baseCalculConjoint);
        ancienneRenteConjoint.setBaseCalcul(baseCalculConjoint);

        demandeInitiale.setRentesAccordees(Arrays.asList(renteAccorde01Requerant, renteAccordee02Conjoint));
        demandeConjoint.setRentesAccordees(Arrays.asList(ancienneRenteConjoint));

        demandesDeLaFamille.add(demandeConjoint);

        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        Assert.assertEquals(
                "La demande du conjoint ne doit pas être copiée car elle est encore modifiable (n'est pas dans l'état validé)",
                demandeConjoint.getId(), copie.getId());
    }

    @Test
    public void testAjournementAvecRentesPourEnfantSurMemeBaseCalculRequerant() throws Exception {

        DemandeRente nouvelleDemande = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        PersonneAVS enfantRequerant = new PersonneAVS();
        enfantRequerant.setId(genererUnIdUnique());

        nouvelleDemande.setId(genererUnIdUnique());
        nouvelleDemande.setEtat(EtatDemandeRente.CALCULE);
        nouvelleDemande.setDateDebutDuDroitInitial("01.01.2014");
        nouvelleDemande.setDateDepot("02.01.2014");
        nouvelleDemande.setDateFinDuDroitInitial("");
        nouvelleDemande.setDateTraitement("03.01.2014");

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        nouvelleDemande.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        nouvelleDemande.setDossier(dossier);

        DemandeRente ancienneDemande = new DemandeRenteVieillesse();
        ancienneDemande.setId(genererUnIdUnique());
        ancienneDemande.setEtat(EtatDemandeRente.CALCULE);
        ancienneDemande.setDateDebutDuDroitInitial("01.01.2010");
        ancienneDemande.setDateDepot("02.01.2010");
        ancienneDemande.setDateFinDuDroitInitial("");
        ancienneDemande.setDateTraitement("03.01.2010");
        ancienneDemande.setEtat(EtatDemandeRente.VALIDE);

        InformationsComplementaires infoComplPourAncienneDemande = new InformationsComplementaires();
        infoComplPourAncienneDemande.setId(genererUnIdUnique());
        ancienneDemande.setInformationsComplementaires(infoComplPourAncienneDemande);

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointRequerant);
        ancienneDemande.setDossier(dossierConjoint);

        RenteAccordee renteAccorde01NouvelleDemandeSansCodeCasSpecial = new RenteAccordee();
        renteAccorde01NouvelleDemandeSansCodeCasSpecial.setId(genererUnIdUnique());
        renteAccorde01NouvelleDemandeSansCodeCasSpecial.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01NouvelleDemandeSansCodeCasSpecial.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente01 = new BaseCalcul();
        baseCalculRente01.setId(genererUnIdUnique());
        baseCalculRente01.setDonneurDeDroit(requerant);
        renteAccorde01NouvelleDemandeSansCodeCasSpecial.setBaseCalcul(baseCalculRente01);

        RenteAccordee renteAccorde02NouvelleDemandeAvecCodeCasSpecial08 = new RenteAccordee();
        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08.setId(genererUnIdUnique());
        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08
                .ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente02 = new BaseCalcul();
        baseCalculRente02.setId(genererUnIdUnique());
        baseCalculRente02.setDonneurDeDroit(conjointRequerant);
        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08.setBaseCalcul(baseCalculRente02);

        RenteAccordee renteAccorde03AncienneDemande = new RenteAccordee();
        renteAccorde03AncienneDemande.setId(genererUnIdUnique());
        renteAccorde03AncienneDemande.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde03AncienneDemande.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        renteAccorde03AncienneDemande.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente03 = new BaseCalcul();
        baseCalculRente03.setId(genererUnIdUnique());
        baseCalculRente03.setDonneurDeDroit(conjointRequerant);
        renteAccorde03AncienneDemande.setBaseCalcul(baseCalculRente03);

        ancienneDemande.setRentesAccordees(Arrays.asList(renteAccorde03AncienneDemande));
        nouvelleDemande.setRentesAccordees(Arrays.asList(renteAccorde01NouvelleDemandeSansCodeCasSpecial,
                renteAccorde02NouvelleDemandeAvecCodeCasSpecial08));

        demandesDeLaFamille.add(ancienneDemande);

        /**
         * Arrivée du requérant à l'âge AVS, avec son conjoint ayant ajourné précédemment
         *
         * <pre>
         * Nouvelle demande (au nom du requérant)  ----  Rente01 (au bénéfice du requérant) sans code cas spécial
         *                                          `--  Rente02 (au bénéfice du conjoint) avec code cas spécial 08
         *
         * Ancienne demande (au nom du conjoint)   ----  Rente03 (au bénéfice du conjoint) avec code cas spécial 08
         * </pre>
         *
         * L'ancienne demande doit être copiée, et sur cette copie doit être rattachée la rente02
         */
        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(nouvelleDemande,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(ancienneDemande, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, nouvelleDemande);
        Assert.assertTrue(
                "La rente avec le code cas spécial 08 doit se trouver dans la nouvelle demande si le bénéficiaire n'est pas le requérant de la demande initiale",
                laRenteEstDansCetteDemande(renteAccorde02NouvelleDemandeAvecCodeCasSpecial08, copie));
        Assert.assertFalse(
                "La rente avec le code cas spécial 08 ne doit plus être rattachée à la demande intitiale quand le bénéficiaire de cette rente n'est pas le requérant",
                laRenteEstDansCetteDemande(renteAccorde02NouvelleDemandeAvecCodeCasSpecial08, nouvelleDemande));

        renteAccorde01NouvelleDemandeSansCodeCasSpecial.setBeneficiaire(conjointRequerant);
        baseCalculRente01.setDonneurDeDroit(conjointRequerant);

        renteAccorde02NouvelleDemandeAvecCodeCasSpecial08.setBeneficiaire(requerant);
        baseCalculRente02.setDonneurDeDroit(requerant);

        renteAccorde03AncienneDemande.setBeneficiaire(conjointRequerant);
        renteAccorde03AncienneDemande.supprimerCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        baseCalculRente03.setDonneurDeDroit(conjointRequerant);

        ancienneDemande.setRentesAccordees(Arrays.asList(renteAccorde03AncienneDemande));
        nouvelleDemande.setRentesAccordees(Arrays.asList(renteAccorde01NouvelleDemandeSansCodeCasSpecial,
                renteAccorde02NouvelleDemandeAvecCodeCasSpecial08));

        /**
         * Arrivée du requérant à l'âge AVS, celui-ci veut ajourner. Son conjoint est déjà à l'AVS (et a donc déjà une
         * demande et une rente)
         *
         * <pre>
         * Nouvelle demande (au nom du requérant)  ----  Rente01 (au bénéfice du requérant) avec code cas spécial 08
         *                                          `--  Rente02 (au bénéfice du conjoint) sans code cas spécial
         *
         * Ancienne demande (au nom du conjoint)   ----  Rente03 (au bénéfice du conjoint) sans code cas spécial
         * </pre>
         *
         * L'ancienne demande doit être copiée, et sur cette copie doit être rattachée la rente02
         */
        copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(nouvelleDemande,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(ancienneDemande, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, nouvelleDemande);
        Assert.assertTrue(
                "La rente avec le code cas spécial 08 doit rester sur la demande initiale si le requérant est le bénéficiaire de la rente",
                laRenteEstDansCetteDemande(renteAccorde02NouvelleDemandeAvecCodeCasSpecial08, nouvelleDemande));
        Assert.assertFalse(
                "La rente sans code cas spécial doit être détachée de la demande initiale si cette demande comporte une rente avec code cas spécial 08",
                laRenteEstDansCetteDemande(renteAccorde01NouvelleDemandeSansCodeCasSpecial, nouvelleDemande));
        Assert.assertTrue(
                "La rente sans code cas spécial doit être attachée à la copie lorsque la demande initiale comporte une rente avec code cas spécial 08",
                laRenteEstDansCetteDemande(renteAccorde01NouvelleDemandeSansCodeCasSpecial, copie));
    }

    @Test
    public void testAjournementAvecUneDemandeAuBeneficeDuConjoint() throws Exception {

        DemandeRente demandeInitiale = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        demandeInitiale.setDossier(dossier);

        DemandeRente demandeDuConjoint = new DemandeRenteVieillesse();
        demandeDuConjoint.setId(genererUnIdUnique());
        demandeDuConjoint.setEtat(EtatDemandeRente.CALCULE);
        demandeDuConjoint.setDateDebutDuDroitInitial("01.01.2010");
        demandeDuConjoint.setDateDepot("02.01.2010");
        demandeDuConjoint.setDateFinDuDroitInitial("");
        demandeDuConjoint.setDateTraitement("03.01.2010");
        demandeDuConjoint.setEtat(EtatDemandeRente.VALIDE);

        InformationsComplementaires infoComplPourDemandeConjoint = new InformationsComplementaires();
        infoComplPourDemandeConjoint.setId(genererUnIdUnique());
        demandeDuConjoint.setInformationsComplementaires(infoComplPourDemandeConjoint);

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointRequerant);
        demandeDuConjoint.setDossier(dossierConjoint);

        RenteAccordee renteAccorde01SansCodeCasSpecial = new RenteAccordee();
        renteAccorde01SansCodeCasSpecial.setId(genererUnIdUnique());
        renteAccorde01SansCodeCasSpecial.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01SansCodeCasSpecial.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente01 = new BaseCalcul();
        baseCalculRente01.setId(genererUnIdUnique());
        baseCalculRente01.setDonneurDeDroit(requerant);
        renteAccorde01SansCodeCasSpecial.setBaseCalcul(baseCalculRente01);

        RenteAccordee renteAccorde02AvecCodeCasSpecial08 = new RenteAccordee();
        renteAccorde02AvecCodeCasSpecial08.setId(genererUnIdUnique());
        renteAccorde02AvecCodeCasSpecial08.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde02AvecCodeCasSpecial08.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        renteAccorde02AvecCodeCasSpecial08.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente02 = new BaseCalcul();
        baseCalculRente02.setId(genererUnIdUnique());
        baseCalculRente02.setDonneurDeDroit(conjointRequerant);
        renteAccorde02AvecCodeCasSpecial08.setBaseCalcul(baseCalculRente02);

        RenteAccordee renteAccorde03AncienneDemande = new RenteAccordee();
        renteAccorde03AncienneDemande.setId(genererUnIdUnique());
        renteAccorde03AncienneDemande.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde03AncienneDemande.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        renteAccorde03AncienneDemande.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente03 = new BaseCalcul();
        baseCalculRente03.setId(genererUnIdUnique());
        baseCalculRente03.setDonneurDeDroit(conjointRequerant);
        renteAccorde03AncienneDemande.setBaseCalcul(baseCalculRente03);

        demandeDuConjoint.setRentesAccordees(Arrays.asList(renteAccorde03AncienneDemande));
        demandeInitiale.setRentesAccordees(Arrays.asList(renteAccorde01SansCodeCasSpecial,
                renteAccorde02AvecCodeCasSpecial08));

        demandesDeLaFamille.add(demandeDuConjoint);

        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(demandeDuConjoint, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, demandeInitiale);
        Assert.assertTrue(
                "La rente avec le code cas spécial 08 doit se trouver dans la nouvelle demande si le bénéficiaire n'est pas le requérant de la demande initiale",
                laRenteEstDansCetteDemande(renteAccorde02AvecCodeCasSpecial08, copie));
        Assert.assertFalse(
                "La rente avec le code cas spécial 08 ne doit plus être rattachée à la demande intitiale quand le bénéficiaire de cette rente n'est pas le requérant",
                laRenteEstDansCetteDemande(renteAccorde02AvecCodeCasSpecial08, demandeInitiale));
    }

    @Test
    public void testAjournementRequerantEtConjointVoulantAjourner() throws Exception {

        /**
         * <pre>
         * Simulation d'une reprise d'un cas fait avant le mandat D0095 :
         *
         * 		- Le requérant avait ajourné avant son épouse
         * 		- L'épouse du requérant a ajourné en arrivant à l'âge AVS
         * 		- Vu que fait sous l'ancien système, les deux demandes sont à l'état calculé
         * 		- On calcul et importe sur la demande du requérant
         *
         * 	But du test : vérifier qu'aucune demande ne soit copiée, mais qu'elles soient utilisées
         * 	tel quel (elle sont à l'état calculé)
         * </pre>
         */

        DemandeRente demandeInitiale = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2012");
        demandeInitiale.setDateDepot("02.01.2012");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2012");
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossierRequerant = new DossierPrestation();
        dossierRequerant.setId(genererUnIdUnique());
        dossierRequerant.setRequerant(requerant);
        demandeInitiale.setDossier(dossierRequerant);

        DemandeRente demandeConjoint = new DemandeRenteVieillesse();
        demandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setDateDebutDuDroitInitial("01.01.2014");
        demandeConjoint.setDateDepot("02.01.2014");
        demandeConjoint.setDateFinDuDroitInitial("");
        demandeConjoint.setDateTraitement("03.01.2014");
        demandeConjoint.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeConjoint = new InformationsComplementaires();
        infoComplPourDemandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setInformationsComplementaires(infoComplPourDemandeConjoint);

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointRequerant);
        demandeConjoint.setDossier(dossierConjoint);

        RenteAccordee renteAccorde01Requerant = new RenteAccordee();
        renteAccorde01Requerant.setId(genererUnIdUnique());
        renteAccorde01Requerant.setBeneficiaire(requerant);
        renteAccorde01Requerant.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01Requerant.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculRequerant = new BaseCalcul();
        baseCalculRequerant.setId(genererUnIdUnique());
        baseCalculRequerant.setDonneurDeDroit(requerant);
        renteAccorde01Requerant.setBaseCalcul(baseCalculRequerant);

        RenteAccordee renteAccordee02Conjoint = new RenteAccordee();
        renteAccordee02Conjoint.setId(genererUnIdUnique());
        renteAccordee02Conjoint.setBeneficiaire(conjointRequerant);
        renteAccordee02Conjoint.setCodePrestation(CodePrestation.CODE_10);
        renteAccordee02Conjoint.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        RenteAccordee ancienneRenteRequerant = new RenteAccordee();
        ancienneRenteRequerant.setId(genererUnIdUnique());
        ancienneRenteRequerant.setBeneficiaire(conjointRequerant);
        ancienneRenteRequerant.setCodePrestation(CodePrestation.CODE_10);
        ancienneRenteRequerant.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculConjoint = new BaseCalcul();
        baseCalculConjoint.setId(genererUnIdUnique());
        baseCalculConjoint.setDonneurDeDroit(conjointRequerant);
        renteAccordee02Conjoint.setBaseCalcul(baseCalculConjoint);
        ancienneRenteRequerant.setBaseCalcul(baseCalculConjoint);

        demandeInitiale.setRentesAccordees(Arrays.asList(renteAccorde01Requerant, renteAccordee02Conjoint));
        demandeConjoint.setRentesAccordees(Arrays.asList(ancienneRenteRequerant));

        demandesDeLaFamille.add(demandeConjoint);

        DemandeRente demandeResultat = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(
                demandeInitiale, demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        Assert.assertEquals(
                "la demande du conjoint ne doit pas être copiée, mais utilisée tel quel car elle est à l'état calculé",
                demandeConjoint.getId(), demandeResultat.getId());
        laRenteEstDansCetteDemande(ancienneRenteRequerant, demandeInitiale);
        laRenteEstDansCetteDemande(renteAccorde01Requerant, demandeInitiale);
        laRenteEstDansCetteDemande(renteAccordee02Conjoint, demandeConjoint);
    }

    @Test
    public void testIncarcerationRequerantIncarcereAvecDesRentesComplementairesPourEnfant() throws Exception {

        DemandeRente demandeInitiale = new DemandeRenteInvalidite();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS enfant1 = new PersonneAVS();
        requerant.setId(genererUnIdUnique());
        PersonneAVS enfant2 = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        demandeInitiale.setDossier(dossier);

        BaseCalcul baseCalculRequerant = new BaseCalcul();
        baseCalculRequerant.setId(genererUnIdUnique());
        baseCalculRequerant.setDonneurDeDroit(requerant);

        RenteAccordee rentePrincipaleAvecIncarceration = new RenteAccordee();
        rentePrincipaleAvecIncarceration.setId(genererUnIdUnique());
        rentePrincipaleAvecIncarceration.setCodePrestation(CodePrestation.CODE_50);
        rentePrincipaleAvecIncarceration.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_07);
        rentePrincipaleAvecIncarceration.setBeneficiaire(requerant);
        rentePrincipaleAvecIncarceration.setBaseCalcul(baseCalculRequerant);

        RenteAccordee renteComplementaireEnfant1 = new RenteAccordee();
        renteComplementaireEnfant1.setId(genererUnIdUnique());
        renteComplementaireEnfant1.setCodePrestation(CodePrestation.CODE_54);
        renteComplementaireEnfant1.setBeneficiaire(enfant1);
        renteComplementaireEnfant1.setBaseCalcul(baseCalculRequerant);

        RenteAccordee renteComplementaireEnfant2 = new RenteAccordee();
        renteComplementaireEnfant2.setId(genererUnIdUnique());
        renteComplementaireEnfant2.setCodePrestation(CodePrestation.CODE_54);
        renteComplementaireEnfant2.setBeneficiaire(enfant2);
        renteComplementaireEnfant2.setBaseCalcul(baseCalculRequerant);

        demandeInitiale.setRentesAccordees(Arrays.asList(rentePrincipaleAvecIncarceration, renteComplementaireEnfant1,
                renteComplementaireEnfant2));

        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_07);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(demandeInitiale, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, demandeInitiale);
        Assert.assertTrue("La rente avec le code cas spécial 07 doit être sur la demande copiée",
                laRenteEstDansCetteDemande(rentePrincipaleAvecIncarceration, copie));
        Assert.assertFalse(
                "Les rentes complémentaires pour enfants ne doivent pas être sur la même demande que la rente principale, car la rente princpale comporte un code cas spécial 07",
                laRenteEstDansCetteDemande(renteComplementaireEnfant1, copie));
        Assert.assertFalse(
                "Les rentes complémentaires pour enfants ne doivent pas être sur la même demande que la rente principale, car la rente princpale comporte un code cas spécial 07",
                laRenteEstDansCetteDemande(renteComplementaireEnfant2, copie));
    }

    @Test
    public void testAjournementRequerantVoulantAjournerEtConjointDejaAjourne() throws REBusinessException {

        DemandeRente demandeInitiale = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossierRequerant = new DossierPrestation();
        dossierRequerant.setId(genererUnIdUnique());
        dossierRequerant.setRequerant(requerant);
        demandeInitiale.setDossier(dossierRequerant);

        DemandeRente demandeConjoint = new DemandeRenteVieillesse();
        demandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setDateDebutDuDroitInitial("01.01.2012");
        demandeConjoint.setDateDepot("02.01.2012");
        demandeConjoint.setDateFinDuDroitInitial("");
        demandeConjoint.setDateTraitement("03.01.2012");
        demandeConjoint.setEtat(EtatDemandeRente.VALIDE);

        InformationsComplementaires infoComplPourDemandeConjoint = new InformationsComplementaires();
        infoComplPourDemandeConjoint.setId(genererUnIdUnique());
        demandeConjoint.setInformationsComplementaires(infoComplPourDemandeConjoint);

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointRequerant);
        demandeConjoint.setDossier(dossierConjoint);

        RenteAccordee renteAccorde01Requerant = new RenteAccordee();
        renteAccorde01Requerant.setId(genererUnIdUnique());
        renteAccorde01Requerant.setBeneficiaire(requerant);
        renteAccorde01Requerant.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01Requerant.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculRequerant = new BaseCalcul();
        baseCalculRequerant.setId(genererUnIdUnique());
        baseCalculRequerant.setDonneurDeDroit(requerant);
        renteAccorde01Requerant.setBaseCalcul(baseCalculRequerant);

        RenteAccordee renteAccordee02Conjoint = new RenteAccordee();
        renteAccordee02Conjoint.setId(genererUnIdUnique());
        renteAccordee02Conjoint.setBeneficiaire(conjointRequerant);
        renteAccordee02Conjoint.setCodePrestation(CodePrestation.CODE_10);
        renteAccordee02Conjoint.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        RenteAccordee ancienneRenteConjoint = new RenteAccordee();
        ancienneRenteConjoint.setId(genererUnIdUnique());
        ancienneRenteConjoint.setBeneficiaire(conjointRequerant);
        ancienneRenteConjoint.setCodePrestation(CodePrestation.CODE_10);
        ancienneRenteConjoint.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        BaseCalcul baseCalculConjoint = new BaseCalcul();
        baseCalculConjoint.setId(genererUnIdUnique());
        baseCalculConjoint.setDonneurDeDroit(conjointRequerant);
        renteAccordee02Conjoint.setBaseCalcul(baseCalculConjoint);
        ancienneRenteConjoint.setBaseCalcul(baseCalculConjoint);

        demandeInitiale.setRentesAccordees(Arrays.asList(renteAccorde01Requerant, renteAccordee02Conjoint));
        demandeConjoint.setRentesAccordees(Arrays.asList(ancienneRenteConjoint));

        demandesDeLaFamille.add(demandeConjoint);

        /**
         * Le conjoint est déjà à l'âge AVS, mais a ajourné sa rente. Le requérant arrive à l'âge AVS et veut aussi
         * ajourner sa rente. On doit copier la demande du conjoint pour y coller sa nouvelle rente ajournée, et la
         * rente du requérant doit rester sur la demande initiale (qui est à son nom)
         */
        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(demandeConjoint, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, demandeInitiale);
        Assert.assertTrue("La rente du requérant doit se trouver sur la demande intiale",
                laRenteEstDansCetteDemande(renteAccorde01Requerant, demandeInitiale));
        Assert.assertTrue("La rente du conjoint doit se trouver sur la copie",
                laRenteEstDansCetteDemande(renteAccordee02Conjoint, copie));
    }

    @Test
    public void testAjournementSansDemandePourLeConjointAlorsQueBaseDeCalculPourLui() {

        DemandeRente demandeInitiale = new DemandeRenteVieillesse();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");
        demandeInitiale.setEtat(EtatDemandeRente.CALCULE);

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        demandeInitiale.setDossier(dossier);

        RenteAccordee renteAccorde01SansCodeCasSpecial = new RenteAccordee();
        renteAccorde01SansCodeCasSpecial.setId(genererUnIdUnique());
        renteAccorde01SansCodeCasSpecial.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde01SansCodeCasSpecial.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente01 = new BaseCalcul();
        baseCalculRente01.setId(genererUnIdUnique());
        baseCalculRente01.setDonneurDeDroit(requerant);
        renteAccorde01SansCodeCasSpecial.setBaseCalcul(baseCalculRente01);

        RenteAccordee renteAccorde02AvecCodeCasSpecial08 = new RenteAccordee();
        renteAccorde02AvecCodeCasSpecial08.setId(genererUnIdUnique());
        renteAccorde02AvecCodeCasSpecial08.setCodePrestation(CodePrestation.CODE_10);
        renteAccorde02AvecCodeCasSpecial08.ajouterCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        renteAccorde02AvecCodeCasSpecial08.setBeneficiaire(conjointRequerant);

        BaseCalcul baseCalculRente02 = new BaseCalcul();
        baseCalculRente02.setId(genererUnIdUnique());
        baseCalculRente02.setDonneurDeDroit(conjointRequerant);
        renteAccorde02AvecCodeCasSpecial08.setBaseCalcul(baseCalculRente02);

        demandeInitiale.setRentesAccordees(Arrays.asList(renteAccorde01SansCodeCasSpecial,
                renteAccorde02AvecCodeCasSpecial08));

        try {
            RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale, demandesDeLaFamille,
                    CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
            Assert.fail("Le fait qu'une base de calcul ne soit pas liée au réquerant, et qu'aucune demande n'émane de ce tiers base calcul doit soulever un message d'erreur (il doit exister une demande pour chaque tiers bases calculs)");
        } catch (REBusinessException ex) {
            // ok
        }
    }

    @Test
    public void testAjournementSansRenteDansLaDemandeInitiale() throws Exception {

        DemandeRente demandeInitiale = new DemandeRenteInvalidite();
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        PersonneAVS conjointRequerant = new PersonneAVS();
        conjointRequerant.setId(genererUnIdUnique());

        demandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setDateDebutDuDroitInitial("01.01.2014");
        demandeInitiale.setDateDepot("02.01.2014");
        demandeInitiale.setDateFinDuDroitInitial("");
        demandeInitiale.setDateTraitement("03.01.2014");

        InformationsComplementaires infoComplPourDemandeInitiale = new InformationsComplementaires();
        infoComplPourDemandeInitiale.setId(genererUnIdUnique());
        demandeInitiale.setInformationsComplementaires(infoComplPourDemandeInitiale);

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(requerant);
        demandeInitiale.setDossier(dossier);

        try {
            RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale, demandesDeLaFamille,
                    CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
            Assert.fail("ne doit pas retourner de copie s'il n'y a pas de rente");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    @Test
    public void testRenteSurvivantEtInvaliditeSurLaMemeDemande() {
        /*
         * Comme décrit dans le BZ 5099, si des rentes d'invalidité et de survivant sont importées sur la même demande
         * d'invalidité, il faut vérifier la présence d'une demande de survivant non validée et si c'est le cas
         * rattacher les rentes de survivant sur celle-ci.
         * Par contre on ne copie pas de demande validée dans ce cas, pas comme pour l'ajourmenet
         */
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        // création de la demande d'invalidité sur laquel on est en train d'importer le calcul
        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        DossierPrestation dossierRequerant = new DossierPrestation();
        dossierRequerant.setId(genererUnIdUnique());
        dossierRequerant.setRequerant(requerant);

        DemandeRente demandeInvalidite = new DemandeRenteInvalidite();
        demandeInvalidite.setId(genererUnIdUnique());
        demandeInvalidite.setDossier(dossierRequerant);
        demandeInvalidite.setEtat(EtatDemandeRente.CALCULE);

        // création de la demande de survivant non-validée sur laquelle devront se rattacher les rentes de survivants
        PersonneAVS conjointDecedeRequerant = new PersonneAVS();
        conjointDecedeRequerant.setId(genererUnIdUnique());
        conjointDecedeRequerant.setDateDeces("01.01.2013");

        DossierPrestation dossierConjoint = new DossierPrestation();
        dossierConjoint.setId(genererUnIdUnique());
        dossierConjoint.setRequerant(conjointDecedeRequerant);

        DemandeRente demandeSurvivant = new DemandeRenteSurvivant();
        demandeSurvivant.setId(genererUnIdUnique());
        demandeSurvivant.setDossier(dossierConjoint);
        demandeSurvivant.setEtat(EtatDemandeRente.ENREGISTRE);

        demandesDeLaFamille.add(demandeSurvivant);

        // création d'une rente principal pour le requérant et de deux complémentaires pour un enfant
        PersonneAVS enfant = new PersonneAVS();
        enfant.setId(genererUnIdUnique());

        BaseCalcul baseCalculInvalidite = new BaseCalcul();
        baseCalculInvalidite.setId(genererUnIdUnique());
        baseCalculInvalidite.setDonneurDeDroit(requerant);

        BaseCalcul baseCalculSurvivant = new BaseCalcul();
        baseCalculSurvivant.setId(genererUnIdUnique());
        baseCalculSurvivant.setDonneurDeDroit(conjointDecedeRequerant);

        RenteAccordee rentePrincipale = new RenteAccordee();
        rentePrincipale.setId(genererUnIdUnique());
        rentePrincipale.setBeneficiaire(requerant);
        rentePrincipale.setCodePrestation(CodePrestation.CODE_50);
        rentePrincipale.setBaseCalcul(baseCalculInvalidite);

        RenteAccordee renteComplementaireInvalidite = new RenteAccordee();
        renteComplementaireInvalidite.setId(genererUnIdUnique());
        renteComplementaireInvalidite.setBeneficiaire(enfant);
        renteComplementaireInvalidite.setCodePrestation(CodePrestation.CODE_54);
        renteComplementaireInvalidite.setBaseCalcul(baseCalculInvalidite);

        RenteAccordee renteComplementaireSurvivant = new RenteAccordee();
        renteComplementaireSurvivant.setId(genererUnIdUnique());
        renteComplementaireSurvivant.setBeneficiaire(enfant);
        renteComplementaireSurvivant.setCodePrestation(CodePrestation.CODE_15);
        renteComplementaireSurvivant.setBaseCalcul(baseCalculSurvivant);

        demandeInvalidite.setRentesAccordees(Arrays.asList(rentePrincipale, renteComplementaireInvalidite,
                renteComplementaireSurvivant));

        RECalculACORDemandeRenteHelper
                .repartirLesRentesSelonLesTypesDesDemandes(demandeInvalidite, demandesDeLaFamille);

        Assert.assertTrue("les rentes d'invalidité ne doivent pas être déplacées dans l'autre demande",
                laRenteEstDansCetteDemande(rentePrincipale, demandeInvalidite));
        Assert.assertTrue("les rentes d'invalidité ne doivent pas être déplacées dans l'autre demande",
                laRenteEstDansCetteDemande(renteComplementaireInvalidite, demandeInvalidite));
        Assert.assertFalse("La rente d'orphelin ne doit plus se trouver sur la demande d'invalidité",
                laRenteEstDansCetteDemande(renteComplementaireSurvivant, demandeInvalidite));
        Assert.assertTrue("la rente d'orphelin doit être sur la demande de survivant",
                laRenteEstDansCetteDemande(renteComplementaireSurvivant, demandeSurvivant));
        Assert.assertEquals(
                "La demande non-validée sur laquelle on rattache des rentes doit voir son état changé à 'Calculé'",
                EtatDemandeRente.CALCULE, demandeSurvivant.getEtat());
    }

    @Test
    public void testMAJan1() throws Exception {
        // Arrange
        BSession sessionMock = PowerMockito.mock(BSession.class);
        BTransaction transactionMock = PowerMockito.mock(BTransaction.class);
        FCalcul fCalculMock = Mockito.mock(FCalcul.class);
        List<Long> renteAccordeIds = new ArrayList<>();
        renteAccordeIds.add(1L);
        renteAccordeIds.add(2L);

        RERenteAccordeeManager managerMock = PowerMockito.mock(RERenteAccordeeManager.class);
        PowerMockito.whenNew(RERenteAccordeeManager.class).withNoArguments().thenReturn(managerMock);

        List<FCalcul.Evenement> evenements = new ArrayList<>();
        FCalcul.Evenement evenementMock = Mockito.mock(FCalcul.Evenement.class);
        FCalcul.Evenement evenementEmpty = Mockito.mock(FCalcul.Evenement.class);
        evenements.add(evenementMock);
        evenements.add(evenementEmpty);
        Mockito.when(fCalculMock.getEvenement()).thenReturn(evenements);

        List<FCalcul.Evenement.BasesCalcul> basesCalculs = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul basesCalculMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        FCalcul.Evenement.BasesCalcul basesCalculEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        basesCalculs.add(basesCalculMock);
        basesCalculs.add(basesCalculEmpty);
        Mockito.when(evenementMock.getBasesCalcul()).thenReturn(basesCalculs);

        FCalcul.Evenement.BasesCalcul.BaseRam baseRamMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.class);
        Mockito.when(basesCalculMock.getBaseRam()).thenReturn(baseRamMock);
        FCalcul.Evenement.BasesCalcul.BaseRam.Bte bteMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.Bte.class);
        Mockito.when(baseRamMock.getBte()).thenReturn(bteMock);
        Mockito.when(bteMock.getAn1()).thenReturn(new Integer(1));

        List<FCalcul.Evenement.BasesCalcul.Decision> decisions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision decisionMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        FCalcul.Evenement.BasesCalcul.Decision decisionEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        decisions.add(decisionMock);
        decisions.add(decisionEmpty);
        Mockito.when(basesCalculMock.getDecision()).thenReturn(decisions);

        List<FCalcul.Evenement.BasesCalcul.Decision.Prestation> prestataions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        prestataions.add(prestationMock);
        prestataions.add(prestationEmpty);
        Mockito.when(decisionMock.getPrestation()).thenReturn(prestataions);

        Rente renteMock = Mockito.mock(Rente.class);
        Mockito.when(prestationMock.getRente()).thenReturn(renteMock);
        String nss = "1234.56";
        Mockito.when(prestationMock.getBeneficiaire()).thenReturn(nss);
        Mockito.when(renteMock.getDebutDroit()).thenReturn(20190614);

        List<RERenteAccordee> renteAccordees = new ArrayList<>();
        RERenteAccordee renteAccordeeMock1 = Mockito.mock(RERenteAccordee.class);
        RERenteAccordee renteAccordeeMock2 = Mockito.mock(RERenteAccordee.class);
        Mockito.when(renteAccordeeMock1.isNew()).thenReturn(false);
        Mockito.when(renteAccordeeMock2.isNew()).thenReturn(false);
        renteAccordees.add(renteAccordeeMock1);
        renteAccordees.add(renteAccordeeMock2);
        Mockito.when(managerMock.getContainerAsList()).thenReturn(renteAccordees);

        PRTiersWrapper tiersWrapperMock = Mockito.mock(PRTiersWrapper.class);
        Mockito.when(renteAccordeeMock1.getIdTiersBeneficiaire()).thenReturn(nss);
        Mockito.when(renteAccordeeMock2.getIdTiersBeneficiaire()).thenReturn(nss);
        PowerMockito.mockStatic(PRTiersHelper.class);
        PowerMockito.when(PRTiersHelper.getTiersParId(sessionMock, nss)).thenReturn(tiersWrapperMock);
        Mockito.when(tiersWrapperMock.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)).thenReturn(nss);
        Mockito.when(renteAccordeeMock1.getDateDebutDroit()).thenReturn("14062019");
        Mockito.when(renteAccordeeMock2.getDateDebutDroit()).thenReturn("15062019");
        Mockito.when(renteAccordeeMock1.getCodePrestation()).thenReturn("68440");
        Mockito.when(renteAccordeeMock2.getCodePrestation()).thenReturn("68440");
        Mockito.when(renteMock.getGenre()).thenReturn(new Integer(68440));

        REBasesCalcul basesCalculEntityMock = PowerMockito.mock(REBasesCalcul.class);
        PowerMockito.whenNew(REBasesCalcul.class).withNoArguments().thenReturn(basesCalculEntityMock);

        // Act
        RECalculACORDemandeRenteHelper helper = new RECalculACORDemandeRenteHelper();
        Whitebox.invokeMethod(helper, "doMAJExtraData", sessionMock, transactionMock, fCalculMock, renteAccordeIds);

        // Assert
        Mockito.verify(managerMock, Mockito.times(1)).setForIdsRentesAccordees("1,2");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE1("1");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE2("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE4("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).update(transactionMock);
    }

    @Test
    public void testMAJan2() throws Exception {
        // Arrange
        BSession sessionMock = PowerMockito.mock(BSession.class);
        BTransaction transactionMock = PowerMockito.mock(BTransaction.class);
        FCalcul fCalculMock = Mockito.mock(FCalcul.class);
        List<Long> renteAccordeIds = new ArrayList<>();
        renteAccordeIds.add(1L);
        renteAccordeIds.add(2L);

        RERenteAccordeeManager managerMock = PowerMockito.mock(RERenteAccordeeManager.class);
        PowerMockito.whenNew(RERenteAccordeeManager.class).withNoArguments().thenReturn(managerMock);

        List<FCalcul.Evenement> evenements = new ArrayList<>();
        FCalcul.Evenement evenementMock = Mockito.mock(FCalcul.Evenement.class);
        FCalcul.Evenement evenementEmpty = Mockito.mock(FCalcul.Evenement.class);
        evenements.add(evenementMock);
        evenements.add(evenementEmpty);
        Mockito.when(fCalculMock.getEvenement()).thenReturn(evenements);

        List<FCalcul.Evenement.BasesCalcul> basesCalculs = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul basesCalculMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        FCalcul.Evenement.BasesCalcul basesCalculEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        basesCalculs.add(basesCalculMock);
        basesCalculs.add(basesCalculEmpty);
        Mockito.when(evenementMock.getBasesCalcul()).thenReturn(basesCalculs);

        FCalcul.Evenement.BasesCalcul.BaseRam baseRamMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.class);
        Mockito.when(basesCalculMock.getBaseRam()).thenReturn(baseRamMock);
        FCalcul.Evenement.BasesCalcul.BaseRam.Bte bteMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.Bte.class);
        Mockito.when(baseRamMock.getBte()).thenReturn(bteMock);
        Mockito.when(bteMock.getAn2()).thenReturn(new Integer(2));

        List<FCalcul.Evenement.BasesCalcul.Decision> decisions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision decisionMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        FCalcul.Evenement.BasesCalcul.Decision decisionEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        decisions.add(decisionMock);
        decisions.add(decisionEmpty);
        Mockito.when(basesCalculMock.getDecision()).thenReturn(decisions);

        List<FCalcul.Evenement.BasesCalcul.Decision.Prestation> prestataions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        prestataions.add(prestationMock);
        prestataions.add(prestationEmpty);
        Mockito.when(decisionMock.getPrestation()).thenReturn(prestataions);

        Rente renteMock = Mockito.mock(Rente.class);
        Mockito.when(prestationMock.getRente()).thenReturn(renteMock);
        String nss = "1234.56";
        Mockito.when(prestationMock.getBeneficiaire()).thenReturn(nss);
        Mockito.when(renteMock.getDebutDroit()).thenReturn(20190614);

        List<RERenteAccordee> renteAccordees = new ArrayList<>();
        RERenteAccordee renteAccordeeMock1 = Mockito.mock(RERenteAccordee.class);
        RERenteAccordee renteAccordeeMock2 = Mockito.mock(RERenteAccordee.class);
        Mockito.when(renteAccordeeMock1.isNew()).thenReturn(false);
        Mockito.when(renteAccordeeMock2.isNew()).thenReturn(false);
        renteAccordees.add(renteAccordeeMock1);
        renteAccordees.add(renteAccordeeMock2);
        Mockito.when(managerMock.getContainerAsList()).thenReturn(renteAccordees);

        PRTiersWrapper tiersWrapperMock = Mockito.mock(PRTiersWrapper.class);
        Mockito.when(renteAccordeeMock1.getIdTiersBeneficiaire()).thenReturn(nss);
        Mockito.when(renteAccordeeMock2.getIdTiersBeneficiaire()).thenReturn(nss);
        PowerMockito.mockStatic(PRTiersHelper.class);
        PowerMockito.when(PRTiersHelper.getTiersParId(sessionMock, nss)).thenReturn(tiersWrapperMock);
        Mockito.when(tiersWrapperMock.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)).thenReturn(nss);
        Mockito.when(renteAccordeeMock1.getDateDebutDroit()).thenReturn("14062019");
        Mockito.when(renteAccordeeMock2.getDateDebutDroit()).thenReturn("14062019");
        Mockito.when(renteAccordeeMock1.getCodePrestation()).thenReturn("68440");
        Mockito.when(renteAccordeeMock2.getCodePrestation()).thenReturn("68000");
        Mockito.when(renteMock.getGenre()).thenReturn(new Integer(68440));

        REBasesCalcul basesCalculEntityMock = PowerMockito.mock(REBasesCalcul.class);
        PowerMockito.whenNew(REBasesCalcul.class).withNoArguments().thenReturn(basesCalculEntityMock);

        // Act
        RECalculACORDemandeRenteHelper helper = new RECalculACORDemandeRenteHelper();
        Whitebox.invokeMethod(helper, "doMAJExtraData", sessionMock, transactionMock, fCalculMock, renteAccordeIds);

        // Assert
        Mockito.verify(managerMock, Mockito.times(1)).setForIdsRentesAccordees("1,2");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE1("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE2("2");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE4("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).update(transactionMock);


    }

    @Test
    public void testMAJan4() throws Exception {
        // Arrange
        BSession sessionMock = PowerMockito.mock(BSession.class);
        BTransaction transactionMock = PowerMockito.mock(BTransaction.class);
        FCalcul fCalculMock = Mockito.mock(FCalcul.class);
        List<Long> renteAccordeIds = new ArrayList<>();
        renteAccordeIds.add(1L);
        renteAccordeIds.add(2L);

        RERenteAccordeeManager managerMock = PowerMockito.mock(RERenteAccordeeManager.class);
        PowerMockito.whenNew(RERenteAccordeeManager.class).withNoArguments().thenReturn(managerMock);

        List<FCalcul.Evenement> evenements = new ArrayList<>();
        FCalcul.Evenement evenementMock = Mockito.mock(FCalcul.Evenement.class);
        FCalcul.Evenement evenementEmpty = Mockito.mock(FCalcul.Evenement.class);
        evenements.add(evenementMock);
        evenements.add(evenementEmpty);
        Mockito.when(fCalculMock.getEvenement()).thenReturn(evenements);

        List<FCalcul.Evenement.BasesCalcul> basesCalculs = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul basesCalculMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        FCalcul.Evenement.BasesCalcul basesCalculEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.class);
        basesCalculs.add(basesCalculMock);
        basesCalculs.add(basesCalculEmpty);
        Mockito.when(evenementMock.getBasesCalcul()).thenReturn(basesCalculs);

        FCalcul.Evenement.BasesCalcul.BaseRam baseRamMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.class);
        Mockito.when(basesCalculMock.getBaseRam()).thenReturn(baseRamMock);
        FCalcul.Evenement.BasesCalcul.BaseRam.Bte bteMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.BaseRam.Bte.class);
        Mockito.when(baseRamMock.getBte()).thenReturn(bteMock);
        Mockito.when(bteMock.getAn4()).thenReturn(new Integer(4));

        List<FCalcul.Evenement.BasesCalcul.Decision> decisions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision decisionMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        FCalcul.Evenement.BasesCalcul.Decision decisionEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.class);
        decisions.add(decisionMock);
        decisions.add(decisionEmpty);
        Mockito.when(basesCalculMock.getDecision()).thenReturn(decisions);

        List<FCalcul.Evenement.BasesCalcul.Decision.Prestation> prestataions = new ArrayList<>();
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationMock = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        FCalcul.Evenement.BasesCalcul.Decision.Prestation prestationEmpty = Mockito.mock(FCalcul.Evenement.BasesCalcul.Decision.Prestation.class);
        prestataions.add(prestationMock);
        prestataions.add(prestationEmpty);
        Mockito.when(decisionMock.getPrestation()).thenReturn(prestataions);

        Rente renteMock = Mockito.mock(Rente.class);
        Mockito.when(prestationMock.getRente()).thenReturn(renteMock);
        String nss = "1234.56";
        String nss2 = "6543.21";
        Mockito.when(prestationMock.getBeneficiaire()).thenReturn(nss);
        Mockito.when(renteMock.getDebutDroit()).thenReturn(20190614);

        List<RERenteAccordee> renteAccordees = new ArrayList<>();
        RERenteAccordee renteAccordeeMock1 = Mockito.mock(RERenteAccordee.class);
        RERenteAccordee renteAccordeeMock2 = Mockito.mock(RERenteAccordee.class);
        Mockito.when(renteAccordeeMock1.isNew()).thenReturn(false);
        Mockito.when(renteAccordeeMock2.isNew()).thenReturn(false);
        renteAccordees.add(renteAccordeeMock1);
        renteAccordees.add(renteAccordeeMock2);
        Mockito.when(managerMock.getContainerAsList()).thenReturn(renteAccordees);

        PRTiersWrapper tiersWrapperMock = Mockito.mock(PRTiersWrapper.class);
        PRTiersWrapper tiersWrapperMock2 = Mockito.mock(PRTiersWrapper.class);
        Mockito.when(renteAccordeeMock1.getIdTiersBeneficiaire()).thenReturn(nss);
        Mockito.when(renteAccordeeMock2.getIdTiersBeneficiaire()).thenReturn(nss2);
        PowerMockito.mockStatic(PRTiersHelper.class);
        PowerMockito.when(PRTiersHelper.getTiersParId(sessionMock, nss)).thenReturn(tiersWrapperMock);
        PowerMockito.when(PRTiersHelper.getTiersParId(sessionMock, nss2)).thenReturn(tiersWrapperMock2);
        Mockito.when(tiersWrapperMock.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)).thenReturn(nss);
        Mockito.when(tiersWrapperMock2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)).thenReturn(nss2);
        Mockito.when(renteAccordeeMock1.getDateDebutDroit()).thenReturn("14062019");
        Mockito.when(renteAccordeeMock2.getDateDebutDroit()).thenReturn("14062019");
        Mockito.when(renteAccordeeMock1.getCodePrestation()).thenReturn("68440");
        Mockito.when(renteAccordeeMock2.getCodePrestation()).thenReturn("68440");
        Mockito.when(renteMock.getGenre()).thenReturn(new Integer(68440));

        REBasesCalcul basesCalculEntityMock = PowerMockito.mock(REBasesCalcul.class);
        PowerMockito.whenNew(REBasesCalcul.class).withNoArguments().thenReturn(basesCalculEntityMock);

        // Act
        RECalculACORDemandeRenteHelper helper = new RECalculACORDemandeRenteHelper();
        Whitebox.invokeMethod(helper, "doMAJExtraData", sessionMock, transactionMock, fCalculMock, renteAccordeIds);

        // Assert
        Mockito.verify(managerMock, Mockito.times(1)).setForIdsRentesAccordees("1,2");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE1("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE2("0");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).setNombreAnneeBTE4("4");
        Mockito.verify(basesCalculEntityMock, Mockito.times(1)).update(transactionMock);
    }

}
