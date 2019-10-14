package ch.globaz.al.business.services.rafam;

import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

import java.util.List;

/**
 * Service de création d'annonces RAFAM
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamCreationService extends JadeApplicationService {
    /**
     * Crée une annonce d'annulation pour l'annonce passée en paramètre. L'annonce est directement enregistrée en
     * persistance
     * 
     * @param annonce
     *            l'annonce à annulée
     * @return l'annonce créée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68c(AnnonceRafamModel)
     */
    public AnnonceRafamModel create68cForAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Appelle la méthode de création d'annonces pour les dossier et/ou droits liés au tiers passé en paramètre.
     * 
     * Elle recherche les éventuels droit et dossier (allocataire) liés au tiers et appelle la méthode de création
     * d'annonce pour chacun d'eux.
     * 
     * @param idTiers
     *            id du tiers pour lequel appeler la méthode création d'annonces
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit)
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier)
     */
    public void creerAnnonceModificationsTiers(String idTiers) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Appelle la méthode de création d'annonces pour les dossiers liés à l'affilié passé en paramètre.
     * 
     * Elle recherche les éventuels dossier liés à l'affilié et appelle la méthode de création
     * d'annonce pour chacun d'eux.
     * 
     * @param numeroAffilie
     *            numéro de l'affilié pour lequel les dossiers doivent être traités
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit)
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier)
     */
    public void creerAnnonceModificationAffilie(String numeroAffilie) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Appelle la méthode de création d'annonces pour le dossier passé en paramètre.
     * 
     * Elle recherche les droits liés au dossier et appelle la méthode de création d'annonce pour chacun d'eux. (état à
     * transmettre)
     * 
     * @param evDecl
     *            Evènement ayant déclenché l'appel de la méthode
     * @param dossier
     *            le dossier pour lequel les annonces doivent être créées
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier,
     *      DroitComplexModel droit)
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge le dossier auquel est lié le droit passé en paramètre et appelle la méthode de création d'annonces (état à
     * transmettre)
     * 
     * @param evDecl
     *            Evènement ayant déclanché l'appel de la méthode
     * 
     * @param droit
     *            Droit pour lequel créer les annonces
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier,
     *      DroitComplexModel droit)
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Crée les annonces pour le droit passé en paramètre
     * 
     * @param evDecl
     *            Evènement ayant déclanché l'appel de la méthode
     * @param etat
     *            Etat dans lequel seront les annonces créées
     * @param dossier
     *            Dossier auquel est lié le droit
     * @param droit
     *            Droit pour lequel créer les annonces
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
            DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge le dossier auquel est lié le droit passé en paramètre et appelle la méthode de création d'annonces
     * 
     * @param evDecl
     *            Evènement ayant déclanché l'appel de la méthode
     * @param etat
     *            Etat dans lequel seront les annonces créées
     * @param droit
     *            Droit pour lequel créer les annonces
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier,
     *      DroitComplexModel droit)
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    public void creerAnnoncesDelegue(String idEmployeur, ChildAllowanceType currentAnnonces) throws Exception;

    public void creerAnnoncesADI(List<AdiEnfantMoisComplexModel> listAdi)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Suspend l'annonce passée en paramètre
     * 
     * @param annonceRafamModel
     *            annonce l'annonce à suspendre
     * @return l'annonce mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel suspendreAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Remplace les annonces en état "à transmettre" par celles en état "enregistrée"
     * 
     * @param idDroit
     *            - droit pour lequel il faut appliquer le remplacement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public void transmettreAnnoncesTemporaires(String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Valide l'annonce passée en paramètre
     * 
     * @param annonceRafamModel
     *            annonce l'annonce à suspendre
     * @return l'annonce mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel validerAnnonce(AnnonceRafamModel annonceRafamModel) throws JadeApplicationException,
            JadePersistenceException;

    public List<RafamFamilyAllowanceType> getTypesAllocation(DossierModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    public List<RafamFamilyAllowanceType> getTypesAllocationForAnnulation(DossierModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    public void creerAnnoncesWithoutDelete(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
                                           DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException ;

    public void creerAnnoncesNaissanceOnly(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;
}
