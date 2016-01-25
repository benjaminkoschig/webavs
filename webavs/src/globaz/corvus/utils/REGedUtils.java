package globaz.corvus.utils;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Classe regroupant différentes méthodes utilitaires traitant de la manipulation de la GED dans les rentes
 * 
 * @author PBA
 */
public class REGedUtils {

    /**
     * Liste des type de rente servant à ranger les documents par catégorie dans la GED.<br />
     * Voir mandat InfoRom 533 pour plus de précision.
     * 
     * @author PBA
     */
    public static enum TypeRente {
        RenteAI("type.demande.rente.ai.pour.ged"),
        RenteAPI_AI("type.demande.rente.api.ai.pour.ged"),
        RenteAPI_AVS("type.demande.rente.api.avs.pour.ged"),
        RenteAVS("type.demande.rente.avs.pour.ged");

        private String clePropriete;

        private TypeRente(String clePropriete) {
            this.clePropriete = clePropriete;
        }

        public String getClePropriete() {
            return clePropriete;
        }
    }

    /**
     * Clé de la propriété pour la GED permettant de trier le document dans le bon dossier (à mettre dans
     * {@link JadePublishDocumentInfo#setPublishProperty(String, String)} comme clé, et la valeur retournée par
     * {@link #getCleGedPourTypeRente(BSession, TypeRente)} comme valeur)
     */
    public static final String PROPRIETE_GED_TYPE_DEMANDE_RENTE = "type.demande.rente";

    /**
     * Retourne la clé utilisée pour trier les documents en GED, selon le type de rente traité (ou le type de demande de
     * rente).<br />
     * Implémenté pour le mandat InfoRom 533.
     * 
     * @param session
     *            une session utilisateur valide, connectée à l'application CORVUS
     * @param typeRente
     *            le type de rente traité par le document
     * @return la clé de tri pour la GED provenant de la configuration en base de données
     * 
     * @throws RETechnicalException
     *             en cas de valeurs erronées en entrée, ou si un problème survient lors de l'accès aux propriétés en
     *             base de données
     * @see {@link TypeRente} pour les clés des propriétés en base de données
     */
    public static String getCleGedPourTypeRente(BSession session, TypeRente typeRente) throws RETechnicalException {
        if ((session == null) || !session.isConnected()) {
            throw new RETechnicalException("A connected session is needed to retrieve this property");
        }
        if (typeRente == null) {
            throw new RETechnicalException("typeRente can't be null");
        }

        switch (typeRente) {
            case RenteAI:
            case RenteAPI_AI:
            case RenteAPI_AVS:
            case RenteAVS:
                try {
                    return session.getApplication().getProperty(typeRente.getClePropriete());
                } catch (Exception ex) {
                    throw new RETechnicalException("error while retriving property", ex);
                }

            default:
                throw new RETechnicalException("typeRente is invalid");
        }
    }

    public static TypeRente getTypeRentePourCetteDecision(BSession session, REDecisionEntity decision) throws Exception {
        if (!decision.isNew()) {
            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setIdDemandeRente(decision.getIdDemandeRente());
            demandeRente.setSession(session);
            demandeRente.retrieve();

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equalsIgnoreCase(demandeRente.getCsTypeDemandeRente())) {
                return getTypeRentePourCetteDecisionAPI(session, decision);
            }

            return REGedUtils.getTypeRentePourCetteDemandeRente(session, demandeRente);
        }
        return null;
    }

    private static TypeRente getTypeRentePourCetteDecisionAPI(BSession session, REDecisionEntity decision)
            throws Exception {

        String csGenreDroitAPI = "";

        try {
            csGenreDroitAPI = getRenteAccordeeDeLaDecision(session, decision).get(0).getCsGenreDroitApi();

        } catch (Exception e) {
            csGenreDroitAPI = "";
        }

        if (JadeStringUtil.isBlankOrZero(csGenreDroitAPI)) {
            return TypeRente.RenteAPI_AVS;
        } else if (IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST.equalsIgnoreCase(csGenreDroitAPI)
                || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE.equalsIgnoreCase(csGenreDroitAPI)) {
            return TypeRente.RenteAPI_AI;
        } else if (IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equalsIgnoreCase(csGenreDroitAPI)
                || IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI.equalsIgnoreCase(csGenreDroitAPI)) {
            return TypeRente.RenteAPI_AVS;
        }

        return null;

    }

    private static List<RERenteAccordee> getRenteAccordeeDeLaDecision(final BSession session,
            final REDecisionEntity decision) throws Exception {

        List<RERenteAccordee> renteAccordeesDecision = new ArrayList<RERenteAccordee>();
        REValidationDecisionsManager renteValideeManager = new REValidationDecisionsManager();
        renteValideeManager.setSession(session);

        REPrestationsDuesManager prestationsDuesManager = new REPrestationsDuesManager();
        prestationsDuesManager.setSession(session);

        // On récupère toutes les rentes validées liées à la décision
        renteValideeManager.setForIdDecision(decision.getIdDecision());

        renteValideeManager.find(BManager.SIZE_NOLIMIT);
        for (Object o1 : renteValideeManager.getContainer()) {
            REValidationDecisions renteValidee = (REValidationDecisions) o1;

            // On récupère toutes les prestations dues de chaque rente validée
            prestationsDuesManager.setForIdsPrestDues(renteValidee.getIdPrestationDue());
            prestationsDuesManager.find(BManager.SIZE_NOLIMIT);
            for (Object o2 : prestationsDuesManager.getContainer()) {
                REPrestationDue prestationDue = (REPrestationDue) o2;

                // Pour chaque prestation due, on récupère le code prestation de la prestation accordée
                String idPrestationAccordee = prestationDue.getIdRenteAccordee();
                if (JadeStringUtil.isBlankOrZero(idPrestationAccordee)) {
                    throw new Exception(
                            "Can not retrieve the REPrestationsAccordees from the the REPrestationDue with id ["
                                    + prestationDue.getIdPrestationDue() + "]");
                }
                RERenteAccordee renteAccordee = new RERenteAccordee();
                renteAccordee.setSession(session);
                renteAccordee.setIdPrestationAccordee(idPrestationAccordee);
                renteAccordee.retrieve();
                if (renteAccordee.isNew()) {
                    throw new Exception("Can not retrieve the REPrestationsAccordees  with id [" + idPrestationAccordee
                            + "]");
                }
                renteAccordeesDecision.add(renteAccordee);
            }
        }
        return renteAccordeesDecision;
    }

    public static TypeRente getTypeRentePourCetteDemandeRente(BSession session, REDemandeRente demandeRente)
            throws Exception {
        return REGedUtils.getTypeRentePourCetteDemandeRente(session, demandeRente, false, false);
    }

    public static TypeRente getTypeRentePourCetteDemandeRente(BSession session, REDemandeRente demandeRente,
            boolean ignorerInvalidite, boolean ignorerAPI) throws Exception {
        if (!demandeRente.isNew()) {

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())
                    || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
                return TypeRente.RenteAVS;
            } else if (!ignorerInvalidite
                    && IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
                return TypeRente.RenteAI;
            } else if (!ignorerAPI
                    && IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demandeRente.getCsTypeDemandeRente())) {
                REDemandeRenteAPI demandeRenteAPI = new REDemandeRenteAPI();
                demandeRenteAPI.setSession(session);
                demandeRenteAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
                demandeRenteAPI.retrieve();

                return REGedUtils.getTypeRentePourCetteDemandeRenteAPI(session, demandeRenteAPI);
            }
        }
        return null;
    }

    public static TypeRente getTypeRentePourCetteDemandeRente(DemandeRente demandeRente) {
        Checkers.checkNotNull(demandeRente, "demandeRente");
        switch (demandeRente.getTypeDemandeRente()) {
            case DEMANDE_API:
                if (demandeRente.getRenteAccordeePrincipale().getCodePrestation().isAPIAI()) {
                    return TypeRente.RenteAPI_AI;
                } else {
                    return TypeRente.RenteAPI_AVS;
                }
            case DEMANDE_INVALIDITE:
                return TypeRente.RenteAI;
            case DEMANDE_SURVIVANT:
            case DEMANDE_VIEILLESSE:
                return TypeRente.RenteAVS;
            default:
                throw new IllegalArgumentException("unknow type");
        }
    }

    public static TypeRente getTypeRentePourCetteDemandeRenteAPI(BSession session, REDemandeRenteAPI demandeRenteAPI) {

        if (!demandeRenteAPI.isNew()) {

            String csGenreDroitAPI = "";

            RERenteAccJoinTblTiersJoinDemRenteManager renteAccJoinTblTiersJoinDemRenteManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
            renteAccJoinTblTiersJoinDemRenteManager.setSession(session);
            renteAccJoinTblTiersJoinDemRenteManager.setForNoDemandeRente(demandeRenteAPI.getIdDemandeRente());
            renteAccJoinTblTiersJoinDemRenteManager.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT
                    + " DESC ");

            try {
                renteAccJoinTblTiersJoinDemRenteManager.find(BManager.SIZE_NOLIMIT);
                RERenteAccJoinTblTiersJoinDemandeRente aRenteAccJoinTblTiersJoinDemandeRente = (RERenteAccJoinTblTiersJoinDemandeRente) renteAccJoinTblTiersJoinDemRenteManager
                        .getFirstEntity();
                csGenreDroitAPI = aRenteAccJoinTblTiersJoinDemandeRente.getCsGenreDroitApi();

            } catch (Exception e) {
                csGenreDroitAPI = "";
            }

            if (JadeStringUtil.isBlankOrZero(csGenreDroitAPI)) {
                return TypeRente.RenteAPI_AVS;
            } else if (IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST.equalsIgnoreCase(csGenreDroitAPI)
                    || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE.equalsIgnoreCase(csGenreDroitAPI)) {
                return TypeRente.RenteAPI_AI;
            } else if (IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equalsIgnoreCase(csGenreDroitAPI)
                    || IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI.equalsIgnoreCase(csGenreDroitAPI)) {
                return TypeRente.RenteAPI_AVS;
            }
        }
        return null;
    }

    public static TypeRente getTypeRentePourListeCodesPrestation(BSession session, Set<CodePrestation> rentes) {
        return REGedUtils.getTypeRentePourListeCodesPrestation(session, rentes, false);
    }

    public static TypeRente getTypeRentePourListeCodesPrestation(BSession session, Set<CodePrestation> rentes,
            boolean filtreMultiRente) {
        return REGedUtils.getTypeRentePourListeCodesPrestation(session, rentes, filtreMultiRente, false);
    }

    public static TypeRente getTypeRentePourListeCodesPrestation(BSession session, Set<CodePrestation> rentes,
            boolean filtreMultiRente, boolean ignorerAPI) {

        if (rentes != null) {

            if (filtreMultiRente) {

                if (rentes.contains(CodePrestation.getCodePrestation(50))
                        && rentes.contains(CodePrestation.getCodePrestation(81))) {
                    return TypeRente.RenteAI;
                }

                if (rentes.contains(CodePrestation.getCodePrestation(10))
                        && rentes.contains(CodePrestation.getCodePrestation(85))) {
                    return TypeRente.RenteAVS;
                }

                if (rentes.contains(CodePrestation.getCodePrestation(14))
                        && rentes.contains(CodePrestation.getCodePrestation(55))) {
                    return TypeRente.RenteAVS;
                }

                if (rentes.contains(CodePrestation.getCodePrestation(14))
                        && rentes.contains(CodePrestation.getCodePrestation(81))) {
                    return TypeRente.RenteAVS;
                }
            }

            for (CodePrestation unCode : rentes) {
                if (!ignorerAPI && unCode.isAPIAVS()) {
                    return TypeRente.RenteAPI_AVS;
                } else if (!ignorerAPI && unCode.isAPIAI()) {
                    return TypeRente.RenteAPI_AI;
                } else if (unCode.isAI()) {
                    return TypeRente.RenteAI;
                } else if (unCode.isVieillesse() || unCode.isSurvivant()) {
                    return TypeRente.RenteAVS;
                }
            }
        }

        return null;
    }
}
