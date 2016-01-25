<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
idEcran = "CAF0064";
AFQuittanceViewBean viewBean = new AFQuittanceViewBean();//(AFQuittanceViewBean)session.getAttribute ("viewBean");
rememberSearchCriterias=true;
String jspLocationNSS = servletContext + mainServletPath + "Root/ci_select.jsp";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<%@page import="java.util.Vector"%>
<SCRIPT>
function changeName(input)
{
	input.value=input.value.replace('�','AE');
	input.value=input.value.replace('�','OE');
	input.value=input.value.replace('�','UE');
	input.value=input.value.replace('�','AE');
	input.value=input.value.replace('�','OE');
	input.value=input.value.replace('�','UE');	
	
	input.value=input.value.replace('�','E');
	input.value=input.value.replace('�','E');
	input.value=input.value.replace('�','O');
	input.value=input.value.replace('�','A');		
	
	input.value=input.value.toUpperCase();
}
</SCRIPT>
<!--<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>-->
<!--<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>-->
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT>
// menu 
top.document.title = "Anzeige der Quittungen"
usrAction = "naos.beneficiairepc.quittance.lister";
servlet = "naos";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der Quittungen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>

<TR> 
            <TD nowrap width="80">Abr.-Nr.</TD>
            <TD nowrap ><INPUT type="text" name="likeNumAffilie"  class="libelleCourt"'></TD> 
            <TD>&nbsp;</TD>
            <TD nowrap width="100">Name des Bez�gers</TD>
            <TD nowrap ><INPUT type="text" name="forNom" class="libelleLong" onchange="changeName(this);"></TD>
            <TD>&nbsp;</TD>
            <TD nowrap width="100">Vorname des Bez�gers</TD>
            <TD nowrap ><INPUT type="text" name="forPrenom" class="libelleLong" onchange="changeName(this);"></TD>
            <TD>&nbsp;</TD>
</TR>
<TR> 
            <TD nowrap width="80">SVN</TD>
            <TD nowrap ><nss:nssPopup
						name="forNumAvsAideMenage" 
						jspName="<%=jspLocationNSS%>"
						value="<%=viewBean.getPartialNss()%>"
						newnss="<%=viewBean.getNumeroavsNNSS()%>"
						avsMinNbrDigit="8" avsAutoNbrDigit="11"
						nssMinNbrDigit="7" nssAutoNbrDigit="10"
						/></TD> 
            <TD>&nbsp;</TD>

</TR>
<TR> 
            <TD nowrap width="80">Jahr</TD>
            <TD nowrap ><INPUT type="text" name="forAnnee"  class="numeroCourt"'></TD> 
            <TD>&nbsp;</TD>
           <!-- <TD nowrap width="100">Npa/Localit�</TD>
            <TD nowrap ><INPUT type="text" name="forLocalite" class="libelleLong"></TD>
            <TD>&nbsp;</TD>-->
            <TD nowrap width="100">Journal-Nr.</TD>
             <% String idJournal = request.getParameter("idJournalQuittances");
             if(JadeStringUtil.isNull(idJournal)==true ||JadeStringUtil.isBlankOrZero(idJournal)==true){
            	 idJournal = "";
             } 
             %>
            <TD nowrap ><INPUT type="text" name="forIdJournalQuittance" class="numeroCourt" value='<%=idJournal%>'></TD>
             <SCRIPT>
              if(document.forms[0].elements('forIdJournalQuittance').value==""){
            	 bFind=false;
             } else {
            	 bFind=true;
             }
            	</SCRIPT>     
            <TD>&nbsp;</TD>
</TR>
<TR>
	<TD>Status</TD>
	<TD>
	<%java.util.HashSet except = new java.util.HashSet();
	except.add(AFQuittanceViewBean.ETAT_CI_PARTIEL);
	except.add(AFQuittanceViewBean.ETAT_FACTURE_PARTIEL);
							%>
							<ct:FWCodeSelectTag name="forEtatQuittances"
							              defaut=""
									wantBlank="<%=true%>"
							        codeType="AFETJRNQUI"
									libelle="libelle"
									except="<%=except%>"
							/>
	</TD>
</TR>

<%-- /tpl:put --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>

<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>

<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>