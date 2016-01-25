package globaz.hermes.print.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.db.gestion.HERappelListViewBean;
import globaz.hermes.db.gestion.HERappelViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JRExporterParameter;

public class HERappelDocument_Doc extends HEDocumentManager {

    private static final long serialVersionUID = 6929053213355830908L;

    public final static int POSITION_DETAIL = 4;

    private Iterator<HERappelViewBean> _docIterator = null;
    private HERappelViewBean caisseCourante = null;
    private Map<String, String> column = null;
    private String email = "";
    public String forCaisse = "";
    public String forDateOrdre = "";
    public String forMotif = "";
    public String forNumAVS = "";
    // ** intervalle pour laquelle la dernière liste de rappels a eu lieu
    private int intervalle = 0;
    private List<Map> list = new ArrayList<Map>();
    private Vector<HERappelViewBean> listeCaisse = new Vector<HERappelViewBean>();
    private String nbJours = "";

    private boolean processFinished = false;

    public HERappelDocument_Doc() throws Exception {
        super();
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    public void afterExecuteReport() {
        setProcessFinished(true);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        loadManager();
        _docIterator = listeCaisse.iterator();
        if (listeCaisse.size() == 1) {
            super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
        }
        setEMailAddress(getEmail());
        if (listeCaisse.size() == 0) {
            // aucun rappel a envoyer donc pas besoin d'envoyer un email
            setSendCompletionMail(false);
        }
    }

    @Override
    public void createDataSource() throws Exception {

        // Set du numéro de document INFOROM
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0011CCI");
        }

        super.setTemplateFile("HERMES_RAPPEL");
        _setLangue(caisseCourante.getLangueCaisse());

        // Renseigne les paramètres du document
        loadCatTexte(caisseCourante == null ? null : getSession().getCode(caisseCourante.getLangueCaisse()),
                HECTConstantes.CS_TYPE_LETTRE_RAPPEL, "");
        // Gestion de l'en-tête
        _handleHeaders(caisseCourante == null ? null : caisseCourante.getTiersCaisse(), "", true);

        HERappelListViewBean rappelForCaissCrt = new HERappelListViewBean();
        rappelForCaissCrt.setSession(getSession());
        // Modif jmc, bug prend tous les extraits pour envoyer les rappels
        rappelForCaissCrt.setForDate(formatDateAMJ(getDate()));
        rappelForCaissCrt.setForNumeroCaisse(caisseCourante.getNumeroCaisse());
        rappelForCaissCrt.setForMotif(getForMotif());
        if (getForNumAVS().length() != 0) {
            rappelForCaissCrt.setForNumeroAvs(getForNumAVS());
        }
        rappelForCaissCrt.find(getTransaction());
        list.clear();
        HERappelViewBean rappel;
        for (int i = 0; i < rappelForCaissCrt.size(); i++) {
            rappel = (HERappelViewBean) rappelForCaissCrt.getEntity(i);
            rappel.setLoadedFromManager(false);
            rappel.retrieve(getTransaction());
            column = new HashMap<String, String>();

            // modif NNSS
            column.put(HERappelDocument_Param.NUM_AVS, rappel.getNumeroAvs());

            column.put(HERappelDocument_Param.NAME, rappel.getNom());
            column.put(HERappelDocument_Param.MOTIF, rappel.getMotif());

            // modif NNSS
            // column.put(HERappelDocument_Param.NUM_CONJOINT,
            // JadeStringUtil.isEmpty(rappel.getNumAVSBeneficiaire())?"":AVSUtils.formatAVS8Or9(rappel.getNumAVSBeneficiaire()));
            column.put(HERappelDocument_Param.NUM_CONJOINT, JadeStringUtil.isEmpty(rappel.getNumAVSBeneficiaire()) ? ""
                    : globaz.commons.nss.NSUtil.formatAVSUnknown(rappel.getNumAVSBeneficiaire()));

            column.put(HERappelDocument_Param.DATE, rappel.getDate());
            list.add(column);
            HEAttenteRetourViewBean crtRappel = new HEAttenteRetourViewBean();
            crtRappel.setSession(getSession());
            crtRappel.setIdAttenteRetour(rappel.getIdAttenteRetour());
            crtRappel.wantCallMethodAfter(false);
            crtRappel.wantCallMethodBefore(false);
            crtRappel.wantCallValidate(false);
            crtRappel.retrieve(getTransaction());
            crtRappel.setDateDernierRappel(JACalendar.todayJJsMMsAAAA());
            crtRappel.update(getTransaction());
        }
        super.setDataSource(list);
    }

    private String formatDateAMJ(String value) {
        try {
            BigDecimal decimal = new BigDecimal(Long.parseLong(value));
            JADate date = new JADate(decimal);

            return JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (Exception e) {
            return "";
        }
    }

    private Calendar getCalendarDate() {
        if (!JadeStringUtil.isEmpty(getNbJours())) {
            // Date du jour en ms
            long current = System.currentTimeMillis();
            // nombre de jour en millisecondes
            long diff = Long.parseLong(getNbJours()) * 24 * 60 * 60 * 1000;
            // la différence en AAAAMMJJ
            long before = current - diff;
            Date d = new Date(before);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c;
        } else {
            return null;
        }
    }

    private String getDate() {
        Calendar c = getCalendarDate();
        if (c != null) {
            return DateUtils.getDateFromCalendar(c);
        } else {
            return "";
        }
    }

    /**
     * Returns the email.
     * 
     * @return String
     */
    public String getEmail() {
        return email;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("HERMES_00037");
    }

    /**
     * Returns the forCaisse.
     * 
     * @return String
     */
    public String getForCaisse() {
        return forCaisse;
    }

    /**
     * Returns the forDateOrdre.
     * 
     * @return String
     */
    public String getForDateOrdre() {
        return forDateOrdre;
    }

    /**
     * Returns the forMotif.
     * 
     * @return String
     */
    public String getForMotif() {
        return forMotif;
    }

    /**
     * Returns the forNumAVS.
     * 
     * @return String
     */
    public String getForNumAVS() {
        return forNumAVS;
    }

    /**
     * Returns the intervalle.
     * 
     * @return int
     */
    public int getIntervalle() {
        return intervalle;
    }

    /**
     * Returns the nbJours.
     * 
     * @return String
     */
    public String getNbJours() {
        return nbJours;
    }

    @Override
    public int getNbLevel() {
        return HECTConstantes.NB_LEVEL_RAPPEL;
    }

    @Override
    public int getNiveauPourDetail() {
        return HERappelDocument_Doc.POSITION_DETAIL;
    }

    private String[] getParams() throws Exception {
        String[] s = new String[2];
        s[0] = caisseCourante == null ? "" : (caisseCourante.getTiersCaisse() == null ? "" : caisseCourante
                .getTiersCaisse().getFormulePolitesse(null));
        s[1] = s[0];
        return s;
    }

    public boolean isProcessFinished() {
        return processFinished;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void loadManager() {
        BStatement statement = null;
        HERappelListViewBean listeRappels = new HERappelListViewBean();
        listeRappels.setSession(getSession());
        // Modif jmc, bug prend tous les extraits pour envoyer les rappels
        listeRappels.setForDate(formatDateAMJ(getDate()));
        listeRappels.setForNumeroCaisse(getForCaisse());
        listeRappels.setForMotif(getForMotif());
        if (getForNumAVS().length() != 0) {
            listeRappels.setForNumeroAvs(getForNumAVS());
        }
        listeRappels.setIsGroupByCaisse(true);
        try {
            listeRappels.find();

            statement = listeRappels.cursorOpen(getTransaction());
            HERappelViewBean rappel = null;
            while ((rappel = (HERappelViewBean) listeRappels.cursorReadNext(statement)) != null) {
                listeCaisse.add(rappel);
            }
            listeRappels.cursorClose(statement);
            System.out.println(DateUtils.getTimeStamp() + " " + listeCaisse.size()
                    + " caisses trouvées pour lesquelles on doit envoyer un rappel");
        } catch (Exception e) {
            super._addError("Les rappels n'ont pas pu être recherchés");
            super.setMsgType(super.ERROR);
            super.setMessage("Les rappels n'ont pas pu être recherchés");
        } finally {
            try {
                if ((statement != null) && (listeRappels != null)) {
                    listeRappels.cursorClose(statement);
                }
                // modif ADO
                statement = null;
            } catch (Exception e) {
                e.printStackTrace();
                super.setMsgType(super.ERROR);
                super.setMessage("La fermeture du curseur a générer une erreur : " + e.getMessage());
            }
        }
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext;
        if (hasNext = _docIterator.hasNext()) {
            caisseCourante = _docIterator.next();
            super.setDocumentTitle(getSession().getLabel("HERMES_10007") + " " + caisseCourante.getNumeroCaisse());
        }
        return hasNext;
    }

    @Override
    public void returnDocument() throws FWIException {
        super.returnDocument();
    }

    /**
     * Sets the email.
     * 
     * @param email
     *            The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        if (JadeStringUtil.isEmpty(value)) {
            value = " ";
        }
        switch (i) {
            case 1:
                this.setParametres(HERappelDocument_Param.CHAMP_TITRE, value);
                break;
            case 2:
                this.setParametres(HERappelDocument_Param.CHAMP_POLITESSE, format(value, getParams()));
                break;
            case 3:
                this.setParametres(HERappelDocument_Param.CHAMP_CORPS, value);
                break;
            case 5:
                this.setParametres(HERappelDocument_Param.CHAMP_CORPS2, format(value, getParams()));
                break;
            case 6:
                this.setParametres(HERappelDocument_Param.CHAMP_SIGNATURE, value);
                break;
        }
    }

    /**
     * Sets the forCaisse.
     * 
     * @param forCaisse
     *            The forCaisse to set
     */
    public void setForCaisse(String forCaisse) {
        this.forCaisse = forCaisse;
    }

    /**
     * Sets the forDateOrdre.
     * 
     * @param forDateOrdre
     *            The forDateOrdre to set
     */
    public void setForDateOrdre(String forDateOrdre) {
        this.forDateOrdre = forDateOrdre;
    }

    /**
     * Sets the forMotif.
     * 
     * @param forMotif
     *            The forMotif to set
     */
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    /**
     * Sets the forNumAVS.
     * 
     * @param forNumAVS
     *            The forNumAVS to set
     */
    public void setForNumAVS(String forNumAVS) {
        this.forNumAVS = forNumAVS;
    }

    /**
     * Sets the intervalle.
     * 
     * @param intervalle
     *            The intervalle to set
     */
    public void setIntervalle(int intervalle) {
        this.intervalle = intervalle;
    }

    /**
     * Sets the nbJours.
     * 
     * @param nbJours
     *            The nbJours to set
     */
    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    @Override
    protected void setPositionToCatTexte(String position, String value) {
        switch (Integer.parseInt(position)) {
            case 1:
                this.setParametres(HERappelDocument_Param.CHAMP_N_ASSURE, value);
                break;
            case 2:
                this.setParametres(HERappelDocument_Param.CHAMP_NOM_PRENOM, value);
                break;
            case 3:
                this.setParametres(HERappelDocument_Param.CHAMP_ASSURE_CONJOINT, value);
                break;
            case 4:
                this.setParametres(HERappelDocument_Param.CHAMP_MOTIF, value);
                break;
            case 5:
                this.setParametres(HERappelDocument_Param.CHAMP_DATE_ARC, value);
                break;
        }
    }

    public void setProcessFinished(boolean b) {
        processFinished = b;
    }

}
