package globaz.prestation.jsp.taglib;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.IOException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Descpription.
 * 
 * @author scr
 */
public class PRDisplayRequerantInfoTag extends TagSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int STYLE_CONDENSED = 1;
    public final static int STYLE_CONDENSED_ASSURE = 5;
    public final static int STYLE_CONDENSED_BEN = 2;
    public final static int STYLE_CONDENSED_WITHOUT_LABEL = 4;
    public final static int STYLE_LONG_WITHOUT_LABEL = 6;
    public final static int STYLE_SPLITTED = 3;

    private String idTiers = null;
    private BSession session = null;
    private int style = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalendarTag.
     */
    public PRDisplayRequerantInfoTag() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     * 
     * @throws javax.servlet.jsp.JspTagException
     *             DOCUMENT ME!
     * @throws JspTagException
     *             DOCUMENT ME!
     */
    @Override
    public int doEndTag() throws javax.servlet.jsp.JspTagException {
        try {
            PRTiersWrapper tv = prepareListe();
            drawTag(tv, style);
        } catch (Exception e) {
            throw new JspTagException("Error: " + e.toString());
        }

        return Tag.EVAL_PAGE;
    }

    /**
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void drawTag(PRTiersWrapper tw, int style) throws IOException {

        int iStyle = style;

        if (tw == null) {

            switch (iStyle) {
                case STYLE_CONDENSED:
                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_NSS_REQUERANT")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"70\" readonly class=\"disabled\" value=\""
                            + " " + session.getLabel("JSP_PRDISPLAY_TEXTE_REQUERANT_NON_DEFINI") + " " + "\">");
                    break;

                case STYLE_CONDENSED_BEN:
                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_BENEFICIAIRE")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"70\" readonly class=\"disabled\" value=\""
                            + " " + session.getLabel("JSP_PRDISPLAY_TEXTE_BENEFICIAIRE_NON_DEFINI") + " " + "\">");
                    break;

                case STYLE_CONDENSED_ASSURE:

                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_ASSURE")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"70\" readonly class=\"disabled\" value=\""
                            + " " + session.getLabel("JSP_PRDISPLAY_TEXTE_ASSURE_NON_DEFINI") + " " + "\">");
                    break;

                case STYLE_CONDENSED_WITHOUT_LABEL:
                    write(" <input type=\"text\" name=\"descriptif\" size=\"80\" readonly class=\"disabled\" value=\""
                            + " " + session.getLabel("JSP_PRDISPLAY_TEXTE_TIERS_NON_DEFINI") + " " + "\">");
                    break;

                case STYLE_SPLITTED:
                    write(session.getLabel("JSP_PRDISPLAY_TEXTE_NON_IMPLEMENTE"));
                    break;

                case STYLE_LONG_WITHOUT_LABEL:
                    write(" <input type=\"text\" name=\"descriptif\" size=\"70\" readonly class=\"disabled\" value=\""
                            + " " + session.getLabel("JSP_PRDISPLAY_TEXTE_ASSURE_NON_DEFINI") + " " + "\">");
                    break;
            }

        } else {

            switch (iStyle) {
                case STYLE_CONDENSED:
                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_NSS_REQUERANT")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"80\" readonly class=\"disabled\" value=\""
                            + NSUtil.formatAVSUnknown(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE), session) + " / "
                            + getLibellePays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session) + "\">");
                    break;

                case STYLE_CONDENSED_BEN:
                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_BENEFICIAIRE")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"80\" readonly class=\"disabled\" value=\""
                            + NSUtil.formatAVSUnknown(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE), session) + " / "
                            + getLibellePays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session) + "\">");
                    break;

                case STYLE_CONDENSED_ASSURE:
                    write("<b>"
                            + session.getLabel("JSP_PRDISPLAY_TITRE_ASSURE")
                            + "</b> &nbsp;&nbsp;&nbsp;&nbsp;"
                            + " <input type=\"text\" name=\"descriptif\" size=\"80\" readonly class=\"disabled\" value=\""
                            + NSUtil.formatAVSUnknown(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE), session) + " / "
                            + getLibellePays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session) + "\">");
                    break;

                case STYLE_CONDENSED_WITHOUT_LABEL:
                    write(" <input type=\"text\" name=\"descriptif\" size=\"80\" readonly class=\"disabled\" value=\""
                            + NSUtil.formatAVSUnknown(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE), session) + " / "
                            + getLibellePays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session) + "\">");
                    break;

                case STYLE_LONG_WITHOUT_LABEL:
                    write(" <input type=\"text\" name=\"descriptif\" size=\"100\" readonly class=\"disabled\" value=\""
                            + NSUtil.formatAVSUnknown(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE), session) + " / "
                            + getLibellePays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session) + "\">");
                    break;

                case STYLE_SPLITTED:
                    write(session.getLabel("JSP_PRDISPLAY_TEXTE_NON_IMPLEMENTE"));
                    break;

            }

        }

    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe, BSession session) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite, BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
        }

    }

    /** 
     */
    public PRTiersWrapper prepareListe() throws Exception {
        // Récupération des info du requérant, à partir de son idTiers
        PRTiersWrapper tw = PRTiersHelper.getTiersById(session, idTiers);
        return tw;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param i
     */
    public void setStyle(int i) {
        style = i;
    }

    private void write(String data) throws IOException {
        pageContext.getOut().write(data);
    }

}
