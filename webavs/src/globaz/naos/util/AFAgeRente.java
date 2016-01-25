/*
 * Created on 01-Jul-05
 */
package globaz.naos.util;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceManager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;

/**
 * Class pour le calculer l'age du Rentier ou de la Rentière.
 * 
 * @author sau
 */
public class AFAgeRente {

    // public static final int ID_ASSURANCE_AVS_PERSONNEL = 110;

    /**
     * Class utilisée pour enregister les paramètres de calculation de la date de retraite.
     * 
     * @author sau
     */
    private class DonneeRetraite {
        int ageAVS = 0;
        String dateNaissance = null;
    }

    private List<DonneeRetraite> ageAVSFemmeList = null;

    private List<DonneeRetraite> ageAVSHommeList = null;
    private String dateFinPeriode = null;

    private BSession session = null;

    /**
     * Constructeur d'AFUtil.
     */
    public AFAgeRente() {
        super();
    }

    /**
     * Ajouter dans la(les) liste(s) AVS, les données pour la détérmination de l'age AVS.
     * 
     * @param newRetraite
     * @param sex
     * @throws Exception
     */
    private void addInAgeAVSList(DonneeRetraite newRetraite, String sex) throws Exception {
        DonneeRetraite retraite = null;
        boolean insertInListOK = true;

        // Homme
        if (JadeStringUtil.isIntegerEmpty(sex) || sex.equals(CodeSystem.SEXE_HOMME)) {
            for (int i = 0; i < ageAVSHommeList.size(); i++) {
                retraite = ageAVSHommeList.get(i);

                if (BSessionUtil.compareDateFirstGreaterOrEqual(session, newRetraite.dateNaissance,
                        retraite.dateNaissance)) {

                    insertInListOK = false;
                    break;
                }
            }
            if (insertInListOK) {
                ageAVSHommeList.add(newRetraite);
            }
        }

        // Femme
        if (JadeStringUtil.isIntegerEmpty(sex) || sex.equals(CodeSystem.SEXE_FEMME)) {
            for (int i = 0; i < ageAVSFemmeList.size(); i++) {
                retraite = ageAVSFemmeList.get(i);

                if (BSessionUtil.compareDateFirstGreaterOrEqual(session, newRetraite.dateNaissance,
                        retraite.dateNaissance)) {

                    insertInListOK = false;
                    break;
                }
            }
            if (insertInListOK) {
                ageAVSFemmeList.add(newRetraite);
            }
        }
    }

    /**
     * Retour le date de Rente.
     * 
     * @param dateNaissance
     *            Format: DD.MM.YYYY
     * @param sex
     * @return la date de rente Format: DD.MM.YYYY
     * @throws Exception
     */
    public String getDateRente(String dateNaissance, String sex) throws Exception {

        if (JadeStringUtil.isIntegerEmpty(dateNaissance)) {
            return dateNaissance;
        } else {
            if (dateNaissance.substring(0, 5).equals("00.00")) {
                dateNaissance = "30.06" + dateNaissance.substring(6);
            }

            int ageAVS = 0;
            DonneeRetraite retraite = null;

            if (sex.equalsIgnoreCase(CodeSystem.SEXE_HOMME)) {
                for (int i = 0; i < ageAVSHommeList.size(); i++) {
                    retraite = ageAVSHommeList.get(i);

                    if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateNaissance, retraite.dateNaissance)) {
                        ageAVS = retraite.ageAVS;
                    } else {
                        break;
                    }
                }
            } else {
                for (int i = 0; i < ageAVSFemmeList.size(); i++) {
                    retraite = ageAVSFemmeList.get(i);

                    if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateNaissance, retraite.dateNaissance)) {
                        ageAVS = retraite.ageAVS;
                    } else {
                        break;
                    }
                }
            }
            return AFUtil.getDateEndOfMonth(dateNaissance.substring(0, 6)
                    + Integer.toString(Integer.parseInt(dateNaissance.substring(6)) + ageAVS));
        }
    }

    /**
     * Initialise et calcule la date de Rente pour la fin d'une période définie.
     * 
     * @param newSession
     * @param newDateFinPeriode
     *            Format: DD.MM.YYYY
     * @throws Exception
     */
    public void initDateRente(BSession newSession, String newDateFinPeriode) throws Exception {
        session = newSession;
        dateFinPeriode = newDateFinPeriode;
        initDateRenteSexe(dateFinPeriode, CodeSystem.SEXE_HOMME);
        initDateRenteSexe(dateFinPeriode, CodeSystem.SEXE_FEMME);
    }

    /**
     * Initialise et calcule la date de Rente pour la fin d'une période et un sexe défini.
     * 
     * @param datePeriode
     * @param sex
     * @throws Exception
     */
    private void initDateRenteSexe(String datePeriode, String sex) throws Exception {

        if ((sex.equalsIgnoreCase(CodeSystem.SEXE_HOMME) && (ageAVSHommeList == null))
                || (sex.equalsIgnoreCase(CodeSystem.SEXE_FEMME) && (ageAVSFemmeList == null))) {

            // DGI ne plus utiliser l'id fixe mais recherche le l'assurance AVS
            // pers.
            AFAssuranceManager assurances = new AFAssuranceManager();
            assurances.setSession(session);
            assurances.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            assurances.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            assurances.find();
            if (assurances.size() != 1) {
                throw new Exception(session.getLabel("1840"));
            }

            // HOMME
            AFParametreAssuranceManager manager = new AFParametreAssuranceManager();
            manager.setISession(session);
            manager.setForIdAssurance(((AFAssurance) assurances.getFirstEntity()).getAssuranceId());
            manager.setForGenre(CodeSystem.GEN_PARAM_ASS_AGE_MAX);
            manager.setForSexe(sex);
            manager.setForDate(AFUtil.getDateEndOfMonth(datePeriode));
            manager.setOrderByDateDebutDesc();
            manager.find();

            if (sex.equalsIgnoreCase(CodeSystem.SEXE_HOMME)) {
                ageAVSHommeList = new ArrayList<DonneeRetraite>(manager.size());
            } else {
                ageAVSFemmeList = new ArrayList<DonneeRetraite>(manager.size());
            }

            // Initialisation des listes contenant les Ages de Retraite Homme et
            // Femme.
            String previousDateDebut = null;
            for (int i = 0; i < manager.size(); i++) {
                AFParametreAssurance paramAssurance = (AFParametreAssurance) manager.get(i);

                DonneeRetraite retraite = new DonneeRetraite();
                if (previousDateDebut != null) {
                    paramAssurance.setDateFin(AFUtil.getDatePreviousDay(previousDateDebut));
                }

                if (JadeStringUtil.isIntegerEmpty(paramAssurance.getDateFin())) {
                    retraite.dateNaissance = "31.12." + datePeriode.substring(6);
                } else {
                    retraite.dateNaissance = paramAssurance.getDateFin().substring(0, 6)
                            + Integer.toString(Integer.parseInt(paramAssurance.getDateFin().substring(6))
                                    - Integer.parseInt(paramAssurance.getValeur()));
                }
                retraite.ageAVS = Integer.parseInt(paramAssurance.getValeur());

                addInAgeAVSList(retraite, sex);

                previousDateDebut = paramAssurance.getDateDebut();
            } // end for

            if (manager.size() == 0) {

                throw new Exception(session.getLabel("1840"));

                /*
                 * DonneeRetraite retraite = new DonneeRetraite(); if (sex.equalsIgnoreCase(CodeSystem.SEXE_HOMME)) {
                 * ageAVSHommeList = new ArrayList(1);
                 * 
                 * retraite.ageAVS = 65; retraite.dateNaissance = "31.12." + datePeriode.substring(6);
                 * addInAgeAVSList(retraite, CodeSystem.SEXE_HOMME); }
                 * 
                 * if (sex.equalsIgnoreCase(CodeSystem.SEXE_FEMME)) { ageAVSFemmeList = new ArrayList(3);
                 * 
                 * retraite = new DonneeRetraite(); retraite.ageAVS = 64; retraite.dateNaissance = "31.12." +
                 * datePeriode.substring(6); addInAgeAVSList(retraite, CodeSystem.SEXE_FEMME);
                 * 
                 * retraite = new DonneeRetraite(); retraite.ageAVS = 63; retraite.dateNaissance = "31.12.1941";
                 * addInAgeAVSList(retraite, CodeSystem.SEXE_FEMME);
                 * 
                 * retraite = new DonneeRetraite(); retraite.ageAVS = 62; retraite.dateNaissance = "31.12.1938";
                 * addInAgeAVSList(retraite, CodeSystem.SEXE_FEMME); }
                 */
            }
        }
    }
}
