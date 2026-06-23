<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>ESI Chess Club — Erreur serveur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">500</div>
        <div class="error-icon">♜</div>
        <h1>Erreur serveur</h1>
        <p>Une erreur inattendue s'est produite. Veuillez réessayer plus tard.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Retour à l'accueil</a>
    </div>
</div>
<%@ include file="../footer.jsp" %>
