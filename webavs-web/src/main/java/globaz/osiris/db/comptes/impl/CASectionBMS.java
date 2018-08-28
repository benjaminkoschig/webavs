package globaz.osiris.db.comptes.impl;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CATypeSection;

/**
 * On surcharge
 * 
 * @since WebBMS 2.0
 */
public class CASectionBMS implements APISectionDescriptor {

/**
     * 
     */
    private static final long serialVersionUID = -3196841993707032759L;

    private String annee = "";
    private JACalendarGregorian calendar = new JACalendarGregorian();
    private String dateSection = "";
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String errorMessage = null;
    private boolean hasErrors = false;
    private String idCategorie = "";
    private String idExterne = "";
    private String idSection = "";
    private String idTypeSection = "";
    private String modeCompensation = "";
    private APISection section;
    private globaz.globall.api.BISession session = null;

    /**
     *
     */
    public CASectionBMS() {
        super();
    }

    /**
     * Date de création : (01.03.2003 13:04:54)
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
     * Date de création : (19.12.2002 11:43:31)
     */
    private void _checkData() throws Exception {
        // Initialser
        JADate _date = new JADate(dateSection);
        annee = String.valueOf(_date.getYear());
        String sousType = "";
        // Vérifier le numéro
        if ((idExterne.length() != 7) && (idExterne.length() != 9)) {
            // _addErrorMessage(getSession().getLabel("7290"));
            return;
        }
        // Sortir si l'id externe commence par zéro (pas interprétable)
        if (idExterne.startsWith("0")) {
            return;
        }
        // Si le numéro est parlant, récupérer le sous-type et l'année de
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

        // Vérifier le sous type si existe
        if (!JadeStringUtil.isIntegerEmpty(idCategorie) && !JadeStringUtil.isIntegerEmpty(sousType)) {
            if (!idCategorie.endsWith(sousType)) {
                _addErrorMessage(getSession().getLabel("7291"));
            }
        }
        // Déduire le sous-type si vide
        if (JadeStringUtil.isIntegerEmpty(idCategorie)) {
            if (idExterne.length() == 7) {
                idCategorie = "2270" + sousType;
            } else if (idExterne.length() == 9) {
                idCategorie = "2270" + sousType;
            }
        }
        // Déduire la période si vide
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
                // 2è Trimestre
            } else if (idCategorie.equals("227042")) {
                dateDebutPeriode = "01.04." + annee;
                dateFinPeriode = "30.06." + annee;
                // 3è Trimestre
            } else if (idCategorie.equals("227043")) {
                dateDebutPeriode = "01.07." + annee;
                dateFinPeriode = "30.09." + annee;
                // 4è Trimestre
            } else if (idCategorie.equals("227044")) {
                dateDebutPeriode = "01.10." + annee;
                dateFinPeriode = "31.12." + annee;
                // 1er semestre
            } else if (idCategorie.equals("227061")) {
                dateDebutPeriode = "01.01." + annee;
                dateFinPeriode = "30.06." + annee;
                // 2ème semestre
            } else if (idCategorie.equals("227062")) {
                dateDebutPeriode = "01.07." + annee;
                dateFinPeriode = "31.12." + annee;
            } else {
                dateDebutPeriode = "01.01." + annee;
                dateFinPeriode = "31.12." + annee;
            }
        }
        // Vérifier le sous-type
        if (JadeStringUtil.isIntegerEmpty(idCategorie)) {
            _addErrorMessage(getSession().getLabel("7292"));
        }
        // Déterminer le type de section
        if (JadeStringUtil.isIntegerEmpty(idTypeSection)) {
            idTypeSection = "1";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 12:59:09)
     */
    private void _clearErrors() {
        hasErrors = false;
        errorMessage = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.05.2003 10:48:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Surcharge :
     * 
     * @return
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceLegale()
     */
    @Override
    public String getDateEcheanceLegale() {
        // Date d'échéance par défaut = date de la facture + 30 jours
        try {
            if (isAssociationOuCpp()) {
                return calendar.addDays(dateSection, 30);
            }
            // Si sous type renseigné
            if (!JadeStringUtil.isBlank(idCategorie) && (idCategorie.length() == 6)) {
                // Récupérer le mois
                int i = Integer.parseInt(idCategorie.substring(4));
                // Décompte mensuel 01-11

                if (!isTardif() && (i <= 11) && (i >= 01)) {
                    JADate d = new JADate(10, i + 1, Integer.parseInt(annee));
                    return d.toStr(".");
                    // Décompte décembre, 4è trimestre, 2ème semestre ou annuel
                } else if (!isTardif() && ((i == 12) || (i == 40) || (i == 44) || (i == 62))) {
                    JADate d = new JADate(10, 01, Integer.parseInt(annee) + 1);
                    return d.toStr(".");
                    // Décompte 1er trimestre
                } else if (!isTardif() && (i == 41)) {
                    JADate d = new JADate(10, 04, Integer.parseInt(annee));
                    return d.toStr(".");
                    // Décompte 2eme trimestre, 1er semestre
                } else if (!isTardif() && ((i == 42) || (i == 46) || (i == 61))) {
                    JADate d = new JADate(10, 07, Integer.parseInt(annee));
                    return d.toStr(".");
                    // Décompte 3eme trimestre
                } else if (!isTardif() && (i == 43)) {
                    JADate d = new JADate(10, 10, Integer.parseInt(annee));
                    return d.toStr(".");
                    // Relevé de type taxation d'office
                } else if (i == 30) {
                    return calendar.addDays(dateSection, 14);
                    // Autres cas -> 30 jours
                } else {
                    return calendar.addDays(dateSection, 30);
                }
                // Si pas de sous type, 30 jours
            } else {
                return calendar.addDays(dateSection, 30);
            }
        } catch (Exception e) {
            System.out.println("Exception in " + this.getClass().getName() + ": " + e.getMessage());
            return dateSection;
        }
    }

    private boolean isAssociationOuCpp() {
        return "10".equals(getIdTypeSection()) || "11".equals(getIdTypeSection());
    }

    /**
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceLSV()
     */
    @Override
    public String getDateEcheanceLSV() {
        return getDateEcheanceFacturation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.05.2003 10:48:18)
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
            // Si le sous-type est fourni, récupérer le code système
            if (!JadeStringUtil.isIntegerEmpty(idCategorie)) {
                globaz.globall.parameters.FWParametersSystemCode code = new FWParametersSystemCode();
                code.setISession(session);
                // Déterminer la langue
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

                    // Le code système n'existe pas, on prend donc le libelle
                    // suivant :
                    if (code.isNew()) {
                        code.getCode(idCategorie);
                    }
                } else {
                    code.getCode(idCategorie);
                }

                // Le code système existe
                if (!code.isNew()) {
                    s = code.getCodeUtilisateur(code.getIdLangue()).getLibelle();
                    // Ajouter l'année si période fournie
                    JADate dDebut = new JADate(dateDebutPeriode);
                    JADate dFin = new JADate(dateFinPeriode);
                    // Si aucune date, ne rien faire
                    if ((dDebut.getYear() == 0) && (dFin.getYear() == 0)) {
                        // Ajouter l'année sauf pour cotisations personnelles
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
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 13:19:59)
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
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 13:24:56)
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
     * Insérez la description de la méthode ici. Date de création : (10.05.2003 10:48:04)
     * 
     * @return globaz.globall.db.Bsession
     */
    public BSession getSession() {
        return (BSession) session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 12:57:18)
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
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:42:41)
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
        dateSection = section.getDateSection();
        idCategorie = section.getCategorieSection();
        dateDebutPeriode = section.getDateDebutPeriode();
        dateFinPeriode = section.getDateFinPeriode();
        modeCompensation = section.getIdModeCompensation();
        idSection = section.getIdSection();
        _checkData();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2002 17:16:15)
     * 
     * @param idExterne
     *            java.lang.String
     * @param idCategorie
     *            java.lang.String
     * @param dateSection
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
        dateSection = newDate;
        dateDebutPeriode = newDateDebut;
        dateFinPeriode = newDateFin;
        _checkData();
    }

    /**
     * 
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceAdaptee()
     * @return Date d'échéance adaptée pour le BMS
     */
    @Override
    public String getDateEcheanceAdaptee() {
        String result = null;

        try {
            JADate dateFacturation = new JADate(dateSection);
            JADate dateFinPeriodeSection = new JADate(dateFinPeriode);
            JADate dateEcheanceLegale = calendar.getNextWorkingDay(new JADate(getDateEcheanceLegale()));

            // Si c'est une taxation d'office, on prend la date de facturation + 30 jours
            if (idExterne.substring(6, 9).equals("500")) {
                dateFacturation = calendar.addDays(dateFacturation, 30);
                dateFacturation = calendar.getNextWorkingDay(dateFacturation);
                return dateFacturation.toStr(".");
            }

            if (!JadeStringUtil.isBlank(idCategorie) && (idCategorie.length() == 6)) {
                // Récupérer le mois
                int categorie = Integer.parseInt(idCategorie.substring(4));

                if (!isAssociationOuCpp() && !isTardif() && (isPeriodique(categorie) || categorie == 36)) {
                    dateFacturation = calendar.addDays(dateFinPeriodeSection, 30);
                    dateFacturation = calendar.getNextWorkingDay(dateFacturation);
                    return dateFacturation.toStr(".");
                }
            } else {
                JadeLogger.warn(this, "ERREUR : idCategorie blank or idCategorie.length <> 6 !!");
            }

            result = dateEcheanceLegale.toStr(".");
        } catch (JAException e) {
            JadeLogger.warn(this, e);
            result = "";
        }
        return result;
    }

    /**
     * Contrôle s'il s'agit d'un décompte tardif (9 en position 7)
     * 
     * @param tardif
     * @return
     */
    private boolean isTardif() {
        // Contrôle s'il s'agit d'un décompte tardif (9 en position 7)
        int tardif = Integer.parseInt(idExterne.substring(6, 7));
        return tardif == 9;
    }

    private boolean isMensuelle(int categorie) {
        return (categorie >= 01) && (categorie <= 12);
    }

    private boolean isTrimestrielle(int categorie) {
        return (categorie >= 41) && (categorie <= 44);
    }

    private boolean isSemestrielle(int categorie) {
        return categorie == 61 || categorie == 62;
    }

    private boolean isAnnuelle(int categorie) {
        return categorie == 40 || categorie == 45 || categorie == 47;
    }

    private boolean isPeriodique(int categorie) {
        return isMensuelle(categorie) || isTrimestrielle(categorie) || isAnnuelle(categorie)
                || isSemestrielle(categorie);
    }

    /**
     * @see globaz.osiris.api.APISectionDescriptor#getDateEcheanceFacturation() Le l'échéance est calculé ainsi
     *      max(dateFacturation + 30j, mois suivant la date de factuation + 12j)
     */
    @Override
    public String getDateEcheanceFacturation() {
        return getDateEcheanceAdaptee();
    }

}
