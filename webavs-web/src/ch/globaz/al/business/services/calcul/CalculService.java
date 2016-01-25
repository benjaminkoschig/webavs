package ch.globaz.al.business.services.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.naos.business.data.AssuranceInfo;

/**
 * Service permettant le calcul des droits AF d'un dossier
 * 
 * @author jts
 */
public interface CalculService extends JadeApplicationService {
    /**
     * Calcul l'âge pour le calcul d'une prestation.
     * 
     * @param dateNaissance
     *            Date de naissance de l'enfant
     * @param dateCalcul
     *            Date pour laquelle le calcul est effectué
     * @return age de l'enfant
     * @throws JadeApplicationException
     *             Exception levée si l'une des dates n'est pas valide
     */
    public int getAgeForCalcul(String dateNaissance, String dateCalcul) throws JadeApplicationException;

    /**
     * Execute le calcul des droits pour un dossier.
     * 
     * @param dossier
     *            Dossier pour lequel le calcul doit être effectué
     * @param dateCalcul
     *            date pour laquelle le calcul doit être effectué
     * @param assuranceInfo
     *            Informations sur l'affilié
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au résultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public ArrayList<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul,
            AssuranceInfo assuranceInfo) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le code système du canton correspondant au tarif passé en paramètre
     * 
     * @param csTarif
     * @return
     * @throws JadeApplicationException
     */
    public String getCantonForTarif(String csTarif) throws JadeApplicationException;

    /**
     * Retourne le code système du tarif correspondant au canton passé en paramètre
     * 
     * @param canton
     *            code système d'un canton
     * @return code système du tarif correspondant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @see ch.globaz.al.business.constantes.ALCSCantons
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    public String getTarifForCanton(String canton) throws JadeApplicationException;

    /**
     * Vérifie si une date est sous le régime LAFam (date > 31.12.2008)
     * 
     * @param date
     *            Date à vérifier
     * @return true si LAFam, false sinon
     * @throws JadeApplicationException
     *             Exception levée si <code>date</code>
     */
    public boolean isDateLAFam(String date) throws JadeApplicationException;

}
