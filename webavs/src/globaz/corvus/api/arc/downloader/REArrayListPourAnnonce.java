package globaz.corvus.api.arc.downloader;

import java.util.ArrayList;
import java.util.List;

public class REArrayListPourAnnonce extends ArrayList<REAnnoncesHermesMap> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean hasAnnonce1104 = false;
    private final List<String> histNoAvsAssure = new ArrayList<String>();
    private String nssForRassemblement = "";

    public List<String> getHistNoAvsAssure() {
        return histNoAvsAssure;
    }

    public String getNssForRassemblement() {
        return nssForRassemblement;
    }

    public boolean hasAnnonce1104() {
        return hasAnnonce1104;
    }

    public void setHasAnnonce1104(boolean hasAnnonce1104) {
        this.hasAnnonce1104 = hasAnnonce1104;
    }

    public void setNssForRassemblement(String nssForRassemblement) {
        this.nssForRassemblement = nssForRassemblement;
    }
}
