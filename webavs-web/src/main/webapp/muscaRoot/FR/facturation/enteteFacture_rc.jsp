<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0003";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "musca.facturation.enteteFacture.lister";
</SCRIPT>
<%
	globaz.musca.db.facturation.FAEnteteFactureViewBean viewBean = (globaz.musca.db.facturation.FAEnteteFactureViewBean)session.getAttribute ("viewBean");
  
	//Vérifie si les passage est verrouillé ou comptabilisé. si oui, n'affiche pas le bouton "Nouveau"
	String passageStatus = globaz.musca.util.FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
	boolean passageLocked =globaz.musca.util.FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();
 
	if ((globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus))
		|| passageLocked
		|| (!objSession.hasRight(userActionNew, "ADD"))
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus) ){
		bButtonNew = false;
	}
%>
<ct:menuChange displayId="options" menuId="FA-PassageFacturation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="forIdJournalCalcul" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="idPassage" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPassage()%>"/>
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des décomptes du journal<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  <TR>
            <TD nowrap width="120">Passage</TD>
            <TD nowrap><INPUT type="text" name="passage" class="libelleLongDisabled" value="<%=globaz.musca.util.FAUtil.getLibellePassage(viewBean.getIdPassage(),session)%>" readonly style="width : 10.0cm"></TD>
            <TD width="80"><INPUT type="hidden" name="idPassage" value='<%=viewBean.getIdPassage()%>'></TD>
            <TD nowrap valign="middle" align="left" width="120">Rôle</TD>
	    	<TD width="159"> 
              <select name="forIdRole">
              	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
              </select>              
            </TD>
          </TR>
	  <TR>
            <TD nowrap>Tri</TD>
            <TD><ct:FWSystemCodeSelectTag name="triDecomptePassage"
			              defaut="909001"
			              codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecomptePassageWithoutBlank(session)%>" />
    		</TD>
            <TD nowrap></TD>
            <TD nowrap valign="middle" align="left">Décomptes</TD>
            <TD><ct:FWSystemCodeSelectTag name="triDecompte"
			              defaut=""
			              codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecompte(session)%>"
			/>
			</TD>
		</TR>
		<TR>
            <TD nowrap>A partir de</TD>
            <TD nowrap><INPUT type="text" name="fromLibelle" tabindex="-1" class="libelleLong"></TD>
            <TD> </TD>
            <TD nowrap valign="middle" align="left" width="149">Montant</TD>
            <TD width="159"><INPUT type="text" name="fromTotalFacture" tabindex="-1" class="montant,libelleLong" size="37"></TD>
          </TR>

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--
			<TD bgcolor="#FFFFFF" colspan="3" align="right">
				<A href="javascript:document.forms[0].submit();">
					<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
				</A>
			<%if(!"yes".equals(request.getParameter("colonneSelection"))) {%>
				<A href="<%=request.getContextPath()%>/musca?userAction=musca.facturation.enteteFacture.afficher&_method=add">
				<IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0">
				</A>
                    <%  }  %>
			</TD>
-->
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>