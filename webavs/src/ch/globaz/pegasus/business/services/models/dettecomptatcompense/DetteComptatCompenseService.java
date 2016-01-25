package ch.globaz.pegasus.business.services.models.dettecomptatcompense;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Map;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.dettecomptatcompense.DetteComptatCompenseException;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseOrdreVersement;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;
import ch.globaz.pegasus.business.vo.decompte.DetteEnComptaVO;

public interface DetteComptatCompenseService extends
        JadeCrudService<SimpleDetteComptatCompense, SimpleDetteComptatCompenseSearch> {

    /**
     * Retourne une map avec comme clé l'id de la dette compensé La valeur de la value correspond a la list des ov qui
     * ont compensé la dette et le montant compensé
     * 
     * @param idSection
     * @param idVersionDroit
     * @param idDroit
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public Map<String, ListTotal<DetteCompenseOrdreVersement>> findDetteOvMontantCompense(String idVersionDroit)
            throws DetteComptatCompenseException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de retrouvé toute les dettes compensé et retourne le montant des dette compensé
     * 
     * @param idVersionDroit
     * @param idDroit
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public ListTotal<SimpleDetteComptatCompense> findListTotalCompense(String idVersionDroit, String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la liste des dette en compta sous forme de value object <DetteEnCOmptaVO>
     * 
     * @param idVersioNDroit
     * @param idDroit
     * @return List<DetteEnComptaVO>
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public ListTotal<DetteEnComptaVO> findListTotalCompenseAsDetteEnComptaVO(String idVersioNDroit, String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException;

    public SimpleDetteComptatCompense read(String idSection, String idVersionDroit, String idDroit)
            throws JadeApplicationException, JadePersistenceException;

}
