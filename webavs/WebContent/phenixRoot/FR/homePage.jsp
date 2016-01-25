<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp"%>
<%@ page import="globaz.templates.*" %>

<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<%
String tmp="";
tmp = "phenixRoot/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/";
String logoSrc = tmp+"appLogo.jsp";
String labelSrc = tmp+"appLabel.jsp";
String iconSrc = request.getContextPath()+"/" +globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/appIcons.jsp?servlet=phenix";
String menuSrc = tmp+"appMenu.jsp";
String aboutSrc = tmp+"appAbout.jsp";
//String errorSrc = tmp+"appError.jsp";
String infoSrc = "appInfo.jsp";
String mainSrc = tmp+"appMain.jsp";
FWFrameworkFramesetInfo framesetInfo = new FWFrameworkFramesetInfo();
%>
<script>if (self != top){top.location.href=window.location.href;}</script>
</HEAD>
<%  
		String titleRow = "";
		if (framesetInfo.hasTitle()) {
			titleRow += framesetInfo.getTitleHeight() + ",";
		}
		response.getWriter().println("<script type=\"text/javascript\">if (self != top) {	top.location.href=window.location.href;}</script>");
		response.getWriter().println("<FRAMESET rows=" + titleRow + "*," + framesetInfo.getErrorHeight() + " border=0>");
		if (framesetInfo.hasTitle()) {
			response.getWriter().println("<FRAMESET cols=" + framesetInfo.getNaviWidth() + ",*," + framesetInfo.getAppIconsWidth() + " border=0>");
			response.getWriter().println("<FRAME name=fr_logo scrolling=no src=" + logoSrc + ">");
			response.getWriter().println("<FRAME name=fr_pagelabel scrolling=auto src=" + labelSrc + ">");
			response.getWriter().println("<FRAME name=fr_appicons scrolling=no src=" + iconSrc + ">");
			response.getWriter().println("</FRAMESET>");
		}
		response.getWriter().println("<FRAMESET cols=" + framesetInfo.getNaviWidth() + ",* border=0>");
		response.getWriter().println("<FRAME name=fr_menu src=" + menuSrc + ">");
		response.getWriter().println("<FRAME name=fr_main scrolling=auto src=" + mainSrc + ">");
		response.getWriter().println("</FRAMESET>");

		response.getWriter().println("<FRAMESET cols=" + framesetInfo.getNaviWidth() + ",* border=0>");



		response.getWriter().println("<FRAME name=fr_about scrolling=auto src=" + aboutSrc + ">");
		response.getWriter().println("<FRAME name=fr_error src=" + infoSrc + " scrolling=auto>");
		response.getWriter().println("</FRAMESET>");
		response.getWriter().println("<NOFRAMES><BODY bgcolor=#FFFFFF><P>L'affichage de cette page requiert un navigateur prenant en charge les cadres.</P></BODY></NOFRAMES>");
		response.getWriter().println("</FRAMESET>");
%>
</HTML>