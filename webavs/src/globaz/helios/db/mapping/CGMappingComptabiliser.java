/*
 * Créé le Dec 12, 2005, dda
 */
package globaz.helios.db.mapping;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dda
 * 
 */
public class CGMappingComptabiliser extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_DESTINATION = 3;

    public static final int ALTERNATE_KEY_SOURCE = 1;

    public static final int ALTERNATE_KEY_SOURCE_WITH_CENTRECHARGE = 2;

    public static final String FIELD_IDCENTRECHARGEDEST = "IDCENTRECHARGEDEST";

    public static final String FIELD_IDCENTRECHARGESRC = "IDCENTRECHARGESRC";
    public static final String FIELD_IDCOMPTEDEST = "IDCOMPTEDEST";
    public static final String FIELD_IDCOMPTESRC = "IDCOMPTESRC";
    public static final String FIELD_IDCONTRECRDEST = "IDCONTRECRDEST";

    public static final String FIELD_IDMANDATDEST = "IDMANDATDEST";
    public static final String FIELD_IDMANDATSRC = "IDMANDATSRC";
    public static final String FIELD_IDMAPCP = "IDMAPCP";
    private static final String LABEL_GLOBAL_MANDAT_INEXISTANT = "GLOBAL_MANDAT_INEXISTANT";

    private static final String LABEL_MAPPING_COMPTE_EGAL = "MAPPING_COMPTE_EGAL";
    private static final String LABEL_MAPPING_COMPTE_ERROR = "MAPPING_COMPTE_ERROR";
    public static final String TABLE_CGMAPCP = "CGMAPCP";

    public static CGPlanComptableViewBean getPlanComptable(BSession session, String idCompte, String idMandat) {
        try {
            if ((!JadeStringUtil.isIntegerEmpty(idCompte)) && (!JadeStringUtil.isIntegerEmpty(idMandat))) {
                CGPlanComptableManager manager = new CGPlanComptableManager();
                manager.setSession(session);

                manager.setForIdMandat(idMandat);
                manager.setForIdCompte(idCompte);

                manager.find();

                if (manager.size() > 0) {
                    return (CGPlanComptableViewBean) manager.getFirstEntity();
                } else {
                    return new CGPlanComptableViewBean();
                }
            } else {
                return new CGPlanComptableViewBean();
            }
        } catch (Exception e) {
            return new CGPlanComptableViewBean();
        }
    }

    private String idCentreChargeDestination = new String();
    private String idCentreChargeSource = new String();
    private String idCompteDestination = new String();

    private String idCompteSource = new String();
    private String idContreEcritureDestination = new String();
    private String idExterneCompteDestination = new String();
    private String idExterneCompteSource = new String();

    private String idExterneContreEcritureDestination = new String();
    private String idMandatDestination = new String();
    private String idMandatSource = new String();

    private String idMapComptabiliser;

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if ((!JadeStringUtil.isIntegerEmpty(getIdCompteSource())) && JadeStringUtil.isBlank(getIdExterneCompteSource())) {
            setIdExterneCompteSource(getPlanComptable(getSession(), getIdCompteSource(), getIdMandatSource())
                    .getIdExterne());
        }

        if ((!JadeStringUtil.isIntegerEmpty(getIdCompteDestination()))
                && JadeStringUtil.isBlank(getIdExterneCompteDestination())) {
            setIdExterneCompteDestination(getPlanComptable(getSession(), getIdCompteDestination(),
                    getIdMandatDestination()).getIdExterne());
        }

        if ((!JadeStringUtil.isIntegerEmpty(getIdContreEcritureDestination()))
                && JadeStringUtil.isBlank(getIdExterneContreEcritureDestination())) {
            setIdExterneContreEcritureDestination(getPlanComptable(getSession(), getIdContreEcritureDestination(),
                    getIdMandatDestination()).getIdExterne());
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdMapComptabiliser(_incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_CGMAPCP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdMapComptabiliser(statement.dbReadNumeric(FIELD_IDMAPCP));

        setIdCompteSource(statement.dbReadNumeric(FIELD_IDCOMPTESRC));
        setIdMandatSource(statement.dbReadNumeric(FIELD_IDMANDATSRC));
        setIdCentreChargeSource(statement.dbReadNumeric(FIELD_IDCENTRECHARGESRC));

        setIdCompteDestination(statement.dbReadNumeric(FIELD_IDCOMPTEDEST));
        setIdMandatDestination(statement.dbReadNumeric(FIELD_IDMANDATDEST));
        setIdContreEcritureDestination(statement.dbReadNumeric(FIELD_IDCONTRECRDEST));
        setIdCentreChargeDestination(statement.dbReadNumeric(FIELD_IDCENTRECHARGEDEST));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdMandatSource())
                || JadeStringUtil.isIntegerEmpty(getIdMandatDestination())) {
            throw new Exception(getSession().getLabel(LABEL_GLOBAL_MANDAT_INEXISTANT));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdCompteSource())) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel(LABEL_MAPPING_COMPTE_ERROR),
                    getIdExterneCompteSource()));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdCompteDestination())) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel(LABEL_MAPPING_COMPTE_ERROR),
                    getIdExterneCompteDestination()));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdContreEcritureDestination())) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel(LABEL_MAPPING_COMPTE_ERROR),
                    getIdExterneContreEcritureDestination()));
        }

        if (getIdCompteSource().equals(getIdCompteDestination())) {
            throw new Exception(getSession().getLabel(LABEL_MAPPING_COMPTE_EGAL));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement, int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_SOURCE:
                statement.writeKey(FIELD_IDCOMPTESRC,
                        _dbWriteNumeric(statement.getTransaction(), getIdCompteSource(), "idCompteSource"));
                statement.writeKey(FIELD_IDMANDATSRC,
                        _dbWriteNumeric(statement.getTransaction(), getIdMandatSource(), "idMandatSource"));
                break;
            case ALTERNATE_KEY_SOURCE_WITH_CENTRECHARGE:
                statement.writeKey(FIELD_IDCOMPTESRC,
                        _dbWriteNumeric(statement.getTransaction(), getIdCompteSource(), "idCompteSource"));
                statement.writeKey(FIELD_IDMANDATSRC,
                        _dbWriteNumeric(statement.getTransaction(), getIdMandatSource(), "idMandatSource"));
                statement.writeKey(FIELD_IDCENTRECHARGESRC,
                        _dbWriteNumeric(statement.getTransaction(), getIdCentreChargeSource(), "idCentreChargeSource"));
                break;
            case ALTERNATE_KEY_DESTINATION:
                statement.writeKey(FIELD_IDCOMPTEDEST,
                        _dbWriteNumeric(statement.getTransaction(), getIdCompteDestination(), "idCompteDestination"));
                statement.writeKey(FIELD_IDMANDATDEST,
                        _dbWriteNumeric(statement.getTransaction(), getIdMandatDestination(), "idMandatDestination"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDMAPCP,
                _dbWriteNumeric(statement.getTransaction(), getIdMapComptabiliser(), "idMapComptabiliser"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDMAPCP,
                _dbWriteNumeric(statement.getTransaction(), getIdMapComptabiliser(), "idMapComptabiliser"));

        statement.writeField(FIELD_IDCOMPTESRC,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteSource(), "idCompteSource"));
        statement.writeField(FIELD_IDMANDATSRC,
                _dbWriteNumeric(statement.getTransaction(), getIdMandatSource(), "idMandatSource"));
        statement.writeField(FIELD_IDCENTRECHARGESRC,
                _dbWriteNumeric(statement.getTransaction(), getIdCentreChargeSource(), "idCentreChargeSource"));

        statement.writeField(FIELD_IDCOMPTEDEST,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteDestination(), "idCompteDestination"));
        statement.writeField(FIELD_IDMANDATDEST,
                _dbWriteNumeric(statement.getTransaction(), getIdMandatDestination(), "idMandatDestination"));
        statement.writeField(
                FIELD_IDCONTRECRDEST,
                _dbWriteNumeric(statement.getTransaction(), getIdContreEcritureDestination(),
                        "idContreEcritureDestination"));
        statement
                .writeField(
                        FIELD_IDCENTRECHARGEDEST,
                        _dbWriteNumeric(statement.getTransaction(), getIdCentreChargeDestination(),
                                "idCentreChargeDestination"));
    }

    /**
     * @return
     */
    public String getIdCentreChargeDestination() {
        return idCentreChargeDestination;
    }

    public String getIdCentreChargeSource() {
        return idCentreChargeSource;
    }

    /**
     * @return
     */
    public String getIdCompteDestination() {
        return idCompteDestination;
    }

    /**
     * @return
     */
    public String getIdCompteSource() {
        return idCompteSource;
    }

    /**
     * @return
     */
    public String getIdContreEcritureDestination() {
        return idContreEcritureDestination;
    }

    /**
     * @return
     */
    public String getIdExterneCompteDestination() {
        return idExterneCompteDestination;
    }

    /**
     * @return
     */
    public String getIdExterneCompteSource() {
        return idExterneCompteSource;
    }

    /**
     * @return
     */
    public String getIdExterneContreEcritureDestination() {
        return idExterneContreEcritureDestination;
    }

    /**
     * @return
     */
    public String getIdMandatDestination() {
        return idMandatDestination;
    }

    /**
     * @return
     */
    public String getIdMandatSource() {
        return idMandatSource;
    }

    /**
     * @return
     */
    public String getIdMapComptabiliser() {
        return idMapComptabiliser;
    }

    /**
     * @param string
     */
    public void setIdCentreChargeDestination(String string) {
        idCentreChargeDestination = string;
    }

    public void setIdCentreChargeSource(String idCentreChargeSource) {
        this.idCentreChargeSource = idCentreChargeSource;
    }

    /**
     * @param string
     */
    public void setIdCompteDestination(String string) {
        idCompteDestination = string;
    }

    /**
     * @param string
     */
    public void setIdCompteSource(String string) {
        idCompteSource = string;
    }

    /**
     * @param string
     */
    public void setIdContreEcritureDestination(String string) {
        idContreEcritureDestination = string;
    }

    /**
     * @param string
     */
    public void setIdExterneCompteDestination(String string) {
        idExterneCompteDestination = string;
    }

    /**
     * @param string
     */
    public void setIdExterneCompteSource(String string) {
        idExterneCompteSource = string;
    }

    /**
     * @param string
     */
    public void setIdExterneContreEcritureDestination(String string) {
        idExterneContreEcritureDestination = string;
    }

    /**
     * @param string
     */
    public void setIdMandatDestination(String string) {
        idMandatDestination = string;
    }

    /**
     * @param string
     */
    public void setIdMandatSource(String string) {
        idMandatSource = string;
    }

    /**
     * @param string
     */
    public void setIdMapComptabiliser(String string) {
        idMapComptabiliser = string;
    }

}
