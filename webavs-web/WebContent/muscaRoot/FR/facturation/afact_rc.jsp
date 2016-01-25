<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0005";
rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
   globaz.musca.db.facturation.FAEnteteFactureViewBean viewBean = (globaz.musca.db.facturation.FAEnteteFactureViewBean)session.getAttribute ("viewBean");
   
   
    //Vérifie si les passage est verrouillé ou comptabilisé. si oui, n'affiche pas le bouton "Nouveau"
	//viewBean.getDonnee();
	String passageStatus = globaz.musca.util.FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
	boolean passageLocked =globaz.musca.util.FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();
 
	if ((globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus))
		|| passageLocked 
		|| (!objSession.hasRight(userActionNew, "ADD"))
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus) ){
		bButtonNew = false;
	}
%>

<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="FA-PassageFacturation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="forIdJournalCalcul" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="idPassage" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPassage()%>"/>
</ct:menuChange>
	
<SCRIPT>
bFind=true;

usrAction = "musca.facturation.afact.lister";

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des afacts du décompte<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  <TR>
            <TD nowrap width="100">Débiteur</TD>
            <TD nowrap width="420"><INPUT type="text" name="passage" class="libelleLongDisabled" style="width : 12.0cm" value="<%=viewBean.getDescriptionTiers()%>" readonly></TD>
            
            <TD width="50" nowrap></TD>
            <TD nowrap valign="middle" align="left" width="80">Module</TD>
            <TD><ct:FWListSelectTag name="forIdModuleFacturation"
			              defaut=""
			              data="<%=globaz.musca.translation.CodeSystem.getListModulesFacturation(session,true)%>"
			/></TD>
          </TR>
	<TR>
            <TD nowrap>Décompte</TD>
            <TD nowrap><INPUT type="text" name="decompte" class="libelleLongDisabled" readonly style="width : 12.0cm" value="<%=viewBean.getDescriptionDecompte()%>"></TD>
            <TD nowrap></TD>
            <TD nowrap valign="middle" align="left"></TD>
            <TD width="184"><INPUT type="hidden" name="idEnteteFacture" value='<%=viewBean.getIdEntete()%>'></TD>
          </TR>

	  <TR>
            <TD nowrap>A partir de</TD>
            <TD nowrap><INPUT type="text" name="fromLibelle" tabindex="1" class="libelleLong" style="width : 12.0cm"></TD>
            <TD></TD>
            <TD nowrap valign="middle" align="left">Total</TD>
            <TD width="184"><INPUT type="text" name="totalFacture" tabindex="-1" class="montantDisabled" readonly value="<%=viewBean.getTotalFacture()%>"></TD>
            <TD> <INPUT type="hidden" name="idPassage" value='<%=viewBean.getIdPassage()%>'></TD>
          </TR>

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--			<TD bgcolor="#FFFFFF" colspan="3" align="right">
				<A href="javascript:document.forms[0].submit();">
					<IMG name="btnFind" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnFind.gif" border="0">
				</A>
			<%if(!"yes".equals(request.getParameter("colonneSelection"))) {%>
				<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.afact.afficher&_method=add">
				<IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0">
				</A>
                    <%  }  %>
			</TD>
-->
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>