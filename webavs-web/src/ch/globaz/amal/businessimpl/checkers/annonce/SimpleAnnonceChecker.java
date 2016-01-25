/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.annonce;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimpleAnnonceChecker extends AmalAbstractChecker {

    /**
     * Contr�le de la cr�ation d'une annonce
     * 
     * @param annonce
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleAnnonce annonce) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les �l�ments boolean doivent �tre renseign�s
        SimpleAnnonceChecker.checkMandatory(annonce);
        // Le detail famille en relation doit exister + ...
        SimpleAnnonceChecker.checkIntegrity(annonce);
    }

    /**
     * Contr�le de la suppression d'une annonce
     * 
     * @param annonce
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleAnnonce annonce) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Effacement autoris� uniquement si le detail famille
        // en relation n'est plus existant >> UN PEU FOIREUX, A ETUDIER
        // On cherche le detail famille li�
        // SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
        // detailFamilleSearch.setForIdDetailFamille(annonce.getIdDetailFamille());
        // AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailFamilleSearch);
        //
        // if (detailFamilleSearch.getSize() > 0) {
        // JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.idDetailFamille.integrity");
        // }

    }

    /**
     * Contr�le de la mise � jour d'une annonce
     * 
     * @param annonce
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleAnnonce annonce) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les �l�ments boolean doivent �tre renseign�s
        SimpleAnnonceChecker.checkMandatory(annonce);
        // Le detail famille en relation doit exister +...
        SimpleAnnonceChecker.checkIntegrity(annonce);

    }

    /**
     * L'�l�ment annonce doit �tre li� � un detail famille existant et les dates de d�buts doivent �tre avant les dates
     * de fin
     * 
     * @param annonce
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    private static void checkIntegrity(SimpleAnnonce annonce) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // On cherche le detail famille li�
        SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
        detailFamilleSearch.setForIdDetailFamille(annonce.getIdDetailFamille());
        AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailFamilleSearch);

        if (detailFamilleSearch.getSize() != 1) {
            JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.idDetailFamille.integrity");
        }

        // contr�le sur les dates : date de d�but doit �tre avant date de fin
        if ((annonce.getFinDroit() != null)
                && ((annonce.getDebutDroit() != null) && !JadeStringUtil.isBlankOrZero(annonce.getFinDroit()))) {
            if (!annonce.getDebutDroit().equals(annonce.getFinDroit())) {
                if (!JadeDateUtil.isDateMonthYearBefore(annonce.getDebutDroit(), annonce.getFinDroit())) {
                    JadeThread
                            .logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.debutDroit.integrity");
                }
            }
        }

    }

    /**
     * Les �l�ments boolean doivent �tre renseign�s
     * 
     * @param annonce
     */
    private static void checkMandatory(SimpleAnnonce annonce) {
        // Code Actif
        try {
            Boolean cActif = annonce.getCodeActif();
            if (cActif == null) {
                JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.cActif.mandatory");
            }
        } catch (Exception ex) {
            JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.cActif.mandatory");
        }
        // Code Forcer
        try {
            Boolean cForce = annonce.getCodeForcer();
            if (cForce == null) {
                JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.codeForcer.mandatory");
            }
        } catch (Exception ex) {
            JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.codeForcer.mandatory");
        }
        // Status refuse
        try {
            Boolean refuse = annonce.getRefuse();
            if (refuse == null) {
                JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.refuse.mandatory");
            }
        } catch (Exception ex) {
            JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.refuse.mandatory");
        }
        // Get annonce cm
        try {
            Boolean annonceCM = annonce.getAnnonceCaisseMaladie();
            if (annonceCM == null) {
                JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.annonceCM.mandatory");
            }
        } catch (Exception ex) {
            JadeThread.logError(annonce.getClass().getName(), "amal.annonce.simpleAnnonce.annonceCM.mandatory");
        }
    }

}
