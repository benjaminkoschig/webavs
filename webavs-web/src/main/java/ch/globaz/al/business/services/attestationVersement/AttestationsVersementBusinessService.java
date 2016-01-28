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
 * @author pta Service définissant les méthodes de recherce pour less attestations de versements
 * 
 */
public interface AttestationsVersementBusinessService extends JadeApplicationService {
    /**
     * Méthode qui initialise lance la recherche en fonction du type de prestation
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @param periodeDe
     * @param periodeA
     * @param typePrestation
     *            type de prestation à rechercher
     * @return ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> returnListPaiement(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de recherche uniquement pour les paiements directs
     * 
     * @param dossierAttestation
     * @param periodeDe
     *            début de la préiode à prendre en considération
     * @param periodeA
     *            fin de la période à prendre en considération
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiements pour les directs et indirects
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiements pour les tiers bénéficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirectTiersBeneficaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiements pour les directs et tiers bénléficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiement indirect
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiements pour les Indirects et tiers bénéficaires
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirectTierBeneficaires(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de rercherche uniquement pour les paiements pour les tiers bénéficiaire
     * 
     * @param dossierAttestation
     *            Dossiers surr lesquels la recherche doit être effectuées
     * @return DossierDeclarationVersementComplexSearchModel les détails de prestations versées
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

}
