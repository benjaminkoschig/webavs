package globaz.campus.vb.annonces;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.Hashtable;

public class GEAnnoncesViewBean extends GEAnnonces implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiersEtudiant = "";
    private String nomCourt = "";
    private String numeroDomicile = "";
    private String rueDomicile = "";
    private String suffixePostalDomicile = "";

    public GEAnnoncesViewBean() {
        super();
    }

    public Boolean avecImputation() {
        Boolean avecImputation = new Boolean(false);
        try {
            GEAnnoncesManager annoncesMng = new GEAnnoncesManager();
            annoncesMng.setSession(getSession());
            annoncesMng.setForIdAnnoncesParent(getIdAnnonce());
            if (annoncesMng.getCount() > 0) {
                avecImputation = new Boolean(true);
            }
        } catch (Exception e) {
            avecImputation = new Boolean(false);
        }
        return avecImputation;
    }

    public String getIdTiersEtudiant() {
        return idTiersEtudiant;
    }

    public String getNomCourt() {
        return nomCourt;
    }

    public String getNumeroDomicile() {
        return numeroDomicile;
    }

    public String getRueDomicile() {
        return rueDomicile;
    }

    @Override
    public String getRueLegal() {
        if (!JadeStringUtil.isBlank(getRueDomicile()) || !JadeStringUtil.isBlank(getNumeroDomicile())) {
            return getRueDomicile() + " " + getNumeroDomicile();
        } else {
            return super.getRueLegal();
        }
    }

    public String getSuffixePostalDomicile() {
        return suffixePostalDomicile;
    }

    @Override
    public String getSuffixePostalLegal() {
        if (!JadeStringUtil.isBlank(getSuffixePostalDomicile())) {
            if (getSuffixePostalDomicile().length() == 1) {
                return "0" + getSuffixePostalDomicile();
            } else {
                return getSuffixePostalDomicile();
            }
        } else {
            return super.getSuffixePostalLegal();
        }
    }

    public void rechercheAdresseCourrier(String idTiers) {
        try {
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setIdTiers(idTiers);
            tiers.setSession(getSession());
            tiers.retrieve();
            if (!tiers.isNew()) {
                TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), true);
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    setAdresseEtude((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_ATTENTION));
                    setRueEtude((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
                    setNpaEtude((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                    setLocaliteEtude((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                    String npasup = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA_SUP);
                    if (Integer.parseInt(npasup) < 10) {
                        npasup = "0" + npasup;
                    }
                    setSuffixePostalEtude(npasup);
                }
            }
        } catch (Exception e) {
            getSession().addError("Recherche de l'adresse de courrier:" + e.getMessage());
        }
    }

    public void setIdTiersEtudiant(String idTiersEtudiant) {
        this.idTiersEtudiant = idTiersEtudiant;
    }

    public void setNomCourt(String nomCourt) {
        this.nomCourt = nomCourt;
    }

    public void setNumeroDomicile(String numeroDomicile) {
        this.numeroDomicile = numeroDomicile;
    }

    public void setRueDomicile(String rueDomicile) {
        this.rueDomicile = rueDomicile;
    }

    public void setSuffixePostalDomicile(String suffixePostalDomicile) {
        this.suffixePostalDomicile = suffixePostalDomicile;
    }
}
