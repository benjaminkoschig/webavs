package ch.globaz.vulpecula.process.myprodis;

import globaz.framework.util.FWMessage;
import java.io.IOException;
import org.apache.http.client.fluent.Response;
import ch.globaz.vulpecula.external.BProcessWithContext;

public abstract class MyProdisProcess extends BProcessWithContext {
    private static final long serialVersionUID = 2501117935782156382L;

    public boolean checkAndLogErrorInResponse(Response response) throws IOException {
        if (response.returnResponse().getStatusLine().getStatusCode() != 200) {
            getMemoryLog().logMessage(getSession().getLabel("JSP_LIEN_MYPRODIS_ERREUR"), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
            return false;
        }
        return true;
    }
}
