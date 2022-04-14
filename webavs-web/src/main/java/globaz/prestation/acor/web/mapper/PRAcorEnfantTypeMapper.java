package globaz.prestation.acor.web.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.*;
import ch.globaz.common.util.Dates;
import globaz.commons.nss.NSUtil;
import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static ch.globaz.common.util.Collections.forEachPair;

@Slf4j
public class PRAcorEnfantTypeMapper extends PRAcorMapper {

    private final ISFSituationFamiliale situationFamiliale;
    private final List<ISFMembreFamilleRequerant> enfantsFamilles;


    public PRAcorEnfantTypeMapper(final ISFSituationFamiliale situationFamiliale,
                                  final List<ISFMembreFamilleRequerant> enfantsFamilles,
                                  final PRAcorMapper prAcorMapper) {
        super(prAcorMapper.getTypeAdressePourRequerant(), prAcorMapper.getTiersRequerant(), prAcorMapper.getDomaineAdresse(), prAcorMapper.getIdNSSBidons(), prAcorMapper.getIdNoAVSBidons(), prAcorMapper.getSession());
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
            enfants.add(enfantTypeFunction.apply(membre, enfant));
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
        enfant.setDateNaissance(Dates.toXMLGregorianCalendar(membre.getDateNaissance()));
        if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
            enfant.setDateDeces(Dates.toXMLGregorianCalendar(membre.getDateDeces()));
        }
        //TODO demande un int dans le JSON sinon l'appli client met une erreur
        enfant.setNationalite(getCodePays(membre.getCsNationalite()));
        enfant.setDomicile(getDomicile(membre.getCsCantonDomicile(), membre.getPays(), this.getTiersRequerant()));
        enfant.setRefugie(false);
        enfant.setSexe(PRACORConst.csSexeEnfantToAcor2020(membre.getCsSexe()));

        String noAvsPere = getNssBidon(detail.getNoAvsPere(), PRACORConst.CS_HOMME, detail.getNomPere() + detail.getPrenomPere(), detail.getDateNaissancePere());
        if (!JadeStringUtil.isBlankOrZero(noAvsPere)) {
            enfant.setNavsPere(PRConverterUtils.formatNssToLong(noAvsPere));
        } else {
            enfant.setPereInconnu(true);
        }
        String noAvsMere = getNssBidon(detail.getNoAvsMere(), PRACORConst.CS_FEMME, detail.getNomMere() + detail.getPrenomMere(), detail.getDateNaissanceMere());
        if (!JadeStringUtil.isBlankOrZero(noAvsMere)) {
            enfant.setNavsMere(PRConverterUtils.formatNssToLong(noAvsMere));
        } else {
            enfant.setMereInconnue(true);
        }
        // TODO rechercher etat civil et mapper selon EtatCivil-types.xsd
        enfant.setEtatCivil(Short.valueOf(PRACORConst.csTypeLienToACOR(this.getSession(), membre.getCsEtatCivil())));
        if (!JadeStringUtil.isBlankOrZero(detail.getDateAdoption())) {
            enfant.setDateAdoption(Dates.toXMLGregorianCalendar(detail.getDateAdoption(), YYYY_MM_DD_FORMAT));
        }

        // EURO_FORM
        enfant.setDonneesPostales(createDonneesPostales());

        // PERIODES
        addPeriodesEnfant(enfant, membre);

        return enfant;
    }

    private void addPeriodesEnfant(EnfantType enfant, ISFMembreFamilleRequerant membre) {
        ISFPeriode[] periodes = recupererPeriodesMembre(membre);
        for (ISFPeriode isfPeriode : periodes) {
            if (isPeriodeBTE(isfPeriode)) {
                enfant.getPeriodeBTE().add(createPeriodeBTE(isfPeriode));
            } else if (isPeriodeEnfantRecueilliGratuitement(isfPeriode)) {
                enfant.getPeriodeRecueilliG().add(createPeriodeRecueilliG(isfPeriode));
            } else if (isPeriodeEnfantRecueilliGratuitementConjoint(isfPeriode)) {
                enfant.getPeriodeRecueilliC().add(createPeriodeRecueilliC(isfPeriode));
            } else if (isPeriodeRefusAF(isfPeriode)) {
                PeriodeEnfantType periode = createPeriodeEnfant(enfant, isfPeriode);
                enfant.getPeriode().add(periode);
            } else if (getEnumPeriodeEnfantType(isfPeriode) != null) {
                PeriodeEnfantType periodeEnfantType = createPeriodeEnfantType(isfPeriode);
                enfant.getPeriode().add(periodeEnfantType);
            }
        }
    }

    private boolean isPeriodeBTE(ISFPeriode isfPeriode) {
        return StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE, isfPeriode.getType()) || StringUtils.equals(PRACORConst.CA_PERIODE_GARDE_ETC_BTE, isfPeriode.getType());
    }

    private boolean isPeriodeEnfantRecueilliGratuitement(ISFPeriode isfPeriode) {
        return StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT, isfPeriode.getType()) || StringUtils.equals(PRACORConst.CA_PERIODE_ENFANT_RECUEILLI_GRATUITEMENT, isfPeriode.getType());
    }

    private boolean isPeriodeEnfantRecueilliGratuitementConjoint(ISFPeriode isfPeriode) {
        return StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT_CONJOINT, isfPeriode.getType()) || StringUtils.equals(PRACORConst.CA_PERIODE_ENFANT_RECUEILLI_GRATUITEMENT_CONJOINT, isfPeriode.getType());
    }

    private boolean isPeriodeRefusAF(final ISFPeriode isfPeriode) {
        return StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_REFUS_AF, isfPeriode.getType()) || StringUtils.equals(PRACORConst.CA_PERIODE_REFUS_AF, isfPeriode.getType());
    }

    private PeriodeEnfantType createPeriodeEnfant(final EnfantType enfant, final ISFPeriode isfPeriode) {
        PeriodeEnfantType periode = new PeriodeEnfantType();
        XMLGregorianCalendar dateDebut = enfant.getDateNaissance();
        XMLGregorianCalendar dateFin = Dates.toXMLGregorianCalendar(Dates.toDate(enfant.getDateNaissance()).plusYears(25));
        if (!isfPeriode.getDateDebut().equals("")) {
            dateDebut = Dates.toXMLGregorianCalendar(Dates.toDate(isfPeriode.getDateDebut()));
        }
        if (!isfPeriode.getDateFin().equals("99.99.9999")) {
            dateFin = Dates.toXMLGregorianCalendar(Dates.toDate(isfPeriode.getDateFin()));
        }
        periode.setDebut(dateDebut);
        periode.setFin(dateFin);
        periode.setType(PeriodeEnfantTypeEnum.ALLOCATION_FAMILIALE);
        return periode;
    }

    static List<PeriodeEnfantType> createPeriodeAllocationFamilliale(List<ISFPeriode> sFperiodes, LocalDate dateNaissance) {
        List<ISFPeriode> sFPeriodes = sFperiodes.stream()
                                                .filter(PRAcorEnfantTypeMapper::isPeriodeRefusAf)
                                                .sorted(Comparator.comparing(o -> Dates.toDate(o.getDateDebut())))
                                                .collect(Collectors.toList());

        List<PeriodeEnfantType> periodes = new ArrayList<>();
        if (!sFPeriodes.isEmpty()) {
            LocalDate dateDebutMin = Dates.toDate(sFPeriodes.get(0).getDateDebut());
            LocalDate dateFinMax = Dates.toDate(sFPeriodes.get(sFPeriodes.size() - 1).getDateFin());
            if (dateDebutMin.isAfter(dateNaissance)) {
                periodes.add(createPeriodeAllocationFamillie(dateNaissance, dateDebutMin.minusDays(1)));
            }

            forEachPair((periode1, periode2) -> {
                LocalDate dateFin = Dates.toDate(periode1.getDateFin());
                LocalDate dateDebut = periode2.map(p -> Dates.toDate(p.getDateDebut())).orElse(null);
                if (dateFin != null && dateDebut != null) {
                    periodes.add(createPeriodeAllocationFamillie(dateFin.plusDays(1), dateDebut.minusDays(1)));
                }
            }, sFPeriodes);

            if (dateFinMax != null) {
                periodes.add(createPeriodeAllocationFamillie(dateFinMax.plusDays(1), dateNaissance.plusYears(25)));
            }
        } else {
            periodes.add(createPeriodeAllocationFamillie(dateNaissance, dateNaissance.plusYears(25)));
        }
        return periodes;
    }


    private static PeriodeEnfantType createPeriodeAllocationFamillie(final LocalDate dateDebut, final LocalDate dateFin) {
        PeriodeEnfantType periode = new PeriodeEnfantType();
        periode.setDebut(Dates.toXMLGregorianCalendar(dateDebut));
        periode.setFin(Dates.toXMLGregorianCalendar(dateFin));
        periode.setType(PeriodeEnfantTypeEnum.ALLOCATION_FAMILIALE);
        return periode;
    }

    private static boolean isPeriodeRefusAf(final ISFPeriode isfPeriode) {
        return ch.globaz.hera.business.constantes.ISFPeriode.CS_TYPE_PERIODE_REFUS_AF.equals(isfPeriode.getType());
    }

    private PeriodeEnfantType createPeriodeEnfantType(ISFPeriode isfPeriode) {
        PeriodeEnfantType periode = new PeriodeEnfantType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        periode.setType(getEnumPeriodeEnfantType(isfPeriode));
        return periode;
    }

    private PeriodeEnfantTypeEnum getEnumPeriodeEnfantType(ISFPeriode isfPeriode) {
        switch (isfPeriode.getType()) {
            case ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE:
            case PRACORConst.CA_PERIODE_DOMICILE_EN_SUISSE:
                return PeriodeEnfantTypeEnum.DOMICILE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE:
            case PRACORConst.CA_PERIODE_NATIONALITE_SUISSE:
                return PeriodeEnfantTypeEnum.NATIONALITE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE:
            case PRACORConst.CA_PERIODE_ETUDE:
                return PeriodeEnfantTypeEnum.ETUDE;
            default:
                return null;
        }
    }

    private PeriodeRecueilliGType createPeriodeRecueilliG(ISFPeriode isfPeriode) {
        PeriodeRecueilliGType periode = new PeriodeRecueilliGType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        periode.setTuteur(StringUtils.equals(String.valueOf(TypeDeDetenteur.TUTEUR_LEGAL.getCodeSystem()), isfPeriode.getCsTypeDeDetenteur()));
        return periode;
    }

    private PeriodeRecueilliCType createPeriodeRecueilliC(ISFPeriode isfPeriode) {
        PeriodeRecueilliCType periode = new PeriodeRecueilliCType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        periode.setParentNonBiologique(Long.valueOf(NSUtil.unFormatAVS(isfPeriode.getNoAvsRecueillant())));
        return periode;
    }

    private ISFPeriode[] recupererPeriodesMembre(ISFMembreFamilleRequerant membre) {
        return PRAcorPeriodeMapper.recupererPeriodesMembre(this.situationFamiliale, membre);
    }

    private PeriodeBTEType createPeriodeBTE(ISFPeriode isfPeriode) {
        PeriodeBTEType periode = new PeriodeBTEType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        if (StringUtils.equals(TypeDeDetenteur.FAMILLE.getCodeSystemAsString(), isfPeriode.getCsTypeDeDetenteur())) {
            if (StringUtils.isNotEmpty(isfPeriode.getNoAvsDetenteurBTE())) {
                periode.setEducateur(Long.valueOf(NSUtil.unFormatAVS(isfPeriode.getNoAvsDetenteurBTE())));
            } else {
                periode.setTiers(false);
            }
        } else if (StringUtils.equals(TypeDeDetenteur.TIERS.getCodeSystemAsString(), isfPeriode.getCsTypeDeDetenteur())) {
            periode.setTiers(true);
        }
        return periode;
    }
}
