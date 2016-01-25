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
 * Service de g�n�ration de prestations permettant de g�n�rer les prestations d'un dossier
 * 
 * @author jts
 * 
 */
public interface GenerationDossierService extends JadeApplicationService {

    /**
     * G�n�re les prestations pour le dossier <code>dossier</code> pour la p�riode <code>debutPeriode</code> -
     * <code>finPeriode</code>
     * 
     * @param dossier
     *            Dossier pour lequel les prestations doivent �tre g�n�r�e
     * @param idDroit
     *            Permet de g�n�rer la prestation pour un seul droit du dossier. Si ce param�tre vaut 0 ou est
     *            <code>null</code>, tous les droit du dossier seront g�n�r�s
     * @param debutPeriode
     *            D�but de la p�riode � g�n�rer sous la forme MM.AAAA
     * @param finPeriode
     *            Fin de la p�riode � g�n�rer sous la forme MM.AAAA. Doit �tre sup�rieur ou �gal �
     *            <code>debutPeriode</code>
     * @param debutRecap
     *            D�but de la p�riode de la r�cap sous la forme MM.AAAA
     * @param finRecap
     *            Fin de la p�riode de la r�cap sous la forme MM.AAAA. Doit �tre sup�rieur ou �gal �
     *            <code>debutRecap</code>
     * @param montant
     *            Montant forc�. Si aucun montant ne doit �tre forc�, passer la valeur 0
     * @param bonification
     *            indique s'il s'agit d'une demande de restitution
     * @param nbUnites
     *            Nombre d'unit�. Utilis� pour sp�cifier le nombre d'unit� (mois, jour, heure) devant �tre g�n�r�
     * @param numFacture
     *            Num�ro de facture
     * @param numProcessus
     *            Num�ro du processus m�tier de paiement li�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void generationDossier(DossierComplexModel dossier, String idDroit, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, String montant, Bonification bonification, String nbUnites,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException;

    /**
     * G�n�re les prestations d�finitives d'un dossier ADI
     * 
     * @param dossier
     *            Dossier pour lequel les prestations doivent �tre g�n�r�e
     * @param debutPeriode
     *            D�but de la p�riode � g�n�rer sous la forme MM.AAAA
     * @param finPeriode
     *            Fin de la p�riode � g�n�rer sous la forme MM.AAAA. Doit �tre sup�rieur ou �gal �
     *            <code>debutPeriode</code>
     * @param debutRecap
     *            D�but de la p�riode de la r�cap sous la forme MM.AAAA
     * @param finRecap
     *            Fin de la p�riode de la r�cap sous la forme MM.AAAA. Doit �tre sup�rieur ou �gal �
     *            <code>debutRecap</code>
     * @param details
     *            Liste des d�tails de prestation � g�n�rer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void generationDossierADI(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, HashMap<String, ArrayList<CalculBusinessModel>> details,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException;
}
