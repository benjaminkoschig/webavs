package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class CATraitementQuittanceSepaProcess extends BProcess {

    /**
     * globaz.osiris.db.ordres.sepa.CATraitementQuittanceSepaProcess
     */
    private static final long serialVersionUID = -7053310867121692418L;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        SepaAcknowledgementProcessor processor = new SepaAcknowledgementProcessor();
        processor.findAndProcessAllAcknowledgements(getSession());

        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return "CATraitementQuittanceSepaProcess";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return null;
    }

}
