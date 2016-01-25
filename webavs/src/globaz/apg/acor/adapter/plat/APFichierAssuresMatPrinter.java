package globaz.apg.acor.adapter.plat;

import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierAssuresPrinter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRFilterIterator;
import java.util.Iterator;

/**
 * <p>
 * Sous-classe permettant la création du fichier ASSURRES_AMAT de ACOR.
 * </p>
 * 
 * @author VRE
 */
public class APFichierAssuresMatPrinter extends PRAbstractFichierAssuresPrinter {

    private static final int STATE_ENFANTS = 2;
    private static final int STATE_PERE = 1;
    private static final int STATE_START = 0;

    private int compteEnfant;
    private Iterator enfants = null;
    private int state = APFichierAssuresMatPrinter.STATE_START;

    protected APFichierAssuresMatPrinter(APACORDroitMatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    protected APACORDroitMatAdapter adapter() {
        return (APACORDroitMatAdapter) parent;
    }

    private APDroitMaternite droit() {
        return (APDroitMaternite) adapter().getDroit();
    }

    private Iterator enfants() throws PRACORException {
        if (enfants == null) {
            enfants = new PRFilterIterator(adapter().situationsFamiliales().iterator(),
                    APACORDroitMatAdapter.ENFANTS_PREDICATE);
        }
        return enfants;
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
        switch (state) {
            case STATE_ENFANTS:
                return enfants().hasNext();
            default:
                return true;
        }
    }

    private void printChampsSpecifiquesVides(StringBuffer writer) {
        // 10. genre de formulaire
        this.writeChampVide(writer);

        // 11. date de début du congé maternité
        this.writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 12. date reprise activité lucrative
        this.writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 13. champ vide
        this.writeChampVide(writer);

        // 14. canton d'imposition
        this.writeChaine(writer, PRACORConst.CA_CODE_3_VIDE); // pas de taux
        // d'imposition

        // 15. taux d'imposition
        this.writeEntier(writer, PRACORConst.CA_ENTIER_VIDE); // taux par défaut

        // 16. assurance facultative
        this.writeOuiNon(writer, false); // pas d'assurance facultative

        // 17. limite inférieure payée à l'employeur
        this.writeReel(writer, PRACORConst.CA_REEL_VIDE);

        // 18. montant garanti, correspondant chez nous au droit acquis saisi APRES le calcul des prestations
        this.writeReel(writer, PRACORConst.CA_REEL_VIDE);

        // 19. montant garanti réduit
        this.writeOuiNon(writer, false);

        // 20. assurance versant la prestation qui donne lieu au montant garanti.
        if (!JadeStringUtil.isBlank(adapter().getDroit().getCsProvenanceDroitAcquis())) {
            this.writeEntier(writer, getCodeAssuranceVersantPrestation(adapter().getDroit()
                    .getCsProvenanceDroitAcquis()));
        } else {
            this.writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);
        }

        // 21. numéro de référence
        this.writeChaineSansFinDeChamp(writer, droit().getReference());
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        switch (state) {
            case STATE_START:
                printDebutLigneAssureWithSexeF(writer, adapter().tiers(), droit().getPays(), droit().getNpa());

                // 10. genre de formulaire
                this.writeEntier(writer, PRACORConst.CA_GENRE_CARTE_DEMANDE);

                // 11. date de début du congé maternité
                this.writeDate(writer, droit().getDateDebutDroit());

                // 12. date reprise activité lucrative
                this.writeDate(writer, droit().getDateRepriseActiv());

                // 13. champ vide
                this.writeChampVide(writer);

                // 14. canton d'imposition
                if (droit().getIsSoumisImpotSource().booleanValue()) {
                    try {
                        this.writeChaine(writer, PRTiersHelper.getCodeOFASCanton(getSession(), droit().getNpa()));
                    } catch (Exception e) {
                        throw new PRACORException(getSession().getLabel("ERROR_CHARGEMENT_CANTON_IMPOSITION"));
                    }
                } else {
                    this.writeChaine(writer, PRACORConst.CA_CODE_3_VIDE);
                }

                // 15. taux d'imposition
                // BZ7673 - Acor attend le taux déjà diviser cad 0.1000 pour 10 %
                writeTaux(writer, droit().getTauxImpotSource());

                // 16. assurance facultative
                this.writeOuiNon(writer, false); // pas d'assurance facultative

                // 17. limite inférieure payée à l'employeur, champ vide (ACOR 2.1.15)
                this.writeChampVide(writer);

                // 18. montant garanti, correspondant chez nous au droit acquis
                this.writeReel(writer, adapter().getDroit().getDroitAcquis());

                // 19. montant garanti réduit
                this.writeOuiNon(writer, false);

                // 20. assurance versant la prestation qui donne lieu au montant garanti.
                if (!JadeStringUtil.isBlank(adapter().getDroit().getCsProvenanceDroitAcquis())) {
                    this.writeEntier(writer, getCodeAssuranceVersantPrestation(adapter().getDroit()
                            .getCsProvenanceDroitAcquis()));
                } else {
                    this.writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);
                }

                // 21. numéro de référence
                this.writeChaineSansFinDeChamp(writer, droit().getReference());

                state = APFichierAssuresMatPrinter.STATE_PERE;

                break;

            case STATE_PERE:
                if (adapter().mariTiers() != null) {
                    this.printDebutLigneAssure(writer, adapter().mariTiers());
                } else {
                    this.printDebutLigneAssure(writer, adapter().noAVSPere(), PRACORConst.CA_DATE_VIDE, "", "", false);
                }

                printChampsSpecifiquesVides(writer);
                state = APFichierAssuresMatPrinter.STATE_ENFANTS;

                break;

            case STATE_ENFANTS:

                APSituationFamilialeMat enfant = (APSituationFamilialeMat) enfants().next();
                String noAVS = enfant.getNoAVS();

                if (JadeStringUtil.isIntegerEmpty(noAVS)) {
                    noAVS = adapter().nssBidon("", PRACORConst.CS_HOMME, "" + compteEnfant, false);
                }

                this.printDebutLigneAssure(writer, noAVS, enfant.getDateNaissance(), enfant.getPrenom(),
                        enfant.getNom(), true);
                printChampsSpecifiquesVides(writer);

                ++compteEnfant;

                break;
        }
    }
}
