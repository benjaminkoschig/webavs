package ch.globaz.eavs.model.eahviv2011000103.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSpouse;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAssessmentDate;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAssessmentType;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractContent;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEpBasicData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEpBusinessData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractRemark;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractReportType;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpBasicData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpBusinessData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpPrivateData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTaxpayer;

public class Content extends AbstractContent {
    private AssessmentDate assessmentDate = null;
    private AssessmentType assessmentType = null;
    private EpBasicData epBasicData = null;
    private Remark remark = null;
    private ReportType reportType = null;
    private SpBasicData spBasicData = null;
    private SpBusinessData spBusinessData = null;
    private Spouse spouse = null;
    private SpPrivateData spPrivateData = null;
    private Taxpayer taxpayer = null;

    @Override
    public AbstractAssessmentDate getAssessmentDate() {
        if (assessmentDate == null) {
            assessmentDate = new AssessmentDate();
        }
        return assessmentDate;
    }

    @Override
    public AbstractAssessmentType getAssessmentType() {
        if (assessmentType == null) {
            assessmentType = new AssessmentType();
        }
        return assessmentType;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(assessmentDate);
        result.add(assessmentType);
        result.add(reportType);
        result.add(taxpayer);
        result.add(spBasicData);
        result.add(spBusinessData);
        result.add(spPrivateData);
        result.add(remark);
        result.add(spouse);
        result.add(epBasicData);
        return result;
    }

    public AbstractEpBasicData getEpBasicData() {
        if (epBasicData == null) {
            epBasicData = new EpBasicData();
        }
        return epBasicData;
    }

    @Override
    public AbstractEpBusinessData getEpBusinessData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractRemark getRemark() {
        if (remark == null) {
            remark = new Remark();
        }
        return remark;
    }

    @Override
    public AbstractReportType getReportType() {
        if (reportType == null) {
            reportType = new ReportType();
        }
        return reportType;
    }

    @Override
    public AbstractSpBasicData getSpBasicData() {
        if (spBasicData == null) {
            spBasicData = new SpBasicData();
        }
        return spBasicData;
    }

    @Override
    public AbstractSpBusinessData getSpBusinessData() {
        if (spBusinessData == null) {
            spBusinessData = new SpBusinessData();
        }
        return spBusinessData;
    }

    @Override
    public AbstractSpouse getSpouse() {
        if (spouse == null) {
            spouse = new Spouse();
        }
        return spouse;
    }

    @Override
    public AbstractSpPrivateData getSpPrivateData() {
        if (spPrivateData == null) {
            spPrivateData = new SpPrivateData();
        }
        return spPrivateData;
    }

    @Override
    public AbstractTaxpayer getTaxpayer() {
        if (taxpayer == null) {
            taxpayer = new Taxpayer();
        }
        return taxpayer;
    }

    @Override
    public void setAssessmentDate(EAVSAbstractModel _assessmentDate) {
        assessmentDate = (AssessmentDate) _assessmentDate;
    }

    @Override
    public void setAssessmentType(EAVSAbstractModel _assessmentType) {
        assessmentType = (AssessmentType) _assessmentType;
    }

    public void setEpBasicData(EAVSAbstractModel _epBasicData) {
        epBasicData = (EpBasicData) _epBasicData;
    }

    @Override
    public void setEpBusinessData(EAVSAbstractModel _epBusinessData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRemark(EAVSAbstractModel _remark) {
        remark = (Remark) _remark;
    }

    @Override
    public void setReportType(EAVSAbstractModel _reportType) {
        reportType = (ReportType) _reportType;
    }

    @Override
    public void setSpBasicData(EAVSAbstractModel _spBasicData) {
        spBasicData = (SpBasicData) _spBasicData;
    }

    @Override
    public void setSpBusinessData(EAVSAbstractModel _spBusinessData) {
        spBusinessData = (SpBusinessData) _spBusinessData;
    }

    public void setSpouse(EAVSAbstractModel _spouse) {
        spouse = (Spouse) _spouse;
    }

    @Override
    public void setSpPrivateData(EAVSAbstractModel _spPrivateData) {
        spPrivateData = (SpPrivateData) _spPrivateData;
    }

    @Override
    public void setTaxpayer(EAVSAbstractModel _taxpayer) {
        taxpayer = (Taxpayer) _taxpayer;
    }

}
