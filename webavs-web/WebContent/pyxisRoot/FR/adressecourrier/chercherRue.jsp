<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.templates.*"%>
<HEAD>
<script>
	function swap (str){
		var array = str.split(",");

		if (array.length== 2) {
			space =" ";
			if ((array[1].indexOf("'",array[1].length-1)>0) || (array[1].indexOf("-",array[1].length-1)>0)) {
				space="";
			}

			return (  array[1].substring(1,2).toUpperCase()+array[1].substring(2)+space+array[0]    );
			
			
		}else {
			return str;
		}
	}
</script>
<style>

A:link { color:#000099; text-decoration:none } 
A:visited { color:#000099; text-decoration:none } 
A:active { color:#000099 ; text-decoration:none} 
A:hover { color:#FF0000 ; text-decoration:underline }

input {border : solid 1px white;background-color:silver}
</style>

</HEAD>


<body>
	<%
		String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
		String rueUrl = request.getContextPath()+"/pyxisRoot/"+languePage+"/adressecourrier/chercherRue.jsp?npa="+request.getParameter("npa")+"&type=startwith&returnField=&rue=";
	%>
<div style="overflow-y:scroll;height:100%;width:100%">	
<table width="100%" cellpadding="0" cellspacing="1">

<%if ("".equals(request.getParameter("returnField"))) {%>
<tr  style="background-color:#f0f0f0;position:relative; top:expression(this.offsetParent.scrollTop );left: 0">
	<th>&nbsp;</th>
	<th align="left"  >
		<b>
		<%if (request.getParameter("npa").length()>=4) {
			pageContext.getOut().write(request.getParameter("npa").substring(0,3)+"x");
			} else {
			pageContext.getOut().write(request.getParameter("npa"));
			}
		
		
		%>
		</b>
	</th>
	
	<th align="left" >
		<div style="text-align:center">
		<a href="<%=rueUrl+"A"%>">A</a>
		<a href="<%=rueUrl+"B"%>">B</a>
		<a href="<%=rueUrl+"C"%>">C</a>
		<a href="<%=rueUrl+"D"%>">D</a>
		<a href="<%=rueUrl+"E"%>">E</a>
		<a href="<%=rueUrl+"F"%>">F</a>
		<a href="<%=rueUrl+"G"%>">G</a>
		<a href="<%=rueUrl+"H"%>">H</a>
		<a href="<%=rueUrl+"I"%>">I</a>
		<a href="<%=rueUrl+"J"%>">J</a>
		<a href="<%=rueUrl+"K"%>">K</a>
		<a href="<%=rueUrl+"L"%>">L</a>
		<a href="<%=rueUrl+"M"%>">M</a>
		<a href="<%=rueUrl+"N"%>">N</a><br>
		<a href="<%=rueUrl+"O"%>">O</a>
		<a href="<%=rueUrl+"P"%>">P</a>
		<a href="<%=rueUrl+"Q"%>">Q</a>
		<a href="<%=rueUrl+"R"%>">R</a>
		<a href="<%=rueUrl+"S"%>">S</a>
		<a href="<%=rueUrl+"T"%>">T</a>
		<a href="<%=rueUrl+"U"%>">U</a>
		<a href="<%=rueUrl+"V"%>">V</a>
		<a href="<%=rueUrl+"W"%>">W</a>
		<a href="<%=rueUrl+"X"%>">X</a>
		<a href="<%=rueUrl+"Y"%>">Y</a>
		<a href="<%=rueUrl+"Z"%>">Z</a>
		</div>
		
	</th >
	<th width="*">&nbsp;
	</th>
</tr>


<%}%>

	

	<% 

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession gSession = (globaz.globall.db.BSession)controller.getSession();
	
	globaz.pyxis.db.adressecourrier.TIRueManager manager = new globaz.pyxis.db.adressecourrier.TIRueManager();
	manager.setSession(gSession);
	
	String rue = request.getParameter("rue").toUpperCase();
	String npa = request.getParameter("npa");
	if (npa == null) {npa="";}else {npa = npa.trim();}
	if (rue == null) {rue="";}else {rue = rue.trim();}
	
	manager.setForNomMajusculeLike(rue);
	manager.setForNpaAdressageLike(npa);
	
	if (gSession.hasErrors()) {
		System.out.println(gSession.getErrors().toString());
	}
		
	try {
		
		manager.find();
								
		
	
				for (int i=0;i<manager.size();i++) {
					globaz.pyxis.db.adressecourrier.TIRue rec = (globaz.pyxis.db.adressecourrier.TIRue) manager.getEntity(i);
	
					String selectString="";
					String selectStringFocus ="";
					if (!"".equals(request.getParameter("returnField"))) {
						selectString ="parent.document.getElementsByName('"+request.getParameter("returnField")+"')[0].value= swap('"+FWTagBuilder.processAmps(rec.getNom()).trim()+"')+','+"+FWTagBuilder.processAmps(rec.getNpaAdressage()).trim();
						if (!"".equals(request.getParameter("numero")))	 {
							selectString+="+','+"+request.getParameter("numero");
						}
						selectString+=";";
						
						selectStringFocus = selectString+"parent.document.getElementsByName('"+request.getParameter("returnField")+"')[0].focus()";
					} else {
						selectString ="parent.document.getElementsByName('rue')[0].value= swap('"+FWTagBuilder.processAmps(rec.getNom()).trim()+"');";
						selectString +="parent.document.getElementsByName('rueRepertoire')[0].value='"+FWTagBuilder.processAmps(rec.getNom()).trim()+"';";
						selectString +="parent.document.getElementsByName('localiteCode')[0].value='"+FWTagBuilder.processAmps(rec.getNpaAdressage()).trim()+"' ;";
						selectString +="parent.document.getElementsByName('rue')[0].style.color='green';";
						selectString +="parent.document.getElementsByName('localiteCode')[0].style.color='green' ;";
						
						selectStringFocus = selectString+"parent.document.getElementsByName('rue')[0].focus()";
						
					}
					
					String access ="";
					pageContext.getOut().write("<tr>");
					if (i<9) {
						access = " accesskey='"+(i+1)+"' ";
						pageContext.getOut().write("<td><input "+access+" type='button' value='"+(i+1)+"' onclick=\""+selectStringFocus+"\">&nbsp;</td>");
							
					} else {
						pageContext.getOut().write("<td></td>");
					}
					
					pageContext.getOut().write("<td><b>"+rec.getNpaAdressage().substring(0,4)+"</b> "+rec.getNpaAdressage().substring(4)+"</td><td><a  href='#' onclick=\""+selectStringFocus+"\">"+rec.getNom()+"</a></td></tr>");
					
					if (manager.size() == 1) {
						pageContext.getOut().write("<script>");
						pageContext.getOut().write(selectString);
						pageContext.getOut().write("</script>");
						
					} 
					
				}
		
		if (manager.size()>0) {
			pageContext.getOut().write("<script>");
			
			if (!"".equals(request.getParameter("returnField"))) {
				pageContext.getOut().write("parent.document.getElementsByName('"+request.getParameter("returnField")+"')[0].focus();");
			
			} else {
				pageContext.getOut().write("parent.document.getElementsByName('rue')[0].focus();");
			
			}
			pageContext.getOut().write("</script>");
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	
	%>
	</table>	
	</div>	
</body>
</html>
