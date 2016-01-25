package globaz.corvus.helpers.acor;

import globaz.corvus.exceptions.REBusinessException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

public class RECalculACORDemandeRenteHelperTest extends TestUnitaireAvecGenerateurIDUnique {

    private void laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(final DemandeRente copie,
            final DemandeRente demandeInitiale) {
        Assert.assertEquals("La date de traitement doit �tre celle de la demande intiale",
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
        String message = "ce n'est pas la bonne demande qui a �t� copi�e";

        Assert.assertEquals(message, demande1.getDateDepot(), demande2.getDateDepot());
        Assert.assertEquals(message, demande1.getDossier(), demande2.getDossier());
        Assert.assertEquals(message, demande1.getGestionnaire(), demande2.getGestionnaire());
        Assert.assertEquals(message, demande1.getTypeDemandeRente(), demande2.getTypeDemandeRente());

        Assert.assertNotEquals("Les informations compl�mentaires de la demande ne doivent pas �tre copi�es",
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
                "La demande du conjoint ne doit pas �tre copi�e car elle est encore modifiable (n'est pas dans l'�tat valid�)",
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
         * Arriv�e du requ�rant � l'�ge AVS, avec son conjoint ayant ajourn� pr�c�demment
         * 
         * <pre>
         * Nouvelle demande (au nom du requ�rant)  ----  Rente01 (au b�n�fice du requ�rant) sans code cas sp�cial
         *                                          `--  Rente02 (au b�n�fice du conjoint) avec code cas sp�cial 08
         * 
         * Ancienne demande (au nom du conjoint)   ----  Rente03 (au b�n�fice du conjoint) avec code cas sp�cial 08
         * </pre>
         * 
         * L'ancienne demande doit �tre copi�e, et sur cette copie doit �tre rattach�e la rente02
         */
        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(nouvelleDemande,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(ancienneDemande, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, nouvelleDemande);
        Assert.assertTrue(
                "La rente avec le code cas sp�cial 08 doit se trouver dans la nouvelle demande si le b�n�ficiaire n'est pas le requ�rant de la demande initiale",
                laRenteEstDansCetteDemande(renteAccorde02NouvelleDemandeAvecCodeCasSpecial08, copie));
        Assert.assertFalse(
                "La rente avec le code cas sp�cial 08 ne doit plus �tre rattach�e � la demande intitiale quand le b�n�ficiaire de cette rente n'est pas le requ�rant",
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
         * Arriv�e du requ�rant � l'�ge AVS, celui-ci veut ajourner. Son conjoint est d�j� � l'AVS (et a donc d�j� une
         * demande et une rente)
         * 
         * <pre>
         * Nouvelle demande (au nom du requ�rant)  ----  Rente01 (au b�n�fice du requ�rant) avec code cas sp�cial 08
         *                                          `--  Rente02 (au b�n�fice du conjoint) sans code cas sp�cial
         * 
         * Ancienne demande (au nom du conjoint)   ----  Rente03 (au b�n�fice du conjoint) sans code cas sp�cial
         * </pre>
         * 
         * L'ancienne demande doit �tre copi�e, et sur cette copie doit �tre rattach�e la rente02
         */
        copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(nouvelleDemande,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(ancienneDemande, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, nouvelleDemande);
        Assert.assertTrue(
                "La rente avec le code cas sp�cial 08 doit rester sur la demande initiale si le requ�rant est le b�n�ficiaire de la rente",
                laRenteEstDansCetteDemande(renteAccorde02NouvelleDemandeAvecCodeCasSpecial08, nouvelleDemande));
        Assert.assertFalse(
                "La rente sans code cas sp�cial doit �tre d�tach�e de la demande initiale si cette demande comporte une rente avec code cas sp�cial 08",
                laRenteEstDansCetteDemande(renteAccorde01NouvelleDemandeSansCodeCasSpecial, nouvelleDemande));
        Assert.assertTrue(
                "La rente sans code cas sp�cial doit �tre attach�e � la copie lorsque la demande initiale comporte une rente avec code cas sp�cial 08",
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
                "La rente avec le code cas sp�cial 08 doit se trouver dans la nouvelle demande si le b�n�ficiaire n'est pas le requ�rant de la demande initiale",
                laRenteEstDansCetteDemande(renteAccorde02AvecCodeCasSpecial08, copie));
        Assert.assertFalse(
                "La rente avec le code cas sp�cial 08 ne doit plus �tre rattach�e � la demande intitiale quand le b�n�ficiaire de cette rente n'est pas le requ�rant",
                laRenteEstDansCetteDemande(renteAccorde02AvecCodeCasSpecial08, demandeInitiale));
    }

    @Test
    public void testAjournementRequerantEtConjointVoulantAjourner() throws Exception {

        /**
         * <pre>
         * Simulation d'une reprise d'un cas fait avant le mandat D0095 :
         * 
         * 		- Le requ�rant avait ajourn� avant son �pouse
         * 		- L'�pouse du requ�rant a ajourn� en arrivant � l'�ge AVS
         * 		- Vu que fait sous l'ancien syst�me, les deux demandes sont � l'�tat calcul�
         * 		- On calcul et importe sur la demande du requ�rant
         * 
         * 	But du test : v�rifier qu'aucune demande ne soit copi�e, mais qu'elles soient utilis�es
         * 	tel quel (elle sont � l'�tat calcul�)
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
                "la demande du conjoint ne doit pas �tre copi�e, mais utilis�e tel quel car elle est � l'�tat calcul�",
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
        Assert.assertTrue("La rente avec le code cas sp�cial 07 doit �tre sur la demande copi�e",
                laRenteEstDansCetteDemande(rentePrincipaleAvecIncarceration, copie));
        Assert.assertFalse(
                "Les rentes compl�mentaires pour enfants ne doivent pas �tre sur la m�me demande que la rente principale, car la rente princpale comporte un code cas sp�cial 07",
                laRenteEstDansCetteDemande(renteComplementaireEnfant1, copie));
        Assert.assertFalse(
                "Les rentes compl�mentaires pour enfants ne doivent pas �tre sur la m�me demande que la rente principale, car la rente princpale comporte un code cas sp�cial 07",
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
         * Le conjoint est d�j� � l'�ge AVS, mais a ajourn� sa rente. Le requ�rant arrive � l'�ge AVS et veut aussi
         * ajourner sa rente. On doit copier la demande du conjoint pour y coller sa nouvelle rente ajourn�e, et la
         * rente du requ�rant doit rester sur la demande initiale (qui est � son nom)
         */
        DemandeRente copie = RECalculACORDemandeRenteHelper.repartirLesRentesPourCodeCasSpecial(demandeInitiale,
                demandesDeLaFamille, CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        lesDonneesDesDemandesSontLesMemesSaufIdDateTraitementEtRentes(demandeConjoint, copie);
        laDateDeTraitementDoitEtreCelleDeLaDemandeInitiale(copie, demandeInitiale);
        Assert.assertTrue("La rente du requ�rant doit se trouver sur la demande intiale",
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
            Assert.fail("Le fait qu'une base de calcul ne soit pas li�e au r�querant, et qu'aucune demande n'�mane de ce tiers base calcul doit soulever un message d'erreur (il doit exister une demande pour chaque tiers bases calculs)");
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
         * Comme d�crit dans le BZ 5099, si des rentes d'invalidit� et de survivant sont import�es sur la m�me demande
         * d'invalidit�, il faut v�rifier la pr�sence d'une demande de survivant non valid�e et si c'est le cas
         * rattacher les rentes de survivant sur celle-ci.
         * Par contre on ne copie pas de demande valid�e dans ce cas, pas comme pour l'ajourmenet
         */
        Set<DemandeRente> demandesDeLaFamille = new HashSet<DemandeRente>();

        // cr�ation de la demande d'invalidit� sur laquel on est en train d'importer le calcul
        PersonneAVS requerant = new PersonneAVS();
        requerant.setId(genererUnIdUnique());

        DossierPrestation dossierRequerant = new DossierPrestation();
        dossierRequerant.setId(genererUnIdUnique());
        dossierRequerant.setRequerant(requerant);

        DemandeRente demandeInvalidite = new DemandeRenteInvalidite();
        demandeInvalidite.setId(genererUnIdUnique());
        demandeInvalidite.setDossier(dossierRequerant);
        demandeInvalidite.setEtat(EtatDemandeRente.CALCULE);

        // cr�ation de la demande de survivant non-valid�e sur laquelle devront se rattacher les rentes de survivants
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

        // cr�ation d'une rente principal pour le requ�rant et de deux compl�mentaires pour un enfant
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

        Assert.assertTrue("les rentes d'invalidit� ne doivent pas �tre d�plac�es dans l'autre demande",
                laRenteEstDansCetteDemande(rentePrincipale, demandeInvalidite));
        Assert.assertTrue("les rentes d'invalidit� ne doivent pas �tre d�plac�es dans l'autre demande",
                laRenteEstDansCetteDemande(renteComplementaireInvalidite, demandeInvalidite));
        Assert.assertFalse("La rente d'orphelin ne doit plus se trouver sur la demande d'invalidit�",
                laRenteEstDansCetteDemande(renteComplementaireSurvivant, demandeInvalidite));
        Assert.assertTrue("la rente d'orphelin doit �tre sur la demande de survivant",
                laRenteEstDansCetteDemande(renteComplementaireSurvivant, demandeSurvivant));
        Assert.assertEquals(
                "La demande non-valid�e sur laquelle on rattache des rentes doit voir son �tat chang� � 'Calcul�'",
                EtatDemandeRente.CALCULE, demandeSurvivant.getEtat());
    }
}
