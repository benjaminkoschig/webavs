package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormule;
import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormuleManager;
import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 7 juil. 2010
 */
public class LERappelViewBean extends ENRappel implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Code système document de la définition de formule liée au rappel */
    private String csDefFormuleRappel = new String();

    private LEFormuleViewBean formule = new LEFormuleViewBean();

    /**
     * Constructeur de LERappelViewBean
     */
    public LERappelViewBean() {
        super();
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENRappel#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        if (!JadeStringUtil.isEmpty(getIdFormule())) {
            formule.setSession(getSession());
            formule.setIdFormule(getIdFormule());
            formule.retrieve(transaction);
        }
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENRappel#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        if (!JadeStringUtil.isEmpty(getCsDefFormuleRappel())) {
            ENDefinitionFormuleManager defFormuleManager = new ENDefinitionFormuleManager();
            defFormuleManager.setForCsDocument(getCsDefFormuleRappel());
            defFormuleManager.setSession(transaction.getSession());
            defFormuleManager.find(transaction);
            if (defFormuleManager.size() > 0) {
                setIdDefinitionFormule(((ENDefinitionFormule) defFormuleManager.getEntity(0)).getIdDefinitionFormule());
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        setIdFormule(getId());
        setAlternateKey(1);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        if (!JadeStringUtil.isEmpty(getCsDefFormuleRappel())) {
            ENDefinitionFormuleManager defFormuleManager = new ENDefinitionFormuleManager();
            defFormuleManager.setForCsDocument(getCsDefFormuleRappel());
            defFormuleManager.setSession(transaction.getSession());
            defFormuleManager.find(transaction);
            if (defFormuleManager.size() > 0) {
                setIdDefinitionFormule(((ENDefinitionFormule) defFormuleManager.getEntity(0)).getIdDefinitionFormule());
            }
        } else { // Si pas renseigné on met a blanc
            setIdDefinitionFormule("");
        }
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsDefFormuleRappel() {
        return csDefFormuleRappel;
    }

    public LEFormuleViewBean getFormule() {
        return formule;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCsDefFormuleRappel(String csDefFormuleRappel) {
        this.csDefFormuleRappel = csDefFormuleRappel;
        getDefinitionFormule().setCsDocument(csDefFormuleRappel);
    }

    public void setFormule(LEFormuleViewBean formule) {
        this.formule = formule;
    }
}
