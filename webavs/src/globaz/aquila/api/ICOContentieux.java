/*
 * Créé le 18 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api;

import globaz.globall.api.BIEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.Collection;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * <p>
 * Interface d'accès aux contentieux Aquila.
 * </p>
 * 
 * @author vre
 */
public interface ICOContentieux extends BIEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * criteres pour sélectionner un contentieux d'après l'identifiant de la section.
     */
    String FOR_ID_SECTION = "forIdSection";

    /** critère pour sélection un contentieux pour une séquence donnée. */
    String FOR_ID_SEQUENCE_CONTENTIEUX = "forIdSequence";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * annule la dernière étape d'un contentieux identifiés par les critères transmis en argument.
     * 
     * @param criteres
     *            critères identifiant le contentieux
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             si les critères sont insuffisants pour identifier un contentieux.
     */
    void annulerDerniereEtape(HashMap criteres, BTransaction transaction) throws Exception;

    /**
     * met une section en contentieux.
     * 
     * @param session
     *            une session AQUILA !!!
     * @param transaction
     *            une transaction ou null
     * @param idSection
     *            DOCUMENT ME!
     * @param csSequence
     *            DOCUMENT ME!
     * @param dateProchainDeclenchement
     *            DOCUMENT ME!
     * @param remarque
     *            DOCUMENT ME!
     * @return le contentieux créé
     * @throws Exception
     *             si le contentieux n'a pas pu être créé
     */
    ICOContentieux creerContentieux(BSession session, BTransaction transaction, String idSection, String csSequence,
            String dateProchainDeclenchement, String remarque) throws Exception;

    /**
     * Créer un contentieux en passant directement à l'étape de sommation avec la mention "Sursis au paiement".
     * <p>
     * Si un contentieux existe déjà pour cette section, et que l'étape sommation est déjà passée, on ne fait rien.
     * </p>
     * 
     * @param session
     *            la session de l'application
     * @param transaction
     *            la transaction pour l'insertion
     * @param idSection
     *            l'identifiant DB de la section
     * @param dateExecution
     *            la date d'exécution pour la sommation
     * @return une instance de ICOContentieux représentant soit le contentieux qui vient d'être créé, soit celui
     *         existant.
     * @throws Exception
     *             DOCUMENT ME!
     */
    ICOContentieux creerSommationSursisPaiement(BSession session, BTransaction transaction, String idSection,
            String dateExecution) throws Exception;

    /**
     * Essaie de trouver l'idContentieux correspondant à un id de section
     * 
     * @param idContentieux
     *            (peut être null ou vide)
     */
    String findIdContentieuxForIdSection(String idSection) throws Exception;

    /**
     * retourne le code système de la dernière étape (action) effectuée.
     * 
     * @return un CS de la famille COETAPP
     */
    String getCsDerniereEtapeContentieux();

    /**
     * retourne le code système de la séquence de ce contentieux.
     * 
     * @return un CS de la famille COSEQP
     */
    String getCsSequenceContentieux();

    /**
     * retourne la date de déclenchement de la dernière étape effectuée.
     * 
     * @return une date au formattée
     */
    String getDateDeclenchement();

    /**
     * retourne la date d'exécution de la dernière étape effectuée.
     * 
     * @return une date formattée
     */
    String getDateExecution();

    /**
     * retourne la date d'ouverture du contentieux.
     * 
     * @return une date formattée
     */
    String getDateOuverture();

    /**
     * retourne l'identifiant du contentieux.
     * 
     * @return un identifiant
     */
    String getIdContentieux();

    /**
     * retourne l'étape courante du contentieux.
     * 
     * @return DOCUMENT ME!
     */
    String getIdDerniereEtapeContentieux();

    /**
     * retourne l'identifiant de la section de ce contentieux.
     * 
     * @return un identifiant
     */
    String getIdSection();

    /**
     * retourne l'identifiant de la séquence du contentieux.
     * 
     * @return un identifiant
     */
    String getIdSequenceContentieux();

    /**
     * Donne le nombre de fois ou un délai a été muté pour un contentieux.
     * 
     * @return le nb de fois ou le délai a été muté
     */
    String getNbDelaiMute();

    /**
     * retourne la date du déclenchement de la prochaine étape de ce contentieux.
     * 
     * @return une date formattée
     */
    String getProchaineDateDeclenchement();

    /**
     * retourne vrai s'il existe un contentieux correspondants aux critères transmis.
     * 
     * @param criteres
     *            une map non nulle de critères (utilisés les clés définies dans cette interface)
     * @return vrai s'il existe au moins un contentieux correspondants aux critères transmis.
     * @throws Exception
     *             si le chargement échoue
     */
    boolean hasContentieux(HashMap criteres) throws Exception;

    /**
     * vérifie si l'étape a été exécutée (et qu'elle n'a pas été annulée).
     * 
     * @param csEtape
     *            le code système correspondant au libellé de l'étape
     * @param dateDe
     *            peut-être null
     * @param dateA
     *            peut être null
     * @param forIdContentieux
     *            DOCUMENT ME!
     * @param forIdSequence
     *            DOCUMENT ME!
     * @return true si l'action a été éxécutée entre les 2 dates. Si une ou les 2 dates sont null, ne prend pas en
     *         compte les dates.
     * @throws Exception
     *             DOCUMENT ME!
     */
    boolean isEtapeExecutee(String csEtape, String dateDe, String dateA, String forIdContentieux, String forIdSequence)
            throws Exception;

    /**
     * retourne la liste des contentieux correspondants aux critères transmis.
     * 
     * @param criteres
     *            une map non nulle de critères (utilisés les clés définies dans cette interface)
     * @return une collection jamais nulle, peut-être vide d'instances de ICOContentieux
     * @throws Exception
     *             si le chargement échoue
     */
    Collection /* ICOContentieux */load(HashMap criteres) throws Exception;

    /**
     * Charge la dernière étape pour ce contentieux en se basant sur l'identifiant de l'étape.
     * <p>
     * Cette méthode ne peut être appellée que pour une instance retournée par {@link #load(HashMap) load}.
     * </p>
     * 
     * @param session
     *            une session pour pouvoir charger l'etape
     * @return une instance de ICOEtape, jamais nulle
     * @throws Exception
     *             si l'etape ne peut être trouvée.
     */
    ICOEtape loadDerniereEtape(BSession session) throws Exception;
}
