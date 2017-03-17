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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.osiris.exception.OsirisException;

class RELingeDeblocageDetteHandler {

    private BSession session;

    public RELingeDeblocageDetteHandler(BSession session) {
        this.session = session;
    }

    public List<RELigneDeblocageDette> toDette(List<RELigneDeblocage> lignesDeblocage, Set<String> idTiers) {
        List<RELigneDeblocageDette> dettes = findListDetteEnCompta(idTiers);
        return mergedDetteWithDeblocage(dettes, lignesDeblocage);
    }

    private List<RELigneDeblocageDette> mergedDetteWithDeblocage(List<RELigneDeblocageDette> dettes,
            List<RELigneDeblocage> lignesDeblocage) {

        Map<String, RELigneDeblocageDette> mapDettes = new HashMap<String, RELigneDeblocageDette>();
        for (RELigneDeblocageDette dette : dettes) {
            mapDettes.put(dette.getIdSectionDetteEnCompta() + "_" + dette.getIdRoleDetteEnCompta(), dette);
        }

        List<RELigneDeblocageDette> list = new ArrayList<RELigneDeblocageDette>();
        Set<String> keyMatch = new HashSet<String>();
        for (RELigneDeblocage ligne : lignesDeblocage) {

            String key = ligne.getIdSectionDetteEnCompta() + "_" + ligne.getIdRoleDetteEnCompta();
            if (mapDettes.containsKey(key)) {
                RELigneDeblocageDette detteComptat = mapDettes.get(key);
                RELigneDeblocageDette dette = new RELigneDeblocageDette();
                dette.setIdEntity(ligne.getIdEntity());
                dette.setIdRentePrestation(ligne.getIdRentePrestation());
                dette.setEtat(ligne.getEtat());
                dette.setType(ligne.getType());
                dette.setRefPaiement(ligne.getRefPaiement());
                dette.setDescription(detteComptat.getDescription());
                dette.setDescriptionCompteAnnexe(detteComptat.getDescriptionCompteAnnexe());
                dette.setType(ligne.getType());
                dette.setMontanDette(detteComptat.getMontanDette());
                dette.setMontant(detteComptat.getMontant());
                dette.setIdRoleDetteEnCompta(detteComptat.getIdRoleDetteEnCompta());
                dette.setIdSectionDetteEnCompta(detteComptat.getIdSectionDetteEnCompta());
                list.add(dette);
                keyMatch.add(key);
            }
        }

        for (RELigneDeblocageDette dette : dettes) {
            String key = dette.getIdSectionDetteEnCompta() + "_" + dette.getIdRoleDetteEnCompta();
            if (keyMatch.contains(key)) {
                mapDettes.remove(key);
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
                List<CASectionJoinCompteAnnexeJoinTiers> sections = mgr.toList();
                for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
                    RELigneDeblocageDette dette = new RELigneDeblocageDette();
                    dette.setIdRoleDetteEnCompta(Integer.valueOf(section.getIdRole()));
                    dette.setIdSectionDetteEnCompta(Integer.valueOf(section.getIdSection()));
                    dette.setDescriptionCompteAnnexe(section.getDescriptionCompteAnnexe());
                    dette.setEtat(RELigneDeblocageEtat.ENREGISTRE);
                    dette.setIdSectionDetteEnCompta(Integer.valueOf(section.getIdSection()));
                    dette.setMontant(new Montant(section.getSolde()));
                    dette.setType(RELigneDeblocageType.DETTE_EN_COMPTA);
                    idsSection.add(section.getIdSection());
                    list.add(dette);
                }
                Map<Integer, String> descriptions = findDescription(idsSection);
                for (RELigneDeblocageDette dette : list) {
                    dette.setDescription(descriptions.get(dette.getIdSectionDetteEnCompta()));
                }
            } catch (Exception e1) {
                throw new RuntimeException("Unable to search the dette en compta ", e1);
            }
        }
        return list;
    }

    private Map<Integer, String> findDescription(Set<String> forIdsSectionIn) throws OsirisException {
        Map<Integer, String> descriptions = new HashMap<Integer, String>();

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
                    descriptions.put(Integer.valueOf(caSection.getId()), caSection.getDescription());
                }
            }
        }

        return descriptions;
    }
}
