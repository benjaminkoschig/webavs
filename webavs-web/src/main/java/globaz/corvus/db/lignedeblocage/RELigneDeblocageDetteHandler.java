package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import globaz.osiris.db.comptes.CASectionManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.osiris.exception.OsirisException;

class RELigneDeblocageDetteHandler {

    private BSession session;

    public RELigneDeblocageDetteHandler(BSession session) {
        this.session = session;
    }

    public List<RELigneDeblocageDette> toDette(Collection<RELigneDeblocage> lignesDeblocage, Set<String> idTiers) {
        Collection<RELigneDeblocageDette> dettes = findListDetteEnCompta(idTiers);
        Set<String> idsSection = new HashSet<String>();
        for (RELigneDeblocage ligne : lignesDeblocage) {
            idsSection.add(ligne.getIdSectionCompensee().toString());
        }
        List<RELigneDeblocageDette> dettesComptabiliser = findDetteComptabiliser(idsSection);
        List<RELigneDeblocageDette> dettesMerged = mergedDetteWithDeblocage(dettes, dettesComptabiliser,
                lignesDeblocage);
        Collections.sort(dettesMerged, new Comparator<RELigneDeblocageDette>() {
            @Override
            public int compare(RELigneDeblocageDette o1, RELigneDeblocageDette o2) {
                return o1.getIdSectionCompensee().compareTo(o2.getIdSectionCompensee());
            }
        });
        return dettesMerged;
    }

    private List<RELigneDeblocageDette> mergedDetteWithDeblocage(Collection<RELigneDeblocageDette> dettes,
            List<RELigneDeblocageDette> dettesComptabiliser, Collection<RELigneDeblocage> lignesDeblocage) {

        Map<String, RELigneDeblocageDette> mapDettes = new HashMap<String, RELigneDeblocageDette>();
        for (RELigneDeblocageDette dette : dettes) {
            mapDettes.put(dette.getIdSectionCompensee() + "_" + dette.getIdRoleSection(), dette);
        }

        for (RELigneDeblocageDette dette : dettesComptabiliser) {
            mapDettes.put(dette.getIdSectionCompensee() + "_" + dette.getIdRoleSection(), dette);
        }

        List<RELigneDeblocageDette> list = new ArrayList<RELigneDeblocageDette>();
        Set<String> keyMatch = new HashSet<String>();
        Map<String, Montant> mapDettesNonLiquide = new HashMap<String, Montant>();
        for (RELigneDeblocage ligne : lignesDeblocage) {
            String key = ligne.getIdSectionCompensee() + "_" + ligne.getIdRoleSection();
            if (mapDettesNonLiquide.containsKey(key)) {
                mapDettesNonLiquide.put(key, mapDettesNonLiquide.get(key).add(ligne.getMontant()));
            } else {
                mapDettesNonLiquide.put(key, ligne.getMontant());
            }
            if (mapDettes.containsKey(key)) {
                RELigneDeblocageDette detteComptat = mapDettes.get(key);
                RELigneDeblocageDette dette = new RELigneDeblocageDette();
                dette.setIdAndJustDoIt(ligne.getIdEntity());
                dette.setIdEntity(ligne.getIdEntity());
                dette.setIdLot(ligne.getIdLot());
                dette.setSpy(ligne.getSpy());
                dette.setMontant(ligne.getMontant());
                dette.setIdAndJustDoIt(ligne.getIdEntity());
                dette.setIdRenteAccordee(ligne.getIdRenteAccordee());
                dette.setEtat(ligne.getEtat());
                dette.setType(ligne.getType());
                dette.setRefPaiement(ligne.getRefPaiement());
                dette.setDescription(detteComptat.getDescription());
                dette.setDescriptionCompteAnnexe(detteComptat.getDescriptionCompteAnnexe());
                dette.setType(ligne.getType());
                dette.setMontanDette(detteComptat.getMontanDette());
                dette.setIdRoleSection(detteComptat.getIdRoleSection());
                dette.setIdSectionCompensee(detteComptat.getIdSectionCompensee());
                list.add(dette);
                keyMatch.add(key);
            }
        }

        for (RELigneDeblocage ligne : lignesDeblocage) {
            String key = ligne.getIdSectionCompensee() + "_" + ligne.getIdRoleSection();
            if (mapDettesNonLiquide.containsKey(key)) {
                if (mapDettesNonLiquide.get(key).equals(ligne.getMontant())) {
                    mapDettes.remove(key);
                }
            }
        }

        list.addAll(mapDettes.values());

        return list;
    }

    private List<RELigneDeblocageDette> findListDetteEnCompta(Set<String> idTiers) {
        List<RELigneDeblocageDette> list = new ArrayList<RELigneDeblocageDette>();
        if (!idTiers.isEmpty()) {
            try {
                CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
                mgr.setForIdTiersIn(idTiers);
                mgr.setSession(session);
                mgr.setForSoldePositif(true);
                mgr.find(BManager.SIZE_NOLIMIT);
                Set<String> idsSection = new HashSet<String>();
                toDettes(idsSection, list, mgr);
            } catch (Exception e1) {
                throw new RuntimeException("Unable to search the dette en compta ", e1);
            }
        }
        return list;
    }

    private List<RELigneDeblocageDette> findDetteComptabiliser(Set<String> idsSection) {
        List<RELigneDeblocageDette> list = new ArrayList<RELigneDeblocageDette>();
        if (!idsSection.isEmpty()) {
            try {
                CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
                mgr.setForIdSectionIn(idsSection);
                mgr.setSession(session);
                mgr.find(BManager.SIZE_NOLIMIT);
                toDettes(idsSection, list, mgr);
            } catch (Exception e1) {
                throw new RuntimeException("Unable to search the dette en compta ", e1);
            }
        }
        return list;
    }

    public RELigneDeblocageDette readDetteComptabiliser(Long idSection, Long idRole) {
        HashSet<String> ids = new HashSet<String>();
        ids.add(String.valueOf(idSection));
        List<RELigneDeblocageDette> dettes = findDetteComptabiliser(ids);
        for (RELigneDeblocageDette dette : dettes) {
            if (idRole.equals(dette.getIdRoleSection())) {
                return dette;
            }
        }
        return null;
    }

    private void toDettes(Set<String> idsSection, List<RELigneDeblocageDette> list,
            CASectionJoinCompteAnnexeJoinTiersManager mgr) throws OsirisException {
        List<CASectionJoinCompteAnnexeJoinTiers> sections = mgr.toList();
        for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
            RELigneDeblocageDette dette = new RELigneDeblocageDette();
            dette.setIdRoleSection(Long.valueOf(section.getIdRole()));
            dette.setIdSectionCompensee(Long.valueOf(section.getIdSection()));
            dette.setDescriptionCompteAnnexe(section.getDescriptionCompteAnnexe());
            dette.setIdCompteAnnexe(section.getIdCompteAnnexe());
            dette.setEtat(RELigneDeblocageEtat.NONE);
            dette.setIdSectionCompensee(Long.valueOf(section.getIdSection()));
            dette.setMontant(Montant.ZERO);
            dette.setMontanDette(new Montant(section.getSolde()));
            dette.setType(RELigneDeblocageType.DETTE_EN_COMPTA);
            idsSection.add(section.getIdSection());
            list.add(dette);
        }
        Map<Long, String> descriptions = findDescription(idsSection);
        for (RELigneDeblocageDette dette : list) {
            dette.setDescription(descriptions.get(dette.getIdSectionCompensee()));
        }
    }

    private Map<Long, String> findDescription(Set<String> forIdsSectionIn) throws OsirisException {
        Map<Long, String> descriptions = new HashMap<Long, String>();

        if (forIdsSectionIn != null && !forIdsSectionIn.isEmpty()) {

            CASectionManager mgr = new CASectionManager();
            mgr.setSession(session);
            mgr.setForIdSectionIn(forIdsSectionIn);

            try {
                mgr.find(BManager.SIZE_NOLIMIT);
                if (mgr.hasErrors()) {
                    new OsirisException("Technical exception, Error in research of the section");
                }
            } catch (Exception e) {
                new OsirisException("Technical exception, Error in research of the section", e);
            }
            if (mgr.size() > 0) {
                List<CASection> sections = mgr.toList();
                for (CASection caSection : sections) {
                    descriptions.put(Long.valueOf(caSection.getId()), caSection.getDescription());
                }
            }
        }

        return descriptions;
    }
}
