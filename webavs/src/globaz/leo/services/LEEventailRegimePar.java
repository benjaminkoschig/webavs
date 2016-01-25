package globaz.leo.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;
import globaz.journalisation.db.journalisation.access.JOGroupeJournalManager;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEEnvoiListViewBean;
import globaz.leo.db.envoi.LEEnvoiViewBean;
import globaz.lupus.db.data.LUJournalDataSource;
import globaz.lupus.db.data.LUJournalDataSource.provenance;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.handler.LUJournalDefaulthandler;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class LEEventailRegimePar implements ITISummarizable {
    public static void main(String[] args) {
        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("NAOS")
                    .newSession("cicicam", "cicicam");
            LEEventailRegimePar test = new LEEventailRegimePar();
            TISummaryInfo[] s = test.getInfoForTiers("116126", session);
            for (int i = 0; i < s.length; i++) {
                System.out.println(s[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String element = "";

    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    BSession session = null;
    private String urlTitle = "";

    private String getDateReception(LEEnvoiViewBean envoi) throws Exception {
        JOGroupeJournalManager groupeManager = new JOGroupeJournalManager();
        groupeManager.setSession(envoi.getSession());
        groupeManager.setForIdGroupeJournal(envoi.getIdGroupeJournal());
        groupeManager.find();
        if (groupeManager.size() > 0) {
            // test si "dateRappel" n'est pas null
            return ((JOGroupeJournal) groupeManager.getEntity(0)).getDateReception();
        } else {
            return "";
        }
    }

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
        session = userSession;
        // préparation du filtre
        String anneePassee = String.valueOf(Integer.parseInt(JACalendar.format(JACalendar.today(),
                JACalendar.FORMAT_YYYY)) - 1);
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, anneePassee);
        //
        LEEnvoiListViewBean envoiList = new LEEnvoiListViewBean();
        envoiList.setSession(userSession);
        envoiList.setProvenance(provenanceCriteres);
        envoiList.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        envoiList.setForValeurCodeSysteme(ILEConstantes.CS_DEF_FORMULE_ATT_DS);
        envoiList.find();
        TISummaryInfo[] s = new TISummaryInfo[envoiList.size()];
        TISummaryInfo crtSum;
        for (int i = 0; i < envoiList.size(); i++) {
            crtSum = new TISummaryInfo();
            StringBuffer line = new StringBuffer();
            line.append("<table><tr><td><li></td><td>");
            line.append("</td><td>");
            LEEnvoiViewBean envoi = (LEEnvoiViewBean) envoiList.getEntity(i);
            line.append(userSession.getLabel("EVENTAIL_REGIME_ENVOI"));
            line.append("</td><td>");
            line.append(envoi.getDate());
            line.append("</td><td>");
            line.append(userSession.getLabel("EVENTAIL_REGIME_RECEPTION"));
            line.append("</td><td>");
            String dateReception = getDateReception(envoi);
            line.append(JadeStringUtil.isBlankOrZero(dateReception) ? userSession.getLabel("EVENTAIL_REGIME_PAS_RECU")
                    : dateReception);
            line.append("</td></tr></table>");
            crtSum.setText(line.toString());
            crtSum.setTitle(userSession.getLabel("EVENTAIL_REGIME_TITRE") + " " + anneePassee);
            crtSum.setUrl("/leo?userAction=leo.envoi.envoi.chercher" + getNumeroAffilie(envoi), "leo.envoi.envoi",
                    FWSecureConstants.READ);
            s[i] = crtSum;
        }
        return s;
    }

    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    private String getNumeroAffilie(LEEnvoiViewBean envoi) {
        try {
            LUJournalDefaulthandler luHandler = new LUJournalDefaulthandler();
            LUJournalDataSource list = luHandler.getProvenanceList(envoi.getIdJournalisation(), envoi.getSession(),
                    null);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    provenance provVb = list.getProvenance(i);
                    if (ILEConstantes.CS_PARAM_GEN_NUMERO.equals(provVb.getCsType())) {
                        StringBuffer s = new StringBuffer("&typeProv1=");
                        s.append(provVb.getCsType());
                        s.append("&valProv1=");
                        s.append(provVb.getValeur());
                        return s.toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public String getStyle() {
        return "";
    }

    @Override
    public String getTitle() {
        return session.getLabel("GESTION_DS");
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icone) {
        icon = icone;
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

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
