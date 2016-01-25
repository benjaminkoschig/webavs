package globaz.al.vb.radiationauto;

import globaz.al.process.dossiers.ALRadiationAutomatiqueDossiersProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.Date;

public class ALRadiationAutomatiqueDossiersViewBean extends BJadePersistentObjectViewBean {

    private String email;
    private boolean GED;
    private String periode;
    private boolean printDecisions;

    @Override
    public void add() throws Exception {
        ALRadiationAutomatiqueDossiersProcess process = new ALRadiationAutomatiqueDossiersProcess();
        process.setDateImpression(JadeDateUtil.getGlobazFormattedDate(new Date()));
        process.setPrintDecisions(printDecisions);
        process.setGED(GED);
        process.setEmail(email);
        process.setSession(getSession());
        BProcessLauncher.start(process, false);
    }

    @Override
    public void delete() throws Exception {
        // NOT IMPLEMENTED
    }

    public String getDefaultDate() {
        return JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    public String getDefaultEmail() {
        return JadeThread.currentUserEmail();
    }

    public String getEmail() {
        if (JadeStringUtil.isEmpty(email)) {
            email = JadeThread.currentUserEmail();
        }

        return email;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getPeriode() {
        if (JadeStringUtil.isBlank(periode)) {
            periode = JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3);
        }

        return periode;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public boolean isGED() {
        return GED;
    }

    public boolean isPrintDecisions() {
        return printDecisions;
    }

    @Override
    public void retrieve() throws Exception {
        // NOT IMPLEMENTED
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGED(boolean gED) {
        GED = gED;
    }

    @Override
    public void setId(String newId) {
        // NOT IMPLEMENTED
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public void setPrintDecisions(boolean printDecisions) {
        this.printDecisions = printDecisions;
    }

    @Override
    public void update() throws Exception {
        // NOT IMPLEMENTED
    }
}