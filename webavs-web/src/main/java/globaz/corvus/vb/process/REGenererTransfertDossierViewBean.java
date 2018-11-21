package globaz.corvus.vb.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater06;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

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
    
    private List<String> listNss; 
    private List<String> listExConjoint;
    private Map<String, PRTiersWrapper> mapNssId;

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
    
    private void genereListNss() throws Exception {
        
            
        listExConjoint = getListExConjoint(idTiers);
        
        List<RERenteLieeJointPrestationAccordee> rentesLiees = getRentesLieesEnCours(getSession(), idTiers);
        
        Set<String> nssUnique = new HashSet<>();
        mapNssId = new HashMap<>();
        
        for(RERenteLieeJointPrestationAccordee rente : rentesLiees) {
            if(isRenteEnfant(rente) || isRenteSurvivant(rente)) {
                addNssFromTiers(rente.getIdTiersComplementaire1(), nssUnique);
            } else if(isRentePrincipale(rente)){
                addNssFromTiersSansExConjoint(rente.getIdTiersBeneficiaire(), nssUnique);
            }
        }
        
        listNss = new ArrayList<>(); 
        
        for(String nssToAdd:nssUnique) {
            listNss.add(nssToAdd);
        }
        
        int reste = 8 - listNss.size();
        for(int i = 0; i <= reste;i++) {
            listNss.add("");
        }
            
    }
    
    private List<String> getListExConjoint(String idTiers) throws Exception {
        List<String> listEx = new ArrayList<>();
        
      // exConjoints
      List<PRTiersWrapper> exConjoints =  SFFamilleUtils.getExConjointsTiers(getSession(),
          ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);
          
      for (PRTiersWrapper unExConjoint : exConjoints) {
          listEx.add(unExConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
      }
      
      return listEx;
        
    }
    
    public void updateIdTiers() throws Exception  {
        for(String nss : listNss) { 
            if(!JadeStringUtil.isEmpty(nss)) {
                PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), nss);
                mapNssId.put(nss, tiers);
            }
        }
    }

    private List<RERenteLieeJointPrestationAccordee> getRentesLieesEnCours(final BSession session,
            final String idTiers) throws Exception {

        List<RERenteLieeJointPrestationAccordee> rentesComplementaires = new ArrayList<RERenteLieeJointPrestationAccordee>();

        RERenteLieeJointPrestationAccordeeManager manager = new RERenteLieeJointPrestationAccordeeManager();
        manager.setSession(session);
        manager.setForIdTiersLiant(idTiers);
        manager.setForCsEtatIn(
                Arrays.asList(IREPrestationAccordee.CS_ETAT_VALIDE, IREPrestationAccordee.CS_ETAT_PARTIEL));
        manager.find(BManager.SIZE_USEDEFAULT);

        for (int i = 0; i < manager.getSize(); i++) {
            RERenteLieeJointPrestationAccordee prestation = (RERenteLieeJointPrestationAccordee) manager.get(i);

            if (JadeStringUtil.isBlankOrZero(prestation.getDateFinDroit())) {
                rentesComplementaires.add(prestation);
            }
        }

        return rentesComplementaires;
    }
    
    private void addNssFromTiersSansExConjoint(String idTiers, Set<String> nssUnique) throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if(tiers != null) {
            String nssTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            if(!listExConjoint.contains(nssTiers)) {
                nssUnique.add(nssTiers);
                mapNssId.put(nssTiers, tiers);
            }
        }
    }
    
    private void addNssFromTiers(String idTiers, Set<String> nssUnique) throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if(tiers != null) {
            String nssTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nssUnique.add(nssTiers);
            mapNssId.put(nssTiers, tiers);
       }
    }
    
    private boolean isRenteEnfant(RERenteLieeJointPrestationAccordee rente) {
        return REGenrePrestationEnum.groupeEnfant.contains(rente.getCodePrestation()); 
    }
    
    private boolean isRenteSurvivant(RERenteLieeJointPrestationAccordee rente) {
        return REGenresPrestations.GENRE_13.equals(rente.getCodePrestation()) || REGenresPrestations.GENRE_23.equals(rente.getCodePrestation()); 
    }
    
    private boolean isRentePrincipale(final RERenteLieeJointPrestationAccordee rente) {
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
        return codePrestation.isRentePrincipale();
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
     * @throws Exception 
     */
    public String getTitreEcran() throws Exception {

        loadDemandeRente();

        if (demandeRente != null) {
            
            if(listNss == null || listNss.isEmpty()) {
                genereListNss();
            } else {
                updateIdTiers();
             }

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

    public String getNss0() {
        return getNssNum(0);
    }

    public void setNss0(String nss0) {
        setNssNum(0, nss0);
    }

    public String getNss1() {
        return getNssNum(1);
    }

    public void setNss1(String nss1) {
        setNssNum(1, nss1);
    }

    public String getNss2() {
        return getNssNum(2);
    }

    public void setNss2(String nss2) {
        setNssNum(2, nss2);
    }
    
    public String getNss3() {
        return getNssNum(3);
    }

    public void setNss3(String nss3) {
        setNssNum(3, nss3);
    }
    
    public String getNss4() {
        return getNssNum(4);
    }

    public void setNss4(String nss4) {
        setNssNum(4, nss4);
    }
    
    public String getNss5() {
        return getNssNum(5);
    }

    public void setNss5(String nss5) {
        setNssNum(5, nss5);
    }
    
    public String getNss6() {
        return getNssNum(6);
    }

    public void setNss6(String nss6) {
        setNssNum(6, nss6);
    }
    
    public String getNss7() {
        return getNssNum(7);
    }

    public void setNss7(String nss7) {
        setNssNum(7, nss7);
    }
   
    private void setNssNum(int index, String nss) {
        if(listNss.size() <= index) {
            for (int i = 0; i <  index + 1 - listNss.size(); i++) {
                listNss.add("");
            }
        }
        listNss.set(index, nss);
    }
    
    private String getNssNum(int index) {
        if(listNss != null && listNss.size() > index) {
            return listNss.get(index);
        }
        return "";
    }

    public List<String> getListNss() {
        return listNss;
    }

    public Map<String, PRTiersWrapper> getMapNssId() {
        return mapNssId;
    }

}
