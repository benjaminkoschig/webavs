/*
 * Globaz SA.
 */
package globaz.naos.db.controleLpp;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AFAffilieSoumiLppConteneur {

    /**
     * Classe interne
     */
    public class Salarie {
        String dateNaissance;
        String genreEcriture;
        String idCompteIndividuel;
        String idTiers;
        boolean isNegatif;
        boolean isSuivi;
        String moisDebut;
        String moisFin;
        int annee;
        String montant;
        String nivSecuAffilie;
        String nivSecuCI;
        String nom;
        String nss;
        String numeroAffilie;
        String sexe;
        String motif;

        public Salarie(String idTiers, String numeroAffilie, String motif, String nom, String sexe, String nss,
                String moisDebut, String moisFin, int annee, String dateNaissance, String genreEcriture, String idCI,
                String montant, boolean isExtourne, String nivSecuAffilie, String nivSecuCI) {
            this.nom = nom;
            this.nss = nss;
            this.sexe = sexe;
            this.numeroAffilie = numeroAffilie;
            if (moisDebut != null) {
                this.moisDebut = moisDebut.trim();
            }
            if (moisFin != null) {
                this.moisFin = moisFin.trim();
            }
            this.dateNaissance = dateNaissance;
            this.genreEcriture = genreEcriture;
            this.montant = montant;
            idCompteIndividuel = idCI;
            this.idTiers = idTiers;
            isNegatif = isExtourne;
            this.nivSecuAffilie = nivSecuAffilie;
            this.nivSecuCI = nivSecuCI;
            this.annee = annee;
            this.motif = motif;
        }

        public String getDateNaissance() {
            return dateNaissance;
        }

        public String getGenreEcriture() {
            return genreEcriture;
        }

        public String getIdCompteIndividuel() {
            return idCompteIndividuel;
        }

        public String getIdTiers() {
            return idTiers;
        }

        public String getMoisDebut() {
            return moisDebut;
        }

        public String getMoisFin() {
            return moisFin;
        }

        public String getMontant() {
            return montant;
        }

        public String getNivSecuAffilie() {
            return nivSecuAffilie;
        }

        public String getNivSecuCI() {
            return nivSecuCI;
        }

        public String getNom() {
            return nom;
        }

        public String getNss() {
            return nss;
        }

        public String getNumeroAffilie() {
            return numeroAffilie;
        }

        public String getSexe() {
            return sexe;
        }

        public boolean isNegatif() {
            return isNegatif;
        }

        public boolean isSuivi() {
            return isSuivi;
        }

        public void setDateNaissance(String dateNaissance) {
            this.dateNaissance = dateNaissance;
        }

        public void setGenreEcriture(String genreEcriture) {
            this.genreEcriture = genreEcriture;
        }

        public void setIdCompteIndividuel(String idCompteIndividuel) {
            this.idCompteIndividuel = idCompteIndividuel;
        }

        public void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public void setMoisDebut(String moisDebut) {
            this.moisDebut = moisDebut;
        }

        public void setMoisFin(String moisFin) {
            this.moisFin = moisFin;
        }

        public void setMontant(String montant) {
            this.montant = montant;
        }

        public void setNegatif(boolean isNegatif) {
            this.isNegatif = isNegatif;
        }

        public void setNivSecuAffilie(String nivSecuAffilie) {
            this.nivSecuAffilie = nivSecuAffilie;
        }

        public void setNivSecuCI(String nivSecuCI) {
            this.nivSecuCI = nivSecuCI;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setNss(String nss) {
            this.nss = nss;
        }

        public void setNumeroAffilie(String numeroAffilie) {
            this.numeroAffilie = numeroAffilie;
        }

        public void setSexe(String sexe) {
            this.sexe = sexe;
        }

        public void setSuivi(boolean isSuivi) {
            this.isSuivi = isSuivi;
        }

        public String getMotif() {
            return motif;
        }

        public void setMotif(String motif) {
            this.motif = motif;
        }

        public int getAnnee() {
            return annee;
        }

        public void setAnnee(int annee) {
            this.annee = annee;
        }
    }

    private Map<String, List<Salarie>> listeSalarieAffilie;

    /**
     * Constructeur
     */
    public AFAffilieSoumiLppConteneur() {
        listeSalarieAffilie = new HashMap<String, List<Salarie>>();
    }

    /**
     * Ajout d'un salarié dans la liste.
     * 
     * @param idAffilie
     * @param salarie
     */
    public void addSalarie(String idAffilie, Salarie salarie) {

        List<Salarie> listeSalarie = getListSalarieForAffilie(idAffilie);
        if (listeSalarie == null) {
            listeSalarie = new ArrayList<AFAffilieSoumiLppConteneur.Salarie>();
        }

        boolean ajouter = true;

        // On regarde le cas des extournes
        for (Salarie sal : listeSalarie) {
            if (!JadeStringUtil.isEmpty(sal.getNss()) && sal.getNss().equals(salarie.getNss())) {
                if (sal.getMoisDebut().equals(salarie.getMoisDebut()) && sal.getMoisFin().equals(salarie.getMoisFin())) {

                    FWCurrency salMontant = new FWCurrency(sal.getMontant());
                    FWCurrency salarieMontant = new FWCurrency(salarie.getMontant());

                    ajouter = false;

                    // On ajoute ou on retire
                    if (salarie.isNegatif() && !sal.isNegatif()) {
                        salMontant.sub(salarieMontant);
                    } else if (salarie.isNegatif() && sal.isNegatif()) {
                        salMontant.add(salarieMontant);
                    } else if (!salarie.isNegatif() && sal.isNegatif()) {
                        salMontant.sub(salarieMontant);
                    } else {
                        salMontant.add(salarieMontant);
                    }
                    sal.setMontant(salMontant.toString());

                    if (salMontant.isNegative()) {
                        sal.setNegatif(true);
                    } else if (salMontant.isZero()) {
                        listeSalarie.remove(sal);
                    }
                    break;
                }
            }
        }

        if (ajouter) {
            listeSalarie.add(salarie);
        }
        listeSalarieAffilie.put(idAffilie, listeSalarie);
    }

    /**
     * Ajout d'un salarié pour un affilié donné
     * 
     * @param idAffilie
     * @param nom
     * @param nss
     * @param moisDebut
     * @param moisFin
     * @param dateNaissance
     * @param genreEcriture
     * @param montant
     */
    public void addSalarie(String idTiers, String idAffilie, String numeroAffilie, String motif, String nom,
            String sexe, String nss, String moisDebut, String moisFin, int annee, String dateNaissance,
            String genreEcriture, String idCI, String montant, boolean isExtourne, String nivSecuAffilie,
            String nivSecuCI) {

        Salarie salarie = new Salarie(idTiers, numeroAffilie, motif, nom, sexe, nss, moisDebut, moisFin, annee,
                dateNaissance, genreEcriture, idCI, montant, isExtourne, nivSecuAffilie, nivSecuCI);

        this.addSalarie(idAffilie, salarie);
    }

    /***
     * Méthode pour ajouter un salarié avec les infos trouvé lors de l'injection de la liste.
     * 
     * @param idTiers
     * @param idAffilie
     * @param numeroAffilie
     * @param motif
     * @param nom
     * @param sexe
     * @param nss
     * @param moisDebut
     * @param moisFin
     * @param annee
     * @param dateNaissance
     * @param montant
     */
    public void addSalarieFromReinjection(String idTiers, String idAffilie, String numeroAffilie, String motif,
            String nom, String sexe, String nss, String moisDebut, String moisFin, int annee, String dateNaissance,
            String montant) {
        Salarie salarie = new Salarie(idTiers, numeroAffilie, motif, nom, sexe, nss, moisDebut, moisFin, annee,
                dateNaissance, "", "", montant, false, "", "");

        this.addSalarie(idAffilie, salarie);
    }

    public List<Salarie> getListSalarieForAffilie(String idAffilie) {
        if (listeSalarieAffilie.get(idAffilie) == null) {
            return null;
        }

        return listeSalarieAffilie.get(idAffilie);
    }

    public Map<String, List<Salarie>> getMapAffilie() {
        return listeSalarieAffilie;
    }

    /**
     * Retourne la liste des affiliés potentiellement soumis
     * 
     * @return
     */
    public String[] getTableauAffilie() {
        List<String> list = new ArrayList<String>(listeSalarieAffilie.keySet());
        return list.toArray(new String[list.size()]);
    }

    /**
     * Récupération des salariés pour un id affilié
     * 
     * @param idAffilie
     * @return
     */
    public Salarie[] getTableauSalarieForAffilie(String idAffilie) {
        if (listeSalarieAffilie.get(idAffilie) == null) {
            return null;
        }

        List<Salarie> listSalarie = listeSalarieAffilie.get(idAffilie);

        return listSalarie.toArray(new Salarie[listSalarie.size()]);
    }

    /**
     * Suppression d'un affilié de la liste
     * 
     * @param idAffilie
     */
    public void removeAffilie(String idAffilie) {
        listeSalarieAffilie.remove(idAffilie);
    }

    /**
     * Suppression d'un salarié pour un id affilié
     * 
     * @param idAffilie
     * @param salarie
     */
    public void removeSalarie(String idAffilie, Salarie salarie) {
        List<Salarie> listeSalarie = getListSalarieForAffilie(idAffilie);
        if (listeSalarie == null) {
            return;
        }

        listeSalarie.remove(salarie);
    }

    public void addAllSalarie(Map<String, List<Salarie>> affilies) {
        for (Entry<String, List<Salarie>> aff : affilies.entrySet()) {
            List<Salarie> sal = listeSalarieAffilie.get(aff.getKey());
            if (sal == null) {
                sal = aff.getValue();
            } else {
                sal.addAll(aff.getValue());
            }
            listeSalarieAffilie.put(aff.getKey(), sal);
        }
    }

    public void clear() {
        listeSalarieAffilie = new HashMap<String, List<Salarie>>();
    }
}
