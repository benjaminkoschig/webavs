package ch.globaz.ariesaurigacommon.utils;

import globaz.jade.client.util.JadeDateUtil;

public class AriesAurigaDateUtils {

    private static void checkNull(String globazDate) {
        if (!JadeDateUtil.isGlobazDate(globazDate)) {
            // TODO voir si une RuntimeException est adaptée
            // A première vue je trouve pas mal vu que c'est dans le cadre d'un util commun à plusieurs module
            throw new RuntimeException("Date value " + String.valueOf(globazDate) + " is not valid");
        }

    }

    public static String getMonth(String globazDate) {
        AriesAurigaDateUtils.checkNull(globazDate);
        return globazDate.substring(3, 5);
    }

    public static int getNombreMoisEntreDates(String premiereDate, String deuxiemeDate) {
        if (JadeDateUtil.isDateAfter(premiereDate, deuxiemeDate)) {
            // Exception technique car ce cas ne devrait pas se produire sauf erreur de programmation
            // TODO voir si une RuntimeException est adaptée
            // A première vue je trouve pas mal vu que c'est dans le cadre d'un util commun à plusieurs module
            throw new RuntimeException("First date is after second one");
        }

        if (!AriesAurigaDateUtils.getYear(premiereDate).equals(AriesAurigaDateUtils.getYear(deuxiemeDate))) {
            // Exception technique car ce cas ne devrait pas se produire sauf erreur de programmation
            // TODO voir si une RuntimeException est adaptée
            // A première vue je trouve pas mal vu que c'est dans le cadre d'un util commun à plusieurs module
            throw new RuntimeException("Dates years are not equal");
        }

        return Integer.parseInt(AriesAurigaDateUtils.getMonth(deuxiemeDate))
                - Integer.parseInt(AriesAurigaDateUtils.getMonth(premiereDate)) + 1;

    }

    public static String getYear(String globazDate) {
        AriesAurigaDateUtils.checkNull(globazDate);
        return globazDate.substring(6, 10);
    }

}
