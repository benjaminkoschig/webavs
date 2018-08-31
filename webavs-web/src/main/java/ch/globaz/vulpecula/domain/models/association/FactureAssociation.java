package ch.globaz.vulpecula.domain.models.association;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class FactureAssociation implements DomainEntity, Serializable {
    private static final long serialVersionUID = 2687890045234435339L;
    private static final String GENRE_ASSOCIATION_PROFESSIONNELLE = "68900004";
    private static final String GENRE_CPP = "68900007";

    private String id;
    private Map<String, AssociationFacture> associations = new HashMap<String, AssociationFacture>();
    private Administration associationParent;
    private ModeleEntete modele;
    private Employeur employeur;
    private Annee annee;
    private EtatFactureAP etat;
    private Passage passageFacturation;

    public FactureAssociation() {
    }

    public FactureAssociation(List<LigneFactureAssociation> lignes) {
        addLignes(lignes);
    }

    public Collection<AssociationFacture> getAssociations() {
        TreeMap<String, AssociationFacture> associationsTrie = new TreeMap<String, AssociationFacture>();
        for (AssociationFacture assoc : associations.values()) {
            associationsTrie.put(assoc.getCodeAdministration(), assoc);
        }
        return associationsTrie.values();
    }

    public ModeleEntete getModele() {
        return modele;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public Administration getAssociationParent() {
        return associationParent;
    }

    public void setAssociationParent(Administration associationParent) {
        this.associationParent = associationParent;
    }

    public Annee getAnnee() {
        return annee;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public void setModele(ModeleEntete modele) {
        this.modele = modele;
    }

    public String getNumeroFacture() {
        return getEnteteFacture().getNumeroSection();
    }

    public EtatFactureAP getEtat() {
        return etat;
    }

    public void setEtat(EtatFactureAP etat) {
        this.etat = etat;
    }

    public String getIdPassageFacturation() {
        if (passageFacturation == null) {
            return null;
        }
        return passageFacturation.getId();
    }

    public Passage getPassageFacturation() {
        return passageFacturation;
    }

    public void setPassageFacturation(Passage passageFacturation) {
        this.passageFacturation = passageFacturation;
    }

    public Date getDateFacturation() {
        if (passageFacturation == null) {
            return null;
        }
        if (!passageFacturation.getDateFacturation().isEmpty()) {
            return new Date(passageFacturation.getDateFacturation());
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setSpy(String spy) {
    }

    public boolean containsAssociation(String idAssociation) {
        return associations.containsKey(idAssociation);
    }

    public AssociationFacture getAssociation(String idAssociation) {
        return associations.get(idAssociation);
    }

    public void addAssociation(AssociationFacture association) {
        associations.put(association.getIdAssociation(), association);
    }

    AssociationFacture getAt(String idAssociation) {
        return associations.get(idAssociation);
    }

    public EnteteFactureAssociation getEnteteFacture() {
        return associations.values().iterator().next().getEntete();
    }

    public List<LigneFactureAssociation> getLignes() {
        List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();
        for (AssociationFacture association : associations.values()) {
            lignes.addAll(association.getLignes());
        }
        return lignes;
    }

    public Montant getMontantFacture() {
        Montant montant = Montant.ZERO;
        for (AssociationFacture association : getAssociations()) {
            montant = montant.add(association.getMontantAssociation());
        }
        return montant;
    }

    public void addLignes(List<LigneFactureAssociation> lignes) {
        for (LigneFactureAssociation ligne : lignes) {
            addLigne(ligne);
        }
    }

    public void addLigne(LigneFactureAssociation ligne) {
        AssociationFacture association;
        if (containsAssociation(ligne.getIdAssociationProfessionnelle())) {
            association = getAssociation(ligne.getIdAssociationProfessionnelle());
        } else {
            association = new AssociationFacture();
            association.setAssociation(ligne.getAssociation());
            addAssociation(association);
        }

        association.addLigne(ligne);
    }

    public List<CotisationFacture> getCotisations() {
        List<CotisationFacture> listeCoti = new ArrayList<CotisationFacture>();
        for (AssociationFacture association : getAssociations()) {
            listeCoti.addAll(association.getCotisations());
        }
        return listeCoti;
    }

    public String getAdresseEmployeur() {
        return employeur.getAdressePrincipale().getAdresseFormatte();
    }

    public String getNumeroAffilie() {
        return employeur.getAffilieNumero();
    }

    /**
     * @return Code système représentant la langue de l'employeur
     */
    public String getEmployeurCSLangue() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur de la facture d'association " + getId() + " est null !");
        }

        return employeur.getLangue();
    }

    /**
     * @return Code système représentant la langue de l'employeur
     */
    public CodeLangue getEmployeurLangue() {
        return CodeLangue.fromValue(getEmployeurCSLangue());
    }

    /**
     * @return le numéro d'affilié de l'employeur
     */
    public String getEmployeurAffilieNumero() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur de la facture d'association " + getId() + " est null !");
        }
        return employeur.getAffilieNumero();
    }

    public String getEmployeurRaisonSociale() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur de la facture d'association " + getId() + " est null !");
        }
        return employeur.getRaisonSociale();
    }

    /**
     * @return l'id tiers de l'employeur
     */
    public String getEmployeurIdTiers() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur de la facture d'association " + getId() + " est null !");
        }

        return employeur.getIdTiers();
    }

    public String getAssociationProfessionnelleGenreAdmin() {
        return getEnteteFacture().getAssociationProfessionnelleParent().getGenre();
    }

    /**
     * @param facture
     * @return
     */
    public boolean isMembre() {
        return GENRE_ASSOCIATION_PROFESSIONNELLE.equals(getAssociationProfessionnelleGenreAdmin());
    }
}
