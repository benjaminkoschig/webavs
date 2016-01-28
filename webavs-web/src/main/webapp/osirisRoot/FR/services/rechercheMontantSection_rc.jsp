
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0059";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.services.*" %>
<%@ page import="globaz.globall.util.*" %>
<%bButtonNew = false;%>
<%
	CARechercheMontantSectionManagerListViewBean viewBean = (CARechercheMontantSectionManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
	rememberSearchCriterias = true;
//	CARechercheMontantViewBean element = (CARechercheMontantViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

//   	viewBean.setForIdCompteAnnexe(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())?viewBean.getForIdCompteAnnexe():element.getIdCompteAnnexe());
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>
<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.services.rechercheMontantSection.lister";
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des soldes des sections<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <TR>
            <td height="18" width="56">Nom</td>
            <td nowrap height="18" width="295">
              <input type="text" name="likeDescription" value="" class="libelleStandard" onChange="jsFromDescription()">
            </td>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">Solde</td>
            <td width="8">&nbsp;</td>
            <td height="18">
            	<SELECT name="forSoldeOperator">
					<OPTION value="=" selected>=</OPTION>
					<OPTION value="&gt;=">&gt;=</OPTION>
					<OPTION value="&lt;=">&lt;=</OPTION>
				</SELECT>
              <input type="text" class="montant" name="forSoldeABS" value="" onChange="jsForMontantABS()">
            </td>
            <td width="8">&nbsp;</td>
            <td height="2">&nbsp;</td>
            <td height="18">&nbsp;</td>
	    </TR>
          <TR>
            <TD height="18" width="56">N&deg; compte</TD>
            <TD nowrap height="18">
            <%String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";%>

			<ct:FWPopupList
	           	name="likeIdExterneRole"
				className="libelle"
				jspName="<%=jspAffilieSelectLocation%>"
				size="25"
				onChange=""
				minNbrDigit="1"
			/>

			<select name="forSelectionRole" tabindex="2">
            	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
           	</select>

			</TD>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">&nbsp;</td>
            <td width="8">&nbsp;</td>
            <td height="18">&nbsp;</TD>
            <td height="18" width="10">&nbsp; </td>
            <td height="18" width="61">&nbsp;</td>
            <td width="2">&nbsp;</td>
          </TR>
          <TR>
          	<TD height="18" width="56">N&deg; section</TD>
            <TD nowrap height="18"><%String jspSectionsSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";%>

			<ct:FWPopupList
	           	name="likeIdExterne"
				className="libelle"
				jspName="<%=jspSectionsSelectLocation%>"
				size="25"
				onChange=""
				minNbrDigit="1"
			/>
            </TD>
            <td height="18" width="10">&nbsp;</td>
            <td height="18" width="61">Id section&nbsp;</td>
            <td width="8">&nbsp;</td>
            <td height="18"><input type="text" class="libelleStandard" name="forIdSection" value=""></td>
          </TR>
          	<input type="hidden" name="likeIdTypeOperation" value=""/>
            <input type="hidden" name="selectorName" value=""/>
			<input type="hidden" name="vueOperationCpteAnnexe" value="true"/>
			<input type="hidden" name="rechercheMontant" value="true"/>


						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>