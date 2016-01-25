/*
 * Créé le 7 nov. 05
 */
package globaz.ij.vb.annonces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJAnnonce4EmeRevisionViewBean extends IJAnnonceViewBean implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);

        BTransaction transaction = statement.getTransaction();
        BSession session = getSession();

        // certain champs pour les cartes rectificatives doivent être à blanc
        if ("3".equals(getCodeGenreCarte()) || "4".equals(getCodeGenreCarte())) {
            if (!JadeStringUtil.isEmpty(getCodeEtatCivil())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_ETAT_CIVIL"));
            }

            if (!JadeStringUtil.isEmpty(getNombreEnfants())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_NOMBRE_ENFANT"));
            }

            if (!JadeStringUtil.isEmpty(getRevenuJournalierDeterminant())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_REVENU_JOURNALIER_DETERMINANT"));
            }

            if (!JadeStringUtil.isEmpty(getCodeGenreReadaptation())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_GENRE_READAPTATION"));
            }

            if (!JadeStringUtil.isEmpty(getOfficeAI())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_OFFICE_AI"));
            }

            if (!JadeStringUtil.isEmpty(getGarantieAA())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_GARANTIE_AA"));
            }

            if (!JadeStringUtil.isEmpty(getIjReduite())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_IJ_REDUITE"));
            }

            if (!JadeStringUtil.isEmpty(getDeductionNourritureLogementPeriode1())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_DEDUCTION_NOURRITURE_LOGEMENT"));
            }

            if (!JadeStringUtil.isEmpty(getNombreJoursInterruptionPeriode1())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_NOMBRE_JOURS_INTERRUPTION"));
            }

            if (!JadeStringUtil.isBlankOrZero(getCodeMotifInterruptionPeriode1())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_MOTIF_INTERRUPTION"));
            }

            if (!JadeStringUtil.isEmpty(getVersementIJPeriode1())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_VERSEMENT_IJ"));
            }

            if (isDeuxiemePeriode()) {
                if (!JadeStringUtil.isEmpty(getDeductionNourritureLogementPeriode2())) {
                    _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_DEDUCTION_NOURRITURE_LOGEMENT"));
                }

                if (!JadeStringUtil.isEmpty(getNombreJoursInterruptionPeriode2())) {
                    _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_NOMBRE_JOURS_INTERRUPTION"));
                }

                if (!JadeStringUtil.isBlankOrZero(getCodeMotifInterruptionPeriode2())) {
                    _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_MOTIF_INTERRUPTION"));
                }

                if (!JadeStringUtil.isEmpty(getVersementIJPeriode2())) {
                    _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_VERSEMENT_IJ"));
                }
            }
        }
    }

    /**
     * getter pour l'attribut code motif interruption periode1
     * 
     * @return la valeur courante de l'attribut code motif interruption periode1
     */
    public String getCodeMotifInterruptionPeriode1() {
        return getPeriodeAnnonce1().getCodeMotifInterruption();
    }

    /**
     * getter pour l'attribut code motif interruption periode2
     * 
     * @return la valeur courante de l'attribut code motif interruption periode2
     */
    public String getCodeMotifInterruptionPeriode2() {
        return getPeriodeAnnonce2().getCodeMotifInterruption();
    }

    /**
     * getter pour l'attribut deduction nourriture logement periode1
     * 
     * @return la valeur courante de l'attribut deduction nourriture logement periode1
     */
    public String getDeductionNourritureLogementPeriode1() {
        return getPeriodeAnnonce1().getDeductionNourritureLogement();
    }

    /**
     * getter pour l'attribut deduction nourriture logement periode2
     * 
     * @return la valeur courante de l'attribut deduction nourriture logement periode2
     */
    public String getDeductionNourritureLogementPeriode2() {
        return getPeriodeAnnonce2().getDeductionNourritureLogement();
    }

    public String getDroitPrestationPourEnfantPeriode1() {
        return getPeriodeAnnonce1().getDroitPrestationPourEnfant();
    }

    public String getDroitPrestationPourEnfantPeriode2() {
        return getPeriodeAnnonce2().getDroitPrestationPourEnfant();
    }

    public String getGarantieDroitAcquis5emeRevisionPeriode1() {
        return getPeriodeAnnonce1().getGarantieDroitAcquis5emeRevision();
    }

    public String getGarantieDroitAcquis5emeRevisionPeriode2() {
        return getPeriodeAnnonce2().getGarantieDroitAcquis5emeRevision();
    }

    /**
     * getter pour l'attribut nombre jours interruption periode1
     * 
     * @return la valeur courante de l'attribut nombre jours interruption periode1
     */
    public String getNombreJoursInterruptionPeriode1() {
        return getPeriodeAnnonce1().getNombreJoursInterruption();
    }

    /**
     * getter pour l'attribut nombre jours interruption periode2
     * 
     * @return la valeur courante de l'attribut nombre jours interruption periode2
     */
    public String getNombreJoursInterruptionPeriode2() {
        return getPeriodeAnnonce2().getNombreJoursInterruption();
    }

    /**
     * getter pour l'attribut periode APeriode1
     * 
     * @return la valeur courante de l'attribut periode APeriode1
     */
    public String getPeriodeAPeriode1() {
        return getPeriodeAnnonce1().getPeriodeA();
    }

    /**
     * getter pour l'attribut periode APeriode2
     * 
     * @return la valeur courante de l'attribut periode APeriode2
     */
    public String getPeriodeAPeriode2() {
        return getPeriodeAnnonce2().getPeriodeA();
    }

    /**
     * getter pour l'attribut periode de periode1
     * 
     * @return la valeur courante de l'attribut periode de periode1
     */
    public String getPeriodeDePeriode1() {
        return getPeriodeAnnonce1().getPeriodeDe();
    }

    /**
     * getter pour l'attribut periode de periode2
     * 
     * @return la valeur courante de l'attribut periode de periode2
     */
    public String getPeriodeDePeriode2() {
        return getPeriodeAnnonce2().getPeriodeDe();
    }

    /**
     * getter pour l'attribut total IJSigne periode1
     * 
     * @return la valeur courante de l'attribut total IJSigne periode1
     */
    public String getTotalIJSignePeriode1() {

        if ("3".equals(getPetiteIJ())) {
            // AIT
            return (getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce1().getMontantAit();

        } else if ("4".equals(getPetiteIJ())) {
            // AA
            return (getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce1().getMontantAllocAssistance();
        } else {
            // petite et grande ij
            return (getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce1().getTotalIJ();
        }
    }

    /**
     * getter pour l'attribut total IJSigne periode2
     * 
     * @return la valeur courante de l'attribut total IJSigne periode2
     */
    public String getTotalIJSignePeriode2() {
        if ("3".equals(getPetiteIJ())) {
            // AIT
            return (getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce2().getMontantAit();

        } else if ("4".equals(getPetiteIJ())) {
            // AA
            return (getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce2().getMontantAllocAssistance();
        } else {
            // petite et grande ij
            return (getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "")
                    + getPeriodeAnnonce2().getTotalIJ();
        }
    }

    /**
     * getter pour l'attribut versement IJPeriode1
     * 
     * @return la valeur courante de l'attribut versement IJPeriode1
     */
    public String getVersementIJPeriode1() {
        return getPeriodeAnnonce1().getVersementIJ();
    }

    /**
     * getter pour l'attribut versement IJPeriode2
     * 
     * @return la valeur courante de l'attribut versement IJPeriode2
     */
    public String getVersementIJPeriode2() {
        return getPeriodeAnnonce2().getVersementIJ();
    }

    /**
     * setter pour l'attribut code motif interruption periode1
     * 
     * @param code
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeMotifInterruptionPeriode1(String code) {
        getPeriodeAnnonce1().setCodeMotifInterruption(code);
    }

    /**
     * setter pour l'attribut code motif interruption periode2
     * 
     * @param code
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeMotifInterruptionPeriode2(String code) {
        getPeriodeAnnonce2().setCodeMotifInterruption(code);
    }

    /**
     * setter pour l'attribut deduction nourriture logement periode1
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeductionNourritureLogementPeriode1(String b) {
        getPeriodeAnnonce1().setDeductionNourritureLogement(b);
    }

    /**
     * setter pour l'attribut deduction nourriture logement periode2
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeductionNourritureLogementPeriode2(String b) {
        getPeriodeAnnonce2().setDeductionNourritureLogement(b);
    }

    public void setDroitPrestationPourEnfantPeriode1(String value) {
        getPeriodeAnnonce1().setDroitPrestationPourEnfant(value);
    }

    public void setDroitPrestationPourEnfantPeriode2(String value) {
        getPeriodeAnnonce2().setDroitPrestationPourEnfant(value);
    }

    public void setGarantieDroitAcquis5emeRevisionPeriode1(String value) {
        getPeriodeAnnonce1().setGarantieDroitAcquis5emeRevision(value);
    }

    public void setGarantieDroitAcquis5emeRevisionPeriode2(String value) {
        getPeriodeAnnonce2().setGarantieDroitAcquis5emeRevision(value);
    }

    /**
     * setter pour l'attribut nombre jours interruption periode1
     * 
     * @param nbJours
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInterruptionPeriode1(String nbJours) {
        getPeriodeAnnonce1().setNombreJoursInterruption(nbJours);
    }

    /**
     * setter pour l'attribut nombre jours interruption periode2
     * 
     * @param nbJours
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInterruptionPeriode2(String nbJours) {
        getPeriodeAnnonce2().setNombreJoursInterruption(nbJours);
    }

    /**
     * setter pour l'attribut periode APeriode1
     * 
     * @param periode
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeAPeriode1(String periode) {
        getPeriodeAnnonce1().setPeriodeA(periode);
    }

    /**
     * setter pour l'attribut periode APeriode2
     * 
     * @param periode
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeAPeriode2(String periode) {
        getPeriodeAnnonce2().setPeriodeA(periode);
    }

    /**
     * setter pour l'attribut periode de periode1
     * 
     * @param periode
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeDePeriode1(String periode) {
        getPeriodeAnnonce1().setPeriodeDe(periode);
    }

    /**
     * setter pour l'attribut periode de periode2
     * 
     * @param periode
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeDePeriode2(String periode) {
        getPeriodeAnnonce2().setPeriodeDe(periode);
    }

    /**
     * setter pour l'attribut total IJSigne periode1
     * 
     * @param totalIJSigne
     *            une nouvelle valeur pour cet attribut
     */
    public void setTotalIJSignePeriode1(String totalIJSigne) {
        FWCurrency currencyTotalIJSigne = new FWCurrency(totalIJSigne);

        if ("3".equals(getPetiteIJ())) {
            // AIT
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce1().setMontantAit("");
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce1().setMontantAit(totalIJSigne);
            } else {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce1().setMontantAit(currencyTotalIJSigne.toString());
            }
        } else if ("4".equals(getPetiteIJ())) {
            // AA
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce1().setMontantAllocAssistance("");
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce1().setMontantAllocAssistance(totalIJSigne);
            } else {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce1().setMontantAllocAssistance(currencyTotalIJSigne.toString());
            }
        } else {
            // petite et grande ij
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce1().setTotalIJ("");
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce1().setTotalIJ(totalIJSigne);
            } else {
                getPeriodeAnnonce1().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce1().setTotalIJ(currencyTotalIJSigne.toString());
            }
        }
    }

    /**
     * setter pour l'attribut total IJSigne periode2
     * 
     * @param totalIJSigne
     *            une nouvelle valeur pour cet attribut
     */
    public void setTotalIJSignePeriode2(String totalIJSigne) {
        FWCurrency currencyTotalIJSigne = new FWCurrency(totalIJSigne);

        if ("3".equals(getPetiteIJ())) {
            // AIT
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce2().setMontantAit("");
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce2().setMontantAit(totalIJSigne);
            } else {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce2().setMontantAit(currencyTotalIJSigne.toString());
            }
        } else if ("4".equals(getPetiteIJ())) {
            // AA
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce2().setMontantAllocAssistance("");
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce2().setMontantAllocAssistance(totalIJSigne);
            } else {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce2().setMontantAllocAssistance(currencyTotalIJSigne.toString());
            }
        } else {
            // petite et grande ij
            if ("".equals(totalIJSigne)) {
                getPeriodeAnnonce2().setTotalIJ("");
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
            } else if (currencyTotalIJSigne.isPositive()) {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("0");
                getPeriodeAnnonce2().setTotalIJ(totalIJSigne);
            } else {
                getPeriodeAnnonce2().setCodeValeurTotalIJ("1");
                currencyTotalIJSigne.negate();
                getPeriodeAnnonce2().setTotalIJ(currencyTotalIJSigne.toString());
            }
        }
    }

    /**
     * setter pour l'attribut versement IJPeriode1
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementIJPeriode1(String s) {
        getPeriodeAnnonce1().setVersementIJ(s);
    }

    /**
     * setter pour l'attribut versement IJPeriode2
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementIJPeriode2(String s) {
        getPeriodeAnnonce2().setVersementIJ(s);
    }
}
