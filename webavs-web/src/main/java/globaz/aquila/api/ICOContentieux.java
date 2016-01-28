/*
 * Cr�� le 18 janv. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
 * Interface d'acc�s aux contentieux Aquila.
 * </p>
 * 
 * @author vre
 */
public interface ICOContentieux extends BIEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * criteres pour s�lectionner un contentieux d'apr�s l'identifiant de la section.
     */
    String FOR_ID_SECTION = "forIdSection";

    /** crit�re pour s�lection un contentieux pour une s�quence donn�e. */
    String FOR_ID_SEQUENCE_CONTENTIEUX = "forIdSequence";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * annule la derni�re �tape d'un contentieux identifi�s par les crit�res transmis en argument.
     * 
     * @param criteres
     *            crit�res identifiant le contentieux
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             si les crit�res sont insuffisants pour identifier un contentieux.
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
     * @return le contentieux cr��
     * @throws Exception
     *             si le contentieux n'a pas pu �tre cr��
     */
    ICOContentieux creerContentieux(BSession session, BTransaction transaction, String idSection, String csSequence,
            String dateProchainDeclenchement, String remarque) throws Exception;

    /**
     * Cr�er un contentieux en passant directement � l'�tape de sommation avec la mention "Sursis au paiement".
     * <p>
     * Si un contentieux existe d�j� pour cette section, et que l'�tape sommation est d�j� pass�e, on ne fait rien.
     * </p>
     * 
     * @param session
     *            la session de l'application
     * @param transaction
     *            la transaction pour l'insertion
     * @param idSection
     *            l'identifiant DB de la section
     * @param dateExecution
     *            la date d'ex�cution pour la sommation
     * @return une instance de ICOContentieux repr�sentant soit le contentieux qui vient d'�tre cr��, soit celui
     *         existant.
     * @throws Exception
     *             DOCUMENT ME!
     */
    ICOContentieux creerSommationSursisPaiement(BSession session, BTransaction transaction, String idSection,
            String dateExecution) throws Exception;

    /**
     * Essaie de trouver l'idContentieux correspondant � un id de section
     * 
     * @param idContentieux
     *            (peut �tre null ou vide)
     */
    String findIdContentieuxForIdSection(String idSection) throws Exception;

    /**
     * retourne le code syst�me de la derni�re �tape (action) effectu�e.
     * 
     * @return un CS de la famille COETAPP
     */
    String getCsDerniereEtapeContentieux();

    /**
     * retourne le code syst�me de la s�quence de ce contentieux.
     * 
     * @return un CS de la famille COSEQP
     */
    String getCsSequenceContentieux();

    /**
     * retourne la date de d�clenchement de la derni�re �tape effectu�e.
     * 
     * @return une date au formatt�e
     */
    String getDateDeclenchement();

    /**
     * retourne la date d'ex�cution de la derni�re �tape effectu�e.
     * 
     * @return une date formatt�e
     */
    String getDateExecution();

    /**
     * retourne la date d'ouverture du contentieux.
     * 
     * @return une date formatt�e
     */
    String getDateOuverture();

    /**
     * retourne l'identifiant du contentieux.
     * 
     * @return un identifiant
     */
    String getIdContentieux();

    /**
     * retourne l'�tape courante du contentieux.
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
     * retourne l'identifiant de la s�quence du contentieux.
     * 
     * @return un identifiant
     */
    String getIdSequenceContentieux();

    /**
     * Donne le nombre de fois ou un d�lai a �t� mut� pour un contentieux.
     * 
     * @return le nb de fois ou le d�lai a �t� mut�
     */
    String getNbDelaiMute();

    /**
     * retourne la date du d�clenchement de la prochaine �tape de ce contentieux.
     * 
     * @return une date formatt�e
     */
    String getProchaineDateDeclenchement();

    /**
     * retourne vrai s'il existe un contentieux correspondants aux crit�res transmis.
     * 
     * @param criteres
     *            une map non nulle de crit�res (utilis�s les cl�s d�finies dans cette interface)
     * @return vrai s'il existe au moins un contentieux correspondants aux crit�res transmis.
     * @throws Exception
     *             si le chargement �choue
     */
    boolean hasContentieux(HashMap criteres) throws Exception;

    /**
     * v�rifie si l'�tape a �t� ex�cut�e (et qu'elle n'a pas �t� annul�e).
     * 
     * @param csEtape
     *            le code syst�me correspondant au libell� de l'�tape
     * @param dateDe
     *            peut-�tre null
     * @param dateA
     *            peut �tre null
     * @param forIdContentieux
     *            DOCUMENT ME!
     * @param forIdSequence
     *            DOCUMENT ME!
     * @return true si l'action a �t� �x�cut�e entre les 2 dates. Si une ou les 2 dates sont null, ne prend pas en
     *         compte les dates.
     * @throws Exception
     *             DOCUMENT ME!
     */
    boolean isEtapeExecutee(String csEtape, String dateDe, String dateA, String forIdContentieux, String forIdSequence)
            throws Exception;

    /**
     * retourne la liste des contentieux correspondants aux crit�res transmis.
     * 
     * @param criteres
     *            une map non nulle de crit�res (utilis�s les cl�s d�finies dans cette interface)
     * @return une collection jamais nulle, peut-�tre vide d'instances de ICOContentieux
     * @throws Exception
     *             si le chargement �choue
     */
    Collection /* ICOContentieux */load(HashMap criteres) throws Exception;

    /**
     * Charge la derni�re �tape pour ce contentieux en se basant sur l'identifiant de l'�tape.
     * <p>
     * Cette m�thode ne peut �tre appell�e que pour une instance retourn�e par {@link #load(HashMap) load}.
     * </p>
     * 
     * @param session
     *            une session pour pouvoir charger l'etape
     * @return une instance de ICOEtape, jamais nulle
     * @throws Exception
     *             si l'etape ne peut �tre trouv�e.
     */
    ICOEtape loadDerniereEtape(BSession session) throws Exception;
}
