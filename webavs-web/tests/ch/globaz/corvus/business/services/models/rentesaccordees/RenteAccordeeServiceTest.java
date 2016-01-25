package ch.globaz.corvus.business.services.models.rentesaccordees;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.businessimpl.services.models.rentesaccordees.RenteAccordeeServiceImpl;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.DossierPrestation;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RenteAccordeeServiceTest extends TestUnitaireAvecGenerateurIDUnique {

    private RenteAccordeeService service;

    private void laRenteEstPresenteDansLeResultat(RenteAccordee rente, Set<RenteAccordee> resultat) {
        Assert.assertTrue("La rente doit �tre pr�sente dans le r�sultat", resultat.contains(rente));
    }

    private void laRenteNEstPasPresenteDansLeResultat(RenteAccordee rente, Set<RenteAccordee> resultat) {
        Assert.assertFalse("La rente ne doit pas �tre pr�sente dans le r�sultat", resultat.contains(rente));
    }

    @Before
    public void setUp() {
        service = Mockito.spy(new RenteAccordeeServiceImpl());
    }

    @Test
    public void testRentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande() throws Exception {

        PersonneAVS pere = new PersonneAVS();
        pere.setId(genererUnIdUnique());
        pere.setNom("p�re");

        PersonneAVS mere = new PersonneAVS();
        mere.setId(genererUnIdUnique());
        mere.setNom("m�re");

        PersonneAVS enfant = new PersonneAVS();
        enfant.setId(genererUnIdUnique());
        enfant.setNom("enfant");

        RenteAccordee ancienneRenteDuPere = new RenteAccordee();
        ancienneRenteDuPere.setId(genererUnIdUnique());
        ancienneRenteDuPere.setCodePrestation(CodePrestation.CODE_10);
        ancienneRenteDuPere.setBeneficiaire(pere);
        ancienneRenteDuPere.setEtat(EtatPrestationAccordee.VALIDE);

        RenteAccordee nouvelleRenteDuPere = new RenteAccordee();
        nouvelleRenteDuPere.setId(genererUnIdUnique());
        nouvelleRenteDuPere.setCodePrestation(CodePrestation.CODE_10);
        nouvelleRenteDuPere.setBeneficiaire(pere);
        nouvelleRenteDuPere.setEtat(EtatPrestationAccordee.CALCULE);

        DemandeRente nouvelleDemandePere = new DemandeRenteVieillesse();
        nouvelleDemandePere.setId(genererUnIdUnique());
        nouvelleDemandePere.setRentesAccordees(Arrays.asList(nouvelleRenteDuPere));

        DossierPrestation dossier = new DossierPrestation();
        dossier.setId(genererUnIdUnique());
        dossier.setRequerant(pere);
        nouvelleDemandePere.setDossier(dossier);

        // on mock la m�thode retournant les rentes accord�es en cours de la famille
        Mockito.doReturn(new HashSet<RenteAccordee>(Arrays.asList(ancienneRenteDuPere))).when(service)
                .rentesAccordeesEnCoursDeLaFamille(pere);

        /*
         * Une seule rente en cours dans l'historique, les deux rentes au b�n�fice du p�re et du m�me type, le service
         * doit retourner l'ancienne rente du p�re pour qu'elle soit diminu�e
         */
        Set<RenteAccordee> result = service
                .rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);

        /*
         * L'ancienne rente d'un type diff�rente (AI), mais doit tout de m�me �tre retourn�e
         */
        ancienneRenteDuPere.setCodePrestation(CodePrestation.CODE_70);
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);

        /*
         * l'ancienne rente est une API, elle ne doit pas �tre retourn�e car la nouvelle rente n'est pas une API
         */
        ancienneRenteDuPere.setCodePrestation(CodePrestation.CODE_81);
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteNEstPasPresenteDansLeResultat(ancienneRenteDuPere, result);

        RenteAccordee ancienneRenteDeLaMere = new RenteAccordee();
        ancienneRenteDeLaMere.setId(genererUnIdUnique());
        ancienneRenteDeLaMere.setCodePrestation(CodePrestation.CODE_50);
        ancienneRenteDeLaMere.setBeneficiaire(mere);
        ancienneRenteDeLaMere.setEtat(EtatPrestationAccordee.VALIDE);

        ancienneRenteDuPere.setCodePrestation(CodePrestation.CODE_10);

        // on ajoute l'ancienne rente de la m�re aux rentes en cours de la famille
        Mockito.doReturn(new HashSet<RenteAccordee>(Arrays.asList(ancienneRenteDuPere, ancienneRenteDeLaMere)))
                .when(service).rentesAccordeesEnCoursDeLaFamille(pere);

        /*
         * seule l'ancienne rente du p�re doit sortir, car l'ancienne rente de la m�re n'a pas de rente dans le nouveau
         * droit
         */
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);
        laRenteNEstPasPresenteDansLeResultat(ancienneRenteDeLaMere, result);

        RenteAccordee nouvelleRenteDeLaMere = new RenteAccordee();
        nouvelleRenteDeLaMere.setId(genererUnIdUnique());
        nouvelleRenteDeLaMere.setCodePrestation(CodePrestation.CODE_50);
        nouvelleRenteDeLaMere.setBeneficiaire(mere);
        nouvelleRenteDeLaMere.setEtat(EtatPrestationAccordee.CALCULE);

        nouvelleDemandePere.setRentesAccordees(Arrays.asList(nouvelleRenteDuPere, nouvelleRenteDeLaMere));

        /*
         * Les deux rentes ont un rempla�ant dans le nouveau droit et doivent donc les deux sortir lors de l'appel au
         * service
         */
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);
        laRenteEstPresenteDansLeResultat(ancienneRenteDeLaMere, result);

        RenteAccordee ancienneRenteComplementairePourEnfantLieeALaRenteDuPere = new RenteAccordee();
        ancienneRenteComplementairePourEnfantLieeALaRenteDuPere.setId(genererUnIdUnique());
        ancienneRenteComplementairePourEnfantLieeALaRenteDuPere.setCodePrestation(CodePrestation.CODE_34);
        ancienneRenteComplementairePourEnfantLieeALaRenteDuPere.setBeneficiaire(enfant);
        ancienneRenteComplementairePourEnfantLieeALaRenteDuPere.setEtat(EtatPrestationAccordee.VALIDE);

        // on ajoute l'ancienne rente de l'enfant (li�e � la rente du p�re) aux rentes en cours de la famille
        Mockito.doReturn(
                new HashSet<RenteAccordee>(Arrays.asList(ancienneRenteDuPere, ancienneRenteDeLaMere,
                        ancienneRenteComplementairePourEnfantLieeALaRenteDuPere))).when(service)
                .rentesAccordeesEnCoursDeLaFamille(pere);

        /*
         * Les deux rentes des parents ont un rempla�ant dans le nouveau droit et doivent donc les deux sortir lors de
         * l'appel au service, celle de l'enfant ne doit pas sortir
         */
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);
        laRenteEstPresenteDansLeResultat(ancienneRenteDeLaMere, result);
        laRenteNEstPasPresenteDansLeResultat(ancienneRenteComplementairePourEnfantLieeALaRenteDuPere, result);

        RenteAccordee nouvelleRenteEnfant = new RenteAccordee();
        nouvelleRenteEnfant.setId(genererUnIdUnique());
        nouvelleRenteEnfant.setCodePrestation(CodePrestation.CODE_34);
        nouvelleRenteEnfant.setBeneficiaire(enfant);
        nouvelleRenteEnfant.setEtat(EtatPrestationAccordee.CALCULE);

        // ajout de la nouvelle rente de l'enfant � la demande du p�re
        nouvelleDemandePere.setRentesAccordees(Arrays.asList(nouvelleRenteDuPere, nouvelleRenteDeLaMere,
                nouvelleRenteEnfant));

        /*
         * Toutes les anciennes rentes doivent �tre diminu�es car elles ont un rempla�ant dans le nouveau droit
         */
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);
        laRenteEstPresenteDansLeResultat(ancienneRenteDeLaMere, result);
        laRenteEstPresenteDansLeResultat(ancienneRenteComplementairePourEnfantLieeALaRenteDuPere, result);

        nouvelleRenteEnfant.setCodePrestation(CodePrestation.CODE_55);
        /*
         * L'ancienne rente de l'enfant ne doit pas �tre diminu�e, car elle est li�e � la rente du p�re tandis que la
         * nouvelle rente est li�e � la rente de la m�re
         */
        result = service.rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(nouvelleDemandePere);
        laRenteEstPresenteDansLeResultat(ancienneRenteDuPere, result);
        laRenteEstPresenteDansLeResultat(ancienneRenteDeLaMere, result);
        laRenteNEstPasPresenteDansLeResultat(ancienneRenteComplementairePourEnfantLieeALaRenteDuPere, result);
    }
}
