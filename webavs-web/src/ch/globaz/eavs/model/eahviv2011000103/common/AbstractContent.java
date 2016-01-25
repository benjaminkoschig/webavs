package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSpouse;

public abstract class AbstractContent extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAssessmentDate getAssessmentDate();

    public abstract AbstractAssessmentType getAssessmentType();

    public abstract AbstractEpBusinessData getEpBusinessData();

    public abstract AbstractRemark getRemark();

    public abstract AbstractReportType getReportType();

    public abstract AbstractSpBasicData getSpBasicData();

    public abstract AbstractSpBusinessData getSpBusinessData();

    public abstract AbstractSpouse getSpouse();

    public abstract AbstractSpPrivateData getSpPrivateData();

    public abstract AbstractTaxpayer getTaxpayer();

    public abstract void setAssessmentDate(EAVSAbstractModel _assessmentDate);

    public abstract void setAssessmentType(EAVSAbstractModel _assessmentType);

    public abstract void setEpBusinessData(EAVSAbstractModel _epBusinessData);

    public abstract void setRemark(EAVSAbstractModel _remark);

    public abstract void setReportType(EAVSAbstractModel _reportType);

    public abstract void setSpBasicData(EAVSAbstractModel _spBasicData);

    public abstract void setSpBusinessData(EAVSAbstractModel _spBusinessData);

    public abstract void setSpPrivateData(EAVSAbstractModel _spPrivateData);

    public abstract void setTaxpayer(EAVSAbstractModel _taxpayer);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}
