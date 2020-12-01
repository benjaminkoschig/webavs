package globaz.apg.itext.decompte.utils;

import java.util.HashMap;
import java.util.Map;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.employeurs.PRDepartement;
import globaz.prestation.tools.PRBlankBNumberFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * Cette classe définit la clé de regroupement utilisée pour le regroupement des différentes prestationsJointRepartition
 * contenues dans le lot
 *
 * @author lga
 */
public class APDonneeRegroupementDecompte {

    /**
     * Ensemble de clés qui seront utilisé pour gérer le regroupement ou pas de certain type de prestations
     *
     * @author lga
     */
    private class Regroupement {
        private static final String STANDARD = "STANDARD";
        private static final String LAMAT = "LAMAT";
        private static final String ACM_ALFA = "ACM_ALFA";
        private static final String ACM_NE = "ACM_NE";
    }

    private String idTiers;
    private String idAffilie;
    private boolean isPaiementEmployeur;
    private PRDepartement departement;
    private APTypeDePrestation typeDePrestation;
    private final APSituationProfessionnelle situationProfessionnelle;
    private boolean isIndependant;
    private boolean isEmployeur;
    private boolean isModuleCompensationPorteEnCompteActif;
    private boolean isPorteEncompte;
    private String idAdressePaiement;

    /**
     * Mapping utilisé pour le regroupement des prestations Standard et ACM_NE
     */
    private static Map<APTypeDePrestation, String> regroupementPrestationStandardEtACM_Ne = new HashMap<APTypeDePrestation, String>();

    /**
     * Mapping utilisé pour le regroupement lorssqu'il n'y a pas de regroupement -> chaque type de prestation est sur un
     * décompte séparé
     */
    private static Map<APTypeDePrestation, String> regroupementSepare = new HashMap<APTypeDePrestation, String>();

    static {
        /*
         * Chaque type de prestation sera sur un décompte séparé
         */
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.STANDARD, Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.COMPCIAB, Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.LAMAT, Regroupement.LAMAT);

        // Regroupement des MATCIAB1
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.MATCIAB1, Regroupement.STANDARD);

        // Même regroupement pour ACM et ACM2
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM_ALFA, Regroupement.ACM_ALFA);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM2_ALFA, Regroupement.ACM_ALFA);

        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM_NE, Regroupement.ACM_NE);
        /*
         * Chaque type de prestation sera sur un décompte séparé hormis les ACM_NE qui seront regroupées avec les
         * prestations standard
         */
        // Même regroupement pour ACM_NE et Standard
        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.STANDARD,
                Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.ACM_NE,
                Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.COMPCIAB,
                Regroupement.STANDARD);

        // Regroupement des MATCIAB1
        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.MATCIAB1, Regroupement.STANDARD);

        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.LAMAT,
                Regroupement.LAMAT);
        APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.put(APTypeDePrestation.ACM_ALFA,
                Regroupement.ACM_ALFA);
    }

    public String genererCleDeRegroupement(final APMethodeRegroupement methodeRegroupement) {
        final StringBuilder key = new StringBuilder();
        key.append(idTiers);
        key.append("-");
        key.append(idAffilie);
        key.append("-");
        if (!isModuleCompensationPorteEnCompteActif) {
            key.append(String.valueOf(isPaiementEmployeur));
            key.append("-");
        }
        key.append(departement == null ? "null" : departement.getDepartement());
        key.append("-");
        if (isModuleCompensationPorteEnCompteActif) {
            key.append(isEmployeur);
            key.append("-");
            key.append(isIndependant);
            key.append("-");
        }

        key.append(idAdressePaiement);
        key.append("-");

        switch (methodeRegroupement) {
            case SEPARE:
                key.append(APDonneeRegroupementDecompte.regroupementSepare.get(typeDePrestation));
                break;

            case STANDARD_ACM_NE:
                key.append(APDonneeRegroupementDecompte.regroupementPrestationStandardEtACM_Ne.get(typeDePrestation));
                break;

            default:
                throw new RuntimeException("Can not generate the regroupement key because the method is empty !");
        }
        key.append("-");
        key.append(isPorteEncompte);
        return key.toString();
    }

    /**
     * Constructeur unique
     *
     * @param repartitionJointPrestation
     *                                       Ne peut pas être null
     * @param situationProfessionnelle
     *                                       Peut être null
     * @param departement
     *                                       Peut être null
     * @param typeDePrestation
     *                                       Ne peut pas être null
     * @throws IllegalArgumentException
     *                                      Si APRepartitionJointPrestation est null
     */
    public APDonneeRegroupementDecompte(final APRepartitionJointPrestation repartitionJointPrestation,
            final APSituationProfessionnelle situationProfessionnelle, final PRDepartement departement,
            final APTypeDePrestation typeDePrestation, boolean isModuleCompensationPorteEnCompteActif, BSession session,
            BITransaction transaction) {

        if (repartitionJointPrestation == null) {
            throw new IllegalArgumentException(
                    "Can not create instance of CleRegroupementDecompte with a null APRepartitionJointPrestation");
        }
        if (typeDePrestation == null) {
            throw new IllegalArgumentException(
                    "Can not create instance of CleRegroupementDecompte with a null APTypeDePrestation");
        }
        idTiers = repartitionJointPrestation.getIdTiers();
        idAffilie = repartitionJointPrestation.getIdAffilie();
        this.typeDePrestation = typeDePrestation;
        this.situationProfessionnelle = situationProfessionnelle;

        isEmployeur = repartitionJointPrestation.getTypePaiement()
                .equals(IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);
        isIndependant = repartitionJointPrestation.getSituatuionPro() != null
                ? repartitionJointPrestation.getSituatuionPro().getIsIndependant()
                : false;

        isPaiementEmployeur = determinerSiPaiementAEmployeur(repartitionJointPrestation, situationProfessionnelle);
        this.departement = determinerDepartement(isPaiementEmployeur, situationProfessionnelle, departement);

        this.isModuleCompensationPorteEnCompteActif = isModuleCompensationPorteEnCompteActif;

        try {
            isPorteEncompte = isSituationProfPorteEnCompte(repartitionJointPrestation.getIdSituationProfessionnelle(),
                    session, transaction);
        } catch (Exception e1) {
            JadeLogger.error(this, e1);
        }
        TIAdressePaiementData adresse;
        try {
            adresse = repartitionJointPrestation.loadAdressePaiement(null);
            if (adresse == null) {
                idAdressePaiement = "";
            } else {
                idAdressePaiement = adresse.getIdAvoirPaiementUnique();
            }
        } catch (Exception e) {
            idAdressePaiement = "";
            JadeLogger.error(this, e);
        }

    }

    private boolean isSituationProfPorteEnCompte(String idSituationProfessionnelle, BSession session,
            BITransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(idSituationProfessionnelle)) {
            APSituationProfessionnelle situationPro = new APSituationProfessionnelle();
            situationPro.setId(idSituationProfessionnelle);
            situationPro.setSession(session);
            situationPro.retrieve(transaction);
            if (!situationPro.isNew()) {
                return situationPro.getIsPorteEnCompte();
            }
        }
        return false;
    }

    //
    /**
     * Determine le département lié à une situation professionnelle
     *
     * @param isPaiementEmployeur
     * @param situationProfessionnelle
     * @return Le département si existant, sinon <code>null</code>
     * @throws Exception
     *                       En cas de problème d'accès à la db
     */
    private PRDepartement determinerDepartement(final boolean isPaiementEmployeur,
            final APSituationProfessionnelle situationProfessionnelle, final PRDepartement departement) {
        PRDepartement resolvedDepartement = null;
        if (isPaiementEmployeur) {
            if (situationProfessionnelle != null) {
                resolvedDepartement = departement;
            }
        }
        return resolvedDepartement;
    }

    /**
     * Détermine si le versement doit être fait à l'emplyoeur ou à l'assuré
     *
     * @param repartition
     * @return
     * @throws Exception
     */
    private boolean determinerSiPaiementAEmployeur(final APRepartitionJointPrestation repartition,
            final APSituationProfessionnelle situationProfessionnelle) {
        // En 1er lieu on regarde s'il est définit comme bénéficiaire
        boolean employeur = repartition.isBeneficiaireEmployeur();

        // Si pas d'id situation professionnelle (versement assuré)
        // Toujours utilisé le décompte assuré ! (spécialement pour cas des étudiants)
        if (situationProfessionnelle == null) {
            employeur = false;
        }
        // Si id situation professionnelle existant (versement employeur) et si c'est un indépendant, toujours
        // utilisé le décompte assuré
        else if ((situationProfessionnelle.getIsIndependant() != null)
                && situationProfessionnelle.getIsIndependant().booleanValue()) {
            employeur = false;
        }
        return employeur;
    }

    /**
     * @return the idTiers
     */
    public final String getIdTiers() {
        return idTiers;
    }

    /**
     * @param idTiers
     *                    the idTiers to set
     */
    public final void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the idAffilie
     */
    public final String getIdAffilie() {
        return idAffilie;
    }

    /**
     * @param idAffilie
     *                      the idAffilie to set
     */
    public final void setIdAffilie(final String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * Définit si le paiement est effectué à l'employeur
     */
    public void setIsPaiementEmployeur(final boolean isPaiementEmployeur) {
        this.isPaiementEmployeur = isPaiementEmployeur;
    }

    /**
     * @return si le paiement est effectué à l'employeur
     */
    public boolean getIsPaiementEmployeur() {
        return isPaiementEmployeur;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * @return the departement
     */
    public final PRDepartement getDepartement() {
        return departement;
    }

    /**
     * @param departement
     *                        the departement to set
     */
    public final void setDepartement(final PRDepartement departement) {
        this.departement = departement;
    }

    /**
     * @return the typeDePrestation
     */
    public final APTypeDePrestation getTypeDePrestation() {
        return typeDePrestation;
    }

    /**
     * @param typeDePrestation
     *                             the typeDePrestation to set
     */
    public final void setTypeDePrestation(final APTypeDePrestation typeDePrestation) {
        this.typeDePrestation = typeDePrestation;
    }

    public String genererCleDeTriDesPrestations(final APMethodeRegroupement methodeRegroupement) {
        final String noAffilieFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
        return noAffilieFormatte + "-";
    }

    public APSituationProfessionnelle getSituationProfessionnelle() {
        return situationProfessionnelle;
    }

    public Boolean isIndependant(){
        return isIndependant;
    }

}
