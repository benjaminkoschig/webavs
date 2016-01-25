package globaz.ij.vb.annonces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJAnnonceManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;

/**
 * @author DVH
 */
public class IJAnnonceViewBean extends IJAnnonce implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean deuxiemePeriode = false;
    private String idTiers = "";

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getNombreJoursPeriode1() {
        return getPeriodeAnnonce1().getNombreJours();
    }

    public String getNombreJoursPeriode2() {
        return getPeriodeAnnonce2().getNombreJours();
    }

    public String getTauxJournalierPeriode1() {
        return getPeriodeAnnonce1().getTauxJournalier();
    }

    public String getTauxJournalierPeriode2() {
        return getPeriodeAnnonce2().getTauxJournalier();
    }

    public boolean isDeuxiemePeriode() {
        return deuxiemePeriode;
    }

    /**
     * Si une annonce enfant dans l'état "Erronee" existe pour une annonce en état "Envoyee", interdire le bouton
     * "Modifier".
     * 
     * Dans les autres cas on peut modifier
     * 
     * @return
     */
    public boolean isModifiable() {

        if (IIJAnnonce.CS_OUVERT.equals(getCsEtat()) || IIJAnnonce.CS_VALIDE.equals(getCsEtat())
                || IIJAnnonce.CS_ERRONEE.equals(getCsEtat())) {

            return true;

        } else if (IIJAnnonce.CS_ENVOYEE.equals(getCsEtat())) {

            IJAnnonceManager aManager = new IJAnnonceManager();
            aManager.setSession(getSession());
            aManager.setForIdParent(getIdAnnonce());
            aManager.setForCsEtat(IIJAnnonce.CS_ERRONEE);
            try {
                return aManager.getCount() == 0;

            } catch (Exception e) {
                // on ne fait rien
            }
        }
        return false;
    }

    public String isSupplementReadaptation() {
        return getParamSpecifique3emeRevisionSur5Positions().substring(4, 5).trim();
    }

    /**
     * on affichera la 2eme periode si elle n'est pas nouvelle (donc si elle existe), ou si la periode1 est nouvelle
     * (donc si on est en train de faire une nouvelle annonce)
     */
    public boolean needToAfficher2EmePeriode() {
        return getPeriodeAnnonce1().isNew() || !getPeriodeAnnonce2().isNew();
    }

    public void setDeuxiemePeriode(boolean deuxiemePeriode) {
        this.deuxiemePeriode = deuxiemePeriode;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNombreJoursPeriode1(String nbJours) {
        getPeriodeAnnonce1().setNombreJours(nbJours);
    }

    public void setNombreJoursPeriode2(String nbJours) {
        getPeriodeAnnonce2().setNombreJours(nbJours);
    }

    public void setSupplementReadaptation(String supplementReadaptation) {
        if (JadeStringUtil.isBlank(supplementReadaptation)) {
            supplementReadaptation = " ";
        }
        setParamSpecifique3emeRevisionSur5Positions(PRStringUtils.replaceStringIn(
                getParamSpecifique3emeRevisionSur5Positions(), 4, 5, supplementReadaptation));
    }

    public void setTauxJournalierPeriode1(String tauxJournalier) {
        getPeriodeAnnonce1().setTauxJournalier(tauxJournalier);
    }

    public void setTauxJournalierPeriode2(String tauxJournalier) {
        getPeriodeAnnonce2().setTauxJournalier(tauxJournalier);
    }
}
