package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;

/**
 * @author SCO
 * @since SCO 11 juin 2010
 */
public class CETiersService {

    /**
     * Permet de retourner une dataSource sur l'adtresse d'un tier
     * 
     * @param typeAdresse
     * @return
     * @throws Exception
     */
    public static TIAdresseDataSource retrieveAdresseDataSource(String typeAdresse, TITiers tiers, String numAffilie)
            throws Exception {

        TIAdresseDataSource d = null;

        if (typeAdresse.equals("domicile")) {
            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        IConstantes.CS_APPLICATION_DEFAUT, numAffilie, JACalendar.todayJJsMMsAAAA(), true, null);
            }
            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, numAffilie, JACalendar.todayJJsMMsAAAA(), true,
                        null);
            }
        } else {
            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, numAffilie, JACalendar.todayJJsMMsAAAA(), true,
                        null);
            }
            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        IConstantes.CS_APPLICATION_DEFAUT, numAffilie, JACalendar.todayJJsMMsAAAA(), true, null);
            }
        }

        return d;
    }

    /**
     * PErmet la récupération d'un tiers par son identifiant
     * 
     * @param session
     *            Une session
     * @param idTiers
     *            Un identifiant de tiers
     * @return TITiers
     * @throws HerculeException
     */
    public static TITiers retrieveTiers(BSession session, String idTiers) throws HerculeException {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve TITiers, session is null");
        }

        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new HerculeException("Unabled to retrieve TITiers, idTiers is null or empty");
        }

        TITiers tiers = new TITiers();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        try {
            tiers.retrieve();
        } catch (Exception e) {
            tiers = null;
            throw new HerculeException("Technical Exception, Unabled to retrieve the tiers ( idTiers = " + idTiers
                    + ")", e);
        }

        return tiers;
    }

    /**
     * Permet la récupération d'un tiers par son identifiant (un Tiers de type TITiersViewBean)
     * 
     * @param session
     * @param idTiers
     * @return
     * @throws HerculeException
     */
    public static TITiersViewBean retrieveTiersViewBean(BSession session, String idTiers) throws HerculeException {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve TITiersViewBean, session is null");
        }

        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new HerculeException("Unabled to retrieve TITiersViewBean, idTiers is null or empty");
        }

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        try {
            tiers.retrieve();
        } catch (Exception e) {
            tiers = null;
            throw new HerculeException("Technical Exception, Unabled to retrieve the tiers viewBean ( idTiers = "
                    + idTiers + ")", e);
        }

        return tiers;
    }

    /**
     * Constructeur de CETiersService
     */
    protected CETiersService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
