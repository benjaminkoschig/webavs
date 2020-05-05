package ch.globaz.al.business.services.calcul;

import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;
import java.util.Map;

/**
 * Service fournissant les m�thodes permettant le calcul des montants effectif des prestations, la r�partition des
 * prestations et les totaux
 * 
 * @author jts
 * 
 */
public interface CalculMontantsService extends JadeApplicationService {

    /**
     * Ajoute un droit actif � la liste des droits calcul�s
     * 
     * @param dossier
     *            Dossier auquel est li� le <code>droit</code>
     * @param droit
     *            Droit � calculer. Doit �tre un droit li� au <code>dossier</code>
     * @param tarifs
     *            R�sultat d'une recherche de tarifs. Le premier de la liste sera utilis� pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Cat�gorie de tarif correspondant au mode de calcul utilis� par la caisse. Il peut aussi �tre utilis�
     *            pour indiquer un tarif forc�
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�. Cette date est utilis�e pour v�rifier l'exportabilit� de
     *            la prestation
     * @return <code>droitsCalcules</code> pass�e en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit actif � la liste des droits calcul�s
     * 
     * @param dossier
     *            Dossier auquel est li� le <code>droit</code>
     * @param droit
     *            Droit � calculer. Doit �tre un droit li� au <code>dossier</code>
     * @param tarifs
     *            R�sultat d'une recherche de tarifs. Le premier de la liste sera utilis� pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Cat�gorie de tarif correspondant au mode de calcul utilis� par la caisse. Il peut aussi �tre utilis�
     *            pour indiquer un tarif forc�
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�. Cette date est utilis�e pour v�rifier l'exportabilit� de
     *            la prestation
     * @param hideDroit
     *            Indique si le droit doit �tre masqu� � l'affichage du calcul, sur la d�cision, ...
     * 
     * @return <code>droitsCalcules</code> pass�e en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String date, boolean hideDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit inactif (tous les montant sont d�fini � 0) � la liste des droits calcul�
     * 
     * @param droit
     *            Droit � calculer. Doit �tre un droit li� au <code>dossier</code>
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�. Cette date est utilis�e pour v�rifier l'exportabilit� de
     *            la prestation
     * @return <code>droitsCalcules</code> pass�e en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     List<CalculBusinessModel> addDroitCalculeInactif(DroitComplexModel droit, List<CalculBusinessModel> droitsCalcules, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit de naissance/accueil � la liste des droits calcul�s
     * 
     * @param dossier
     *            Dossier auquel est li� le <code>droit</code>
     * @param droit
     *            Droit � calculer. Doit �tre un droit li� au <code>dossier</code>
     * @param tarifs
     *            R�sultat d'une recherche de tarifs. Le premier de la liste sera utilis� pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Cat�gorie de tarif correspondant au mode de calcul utilis� par la caisse. Il peut aussi �tre utilis�
     *            pour indiquer un tarif forc�
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param type
     *            Type de prestation {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_NAIS} ou
     *            {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_ACCE}
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�. Cette date est utilis�e pour v�rifier l'exportabilit� de
     *            la prestation
     * @return <code>droitsCalcules</code> pass� en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeNaissance(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String type, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet l'ajout de droits calcul�s particulier tel que la famille nombreuse, le m�nage agricole jurassien, ...
     * 
     * @param montant
     *            Le montant de base
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param droit
     *            Droit li� au calcul
     * @param typeDroit
     *            Type de droit provenant du groupe {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE}
     * @param categorieTarif
     *            Cat�gorie de tarif provenant du groupe de codes syst�me
     *            {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE}
     * @param tarifCaisse
     *            Tarif appliqu� par la caisse
     * @param tarifCanton
     *            Tarif appliqu� par le canton
     * @return <code>droitsCalcules</code> pass� en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton) throws JadeApplicationException;

    /**
     * Permet l'ajout de droits calcul�s particulier tel que la famille nombreuse, le m�nage agricole jurassien, ...
     * 
     * @param montant
     *            Le montant de base
     * @param droitsCalcules
     *            Liste � laquelle le droit calcul� sera ajout�
     * @param droit
     *            Droit li� au calcul
     * @param typeDroit
     *            Type de droit provenant du groupe {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE}
     * @param categorieTarif
     *            Cat�gorie de tarif provenant du groupe de codes syst�me
     *            {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE}
     * @param tarifCaisse
     *            Tarif appliqu� par la caisse
     * @param tarifCanton
     *            Tarif appliqu� par le canton
     * @param hideDroit
     *            Indique si le droit doit �tre masqu� � l'affichage du calcul, sur la d�cision, ...
     * 
     * @return <code>droitsCalcules</code> pass� en param�tre avec le droit ajout�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
     List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton, boolean hideDroit)
            throws JadeApplicationException;

    /**
     * Calcul le montant total des droits.
     * 
     * Retourne une <code>HashMap</code> contenant les r�sultats ainsi que la liste des droits calcul�s accessibles �
     * l'aide des cl�s :
     * <ul>
     * <li>{@link ch.globaz.al.business.constantes.ALConstCalcul#TOTAL_EFFECTIF}</li>
     * <li>
     * {@link ch.globaz.al.business.constantes.ALConstCalcul#TOTAL_UNITE_EFFECTIF}</li>
     * <li>
     * {@link ch.globaz.al.business.constantes.ALConstCalcul#DROITS_CALCULES}</li>
     * </ul>
     * 
     * 
     * @param dossier
     *            Dossier pour lequel les droits sont calcul�
     * @param droitsCalcules
     *            Liste des droits calcul�. Il s'agit d'une <code>ArrayList</code> contenant des instances de
     *            {@link ch.globaz.al.business.models.droit.CalculBusinessModel}
     * @param avecNAIS
     *            Indique si les prestation de naissance/accueil doivent �tre prises en compte pour le calcul du total
     * @param unite
     *            Unit� pour le calcul {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param nbUnites
     *            Nombre d'unit�s
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�
     * @return HashMap contenant les montants totaux
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.CalculBusinessModel
     */
    @SuppressWarnings("unchecked")
    Map calculerTotalMontant(DossierComplexModel dossier, List<CalculBusinessModel> droitsCalcules,
                             String unite, String nbUnites, boolean avecNAIS, String date) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * V�rifie si un montant a �t� forc� � pour un droit calcul�
     * 
     * @param calcDroit
     *            R�sultat d'un calcul pour un droit
     * @return <code>true</code> si le montant a �t� forc�, <code>false</code> dans le cas contraire forc� � un autre
     *         montant
     */
    boolean isMontantForce(CalculBusinessModel calcDroit);

    /**
     * V�rifie si un montant a �t� forc� � 0 pour un droit calcul�
     * 
     * @param calcDroit
     *            R�sultat d'un calcul pour un droit
     * @return <code>true</code> si le montant a �t� forc� � 0, <code>false</code> s'il n'a pas �t� forc� ou s'il a �t�
     *         forc� � un autre montant
     */
    boolean isMontantForceZero(CalculBusinessModel calcDroit);
}
