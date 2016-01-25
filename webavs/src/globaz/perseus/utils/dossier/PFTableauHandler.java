/**
 * 
 */
package globaz.perseus.utils.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.demande.DemandeEtendue;
import ch.globaz.perseus.business.models.demande.DemandeEtendueSearchModel;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFTableauHandler {

    private Map<String, PFLigneHandler> lignes;

    public PFTableauHandler(String idDossier) throws Exception {
        lignes = new HashMap<String, PFLigneHandler>();

        // Ajout de la première ligne d'entete
        PFLigneHandler entete = new PFLigneHandler(lignes.size());
        entete.buildEntete();
        lignes.put("entete", entete);

        // On précharge les prestations
        PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
        prestationSearchModel.setForIdDossier(idDossier);
        prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
        prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);

        // Ligne de total
        HashMap<String, Float> totalMois = new HashMap<String, Float>();
        // Mois courant
        String moisCourant = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

        // Pour chaque demande
        DemandeEtendueSearchModel demandeEtendueSearchModel = new DemandeEtendueSearchModel();
        demandeEtendueSearchModel.setForIdDossier(idDossier);
        demandeEtendueSearchModel.setForCsEtat(CSEtatDemande.VALIDE.getCodeSystem());
        demandeEtendueSearchModel.setOrderKey(DemandeEtendueSearchModel.ORDER_BY_DATETIME_DECISION);
        demandeEtendueSearchModel = (DemandeEtendueSearchModel) JadePersistenceManager
                .search(demandeEtendueSearchModel);
        for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
            DemandeEtendue demande = (DemandeEtendue) model;
            boolean prendre = true;
            // Si il y'a une pcfAccordée on prend seulement celles qui ont une date de décision (si non c'est un projet
            // non validé)
            if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())
                    && JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getDateDecision())) {
                prendre = false;
            }
            if (prendre) {
                lignes.put(demande.getId(), new PFLigneHandler(demande, lignes.size(), prestationSearchModel,
                        totalMois, moisCourant));
            }
        }

        // Ajout de la première ligne d'entete
        PFLigneHandler total = new PFLigneHandler(lignes.size());
        total.buildTotal(totalMois);
        lignes.put("total", total);

    }

    /**
     * @return the lignes
     */
    public Map<String, PFLigneHandler> getLignes() {
        return lignes;
    }

    public List<PFLigneHandler> getListLignes() {
        ArrayList<PFLigneHandler> listLignes = new ArrayList<PFLigneHandler>(lignes.values());
        Comparator<PFLigneHandler> comparator = new Comparator<PFLigneHandler>() {
            @Override
            public int compare(PFLigneHandler o1, PFLigneHandler o2) {
                if (o1.getOrder() < o2.getOrder()) {
                    return -1;
                } else if (o1.getOrder() > o2.getOrder()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(listLignes, comparator);

        return listLignes;
    }

    /**
     * @param lignes
     *            the lignes to set
     */
    public void setLignes(Map<String, PFLigneHandler> lignes) {
        this.lignes = lignes;
    }

}
