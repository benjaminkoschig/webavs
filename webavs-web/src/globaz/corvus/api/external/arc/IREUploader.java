package globaz.corvus.api.external.arc;

import globaz.corvus.api.external.arc.rassemblement.IArcVO;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;

/**
 * L'interface de base que doivent implémentés les uploaders
 * 
 * @author SCR
 */
public interface IREUploader {

    /**
     * Set la session de l'uploader
     * 
     * @param session
     */
    void setSession(BISession session);

    /**
     * Prodede a l'upload du vo donne
     * 
     * @param transaction
     * @param vo
     * @throws REUploaderException
     */
    void upload(BITransaction transaction, IArcVO[] vo) throws REUploaderException;
}
