package globaz.prestation.acor.acor2020.mapper;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.FamilleType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeJour;
import ch.globaz.common.util.Dates;
import globaz.corvus.acor.adapter.plat.REACORDemandeAdapter;
import globaz.corvus.acor2020.business.ImplMembreFamilleRequerantWrapper;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        super(prAcorMapper.getTypeAdressePourRequerant(), prAcorMapper.getTiersRequerant(),prAcorMapper.getDomaineAdresse(), prAcorMapper.getSession());
        this.situationFamiliale = situationFamiliale;
        this.membreRequerant = membreRequerant;
        this.conjoints = conjoints;
    }

    public List<FamilleType> map() {
        return conjoints.stream()
                        .map(this::creerLignes).flatMap(Collection::stream).map(this::createFamille)
                        .collect(Collectors.toList());
    }

    private List<Ligne> creerLignes(ISFMembreFamilleRequerant conjoint) {
        List<Ligne> lignesList = new ArrayList<>();

        // Les relations sont ordonnées de la plus ancienne à la plus récente.
        ISFRelationFamiliale[] relationsAll = null;
        // Cas des ex-conjoints du conjoint du requérant....
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
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation
                                                                                          .getTypeLien())
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
                Ligne l = null;

                // Le type de lien séparé de fait doit être considéré comme marié ou lpart_enregistré
                if (ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                                                                                        .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                      ISFSituationFamiliale.CS_TYPE_LIEN_MARIE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                      relation.getTypeLien(), dateMariage);
                    }
                } else if (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT.equals(relation
                                                                                           .getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                                                                                        .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                      ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                      relation.getTypeLien(), dateMariage);
                    }
                } else {
                    l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                  relation.getTypeLien(), dateMariage);
                }


                if (!ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation.getTypeRelation())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())){
                    l.setDateFin(relation.getDateDebut());
                } else {
                    l.setDateFin(relation.getDateFin());
                }

                // mise à jour date de fin si décè du conjoint
                if (!conjoint.getDateDeces().isEmpty() && l.getDateFin().isEmpty()
                        && (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        || (ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())))) {
                    l.setDateFin(conjoint.getDateDeces());
                    l.setTypeLien(ISFSituationFamiliale.CS_TYPE_LIEN_VEUF);
                } else if(!conjoint.getDateDeces().isEmpty() && l.getDateFin().isEmpty()
                        && (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation.getTypeLien())
                        || (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_SEPARE.equals(relation.getTypeLien())))) {
                    l.setDateFin(conjoint.getDateDeces());
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

    private FamilleType createFamille(Ligne ligne) {
        FamilleType famille = new FamilleType();
        if ((ligne.getConjoint() instanceof ImplMembreFamilleRequerantWrapper)
                && REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                .equals(ligne.getConjoint().getRelationAuRequerant())) {

            ImplMembreFamilleRequerantWrapper exConjointDuConjoint = (ImplMembreFamilleRequerantWrapper) ligne
                    .getConjoint();
            famille.getNavs().add(PRConverterUtils.formatNssToLong(exConjointDuConjoint.getNssConjoint()));
            famille.getNavs().add(getNssMembre(exConjointDuConjoint));
        } else {
            famille.getNavs().add(PRConverterUtils.formatNssToLong(this.getTiersRequerant().getNSS()));
            famille.getNavs().add(getNssMembre(ligne.getConjoint()));
        }
        famille.setDebut(Dates.toXMLGregorianCalendar(ligne.getDateMariage()));
        if (ligne.getDateFin() != null) {
            short typeRelation = PRConverterUtils.formatRequiredShort(PRACORConst.csTypeLienToACOR(getSession(), ligne.getTypeLien()));
            if (typeRelation == 5 || typeRelation == 9) {
                PeriodeJour separation = new PeriodeJour();
                separation.setDebut(Dates.toXMLGregorianCalendar(ligne.getDateFin()));
                famille.getPeriodeSeparation().add(separation);
            } else {
                FamilleType.DonneesFin donnesFin = new FamilleType.DonneesFin();
                donnesFin.setFin(Dates.toXMLGregorianCalendar(ligne.getDateFin()));
                famille.setDonneesFin(donnesFin);
                donnesFin.setType(PRConverterUtils.formatRequiredShort(PRACORConst.csTypeLienToACOR(getSession(), ligne.getTypeLien())));
            }
        }
        famille.setLien(PRConverterUtils.formatRequiredShort(PRACORConst.csTypeLienFamilleToACOR(getSession(), ligne.getTypeLien())));
        famille.setPensionAlimentaire(false);
        return famille;
    }
}
