/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee.step1;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.process.repriseFinAnnee.AMProcessRepriseFinAnneeEnum;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneeEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private JadeProcessEntity currentEntity = null;
    private Map<Enum<?>, String> data = null;
    private String idContribuable = "";
    private List<String> idsDetailFamille = new ArrayList<String>();

    private void checkAndSetFinDroit(String idCurrentFamille, String anneeHistorique) throws FamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // -------------------------------------------------------------
        // 1) Récupération de la date de naissance
        // -------------------------------------------------------------
        FamilleContribuable currentFamille = AmalServiceLocator.getFamilleContribuableService().read(idCurrentFamille);
        SimpleFamille currentSimpleFamille = currentFamille.getSimpleFamille();
        String dateNaissance = "";
        if (JadeStringUtil.isBlankOrZero(currentSimpleFamille.getIdTier())) {
            dateNaissance = currentSimpleFamille.getDateNaissance();
        } else {
            if (JadeStringUtil.isBlankOrZero(currentFamille.getPersonneEtendue().getPersonne().getDateNaissance())) {
                dateNaissance = currentSimpleFamille.getDateNaissance();
            } else {
                dateNaissance = currentFamille.getPersonneEtendue().getPersonne().getDateNaissance();
            }
        }

        // -------------------------------------------------------------
        // 2) Contrôle si rôle enfant et fin droit à ajuster si > 25 ans
        // -------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
            JadeThread.logWarn("WARNING", "Pas de date de naissance pour : " + currentSimpleFamille.getNomPrenom()
                    + ";");
        } else {
            int iNbYear = JadeDateUtil.getNbYearsBetween(dateNaissance, "01.01." + anneeHistorique,
                    JadeDateUtil.YEAR_COMPARISON);
            if (iNbYear == -1) {
                JadeThread.logWarn("WARNING",
                        "Nombre années impossible à déterminer : " + currentSimpleFamille.getNomPrenom() + " - "
                                + dateNaissance + ";");
            } else if (iNbYear > 25) {
                if (currentSimpleFamille.getPereMereEnfant().equals(IAMCodeSysteme.CS_TYPE_ENFANT)) {
                    if (JadeStringUtil.isBlankOrZero(currentSimpleFamille.getFinDefinitive())) {
                        JadeThread.logInfo("INFO", "FIN DE DROIT : " + currentSimpleFamille.getNomPrenom() + " - "
                                + dateNaissance + " - " + iNbYear + " ans" + ";");
                        currentSimpleFamille.setFinDefinitive("12." + (Integer.parseInt(anneeHistorique) - 1));
                        currentSimpleFamille
                                .setCodeTraitementDossier(IAMCodeSysteme.AMCodeTraitementDossierFamille.PLUS_CHARGE_PARENTS
                                        .getValue());
                        AmalImplServiceLocator.getSimpleFamilleService().update(currentSimpleFamille);
                    }
                }
            } else {
                // JadeThread.logInfo("INFO", "Pas de fin de droit : " + currentSimpleFamille.getNomPrenom() + " - "
                // + dateNaissance + " - " + iNbYear + " ans" + ";");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#run()
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);

        if (idsDetailFamille.size() > 0) {
            for (int iIdDetailFamille = 0; iIdDetailFamille < idsDetailFamille.size(); iIdDetailFamille++) {
                // -------------------------------------------------------------
                // 1) Pour chaque détail famille, récupérer le membre de famille
                // -------------------------------------------------------------
                String idCurrentDetailFamille = idsDetailFamille.get(iIdDetailFamille);

                SimpleDetailFamille currentDetailFamille = AmalServiceLocator.getDetailFamilleService().read(
                        idCurrentDetailFamille);
                String idCurrentFamille = currentDetailFamille.getIdFamille();

                // -------------------------------------------------------------
                // 2) Check date naissance et set fin droit
                // -------------------------------------------------------------
                checkAndSetFinDroit(idCurrentFamille, anneeHistorique);
            }
        } else {

            // ------------------------------------------------------------
            // 1 Récupérer la famille du contribuable
            // ------------------------------------------------------------
            FamilleContribuableViewSearch searchFamille = new FamilleContribuableViewSearch();
            searchFamille.setForIdContribuable(idContribuable);
            searchFamille = AmalServiceLocator.getFamilleContribuableService().search(searchFamille);
            for (int iFamille = 0; iFamille < searchFamille.getSize(); iFamille++) {
                FamilleContribuableView currentFamilleView = (FamilleContribuableView) searchFamille.getSearchResults()[iFamille];
                String idCurrentFamille = currentFamilleView.getId();

                // -------------------------------------------------------------
                // 2) Check date naissance et set fin droit
                // -------------------------------------------------------------
                checkAndSetFinDroit(idCurrentFamille, anneeHistorique);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#setCurrentEntity(ch.globaz
     * .jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        currentEntity = entity;
        idContribuable = entity.getIdRef();
        String idsDetailFamilleFromEntity = entity.getValue1();
        if (!JadeStringUtil.isEmpty(idsDetailFamilleFromEntity)) {
            if (idsDetailFamilleFromEntity.indexOf(";") >= 0) {
                String[] allIds = idsDetailFamilleFromEntity.split(";");
                for (int iCurrentId = 0; iCurrentId < allIds.length; iCurrentId++) {
                    idsDetailFamille.add(allIds[iCurrentId]);
                }
            } else {
                idsDetailFamille.add(idsDetailFamilleFromEntity);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties#setProperties(java.util
     * .Map)
     */
    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        data = map;
    }

}
