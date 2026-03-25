-- Schema SQL pour EduAssist (MySQL / MariaDB)
-- Généré à partir des entités Java dans com.eduassist.model

-- Supprime les tables dans l'ordre inverse pour permettre re-création
DROP TABLE IF EXISTS `user_achievement`;
DROP TABLE IF EXISTS `user_stats`;
DROP TABLE IF EXISTS `study_session`;
DROP TABLE IF EXISTS `note`;
DROP TABLE IF EXISTS `suggestion_ia`;
DROP TABLE IF EXISTS `knowledge_node`;
DROP TABLE IF EXISTS `topic`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `achievement`;
DROP TABLE IF EXISTS `user`;

-- Table `user`
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nom` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `achievement`
CREATE TABLE `achievement` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `icone_url` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `course`
CREATE TABLE `course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(150) NOT NULL,
  `description` TEXT,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_course_user_id` (`user_id`),
  CONSTRAINT `fk_course_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `topic`
CREATE TABLE `topic` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(150) NOT NULL,
  `niveau_maitrise` INT NOT NULL DEFAULT 0,
  `date_prochaine_revision` DATE DEFAULT NULL,
  `course_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_topic_course_id` (`course_id`),
  CONSTRAINT `fk_topic_course` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `knowledge_node`
CREATE TABLE `knowledge_node` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `concept_name` VARCHAR(150) NOT NULL,
  `category` VARCHAR(100),
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_knowledgenode_topic_id` (`topic_id`),
  KEY `idx_knowledgenode_topic_id` (`topic_id`),
  CONSTRAINT `fk_knowledgenode_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `suggestion_ia`
CREATE TABLE `suggestion_ia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(200) NOT NULL,
  `type` VARCHAR(50),
  `url` VARCHAR(500),
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_suggestion_topic_id` (`topic_id`),
  CONSTRAINT `fk_suggestion_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `note`
CREATE TABLE `note` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `contenu` TEXT,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_note_topic_id` (`topic_id`),
  CONSTRAINT `fk_note_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `study_session`
CREATE TABLE `study_session` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `xp_earned` INT NOT NULL DEFAULT 0,
  `topic_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_studysession_topic_id` (`topic_id`),
  CONSTRAINT `fk_studysession_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table `user_stats`
CREATE TABLE `user_stats` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `current_streak` INT NOT NULL DEFAULT 0,
  `longest_streak` INT NOT NULL DEFAULT 0,
  `total_xp` INT NOT NULL DEFAULT 0,
  `level` INT NOT NULL DEFAULT 1,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userstats_user_id` (`user_id`),
  KEY `idx_userstats_user_id` (`user_id`),
  CONSTRAINT `fk_userstats_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table de jointure `user_achievement`
CREATE TABLE `user_achievement` (
  `user_id` INT NOT NULL,
  `achievement_id` INT NOT NULL,
  PRIMARY KEY (`user_id`,`achievement_id`),
  KEY `idx_ua_user_id` (`user_id`),
  KEY `idx_ua_achievement_id` (`achievement_id`),
  CONSTRAINT `fk_ua_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ua_achievement` FOREIGN KEY (`achievement_id`) REFERENCES `achievement`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- End of schema

