package ma.ac.esi.chessclub.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ma.ac.esi.chessclub.model.Puzzle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Service d'intégration avec l'API Lichess.
 * Documentation : https://lichess.org/api
 *
 * Endpoints utilisés :
 *   GET https://lichess.org/api/puzzle/daily  → Puzzle du jour
 *   GET https://lichess.org/api/puzzle/random → Puzzle aléatoire
 */
public class LichessAPIService {

    private static final String LICHESS_API_URL = "https://lichess.org/api";
    private static final int    TIMEOUT_MS      = 5000; // 5 secondes

    /**
     * Récupère le puzzle du jour depuis l'API Lichess.
     *
     * Structure de la réponse JSON :
     * {
     *   "puzzle": {
     *     "id": "abc123",
     *     "fen": "...",
     *     "solution": ["e2e4", "e7e5"],
     *     "themes": ["opening"],
     *     "rating": 1500
     *   },
     *   "game": { ... }
     * }
     */
    public Puzzle getDailyPuzzle() throws Exception {
        return fetchPuzzle(LICHESS_API_URL + "/puzzle/daily");
    }

    /**
     * Récupère un puzzle aléatoire depuis l'API Lichess.
     */
    public Puzzle getRandomPuzzle() throws Exception {
        return fetchPuzzle(LICHESS_API_URL + "/puzzle/random");
    }

    // ============================================================
    // Méthode privée de récupération
    // ============================================================

    private Puzzle fetchPuzzle(String urlString) throws Exception {
        // Ouvrir la connexion HTTP
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "ESIChessClub/1.0 (educational project)");
        conn.setConnectTimeout(TIMEOUT_MS);
        conn.setReadTimeout(TIMEOUT_MS);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("API Lichess a retourné le code : " + responseCode);
        }

        // Lire la réponse
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            conn.disconnect();
        }

        // Parser le JSON
        return parseJsonToPuzzle(response.toString());
    }

    /**
     * Parse la réponse JSON de l'API Lichess en objet Puzzle.
     */
    private Puzzle parseJsonToPuzzle(String jsonStr) {
        JsonObject root = JsonParser.parseString(jsonStr).getAsJsonObject();
        JsonObject puzzleJson = root.getAsJsonObject("puzzle");

        Puzzle puzzle = new Puzzle();

        // ID Lichess
        if (puzzleJson.has("id")) {
            puzzle.setPuzzleIdLichess(puzzleJson.get("id").getAsString());
        }

        // Position FEN
        if (puzzleJson.has("fen")) {
            puzzle.setFenPosition(puzzleJson.get("fen").getAsString());
        }

        // Solution (liste de coups)
        if (puzzleJson.has("solution")) {
            JsonArray solution = puzzleJson.getAsJsonArray("solution");
            List<String> moves = new ArrayList<>();
            for (JsonElement el : solution) {
                moves.add(el.getAsString());
            }
            if (!moves.isEmpty()) {
                puzzle.setBestMove(moves.get(0));
                puzzle.setAllMoves(String.join(" ", moves));
            }
        }

        // Difficulté (rating Lichess → échelle 1-9)
        if (puzzleJson.has("rating")) {
            int rating = puzzleJson.get("rating").getAsInt();
            puzzle.setDifficulty(ratingToDifficulty(rating));
        } else if (puzzleJson.has("difficulty") && !puzzleJson.get("difficulty").isJsonNull()) {
            puzzle.setDifficulty(puzzleJson.get("difficulty").getAsInt());
        } else {
            puzzle.setDifficulty(5);
        }

        // Thèmes
        if (puzzleJson.has("themes")) {
            JsonArray themes = puzzleJson.getAsJsonArray("themes");
            if (themes.size() > 0) {
                puzzle.setTheme(themes.get(0).getAsString());
            }
        }

        puzzle.setFetchedFromApi(true);
        return puzzle;
    }

    /**
     * Convertit le rating Lichess (500-3000) en difficulté 1-9.
     */
    private int ratingToDifficulty(int rating) {
        if (rating < 800)  return 1;
        if (rating < 1000) return 2;
        if (rating < 1200) return 3;
        if (rating < 1400) return 4;
        if (rating < 1600) return 5;
        if (rating < 1800) return 6;
        if (rating < 2000) return 7;
        if (rating < 2200) return 8;
        return 9;
    }
}
