<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>

<html>
	<head>


		<title>${view.name}</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=ISO-8859-1">
		<title><fmt:message key="menu.title" />
		</title>
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/css/screen.css"></link>
		<script type="text/javascript">
function send(id) {
	document.forms['form2'].elements['id'].value = id;
	document.forms['form2'].submit();
}
function del(id) {
	document.forms['form3'].elements['id'].value = id;
	document.forms['form3'].submit();
}
</script>
	</head>
	<body onload="document.body.style.cursor='default'";>
		<table align="left" width="90%">
			<tbody>
				<tr>
					<td valign="top" align="left">
						<div style="">

							<form name="form2" id="form2" action="./get.html" method="post">
								<input name="id" id="id" value="" type="hidden">
								<input name="entity" id="entity" value="${entityName}"
									type="hidden">
							</form>
							<form name="form3" id="form3" action="./del.html" method="post">
								<input name="id" id="id" value="" type="hidden">
								<input name="entity" id="entity" value="${entityName}"
									type="hidden">
							</form>
						</div>
						<form name="form1" id="form1" action="./search.html" method="post">
							<input type="hidden" name="entity" id="entity"
								value="${entityName}">
							<h3>
								${name}
							</h3>
							<div align="left">
								<table border="0">
									<tr>
										<td valign="top">

										</td>
										<td valign="baseline">
											Filtro:
										</td>
										<td valign="baseline">
											<select id="filtro" name="filtro">
												<c:forEach items="${columns}" var="property"
													varStatus="propertyCount" begin="0">
													<option value="${property}">
														${property}
													</option>
												</c:forEach>
											</select>
										</td>
										<td valign="baseline">
											Valor:
										</td>
										<td valign="baseline">
											<input name="fltrValue" value="${fltrValue}">
										</td>
										<td valign="baseline">
											<input value="Consultar" type="submit">
										</td>
									</tr>
								</table>


							</div>
						</form>
						<c:set var="lastUrl" value="list.html" scope="request"/>
						
						<script type="text/javascript">
											<c:forEach items="${columns}" var="property"	varStatus="propertyCount" begin="0">
												<c:if test="${property == filtro}">
														document.getElementById("filtro").selectedIndex='${propertyCount.index}';
														<c:set var="lastUrl" value="search.html" scope="request"/>
												</c:if>

											</c:forEach>
											</script>
											<c:if test="${fn:length(objects)==50}">
											<i>La consulta contiene mas de 50 resultados, utilice uno de los filtros puestos a disposicion</i> 
											</c:if>
						<table>
							<tr>
								<td>
							
								
									<tagdisplay:table name="${objects}" requestURI="${lastUrl}"
										id="object" export="true" cellpadding="5" 
										size="100">
										<c:forEach items="${columns}" var="property"
											varStatus="propertyCount" begin="0">
											<c:choose>
												<c:when test='${propertyCount.index == 0 }'>
													<tagdisplay:column media='all' title="${property}">

														<a href="#" onclick="send('${object[property]}');">
															${object[property]}</a>
													</tagdisplay:column>
												</c:when>
												<c:otherwise>
													<tagdisplay:column media='all' title="${property}">
														${object[property]}
														</tagdisplay:column>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<tagdisplay:column media='all' title=".">
											<a href="#" onclick="del('${object[columns[0]]}');">
												Delete</a>
										</tagdisplay:column>

									</tagdisplay:table>
									<input type="button" onclick="send('');"
										value="Nuevo(a) ${name}">
								</td>

							</tr>
						</table>
				</tr>

				<tr>
					<td colspan="2">
						${message }
					</td>
				</tr>
			</tbody>
		</table>

	</body>

</html>
