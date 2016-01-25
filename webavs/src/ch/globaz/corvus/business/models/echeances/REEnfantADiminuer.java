package ch.globaz.corvus.business.models.echeances;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class REEnfantADiminuer implements IREEcheances {

    private String csSexeTiers;
    private String dateDecesTiers;
    private String dateNaissanceTiers;
    private SortedSet<IREEnfantEcheances> enfantsDuTiers;
    private boolean hasRenteVeuvePerdure;
    private String idTiers;
    private List<REMotifEcheance> motifs;
    private String nomTiers;
    private String nssTiers;
    private SortedSet<IREPeriodeEcheances> periodes;
    private String prenomTiers;
    private List<IRERelationEcheances> relations;
    private Set<IRERenteEcheances> rentesDuTiers;
    private String communePolitique = "";

    public REEnfantADiminuer() {
        super();

        csSexeTiers = "";
        dateDecesTiers = "";
        dateNaissanceTiers = "";
        enfantsDuTiers = new TreeSet<IREEnfantEcheances>();
        hasRenteVeuvePerdure = false;
        idTiers = "";
        motifs = new ArrayList<REMotifEcheance>();
        nomTiers = "";
        nssTiers = "";
        periodes = new TreeSet<IREPeriodeEcheances>();
        prenomTiers = "";
        relations = new ArrayList<IRERelationEcheances>();
        rentesDuTiers = new HashSet<IRERenteEcheances>();
    }

    @Override
    public int compareTo(IREEcheances uneAutreEcheance) {
        int compareNom = JadeStringUtil.convertSpecialChars(uneAutreEcheance.getNomTiers()).compareTo(
                JadeStringUtil.convertSpecialChars(getNomTiers()));
        if (compareNom != 0) {
            return -1 * compareNom;
        }

        int comparePrenom = JadeStringUtil.convertSpecialChars(uneAutreEcheance.getPrenomTiers()).compareTo(
                JadeStringUtil.convertSpecialChars(getPrenomTiers()));
        if (comparePrenom != 0) {
            return -1 * comparePrenom;
        }

        return getIdTiers().compareTo(uneAutreEcheance.getIdTiers());
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj != null) && this.getClass().getName().equals(obj.getClass().getName())) {
            return compareTo((IREEcheances) obj) == 0;
        }
        return false;
    }

    @Override
    public String getCsSexeTiers() {
        return csSexeTiers;
    }

    @Override
    public String getDateDecesTiers() {
        return dateDecesTiers;
    }

    @Override
    public String getDateNaissanceTiers() {
        return dateNaissanceTiers;
    }

    @Override
    public SortedSet<IREEnfantEcheances> getEnfantsDuTiers() {
        return enfantsDuTiers;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    public List<REMotifEcheance> getMotifs() {
        return motifs;
    }

    @Override
    public String getNomTiers() {
        return nomTiers;
    }

    @Override
    public String getNssTiers() {
        return nssTiers;
    }

    @Override
    public SortedSet<IREPeriodeEcheances> getPeriodes() {
        return periodes;
    }

    @Override
    public String getPrenomTiers() {
        return prenomTiers;
    }

    @Override
    public List<IRERelationEcheances> getRelations() {
        return relations;
    }

    @Override
    public Set<IRERenteEcheances> getRentesDuTiers() {
        return rentesDuTiers;
    }

    @Override
    public boolean hasPrestationBloquee() {
        for (IRERenteEcheances rente : rentesDuTiers) {
            if (rente.isPrestationBloquee()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRenteVeuvePerdure() {
        return hasRenteVeuvePerdure;
    }

    public void setCsSexeTiers(String csSexeTiers) {
        this.csSexeTiers = csSexeTiers;
    }

    public void setDateDecesTiers(String dateDecesTiers) {
        this.dateDecesTiers = dateDecesTiers;
    }

    public void setDateNaissanceTiers(String dateNaissanceTiers) {
        this.dateNaissanceTiers = dateNaissanceTiers;
    }

    public void setEnfantsDuTiers(SortedSet<IREEnfantEcheances> enfantsDuTiers) {
        this.enfantsDuTiers = enfantsDuTiers;
    }

    public void setHasRenteVeuvePerdure(boolean hasRenteVeuvePerdure) {
        this.hasRenteVeuvePerdure = hasRenteVeuvePerdure;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMotifs(List<REMotifEcheance> motifs) {
        this.motifs = motifs;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setPeriodes(SortedSet<IREPeriodeEcheances> periodes) {
        this.periodes = periodes;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setRelations(List<IRERelationEcheances> relations) {
        this.relations = relations;
    }

    public void setRentesDuTiers(Set<IRERenteEcheances> rentesDuTiers) {
        this.rentesDuTiers = rentesDuTiers;
    }

    @Override
    public String getCommunePolitique() {
        return communePolitique;
    }
}
