package globaz.aquila.vb.process;

import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.process.COProcessDumpConfigEtapes;
import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.jade.client.util.JadeStringUtil;
import java.rmi.RemoteException;

/**
 * <H1>Description</H1>
 * <p>
 * Un viewBean léger pour obtenir l'adresse de l'utilisateur lors du dump des requêtes SQL de configuration des étapes.
 * </p>
 * 
 * @author vre
 */
public class COProcessDumpConfigEtapesViewBean extends COAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean baseDuplique = Boolean.FALSE;
    private String csSequenceBase = COSequence.CS_SEQUENCE_AVS;
    private String email;
    private Boolean includeDelete = Boolean.FALSE;
    private Boolean recomputeIndexes = Boolean.FALSE;
    private String schema = "SCHEMA";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see COProcessDumpConfigEtapes#getBaseDuplique()
     */
    public Boolean getBaseDuplique() {
        return baseDuplique;
    }

    /**
     * @see COProcessDumpConfigEtapes#getCsSequenceBase()
     */
    public String getCsSequenceBase() {
        return csSequenceBase;
    }

    /**
     * @see COProcessDumpConfigEtapes#getEMailAddress()
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email) && (getISession() != null)) {
            try {
                email = getISession().getUserEMail();
            } catch (RemoteException e) {
                email = e.getMessage();
            }
        }

        return email;
    }

    /**
     * @see COProcessDumpConfigEtapes#getIncludeDelete()
     */
    public Boolean getIncludeDelete() {
        return includeDelete;
    }

    /**
     * @see COProcessDumpConfigEtapes#getRecomputeIndexes()
     */
    public Boolean getRecomputeIndexes() {
        return recomputeIndexes;
    }

    /**
     * @see COProcessDumpConfigEtapes#getSchema()
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @see #getBaseDuplique()
     */
    public void setBaseDuplique(Boolean baseDuplique) {
        this.baseDuplique = baseDuplique;
    }

    /**
     * @see #getCsSequenceBase()
     */
    public void setCsSequenceBase(String csSequenceBase) {
        this.csSequenceBase = csSequenceBase;
    }

    /**
     * @see #getEmail()
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see #getIncludeDelete()
     */
    public void setIncludeDelete(Boolean includeDelete) {
        this.includeDelete = includeDelete;
    }

    /**
     * @see #getRecomputeIndexes()
     */
    public void setRecomputeIndexes(Boolean recomputeIndexes) {
        this.recomputeIndexes = recomputeIndexes;
    }

    /**
     * @see #getSchema()
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }
}
