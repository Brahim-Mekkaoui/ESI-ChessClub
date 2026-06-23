<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    ma.ac.esi.chessclub.model.User currentUser =
        (ma.ac.esi.chessclub.model.User) session.getAttribute("user");
    boolean isLoggedIn = currentUser != null;
    boolean isAdmin    = isLoggedIn && "ADMIN".equals(currentUser.getRole());
    String  cp         = request.getContextPath();

    String successMsg = (String) session.getAttribute("successMessage");
    String errorMsg   = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");

    String uri = request.getRequestURI();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club</title>
    <link rel="stylesheet" href="<%= cp %>/css/style.css">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>♟</text></svg>">
</head>
<body>

<nav class="navbar">
    <div class="navbar-container">
<a class="navbar-brand" href="<%= cp %>/home" style="display:flex; align-items:center; gap:10px;">
    <img src="<%= cp %>/img/logo.jpg" alt="Logo" style="height:40px;">
    ESI Chess Club
</a>
        <ul class="navbar-nav">
            <li><a class="nav-link <%= uri.contains("/home") ? "active" : "" %>"
                   href="<%= cp %>/home">Accueil</a></li>
            <li><a class="nav-link <%= uri.contains("/tournaments") ? "active" : "" %>"
                   href="<%= cp %>/tournaments">Tournois</a></li>
            <li><a class="nav-link <%= uri.contains("/puzzle") ? "active" : "" %>"
                   href="<%= cp %>/puzzle">Puzzle du Jour</a></li>
            <li><a class="nav-link <%= uri.contains("/leaderboard") ? "active" : "" %>"
                   href="<%= cp %>/leaderboard">Classement</a></li>
            <li><a class="nav-link <%= uri.contains("/news") && !uri.contains("admin") ? "active" : "" %>"
                   href="<%= cp %>/news">Actualités</a></li>

            <% if (isLoggedIn) { %>
                <% if (isAdmin) { %>
                    <li class="nav-dropdown">
                        <a class="nav-link" style="color:var(--accent-gold);">⚙ Admin ▾</a>
                        <div class="dropdown-menu">
                            <a href="<%= cp %>/admin/tournament/create">Nouveau tournoi</a>
                            <a href="<%= cp %>/admin/news/create">Publier actualité</a>
                        </div>
                    </li>
                <% } %>
                <li class="nav-dropdown">
                    <a class="nav-link" href="<%= cp %>/user">
                        👤 <%= currentUser.getUsername() %> ▾
                    </a>
                    <div class="dropdown-menu">
                        <a href="<%= cp %>/user">Mon profil</a>
                        <a href="<%= cp %>/logout">Se déconnecter</a>
                    </div>
                </li>
            <% } else { %>
                <li><a class="nav-link" href="<%= cp %>/login">Connexion</a></li>
                <li><a class="btn btn-primary btn-sm" style="margin-left:8px;"
                       href="<%= cp %>/register">S'inscrire</a></li>
            <% } %>
        </ul>
    </div>
</nav>

<% if (successMsg != null) { %>
<div class="alert alert-success alert-global"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
<div class="alert alert-error alert-global"><%= errorMsg %></div>
<% } %>

<div class="main-content">
