/**
 * 
 */
package ch.globaz.al.business.services.attestationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;

/**
 * @author pta Service d�finissant les m�thodes de recherce pour less attestations de versements
 * 
 */
public interface AttestationsVersementBusinessService extends JadeApplicationService {
    /**
     * M�thode qui initialise lance la recherche en fonction du type de prestation
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @param periodeDe
     * @param periodeA
     * @param typePrestation
     *            type de prestation � rechercher
     * @return ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> returnListPaiement(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de recherche uniquement pour les paiements directs
     * 
     * @param dossierAttestation
     * @param periodeDe
     *            d�but de la pr�iode � prendre en consid�ration
     * @param periodeA
     *            fin de la p�riode � prendre en consid�ration
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiements pour les directs et indirects
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiements pour les tiers b�n�ficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirectTiersBeneficaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiements pour les directs et tiers b�nl�ficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiement indirect
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiements pour les Indirects et tiers b�n�ficaires
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirectTierBeneficaires(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de rercherche uniquement pour les paiements pour les tiers b�n�ficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit �tre effectu�es
     * @return DossierDeclarationVersementComplexSearchModel les d�tails de prestations vers�es
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

}
