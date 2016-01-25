package globaz.hermes.db.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.io.Serializable;

public class HEConfigurationServiceViewBean extends BEntity implements FWViewBeanInterface, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String colonneSelection;
    private String emailAdresse = new String();
    private String idService = new String();
    private String referenceInterne = new String();
    private String serviceName = new String();

    private final String tableName = "HESEARC";

    public HEConfigurationServiceViewBean() {
        super();
    }

    public HEConfigurationServiceViewBean(BSession session) {
        this();
        setSession(session);
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdService(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

    }

    @Override
    protected String _getTableName() {
        return tableName;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idService = statement.dbReadNumeric("RDIDSE");
        serviceName = statement.dbReadString("RDSERV");
        referenceInterne = statement.dbReadString("RDREFI");
        emailAdresse = statement.dbReadString("RDMAIL");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RDIDSE", _dbWriteNumeric(statement.getTransaction(), getIdService(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RDIDSE", _dbWriteNumeric(statement.getTransaction(), getIdService(), "idService"));
        statement.writeField("RDSERV", _dbWriteString(statement.getTransaction(), getServiceName(), "serviceName"));
        statement.writeField("RDREFI",
                _dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField("RDMAIL", _dbWriteString(statement.getTransaction(), getEmailAdresse(), "emailAdresse"));
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdService() {
        return idService;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setColonneSelection(String newColonneSelection) {
        colonneSelection = newColonneSelection;
    }

    public void setEmailAdresse(String newEmailAdresse) {
        emailAdresse = newEmailAdresse;
    }

    public void setIdService(String newIdService) {
        idService = newIdService;
    }

    public void setReferenceInterne(String newReferenceInterne) {
        referenceInterne = newReferenceInterne;
    }

    public void setServiceName(String newServiceName) {
        serviceName = newServiceName;
    }

}
