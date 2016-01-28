package globaz.naos.db.taxeCo2;

import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

public class AFTaxeCo2ViewBean extends AFTaxeCo2 implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeMasse = new String();
    private String anneeRedistri = new String();
    private String email = new String();
    private String masseT = new String();
    private String pageName = new String();
    private Boolean reinitialiser = new Boolean(false);

    /**
     * Constructeur de AFAffiliation
     */
    public AFTaxeCo2ViewBean() {
        super();
    }

    @Override
    public String getAnneeMasse() {
        if (JadeStringUtil.isEmpty(anneeMasse)) {
            if (JadeStringUtil.isEmpty(super.anneeMasse)) {
                return "" + (JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA().substring(6)) - 2);
            } else {
                return super.anneeMasse;
            }
        }
        return anneeMasse;
    }

    public String getAnneeMasseFiger() {
        if (JadeStringUtil.isEmpty(anneeMasse)) {
            if (JadeStringUtil.isEmpty(super.anneeMasse)) {
                return "" + (JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA().substring(6)) - 1);
            } else {
                return super.anneeMasse;
            }
        }
        return anneeMasse;
    }

    public String getAnneeRedistri() {
        if (JadeStringUtil.isEmpty(anneeRedistri)) {
            if (JadeStringUtil.isEmpty(super.anneeRedistribution)) {
                return "" + (JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA().substring(6)));
            } else {
                return super.anneeRedistribution;
            }
        }
        return anneeRedistri;
    }

    public String getAnneeRedistriFiger() {
        if (JadeStringUtil.isEmpty(anneeRedistri)) {
            if (JadeStringUtil.isEmpty(super.anneeRedistribution)) {
                return "" + (JadeStringUtil.toInt(JACalendar.todayJJsMMsAAAA().substring(6)) + 1);
            } else {
                return super.anneeRedistribution;
            }
        }
        return anneeRedistri;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    @Override
    protected BManager getManager() {
        return new AFTaxeCo2Manager();
    }

    public String getMasseT() {
        return JANumberFormatter.fmt(masseT.toString(), true, true, false, 2);
    }

    public String getPageName() {
        return pageName;
    }

    public Boolean getReinitialiser() {
        return reinitialiser;
    }

    @Override
    public void setAnneeMasse(String anneeMasse) {
        this.anneeMasse = anneeMasse;
    }

    public void setAnneeRedistri(String anneeRedistri) {
        this.anneeRedistri = anneeRedistri;
        setAnneeRedistribution(anneeRedistri);
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public void setMasseT(String masseT) {
        this.masseT = masseT;
    }

    public void setMasseTotal(String anneeMasse) {
        BigDecimal masseTotale = new BigDecimal(0);
        AFTaxeCo2Manager mana = new AFTaxeCo2Manager();
        mana.setSession(getSession());
        mana.setForAnneeMasse(anneeMasse);
        try {
            mana.find();
            if (mana.size() > 0) {
                masseTotale = mana.getSum(AFTaxeCo2.FIELDNAME_MASSE);
            }
            setMasseT(masseTotale.toString());
        } catch (Exception e) {

        }

    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setReinitialiser(Boolean reinitialiser) {
        this.reinitialiser = reinitialiser;
    }

}
