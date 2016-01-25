<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0014";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.musca.db.facturation.FAPassageViewBean" %>
<%@ page import="globaz.musca.db.facturation.FAAfactAQuittancerViewBean" %>
<%@ page import="globaz.musca.translation.CodeSystem"%>
<%@ page import="globaz.osiris.db.comptes.CARoleManager" %>
<%@ page import="globaz.osiris.db.comptes.CARole" %>
<%
	FAPassageViewBean viewBean = (FAPassageViewBean)session.getAttribute ("viewBean");
	String fromIdExtRole = request.getParameter("fromIdExterneRole");
	bButtonNew = false;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
bFind=true;

// menu 
//usrAction = "musca.facturation.afact.listerAQuittancer";
usrAction = "musca.facturation.afactAQuittancer.lister"
</SCRIPT>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="FA-PassageFacturation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="idPassage" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPassage()%>"/>
</ct:menuChange>	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des afacts en suspens<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
            <TD nowrap width="120">Passage</TD>
            <TD nowrap><INPUT type="text" name="passage" class="libelleLongDisabled" value="<%=viewBean.getIdPassage()+" - "+ viewBean.getLibelle()%>" readonly style="width : 10.0cm"></TD>
            <TD width="80"><INPUT type="hidden" name="forIdPassage" value='<%=viewBean.getIdPassage()%>'></TD>
            <TD nowrap valign="middle" align="left" width="120">Rôle</TD>
            <TD width="120">
            <SELECT style="width:100%" name="forIdRole" >
			<%CARole tempRole;
				CARoleManager manRole = new CARoleManager();
				manRole.setSession(objSession);
				manRole.find();
				for(int i = 0; i < manRole.size(); i++){
					tempRole = (CARole)manRole.getEntity(i); %>
                		<option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <% } %>
              </SELECT>
             </TD>
		</TR>
		<TR> 
			<TD nowrap width="120">Tri</TD>
			<TD nowrap>
				<SELECT style="width:100%" name="orderBy">
					<OPTION value="<%=FAAfactAQuittancerViewBean.ORDER_NO_DEBITEUR%>">Numéro de débiteur</OPTION>
					<OPTION value="<%=FAAfactAQuittancerViewBean.ORDER_NOM%>">Nom</OPTION>
					<OPTION value="<%=FAAfactAQuittancerViewBean.ORDER_MONTANT%>">Montant</OPTION>
				</SELECT>
			</TD>
			<TD nowrap></TD>
           <TD nowrap valign="middle" align="left" width="150">Montant</TD>
           <TD width="159">
           		<INPUT type="text" name="fromMontant" tabindex="-1" class="montant,libelleLong" size="37">
           </TD>
 		</TR>
 		<TR>       
			<TD nowrap width="120">A partir de</TD>
			<TD nowrap width="120">
			<INPUT type="text" name="fromIdExterneRole" tabindex="-1" class="libelleLong" size="37"></TD>
		</TR>
		<TR>
			<TD nowrap valign="middle" align="left" width="120">Etat</TD>
            <TD width="120">
				<SELECT style="width:100%" name="forAQuittancerString">
					<OPTION value="all"></OPTION>
					<OPTION value="aRefuser">Accepté</OPTION>
					<OPTION value="aValider">Refusé</OPTION>
				</SELECT>
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