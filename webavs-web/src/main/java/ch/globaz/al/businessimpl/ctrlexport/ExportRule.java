package ch.globaz.al.businessimpl.ctrlexport;

import globaz.jade.exception.JadeApplicationException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALConstLoisExport;
import ch.globaz.al.business.exceptions.ctrlexport.ALCtrlExportException;

/**
 * Classe repr�sentant une r�gle d'exportation des allocations (pour une combinaison origine allocataire,r�sidence
 * enfant)
 * 
 * @author GMO
 * 
 */
public class ExportRule {

    /** liste des nationalit�s pour lesquelles la r�gle s'applique */
    private List allocatairesNationalite = new ArrayList();

    /** liste des nationalit�s pour lesquelles la r�gle s'applique */
    private List enfantsResidence = new ArrayList();
    /**
     * indique si l'allocation est exportable, dans le cas lafam, jusqu'� 16 ans
     */
    private boolean lafam16 = false;

    /**
     * indique si l'allocation est exportable, dans le cas lafam, de 16 � 25 ans
     */
    private boolean lafam1625 = false;
    /** indique si l'allocation est exportable, dans le cas lfa, jusqu'� 16 ans */
    private boolean lfa16 = false;
    /** indique si l'allocation est exportable, dans le cas lfa, de 16 � 25 ans */
    private boolean lfa1625 = false;
    /**
     * indique si l'allocation est exportable, dans le cas lfa, allocation de m�nage
     */
    private boolean lfaMenage = false;
    /** Num�ro de la r�gle */
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
     * ajoute une nationalit� d'allocataire pour laquelle la r�gle s'applique
     * 
     * @param nationalite
     *            - le code pays � ajouter
     */
    protected void addNationalite(String nationalite) {
        allocatairesNationalite.add(nationalite);
    }

    /**
     * ajoute un pays de r�sidence enfant pour lequel la r�gle s'applique
     * 
     * @param residence
     *            - le code pays � ajouter
     */
    protected void addResidence(String residence) {
        enfantsResidence.add(residence);
    }

    /**
     * efface toutes les nationalit�s d'allocataire pour lesquelles la r�gle s'applique
     */
    protected void clearNationalites() {
        allocatairesNationalite.clear();
    }

    /**
     * efface tous les pays de r�sidence pour lesquels la r�gle s'applique
     */
    protected void clearResidences() {
        enfantsResidence.clear();
    }

    /**
     * v�rifie si <code>nationalite</code> est dans la liste des nationalit�s pour lesquelles la r�gle s'applique
     * 
     * @param nationalite
     *            - la nationalit� � v�rifier
     * @return true, si elle s'y trouve, false sinon
     */
    protected boolean containsNationalite(String nationalite) {
        return allocatairesNationalite.contains(nationalite);
    }

    /**
     * v�rifie si [residence] est dans la liste des r�sidences pour lesquels la r�gle s'applique
     * 
     * @param residence
     *            - la r�sidence � v�rifier
     * @return true, si elle s'y trouve, false sinon
     */
    protected boolean containsResidence(String residence) {
        return enfantsResidence.contains(residence);
    }

    /**
     * @return le num�ro de la r�gle
     */
    public int getNum() {
        return num;
    }

    /**
     * Obtient la pr�cision que fournit la r�gle (selon si le pays est d�fini dans la r�gle ou r�gle par d�faut par ex.)
     * 
     * @return pr�cision de la r�gle
     */
    protected int getPrecision() {
        int precision = 0;
        // on checke chaque nationalit�
        for (int i = 0; i < allocatairesNationalite.size(); i++) {
            // si au moins une est d�fini et autre que union => on ajoute 70 �
            // la pr�cision
            if (!ALConstLoisExport.AELE.equals(allocatairesNationalite.get(i).toString())) {
                precision += 70;
                break;
            }
            // si il n'y a qu'une nationalit� et que union (AELE) => 40%
            if ((allocatairesNationalite.size() == 1)
                    && ALConstLoisExport.AELE.equals(allocatairesNationalite.get(i).toString())) {
                precision += 40;
                break;
            }

        }
        if (allocatairesNationalite.isEmpty()) {
            precision += 10;
        }
        // m�me traitement pour les pays de r�sidence de l'enfant
        for (int i = 0; i < enfantsResidence.size(); i++) {
            // si au moins une est d�fini et autre que union => on ajoute 70 �
            // la pr�cision
            if (!ALConstLoisExport.AELE.equals(enfantsResidence.get(i).toString())) {
                precision += 30;
                break;
            }
            // si il n'y a qu'une nationalit� et que union (AELE) => 40%
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
     *            - loi � renseigner, constante provenant de {@link ch.globaz.al.business.constantes.ALConstLoisExport}
     * @return valeur du statut
     * @throws JadeApplicationException
     *             Exception lev�e si <code>enumLoi</code> n'a pas une valeur valide
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
     * retourne si des nationalit�s d'allocataires sont d�finies
     * 
     * @return true si des nationalit�s d'allocataire sont d�finies, false sinon
     */
    protected boolean isNationaliteEmpty() {
        // return this.allocatairesNationalite.size() == 0;
        return allocatairesNationalite.isEmpty();
    }

    /**
     * retourne si des pays de r�sidence d'enfants sont d�finis
     * 
     * @return true si des pays de r�sidence d'enfants sont d�finis, false sinon
     */
    protected boolean isResidenceEmpty() {
        return enfantsResidence.isEmpty();
    }

    /**
     * retire un pays de r�sidence enfant pour lequel la r�gle s'applique
     * 
     * @param index
     *            - la position dans la liste du code pays � retirer
     */
    protected void removeNationalite(int index) {
        allocatairesNationalite.remove(index);
    }

    /**
     * retire une nationalit� d'allocataire pour laquelle la r�gle s'applique
     * 
     * @param nationalite
     *            - le code pays � retirer
     */
    protected void removeNationalite(String nationalite) {
        allocatairesNationalite.remove(nationalite);
    }

    /**
     * retire un pays de r�sidence enfant pour lequel la r�gle s'applique
     * 
     * @param index
     *            - la position dans la liste du code pays � retirer
     */
    protected void removeResidence(int index) {
        enfantsResidence.remove(index);
    }

    /**
     * retire un pays de r�sidence enfant pour lequel la r�gle s'applique
     * 
     * @param residence
     *            - le code pays � retirer
     */
    protected void removeResidence(String residence) {
        enfantsResidence.remove(residence);
    }

    /**
     * ajoute une liste de nationalit�s allocataire pour lesquelles la r�gle s'applique
     * 
     * @param nationalitesList
     *            la liste � ajouter
     */
    protected void setNationalites(ArrayList nationalitesList) {
        allocatairesNationalite.addAll(nationalitesList);
    }

    /**
     * D�finit le num�ro de la r�gle
     * 
     * @param newNum
     *            nouveau num�ro de la r�gle
     */
    protected void setNum(int newNum) {
        num = newNum;
    }

    /**
     * ajoute une liste de r�sidences enfant pour lesquelles la r�gle s'applique
     * 
     * @param residencesList
     *            la liste � ajouter
     */
    protected void setResidences(ArrayList residencesList) {
        enfantsResidence.addAll(residencesList);
    }

    /**
     * d�finit le statut d'une loi
     * 
     * @param status
     *            - valeur de la loi
     * @param enumLoi
     *            - loi � d�finir, constante provenant de {@link ch.globaz.al.business.constantes.ALConstLoisExport}
     * @throws JadeApplicationException
     *             Exception lev�e si <code>enumLoi</code> n'a pas une valeur valide
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
