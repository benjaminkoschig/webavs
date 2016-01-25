package ch.globaz.amal.businessimpl.services.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.web.application.AMApplication;

public class AMSummary implements ITISummarizable {
    String element = "";
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    String titre = "";

    private String urlTitle = "";

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession aUserSession) throws Exception {
        try {
            BSession bSession = new BSession(AMApplication.DEFAULT_APPLICATION_AMAL);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }

        boolean hasRightAmal = aUserSession.hasRight("amal.contribuable.contribuable", FWSecureConstants.READ);

        String titleAmal = aUserSession.getLabel("VG_AMAL_TITLE");
        setTitle(titleAmal);
        setUrlTitle("");

        Object token = null;
        try {
            if (aUserSession.getCurrentThreadTransaction() != null) {
                BJadeThreadActivator.startUsingContext(aUserSession.getCurrentThreadTransaction());
                token = aUserSession.getCurrentThreadTransaction();
            } else {
                token = new Object();
                JadeThreadActivator.startUsingJdbcContext(token, initContext(aUserSession).getContext());
            }
            TISummaryInfo[] res = new TISummaryInfo[1];
            TISummaryInfo content = new TISummaryInfo();
            try {
                FamilleContribuableSearch fcSearch = new FamilleContribuableSearch();
                fcSearch.setForCodeActif(true);
                fcSearch.setForIdTier(idTiers);
                fcSearch.setOrderKey("vueGlobale");
                fcSearch = AmalServiceLocator.getFamilleContribuableService().search(fcSearch);

                String idContribuableToShow = "";

                StringBuffer s = new StringBuffer();
                if (fcSearch.getSize() > 0) {
                    FamilleContribuable fc = (FamilleContribuable) fcSearch.getSearchResults()[0];
                    String debutDroit = fc.getSimpleDetailFamille().getDebutDroit();
                    String finDroit = fc.getSimpleDetailFamille().getFinDroit();
                    if (JadeStringUtil.isBlankOrZero(finDroit)) {
                        finDroit = "12." + fc.getSimpleDetailFamille().getAnneeHistorique();
                    }

                    String dateDernierEnvoi = fc.getSimpleDetailFamille().getDateEnvoi();

                    idContribuableToShow = fc.getSimpleContribuable().getIdContribuable();

                    s.append("<table width='100%'>");
                    s.append("	<tr>");
                    s.append("		<td>");
                    s.append(aUserSession.getLabel("VG_AMAL_TEXT_PERIODE"));
                    s.append("			: " + debutDroit + " - " + finDroit);
                    s.append("		</td>");
                    s.append("	</tr>");
                    s.append("	<tr>");
                    s.append("		<td>");
                    s.append(aUserSession.getLabel("VG_AMAL_TEXT_DERNIER_ENVOI"));
                    s.append("			: " + dateDernierEnvoi);
                    s.append("		</td>");
                    s.append("	</tr>");
                    s.append("</table>");
                }

                content.setText(s.toString());

                setTitle(aUserSession.getLabel("VG_AMAL_TITLE"));
                if (hasRightAmal) {
                    if (!JadeStringUtil.isBlankOrZero(idContribuableToShow)) {
                        setUrlTitle("amal?userAction=amal.contribuable.contribuable.afficher&selectedId="
                                + idContribuableToShow);
                    } else {
                        setUrlTitle("amal?userAction=amal.contribuable.contribuable.chercher");
                    }
                }

                res[0] = content;
            } catch (Exception e) {
                StringBuffer s = new StringBuffer();
                s.append(e.getMessage());
                content.setText(s.toString());
                res[0] = content;
            }

            return res;
        } finally {
            if (token instanceof BTransaction) {
                BJadeThreadActivator.stopUsingContext((BTransaction) token);
            } else {
                JadeThreadActivator.stopUsingContext(token);
            }
        }

    }

    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    @Override
    public String getStyle() {
        return "";
    }

    @Override
    public String getTitle() {
        return titre;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
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

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    @Override
    public void setStyle(String style) {
        // Nothing
    }

    @Override
    public void setTitle(String title) {
        titre = title;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
