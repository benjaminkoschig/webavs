package ch.globaz.vulpecula.process.caissemaladie;

import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class CaisseMaladieDecomptesProcess extends CaisseMaladieProcess {
    private CaisseMaladieParTravailleurExcel excelDoc;
    private static final long serialVersionUID = 1938382574142921185L;

    @Override
    protected void retrieve() {
        String query = "Select rub.IDEXTERNE as ID_EXTERNE, aff.MACONV as CONVENTION, aff.MALNAF as MALNAF, aff.MADESC as DESCRIPTION,poste.id as ID_POSTE, "
                + "avs.HXNAVS as NSS, tiers.htlde1 as NOM,tiers.htlde2 as PRENOM,code.PCOLUT as SEXE, ligne.salaire_total as total, cot.TAUX as TAUX, ligne.PERIODE_DEBUT as PERIODE_DEBUT, ligne.PERIODE_FIN as PERIODE_FIN "
                + "from SCHEMA.PT_DECOMPTES dec "
                + "inner join SCHEMA.PT_HISTORIQUE_DECOMPTES hist on hist.ID_PT_DECOMPTES=dec.id and hist.CS_ETAT=68012007 "
                + "inner join SCHEMA.PT_DECOMPTE_LIGNES ligne on dec.id=ligne.ID_PT_DECOMPTES "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on ligne.ID_PT_POSTES_TRAVAIL=poste.ID  "
                + "inner join SCHEMA.PT_TRAVAILLEURS tra on tra.id=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "inner join SCHEMA.tipavsp avs on tra.ID_TITIERP=avs.htitie "
                + "inner join SCHEMA.titierp tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join WEBAVSS.TIPERSP tipers on tiers.HTITIE=tipers.HTITIE "
                + "inner join WEBAVSS.fwcoup code on tipers.HPTSEX=code.PCOSID "
                + "inner join SCHEMA.PT_COTISATION_DECOMPTES cot on ligne.id=cot.ID_PT_DECOMPTE_LIGNES "
                + "inner join SCHEMA.afcotip coti on cot.ID_AFCOTIP=coti.MEICOT "
                + "inner join SCHEMA.afassup assu on coti.MBIASS=assu.mbiass "
                + "inner join SCHEMA.CARUBRP rub on assu.MBIRUB=rub.IDRUBRIQUE "
                // + "where malnaf ='0000006.00-01' and "
                // + "where rub.IDEXTERNE='7200.4030.0000' and "
                + "where hist.DATE between "
                + getDateAnnonceFrom()
                + " and "
                + getDateAnnonceTo()
                + " and assu.MBTTYP=68904007 " + " and code.PLAIDE='F' " + "order by aff.malnaf, avs.HXNAVS";

        List<LigneDecompteCMDTO> lignes = SCM.newInstance(LigneDecompteCMDTO.class).query(query).execute();
        Map<String, LigneDecompteCMDTO> map = new HashMap<String, LigneDecompteCMDTO>();

        for (LigneDecompteCMDTO ligne : lignes) {
            String convention = ligne.getConvention();
            String malnaf = ligne.getMalnaf();
            String idPoste = ligne.getIdPoste();
            String nss = ligne.getNss();
            String taux = ligne.getTaux();
            String periodeDebut = ligne.getPeriodeDebut();
            String l_periodeDebut = "N/A";
            String l_periodeFin = "N/A";
            String nomCM = "N/A";
            Date d_periodeDebut = new Date(periodeDebut);
            String anneePeriode = d_periodeDebut.getAnnee();

            /*
             * Pour chaque travailleur, retrouver les caisses maladies auxquelles il est ou a été assurées.
             */
            String queryCaisse = "SELECT ID as ID, ID_PT_POSTES_TRAVAILS AS ID_POSTE_TRAVAIL, admi.HTLDE1 as DESIGNATION,MOIS_DEBUT AS MOIS_DEBUT, MOIS_FIN as MOIS_FIN FROM SCHEMA.PT_CAISSES_MALADIES caisse "
                    + "INNER JOIN SCHEMA.TITIERP admi on caisse.ID_TIADMIP=admi.htitie where ID_PT_POSTES_TRAVAILS = "
                    + idPoste
                    + " AND (("
                    + anneePeriode
                    + " >= CAST(MOIS_DEBUT/100 as int) and MOIS_FIN=0) OR ("
                    + anneePeriode + " BETWEEN CAST(MOIS_DEBUT/100 as int) AND CAST(MOIS_FIN/100 as int)))";

            List<CaisseMaladieTempDTO> listcm = SCM.newInstance(CaisseMaladieTempDTO.class).query(queryCaisse)
                    .execute();

            /*
             * Pour chaque ligne, la caisse maladie les périodes de début et de fin de cotisation seront reprise pour
             * compléter les inforamtions de la/les caisse(s) maladie(s) de chaque travailleur
             */
            for (CaisseMaladieTempDTO cm : listcm) {
                nomCM = cm.getDesignation();
                l_periodeDebut = cm.getMoisDebut();
                l_periodeFin = cm.getMoisFin();
            }

            String groupKey = nss + "_" + taux + "_" + nomCM + "_" + malnaf;

            Convention conv = VulpeculaRepositoryLocator.getConventionRepository().findById(convention);
            ligne.setConvention(conv.getDesignation());
            if (!map.containsKey(groupKey)) {
                ligne.setCaisseMaladie(nomCM);
                ligne.setPeriodeDebut(l_periodeDebut);
                ligne.setPeriodeFin(l_periodeFin);
                map.put(groupKey, ligne);
            } else {
                LigneDecompteCMDTO dec = map.get(groupKey);
                Montant somme = new Montant(dec.getTotal());
                somme = somme.add(new Montant(ligne.getTotal()));
                dec.setTotal(somme.getValueNormalisee());
            }

        }

        writeFile(map);
    }

    /**
     * Appel de la méthode permettant de créer le fichier excel.
     * 
     * @param map Liste des caisses maladies par travailleur à ajouter dans le fichier Excel
     */
    private void writeFile(Map<String, LigneDecompteCMDTO> map) {
        excelDoc = new CaisseMaladieParTravailleurExcel(getSession(), DocumentConstants.LISTES_AMCAB_DOC_NAME,
                DocumentConstants.LISTES_AMCAB_NAME, map);
        excelDoc.create();
    }

    @Override
    protected void print() throws Exception {
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelDoc.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_AMCAB_NAME;
    }
}
