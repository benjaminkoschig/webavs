package globaz.naos.db.avisMutation;

/**
 * Insérez la description du type ici. Date de création : (31.05.2002 09:49:17)
 * 
 * @author: Administrator
 */
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import java.io.ByteArrayOutputStream;

public class AFAvisMutationViewBean extends AFAvisMutation implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String message;
    private String msgType;
    private ByteArrayOutputStream outImpression;

    /**
     * Commentaire relatif au constructeur AFAssuranceViewBean.
     */
    public AFAvisMutationViewBean() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getDateAvis() {
        if (JadeStringUtil.isEmpty(getDateAnnonce())) {
            // date vide, date du jour ou début d'affiliation si > date du jour
            String today = JACalendar.todayJJsMMsAAAA();
            try {
                if (BSessionUtil.compareDateFirstGreater(getSession(), getAffiliation().getDateDebut(), today)) {
                    setDateAnnonce(getAffiliation().getDateDebut());
                } else if (!JadeStringUtil.isEmpty(getAffiliation().getDateFin())
                        && BSessionUtil.compareDateFirstLower(getSession(), getAffiliation().getDateFin(), today)) {
                    setDateAnnonce(getAffiliation().getDateFin());
                } else {
                    setDateAnnonce(today);
                }
            } catch (Exception ex) {
                setDateAnnonce(today);
            }
        }
        return getDateAnnonce();
    }

    /**
     * Returns the outImpression.
     * 
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream getOutImpression() {
        return outImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Sets the outImpression.
     * 
     * @param outImpression
     *            The outImpression to set
     */
    public void setOutImpression(ByteArrayOutputStream outImpression) {
        this.outImpression = outImpression;
    }

}
