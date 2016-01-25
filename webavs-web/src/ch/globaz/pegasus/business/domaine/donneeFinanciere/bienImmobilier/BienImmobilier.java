package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Dette;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public abstract class BienImmobilier extends DonneeFinanciere implements Dette, Fortune, Revenu {

    protected final Montant interetHypothecaire;
    protected final Montant dette;
    protected final Part part;
    protected final ProprieteType proprieteType;

    public BienImmobilier(Montant interetHypothecaire, Montant dette, Part part, ProprieteType proprieteType,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.part = part;
        this.proprieteType = proprieteType;

        this.interetHypothecaire = interetHypothecaire.addAnnuelPeriodicity();
        this.dette = dette.addAnnuelPeriodicity();
    }

    @Override
    public Montant computeDetteBrut() {
        return dette;
    }

    public Montant computeInteret() {
        return interetHypothecaire.multiply(part);
    }

    @Override
    public Montant computeDette() {
        if (proprieteType.isProprietaire()) {
            return dette.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    public Montant getInteretHypothecaire() {
        return interetHypothecaire;
    }

    public Montant getDette() {
        return dette;
    }

    public Part getPart() {
        return part;
    }

    public ProprieteType getProprieteType() {
        return proprieteType;
    }

}
