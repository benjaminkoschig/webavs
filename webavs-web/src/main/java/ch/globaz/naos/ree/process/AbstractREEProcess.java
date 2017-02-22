package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import java.util.Date;
import java.util.List;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.protocol.ProtocolAndMessages;
import ch.globaz.naos.ree.protocol.TechnicalProtocol;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;

public abstract class AbstractREEProcess {

    private BSession session;
    private final SedexInfo sedexInfo;
    private final InfoCaisse infoCaisse;

    private Date debutTraitement;

    public AbstractREEProcess(BSession session, SedexInfo sedexInfo, InfoCaisse infoCaisse) {
        this.session = session;
        this.sedexInfo = sedexInfo;
        this.infoCaisse = infoCaisse;
    }

    public final ProtocolAndMessages execute(final List<String> idAffilies, SedexMessageSender sedexMessageSender) {
        debutTraitement = new Date();
        ProcessProtocolAndMessages result = executeProcess(idAffilies, sedexMessageSender);
        if (result == null) {
            throw new IllegalArgumentException("executeProcess() must return a valid ProcessProtocol instance");
        }
        TechnicalProtocol technicalProtocol = new TechnicalProtocol(debutTraitement, new Date());
        return new ProtocolAndMessages(technicalProtocol, result);
    }

    /**
     * Lance le traitement du processus. </br>
     * 
     * @param idAffilies la liste des ids affiliés à traiter
     * @return le result du traitement, les messages business ainsi que le protocol du process
     */
    protected abstract ProcessProtocolAndMessages executeProcess(final List<String> idAffilies,
            SedexMessageSender sedexMessageSender);

    protected BSession getSession() {
        return session;
    }

    protected InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    protected SedexInfo getSedexInfo() {
        return sedexInfo;
    }
}
