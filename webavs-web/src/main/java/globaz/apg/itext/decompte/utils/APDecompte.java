package globaz.apg.itext.decompte.utils;

import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.prestation.db.employeurs.PRDepartement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contient l'ensemble des informations nécessaires à la génération d'un décompte -> clé de regroupement -> liste des
 * prestations regroupées
 * 
 * @author lga
 */
public class APDecompte {

    private final APDonneeRegroupementDecompte donneePourRegroupement;
    private List<APFactureACompenser> facturesACompenser;
    private Map<String, List<APRepartitionJointPrestation>> repartitionsEnfants;
    private final Set<APPrestationJointRepartitionPOJO> repartitionsPeres = new HashSet<APPrestationJointRepartitionPOJO>();
    private APTypeDeDecompte typeDeDecompte;

    public APDecompte(final APPrestationJointRepartitionPOJO repartitionJointPrestation) {
        donneePourRegroupement = repartitionJointPrestation.getDonneePourRegroupement();
        addRepartitionPere(repartitionJointPrestation);
    }

    /**
     * Ajoute une facture à compenser uniquement si elle est réellement à compenser
     * <code>APFactureACompenser.getIsCompenser()</code>
     * 
     * @param factureACompenser
     */
    public void addFactureACompenser(final APFactureACompenser factureACompenser) {
        // On ajoute que les factures à compenser
        if (!factureACompenser.getIsCompenser().booleanValue()) {
            return;
        }

        if (facturesACompenser == null) {
            facturesACompenser = new ArrayList<APFactureACompenser>();
        }
        if (!facturesACompenser.contains(factureACompenser)) {
            facturesACompenser.add(factureACompenser);
        }
    }

    /**
     * Ajoute une ventilation de montant.
     */
    public void addRepartitionEnfant(final APRepartitionJointPrestation repartition) {
        if (repartitionsEnfants == null) {
            repartitionsEnfants = new HashMap<String, List<APRepartitionJointPrestation>>();
        }

        List<APRepartitionJointPrestation> enfants = repartitionsEnfants.get(repartition.getIdParent());

        if (enfants == null) {
            enfants = new ArrayList<APRepartitionJointPrestation>();
            repartitionsEnfants.put(repartition.getIdParent(), enfants);
        }
        enfants.add(repartition);
    }

    /**
     * Ajouter une répartition au décompte de ce bénéficiaire, il doit forcement s'agir d'une répartition parente.<br />
     * Ajoute également les ventilations de montant de cette répartition.
     */
    public void addRepartitionPere(final APPrestationJointRepartitionPOJO repartition) {
        repartitionsPeres.add(repartition);
    }

    public final void determinerLeTypeDuDecompte() {
        final List<APTypeDePrestation> types = new ArrayList<APTypeDePrestation>();
        for (final APPrestationJointRepartitionPOJO pojo : repartitionsPeres) {
            if (!types.contains(pojo.getTypeDePrestation())) {
                types.add(pojo.getTypeDePrestation());
            }
        }

        typeDeDecompte = APTypeDeDecompte.determinerTypeDeDecompte(types);
        if (typeDeDecompte == null) {
            throw new RuntimeException("Impossible de déterminer le type de décompte");
        }
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getDepartement()
     */
    public final PRDepartement getDepartement() {
        return donneePourRegroupement.getDepartement();
    }

    public List<APFactureACompenser> getFacturesACompenser() {
        return facturesACompenser;
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getIdAffilie()
     */
    public final String getIdAffilie() {
        return donneePourRegroupement.getIdAffilie();
    }

    // ---------------- METHODES DELEGUEES --------------------------------------------------------------------------//
    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getIdTiers()
     */
    public final String getIdTiers() {
        return donneePourRegroupement.getIdTiers();
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getIsPaiementEmployeur()
     */
    public boolean getIsPaiementEmployeur() {
        return donneePourRegroupement.getIsPaiementEmployeur();
    }

    /**
     * Retourne la répartition enfant en fonction du bénéficiaire du paiement si elle existe, sinon <code>null</code>
     * 
     * @param repartition
     *            La répartition contenant l'id du bénéficiaire du paiement
     * @return La répartition enfant en fonction du bénéficiaire du paiement si elle existe, sinon <code>null</code>
     */
    public List<APRepartitionJointPrestation> getRepartitionsEnfants(final APRepartitionJointPrestation repartition) {
        // TODO c'est pas top cette histoire voir en amont l'utilisation
        if (repartitionsEnfants == null) {
            return null;
        } else {
            return repartitionsEnfants.get(repartition.getIdRepartitionBeneficiairePaiement());
        }
    }

    /**
     * Retourne les répartitions pères
     * 
     * @return Les répartitions pères
     */
    public Set<APRepartitionJointPrestation> getRepartitionsPeres() {
        final Set<APRepartitionJointPrestation> repartitions = new HashSet<APRepartitionJointPrestation>();
        for (final APPrestationJointRepartitionPOJO pojo : repartitionsPeres) {
            repartitions.add(pojo.getPrestationJointRepartition());
        }
        return repartitions;
    }

    public APTypeDeDecompte getTypeDeDecompte() {
        return typeDeDecompte;
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getTypeDePrestation()
     */
    public final APTypeDePrestation getTypeDePrestation() {
        return donneePourRegroupement.getTypeDePrestation();
    }

    public final Boolean isIndependant(){
        return donneePourRegroupement.isIndependant();
    }

    public final String getIdEmployeur() {
        return donneePourRegroupement.getIdEmployeur();
    }

    public final BigDecimal getRevenuAnnuel() {
        return donneePourRegroupement.getRevenuAnnuel();
    }
}
