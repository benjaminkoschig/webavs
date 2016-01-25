/*
 * Créé le 7 nov. 05
 */
package globaz.ij.vb.annonces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJAnnonce3EmeRevisionViewBean extends IJAnnonceViewBean implements FWViewBeanInterface {

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

        // certains champs doivent être vides dans le cas d'une carte
        // rectificative
        if ("3".equals(getCodeGenreCarte()) || "4".equals(getCodeGenreCarte())) {
            if (!JadeStringUtil.isEmpty(getCodeEtatCivil())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_ETAT_CIVIL"));
            }

            if (!JadeStringUtil.isEmpty(getNombreEnfants())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_NOMBRE_ENFANTS"));
            }

            if (!JadeStringUtil.isEmpty(getRevenuJournalierDeterminant())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_REVENU_JOURNALIER_MOYEN_EFFECTIF"));
            }

            if (!JadeStringUtil.isEmpty(getOfficeAI())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_OFFICE_AI"));
            }

            if (!getParamSpecifique3emeRevisionSur5Positions().equals("     ")) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_TOUT_LES_TRUCS"));
            }

            if (!JadeStringUtil.isEmpty(getPetiteIJ())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_PETITE_IJ"));
            }

            if (!JadeStringUtil.isEmpty(getCodeGenreReadaptation())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_GENRE_READAPTATION"));
            }

            if (!JadeStringUtil.isEmpty(getGarantieAA())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_GARANTIE_AA"));
            }

            if (!JadeStringUtil.isEmpty(getIjReduite())) {
                _addError(transaction, session.getLabel("ERREUR_ANNONCE_RESTITUTION_IJ_REDUITE"));
            }
        }
    }

    /**
     * getter pour l'attribut periode a
     * 
     * @return la valeur courante de l'attribut periode a
     */
    public String getPeriodeA() {
        return getPeriodeAnnonce1().getPeriodeA();
    }

    /**
     * getter pour l'attribut periode de
     * 
     * @return la valeur courante de l'attribut periode de
     */
    public String getPeriodeDe() {
        return getPeriodeAnnonce1().getPeriodeDe();
    }

    /**
     * getter pour l'attribut allocation assistance
     * 
     * @return la valeur courante de l'attribut allocation assistance
     */
    public String isAllocationAssistance() {
        return getParamSpecifique3emeRevisionSur5Positions().substring(2, 3).trim();
    }

    /**
     * getter pour l'attribut allocation exploitation
     * 
     * @return la valeur courante de l'attribut allocation exploitation
     */
    public String isAllocationExploitation() {
        return getParamSpecifique3emeRevisionSur5Positions().substring(3, 4).trim();
    }

    /**
     * getter pour l'attribut allocation menage
     * 
     * @return la valeur courante de l'attribut allocation menage
     */
    public String isAllocationMenage() {
        return getParamSpecifique3emeRevisionSur5Positions().substring(1, 2).trim();
    }

    /**
     * getter pour l'attribut allocation personne seule
     * 
     * @return la valeur courante de l'attribut allocation personne seule
     */
    public String isAllocationPersonneSeule() {
        return getParamSpecifique3emeRevisionSur5Positions().substring(0, 1).trim();
    }

    /**
     * setter pour l'attribut allocation assistance
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationAssistance(String b) {
        if ("".equals(b)) {
            b = " ";
        }

        setParamSpecifique3emeRevisionSur5Positions(PRStringUtils.replaceStringIn(
                getParamSpecifique3emeRevisionSur5Positions(), 2, 3, b));
    }

    /**
     * setter pour l'attribut allocation exploitation
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationExploitation(String b) {
        if ("".equals(b)) {
            b = " ";
        }

        setParamSpecifique3emeRevisionSur5Positions(PRStringUtils.replaceStringIn(
                getParamSpecifique3emeRevisionSur5Positions(), 3, 4, b));
    }

    /**
     * setter pour l'attribut allocation menage
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationMenage(String b) {
        if ("".equals(b)) {
            b = " ";
        }

        setParamSpecifique3emeRevisionSur5Positions(PRStringUtils.replaceStringIn(
                getParamSpecifique3emeRevisionSur5Positions(), 1, 2, b));
    }

    /**
     * setter pour l'attribut allocation personne seule
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationPersonneSeule(String b) {
        if ("".equals(b)) {
            b = " ";
        }

        setParamSpecifique3emeRevisionSur5Positions(PRStringUtils.replaceStringIn(
                getParamSpecifique3emeRevisionSur5Positions(), 0, 1, b));
    }

    /**
     * setter pour l'attribut periode a
     * 
     * @param periodeA
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeA(String periodeA) {
        getPeriodeAnnonce1().setPeriodeA(periodeA);
    }

    /**
     * setter pour l'attribut periode de
     * 
     * @param periodeDe
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeDe(String periodeDe) {
        getPeriodeAnnonce1().setPeriodeDe(periodeDe);
    }
}
