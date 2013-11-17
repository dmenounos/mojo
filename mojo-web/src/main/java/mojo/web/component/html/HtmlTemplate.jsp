<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="http://mojo.example.org/component" %>

<!DOCTYPE html>
<html>
	<head>

		<title>${bean.title}</title>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<c:forEach var="style" items="${bean.styles}">
			<link rel="stylesheet" href="${style}" />
		</c:forEach>

		<c:forEach var="script" items="${bean.scripts}">
			<script src="${script}"></script>
		</c:forEach>

		<script type="text/javascript">
			contextPath = '${contextPath}';
		</script>

	</head>
	<body>

		<m:child />

	</body>
</html>
