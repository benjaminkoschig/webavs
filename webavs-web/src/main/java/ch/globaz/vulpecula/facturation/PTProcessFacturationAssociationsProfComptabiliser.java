package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;

/**
 * Processus de facturation des associations professionnelles
 * 
 * @since WebBMS 2.1
 */
public class PTProcessFacturationAssociationsProfComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = -7993191008606629749L;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public PTProcessFacturationAssociationsProfComptabiliser() {
        super();
    }

    public PTProcessFacturationAssociationsProfComptabiliser(final BProcess parent) {
        super(parent);
    }

    @Override
    protected void clean() {
    }

    @Override
    protected boolean launch() {
        List<EnteteFactureAssociation> listeEnteteFacture = VulpeculaRepositoryLocator.getEnteteFactureRepository()
                .findByIdPassageFacturation(getPassage().getId());

        for (EnteteFactureAssociation enteteFacture : listeEnteteFacture) {

            if (!getTransaction().hasErrors()) {
                // On va mettre à jour le numéro de passage et l'état du décompte à validé
                majEtatFacture(enteteFacture);
            } else {
                if (JadeThread.logMessages() != null && JadeThread.logMessages().length > 0) {
                    for (JadeBusinessMessage message : JadeThread.logMessages()) {
                        getTransaction().addErrors(message.getMessageId());
                        LOGGER.error(message.getMessageId());
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Met le passage à l'état facturation
     * 
     * @param enteteFacture
     */
    private void majEtatFacture(final EnteteFactureAssociation enteteFacture) {
        enteteFacture.setEtat(EtatFactureAP.COMPTABILISE);
        VulpeculaRepositoryLocator.getEnteteFactureRepository().update(enteteFacture);
    }

}
