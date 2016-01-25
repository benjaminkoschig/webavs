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
     * Service qui insere une opération dans un journal
     * 
     * @param idJournal
     *            L'identifiant du journal ou sera ajouté l'écriture
     * @param codeDebitCredit
     *            Le code de l'écriture (code débit ou code crédit)¨
     * @param idCompteAnnexe
     *            L'identifiant d'un compte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param date
     *            La Date comptable de l'écriture
     * @param numeroRubrique
     *            le numéro de la rubrique
     * @param montant
     *            Le montant de l'écriture.
     * @return EcritureSimpleModel Le model correspondant à l'écriture
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    EcritureSimpleModel addEcriture(String idJournal, String codeDebitCredit, String idCompteAnnexe, String idSection,
            String date, String numeroRubrique, String montant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service qui insere une opération dans un journal par référence rubrique
     * 
     * @param idJournal
     *            L'identifiant du journal ou sera ajouté l'écriture
     * @param libelle
     *            Libellé de l'écriture
     * @param codeDebitCredit
     *            Le code de l'écriture (code débit ou code crédit)¨
     * @param idCompteAnnexe
     *            L'identifiant d'un compte annexe
     * @param idSection
     *            L'identifiant d'une section
     * @param date
     *            La Date comptable de l'écriture
     * @param referenceRubrique
     *            le numéro de référence de la rubrique
     * @param montant
     *            Le montant de l'écriture.
     * @return EcritureSimpleModel Le model correspondant à l'écriture
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public EcritureSimpleModel addEcritureByRefRubrique(String idJournal, String libelle, String codeDebitCredit,
            String idCompteAnnexe, String idSection, String date, String referenceRubrique, String montant)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de création d'un ordre de versement
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
     *            le code iso monnaie de dépot
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
     * Service de création d'un ordre de versement
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
     *            le code iso monnaie de dépot
     * @param typeVirement
     *            Le type de virement
     * @param natureOrdre
     *            La nature de l'odre de versement
     * @param motif
     *            Le motif de l'ordre de versement
     * @param referenceBVR
     *            Le numéro de reference BVR de l'ordre de versement
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
     * @return Le journal dans l'état comptabilisé
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
     * @return Le journal dans l'état comptabilisé
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    JournalSimpleModel comptabiliseSynchrone(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Création d'un journal
     * 
     * @param libelle
     *            Un libelle de journal
     * @param dateValeur
     *            La date comptable du journal
     * @return Le model de journal avec un id unique
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    JournalSimpleModel createJournal(String libelle, String dateValeur) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service de création d'un journal et de ses écritures en utilisant un conteneur.<br>
     * Ce service permet de passer une conteneur qui contient les informations de création d'un journal ainsi<BR>
     * que la liste des écritures que l'on va lui fournir. Cela permet de faire une seule fois l'appel au service.
     * 
     * @param conteneur
     * @return
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    JournalSimpleModel createJournalAndOperations(JournalConteneur conteneur) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service qui retourne le total des écritures contenues dans le journal.
     * 
     * @param journalModel
     *            Un model de journal
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    String getSommeEcritures(JournalSimpleModel journalModel) throws JadePersistenceException, JadeApplicationException;
}
