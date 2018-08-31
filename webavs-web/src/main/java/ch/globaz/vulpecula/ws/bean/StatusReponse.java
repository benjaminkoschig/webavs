package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlEnumValue;

public enum StatusReponse {
    @XmlEnumValue("OK")
    OK,
    @XmlEnumValue("ERROR")
    ERROR;
}
