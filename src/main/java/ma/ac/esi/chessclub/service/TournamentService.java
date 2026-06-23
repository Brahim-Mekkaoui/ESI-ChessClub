package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.Match;
import ma.ac.esi.chessclub.model.Tournament;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.repository.MatchRepository;
import ma.ac.esi.chessclub.repository.TournamentRepository;
import ma.ac.esi.chessclub.repository.UserRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Service métier pour la gestion des tournois.
 */
public class TournamentService {

    private final TournamentRepository tournamentRepository = new TournamentRepository();
    private final MatchRepository matchRepository           = new MatchRepository();
    private final UserRepository userRepository             = new UserRepository();

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.getAllTournaments();
    }

    public List<Tournament> getTournamentsByStatus(String status) {
        return tournamentRepository.getTournamentsByStatus(status);
    }

    public Tournament getTournamentWithMatches(int id) {
        Tournament tournament = tournamentRepository.getTournamentById(id);
        if (tournament == null) return null;
        tournament.setMatches(matchRepository.getMatchesByTournament(id));
        return tournament;
    }

    /**
     * Crée un tournoi avec validation des données.
     * @return l'ID du tournoi créé, ou -1 si erreur
     */
    public int createTournament(String name, String description, String format,
                                 String startDateStr, String endDateStr,
                                 String location, int organizerId) {
        return createTournament(name, description, format, startDateStr, endDateStr, location, organizerId, 8);
    }

    public int createTournament(String name, String description, String format,
                                 String startDateStr, String endDateStr,
                                 String location, int organizerId, int maxPlayers) {
        if (name == null || name.trim().isEmpty()) return -1;
        if (!isValidFormat(format)) return -1;

        Tournament t = new Tournament();
        t.setName(name.trim());
        t.setDescription(description);
        t.setFormat(format);
        t.setOrganizerId(organizerId);
        t.setLocation(location);
        t.setMaxPlayers(maxPlayers);

        try {
            if (startDateStr != null && !startDateStr.isEmpty())
                t.setStartDate(java.sql.Date.valueOf(startDateStr));
            if (endDateStr != null && !endDateStr.isEmpty())
                t.setEndDate(java.sql.Date.valueOf(endDateStr));
        } catch (IllegalArgumentException e) {
            // dates optionnelles
        }

        return tournamentRepository.insertTournament(t);
    }

    public boolean updateTournament(Tournament t) {
        return tournamentRepository.updateTournament(t);
    }

    public boolean updateStatus(int id, String status) {
        return tournamentRepository.updateTournamentStatus(id, status);
    }

    public boolean addParticipant(int tournamentId, int userId) {
        return tournamentRepository.addParticipant(tournamentId, userId);
    }

    /**
     * Génère automatiquement les matchs pour un tournoi Round Robin.
     * Chaque participant affronte tous les autres participants une fois.
     */
    public List<Match> generateRoundRobinMatches(int tournamentId) {
        List<User> participants = userRepository.getParticipants(tournamentId);
        List<Match> matches = new ArrayList<>();
        int round = 1;

        for (int i = 0; i < participants.size(); i++) {
            for (int j = i + 1; j < participants.size(); j++) {
                Match match = new Match();
                match.setTournamentId(tournamentId);
                match.setWhitePlayerId(participants.get(i).getId());
                match.setBlackPlayerId(participants.get(j).getId());
                match.setRoundNumber(round++);
                matchRepository.insertMatch(match);
                matches.add(match);
            }
        }
        return matches;
    }

    private boolean isValidFormat(String format) {
        return "ROUND_ROBIN".equals(format) ||
               "SWISS".equals(format) ||
               "SINGLE_ELIMINATION".equals(format);
    }
}
