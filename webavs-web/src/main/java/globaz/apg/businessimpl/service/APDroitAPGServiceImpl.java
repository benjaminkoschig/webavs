package globaz.apg.businessimpl.service;

import globaz.apg.business.service.APDroitAPGService;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APDroitAPGServiceImpl implements APDroitAPGService {

    /**
     * @param periodesAControler
     * @return
     */
    private List<PRPeriode> toPRPeriode(List<APPeriodeAPG> periodesAControler) {
        List<PRPeriode> periodes = new ArrayList<PRPeriode>();
        for (APPeriodeAPG p : periodesAControler) {
            periodes.add(new PRPeriode(p.getDateDebutPeriode(), p.getDateFinPeriode()));
        }
        return periodes;
    }

    @Override
    public List<PRPeriode> controlerPrestation(List<APPeriodeAPG> periodesAControler, List<APEnfantAPG> enfantsDuTiers)
            throws Exception {

        List<PRPeriode> periodes = toPRPeriode(periodesAControler);

        Map<String, PRPeriode> periodesASplitter = new HashMap<String, PRPeriode>();
        List<PRPeriode> periodesFinales = new ArrayList<PRPeriode>();

        // Pour chaque enfant, on va voir si sa date de naissance tombe dans une période
        for (APEnfantAPG enfant : enfantsDuTiers) {
            String dateDeNaissance = enfant.getDateDebutDroit();
            for (PRPeriode periode : periodes) {
                if (PRDateUtils.isDateDansLaPeriode(periode, dateDeNaissance)) {
                    periodesASplitter.put(dateDeNaissance, periode);
                } else {
                    periodesFinales.add(periode);
                }
            }
        }

        // Si il y a une ou plusieurs périodes à splitter, on supprime toutes les périodes et on les recréer dans
        // l'ordre chronologique
        if (periodesASplitter.size() > 0) {
            for (String date : periodesASplitter.keySet()) {
                PRPeriode periode = periodesASplitter.get(date);

                String dateDebutP1 = periode.getDateDeDebut();
                String dateFinP1 = JadeDateUtil.addDays(date, -1);
                String dateDebutP2 = date;
                String dateFinP2 = periode.getDateDeFin();

                // Si dateDebutP1 = 01.01.2012 et date = 01.01.2012 -> cette période vaudra 0
                if (JadeDateUtil.getNbDayBetween(dateDebutP1, dateFinP1) > 0) {
                    periodesFinales.add(new PRPeriode(dateDebutP1, dateFinP1));
                }

                if (JadeDateUtil.getNbDayBetween(dateDebutP2, dateFinP2) > 0) {
                    periodesFinales.add(new PRPeriode(dateDebutP2, dateFinP2));
                }
            }
        }
        return periodesFinales;
    }

    @Override
    public boolean isDecoupageDesPeriodesAPGNecessaire(List<APPeriodeAPG> periodesAControler,
            List<APEnfantAPG> enfantsDuTiers) throws Exception {
        // On les transforme en PRPeriode
        List<PRPeriode> periodes = toPRPeriode(periodesAControler);

        // Pour chaque enfant, on va voir si sa date de naissance tombe dans une période
        for (APEnfantAPG enfant : enfantsDuTiers) {
            String dateDeNaissance = enfant.getDateDebutDroit();
            for (PRPeriode periode : periodes) {
                if (PRDateUtils.isDateDansLaPeriode(periode, dateDeNaissance)) {
                    return true;
                }
            }
        }
        return false;
    }

}
