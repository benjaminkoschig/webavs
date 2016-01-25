<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0069";
	rememberSearchCriterias = true;
	
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.taxeCo2.taxeCo2.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					TAXE CO2
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
            				<TD nowrap width="100" height="31">A partir de </TD>
							<TD nowrap width="180">
								<ct:FWPopupList name="fromNumAffilie" 
									value="" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
								/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffiliationId').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
				            <TD nowrap width="100" title="Année à laquelle la masse a été prise en compte">Année Masse</TD>
				            <TD nowrap width="220" title="Année à laquelle la masse a été prise en compte">
				              <input type="text" name="forAnneeMasse" size="10" maxlength="4" value=""/>
				            </TD>
	            			<TD nowrap width="100" title="Année à laquelle la masse sera redistribuée">Année de redistribution</TD>
	            			<TD nowrap width="220"  title="Année à laquelle la masse sera redistribuée"> 
								<input name="forAnneeRedistri" type="text" size="10" value="">
							</TD>
          				</TR>
          				<TR>
          					<TD nowrap width="100" height="31">Pour </TD>
							<TD nowrap width="180">
								<ct:FWPopupList name="forNumAffilie" 
									value="" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
								/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffiliationId').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
            				<TD nowrap width="100" height="31">Masse </TD>
            				<TD nowrap width="220">
								<INPUT type="text" name="fromMasse" size="20" value="" tabindex="-1">
	     					</TD>
	     					<TD nowrap width="120" height="31">Taux Forcé</TD>
				            <TD nowrap width="220">
								<SELECT style="width:60%" name="forTauxForce">
									<OPTION value="all"></OPTION>
									<OPTION value="force">Taux forcé</OPTION>
									<OPTION value="nonForce">Taux non forcé</OPTION>
								</SELECT>
							</TD>
          				</TR>
          				<TR>
							<TD nowrap width="100" height="31">Etat</TD>
				            <TD nowrap width="180">
								<ct:FWCodeSelectTag 
		               				name="forEtatTaxe" 
									defaut="848002"
									codeType="VEETATAXE"
									wantBlank="true"/> 
							</TD>
							<TD nowrap width="100" height="31">Motif de fin</TD>
	            			<TD nowrap width="220"> 
								<ct:FWCodeSelectTag 
		               				name="forMotifFin" 
									defaut=""
									codeType="VEMOTIFFIN"
									wantBlank="true"/> 
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