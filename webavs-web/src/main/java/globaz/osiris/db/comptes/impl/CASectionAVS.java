package globaz.osiris.db.comptes.impl;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.utils.CADateUtil;

/**
 * Date de cr�ation : (11.03.2002 11:46:05)
 * 
 * @author: Administrator
 */
public class CASectionAVS implements APISectionDescriptor {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static int DAYS_ADDED = 12; // Utilis� dans :
    // getDateEcheanceAdaptee()

    private String annee = new String();
    private JACalendarGregorian calendar = new JACalendarGregorian();
    private String date = new String();
    private String dateDebutPeriode = new String();
    private String dateFinPeriode = new String();
    private String errorMessage = null;
    private boolean hasErrors = false;
    private String idCategorie = new String();
    private String idExterne = new String();
    private String idSection = new String();
    private String idTypeSection = new String();
    private String modeCompensation = new String();
    private APISection section;
    private globaz.globall.api.BISession session = null;

    public CASectionAVS() {
        super();
    }

    /**
     * Date de cr�ation : (01.03.2003 13:04:54)
     * 
     * @param errorMessage
     *            java.lang.String
     */
    private void _addErrorMessage(String newErrorMessage) {
        hasErrors = true;
        if (errorMessage == null) {
            errorMessage = newErrorMessage;
        }
    }

    /**
     * Date de cr�ation : (19.12.2002 11:43:31)
     */
    private void _checkData() throws Exception {
        // Initialser
        JADate _date = new JADate(date);
        annee = String.valueOf(_date.getYear());
        String sousType = "";
        // V�rifier le num�ro
        if ((idExterne.length() != 7) && (idExterne.length() != 9)) {
            // _addErrorMessage(getSession().getLabel("7290"));
            return;
        }
        // Sortir si l'id externe commence par z�ro (pas interpr�table)
        if (idExterne.startsWith("0")) {
            return;
        }
        // Si le num�ro est parlant, r�cup�rer le sous-type et l'ann�e de
        // cotisations

        if (idExterne.length() == 7) {
            sousType = idExterne.substring(2, 4);
            int an = Integer.parseInt(idExterne.substring(0, 2));
            if (an < 48) {
                an += 2000;
            } else {
                an += 1900;
            }
            annee = String.valueOf(an);
        } else if (idExterne.length() == 9) {
            sousType = idExterne.substring(4, 6);
            annee = idExterne.substring(0, 4);
        }

        // V�rifier le sous type si existe
        if (!JadeStringUtil.isIntegerEmpty(idCategorie) && !JadeStringUtil.isIntegerEmpty(sousType)) {
            if (!idCategorie.endsWith(sousType)) {
                _addErrorMessage(getSession().getLabel("7291"));
            }
        }
        // D�duire le sous-type si vide
        if (JadeStringUtil.isIntegerEmpty(idCategorie)) {
            if (idExterne.length() == 7) {
                idCategorie = "2270" + sousType;
            } else if (idExterne.length() == 9) {
                idCategorie = "2270" + sousType;
            }
        }
        // D�duire la p�riode si vide
        if (JadeStringUtil.isBlank(dateDebutPeriode)) {
            if ((idCategorie.compareTo("227001") >= 0) && (idCategorie.compareTo("227012") <= 0)) {
                dateDebutPeriode = "01." + idCategorie.substring(4, 6) + "." + annee;
                JADate _dateDeb = new JADate(dateDebutPeriode);
                _dateDeb = calendar.setLastInMonth(_dateDeb);
                dateFinPeriode = _dateDeb.toStr(".");
                // 1er Trimestre
            } else if (idCategorie.equals("227041")) {
                dateDebutPeriode = "01.01." + annee;
                dateFinPeriode = "31.03." + annee;
                // 2� Trimestre
            } else if (idCategorie.equals("227042")) {
                dateDebutPeriode = "01.04." + annee;
                dateFinPeriode = "30.06." + annee;
                // 3� Trimestre
            } else if (idCategorie.equals("227043")) {
                dateDebutPeriode = "01.07." + annee;
                dateFinPeriode = "30.09." + annee;
                // 4� Trimestre
            } else if (idCategorie.equals("227044")) {
                dateDebutPeriode = "01.10." + annee;
                dateFinPeriode = "31.12." + annee;
                // 1er semestre
            } else if (idCategorie.equals("227061")) {
                dateDebutPeriode = "01.01." + annee;
                dateFinPeriode = "30.06." + annee;
                // 2�me semestre
            } else if (idCategorie.equals("227062")) {
                dateDebutPeriode = "01.07." + annee;
                dateFinPeriode = "31.12." + annee;
            } else {
                dateDebutPeriode = "01.01." + annee;
                dateFinPeriode = "31.12." + annee;
            }
        }
        // V�rifier le sous-type
        if (JadeStringUtil.isIntegerEmpty(idCategorie)) {
            _addErrorMessage(getSession().getLabel("7292"));
        }
        // D�terminer le type de section
        if (JadeStringUtil.isIntegerEmpty(idTypeSection)) {
            idTypeSection = "1";
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 12:59:09)
     */
    private void _clearErrors() {
        hasErrors = false;
        errorMessage = null;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.05.2003 10:48:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Calcule la date d'�ch�ance adapt�e pour Inforom (+12j) <br>
     * Base = max(dateFacturation + 12j, date d'�ch�ance l�gale) <br>
     * Cr�� le : 10 nov. 06 <br>
     * Surcharge :
     * 
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceAdaptee()
     * @author: sel
     * @return Date d'�ch�ance adapt�e pour Inforom (+12j)
     */
    @Override
    public String getDateEcheanceAdaptee() {
        String result = null;

        try {
            JADate dateFacturation = new JADate(date);
            JADate dateEcheanceLegale = new JADate(getDateEcheanceLegale());
            dateEcheanceLegale = calendar.getNextWorkingDay(dateEcheanceLegale);

            // dateFacturation + 12j
            dateFacturation = calendar.addDays(dateFacturation, CASectionAVS.DAYS_ADDED);
            dateFacturation = calendar.getNextWorkingDay(dateFacturation);

            // max(dateFacturation + 12j, date d'�ch�ance de la section)
            if ((calendar.compare(dateFacturation, dateEcheanceLegale) == JACalendar.COMPARE_FIRSTUPPER)) {
                result = dateFacturation.toStr(".");
            } else {
                result = dateEcheanceLegale.toStr(".");
            }
        } catch (JAException e) {
            result = "";
        }
        return result;
    }

    /**
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceFacturation() Le l'�ch�ance est calcul� ainsi
     *      max(dateFacturation + 30j, mois suivant la date de factuation + 12j)
     */
    @Override
    public String getDateEcheanceFacturation() {
        String result = null;
        try {
            // D�lai de base = le 12eme jour du mois suivant
            JADate delaiDeBaseJa = calendar.addDays(new JADate(date), 12);

            // Si le d�lai de base est inf�rieur � l��ch�ance l�gale,
            // le d�lai est fix� � l��ch�ance.

            JADate delaiLegalJa = new JADate(getDateEcheanceLegale());
            // Dans le cas d'un relev� de type taxation d'office,
            // le d�lai de paiement de suite doit appara�tre sur la facture
            try {
                if (Integer.parseInt(idCategorie.substring(4)) == 30) {
                    result = "DE_SUITE";
                } else if (delaiDeBaseJa.toInt() < delaiLegalJa.toInt()) {
                    // delaiLegalJa = this.calendar.getNextWorkingDay(delaiLegalJa);
                    delaiLegalJa = CADateUtil.getDateOuvrable(delaiLegalJa);
                    result = delaiLegalJa.toStr(".");
                } else {
                    // delaiDeBaseJa = this.calendar.getNextWorkingDay(delaiDeBaseJa);
                    delaiDeBaseJa = CADateUtil.getDateOuvrable(delaiDeBaseJa);
                    result = delaiDeBaseJa.toStr(".");
                }
            } catch (Exception e) {
                result = getDateEcheanceLegale();
            }
        } catch (JAException e) {
            result = getDateEcheanceLegale();
        }
        return result;
    }

    /**
     * Surcharge :
     * 
     * @return
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceLegale()
     */
    @Override
    public String getDateEcheanceLegale() {
        // Date d'�ch�ance par d�faut = date de la facture + 30 jours
        try {
            // Si sous type renseign�
            if (!JadeStringUtil.isBlank(idCategorie) && (idCategorie.length() == 6)) {
                // Contr�le s'il s'agit d'un d�compte tardif (9 en position 7)
                int pos7 = Integer.parseInt(idExterne.substring(6, 7));
                // R�cup�rer le mois
                int i = Integer.parseInt(idCategorie.substring(4));
                // D�compte mensuel 01-11
                if ((pos7 != 9) && (i <= 11) && (i >= 01)) {
                    JADate d = new JADate(10, i + 1, Integer.parseInt(annee));
                    return d.toStr(".");
                    // D�compte d�cembre, 4� trimestre, 2�me semestre ou annuel
                } else if ((pos7 != 9) && ((i == 12) || (i == 40) || (i == 44) || (i == 62))) {
                    JADate d = new JADate(10, 01, Integer.parseInt(annee) + 1);
                    return d.toStr(".");
                    // D�compte 1er trimestre
                } else if ((pos7 != 9) && (i == 41)) {
                    JADate d = new JADate(10, 04, Integer.parseInt(annee));
                    return d.toStr(".");
                    // D�compte 2eme trimestre, 1er semestre
                } else if ((pos7 != 9) && ((i == 42) || (i == 46) || (i == 61))) {
                    JADate d = new JADate(10, 07, Integer.parseInt(annee));
                    return d.toStr(".");
                    // D�compte 3eme trimestre
                } else if ((pos7 != 9) && (i == 43)) {
                    JADate d = new JADate(10, 10, Integer.parseInt(annee));
                    return d.toStr(".");
                    // Relev� de type taxation d'office
                } else if (i == 30) {
                    return calendar.addDays(date, 14);
                    // Autres cas -> 30 jours
                } else {
                    return calendar.addDays(date, 30);
                }
                // Si pas de sous type, 30 jours
            } else {
                return calendar.addDays(date, 30);
            }
        } catch (Exception e) {
            System.out.println("Exception in " + this.getClass().getName() + ": " + e.getMessage());
            return date;
        }
    }

    /**
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceLSV()
     */
    @Override
    public String getDateEcheanceLSV() {
        return getDateEcheanceFacturation();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.05.2003 10:48:18)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * Surcharge :
     * 
     * @return
     * @see globaz.osiris.api.APISectionDescriptor#getDescription()
     */
    @Override
    public String getDescription() {
        String iso = "FR";
        try {
            iso = session.getIdLangueISO();
        } catch (Exception e) {
            System.out.println("Exception in CASectionAvs.getDescription()");
            JadeLogger.error(this, e);
        }
        return this.getDescription(iso);
    }

    /**
     * Surcharge :
     * 
     * @param codeISOLangue
     * @return
     * @see globaz.osiris.api.APISectionDescriptor#getDescription(java.lang.String)
     */
    @Override
    public String getDescription(String codeISOLangue) {
        // Initialiser
        String s = "";
        try {
            // Si le sous-type est fourni, r�cup�rer le code syst�me
            if (!JadeStringUtil.isIntegerEmpty(idCategorie)) {
                globaz.globall.parameters.FWParametersSystemCode code = new FWParametersSystemCode();
                code.setISession(session);
                // D�terminer la langue
                code.setIdLangue("F");
                if (codeISOLangue.equalsIgnoreCase("de")) {
                    code.setIdLangue("D");
                } else if (codeISOLangue.equalsIgnoreCase("it")) {
                    code.setIdLangue("I");
                }

                // Si l'idExterne contient un 9 dans la position 7 (AAAAMM9XXX)
                if (!JadeStringUtil.isIntegerEmpty(idExterne) && (idExterne.length() > 7)
                        && (Integer.parseInt(idExterne.substring(6, 7)) == 9)) {

                    code.setIdCode("2560" + idCategorie.substring(4, 6));
                    code.retrieve();

                    // Le code syst�me n'existe pas, on prend donc le libelle
                    // suivant :
                    if (code.isNew()) {
                        code.getCode(idCategorie);
                    }
                } else {
                    code.getCode(idCategorie);
                }

                // Le code syst�me existe
                if (!code.isNew()) {
                    s = code.getCodeUtilisateur(code.getIdLangue()).getLibelle();
                    // Ajouter l'ann�e si p�riode fournie
                    JADate dDebut = new JADate(dateDebutPeriode);
                    JADate dFin = new JADate(dateFinPeriode);
                    // Si aucune date, ne rien faire
                    if ((dDebut.getYear() == 0) && (dFin.getYear() == 0)) {
                        // Ajouter l'ann�e sauf pour cotisations personnelles
                    } else {
                        if (!idCategorie.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS)) {
                            if (dDebut.getYear() == dFin.getYear()) {
                                s = s + " " + String.valueOf(dFin.getYear());
                            } else {
                                s = s + " " + String.valueOf(dDebut.getYear()) + "-" + String.valueOf(dFin.getYear());
                            }
                        }
                    }
                }

            }
            // Si aucune description, utiliser celle du type de section
            if (JadeStringUtil.isBlank(s)) {
                CATypeSection type = new CATypeSection();
                type.setISession(session);
                type.setIdTypeSection(idTypeSection);
                try {
                    type.retrieve();
                } catch (Exception e) {
                    JadeLogger.warn(this, e);
                    type = null;
                }
                // Description
                if (type != null) {
                    s = s + type.getDescription(codeISOLangue);
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        // Retourne la valeur
        return s;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 13:19:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getErrors() {
        return errorMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISectionDescriptor#getIdCategorie()
     */
    @Override
    public String getIdCategorie() {
        return idCategorie;
    }

    /**
     * @return the idSection
     */
    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 13:24:56)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getModeCompensation() {
        return modeCompensation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.05.2003 10:48:04)
     * 
     * @return globaz.globall.db.Bsession
     */
    public BSession getSession() {
        return (BSession) session;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 12:57:18)
     * 
     * @return boolean
     */
    @Override
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * @param idSection
     *            the idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:42:41)
     * 
     * @param newSession
     *            globaz.globall.api.BISession
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * Surcharge :
     * 
     * @param newSection
     * @throws Exception
     * @see globaz.osiris.api.APISectionDescriptor#setSection(globaz.osiris.api.APISection)
     */
    @Override
    public void setSection(APISection newSection) throws Exception {
        _clearErrors();
        section = newSection;
        idExterne = section.getIdExterne();
        idTypeSection = section.getIdTypeSection();
        date = section.getDateSection();
        idCategorie = section.getCategorieSection();
        dateDebutPeriode = section.getDateDebutPeriode();
        dateFinPeriode = section.getDateFinPeriode();
        modeCompensation = section.getIdModeCompensation();
        idSection = section.getIdSection();
        _checkData();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2002 17:16:15)
     * 
     * @param idExterne
     *            java.lang.String
     * @param idCategorie
     *            java.lang.String
     * @param date
     *            java.lang.String
     * @param dateDebut
     *            java.lang.String
     * @param dateFin
     *            java.lang.String
     */
    @Override
    public void setSection(String newIdExterne, String newIdTypeSection, String newidCategorie, String newDate,
            String newDateDebut, String newDateFin) throws Exception {
        _clearErrors();
        idExterne = newIdExterne;
        idTypeSection = newIdTypeSection;
        idCategorie = newidCategorie;
        date = newDate;
        dateDebutPeriode = newDateDebut;
        dateFinPeriode = newDateFin;
        _checkData();
    }
}
