package globaz.al.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * classe implémentant les méthodes pour intégré les attributs affilié dans la vue globale
 * 
 * @author GMO
 * 
 */
public class ALAttributAffilieSummary implements ITISummarizable {
    String element = "";
    String icon = "";

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    private String urlTitle = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#getElement()
     */
    @Override
    public String getElement() {
        return element;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#getIcon()
     */
    @Override
    public String getIcon() {
        return icon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITISummarizable#getInfoForTiers(java.lang.String, globaz.globall.db.BSession)
     */
    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {

        JadeThreadContext threadContext = initContext(userSession);

        JadeThreadActivator.startUsingJdbcContext(this, threadContext.getContext());

        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForIdTiers(idTiers);
        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
        TISummaryInfo[] res = new TISummaryInfo[searchModel.getSize()];
        for (int i = 0; i < searchModel.getSize(); i++) {
            res[i] = handleAffiliationAF((AffiliationSimpleModel) searchModel.getSearchResults()[i], userSession);
        }
        JadeThreadActivator.stopUsingContext(this);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#getMaxHorizontalItems()
     */
    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#getStyle()
     */
    @Override
    public String getStyle() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#getTitle()
     */
    @Override
    public String getTitle() {
        return "Allocations Familiales";
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    /**
     * Formate les informations de l'affiliation qu'on veut afficher dans la vue globale
     * 
     * @param affiliation
     *            affiliation dont les infos sont affichées
     * @param userSession
     *            la session utilisateur
     * @return TISummaryInfo les informations formattées et affichables dans la vue globale
     * @throws Exception
     */
    private TISummaryInfo handleAffiliationAF(AffiliationSimpleModel affiliation, BSession userSession)
            throws Exception {
        TISummaryInfo crtSum = new TISummaryInfo();
        StringBuffer s = new StringBuffer();
        s.append("<table><tr><td><li>");
        s.append("Attributs AF de l'affiliation AF :" + affiliation.getRaisonSociale());
        s.append("</li></td></tr></table>");
        crtSum.setText(s.toString());
        crtSum.setTitle("Attributs affiliation AF", userSession);

        crtSum.setUrl("/al?userAction=al.attribut.affilie.afficher&selectedId=" + affiliation.getAffiliationId(),
                "al.attribut.affilie", FWSecureConstants.READ);
        return crtSum;
    }

    /**
     * Initialise un threadContexte pour pouvoir utiliser la nouvelle persistence depuis la vue globale
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#setElement(java.lang.String)
     */
    @Override
    public void setElement(String element) {
        this.element = element;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#setIcon(java.lang.String)
     */
    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#setStyle(java.lang.String)
     */
    @Override
    public void setStyle(String style) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.summary.ITIBaseSummarizable#setTitle(java.lang.String)
     */
    @Override
    public void setTitle(String arg0) {

    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
