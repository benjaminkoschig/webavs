package ch.globaz.cygnus.businessimpl.services;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.globall.db.BManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.cygnus.business.services.RegimeLoader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.Regimes;

public class RegimeLoaderImpl implements RegimeLoader {

    @Override
    public Map<String, Regimes> loadRegimes(Map<String, MembresFamilles> mapMembreFamille) {
        String[] listSousTypeSoins = new String[] { IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME,
                IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE };

        Map<String, Regimes> regimesByIdDroit = new HashMap<String, Regimes>();
        List<String> idsTiers = getListIdsTiers(mapMembreFamille);

        if (idsTiers.size() > 0) {
            RFPrestationAccordeeJointREPrestationAccordeeManager mgr = new RFPrestationAccordeeJointREPrestationAccordeeManager();
            try {
                mgr.setForIdTiersBeneFiciairesIn(idsTiers);
                mgr.setForGenrePrestaAccordee(IREPrestationAccordee.CS_GENRE_RFM);
                mgr.setForCsSourceRfmAccordee(listSousTypeSoins);
                mgr.setForIsDateFinDroitNull(true);
                mgr.changeManagerSize(BManager.SIZE_NOLIMIT);

                mgr.find();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!mgr.isEmpty()) {
                Iterator<RFPrestationAccordeeJointREPrestationAccordee> itr = mgr.iterator();
                List<RFPrestationAccordeeJointREPrestationAccordee> listRegimes = new ArrayList<RFPrestationAccordeeJointREPrestationAccordee>();
                while (itr.hasNext()) {
                    listRegimes.add(itr.next());
                }

                Regimes regimesByIdTiers = groupRegimeByIdTiers(listRegimes, mapMembreFamille);
                regimesByIdDroit = groupRegimeByIdDroit(regimesByIdTiers, mapMembreFamille);
            }
        }

        return regimesByIdDroit;
    }

    Map<String, Regimes> groupRegimeByIdDroit(Regimes regimesByIdTiers, Map<String, MembresFamilles> mapMembreFamille) {

        Map<String, Regimes> mapFinale = new HashMap<String, Regimes>();

        for (Entry<String, MembresFamilles> famille : mapMembreFamille.entrySet()) {

            // <idTiers, Regime>
            Regimes mapRegDeLaFamille = new Regimes();

            for (MembreFamille membreFamille : famille.getValue().getMembresFamilles()) {

                String idTiers = String.valueOf(membreFamille.getPersonne().getId());

                mapRegDeLaFamille.add(regimesByIdTiers.get(idTiers));
            }

            // <idDroit, Regimes>
            mapFinale.put(famille.getKey(), mapRegDeLaFamille);
        }
        return mapFinale;
    }

    List<String> getListIdsTiers(Map<String, MembresFamilles> mapMembreFamille) {
        List<String> listIds = new ArrayList<String>();
        for (String keyRole : mapMembreFamille.keySet()) {
            for (MembreFamille membreFamille : mapMembreFamille.get(keyRole).getMembresFamilles()) {
                listIds.add(String.valueOf(membreFamille.getPersonne().getId()));
            }
        }
        return listIds;
    }

    Regimes groupRegimeByIdTiers(List<RFPrestationAccordeeJointREPrestationAccordee> listRegimes,
            Map<String, MembresFamilles> mapMembreFamille) {

        Regimes regimes = new Regimes();

        for (Map.Entry<String, MembresFamilles> famille : mapMembreFamille.entrySet()) {

            for (MembreFamille membreFamille : famille.getValue().getMembresFamilles()) {

                String idTiers = String.valueOf(membreFamille.getPersonne().getId());

                for (RFPrestationAccordeeJointREPrestationAccordee prestAcc : listRegimes) {

                    if (idTiers.equals(prestAcc.getIdTiers())) {
                        Date dateFin = null;
                        if (prestAcc.getDateFinDroit() != null && prestAcc.getDateFinDroit().trim().length() > 0
                                && !prestAcc.getDateFinDroit().trim().equals("0")) {
                            dateFin = new Date(prestAcc.getDateFinDroit());
                        }
                        DonneeFinanciere df = new DonneeFinanciereHeader(membreFamille.getRoleMembreFamille(),
                                new Date(prestAcc.getDateDebutDroit()), dateFin, prestAcc.getId(),
                                membreFamille.getId());

                        regimes.put(idTiers, createRFRegimePrestationAccordee(prestAcc, df));
                    }
                }
            }
        }

        return regimes;
    }

    Regime createRFRegimePrestationAccordee(RFPrestationAccordeeJointREPrestationAccordee prestAcc, DonneeFinanciere df) {

        return new Regime(new Montant(prestAcc.getMontantPrestation()), prestAcc.getCs_source(), df);
    }
}
