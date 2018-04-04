/*
 * Créé le 23 mai 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.db.PRAbstractManager;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APSituationProfessionnelleManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forHasNoRevenu = Boolean.FALSE;
    private String forIdDroit = "";
    private Boolean isAllocationMax = null;
    private String notForIdSituationProfessionnelle = "";
    private String forIdSituationProfessionnelle = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APSituationProfessionnelle.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdSituationProfessionnelle)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), notForIdSituationProfessionnelle);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdSituationProfessionnelle)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdSituationProfessionnelle);
        }

        if (isAllocationMax != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APSituationProfessionnelle.FIELDNAME_ISALLOCATIONMAX + "="
                    + _dbWriteBoolean(statement.getTransaction(), isAllocationMax, BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (forHasNoRevenu.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " ( ";

            sqlWhere += APSituationProfessionnelle.FIELDNAME_REVENUINDEPENDANT;
            sqlWhere += " = ";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");

            sqlWhere += " AND ";

            sqlWhere += APSituationProfessionnelle.FIELDNAME_SALAIREHORAIRE;
            sqlWhere += " = ";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");

            sqlWhere += " AND ";

            sqlWhere += APSituationProfessionnelle.FIELDNAME_SALAIREMENSUEL;
            sqlWhere += " = ";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");

            sqlWhere += " AND ";

            sqlWhere += APSituationProfessionnelle.FIELDNAME_AUTRSALAIRE;
            sqlWhere += " = ";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");

            sqlWhere += " AND ";

            sqlWhere += APSituationProfessionnelle.FIELDNAME_ISALLOCATIONMAX;
            sqlWhere += " <> ";
            sqlWhere += _dbWriteBoolean(statement.getTransaction(), Boolean.TRUE, BConstants.DB_TYPE_BOOLEAN_CHAR);
            ;

            sqlWhere += " ) ";
        }

        return sqlWhere;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationProfessionnelle();
    }

    /**
     * @return
     */
    public Boolean getForHasNoRevenu() {
        return forHasNoRevenu;
    }

    /**
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut is allocation max
     * 
     * @return la valeur courante de l'attribut is allocation max
     */
    public String getIsAllocationMax() {
        return (isAllocationMax != null) ? isAllocationMax.toString() : "";
    }

    /**
     * getter pour l'attribut not for id situation professionnelle
     * 
     * @return la valeur courante de l'attribut not for id situation professionnelle
     */
    public String getNotForIdSituationProfessionnelle() {
        return notForIdSituationProfessionnelle;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF;
    }

    public BigDecimal getRevenuAnnuelSituationsProfessionnelles() throws Exception {

        if (this == null || size() == 0) {
            throw new Exception();
        }

        BigDecimal salaireHoraire = new BigDecimal(0);
        BigDecimal nbHeuresSemaine = new BigDecimal(0);
        BigDecimal salaireMensuel = new BigDecimal(0);
        BigDecimal autreSalaireSem = new BigDecimal(0);
        BigDecimal autreSalaireAnn = new BigDecimal(0);
        BigDecimal autreRemuneration = new BigDecimal(0);
        BigDecimal salaireNature = new BigDecimal(0);
        BigDecimal revenuIndependant = new BigDecimal(0);

        BigDecimal revenuAnnuel = new BigDecimal(0);
        revenuAnnuel.setScale(10);

        BigDecimal revenuJournalier = new BigDecimal(0);
        revenuJournalier.setScale(10);

        BigDecimal revenuIntermediaire = new BigDecimal(0);
        revenuIntermediaire.setScale(10);

        for (Iterator iter = iterator(); iter.hasNext();) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) iter.next();

            // salaire horaire
            if ((sitPro.getSalaireHoraire() != null) && !JadeStringUtil.isDecimalEmpty(sitPro.getSalaireHoraire())) {

                salaireHoraire = new BigDecimal(sitPro.getSalaireHoraire());
                nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());

                revenuIntermediaire = salaireHoraire.multiply(nbHeuresSemaine);
                revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(7), 10, BigDecimal.ROUND_DOWN);

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);
            }

            // salaire mensuel
            if ((sitPro.getSalaireMensuel() != null) && !JadeStringUtil.isDecimalEmpty(sitPro.getSalaireMensuel())) {

                salaireMensuel = new BigDecimal(sitPro.getSalaireMensuel());
                revenuIntermediaire = salaireMensuel.divide(new BigDecimal(30), 10, BigDecimal.ROUND_DOWN);

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);

            }

            // autre salaire
            if ((sitPro.getAutreSalaire() != null) && !JadeStringUtil.isDecimalEmpty(sitPro.getAutreSalaire())) {

                // si périodicité 4 semaines
                if (sitPro.getPeriodiciteAutreSalaire().equals(IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                    autreSalaireSem = new BigDecimal(sitPro.getAutreSalaire());
                    revenuIntermediaire = autreSalaireSem.divide(new BigDecimal(28), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité Année
                else if (sitPro.getPeriodiciteAutreSalaire().equals(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                    autreSalaireAnn = new BigDecimal(sitPro.getAutreSalaire());
                    revenuIntermediaire = autreSalaireAnn.divide(new BigDecimal(360), 10, BigDecimal.ROUND_DOWN);
                }

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);
            }

            // autre rémunération
            if ((sitPro.getAutreRemuneration() != null)
                    && !JadeStringUtil.isDecimalEmpty(sitPro.getAutreRemuneration())) {

                autreRemuneration = new BigDecimal(sitPro.getAutreRemuneration());

                // si %
                if (sitPro.getIsPourcentAutreRemun().booleanValue()) {

                    // Le "BigDecimal" qui n'est pas à zéro représente le
                    // montant sur lequel le % sera calculé
                    if (!JadeStringUtil.isDecimalEmpty(salaireHoraire.toString())) {
                        revenuIntermediaire = salaireHoraire.divide(new BigDecimal(100), 10, BigDecimal.ROUND_DOWN);
                        revenuIntermediaire = revenuIntermediaire.multiply(new BigDecimal(sitPro
                                .getPourcentAutreRemun()));

                        revenuIntermediaire = revenuIntermediaire.multiply(nbHeuresSemaine);
                        revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(7), 10, BigDecimal.ROUND_DOWN);

                    }

                    else if (!JadeStringUtil.isDecimalEmpty(salaireMensuel.toString())) {
                        revenuIntermediaire = salaireMensuel.divide(new BigDecimal(100), 10, BigDecimal.ROUND_DOWN);
                        revenuIntermediaire = revenuIntermediaire.multiply(new BigDecimal(sitPro
                                .getPourcentAutreRemun()));

                        revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(30), 10, BigDecimal.ROUND_DOWN);
                    }

                    else if (!JadeStringUtil.isDecimalEmpty(autreSalaireSem.toString())) {
                        revenuIntermediaire = autreSalaireSem.divide(new BigDecimal(100), 10, BigDecimal.ROUND_DOWN);
                        revenuIntermediaire = revenuIntermediaire.multiply(new BigDecimal(sitPro
                                .getPourcentAutreRemun()));

                        revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(28), 10, BigDecimal.ROUND_DOWN);
                    }

                    else if (!JadeStringUtil.isDecimalEmpty(autreSalaireAnn.toString())) {
                        revenuIntermediaire = autreSalaireAnn.divide(new BigDecimal(100), 10, BigDecimal.ROUND_DOWN);
                        revenuIntermediaire = revenuIntermediaire.multiply(new BigDecimal(sitPro
                                .getPourcentAutreRemun()));

                        revenuIntermediaire = revenuIntermediaire
                                .divide(new BigDecimal(360), 10, BigDecimal.ROUND_DOWN);

                    }

                    revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                    revenuIntermediaire = new BigDecimal(0);
                }

                // si périodicité horaire
                else if (sitPro.getPeriodiciteAutreRemun().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                    nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                    revenuIntermediaire = autreRemuneration.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(7), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité mois
                else if (sitPro.getPeriodiciteAutreRemun().equals(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                    revenuIntermediaire = autreRemuneration.divide(new BigDecimal(30), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité 4 semaines
                else if (sitPro.getPeriodiciteAutreRemun()
                        .equals(IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                    revenuIntermediaire = autreRemuneration.divide(new BigDecimal(28), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité année
                else if (sitPro.getPeriodiciteAutreRemun().equals(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                    revenuIntermediaire = autreRemuneration.divide(new BigDecimal(360), 10, BigDecimal.ROUND_DOWN);
                }

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);
            }

            // salaire nature
            if ((sitPro.getSalaireNature() != null) && !JadeStringUtil.isDecimalEmpty(sitPro.getSalaireNature())) {

                salaireNature = new BigDecimal(sitPro.getSalaireNature());

                // si périodicité horaire
                if (sitPro.getPeriodiciteSalaireNature().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                    nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                    revenuIntermediaire = salaireNature.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal(7), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité mois
                else if (sitPro.getPeriodiciteSalaireNature().equals(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                    revenuIntermediaire = salaireNature.divide(new BigDecimal(30), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité 4 semaines
                else if (sitPro.getPeriodiciteSalaireNature().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                    revenuIntermediaire = salaireNature.divide(new BigDecimal(28), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité année
                else if (sitPro.getPeriodiciteSalaireNature().equals(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                    revenuIntermediaire = salaireNature.divide(new BigDecimal(360), 10, BigDecimal.ROUND_DOWN);
                }

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);
            }

            // revenu indépendant
            if ((sitPro.getRevenuIndependant() != null)
                    && !JadeStringUtil.isDecimalEmpty(sitPro.getRevenuIndependant())) {

                revenuIndependant = new BigDecimal(sitPro.getRevenuIndependant());

                revenuIntermediaire = revenuIndependant.divide(new BigDecimal(360), 10, BigDecimal.ROUND_DOWN);

                revenuJournalier = revenuJournalier.add(revenuIntermediaire);
                revenuIntermediaire = new BigDecimal(0);
            }

        }

        // Transformation du revenuJournalier en revenuAnnuel
        revenuAnnuel = revenuJournalier.multiply(new BigDecimal(360));

        return revenuAnnuel;
    }

    /**
     * @param boolean1
     */
    public void setForHasNoRevenu(Boolean boolean1) {
        forHasNoRevenu = boolean1;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut is allocation max
     * 
     * @param isAllocationMax
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationMax(String isAllocationMax) {
        this.isAllocationMax = JadeStringUtil.isEmpty(isAllocationMax) ? null : Boolean.valueOf(isAllocationMax);
    }

    /**
     * setter pour l'attribut not for id situation professionnelle
     * 
     * @param notForIdSituationProfessionnelle
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdSituationProfessionnelle(String notForIdSituationProfessionnelle) {
        this.notForIdSituationProfessionnelle = notForIdSituationProfessionnelle;
    }

    public String getForIdSituationProfessionnelle() {
        return forIdSituationProfessionnelle;
    }

    public void setForIdSituationProfessionnelle(String forIdSituationProfessionnelle) {
        this.forIdSituationProfessionnelle = forIdSituationProfessionnelle;
    }

}
