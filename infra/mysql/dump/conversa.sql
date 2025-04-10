CREATE DATABASE  IF NOT EXISTS `conversa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `conversa`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: conversa
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amis`
--

DROP TABLE IF EXISTS `amis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amis` (
  `MG_ID` int DEFAULT NULL,
  `AMIS_STATUT` varchar(11) NOT NULL,
  `AMIS_DATE_DEMANDE` date NOT NULL,
  `USER_ID_amiDe` int NOT NULL,
  `USER_ID_utilisateur` int NOT NULL,
  KEY `MG_ID` (`MG_ID`),
  KEY `USER_ID_amiDe` (`USER_ID_amiDe`),
  KEY `USER_ID_utilisateur` (`USER_ID_utilisateur`),
  CONSTRAINT `amis_ibfk_1` FOREIGN KEY (`MG_ID`) REFERENCES `groupe_messages_prives` (`MG_ID`) ON DELETE CASCADE,
  CONSTRAINT `amis_ibfk_2` FOREIGN KEY (`USER_ID_amiDe`) REFERENCES `utilisateur` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `amis_ibfk_3` FOREIGN KEY (`USER_ID_utilisateur`) REFERENCES `utilisateur` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amis`
--

LOCK TABLES `amis` WRITE;
/*!40000 ALTER TABLE `amis` DISABLE KEYS */;
/*!40000 ALTER TABLE `amis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupe_messages_prives`
--

DROP TABLE IF EXISTS `groupe_messages_prives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groupe_messages_prives` (
  `MG_ID` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`MG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupe_messages_prives`
--

LOCK TABLES `groupe_messages_prives` WRITE;
/*!40000 ALTER TABLE `groupe_messages_prives` DISABLE KEYS */;
INSERT INTO `groupe_messages_prives` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14);
/*!40000 ALTER TABLE `groupe_messages_prives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_privee`
--

DROP TABLE IF EXISTS `message_privee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_privee` (
  `MP_ID` int NOT NULL AUTO_INCREMENT,
  `MP_DATE` datetime NOT NULL,
  `MP_MESSAGES` text NOT NULL,
  `USER_ID` int NOT NULL,
  `MG_ID` int NOT NULL,
  PRIMARY KEY (`MP_ID`),
  KEY `USER_ID` (`USER_ID`),
  KEY `MG_ID` (`MG_ID`),
  CONSTRAINT `message_privee_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `utilisateur` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `message_privee_ibfk_2` FOREIGN KEY (`MG_ID`) REFERENCES `groupe_messages_prives` (`MG_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_privee`
--

LOCK TABLES `message_privee` WRITE;
/*!40000 ALTER TABLE `message_privee` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_privee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur` (
  `USER_ID` int NOT NULL AUTO_INCREMENT,
  `USER_EMAIL` varchar(50) NOT NULL,
  `USER_PASSWORD` varchar(50) NOT NULL,
  `USER_NAME` varchar(50) NOT NULL,
  `USER_DATE` date NOT NULL,
  `USER_ROLE` varchar(11) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_EMAIL` (`USER_EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

LOCK TABLES `utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-10 16:40:42
