package ch.globaz.al.business.models.droit;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import java.util.ArrayList;
import java.util.Date;

/**
 * Modèle utilisé pour stocker les résultats du calcul des droits.
 * 
 * @author jts
 * 
 */
public class CalculBusinessModel implements Comparable<Object> {

    /**
     * Montant de base de la prestation. C'est le montant qui est utilisé pour déterminer la prestation qui sera versé à
     * l'allocataire
     */
    private String calculResultMontantBase = null;
    /**
     * Montant de base de la prestation selon le tarif de la caisse.
     */
    private String calculResultMontantBaseCaisse = null;
    /**
     * Montant de base de la prestation selon le tarif du canton.
     */
    private String calculResultMontantBaseCanton = null;
    /**
     * Montant effectif de la prestation. Calculé en fonction du taux du dossier, montant forcé, ... C'est le montant
     * qui est versé à l'allocataire
     */
    private String calculResultMontantEffectif = null;
    /**
     * Montant effectif selon le tarif de la caisse
     */
    private String calculResultMontantEffectifCaisse = null;
    /**
     * Montant effectif selon le tarif du canton
     */
    private String calculResultMontantEffectifCanton = null;
    /**
     * Montant Impot à la source
     */
    private String calculResultMontantIS = null;

    /**
     * Droit lié à la prestation
     */
    private DroitComplexModel droit = null;
    /**
     * Indique si le droit doit être masqué à l'affichage du calcul, sur la décision, ...
     */
    private boolean hideDroit = false;
    /**
     * Indique si le droit est actif ou non
     */
    private boolean isActif = false;
    /**
     * Indique si l'échéance du droit est au-délà de la limite légale
     */
    private boolean isEcheanceOverLimiteLegale = false;
    /**
     * Indique si la prestation est exportable ou non
     */
    private boolean isExportable = false;

    /** Messages d'erreur */
    private ArrayList<String> messagesError = new ArrayList<String>();
    /** Messages d'information */
    private ArrayList<String> messagesInfo = new ArrayList<String>();
    /** Messages d'avertissement */
    private ArrayList<String> messagesWarning = new ArrayList<String>();

    /**
     * Montant de base en vigueur dans le canton de l'allocataire. Utilisé uniquement pour les dossier intercantonaux
     */
    private String montantAllocataire = "0.0";

    /**
     * Montant de base en vigueur dans le canton de l'autre parent. Utilisé uniquement pour les dossier intercantonaux
     */
    private String montantAutreParent = "0.0";

    /**
     * Rang de l'enfant au moment du calcul
     */
    private String rang = null;
    /**
     * Tarif catégorie de tarif utilisée pour le calcul du droit
     */
    private String tarif = null;
    /**
     * Tarif correspondant au montant stocké dans {@link CalculBusinessModel#calculResultMontantBaseCaisse}
     */
    private String tarifCaisse = null;
    /**
     * Tarif correspondant au montant stocké dans {@link CalculBusinessModel#calculResultMontantBaseCanton}
     */
    private String tarifCanton = null;
    /**
     * Indique si un montant a été forcé au niveau du droit
     */
    private boolean tarifForce = false;
    /**
     * Type de droit calculé
     */
    private String type = null;

    /**
     * @param message
     *            le message à ajouter
     */
    public void addMessageError(String message) {
        messagesError.add(message);
    }

    /**
     * @param message
     *            le message à ajouter
     */
    public void addMessageInfo(String message) {
        messagesInfo.add(message);
    }

    /**
     * @param message
     *            le message à ajouter
     */
    public void addMessageWarning(String message) {
        messagesWarning.add(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {

        if (!(o instanceof CalculBusinessModel)) {
            throw new IllegalArgumentException("CalculBusinessModel#compareTo : o is not an instance of compareTo");
        }

        String today = JadeDateUtil.getGlobazFormattedDate(new Date());

        int ageAutre = 0;
        int age = 0;

        if (getDroit() == null) {
            return -1;
        } else if (((CalculBusinessModel) o).getDroit() == null) {
            return 1;
        }

        if (getDroit().getEnfantComplexModel() != null) {
            age = JadeDateUtil.getNbYearsBetween(getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getPersonne().getDateNaissance(), today);
        }

        if (getDroit().getEnfantComplexModel() != null) {
            ageAutre = JadeDateUtil.getNbYearsBetween(((CalculBusinessModel) o).getDroit().getEnfantComplexModel()
                    .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(), today);
        }

        double montantAutre = Double.parseDouble(((CalculBusinessModel) o).getCalculResultMontantBase());

        double montant = Double.parseDouble(getCalculResultMontantBase());

        if (((montantAutre > 0) && (montant > 0)) || ((montantAutre == 0) && (montant == 0))) {

            return ageAutre - age;
        } else if ((montantAutre > 0) && (montant == 0)) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Retourne le résultat du calcul (montant de base)
     * 
     * @return the calculResultMontantBase
     */
    public String getCalculResultMontantBase() {
        return calculResultMontantBase;
    }

    /**
     * @return the calculResultMontantBaseCaisse
     */
    public String getCalculResultMontantBaseCaisse() {
        return calculResultMontantBaseCaisse;
    }

    /**
     * @return the calculResultMontantBaseCanton
     */
    public String getCalculResultMontantBaseCanton() {
        return calculResultMontantBaseCanton;
    }

    /**
     * Retourne le résultat du calcul (montant effectif)
     * 
     * @return the calculResultMontantEffectif
     */
    public String getCalculResultMontantEffectif() {
        return calculResultMontantEffectif;
    }

    /**
     * @return the calculResultMontantEffectifCaisse
     */
    public String getCalculResultMontantEffectifCaisse() {
        return calculResultMontantEffectifCaisse;
    }

    /**
     * @return the calculResultMontantEffectifCanton
     */
    public String getCalculResultMontantEffectifCanton() {
        return calculResultMontantEffectifCanton;
    }

    /**
     * @return the calculResultMontantIS
     */
    public String getCalculResultMontantIS() {
        return calculResultMontantIS;
    }

    /**
     * @return the droit
     */
    public DroitComplexModel getDroit() {
        return droit;
    }

    /**
     * @return the messagesError
     */
    public ArrayList<String> getMessagesError() {
        return messagesError;
    }

    /**
     * @return the messagesInfo
     */
    public ArrayList<String> getMessagesInfo() {
        return messagesInfo;
    }

    /**
     * @return the messagesWarning
     */
    public ArrayList<String> getMessagesWarning() {
        return messagesWarning;
    }

    /**
     * Montant de base en vigueur dans le canton de l'allocataire. Utilisé uniquement pour les dossier intercantonaux
     * 
     * @return montant en vigueur dans le canton de l'allocataire
     */
    public String getMontantAllocataire() {
        return montantAllocataire;
    }

    /**
     * Montant de base en vigueur dans le canton de l'autre parent. Utilisé uniquement pour les dossier intercantonaux
     * 
     * @return le montant en vigueur dans le canton de l'autre parent
     */
    public String getMontantAutreParent() {
        return montantAutreParent;
    }

    /**
     * @return the rang
     */
    public String getRang() {
        return rang;
    }

    /**
     * @return the tarif
     */
    public String getTarif() {
        return tarif;
    }

    /**
     * @return le tarif correspondant au montant stocké dans {@link CalculBusinessModel#calculResultMontantBaseCaisse}
     */
    public String getTarifCaisse() {
        return tarifCaisse;
    }

    /**
     * @return le tarif correspondant au montant stocké dans {@link CalculBusinessModel#calculResultMontantBaseCanton}
     */
    public String getTarifCanton() {
        return tarifCanton;
    }

    /**
     * Retourne le type du droit calculé
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the isActif
     */
    public boolean isActif() {
        return isActif;
    }

    /**
     * @return isEcheanceOverLimiteLegale
     */
    public boolean isEcheanceOverLimiteLegale() {
        return isEcheanceOverLimiteLegale;
    }

    /**
     * @return the isExportable
     */
    public boolean isExportable() {
        return isExportable;
    }

    /**
     * Indique si le droit doit être caché. Peut être utile, par exemple, pour n'afficher que le montant dans la liste
     * des droits ou sur une décision
     * 
     * @return <code>true</code> si le droit doit être masqué, <code>false</code> sinon
     */
    public boolean isHideDroit() {
        return hideDroit;
    }

    /**
     * @return <code>true</code> si un tarif a été forcé
     */
    public boolean isTarifForce() {
        return tarifForce;
    }

    /**
     * @param isActif
     *            the isActif to set
     */
    public void setActif(boolean isActif) {
        this.isActif = isActif;
    }

    /**
     * Définit le résultat du calcul (montant de base)
     * 
     * @param calculResultMontantBase
     *            the calculResultMontantBase to set
     */
    public void setCalculResultMontantBase(String calculResultMontantBase) {
        this.calculResultMontantBase = calculResultMontantBase;
    }

    /**
     * @param calculResultMontantBaseCaisse
     *            the calculResultMontantBaseCaisse to set
     */
    public void setCalculResultMontantBaseCaisse(String calculResultMontantBaseCaisse) {
        this.calculResultMontantBaseCaisse = calculResultMontantBaseCaisse;
    }

    /**
     * @param calculResultMontantBaseCanton
     *            the calculResultMontantBaseCanton to set
     */
    public void setCalculResultMontantBaseCanton(String calculResultMontantBaseCanton) {
        this.calculResultMontantBaseCanton = calculResultMontantBaseCanton;
    }

    /**
     * Définit le résultat du calcul (montant effectif)
     * 
     * @param calculResultMontantEffectif
     *            the calculResultMontantEffectif to set
     */
    public void setCalculResultMontantEffectif(String calculResultMontantEffectif) {
        this.calculResultMontantEffectif = calculResultMontantEffectif;
    }

    /**
     * @param calculResultMontantEffectifCaisse
     *            the calculResultMontantEffectifCaisse to set
     */
    public void setCalculResultMontantEffectifCaisse(String calculResultMontantEffectifCaisse) {
        this.calculResultMontantEffectifCaisse = calculResultMontantEffectifCaisse;
    }

    /**
     * @param calculResultMontantEffectifCanton
     *            the calculResultMontantEffectifCanton to set
     */
    public void setCalculResultMontantEffectifCanton(String calculResultMontantEffectifCanton) {
        this.calculResultMontantEffectifCanton = calculResultMontantEffectifCanton;
    }

    /**
     * @param calculResultMontantIS
     *            the calculResultMontantIS to set
     */
    public void setCalculResultMontantIS(String calculResultMontantIS) {
        this.calculResultMontantIS = calculResultMontantIS;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(DroitComplexModel droit) {
        this.droit = droit;
        if (!droit.getTiersBeneficiaireModel().isNew()) {
            addMessageWarning(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "al.calcul.droit.info.beneficiaire"));
        }
    }

    /**
     * @param isEcheanceOverLimiteLegale
     *            limite légale dépassée ou non
     */
    public void setEcheanceOverLimiteLegale(boolean isEcheanceOverLimiteLegale) {
        this.isEcheanceOverLimiteLegale = isEcheanceOverLimiteLegale;

        if (isEcheanceOverLimiteLegale) {
            addMessageWarning(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "al.calcul.warning.echeance"));
        }
    }

    /**
     * @param isExportable
     *            the isExportable to set
     */
    public void setExportable(boolean isExportable) {
        this.isExportable = isExportable;

        if (!this.isExportable) {
            addMessageWarning(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "al.calcul.warning.nonexportable"));
        }
    }

    /**
     * Définit si le droit doit être caché. Peut être utile, par exemple, pour n'afficher que le montant dans la liste
     * des droits ou sur une décision
     * 
     * @param hideDroit
     *            <code>true</code> si le droit doit être masqué, <code>false</code> sinon
     */
    public void setHideDroit(boolean hideDroit) {
        this.hideDroit = hideDroit;
    }

    /**
     * @param montantAllocataire
     *            the montantAllocataire to set
     */
    public void setMontantAllocataire(String montantAllocataire) {
        this.montantAllocataire = montantAllocataire;
    }

    /**
     * @param montantAutreParent
     *            the montantAutreParent to set
     */
    public void setMontantAutreParent(String montantAutreParent) {
        this.montantAutreParent = montantAutreParent;
    }

    /**
     * @param rang
     *            the rang to set
     */
    public void setRang(String rang) {
        this.rang = rang;
    }

    /**
     * Définit le tarif utilisé lors du calcul du droit
     * 
     * @param tarif
     *            the tarif to set
     */
    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    /**
     * @param tarifCaisse
     *            le tarif correspondant au montant stocké dans
     *            {@link CalculBusinessModel#calculResultMontantBaseCaisse}
     */
    public void setTarifCaisse(String tarifCaisse) {
        this.tarifCaisse = tarifCaisse;
    }

    /**
     * @param tarifCanton
     *            le tarif correspondant au montant stocké dans
     *            {@link CalculBusinessModel#calculResultMontantBaseCanton}
     */
    public void setTarifCanton(String tarifCanton) {
        this.tarifCanton = tarifCanton;
    }

    /**
     * @param tarifForce
     *            le montant forcé
     */
    public void setTarifForce(boolean tarifForce) {
        this.tarifForce = tarifForce;
    }

    /**
     * Définit le type du droit qui a été calculé
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}