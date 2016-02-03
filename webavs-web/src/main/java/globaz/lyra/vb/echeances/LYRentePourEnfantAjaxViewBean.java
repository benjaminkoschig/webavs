package globaz.lyra.vb.echeances;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import ch.globaz.common.properties.CommonProperties;
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

        /**
         * Si la propriété est à true, on va enrichir les données REEnfantADiminuer avec le code de la commune politique
         * et trier les données
         */
        if (CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue()) {
            manageCommunePolitique();
        }
    }

    private void manageCommunePolitique() throws ParseException {
        String dateString = "01." + moisTraitement;
        dateString = JadeDateUtil.getLastDateOfMonth(dateString);
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateString);

        Set<String> setIdTiers = new HashSet<String>();

        for (REEnfantADiminuer rente : rentes) {
            setIdTiers.add(rente.getIdTiers());
        }

        BSession session = BSessionUtil.getSessionFromThreadContext();

        Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date, session);

        for (REEnfantADiminuer rente : rentes) {
            rente.setCommunePolitique(mapCommuneParIdTiers.get(rente.getIdTiers()));
        }
        // ------------------------------
        Collections.sort(rentes, new Comparator<REEnfantADiminuer>() {

            @Override
            public int compare(REEnfantADiminuer o1, REEnfantADiminuer o2) {

                int value = o1.getCommunePolitique().compareTo(o2.getCommunePolitique());
                if (value != 0) {
                    return value;
                }

                String nom1 = JadeStringUtil.convertSpecialChars(o1.getNomTiers());
                String nom2 = JadeStringUtil.convertSpecialChars(o2.getNomTiers());
                value = nom1.compareTo(nom2);

                if (value != 0) {
                    return value;
                }

                String prenom1 = JadeStringUtil.convertSpecialChars(o1.getPrenomTiers());
                String prenom2 = JadeStringUtil.convertSpecialChars(o2.getPrenomTiers());

                return prenom1.compareTo(prenom2);

            }
        });
        System.out.println();
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
