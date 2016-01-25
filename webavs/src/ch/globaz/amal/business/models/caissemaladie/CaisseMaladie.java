/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.Comparator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * @author cbu
 * 
 */
public class CaisseMaladie extends JadeComplexModel implements Comparable<CaisseMaladie> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum CaisseMaladieComparator implements Comparator<CaisseMaladie> {

        ID_SORT {
            @Override
            public int compare(CaisseMaladie cm1, CaisseMaladie cm2) {
                return Integer.valueOf(cm1.getNumCaisse()).compareTo(Integer.valueOf(cm2.getNumCaisse()));
            }
        },
        NAME_SORT {
            @Override
            public int compare(CaisseMaladie cm1, CaisseMaladie cm2) {
                return cm1.getNomCaisse().toUpperCase().compareTo(cm2.getNomCaisse().toUpperCase());
            }
        };

        public static Comparator<CaisseMaladie> decending(final Comparator<CaisseMaladie> other) {
            return new Comparator<CaisseMaladie>() {
                @Override
                public int compare(CaisseMaladie cm1, CaisseMaladie cm2) {
                    return -1 * other.compare(cm1, cm2);
                }
            };
        }

        public static Comparator<CaisseMaladie> getComparator(final CaisseMaladieComparator... multipleOptions) {
            return new Comparator<CaisseMaladie>() {
                @Override
                public int compare(CaisseMaladie cm1, CaisseMaladie cm2) {
                    for (CaisseMaladieComparator option : multipleOptions) {
                        int result = option.compare(cm1, cm2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return 0;
                }
            };
        }

    }

    private AdministrationComplexModel administrationComplexModel = new AdministrationComplexModel();
    private AdministrationComplexModel administrationComplexModelGroupes = new AdministrationComplexModel();
    private String dateFinCaisse = null;
    private String idTiersCaisse = null;
    private String idTiersGroupe = null;
    private String inactif = null;
    private String nomCaisse = null;
    private String nomGroupe = null;
    private String numCaisse = null;
    private String numGroupe = null;
    private String sedexYear = null;
    // private SimpleDetailCaisseMaladie simpleDetailCaisseMaladie = new SimpleDetailCaisseMaladie();
    private String typeAdmin = null;
    private String typeLien = null;

    public CaisseMaladie() {
        super();
        administrationComplexModel = new AdministrationComplexModel();
        administrationComplexModelGroupes = new AdministrationComplexModel();
    }

    @Override
    public int compareTo(CaisseMaladie cm) {
        int n1 = Integer.parseInt(cm.getNumCaisse());
        int n2 = Integer.parseInt(numCaisse);

        if (n1 > n2) {
            return -1;
        } else if (n1 == n2) {
            return 0;
        } else {
            return 1;
        }
    }

    public AdministrationComplexModel getAdministrationComplexModel() {
        return administrationComplexModel;
    }

    public AdministrationComplexModel getAdministrationComplexModelGroupes() {
        return administrationComplexModelGroupes;
    }

    public String getDateFinCaisse() {
        return dateFinCaisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return administrationComplexModel.getId();
    }

    public String getIdTiersCaisse() {
        return idTiersCaisse;
    }

    public String getIdTiersGroupe() {
        return idTiersGroupe;
    }

    public String getInactif() {
        return inactif;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    public String getNumGroupe() {
        return numGroupe;
    }

    // public SimpleDetailCaisseMaladie getSimpleDetailCaisseMaladie() {
    // return this.simpleDetailCaisseMaladie;
    // }

    public String getSedexYear() {
        return sedexYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return administrationComplexModel.getSpy();
    }

    public String getTypeAdmin() {
        return typeAdmin;
    }

    public String getTypeLien() {
        return typeLien;
    }

    public void setAdministrationComplexModel(AdministrationComplexModel administrationComplexModel) {
        this.administrationComplexModel = administrationComplexModel;
    }

    public void setAdministrationComplexModelGroupes(AdministrationComplexModel administrationComplexModelGroupes) {
        this.administrationComplexModelGroupes = administrationComplexModelGroupes;
    }

    public void setDateFinCaisse(String dateFinCaisse) {
        this.dateFinCaisse = dateFinCaisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        administrationComplexModel.setId(id);
    }

    public void setIdTiersCaisse(String idTiersCaisse) {
        this.idTiersCaisse = idTiersCaisse;
    }

    public void setIdTiersGroupe(String idTiersGroupe) {
        this.idTiersGroupe = idTiersGroupe;
    }

    public void setInactif(String inactif) {
        this.inactif = inactif;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    // public void setSimpleDetailCaisseMaladie(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie) {
    // this.simpleDetailCaisseMaladie = simpleDetailCaisseMaladie;
    // }

    public void setNumGroupe(String numGroupe) {
        this.numGroupe = numGroupe;
    }

    public void setSedexYear(String sedexYear) {
        this.sedexYear = sedexYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        administrationComplexModel.setSpy(spy);
    }

    public void setTypeAdmin(String typeAdmin) {
        this.typeAdmin = typeAdmin;
    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }
}
