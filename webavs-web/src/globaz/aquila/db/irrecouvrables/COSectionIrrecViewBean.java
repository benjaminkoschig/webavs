package globaz.aquila.db.irrecouvrables;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.irrecouvrable.CAAmortissementCi;
import globaz.osiris.db.irrecouvrable.CAKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CALigneDePoste;
import globaz.osiris.db.irrecouvrable.CAPoste;
import globaz.osiris.db.irrecouvrable.CATypeLigneDePoste;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author bjo
 * 
 */
public class COSectionIrrecViewBean extends COAbstractViewBeanSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal affectationAffecte;
    private BigDecimal affectationDisponible;
    private BigDecimal affectationSolde;
    private String affectationSoldeToSubmit;
    private Map<Integer, CAAmortissementCi> amortissementCiMap;
    private String descriptionAffilie;
    private String email;
    private boolean hasRubriqueCotPers;
    private String idCompteAnnexe;
    private String idCompteIndividuelAffilie;
    private List<String> idSectionsList;
    private BigDecimal montantAffecteTotal;
    private BigDecimal montantDuTotal;
    private String numeroAffilie;
    private Map<CAKeyPosteContainer, CAPoste> postesMap;
    private BigDecimal soldeTotal;
    private TITiersViewBean tiers;
    private Boolean wantTraiterAmortissementCi = false;

    public boolean displayBlocAmortissementCi() {
        if (getIdCompteIndividuelAffilie() != null) {
            return true;
        }
        if (!getAmortissementCiMap().isEmpty() && hasRubriqueCotPers()) {
            return true;
        }
        return false;
    }

    public void displayPostesMapInConsole() {
        System.out.println("--------------------------- AFFICHAGE DES POSTES ---------------------------");
        String result = "";
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAKeyPosteContainer key = posteEntry.getKey();
            CAPoste poste = posteEntry.getValue();
            result += "\nkey(ordrePriorite, numeroRubriqueIrrecouvrable, annee, , type) : " + key.getOrdrePriorite()
                    + ", " + key.getNumeroRubriqueIrrecouvrable() + ", " + key.getAnnee() + ", " + key.getType();
            result += "\n" + poste.toString();
            result += "###########################################################################";
        }
        System.out.println(result);
    }

    public BigDecimal getAffectationAffecte() {
        return affectationAffecte;
    }

    public BigDecimal getAffectationDisponible() {
        return affectationDisponible;
    }

    public BigDecimal getAffectationSolde() {
        return affectationSolde;
    }

    public String getAffectationSoldeToSubmit() {
        return affectationSoldeToSubmit;
    }

    public Map<Integer, CAAmortissementCi> getAmortissementCiMap() {
        return amortissementCiMap;
    }

    public String getDescriptionAffilie() {
        return descriptionAffilie;
    }

    public String getEmail() throws RemoteException {
        if (JadeStringUtil.isBlank(email)) {
            email = getISession().getUserEMail();
        }
        return email;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteIndividuelAffilie() {
        return idCompteIndividuelAffilie;
    }

    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    public BigDecimal getMontantAffecteTotal() {
        return montantAffecteTotal;
    }

    public BigDecimal getMontantDuTotal() {
        return montantDuTotal;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public Map<CAKeyPosteContainer, CAPoste> getPostesMap() {
        return postesMap;
    }

    public BigDecimal getSoldeTotal() {
        return soldeTotal;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public boolean hasRubriqueCotPers() {
        return hasRubriqueCotPers;
    }

    public Boolean isWantTraiterAmortissementCi() {
        return wantTraiterAmortissementCi;
    }

    public void setAffectationAffecte(BigDecimal affectationAffecte) {
        this.affectationAffecte = affectationAffecte;
    }

    public void setAffectationDisponible(BigDecimal affectationDisponible) {
        this.affectationDisponible = affectationDisponible;
    }

    public void setAffectationSolde(BigDecimal affectationSolde) {
        this.affectationSolde = affectationSolde;
    }

    public void setAffectationSoldeToSubmit(String affectationSoldeToSubmit) {
        this.affectationSoldeToSubmit = affectationSoldeToSubmit;
    }

    public void setAmortissementCiMap(Map<Integer, CAAmortissementCi> amortissementCiMap) {
        this.amortissementCiMap = amortissementCiMap;
    }

    public void setDescriptionAffilie(String descriptionAffilie) {
        this.descriptionAffilie = descriptionAffilie;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHasRubriqueCotPers(boolean hasRubriqueCotPers) {
        this.hasRubriqueCotPers = hasRubriqueCotPers;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdCompteIndividuelAffilie(String idCompteIndividuelAffilie) {
        this.idCompteIndividuelAffilie = idCompteIndividuelAffilie;
    }

    public void setIdSectionsList(List<String> idSectionsList) {
        this.idSectionsList = idSectionsList;
    }

    public void setIdSectionsList(String string) {
        idSectionsList = Arrays.asList(JadeStringUtil.split(string, ',', Integer.MAX_VALUE));
    }

    public void setMontantAffecteTotal(BigDecimal montantAffecteTotal) {
        this.montantAffecteTotal = montantAffecteTotal;
    }

    public void setMontantDuTotal(BigDecimal montantDuTotal) {
        this.montantDuTotal = montantDuTotal;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setPostesMap(Map<CAKeyPosteContainer, CAPoste> postesMap) {
        this.postesMap = postesMap;
    }

    public void setSoldeTotal(BigDecimal soldeTotal) {
        this.soldeTotal = soldeTotal;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setWantTraiterAmortissementCi(Boolean wantTraiterAmortissementCi) {
        this.wantTraiterAmortissementCi = wantTraiterAmortissementCi;
    }

    public void updateMontantAffecteLigneDePoste(String ordrePriorite, String numeroRubriqueIrrecouvrable,
            String annee, String type, String idSection, String nouveauMontantAffecte) {

        CATypeLigneDePoste typeligne;
        if (type.equals(CATypeLigneDePoste.SIMPLE.name())) {
            typeligne = CATypeLigneDePoste.SIMPLE;
        } else if (type.equals(CATypeLigneDePoste.SALARIE.name())) {
            typeligne = CATypeLigneDePoste.SALARIE;
        } else {
            typeligne = CATypeLigneDePoste.EMPLOYEUR;
        }

        // création de la key pour identifier le poste
        CAKeyPosteContainer keyPosteContainer = new CAKeyPosteContainer(new Integer(annee),
                numeroRubriqueIrrecouvrable, ordrePriorite, typeligne);

        // récupération du poste et de la ligne puis mise à jour de cette dernière
        CAPoste poste = null;
        if (postesMap.containsKey(keyPosteContainer)) {
            poste = postesMap.get(keyPosteContainer);
            CALigneDePoste ligneDePoste = poste.getLigneDePoste(idSection, typeligne);
            BigDecimal nouveauMontantAffecteBD = new BigDecimal(nouveauMontantAffecte);
            if (ligneDePoste.getMontantAffecte().compareTo(nouveauMontantAffecteBD) != 0) {
                ligneDePoste.setMontantAffecte(nouveauMontantAffecteBD);
            }
        } else {
            System.out.println("PosteKey not found : \n" + keyPosteContainer.toString());
        }

    }

    public void updateMontantAmortissementCi(String annee, String nouveauMontantAmortissement) {
        Integer anneeKey = new Integer(annee);
        BigDecimal nouveauMontant = new BigDecimal(nouveauMontantAmortissement);
        CAAmortissementCi amortissementCi = amortissementCiMap.get(anneeKey);
        amortissementCi.setMontantAmortissement(nouveauMontant);
    }

    public boolean validerViewBean() throws RemoteException {
        if (JadeStringUtil.isBlank(getAffectationSoldeToSubmit())
                || (new BigDecimal(getAffectationSoldeToSubmit()).signum() != 0)) {
            _addError(((BSession) getISession()).getLabel("IRRECOUVRABLE_SOLDE_AFFECTATION_ERROR"));
        }

        if (getISession().hasErrors()) {
            setMessage(getISession().getErrors().toString());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return FWViewBeanInterface.OK.equals(getMsgType());
    }
}
