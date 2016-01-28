package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un poste d'irrécouvrable dans le cadre de la ventilation (contentieux) Un poste est composé entre autre de
 * lignes de postes
 * 
 * @author bjo
 * 
 */
public class CAPoste implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer annee;
    private String idRubrique;
    private String idRubriqueIrrecouvrable;
    private boolean isPosteOnError;
    private boolean isRubriqueIndetermine;
    private String libelleRubriqueIrrecouvrable;
    private List<CALigneDePoste> lignesDePosteList;
    private String numeroRubriqueIrrecouvrable;
    private String ordrePriorite;
    private CATypeLigneDePoste type;

    /**
     * @param annee
     * @param idRubrique
     * @param idRubriqueIrrecouvrable
     * @param isRubriqueIndetermine
     * @param numeroRubriqueIrrecouvrable
     * @param libelleRubriqueIrrecouvrable
     * @param ordrePriorite
     * @param type
     */
    public CAPoste(Integer annee, String idRubrique, String idRubriqueIrrecouvrable, boolean isRubriqueIndetermine,
            String numeroRubriqueIrrecouvrable, String libelleRubriqueIrrecouvrable, String ordrePriorite,
            CATypeLigneDePoste type) {
        this.annee = annee;
        this.idRubrique = idRubrique;
        this.idRubriqueIrrecouvrable = idRubriqueIrrecouvrable;
        this.libelleRubriqueIrrecouvrable = libelleRubriqueIrrecouvrable;
        this.isRubriqueIndetermine = isRubriqueIndetermine;
        lignesDePosteList = new ArrayList<CALigneDePoste>();
        this.numeroRubriqueIrrecouvrable = numeroRubriqueIrrecouvrable;
        this.ordrePriorite = ordrePriorite;
        this.type = type;
        isPosteOnError = false;
    }

    /**
     * Ajoute la ligne de poste passée en paramètre au poste
     * 
     * @param ligneDePoste
     */
    public void addLigneInPoste(CALigneDePoste ligneDePoste) {
        lignesDePosteList.add(ligneDePoste);
    }

    public Integer getAnnee() {
        return annee;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdRubriqueIrrecouvrable() {
        return idRubriqueIrrecouvrable;
    }

    public String getLibelleRubriqueIrrecouvrable() {
        return libelleRubriqueIrrecouvrable;
    }

    /**
     * Retourne la ligne de poste identifiée par idSection et type
     * 
     * @param idSection
     * @param type
     * @return
     */
    public CALigneDePoste getLigneDePoste(String idSection, CATypeLigneDePoste type) {
        CALigneDePoste ligneDePoste = null;
        for (CALigneDePoste ligne : lignesDePosteList) {
            if (ligne.getIdSection().equals(idSection) && ligne.getType().equals(type)) {
                ligneDePoste = ligne;
                break;
            }
        }
        return ligneDePoste;
    }

    /**
     * Retourne l'ensemble des lignes du poste
     * 
     * @return
     */
    public List<CALigneDePoste> getLignesDePosteList() {
        return lignesDePosteList;
    }

    public BigDecimal getMontantAffecteTotal() {
        // return this.montantAffecteTotal;
        BigDecimal montantAffecteTotal = new BigDecimal(0);
        for (CALigneDePoste ligneDePoste : lignesDePosteList) {
            montantAffecteTotal = montantAffecteTotal.add(ligneDePoste.getMontantAffecte());
        }

        return montantAffecteTotal;
    }

    /**
     * Parours toutes les lignes du postes et retourne le montantDu total
     * 
     * @return
     */
    public BigDecimal getMontantDuTotal() {
        BigDecimal montantDuTotal = new BigDecimal(0);
        for (CALigneDePoste ligneDePoste : lignesDePosteList) {
            montantDuTotal = montantDuTotal.add(ligneDePoste.getMontantDu());
        }

        return montantDuTotal;
    }

    public String getNumeroRubriqueIrrecouvrable() {
        return numeroRubriqueIrrecouvrable;
    }

    public String getOrdrePriorite() {
        return ordrePriorite;
    }

    public CATypeLigneDePoste getType() {
        return type;
    }

    public boolean isPosteOnError() {
        return isPosteOnError;
    }

    public boolean isRubriqueIndetermine() {
        return isRubriqueIndetermine;
    }

    public void setPosteOnError(boolean isPosteOnError) {
        this.isPosteOnError = isPosteOnError;
    }

    @Override
    public String toString() {
        String result = "";
        result += "           idRubrique                  : " + idRubrique + "\n";
        result += "           annee                       : " + annee + "\n";
        result += "           isRubriqueIndeterminee      : " + isRubriqueIndetermine + "\n";
        result += "           idRubriqueIrrecouvrable     : " + idRubriqueIrrecouvrable + "\n";
        result += "           numeroRubriqueIrrecouvrable : " + numeroRubriqueIrrecouvrable + "\n";
        result += "           libelleRubriqueIrrecouvrable: " + libelleRubriqueIrrecouvrable + "\n";
        result += "           type          : " + type + "\n";
        result += "           ordrePriorite : " + ordrePriorite + "\n";
        result += "           isPosteOnError: " + isPosteOnError + "\n";
        result += "           lignesDePost  : \n";
        for (CALigneDePoste ligneDePoste : lignesDePosteList) {
            result += ligneDePoste.toString();
        }
        return result;
    }
}
