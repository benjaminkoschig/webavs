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
 * Service fournissant les méthodes permettant le calcul des montants effectif des prestations, la répartition des
 * prestations et les totaux
 * 
 * @author jts
 * 
 */
public interface CalculMontantsService extends JadeApplicationService {

    /**
     * Ajoute un droit actif à la liste des droits calculés
     * 
     * @param dossier
     *            Dossier auquel est lié le <code>droit</code>
     * @param droit
     *            Droit à calculer. Doit être un droit lié au <code>dossier</code>
     * @param tarifs
     *            Résultat d'une recherche de tarifs. Le premier de la liste sera utilisé pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Catégorie de tarif correspondant au mode de calcul utilisé par la caisse. Il peut aussi être utilisé
     *            pour indiquer un tarif forcé
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param date
     *            Date pour laquelle le calcul a été effectué. Cette date est utilisée pour vérifier l'exportabilité de
     *            la prestation
     * @return <code>droitsCalcules</code> passée en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit actif à la liste des droits calculés
     * 
     * @param dossier
     *            Dossier auquel est lié le <code>droit</code>
     * @param droit
     *            Droit à calculer. Doit être un droit lié au <code>dossier</code>
     * @param tarifs
     *            Résultat d'une recherche de tarifs. Le premier de la liste sera utilisé pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Catégorie de tarif correspondant au mode de calcul utilisé par la caisse. Il peut aussi être utilisé
     *            pour indiquer un tarif forcé
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param date
     *            Date pour laquelle le calcul a été effectué. Cette date est utilisée pour vérifier l'exportabilité de
     *            la prestation
     * @param hideDroit
     *            Indique si le droit doit être masqué à l'affichage du calcul, sur la décision, ...
     * 
     * @return <code>droitsCalcules</code> passée en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String date, boolean hideDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit inactif (tous les montant sont défini à 0) à la liste des droits calculé
     * 
     * @param droit
     *            Droit à calculer. Doit être un droit lié au <code>dossier</code>
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param date
     *            Date pour laquelle le calcul a été effectué. Cette date est utilisée pour vérifier l'exportabilité de
     *            la prestation
     * @return <code>droitsCalcules</code> passée en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     List<CalculBusinessModel> addDroitCalculeInactif(DroitComplexModel droit, List<CalculBusinessModel> droitsCalcules, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un droit de naissance/accueil à la liste des droits calculés
     * 
     * @param dossier
     *            Dossier auquel est lié le <code>droit</code>
     * @param droit
     *            Droit à calculer. Doit être un droit lié au <code>dossier</code>
     * @param tarifs
     *            Résultat d'une recherche de tarifs. Le premier de la liste sera utilisé pour le calcul du montant
     *            effectif.
     * @param tarifForce
     *            Catégorie de tarif correspondant au mode de calcul utilisé par la caisse. Il peut aussi être utilisé
     *            pour indiquer un tarif forcé
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param rang
     *            Rang de l'enfant au moment du calcul
     * @param type
     *            Type de prestation {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_NAIS} ou
     *            {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_ACCE}
     * @param date
     *            Date pour laquelle le calcul a été effectué. Cette date est utilisée pour vérifier l'exportabilité de
     *            la prestation
     * @return <code>droitsCalcules</code> passé en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> addDroitCalculeNaissance(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String type, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet l'ajout de droits calculés particulier tel que la famille nombreuse, le ménage agricole jurassien, ...
     * 
     * @param montant
     *            Le montant de base
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param droit
     *            Droit lié au calcul
     * @param typeDroit
     *            Type de droit provenant du groupe {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE}
     * @param categorieTarif
     *            Catégorie de tarif provenant du groupe de codes système
     *            {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE}
     * @param tarifCaisse
     *            Tarif appliqué par la caisse
     * @param tarifCanton
     *            Tarif appliqué par le canton
     * @return <code>droitsCalcules</code> passé en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton) throws JadeApplicationException;

    /**
     * Permet l'ajout de droits calculés particulier tel que la famille nombreuse, le ménage agricole jurassien, ...
     * 
     * @param montant
     *            Le montant de base
     * @param droitsCalcules
     *            Liste à laquelle le droit calculé sera ajouté
     * @param droit
     *            Droit lié au calcul
     * @param typeDroit
     *            Type de droit provenant du groupe {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE}
     * @param categorieTarif
     *            Catégorie de tarif provenant du groupe de codes système
     *            {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE}
     * @param tarifCaisse
     *            Tarif appliqué par la caisse
     * @param tarifCanton
     *            Tarif appliqué par le canton
     * @param hideDroit
     *            Indique si le droit doit être masqué à l'affichage du calcul, sur la décision, ...
     * 
     * @return <code>droitsCalcules</code> passé en paramètre avec le droit ajouté
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
     List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton, boolean hideDroit)
            throws JadeApplicationException;

    /**
     * Calcul le montant total des droits.
     * 
     * Retourne une <code>HashMap</code> contenant les résultats ainsi que la liste des droits calculés accessibles à
     * l'aide des clés :
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
     *            Dossier pour lequel les droits sont calculé
     * @param droitsCalcules
     *            Liste des droits calculé. Il s'agit d'une <code>ArrayList</code> contenant des instances de
     *            {@link ch.globaz.al.business.models.droit.CalculBusinessModel}
     * @param avecNAIS
     *            Indique si les prestation de naissance/accueil doivent être prises en compte pour le calcul du total
     * @param unite
     *            Unité pour le calcul {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param nbUnites
     *            Nombre d'unités
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @return HashMap contenant les montants totaux
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.CalculBusinessModel
     */
    @SuppressWarnings("unchecked")
    Map calculerTotalMontant(DossierComplexModel dossier, List<CalculBusinessModel> droitsCalcules,
                             String unite, String nbUnites, boolean avecNAIS, String date) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Vérifie si un montant a été forcé à pour un droit calculé
     * 
     * @param calcDroit
     *            Résultat d'un calcul pour un droit
     * @return <code>true</code> si le montant a été forcé, <code>false</code> dans le cas contraire forcé à un autre
     *         montant
     */
    boolean isMontantForce(CalculBusinessModel calcDroit);

    /**
     * Vérifie si un montant a été forcé à 0 pour un droit calculé
     * 
     * @param calcDroit
     *            Résultat d'un calcul pour un droit
     * @return <code>true</code> si le montant a été forcé à 0, <code>false</code> s'il n'a pas été forcé ou s'il a été
     *         forcé à un autre montant
     */
    boolean isMontantForceZero(CalculBusinessModel calcDroit);
}
