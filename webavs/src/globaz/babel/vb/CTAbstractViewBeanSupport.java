package globaz.babel.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cette classe est une implementation legere des interfaces {@link globaz.framework.bean.FWViewBeanInterface
 * FWViewBeanInterface} et {@link globaz.globall.db.BIPersistentObject BIPersistentObject}.
 * </p>
 * 
 * <p>
 * Cette classe trouve son utilite:
 * </p>
 * 
 * <ol>
 * <li>Dans les ecrans de {@link globaz.globall.db.BProcess BProcess}. Cette classe permet alors de creer des objets
 * plus legers que les process et qui peuvent donc sans problemes etre stockes dans la session http.</li>
 * <li>Dans les ecrans a partir desquels plusieurs entites sont modifiees. Cette classe fournit alors une alternative
 * interessante lorsque les mecanismes de {@link globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
 * _afterAdd} et consort ne peuvent être mis en oeuvre facilement.</li>
 * </ol>
 * 
 * <p>
 * Les spécificités à connaitre:
 * </p>
 * 
 * <ol>
 * <li>La plupart des methodes heritees de {@link globaz.globall.db.BIPersistentObject BIPersistentObject} se contentent
 * de lancer une {@link globaz.framework.printing.itext.exception.FWIException FWIException}. Il faut les redéfinir si
 * nécessaire.</li>
 * <li>Il n'y a pas de méthode {@link globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement) _validate} car
 * il s'agit d'une méthode protegee dans {@link globaz.globall.db.BEntity BEntity}. Un méthode publique abstraite
 * {@link #validate()} a donc été rajoutee dans cette classe. Son appel doit etre gere de maniere manuelle.</li>
 * </ol>
 * 
 * @author vre
 */
public abstract class CTAbstractViewBeanSupport implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String action = "";
    private String id = "";
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    private BISession session;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param errorLabel
     *            DOCUMENT ME!
     */
    public void _addError(String errorLabel) {
        BSession bSession = (BSession) session;

        bSession.addError(bSession.getLabel(errorLabel));
        setMsgType(FWViewBeanInterface.ERROR);
        setMessage(bSession.getErrors().toString());
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new FWIException("Impossible d'effectuer cette opération sur une instance de CTAbstractViewBeanSupport");
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new FWIException("Impossible d'effectuer cette opération sur une instance de CTAbstractViewBeanSupport");
    }

    /**
     * getter pour l'attribut action
     * 
     * @return la valeur courante de l'attribut action
     */
    public String getAction() {
        return action;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * getter pour l'attribut session
     * 
     * @return Un objet de type BSession. Peut être null si la BISession du viewBean ne peut pas être converti en
     *         BSession
     */
    public BSession getSession() {
        try {
            BSession bSession = (BSession) session;

            return bSession;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new FWIException("Impossible d'effectuer cette opération sur une instance de CTAbstractViewBeanSupport");
    }

    /**
     * setter pour l'attribut action
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAction(String string) {
        action = string;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new FWIException("Impossible d'effectuer cette opération sur une instance de CTAbstractViewBeanSupport");
    }

    /**
     * Valide les données de ce viewBean.
     * 
     * @return vrai si les données de ce viewBean sont valides.
     */
    public abstract boolean validate();
}
