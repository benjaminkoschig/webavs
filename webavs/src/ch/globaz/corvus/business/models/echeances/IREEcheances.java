package ch.globaz.corvus.business.models.echeances;

import globaz.corvus.db.echeances.REEcheancesManager;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface IREEcheances extends Comparable<IREEcheances> {

    public String getCsSexeTiers();

    public String getDateDecesTiers();

    public String getDateNaissanceTiers();

    public SortedSet<IREEnfantEcheances> getEnfantsDuTiers();

    public String getIdTiers();

    public String getNomTiers();

    public String getNssTiers();

    public SortedSet<IREPeriodeEcheances> getPeriodes();

    public String getPrenomTiers();

    public List<IRERelationEcheances> getRelations();

    public Set<IRERenteEcheances> getRentesDuTiers();

    public boolean hasPrestationBloquee();

    /**
     * Vrai si le tiers a une rente vieillesse terminée avec comme type d'information complémentaire
     * "Rente de veuve perdure".<br/>
     * La recherche de cette information se fait dans {@link REEcheancesManager#_afterFind()}
     * 
     * @return
     */
    public boolean hasRenteVeuvePerdure();

    public String getCommunePolitique();

}