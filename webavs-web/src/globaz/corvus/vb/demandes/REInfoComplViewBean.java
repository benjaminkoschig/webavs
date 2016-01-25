package globaz.corvus.vb.demandes;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.pyxis.api.ITIPersonne;

/**
 * @author PBA
 */

public class REInfoComplViewBean extends PRInfoCompl implements FWViewBeanInterface {

    private static final long serialVersionUID = 1L;
    private String csEtatDemandeRente;
    private String csSexe;
    private String csTypeCalcul;
    private String csTypeDemandeRente;
    private String idDemandeRente;
    private String idTiers;
    private boolean isBloque;
    private String nss;

    public REInfoComplViewBean() {
        super();

        csEtatDemandeRente = "";
        csSexe = "";
        csTypeCalcul = "";
        csTypeDemandeRente = "";
        idDemandeRente = "";
        idTiers = "";
        nss = "";
    }

    private boolean hasRaEnCours() {
        try {

            RERenteAccJoinTblTiersJoinDemRenteManager ram = new RERenteAccJoinTblTiersJoinDemRenteManager();
            ram.setSession(getSession());

            boolean isLikeNumeroAVSNNSS = (nss.length() > 14);

            ram.setLikeNumeroAVSNNSS(Boolean.toString(isLikeNumeroAVSNNSS));
            ram.wantCallMethodBeforeFind(true);
            ram.setLikeNumeroAVS(nss);
            ram.setRechercheFamille(true);
            ram.setForCsEtatNotIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", " + IREPrestationAccordee.CS_ETAT_AJOURNE);

            ram.find();

            if (!ram.isEmpty()) {
                for (int i = 0; i < ram.size(); i++) {
                    RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) ram.get(i);

                    if (JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return false;
    }

    /**
     * Prédicat informant si les conditions autorisant l'utilisation du bouton "Print" pour une info complémentaire de
     * type "rente veuve perdure" sont remplies
     * 
     * @return Résultat du prédicat
     */
    public boolean doPrintButtonGenereRenteVeuvePerdure() {
        return isInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE);
    }

    /**
     * Prédicat informant si les conditions autorisant l'utilisation du bouton "Print" pour une info complémentaire de
     * type "transfert dossier" sont remplies
     * 
     * @return Résultat du prédicat
     */
    public boolean doPrintButtonGenereTransfertDossier() {
        if (isInfoComplementaireTransfertDossier()) {
            // 1er cas: demande validé
            if (isEtatDemandeValide() && isTransfertCaisseCompetenteAndValidate()) {
                return true;
            }

            // 2ème cas: demande enregistrée ou calculée ou au calcul
            if (isEtatDemandeEnregistre() || isEtatDemandeRenteAuCalcul() || isEtatDemandeCalcule()) {
                return true;
            }
        }

        return false;
    }

    private boolean isEtatDemandeCalcule() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
    }

    private boolean isEtatDemandeRenteAuCalcul() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL);
    }

    private boolean isEtatDemandeEnregistre() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
    }

    /**
     * Prédicat informant si les conditions autorisant l'affichage du bouton "Print" sont remplies
     * 
     * @return Résultat du prédicat
     */
    public boolean doShowPrintButton() {
        // Le viewbean ne doit pas comporter d'erreur ...
        if (hasNoError() && REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
            if (doPrintButtonGenereRenteVeuvePerdure()) {
                return true;
            }

            if (doPrintButtonGenereTransfertDossier()) {
                return true;
            }
        }

        return false;
    }

    public String getCsEtatDemandeRente() {
        return csEtatDemandeRente;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeCalcul() {
        return csTypeCalcul;
    }

    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public boolean getIsBloque() {
        return isBloque;
    }

    public String getNss() {
        return nss;
    }

    /**
     * Prédicat informant si le viewbean possède un message de type erreur
     * 
     * @return Résultat du prédicat
     */
    public boolean hasNoError() {
        return FWViewBeanInterface.ERROR.equals(getMsgType()) == false;
    }

    public boolean isCalculPrevisionnel() {
        return isTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL);
    }

    /**
     * BZ 5493, on affiche l'option décès que si c'est une demande de rente vieillesse
     * 
     * @return <code>true</code> s'il la demande est de type "Vieillesse", sinon <code>false</code>
     */
    public boolean isDemandeRenteVieillesse() {
        return IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(csTypeDemandeRente);
    }

    public boolean isEtatDemandeValide() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
    }

    private boolean isEtatDemande(String etat) {
        return (etat != null) && etat.equals(csEtatDemandeRente);
    }

    private boolean isEtatDemandeRenteCourantValide() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
    }

    private boolean isEtatDemandeRenteValide() {
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
    }

    public boolean isEtatTransfert() {
        // si la demande est dans l'état transféré ou terminé, aucune
        // modification n'est possible
        return isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)
                || isEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
    }

    private boolean isFemme() {
        return ITIPersonne.CS_FEMME.equals(csSexe);
    }

    private boolean isInfoCompl(String type) {
        return (type != null) && type.equals(getTypeInfoCompl());
    }

    public boolean isInfoComplementaireDeces() {
        return isInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_DECES);
    }

    public boolean isInfoComplementaireEnvoye() {
        return isInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_ENVOYE);
    }

    private boolean isInfoComplementaireTransfertDossier() {
        return isInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_TRANSFERT_DOSSIER);
    }

    public boolean isTransfertCaisseCompetenteAndValidate() {
        // BZ 4580, Ne pas afficher le bouton imprimer dans la jsp si transfert avec
        // demande validé et interdiction des validations activée.
        return isTypeCalculStandard() && isEtatDemandeValide() && hasRaEnCours();
    }

    private boolean isTypeCalcul(String typeCalcul) {
        return (typeCalcul != null) && typeCalcul.equals(csTypeCalcul);
    }

    private boolean isTypeCalculStandard() {
        return isTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_STANDARD);
    }

    public boolean isValide() throws Exception {
        // si la demande est dans l'état validé ou courant validé, certaines
        // modifications sont possibles
        return isEtatDemandeRenteValide() || isEtatDemandeRenteCourantValide();
    }

    /**
     * BZ 5462<br/>
     * Pour qu'il n'y ai la possibilité d'avoir une rente de veuve perdure qu'avec une rente vieillesse et dans un des
     * états suivant :<br/>
     * <li>Enregistré</li> <li>Au calcul</li> <li>Calculé</li> <li>Terminé</li><br/>
     * <br/>
     * 
     * @return <code>true</code> s'il est autorisé d'afficher l'option "rente de veuve perdure", sinon
     *         <code>false</code>
     */
    public boolean isValideForRenteVeuvePerdure() {
        if (isFemme()) {
            if (isDemandeRenteVieillesse()) {

                String[] etatsAutorises = { IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE,
                        IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL, IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE,
                        IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE };

                for (String etat : etatsAutorises) {
                    if (isEtatDemande(etat)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void setCsEtatDemandeRente(String csEtatDemandeRente) {
        this.csEtatDemandeRente = csEtatDemandeRente;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeCalcul(String csTypeCalcul) {
        this.csTypeCalcul = csTypeCalcul;
    }

    public void setCsTypeDemandeRente(String csTypeDemandeRente) {
        this.csTypeDemandeRente = csTypeDemandeRente;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsBloque(boolean isBloque) {
        this.isBloque = isBloque;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }
}
