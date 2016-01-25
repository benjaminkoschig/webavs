<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP0013";
	globaz.phenix.db.principale.CPDecisionViewBean viewBean = (globaz.phenix.db.principale.CPDecisionViewBean)session.getAttribute ("viewBean");
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.phenix.db.principale.CPDecision"%>
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
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Beitrag - Verfügung initialisierung"
function add() {
	//Initialisation
     	document.forms[0].anneeDecision.value="<%=globaz.globall.util.JACalendar.getYear(globaz.globall.util.JACalendar.today().toString())%>";
     	document.forms[0].elements("anneeDecision").focus();
}
function upd() {
}
function validate() {
	state = validateFields(); 
	document.forms[0].elements('userAction').value="phenix.principale.decision.initialiser";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.decision.chercher";
	document.forms[0].elements('selectedId2').value="<%=viewBean.getIdAffiliation()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.getIdTiers()%>";
}
function del() {
	
}
function init(){
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Erfassung einer Verfügung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
            <TD nowrap width="132">Mitglied-Nr.</TD>
            <TD nowrap width="200"><INPUT type="text" name="selection" value="<%=viewBean.getSelection()%>" doClientValidation="NOT_EMPTY" class="numeroId"> 
 		  <%
			Object[] tiersMethodsName= new Object[]{
				new String[]{"setSelection","getNumAffilieActuel"},
				new String[]{"setIdTiers","getIdTiers"},
				new String[]{"setDescriptionTiers","getNom"},		
			};
			Object[] tiersParams = new Object[]{
				new String[]{"selection","_pos"},
			};

			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionInit_de.jsp";	
			%>
			<ct:ifhasright element="pyxis.tiers.tiers.chercher" crud="r">
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=tiersParams%>"
				redirectUrl="<%=redirectUrl%>"
			/>
			</ct:ifhasright>
            
            </TD>
            <TD width="192">
			<INPUT name="descriptionTiers" class="libelleLongDisabled" readonly value="<%=viewBean.getDescriptionTiers()%>" type="text">
		</TD>
          </TR>   
          <TR>
		<TD nowrap width="160">&nbsp;</TD>
            <TD nowrap width="202"><INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
            </TD>
            <TD nowrap width="192"></TD>
          </TR>  
          <TR>
            <TD nowrap width="150">Verfügungsjahr</TD>
            <TD nowrap width="202"><INPUT type="text" name="anneeDecision" maxlength="4" size="4"></TD>
            <TD nowrap width="192"></TD>
          </TR>
           <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap width="202"><INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"></TD>
            <TD nowrap width="192">
                <INPUT type="hidden" name="selectedId2" value="<%=viewBean.getIdAffiliation()%>">
		</TD>
          </TR>
      	<TR>
            <TD nowrap width="160">Verfügung als</TD>
            <% String genreAff = viewBean._initGenreAffilie();
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(genreAff)) {%>
            	<TD nowrap width="202">
					<%
						java.util.HashSet except = new java.util.HashSet();
						except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
						except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
						except.add(globaz.phenix.db.principale.CPDecision.CS_INDEPENDANT);
						except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
						except.add(globaz.phenix.db.principale.CPDecision.CS_AGRICULTEUR);
						except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
					%>
					<ct:FWCodeSelectTag name="genreAffilie"
						defaut="<%=genreAff%>"
						codeType="CPGENDECIS"
						except="<%=except%>"
					/>
		        </TD>
            <% } else { %>
	            <TD nowrap width="202"><INPUT name="libelleGenreAffilie" class="libelleLongDisabled" readonly value="<%=globaz.phenix.translation.CodeSystem.getLibelle(viewBean.getSession(), genreAff)%>" type="text"></TD>
	            <TD nowrap width="192"><INPUT type='hidden' name="genreAffilie" value="<%=genreAff%>" type="text"></TD>
	         <% } %>
          </TR>
  	 <TR> 
  	        <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap width="202"></TD>
            <TD nowrap width="192"></TD>
          </TR>
          <TR>
            <TD nowrap width="160">Verfügungsart</TD>
            <TD nowrap width="202">
			<ct:FWCodeSelectTag name="typeDecision"
				defaut="605001"
				codeType="CPTYPDECIS"
	       	/>
            </TD>
            <TD nowrap width="192"></TD>
          </TR>
	<TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap width="202"></TD>
            <TD nowrap width="192"><INPUT type="hidden" name="numAffilie" value="<%=viewBean.loadAffiliation().getAffilieNumero()%>"></TD>
          </TR>          
	          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>