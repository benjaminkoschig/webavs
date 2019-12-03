package globaz.corvus.acor.adapter.plat;

import globaz.externe.IPRConstantesExternes;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
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
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1>
 *
 * @author vre
 */
public class REFichierEuroFormPrinter extends PRAbstractFichierPlatPrinter {

    private static final String IS_WANT_ADRESSE_COURRIER = "isWantAdresseCourrier";

    private boolean writed = false;

    public REFichierEuroFormPrinter(REACORDemandeAdapter adapter, String fileName) {
        super(adapter, fileName);
    }

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
    }

    @Override
    public void printLigne(StringBuffer buffer) throws PRACORException {
        try {
            if (adapter().isDemandeSurvivant()) {
                String idTiers = rechercherLeTiers(adapter().idTiersAssure());
                writeData(idTiers, buffer);
            } else {
                writeData(adapter().idTiersAssure(), buffer);
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
                adapter().getSession(), ISFSituationFamiliale.CS_DOMAINE_STANDARD, idTiersRequerant);

        // On récupère tous les membres de la famille
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersRequerant);

        return membresFamille;
    }

    private void writeData(String idTiers, StringBuffer buffer) throws Exception {
        try {
            TITiers t = new TITiers();
            t.setIdTiers(idTiers);
            t.setSession(adapter().getSession());

            TIAdresseDataSource adresse;
            String prop = getWantAdresseCourrierProperties();

            if ("true".equals(prop)) {
                adresse = t.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_COURRIER,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);
            } else {
                adresse = t.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);
            }

            // Informations sur du tiers
            PRTiersWrapper tiers = PRTiersHelper.getTiersById(adapter().getSession(), idTiers);
            // Informations sur le domicile
            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(adapter().getSession(), adapter()
                            .getSession().getCurrentThreadTransaction(), idTiers,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            // Writing des données
            StringBuffer sb = writeDataToBuffer(tiers, adresse, adressePaiement);
            buffer.append(sb.toString());
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    private String getWantAdresseCourrierProperties() {
        String prop = null;
        try {
            BSession session = adapter().getSession();
            if ("corvus".equalsIgnoreCase(session.getApplicationId())) {
                prop = session.getApplication().getProperty(IS_WANT_ADRESSE_COURRIER);
            } else {
                prop = null;
            }
        } catch (Exception e) {
            prop = null;
        }
        return prop;
    }

    private StringBuffer writeDataToBuffer(PRTiersWrapper tiers, TIAdresseDataSource adresse,
                                           TIAdressePaiementData adressePaiement) throws Exception {

        StringBuffer data = new StringBuffer();
        // 1. NSS
        if (tiers != null) {
            this.writeChaine(data, tiers.getNSS());
        } else {
            this.writeChampVide(data);
        }
        // Adresse domicile
        if (adresse != null) {
            // 2. Numéro et rue
            StringBuilder nomRue = new StringBuilder();
            nomRue.append(adresse.rue).append(PRACORConst.CA_CHAINE_VIDE);
            nomRue.append(adresse.numeroRue);
            this.writeChaine(data, nomRue.toString().trim());

            // 3. Localite
            this.writeChaine(data, adresse.localiteNom);
            // 4. Code postal
            this.writeChaine(data, adresse.localiteNpa);

            // 5. Récupération du code pays
            String codePays = StringUtils.EMPTY;
            if (!JadeStringUtil.isBlankOrZero(adresse.paysIso)) {
                TIPaysManager paysManager = new TIPaysManager();
                paysManager.setSession(adapter().getSession());
                paysManager.setForCodeIso(adresse.paysIso);
                paysManager.find(BManager.SIZE_NOLIMIT);
                if (!paysManager.getContainer().isEmpty()) {
                    codePays = ((TIPays) paysManager.getContainer().get(0)).getCodeCentrale();
                }
            }
            this.writeChaine(data, codePays);
        }
        // Si pas d'adresse on insère des tabulations pour éviter un décalage dans Acor
        else {
            this.writeChampVide(data);
            this.writeChampVide(data);
            this.writeChampVide(data);
            this.writeChampVide(data);
        }

        // Adresse paiement du tiers
        if (adressePaiement != null) {
            // 6. Nom du tiers
            StringBuilder nomTiers = new StringBuilder();
            nomTiers.append(adressePaiement.getNomTiers1()).append(PRACORConst.CA_CHAINE_VIDE);
            nomTiers.append(adressePaiement.getNomTiers2());
            this.writeChaine(data, nomTiers.toString().trim());

            String idTiersBanque = adressePaiement.getIdTiersBanque();
            TIAbstractAdresseData banque = null;
            if (!JadeStringUtil.isBlankOrZero(idTiersBanque)) {
                banque = TIAdresseResolver.dataSourceAdr(adapter().getSession(), idTiersBanque,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "", JACalendar.todayJJsMMsAAAA(), true);
            }

            // Adresse de la banque
            if (banque != null) {
                // 7. Nom banque
                StringBuilder banqueDesignation = new StringBuilder();
                banqueDesignation.append(banque.getDesignation1_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                banqueDesignation.append(banque.getDesignation2_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                banqueDesignation.append(banque.getDesignation3_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                banqueDesignation.append(banque.getDesignation4_tiers());
                this.writeChaine(data, banqueDesignation.toString().trim());

                // 8. Numéro et rue
                StringBuilder banqueAdresse = new StringBuilder();
                banqueAdresse.append(banque.getRue()).append(PRACORConst.CA_CHAINE_VIDE);
                banqueAdresse.append(banque.getNumero());
                this.writeChaine(data,banqueAdresse.toString().trim());

                // 9. Localité
                this.writeChaine(data,banque.getLocalite());
                // 10. Code postal
                this.writeChaine(data,banque.getNpa());
                // 11. Code pays
                this.writeChaine(data,banque.getIdPays());
            } else {
                this.writeChampVide(data);
                this.writeChampVide(data);
                this.writeChampVide(data);
                this.writeChampVide(data);
                this.writeChampVide(data);
            }

            // 12. swift
            this.writeChaine(data, adressePaiement.getSwift());
            // Apparemment pour récupérer l'IBAN il ne faut pas utiliser la méthode getIban() mais getCompte()... ->
            // trop facile sinon
            // 13. IBAN
            this.writeChaine(data, adressePaiement.getCompte());
        }
        return data;
    }

    @Override
    public void dispose() {
        // WTF ??
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
