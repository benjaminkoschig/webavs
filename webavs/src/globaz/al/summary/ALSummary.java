package globaz.al.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.SortedMap;
import java.util.TreeMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.web.application.ALApplication;

public class ALSummary implements ITISummarizable {
    String element = "";
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private BSession session = null;
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
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        try {
            BSession bSession = new BSession(ALApplication.DEFAULT_APPLICATION_WEBAF);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        Object token = null;
        setSession(userSession);

        urlTitle = "";

        try {

            if (userSession.getCurrentThreadTransaction() != null) {
                BJadeThreadActivator.startUsingContext(userSession.getCurrentThreadTransaction());
                token = userSession.getCurrentThreadTransaction();
            } else {
                token = new Object();
                JadeThreadActivator.startUsingJdbcContext(token, initContext(userSession).getContext());
            }

            boolean hasRightAL = userSession.hasRight("al.dossier", FWSecureConstants.READ);

            DossierComplexSearchModel searchModel = new DossierComplexSearchModel();
            searchModel.setForIdTiersAllocataire(idTiers);
            searchModel.setWhereKey("dossiersForIdTiers");
            searchModel = ALServiceLocator.getDossierComplexModelService().search(searchModel);

            SortedMap<Integer, DossierComplexModel> mapDossiers = new TreeMap<Integer, DossierComplexModel>();
            // Parcours de tout les dossiers pour retrouver les dossier en état "Actif"
            for (int i = 0; i < searchModel.getSize(); i++) {
                DossierComplexModel d = (DossierComplexModel) searchModel.getSearchResults()[i];
                if (ALCSDossier.ETAT_ACTIF.equals(d.getDossierModel().getEtatDossier())) {
                    JADate jaDate = new JADate(d.getDossierModel().getDebutValidite());
                    mapDossiers.put(new Integer(jaDate.toStrAMJ()), d);
                }
            }

            if (mapDossiers.size() == 0) {
                // Parcours de tout les dossiers pour retrouver les dossier en état "Suspendu"
                for (int i = 0; i < searchModel.getSize(); i++) {
                    DossierComplexModel d = (DossierComplexModel) searchModel.getSearchResults()[i];
                    if (ALCSDossier.ETAT_SUSPENDU.equals(d.getDossierModel().getEtatDossier())) {
                        JADate jaDate = new JADate(d.getDossierModel().getDebutValidite());
                        mapDossiers.put(new Integer(jaDate.toStrAMJ()), d);
                    }
                }
            }

            if (mapDossiers.size() == 0) {
                // Parcours de tout les dossiers pour retrouver les dossier en état "Radié"
                for (int i = 0; i < searchModel.getSize(); i++) {
                    DossierComplexModel d = (DossierComplexModel) searchModel.getSearchResults()[i];
                    if (ALCSDossier.ETAT_RADIE.equals(d.getDossierModel().getEtatDossier())) {
                        JADate jaDate = new JADate(d.getDossierModel().getFinValidite());
                        mapDossiers.put(new Integer(jaDate.toStrAMJ()), d);
                    }
                }
            }

            if (mapDossiers.size() == 0) {
                // Parcours de tout les dossiers pour retrouver les dossier en état "Refusé"
                for (int i = 0; i < searchModel.getSize(); i++) {
                    DossierComplexModel d = (DossierComplexModel) searchModel.getSearchResults()[i];
                    if (ALCSDossier.ETAT_REFUSE.equals(d.getDossierModel().getEtatDossier())) {
                        JADate jaDate = new JADate(d.getDossierModel().getDebutValidite());
                        mapDossiers.put(new Integer(jaDate.toStrAMJ()), d);
                    }
                }
            }

            if (mapDossiers.size() == 0) {
                // En désespoir de cause, on prend le dernier dossier, peu import le type
                for (int i = 0; i < searchModel.getSize(); i++) {
                    DossierComplexModel d = (DossierComplexModel) searchModel.getSearchResults()[0];
                    Integer i_date = 299991231;
                    if (JadeDateUtil.isGlobazDate(d.getDossierModel().getDebutValidite())) {
                        JADate jaDate = new JADate(d.getDossierModel().getDebutValidite());
                        i_date = new Integer(jaDate.toStrAMJ());
                    } else if (JadeDateUtil.isGlobazDate(d.getDossierModel().getFinValidite())) {
                        JADate jaDate = new JADate(d.getDossierModel().getFinValidite());
                        i_date = new Integer(jaDate.toStrAMJ());
                    } else {
                        // Si aucune date, on se sert de l'idDossier
                        i_date = new Integer(d.getDossierModel().getIdDossier());
                    }
                    mapDossiers.put(i_date, d);
                }
            }

            TISummaryInfo[] res = new TISummaryInfo[1];
            if (mapDossiers.size() == 0) {
                res[0] = handleDossierAF(new DossierComplexModel(), userSession);
            } else {
                res[0] = handleDossierAF(mapDossiers.get(mapDossiers.lastKey()), userSession);
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

    public BSession getSession() {
        return session;
    }

    @Override
    public String getStyle() {
        return "";
    }

    @Override
    public String getTitle() {
        return getSession().getLabel("VG_AF_TITLE");
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
    private TISummaryInfo handleDossierAF(DossierComplexModel dossier, BSession userSession) throws Exception {
        boolean hasDroitDossier = userSession.hasRight("al.dossier.dossier", FWSecureConstants.READ);

        TISummaryInfo crtSum = new TISummaryInfo();
        if (!dossier.isNew()) {
            StringBuffer s = new StringBuffer();
            s.append("<table>");
            s.append("<tr><td>");

            s.append(userSession.getLabel("VG_AF_TEXT_ETAT"));
            s.append(" : ");
            s.append(userSession.getCodeLibelle(dossier.getDossierModel().getEtatDossier()));
            if (ALCSDossier.ETAT_RADIE.equals(dossier.getDossierModel().getEtatDossier())) {
                s.append(" (" + dossier.getDossierModel().getFinValidite() + ")");
            }
            s.append("</td></tr>");
            s.append("<tr><td>");
            s.append(userSession.getLabel("VG_AF_TEXT_ACTIVITE"));
            s.append(" : ");
            s.append(userSession.getCodeLibelle(dossier.getDossierModel().getActiviteAllocataire()));

            s.append("</td></tr>");
            s.append("</table>");

            crtSum.setText(s.toString());

            if (hasDroitDossier) {
                setUrlTitle("al?userAction=al.dossier.dossierMain.afficher&selectedId="
                        + dossier.getDossierModel().getIdDossier());
            }
        } else {
            if (hasDroitDossier) {
                setUrlTitle("al?userAction=al.dossier.dossier.chercher");
            }
        }
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

    public void setSession(BSession session) {
        this.session = session;
    }

    @Override
    public void setStyle(String style) {

    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
