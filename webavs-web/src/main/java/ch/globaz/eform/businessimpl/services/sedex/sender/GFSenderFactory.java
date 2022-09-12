package ch.globaz.eform.businessimpl.services.sedex.sender;

import ch.globaz.eform.businessimpl.services.sedex.constant.GFMessageTypeSedex;
import ch.globaz.eform.businessimpl.services.sedex.sender.dadossier.GF2021000101Sender;
import ch.globaz.eform.businessimpl.services.sedex.sender.dadossier.GF2021000102Sender;

import java.util.HashMap;
import java.util.Map;

public class GFSenderFactory {
    private final Map<GFMessageTypeSedex, Class<? extends GFDaDossierSender>> mapClasses;
    private static GFSenderFactory instance;

    private GFSenderFactory() {
        mapClasses = new HashMap<>();
        mapClasses.put(GFMessageTypeSedex.TYPE_2021_DEMANDE, GF2021000101Sender.class);
        mapClasses.put(GFMessageTypeSedex.TYPE_2021_TRANSFERE, GF2021000102Sender.class);
    }

    public static GFDaDossierSender getSedexSender(GFMessageTypeSedex messageTypeSedex) {
        if (instance == null) { instance = new GFSenderFactory(); }

        try {
            return instance.mapClasses.get(messageTypeSedex).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
