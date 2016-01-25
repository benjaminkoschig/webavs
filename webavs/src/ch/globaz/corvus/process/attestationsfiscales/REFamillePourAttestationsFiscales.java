package ch.globaz.corvus.process.attestationsfiscales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class REFamillePourAttestationsFiscales implements Comparable<REFamillePourAttestationsFiscales>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean hasPlusieursAdressePaiement;
    private Map<String, RETiersPourAttestationsFiscales> tiersBeneficiaires;
    private RETiersPourAttestationsFiscales tiersRequerant;

    public REFamillePourAttestationsFiscales() {
        super();

        hasPlusieursAdressePaiement = false;
        tiersRequerant = null;
        tiersBeneficiaires = new HashMap<String, RETiersPourAttestationsFiscales>();
    }

    @Override
    public int compareTo(REFamillePourAttestationsFiscales uneFamille) {
        return tiersRequerant.compareTo(uneFamille.getTiersRequerant());
    }

    Map<String, RETiersPourAttestationsFiscales> getMapTiersBeneficiaire() {
        return tiersBeneficiaires;
    }

    public List<RERentePourAttestationsFiscales> getRentesDeLaFamille() {
        List<RERentePourAttestationsFiscales> rentes = new ArrayList<RERentePourAttestationsFiscales>();

        for (RETiersPourAttestationsFiscales unTiersBeneficiaire : tiersBeneficiaires.values()) {
            rentes.addAll(unTiersBeneficiaire.getRentes());
        }

        Collections.sort(rentes);

        return rentes;
    }

    public Collection<RETiersPourAttestationsFiscales> getTiersBeneficiaires() {
        return tiersBeneficiaires.values();
    }

    public RETiersPourAttestationsFiscales getTiersRequerant() {
        return tiersRequerant;
    }

    public boolean hasPlusieursAdressePaiement() {
        return hasPlusieursAdressePaiement;
    }

    void setHasPlusieursAdressePaiement(boolean hasPlusieursAdressePaiement) {
        this.hasPlusieursAdressePaiement = hasPlusieursAdressePaiement;
    }

    void setTiersRequerant(RETiersPourAttestationsFiscales tiersRequerant) {
        this.tiersRequerant = tiersRequerant;
    }
}
