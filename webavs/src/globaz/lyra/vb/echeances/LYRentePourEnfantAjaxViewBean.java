package globaz.lyra.vb.echeances;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.REEnfantADiminuer;
import ch.globaz.corvus.business.models.rentesaccordees.RERenteJoinPeriodeSearchModel;
import ch.globaz.corvus.business.services.CorvusServiceLocator;

public class LYRentePourEnfantAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisTraitement;
    private List<REEnfantADiminuer> rentes;

    public LYRentePourEnfantAjaxViewBean() {
        super();

        moisTraitement = "";
        initList();
    }

    @Override
    public void find() throws Exception {
        rentes = CorvusServiceLocator.getEcheanceService().getRenteEnfantDontPeriodeFiniDansMois(moisTraitement);
    }

    public String getMoisTraitement() {
        return moisTraitement;
    }

    public JadePeriodWrapper getPeriodeChevauchantMoisTraitement(SortedSet<IREPeriodeEcheances> periodes) {
        String premierDuMoisSuivantCeluiDeTraitement = JadeDateUtil.addMonths("01." + getMoisTraitement(), 1);
        JadePeriodWrapper periodeDeComparaison = new JadePeriodWrapper(premierDuMoisSuivantCeluiDeTraitement,
                "31.12.2999");
        for (IREPeriodeEcheances unePeriode : periodes) {
            switch (unePeriode.getPeriode().comparerChevauchementMois(periodeDeComparaison)) {
                case LesPeriodesSeSuivent:
                    return unePeriode.getPeriode();
            }
        }
        return null;
    }

    public List<REEnfantADiminuer> getRentes() {
        return rentes;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return new RERenteJoinPeriodeSearchModel();
    }

    @Override
    public void initList() {
        rentes = new ArrayList<REEnfantADiminuer>();
    }

    public void setMoisTraitement(String moisTraitement) {
        this.moisTraitement = moisTraitement;
    }
}
