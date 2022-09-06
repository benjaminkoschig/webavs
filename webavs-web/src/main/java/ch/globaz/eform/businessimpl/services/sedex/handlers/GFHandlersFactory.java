package ch.globaz.eform.businessimpl.services.sedex.handlers;

import ch.globaz.eform.businessimpl.services.sedex.handlers.dadossier.GF2021000101Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.dadossier.GF2021000102Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2501001800Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2501001820Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2501002690Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2501002691Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2504002600Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2504002820Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2504003700Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2504003710Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2504003860Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2506006860Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2506006880Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2509007500Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2509007510Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2509007520Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2513002600Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2513002700Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2513002701Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007440Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007450Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007460Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007470Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007480Handler;
import ch.globaz.eform.businessimpl.services.sedex.handlers.eform.GF2514007490Handler;
import globaz.globall.db.BSession;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.sedex.message.SimpleSedexMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class GFHandlersFactory {
    HashMap<Class<?>, Class<?>> mapClasses;

    public GFHandlersFactory(){
        mapClasses = new HashMap<>();
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.Message.class, GF2021000101Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message.class, GF2021000102Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2501_001800._1.Message.class, GF2501001800Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2501_001820._1.Message.class, GF2501001820Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2501_002690._1.Message.class, GF2501002690Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2501_002691._1.Message.class, GF2501002691Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2504_002600._1.Message.class, GF2504002600Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2504_002820._1.Message.class, GF2504002820Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2504_003700._1.Message.class, GF2504003700Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2504_003710._1.Message.class, GF2504003710Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2504_003860._1.Message.class, GF2504003860Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2506_006860._1.Message.class, GF2506006860Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2506_006880._1.Message.class, GF2506006880Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2509_007500._1.Message.class, GF2509007500Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2509_007510._1.Message.class, GF2509007510Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2509_007520._1.Message.class, GF2509007520Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2513_002600._1.Message.class, GF2513002600Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2513_002700._1.Message.class, GF2513002700Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2513_002701._1.Message.class, GF2513002701Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007440._1.Message.class, GF2514007440Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007450._1.Message.class, GF2514007450Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007460._1.Message.class, GF2514007460Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007470._1.Message.class, GF2514007470Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007480._1.Message.class, GF2514007480Handler.class);
        mapClasses.put(eform.ch.eahv_iv.xmlns.eahv_iv_2514_007490._1.Message.class, GF2514007490Handler.class);
    }

    public GFSedexhandler getSedexHandler(SimpleSedexMessage currentSimpleMessage, BSession session) throws Exception {
        JAXBServices jaxbs = JAXBServices.getInstance();
        Class<?>[] addClasses = mapClasses.keySet().toArray(new Class[0]);
        Object oMessage = jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
        return mapClasses.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(oMessage))
                .map(entry -> {
                    try {
                        GFSedexhandler handler = (GFSedexhandler) entry.getValue().newInstance();
                        handler.setMessage(oMessage);
                        handler.setSession(session);
                        return handler;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst().orElse(null);
    }
}
