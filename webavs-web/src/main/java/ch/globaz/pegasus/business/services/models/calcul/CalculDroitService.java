package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Collection;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivantSearch;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

public interface CalculDroitService extends JadeApplicationService {

    /**
     * Calcule les PC accord�es pour un droit donn�. La derni�re version du droit est syst�matiquement utilis�e. Si
     * d'anciens r�sultats de PC accord�es pour la m�me version de droit existent, il faut d'abords les effacer en
     * appelant le service reinitialiseDroit avant de calculer le droit. Les PC accord�es g�n�r�s sont sauvegard�s en
     * base de donn�es.
     * 
     * @param droit
     *            Le droit � calculer
     * @return Le droit calcul� (inchang� mais spy mis � jour)
     * @throws CalculException
     *             en cas d'erreur li� au calcul, business ou technique
     * @throws JadePersistenceException
     *             en cas d'erreur de persistance
     * @throws JadeApplicationException
     *             en cas d'autres erreurs li�s au framework
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Droit calculDroit(Droit droit) throws CalculException, JadePersistenceException, JadeApplicationException,
            SecurityException, NoSuchMethodException;

    public Droit calculDroit(Droit droit, boolean retroactif, List<String> dfForVersion) throws CalculException,
            JadePersistenceException, JadeApplicationException, SecurityException, NoSuchMethodException;

    public String[] calculDroitPlageCalcul(Droit droit, boolean retroactif) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, CalculException, JadePersistenceException, DemandeException;

    public abstract IPeriodePCAccordee calculDroitPourComparaison(Droit droit, Collection<String> listeIdPersonnes,
            DonneesHorsDroitsProvider containerGlobal, boolean persistCalcul, boolean retroactif)
            throws CalculException, JadePersistenceException, JadeApplicationException;

    /**
     * Lance un calcul standard, mais ne persisite pas les r�sultats et retourne uniquement le blob. Il possible de
     * forcer la date de d�but du calcule
     * 
     * @param droit
     * @param listeIdPersonnes
     * @param containerGlobal
     * @return
     * @throws CalculException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public IPeriodePCAccordee calculWithoutPersist(Droit droit, Collection<String> listeIdPersonnes,
            DonneesHorsDroitsProvider containerGlobal, String dateDebutPeriodeForce) throws CalculException,
            JadePersistenceException, JadeApplicationException;

    /**
     * Permet de g�n�rer de copie de pca depuis une version de droit. Ceci est surtout utilis quand le programme n'a pas
     * g�n�rer de copie.
     * 
     * @param idVersionDroit
     * @return
     * @throws PCAccordeeException
     * @throws CalculException
     * @throws PropertiesException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws JadeApplicationException
     */
    public List<CalculPcaReplace> genrateIfNeededCopie(String idVersionDroit) throws PCAccordeeException,
            CalculException, PropertiesException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException, JadeApplicationException;

    /**
     * Initialise, ou r�initialise, un droit dans l'�tat 'Au calcul' et efface les �ventuels anciens r�sultats.<br/>
     * <b style="color:red">!! IMPORTANT : la session est committ�e durant l'execution de cette m�thode !!</b>
     * 
     * @param droit
     *            Droit � r�initaliser
     * @throws Exception
     *             en cas d'erreur
     */
    public void reinitialiseDroit(Droit droit) throws Exception;

    public CalculMoisSuivantSearch searchDonneesFinancieresForVersionDroit(Droit droit) throws CalculException,
            JadePersistenceException;

    public List<CalculPcaReplace> genrateIfNeededCopieWithOutPersist(String idVersionDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public List<CalculPcaReplace> genrateIfNeededDeleteWithOutPersist(String idVersionDroit)
            throws JadePersistenceException, JadeApplicationException;

}
