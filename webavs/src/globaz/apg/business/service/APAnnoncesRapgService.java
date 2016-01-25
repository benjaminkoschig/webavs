package globaz.apg.business.service;

import globaz.apg.exceptions.APAnnoncesException;
import globaz.apg.exceptions.APPlausibilitesException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;

public interface APAnnoncesRapgService extends JadeApplicationService {

    public final String messageType = "2015";
    public final String subMessageType1 = "000101";
    public final String subMessageType3 = "000301";
    public final String subMessageType4 = "000401";

    /**
     * Envoyer les annonces pour un lot
     * 
     * @param idLot
     * @param accountingMonth
     */
    public List<APChampsAnnonce> envoyerAnnonces(List<APChampsAnnonce> listChampsAnnonces, FWMemoryLog memoryLog,
            BSession session) throws APAnnoncesException, APPlausibilitesException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Ajouter les constantes dans une annonce pour les contrôles (senderId, recipientId, messageType, subMessageType,
     * deliveryOffice)
     * 
     * @param champsAnnonce
     * @return
     */
    public APChampsAnnonce fullWithConstants(APChampsAnnonce champsAnnonce) throws APAnnoncesException;

    /**
     * Retourne une annonce au bon format sur la base des champsAnnonce
     * 
     * @param champsAnnonce
     * @return
     */
    public Object preparerAnnonce(APChampsAnnonce champsAnnonce) throws APAnnoncesException;

}
