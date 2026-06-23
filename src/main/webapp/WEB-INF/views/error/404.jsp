<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>ESI Chess Club — Page introuvable</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">404</div>
        <div class="error-icon">♟</div>
        <h1>Page introuvable</h1>
        <p>La page que vous cherchez n'existe pas ou a été déplacée.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Retour à l'accueil</a>
    </div>
</div>
<%@ include file="../footer.jsp" %>
