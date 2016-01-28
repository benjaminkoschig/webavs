package ch.globaz.vulpecula.business.services.is;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;

public interface ImpotSourceService {
    /**
     * Retourne les prestations AF en paiements directs pour les allocataires IS.
     * Si l'on considère les prestations :
     * 
     * <table border="1">
     * <tr>
     * <td>Periode de début</td>
     * <td>Periode de fin</td>
     * <td>Montant</td>
     * </tr>
     * <tr>
     * <td>01.2014</td>
     * <td>01.2014</td>
     * <td>200.-</td>
     * </tr>
     * <tr>
     * <td>02.2014</td>
     * <td>02.2014</td>
     * <td>200.-</td>
     * </tr>
     * <tr>
     * <td>04.2014</td>
     * <td>04.2014</td>
     * <td>200.-</td>
     * </tr>
     * </table>
     * Celles-ci seront regroupées de cette manière.
     * <table border="1">
     * <tr>
     * <td>Date début</td>
     * <td>Date fin</td>
     * <td>Montant</td>
     * </tr>
     * <tr>
     * <td>01.2014</td>
     * <td>02.2014</td>
     * <td>400.-</td>
     * </tr>
     * <tr>
     * <td>04.2014</td>
     * <td>04.2014</td>
     * <td>200.-</td>
     * </tr>
     * </table>
     * 
     * @param idAllocataire String représentant l'id de l'allocataire. Peut être null si l'on souhaite tous les
     *            allocataires
     * @param dateDebut Date de début à laquelle prendre les prestations.
     * @param dateFin Date de fin à laquelle prendre les prestations
     * @return Un ensemble de prestations groupées contenant non seulement le montant total des prestations durant la
     *         période mais également le montant de l'impôt à la source. Toutes ces informations proviennent des
     *         rubriques en comptabilité.
     * @throws TauxImpositionNotFoundException
     */
    List<PrestationGroupee> getPrestationsForAllocIS(String idAllocataire, Date dateDebut, Date dateFin)
            throws TauxImpositionNotFoundException;

    /**
     * Retourne les prestations AF pour les allocataires qui ne sont pas imposés à la source, soit des prestations en
     * paiements directs ET indirects.
     * 
     * @param annee Année à laquelle sélectionner les prestations
     * @return Liste de prestations
     */
    List<PrestationGroupee> getPrestationsForAllocNonIS(Annee annee);

    /**
     * Retourne les prestations AF liés à un processus AF.
     * 
     * @param idProcessus Id du processus aF
     * @return Liste de prestations
     */
    List<EntetePrestationComplexModel> getEntetesPrestationsIS(String idProcessus);

    /**
     * Retourne toutes les prestations relatives à un processus AF.
     * 
     * @param idProcessus String représentant l'id d'un processus AF
     * @return Liste de prestations
     */
    List<EntetePrestationComplexModel> getEntetesPrestations(String idProcessus);

    /**
     * @see #getPrestationsForAllocIS(String, Date, Date)
     * @param canton Canton de résidence de l'allocataire
     * @param caisseAF Caisse AF
     * @param annee Année correspondant à la date de comptabilisation
     * @return Liste de prestations
     * @throws TauxImpositionNotFoundException
     */
    Map<String, Collection<PrestationGroupee>> getPrestationsForAllocIS(String canton, String caisseAF, Annee annee)
            throws TauxImpositionNotFoundException;

    /**
     * Retourne les prestations AF pour les allocataires qui ne sont pas imposés à la source, soit des prestations en
     * paiements directs ET indirects groupés par caisseAF.
     * 
     * @param annee Année à laquelle sélectionner les prestations (BASE SUR LA DATE DE COMPTABILISATION)
     * @return Liste de prestations
     */
    Map<String, PrestationGroupee> getPrestationsForAllocISGroupByCaisseAF(Annee annee);
}
