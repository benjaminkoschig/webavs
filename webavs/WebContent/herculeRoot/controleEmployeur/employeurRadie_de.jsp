<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.controleEmployeur.CEEmployeurRadieViewBean"%>
<%
	idEcran="CCE2009";

	//Récupération des beans
	CEEmployeurRadieViewBean viewBean = (CEEmployeurRadieViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.employeurRadie.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	function validate() {
	    state = validateFields();

	    return state;
	}

	function afficheTypeAdresse() {
		if (document.getElementById('listeExcel').checked) {
			document.getElementById('afficheType').style.display = 'inline';
			document.getElementById('afficheType').style.visibility = 'visible';
		} else {
			document.getElementById('afficheType').style.display = 'none';
			document.getElementById('afficheType').style.visibility = "hidden";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_EMPLOYEUR_RADIE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
		<TR>
			<TD><ct:FWLabel key="FROM_DATE"/></TD>
			<TD>
				<ct:FWCalendarTag name="fromDateRadiation" doClientValidation="CALENDAR"  value="<%=viewBean.getFromDateRadiation()%>" />
				&nbsp;
				&nbsp;
				<ct:FWLabel key="TO_DATE"/>
				&nbsp;
				&nbsp;
				<ct:FWCalendarTag name="toDateRadiation" doClientValidation="CALENDAR" value="<%=viewBean.getToDateRadiation()%>" />								
			</TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key="FROM_MASSE_SALARIALE"/></TD>
			<TD>
				<INPUT type="text" name="fromMasseSalariale" onkeypress="return filterCharForPositivFloat(window.event);" onchange="validateFloatNumber(this);" value="<%=viewBean.getFromMasseSalariale()%>" style="text-align : right" />
				&nbsp;
				&nbsp;
				<ct:FWLabel key="TO_MASSE_SALARIALE"/>
				&nbsp;
				&nbsp;
				<INPUT type="text" name="toMasseSalariale" onkeypress="return filterCharForPositivFloat(window.event);" onchange="validateFloatNumber(this);" value="<%=viewBean.getToMasseSalariale()%>" style="text-align : right" />								
			</TD>
		</TR>
		<TR>
			<TD nowrap><ct:FWLabel key="MOTIF_RADIATION"/></TD>
			<TD nowrap>
				<ct:FWCodeSelectTag 
             		name="forMotifRadiation" 
					defaut="<%=viewBean.getForMotifRadiation()%>"
					codeType="VEMOTIFFIN"
					wantBlank="true"/> 			
			</TD>
		</TR>
		<TR>
        	<TD width="23%" height="2"><ct:FWLabel key="TYPE_ADRESSE_DEFAUT"/></TD>
        	<TD height="2"> 
    			<SELECT id="tri" name="typeAdresse" doClientValidation="">
					<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="DOMICILE"/></OPTION>
					<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="COURRIER"/></OPTION>
				</SELECT>
  			</TD>
      	</TR>
		<TR>
			<TD></TD>
			<TD></TD>
		</TR>
		<TR>
        	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
        	<TD height="2"> 
          		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        	</TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>