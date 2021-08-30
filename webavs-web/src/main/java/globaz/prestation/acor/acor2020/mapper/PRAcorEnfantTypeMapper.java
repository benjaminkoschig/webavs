package globaz.prestation.acor.acor2020.mapper;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantType;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class PRAcorEnfantTypeMapper extends PRAcorMapper {

    private final ISFSituationFamiliale situationFamiliale;
    private final List<ISFMembreFamilleRequerant> enfantsFamilles;

    public PRAcorEnfantTypeMapper(final Boolean adresseCourrierPourRequerant,
                                  final PRTiersWrapper tiersRequerant,
                                  final ISFSituationFamiliale situationFamiliale,
                                  final List<ISFMembreFamilleRequerant> enfantsFamilles,
                                  final BSession session) {
        super(adresseCourrierPourRequerant, tiersRequerant,session);
        this.situationFamiliale = situationFamiliale;
        this.enfantsFamilles = enfantsFamilles;
    }

    public List<EnfantType> map() throws PRACORException {
        return this.createListEnfants(this.enfantsFamilles, (membreFamilleRequerant, enfantType) -> enfantType);
    }

    public List<EnfantType> map(BiFunction<ISFMembreFamilleRequerant, EnfantType, EnfantType> enfantTypeFunction) throws PRACORException {
        return this.createListEnfants(this.enfantsFamilles, enfantTypeFunction);
    }

    private List<EnfantType> createListEnfants(List<ISFMembreFamilleRequerant> enfantsFamilles, final BiFunction<ISFMembreFamilleRequerant, EnfantType, EnfantType> enfantTypeFunction) throws PRACORException {
        List<EnfantType> enfants = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : enfantsFamilles) {
            EnfantType enfant = createEnfantType(membre);
            enfants.add(enfantTypeFunction.apply(membre,enfant));
        }
        return enfants;
    }

    private EnfantType createEnfantType(final ISFMembreFamilleRequerant membre) throws PRACORException {
        EnfantType enfant = new EnfantType();
        ISFEnfant detail;
        try {
            detail = this.situationFamiliale.getEnfant(membre.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException(getSession().getLabel("ERREUR_DETAILS_ENFANTS"), e);
        }

        enfant.setNavs(getNssMembre(membre));
        if (StringUtils.isNotEmpty(membre.getNom())) {
            enfant.setNom(membre.getNom());
        }
        if (StringUtils.isNotEmpty(membre.getPrenom())) {
            enfant.setPrenom(membre.getPrenom());
        }
        enfant.setDateNaissance(PRConverterUtils.formatDate(membre.getDateNaissance(), DD_MM_YYYY_FORMAT));
        if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
            enfant.setDateDeces(PRConverterUtils.formatDate(membre.getDateDeces(), DD_MM_YYYY_FORMAT));
        }
        //TODO demande un int dans le JSON sinon l'appli client met une erreur
        enfant.setNationalite(getCodePays(membre.getCsNationalite()));
        enfant.setDomicile(getDomicile(membre.getCsCantonDomicile(), membre.getPays(), this.getTiersRequerant()));
        enfant.setRefugie(false);
        enfant.setSexe(PRACORConst.csSexeEnfantToAcor2020(membre.getCsSexe()));

        String noAvsPere = nssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME, detail.getNomPere() + detail.getPrenomPere(), true);
        if (!JadeStringUtil.isBlankOrZero(noAvsPere)) {
            enfant.setNavsPere(PRConverterUtils.formatNssToLong(noAvsPere));
        } else {
            enfant.setPereInconnu(true);
        }
        String noAvsMere = nssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME, detail.getNomMere() + detail.getPrenomMere(), true);
        if (!JadeStringUtil.isBlankOrZero(noAvsMere)) {
            enfant.setNavsMere(PRConverterUtils.formatNssToLong(noAvsMere));
        } else {
            enfant.setMereInconnue(true);
        }
        // TODO rechercher etat civil et mapper selon EtatCivil-types.xsd
        enfant.setEtatCivil(Short.valueOf(PRACORConst.csTypeLienToACOR(this.getSession(), membre.getCsEtatCivil())));
        if (!JadeStringUtil.isBlankOrZero(detail.getDateAdoption())) {
            enfant.setDateAdoption(PRConverterUtils.formatDate(detail.getDateAdoption(), YYYY_MM_DD_FORMAT));
        }

        // EURO_FORM
        enfant.setDonneesPostales(createDonneesPostales());

        // PERIODES
        //addPeriodesEnfant(enfant, membre);
        return enfant;
    }

//    private void addPeriodesEnfant(EnfantType enfant, ISFMembreFamilleRequerant membre) {
//        for (ISFPeriode isfPeriode : recupererPeriodesMembre(membre)) {
//            // TODO ajouter les période recueilli C et G (G = recueilli gratuitement avec ou sans tuteur)
//            if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE, isfPeriode.getType())) {
//                enfant.getPeriodeBTE().add(createPeriodeBTE(isfPeriode));
//            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT, isfPeriode.getType())) {
//                enfant.getPeriodeRecueilliG().add(createPeriodeRecueilliG(isfPeriode));
//            } else if (getEnumPeriodeEnfantType(isfPeriode) != null) {
//                enfant.getPeriode().add(createPeriodeEnfantType(isfPeriode));
//            }
//        }
//    }
//
//    private PeriodeEnfantType createPeriodeEnfantType(ISFPeriode isfPeriode) {
//        PeriodeEnfantType periode = new PeriodeEnfantType();
//        periode.setDebut(PRConverterUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
//        periode.setFin(PRConverterUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
//        periode.setType(getEnumPeriodeEnfantType(isfPeriode));
//        return periode;
//    }
//
//    private PeriodeEnfantTypeEnum getEnumPeriodeEnfantType(ISFPeriode isfPeriode) {
//        switch (isfPeriode.getType()) {
//            case ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE:
//                return PeriodeEnfantTypeEnum.DOMICILE;
//            case ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE:
//                return PeriodeEnfantTypeEnum.NATIONALITE;
//            case ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE:
//                return PeriodeEnfantTypeEnum.ETUDE;
//            default:
//                return null;
//        }
//    }

//
//    private PeriodeRecueilliGType createPeriodeRecueilliG(ISFPeriode isfPeriode) {
//        PeriodeRecueilliGType periode = new PeriodeRecueilliGType();
//        periode.setDebut(PRConverterUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
//        periode.setFin(PRConverterUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
//        if (StringUtils.equals(String.valueOf(TypeDeDetenteur.TUTEUR_LEGAL.getCodeSystem()), isfPeriode.getCsTypeDeDetenteur())) {
//            periode.setTuteur(true);
//        } else {
//            periode.setTuteur(false);
//        }
//        return periode;
//    }
//
//    private ISFPeriode[] recupererPeriodesMembre(ISFMembreFamilleRequerant membre) {
//        ISFPeriode[] periodes;
//        try {
//            ISFPeriode[] periodesToFilre = situationFamiliale.getPeriodes(membre.getIdMembreFamille());
//
//            // On filtre les période qui ne sont pas connues d'ACOR
//            List<ISFPeriode> listPeriode = new ArrayList<ISFPeriode>();
//            for (int i = 0; i < periodesToFilre.length; i++) {
//                if (!getTypePeriode(periodesToFilre[i]).isEmpty()) {
//                    listPeriode.add(periodesToFilre[i]);
//                }
//            }
//            periodes = listPeriode.toArray(new ISFPeriode[listPeriode.size()]);
//
//            // si demande survivant
//            if (getTypeDemande().equals(PRACORConst.CA_TYPE_DEMANDE_SURVIVANT)) {
//                // si membre = requérant
//                PRDemande demandePrest = new PRDemande();
//                demandePrest.setSession(getSession());
//                demandePrest.setIdDemande(demandeRente.getIdDemandePrestation());
//                demandePrest.retrieve();
//
//                if (demandePrest.getIdTiers().equals(membre.getIdTiers())) {
//
//                    // Workaround ACOR
//                    // Si personne décédée sans période de domicile en
//                    // suisse,
//                    // Il faut la crééer pour l'envoyer à ACOR autrement
//                    // ACOR ne calcul pas.
//                    // si requérant avec date décès
//                    if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
//                        // si suisse
//
//                        if (membre.getCsNationalite().equals("100")) {
//                            // voir si une période de domicile en suisse
//                            // dans la liste
//                            boolean isPeriodeDomicileSuisse = false;
//                            for (int i = 0; i < periodes.length; i++) {
//                                ISFPeriode periode = periodes[i];
//                                if (periode.getNoAvs().equals(((ISFMembreFamilleRequerant) membre).getNss())
//                                        && ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE.equals(periode
//                                                                                                         .getType())) {
//                                    isPeriodeDomicileSuisse = true;
//                                }
//                            }
//
//                            if (!isPeriodeDomicileSuisse) {
//                                // Créer une période de domicile en
//                                // suisse pour cet assuré
//                                SFPeriodeWrapper periode = new SFPeriodeWrapper();
//                                periode.setDateFin(membre.getDateDeces());
//                                periode.setDateDebut(membre.getDateNaissance());
//                                periode.setNoAvs(membre.getNss());
//                                periode.setPays(membre.getCsNationalite());
//                                periode.setType(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE);
//
//                                List lperiodes = new ArrayList();
//                                for (int i = 0; i < periodes.length; i++) {
//                                    lperiodes.add(periodes[i]);
//                                }
//                                lperiodes.add(periode);
//                                periodes = (ISFPeriode[]) lperiodes.toArray(new ISFPeriode[lperiodes.size()]);
//                            }
//
//                        }
//
//                    }
//
//                }
//
//            }
//
//        } catch (Exception e) {
//            LOG.error("Erreur lors de la récupération des périodes par membres.", e);
//            periodes = new ISFPeriode[0];
//        }
//        return periodes;
//    }
//
//    private PeriodeBTEType createPeriodeBTE(ISFPeriode isfPeriode) {
//        PeriodeBTEType periode = new PeriodeBTEType();
//        periode.setDebut(PRConverterUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
//        periode.setFin(PRConverterUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
//        if (StringUtils.equals(TypeDeDetenteur.FAMILLE.getCodeSystemAsString(), isfPeriode.getCsTypeDeDetenteur())) {
//            if (StringUtils.isNotEmpty(isfPeriode.getNoAvsDetenteurBTE())) {
//                periode.setEducateur(Long.valueOf(NSUtil.unFormatAVS(isfPeriode.getNoAvsDetenteurBTE())));
//            } else {
//                periode.setTiers(false);
//            }
//        } else if (StringUtils.equals(TypeDeDetenteur.TIERS.getCodeSystemAsString(), isfPeriode.getCsTypeDeDetenteur())) {
//            periode.setTiers(true);
//        }
//        return periode;
//    }
//
//
//    private String getTypePeriode(ISFPeriode periode) {
//
//        if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE)) {
//            return "do";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE)) {
//            return "tr";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE)) {
//            return "na";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION)) {
//            return "af";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION)) {
//            return "ex";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE)) {
//            return "ae";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT)) {
//            return "rc";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
//            return "et";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ)) {
//            return "ij";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE)) {
//            return "be";
//        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION)) {
//            return "in";
//        } else {
//            return "";
//        }
//    }

}
