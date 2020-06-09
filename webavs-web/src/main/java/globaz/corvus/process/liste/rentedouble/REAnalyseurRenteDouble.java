package globaz.corvus.process.liste.rentedouble;

import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteJoinPersonneAvs;

import java.math.BigDecimal;
import java.util.*;

import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.PrestationAccordee;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;
import ch.globaz.prestation.domaine.constantes.TypePrestationComplementaire;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Sexe;

public class REAnalyseurRenteDouble {

    private class PrestationsAccordeesPourUnePersonne {

        PersonneAVS beneficiaire;


        Map<PrestationAccordee, RERenteJoinPersonneAvs> entiteBDDpourEntiteDomaine;

        public PrestationsAccordeesPourUnePersonne() {
            beneficiaire = new PersonneAVS();
            entiteBDDpourEntiteDomaine = new HashMap<PrestationAccordee, RERenteJoinPersonneAvs>();
        }

        public Set<PrestationAccordee> getPrestationsAccordees() {
            return entiteBDDpourEntiteDomaine.keySet();
        }

        public Collection<RERenteJoinPersonneAvs> getEntitesBDD() {
            return entiteBDDpourEntiteDomaine.values();
        }

        @Override
        public int hashCode() {
            return beneficiaire.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PrestationsAccordeesPourUnePersonne) {
                return beneficiaire.equals(((PrestationsAccordeesPourUnePersonne) obj).beneficiaire);
            }
            return false;
        }
        public Map<PrestationAccordee, RERenteJoinPersonneAvs> getEntiteBDDpourEntiteDomaine() {
            return entiteBDDpourEntiteDomaine;
        }

        public void setEntiteBDDpourEntiteDomaine(Map<PrestationAccordee, RERenteJoinPersonneAvs> entiteBDDpourEntiteDomaine) {
            this.entiteBDDpourEntiteDomaine = entiteBDDpourEntiteDomaine;
        }

    }

    public REAnalyseurRenteDouble() {
        super();
    }

    int getNombreRenteAVS(Set<PrestationAccordee> rentesDuTiers) {
        int nbRentesAVS = 0;

        for (PrestationAccordee uneRenteDuTiers : rentesDuTiers) {
            switch (uneRenteDuTiers.getCodePrestation().getDomaineCodePrestation()) {
                case AI:
                case SURVIVANT:
                case VIEILLESSE:
                    nbRentesAVS++;
                    break;

                default:
                    break;
            }
        }

        return nbRentesAVS;
    }

    public List<RERenteJoinPersonneAvs> getRentesDoubles(List<RERenteJoinPersonneAvs> rentesAAnalyser) {
        Set<PrestationsAccordeesPourUnePersonne> prestationsAccordeesParBeneficiaire = regrouperPrestationsAccordeesParBeneficiaire(rentesAAnalyser);

        List<RERenteJoinPersonneAvs> rentesDoubles = new ArrayList<RERenteJoinPersonneAvs>();

        for (PrestationsAccordeesPourUnePersonne prestationsAccordeesPourUnePersonne : prestationsAccordeesParBeneficiaire) {

            Set<PrestationAccordee> prestationsAccordeesDuTiers = prestationsAccordeesPourUnePersonne
                    .getPrestationsAccordees();
            if (getNombreRenteAVS(prestationsAccordeesDuTiers) > 1 ) {
                List<PrestationAccordee> list = RenteEnfantsADoubleLPART(prestationsAccordeesDuTiers);
                for(PrestationAccordee prest : list){
                    rentesDoubles.add(prestationsAccordeesPourUnePersonne.getEntiteBDDpourEntiteDomaine().get(prest));
                }
            }

            if (hasDeuxPC(prestationsAccordeesDuTiers) || hasDeuxRFM(prestationsAccordeesDuTiers)
                    || hasDeuxAPI(prestationsAccordeesDuTiers)) {
                rentesDoubles.addAll(prestationsAccordeesPourUnePersonne.getEntitesBDD());
                continue;
            }

            if (getNombreRenteAVS(prestationsAccordeesDuTiers) > 2) {
                rentesDoubles.addAll(prestationsAccordeesPourUnePersonne.getEntitesBDD());
                continue;
            }

            if ((getNombreRenteAVS(prestationsAccordeesDuTiers) == 2) && !hasRX4etRX5(prestationsAccordeesDuTiers)) {
                rentesDoubles.addAll(prestationsAccordeesPourUnePersonne.getEntitesBDD());
            }
        }

        return rentesDoubles;
    }

    private  List<PrestationAccordee>  RenteEnfantsADoubleLPART(Set<PrestationAccordee> prestationsAccordeesDuTiers) {
        PrestationAccordee pa1;
        PrestationAccordee pa2;
        HashMap<PrestationAccordee,String> listPrestationsADoubles = new HashMap<>();
        List<PrestationAccordee> prestationAccordeesList = new ArrayList<>();
        List<PrestationAccordee> listPrestations = new LinkedList<>(prestationsAccordeesDuTiers);
        for (int i = 0; i < listPrestations.size(); i++) {
            pa1 = listPrestations.get(i);
            for (int j = 1; j < listPrestations.size(); j++) {
                pa2 = listPrestations.get(j);
                if ((pa2.isHasCodeSpecial60()==true) && (pa1.isHasCodeSpecial60()==true) && pa1.getId() != pa2.getId()) {
                    if (pa1.getCodePrestation().isRenteComplementairePourEnfant() && pa1.getCodePrestation().isRenteComplementairePourEnfant()
                            && pa1.getIdTiersNssCompl1().equals(pa2.getIdTiersNssCompl1())) {
                        listPrestationsADoubles.put(pa1,"");
                        listPrestationsADoubles.put(pa2,"");
                    }
                }
            }
        }
        prestationAccordeesList.addAll(listPrestationsADoubles.keySet());
        return prestationAccordeesList;
    }


    private Set<PrestationsAccordeesPourUnePersonne> regrouperPrestationsAccordeesParBeneficiaire(
            List<RERenteJoinPersonneAvs> rentes) {
        Map<PersonneAVS, PrestationsAccordeesPourUnePersonne> rentesParTiers = new HashMap<PersonneAVS, REAnalyseurRenteDouble.PrestationsAccordeesPourUnePersonne>();

        for (RERenteJoinPersonneAvs uneRente : rentes) {

            PrestationAccordee prestationAccordee = new PrestationAccordee();
            prestationAccordee.setId(Long.parseLong(uneRente.getIdPrestationAccordee()));
            prestationAccordee.setCodePrestation(CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                    .getCodePrestation())));
            prestationAccordee.setMoisDebut(uneRente.getDateDebutDroit());
            prestationAccordee.setMoisFin(uneRente.getDateFinDroit());
            prestationAccordee.setMontant(new BigDecimal(uneRente.getMontantPrestation()));

            PersonneAVS beneficiaireRente = new PersonneAVS();
            beneficiaireRente.setId(Long.parseLong(uneRente.getIdTiers()));
            beneficiaireRente.setNss(new NumeroSecuriteSociale(uneRente.getNssBeneficiaire()));
            beneficiaireRente.setNom(uneRente.getNomBeneficiaire());
            beneficiaireRente.setPrenom(uneRente.getPrenomBeneficiaire());
            try {
                beneficiaireRente.setSexe(Sexe.parse(uneRente.getCsSexeBeneficiaire()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Le tiers [" + uneRente.getIdTiers() + "] avecle NSS ["
                        + uneRente.getNssBeneficiaire() + "] de la rente [" + uneRente.getId()
                        + "] possède un code système sexe invalide [" + uneRente.getCsSexeBeneficiaire() + "]");
            }
            beneficiaireRente.setDateNaissance(uneRente.getDateNaissanceBeneficiaire());

            prestationAccordee.setBeneficiaire(beneficiaireRente);

            if (uneRente.contientCodeSpecial("60")) {
                prestationAccordee.setHasCodeSpecial60(true);
                prestationAccordee.setIdTiersNssCompl1(uneRente.getIdTiersNssCompl1());
                prestationAccordee.setIdTiersNssCompl2(uneRente.getIdTiersNssCompl2());
            }

            if (rentesParTiers.containsKey(beneficiaireRente)) {
                rentesParTiers.get(beneficiaireRente).entiteBDDpourEntiteDomaine.put(prestationAccordee, uneRente);
            } else {
                PrestationsAccordeesPourUnePersonne rentesPourCeTiers = new PrestationsAccordeesPourUnePersonne();
                rentesPourCeTiers.beneficiaire = beneficiaireRente;
                rentesPourCeTiers.entiteBDDpourEntiteDomaine.put(prestationAccordee, uneRente);

                rentesParTiers.put(beneficiaireRente, rentesPourCeTiers);
            }
        }

        return new HashSet<REAnalyseurRenteDouble.PrestationsAccordeesPourUnePersonne>(rentesParTiers.values());
    }

    boolean hasDeuxAPI(Set<PrestationAccordee> rentesDuTiers) {
        return hasDeuxRenteDesDomaines(rentesDuTiers, DomaineCodePrestation.API);
    }

    boolean hasDeuxPC(Set<PrestationAccordee> rentesDuTiers) {
        return hasDeuxRenteDesDomaines(rentesDuTiers, DomaineCodePrestation.PRESTATION_COMPLEMENTAIRE);
    }

    private boolean hasDeuxRenteDesDomaines(Set<PrestationAccordee> rentesDuTiers, DomaineCodePrestation... domaines) {
        Set<DomaineCodePrestation> setDomaines = new HashSet<DomaineCodePrestation>(Arrays.asList(domaines));
        int nbRenteDuGroupe = 0;
        int nbPcStandard = 0;
        int nbPcAllocationDeNoel = 0;

        for (PrestationAccordee uneRenteDuTiers : rentesDuTiers) {
            CodePrestation codePrestation = uneRenteDuTiers.getCodePrestation();

            if (setDomaines.contains(codePrestation.getDomaineCodePrestation())) {
                if (DomaineCodePrestation.PRESTATION_COMPLEMENTAIRE.equals(codePrestation.getDomaineCodePrestation())) {

                    if (TypePrestationComplementaire.ALLOCATION_DE_NOEL.equals(codePrestation.getTypePC())) {
                        nbPcAllocationDeNoel++;
                    } else {
                        nbPcStandard++;
                    }
                } else {
                    nbRenteDuGroupe++;
                }
            }
        }

        return (nbRenteDuGroupe == 2) || (nbPcStandard == 2) || (nbPcAllocationDeNoel == 2);
    }

    boolean hasDeuxRentesAVS(Set<PrestationAccordee> rentesDuTiers) {
        return hasDeuxRenteDesDomaines(rentesDuTiers, DomaineCodePrestation.AI, DomaineCodePrestation.SURVIVANT,
                DomaineCodePrestation.VIEILLESSE);
    }

    boolean hasDeuxRFM(Set<PrestationAccordee> rentesDuTiers) {
        return hasDeuxRenteDesDomaines(rentesDuTiers, DomaineCodePrestation.REMBOURSEMENT_FRAIS_MEDICAUX);
    }

    boolean hasRX4etRX5(Set<PrestationAccordee> rentesDuTiers) {
        if (hasDeuxRentesAVS(rentesDuTiers)) {
            boolean hasRenteX4 = false;
            boolean hasRenteX5 = false;

            for (PrestationAccordee uneRenteDuTiers : rentesDuTiers) {

                if (uneRenteDuTiers.getCodePrestation().isRentesComplementairePourEnfantsLieesRenteDeLaMere() && uneRenteDuTiers.isHasCodeSpecial60() ==false ) {
                    hasRenteX4 = true;
                }
                if (uneRenteDuTiers.getCodePrestation().isRentesComplementairePourEnfantsLieesRenteDuPere()  && uneRenteDuTiers.isHasCodeSpecial60() ==false ) {
                    hasRenteX5 = true;
                }

                if (hasRenteX4 && hasRenteX5) {
                    return true;
                }
            }
        }
        return false;
    }
}
