package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * <p>
 * Objet immuable contenant les donn�es pour l'affichage de l'aper�u (une ligne) d'une rente vers�e � tort dans l'�cran
 * des rentes vers�e � tort
 * </p>
 * <p>
 * Chaque valeur d'entr�e est test� dans le constructeur afin qu'elle ne soit pas <code>null</code>
 * </p>
 * <p>
 * Cette objet est sp�cialis� pour la vue, et contient de ce fait quelques m�thodes sp�cifiques � cet effet
 * </p>
 */
public class RELigneApercuRenteVerseeATort implements Comparable<RELigneApercuRenteVerseeATort> {

    private CodePrestation codePrestation;
    private Long idRenteAncienDroit;
    private Long idRenteNouveauDroit;
    private Long idRenteVerseeATort;
    private BigDecimal montant;
    private Periode periode;
    private boolean saisieManuelle;
    private TypeRenteVerseeATort typeRenteVerseeATort;

    /**
     * Construit cet objet en v�rifiant que les param�tres d'entr�es ne soit pas null. Lance une
     * {@link IllegalArgumentException} si c'est le cas
     * 
     * @param periode
     *            la p�riode durant laquelle une rente a �t� vers�e � tort
     * @param montant
     *            le montant total vers�e � tort durant la p�riode
     * @param idRenteAncienDroit
     *            l'ID (en BDD) de la rente de l'ancien droit ayant permis le calcul de cette rente vers�e � tort
     * @param idRenteNouveauDroit
     *            l'ID (en BDD) de la rente du nouveau droit ayant permis le calcul de cette rente vers�e � tort
     * @param idRenteVerseeATort
     *            l'ID (en BDD) de cette rente vers�e � tort
     * @param typeRenteVerseeATort
     *            le type de cette rente vers�e � tort
     */
    public RELigneApercuRenteVerseeATort(CodePrestation codePrestation, Periode periode, BigDecimal montant,
            Long idRenteAncienDroit, Long idRenteNouveauDroit, Long idRenteVerseeATort,
            TypeRenteVerseeATort typeRenteVerseeATort, boolean saisieManuelle) {
        super();

        Checkers.checkNotNull(codePrestation, "codePrestation");
        Checkers.checkNotNull(periode, "periode");
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(idRenteVerseeATort, "idRenteVerseeATort");
        Checkers.checkNotNull(typeRenteVerseeATort, "typeRenteVerseeATort");

        this.codePrestation = codePrestation;
        this.idRenteAncienDroit = idRenteAncienDroit;
        this.idRenteNouveauDroit = idRenteNouveauDroit;
        this.idRenteVerseeATort = idRenteVerseeATort;
        this.periode = periode;
        this.montant = montant;
        this.typeRenteVerseeATort = typeRenteVerseeATort;
        this.saisieManuelle = saisieManuelle;
    }

    @Override
    public int compareTo(RELigneApercuRenteVerseeATort o) {
        int compare = codePrestation.compareTo(o.codePrestation);

        if (compare == 0) {
            compare = periode.compareTo(o.periode);
        }
        if (compare == 0) {
            compare = idRenteVerseeATort.compareTo(o.idRenteVerseeATort);
        }

        return compare;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RELigneApercuRenteVerseeATort) {
            return idRenteVerseeATort.equals(((RELigneApercuRenteVerseeATort) obj).idRenteVerseeATort);
        }
        return false;
    }

    public int getCodePrestation() {
        return codePrestation.getCodePrestation();
    }

    /**
     * @return la date de d�but de cette rente vers�e � tort
     */
    public String getDateDebut() {
        return periode.getDateDebut();
    }

    /**
     * @return la date de fin de cette rente vers�e � tort
     */
    public String getDateFin() {
        return periode.getDateFin();
    }

    /**
     * @return <code>true</code> si la rente vers�e � tort a �t� cr��e par le m�canisme de saisie manuelle
     */
    public boolean getEstUneSaisieManuelle() {
        return saisieManuelle;
    }

    /**
     * @return l'ID (en BDD) de la rente de l'ancien droit ayant permis le calcul de cette rente vers�e � tort
     */
    public Long getIdRenteAncienDroit() {
        return idRenteAncienDroit;
    }

    /**
     * @return l'ID (en BDD) de la rente du nouveau droit ayant permis le calcul de cette rente vers�e � tort
     */
    public Long getIdRenteNouveauDroit() {
        return idRenteNouveauDroit;
    }

    /**
     * @return l'ID (en BDD) de cette rente vers�e � tort
     */
    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    /**
     * @return le montant total vers�e � tort durant la p�riode
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return le montant total vers�e � tort durant la p�riode mise en forme par la classe {@link FWCurrency}
     */
    public String getMontantFormatte() {
        return new FWCurrency(getMontant().doubleValue()).toStringFormat();
    }

    /**
     * @return la p�riode durant laquelle une rente a �t� vers�e � tort
     */
    public Periode getPeriode() {
        return periode;
    }

    /**
     * @return le type de cette rente vers�e � tort
     */
    public TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(this.getClass().getName()).append("(id:").append(getIdRenteVerseeATort()).append(")");
        return super.toString();
    }
}
