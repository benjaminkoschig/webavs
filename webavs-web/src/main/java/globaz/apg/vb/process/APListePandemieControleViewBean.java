package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class APListePandemieControleViewBean extends PRAbstractViewBeanSupport {

    private String eMailAddress = "";

    @Override
    public boolean validate() {
        return true;
    }
    /**
     * getter pour l'attribut EMail address
     *
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }
    /**
     * setter pour l'attribut EMail address
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

}
