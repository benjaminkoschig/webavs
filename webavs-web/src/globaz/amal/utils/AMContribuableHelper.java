package globaz.amal.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import globaz.pyxis.db.tiers.TIMoyenCommunicationManager;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.amal.businessimpl.utils.SessionProvider;

public class AMContribuableHelper {
    public static Map<String, String> getMoyensContact(String idTiers) {

        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return null;
        }

        TIMoyenCommunicationManager communicationManager = new TIMoyenCommunicationManager();
        communicationManager.setSession(SessionProvider.findSession());
        communicationManager.setForIdContact(idTiers);
        Map<String, String> mapMoyens = new HashMap<String, String>();
        try {
            communicationManager.find();

            for (int i = 0; i <= communicationManager.size() - 1; i++) {
                TIMoyenCommunication moyenCommunication = (TIMoyenCommunication) communicationManager.getEntity(i);
                String key = moyenCommunication.getTypeCommunication();
                String value = moyenCommunication.getMoyen();
                mapMoyens.put(key, value);
            }
        } catch (Exception e) {
            mapMoyens = null;
        }
        return mapMoyens;
    }

    public static String getPays(String idPays) {

        if (!JadeStringUtil.isBlankOrZero(idPays)) {
            BSession session = SessionProvider.findSession();

            TIPaysManager paysManager = new TIPaysManager();
            paysManager.setSession(session);
            paysManager.setForIdPays(idPays);
            try {
                paysManager.find();
                TIPays pays = (TIPays) paysManager.getEntity(0);
                return pays.getCodeIso();
            } catch (Exception e) {
                session.addWarning("Code pays introuvable pour l'idPays : " + idPays);
            }
        }

        return "";

    }

    public static String getSexe(String idSexe) {
        if ("516001".equals(idSexe)) {
            return "H";
        } else if ("516002".equals(idSexe)) {
            return "F";
        } else {
            return "";
        }
    }
}
