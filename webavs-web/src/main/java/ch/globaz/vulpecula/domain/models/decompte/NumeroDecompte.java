package ch.globaz.vulpecula.domain.models.decompte;

import ch.globaz.vulpecula.domain.constants.Constants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.ValueObject;

/**
 * Cr�ation d'un objet numero de d�compte correspondant au standard au niveau facturation.
 * Soit un entier sur 9 caract�res
 * Pour un employeur Mensuelle : 01-12
 * Pour un employeur Trimestrielle : 41-44
 * Pour un employeur Annuel : 40
 * Pour un compl�mentaire : 18
 * 
 * @since Web@BMS 0.01.01
 */
public class NumeroDecompte implements ValueObject {
    private static final long serialVersionUID = 1L;
    private static final String FORMAT_OFFSET = "%03d";
    private static final String OFFSET_FIRST = "000";
    private static final int OFFSET_INCREMENT = 10;

    public static final String PERIODIQUE_TRIMESTRIELLE = "4";
    public static final String PERIODIQUE_ANNUELLE = "40";
    public static final String COMPLEMENTAIRE = "36";
    public static final String CONTROLE_EMPLOYEUR = "17";
    public static final String SPECIAL = "18";

    /**
     * ATTENTION : En cas de modification du no� �a ne correspondra plus avec la classe APISection
     */
    public static final String ABSENCES_JUSTIFIEES = "91";
    public static final String CONGES_PAYES = "92";
    public static final String SERVICE_MILITAIRE = "93";

    private final String annee;
    private final String code;
    private final String offset;

    /**
     * Cr�ation d'un num�ro de d�compte � partir d'une cha�ne de caract�res.
     * Elle doit correspondre au format suivant(xxxxmmooo).
     * 
     * @param numeroDecompte String repr�sentant un num�ro de d�compte
     */
    public NumeroDecompte(final String numeroDecompte) {
        if (numeroDecompte.length() != 9) {
            throw new IllegalArgumentException("Le numero de d�compte " + numeroDecompte
                    + " doit disposer de 9 caract�res");
        }
        annee = numeroDecompte.substring(0, 4);
        code = numeroDecompte.substring(4, 6);
        offset = numeroDecompte.substring(6, 9);
    }

    /**
     * Cr�ation d'un num�ro de d�compte � partir d'un type de d�compte, d'une date et de la p�riodicit� d'un employeur.
     * 
     * @param typeDecompte Type de d�compte permettant de d�terminer le code
     * @param date Date � partir de laquelle l'ann�e sera d�termin�e
     * @param periodicite P�riodicit� de l'employeur
     * @param offset L'offset du num�ro (no) 010
     */
    public NumeroDecompte(final TypeDecompte typeDecompte, final Date date, final String periodicite,
            final String offset) {
        this(computeNumeroDecompte(typeDecompte, date, periodicite, offset));
    }

    /**
     * G�n�ration d'un num�ro de d�compte � partir des trois param�tres
     * 
     * @param annee Ann�e du num�ro
     * @param code Code du num�ro (li� au type d�compte)
     * @param no Offset du d�compte
     */
    public NumeroDecompte(final String annee, final String code, final String offset) {
        this.annee = annee;
        this.code = code;
        this.offset = offset;
    }

    /**
     * G�n�ration du num�ro de d�compte � partir de l'ann�e et du code.
     * 
     * @param annee Ann�e du num�ro
     * @param code du num�ro
     */
    public NumeroDecompte(final String annee, final String code) {
        this(annee, code, "000");
    }

    /**
     * G�n�ration d'un num�ro de d�compte � partir des trois param�tres
     * 
     * @param annee Ann�e du num�ro
     * @param code Code du num�ro (li� au type de d�compte)
     * @param no offset du d�compte
     */
    public NumeroDecompte(final Annee annee, final String code, final String offset) {
        this(String.valueOf(annee.getValue()), code, offset);
    }

    /**
     * Cr�ation du num�ro de d�compte suivant � partir de l'offset pass� en param�tre.
     * 
     * @param typeDecompte Type de d�compte sur lequel le code sera d�termin�
     * @param date Date sur laquelle l'ann�e sera d�duite
     * @param periodicite P�riodicit� du d�compte
     * @param offset Offset sur lequel le nouvel offset sera g�n�r�
     * @return Num�ro de d�compte suivant de l'offset actuel
     */
    public static NumeroDecompte next(final TypeDecompte typeDecompte, final Date date, final String periodicite,
            final String offset) {
        StringBuilder sb = new StringBuilder();
        if (offset != null) {
            Integer value = Integer.valueOf(offset);
            value += OFFSET_INCREMENT;
            sb.append(String.format(FORMAT_OFFSET, value));
        } else {
            sb.append(OFFSET_FIRST);
        }
        String newOffset = sb.toString();
        return new NumeroDecompte(typeDecompte, date, periodicite, newOffset);
    }

    private static String computeNumeroDecompte(final TypeDecompte typeDecompte, final Date date,
            final String periodicite, final String offset) {
        StringBuilder sb = new StringBuilder();
        sb.append(date.getAnnee());
        sb.append(getCodeForTypeDecompte(typeDecompte, periodicite, date));
        sb.append(offset);
        return sb.toString();
    }

    /**
     * Retourne le code associ� � un type de d�compte, un p�riodicit� et une date
     * 
     * @param typeDecompte Le type de d�compte sur lequel se baser
     * @param periodicite Periodicit� de l'employeur
     * @param date Date � partir de laquelle calculer
     * @return String repr�sentant le code du num�ro de d�compte
     */
    public static String getCodeForTypeDecompte(TypeDecompte typeDecompte, String periodicite, Date date) {
        StringBuilder sb = new StringBuilder();
        switch (typeDecompte) {
            case PERIODIQUE:
                if (Constants.PERIODICITE_MENSUELLE.equals(periodicite)) {
                    sb.append(date.getMois());
                } else if (Constants.PERIODICITE_TRIMESTRIELLE.equals(periodicite)) {
                    sb.append(PERIODIQUE_TRIMESTRIELLE);
                    sb.append(date.getCurrentPeriodeTrimestrielle());
                } else {
                    sb.append(PERIODIQUE_ANNUELLE);
                }
                break;
            case COMPLEMENTAIRE:
                sb.append(COMPLEMENTAIRE);
                break;
            case CONTROLE_EMPLOYEUR:
                sb.append(CONTROLE_EMPLOYEUR);
                break;
            case SPECIAL_SALAIRE:
            case SPECIAL_CAISSE:
            case CPP:
                sb.append(SPECIAL);
                break;
        }
        return sb.toString();
    }

    public String getValue() {
        return annee + code + offset;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String getAnnee() {
        return annee;
    }

    public String getCode() {
        return code;
    }

    public String getOffset() {
        return offset;
    }

    public NumeroDecompte addOffset(int number) {
        int iOffset = Integer.valueOf(offset);
        return new NumeroDecompte(annee + code + String.valueOf(iOffset + number));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annee == null) ? 0 : annee.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((offset == null) ? 0 : offset.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NumeroDecompte other = (NumeroDecompte) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (offset == null) {
            if (other.offset != null) {
                return false;
            }
        } else if (!offset.equals(other.offset)) {
            return false;
        }
        return true;
    }

}
