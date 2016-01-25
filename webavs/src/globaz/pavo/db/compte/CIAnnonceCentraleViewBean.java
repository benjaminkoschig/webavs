package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author: mmo
 */
public class CIAnnonceCentraleViewBean extends CIAnnonceCentrale implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean lectureSeule = true;
    private String moisAnneeCreation = new String();

    public String getMoisAnneeCreation() {

        String theDateCreation = super.getDateCreation();

        if (!JadeStringUtil.isBlankOrZero(theDateCreation)) {
            moisAnneeCreation = theDateCreation.substring(3);
        }

        return moisAnneeCreation;
    }

    public boolean isLectureSeule() {
        return lectureSeule;
    }

    public void setLectureSeule(boolean lectureSeule) {
        this.lectureSeule = lectureSeule;
    }

    public void setMoisAnneeCreation(String moisAnneeCreation) {
        String theDateCreation = "01." + moisAnneeCreation;
        super.setDateCreation(theDateCreation);
        super.setDateEnvoi(JadeDateUtil.getLastDateOfMonth(theDateCreation));
    }

}
