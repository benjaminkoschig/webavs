package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.ech.xmlns.ech_0104_69._3.NoticeType;
import ch.ech.xmlns.ech_0104_69._3.RegisterStatusRecordType;
import ch.ech.xmlns.ech_0104_69._3.UPISynchronizationRecordType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

/**
 * Service fournissant des méthodes permettant l'initialisation de différents types d'annonces RAFam
 * 
 * @author jts
 * 
 */
public interface InitAnnoncesRafamService extends JadeApplicationService {

    /**
     * Identifie la base légale correspondant aux informations contenues dans le dossier et le droit passés en
     * paramètre.
     * 
     * @param dossier
     *            dossier pour lequel identifier la base légale
     * @param droit
     *            droit pour lequel identifier la base légale
     * @return la base légale identifiée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RafamLegalBasis getBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Identifie le canton correspondant aux informations contenues dans le dossier et le droit passés en paramètre.
     * 
     * @param dossier
     *            dossier pour lequel identifier le canton
     * @param droit
     *            droit pour lequel identifier le canton
     * 
     * @return le canton identifié
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getCantonBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Génère une référence interne (InternalOfficeReference) pour le dossier et le droit passés en paramètre. La
     * référence est composée d'un préfixe, du numéro de dossier et du numéro de droit
     * 
     * @param dossier
     *            dossier pour lequel générer la référence interne
     * @param droit
     *            droit pour lequel générer la référence interne
     * @return la référence interne
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getInternalOfficeReference(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException;

    /**
     * Initialise un modèle d'annonce 68a
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * 
     * 
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68a avec un état définissable par paramètre
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param etat
     *            etat de l'annonce - ne peut être que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * 
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68b avec un état définissable paramètrable
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param etat
     *            etat de l'annonce - ne peut être que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * 
     * @param lastAnnonce
     *            Dernière annonce pour ce droit/genre d'allocation. Le record number sera récupéré de cette annonce
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat, RafamEvDeclencheur evDeclencheur,
            AnnonceRafamModel lastAnnonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68b
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param lastAnnonce
     *            Dernière annonce pour ce droit/genre d'allocation. Le record number sera récupéré de cette annonce
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEvDeclencheur evDeclencheur, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68c
     * 
     * @param annonce
     *            l'annonce à annuler
     * 
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68c
     * 
     * @param annonce
     *            l'annonce à annuler
     * @param etat
     *            etat de l'annonce - ne peut être que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel annonce, RafamEtatAnnonce etat)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 69b (annonce UPI)
     * 
     * @param message
     *            le message provenant de la centrale et contenant les informations à enregistrer
     * 
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69b(UPISynchronizationRecordType message) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'annonce délégué 69d (Etat registre)
     * 
     * @param message
     *            le message provenant de la centrale et contenant les informations à enregistrer
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69cDelegue(RegisterStatusRecordType message, boolean initial)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 69d (Notice)
     * 
     * @param notice
     *            le message provenant de la centrale et contenant les informations à enregistrer
     * @return l'annonce initialisée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69d(NoticeType notice) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68a provenant d'un employeur délégué
     * 
     * @param beneficiary
     *            - répresente un noeud bénéficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - répresente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - répresente un noeud allowance du fichier employeur * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68a(BeneficiaryType beneficiary, ChildType child, AllowanceType allowance)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68b provenant d'un employeur délégué
     * 
     * @param beneficiary
     *            - répresente un noeud bénéficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - répresente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - répresente un noeud allowance du fichier employeur
     * @param lastAnnonce
     *            Dernière annonce pour ce droit/genre d'allocation. Le record number sera récupéré de cette annonce
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68b(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel lastAnnonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'annonce 68c provenant d'un employeur délégué
     * 
     * @param beneficiary
     *            - répresente un noeud bénéficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - répresente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - répresente un noeud allowance du fichier employeur * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68c(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;
}
