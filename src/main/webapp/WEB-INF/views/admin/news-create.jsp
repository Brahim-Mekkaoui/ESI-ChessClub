<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Publier une actualité</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">

    <a href="${pageContext.request.contextPath}/news" class="back-link">← Retour aux actualités</a>
    <div class="page-header"><h1>📰 Publier une actualité</h1></div>

    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

    <div class="card" style="max-width:640px;margin:0 auto;">
        <form method="post" action="${pageContext.request.contextPath}/admin/news/create" class="edit-form">
            <div class="form-group">
                <label for="title">Titre *</label>
                <input type="text" id="title" name="title" class="form-control"
                       placeholder="Titre de l'actualité" required>
            </div>
            <div class="form-group">
                <label for="content">Contenu *</label>
                <textarea id="content" name="content" class="form-control" rows="8"
                          placeholder="Rédigez votre actualité..." required></textarea>
            </div>
            <div style="display:flex;gap:12px;margin-top:8px;">
                <button type="submit" class="btn btn-primary">Publier</button>
                <a href="${pageContext.request.contextPath}/news" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>

</div>
<%@ include file="../footer.jsp" %>
