/*
 * Créé le 24 oct. 07
 */
package globaz.ij.vb.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author bsc
 */
public class IJBaseIndemnisationAitAaViewBean extends IJBaseIndemnisationViewBean implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean wantValidateNbJoursAit = true;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _validate(BStatement statement) throws Exception {

        super._validate(statement);

        if (wantValidateNbJoursAit) {
            IJPrononce pr = loadPrononce(statement.getTransaction());

            // pour les AIT,
            if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(pr.getCsTypeIJ())) {

                // ////////////////////////////////////////////////////////////////////
                // le nombre de jours est limite a 180 par année
                // ////////////////////////////////////////////////////////////////////
                IJBaseIndemnisationManager biManager = new IJBaseIndemnisationManager();
                biManager.setSession(getSession());
                biManager.setForIdPrononce(getIdPrononce());
                // la base d'ind courrante ne doit pas etre consideree
                if (!isNew()) {
                    biManager.setNotForIdBaseIndemnisation(getIdBaseIndemisation());
                }
                // les bases corrigees ne sont pas considerees
                biManager.setNotForCsEtat(IIJBaseIndemnisation.CS_ANNULE);
                // pour l'annee de la date de debut de la BI
                int annee = new JADate(getDateDebutPeriodeEtendue()).getYear();
                biManager.setForIsActiveDuringPeriode("01.01." + annee, "31.12." + annee);

                // pour les AIT et Alloc. Ass le nombre de jours est dans les
                // jours internes
                BigDecimal nbJours = biManager.getSum(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERNE);

                if ((nbJours.add(new BigDecimal(getNombreJoursInterne())).compareTo(new BigDecimal(180))) > 0) {

                    _addError(statement.getTransaction(), getSession().getLabel("JSP_NB_JOURS_MAX_POUR_AIT"));
                    setWantValidateNbJoursAit(false);
                }
            }
        }
    }

    public void setWantValidateNbJoursAit(boolean wantValidateNbJoursAit) {
        this.wantValidateNbJoursAit = wantValidateNbJoursAit;
    }

    public boolean wantValidateNbJoursAit() {
        return wantValidateNbJoursAit;
    }

}
