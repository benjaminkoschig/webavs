package globaz.apg.acor.adapter.plat;

import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierAssuresPrinter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;

/**
 * <p>
 * Classe permettant la création du fichier ASSURES_APG pour le logiciel ACOR.
 * </p>
 * 
 * @author VRE
 */
public class APFichierAssuresAPGPrinter extends PRAbstractFichierAssuresPrinter {

    private boolean inscrit = false;

    protected APFichierAssuresAPGPrinter(APAbstractACORDroitAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    protected APAbstractACORDroitAdapter adapter() {
        return (APAbstractACORDroitAdapter) parent;
    }

    private APDroitAPG droit() {
        return (APDroitAPG) adapter().getDroit();
    }

    private String getCodeAssuranceVersantPrestation(String csProvenanceDroitAcquis) {
        if (IAPDroitAPG.CS_IJ_ASSURANCE_INVALIDITE.equals(csProvenanceDroitAcquis)) {
            return "1";
        } else if (IAPDroitAPG.CS_IJ_ASSURANCE_MALADIE_OBLIGATOIRE.equals(csProvenanceDroitAcquis)) {
            return "2";
        } else if (IAPDroitAPG.CS_IJ_ASSURANCE_ACCIDENT_OBLIGATOIRE.equals(csProvenanceDroitAcquis)) {
            return "3";
        } else if (IAPDroitAPG.CS_IJ_ASSURANCE_CHOMAGE.equals(csProvenanceDroitAcquis)) {
            return "4";
        } else if (IAPDroitAPG.CS_IJ_ASSURANCE_MILITAIRE.equals(csProvenanceDroitAcquis)) {
            return "5";
        }
        return "0";
    }

    @Override
    public boolean hasLignes() throws PRACORException {
        return !inscrit;
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        this.printDebutLigneAssure(writer, adapter().tiers(), droit().getPays(), droit().getNpa());

        // 10. genre de carte APG
        if (droit().getDuplicata().booleanValue()) {
            this.writeEntier(writer, PRACORConst.CA_GENRE_CARTE_DUPLICATA);
        } else {
            this.writeEntier(writer, PRACORConst.CA_GENRE_CARTE_DEMANDE);
        }

        // 11. genre de service
        this.writeEntier(writer, PRACORConst.csGenreServiceToAcor(adapter().getSession(), droit().getGenreService()));

        // 12. numéro de compte (ou numéro de référence)
        this.writeChaine(writer, droit().getNoCompte());

        // 13. numéro de contrôle
        this.writeEntier(writer, droit().getNoControlePers());

        // 14. montant total des frais de garde
        this.writeReel(writer, situationFamiliale().getFraisGarde());

        // 15. canton d'imposition
        if (droit().getIsSoumisImpotSource().booleanValue()) {
            try {
                this.writeChaine(writer, PRTiersHelper.getCodeOFASCanton(getSession(), droit().getNpa()));
            } catch (Exception e) {
                throw new PRACORException(getSession().getLabel("ERROR_CHARGEMENT_CANTON_IMPOSITION"));
            }
        } else {
            this.writeChaine(writer, PRACORConst.CA_CODE_3_VIDE);
        }

        // 16. taux d'imposition
        // BZ7673 - Acor attend le taux déjà diviser cad 0.1000 pour 10 %
        writeTaux(writer, droit().getTauxImpotSource());

        // 17. assurance facultative (spécifique à la caisse suisse)
        this.writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);

        // 18. limite inférieure payée à l'employeur, ce champ n'est plus utilisé (ACOR 2.1.15)
        this.writeChampVide(writer);

        // 19. montant garanti, correspondant chez nous au droit acquis
        this.writeReel(writer, adapter().getDroit().getDroitAcquis());

        // 20. montant garanti réduit
        this.writeOuiNon(writer, false);

        // 21. assurance donnant droit au montant garanti (0=aucune)
        if (!JadeStringUtil.isBlank(adapter().getDroit().getCsProvenanceDroitAcquis())) {
            this.writeEntier(writer, getCodeAssuranceVersantPrestation(adapter().getDroit()
                    .getCsProvenanceDroitAcquis()));
        } else {
            this.writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);
        }

        // 22. numéro de référence de la prestation (pas de réf pour les APG chez nous)
        this.writeChampVideSansFinDeChamp(writer);

        inscrit = true;
    }

    private APSituationFamilialeAPG situationFamiliale() throws PRACORException {
        try {
            return droit().loadSituationFamilliale();
        } catch (Exception e) {
            throw new PRACORException(parent.getSession().getLabel("ERREUR_CHARGEMENT_SITUATION_FAMILIALE"), e);
        }
    }
}
