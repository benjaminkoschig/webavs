package globaz.prestation.acor.acor2020.mapper;

import acor.rentes.xsd.common.AdresseType;
import acor.rentes.xsd.common.BanqueAdresseType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DonneesPostalesType;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.util.TIAdresseResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class PRAcorMapper {
    protected static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

    private final Map<String, String> idNoAVSBidons = new HashMap<>();
    private final Map<String, String> idNSSBidons = new HashMap<>();

    @Getter
    private final String typeAdressePourRequerant;
    @Getter
    private final PRTiersWrapper tiersRequerant;
    @Getter
    private final String domaineAdresse;
    @Getter
    private BSession session;

    public PRAcorMapper(String typeAdressePourRequerant,
                        PRTiersWrapper tiersRequerant,
                        String domaineAdresse,
                        BSession session) {

        this.typeAdressePourRequerant = typeAdressePourRequerant;
        this.tiersRequerant = tiersRequerant;
        this.domaineAdresse = domaineAdresse;
        this.session = session;
    }

    public Integer getDomicile(String csCantonDomicile, String codePays, PRTiersWrapper tiersRequerant) {
        /*
         * 1) On prend le canton
         * 2) Sinon le pays
         * 3) Sinon prendre le pays du requérant
         */
        if (!JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {
            // Le canton peut désigner 'Etranger', dans ce cas il faut reprendre le pays
            if (PRACORConst.CODE_CANTON_ETRANGER.equals(csCantonDomicile)) {
                if (JadeStringUtil.isIntegerEmpty(codePays)) {
                    // On retourne un code 999 si le canton et pays sont inconnus
                    return PRConverterUtils.formatRequiredInteger(PRACORConst.CANTON_ET_PAYS_INCONNU);
                } else {
                    return PRConverterUtils.formatRequiredInteger(codePays);
                }
            } else {
                return PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(csCantonDomicile));
            }
        } else {
            if (!JadeStringUtil.isIntegerEmpty(codePays)) {
                return PRConverterUtils.formatRequiredInteger(codePays);
            } else {
                return PRConverterUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(tiersRequerant.getCanton()));
            }
        }
    }

    public Integer getCodePays(String csNationalite) {
        if (!JadeStringUtil.isIntegerEmpty(csNationalite)) {
            return PRConverterUtils.formatRequiredInteger(PRACORConst.csEtatToAcor(csNationalite));
        } else {
            return PRConverterUtils.formatRequiredInteger(PRACORConst.CA_ORIGINE_INCONNU);
        }
    }

    public long getNssMembre(ISFMembreFamilleRequerant membre) {
        String nss = membre.getNss();
        if (JadeStringUtil.isBlank(membre.getNss()) || JadeStringUtil.isIntegerEmpty(membre.getNss())) {
            nss = nssBidon(membre.getNss(), membre.getCsSexe(), membre.getNom() + membre.getPrenom(), !membre
                    .getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT));
        }
        return PRConverterUtils.formatNssToLong(nss);
    }

    public String nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        try {
            String nssRequerant = NSUtil.unFormatAVS(tiersRequerant.getNSS());

            // NNSS
            if (nssRequerant.length() > 11) {
                return nssBidon(nss, nomPrenom, conjoint);
            }
            // NAVS
            else {
                return noAVSBidon(nss, csSexe, nomPrenom, conjoint);
            }

        } catch (Exception e) {
            return nssBidon(nss, nomPrenom, conjoint);
        }
    }

    private String noAVSBidon(String noAVS, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(noAVS)) {
            return noAVS;
        }

        String retValue;

        if (JadeStringUtil.isIntegerEmpty(csSexe)) {
            /*
             * le sexe n'est pas stocke dans la situation familliale, par defaut on va prendre le sexe oppose au
             * requerant, de cette manière, les eventuelles relations de conjoint seront acceptees par ACOR
             */
            retValue = PRACORConst.CS_FEMME.equals(tiersRequerant.getSexe()) ? PRACORConst.CA_NO_AVS_BIDON_HOMME
                    : PRACORConst.CA_NO_AVS_BIDON_FEMME;
        } else {
            // le sexe est defini dans la situation familiale.
            retValue = PRACORConst.CS_FEMME.equals(csSexe) ? PRACORConst.CA_NO_AVS_BIDON_FEMME
                    : PRACORConst.CA_NO_AVS_BIDON_HOMME;
        }

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */
        String idNoAVSBidon = conjoint + "_" + nomPrenom;
        String noUnique = idNoAVSBidons.get(idNoAVSBidon);

        if (noUnique == null) {
            noUnique = String.valueOf(idNoAVSBidons.size() + 1);
            idNoAVSBidons.put(idNoAVSBidon, noUnique);
        }

        return noUnique + retValue.substring(noUnique.length());
    }


    private String nssBidon(String nss, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        String idNssBidon = conjoint + "_" + nomPrenom;

        // Prendre un nss de la liste des 25 et voir s'il existe déjà dans la map (itérer),
        // s'il existe, prendre un autre et retest, s'il existe pas, le retourner et l'insérer.

        boolean isOK = false;
        boolean isEqual = false;
        String nss13 = "";
        int increment = 0;

        while (!isOK) {

            nss13 = PRNSS13ChiffresUtils.getNSSErrone(increment);

            Set keys = idNSSBidons.keySet();

            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                String nssKey = idNSSBidons.get(key);

                if (nssKey.equals(nss13)) {
                    isEqual = true;
                    break;
                }

            }

            if (!isEqual) {
                isOK = true;
                idNoAVSBidons.put(idNssBidon, nss13);
            } else {
                increment++;
                isEqual = false;
            }

        }

        // le sexe est defini dans la situation familiale.
        // String nss13 = PRNSS13ChiffresUtils.getRandomNSS(getSession());

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */

        String noUnique = idNSSBidons.get(idNssBidon);

        if (noUnique == null) {
            noUnique = nss13;
            idNSSBidons.put(idNssBidon, noUnique);
        }
        return noUnique;
    }

    public DonneesPostalesType createDonneesPostales() {
        DonneesPostalesType donneesPostalesType = new DonneesPostalesType();

        try {
            TIAdresseDataSource adresse = loadAdresse(this.typeAdressePourRequerant, this.domaineAdresse, tiersRequerant.getIdTiers());
            if (adresse != null) {
                AdresseType adresseType = new AdresseType();
                // 2. Numéro et rue
                adresseType.setAdresse(adresse.rue + " " + adresse.numeroRue);
                // 3. Localite
                adresseType.setLocalite(adresse.localiteNom);
                // 4. Code postal
                adresseType.setCodePostal(adresse.localiteNpa);
                // 5. Récupération du code pays
                String codePays = "0";
                if (!JadeStringUtil.isBlankOrZero(adresse.paysIso)) {
                    TIPaysManager paysManager = new TIPaysManager();
                    paysManager.setSession(session);
                    paysManager.setForCodeIso(adresse.paysIso);
                    paysManager.find(BManager.SIZE_NOLIMIT);
                    if (!paysManager.getContainer().isEmpty()) {
                        codePays = ((TIPays) paysManager.getContainer().get(0)).getCodeCentrale();
                    }
                }
                adresseType.setPays(PRConverterUtils.formatRequiredInteger(codePays));
                donneesPostalesType.setAdresse(adresseType);
            }

            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(session, null, tiersRequerant.getIdTiers(),
                                                                                         domaineAdresse, "", JACalendar.todayJJsMMsAAAA());

            if (adressePaiement != null && StringUtils.isNotEmpty(adressePaiement.getId())) {

                BanqueAdresseType banqueAdresseType = new BanqueAdresseType();
                // 6. Nom du tiers
                banqueAdresseType.setNomTitulaire(adressePaiement.getNomTiers1() + adressePaiement.getNomTiers2());

                String idTiersBanque = adressePaiement.getIdTiersBanque();
                TIAbstractAdresseData banque = null;
                if (!JadeStringUtil.isBlankOrZero(idTiersBanque)) {
                    banque = TIAdresseResolver.dataSourceAdr(session, idTiersBanque,
                                                             domaineAdresse,
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
                    banqueAdresseType.setNom(banqueDesignation.toString().trim());

                    // 8. Numéro et rue
                    StringBuilder banqueAdresse = new StringBuilder();
                    banqueAdresse.append(banque.getRue()).append(PRACORConst.CA_CHAINE_VIDE);
                    banqueAdresse.append(banque.getNumero());
                    banqueAdresseType.setAdresse(banqueAdresse.toString().trim());
                    // 9. Localité
                    banqueAdresseType.setLocalite(banque.getLocalite());
                    // 10. Code postal
                    banqueAdresseType.setCodePostal(banque.getNpa());
                    // 11. Code pays
                    banqueAdresseType.setPays(PRConverterUtils.formatRequiredInteger(banque.getIdPays()));
                }

                // 12. swift
                if (StringUtils.isNotEmpty(adressePaiement.getSwift())) {
                    banqueAdresseType.setBic(adressePaiement.getSwift());
                }
                // Apparemment pour récupérer l'IBAN il ne faut pas utiliser la méthode getIban() mais getCompte()... ->
                // trop facile sinon
                // 13. IBAN
                // On doit supprimer les espaces pour respecter le xsd ACOR
                if (StringUtils.isNotEmpty(adressePaiement.getCompte())) {
                    banqueAdresseType.setIban(adressePaiement.getCompte().replace(" ", ""));
                }
                donneesPostalesType.setBanque(banqueAdresseType);
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la création des données postales.", e);
        }
        return donneesPostalesType;
    }

    private TIAdresseDataSource loadAdresse(String typeAdresse, String domaineAdresse, String idTiers) throws Exception {
        TITiers tiers = new TITiers();
        tiers.setIdTiers(idTiers);
        tiers.setSession(session);
        return tiers.getAdresseAsDataSource(typeAdresse, domaineAdresse, JACalendar.todayJJsMMsAAAA(), true);
    }

}
