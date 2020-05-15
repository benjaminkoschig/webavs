package globaz.apg.itext.decompte.utils;

import globaz.apg.db.droits.APEmployeur;
import globaz.pyxis.db.tiers.TITiers;

public class APEmployeurTiersUtil {

    private TITiers tiers;
    private APEmployeur employeur;

    public APEmployeurTiersUtil(){
        tiers = new TITiers();
        employeur = new APEmployeur();
    }

    public APEmployeurTiersUtil(TITiers tiers, APEmployeur employeur) {
        this.tiers = tiers;
        this.employeur = employeur;
    }

    public TITiers getTiers() {
        return tiers;
    }

    public void setTiers(TITiers tiers) {
        this.tiers = tiers;
    }

    public APEmployeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(APEmployeur employeur) {
        this.employeur = employeur;
    }
}
