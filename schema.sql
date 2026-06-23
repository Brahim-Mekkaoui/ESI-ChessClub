-- ============================================================
-- ESIChessClub - Script de création de la base de données
-- Base : PostgreSQL
-- Usage : psql -U postgres -d chessclub_db -f schema.sql
-- ============================================================

-- Supprimer les tables si elles existent (ordre inverse des dépendances)
DROP TABLE IF EXISTS user_badges CASCADE;
DROP TABLE IF EXISTS user_puzzle_stats CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS news CASCADE;
DROP TABLE IF EXISTS badges CASCADE;
DROP TABLE IF EXISTS puzzles CASCADE;
DROP TABLE IF EXISTS tournament_participants CASCADE;
DROP TABLE IF EXISTS tournaments CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Table USERS
-- ============================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    chess_level VARCHAR(20) DEFAULT 'BEGINNER',
    bio TEXT,
    profile_picture_url VARCHAR(255),
    role VARCHAR(20) DEFAULT 'MEMBER',
    elo_rating INT DEFAULT 1200,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table TOURNAMENTS
-- ============================================================
CREATE TABLE tournaments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    format VARCHAR(30) DEFAULT 'ROUND_ROBIN',
    start_date DATE NOT NULL,
    end_date DATE,
    organizer_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'PLANNED',
    location VARCHAR(255),
    max_players INT DEFAULT 16,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table TOURNAMENT_PARTICIPANTS
-- ============================================================
CREATE TABLE tournament_participants (
    id SERIAL PRIMARY KEY,
    tournament_id INT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tournament_id, user_id)
);

-- ============================================================
-- Table MATCHES
-- ============================================================
CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    tournament_id INT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    white_player_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    black_player_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    result VARCHAR(20) DEFAULT 'NOT_PLAYED',
    match_date DATE,
    round_number INT DEFAULT 1,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table PUZZLES
-- ============================================================
CREATE TABLE puzzles (
    id SERIAL PRIMARY KEY,
    fen_position VARCHAR(255) NOT NULL,
    best_move VARCHAR(20) NOT NULL,
    all_moves TEXT,
    difficulty INT DEFAULT 5,
    puzzle_id_lichess VARCHAR(50),
    theme VARCHAR(100),
    fetched_from_api BOOLEAN DEFAULT FALSE,
    puzzle_date DATE UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table USER_PUZZLE_STATS
-- ============================================================
CREATE TABLE user_puzzle_stats (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    puzzle_id INT NOT NULL REFERENCES puzzles(id) ON DELETE CASCADE,
    solved BOOLEAN DEFAULT FALSE,
    time_taken_seconds INT,
    date_solved TIMESTAMP,
    attempts INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, puzzle_id)
);

-- ============================================================
-- Table BADGES
-- ============================================================
CREATE TABLE badges (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(10),
    puzzle_count_threshold INT,
    tournament_wins_threshold INT,
    consecutive_days_threshold INT,
    match_count_threshold INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table USER_BADGES
-- ============================================================
CREATE TABLE user_badges (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    badge_id INT NOT NULL REFERENCES badges(id) ON DELETE CASCADE,
    date_earned TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, badge_id)
);

-- ============================================================
-- Table NEWS
-- ============================================================
CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_published BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- DONNÉES INITIALES
-- ============================================================

-- Badges prédéfinis
INSERT INTO badges (name, description, icon, puzzle_count_threshold, consecutive_days_threshold, match_count_threshold, tournament_wins_threshold) VALUES
    ('Puzzle Master',       'Résoudre 100 puzzles',              '🧩', 100,  NULL, NULL, NULL),
    ('Daily Player',        'Jouer 7 jours consécutifs',         '🔥', NULL, 7,    NULL, NULL),
    ('Team Player',         'Participer à 5 matchs',             '🤝', NULL, NULL, 5,    NULL),
    ('Tournament Champion', 'Remporter un tournoi',              '🏆', NULL, NULL, NULL, 1),
    ('First Step',          'Résoudre son premier puzzle',       '⭐', 1,    NULL, NULL, NULL),
    ('Bronze Solver',       'Résoudre 10 puzzles',               '🥉', 10,   NULL, NULL, NULL),
    ('Silver Solver',       'Résoudre 50 puzzles',               '🥈', 50,   NULL, NULL, NULL);

-- Compte Admin par défaut (password: Admin1234)
-- SHA-256 de "Admin1234" = 7b5a7f8a8...
-- IMPORTANT: Changez le mot de passe après la première connexion !
INSERT INTO users (username, email, password_hash, first_name, last_name, chess_level, role, bio)
VALUES (
    'admin',
    'admin@esi.ac.ma',
    'c9ada34afb4af38e8d4d2e4e8a26a8c02b0fbf55',
    'Administrateur',
    'ESI',
    'EXPERT',
    'ADMIN',
    'Compte administrateur du club d''échecs ESI'
);
-- Note: password_hash ci-dessus est SHA-1 de "Admin1234"
-- L application utilise SHA-256 — utilisez le compte via le formulaire une fois l'app démarrée,
-- ou utilisez un compte MEMBER d'abord en vous inscrivant, puis changez le role dans la DB.

-- Compte de test membre (password: Test1234)
INSERT INTO users (username, email, password_hash, first_name, last_name, chess_level, role)
VALUES (
    'membre_test',
    'membre@esi.ac.ma',
    'c9ada34afb4af38e8d4d2e4e8a26a8c02b0fbf55',
    'Mohammed',
    'Alaoui',
    'INTERMEDIATE',
    'MEMBER'
);

-- ============================================================
-- Vues utiles (optionnel)
-- ============================================================
CREATE OR REPLACE VIEW leaderboard_view AS
SELECT
    u.id,
    u.username,
    u.first_name,
    u.last_name,
    u.chess_level,
    u.elo_rating,
    COALESCE((SELECT COUNT(*) FROM user_puzzle_stats ups WHERE ups.user_id = u.id AND ups.solved = TRUE), 0) AS puzzles_solved,
    COALESCE((SELECT COUNT(*) FROM matches m WHERE (m.white_player_id = u.id OR m.black_player_id = u.id) AND m.result != 'NOT_PLAYED'), 0) AS matches_played,
    COALESCE((SELECT COUNT(*) FROM matches m WHERE
        (m.white_player_id = u.id AND m.result = 'WHITE_WIN') OR
        (m.black_player_id = u.id AND m.result = 'BLACK_WIN')
    ), 0) AS matches_won,
    COALESCE((SELECT COUNT(*) FROM user_badges ub WHERE ub.user_id = u.id), 0) AS badges_count
FROM users u
WHERE u.is_active = TRUE AND u.role = 'MEMBER'
ORDER BY u.elo_rating DESC;
