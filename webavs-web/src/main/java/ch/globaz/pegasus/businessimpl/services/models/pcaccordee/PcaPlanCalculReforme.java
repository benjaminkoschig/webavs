package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculReforme;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculReformeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompteSearch;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionUtils;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;


public class PcaPlanCalculReforme {

    /**
     * retourne la liste des pca avec le type du plan de calcul retenu (réforme ou non)
     * @param idDroit
     * @param noVersionDroitCourant
     * @return
     * @throws JadePersistenceException
     */
    public static List<PCAccordeePlanCalculReforme> findPcaCourrante(String idDroit, String noVersionDroitCourant)
            throws JadePersistenceException {

        PCAccordeePlanCalculReformeSearch search = new PCAccordeePlanCalculReformeSearch();

        search.setForIdDroit(idDroit);
        search.setForNoVersion(noVersionDroitCourant);
        search.setForIsPlanRetenu(true);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return PersistenceUtil.search(search);

    }

    /**
     * retourne la liste des pca avec le type du plan de calcul associe (réforme ou non)
     * @param idDroit
     * @param noVersionDroitCourant
     * @return
     * @throws JadePersistenceException
     */
    public static List<PCAccordeePlanCalculReforme> findPcaDroit(String idDroit,
                                                                      String noVersionDroitCourant) throws JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the dDroit is null!");
        }

        if (JadeStringUtil.isBlankOrZero(noVersionDroitCourant)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the noVersionDroitCourant is null!");
        }

        PCAccordeePlanCalculReformeSearch search = new PCAccordeePlanCalculReformeSearch();
        List<PCAccordeePlanCalculReforme> list = new ArrayList<>();

        if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
            search.setForIdDroit(idDroit);
            search.setForNoVersion(noVersionDroitCourant);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            list = PersistenceUtil.search(search);

            if (list.size() == 0) {
                if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
                    throw new JadePersistenceException("Unable to find the old pca for change the stat to historisée");
                }
            }
        }
        return list;
    }

    /**
     * Retour la date à partir de laquelle les pca sont de type réforme sans calcul comparatif pour la verion de droit donnée
     * @param idDroit
     * @param noVersionDroitCourant
     * @return
     * @throws JadePersistenceException
     */
    public static String getSplitDateReformeFromVersion(String idDroit, String noVersionDroitCourant) throws JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the dDroit is null!");
        }

        if (JadeStringUtil.isBlankOrZero(noVersionDroitCourant)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the noVersionDroitCourant is null!");
        }

        PCAccordeePlanCalculReformeSearch search = new PCAccordeePlanCalculReformeSearch();
        List<PCAccordeePlanCalculReforme> list = new ArrayList<>();

        if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
            search.setForIdDroit(idDroit);
            search.setForNoVersion(noVersionDroitCourant);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            list = PersistenceUtil.search(search);

            if (list.size() == 0) {
                if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
                    throw new JadePersistenceException("Unable to find the old pca for change the stat to historisée");
                }
            }
        }

        Map<String, List<PCAccordeePlanCalculReforme>> mPcaPdc = new HashMap<>();
        for(PCAccordeePlanCalculReforme pcaPdc : list) {
            if(mPcaPdc.get(pcaPdc.getIdPca()) == null){
                List<PCAccordeePlanCalculReforme> lPcaPdc = new ArrayList<>();
                mPcaPdc.put(pcaPdc.getIdPca(), lPcaPdc);
            }
            mPcaPdc.get(pcaPdc.getIdPca()).add(pcaPdc);
        }

        String dateSplit = null;
        for(List<PCAccordeePlanCalculReforme> lPcaPdc: mPcaPdc.values()) {
            if (onlyReforme(lPcaPdc)) {
                return lPcaPdc.get(0).getDateDebut();
            }
        }

        return null;
    }

    /**
     * return vrai si toutes les pca sont de type réforme
     * @param lPcaPdc
     * @return
     */
    private static boolean onlyReforme(List<PCAccordeePlanCalculReforme> lPcaPdc){
        for(PCAccordeePlanCalculReforme pcaPdc : lPcaPdc) {
            if(!pcaPdc.getReformePc()) {
                return false;
            }
        }
        return true;
    }

}
