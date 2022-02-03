package ch.globaz.eform.services.sedex.handlers;

import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.message.SimpleSedexMessage;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class GFFormHandlersFactory {
    HashMap<Class<?>, Class<?>> mapClasses;

    public GFFormHandlersFactory(){
        mapClasses = new HashMap();
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

    public GFFormHandler getFormHandler(SimpleSedexMessage currentSimpleMessage, String zipFileLocation, String sedexCurrentFolder) throws JAXBValidationError, JAXBValidationWarning, JAXBException, IOException, SAXException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JAXBServices jaxbs = JAXBServices.getInstance();
        Class<?>[] addClasses = mapClasses.keySet().toArray(new Class[0]);
        Object oMessage = jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
        return getGfFormHandler(currentSimpleMessage, sedexCurrentFolder, oMessage, addClasses);
    }

    private GFFormHandler getGfFormHandler(SimpleSedexMessage currentSimpleMessage, String currentSedexFolder, Object message, Class<?>[] addClasses) throws InstantiationException, IllegalAccessException, IOException, NoSuchMethodException {
        GFFormHandler handler = null;
        Iterator<Class<?>> iterator = Arrays.stream(addClasses).iterator();
        while(handler == null && iterator.hasNext()){
            Class<?> theClass = iterator.next();
            if(theClass != null && theClass.isInstance(message)){
                handler = (GFFormHandler) mapClasses.get(theClass).newInstance();
                handler.setMessage(message);
            }
        }
        return handler;
    }
}
