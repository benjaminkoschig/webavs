/*
 * Créé le 13 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.irrecouvrables;

import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <H1>Description</H1>
 * <p>
 * Une classe qui reprend les données de ventilation retournées par Osiris et les réorganisent de manière à faciliter
 * l'affichage dans l'écran de ventilation des irrécouvrables.
 * </p>
 * 
 * @author vre
 */
public class CORecouvrementPoste implements Serializable, Comparable<Object> {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ID_EXTERNE_BEGIN_WITH_2110_3300 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_3300
            .substring(0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_3300.substring(5, 9) + ")(.)*";
    private boolean cotisationsPersonnelles;
    private String idRubriqueRecouvrement;
    private String libellePoste;

    private BigDecimal montantAmortissement = new BigDecimal(0);

    private BigDecimal montantDejaRecouvert = new BigDecimal(0);
    private BigDecimal montantRecouvrement = new BigDecimal(0);
    private BigDecimal montantSolde = new BigDecimal(0);
    private int ordre;

    /**
     * @param rubriqueRecouvrement
     * @param ordre
     */
    public CORecouvrementPoste(CARubrique rubriqueRecouvrement, String ordre) {
        idRubriqueRecouvrement = rubriqueRecouvrement.getIdRubrique();
        libellePoste = rubriqueRecouvrement.getDescription();
        this.ordre = Integer.parseInt(ordre);
        Pattern pattern = Pattern.compile(CORecouvrementPoste.ID_EXTERNE_BEGIN_WITH_2110_3300);
        Matcher matcher = pattern.matcher(rubriqueRecouvrement.getIdExterne());
        cotisationsPersonnelles = matcher.matches();
    }

    @Override
    public int compareTo(Object arg0) {
        CORecouvrementPoste poste = (CORecouvrementPoste) arg0;

        if (ordre < poste.ordre) {
            return -1;
        } else if (ordre > poste.ordre) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the idRubriqueRecouvrement
     */
    public String getIdRubriqueRecouvrement() {
        return idRubriqueRecouvrement;
    }

    public String getLibellePoste() {
        return libellePoste;
    }

    public BigDecimal getMontantSolde() {
        if (montantSolde == null) {
            if ((montantAmortissement == null) || (montantDejaRecouvert == null) || (montantRecouvrement == null)) {
                throw new IllegalStateException(
                        "Le montant d'amortissement, le montant recouvert ou le montant de recouvrement n'est pas renseigné, impossible de calculer le solde");
            }

            montantSolde = montantAmortissement.subtract(montantDejaRecouvert);
            montantSolde = montantSolde.subtract(montantRecouvrement);
        }

        return montantSolde;
    }

    public String getMontantSoldeFormatte() {
        return JANumberFormatter.format(getMontantSolde());
    }

    public int getOrdre() {
        return ordre;
    }

    public boolean isCotisationsPersonnelles() {
        return cotisationsPersonnelles;
    }

    /** recalcule les sommes des montants verses et irrecouvrables. */
    public void resetMontants() {
        // TODO sch 28 mai 2014 : Montants à initialiser
    }

    /**
     * @param idRubriqueRecouvrement
     *            the idRubriqueRecouvrement to set
     */
    public void setIdRubriqueRecouvrement(String idRubriqueRecouvrement) {
        this.idRubriqueRecouvrement = idRubriqueRecouvrement;
    }

    /**
     * @param ordre
     */
    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
