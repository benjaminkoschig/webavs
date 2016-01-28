package globaz.aquila.db.irrecouvrables;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.osiris.db.irrecouvrable.CARecouvrementCi;
import globaz.osiris.db.irrecouvrable.CARecouvrementKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementPoste;
import globaz.osiris.db.irrecouvrable.CARecouvrementVentilateur;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CORecouvrementCotisationsViewBean extends COAbstractViewBeanSupport {

    private static final long serialVersionUID = -7633398785519256349L;
    private String annee;
    private String idCompteAnnexe;
    private List<String> idSectionsList;
    private String montantARecouvrir;
    private String montantDisponible;
    private String montantNoteDeCreditCumul;
    private List<String> selectedBasesAmortissement = new ArrayList<String>();
    private CARecouvrementVentilateur ventilateur;
    private Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPostesMap;
    private Map<Integer, CARecouvrementCi> recouvrementCiMap;
    private String numeroAffilie;
    private String descriptionAffilie;
    private TITiersViewBean tiers;
    private String idCompteIndividuelAffilie;
    private BigDecimal amortissementTotal;
    private BigDecimal dejaRecouvertTotal;
    private BigDecimal recouvrementTotal;
    private BigDecimal soldeTotal;
    private boolean hasRubriqueCotPers;
    private String email;
    private String nomAffilie;
    private boolean effectuerRectificationCI;

    public boolean getHasRecouvrementPosteOnError() {
        for (CARecouvrementPoste poste : recouvrementPostesMap.values()) {
            if (poste.isRecouvrementPosteOnError()) {
                return true;
            }
        }

        return false;
    }

    public Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> getRecouvrementPostesMap() {
        return recouvrementPostesMap;
    }

    public void setRecouvrementPostesMap(Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPostesMap) {
        this.recouvrementPostesMap = recouvrementPostesMap;
    }

    public String getAnnee() {
        return annee;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    /**
     * Retourne la liste des id de section sous la forme d'un chaîne dans laquelle les id sont séparée par une ','
     * 
     * @return chaine contenant la liste des id de section
     */
    public String getStringIdSectionsList() {
        StringBuilder stringIdSectionsList = new StringBuilder();

        for (int i = 0; i < getIdSectionsList().size(); i++) {

            stringIdSectionsList.append(getIdSectionsList().get(i));

            if (i != (getIdSectionsList().size() - 1)) {
                stringIdSectionsList.append(",");
            }
        }

        return stringIdSectionsList.toString();
    }

    public String getMontantARecouvrir() {
        return montantARecouvrir;
    }

    public String getMontantDisponible() {
        return montantDisponible;
    }

    public List<String> getSelectedBasesAmortissement() {
        return selectedBasesAmortissement;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdSectionsList(List<String> idSectionsList) {
        this.idSectionsList = idSectionsList;
    }

    public void setMontantARecouvrir(String montantARecouvrir) {
        this.montantARecouvrir = montantARecouvrir;
    }

    public void setMontantDisponible(String montantDisponible) {
        this.montantDisponible = montantDisponible;
    }

    public void setSelectedBasesAmortissement(List<String> selectedBasesAmortissement) {
        this.selectedBasesAmortissement = selectedBasesAmortissement;
    }

    public CARecouvrementVentilateur getVentilateur() {
        return ventilateur;
    }

    public void setVentilateur(CARecouvrementVentilateur ventilateur) {
        this.ventilateur = ventilateur;
    }

    public void displayRecouvrementPostesMapInConsole() {
        System.out.println("--------------------------- AFFICHAGE DES POSTES ---------------------------");
        String result = "";
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementKeyPosteContainer key = recouvrementPosteEntry.getKey();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            result += "\nkey(ordrePriorite, numeroRubriqueIrrecouvrable, annee, , type) : " + key.getOrdrePriorite()
                    + ", " + key.getNumeroRubriqueRecouvrement();
            result += "\n" + recouvrementPoste.toString();
            result += "###########################################################################";
        }
        System.out.println(result);
    }

    public Map<Integer, CARecouvrementCi> getRecouvrementCiMap() {
        return recouvrementCiMap;
    }

    public void setRecouvrementCiMap(Map<Integer, CARecouvrementCi> recouvrementCiMap) {
        this.recouvrementCiMap = recouvrementCiMap;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getDescriptionAffilie() {
        return descriptionAffilie;
    }

    public void setDescriptionAffilie(String descriptionAffilie) {
        this.descriptionAffilie = descriptionAffilie;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public String getIdCompteIndividuelAffilie() {
        return idCompteIndividuelAffilie;
    }

    public void setIdCompteIndividuelAffilie(String idCompteIndividuelAffilie) {
        this.idCompteIndividuelAffilie = idCompteIndividuelAffilie;
    }

    public BigDecimal getAmortissementTotal() {
        return amortissementTotal;
    }

    public void setAmortissementTotal(BigDecimal amortissementTotal) {
        this.amortissementTotal = amortissementTotal;
    }

    public BigDecimal getDejaRecouvertTotal() {
        return dejaRecouvertTotal;
    }

    public void setDejaRecouvertTotal(BigDecimal dejaRecouvertTotal) {
        this.dejaRecouvertTotal = dejaRecouvertTotal;
    }

    public BigDecimal getRecouvrementTotal() {
        return recouvrementTotal;
    }

    public void setRecouvrementTotal(BigDecimal recouvrementTotal) {
        this.recouvrementTotal = recouvrementTotal;
    }

    public BigDecimal getSoldeTotal() {
        return soldeTotal;
    }

    public void setSoldeTotal(BigDecimal soldeTotal) {
        this.soldeTotal = soldeTotal;
    }

    public boolean isHasRubriqueCotPers() {
        return hasRubriqueCotPers;
    }

    public void setHasRubriqueCotPers(boolean hasRubriqueCotPers) {
        this.hasRubriqueCotPers = hasRubriqueCotPers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public boolean isEffectuerRectificationCI() {
        return effectuerRectificationCI;
    }

    public void setEffectuerRectificationCI(boolean effectuerRectificationCI) {
        this.effectuerRectificationCI = effectuerRectificationCI;
    }

    /**
     * @return the montantNoteDeCreditCumul
     */
    public String getMontantNoteDeCreditCumul() {
        return montantNoteDeCreditCumul;
    }

    /**
     * @param montantNoteDeCreditCumul the montantNoteDeCreditCumul to set
     */
    public void setMontantNoteDeCreditCumul(String montantNoteDeCreditCumul) {
        this.montantNoteDeCreditCumul = montantNoteDeCreditCumul;
    }
}