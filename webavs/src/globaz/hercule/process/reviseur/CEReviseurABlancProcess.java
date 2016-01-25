package globaz.hercule.process.reviseur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;

/**
 * @author MMO
 * @since 9 aout 2010
 */
public class CEReviseurABlancProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String idReviseur = "";

    /**
     * Constructeur de CEReviseurABlancProcess
     */
    public CEReviseurABlancProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws HerculeException, Exception {

        BStatement statement = null;

        try {

            statement = new BStatement(getTransaction());
            statement.createStatement();
            statement.execute(giveSqlUpdate());
            getTransaction().commit();

        } catch (Exception e) {

            getTransaction().rollback();
            setState(ERROR);
            throw new HerculeException(getSession().getLabel("REVISEUR_A_BLANC_ERREUR_EXECUTION_REQUETE_SQL"), e);

        } finally {

            if ((statement != null) && (statement.isOpened())) {
                statement.closeStatement();
            }
        }
        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted()) {
            return getSession().getLabel("EMAIL_OBJECT_REVISEUR_A_BLANC_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_REVISEUR_A_BLANC");
        }
    }

    public String getIdReviseur() {
        return idReviseur;
    }

    /**
     * Création de l'ordre sql qui permet de mettre a blanc les réviseurs.
     * 
     * @return
     */
    private String giveSqlUpdate() {

        StringBuffer buff = new StringBuffer();

        buff.append("UPDATE " + Jade.getInstance().getDefaultJdbcCollection() + CEControleEmployeur.TABLE_CECONTP);
        buff.append(" SET MDICTL = 0 WHERE (MDDEFF = 0 OR MDDEFF IS NULL )");

        if (!JadeStringUtil.isBlankOrZero(getIdReviseur())) {
            buff.append(" AND MDICTL = " + getIdReviseur());
        }

        return buff.toString();
    }

    // *******************************************************
    // Getter
    // ***************************************************

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setIdReviseur(String idReviseur) {
        this.idReviseur = idReviseur;
    }

}
