/**
 * 
 */
package globaz.perseus.vb.variablemetier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFVariableMetierViewBean extends BJadePersistentObjectViewBean {
    private VariableMetier variableMetier;

    public PFVariableMetierViewBean() {
        super();
        setVariableMetier(new VariableMetier());
    }

    /**
     * @param variableMetier
     */
    public PFVariableMetierViewBean(VariableMetier variableMetier) {
        super();
        setVariableMetier(variableMetier);
    }

    /**
     * @throws VariableMetierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws VariableMetierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        variableMetier = PerseusServiceLocator.getVariableMetierService().create(variableMetier);
    }

    /**
     * @throws VariableMetierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws VariableMetierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        variableMetier = PerseusServiceLocator.getVariableMetierService().delete(variableMetier);
    }

    public String getFraction() {
        return (isFraction()) ? variableMetier.getSimpleVariableMetier().getFractionNumerateur() + " / "
                + variableMetier.getSimpleVariableMetier().getFractionDenominateur() : "";
    }

    public String getFractionDenomiteurFormated() {
        return (isFraction()) ? variableMetier.getSimpleVariableMetier().getFractionDenominateur() : "";
    }

    public String getFractionNumerateurFormated() {
        return isFraction() ? variableMetier.getSimpleVariableMetier().getFractionNumerateur() : "";
    }

    @Override
    public String getId() {
        return variableMetier.getId();
    }

    public String getMontantFormated() {
        String montant = null;
        if (isMontant()) {
            montant = variableMetier.getSimpleVariableMetier().getMontant();
        }
        return (JadeStringUtil.isEmpty(montant)) ? "" : new FWCurrency(montant).toStringFormat();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (variableMetier != null) && !variableMetier.isNew() ? new BSpy(variableMetier.getSpy()) : new BSpy(
                getSession());
    }

    public String getTauxFromatted() {
        return (isTaux()) ? variableMetier.getSimpleVariableMetier().getTaux() : "";
    }

    /**
     * @return VariableMetier
     */
    public VariableMetier getVariableMetier() {
        return variableMetier;
    }

    private boolean isFraction() {
        return IPCVariableMetier.CS_FRACTION.equals(variableMetier.getSimpleVariableMetier().getTypeDeDonnee());
    }

    private boolean isMontant() {
        return IPCVariableMetier.CS_MONTANT.equals(variableMetier.getSimpleVariableMetier().getTypeDeDonnee());
    }

    private boolean isTaux() {
        return IPCVariableMetier.CS_TAUX.equals(variableMetier.getSimpleVariableMetier().getTypeDeDonnee());
    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        variableMetier = PerseusServiceLocator.getVariableMetierService().read(variableMetier.getId());
    }

    /**
     * @param idVariableMetier
     */
    @Override
    public void setId(String id) {
        variableMetier.setId(id);
    }

    /**
     * @param variableMetier
     *            the variableMetier to set
     */
    public void setVariableMetier(VariableMetier variableMetier) {
        this.variableMetier = variableMetier;

    }

    /**
     * @throws VariableMetierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws VariableMetierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        variableMetier = PerseusServiceLocator.getVariableMetierService().update(variableMetier);
    }
}
