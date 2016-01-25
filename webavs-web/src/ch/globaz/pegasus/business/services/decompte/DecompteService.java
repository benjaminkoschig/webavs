package ch.globaz.pegasus.business.services.decompte;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.pca.DecomptePca;

public interface DecompteService extends JadeApplicationService {

    /**
     * Permet de trouver la date min et la date max d'une liste de pca
     * 
     * @param newPcas
     * @return un Tableau avec 2 valeurs la première correspond à la date min et la deuxième à la date max
     */
    public String[] determinDateMinMax(List<PcaDecompte> newPcas);

    public DecomptePca generateDecomptePca(List<PcaDecompte> newPcas, List<PcaDecompte> replacedPca,
            String dateDernierPmtPca, List<SimpleJoursAppoint> joursAppointNew,
            List<SimpleJoursAppoint> joursAppointReplaced) throws DecompteException;

    public DecompteTotalPcVO getDecompteTotalPCA(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet de savoir si il reste de l'argent sur le retro que l'on peut retenir
     * 
     * @param idVersionDroit
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public Boolean isMontantDisponibleRetnable(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet de savoir si il reste de l'argent sur le retro que l'on peut retenir
     * 
     * @param idVersionDroit
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public int saveRemarqueSurDecompte(String idVersionDroit, String remarque) throws JadePersistenceException,
            JadeApplicationException;
}
