<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@page import="globaz.phenix.db.communications.CPSedexContribuable"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
bButtonUpdate = false;
bButtonDelete = false;
idEcran="CCP1017";
CPSedexContribuable viewBean = (CPSedexContribuable)session.getAttribute ("viewBean");
boolean hasJournal = true;
key="globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean-idRetour"+viewBean.getIdRetour();
%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausiManager"%>
<%@page import="globaz.phenix.db.communications.CPLienCommunicationsPlausi"%>
<%@page import="globaz.phenix.interfaces.ICommunicationRetour"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Données fiscales Détail"
function add() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.modifierCustom";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.supprimer";
		document.forms[0].submit();
	}
}

function afficherDonneesPrivees() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesPrivees";
		document.forms[0].submit();
}

function afficherDonneesCommunication() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommunication";
		document.forms[0].submit();
}

function afficherContribuable() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherContribuable";
		document.forms[0].submit();
}

function afficherDonneesBase() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesBase";
		document.forms[0].submit();
}

function afficherDonneesCommerciales() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommerciales";
		document.forms[0].submit();
}

function afficherConjoint() {
		document.forms[0].elements('userAction').value="phenix.communications.apercuCommunicationFiscaleRetour.afficherConjoint";
		document.forms[0].submit();
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Données fiscales<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<div id="tabs"> 
		<ul>
  			<li><a  onclick="afficherDonneesCommunication()" href="#">Données communication</a></li>
  			<li><a  class="selected">Contribuable</a></li>
  			<li><a  onclick="afficherDonneesBase()" href="#">Données de base</a></li>
  			<li><a  onclick="afficherDonneesPrivees()" href="#">Données privées</a></li>
  			<li><a  onclick="afficherDonneesCommerciales()" href="#">Données commerciales</a></li>
  			<li><a  onclick="afficherConjoint()" href="#">Conjoint</a></li>
		</ul>
		</div>
		<TR>
			<TD>
			  <INPUT type="hidden"  name="isForBackup" value="<%=viewBean.isForBackup()%>">
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SENDERID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="senderId" tabindex="1" value="<%=viewBean.getSenderId()%>" class="libelleLong">
	    
				&nbsp;&nbsp;<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ACTION", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="action" tabindex="1" value="<%=viewBean.getAction()%>" class="libelleLong">
	     	</TD>
	     </TR>
	     <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MESSAGEID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="messageId" tabindex="2" value="<%=viewBean.getMessageId()%>" class="libelleLong">
				&nbsp;&nbsp;<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REFERENCEMESSAGEID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="referenceMessageId" tabindex="3" value="<%=viewBean.getReferenceMessageId()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OURBUSINESSREFERENCEID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="ourBusinessReferenceId" tabindex="4" value="<%=viewBean.getOurBusinessReferenceID()%>" class="libelleLong">
	     
				&nbsp;&nbsp;<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("YOURBUSINESSREFERENCEID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="yourBusinessReferenceId" tabindex="5" value="<%=viewBean.getYourBusinessReferenceId()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ASSESSMENTDATE", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT width="8cm" type="text" name="assessmentDate" tabindex="6" value="<%=viewBean.getAssessmentDate()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ASSESSMENTTYPE", "FR")%>" type="text" class="disabled" readonly>
				<%if(viewBean.getAssessmentType().equals("1")||viewBean.getAssessmentType().equals("2")||viewBean.getAssessmentType().equals("3")||viewBean.getAssessmentType().equals("4")||viewBean.getAssessmentType().equals("5")||viewBean.getAssessmentType().equals("11")){ %>
			    <INPUT width="8cm" type="text" tabindex="7" value="<%=viewBean.getSession().getApplication().getLabel("ASSESSMENTTYPE_"+viewBean.getAssessmentType(), "FR")%>" class="libelleLongDisabled" readonly>
			    <%}%>
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text" tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REPORTTYPE", "FR")%>" type="text" class="disabled" readonly>
				<%if(viewBean.getReportType().equals("1")||viewBean.getReportType().equals("2")||viewBean.getReportType().equals("4")){ %>
			    <INPUT type="text" tabindex="8" value="<%=viewBean.getSession().getApplication().getLabel("REPORTTYPE_"+viewBean.getReportType(), "FR")%>" class="libelleLongDisabled" readonly>
			    <%} %>
	     	</TD>	
        </TR>
         <TR>
	   		<TD nowrap  height="11" colspan="4">
            	<hr size="3" width="100%">
            </TD>
        </TR>
           <TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("VN", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="vn" tabindex="9" value="<%=viewBean.getVn()%>" class="libelleLong">
	     	</TD>	
        </TR>
     
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("OFFICIALNAME", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="officialName" tabindex="12" value="<%=viewBean.getOfficialName()%>" class="libelleLong">
				 &nbsp;&nbsp;
 				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ADDRESSLINE1", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="addressline1" tabindex="16" value="<%=viewBean.getAddressLine1()%>" class="libelleLong">
	     	</TD>	
        </TR>	     	
	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("FIRSTNAME", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="firstName" tabindex="13" value="<%=viewBean.getFirstName()%>" class="libelleLong">
			     &nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("ADDRESSLINE2", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="addressline2" tabindex="16" value="<%=viewBean.getAddressLine2()%>" class="libelleLong">
	     	</TD>	
        </TR>
          <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SEX", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="libSex" tabindex="14" value="<%=CPToolBox.getLibSexe(viewBean.getSession(), viewBean.getSex())%>" class="libelleLong" readonly="readonly">
	  		    &nbsp;&nbsp;
	  		   <INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("STREET", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="street" tabindex="16" value="<%=viewBean.getStreet()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("YEARMONTHDAY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="yearMonthDay" tabindex="15" value="<%=viewBean.getYearMonthDay()%>" class="libelleLong">
	     		&nbsp;&nbsp;
	     		<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("HOUSENUMBER", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="houseNumber" tabindex="17" value="<%=viewBean.getHouseNumber()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("MARITALSTATUS", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="libMaritalStatus" tabindex="21" value="<%=CPToolBox.getLibEtatCivil(viewBean.getSession(), viewBean.getMaritalStatus())%>" class="libelleLong" readonly="readonly">
	     	&nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("LOCALITY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="locality" tabindex="16" value="<%=viewBean.getLocality()%>" class="libelleLong">
	     	</TD>	
        </TR>
       
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DATEOFMARITALSTATUS", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="dateOfMaritalStatus" tabindex="22" value="<%=viewBean.getDateOfMaritalStatus()%>" class="libelleLong">
	     	&nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("TOWN", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="town" tabindex="18" value="<%=viewBean.getTown()%>" class="libelleLong">
	     	</TD>	
        </TR>
       
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("DATEOFENTRY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="dateOfEntry" tabindex="23" value="<%=viewBean.getDateOfEntry()%>" class="libelleLong">
	     	&nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("SWISSZIPCODE", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="swissZipCode" tabindex="19" value="<%=viewBean.getSwissZipCode()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("INITIALMESSAGEDATE", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="initialMessageDate" tabindex="16" value="<%=viewBean.getInitialMessageDate()%>" class="libelleLong">
	     	&nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("COUNTRY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="country" tabindex="20" value="<%=viewBean.getCountry()%>" class="libelleLong">
	     	</TD>	
        </TR>
      	 <TR>
	   					<TD nowrap  height="11" colspan="4">
              				<hr size="3" width="100%">
            			</TD>
          			</TR>
           <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PERSONIDCATEGORY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="personIdCategory" tabindex="10" value="<%=viewBean.getPersonIdCategory()%>" class="libelleLong">
	     	&nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("PERSONID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="personId" tabindex="11" value="<%=viewBean.getPersonId()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("LOCALPERSONIDCATEGORY", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="personIdCategory" tabindex="10" value="<%=viewBean.getLocalPersonIdCategory()%>" class="libelleLong">
	   &nbsp;&nbsp;
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("LOCALPERSONID", "FR")%>" type="text" class="disabled" readonly>

			    <INPUT type="text" name="personIdCategory" tabindex="10" value="<%=viewBean.getLocalPersonId()%>" class="libelleLong">
	     	</TD>	
        </TR>
        <TR>
	     	<TD>
				<INPUT width="8cm" type="text"  tabindex="-1" value="<%=viewBean.getSession().getApplication().getLabel("REMARK", "FR")%>" type="text" class="disabled" readonly>
			</TD>	
				
		 </TR>
        <TR>
        	<TD>		
			    <textarea rows="3" cols="100" name="remark" tabindex="24"><%=viewBean.getRemark()%></textarea>
	     	</TD>	
        </TR>
       <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getIdRetour()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>