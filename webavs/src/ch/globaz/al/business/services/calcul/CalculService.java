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
     * Calcul l'�ge pour le calcul d'une prestation.
     * 
     * @param dateNaissance
     *            Date de naissance de l'enfant
     * @param dateCalcul
     *            Date pour laquelle le calcul est effectu�
     * @return age de l'enfant
     * @throws JadeApplicationException
     *             Exception lev�e si l'une des dates n'est pas valide
     */
    public int getAgeForCalcul(String dateNaissance, String dateCalcul) throws JadeApplicationException;

    /**
     * Execute le calcul des droits pour un dossier.
     * 
     * @param dossier
     *            Dossier pour lequel le calcul doit �tre effectu�
     * @param dateCalcul
     *            date pour laquelle le calcul doit �tre effectu�
     * @param assuranceInfo
     *            Informations sur l'affili�
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au r�sultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public ArrayList<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul,
            AssuranceInfo assuranceInfo) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le code syst�me du canton correspondant au tarif pass� en param�tre
     * 
     * @param csTarif
     * @return
     * @throws JadeApplicationException
     */
    public String getCantonForTarif(String csTarif) throws JadeApplicationException;

    /**
     * Retourne le code syst�me du tarif correspondant au canton pass� en param�tre
     * 
     * @param canton
     *            code syst�me d'un canton
     * @return code syst�me du tarif correspondant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @see ch.globaz.al.business.constantes.ALCSCantons
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    public String getTarifForCanton(String canton) throws JadeApplicationException;

    /**
     * V�rifie si une date est sous le r�gime LAFam (date > 31.12.2008)
     * 
     * @param date
     *            Date � v�rifier
     * @return true si LAFam, false sinon
     * @throws JadeApplicationException
     *             Exception lev�e si <code>date</code>
     */
    public boolean isDateLAFam(String date) throws JadeApplicationException;

}
