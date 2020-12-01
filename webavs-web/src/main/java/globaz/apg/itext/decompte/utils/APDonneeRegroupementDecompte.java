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
 * Cette classe d�finit la cl� de regroupement utilis�e pour le regroupement des diff�rentes prestationsJointRepartition
 * contenues dans le lot
 *
 * @author lga
 */
public class APDonneeRegroupementDecompte {

    /**
     * Ensemble de cl�s qui seront utilis� pour g�rer le regroupement ou pas de certain type de prestations
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
     * Mapping utilis� pour le regroupement des prestations Standard et ACM_NE
     */
    private static Map<APTypeDePrestation, String> regroupementPrestationStandardEtACM_Ne = new HashMap<APTypeDePrestation, String>();

    /**
     * Mapping utilis� pour le regroupement lorssqu'il n'y a pas de regroupement -> chaque type de prestation est sur un
     * d�compte s�par�
     */
    private static Map<APTypeDePrestation, String> regroupementSepare = new HashMap<APTypeDePrestation, String>();

    static {
        /*
         * Chaque type de prestation sera sur un d�compte s�par�
         */
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.STANDARD, Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.COMPCIAB, Regroupement.STANDARD);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.LAMAT, Regroupement.LAMAT);

        // Regroupement des MATCIAB1
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.MATCIAB1, Regroupement.STANDARD);

        // M�me regroupement pour ACM et ACM2
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM_ALFA, Regroupement.ACM_ALFA);
        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM2_ALFA, Regroupement.ACM_ALFA);

        APDonneeRegroupementDecompte.regroupementSepare.put(APTypeDePrestation.ACM_NE, Regroupement.ACM_NE);
        /*
         * Chaque type de prestation sera sur un d�compte s�par� hormis les ACM_NE qui seront regroup�es avec les
         * prestations standard
         */
        // M�me regroupement pour ACM_NE et Standard
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
     *                                       Ne peut pas �tre null
     * @param situationProfessionnelle
     *                                       Peut �tre null
     * @param departement
     *                                       Peut �tre null
     * @param typeDePrestation
     *                                       Ne peut pas �tre null
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
     * Determine le d�partement li� � une situation professionnelle
     *
     * @param isPaiementEmployeur
     * @param situationProfessionnelle
     * @return Le d�partement si existant, sinon <code>null</code>
     * @throws Exception
     *                       En cas de probl�me d'acc�s � la db
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
     * D�termine si le versement doit �tre fait � l'emplyoeur ou � l'assur�
     *
     * @param repartition
     * @return
     * @throws Exception
     */
    private boolean determinerSiPaiementAEmployeur(final APRepartitionJointPrestation repartition,
            final APSituationProfessionnelle situationProfessionnelle) {
        // En 1er lieu on regarde s'il est d�finit comme b�n�ficiaire
        boolean employeur = repartition.isBeneficiaireEmployeur();

        // Si pas d'id situation professionnelle (versement assur�)
        // Toujours utilis� le d�compte assur� ! (sp�cialement pour cas des �tudiants)
        if (situationProfessionnelle == null) {
            employeur = false;
        }
        // Si id situation professionnelle existant (versement employeur) et si c'est un ind�pendant, toujours
        // utilis� le d�compte assur�
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
     * D�finit si le paiement est effectu� � l'employeur
     */
    public void setIsPaiementEmployeur(final boolean isPaiementEmployeur) {
        this.isPaiementEmployeur = isPaiementEmployeur;
    }

    /**
     * @return si le paiement est effectu� � l'employeur
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
