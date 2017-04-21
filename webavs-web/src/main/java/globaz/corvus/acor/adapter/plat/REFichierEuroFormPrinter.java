package globaz.corvus.acor.adapter.plat;

import globaz.externe.IPRConstantesExternes;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.util.TIAdresseResolver;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.hera.business.constantes.ISFMembreFamille;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class REFichierEuroFormPrinter implements PRFichierACORPrinter {

    private static final String FIELD_DEPARATOR = "\t";

    private REACORDemandeAdapter adapter;
    private String fileName;
    private boolean writed = false;

    public REFichierEuroFormPrinter(REACORDemandeAdapter adapter, String fileName) {
        this.adapter = adapter;
        this.fileName = fileName;
    }

    @Override
    public void printLigne(StringBuffer buffer) throws PRACORException {
        try {
            if (adapter.isDemandeSurvivant()) {
                String idTiers = rechercherLeTiers(adapter.idTiersAssure());
                writeData(idTiers, buffer);
            } else {
                writeData(adapter.idTiersAssure(), buffer);
            }
        } catch (Exception e) {
            throw new PRACORException(e.toString(), e);
        }
        writed = true;
    }

    private String rechercherLeTiers(String idTiersAssure) throws Exception {
        List<ISFMembreFamilleRequerant> membresFamille = Arrays.asList(getToutesLesMembresFamilles(idTiersAssure));
        // On trie car on veut prendre en premier le conjoint et si ce n'est pas possible on prend l'enfant le plus
        // jeune.
        Collections.sort(membresFamille, new Comparator<ISFMembreFamilleRequerant>() {
            @Override
            public int compare(ISFMembreFamilleRequerant o1, ISFMembreFamilleRequerant o2) {
                if (o1.getRelationAuRequerant().equals(o2.getRelationAuRequerant())) {
                    if (!JadeStringUtil.isBlankOrZero(o1.getDateNaissance())
                            && !JadeStringUtil.isBlankOrZero(o2.getDateNaissance())) {
                        Date dateNaisse1 = new Date(o1.getDateNaissance());
                        Date dateNaisse2 = new Date(o2.getDateNaissance());
                        return dateNaisse2.compareTo(dateNaisse1);
                    }
                }
                return o2.getRelationAuRequerant().compareTo(o1.getRelationAuRequerant());
            }
        });

        for (ISFMembreFamilleRequerant tiers : membresFamille) {
            // On ne veut pas traiter le tiers requérant et les membres de famille qui n'ont pas d'idTiers
            if (!idTiersAssure.equals(tiers.getIdTiers()) && !JadeStringUtil.isBlankOrZero(tiers.getIdTiers())) {
                if (ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(tiers.getRelationAuRequerant())
                        || ISFMembreFamille.CS_TYPE_RELATION_ENFANT.equals(tiers.getRelationAuRequerant())) {
                    return tiers.getIdTiers();
                }
            }
        }

        throw new Exception("Impossible de retrouver l'idTiers du conjoint pour l'idTiersAssuré [" + idTiersAssure
                + "]");

    }

    /**
     * Recherche les membres de la famille du tiers requérant
     * 
     * @param idTiersRequerant
     * @return
     * @throws Exception
     */
    protected ISFMembreFamilleRequerant[] getToutesLesMembresFamilles(String idTiersRequerant) throws Exception {
        // On recherche la sit famille du tiers requérant
        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(
                adapter.getSession(), ISFSituationFamiliale.CS_DOMAINE_STANDARD, idTiersRequerant);

        // On récupère tous les membres de la famille
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersRequerant);

        return membresFamille;
    }

    private void writeData(String idTiers, StringBuffer buffer) throws Exception {
        try {
            TITiers t = new TITiers();
            t.setIdTiers(idTiers);
            t.setSession(adapter.getSession());

            TIAdresseDataSource adresse = t.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);

            // Informations sur du tiers
            PRTiersWrapper tiers = PRTiersHelper.getTiersById(adapter.getSession(), idTiers);
            // Informations sur le domicile
            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(adapter.getSession(), adapter
                    .getSession().getCurrentThreadTransaction(), idTiers,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            // Writing des données
            StringBuilder sb = writeDataToBuffer(tiers, adresse, adressePaiement);
            buffer.append(sb.toString());
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    private StringBuilder writeDataToBuffer(PRTiersWrapper tiers, TIAdresseDataSource adresse,
            TIAdressePaiementData adressePaiement) throws Exception {

        StringBuilder data = new StringBuilder();
        // NSS
        if (tiers != null) {
            data.append(putValue(tiers.getNSS()));
        }
        // Adresse domicile
        if (adresse != null) {
            String rueEtNumero = manageValue(adresse.rue);
            rueEtNumero += " ";
            rueEtNumero += manageValue(adresse.numeroRue);

            data.append(putValue(rueEtNumero));
            data.append(putValue(adresse.localiteNom));
            data.append(putValue(adresse.localiteNpa));

            // Récupération du code pays
            String codePays = "";
            if (!JadeStringUtil.isBlankOrZero(adresse.paysIso)) {
                TIPaysManager paysManager = new TIPaysManager();
                paysManager.setSession(adapter.getSession());
                paysManager.setForCodeIso(adresse.paysIso);
                paysManager.find(BManager.SIZE_NOLIMIT);
                if (!paysManager.getContainer().isEmpty()) {
                    codePays = ((TIPays) paysManager.getContainer().get(0)).getCodeCentrale();
                }
            }

            data.append(putValue(codePays));
        }

        // Adresse paiement du tiers
        if (adressePaiement != null) {
            String nomPrenom = manageValue(adressePaiement.getNomTiers1());
            nomPrenom += " ";
            nomPrenom += manageValue(adressePaiement.getNomTiers2());
            nomPrenom = nomPrenom.trim();

            data.append(putValue(nomPrenom));

            String idTiersBanque = adressePaiement.getIdTiersBanque();
            TIAbstractAdresseData banque = null;
            if (!JadeStringUtil.isBlankOrZero(idTiersBanque)) {
                banque = TIAdresseResolver.dataSourceAdr(adapter.getSession(), idTiersBanque,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "", JACalendar.todayJJsMMsAAAA(), true);
            }

            // Adresse de la banque
            if (banque != null) {
                String nomBanque = manageValue(banque.getDesignation1_tiers());
                nomBanque += " ";
                nomBanque += manageValue(banque.getDesignation2_tiers());
                nomBanque += " ";
                nomBanque += manageValue(banque.getDesignation3_tiers());
                nomBanque += " ";
                nomBanque += manageValue(banque.getDesignation4_tiers());
                nomBanque += " ";
                nomBanque = nomBanque.trim();

                data.append(putValue(nomBanque));

                String rueEtNumero = manageValue(banque.getRue());
                rueEtNumero += " ";
                rueEtNumero += manageValue(banque.getNumero());
                rueEtNumero = rueEtNumero.trim();

                data.append(putValue(rueEtNumero));
                data.append(putValue(banque.getLocalite()));
                data.append(putValue(banque.getNpa()));
                data.append(putValue(banque.getIdPays()));
            } else {
                data.append(FIELD_DEPARATOR);
                data.append(FIELD_DEPARATOR);
                data.append(FIELD_DEPARATOR);
                data.append(FIELD_DEPARATOR);
                data.append(FIELD_DEPARATOR);
            }

            data.append(putValue(adressePaiement.getSwift()));
            // Apparemment pour récupérer l'IBAN il ne faut pas utiliser la méthode getIban() mais getCompte()... ->
            // trop facile sinon
            data.append(putValue(adressePaiement.getCompte()));
        }
        return data;
    }

    /**
     * Gère le cas ou la valeur serait null ou une chaîne vide ET ajoute le caractère de séparation des champs
     * 
     * @param value
     * @return @return la chaîne de caractère si non null, sinon une chaîne vide avec le caractère de séparation de
     *         champs
     */
    private String putValue(String value) {
        return manageValue(value) + FIELD_DEPARATOR;
    }

    /**
     * Gère le cas ou la valeur serait null ou une chaîne vide
     * 
     * @param value La valeur à gérer
     * @return la chaîne de caractère si non null, sinon une chaîne vide
     */
    private String manageValue(String value) {
        return JadeStringUtil.isBlank(value) ? "" : value;
    }

    @Override
    public void dispose() {
        // WTF ??
    }

    @Override
    public String getNomFichier() {
        return fileName;
    }

    @Override
    public boolean hasLignes() throws PRACORException {
        return !writed;
    }

    @Override
    public boolean isForcerFichierVide() {
        return false;
    }
}
