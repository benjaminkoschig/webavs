package globaz.corvus.utils.adaptation.rentes;

import acor.ch.admin.zas.rc.annonces.rente.rc.ELRueckMeldungType;
import globaz.corvus.db.annonces.REAnnonce61;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class REAnnonces61Mapper {

    public REAnnonce61 createAnnonce61 (ELRueckMeldungType rrELRueckMeldungType, String dateAnnonce) {
        REAnnonce61 ann61 = new REAnnonce61();

        // 1 | Code application: 61
        ann61.setCodeApplication("61");

        // 2 | Code enregistrement: 01
        ann61.setCodeEnregistrement01("01");

        // 3 | Numéro de la Caisse
        ann61.setNumeroCaisse(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getELStelleZweigstelle()).substring(0,3));

        // 4 | Numéro de l'agence
        ann61.setNumeroAgence(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getELStelleZweigstelle()).substring(3,6));

        // 5 | Numéro de l'annonce
//        ann61.setNumeroAnnonce(PRConverterUtils.formatLongToString(rrELRueckMeldungType.));

        // 6 | Référence interne de la Caisse
        ann61.setReferenceCaisseInterne(rrELRueckMeldungType.getInternerHinweisELStelle());

        // 7 | Canton de résidence
        ann61.setCantonEtatDomicile(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getWohnkantonStaat()));

        // 8 | Date d'annonce
        ann61.setDateAnnonce(dateAnnonce);


        ////

        // Nss de l'assuré
        ann61.setNss(rrELRueckMeldungType.getVNrLeistungsberechtigtePerson());

        //
        ann61.setNouveauMontant(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getMonatsbetragNeu()));

        //
        ann61.setGenrePrestation(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getLeistungsart()));

        //
        ann61.setDebutDroit(PRConverterUtils.formatDateToMMAA(rrELRueckMeldungType.getAnspruchsbeginn()));

        //
//        ann61.setDateRapport(PRConverterUtils.formatDateToMMAA(rrELRueckMeldungType.getBerichtsmonat()));
        ann61.setMoisRapport(PRConverterUtils.formatDateToMMAA(rrELRueckMeldungType.getBerichtsmonat()));

        //
        ann61.setCodeRetour(rrELRueckMeldungType.getVerarbeitungscode());

        //
        ann61.setDegreInvalidite(PRConverterUtils.formatShortToString(rrELRueckMeldungType.getInvaliditaetsgrad()));

        //
        ann61.setObservation(rrELRueckMeldungType.getBemerkungenZAS());

        //
        ann61.setAncienMontant(PRConverterUtils.formatIntegerToString(rrELRueckMeldungType.getMonatsbetragAlt()));

        //
        ann61.setFractionRente(PRConverterUtils.formatBigDecimalToString(rrELRueckMeldungType.getProzentualAnteil()));

        return ann61;

    }

}
