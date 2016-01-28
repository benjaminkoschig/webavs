package ch.globaz.al.business.models.prestation.paiement;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Mod�le de recherche permettant d'effectuer des recherches sur les mod�les li� � la compensation sur facture et le
 * versement direct de prestations AF.
 * 
 * Tous les sous-mod�le de recherche doivent �tendre ce mod�le
 * 
 * @author jts
 * 
 */
public abstract class CompensationPaiementPrestationComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche selon le type de bonification
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI
     */
    private String forBonification = null;

    /**
     * Recherche selon l'�tat de la r�cap
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT
     */
    private String forEtat = null;
    /**
     * Recherche sur l'id de journal de comptabilit�
     */
    private String forIdJournal = null;
    /**
     * Recherche sur l'id de passage de facturation
     */
    private String forIdPassage = null;

    /**
     * Recherche sur le num�ro du processus AF li� � la r�cap
     */
    private String forIdProcessusPeriodique = null;

    /**
     * Recherche sur la fin d'une p�riode
     */
    private String forPeriodeA = null;

    /**
     * Recherche sur une ou plusieurs activit�(s)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    private Collection<String> inActivites = null;
    /**
     * Recherche selon une liste de types de bonification
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI
     */
    private Collection<String> inBonifications = null;

    /**
     * @return the forBonification
     */
    public String getForBonification() {
        return forBonification;
    }

    /**
     * @return the forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forIdJournal
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @return the forIdPassage
     */
    public String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * 
     * @return the forIdProcessusPeriodique
     */
    public String getForIdProcessusPeriodique() {
        return forIdProcessusPeriodique;
    }

    /**
     * @return the forPeriodeA
     */
    public String getForPeriodeA() {
        return forPeriodeA;
    }

    /**
     * @return the inActivites
     */
    public Collection<String> getInActivites() {
        return inActivites;
    }

    /**
     * @return the inBonifications
     */
    public Collection<String> getInBonifications() {
        return inBonifications;
    }

    /**
     * @param forBonification
     *            type de bonification. Code syst�me appartenant au groupe
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     */
    public void setForBonification(String forBonification) {
        this.forBonification = forBonification;
    }

    /**
     * @param forEtat
     *            Etat de la r�cap. Code syst�me appartenant au groupe
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdJournal
     *            the forIdJournal to set
     */
    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    /**
     * D�finit la recherche sur l'id de passage de facturation
     * 
     * @param forIdPassage
     *            the forIdPassage to set
     */
    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * 
     * @param forIdProcessusPeriodique
     *            the idProcessusPeriodique to set
     */
    public void setForIdProcessusPeriodique(String forIdProcessusPeriodique) {
        this.forIdProcessusPeriodique = forIdProcessusPeriodique;
    }

    /**
     * @param forPeriodeA
     *            p�riode de fin de la r�cap (format MM.AAAA)
     */
    public void setForPeriodeA(String forPeriodeA) {
        this.forPeriodeA = forPeriodeA;
    }

    /**
     * @param inActivites
     *            type d'activit� des allocataires. Codes syst�me appartenant au groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC}
     */
    public void setInActivites(Collection<String> inActivites) {
        this.inActivites = inActivites;
    }

    /**
     * @param inBonifications
     *            the inBonifications to set
     */
    public void setInBonifications(Collection<String> inBonifications) {
        this.inBonifications = inBonifications;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class whichModelClass() {
        return CompensationPaiementPrestationComplexModel.class;
    }
}
