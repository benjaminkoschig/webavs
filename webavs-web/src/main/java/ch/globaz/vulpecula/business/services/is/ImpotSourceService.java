package ch.globaz.vulpecula.business.services.is;

import ch.globaz.common.properties.PropertiesException;
import globaz.vulpecula.business.exception.VulpeculaException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.is.DetailPrestationAF;
import ch.globaz.al.exception.TauxImpositionNotFoundException;

public interface ImpotSourceService {
    /**
     * Retourne les prestations AF en paiements directs pour les allocataires IS.
     * Si l'on consid?re les prestations :
     * 
     * <table border="1">
     * <tr>
     * <td>Periode de d?but</td>
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
     * Celles-ci seront regroup?es de cette mani?re.
     * <table border="1">
     * <tr>
     * <td>Date d?but</td>
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
     * @param idAllocataire String repr?sentant l'id de l'allocataire. Peut ?tre null si l'on souhaite tous les
     *            allocataires
     * @param dateDebut Date de d?but ? laquelle prendre les prestations.
     * @param dateFin Date de fin ? laquelle prendre les prestations
     * @return Un ensemble de prestations group?es contenant non seulement le montant total des prestations durant la
     *         p?riode mais ?galement le montant de l'imp?t ? la source. Toutes ces informations proviennent des
     *         rubriques en comptabilit?.
     * @throws TauxImpositionNotFoundException
     */
    List<PrestationGroupee> getPrestationsForAllocIS(String idAllocataire, Date dateDebut, Date dateFin)
            throws TauxImpositionNotFoundException, PropertiesException;

    /**
     * Retourne les prestations AF pour les allocataires qui ne sont pas impos?s ? la source, soit des prestations en
     * paiements directs ET indirects.
     * 
     * @param annee Ann?e ? laquelle s?lectionner les prestations
     * @return Liste de prestations
     */
    List<PrestationGroupee> getPrestationsForAllocNonIS(Annee annee) throws PropertiesException;

    /**
     * Retourne les prestations AF li?s ? un processus AF.
     * 
     * @param idProcessus Id du processus aF
     * @return Liste de prestations
     */
    List<EntetePrestationComplexModel> getEntetesPrestationsIS(String idProcessus) throws PropertiesException;

    /**
     * Retourne toutes les prestations relatives ? un processus AF.
     * 
     * @param idProcessus String repr?sentant l'id d'un processus AF
     * @return Liste de prestations
     */
    List<EntetePrestationComplexModel> getEntetesPrestations(String idProcessus);

    /**
     * 
     * @param dateDebut
     * @param dateFin
     * @return Liste de prestations pr?vues expr?ssement pour la liste des AF vers?es
     */
    Map<String, Collection<DetailPrestationAF>> getPrestationsForListAFVersees(String dateDebut, String dateFin)
            throws VulpeculaException;

    /**
     * @see #getPrestationsForAllocIS(String, Date, Date)
     * @param dateDebut date de d?but
     * @param dateFin date de fin
     * @return Liste de prestations
     * @throws TauxImpositionNotFoundException
     */
    Map<String, List<PrestationGroupee>> getPrestationsForAllocIS(String dateDebut, String dateFin)
            throws TauxImpositionNotFoundException, PropertiesException;

    /**
     * Retourne les prestations AF pour les allocataires qui ne sont pas impos?s ? la source, soit des prestations en
     * paiements directs ET indirects group?s par caisseAF.
     * 
     * @return Liste de prestations
     */
    Map<String, PrestationGroupee> getPrestationsForAllocISGroupByCaisseAF(String dateDebut, String dateFin, String canton) throws PropertiesException;

    Map<String, BigDecimal> getMontantISCaisseAFComptaAux(List<String> caisses, String dateDebut, String dateFin) throws Exception;

}
