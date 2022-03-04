package ch.globaz.eform.web.servlet;

import ch.globaz.eform.web.application.GFApplication;
import globaz.framework.servlets.FWJadeServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class GFMainServlet extends FWJadeServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Noeuds du menu désactivés en mode droit "Lecture seule"
     */
    private static final List<String> nodesDisabledReadingMode = new ArrayList<>();

    static {
        GFMainServlet.nodesDisabledReadingMode.add("Nouveau");
    }

    /**
     *
     */
    public GFMainServlet() {
        super(GFApplication.DEFAULT_APPLICATION_EFORM, GFApplication.APPLICATION_NAME, GFApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void initializeActionMapping() {

    }

    @Override
    protected void customize(FWUrlsStack aStack) {

    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.framework.servlets.FWServlet#goHomePage(javax.servlet.http.HttpSession ,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goHomePage(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    }

    @Override
    public boolean hasLanguageInPagesPath() {
        return false;
    }
}
