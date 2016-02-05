package globaz.apg.module.calcul;

import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import java.math.BigDecimal;
import com.google.common.base.Preconditions;

/**
 * Classe encapsulant les valeurs utilisées pour la calcul et paramétrable
 */
public class APCalculParametresAMAT {

    private static final String CLE_MONTANT_MAX_MATERNITE = "M_MAXGROS";
    private static final String CLE_MONTANT_MIN_MATERNITE = "M_MINGROS";
    private static final String CS_MONTANT_MATERNITE = "0";
    private static final int NBRE_DIGIT_MONTANT_MATERNITE = 2;
    private static final String VALEUR = "";

    private BigDecimal montantMax;
    private BigDecimal montantMin;

    /**
     * 
     * @param transaction la transaction permettant d'aller chercher les informations en db
     * @param dateDebut la date de debut de la prestations detreminant la periode du parametres a aller chercher en db
     * @return une instance de {@link APCalculParametresAMAT}
     * @throws Exception parce que le FW le veut
     * @throws NullPointerException si paramptres null
     * @throws IllegalArgumentException si date pas au format Jade
     */
    public APCalculParametresAMAT getParametres(BTransaction transaction, String dateDebut) throws Exception {

        Preconditions.checkNotNull(transaction, "The transaction passed as argument can't be null");
        Preconditions.checkNotNull(dateDebut, "The dateDebut passed as argument can't be null");
        Preconditions.checkArgument(JadeDateUtil.isGlobazDate(dateDebut));

        montantMax = new BigDecimal(FWFindParameter.findParameter(transaction, CS_MONTANT_MATERNITE,
                CLE_MONTANT_MAX_MATERNITE, dateDebut, VALEUR, NBRE_DIGIT_MONTANT_MATERNITE));
        montantMin = new BigDecimal(FWFindParameter.findParameter(transaction, CS_MONTANT_MATERNITE,
                CLE_MONTANT_MIN_MATERNITE, dateDebut, VALEUR, NBRE_DIGIT_MONTANT_MATERNITE));

        return this;
    }

    public BigDecimal getMontantMax() {
        return montantMax;
    }

    public void setMontantMax(BigDecimal montantMax) {
        this.montantMax = montantMax;
    }

    public BigDecimal getMontantMin() {
        return montantMin;
    }

    public void setMontantMin(BigDecimal montantMin) {
        this.montantMin = montantMin;
    }

}
