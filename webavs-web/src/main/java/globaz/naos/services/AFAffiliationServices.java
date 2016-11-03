package globaz.naos.services;

import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.exceptions.AFTechnicalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ch.globaz.common.domaine.Checkers;
import com.google.common.base.Joiner;

/**
 * Cette classe est une classe de service permettant de retourner des objets de type AFAffiliation
 * 
 * @author jwe
 * 
 */

public final class AFAffiliationServices {

    private AFAffiliationServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Récupération de la liste des particularités pour une affiliation
     * 
     * @param idAffiliation Un id d'affiliation
     * @param session Une session valide
     * @return Une liste de particularité
     */
    public static List<AFParticulariteAffiliation> findListParticulariteAffiliation(String idAffiliation,
            BSession session) {
        return findListParticulariteAffiliation(idAffiliation, null, session);
    }

    /**
     * Récupération de la liste des particularités pour une affiliation et pour une année donnée
     * 
     * @param idAffiliation Un id d'affiliation
     * @param annee Une année
     * @param session Une session valide
     * @return Une liste de particularité
     */
    public static List<AFParticulariteAffiliation> findListParticulariteAffiliation(String idAffiliation, String annee,
            BSession session) {

        if (idAffiliation == null) {
            throw new NullPointerException("idAffiliation must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        AFParticulariteAffiliationManager manager = new AFParticulariteAffiliationManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);

        if (!JadeStringUtil.isEmpty(annee)) {
            manager.setDateDebutLessOrEqual("31.12." + annee);
            manager.setDateFinGreatOrEqual("01.01." + annee);
        }

        try {
            manager.find();
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer les particularités de l'affiliation id : "
                    + idAffiliation, e);
        }

        List<AFParticulariteAffiliation> listParticularites = new ArrayList<AFParticulariteAffiliation>();

        if (manager.isEmpty()) {
            return listParticularites;
        }

        listParticularites.addAll(manager.getContainer());

        return listParticularites;

    }

    public static List<AFAffiliation> searchAffiliationParitaireByNumero(String affilieNumero, String dateDebut,
            String dateFin, BSession session) {
        Checkers.checkNotNull(session, "session");
        Checkers.checkNotNull(affilieNumero, "dateDebut");
        Checkers.checkNotNull(affilieNumero, "dateFin");

        AFAffiliationManager affiliationManager = new AFAffiliationManager();

        affiliationManager.setForAffilieNumero(affilieNumero);
        affiliationManager.setSession(session);
        affiliationManager.setForTypesAffParitaires();
        affiliationManager.setForDateDebutAffLowerOrEqualTo(dateFin);
        affiliationManager.setForDateFinGreater(dateDebut);

        try {
            affiliationManager.find();
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer une affiliation paritaire pour la période donnée ["
                    + affilieNumero + "," + dateDebut + "," + dateFin + "]", e);
        }

        List<AFAffiliation> listAffilie = new ArrayList<AFAffiliation>();

        if (affiliationManager.isEmpty()) {
            return listAffilie;
        }

        listAffilie.addAll(affiliationManager.getContainer());

        return listAffilie;
    }

    public static AFAffiliation getAffiliationParitaireByNumero(String affilieNumero, String annee, BSession session) {

        if (affilieNumero == null) {
            throw new NullPointerException("affilieNumero must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        if (annee == null) {
            throw new NullPointerException("annee must be not null");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();

        affiliationManager.setForAffilieNumero(affilieNumero);
        affiliationManager.setSession(session);
        affiliationManager.setForTypesAffParitaires();
        affiliationManager.setForAnnee(annee);

        try {
            affiliationManager.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer l'affiliation paritaire active de numéro : "
                    + affilieNumero, e);
        }

        if (!affiliationManager.isEmpty()) {
            return (AFAffiliation) affiliationManager.getFirstEntity();
        }

        return null;
    }

    /**
     * Cette méthode retourne une affiliaton en fonction du numéro d'affilié passé en paramètre
     * La méthode retourne uniquement une affiliation active de type paritaire
     * 
     * @param affilieNumero Un numéro d'affilié
     * @param session Une session
     * @return Une affiliation, null si aucune affiliation trouvée
     * @throws NullPointerException Si le numéro d'affilié est null
     * @throws NullPointerException Si la session est null
     * @throws AFTechnicalException Si la récupération de l'affilaition a échoué
     */
    public static AFAffiliation getAffiliationActiveParitaireByNumero(String affilieNumero, BSession session) {

        if (affilieNumero == null) {
            throw new NullPointerException("affilieNumero must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();

        affiliationManager.setForAffilieNumero(affilieNumero);
        affiliationManager.setSession(session);
        affiliationManager.setForActif(true);
        affiliationManager.setForTypesAffParitaires();

        try {
            affiliationManager.find();
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer l'affiliation paritaire active de numéro : "
                    + affilieNumero, e);
        }

        if (affiliationManager.size() > 0) {
            return ((AFAffiliation) affiliationManager.getFirstEntity());
        }

        return null;
    }

    /**
     * Cette méthode permet de retourner un manager contenant une liste d'affiliations (actives et non actives)
     * 
     * @param idTiers : l'id du tiers
     * @param session : la session active
     * @return une liste d'affiliations si le manager en contient, sinon null
     * @throws NullPointerException Si idTiers ou session = null
     */
    public static List<AFAffiliation> getAffiliationsByIDTiers(String idTiers, BSession session) {

        if (idTiers == null) {
            throw new NullPointerException("idTiers must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setForIdTiers(idTiers);
        affiliationManager.setSession(session);
        try {
            affiliationManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new AFTechnicalException(
                    "Impossible de récupérer la ou les affilition(s) de l'id tiers : " + idTiers, e);
        }

        List<AFAffiliation> affiliationsList = new ArrayList<AFAffiliation>();

        for (int i = 0; i < affiliationManager.size(); i++) {
            AFAffiliation affiliaton = (AFAffiliation) affiliationManager.get(i);
            affiliationsList.add(affiliaton);
        }
        if (affiliationsList.size() > 0) {
            return affiliationsList;
        }
        return null;
    }

    /**
     * Cette méthode retourne des affiliatons en fonction des numéros d'affilié passé en paramètre
     * 
     * @param affilieNumero Un numéro d'affilié
     * @param session Une session
     * @return Une List d'affiliation, aucune affiliation trouvée retourne une liste vide
     * @throws NullPointerException Si le numéro d'affilié est null
     * @throws NullPointerException Si la session est null
     * @throws AFTechnicalException Si la récupération de l'affilaition a échoué
     */
    public static List<AFAffiliation> searchAffiliationByNumeros(Collection<String> affilieNumeros, BSession session) {

        if (affilieNumeros == null) {
            throw new NullPointerException("affilieNumero must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        List<AFAffiliation> affiliations = new ArrayList<AFAffiliation>();
        if (!affilieNumeros.isEmpty()) {

            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            String in = "\'" + Joiner.on("\',\'").skipNulls().join(affilieNumeros) + "\'";
            affiliationManager.setForAffilieNumeroIn(in);
            affiliationManager.setSession(session);
            affiliationManager.setForTypesAffParitaires();

            try {
                affiliationManager.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new AFTechnicalException("Impossible de récupérer les affiliation, numéros : " + in, e);
            }
            affiliations = affiliationManager.toList();
        }
        return affiliations;
    }

    /**
     * Cette méthode retourne des affiliatons en fonction des id d'affilié passé en paramètre
     */
    public static List<AFAffiliation> searchAffiliationByIds(Collection<String> ids, BSession session) {

        if (ids == null) {
            throw new NullPointerException("affilieNumero must be not null");
        }

        if (session == null) {
            throw new NullPointerException("session must be not null");
        }

        List<AFAffiliation> affiliations = new ArrayList<AFAffiliation>();
        if (!ids.isEmpty()) {

            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setForIdsAffiliation(ids);
            affiliationManager.setSession(session);
            affiliationManager.setForTypesAffParitaires();

            try {
                affiliationManager.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new AFTechnicalException("Impossible de récupérer les affiliation, ids : "
                        + Joiner.on(",").join(ids), e);
            }
            affiliations = affiliationManager.toList();
        }
        return affiliations;
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public static boolean hasRightAccesSecurity(String numAffilie) {
        return hasRightAccesSecurity(numAffilie, BSessionUtil.getSessionFromThreadContext());
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public static boolean hasRightAccesSecurity(String numAffilie, BSession session) {
        List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(
                Arrays.asList(numAffilie.trim()), session);

        if (!affiliations.isEmpty()) {
            return hasRightAccesSecurity(affiliations.get(0), session);
        }

        return false;
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public static boolean hasRightAccesSecurity(AFAffiliation affiliation) {
        return hasRightAccesSecurity(affiliation, BSessionUtil.getSessionFromThreadContext());
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public static boolean hasRightAccesSecurity(AFAffiliation affiliation, BSession session) {
        try {
            Integer affilieSecurity = 0;

            if (affiliation == null) {
                return false;
            } else {
                String codeSecurity = affiliation.getAccesSecurite();
                // On utilise la dernier numéro du code pour avoir le niveau
                affilieSecurity = Integer.parseInt(codeSecurity.substring(codeSecurity.length() - 1));
            }
            return hasRightAccesSecurity(affilieSecurity, session);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public static boolean hasRightAccesSecurity(Integer niveauSecurity, BSession session) {
        try {
            // recherche du complément "secureCode" du user
            FWSecureUserDetail user = new FWSecureUserDetail();

            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(AFAffiliation.SECURE_CODE);

            user.retrieve();

            Integer userSecurity = 0;

            if (!JadeStringUtil.isEmpty(user.getData())) {
                userSecurity = Integer.parseInt(user.getData());
            }

            if (niveauSecurity <= userSecurity) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

}
