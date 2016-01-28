package globaz.corvus.vb.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater06;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Iterator;

public class REGenererTransfertDossierViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String MOTIF_DOMICILE_ETRANGER = "3";
    public static final String MOTIF_PERCEPTION_DERNIERES_COTI = "2";
    public static final String MOTIF_RENTE_AVS_AI = "1";
    private String DateEnvoi = "";
    private REDemandeRente demandeRente = null;
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String idDemandeRente = "";
    private String idInfoCompl = "";
    private String idTiers = "";
    private PRInfoCompl infoCompl = null;
    // Utiliser pour la mise en GED
    private Boolean isSendToGed = Boolean.FALSE;
    private String langueAssure = "";
    private String MoisCessationPaiement = "";
    private String motif = "";

    private String motifTransmission = "";
    private String nomAssure = "";

    private String nomPrenomTiers = "";
    private String nss = "";

    private String remarque = "";
    private String remarqueTraEncous = "";
    private String texteRemarque = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private boolean asRaEnCours() {
        try {

            boolean asRaEnCours = false;
            loadDemandeRente();

            RERenteAccJoinTblTiersJoinDemRenteManager ram = new RERenteAccJoinTblTiersJoinDemRenteManager();
            ram.setSession(getSession());
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
            String nss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            if (nss.length() > 14) {
                ram.setLikeNumeroAVSNNSS("true");
            } else {
                ram.setLikeNumeroAVSNNSS("false");
            }
            ram.wantCallMethodBeforeFind(true);
            ram.setLikeNumeroAVS(nss);
            ram.setRechercheFamille(true);
            ram.setForCsEtatNotIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", " + IREPrestationAccordee.CS_ETAT_AJOURNE);

            ram.find();

            if (ram.isEmpty()) {
                return asRaEnCours;
            } else {
                for (Iterator iterator = ram.iterator(); iterator.hasNext();) {
                    RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) iterator
                            .next();

                    if (JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
                        asRaEnCours = true;
                        break;
                    }
                }
                return asRaEnCours;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getDateEnvoi() {
        return DateEnvoi;
    }

    public String getDatePaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

    public String getDateTransfertValideDepuisInfoCompl() {
        loadInfoCompl();
        return infoCompl.getDateInfoCompl();
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getLangueAssure() {
        return langueAssure;
    }

    public String getMoisCessationPaiement() {
        return MoisCessationPaiement;
    }

    public String getMotif() {
        return motif;
    }

    public String getMotifTransmission() {
        return motifTransmission;
    }

    public String getNomAssure() {
        return nomAssure;
    }

    public String getNomPrenomTiers() {
        return nomPrenomTiers;
    }

    public String getNss() {
        return nss;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getRemarqueTraEncous() {
        return remarqueTraEncous;
    }

    public String getTexteRemarque() {
        return texteRemarque;
    }

    /**
     * Donne le titre de l'ecran en fonction du document a imprimer
     * 
     * @return
     */
    public String getTitreEcran() {

        loadDemandeRente();

        if (demandeRente != null) {

            if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demandeRente.getCsTypeCalcul())
                    && (!isValide() || !asRaEnCours())) {

                return getSession().getLabel("JSP_LTD_D_TITRE_TRANSFER_CAISSE_COMP");

            } else if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul())
                    && !isValide()) {

                return getSession().getLabel("JSP_LTD_D_TITRE_DEMANDE_CALCUL_PREV");

            } else if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demandeRente.getCsTypeCalcul()) && isValide()
                    && asRaEnCours()) {
                return getSession().getLabel("JSP_LTD_D_TITRE_TRANSFER_CAISSE_COMP_VALIDE");
            }
        }
        return "";
    }

    /**
     * Vrais si le docement a imprimer est "Demande calcul previsionnel"
     * 
     * @return
     */
    public boolean isDemandeCalculPrevisionnel() {
        loadDemandeRente();

        if (demandeRente != null) {
            if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul()) && !isValide()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vrais si le docement a imprimer est "Transfer à caisse competante"
     * 
     * @return
     */
    public boolean isTransfertCaisseCompetente() {
        loadDemandeRente();

        if (demandeRente != null) {
            // BZ 4577 si la demande de rente est en état validée mais qu'elle
            // n'a plus de rente accordées en cours, c'est ce document qui sera
            // imprimé
            if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demandeRente.getCsTypeCalcul())
                    && (!isValide() || !asRaEnCours())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTransfertCaisseCompetenteAndValidate() {
        loadDemandeRente();

        // BZ 4577 pour ce type de document, la demande doit avoir des ra en
        // cours
        if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demandeRente.getCsTypeCalcul()) && isValide()
                && asRaEnCours()) {
            return true;
        }
        return false;
    }

    public boolean isValide() {

        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())) {
            return true;
        }
        return false;
    }

    private void loadDemandeRente() {
        if (demandeRente == null) {
            demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            try {
                demandeRente.retrieve();
            } catch (Exception e) {
                demandeRente = null;
            }
        }
    }

    private void loadInfoCompl() {
        if (infoCompl == null) {
            infoCompl = new PRInfoCompl();
            infoCompl.setSession(getSession());
            infoCompl.setIdInfoCompl(getIdInfoCompl());
            try {
                infoCompl.retrieve();
            } catch (Exception e) {
                infoCompl = null;
            }
        }
    }

    public void setDateEnvoi(String dateEnvoi) {
        DateEnvoi = dateEnvoi;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdInfoCompl(String idInfoCompl) {
        this.idInfoCompl = idInfoCompl;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setLangueAssure(String langueAssure) {
        this.langueAssure = langueAssure;
    }

    public void setMoisCessationPaiement(String moisCessationPaiement) {
        MoisCessationPaiement = moisCessationPaiement;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setMotifTransmission(String motifTransmission) {
        this.motifTransmission = motifTransmission;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public void setNomPrenomTiers(String nomPrenomTiers) {
        this.nomPrenomTiers = nomPrenomTiers;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setRemarqueTraEncous(String remarqueTraEncous) {
        this.remarqueTraEncous = remarqueTraEncous;
    }

    public void setTexteRemarque(String texteRemarque) {
        this.texteRemarque = texteRemarque;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {

        boolean isValidate = true;

        if (JadeStringUtil.isEmpty(eMailAddress)) {
            _addError("JSP_LTD_D_ERROREMAILNULL");
            isValidate = false;
        }

        if (isTransfertCaisseCompetenteAndValidate()) {
            if (JadeStringUtil.isEmpty(motifTransmission)) {
                _addError("JSP_LTD_D_ERRORMOTIFNULL");
                isValidate = false;
            }

            if (JadeStringUtil.isEmpty(MoisCessationPaiement)) {
                _addError("JSP_LTD_D_ERRORCESSATIONPAIEMENTNULL");
                isValidate = false;
            } else if (getMoisCessationPaiement().length() != 7) {
                _addError("JSP_LTD_D_ERRORCESSATIONPAIEMENT_FORMAT");
                isValidate = false;
            } else if (!validerDateCessationPmt(MoisCessationPaiement)) {
                _addError("JSP_LTD_D_ERRORCESSATIONPAIEMENT_PLUS_PETIT_DERNIER_PMT");
                isValidate = false;
            }

            if (JadeStringUtil.isEmpty(DateEnvoi)) {
                _addError("JSP_LTD_D_ERRORDATEENVOINULL");
                isValidate = false;
            }
        }

        if (isTransfertCaisseCompetente() || isDemandeCalculPrevisionnel()) {
            try {
                // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                // se trouvant dans le fichier corvus.properties
                if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), getIdTiers(),
                        REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater06(),
                        JACalendar.todayJJsMMsAAAA()))) {
                    addErrorAvecMessagePret(getSession().getLabel("JSP_LTD_D_TIERS_SANS_ADRESSE") + " NSS " + nss + " "
                            + getNomPrenomTiers());
                    isValidate = false;
                }
            } catch (Exception e) {
                _addError(e.toString());
                e.printStackTrace();
            }

        }

        if (isDemandeCalculPrevisionnel()) {
            if (getMotif().equals(REGenererTransfertDossierViewBean.MOTIF_RENTE_AVS_AI)) {
                if (JadeStringUtil.isEmpty(getNomAssure())) {
                    _addError("JSP_LTD_D_ERREUR_ASSURE_VIDE");
                    isValidate = false;
                }
            }
        }
        return isValidate;
    }

    private boolean validerDateCessationPmt(String dateCessationPmt) {

        JACalendarGregorian j = new JACalendarGregorian();

        try {
            int i;

            i = j.compare(dateCessationPmt, REPmtMensuel.getDateDernierPmt(getSession()));

            if ((JACalendar.COMPARE_FIRSTUPPER == i) || (JACalendar.COMPARE_EQUALS == i)) {
                return true;

            } else {
                return false;
            }
        } catch (JAException e) {
            _addError(e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
