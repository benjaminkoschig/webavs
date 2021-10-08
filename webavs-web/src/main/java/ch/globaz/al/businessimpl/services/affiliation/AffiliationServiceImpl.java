package ch.globaz.al.businessimpl.services.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.exceptions.affiliations.ALAffiliationException;
import ch.globaz.al.business.services.affiliation.AffiliationService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service affiliation des AF
 * 
 * @author GMO
 * 
 */
public class AffiliationServiceImpl extends ALAbstractBusinessServiceImpl implements AffiliationService {

    @Override
    public String convertCantonNaos2CantonAF(String cantonNaos) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(cantonNaos)) {
            throw new ALAffiliationException("AffiliationServiceImpl#convertCantonNaos2CantonAF : " + cantonNaos
                    + "is not a valid value");
        }

        HashMap<String, String> cantons = new HashMap<String, String>();
        cantons.put("505019", ALCSCantons.AG);
        cantons.put("505016", ALCSCantons.AI);
        cantons.put("505015", ALCSCantons.AR);
        cantons.put("505002", ALCSCantons.BE);
        cantons.put("505013", ALCSCantons.BL);
        cantons.put("505012", ALCSCantons.BS);
        cantons.put("505010", ALCSCantons.FR);
        cantons.put("505025", ALCSCantons.GE);
        cantons.put("505008", ALCSCantons.GL);
        cantons.put("505018", ALCSCantons.GR);
        cantons.put("505026", ALCSCantons.JU);
        cantons.put("505003", ALCSCantons.LU);
        cantons.put("505024", ALCSCantons.NE);
        cantons.put("505007", ALCSCantons.NW);
        cantons.put("505006", ALCSCantons.OW);
        cantons.put("505017", ALCSCantons.SG);
        cantons.put("505014", ALCSCantons.SH);
        cantons.put("505011", ALCSCantons.SO);
        cantons.put("505005", ALCSCantons.SZ);
        cantons.put("505020", ALCSCantons.TG);
        cantons.put("505021", ALCSCantons.TI);
        cantons.put("505004", ALCSCantons.UR);
        cantons.put("505022", ALCSCantons.VD);
        cantons.put("505023", ALCSCantons.VS);
        cantons.put("505009", ALCSCantons.ZG);
        cantons.put("505001", ALCSCantons.ZH);

        String canton = cantons.get(cantonNaos);

        if (null == canton) {
            throw new ALAffiliationException("AffiliationServiceImpl#convertCantonNaos2CantonAF : " + cantonNaos
                    + " is not a valid value");
        }

        return canton;
    }

    @Override
    public String convertCantonAF2Naos(String cantonAF) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(cantonAF)) {
            throw new ALAffiliationException("AffiliationServiceImpl#convertCantonAF2Naos : " + cantonAF
                    + "is not a valid value");
        }

        HashMap<String, String> cantons = new HashMap<String, String>();
        cantons.put(ALCSCantons.AG, "505019");
        cantons.put(ALCSCantons.AI, "505016");
        cantons.put(ALCSCantons.AR, "505015");
        cantons.put(ALCSCantons.BE, "505002");
        cantons.put(ALCSCantons.BL, "505013");
        cantons.put(ALCSCantons.BS, "505012");
        cantons.put(ALCSCantons.FR, "505010");
        cantons.put(ALCSCantons.GE, "505025");
        cantons.put(ALCSCantons.GL, "505008");
        cantons.put(ALCSCantons.GR, "505018");
        cantons.put(ALCSCantons.JU, "505026");
        cantons.put(ALCSCantons.LU, "505003");
        cantons.put(ALCSCantons.NE, "505024");
        cantons.put(ALCSCantons.NW, "505007");
        cantons.put(ALCSCantons.OW, "505006");
        cantons.put(ALCSCantons.SG, "505017");
        cantons.put(ALCSCantons.SH, "505014");
        cantons.put(ALCSCantons.SO, "505011");
        cantons.put(ALCSCantons.SZ, "505005");
        cantons.put(ALCSCantons.TG, "505020");
        cantons.put(ALCSCantons.TI, "505021");
        cantons.put(ALCSCantons.UR, "505004");
        cantons.put(ALCSCantons.VD, "505022");
        cantons.put(ALCSCantons.VS, "505023");
        cantons.put(ALCSCantons.ZG, "505009");
        cantons.put(ALCSCantons.ZH, "505001");

        String canton = cantons.get(cantonAF);

        if (null == canton) {
            throw new ALAffiliationException("AffiliationServiceImpl#convertCantonAF2Naos : " + cantonAF
                    + " is not a valid value");
        }

        return canton;
    }
}