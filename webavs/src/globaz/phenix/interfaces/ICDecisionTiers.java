package globaz.phenix.interfaces;

import globaz.globall.api.BIEntity;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author dda
 * 
 */
public interface ICDecisionTiers extends BIEntity {
    public TITiersViewBean _getTiers();

    public String getAncienNumAvs();

    public String getAnneeDecision();

    public String getAnneePrise();

    public Boolean getBloque();

    public String getCollaborateur();

    public String getDateEtat();

    public String getDateInformation();

    public String getDebutActivite();

    public String getDebutDecision();

    public IDecision getDerniereDecision();

    public String getDesignation1();

    public String getDesignation2();

    public String getDesignation3();

    public String getDesignation4();

    public Boolean getDivision2();

    public String getEtat();

    public Boolean getFacturation();

    public String getFinActivite();

    public String getFinDecision();

    public String getGenreAffilie();

    public String getHeureEtat();

    public String getIdAffiliation();

    public String getIdCommunication();

    public String getIdConjoint();

    public String getIdDecision();

    public String getIdIfdDefinitif();

    public String getIdIfdProvisoire();

    public String getIdPassage();

    public String getIdPays();

    public String getIdTiers();

    public Boolean getImpression();

    public String getInteret();

    public String getLangue();

    public String getNumAffilie();

    public String getNumAvsActuel();

    public String getNumContribuableActuel();

    public Boolean getOpposition();

    public String getProrata();

    public String getResponsable();

    public String getSpecification();

    public String getTaxation();

    public TITiersViewBean getTiers();

    public String getTitreTiers();

    public String getTotal();

    public String getTypeDecision();

    public String getTypeTiers();

    public String getUserEtat();

    public Boolean isBloque();

    public Boolean isComplementaire();

    public Boolean isDivision2();

    public Boolean isFacturation();

    public Boolean isImpression();

    public Boolean isLettreSignature();

    public Boolean isOpposition();

    public void setAncienNumAvs(String ancienNumAvs);

    public void setAnneeDecision(String newAnneeDecision);

    public void setAnneePrise(String newAnneePrise);

    public void setBloque(Boolean newBloque);

    public void setCollaborateur(String string);

    public void setComplementaire(Boolean complementaire);

    public void setDateEtat(String dateEtat);

    public void setDateInformation(String newDateInformation);

    public void setDebutActivite(String debutActivite);

    public void setDebutDecision(String newDebutDecision);

    public void setDesignation1(String designation1);

    public void setDesignation2(String designation2);

    public void setDesignation3(String designation3);

    public void setDesignation4(String designation4);

    public void setDivision2(Boolean newDivision2);

    public void setEtat(String etat);

    public void setFacturation(Boolean newFacturation);

    public void setFinActivite(String finActivite);

    public void setFinDecision(String newFinDecision);

    public void setGenreAffilie(String newGenreAffilie);

    public void setHeureEtat(String heureEtat);

    public void setIdAffiliation(String newIdAffiliation);

    public void setIdCommunication(String newIdCommunication);

    public void setIdConjoint(String newIdConjoint);

    public void setIdDecision(String newIdDecision);

    public void setIdIfdDefinitif(String newIdIfdDefinitif);

    public void setIdIfdProvisoire(String newIdIfdProvisoire);

    public void setIdPassage(String newIdPassage);

    public void setIdPays(String idPays);

    public void setIdTiers(String newIdTiers);

    public void setImpression(Boolean newImpression);

    public void setInteret(String newInteret);

    public void setLangue(String langue);

    public void setLettreSignature(Boolean lettreSignature);

    public void setNumAffilie(String numAffilieActuel);

    public void setNumAvsActuel(String numAvsActuel);

    public void setNumContribuableActuel(String numContribuableActuel);

    public void setOpposition(Boolean newOpposition);

    public void setProrata(String newProrata);

    public void setResponsable(String newResponsable);

    public void setSpecification(String specification);

    public void setTaxation(String newTaxation);

    public void setTiers(TITiersViewBean tiers);

    public void setTitreTiers(String titreTiers);

    public void setTotal(String string);

    public void setTypeDecision(String newTypeDecision);

    public void setTypeTiers(String typeTiers);

    public void setUserEtat(String userEtat);
}
