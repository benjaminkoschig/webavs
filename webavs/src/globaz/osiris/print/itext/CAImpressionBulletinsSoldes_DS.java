package globaz.osiris.print.itext;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CASection;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAImpressionBulletinsSoldes_DS implements JRDataSource {
    int index = 0;
    String isoLangueTiers;
    String libelleCourant;
    double montantCompensation = 0;
    CASection section;
    BSession session;
    Double valeurCourante;

    /**
     * Constructor for CAImpressionBulletinsSoldes_DS.
     */
    public CAImpressionBulletinsSoldes_DS(CASection newSection, BSession session, String isoLangueTiers) {
        super();
        section = newSection;
        this.session = session;
        this.isoLangueTiers = isoLangueTiers;
    }

    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(JRField)
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        if (jrField.getName().equals("COL_1")) {
            return libelleCourant;
        }
        if (jrField.getName().equals("COL_6")) {
            return valeurCourante;
        }
        return null;
    }

    /**
     * Returns the montantCompensation.
     * 
     * @return double
     */
    public double getMontantCompensation() {
        return montantCompensation;
    }

    /**
     * Returns the section.
     * 
     * @return CASection
     */
    public CASection getSection() {
        return section;
    }

    public String getSolde() {
        FWCurrency soldeSection = new FWCurrency(section.getSolde());
        soldeSection.sub(montantCompensation);
        return soldeSection.toStringFormat();
    }

    /**
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() {
        try {
            if (index == 0) {
                libelleCourant = session.getApplication().getLabel("CACMONTANTINIT", isoLangueTiers);
                valeurCourante = new Double(new FWCurrency(section.getBase()).doubleValue());
                if (valeurCourante.doubleValue() == 0) {
                    index++;
                }
            }
            if (index == 1) {
                libelleCourant = session.getApplication().getLabel("CACPAIEMENTCOMP", isoLangueTiers);
                // Prendre le Paiement/compensation de la section courante et
                // lui ajouter le montant négatif provenant de musca
                valeurCourante = new Double(new FWCurrency(section.getPmtCmp()).doubleValue()
                        + (montantCompensation * -1));
                if (valeurCourante.doubleValue() == 0) {
                    index++;
                }
            }
            if (index == 2) {
                libelleCourant = session.getApplication().getLabel("CACTAXESSOMM", isoLangueTiers);
                valeurCourante = new Double(new FWCurrency(section.getTaxes()).doubleValue());
                if (valeurCourante.doubleValue() == 0) {
                    index++;
                }
            }
            if (index == 3) {
                libelleCourant = session.getApplication().getLabel("CACFRAIS", isoLangueTiers);
                double tot = new FWCurrency(section.getFrais()).doubleValue();
                tot += new FWCurrency(section.getAmende()).doubleValue();
                valeurCourante = new Double(tot);

                // valeurCourante = valeurCourante. Double(new
                // FWCurrency(section.getAmende()).doubleValue());

                if (valeurCourante.doubleValue() == 0) {
                    index++;
                }
            }
            if (index == 4) {
                libelleCourant = session.getApplication().getLabel("CACINTERET", isoLangueTiers);
                valeurCourante = new Double(new FWCurrency(section.getInterets()).doubleValue());
                if (valeurCourante.doubleValue() == 0) {
                    index++;
                }
            }
            if (index == 5) {
                return false;
            }
            index++;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the montantCompensation.
     * 
     * @param montantCompensation
     *            The montantCompensation to set
     */
    public void setMontantCompensation(double montantCompensation) {
        this.montantCompensation = montantCompensation;
    }

}
