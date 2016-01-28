package globaz.perseus.utils.dossier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.DemandeEtendue;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFLigneHandler {

    public static Integer NB_MONTHS_AFTER_NOW = 3;
    public static Integer NB_MONTHS_BEFORE_NOW = 11;

    private Map<String, PFCelluleHandler> cellules;
    private String idDemande;
    private List<String> listMois;
    private String montant;
    private Integer order;

    public PFLigneHandler(DemandeEtendue demandeEtendue, Integer order, PrestationSearchModel prestationSearchModel,
            Map<String, Float> totalMois, String moisCourant) throws Exception {
        this(order);

        idDemande = demandeEtendue.getId();
        if (JadeStringUtil.isEmpty(demandeEtendue.getSimplePcfAccordee().getId())) {
            montant = "0";
        } else {
            montant = demandeEtendue.getSimplePcfAccordee().getMontant();
        }

        // On commence par retrouver la décision
        Decision decision = null;
        // On récupère la décision pour avoir le type de décision
        DecisionSearchModel dsm = new DecisionSearchModel();
        dsm.setForIdDemande(demandeEtendue.getId());
        dsm = PerseusServiceLocator.getDecisionService().search(dsm);
        for (JadeAbstractModel model : dsm.getSearchResults()) {
            Decision dec = (Decision) model;
            if (!CSTypeDecision.PROJET.getCodeSystem().equals(dec.getSimpleDecision().getCsTypeDecision())
                    && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(dec.getSimpleDecision().getCsTypeDecision())) {
                decision = dec;
            }
        }

        // Ajout des deux premières cellules
        PFCelluleHandler cellule1 = new PFCelluleHandler(cellules.size());
        cellule1.setTexte(idDemande);
        cellule1.setTitre(true);
        cellules.put("cellule1", cellule1);
        PFCelluleHandler cellule2 = new PFCelluleHandler(cellules.size());
        cellule2.setTexte(BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                decision.getSimpleDecision().getCsTypeDecision()));
        cellule2.setTitre(true);
        cellules.put("cellule2", cellule2);
        PFCelluleHandler cellule3 = new PFCelluleHandler(cellules.size());
        cellule3.setTexte(new FWCurrency(montant).toStringFormat());
        cellule3.setTitre(true);
        cellules.put("cellule3", cellule3);

        // Définir les paramètres pcfa
        String dateDebutPcfa = demandeEtendue.getSimpleDemande().getDateDebut().substring(3);
        String dateFinPcfa = demandeEtendue.getSimpleDemande().getDateFin();
        dateFinPcfa = (JadeStringUtil.isEmpty(dateFinPcfa)) ? "12.2999" : dateFinPcfa.substring(3);
        Float montantMensuelPcfa = Float.parseFloat(montant);

        // Trouver la prestation retro correspondante à la pcfacordée
        Prestation prestationRetro = null;
        for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
            Prestation prestation = (Prestation) model;
            if (demandeEtendue.getId().equals(prestation.getDecision().getDemande().getId())) {
                prestationRetro = prestation;
            }
        }

        String dateDebutPrest = null;
        String dateFinPrest = null;
        Float montantRetroArepartir = null;
        Float montantRetroPourControle = new Float(0);
        if (prestationRetro != null) {
            dateDebutPrest = prestationRetro.getSimplePrestation().getDateDebut();
            dateFinPrest = prestationRetro.getSimplePrestation().getDateFin();
            montantRetroArepartir = Float.parseFloat(prestationRetro.getSimplePrestation().getMontantTotal());
        }

        // Ajout d'une cellule par mois
        for (String mois : getListMois()) {
            // Initialise à 0 le total si il existe pas encore
            if (totalMois.get(mois) == null) {
                totalMois.put(mois, new Float(0));
            }
            PFCelluleHandler cellule = new PFCelluleHandler(cellules.size());
            // Si le mois est compris dans la pcfa coloré la cellule (date
            if (!JadeDateUtil.isDateMonthYearBefore(mois, dateDebutPcfa)
                    && !JadeDateUtil.isDateMonthYearAfter(mois, dateFinPcfa)) {
                cellule.setColored(true);
                // Si elle est pas retroactive, mettre le montant mensuel
                if (!JadeStringUtil.isEmpty(demandeEtendue.getSimplePcfAccordee().getId())
                        && !JadeDateUtil.isDateMonthYearBefore(mois, demandeEtendue.getSimplePcfAccordee()
                                .getDateDecision())) {
                    cellule.setMontantMensuel(montantMensuelPcfa.toString());

                    // Mettre à jour le total mensuel
                    totalMois.put(mois, totalMois.get(mois) + montantMensuelPcfa);
                }
            }

            // Si il y'a une prestation retro
            if (prestationRetro != null) {
                // On regarde si le mois est dans la prestation
                if (!JadeDateUtil.isDateMonthYearBefore(mois, dateDebutPrest.substring(3))
                        && !JadeDateUtil.isDateMonthYearAfter(mois, dateFinPrest.substring(3))) {

                    Float montantMensuel = montantMensuelPcfa - totalMois.get(mois);
                    cellule.setMontantMensuel(montantMensuel.toString());
                    montantRetroPourControle += montantMensuel;
                    cellule.setRetroActif(true);

                    // Mettre à jour le total mensuel
                    totalMois.put(mois, totalMois.get(mois) + montantMensuel);
                }
                // Si le mois est le mois de la décision on met le montant retro total
                if (mois.equals(demandeEtendue.getSimplePcfAccordee().getDateDecision())) {
                    cellule.setMontantRetro(prestationRetro.getSimplePrestation().getMontantTotal());
                }
            } else {
                if (mois.equals(demandeEtendue.getSimplePcfAccordee().getDateDecision())) {
                    cellule.setMontantRetro("0.00");
                }
            }

            // Si le mois est le mois en cours
            if (mois.equals(moisCourant)) {
                cellule.setMoisCourant(true);
            }

            cellules.put(mois, cellule);
        }

        // Mettre en erreur la cellule de décision si la montant à répartir est différent du montant pour contrôle
        if ((montantRetroArepartir != null) && !montantRetroPourControle.equals(montantRetroArepartir)) {
            cellules.get(moisCourant).setErreur(true);
        }

    }

    public PFLigneHandler(Integer order) throws Exception {
        cellules = new HashMap<String, PFCelluleHandler>();
        listMois = new ArrayList<String>();
        this.order = order;

        // Faire la liste des mois
        String moisCourant = JadeDateUtil.addMonths("01."
                + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt(),
                -PFLigneHandler.NB_MONTHS_BEFORE_NOW);
        for (int i = 0; i < PFLigneHandler.NB_MONTHS_BEFORE_NOW + PFLigneHandler.NB_MONTHS_AFTER_NOW + 1; i++) {
            listMois.add(moisCourant.substring(3));
            moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
        }
    }

    public void buildEntete() {
        // Ajout des deux premières cellules
        PFCelluleHandler c1 = new PFCelluleHandler(cellules.size());
        c1.setTexte("N° Demande");
        c1.setTitre(true);
        cellules.put("vide1", c1);
        PFCelluleHandler c2 = new PFCelluleHandler(cellules.size());
        c2.setTexte("Type");
        c2.setTitre(true);
        cellules.put("vide2", c2);
        PFCelluleHandler c3 = new PFCelluleHandler(cellules.size());
        c3.setTexte("Montant mensuel");
        c3.setTitre(true);
        cellules.put("vide3", c3);

        // Ajout d'une cellule d'entête par mois
        for (String mois : getListMois()) {
            PFCelluleHandler cellule = new PFCelluleHandler(cellules.size());
            cellule.setTitre(true);
            cellule.setTexte(mois);
            cellules.put(mois, cellule);
        }
    }

    public void buildTotal(Map<String, Float> montantTotal) {
        // Ajout des deux premières cellules
        PFCelluleHandler c1 = new PFCelluleHandler(cellules.size());
        c1.setTexte("");
        c1.setTitre(true);
        cellules.put("vide1", c1);
        PFCelluleHandler c2 = new PFCelluleHandler(cellules.size());
        c2.setTexte("");
        c2.setTitre(true);
        cellules.put("vide2", c2);
        PFCelluleHandler c3 = new PFCelluleHandler(cellules.size());
        c3.setTexte("Total");
        c3.setTitre(true);
        cellules.put("vide3", c3);
        // Ajout d'une cellule de total par mois
        for (String mois : getListMois()) {
            PFCelluleHandler cellule = new PFCelluleHandler(cellules.size());
            if (montantTotal.get(mois) == null) {
                cellule.setMontantMensuel("0.00");
            } else {
                cellule.setMontantMensuel(montantTotal.get(mois).toString());
            }
            cellule.setTotal(true);
            cellules.put(mois, cellule);
        }

    }

    /**
     * @return the cellules
     */
    public Map<String, PFCelluleHandler> getCellules() {
        return cellules;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    public List<PFCelluleHandler> getListCellules() {
        ArrayList<PFCelluleHandler> listCellules = new ArrayList<PFCelluleHandler>(cellules.values());
        Comparator<PFCelluleHandler> comparator = new Comparator<PFCelluleHandler>() {
            @Override
            public int compare(PFCelluleHandler o1, PFCelluleHandler o2) {
                if (o1.getOrder() < o2.getOrder()) {
                    return -1;
                } else if (o1.getOrder() > o2.getOrder()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(listCellules, comparator);
        return listCellules;
    }

    /**
     * @return the listMois
     */
    public List<String> getListMois() {
        return listMois;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param cellules
     *            the cellules to set
     */
    public void setCellules(Map<String, PFCelluleHandler> cellules) {
        this.cellules = cellules;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param listMois
     *            the listMois to set
     */
    public void setListMois(List<String> listMois) {
        this.listMois = listMois;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

}
