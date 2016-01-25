/*
 * Créé le 3 jan. 07
 */
package globaz.corvus.vb.annonces;

import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public abstract class REAbstractAnnonceHeaderProxyViewBean implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private REAnnonceHeader annonce;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REAbstractAnnonceHeaderProxyViewBean.
     * 
     * @param prononce
     *            DOCUMENT ME!
     */
    protected REAbstractAnnonceHeaderProxyViewBean(REAnnonceHeader annonce) {
        this.annonce = annonce;
    }

    // ~ Methodes
    // ---------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        annonce.add();
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BEntity#add(BTransaction)
     */
    public void add(BTransaction transaction) throws Exception {
        annonce.add(transaction);
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        annonce.delete();
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BEntity#delete(BTransaction)
     */
    public void delete(BTransaction transaction) throws Exception {
        annonce.delete(transaction);
    }

    /**
     * @return the annonce
     */
    public REAnnonceHeader getAnnonce() {
        return annonce;
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getCodeApplication()
     */
    public String getCodeApplication() {
        return annonce.getCodeApplication();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getCodeEnregistrement01()
     */
    public String getCodeEnregistrement01() {
        return annonce.getCodeEnregistrement01();
    }

    /**
     * @return
     * @see globaz.globall.db.BEntity#getCollection()
     */
    public String getCollection() {
        return annonce.getCollection();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getEtat()
     */
    public String getEtat() {
        return annonce.getEtat();
    }

    /**
     * @return
     * @see globaz.globall.api.BIEntity#getId()
     */
    @Override
    public String getId() {
        return annonce.getId();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getIdAnnonce()
     */
    public String getIdAnnonce() {
        return annonce.getIdAnnonce();
    }

    /**
     * @return
     * @see globaz.globall.api.BIEntity#getISession()
     */
    @Override
    public BISession getISession() {
        return annonce.getISession();
    }

    /**
     * @return
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return annonce.getMessage();
    }

    /**
     * @return
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return annonce.getMsgType();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getNumeroAgence()
     */
    public String getNumeroAgence() {
        return annonce.getNumeroAgence();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getNumeroCaisse()
     */
    public String getNumeroCaisse() {
        return annonce.getNumeroCaisse();
    }

    /**
     * @return
     * @see globaz.globall.db.BEntity#getSpy()
     */
    public BSpy getSpy() {
        return annonce.getSpy();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#hasSpy()
     */
    public boolean hasSpy() {
        return annonce.hasSpy();
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        annonce.retrieve();
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BEntity#retrieve(BTransaction)
     */
    public void retrieve(BTransaction transaction) throws Exception {
        annonce.retrieve(transaction);
    }

    /**
     * @param codeApplication
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setCodeApplication(java.lang.String)
     */
    public void setCodeApplication(String codeApplication) {
        annonce.setCodeApplication(codeApplication);
    }

    /**
     * @param codeEnregistrement01
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setCodeEnregistrement01(java.lang.String)
     */
    public void setCodeEnregistrement01(String codeEnregistrement01) {
        annonce.setCodeEnregistrement01(codeEnregistrement01);
    }

    /**
     * @param etat
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setEtat(java.lang.String)
     */
    public void setEtat(String etat) {
        annonce.setEtat(etat);
    }

    /**
     * @param newId
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        annonce.setId(newId);
    }

    /**
     * @param idAnnonce
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setIdAnnonce(java.lang.String)
     */
    public void setIdAnnonce(String idAnnonce) {
        annonce.setIdAnnonce(idAnnonce);
    }

    /**
     * @param newSession
     * @see globaz.globall.api.BIEntity#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        annonce.setISession(newSession);
    }

    /**
     * @param message
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        annonce.setMessage(message);
    }

    /**
     * @param msgType
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        annonce.setMsgType(msgType);
    }

    /**
     * @param numeroAgence
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setNumeroAgence(java.lang.String)
     */
    public void setNumeroAgence(String numeroAgence) {
        annonce.setNumeroAgence(numeroAgence);
    }

    /**
     * @param numeroCaisse
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setNumeroCaisse(java.lang.String)
     */
    public void setNumeroCaisse(String numeroCaisse) {
        annonce.setNumeroCaisse(numeroCaisse);
    }

    /**
     * @param newSession
     * @see globaz.globall.db.BEntity#setSession(globaz.globall.db.BSession)
     */
    public void setSession(BSession newSession) {
        annonce.setSession(newSession);
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        annonce.update();
    }

    /**
     * @throws Exception
     * @see globaz.globall.db.BEntity#update(BTransaction)
     */
    public void update(BTransaction transaction) throws Exception {
        annonce.update(transaction);
    }

    /**
     * @return DOCUMENT ME!
     */
    public boolean validate() {
        return true;
    }

    /**
     * @param newCallValidate
     *            DOCUMENT ME!
     */
    public void wantCallValidate(boolean newCallValidate) {
        annonce.wantCallValidate(newCallValidate);
    }

}
