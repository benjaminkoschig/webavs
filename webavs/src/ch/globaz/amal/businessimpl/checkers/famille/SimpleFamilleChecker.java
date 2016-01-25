/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.famille;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;

/**
 * @author CBU
 * 
 */
public class SimpleFamilleChecker extends AmalAbstractChecker {

    /**
     * @param simpleFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleFamille simpleFamille) throws FamilleException, JadePersistenceException {
        SimpleFamilleChecker.checkMandatory(simpleFamille);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleFamilleChecker.checkIntegrity(simpleFamille);
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForDelete(SimpleFamille simpleFamille) throws FamilleException, JadePersistenceException {
        // CBU 19.05.11 On effectue le controle de la liaison PYXIS uniquement en web.
        // if (!JadeStringUtil.isBlankOrZero(simpleFamille.getIdTier())) {
        // JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.simpleFamille.integrity");
        // }

        try {
            if (SimpleFamilleChecker.hasSubsides(simpleFamille)) {
                JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.detailFamille.integrity");
            }
        } catch (DetailFamilleException e) {
            throw new FamilleException("Unable to check detailFamille for delete famille", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Unable to get detailFamille service", e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new FamilleException("Unable to get detailFamille service", e);
        }
    }

    /**
     * @param simpleFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleFamille simpleFamille) throws FamilleException, JadePersistenceException {
        SimpleFamilleChecker.checkMandatory(simpleFamille);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleFamilleChecker.checkIntegrity(simpleFamille);
        }
    }

    private static void checkIntegrity(SimpleFamille simpleFamille) throws FamilleException, JadePersistenceException {

        if (!JadeStringUtil.isBlankOrZero(simpleFamille.getDateAvisRIP())
                && !JadeDateUtil.isGlobazDate(simpleFamille.getDateAvisRIP())) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.revenu.simpleFamille.integrity.dateAvisRIP");
        }

        if (!JadeStringUtil.isBlankOrZero(simpleFamille.getDateNaissance())
                && !JadeDateUtil.isGlobazDate(simpleFamille.getDateNaissance())) {
            JadeThread
                    .logError(simpleFamille.getClass().getName(), "amal.revenu.simpleFamille.integrity.dateNaissance");
        }

        if (!JadeStringUtil.isBlankOrZero(simpleFamille.getCodeTraitementDossier())
                && JadeStringUtil.isBlankOrZero(simpleFamille.getFinDefinitive())) {
            JadeThread.logError(simpleFamille.getClass().getName(),
                    "amal.revenu.simpleFamille.integrity.finDefEmptyAndNotCodeTraitement");
        }

        if (JadeStringUtil.isBlankOrZero(simpleFamille.getCodeTraitementDossier())
                && !JadeStringUtil.isBlankOrZero(simpleFamille.getFinDefinitive())) {
            JadeThread.logError(simpleFamille.getClass().getName(),
                    "amal.revenu.simpleFamille.integrity.CodeTraitementEmptyAndNotFin");
        }
    }

    private static void checkMandatory(SimpleFamille simpleFamille) {
        if (JadeStringUtil.isBlankOrZero(simpleFamille.getIdContribuable())) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.idContribuable.mandatory");
        }

        if (JadeStringUtil.isBlank(simpleFamille.getNomPrenom())) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.nomPrenom.mandatory");
        }

        if (JadeStringUtil.isEmpty(simpleFamille.getDateNaissance())) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.dateNaissance.mandatory");
        }

        if (simpleFamille.getIsContribuable() == null) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.isContribuable.mandatory");
        }

        if (JadeStringUtil.isBlankOrZero(simpleFamille.getPereMereEnfant())) {
            JadeThread.logError(simpleFamille.getClass().getName(), "amal.famille.pereMereEnfant.mandatory");
        }
    }

    private static Boolean hasSubsides(SimpleFamille simpleFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
        detailFamilleSearch.setForIdFamille(simpleFamille.getIdFamille());

        return AmalServiceLocator.getDetailFamilleService().count(detailFamilleSearch) > 0;
    }
}
