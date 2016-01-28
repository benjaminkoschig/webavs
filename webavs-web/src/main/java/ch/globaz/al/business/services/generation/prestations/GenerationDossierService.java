package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;

/**
 * Service de génération de prestations permettant de générer les prestations d'un dossier
 * 
 * @author jts
 * 
 */
public interface GenerationDossierService extends JadeApplicationService {

    /**
     * Génère les prestations pour le dossier <code>dossier</code> pour la période <code>debutPeriode</code> -
     * <code>finPeriode</code>
     * 
     * @param dossier
     *            Dossier pour lequel les prestations doivent être générée
     * @param idDroit
     *            Permet de générer la prestation pour un seul droit du dossier. Si ce paramètre vaut 0 ou est
     *            <code>null</code>, tous les droit du dossier seront générés
     * @param debutPeriode
     *            Début de la période à générer sous la forme MM.AAAA
     * @param finPeriode
     *            Fin de la période à générer sous la forme MM.AAAA. Doit être supérieur ou égal à
     *            <code>debutPeriode</code>
     * @param debutRecap
     *            Début de la période de la récap sous la forme MM.AAAA
     * @param finRecap
     *            Fin de la période de la récap sous la forme MM.AAAA. Doit être supérieur ou égal à
     *            <code>debutRecap</code>
     * @param montant
     *            Montant forcé. Si aucun montant ne doit être forcé, passer la valeur 0
     * @param bonification
     *            indique s'il s'agit d'une demande de restitution
     * @param nbUnites
     *            Nombre d'unité. Utilisé pour spécifier le nombre d'unité (mois, jour, heure) devant être généré
     * @param numFacture
     *            Numéro de facture
     * @param numProcessus
     *            Numéro du processus métier de paiement lié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void generationDossier(DossierComplexModel dossier, String idDroit, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, String montant, Bonification bonification, String nbUnites,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException;

    /**
     * Génère les prestations définitives d'un dossier ADI
     * 
     * @param dossier
     *            Dossier pour lequel les prestations doivent être générée
     * @param debutPeriode
     *            Début de la période à générer sous la forme MM.AAAA
     * @param finPeriode
     *            Fin de la période à générer sous la forme MM.AAAA. Doit être supérieur ou égal à
     *            <code>debutPeriode</code>
     * @param debutRecap
     *            Début de la période de la récap sous la forme MM.AAAA
     * @param finRecap
     *            Fin de la période de la récap sous la forme MM.AAAA. Doit être supérieur ou égal à
     *            <code>debutRecap</code>
     * @param details
     *            Liste des détails de prestation à générer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void generationDossierADI(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, HashMap<String, ArrayList<CalculBusinessModel>> details,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException;
}
