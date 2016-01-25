/*
 * Créé le 24 mars 2010
 */
package globaz.cygnus.vb.conventions;

import globaz.cygnus.db.conventions.RFMontantsConvention;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import java.util.Vector;

/**
 * author fha
 */
public class RFRechercheMontantsConventionViewBean extends RFMontantsConvention implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient Vector<String[]> csTypeBeneficiairePcData = null;
    private transient Boolean enCours = Boolean.FALSE;

    private transient String fromDate = "";
    private transient String libelle = "";
    private transient Vector<String[]> orderBy = null;
    private transient String triePar = "";

    public RFRechercheMontantsConventionViewBean() {
        super();
    }

    @Override
    public BSpy getCreationSpy() {

        RFMontantsConvention sts = new RFMontantsConvention();

        try {
            sts = RFMontantsConvention.loadMontantConvention(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdMontant());
        } catch (Exception e) {
        }
        return sts.getCreationSpy();
    }

    public Vector<String[]> getCsTypeBeneficiairePcData() {
        return csTypeBeneficiairePcData;
    }

    public Boolean getEnCours() {
        return enCours;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getLibelle() {
        return libelle;
    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(3);
            orderBy.add(new String[] { "", "" });
            orderBy.add(new String[] { RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE,
                    getSession().getLabel("JSP_RF_SAISIE_MNT_CONV_BENEFICIAIRE") });
            orderBy.add(new String[] { RFMontantsConvention.FIELDNAME_DATE_DEBUT,
                    getSession().getLabel("JSP_RF_SAISIE_MNT_CONV_PERIODE") });
        }

        return orderBy;
    }

    @Override
    public BSpy getSpy() {

        RFMontantsConvention sts = new RFMontantsConvention();

        try {
            sts = RFMontantsConvention.loadMontantConvention(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdMontant());
        } catch (Exception e) {
        }
        return sts.getSpy();
    }

    public String getTriePar() {
        return triePar;
    }

    public void setCsTypeBeneficiairePcData(Vector<String[]> csTypeBeneficiairePcData) {
        this.csTypeBeneficiairePcData = csTypeBeneficiairePcData;
    }

    public void setEnCours(Boolean enCours) {
        this.enCours = enCours;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setTriePar(String triePar) {
        this.triePar = triePar;
    }

}
