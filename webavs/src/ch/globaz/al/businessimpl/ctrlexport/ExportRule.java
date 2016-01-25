package ch.globaz.al.businessimpl.ctrlexport;

import globaz.jade.exception.JadeApplicationException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALConstLoisExport;
import ch.globaz.al.business.exceptions.ctrlexport.ALCtrlExportException;

/**
 * Classe représentant une règle d'exportation des allocations (pour une combinaison origine allocataire,résidence
 * enfant)
 * 
 * @author GMO
 * 
 */
public class ExportRule {

    /** liste des nationalités pour lesquelles la règle s'applique */
    private List allocatairesNationalite = new ArrayList();

    /** liste des nationalités pour lesquelles la règle s'applique */
    private List enfantsResidence = new ArrayList();
    /**
     * indique si l'allocation est exportable, dans le cas lafam, jusqu'à 16 ans
     */
    private boolean lafam16 = false;

    /**
     * indique si l'allocation est exportable, dans le cas lafam, de 16 à 25 ans
     */
    private boolean lafam1625 = false;
    /** indique si l'allocation est exportable, dans le cas lfa, jusqu'à 16 ans */
    private boolean lfa16 = false;
    /** indique si l'allocation est exportable, dans le cas lfa, de 16 à 25 ans */
    private boolean lfa1625 = false;
    /**
     * indique si l'allocation est exportable, dans le cas lfa, allocation de ménage
     */
    private boolean lfaMenage = false;
    /** Numéro de la règle */
    private int num = 0;

    /**
     * Constructeur
     */
    public ExportRule() {
        super();
        clearNationalites();
        clearResidences();
    }

    /**
     * ajoute une nationalité d'allocataire pour laquelle la règle s'applique
     * 
     * @param nationalite
     *            - le code pays à ajouter
     */
    protected void addNationalite(String nationalite) {
        allocatairesNationalite.add(nationalite);
    }

    /**
     * ajoute un pays de résidence enfant pour lequel la règle s'applique
     * 
     * @param residence
     *            - le code pays à ajouter
     */
    protected void addResidence(String residence) {
        enfantsResidence.add(residence);
    }

    /**
     * efface toutes les nationalités d'allocataire pour lesquelles la règle s'applique
     */
    protected void clearNationalites() {
        allocatairesNationalite.clear();
    }

    /**
     * efface tous les pays de résidence pour lesquels la règle s'applique
     */
    protected void clearResidences() {
        enfantsResidence.clear();
    }

    /**
     * vérifie si <code>nationalite</code> est dans la liste des nationalités pour lesquelles la règle s'applique
     * 
     * @param nationalite
     *            - la nationalité à vérifier
     * @return true, si elle s'y trouve, false sinon
     */
    protected boolean containsNationalite(String nationalite) {
        return allocatairesNationalite.contains(nationalite);
    }

    /**
     * vérifie si [residence] est dans la liste des résidences pour lesquels la règle s'applique
     * 
     * @param residence
     *            - la résidence à vérifier
     * @return true, si elle s'y trouve, false sinon
     */
    protected boolean containsResidence(String residence) {
        return enfantsResidence.contains(residence);
    }

    /**
     * @return le numéro de la règle
     */
    public int getNum() {
        return num;
    }

    /**
     * Obtient la précision que fournit la règle (selon si le pays est défini dans la règle ou règle par défaut par ex.)
     * 
     * @return précision de la règle
     */
    protected int getPrecision() {
        int precision = 0;
        // on checke chaque nationalité
        for (int i = 0; i < allocatairesNationalite.size(); i++) {
            // si au moins une est défini et autre que union => on ajoute 70 à
            // la précision
            if (!ALConstLoisExport.AELE.equals(allocatairesNationalite.get(i).toString())) {
                precision += 70;
                break;
            }
            // si il n'y a qu'une nationalité et que union (AELE) => 40%
            if ((allocatairesNationalite.size() == 1)
                    && ALConstLoisExport.AELE.equals(allocatairesNationalite.get(i).toString())) {
                precision += 40;
                break;
            }

        }
        if (allocatairesNationalite.isEmpty()) {
            precision += 10;
        }
        // même traitement pour les pays de résidence de l'enfant
        for (int i = 0; i < enfantsResidence.size(); i++) {
            // si au moins une est défini et autre que union => on ajoute 70 à
            // la précision
            if (!ALConstLoisExport.AELE.equals(enfantsResidence.get(i).toString())) {
                precision += 30;
                break;
            }
            // si il n'y a qu'une nationalité et que union (AELE) => 40%
            if ((enfantsResidence.size() == 1) && ALConstLoisExport.AELE.equals(enfantsResidence.get(i).toString())) {
                precision += 20;
                break;
            }
        }
        if (enfantsResidence.isEmpty()) {
            precision += 10;
        }

        return precision;
    }

    /**
     * renseigne sur le statut d'une loi
     * 
     * @param enumLoi
     *            - loi à renseigner, constante provenant de {@link ch.globaz.al.business.constantes.ALConstLoisExport}
     * @return valeur du statut
     * @throws JadeApplicationException
     *             Exception levée si <code>enumLoi</code> n'a pas une valeur valide
     */
    protected boolean getStatus(int enumLoi) throws JadeApplicationException {
        boolean value = false;
        switch (enumLoi) {
            case ALConstLoisExport.LOI_LAFAM_16:
                value = lafam16;
                break;
            case ALConstLoisExport.LOI_LAFAM_16_25:
                value = lafam1625;
                break;
            case ALConstLoisExport.LOI_LFA_16:
                value = lfa16;
                break;
            case ALConstLoisExport.LOI_LFA_16_25:
                value = lfa1625;
                break;
            case ALConstLoisExport.LOI_LFA_MENAGE:
                value = lfaMenage;
                break;
            default:
                throw new ALCtrlExportException("ExportRule#getStatus : " + enumLoi + " is not a valid law");
        }

        return value;
    }

    /**
     * retourne si des nationalités d'allocataires sont définies
     * 
     * @return true si des nationalités d'allocataire sont définies, false sinon
     */
    protected boolean isNationaliteEmpty() {
        // return this.allocatairesNationalite.size() == 0;
        return allocatairesNationalite.isEmpty();
    }

    /**
     * retourne si des pays de résidence d'enfants sont définis
     * 
     * @return true si des pays de résidence d'enfants sont définis, false sinon
     */
    protected boolean isResidenceEmpty() {
        return enfantsResidence.isEmpty();
    }

    /**
     * retire un pays de résidence enfant pour lequel la règle s'applique
     * 
     * @param index
     *            - la position dans la liste du code pays à retirer
     */
    protected void removeNationalite(int index) {
        allocatairesNationalite.remove(index);
    }

    /**
     * retire une nationalité d'allocataire pour laquelle la règle s'applique
     * 
     * @param nationalite
     *            - le code pays à retirer
     */
    protected void removeNationalite(String nationalite) {
        allocatairesNationalite.remove(nationalite);
    }

    /**
     * retire un pays de résidence enfant pour lequel la règle s'applique
     * 
     * @param index
     *            - la position dans la liste du code pays à retirer
     */
    protected void removeResidence(int index) {
        enfantsResidence.remove(index);
    }

    /**
     * retire un pays de résidence enfant pour lequel la règle s'applique
     * 
     * @param residence
     *            - le code pays à retirer
     */
    protected void removeResidence(String residence) {
        enfantsResidence.remove(residence);
    }

    /**
     * ajoute une liste de nationalités allocataire pour lesquelles la règle s'applique
     * 
     * @param nationalitesList
     *            la liste à ajouter
     */
    protected void setNationalites(ArrayList nationalitesList) {
        allocatairesNationalite.addAll(nationalitesList);
    }

    /**
     * Définit le numéro de la règle
     * 
     * @param newNum
     *            nouveau numéro de la règle
     */
    protected void setNum(int newNum) {
        num = newNum;
    }

    /**
     * ajoute une liste de résidences enfant pour lesquelles la règle s'applique
     * 
     * @param residencesList
     *            la liste à ajouter
     */
    protected void setResidences(ArrayList residencesList) {
        enfantsResidence.addAll(residencesList);
    }

    /**
     * définit le statut d'une loi
     * 
     * @param status
     *            - valeur de la loi
     * @param enumLoi
     *            - loi à définir, constante provenant de {@link ch.globaz.al.business.constantes.ALConstLoisExport}
     * @throws JadeApplicationException
     *             Exception levée si <code>enumLoi</code> n'a pas une valeur valide
     * 
     */
    protected void setStatus(boolean status, int enumLoi) throws JadeApplicationException {
        switch (enumLoi) {
            case ALConstLoisExport.LOI_LAFAM_16:
                lafam16 = status;
                break;
            case ALConstLoisExport.LOI_LAFAM_16_25:
                lafam1625 = status;
                break;
            case ALConstLoisExport.LOI_LFA_16:
                lfa16 = status;
                break;
            case ALConstLoisExport.LOI_LFA_16_25:
                lfa1625 = status;
                break;
            case ALConstLoisExport.LOI_LFA_MENAGE:
                lfaMenage = status;
                break;
            default:
                throw new ALCtrlExportException("ExportRule#setStatus : " + enumLoi + " is not a valid law");
        }
    }

}
