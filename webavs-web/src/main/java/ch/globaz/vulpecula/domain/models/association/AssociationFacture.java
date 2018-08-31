package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.domain.comparators.CotisationAssociationProfessionnelleComparator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AssociationFacture {
    private Administration association;
    private Map<String, CotisationFacture> cotisations = new HashMap<String, CotisationFacture>();
    private Taux rabaisAssociation = new Taux(0);
    private LigneFactureAssociation ligne;

    public Administration getAssociation() {
        return association;
    }

    public void setAssociation(Administration association) {
        this.association = association;
    }

    public Taux getRabaisAssociation() {
        return rabaisAssociation;
    }

    public void setRabaisAssociation(Taux rabaisAssociation) {
        this.rabaisAssociation = rabaisAssociation;
    }

    public void addLigne(LigneFactureAssociation ligne) {
        if (ligne.isRabaisSpecial()) {
            rabaisAssociation = ligne.getTauxCotisation();
            this.ligne = ligne;
        } else {
            CotisationFacture cotisation;
            if (!cotisations.containsKey(ligne.getIdCotisation())) {
                cotisation = new CotisationFacture();
                cotisation.setCotisation(ligne.getCotisationAssociationProfessionnelle());
                cotisations.put(ligne.getIdCotisation(), cotisation);
            }
            cotisation = cotisations.get(ligne.getIdCotisation());
            cotisation.addLigne(ligne);
        }
    }

    public String getLibelle() {
        return association.getDesignation1() + " " + association.getDesignation2();
    }

    public Collection<CotisationFacture> getCotisations() {
        Map<String, CotisationFacture> cotisationsTrie = new TreeMap<String, CotisationFacture>();
        for (CotisationFacture coti : cotisations.values()) {
            cotisationsTrie.put(getKeyTri(coti), coti);
        }
        return cotisationsTrie.values();
    }

    /**
     * @param coti
     * @return
     */
    private String getKeyTri(CotisationFacture coti) {
        return (coti.getPrintOrderCotisation().isEmpty() ? "99999999" : coti.getPrintOrderCotisation()) + "-"
                + coti.getIdCotisation();
    }

    public List<CotisationFacture> getCotisationsOrdreAssociation() {
        List<CotisationFacture> liste = new ArrayList<CotisationFacture>(cotisations.values());
        Collections.sort(liste, new CotisationAssociationProfessionnelleComparator());
        return liste;
    }

    public String getIdAssociation() {
        return association.getId();
    }

    CotisationFacture getAt(String idCotisation) {
        return cotisations.get(idCotisation);
    }

    public Montant getMontantCotisations() {
        Montant montant = Montant.ZERO;
        for (CotisationFacture cotisation : getCotisations()) {
            montant = montant.add(cotisation.getMontantTotal());
        }
        return montant;
    }

    public Montant getMontantRabais() {
        return getMontantCotisations().multiply(rabaisAssociation).negate().normalize();
    }

    public Montant getMontantAssociation() {
        return getMontantRabais().add(getMontantCotisations());
    }

    public List<LigneFactureAssociation> getLignes() {
        List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();
        if (ligne != null) {
            rabaisAssociation = ligne.getTauxCotisation();
            lignes.add(ligne);
        }
        for (CotisationFacture cotisation : getCotisations()) {
            lignes.addAll(cotisation.getLignes());
        }
        return lignes;
    }

    public LigneFactureAssociation getLigneRabais() {
        return ligne;
    }

    public boolean isReduction() {
        return rabaisAssociation != null && !rabaisAssociation.isZero();
    }

    /**
     * WARNING : Cette méthode doit être utilisé uniquement dans une optique de persistence dans un repository. Elle n'a
     * aucune valeur métier et est donc remplacé par l'objet {@link FactureAssociation} Retourne l'entête de facture de
     * la première ligne.
     *
     * @return Entête de facture de la première ligne
     */
    public EnteteFactureAssociation getEntete() {
        return getCotisations().iterator().next().getLignes().get(0).getEnteteFacture();
    }

    public String getCodeAdministration() {
        return association.getCodeAdministration();
    }

    public int getSizeCotisations() {
        return cotisations.size();
    }
}