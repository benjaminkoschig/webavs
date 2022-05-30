package globaz.prestation.acor.web.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.FamilleType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeJour;
import ch.globaz.common.util.Dates;
import globaz.corvus.acor.adapter.plat.REACORDemandeAdapter;
import globaz.corvus.acorweb.business.ImplMembreFamilleRequerantWrapper;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import org.apache.axis.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PRAcorFamilleTypeMapper extends PRAcorMapper {

    private final Map<String, ISFRelationFamiliale[]> relations = new HashMap<>();

    private final ISFMembreFamilleRequerant membreRequerant;
    private final ISFSituationFamiliale situationFamiliale;
    private final List<ISFMembreFamilleRequerant> conjoints;

    public PRAcorFamilleTypeMapper(final ISFMembreFamilleRequerant membreRequerant,
                                   final ISFSituationFamiliale situationFamiliale,
                                   final List<ISFMembreFamilleRequerant> conjoints,
                                   final PRAcorMapper prAcorMapper) {
        super(prAcorMapper.getTypeAdressePourRequerant(), prAcorMapper.getTiersRequerant(), prAcorMapper.getDomaineAdresse(), prAcorMapper.getIdNSSBidons(), prAcorMapper.getIdNoAVSBidons(), prAcorMapper.getSession());
        this.situationFamiliale = situationFamiliale;
        this.membreRequerant = membreRequerant;
        this.conjoints = conjoints;
    }

    public List<FamilleType> map() {
        return conjoints.stream()
                .map(this::creerLignes).flatMap(Collection::stream).map(this::createFamille)
                .collect(Collectors.toList());
    }

    private List<PRAcorLienFamilial> creerLignes(ISFMembreFamilleRequerant conjoint) {
        List<PRAcorLienFamilial> lignesList = new ArrayList<>();

        // Les relations sont ordonn�es de la plus ancienne � la plus r�cente.
        ISFRelationFamiliale[] relationsAll = null;
        // Cas des ex-conjoints du conjoint du requ�rant....
        if ((conjoint instanceof ImplMembreFamilleRequerantWrapper)
                && ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                .equals(((ImplMembreFamilleRequerantWrapper) conjoint).getRelationAuRequerant())) {

            relationsAll = relations(
                    ((ImplMembreFamilleRequerantWrapper) conjoint).getIdMFDuConjoint(),
                    conjoint.getIdMembreFamille());

        } else {
            relationsAll = relations(membreRequerant.getIdMembreFamille(), conjoint.getIdMembreFamille());
        }

        // regroup
        String dateMariage = null;

        for (int idRelation = 0; idRelation < relationsAll.length; ++idRelation) {

            ISFRelationFamiliale relation = relationsAll[idRelation];

            // Ce cas apparait pour les types de relations ENFANT_COMMUN ou RELATION_INDEFINIE.
            if (relation.getTypeLien() == null) {
                continue;
            }

            if (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {

                dateMariage = relation.getDateDebutRelation();
            }

            boolean isLastElement = idRelation == (relationsAll.length - 1) ? true : false;

            String csTypeLienNextElem = null;
            if (!isLastElement) {
                csTypeLienNextElem = relationsAll[idRelation + 1].getTypeLien();
            }

            if (isLastElement || ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(csTypeLienNextElem)
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(csTypeLienNextElem)) {

                // BZ-5083
                // On stocke le cas en cours
                PRAcorLienFamilial l = null;

                // Le type de lien s�par� de fait doit �tre consid�r� comme mari� ou lpart_enregistr�
                if (ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation.getTypeRelation())) {
                        l = new PRAcorLienFamilial(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                ISFSituationFamiliale.CS_TYPE_LIEN_MARIE, dateMariage);
                    } else {
                        l = new PRAcorLienFamilial(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                }
                if (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_SEPARE.equals(relation.getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(relation.getTypeRelation())) {
                        l = new PRAcorLienFamilial(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE, dateMariage);
                    } else {
                        l = new PRAcorLienFamilial(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                } else {
                    l = new PRAcorLienFamilial(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                            relation.getTypeLien(), dateMariage);
                }


                if (!ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation.getTypeRelation())
                        && !ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(relation.getTypeRelation())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {
                    l.setDateFin(relation.getDateDebut());
                } else if (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(relation.getTypeRelation())) {
                    if (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(csTypeLienNextElem)) {
                        l.setTypeFin(10);
                    }
                    l.setDateFin(relation.getDateFin());
                } else {
                    l.setDateFin(relation.getDateFin());
                }

                // mise � jour date de fin si d�c�s du conjoint ou requ�rant
                String dateDeces = conjoint.getDateDeces();
                if (conjoint instanceof ImplMembreFamilleRequerantWrapper) {
                    String dateDecesRequerant = ((ImplMembreFamilleRequerantWrapper) conjoint).getTiersRequerant().getDateDeces();
                    if (!dateDeces.isEmpty()) {
                        if (!dateDecesRequerant.isEmpty() && JadeDateUtil.isDateBefore(dateDecesRequerant, dateDeces)) {
                            dateDeces = dateDecesRequerant;
                        }
                    } else if (!dateDecesRequerant.isEmpty()) {
                        dateDeces = dateDecesRequerant;
                    }
                }
                if (!dateDeces.isEmpty() && l.getDateFin().isEmpty()
                        && (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        || (ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())))) {
                    l.setDateFin(dateDeces);
                    l.setTypeLien(ISFSituationFamiliale.CS_TYPE_LIEN_VEUF);
                } else if (!dateDeces.isEmpty() && l.getDateFin().isEmpty()
                        && (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                        || (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_SEPARE.equals(relation.getTypeLien())))) {
                    l.setDateFin(dateDeces);
                    l.setTypeLien(ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES);
                }

                lignesList.add(l);
            }
        }

        return lignesList;
    }

    private ISFRelationFamiliale[] relations(String idM1, String idM2) {
        String id = idM1 + "_" + idM2;
        ISFRelationFamiliale[] retValue = relations.get(id);

        if (retValue == null) {
            try {
                retValue = situationFamiliale.getToutesRelationsConjoints(idM1, idM2, Boolean.FALSE);
                relations.put(id, retValue);
            } catch (PRACORException e) {
                //throw e;
            } catch (Exception e) {
                //throw new PRACORException(this.getSession().getLabel("ERREUR_CHARGEMENT_RELATIONS_CONJOINTS"), e);
            }
        }

        return retValue;
    }

    /**
     * @param prAcorLienFamilial
     * @return
     */
    private FamilleType createFamille(PRAcorLienFamilial prAcorLienFamilial) {
        FamilleType famille = new FamilleType();
        if ((prAcorLienFamilial.getConjoint() instanceof ImplMembreFamilleRequerantWrapper)
                && REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                .equals(prAcorLienFamilial.getConjoint().getRelationAuRequerant())) {

            ImplMembreFamilleRequerantWrapper exConjointDuConjoint = (ImplMembreFamilleRequerantWrapper) prAcorLienFamilial
                    .getConjoint();
            famille.getNavs().add(PRConverterUtils.formatNssToLong(exConjointDuConjoint.getNssConjoint()));
            famille.getNavs().add(getNssMembre(exConjointDuConjoint));
        } else {
            famille.getNavs().add(PRConverterUtils.formatNssToLong(this.getTiersRequerant().getNSS()));
            famille.getNavs().add(getNssMembre(prAcorLienFamilial.getConjoint()));
        }
        famille.setDebut(Dates.toXMLGregorianCalendar(prAcorLienFamilial.getDateMariage()));
        if (!StringUtils.isEmpty(prAcorLienFamilial.getDateFin())) {
            short typeRelation = PRConverterUtils.formatRequiredShort(PRACORConst.csTypeLienToACOR(getSession(), prAcorLienFamilial.getTypeLien()));
            if (typeRelation == 5 || typeRelation == 9) {
                PeriodeJour separation = new PeriodeJour();
                separation.setDebut(Dates.toXMLGregorianCalendar(prAcorLienFamilial.getDateFin()));
                famille.getPeriodeSeparation().add(separation);
            } else {
                FamilleType.DonneesFin donnesFin = new FamilleType.DonneesFin();
                donnesFin.setFin(Dates.toXMLGregorianCalendar(prAcorLienFamilial.getDateFin()));
                if (prAcorLienFamilial.getTypeFin() != 0) {
                    donnesFin.setType(prAcorLienFamilial.getTypeFin());
                } else {
                    donnesFin.setType(PRConverterUtils.formatRequiredInteger(PRACORConst.csTypeLienToACOR(getSession(), prAcorLienFamilial.getTypeLien())));
                }
                famille.setDonneesFin(donnesFin);
            }
        }
        famille.setLien(PRConverterUtils.formatRequiredShort(PRACORConst.csTypeLienFamilleToACOR(getSession(), prAcorLienFamilial.getTypeLien())));
        famille.setPensionAlimentaire(false);
        return famille;
    }
}
