/*
 * Créé le 17 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEAvisDeces_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[0], args[1]);
            HEAvisDeces_Doc test = new HEAvisDeces_Doc();
            test.setSession(session);
            test.setIdLot("67");
            test.setEMailAddress("ald@globaz.ch");
            test.executeProcess();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private boolean archivage = false;
    private HEOutputAnnonceViewBean avisCourant = null;
    private String idLot;
    private HEOutputAnnonceListViewBean listesAnnonces;
    private String service;
    private BStatement statement;

    private void _initHeaderDoc() {
        this.setParametres(HEAvisDeces_Param.P_COMPAGNY_NAME_FR,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + "FR"));
        this.setParametres(HEAvisDeces_Param.P_COMPAGNY_NAME_DE,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + "DE"));
        this.setParametres(HEAvisDeces_Param.P_LIEU_DATE_DE,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + "DE")
                        + JACalendar.format(avisCourant.getDateAnnonce(), JACalendar.LANGUAGE_DE));
        this.setParametres(HEAvisDeces_Param.P_LIEU_DATE_FR,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + "FR")
                        + JACalendar.format(avisCourant.getDateAnnonce(), JACalendar.LANGUAGE_FR));
        this.setParametres(HEAvisDeces_Param.P_NUM_CAISSE, avisCourant.getNumeroCaisse());
        _setParametres(HEAvisDeces_Param.P_ETAT_NOM, IHEAnnoncesViewBean.ETAT_NOMINATIF);
        _setParametres(HEAvisDeces_Param.P_NUM_BASE, IHEAnnoncesViewBean.NUMERO_ASSURE);
        _setParametres(HEAvisDeces_Param.P_DATE_NAISSANCE, IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA);
        _setParametres(HEAvisDeces_Param.P_PAYS_ORIGINE, IHEAnnoncesViewBean.CS_LIEU_ORIGINE);
        _setParametres(HEAvisDeces_Param.P_DOMICILE, IHEAnnoncesViewBean.CS_DOMICILE);
        _setParametres(HEAvisDeces_Param.P_ANNEE_DECES, IHEAnnoncesViewBean.CS_DATE_DECES);
    }

    private List _initListAssure() throws Exception {
        List list = new ArrayList();
        HEOutputAnnonceListViewBean arcs5202 = new HEOutputAnnonceListViewBean();
        arcs5202.setSession(getSession());
        arcs5202.setForRefUnique(avisCourant.getRefUnique());
        arcs5202.setForIdLot(avisCourant.getIdLot());
        arcs5202.setForCodeApplication("52");
        arcs5202.setCodeApplication3839(false);
        arcs5202.setForCodeEnregistrement("02");
        arcs5202.find(getTransaction());
        Map column;
        HEOutputAnnonceViewBean crt;
        for (int i = 0; i < arcs5202.size(); i++) {
            column = new HashMap();
            crt = (HEOutputAnnonceViewBean) arcs5202.getEntity(i);
            column.put(
                    HEAvisDeces_Param.COL_NUM_CAISSE,
                    crt.getField(IHEAnnoncesViewBean.CS_NUM_CAISSE_PAIE_PRESTATION) + "."
                            + crt.getField(IHEAnnoncesViewBean.CS_NUM_AGENCE_PAIE_PRESTATION));

            // Modif NNSS
            // column.put(HEAvisDeces_Param.COL_NUM_ASSURE_NOM,AVSUtils.formatAVS8Or9(crt.getField(HEAnnoncesViewBean.CS_NUM_ASSURE_AYANT_DROIT_PRESTATION))+"/"+crt.getField(HEAnnoncesViewBean.ETAT_NOMINATIF));
            column.put(
                    HEAvisDeces_Param.COL_NUM_ASSURE_NOM,
                    globaz.commons.nss.NSUtil.formatAVSUnknown(crt
                            .getField(IHEAnnoncesViewBean.CS_NUM_ASSURE_AYANT_DROIT_PRESTATION))
                            + "/"
                            + crt.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));

            // modif NNSS
            // column.put(HEAvisDeces_Param.COL_1ER_NUM_ASSURE,
            // JAUtil.isStringEmpty(crt.getField(HEAnnoncesViewBean.CS_PREM_NUM_ASSURE_COMPL))?"":AVSUtils.formatAVS8Or9(crt.getField(HEAnnoncesViewBean.CS_PREM_NUM_ASSURE_COMPL)));
            column.put(
                    HEAvisDeces_Param.COL_1ER_NUM_ASSURE,
                    JAUtil.isStringEmpty(crt.getField(IHEAnnoncesViewBean.CS_PREM_NUM_ASSURE_COMPL)) ? ""
                            : globaz.commons.nss.NSUtil.formatAVSUnknown(crt
                                    .getField(IHEAnnoncesViewBean.CS_PREM_NUM_ASSURE_COMPL)));

            column.put(HEAvisDeces_Param.COL_GENRE_PRESTATION,
                    crt.getField(IHEAnnoncesViewBean.CS_CHIFFRE_CLE_GENRE_PRESTATION));
            column.put(HEAvisDeces_Param.COL_COMMENTAIRE,
                    getLibelleToCode(crt.getField(IHEAnnoncesViewBean.CS_COMMENTAIRE)));
            list.add(column);
        }
        // si jamais aucun 5202, protéger la génération du document
        if (arcs5202.size() == 0) {
            column = new HashMap();
            list.add(column);
        }
        return list;
    }

    /**
     * @param string
     * @param string2
     */
    private void _setParametres(String param, String valeurAnnonce) {
        try {
            if (HEAnnoncesViewBean.isNumeroAVS(valeurAnnonce)) {
                this.setParametres(param, NSUtil.formatAVSUnknown(avisCourant.getField(valeurAnnonce)));
            } else if (HEAnnoncesViewBean.isDate_MMAA(valeurAnnonce)) {
                this.setParametres(param,
                        DateUtils.convertDate(avisCourant.getField(valeurAnnonce), DateUtils.MMAA, DateUtils.MMAA_DOTS));
            } else if (IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA.equals(valeurAnnonce)) {
                this.setParametres(param, DateUtils.convertDate(avisCourant.getField(valeurAnnonce),
                        DateUtils.JJMMAAAA, DateUtils.JJMMAAAA_DOTS));
            } else {
                this.setParametres(param, avisCourant.getField(valeurAnnonce));
            }
        } catch (Exception e) {
            this.setParametres(param, e.getMessage());
        }
    }

    private void _setTemplate() {
        super.setTemplateFile(HEAvisDeces_Param.TEMPLATE_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterExecuteReport ()
     */
    @Override
    public void afterExecuteReport() {
        try {
            listesAnnonces.cursorClose(statement);
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "HEAvisDeces_Doc");
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        listesAnnonces = new HEOutputAnnonceListViewBean();
        listesAnnonces.setSession(getSession());
        listesAnnonces.setForIdLot(idLot);
        listesAnnonces.setForCodeApplication("52");
        listesAnnonces.setCodeApplication3839(false);
        listesAnnonces.setForCodeEnregistrement("01");
        try {
            statement = listesAnnonces.cursorOpen(getTransaction());
            if (listesAnnonces.getCount() == 1) {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            }
        } catch (Exception e) {
            super._addError(e.getMessage());
            super.setMsgType(super.ERROR);
            super.setMessage(e.getMessage());
            getTransaction().addErrors(e.getMessage());
        }
    }

    @Override
    public void createDataSource() throws Exception {
        // Set du numéro de document INFOROM
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0158CCI");
        }
        _setTemplate();
        _initHeaderDoc();
        super.setDataSource(_initListAssure());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        String email;
        if (!JadeStringUtil.isBlank(getService())) {
            email = getSession().getLabel("HERMES_10028") + "/" + getService();
        } else {
            email = getSession().getLabel("HERMES_10028");
        }
        return email;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    public String getLibelleToCode(String code) {
        try {
            FWParametersSystemCodeManager codeCommentaires = new FWParametersSystemCodeManager();
            codeCommentaires.setSession(getSession());
            codeCommentaires.setForIdGroupe("AVISDECCOM");
            codeCommentaires.setForIdTypeCode("11100017");
            codeCommentaires.setForActif(Boolean.TRUE);
            codeCommentaires.setForCodeUtilisateur(code);
            codeCommentaires.find(1);
            if (codeCommentaires.size() > 0) {
                FWParametersSystemCode crt = (FWParametersSystemCode) codeCommentaires.getFirstEntity();
                return crt.getCodeUtilisateur("D").getLibelle() + "\n" + crt.getCodeUtilisateur("F").getLibelle();
            }
            return code;
        } catch (Exception e) {
            return code;
        }
    }

    public String getService() {
        return service;
    }

    /**
     * @return
     */
    public boolean isArchivage() {
        return archivage;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        try {
            avisCourant = (HEOutputAnnonceViewBean) listesAnnonces.cursorReadNext(statement);
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "HEAvisDeces_Doc");
        }
        if (avisCourant != null) {
            try {
                super.setDocumentTitle(NSUtil.formatAVSUnknown(avisCourant.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))
                        + "/" + avisCourant.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
            } catch (Exception e) {
                super.setDocumentTitle(e.getMessage());
            }
            hasNext = true;
        }
        return hasNext;
    }

    /**
     * @param b
     */
    public void setArchivage(boolean b) {
        archivage = b;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    public void setService(String service) {
        this.service = service;
    }

}
