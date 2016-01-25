package globaz.corvus.process;

import globaz.corvus.itext.RERecapitulationRentes;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

/**
 * 
 * @author JJE
 */
public class REGenererRecapitulationRentesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private REDetailRecapMensuelleViewBean reDetRecMenViewBean = null;

    public REGenererRecapitulationRentesProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

    public REGenererRecapitulationRentesProcess(BProcess parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public REGenererRecapitulationRentesProcess(BSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        RERecapitulationRentes recapitulationRentes = new RERecapitulationRentes();
        recapitulationRentes.setSession(getSession());
        // BZ 5711 : titre du document dans l'email de validation
        recapitulationRentes.setFileTitle(getSession().getLabel("DOCUMENT_RECAP_RENTES_TITRE"));
        recapitulationRentes.setEMailAddress(getEMailAddress());
        recapitulationRentes.setReDetRecMenViewBean(reDetRecMenViewBean);
        recapitulationRentes.setParent(this);

        recapitulationRentes.executeProcess();

        // Tester si abort
        if (isAborted()) {
            return false;
        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("EMAIL_TITRE_RECAP_RENTES");
    }

    public REDetailRecapMensuelleViewBean getReDetRecMenViewBean() {
        return reDetRecMenViewBean;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setReDetRecMenViewBean(REDetailRecapMensuelleViewBean reDetRecMenViewBean) {
        this.reDetRecMenViewBean = reDetRecMenViewBean;
    }

}
