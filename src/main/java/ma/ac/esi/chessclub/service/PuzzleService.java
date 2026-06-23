package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.Puzzle;
import ma.ac.esi.chessclub.model.Badge;
import ma.ac.esi.chessclub.repository.PuzzleRepository;
import ma.ac.esi.chessclub.repository.UserPuzzleStatsRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des puzzles quotidiens.
 * Coordonne l'API Lichess, la persistance et les statistiques.
 */
public class PuzzleService {

    private final PuzzleRepository          puzzleRepo  = new PuzzleRepository();
    private final UserPuzzleStatsRepository statsRepo   = new UserPuzzleStatsRepository();
    private final LichessAPIService         lichessAPI  = new LichessAPIService();
    private final BadgeService              badgeService = new BadgeService();

    /**
     * Récupère le puzzle du jour.
     * Stratégie : chercher en base d'abord, sinon appeler Lichess API.
     *
     * @return le puzzle du jour (jamais null si l'API est accessible)
     */
    public Puzzle getDailyPuzzle() {
        LocalDate today = LocalDate.now();

        // 1. Chercher le puzzle d'aujourd'hui en base
        Puzzle puzzle = puzzleRepo.getPuzzleByDate(today);
        if (puzzle != null) {
            return puzzle;
        }

        // 2. Appeler l'API Lichess
        try {
            puzzle = lichessAPI.getDailyPuzzle();
            if (puzzle != null) {
                puzzle.setPuzzleDate(java.sql.Date.valueOf(today));
                int id = puzzleRepo.insertPuzzle(puzzle);
                puzzle.setId(id);
                return puzzle;
            }
        } catch (Exception e) {
            System.err.println("[PuzzleService] Erreur API Lichess : " + e.getMessage());
        }

        // 3. Fallback : retourner un puzzle par défaut si API indisponible
        return createFallbackPuzzle(today);
    }

    /**
     * Soumet la réponse d'un utilisateur à un puzzle.
     *
     * @param userId    l'ID de l'utilisateur
     * @param puzzleId  l'ID du puzzle
     * @param move      le coup soumis par l'utilisateur
     * @param timeTaken le temps pris en secondes
     * @return true si le coup est correct
     */
    public boolean submitAnswer(int userId, int puzzleId, String move, int timeTaken) {
        Puzzle puzzle = puzzleRepo.getPuzzleById(puzzleId);
        if (puzzle == null) return false;

        boolean correct = puzzle.isCorrectMove(move);

        // Enregistrer la tentative
        statsRepo.upsertStats(userId, puzzleId, correct, timeTaken);

        // Si correct, vérifier les badges
        if (correct) {
            List<Badge> newBadges = badgeService.checkAndAwardBadges(userId);
            // Les nouveaux badges sont loggués par BadgeService
        }

        return correct;
    }

    /**
     * Vérifie si l'utilisateur a déjà résolu le puzzle du jour.
     */
    public boolean hasSolvedTodayPuzzle(int userId, int puzzleId) {
        return statsRepo.hasSolvedToday(userId, puzzleId);
    }

    /**
     * Puzzle de secours si l'API Lichess est indisponible.
     * Position célèbre : Gambit du Roi.
     */
    private Puzzle createFallbackPuzzle(LocalDate date) {
        Puzzle p = new Puzzle();
        p.setFenPosition("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
        p.setBestMove("Nc6");
        p.setAllMoves("Nc6");
        p.setDifficulty(3);
        p.setPuzzleIdLichess("fallback");
        p.setTheme("opening");
        p.setFetchedFromApi(false);
        p.setPuzzleDate(java.sql.Date.valueOf(date));

        int id = puzzleRepo.insertPuzzle(p);
        p.setId(id);
        return p;
    }
}
