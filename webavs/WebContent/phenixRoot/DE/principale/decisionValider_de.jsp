<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@page import="globaz.draco.util.DSUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.phenix.db.principale.CPDecisionValiderViewBean viewBean = (globaz.phenix.db.principale.CPDecisionValiderViewBean)session.getAttribute ("viewBean");
	java.lang.Boolean impressionFlux;
		try {
			impressionFlux =  ((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).isAffichageDecisionFluxPDF();
		}catch (Exception e) {
			impressionFlux = Boolean.TRUE;
	}
	boolean isJournalRetour = false;
	if(request.getParameter("provenance")!=null)
	{
		if(((String)request.getParameter("provenance")).equals("journalRetour"))
			isJournalRetour = true;
	}
	idEcran="CCP0007";
	
// Affichage de la décision dans une autre fenêtre	
if(viewBean.getDocListe()!=null && viewBean.getDocListe().size()>0){
	for(int i=0;i<viewBean.getDocListe().size();i++){
		%>
			<% if(impressionFlux.booleanValue()){ %>
			
<%@page import="globaz.jade.client.util.JadeStringUtil"%><script>
				window.open('<%=request.getContextPath()+"/phenix?userAction=phenix.document.decision.generer&doc="+(String)viewBean.getDocListe().elementAt(i)%>');								
			</script>			
			<% } else {%>
			<script>
				window.open("<%=request.getContextPath()+"/persistence/"+(String)viewBean.getDocListe().elementAt(i)%>");
			</script>	
			<% } %>
		<%
	}
}

bButtonDelete = false;
%>
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
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="JavaScript">

top.document.title = "Beiträge - Verfügung validieren"
function add() {
}
function upd() {
}
function validate() {
	state = validateFields(); 
<%if(isJournalRetour) { %>
	document.forms[0].elements('userAction').value="phenix.principale.decisionValider.modifierProvenanceJournalRetour";
<% }else{ %>
	document.forms[0].elements('userAction').value="phenix.principale.decisionValider.modifier";
<% } %>
	return (state);
}
function cancel() {
<%if(isJournalRetour) { %>
	document.forms[0].elements('userAction').value="phenix.communications.validationJournalRetour.chercher";
<% }else{ %>
	document.forms[0].elements('userAction').value="phenix.principale.decisionValider.chercher";
	document.forms[0].elements('selectedId2').value="<%=viewBean.getIdAffiliation()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.getIdTiers()%>";
<% } %>
}
function del() {
	
}
function init(){
  
	//document.getElementById("btnCan").click();
}
/*
*/

</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Verfügungsvalidierung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap colspan="4" style="font-weight : bolder;">Falls folgende Verfügung korrekt ist, bitte validieren. 
            Die Verfügung wird mit dem nächsten Job bearbeitet.</TD>
          </TR>
          <TR> 
            <TD nowrap width="80">&nbsp;</TD>
            <TD nowrap></TD>
	      <TD width="50"></TD>
             <TD nowrap width="125"></TD>
          </TR>

	   <TR>
            <TD nowrap width="120" height="10">Versicherter</TD>
            <TD nowrap> 
              <INPUT type="text" name="numAffilie" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" tabindex="-1" readonly>
	     </TD>
            <TD nowrap><INPUT type="text" name="nom" value="<%=viewBean.loadTiers().getNom()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
          </TR>
	   <TR>
            <TD nowrap width="120">Verfügung als </TD>
            <TD nowrap> 
              <INPUT name="libelleGenreAffilie" type="text" value="<%=viewBean.getLibelleGenreAffilie()%>" class="libelleLongDisabled" readonly>
            </TD>
             <TD nowrap width="50">
            	<INPUT type="hidden" name="complementaire" value="<%=viewBean.getComplementaire()%>">
            	<INPUT type="hidden" name="bloque" value="<%=viewBean.getBloque()%>">
            	<INPUT type="hidden" name="facturation" value="<%=viewBean.getFacturation()%>">
            	<INPUT type="hidden" name="division2" value="<%=viewBean.getDivision2()%>">
            	<INPUT type="hidden" name="impression" value="<%=viewBean.getImpression()%>">
            	<INPUT type="hidden" name="opposition" value="<%=viewBean.getOpposition()%>">
            	<INPUT type="hidden" name="recours" value="<%=viewBean.getRecours()%>">
            	<INPUT type="hidden" name="lettreSignature" value="<%=viewBean.getLettreSignature()%>">
            	<INPUT type="hidden" name="active" value="<%=viewBean.getActive()%>">
            	<INPUT type="hidden" name="debutActivite" value="<%=viewBean.getDebutActivite()%>">
            	<INPUT type="hidden" name="premiereAssurance" value="<%=viewBean.getPremiereAssurance()%>">
            	<INPUT type="hidden" name="cotiMinimumPayeEnSalarie" value="<%=viewBean.getCotiMinimumPayeEnSalarie()%>">  	
            </TD>
            <TD nowrap width="125"> 
            </TD>
          </TR>
          <TR>
            <TD nowrap width="120">Verfügungsart </TD>
            <TD nowrap><INPUT name="libelleTypeDecision" type="text" value="<%=viewBean.getLibelleTypeDecision()%>" class="libelleLongDisabled" readonly></TD>
            <TD nowrap>Validierung forcieren &nbsp;<input type="checkbox" name="validationForcee"></TD>
          </TR>
		<tr>
         <td>
             	<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
                <INPUT type="hidden" name="selectedId2" value="<%=viewBean.getIdAffiliation()%>">
             </TD>
</tr>
          
          
	  <TR>
	  		 <TD nowrap  height="11" colspan="5"> 
              <hr size="3">
            </TD>
          </TR>  
          <TR>
        	<TD nowrap width="60">Job</TD>
            <TD><INPUT type="text" name="idPassage" class="libelleLongDisabled" value="<%=JadeStringUtil.isEmpty(viewBean.getIdPassage())?"":viewBean.getIdPassage()%>" readonly="readonly" >
            <%
				Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
				};
            	viewBean.setDocListe(null);
				Object[] psgParams= new Object[]{};
				//String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/listeDecision_de.jsp";	
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionValider_de.jsp";	
				%>
				<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
			    <ct:FWSelectorTag 
				name="passageSelector" 
				
				methods="<%=psgMethodsName%>"
				providerPrefix="FA"			
				providerApplication ="musca"			
				providerAction ="musca.facturation.passage.chercher"			
				providerActionParams ="<%=psgParams%>"
				redirectUrl="<%=redirectUrl%>"			
				/> 
				</ct:ifhasright>
				<input type="hidden" name="selectorName" value="">
            </TD>	
         </TR>
         <TR>
            <TD nowrap width="160"><INPUT type="hidden" name="idDecision" value="<%=viewBean.getIdDecision()%>"></TD>
            <%
            	if(JadeStringUtil.isEmpty(viewBean.getLibellePassage())){
            		if(viewBean.getPassage()!=null){
            			viewBean.setLibellePassage(viewBean.getPassage().getLibelle());
            		}else{
            			viewBean.setLibellePassage("");
            		}
            	}
            %>
              <TD nowrap><INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly></TD>
           <% if(CPToolBox.isCaisseCCCVS() && !JadeStringUtil.isBlankOrZero(viewBean.getIdCommunication())) {  %>	
            	<TD nowrap>in Ged ablegen &nbsp;<input type="checkbox" name="miseEnGEDValidationRetour"></TD>
            <% } else {  %>
            	<TD nowrap></TD>
            <% }%>
          </TR>
   	  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-decision" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDecision()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>"/>
</ct:menuChange>
<script>
// menu 
var oldBodyOnLoad = document.body.onload;
var newBodyOnLoad = function() {
	oldBodyOnLoad();
	document.all("btnUpd").click();
}
document.body.onload = newBodyOnLoad;
//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>