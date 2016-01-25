package ch.globaz.pegasus.business.services.models.mutation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.mutation.MutationAbstract;
import ch.globaz.pegasus.business.models.mutation.MutationPca;
import ch.globaz.pegasus.business.models.recap.RecapCategorie;
import ch.globaz.pegasus.business.vo.lot.MutationPcaVo;
import ch.globaz.pegasus.businessimpl.services.models.mutation.AlloctionNoelGrouped;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;

public interface MutationPcaService extends JadeApplicationService {
    /**
     * Permet de rechercher les mutations et le groupe en fonction de la catégorie de la PCA et aussi par futur et
     * normal
     * 
     * @param dateMonth
     * @return Map<RecapCategorie,Map<categorie,listeDesMutation>
     * @throws JadePersistenceException
     * @throws MutationException
     */
    public Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> findAndgroupeByCategorie(String dateMonth)
            throws MutationException, JadePersistenceException;

    /**
     * Permet de grouper en fonction de la catégorie de la mutation Un mutation peut se trouvé dans deux catégorie ou
     * domaine. Car il faudra dans un faire une diminution et dans l'autre une augmentations.
     * 
     * @param list
     * @return Map<FUTUR|NORAML|...,Map<categorie,listeDesMutation>
     * @throws MutationException
     * @throws JadePersistenceException
     */
    public Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> groupeByCategorie(List<MutationPcaVo> list);

    /**
     * Permet de merger 2 liste de pca la première correspond aux pca qui on été validé dans une decision et la deuxième
     * contient les ancien pca courante Ensuite cette fonction consolide les donnée et retour une list de mutationPCavo
     * 
     * @param listeValidee
     * @param listOldCurent
     * @param dateMonth
     * @return
     */
    public List<MutationPcaVo> mergeListAndConsolideData(List<MutationPca> listeValidee,
            List<MutationAbstract> listOldCurent, AlloctionNoelGrouped allocation, String dateMonth);

    public void sortByDate(List<MutationPcaVo> list);
}
