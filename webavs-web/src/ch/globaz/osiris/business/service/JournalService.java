package ch.globaz.osiris.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.OrdreVersementComplexModel;

/**
 * Services d'un journal
 * 
 * @author SCO 19 mai 2010
 */
public interface JournalService extends JadeApplicationService {

    public static final String ANNULE = "202005";
    public static final String COMPTABILISE = "202002";
    public static final String ERREUR = "202004";
    /* Etat des journaux */
    public static final String OUVERT = "202001";
    public static final String PARTIEL = "202003";
    public static final String TRAITEMENT = "202006";

    /**
     * Service qui insere une op�ration dans un journal
     * 
     * @param idJournal
     *            L'identifiant du journal ou sera ajout� l'�criture
     * @param codeDebitCredit
     *            Le code de l'�criture (code d�bit ou code cr�dit)�
     * @param idCompteAnnexe
     *            L'identifiant d'un compte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param date
     *            La Date comptable de l'�criture
     * @param numeroRubrique
     *            le num�ro de la rubrique
     * @param montant
     *            Le montant de l'�criture.
     * @return EcritureSimpleModel Le model correspondant � l'�criture
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    EcritureSimpleModel addEcriture(String idJournal, String codeDebitCredit, String idCompteAnnexe, String idSection,
            String date, String numeroRubrique, String montant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service qui insere une op�ration dans un journal par r�f�rence rubrique
     * 
     * @param idJournal
     *            L'identifiant du journal ou sera ajout� l'�criture
     * @param libelle
     *            Libell� de l'�criture
     * @param codeDebitCredit
     *            Le code de l'�criture (code d�bit ou code cr�dit)�
     * @param idCompteAnnexe
     *            L'identifiant d'un compte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param date
     *            La Date comptable de l'�criture
     * @param referenceRubrique
     *            le num�ro de r�f�rence de la rubrique
     * @param montant
     *            Le montant de l'�criture.
     * @return EcritureSimpleModel Le model correspondant � l'�criture
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public EcritureSimpleModel addEcritureByRefRubrique(String idJournal, String libelle, String codeDebitCredit,
            String idCompteAnnexe, String idSection, String date, String referenceRubrique, String montant)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de cr�ation d'un ordre de versement
     * 
     * @param idJournal
     *            L'identifiant d'un journal
     * @param idCompteAnnexe
     *            L'identifiant d'un comte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param idAdressePaiement
     *            L'identifiant d'une adresse de paiement
     * @param date
     *            La date de l'ordre de versement
     * @param montant
     *            Le montant de l'ordre de versment
     * @param codeIsoMonnaieBoni
     *            le code iso monnaie de bonnification
     * @param codeIsoMonnaieDepot
     *            le code iso monnaie de d�pot
     * @param typeVirement
     *            Le type de virement
     * @param natureOrdre
     *            La nature de l'odre de versement
     * @param motif
     *            Le motif de l'ordre de versement
     * @return Le model de d'ordre de versement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    OrdreVersementComplexModel addOrdreVersement(String idJournal, String idCompteAnnexe, String idSection,
            String idAdressePaiement, String date, String montant, String codeIsoMonnaieBoni,
            String codeIsoMonnaieDepot, String typeVirement, String natureOrdre, String motif)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de cr�ation d'un ordre de versement
     * 
     * @param idJournal
     *            L'identifiant d'un journal
     * @param idCompteAnnexe
     *            L'identifiant d'un comte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param idAdressePaiement
     *            L'identifiant d'une adresse de paiement
     * @param date
     *            La date de l'ordre de versement
     * @param montant
     *            Le montant de l'ordre de versment
     * @param codeIsoMonnaieBoni
     *            le code iso monnaie de bonnification
     * @param codeIsoMonnaieDepot
     *            le code iso monnaie de d�pot
     * @param typeVirement
     *            Le type de virement
     * @param natureOrdre
     *            La nature de l'odre de versement
     * @param motif
     *            Le motif de l'ordre de versement
     * @param referenceBVR
     *            Le num�ro de reference BVR de l'ordre de versement
     * @return Le model de d'ordre de versement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    OrdreVersementComplexModel addOrdreVersement(String idJournal, String idCompteAnnexe, String idSection,
            String idAdressePaiement, String date, String montant, String codeIsoMonnaieBoni,
            String codeIsoMonnaieDepot, String typeVirement, String natureOrdre, String motif, String referenceBVR)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de comptabilisation d'un journal par le lancement d'un process
     * 
     * @param journalModel
     *            Un model de journal
     * @return Le journal dans l'�tat comptabilis�
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    JournalSimpleModel comptabilise(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service de comptabilisation d'un journal de facon synchrone
     * 
     * @param journalModel
     *            Un model de journal
     * @return Le journal dans l'�tat comptabilis�
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    JournalSimpleModel comptabiliseSynchrone(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Cr�ation d'un journal
     * 
     * @param libelle
     *            Un libelle de journal
     * @param dateValeur
     *            La date comptable du journal
     * @return Le model de journal avec un id unique
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    JournalSimpleModel createJournal(String libelle, String dateValeur) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service de cr�ation d'un journal et de ses �critures en utilisant un conteneur.<br>
     * Ce service permet de passer une conteneur qui contient les informations de cr�ation d'un journal ainsi<BR>
     * que la liste des �critures que l'on va lui fournir. Cela permet de faire une seule fois l'appel au service.
     * 
     * @param conteneur
     * @return
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    JournalSimpleModel createJournalAndOperations(JournalConteneur conteneur) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service qui retourne le total des �critures contenues dans le journal.
     * 
     * @param journalModel
     *            Un model de journal
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    String getSommeEcritures(JournalSimpleModel journalModel) throws JadePersistenceException, JadeApplicationException;
}
