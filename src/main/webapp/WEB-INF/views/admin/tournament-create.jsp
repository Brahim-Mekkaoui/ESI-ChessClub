<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Créer un tournoi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">

    <a href="${pageContext.request.contextPath}/tournaments" class="back-link">← Retour aux tournois</a>
    <div class="page-header"><h1>🏆 Nouveau Tournoi</h1></div>

    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

    <div class="card" style="max-width:600px;margin:0 auto;">
        <form method="post" action="${pageContext.request.contextPath}/admin/tournament/create" class="edit-form">
            <div class="form-group">
                <label for="name">Nom du tournoi *</label>
                <input type="text" id="name" name="name" class="form-control"
                       placeholder="ex: Championnat ESI 2024" required>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" class="form-control" rows="3"
                          placeholder="Décrivez le tournoi..."></textarea>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="format">Format *</label>
                    <select id="format" name="format" class="form-control">
                        <option value="ROUND_ROBIN">Round Robin (toutes rondes)</option>
                        <option value="SWISS">Système Suisse</option>
                        <option value="SINGLE_ELIMINATION">Élimination directe</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="maxPlayers">Joueurs max *</label>
                    <input type="number" id="maxPlayers" name="maxPlayers" class="form-control"
                           min="2" max="64" value="8" required>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="startDate">Date de début</label>
                    <input type="date" id="startDate" name="startDate" class="form-control">
                </div>
                <div class="form-group">
                    <label for="endDate">Date de fin</label>
                    <input type="date" id="endDate" name="endDate" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label for="location">Lieu</label>
                <input type="text" id="location" name="location" class="form-control"
                       placeholder="ex: Salle informatique B21">
            </div>
            <div style="display:flex;gap:12px;margin-top:8px;">
                <button type="submit" class="btn btn-primary">Créer le tournoi</button>
                <a href="${pageContext.request.contextPath}/tournaments" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>

</div>
<%@ include file="../footer.jsp" %>
