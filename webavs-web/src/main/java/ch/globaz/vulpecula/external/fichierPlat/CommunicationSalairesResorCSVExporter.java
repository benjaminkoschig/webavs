package ch.globaz.vulpecula.external.fichierPlat;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.fs.JadeFsFacade;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;
import ch.globaz.vulpecula.process.communicationsalaires.SalairesAAnnoncer;

public class CommunicationSalairesResorCSVExporter extends FichierPlatExporterAbstract {

    private static final CommunicationSalairesResorCSVExporter instance = new CommunicationSalairesResorCSVExporter();
    private static final String NUM_AFF_BMS_ENDING = "3";

    private static final String separator = ";";

    private static final String LIGNE_DETAIL_SALAIRE = "D";
    private static final int ID_CENTRE_ENCAISSEMENT = 3;

    private static final String CS_HOMME = "516001";
    private static final String HOMME = "M";
    private static final String FEMME = "F";

    private static final String ALLEMAND = "D";
    private static final String FRANCAIS = "F";
    private static final String ITALIEN = "I";
    private static final String ANGLAIS = "E";

    private static final String CS_ALLEMAND = "503002";
    private static final String CS_FRANCAIS = "503001";
    private static final String CS_ANGLAIS = "503003";
    private static final String CS_ITALIEN = "503004";

    private static final String CONVENTION_01 = "01";
    private static final String CONVENTION_02 = "02";
    private static final String CONVENTION_03 = "03";
    private static final String CONVENTION_04 = "04";
    private static final String CONVENTION_05 = "05";
    private static final String CONVENTION_12 = "12";
    private static final String CONVENTION_13 = "13";

    private static final String SECTEUR_01 = "0600";
    private static final String SECTEUR_02 = "0800";
    private static final String SECTEUR_03 = "0600";
    private static final String SECTEUR_04 = "1100";
    private static final String SECTEUR_05 = "0400";
    private static final String SECTEUR_12 = "1400";
    private static final String SECTEUR_13 = "9000";

    private CommunicationSalairesResorCSVExporter() {
    }

    public static void export(List<SalairesAAnnoncer> listeSalairesAAnnoncer, String exportDirectoryPath,
            String fileName, Annee annee) throws Exception {
        if (JadeFsFacade.exists(exportDirectoryPath + fileName)) {
            JadeFsFacade.delete(exportDirectoryPath + fileName);
        }
        String content = CommunicationSalairesResorCSVExporter.createLines(listeSalairesAAnnoncer, annee);
        instance.createExportFile(content, exportDirectoryPath, fileName);
    }

    private static String limiterLongueur(String chaine, int longueurMax) {
        if (chaine.length() <= longueurMax) {
            return chaine;
        } else {
            return chaine.substring(0, longueurMax);
        }
    }

    private static String createLines(List<SalairesAAnnoncer> listeSalairesAAnnoncer, Annee annee) {
        StringBuilder lines = new StringBuilder();

        int i = 0;
        int compteur = 0;
        Montant montantTotal = Montant.ZERO;
        for (SalairesAAnnoncer salairesAAnnoncer : listeSalairesAAnnoncer) {
            if (salairesAAnnoncer.getListeDecomptes() != null && salairesAAnnoncer.getListeDecomptes().size() > 0) {
                for (DecompteSalaire salaire : salairesAAnnoncer.getListeDecomptes()) {
                    if (!salaire.getSalaireTotal().isZero()) {
                        compteur++;
                        if (i != 0) {
                            lines.append(NEW_LINE);
                        }
                        i++;
                        // Type de record l1 (D=ligne détail salaire)
                        lines.append(LIGNE_DETAIL_SALAIRE);
                        lines.append(separator);
                        // Id centre encaissement l3 (BMS = 3)
                        lines.append(ID_CENTRE_ENCAISSEMENT);
                        lines.append(separator);
                        // NSS non formatée l13
                        lines.append(getNSS(salaire.getTravailleur().getNumAvsActuel()));
                        lines.append(separator);
                        // NSS non formatée l11, pas d'historique au BMS
                        // lines.append(salaire.getTravailleur().getAncienNumAvs());
                        lines.append(separator);
                        // Nom l40
                        lines.append(limiterLongueur(salaire.getTravailleur().getDesignation1(), 40));
                        lines.append(separator);
                        // Prénom l40
                        lines.append(limiterLongueur(salaire.getTravailleur().getDesignation2(), 40));
                        lines.append(separator);
                        // Date de naissance l8
                        if (!JadeStringUtil.isEmpty(salaire.getTravailleur().getDateNaissance())) {
                            Date dateNaissance = new Date(salaire.getTravailleur().getDateNaissance());
                            lines.append(dateNaissance.getAnnee() + dateNaissance.getMois() + dateNaissance.getJour());
                        }
                        lines.append(separator);
                        // Sexe l1 (M ou F)
                        if (salaire.getTravailleur().getSexe().equals(CS_HOMME)) {
                            lines.append(HOMME);
                        } else {
                            lines.append(FEMME);
                        }
                        lines.append(separator);
                        // Langue l1 (D,F,E,I)
                        if (salaire.getTravailleur().getLangue().equals(CS_FRANCAIS)) {
                            lines.append(FRANCAIS);
                        } else if (salaire.getTravailleur().getLangue().equals(CS_ALLEMAND)) {
                            lines.append(ALLEMAND);
                        } else if (salaire.getTravailleur().getLangue().equals(CS_ANGLAIS)) {
                            lines.append(ANGLAIS);
                        } else if (salaire.getTravailleur().getLangue().equals(CS_ITALIEN)) {
                            lines.append(ITALIEN);
                        }
                        lines.append(separator);
                        // Nationalité l3 (Code ISO)
                        lines.append(salaire.getTravailleur().getPays().getCodeIso());
                        lines.append(separator);
                        // Nom de l'employeur l60
                        lines.append(limiterLongueur(salaire.getEmployeur().getRaisonSociale(), 100));
                        lines.append(separator);
                        // Secteur professionnel l4
                        if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_01)) {
                            lines.append(SECTEUR_01);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_02)) {
                            lines.append(SECTEUR_02);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_03)) {
                            lines.append(SECTEUR_03);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_04)) {
                            lines.append(SECTEUR_04);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_05)) {
                            lines.append(SECTEUR_05);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_12)) {
                            lines.append(SECTEUR_12);
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_13)) {
                            lines.append(SECTEUR_13);
                        }
                        lines.append(separator);
                        // Début activité l8 (AAAAMMJJ)
                        Date dateDebut = null;
                        if (!JadeStringUtil.isBlankOrZero(salaire.getEmployeur().getDateDebut())
                                && (!salaire.getEmployeur().getDateDebut().equalsIgnoreCase("null"))) {
                            dateDebut = new Date(salaire.getEmployeur().getDateDebut());
                            lines.append(dateDebut.getAnnee() + dateDebut.getMois() + dateDebut.getJour());
                        }
                        lines.append(separator);
                        // Fin activité l8 (AAAAMMJJ)
                        Date dateFin = null;
                        if (!JadeStringUtil.isBlankOrZero(salaire.getEmployeur().getDateFin())
                                && (!salaire.getEmployeur().getDateDebut().equalsIgnoreCase("null"))) {
                            dateFin = new Date(salaire.getEmployeur().getDateFin());
                            lines.append(dateFin.getAnnee() + dateFin.getMois() + dateFin.getJour());
                        }
                        lines.append(separator);
                        // Taux occupation l3
                        List<Occupation> tauxOccupations = VulpeculaRepositoryLocator.getOccupationRepository()
                                .findOccupationsByIdPosteTravail(salaire.getIdPosteTravail());
                        Occupation taux = null;
                        for (Occupation occupation : tauxOccupations) {
                            if (dateDebut != null) {
                                if (occupation.getDateValidite().afterOrEquals(dateDebut)) {
                                    if (dateFin != null) {
                                        if (occupation.getDateValidite().before(dateFin)) {
                                            taux = occupation;
                                            break;
                                        }
                                    } else {
                                        taux = occupation;
                                        break;
                                    }
                                }
                            }
                        }
                        if (taux != null) {
                            String tauxAsString = taux.getTaux().getValueWith(0);
                            tauxAsString = tauxAsString.substring(0, tauxAsString.indexOf('.'));
                            if (tauxAsString.length() > 3) {
                                tauxAsString = tauxAsString.substring(0, 3);
                            }
                            lines.append(tauxAsString);
                        }
                        lines.append(separator);
                        // Profession l4
                        if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_01)) {
                            // Apprenti
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Charpentier qualifié
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007013")) {
                                lines.append("0602");
                            }
                            // Décorateur intérieur
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007014")) {
                                lines.append("0616");
                            }
                            // Ebéniste qualifié
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007021")) {
                                lines.append("0605");
                            }
                            // Etudiant
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0006");
                            }
                            // Femme de ménage 68007006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Manœuvre 68007009
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("0009");
                            }
                            // Menuisier qualifié 68007015
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007015")) {
                                lines.append("0607");
                            }
                            // Machiniste spécialisé 68007016
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007016")) {
                                lines.append("0697");
                            }
                            // Personnel administratif 68007010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0699");
                            }
                            // Parqueteur qualifié 68007017
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007017")) {
                                lines.append("0702");
                            }
                            // Personnel technique 68007011
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0698");
                            }
                            // Personnel de vente 68007018
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007018")) {
                                lines.append("0699");
                            }
                            // Travailleur semi-qualifié 68007019
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007019")) {
                                lines.append("0697");
                            }
                            // Travailleur qualifié 68007012
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("0697");
                            }
                            // Vitrier qualifié 68007020
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007020")) {
                                lines.append("0697");
                            }
                            // Indépendant 68007007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007007")) {
                                lines.append("0697");
                            }
                            // Stagiaire 68007050
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_02)) {
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Etudiant 68007003 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0006");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Manœuvre 68007009 0009
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("0009");
                            }
                            // Personnel administratif 68007010 0899
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0899");
                            }
                            // Plâtrier 68007022 0802
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007022")) {
                                lines.append("0802");
                            }
                            // Plâtrier peintre 68007023 0803
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007023")) {
                                lines.append("0803");
                            }
                            // Parqueteur qualifié 68007017 0702
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007017")) {
                                lines.append("0702");
                            }
                            // Personnel technique 68007011 0898
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0898");
                            }
                            // Travailleur qualifié 68007012 0897
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("0897");
                            }
                            // Indépendant 68007007 0897
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007007")) {
                                lines.append("0897");
                            }
                            // Ouvrier B 68007059 0897
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007059")) {
                                lines.append("0897");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_03)) {
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Chef monteur et monteur A 68007002 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007002")) {
                                lines.append("0005");
                            }
                            // Etudiant 68007003 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0006");
                            }
                            // Ferblantier 68007004 0502
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007004")) {
                                lines.append("0502");
                            }
                            // Ferblantier-installateur sanitaire 68007005 0503
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007005")) {
                                lines.append("0503");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Indépendant 68007007 0503
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007007")) {
                                lines.append("0503");
                            }
                            // Installateur sanitaire 68007008 1001
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007008")) {
                                lines.append("1001");
                            }
                            // Manœuvre 68007009 0009
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("0009");
                            }
                            // Personnel technique 68007011 0598
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0598");
                            }
                            // Personnel administratif 68007010 0599
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0599");
                            }
                            // Travailleur qualifié 68007012 0503
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("0503");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_04)) {
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Etudiant 68007003 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0005");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Indépendant 68007007 1101
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007007")) {
                                lines.append("1101");
                            }
                            // Installateur sanitaire 68007008 1001
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007008")) {
                                lines.append("1001");
                            }
                            // Manœuvre 68007009 0009
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("0009");
                            }
                            // Personnel administratif 68007010 0011
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0011");
                            }
                            // Personnel technique 68007011 0012
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0012");
                            }
                            // Soudeur 68007024 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007024")) {
                                lines.append("0010");
                            }
                            // Travailleur qualifié 68007012 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("0010");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_05)) {
                            // Travailleur qualifié 68007012 0403
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("0403");
                            }
                            // Monteur électricien 68007029 0403
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007029")) {
                                lines.append("0403");
                            }
                            // Aide-monteur électricien 68007025 0001
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007025")) {
                                lines.append("0001");
                            }
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Contrôleur avec brevet fédéral 68007026 0003
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007026")) {
                                lines.append("0003");
                            }
                            // Chef de chantier 68007027 0004
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007027")) {
                                lines.append("0004");
                            }
                            // Chef monteur et monteur A 68007002 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007002")) {
                                lines.append("0005");
                            }
                            // Dessinateur-électricien 68007028 0401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007028")) {
                                lines.append("0401");
                            }
                            // Etudiant 68007003 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0005");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Manœuvre 68007009 0009
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("0009");
                            }
                            // Contremaître avec maîtrise fédérale 68007030 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007030")) {
                                lines.append("0010");
                            }
                            // Monteur de ligne (sans CFC) 68007031 0404
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007031")) {
                                lines.append("0404");
                            }
                            // Personnel administratif 68007010 0011
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0011");
                            }
                            // Personnel technique 68007011 0012
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0012");
                            }
                            // Spécialiste télécom (télématicien) 68007032 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007032")) {
                                lines.append("0010");
                            }
                            // Soudeur 68007024 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007024")) {
                                lines.append("0010");
                            }
                            // Monteur en tableau 68007058 0010
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007058")) {
                                lines.append("0010");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_12)) {
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Manœuvre 68007009 1402
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007009")) {
                                lines.append("1402");
                            }
                            // Paysagiste 68007034 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007034")) {
                                lines.append("1401");
                            }
                            // Travailleur semi-qualifié 68007019 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007019")) {
                                lines.append("1401");
                            }
                            // Travailleur débutant 68007035 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007035")) {
                                lines.append("1401");
                            }
                            // Travailleur qualifié 68007012 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("1401");
                            }
                            // Travailleur remplaçant 68007044 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007044")) {
                                lines.append("1401");
                            }
                            // Travailleur < 22 heures 68007045 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007045")) {
                                lines.append("1401");
                            }
                            // Travailleur sans qualif. Ayant 4 ans 68007046 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007046")) {
                                lines.append("1401");
                            }
                            // Contremaître 68007033 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007033")) {
                                lines.append("1401");
                            }
                            // Etudiant 68007003 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0005");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Personnel administratif 68007010 1499
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("1499");
                            }
                            // Personnel technique 68007011 1498
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("1498");
                            }
                            // Chef d'équipe 68007056 1401
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007056")) {
                                lines.append("1401");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        } else if (salaire.getEmployeur().getConvention().getCode().equals(CONVENTION_13)) {
                            // Apprenti 68007001 0002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007001")) {
                                lines.append("0002");
                            }
                            // Décorateur intérieur 68007014 9002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007014")) {
                                lines.append("9002");
                            }
                            // Travailleur débutant 68007035 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007035")) {
                                lines.append("9099");
                            }
                            // Travailleur interimaire autre 68007036 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007036")) {
                                lines.append("9099");
                            }
                            // Travailleur interimaire constr. Métallique 68007037 9004
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007037")) {
                                lines.append("9004");
                            }
                            // Travailleur interimaire électricien 68007039 9004
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007039")) {
                                lines.append("9004");
                            }
                            // Travailleur interimaire ferbl.-chauffage 68007038 9004
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007038")) {
                                lines.append("9004");
                            }
                            // Travailleur interimaire menuisier 68007040 9002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007040")) {
                                lines.append("9002");
                            }
                            // Travailleur interimaire nettoyeur 68007041 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007041")) {
                                lines.append("9099");
                            }
                            // Travailleur interimaire peintre 68007042 9001
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007042")) {
                                lines.append("9001");
                            }
                            // Travailleur interimaire paysagiste 68007043 9005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007043")) {
                                lines.append("9005");
                            }
                            // Travailleur qualifié 68007012 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007012")) {
                                lines.append("9099");
                            }
                            // Travailleur remplaçant 68007044 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007044")) {
                                lines.append("9099");
                            }
                            // Travailleur < 22 heures 68007045 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007045")) {
                                lines.append("9099");
                            }
                            // Travailleur sans qualif. Ayant 4 ans 68007046 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007046")) {
                                lines.append("9099");
                            }
                            // Etudiant 68007003 0005
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007003")) {
                                lines.append("0005");
                            }
                            // Femme de ménage 68007006 0007
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007006")) {
                                lines.append("0007");
                            }
                            // Indépendant 68007007 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007007")) {
                                lines.append("9099");
                            }
                            // Personnel technique 68007011 0012
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007011")) {
                                lines.append("0012");
                            }
                            // Personnel administratif 68007010 0011
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007010")) {
                                lines.append("0011");
                            }
                            // Personnel de vente 68007018 0011
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007018")) {
                                lines.append("0011");
                            }
                            // Chef d'équipe 68007056 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007056")) {
                                lines.append("9099");
                            }
                            // Sellier 68007057 9099
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007057")) {
                                lines.append("9099");
                            }
                            // Courtepointière 68007051 9002
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007051")) {
                                lines.append("9002");
                            }
                            // Stagiaire 68007050 0006
                            if (salaire.getPosteTravail().getQualification().getValue().equals("68007050")) {
                                lines.append("0006");
                            }
                        }
                        lines.append(separator);
                        // Catégorie de personnel l3
                        boolean isNCR = false;
                        if (salaire.getPosteTravail().getParametresQualifications().size() > 0) {
                            ConventionQualification qualification = salaire.getPosteTravail()
                                    .getParametresQualifications().get(0);
                            Personnel personnel = qualification.getPersonnel();
                            if (personnel == Personnel.ADMINISTRATIF) {
                                isNCR = true;
                            } else if (personnel == Personnel.EXPLOITATION) {
                                isNCR = false;
                            }
                        }

                        if (isNCR) {
                            lines.append("NCR");
                        } else {
                            lines.append("CRA");
                        }
                        lines.append(separator);
                        // Date d'entrée RESOR l8(AAAAMMJJ)
                        if (salaire.getPosteTravail().getAdhesionsCotisations().size() > 0) {
                            AdhesionCotisationPosteTravail adh = salaire.getPosteTravail().getAdhesionsCotisations()
                                    .get(0);
                            if (adh.getPeriode().getDateDebut() != null) {
                                lines.append(adh.getPeriode().getDateDebut().getAnnee()
                                        + adh.getPeriode().getDateDebut().getMois()
                                        + adh.getPeriode().getDateDebut().getJour());
                            }
                        }
                        lines.append(separator);
                        // Date de sortie RESOR l8(AAAAMMJJ)
                        if (salaire.getPosteTravail().getAdhesionsCotisations().size() > 0) {
                            AdhesionCotisationPosteTravail adh = salaire.getPosteTravail().getAdhesionsCotisations()
                                    .get(0);
                            if (adh.getPeriode().getDateFin() != null) {
                                lines.append(adh.getPeriode().getDateFin().getAnnee()
                                        + adh.getPeriode().getDateFin().getMois()
                                        + adh.getPeriode().getDateFin().getJour());
                            }
                        }
                        lines.append(separator);
                        // Genre de salaire l1 (M/H)
                        if (salaire.getPosteTravail().getTypeSalaire().getValue().equals(TypeSalaire.HEURES.getValue())) {
                            lines.append("H");
                        } else if (salaire.getPosteTravail().getTypeSalaire().getValue()
                                .equals(TypeSalaire.MOIS.getValue())) {
                            lines.append("M");
                        } else if (salaire.getPosteTravail().getTypeSalaire().getValue()
                                .equals(TypeSalaire.CONSTANT.getValue())) {
                            lines.append("C");
                        }
                        lines.append(separator);
                        // Source du salaire par défaut E (01)
                        lines.append("01");
                        lines.append(separator);
                        // Période salaire
                        // Mois de début 81-83 l8
                        lines.append(salaire.getPeriodeDebut().getAnnee() + salaire.getPeriodeDebut().getMois()
                                + salaire.getPeriodeDebut().getJour());
                        lines.append(separator);
                        // Mois de fin 83-85 l8
                        lines.append(salaire.getPeriodeFin().getAnnee() + salaire.getPeriodeFin().getMois()
                                + salaire.getPeriodeFin().getJour());
                        lines.append(separator);
                        // Année 85-89 l4
                        lines.append(annee);
                        lines.append(separator);
                        // Montant avec point l8 précision 2
                        montantTotal = montantTotal.add(salaire.getSalaireTotal());
                        lines.append(salaire.getSalaireTotalAsValue().replace('.', ','));
                        lines.append(separator);
                        // Id historique sal.
                        lines.append(separator);
                        // Nombre enregistrements
                        lines.append(separator);
                        // Total salaires
                        // lines.append(separator);
                    }

                }

            }

        }
        lines.append(NEW_LINE);
        // Type Record
        lines.append("T");
        lines.append(separator);
        // Id centre encaissement l3 (BMS = 3)
        lines.append(ID_CENTRE_ENCAISSEMENT);
        lines.append(separator);
        for (int j = 0; j < 24; j++) {
            // Zones pour le détail des lignes
            lines.append(separator);
        }
        // Nombre d'enregistrements l7
        lines.append(compteur);
        lines.append(separator);
        // Total des salaires l11 précision 2
        lines.append(montantTotal.toString().replace('.', ','));
        return lines.toString();
    }

    private static String getNumeroAffilieFormate(String numeroAffilie) {
        // On supprime les . et les -
        numeroAffilie = numeroAffilie.replace(".", "").replace("-", "");
        // On remplace les 2 derniers chiffres par l'extension BMS -3
        numeroAffilie = numeroAffilie.substring(0, numeroAffilie.length() - 2);
        numeroAffilie = numeroAffilie + NUM_AFF_BMS_ENDING;
        return numeroAffilie;
    }

    private static String getNumeroAVS(String numeroAvs) {
        if (!JadeStringUtil.isEmpty(numeroAvs)) {
            numeroAvs = numeroAvs.replace(".", "");
        } else {
            numeroAvs = "";
        }
        return numeroAvs;
    }

    private static String getNSS(String numeroAvs) {
        if (!JadeStringUtil.isEmpty(numeroAvs) && numeroAvs.length() > 3) {
            numeroAvs = numeroAvs.replace(".", "");
        } else {
            numeroAvs = "";
        }
        return numeroAvs;
    }

    private static String getNomPrenom(String designation1, String designation2) {
        String designation = designation1 + " " + designation2;
        if (designation.length() > 60) {
            designation = designation.substring(0, 60);
        }
        return designation;
    }

    private static String getNomPrenom(String designation) {
        if (designation.length() > 60) {
            designation = designation.substring(0, 60);
        }
        return designation;
    }
}
