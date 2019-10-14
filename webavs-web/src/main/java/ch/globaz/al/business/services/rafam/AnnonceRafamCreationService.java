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
 * Service de cr�ation d'annonces RAFAM
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamCreationService extends JadeApplicationService {
    /**
     * Cr�e une annonce d'annulation pour l'annonce pass�e en param�tre. L'annonce est directement enregistr�e en
     * persistance
     * 
     * @param annonce
     *            l'annonce � annul�e
     * @return l'annonce cr��e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#initAnnonce68c(AnnonceRafamModel)
     */
    public AnnonceRafamModel create68cForAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Appelle la m�thode de cr�ation d'annonces pour les dossier et/ou droits li�s au tiers pass� en param�tre.
     * 
     * Elle recherche les �ventuels droit et dossier (allocataire) li�s au tiers et appelle la m�thode de cr�ation
     * d'annonce pour chacun d'eux.
     * 
     * @param idTiers
     *            id du tiers pour lequel appeler la m�thode cr�ation d'annonces
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit)
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier)
     */
    public void creerAnnonceModificationsTiers(String idTiers) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Appelle la m�thode de cr�ation d'annonces pour les dossiers li�s � l'affili� pass� en param�tre.
     * 
     * Elle recherche les �ventuels dossier li�s � l'affili� et appelle la m�thode de cr�ation
     * d'annonce pour chacun d'eux.
     * 
     * @param numeroAffilie
     *            num�ro de l'affili� pour lequel les dossiers doivent �tre trait�s
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit)
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier)
     */
    public void creerAnnonceModificationAffilie(String numeroAffilie) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Appelle la m�thode de cr�ation d'annonces pour le dossier pass� en param�tre.
     * 
     * Elle recherche les droits li�s au dossier et appelle la m�thode de cr�ation d'annonce pour chacun d'eux. (�tat �
     * transmettre)
     * 
     * @param evDecl
     *            Ev�nement ayant d�clench� l'appel de la m�thode
     * @param dossier
     *            le dossier pour lequel les annonces doivent �tre cr��es
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier,
     *      DroitComplexModel droit)
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge le dossier auquel est li� le droit pass� en param�tre et appelle la m�thode de cr�ation d'annonces (�tat �
     * transmettre)
     * 
     * @param evDecl
     *            Ev�nement ayant d�clanch� l'appel de la m�thode
     * 
     * @param droit
     *            Droit pour lequel cr�er les annonces
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see AnnonceRafamCreationService#creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier,
     *      DroitComplexModel droit)
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Cr�e les annonces pour le droit pass� en param�tre
     * 
     * @param evDecl
     *            Ev�nement ayant d�clanch� l'appel de la m�thode
     * @param etat
     *            Etat dans lequel seront les annonces cr��es
     * @param dossier
     *            Dossier auquel est li� le droit
     * @param droit
     *            Droit pour lequel cr�er les annonces
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
            DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge le dossier auquel est li� le droit pass� en param�tre et appelle la m�thode de cr�ation d'annonces
     * 
     * @param evDecl
     *            Ev�nement ayant d�clanch� l'appel de la m�thode
     * @param etat
     *            Etat dans lequel seront les annonces cr��es
     * @param droit
     *            Droit pour lequel cr�er les annonces
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Suspend l'annonce pass�e en param�tre
     * 
     * @param annonceRafamModel
     *            annonce l'annonce � suspendre
     * @return l'annonce mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel suspendreAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Remplace les annonces en �tat "� transmettre" par celles en �tat "enregistr�e"
     * 
     * @param idDroit
     *            - droit pour lequel il faut appliquer le remplacement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public void transmettreAnnoncesTemporaires(String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Valide l'annonce pass�e en param�tre
     * 
     * @param annonceRafamModel
     *            annonce l'annonce � suspendre
     * @return l'annonce mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
