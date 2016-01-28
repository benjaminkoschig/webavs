/*
 * Créé le 13 fev. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.Vector;

/**
 * 
 * @author bsc
 */
public abstract class REAbstractBasesCalculProxyViewBean implements FWViewBeanInterface, BIPersistentObject {

    public static final String CS_ADMIN_GENRE_OFFICE_AI = "509004";

    private REBasesCalcul basesCalcul;
    private boolean modifie = false;

    protected REAbstractBasesCalculProxyViewBean(REBasesCalcul basesCalcul) {
        this.basesCalcul = basesCalcul;
    }

    @Override
    public void add() throws Exception {
        basesCalcul.add();
    }

    public void add(BITransaction transaction) throws Exception {
        basesCalcul.add(transaction);
    }

    @Override
    public void delete() throws Exception {
        basesCalcul.delete();
    }

    public void delete(BITransaction transaction) throws Exception {
        basesCalcul.delete(transaction);
    }

    public String getAnneeBonifTacheAssistance() {
        // format xx.xx
        if (basesCalcul.getAnneeBonifTacheAssistance().length() == 5) {
            // si 00.00
            if (basesCalcul.getAnneeBonifTacheAssistance().substring(0, 2).equals("00")
                    && basesCalcul.getAnneeBonifTacheAssistance().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getAnneeBonifTacheAssistance();
    }

    public String getAnneeBonifTacheEduc() {
        // format xx.xx
        if (basesCalcul.getAnneeBonifTacheEduc().length() == 5) {
            // si 00.00
            if (basesCalcul.getAnneeBonifTacheEduc().substring(0, 2).equals("00")
                    && basesCalcul.getAnneeBonifTacheEduc().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getAnneeBonifTacheEduc();
    }

    public String getAnneeBonifTransitoire() {
        // format x.x
        if (basesCalcul.getAnneeBonifTransitoire().length() == 3) {
            // si 0.0
            if (basesCalcul.getAnneeBonifTransitoire().substring(0, 1).equals("0")
                    && basesCalcul.getAnneeBonifTransitoire().substring(2).equals("0")) {
                return "";
            } else {
                return basesCalcul.getAnneeBonifTransitoire();
            }
        } else {
            return basesCalcul.getAnneeBonifTransitoire();
        }
    }

    public String getAnneeCotiClasseAge() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getAnneeCotiClasseAge())) {
            return "";
        } else {
            return basesCalcul.getAnneeCotiClasseAge();
        }
    }

    public String getAnneeDeNiveau() {
        return basesCalcul.getAnneeDeNiveau();
    }

    public String getAnneeTraitement() {
        return basesCalcul.getAnneeTraitement();
    }

    /**
     * getter pour l'attribut basesCalcul.
     * 
     * @return la valeur courante de l'attribut prononce
     */
    public REBasesCalcul getBasesCalcul() {
        return basesCalcul;
    }

    public String getCleInfirmiteAyantDroit() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getCleInfirmiteAyantDroit())) {
            return "";
        } else {
            return basesCalcul.getCleInfirmiteAyantDroit();
        }

    }

    public String getCodeOfficeAi() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getCodeOfficeAi())) {
            return "";
        } else {
            return basesCalcul.getCodeOfficeAi();
        }
    }

    /**
     * getter pour l'attribut creationSpy
     * 
     * @return la valeur courante de l'attribut creationSpy
     */
    public BSpy getCreationSpy() {
        return basesCalcul.getCreationSpy();
    }

    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(basesCalcul.getCsEtat());
    }

    public String getCsEtatRenteAccordee() {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(basesCalcul.getIdRenteCalculee());
        try {
            ra.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
        return ra.getCsEtat();
    }

    public String getCsEtatDemande() {

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(getSession());
        demande.setIdRenteCalculee(basesCalcul.getIdRenteCalculee());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        try {
            demande.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
        return demande.getCsEtat();
    }

    public String getDateFinDroit() {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(basesCalcul.getIdRenteCalculee());
        try {
            ra.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
        return ra.getDateFinDroit();
    }

    public String getDegreInvalidite() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getDegreInvalidite())) {
            return "";
        } else {
            return basesCalcul.getDegreInvalidite();
        }
    }

    public String getDroitApplique() {
        return basesCalcul.getDroitApplique();
    }

    public String getDureeCotiAvant73() {
        return basesCalcul.getDureeCotiAvant73();
    }

    public String getDureeCotiDes73() {
        return basesCalcul.getDureeCotiDes73();
    }

    public String getDureeRevenuAnnuelMoyen() {
        return basesCalcul.getDureeRevenuAnnuelMoyen();
    }

    public String getEchelleRente() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getEchelleRente())) {
            return "";
        } else {
            return basesCalcul.getEchelleRente();
        }

    }

    public String getFacteurRevalorisation() {
        // format x.xxxx
        if (basesCalcul.getFacteurRevalorisation().length() == 5) {
            // si 0.000
            if (basesCalcul.getFacteurRevalorisation().substring(0, 1).equals("0")
                    && basesCalcul.getFacteurRevalorisation().substring(2).equals("000")) {
                return "";
            } else {
                return basesCalcul.getFacteurRevalorisation();
            }
        } else {
            return basesCalcul.getFacteurRevalorisation();
        }
    }

    @Override
    public String getId() {
        return basesCalcul.getId();
    }

    public String getIdBasesCalcul() {
        return basesCalcul.getIdBasesCalcul();
    }

    public String getIdRenteCalculee() {
        return basesCalcul.getIdRenteCalculee();
    }

    public String getIdTiersBaseCalcul() {
        return basesCalcul.getIdTiersBaseCalcul();
    }

    @Override
    public BISession getISession() {
        return basesCalcul.getISession();
    }

    /**
     * Retourne la liste des codes systèmes pour l'état du droit augmentée des champs 'tous' et 'non définitif'.
     * 
     * @return un Vector de String[2]{noOfficeAI, nomOfficeAI}.
     */
    public Vector getListeOfficeAI() {
        Vector result = new Vector();
        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationForGenre(getSession(),
                    REAbstractBasesCalculProxyViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche dews offoce AI
            JadeLogger.error(this, e);
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                result.add(0, new String[] { officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION),
                        officesAI[i].getProperty(PRTiersWrapper.PROPERTY_NOM) });

            }
        }

        return result;
    }

    /**
     * @return la valeur courante de l'attribut message
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return basesCalcul.getMessage();
    }

    public String getMoisAppointsAvant73() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getMoisAppointsAvant73())) {
            return "";
        } else {
            if (basesCalcul.getMoisAppointsAvant73().length() == 2) {
                return basesCalcul.getMoisAppointsAvant73();
            } else if (basesCalcul.getMoisAppointsAvant73().length() == 0) {
                return "";
            } else {
                return "0" + basesCalcul.getMoisAppointsAvant73();
            }
        }

    }

    public String getMoisAppointsDes73() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getMoisAppointsDes73())) {
            return "";
        } else {
            if (basesCalcul.getMoisAppointsDes73().length() == 2) {
                return basesCalcul.getMoisAppointsDes73();
            } else if (basesCalcul.getMoisAppointsDes73().length() == 0) {
                return "";
            } else {
                return "0" + basesCalcul.getMoisAppointsDes73();
            }
        }
    }

    public String getMoisCotiAnneeOuvertDroit() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getMoisCotiAnneeOuvertDroit())) {
            return "";
        } else {
            return basesCalcul.getMoisCotiAnneeOuvertDroit();
        }
    }

    public String getMontantMaxR10Ech44() {
        FWCurrency currency = new FWCurrency(basesCalcul.getMontantMaxR10Ech44());
        if (currency.isZero()) {
            return "";
        }
        return basesCalcul.getMontantMaxR10Ech44();
    }

    /**
     * @return la valeur courante de l'attribut msgType
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return basesCalcul.getMsgType();
    }

    public String getNombreAnneeBTE_1() {
        return basesCalcul.getNombreAnneeBTE1();
    }

    public String getNombreAnneeBTE_2() {
        return basesCalcul.getNombreAnneeBTE2();
    }

    public String getNombreAnneeBTE_4() {
        return basesCalcul.getNombreAnneeBTE4();
    }

    public String getPeriodeAssEtrangerAv73() {
        // format xx.xx
        if (basesCalcul.getPeriodeAssEtrangerAv73().length() == 5) {
            // si 00.00
            if (basesCalcul.getPeriodeAssEtrangerAv73().substring(0, 2).equals("00")
                    && basesCalcul.getPeriodeAssEtrangerAv73().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getPeriodeAssEtrangerAv73();
    }

    public String getPeriodeAssEtrangerDes73() {
        // format xx.xx
        if (basesCalcul.getPeriodeAssEtrangerDes73().length() == 5) {
            // si 00.00
            if (basesCalcul.getPeriodeAssEtrangerDes73().substring(0, 2).equals("00")
                    && basesCalcul.getPeriodeAssEtrangerDes73().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getPeriodeAssEtrangerDes73();
    }

    public String getPeriodeJeunesse() {
        // format xx.xx
        if (basesCalcul.getPeriodeJeunesse().length() == 5) {
            // si 00.00
            if (basesCalcul.getPeriodeJeunesse().substring(0, 2).equals("00")
                    && basesCalcul.getPeriodeJeunesse().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getPeriodeJeunesse();
    }

    public String getPeriodeMariage() {
        // format xx.xx
        if (basesCalcul.getPeriodeMariage().length() == 5) {
            // si 00.00
            if (basesCalcul.getPeriodeMariage().substring(0, 2).equals("00")
                    && basesCalcul.getPeriodeMariage().substring(3).equals("00")) {
                return "";
            }
        }
        return basesCalcul.getPeriodeMariage();
    }

    public String getReferenceDecision() {
        return basesCalcul.getReferenceDecision();
    }

    public String getResultatComparatif() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getResultatComparatif())) {
            return "";
        }
        return basesCalcul.getResultatComparatif();
    }

    public String getRevenuAnnuelMoyen() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getRevenuAnnuelMoyen())) {
            return "";
        }
        return basesCalcul.getRevenuAnnuelMoyen();
    }

    public String getRevenuJeunesse() {
        FWCurrency currency = new FWCurrency(basesCalcul.getRevenuJeunesse());
        if (currency.isZero()) {
            return "";
        }
        return basesCalcul.getRevenuJeunesse();
    }

    public String getRevenuPrisEnCompte() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getRevenuPrisEnCompte())) {
            return "";
        }
        return basesCalcul.getRevenuPrisEnCompte();
    }

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    public BSession getSession() {
        return basesCalcul.getSession();
    }

    /**
     * getter pour l'attribut spy.
     * 
     * @return la valeur courante de l'attribut spy
     */
    public BSpy getSpy() {
        return basesCalcul.getSpy();
    }

    public String getSupplementCarriere() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getSupplementCarriere())) {
            return "";
        }
        return basesCalcul.getSupplementCarriere();
    }

    public String getSurvenanceEvtAssAyantDroit() {
        // format xx.xxxx
        if (basesCalcul.getSurvenanceEvtAssAyantDroit().length() == 7) {
            // si 00.0000
            if (basesCalcul.getSurvenanceEvtAssAyantDroit().substring(0, 2).equals("00")
                    && basesCalcul.getSurvenanceEvtAssAyantDroit().substring(3).equals("0000")) {
                return "";
            }
        }
        return basesCalcul.getSurvenanceEvtAssAyantDroit();
    }

    public String getTypeCalculComparatif() {
        if (JadeStringUtil.isBlankOrZero(basesCalcul.getTypeCalculComparatif())) {
            return "";
        }
        return basesCalcul.getTypeCalculComparatif();
    }

    public boolean hasSpy() {
        return basesCalcul.hasSpy();
    }

    public boolean isDemandeRenteCsEtatNonValide() {
        return basesCalcul.isDemandeRenteCsEtatNonValide();
    }

    public Boolean isInvaliditePrecoce() {
        return basesCalcul.isInvaliditePrecoce();
    }

    public Boolean isLimiteRevenu() {
        return basesCalcul.isLimiteRevenu();
    }

    public Boolean isMinimuGaranti() {
        return basesCalcul.isMinimuGaranti();
    }

    public boolean isModifiable() {
        try {
            REDemandeRenteManager drManager = new REDemandeRenteManager();
            drManager.setSession(getSession());
            drManager.setForIdRenteCalculee(basesCalcul.getIdRenteCalculee());
            drManager.find(1);

            if (drManager.getSize() > 0) {
                REDemandeRente dem = (REDemandeRente) drManager.getFirstEntity();

                // si la demande est dans l'état validé, aucunes modifications
                // n'est possible
                if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(dem.getCsEtat())) {

                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * getter pour l'attribut modifie.
     * 
     * @return la valeur courante de l'attribut modifie
     */
    public boolean isModifie() {
        return modifie;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return true;
        // return
        // REDeleteCascadeDemandeAPrestationsDues.isModifierPermis(getPrononce());
    }

    public Boolean isPartageRevenuCalcul() {
        return basesCalcul.getIsPartageRevenuCalcul();
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide() {

        try {

            REDemandeRenteManager drManager = new REDemandeRenteManager();
            drManager.setSession(getSession());
            drManager.setForIdRenteCalculee(basesCalcul.getIdRenteCalculee());
            drManager.find(1);

            if (drManager.getSize() > 0) {
                // Recherche des dates dt,dj,dpmt,ddeb
                REDemandeRente dem = (REDemandeRente) drManager.getFirstEntity();

                PRAssert.notIsNew(dem, "Entity not found");

                JACalendar cal = new JACalendarGregorian();
                JADate datePmtMensuel = null;

                if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt(getSession()))) {
                    datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel
                            .getDateDernierPmt(getSession())));
                }

                JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
                JADate dateTraitement = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem
                        .getDateTraitement()));
                JADate dateJour = JACalendar.today();
                dateJour.setDay(1);

                // Si (dt=dj et dt=dpmt) ou (dt<dj et dt<dpmt et ddeb > dpmt) la
                // préparation de la décision peut s'effectuer
                if (datePmtMensuel != null) {
                    return (((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTLOWER)
                            && (cal.compare(dateTraitement, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) && (cal
                                .compare(dateDebutDroit, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER)) ||

                    ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_EQUALS) && (cal.compare(
                            dateTraitement, datePmtMensuel) == JACalendar.COMPARE_EQUALS)));
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public Boolean isRevenuSplitte() {
        return basesCalcul.isRevenuSplitte();
    }

    @Override
    public void retrieve() throws Exception {
        basesCalcul.retrieve();
    }

    public void retrieve(BITransaction transaction) throws Exception {
        basesCalcul.retrieve(transaction);
    }

    public void setAnneeBonifTacheAssistance(String string) {
        basesCalcul.setAnneeBonifTacheAssistance(string);
    }

    public void setAnneeBonifTacheEduc(String string) {
        basesCalcul.setAnneeBonifTacheEduc(string);
    }

    public void setAnneeBonifTransitoire(String string) {
        basesCalcul.setAnneeBonifTransitoire(string);
    }

    public void setAnneeCotiClasseAge(String string) {
        basesCalcul.setAnneeCotiClasseAge(string);
    }

    public void setAnneeDeNiveau(String string) {
        basesCalcul.setAnneeDeNiveau(string);
    }

    public void setAnneeTraitement(String string) {
        basesCalcul.setAnneeTraitement(string);
    }

    public void setCleInfirmiteAyantDroit(String string) {
        basesCalcul.setCleInfirmiteAyantDroit(string);
    }

    public void setCodeOfficeAi(String string) {
        basesCalcul.setCodeOfficeAi(string);
    }

    public void setDegreInvalidite(String string) {
        basesCalcul.setDegreInvalidite(string);
    }

    public void setDroitApplique(String string) {
        basesCalcul.setDroitApplique(string);
    }

    public void setDureeCotiAvant73(String string) {
        basesCalcul.setDureeCotiAvant73(string);
    }

    public void setDureeCotiDes73(String string) {
        basesCalcul.setDureeCotiDes73(string);
    }

    public void setDureeRevenuAnnuelMoyen(String string) {
        basesCalcul.setDureeRevenuAnnuelMoyen(string);
    }

    public void setEchelleRente(String string) {
        basesCalcul.setEchelleRente(string);
    }

    public void setFacteurRevalorisation(String string) {
        basesCalcul.setFacteurRevalorisation(string);
    }

    @Override
    public void setId(String newId) {
        basesCalcul.setId(newId);
    }

    public void setIdBasesCalcul(String string) {
        basesCalcul.setIdBasesCalcul(string);
    }

    public void setIdRenteCalculee(String string) {
        basesCalcul.setIdRenteCalculee(string);
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        basesCalcul.setIdTiersBaseCalcul(idTiersBaseCalcul);
    }

    @Override
    public void setISession(BISession newSession) {
        basesCalcul.setISession(newSession);
    }

    public void setIsInvaliditePrecoce(Boolean b) {
        basesCalcul.setInvaliditePrecoce(b);
    }

    public void setIsLimiteRevenu(Boolean b) {
        basesCalcul.setLimiteRevenu(b);
    }

    public void setIsMinimuGaranti(Boolean b) {
        basesCalcul.setMinimuGaranti(b);
    }

    public void setIsPartageRevenuActuel(Boolean b) {
        basesCalcul.setIsPartageRevenuActuel(b);
    }

    public void setIsRevenuSplitte(Boolean b) {
        basesCalcul.setRevenuSplitte(b);
    }

    /**
     * @param message
     *            une nouvelle valeur pour cet attribut
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        basesCalcul.setMessage(message);
    }

    /**
     * setter pour l'attribut modifie.
     * 
     * @param modifie
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    public void setMoisAppointsAvant73(String string) {
        basesCalcul.setMoisAppointsAvant73(string);
    }

    public void setMoisAppointsDes73(String string) {
        basesCalcul.setMoisAppointsDes73(string);
    }

    public void setMoisCotiAnneeOuvertDroit(String string) {
        basesCalcul.setMoisCotiAnneeOuvertDroit(string);
    }

    public void setMontantMaxR10Ech44(String string) {
        basesCalcul.setMontantMaxR10Ech44(string);
    }

    /**
     * @param msgType
     *            une nouvelle valeur pour cet attribut
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        basesCalcul.setMsgType(msgType);
    }

    public void setNombreAnneeBTE_1(String s) {
        basesCalcul.setNombreAnneeBTE1(s);
    }

    public void setNombreAnneeBTE_2(String s) {
        basesCalcul.setNombreAnneeBTE2(s);
    }

    public void setNombreAnneeBTE_4(String s) {
        basesCalcul.setNombreAnneeBTE4(s);
    }

    public void setPeriodeAssEtrangerAv73(String string) {
        basesCalcul.setPeriodeAssEtrangerAv73(string);
    }

    public void setPeriodeAssEtrangerDes73(String string) {
        basesCalcul.setPeriodeAssEtrangerDes73(string);
    }

    public void setPeriodeJeunesse(String string) {
        basesCalcul.setPeriodeJeunesse(string);
    }

    public void setPeriodeMariage(String string) {
        basesCalcul.setPeriodeMariage(string);
    }

    public void setReferenceDecision(String s) {
        basesCalcul.setReferenceDecision(s);
    }

    public void setResultatComparatif(String string) {
        basesCalcul.setResultatComparatif(string);
    }

    public void setRevenuAnnuelMoyen(String string) {
        basesCalcul.setRevenuAnnuelMoyen(string);
    }

    public void setRevenuJeunesse(String string) {
        basesCalcul.setRevenuJeunesse(string);
    }

    public void setRevenuPrisEnCompte(String string) {
        basesCalcul.setRevenuPrisEnCompte(string);
    }

    /**
     * setter pour l'attribut session.
     * 
     * @param newSession
     *            une nouvelle valeur pour cet attribut
     */
    public void setSession(BSession newSession) {
        basesCalcul.setSession(newSession);
    }

    public void setSupplementCarriere(String string) {
        basesCalcul.setSupplementCarriere(string);
    }

    public void setSurvenanceEvtAssAyantDroit(String string) {
        basesCalcul.setSurvenanceEvtAssAyantDroit(string);
    }

    public void setTypeCalculComparatif(String string) {
        basesCalcul.setTypeCalculComparatif(string);
    }

    @Override
    public void update() throws Exception {
        basesCalcul.update();
    }

    public void update(BITransaction transaction) throws Exception {
        basesCalcul.update(transaction);
    }

    public boolean validate() {
        return true;
    }

    public void wantCallValidate(boolean newCallValidate) {
        basesCalcul.wantCallValidate(newCallValidate);
    }
}
