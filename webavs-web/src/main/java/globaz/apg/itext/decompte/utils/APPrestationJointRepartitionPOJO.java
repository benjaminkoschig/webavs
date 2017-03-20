package globaz.apg.itext.decompte.utils;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.prestation.db.employeurs.PRDepartement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * D�finit la structure de donn�es re�ue par le manager lors de la recherche des prestationsJointRepartition contenus
 * dans le lot
 * 
 * @author lga
 */
public class APPrestationJointRepartitionPOJO {

    private final APDonneeRegroupementDecompte donneePourRegroupement;
    private APRepartitionJointPrestation prestationJointRepartition;
    private List<APFactureACompenser> facturesACompenser;
    private Map<String, List<APRepartitionJointPrestation>> repartitionsEnfants;
    private final Set<APRepartitionJointPrestation> repartitionsPeres = new HashSet<APRepartitionJointPrestation>();
    private String cleDeRegroupement;
    private String cleDeTriPrestations;

    public APPrestationJointRepartitionPOJO(final APRepartitionJointPrestation repartitionJointPrestation,
            final APSituationProfessionnelle situationProfessionnelle, final PRDepartement departement,
            final APTypeDePrestation typeDePrestation, final boolean isModuleCompensationPorteEnCompteActif) {

        if (repartitionJointPrestation == null) {
            throw new IllegalArgumentException(
                    "Can not create instance of PrestationJointRepartitionPOJO with a null APRepartitionJointPrestation");
        }
        if (typeDePrestation == null) {
            throw new IllegalArgumentException(
                    "Can not create instance of PrestationJointRepartitionPOJO with a null APTypeDePrestation");
        }

        prestationJointRepartition = repartitionJointPrestation;
        donneePourRegroupement = new APDonneeRegroupementDecompte(prestationJointRepartition, situationProfessionnelle,
                departement, typeDePrestation, isModuleCompensationPorteEnCompteActif);
    }

    /**
     * Ajoute une facture � compenser uniquement si elle est r�ellement � compenser
     * <code>APFactureACompenser.getIsCompenser()</code>
     * 
     * @param factureACompenser
     */
    public void addFactureACompenser(final APFactureACompenser factureACompenser) {
        // On ajoute que les factures � compenser
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
     * Retourne la r�partition enfant en fonction du b�n�ficiaire du paiement si elle existe, sinon <code>null</code>
     * 
     * @param repartition
     *            La r�partition contenant l'id du b�n�ficiaire du paiement
     * @return La r�partition enfant en fonction du b�n�ficiaire du paiement si elle existe, sinon <code>null</code>
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
     * Ajouter une r�partition au d�compte de ce b�n�ficiaire, il doit forcement s'agir d'une r�partition parente.<br />
     * Ajoute �galement les ventilations de montant de cette r�partition.
     */
    public void addRepartitionPere(final APRepartitionJointPrestation repartition) {
        repartitionsPeres.add(repartition);
    }

    /**
     * Retourne les r�partitions p�res
     * 
     * @return Les r�partitions p�res
     */
    public Set<APRepartitionJointPrestation> getRepartitionsPeres() {
        return repartitionsPeres;
    }

    /**
     * @return the prestationJointRepartition
     */
    public final APRepartitionJointPrestation getPrestationJointRepartition() {
        return prestationJointRepartition;
    }

    /**
     * @param prestationJointRepartition
     *            the prestationJointRepartition to set
     */
    public final void setPrestationJointRepartition(final APRepartitionJointPrestation prestationJointRepartition) {
        this.prestationJointRepartition = prestationJointRepartition;
    }

    /**
     * G�n�re la cl� de regroupement en fonction de la m�thode de regroupement des prestations pass�e en argument
     * 
     * @param methodeRegroupement
     */
    public void genererCleDeRegroupement(final APMethodeRegroupement methodeRegroupement) {
        cleDeRegroupement = donneePourRegroupement.genererCleDeRegroupement(methodeRegroupement);
        cleDeTriPrestations = donneePourRegroupement.genererCleDeTriDesPrestations(methodeRegroupement);
    }

    /**
     * @return the cleRegroupement
     */
    public final String getCleDeRegroupement() {
        return cleDeRegroupement;
    }

    public String getCleDeTriPrestations() {
        return cleDeTriPrestations;
    }

    public List<APFactureACompenser> getFacturesACompenser() {
        return facturesACompenser;
    }

    public APDonneeRegroupementDecompte getDonneePourRegroupement() {
        return donneePourRegroupement;
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
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getIdAffilie()
     */
    public final String getIdAffilie() {
        return donneePourRegroupement.getIdAffilie();
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getIsPaiementEmployeur()
     */
    public boolean getIsPaiementEmployeur() {
        return donneePourRegroupement.getIsPaiementEmployeur();
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getDepartement()
     */
    public final PRDepartement getDepartement() {
        return donneePourRegroupement.getDepartement();
    }

    /**
     * @return
     * @see globaz.apg.itext.decompte.utils.APDonneeRegroupementDecompte#getTypeDePrestation()
     */
    public final APTypeDePrestation getTypeDePrestation() {
        return donneePourRegroupement.getTypeDePrestation();
    }

}
