package globaz.orion.vb.partnerWeb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

public class EBListeDesSalairesViewBean extends EBAbstractViewBean {
    private String email;
    private Boolean genererEtapesRappel = false;
    private String forDateReference = JACalendar.todayJJsMMsAAAA();
    private Boolean forIsSimulation = true;
    private String forDateImpression = JACalendar.todayJJsMMsAAAA();
    private Boolean displayGenererEtapesRappel = false;

    public String getForDateImpression() {
        return forDateImpression;
    }

    public void setForDateImpression(String forDateImpression) {
        this.forDateImpression = forDateImpression;
    }

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public EBListeDesSalairesViewBean() {
        super();
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            try {
                email = getISession().getUserEMail();
            } catch (Exception e) {
                setMessage("Impossible de trouver l'adresse e-mail de l'utilisateur");
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    /**
     * setter pour l'attribut email
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String string) {
        email = string;
    }

    public String getForDateReference() {
        return forDateReference;
    }

    public void setForDateReference(String forDateReference) {
        this.forDateReference = forDateReference;
    }

    public Boolean getForIsSimulation() {
        return forIsSimulation;
    }

    public void setForIsSimulation(Boolean forIsSimulation) {
        this.forIsSimulation = forIsSimulation;
    }

    public Boolean getGenererEtapesRappel() {
        return genererEtapesRappel;
    }

    public void setGenererEtapesRappel(Boolean genererEtapesRappel) {
        this.genererEtapesRappel = genererEtapesRappel;
    }

    public Boolean getDisplayGenererEtapesRappel() {
        return displayGenererEtapesRappel;
    }

    public void setDisplayGenererEtapesRappel(Boolean displayGenererEtapesRappel) {
        this.displayGenererEtapesRappel = displayGenererEtapesRappel;
    }

}
