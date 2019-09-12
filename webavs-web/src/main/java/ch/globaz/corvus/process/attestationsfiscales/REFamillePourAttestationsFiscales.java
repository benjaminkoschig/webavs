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
    private RETiersPourAttestationsFiscales tiersPourCorrespondance;
    private boolean hasRetroactif;
    private boolean hasRetroactifSurPlusieursAnnees;
    private boolean hasRenteConjointAdressePaiementSepare;



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

    public void setTiersPourCorrespondance(RETiersPourAttestationsFiscales tiersPourCorrespondance) {
        this.tiersPourCorrespondance = tiersPourCorrespondance;
    }

    public RETiersPourAttestationsFiscales getTiersPourCorrespondance() {
        return tiersPourCorrespondance;
    }

    /**
     * Return <code>true</code> si un des tiers b�n�ficiaires de la familles est au b�n�fice d'un rente PC dans l'ann�e
     * fiscales
     * 
     * @return <code>true</code> si un des tiers b�n�ficiaires de la familles est au b�n�fice d'un rente PC dans l'ann�e
     */
    public boolean getHasRentePC() {
        for (RETiersPourAttestationsFiscales tiers : tiersBeneficiaires.values()) {
            if (tiers.hasPcEnDecembre()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Si la famille � eu au moins une d�cision r�troactive dans l'ann�e
     * 
     * @param hasRetroactif
     */
    public void setHasRetroactif(boolean hasRetroactif) {
        this.hasRetroactif = hasRetroactif;
    }

    /**
     * Retourne <code>true</code> si la famille � eu au moins une d�cision r�troactive dans l'ann�e
     * 
     * @return
     */
    public boolean getHasRetroactif() {
        return hasRetroactif;
    }

    /**
     * D�finit si la famille poss�de une d�cision dans l'ann�e fiscales avec r�tro sur plusieurs ann�es
     * 
     * @param hasRetroactifSurPlusieursAnnees
     */
    public void setHasRetroactifSurPlusieursAnnees(boolean hasRetroactifSurPlusieursAnnees) {
        this.hasRetroactifSurPlusieursAnnees = hasRetroactifSurPlusieursAnnees;
    }

    /**
     * D�finit si la famille poss�de une d�cision dans l'ann�e fiscales avec r�tro sur plusieurs ann�es
     * 
     * @return <code>true</code> si la famille poss�de une d�cision dans l'ann�e fiscales avec r�tro sur plusieurs
     *         ann�es
     */
    public boolean getHasRetroactifSurPlusieursAnnees() {
        return hasRetroactifSurPlusieursAnnees;
    }

    public boolean hasRenteConjointAdressePaiementSepare() {
        return hasRenteConjointAdressePaiementSepare;
    }

    public void setHasRenteConjointAdressePaiementSepare(boolean hasRenteConjointAdressePaiementSepare) {
        this.hasRenteConjointAdressePaiementSepare = hasRenteConjointAdressePaiementSepare;
    }

}
