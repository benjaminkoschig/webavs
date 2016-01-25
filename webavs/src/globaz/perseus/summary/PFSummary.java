/**
 * 
 */
package globaz.perseus.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.List;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFSummary implements ITISummarizable {
    private String element = "";
    private String icon = "";

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private String titre = "";

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
            BSession bSession = new BSession("PERSEUS");
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        Object token = null;
        try {
            if (aUserSession.getCurrentThreadTransaction() != null) {
                BJadeThreadActivator.startUsingContext(aUserSession.getCurrentThreadTransaction());
                token = aUserSession.getCurrentThreadTransaction();
            } else {
                token = new Object();
                JadeThreadActivator.startUsingJdbcContext(token, initContext(aUserSession).getContext());
            }

            boolean hasRightDossierPC = aUserSession.hasRight("perseus.dossier.dossier", FWSecureConstants.READ);

            setTitle(aUserSession.getLabel("VG_PCF_TITLE"));
            setUrlTitle("");

            // if (hasRightDossierPC) {
            // Dossiers pc familles
            List<Dossier> listDossiersPcf = PerseusServiceLocator.getDossierService().searchDossierForTiers(idTiers,
                    false);
            // Dossiers rente-pont
            List<Dossier> listDossiersRp = PerseusServiceLocator.getDossierService().searchDossierForTiers(idTiers,
                    true);

            if ((listDossiersPcf.size() + listDossiersRp.size()) > 0) {
                TISummaryInfo[] resTab = new TISummaryInfo[listDossiersPcf.size() + listDossiersRp.size()];

                int indexTab = 0;

                String idDossier = "";
                // Pour chaque dossier pcfamilles
                for (Dossier dossier : listDossiersPcf) {
                    TISummaryInfo res = new TISummaryInfo();
                    // res.setText(dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    // .getNumAvsActuel()
                    // + " "
                    // + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    // + " " + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2());

                    res.setUrl("/perseus?userAction=perseus.demande.demande.chercher&idDossier=" + dossier.getId(),
                            "perseus.demande.demande", FWSecureConstants.READ);
                    res.setTitle(aUserSession.getLabel("VG_PCF_TEXT_DOSSIER_PC_FAMILLE"), aUserSession);

                    idDossier = dossier.getId();
                    resTab[indexTab] = res;
                    indexTab++;
                }
                // Pour chaque dossier pcfamilles
                for (Dossier dossier : listDossiersRp) {
                    TISummaryInfo res = new TISummaryInfo();
                    // res.setText(dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    // .getNumAvsActuel()
                    // + " "
                    // + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    // + " " + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2());

                    res.setUrl("/perseus?userAction=perseus.rentepont.rentePont.chercher&idDossier=" + dossier.getId(),
                            "perseus.rentepont.rentePont", FWSecureConstants.READ);
                    res.setTitle(aUserSession.getLabel("VG_PCF_TEXT_DOSSIER_RENTE_PONT"), aUserSession);

                    idDossier = dossier.getId();

                    resTab[indexTab] = res;
                    indexTab++;
                }

                if (hasRightDossierPC) {
                    setUrlTitle("perseus?userAction=perseus.dossier.dossier.chercher&idDossier=" + idDossier);
                }
                return resTab;
            } else {
                setUrlTitle("perseus?userAction=perseus.dossier.dossier.chercher&idTiers=" + idTiers);
            }

            // }
            return null;

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
