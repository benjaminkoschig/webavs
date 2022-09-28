package globaz.webavs.common.ws.security;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.webavs.common.CommonProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SecurityUtils {

    public static final String PROPERTY_PYXIS_PLAGE_VALEURS_ACCES_WS = "EWSACNT";

    private static List<String> getListCaissesWebServices(BSession session) throws Exception {
        List<String> listCaissesEbill = new ArrayList<>();
        FWFindParameterManager mgr = getPlageValeurWebServices(session);
        for(int idx = 0; idx < mgr.size(); idx++){
            FWFindParameter param = (FWFindParameter) mgr.get(idx);
            if(!JadeStringUtil.isBlank(param.getValeurAlphaParametre())){
                listCaissesEbill.add(JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(param.getValeurAlphaParametre()));
            }
        }
        return listCaissesEbill;
    }

    private static FWFindParameterManager getPlageValeurWebServices(BSession session) throws Exception {
        FWFindParameterManager mgr = new FWFindParameterManager();
        mgr.setSession(session);
        mgr.setIdApplParametre(session.getApplicationId());
        mgr.setIdCleDiffere(PROPERTY_PYXIS_PLAGE_VALEURS_ACCES_WS);
        mgr.find(BManager.SIZE_NOLIMIT);
        return mgr;
    }

    public static boolean hasAccessToWS(BSession session){
        boolean hasAccessWS = false;
        try {
            // Vérifier que la caisse avs est dans la liste crypté (Applications->Administration->Plages de valeurs -> PYXIS + EWSACNT
            String noCaisse = session.getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE, "");
            List<String> getListCaisses = null;
            try {
                getListCaisses = getListCaissesWebServices(session);
            } catch (Exception e) {
                LOG.error("Erreur lors de la récupération de la liste des accès WS pour les caisses",  e);
            }
            hasAccessWS = getListCaisses.contains(noCaisse);

        } catch (Exception ex) {
            LOG.error("Erreur lors de la récuopération des accès WS", ex);
        }
        return hasAccessWS;
    }
}
