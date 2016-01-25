package globaz.ij.acor.adapter.plat;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * 
 * @Deprecated. Le calcul des prestations se fait désormais sans ACOR !!!
 */
public class IJACORBaseIndemnisationAdapter extends IJACORPrononceAdapter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IJBaseIndemnisation baseIndemnisation;
    private List<PRFichierACORPrinter> fichiers;
    private IJIJCalculee ijCalculee;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJACORBaseIndemnisationAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     */
    public IJACORBaseIndemnisationAdapter(BSession session, IJPrononce prononce, IJBaseIndemnisation baseIndemnisation,
            IJIJCalculee ijCalculee) {
        super(session, prononce);
        this.baseIndemnisation = baseIndemnisation;
        this.ijCalculee = ijCalculee;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut base indemnistation.
     * 
     * @return la valeur courante de l'attribut base indemnistation
     */
    public IJBaseIndemnisation getBaseIndemnistation() {
        return baseIndemnisation;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.ij.acor.adapter.plat.IJACORPrononceAdapter#getDateDebutDroit()
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    @Override
    public String getDateDebutDroit() {
        return getIjCalculee().getDateDebutDroit();
    }

    /**
     * getter pour l'attribut date determinante.
     * 
     * @return la valeur courante de l'attribut date determinante
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String getDateDeterminante() throws PRACORException {

        try {
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getIjCalculee().getDateDebutDroit(),
                    baseIndemnisation.getDateDebutPeriode())) {
                return baseIndemnisation.getDateDebutPeriode();
            } else {
                return getIjCalculee().getDateDebutDroit();
            }
        } catch (PRACORException e1) {
            throw e1;
        } catch (Exception e2) {
            e2.printStackTrace();
            return baseIndemnisation.getDateDebutPeriode();
        }

    }

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List<PRFichierACORPrinter> getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList<PRFichierACORPrinter>();
            fichiers.add(fichierDemandeDefautPrinter());

            // bz-NEW_ACOR_IJ
            // this.fichiers.add(new IJFichierAssuresPrinter(this, IJACORPrononceAdapter.NF_ASSURES_IJ));
            fichiers.add(new IJFichierAssuresPrinter(this, PRACORConst.NF_ASSURES));
            fichiers.add(new IJFichierIJPrinter(this, IJACORPrononceAdapter.NF_IJ));
            fichiers.add(new IJFichierFamillePrinter(this, PRACORConst.NF_FAMILLES));
            fichiers.add(new IJFichierEnfantPrinter(this, PRACORConst.NF_ENFANTS));
            fichiers.add(new IJFichierPeriodePrinter(this, PRACORConst.NF_PERIODES));
            fichiers.add(fichierDemGEDOPrinter());
            // bz-NEW_ACOR_IJ
            // this.fichiers.add(new IJFichierRevenuPrinter(this, IJACORPrononceAdapter.NF_REVENU));
            // this.fichiers.add(new IJFichierIJPrinter(this, IJACORPrononceAdapter.NF_IJ));
            // this.fichiers.add(new IJFichierDecomptePrinter(this, IJACORPrononceAdapter.NF_DECOMPTE));
            fichiers.add(new IJFichierRenteEnCours(this, PRACORConst.NF_RENTES));
            fichiers.add(new IJFichierEuroFormPrinter(this, PRACORConst.NF_EURO_FORM));
        }

        return fichiers;
    }

    /**
     * getter pour l'attribut ij calculee.
     * 
     * @return la valeur courante de l'attribut ij calculee
     */
    public IJIJCalculee getIjCalculee() {
        return ijCalculee;
    }
}
