package ch.globaz.pegasus.businessimpl.services.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.models.summary.SummaryPca;
import ch.globaz.pegasus.business.models.summary.SummaryPcaSearch;
import ch.globaz.pegasus.business.models.summary.SummaryRente;
import ch.globaz.pegasus.business.models.summary.SummaryRenteSearch;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.query.Queries;
import ch.globaz.pegasus.web.application.PCApplication;

public class PCSummaryPca implements ITISummarizable {
    private BSession aUserSession;
    private String element = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private String titre = "";
    private String urlTitle = "";

    public void addInfosInTitle(SummaryPca pca) {
        StringBuilder param = new StringBuilder();
        param.append(".chercher&cocheCasFamille=true&idDossier=");
        param.append(pca.getIdDossier());
        param.append("&dossierSearch.likeNss=");
        param.append(pca.getNss().substring(4));
        titre = createHref(IPCActions.ACTION_DOSSIER, param, "VG_PC_TITLE").toString();
    }

    public StringBuilder createHref(String action, StringBuilder param, String content) {
        StringBuilder str = new StringBuilder();
        boolean hasRight = hasRight(action);

        if (hasRight) {
            str.append("<a href='#' onclick=\"callLocalUrl('/pegasus?userAction=");
            str.append(action);
            str.append(param);
            str.append("')\" >");
            str.append(aUserSession.getLabel(content));
            str.append("</a>");
        } else {
            str.append("<span>");
            str.append(aUserSession.getLabel(content));
            str.append("</span>");
        }
        return str;
    }

    public List<SummaryRente> findRentes(SummaryPca pca) throws JadePersistenceException {
        SummaryRenteSearch search = new SummaryRenteSearch();
        search.setForIdDroit(pca.getIdDroit());
        search.setForIdTiers(pca.getIdTiers());
        search.setForDateValable(pca.getDateDebutPca());
        search.setInCsTypeDonneeFinancierer(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add("64007001");
                this.add("64007003");
            }
        });
        List<SummaryRente> list = PersistenceUtil.search(search, search.whichModelClass());
        return list;
    }

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession aUserSession) throws Exception {
        try {
            BSession bSession = new BSession(PCApplication.DEFAULT_APPLICATION_PEGASUS);
        } catch (Exception ex) {
            JadeLogger.info(this, "The module PC [pegasus] seems to no exist");
        }
        TISummaryInfo summaryInfo = new TISummaryInfo();
        Object token = null;
        this.aUserSession = aUserSession;
        try {
            token = PersistenceUtil.startUsingContex(aUserSession);
            List<SummaryPca> list = searchPca(idTiers);
            // if ((list.size() > 0) && this.hasRight(IPCActions.ACTION_DOSSIER)) {
            if (list.size() > 0) {
                SummaryPca pca = list.get(0);
                if (pca != null) {
                    List<SummaryRente> listRentes = findRentes(pca);
                    addInfosInTitle(pca);
                    summaryInfo.setText(render(pca, listRentes));
                } else {
                    // this.titre = aUserSession.getLabel("VG_PC_TITLE");
                    StringBuilder param = new StringBuilder();
                    param.append(".chercher");
                    titre = createHref(IPCActions.ACTION_DOSSIER, param, "VG_PC_TITLE").toString();
                }
            } else {
                StringBuilder param = new StringBuilder();
                param.append(".chercher");
                titre = createHref(IPCActions.ACTION_DOSSIER, param, "VG_PC_TITLE").toString();
            }
        } catch (JadePersistenceException jpe) {
            throw new Exception(jpe);
        } catch (Exception e) {
            titre = aUserSession.getLabel("VG_PC_TITLE");
            return new TISummaryInfo[] { summaryInfo };
        } finally {
            if (token instanceof BTransaction) {
                BJadeThreadActivator.stopUsingContext((BTransaction) token);
            } else {
                JadeThreadActivator.stopUsingContext(token);
            }
        }
        return new TISummaryInfo[] { summaryInfo };
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
        return null;
    }

    @Override
    public String getTitle() {
        return titre;
    }

    public String getTitre() {
        return titre;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public boolean hasRight(String action) {
        boolean hasRight = BSessionUtil.getSessionFromThreadContext().hasRight(action, FWSecureConstants.READ);
        return hasRight;
    }

    public String render(SummaryPca pca, List<SummaryRente> listRente) {

        StringBuilder str = new StringBuilder();

        String domainePC = null;

        if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(pca.getCsTypePca())) {
            domainePC = "AI";
        } else if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(pca.getCsTypePca())
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(pca.getCsTypePca())) {
            domainePC = "AVS";
        }

        StringBuilder codeRente = new StringBuilder();
        for (SummaryRente rente : listRente) {
            String csCode = rente.getCsApi();
            if (JadeStringUtil.isEmpty(csCode)) {
                csCode = rente.getCsRenteAvsAi();
            }
            // if (codeRente.indexOf(this.aUserSession.getCode(csCode)) == -1) {
            codeRente.append(aUserSession.getCode(csCode));
            codeRente.append(", ");
            // }
        }
        codeRente = new StringBuilder(codeRente.substring(0, codeRente.length() - 2));
        StringBuilder param = new StringBuilder();
        param.append(".chercher&whereKey=forVersionnedPca&idDossier=");
        param.append(pca.getIdDossier());

        str.append("<div>");
        str.append(createHref(IPCActions.ACTION_PCACCORDEES_LIST, param, "JSP_SUMMARY_PC_PERIODE"));
        str.append(":<span style='padding-left:5px'>" + pca.getDateDebutPca() + " - " + pca.getDateFinPca() + "</span>");
        str.append("</div>");

        str.append("<div style='margin:5px 0px'>");
        str.append(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_SUMMARY_PC_GENRE"));
        str.append(": ");
        str.append(domainePC);
        str.append(" ");
        str.append(codeRente);
        str.append("<span style='padding-left:30px'>");
        str.append(BSessionUtil.getSessionFromThreadContext().getCodeLibelle(pca.getCsGenrePca()));
        str.append("</span>");
        str.append("</div");
        return str.toString();
    }

    private List<SummaryPca> searchPca(String idTiers) throws JadePersistenceException, SQLException {
        SummaryPcaSearch search = new SummaryPcaSearch();
        String sql = Queries.getQuery("summary");
        search.setForIdTiers(idTiers);
        search.setForDateValable(JadePersistenceUtil.parseDateToSql(JACalendar.todayJJsMMsAAAA()).substring(0, 6));
        search.setForCsEtatPca(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        List<SummaryPca> list = PersistenceUtil.executeQuery(sql, search, SummaryPca.class); // PersistenceUtil.search(search,
        return list;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icon) {
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

    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
