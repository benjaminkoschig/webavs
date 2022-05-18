/*
 * mmu Cr�� le 17 oct. 05
 */
package globaz.hera.impl.standard;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuRelationConjoint;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author mmu
 * <p>
 * 17 oct. 05
 */
public class SFRelationFamilialeStd extends SFApercuRelationConjoint {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String date = null;
    private String typeLien = null;

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * La date de d�but du type de relation
     *
     * @return null en cas d'exception
     * @see globaz.hera.db.famille.SFRelationConjoint#getDateDebut()
     */
    @Override
    public String getDateDebut() {
        // si le type de lien est de type veuf,
        // la date de d�but correspond � la date de d�c�s d'un des conjoints + 1
        // jour
        if (ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(getTypeLien())
                || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(getTypeLien())) {
            try {

                JACalendarGregorianStandard calendar = new JACalendarGregorianStandard();
                return calendar.addDays(new JADate(getDateFinRelation()), 1).toStr(".");
            } catch (Exception e) {
                return null;
            }
        } else {
            return super.getDateDebut();
        }
    }

    /**
     * Renvoie la date de d�but effective de la relation (pas du type de lien). En effet, une relation mariage peut
     * g�n�rer deux liens: mari� et veuf, avec comme date de d�but de veuvage la date de d�but de la relation.
     * <p>
     * Pour un lien veuf retourn la date de d�but de mariage
     *
     * @return
     */
    public String getDateDebutRelation() {
        return super.getDateDebut();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.hera.db.famille.SFRelationConjoint#getDateFin()
     */
    @Override
    public String getDateFin() {
        return super.getDateFin();
    }

    public String getTypeLien() {
        return getTypeLien(date);
    }

    /**
     * Donne le type de lien � une date donn�e
     *
     * @return Code System de ISFRelationFamiliale.CS_TYPE_LIEN ou null pour les autres types ou en cas d'erreur
     */
    /*
     * Bien que cette methode ait en parametre une date, le type de lien n'est calcul� qu'une seule fois, car il est
     * fonction d'une date
     */
    public String getTypeLien(String date) {
        if (typeLien != null) {
            return typeLien;
        }
        String typeRel = getTypeRelation();
        // divorc�

        if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(typeRel)) {
            typeLien = ISFSituationFamiliale.CS_TYPE_LIEN_DIVORCE;
        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(typeRel)) {
            typeLien = ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT;

            // mari�, s�par� ou veuf
            // si un d�c�s intervient pendant un mariage ou une s�paration la
            // relation est veuf
        } else if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(typeRel)) {

            String dateFinMariage = "31.12.2999";
            String datePourTraitement = "31.12.2999";

            if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
                dateFinMariage = getDateFin();
            }

            if (!JadeStringUtil.isBlankOrZero(date)) {
                datePourTraitement = date;
            }

            // un des conjoints meurt avant la date --> veuf
            try {
                // Si un d�c�s intervient avant la fin de mariage et avant la
                // date donn�e --> veuf
                if (!JadeStringUtil.isBlankOrZero(getDateDeces1())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces1(), getDateFin())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces1(), date)
                        || !JadeStringUtil.isBlankOrZero(getDateDeces2())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces2(), getDateFin())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces2(), date)) {

                        typeLien = ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES;
                } else {
                    // sinon mariage
                    typeLien = relConjToTypeLien(typeRel);
                }
            } catch (Exception e) {
                return null; // ne devraient pas arriver � ce point, sauf si la
                // date est effectivement mal formatee
            }

        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(typeRel)) {

            String dateFinMariage = "31.12.2999";
            String datePourTraitement = "31.12.2999";

            if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
                dateFinMariage = getDateFin();
            }

            if (!JadeStringUtil.isBlankOrZero(date)) {
                datePourTraitement = date;
            }

            // un des conjoints meurt avant la date --> veuf
            try {
                // Si un d�c�s intervient avant la fin de mariage et avant la
                // date donn�e --> veuf
                if (!JadeStringUtil.isBlankOrZero(getDateDeces1())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces1(), getDateFin())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces1(), date)
                        || !JadeStringUtil.isBlankOrZero(getDateDeces2())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces2(), getDateFin())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces2(), date)) {

                        typeLien = ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES;
                } else {
                    // sinon mariage
                    typeLien = relConjToTypeLien(typeRel);
                }
            } catch (Exception e) {
                return null; // ne devraient pas arriver � ce point, sauf si la
                // date est effectivement mal formatee
            }

        } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(typeRel)
                || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(typeRel)
                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(typeRel)
                || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(typeRel)) {
            typeLien = relConjToTypeLien(typeRel);
        }
        // autre
        else {
            return null;
        }
        return typeLien;
    }

    /**
     * Vrais si les deux partenaires donnes sont du meme sexe
     *
     * @param idMembreFamille1
     * @param idMembreFamille2
     * @return
     * @throws Exception
     */
    private boolean isPartenariatEntrePersonneDuMemeSexe(String idMF1, String idMF2) {

        try {
            SFMembreFamille mf1 = new SFMembreFamille();
            mf1.setSession(getSession());
            mf1.setIdMembreFamille(idMF1);
            mf1.retrieve();

            SFMembreFamille mf2 = new SFMembreFamille();
            mf2.setSession(getSession());
            mf2.setIdMembreFamille(idMF2);
            mf2.retrieve();

            String sexeMF1 = mf1.getCsSexe();
            String sexeMF2 = mf2.getCsSexe();

            return !JadeStringUtil.isIntegerEmpty(sexeMF1) && !JadeStringUtil.isIntegerEmpty(sexeMF2)
                    && sexeMF1.equals(sexeMF2);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSameRelation(SFRelationFamilialeStd rel2) {
        if (getIdConjoints().equals(rel2.getIdConjoints())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Traduit les Relations Conjugale en Type de Lien
     */
    public String relConjToTypeLien(String relationConjugale) {

        if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_MARIE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_LPART_SEPARE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_LPART_SEPARE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_DIVORCE;

        } else if (ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(relationConjugale)) {
            return ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT;

        } else {
            return null;
        }
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param string
     */
    public void setTypeLien(String string) {
        typeLien = string;
    }

}
