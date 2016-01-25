package globaz.jsp.taglib;

import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.calendar.IJCalendarHelper;
import globaz.ij.calendar.IJJour;
import globaz.ij.calendar.IJMois;
import globaz.ij.calendar.IJSemaine;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Descpription.
 * 
 * @author scr Date de création 14 sept. 05
 */
public class IJCalendarTag extends TagSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IJMois data = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalendarTag.
     */
    public IJCalendarTag() {
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
            prepareListe();
            drawTag();
        } catch (Exception e) {
            throw new JspTagException("Error: " + e.toString());
        }

        return EVAL_PAGE;
    }

    /**
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void drawTag() throws IOException {
        if (data == null) {
            return;
        }

        write("<table width=\"100%\" border=\"1\">");
        write("<th colspan=\"8\">" + data.getLibelleMois() + "</th>");
        write("<tr>");
        write("<td></td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 0) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 1) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 2) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 3) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 4) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 5) + "</td>");
        write("<td>" + IJCalendarHelper.getShortDayName(data.getSession().getIdLangueISO(), 6) + "</td>");
        write("</tr>");

        for (Iterator iter = data.getListSemaines().iterator(); iter.hasNext();) {
            IJSemaine semaine = (IJSemaine) iter.next();

            write("<tr><td style=\"font-size: 9px;\">");
            write(semaine.getDescription() + "</td>");

            for (Iterator iterator = semaine.getListJours().iterator(); iterator.hasNext();) {
                IJJour jour = (IJJour) iterator.next();

                if (jour.isActif()) {
                    write("<td><input type=\"text\" name=\"" + IIJBaseIndemnisation.PREFIX_JOUR_CALENDRIER
                            + jour.getKey() + "\" value=\"" + jour.getValeur()
                            + "\"  style=\"width: 20px;\" maxlength=\"1\"></td>");
                } else {
                    write("<td>&nbsp;</td>");
                }
            }

            write("</tr>");
        }

        write("</table>");
    }

    /** 
     */
    public void prepareListe() {
        ;
    }

    /**
     * setter pour l'attribut data.
     * 
     * @param data
     *            une nouvelle valeur pour cet attribut
     */
    public void setData(IJMois data) {
        this.data = data;
    }

    private void write(String data) throws IOException {
        pageContext.getOut().write(data);
    }
}
