/*
 * Créé le 23 avr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.utils;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class REMappingSaisieDemandeRente {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 *
	 */
    private static RESaisieDemandeRenteViewBean buildSaisieDemandeRenteViweBean(REDemandeRenteAPI demandeRente,
            BTransaction transaction) throws Exception {

        RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();

        // demande
        viewBean.setIdRequerant(demandeRente.loadDemandePrestation(transaction).getIdTiers());

        // demande rente
        viewBean.setIdDemandeRente(demandeRente.getIdDemandeRente());
        viewBean.setIdDemandePrestation(demandeRente.getIdDemandePrestation());
        viewBean.setIdDemandeRenteParent(demandeRente.getIdDemandeRenteParent());
        viewBean.setDateTraitement(demandeRente.getDateTraitement());
        viewBean.setDateDepot(demandeRente.getDateDepot());
        viewBean.setDateReception(demandeRente.getDateReception());
        viewBean.setCsEtat(demandeRente.getCsEtat());
        viewBean.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
        viewBean.setIdInfoComplementaire(demandeRente.getIdInfoComplementaire());
        viewBean.setCsTypeCalcul(demandeRente.getCsTypeCalcul());
        viewBean.setCsTypeDemandeRente(demandeRente.getCsTypeDemandeRente());
        viewBean.setIdGestionnaire(demandeRente.getIdGestionnaire());
        viewBean.setNbPagesMotivationApi(demandeRente.getNbPageMotivation());

        // viewBean.setIsCelibataireSansEnfants();

        // demande rente API
        viewBean.setCsGenrePrononceAiApi(demandeRente.getCsGenrePrononceAI());
        viewBean.setCodeOfficeAiApi(demandeRente.getCodeOfficeAI());
        viewBean.setCsInfirmiteApi(demandeRente.getCsInfirmite());
        viewBean.setCsAtteinteApi(demandeRente.getCsAtteinte());
        viewBean.setDateSurvenanceEvenementAssureAPI(demandeRente.getDateSuvenanceEvenementAssure());

        viewBean.setSpy(demandeRente.getSpy());
        viewBean.setSession(demandeRente.getSession());

        // Liste periodes
        // Voir si nécessaire de le faire ici, mais normallement cela est fait
        // dans l'action afficher

        return viewBean;
    }

    /**
	 *
	 */
    private static RESaisieDemandeRenteViewBean buildSaisieDemandeRenteViweBean(REDemandeRenteInvalidite demandeRente,
            BTransaction transaction) throws Exception {

        RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();

        // demande
        viewBean.setIdRequerant(demandeRente.loadDemandePrestation(transaction).getIdTiers());

        // demande rente
        viewBean.setIdDemandeRente(demandeRente.getIdDemandeRente());
        viewBean.setIdDemandePrestation(demandeRente.getIdDemandePrestation());
        viewBean.setIdDemandeRenteParent(demandeRente.getIdDemandeRenteParent());
        viewBean.setDateTraitement(demandeRente.getDateTraitement());
        viewBean.setDateDepot(demandeRente.getDateDepot());
        viewBean.setDateReception(demandeRente.getDateReception());
        viewBean.setCsEtat(demandeRente.getCsEtat());
        viewBean.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
        viewBean.setIdInfoComplementaire(demandeRente.getIdInfoComplementaire());
        viewBean.setCsTypeCalcul(demandeRente.getCsTypeCalcul());
        viewBean.setCsTypeDemandeRente(demandeRente.getCsTypeDemandeRente());
        viewBean.setIdGestionnaire(demandeRente.getIdGestionnaire());
        viewBean.setNbPagesMotivationInv(demandeRente.getNbPageMotivation());

        // viewBean.setIsCelibataireSansEnfants();

        // demande rente invalidite
        viewBean.setCsGenrePrononceAiInv(demandeRente.getCsGenrePrononceAI());
        viewBean.setCodeOfficeAiInv(demandeRente.getCodeOfficeAI());
        viewBean.setCsInfirmiteInv(demandeRente.getCsInfirmite());
        viewBean.setCsAtteinteInv(demandeRente.getCsAtteinte());
        viewBean.setPourcentageReductionInv(demandeRente.getPourcentageReduction());
        viewBean.setDateSurvenanceEvenementAssureInv(demandeRente.getDateSuvenanceEvenementAssure());
        viewBean.setPourcentRedFauteGrave(demandeRente.getPourcentRedFauteGrave());
        viewBean.setPourcentRedNonCollaboration(demandeRente.getPourcentRedNonCollaboration());
        viewBean.setDateDebutRedNonCollaboration(demandeRente.getDateDebutRedNonCollaboration());
        viewBean.setDateFinRedNonCollaboration(demandeRente.getDateFinRedNonCollaboration());

        viewBean.setSpy(demandeRente.getSpy());
        viewBean.setSession(demandeRente.getSession());

        // Liste periodes
        // Voir si nécessaire de le faire ici, mais normallement cela est fait
        // dans l'action afficher

        return viewBean;
    }

    /**
	 *
	 */
    private static RESaisieDemandeRenteViewBean buildSaisieDemandeRenteViweBean(REDemandeRenteSurvivant demandeRente,
            BTransaction transaction) throws Exception {

        RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();

        // demande
        viewBean.setIdRequerant(demandeRente.loadDemandePrestation(transaction).getIdTiers());

        // demande rente
        viewBean.setIdDemandeRente(demandeRente.getIdDemandeRente());
        viewBean.setIdDemandePrestation(demandeRente.getIdDemandePrestation());
        viewBean.setIdDemandeRenteParent(demandeRente.getIdDemandeRenteParent());
        viewBean.setDateTraitement(demandeRente.getDateTraitement());
        viewBean.setDateDepot(demandeRente.getDateDepot());
        viewBean.setDateReception(demandeRente.getDateReception());
        viewBean.setCsEtat(demandeRente.getCsEtat());
        viewBean.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
        viewBean.setIdInfoComplementaire(demandeRente.getIdInfoComplementaire());
        viewBean.setCsTypeCalcul(demandeRente.getCsTypeCalcul());
        viewBean.setCsTypeDemandeRente(demandeRente.getCsTypeDemandeRente());
        viewBean.setIdGestionnaire(demandeRente.getIdGestionnaire());

        // viewBean.setIsCelibataireSansEnfants();

        // demande rente survivant
        viewBean.setPourcentageReductionSur(demandeRente.getPourcentageReduction());

        viewBean.setSpy(demandeRente.getSpy());
        viewBean.setSession(demandeRente.getSession());

        return viewBean;
    }

    /**
	 *
	 */
    private static RESaisieDemandeRenteViewBean buildSaisieDemandeRenteViweBean(REDemandeRenteVieillesse demandeRente,
            BTransaction transaction) throws Exception {

        RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();

        // demande
        viewBean.setIdRequerant(demandeRente.loadDemandePrestation(transaction).getIdTiers());

        // demande rente
        viewBean.setIdDemandeRente(demandeRente.getIdDemandeRente());
        viewBean.setIdDemandePrestation(demandeRente.getIdDemandePrestation());
        viewBean.setIdDemandeRenteParent(demandeRente.getIdDemandeRenteParent());
        viewBean.setDateTraitement(demandeRente.getDateTraitement());
        viewBean.setDateDepot(demandeRente.getDateDepot());
        viewBean.setDateReception(demandeRente.getDateReception());
        viewBean.setCsEtat(demandeRente.getCsEtat());
        viewBean.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
        viewBean.setIdInfoComplementaire(demandeRente.getIdInfoComplementaire());
        viewBean.setCsTypeCalcul(demandeRente.getCsTypeCalcul());
        viewBean.setCsTypeDemandeRente(demandeRente.getCsTypeDemandeRente());
        viewBean.setIdGestionnaire(demandeRente.getIdGestionnaire());

        // viewBean.setIsCelibataireSansEnfants();

        // demande rente vieillesse
        viewBean.setCsAnneeAnticipationVie(demandeRente.getCsAnneeAnticipation());
        viewBean.setIsAjournementRequerantVie(demandeRente.getIsAjournementRequerant());
        viewBean.setDateRevocationRequerantVie(demandeRente.getDateRevocationRequerant());

        viewBean.setSpy(demandeRente.getSpy());
        viewBean.setSession(demandeRente.getSession());

        return viewBean;
    }

    /**
	 *
	 */
    public static RESaisieDemandeRenteViewBean buildSaisieDemandeRenteViweBean(String idDemandeRente, BSession session,
            BTransaction transaction) throws Exception {
        // on cherche la demande de rente pour trouver le type
        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(session);
        demandeRente.setIdDemandeRente(idDemandeRente);
        demandeRente.retrieve(transaction);

        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demandeRente.getCsTypeDemandeRente())) {
            REDemandeRenteAPI demandeRenteAPI = new REDemandeRenteAPI();
            demandeRenteAPI.setSession(session);
            demandeRenteAPI.setIdDemandeRente(idDemandeRente);
            demandeRenteAPI.retrieve(transaction);
            return buildSaisieDemandeRenteViweBean(demandeRenteAPI, transaction);
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
            REDemandeRenteInvalidite demandeRenteInvalidite = new REDemandeRenteInvalidite();
            demandeRenteInvalidite.setSession(session);
            demandeRenteInvalidite.setIdDemandeRente(idDemandeRente);
            demandeRenteInvalidite.retrieve(transaction);
            return buildSaisieDemandeRenteViweBean(demandeRenteInvalidite, transaction);
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
            REDemandeRenteSurvivant demandeRenteSurvivant = new REDemandeRenteSurvivant();
            demandeRenteSurvivant.setSession(session);
            demandeRenteSurvivant.setIdDemandeRente(idDemandeRente);
            demandeRenteSurvivant.retrieve(transaction);
            return buildSaisieDemandeRenteViweBean(demandeRenteSurvivant, transaction);
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())) {
            REDemandeRenteVieillesse demandeRenteVieillesse = new REDemandeRenteVieillesse();
            demandeRenteVieillesse.setSession(session);
            demandeRenteVieillesse.setIdDemandeRente(idDemandeRente);
            demandeRenteVieillesse.retrieve(transaction);
            return buildSaisieDemandeRenteViweBean(demandeRenteVieillesse, transaction);
        } else {
            throw new Exception("Type de demande de rente inconnu");
        }
    }

}
