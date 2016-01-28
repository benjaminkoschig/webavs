package ch.globaz.osiris.business.data;

import java.util.ArrayList;
import java.util.Collection;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.OrdreVersementComplexModel;

/**
 * Conteneur permettant de d'associer un journal et des �critures a celui ci.<br>
 * Il est utilis� lors de l'ajout en une seule fois d'un journal et de ses �critures.
 * 
 * @author SCO
 */
public class JournalConteneur {

    /** Conteneur pour les �criture */
    private Collection<EcritureSimpleModel> collectionEcriture;
    /** Conteneur pour les ordres de versement */
    private Collection<OrdreVersementComplexModel> collectionOrdresVersement;
    /** mod�le du journal auquel sont li� les ordre et les �critures */
    private JournalSimpleModel journalModel;

    /**
     * Constructeur.
     * 
     * Initialise les collections
     */
    public JournalConteneur() {
        collectionEcriture = new ArrayList<EcritureSimpleModel>();
        collectionOrdresVersement = new ArrayList<OrdreVersementComplexModel>();
    }

    public void addEcriture(EcritureSimpleModel ecriture) {
        collectionEcriture.add(ecriture);
    }

    /**
     * Ajoute une op�ration de type �criture dans la collection d'�criture
     * 
     * @param libelle
     *            Libell� de l'�criture
     * @param codeDebitCredit
     *            Le code du d�bit ou cr�dit
     * @param idCompteAnnexe
     *            L'identifiant du compte annexe
     * @param idSection
     *            L'identification de section
     * @param date
     *            la date de l'�criture
     * @param numeroRubrique
     *            Un num�ro de rubrique (idExterne)
     * @param montant
     *            Le montant de l'ecriture
     */
    public void addEcriture(String libelle, String codeDebitCredit, String idCompteAnnexe, String idSection,
            String date, String numeroRubrique, String montant) {

        EcritureSimpleModel operationModel = new EcritureSimpleModel();

        operationModel.setLibelle(libelle);
        operationModel.setCodeDebitCredit(codeDebitCredit);
        operationModel.setIdCompteAnnexe(idCompteAnnexe);
        operationModel.setIdSection(idSection);
        operationModel.setDate(date);
        operationModel.setIdCompte(numeroRubrique);
        operationModel.setMontant(montant);

        collectionEcriture.add(operationModel);
    }

    /**
     * Ajoute le journal qui sera utilis� pour l'ajout des op�rations
     * 
     * @param journal
     *            mod�le du journal
     */
    public void AddJournal(JournalSimpleModel journal) {
        journalModel = journal;
    }

    /**
     * Ajoute les informations du journal qui sera cr�e pour l'ajout des op�rations
     * 
     * @param libelle
     *            Le libelle du journal
     * @param dateValeur
     *            La date comptable du journal
     */
    public void AddJournal(String libelle, String dateValeur) {

        journalModel = new JournalSimpleModel();
        journalModel.setLibelle(libelle);
        journalModel.setDateValeurCG(dateValeur);
    }

    /**
     * Ajoute un ordre de versement dans la collection des ordres de versement
     * 
     * @param ordreVersement
     *            l'ordre � ajouter
     */
    public void addOrdreVersement(OrdreVersementComplexModel ordreVersement) {
        collectionOrdresVersement.add(ordreVersement);
    }

    /**
     * Ajoute un ordre de versement dans la collection des ordres de versement
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
     *            Le montant de l'ordre de versement
     * @param codeIsoMonnaieBoni
     *            le code iso monnaie de bonification
     * @param codeIsoMonnaieDepot
     *            le code iso monnaie de d�p�t
     * @param typeVirement
     *            Le type de virement
     * @param natureOrdre
     *            La nature de l'odre de versement
     * @param motif
     *            Le motif de l'ordre de versement
     */
    public void addOrdreVersement(String idJournal, String idCompteAnnexe, String idSection, String idAdressePaiement,
            String date, String montant, String codeIsoMonnaieBoni, String codeIsoMonnaieDepot, String typeVirement,
            String natureOrdre, String motif) {

        OrdreVersementComplexModel ordreVersement = new OrdreVersementComplexModel();
        ordreVersement.getOperation().setIdJournal(idJournal);
        ordreVersement.getOperation().setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.getOperation().setIdSection(idSection);
        ordreVersement.getOperation().setDate(date);
        ordreVersement.getOperation().setMontant(montant);

        ordreVersement.getOrdre().setIdAdressePaiement(idAdressePaiement);
        ordreVersement.getOrdre().setCodeISOMonnaieBonification(codeIsoMonnaieBoni);
        ordreVersement.getOrdre().setCodeISOMonnaieDepot(codeIsoMonnaieDepot);
        ordreVersement.getOrdre().setTypeVirement(typeVirement);
        ordreVersement.getOrdre().setNatureOrdre(natureOrdre);
        ordreVersement.getOrdre().setMotif(motif);

        collectionOrdresVersement.add(ordreVersement);
    }

    /**
     * @return la liste des �critures
     */
    public Collection<EcritureSimpleModel> getCollectionEcriture() {
        return collectionEcriture;
    }

    /**
     * @return la liste des ordres de versement
     */
    public Collection<OrdreVersementComplexModel> getCollectionOrdreVersement() {
        return collectionOrdresVersement;
    }

    /**
     * @return le mod�le du journal
     */
    public JournalSimpleModel getJournalModel() {
        return journalModel;
    }
}
