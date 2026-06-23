<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="header.jsp"/>
<%
    ma.ac.esi.chessclub.model.Puzzle puzzle =
        (ma.ac.esi.chessclub.model.Puzzle) request.getAttribute("puzzle");
    Boolean alreadySolved = (Boolean) request.getAttribute("alreadySolved");
    Boolean correct = (Boolean) request.getAttribute("correct");
    String submittedMove = (String) request.getAttribute("submittedMove");
    String feedbackMessage = (String) request.getAttribute("feedbackMessage");
    String cp = request.getContextPath();
%>

<link rel="stylesheet" href="<%= cp %>/css/chess/chessboard-1.0.0.min.css">

<div class="container">
    <h2>🧩 Puzzle du Jour</h2>
    <p class="text-muted">Entraînez-vous avec un puzzle quotidien — trouvez le meilleur coup !</p>

    <% if (feedbackMessage != null) { %>
        <div class="alert <%= (correct != null && correct) ? "alert-success" : "alert-error" %>">
            <%= feedbackMessage %>
        </div>
    <% } %>

    <% if (puzzle != null) { %>
        <div class="puzzle-grid">
            <div class="puzzle-board">
                <div id="chessboard" style="width: 400px; max-width: 100%;"></div>
                <div class="puzzle-controls" style="margin-top: 10px; display: flex; gap: 10px; flex-wrap: wrap;">
                    <button class="btn btn-sm" onclick="resetBoard()">🔄 Réinitialiser</button>
                    <button class="btn btn-sm" onclick="flipBoard()">🔃 Retourner</button>
                    <button class="btn btn-primary btn-sm" onclick="submitMove()">✅ Valider le coup</button>
                </div>
            </div>
            <div class="puzzle-info">
<div class="turn-indicator" style="text-align:center; margin-bottom:10px; font-size:1.2rem; font-weight:bold;">
    <% 
        String fen = puzzle.getFenPosition();
        String turn = fen.contains(" b ") ? "Noirs" : "Blancs";
    %>
    🔄 Trait aux <span style="color:<%= turn.equals("Blancs") ? "#fff" : "#000" %>; background:<%= turn.equals("Blancs") ? "#333" : "#eee" %>; padding:5px 15px; border-radius:20px;"><%= turn %></span>
</div>
                <div class="card">
                    <h3>Informations</h3>
                    <p><strong>Difficulté :</strong> 
                        <span class="badge badge-<%= puzzle.getDifficultyColor() %>">
                            <%= puzzle.getDifficultyLabel() %> (<%= puzzle.getDifficulty() %>/9)
                        </span>
                    </p>
                    <% if (puzzle.getTheme() != null) { %>
                        <p><strong>Thème :</strong> <%= puzzle.getTheme() %></p>
                    <% } %>
                    <% if (puzzle.isFetchedFromApi()) { %>
                        <p class="text-success">✅ Puzzle Lichess original</p>
                    <% } else { %>
                        <p class="text-warning">⚠️ Puzzle de secours (API indisponible)</p>
                    <% } %>
                </div>

                <div class="card">
                    <h3>Comment jouer</h3>
                    <p>Déplacez la pièce sur l'échiquier, puis cliquez sur <strong>"Valider le coup"</strong>.</p>
                    <p><small>Le chronomètre démarre au chargement de la page.</small></p>
                </div>

                <% if (alreadySolved != null && alreadySolved) { %>
                    <div class="card card-success">
                        <p>🎉 Vous avez déjà résolu ce puzzle aujourd'hui !</p>
                    </div>
                <% } %>
            </div>
        </div>
    <% } else { %>
        <div class="alert alert-error">Puzzle non disponible aujourd'hui.</div>
    <% } %>
</div>

<script src="/chessclub/js/jquery-3.7.1.min.js"></script>
<script src="<%= cp %>/js/chess.min.js"></script>
<script src="<%= cp %>/js/chessboard-1.0.0.min.js"></script>
<script>
    const game = new Chess('<%= puzzle != null ? puzzle.getFenPosition() : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" %>');
    let board = null;
    let startTime = Date.now();
    let moveFrom = null;

    const config = {
	        pieceTheme:'https://chessboardjs.com/img/chesspieces/wikipedia/{piece}.png',
        draggable: true,
        position: '<%= puzzle != null ? puzzle.getFenPosition() : "start" %>',
        onDragStart: function(source, piece, position, orientation) {
            <% if (alreadySolved != null && alreadySolved) { %>
                return false;
            <% } %>
            if (game.game_over()) return false;
            if (game.turn() === 'b' && piece.search(/^b/) === -1) return false;
            if (game.turn() === 'w' && piece.search(/^w/) === -1) return false;
        },
        onDrop: function(source, target) {
            const move = game.move({
                from: source,
                to: target,
                promotion: 'q'
            });

            if (move === null) return 'snapback';
        },
        onSnapEnd: function() {
            board.position(game.fen());
        }
    };

    board = Chessboard('chessboard', config);

    function resetBoard() {
        game.load('<%= puzzle != null ? puzzle.getFenPosition() : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" %>');
        board.position('<%= puzzle != null ? puzzle.getFenPosition() : "start" %>');
        startTime = Date.now();
    }

    function flipBoard() {
        board.flip();
    }

    function submitMove() {
        const history = game.history({ verbose: true });
        if (history.length === 0) {
            alert("Faites d'abord un mouvement !");
            return;
        }

        const lastMove = history[history.length - 1];
        const moveUci = lastMove.from + lastMove.to;
        const timeTaken = Math.floor((Date.now() - startTime) / 1000);

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '<%= cp %>/puzzle';

        const fields = {
            puzzleId: '<%= puzzle != null ? puzzle.getId() : "" %>',
            move: moveUci,
            timeTaken: timeTaken
        };

        for (const [key, value] of Object.entries(fields)) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = value;
            form.appendChild(input);
        }

        document.body.appendChild(form);
        form.submit();
    }
</script>

<style>
    .puzzle-grid {
        display: flex;
        gap: 30px;
        flex-wrap: wrap;
        margin-top: 20px;
    }
    .puzzle-board {
        flex: 1;
        min-width: 400px;
    }
    .puzzle-info {
        flex: 0 0 300px;
    }
    .puzzle-controls {
        margin-top: 15px;
    }
    .card {
        background: white;
        padding: 20px;
        border-radius: 10px;
        margin-bottom: 15px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        color: #333;
    }
    .card h3 {
        margin-top: 0;
        color: #333;
    }
    .card p {
        color: #555;
    }
    .card-success {
        background: #e8f5e9;
        border-left: 4px solid var(--success);
    }
</style>

<jsp:include page="footer.jsp"/>