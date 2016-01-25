/**
 * 
 */
package globaz.lynx.service.tiers;

import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.ITIAdresseDefTable;
import globaz.pyxis.db.adressecourrier.ITIAvoirAdresseDefTable;
import globaz.pyxis.db.adressecourrier.ITILocaliteDefTable;
import globaz.pyxis.db.adressecourrier.ITIPaysDefTable;
import globaz.pyxis.db.adressepaiement.ITIAdressePaiementDefTable;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import globaz.pyxis.db.tiers.ITIBanqueDefTable;
import globaz.pyxis.db.tiers.ITIHistoriqueAffilieDefTable;
import globaz.pyxis.db.tiers.ITIHistoriqueAvsDefTable;
import globaz.pyxis.db.tiers.ITIHistoriqueContribuableDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author sel
 * 
 */
public class LXAdressePaiementDataManager extends TIAdressePaiementDataManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BANQUE).append(".").append(ITITiersDefTable.ID_TIERS);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BANQUE_ID_TIERS);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BANQUE).append(".").append(ITITiersDefTable.DESIGNATION_1);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BANQUE_DESIGNATION_1);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BANQUE).append(".").append(ITITiersDefTable.DESIGNATION_2);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BANQUE_DESIGNATION_2);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.CLEARING);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_BANQUE_CLEARING);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.NEW_CLEARING);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_BANQUE_NEW_CLEARING);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.CODE_SWIFT);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_BANQUE_CODE_SWIFT);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.IBAN);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_BANQUE_IBAN);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.NUM_CPP_BANQUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_BANQUE_NUM_CPP_BANQUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE).append(".").append(ITILocaliteDefTable.NOM_LOCALITE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE_NOM_LOCALITE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE).append(".")
                .append(ITILocaliteDefTable.NUMERO_POSTAL);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE_NUMERO_POSTAL);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_PAYS_BANQUE).append(".").append(ITIPaysDefTable.ID_PAYS);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_PAYS_BANQUE_ID_PAYS);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_PAYS_BANQUE).append(".").append(ITIPaysDefTable.CODE_ISO);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_PAYS_BANQUE_CODE_ISO);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".").append(ITIAdresseDefTable.RUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_RUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".").append(ITIAdresseDefTable.NUMERO_RUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_NUMERO_RUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".").append(ITIAdresseDefTable.CASE_POSTALE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_CASE_POSTALE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".")
                .append(ITIAdresseDefTable.LIGNE_ADRESSE_1);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_LIGNE_ADRESSE_1);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".")
                .append(ITIAdresseDefTable.LIGNE_ADRESSE_2);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_LIGNE_ADRESSE_2);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".")
                .append(ITIAdresseDefTable.LIGNE_ADRESSE_3);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_LIGNE_ADRESSE_3);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".")
                .append(ITIAdresseDefTable.LIGNE_ADRESSE_4);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE_LIGNE_ADRESSE_4);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.NUMERO_COMPTE_BANCAIRE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT_NUMERO_COMPTE_BANCAIRE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.CS_CODE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT_CS_CODE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.NUMERO_CCP);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT_NUMERO_CCP);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.ID_PAYS);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT_ID_PAYS);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.ID_ADRESSE_PAIEMENT_UNIQUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT_ID_ADRESSE_PAIEMENT_UNIQUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.CS_TITRE_TIERS);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_CS_TITRE_TIERS);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_1);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_DESIGNATION_1);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_2);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_DESIGNATION_2);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_3);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_DESIGNATION_3);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_4);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_DESIGNATION_4);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.CS_LANGUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE_CS_LANGUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.ID_ADRESSE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_ID_ADRESSE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.ATTENTION);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_ATTENTION);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".")
                .append(ITIAdresseDefTable.CS_TITRE_ADRESSE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_CS_TITRE_ADRESSE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.LIGNE_ADRESSE_1);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_LIGNE_ADRESSE_1);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.LIGNE_ADRESSE_2);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_LIGNE_ADRESSE_2);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.LIGNE_ADRESSE_3);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_LIGNE_ADRESSE_3);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.LIGNE_ADRESSE_4);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_LIGNE_ADRESSE_4);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.RUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_RUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.NUMERO_RUE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_NUMERO_RUE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.CASE_POSTALE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS_CASE_POSTALE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS).append(".").append(ITILocaliteDefTable.ID_LOCALITE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS_ID_LOCALITE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS).append(".").append(ITILocaliteDefTable.NUMERO_POSTAL);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS_NUMERO_POSTAL);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS).append(".").append(ITILocaliteDefTable.NOM_LOCALITE);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS_NOM_LOCALITE);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_PAYS_TIERS).append(".").append(ITIPaysDefTable.ID_PAYS);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_PAYS_TIERS_ID_PAYS);
        fields.append(",");

        fields.append(TIAdressePaiementData.ALIAS_PAYS_TIERS).append(".").append(ITIPaysDefTable.CODE_ISO);
        fields.append(" ").append(TIAdressePaiementData.ALIAS_PAYS_TIERS_CODE_ISO);
        // fields.append(",");

        // fields.append(TIAdressePaiementData.ALIAS_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_1);
        // fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_DESIGNATION_1);
        // fields.append(",");
        //
        // fields.append(TIAdressePaiementData.ALIAS_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_2);
        // fields.append(" ").append(TIAdressePaiementData.ALIAS_TIERS_DESIGNATION_2);
        // fields.append(",");

        // fields.append(TIAdressePaiementData.ALIAS_PERSONNE_AVS).append(".")
        // .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        // fields.append(" ").append(TIAdressePaiementData.ALIAS_PERSONNE_AVS_NUMERO_AVS_ACTUEL);

        if (!JadeStringUtil.isBlank(getForNumAvsBeneficiaireLike())) {
            fields.append(",");
            fields.append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_AVS).append(".")
                    .append(ITIHistoriqueAvsDefTable.NUMERO_AVS).append(" ")
                    .append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_AVS_NUMERO_AVS);
        }

        if (!JadeStringUtil.isBlank(getForNumContribuableBeneficiaireLike())) {
            fields.append(",");
            fields.append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_CONTRIBUABLE).append(".")
                    .append(ITIHistoriqueContribuableDefTable.NUMERO_CONTRIBUABLE).append(" ")
                    .append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_CONTRIBUABLE_NUMERO_CONTRIBUABLE);
        }

        if (!JadeStringUtil.isBlank(getForNumAffilieBeneficiaireLike())) {
            fields.append(",");
            fields.append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_AFFILIE).append(".")
                    .append(ITIHistoriqueAffilieDefTable.NUMERO_AFFILIE).append(" ")
                    .append(TIAdressePaiementDataManager.ALIAS_HISTORIQUE_AFFILIE_NUMERO_AFFILIE);
        }

        return fields.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String today = JACalendar.today().toStrAMJ();

        String adresse = _getCollection() + ITIAdresseDefTable.TABLE_NAME;
        String adressePaiement = _getCollection() + ITIAdressePaiementDefTable.TABLE_NAME;
        String avoirAdresse = _getCollection() + ITIAvoirAdresseDefTable.TABLE_NAME;
        // String avoirPaiement = this._getCollection() + ITIAvoirPaiementDefTable.TABLE_NAME;
        String banque = _getCollection() + ITIBanqueDefTable.TABLE_NAME;
        String localite = _getCollection() + ITILocaliteDefTable.TABLE_NAME;
        String pays = _getCollection() + ITIPaysDefTable.TABLE_NAME;
        // String personneAvs = this._getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder from = new StringBuilder();

        // from.append(avoirPaiement).append(" ").append(TIAdressePaiementData.ALIAS_AVOIR_PAIEMENT);

        // from.append(" INNER JOIN ");
        // from.append(tiers).append(" ").append(TIAdressePaiementData.ALIAS_TIERS);
        // from.append(" ON (");
        // from.append(TIAdressePaiementData.ALIAS_AVOIR_PAIEMENT).append(".").append(ITIAvoirPaiementDefTable.ID_TIERS);
        // from.append("=");
        // from.append(TIAdressePaiementData.ALIAS_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);
        // from.append(")");

        // from.append(" LEFT OUTER JOIN ");
        // from.append(personneAvs).append(" ").append(TIAdressePaiementData.ALIAS_PERSONNE_AVS);
        // from.append(" ON (");
        // from.append(TIAdressePaiementData.ALIAS_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);
        // from.append("=");
        // from.append(TIAdressePaiementData.ALIAS_PERSONNE_AVS).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);
        // from.append(")");

        // from.append(" INNER JOIN ");
        from.append(adressePaiement).append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT);
        // from.append(" ON (");
        // from.append(TIAdressePaiementData.ALIAS_AVOIR_PAIEMENT).append(".")
        // .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
        // from.append("=");
        // from.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
        // .append(ITIAdressePaiementDefTable.ID_ADRESSE_PAIEMENT);
        // from.append(")");

        from.append(" INNER JOIN ");
        from.append(adresse).append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.ID_ADRESSE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.ID_ADRESSE);
        from.append(")");

        from.append(" LEFT OUTER JOIN ");
        from.append(tiers).append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.ID_TIERS_ADRESSE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.ID_TIERS);
        from.append(")");

        from.append(" INNER JOIN ");
        from.append(localite).append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_TIERS).append(".").append(ITIAdresseDefTable.ID_LOCALITE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS).append(".").append(ITILocaliteDefTable.ID_LOCALITE);
        from.append(")");

        from.append(" INNER JOIN ");
        from.append(pays).append(" ").append(TIAdressePaiementData.ALIAS_PAYS_TIERS);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_PAYS_TIERS).append(".").append(ITIPaysDefTable.ID_PAYS);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_LOCALITE_TIERS).append(".").append(ITILocaliteDefTable.ID_PAYS);
        from.append(")");

        from.append(" LEFT OUTER JOIN ");
        from.append(tiers).append(" ").append(TIAdressePaiementData.ALIAS_TIERS_BANQUE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_PAIEMENT).append(".")
                .append(ITIAdressePaiementDefTable.ID_TIERS);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_TIERS_BANQUE).append(".").append(ITITiersDefTable.ID_TIERS);
        from.append(")");

        from.append(" LEFT OUTER JOIN ");
        from.append(banque).append(" ").append(TIAdressePaiementData.ALIAS_BANQUE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.ID_BANQUE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_TIERS_BANQUE).append(".").append(ITITiersDefTable.ID_TIERS);
        from.append(")");

        // pour avoir la bonne adresse de la banque, il faut tenir compte du type d'adresse(DOMICILE), de l'application
        // (DEFAUT)
        // et des dates de debut et fin de relation
        from.append(" LEFT OUTER JOIN ");
        from.append(avoirAdresse).append(" ").append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE);

        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_BANQUE).append(".").append(ITIBanqueDefTable.ID_BANQUE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.ID_TIERS);

        from.append(" AND ");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.CS_TYPE_ADRESSE);
        from.append("=");
        from.append(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);

        from.append(" AND ");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.ID_APPLICATION);
        from.append("=");
        from.append(IConstantes.CS_APPLICATION_DEFAUT);

        from.append(" AND ");
        from.append("((");

        from.append(today);
        from.append(" BETWEEN ");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.DATE_DEBUT);
        from.append(" AND ");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.DATE_FIN);

        from.append(" OR (");

        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.DATE_FIN).append("=").append("0");

        from.append(" AND ");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.DATE_DEBUT).append("<=").append(today);

        from.append("))))");

        from.append(" LEFT OUTER JOIN ");
        from.append(adresse).append(" ").append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".").append(ITIAdresseDefTable.ID_ADRESSE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_AVOIR_ADRESSE_BANQUE).append(".")
                .append(ITIAvoirAdresseDefTable.ID_ADRESSE);
        from.append(")");

        from.append(" LEFT OUTER JOIN ");
        from.append(localite).append(" ").append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE).append(".").append(ITILocaliteDefTable.ID_LOCALITE);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_ADRESSE_BANQUE).append(".").append(ITIAdresseDefTable.ID_LOCALITE);
        from.append(")");

        from.append(" LEFT OUTER JOIN ");
        from.append(pays).append(" ").append(TIAdressePaiementData.ALIAS_PAYS_BANQUE);
        from.append(" ON (");
        from.append(TIAdressePaiementData.ALIAS_PAYS_BANQUE).append(".").append(ITIPaysDefTable.ID_PAYS);
        from.append("=");
        from.append(TIAdressePaiementData.ALIAS_LOCALITE_BANQUE).append(".").append(ITILocaliteDefTable.ID_PAYS);
        from.append(")");

        return from.toString();
    }

}
