package ch.globaz.common;

import globaz.globall.db.BSessionUtil;
import ch.globaz.common.FusionTiersMultiple.utils.MessageBundleUtil;

public class LabelCommonProvider {
    public static String getLabel(String key, String languageIso) {
        return MessageBundleUtil.getMessage(key, languageIso);
    }

    public static String getLabel(String key) {
        if (BSessionUtil.getSessionFromThreadContext() == null) {
            throw new RuntimeException("No thread context found");
        }

        return MessageBundleUtil.getMessage(key, BSessionUtil.getSessionFromThreadContext().getIdLangueISO());
    }
}
