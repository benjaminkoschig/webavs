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
 * Service fournissant des m�thodes permettant l'initialisation de diff�rents types d'annonces RAFam
 * 
 * @author jts
 * 
 */
public interface InitAnnoncesRafamService extends JadeApplicationService {

    /**
     * Identifie la base l�gale correspondant aux informations contenues dans le dossier et le droit pass�s en
     * param�tre.
     * 
     * @param dossier
     *            dossier pour lequel identifier la base l�gale
     * @param droit
     *            droit pour lequel identifier la base l�gale
     * @return la base l�gale identifi�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RafamLegalBasis getBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Identifie le canton correspondant aux informations contenues dans le dossier et le droit pass�s en param�tre.
     * 
     * @param dossier
     *            dossier pour lequel identifier le canton
     * @param droit
     *            droit pour lequel identifier le canton
     * 
     * @return le canton identifi�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getCantonBaseLegale(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * G�n�re une r�f�rence interne (InternalOfficeReference) pour le dossier et le droit pass�s en param�tre. La
     * r�f�rence est compos�e d'un pr�fixe, du num�ro de dossier et du num�ro de droit
     * 
     * @param dossier
     *            dossier pour lequel g�n�rer la r�f�rence interne
     * @param droit
     *            droit pour lequel g�n�rer la r�f�rence interne
     * @return la r�f�rence interne
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getInternalOfficeReference(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException;

    /**
     * Initialise un mod�le d'annonce 68a
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * 
     * 
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68a avec un �tat d�finissable par param�tre
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param etat
     *            etat de l'annonce - ne peut �tre que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * 
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68a(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68b avec un �tat d�finissable param�trable
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param etat
     *            etat de l'annonce - ne peut �tre que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * 
     * @param lastAnnonce
     *            Derni�re annonce pour ce droit/genre d'allocation. Le record number sera r�cup�r� de cette annonce
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEtatAnnonce etat, RafamEvDeclencheur evDeclencheur,
            AnnonceRafamModel lastAnnonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68b
     * 
     * @param dossier
     *            dossier pour lequel initialiser l'annonce
     * @param droit
     *            droit pour lequel initialiser l'annonce
     * @param type
     *            genre d'allocation familiale
     * @param lastAnnonce
     *            Derni�re annonce pour ce droit/genre d'allocation. Le record number sera r�cup�r� de cette annonce
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68b(DossierComplexModel dossier, DroitComplexModel droit,
            RafamFamilyAllowanceType type, RafamEvDeclencheur evDeclencheur, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68c
     * 
     * @param annonce
     *            l'annonce � annuler
     * 
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68c
     * 
     * @param annonce
     *            l'annonce � annuler
     * @param etat
     *            etat de l'annonce - ne peut �tre que RafamEtatAnnonce.A_TRANSMETTRE ou RafamEtatAnnonce.ENREGISTRE
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce68c(AnnonceRafamModel annonce, RafamEtatAnnonce etat)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 69b (annonce UPI)
     * 
     * @param message
     *            le message provenant de la centrale et contenant les informations � enregistrer
     * 
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69b(UPISynchronizationRecordType message) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce d�l�gu� 69d (Etat registre)
     * 
     * @param message
     *            le message provenant de la centrale et contenant les informations � enregistrer
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69cDelegue(RegisterStatusRecordType message, boolean initial)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 69d (Notice)
     * 
     * @param notice
     *            le message provenant de la centrale et contenant les informations � enregistrer
     * @return l'annonce initialis�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamModel initAnnonce69d(NoticeType notice) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68a provenant d'un employeur d�l�gu�
     * 
     * @param beneficiary
     *            - r�presente un noeud b�n�ficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - r�presente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - r�presente un noeud allowance du fichier employeur * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68a(BeneficiaryType beneficiary, ChildType child, AllowanceType allowance)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68b provenant d'un employeur d�l�gu�
     * 
     * @param beneficiary
     *            - r�presente un noeud b�n�ficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - r�presente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - r�presente un noeud allowance du fichier employeur
     * @param lastAnnonce
     *            Derni�re annonce pour ce droit/genre d'allocation. Le record number sera r�cup�r� de cette annonce
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68b(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel lastAnnonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'annonce 68c provenant d'un employeur d�l�gu�
     * 
     * @param beneficiary
     *            - r�presente un noeud b�n�ficiaire du fichier employeur sans les sous-noeuds child / allowance
     * @param child
     *            - r�presente un noeud child du fichier employeur sans le sous-noeud allowance
     * @param allowance
     *            - r�presente un noeud allowance du fichier employeur * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel initAnnonceDelegue68c(BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance, AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;
}
