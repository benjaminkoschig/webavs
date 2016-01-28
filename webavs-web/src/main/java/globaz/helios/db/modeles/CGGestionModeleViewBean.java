package globaz.helios.db.modeles;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class CGGestionModeleViewBean extends CGGestionModele implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_ROWS = 19;
    private static final int DEFAULT_SHOW_ROWS = 2;

    private static final String PROPERTY_MAX_ROWS = "gestionEcrituresMaxRows";
    private static final String PROPERTY_SHOW_ROWS = "gestionEcrituresShowRows";

    private int showRows = 0;

    public String getCours(int i) {
        String result = "0.00";

        if (getLignes().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty(((CGLigneModeleEcriture) getLignes().get(i)).getCoursMonnaie())) {
                result = ((CGLigneModeleEcriture) getLignes().get(i)).getCoursMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getIdCompte(int i) {
        if (getLignes().size() > i) {
            return ((CGLigneModeleEcriture) getLignes().get(i)).getIdCompte();
        } else {
            return "";
        }
    }

    public String getIdCompteCharge(int i) {
        if (getLignes().size() > i) {
            return ((CGLigneModeleEcriture) getLignes().get(i)).getIdCentreCharge();
        } else {
            return "";
        }
    }

    public String getIdEcriture(int i) {
        if (getLignes().size() > i) {
            return ((CGLigneModeleEcriture) getLignes().get(i)).getIdLigneModeleEcriture();
        } else {
            return "";
        }
    }

    public String getIdExt(int i) {
        if (getLignes().size() > i) {
            if (JadeStringUtil.isBlank(((CGLigneModeleEcriture) getLignes().get(i)).getIdExterneCompte())) {
                return ((CGLigneModeleEcriture) getLignes().get(i)).getIdExterne();
            } else {
                return ((CGLigneModeleEcriture) getLignes().get(i)).getIdExterneCompte();
            }
        } else {
            return "";
        }
    }

    public String getLibelle(int i) {
        if (getLignes().size() > i) {
            String result = ((CGLigneModeleEcriture) getLignes().get(i)).getLibelle();
            result = JadeStringUtil.change(result, "\"", "&quot;");
            return result;
        } else {
            return "";
        }
    }

    public int getMaxRows() {
        try {
            return Integer.parseInt(getSession().getApplication().getProperty(PROPERTY_MAX_ROWS).trim());
        } catch (Exception e) {
            return DEFAULT_MAX_ROWS;
        }
    }

    public String getMontantCrebit(int i) {
        String result = "";
        if (i == 1) {
            result = "0.00";
        }

        if (getLignes().size() > i) {
            if (((CGLigneModeleEcriture) getLignes().get(i)).isAvoir()
                    && !JadeStringUtil.isBlank(((CGLigneModeleEcriture) getLignes().get(i)).getMontant())) {
                result = ((CGLigneModeleEcriture) getLignes().get(i)).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getMontantDebit(int i) {
        String result = "";
        if (i == 0) {
            result = "0.00";
        }

        if (getLignes().size() > i) {
            if (((CGLigneModeleEcriture) getLignes().get(i)).isDoit()
                    && !JadeStringUtil.isBlank(((CGLigneModeleEcriture) getLignes().get(i)).getMontant())) {
                result = ((CGLigneModeleEcriture) getLignes().get(i)).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getMontantEtranger(int i) {
        String result = "0.00";

        if (getLignes().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty(((CGLigneModeleEcriture) getLignes().get(i)).getMontantMonnaie())) {
                result = ((CGLigneModeleEcriture) getLignes().get(i)).getMontantMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public int getShowRows() {
        try {
            if (showRows == 0) {
                showRows = Integer.parseInt(getSession().getApplication().getProperty(PROPERTY_SHOW_ROWS).trim());
            }
        } catch (Exception e) {
            return DEFAULT_SHOW_ROWS;
        }

        return showRows;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }
}
