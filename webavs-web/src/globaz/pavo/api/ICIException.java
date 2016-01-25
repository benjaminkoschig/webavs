package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;

public interface ICIException extends BIEntity {

    /**
     * ajoute un enregistrement dans la DB
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                échec critique
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    @Override
    public String getId();

    @Override
    public BISession getISession();

    @Override
    public String getLastModifiedDate();

    @Override
    public String getLastModifiedTime();

    @Override
    public String getLastModifiedUser();

    @Override
    public boolean isNew();

    public void setAffilie(String numeroAffilie);

    public void setDateEngagement(String dateEngagement);

    @Override
    public void setISession(BISession newSession);

    public void setIsJsp(String isJsp);

    public void setLangueCorrespondance(String langue);

    public void setNumeroAvs(String numeroAvs);

    /**
     * modification dans la DB
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si echec
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
