<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<p class="exception">
	<fmt:message key="${exception.key}">
		<c:forEach items="${exception.params}" var="param">
			<fmt:param value="${param}" />
		</c:forEach>
	</fmt:message>
</p>

<!--
Exception: ${exception.message}
<c:forEach items="${exception.stackTrace}" var="ste">
	${ste}
</c:forEach>
-->
