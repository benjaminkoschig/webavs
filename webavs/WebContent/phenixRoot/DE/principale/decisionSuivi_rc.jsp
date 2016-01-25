<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
idEcran="CCP0025";
globaz.phenix.db.principale.CPDecisionListerViewBean viewBean = (globaz.phenix.db.principale.CPDecisionListerViewBean)session.getAttribute ("viewBean");
bButtonFind = true;
bButtonNew= false;
rememberSearchCriterias=true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Verfügung - Suchen"
usrAction = "phenix.principale.decisionSuivi.lister";
servlet = "phenix";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Verfügung suchen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="120" align="left">Mitglied-Nr.</TD>
            <TD nowrap ><INPUT type="text" name="likeNumAffilie"  class="libelleLong"'></TD> 
            <TD width="10"></TD>
            <TD nowrap width="100" align="left">Jahr</TD>
            <TD nowrap ><INPUT type="text" name="forAnnee" class="numeroCourt"></TD>
            <TD width="10"></TD>
            <TD nowrap width="100">Typ</TD>
            <TD nowrap width="200"><ct:FWCodeSelectTag name="forTypeDecision"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPTYPDECIS"
		       	/>
            </TD>
          </TR>
          <TR> 
            <TD nowrap width="120" align="left">Name</TD>
            <TD nowrap ><INPUT type="text" name="likeNom"  class="libelleLong"'></TD> 
            <TD width="10"></TD>
            <TD nowrap width="100" align="left">Vorname</TD>
            <TD nowrap ><INPUT type="text" name="likePrenom" class="libellelong"></TD>
            <TD width="10"></TD>
            <TD nowrap width="100">Status</TD>
            <TD nowrap><ct:FWCodeSelectTag name="forEtat"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPETADECIS"
		       	/>
            </TD>
          </TR>
          <TR> 
            <TD nowrap width="120" align="left">Massgebendes Vermögen</TD>
            <TD nowrap ><INPUT type="text" name="forRevenuFortune"  class="montant"'></TD> 
            <TD width="10"></TD>
            <TD nowrap width="100" align="left">Jährliche Beiträge</TD>
            <TD nowrap ><INPUT type="text" name="forCotisation" class="montant"></TD>
            <TD width="10"></TD>
            <TD nowrap width="100">Art</TD>
           <TD nowrap>
            <%
			java.util.HashSet except = new java.util.HashSet();
			except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
			except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
			%>
            <ct:FWCodeSelectTag name="forGenreAffilie"
				defaut=""
				wantBlank="<%=true%>"
			    codeType="CPGENDECIS"
			    except="<%=except%>"
				/>
            </TD>
          </TR>
          <TR> 
            <TD nowrap width="120" align="left">Job-Nr.</TD>
            <TD nowrap> 
              <INPUT type="text" name="idPassage" maxlength="15" size="15" value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionSuivi_rc.jsp";	
			%>
			<!--
			-->
			<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
            <ct:FWSelectorTag 
			name="passageSelector" 
			
			methods="<%=psgMethodsName%>"
			providerPrefix="FA"			
			providerApplication ="musca"			
			providerAction ="musca.facturation.passage.chercher"			
			providerActionParams ="<%=psgParams%>"
			redirectUrl="<%=redirectUrl%>"	
			target="fr_main"			
			/> 
			</ct:ifhasright>
			<input type="hidden" name="selectorName" value="">
			</TD>
            <TD height="20" width=""></TD>
            <TD nowrap></TD>
            <TD></TD>
            <TD nowrap align="left"></TD>
            <TD nowrap ></TD> 
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