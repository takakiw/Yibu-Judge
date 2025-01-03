-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: yibu_judge
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `code_template`
--

CREATE DATABASE IF NOT EXISTS `yibu_judge`;
USE `yibu_judge`;

DROP TABLE IF EXISTS `code_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `code_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识题目代码模块',
  `problem_id` bigint NOT NULL COMMENT '题目表的主键 ID',
  `language_id` int NOT NULL COMMENT '语言表的主键 ID',
  `template_code` text COMMENT '题目的模板代码，提供给用户参考',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_pid_langid` (`problem_id`,`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='题目代码模块表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code_template`
--

LOCK TABLES `code_template` WRITE;
/*!40000 ALTER TABLE `code_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `code_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contest`
--

DROP TABLE IF EXISTS `contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '比赛名称',
  `description` text COMMENT '比赛描述',
  `start_time` datetime NOT NULL COMMENT '比赛开始时间',
  `end_time` datetime NOT NULL COMMENT '比赛结束时间',
  `organizer` varchar(255) DEFAULT NULL COMMENT '组织者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest`
--

LOCK TABLES `contest` WRITE;
/*!40000 ALTER TABLE `contest` DISABLE KEYS */;
INSERT INTO `contest` VALUES (1,'Algorithm Challenge','A competition for algorithm enthusiasts.','2024-12-13 09:00:00','2024-12-15 17:00:00','TechOrg','2024-12-12 19:51:52','2024-12-13 21:04:34'),(2,'Data Science Hackathon','Showcase your data science skills.','2024-12-20 10:00:00','2024-12-20 18:00:00','DataClub','2024-12-12 19:51:52','2024-12-12 19:54:34'),(3,'Coding Marathon','Solve coding problems non-stop!','2024-12-25 08:00:00','2024-12-25 20:00:00','CodeMaster','2024-12-12 19:51:52','2024-12-12 19:54:34');
/*!40000 ALTER TABLE `contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contest_leaderboard`
--

DROP TABLE IF EXISTS `contest_leaderboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contest_leaderboard` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contest_id` int unsigned NOT NULL COMMENT '比赛ID',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `score` int DEFAULT '0' COMMENT '得分',
  `finish_time` bigint DEFAULT '0' COMMENT '完成比赛总耗时/秒',
  `details` json DEFAULT NULL COMMENT '每道题的完成时间、尝试次数等详情',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `penalty` bigint DEFAULT '0' COMMENT '罚时 / s',
  PRIMARY KEY (`id`),
  UNIQUE KEY `contest_id` (`contest_id`,`user_id`),
  KEY `contest_idx` (`contest_id`),
  KEY `user_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='比赛排行榜表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest_leaderboard`
--

LOCK TABLES `contest_leaderboard` WRITE;
/*!40000 ALTER TABLE `contest_leaderboard` DISABLE KEYS */;
INSERT INTO `contest_leaderboard` VALUES (2,1,1864364122813108224,NULL,0,'{\"t1\": {\"time\": \"1h32m11s\", \"attempts\": \"15m\"}, \"t2\": {\"time\": \"1h32m11s\", \"attempts\": \"15m\"}}','2024-12-13 21:02:40','2024-12-13 21:02:40',0),(5,2,1864364122813108224,0,0,NULL,'2024-12-13 22:54:08','2024-12-13 22:54:08',0),(6,1,1,600,1800000,'{\"t2\": {\"time\": \"23m:59s\", \"penalty\": \"5m\", \"attempts\": 1}, \"t3\": {\"time\": \"23m:59s\", \"penalty\": \"5m\", \"attempts\": 1}, \"t5\": {\"time\": \"23m:59s\", \"penalty\": \"5m\", \"attempts\": 1}}','2024-12-13 23:07:18','2024-12-13 23:27:43',5000);
/*!40000 ALTER TABLE `contest_leaderboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contest_problems`
--

DROP TABLE IF EXISTS `contest_problems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contest_problems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '比赛题目',
  `contest_id` int NOT NULL COMMENT '关联比赛ID',
  `problem_id` int NOT NULL COMMENT '关联题目ID',
  `problem_order` int DEFAULT NULL COMMENT '题目在比赛中的顺序',
  `score` int DEFAULT '0' COMMENT '题目分值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_pid_cid` (`problem_id`,`contest_id`),
  UNIQUE KEY `unique_pid` (`problem_id`),
  KEY `index_cid` (`contest_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest_problems`
--

LOCK TABLES `contest_problems` WRITE;
/*!40000 ALTER TABLE `contest_problems` DISABLE KEYS */;
INSERT INTO `contest_problems` VALUES (15,'Sorting Algorithms',1,1,1,100,'2024-12-12 19:51:45'),(16,'Graph Traversal',1,2,2,150,'2024-12-12 19:51:45'),(17,'Dynamic Programming',1,3,3,200,'2024-12-12 19:51:45'),(18,'Data Cleaning',2,4,1,120,'2024-12-12 19:51:45'),(19,'Model Evaluation',2,5,2,180,'2024-12-12 19:51:45'),(20,'Big Data Analysis',2,6,3,220,'2024-12-12 19:51:45'),(21,'String Manipulation',3,7,1,110,'2024-12-12 19:51:45'),(22,'Binary Search Tree',3,8,2,160,'2024-12-12 19:51:45'),(23,'Hashing Techniques',3,9,3,190,'2024-12-12 19:51:45');
/*!40000 ALTER TABLE `contest_problems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '语言的唯一标识',
  `name` varchar(50) NOT NULL COMMENT '语言名称，例如 C++, Java',
  `suffix` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='编程语言表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language`
--

LOCK TABLES `language` WRITE;
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` VALUES (1,'C','.c'),(2,'CPP','.cpp'),(3,'Python3','.py'),(4,'Java','.java');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem_tags`
--

DROP TABLE IF EXISTS `problem_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem_tags` (
  `problem_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`problem_id`,`tag_id`),
  KEY `index_pid` (`problem_id`),
  KEY `index_tid` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem_tags`
--

LOCK TABLES `problem_tags` WRITE;
/*!40000 ALTER TABLE `problem_tags` DISABLE KEYS */;
INSERT INTO `problem_tags` VALUES (1,1),(1,5),(2,2),(2,6),(3,3),(3,4),(4,2),(4,7),(5,1),(5,8),(6,3),(6,9),(7,4),(7,10),(8,1),(8,6),(9,2),(9,5),(10,3),(10,8),(11,4),(11,7),(12,1),(12,3),(13,2),(13,5),(14,3),(14,4),(15,6),(15,9),(16,7),(16,10),(17,2),(17,8);
/*!40000 ALTER TABLE `problem_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problems`
--

DROP TABLE IF EXISTS `problems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `input_desc` text NOT NULL,
  `output_desc` text NOT NULL,
  `time_limit` bigint DEFAULT '1000',
  `memory_limit` bigint DEFAULT '536870912',
  `difficulty` int DEFAULT '0',
  `submit_count` int DEFAULT '0',
  `accepted_count` int DEFAULT '0',
  `auth` int DEFAULT '0',
  `data_range` varchar(200) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problems`
--

LOCK TABLES `problems` WRITE;
/*!40000 ALTER TABLE `problems` DISABLE KEYS */;
INSERT INTO `problems` VALUES (1,'两数之和','给定一个整数数组 nums 和一个目标值 target，找出数组中和为目标值的两个数。','输入一个整数数组 nums 和目标值 target。','返回数组中两个数的下标。',1000,1024,1,100,50,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(2,'爬楼梯','假设你正在爬楼梯。需要 n 阶你才能到达楼顶。每次可以爬 1 或 2 阶楼梯。','输入一个整数 n，表示楼梯的阶数。','返回到达楼顶的方法总数。',1000,1024,1,200,150,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(3,'最长回文子串','给定一个字符串 s，找到 s 中最长的回文子串。','输入一个字符串 s。','返回 s 中的最长回文子串。',1000,1024,2,300,250,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(4,'爬楼梯 II','假设你正在爬楼梯，每次你可以爬 1、2、3 或 4 阶楼梯，求爬到楼顶的总方法数。','输入一个整数 n，表示楼梯的阶数。','返回到达楼顶的总方法数。',1000,1024,2,150,120,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(5,'合并两个有序数组','给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中。','输入两个有序整数数组 nums1 和 nums2。','返回合并后的数组。',1000,1024,1,250,200,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(6,'岛屿的最大面积','给定一个二维的矩阵 grid，包含 0 和 1，1 表示陆地，0 表示水域，找到最大的岛屿的面积。','输入一个二维矩阵 grid。','返回最大的岛屿的面积。',1000,1024,3,300,270,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(7,'最大矩形','给定一个由 0 和 1 组成的二维矩阵，找到包含 1 的最大矩形的面积。','输入一个二维矩阵。','返回矩形的最大面积。',1000,1024,3,120,110,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(8,'LRU 缓存机制','运用你所掌握的数据结构，设计并实现一个 LRU（最近最少使用）缓存机制。','输入缓存的大小和操作指令。','返回每次查询的结果。',1000,1024,2,250,200,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(9,'跳跃游戏 II','给定一个非负整数数组 nums，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。','输入一个非负整数数组 nums。','返回到达数组最后一个位置的最小跳跃次数。',1000,1024,2,150,130,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(10,'滑动窗口最大值','给定一个数组 nums，和一个滑动窗口的大小 k，找到所有滑动窗口中的最大值。','输入一个数组 nums 和滑动窗口大小 k。','返回滑动窗口中的最大值。',1000,1024,2,200,170,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(11,'字符串的排列','给定一个字符串 s，返回 s 的所有排列。','输入一个字符串 s。','返回所有可能的排列。',1000,1024,2,120,100,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(12,'最小路径和','给定一个包含非负整数的 m x n 网格，从左上角到右下角的最小路径和。','输入一个 m x n 网格。','返回从左上角到右下角的最小路径和。',1000,1024,1,250,220,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(13,'三数之和','给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c，使得 a + b + c = 0。','输入一个整数数组 nums。','返回所有满足条件的三元组。',1000,1024,2,300,260,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(14,'子集','给定一个整数数组 nums，返回所有可能的子集（解集）。','输入一个整数数组 nums。','返回所有可能的子集。',1000,1024,1,200,180,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(15,'括号生成','给定 n 对括号，编写一个函数，生成所有可能的有效括号组合。','输入一个整数 n，表示括号对数。','返回所有可能的有效括号组合。',1000,1024,2,150,130,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(16,'复制带随机指针的链表','给定一个链表，每个节点包含一个额外的随机指针，该指针可以指向链表中的任何节点。','输入一个链表。','返回复制的链表。',1000,1024,3,250,220,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(17,'括号匹配','给定一个字符串，包含仅字符 ( )，判断是否有效括号。','输入一个字符串。','返回 true 或 false，表示是否为有效的括号序列。',1000,1024,1,200,190,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(18,'最长公共子串','给定两个字符串，求它们的最长公共子串。','输入两个字符串。','返回它们的最长公共子串。',1000,1024,2,150,130,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23'),(19,'寻找重复数','给定一个包含 n + 1 个整数的数组 nums，其中每个整数在 1 到 n 之间。返回重复的那个数。','输入一个整数数组 nums。','返回数组中重复的那个数。',1000,1024,3,220,200,0,'\'\'','2024-12-05 19:48:23','2024-12-05 19:48:23');
/*!40000 ALTER TABLE `problems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submit`
--

DROP TABLE IF EXISTS `submit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提交记录的主键',
  `user_id` bigint NOT NULL COMMENT '用户 ID，关联用户表',
  `problem_id` bigint NOT NULL COMMENT '题目 ID，关联题目表',
  `contest_id` int DEFAULT NULL,
  `status` int DEFAULT '0' COMMENT '提交状态，例如 0: 判题中，1:AC, 2:WA, 3:TLE,...',
  `lang_id` int NOT NULL COMMENT '编程语言 ID，关联语言表',
  `runtime` int DEFAULT '0' COMMENT '运行时间（ms）',
  `memory` int DEFAULT '0' COMMENT '内存占用 bytes',
  `code_path` varchar(300) COMMENT '用户提交的代码',
  `result_message` varchar(255) DEFAULT NULL COMMENT '判题结果信息',
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`id`),
  KEY `index_uid` (`user_id`),
  KEY `index_pid` (`problem_id`),
  KEY `index_status` (`status`),
  KEY `index_cid` (`contest_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='提交记录表';
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,'数组','2024-12-05 19:48:08'),(2,'动态规划','2024-12-05 19:48:08'),(3,'图论','2024-12-05 19:48:08'),(4,'字符串','2024-12-05 19:48:08'),(5,'贪心算法','2024-12-05 19:48:08'),(6,'深度优先搜索','2024-12-05 19:48:08'),(7,'广度优先搜索','2024-12-05 19:48:08'),(8,'二分查找','2024-12-05 19:48:08'),(9,'回溯','2024-12-05 19:48:08'),(10,'排序','2024-12-05 19:48:08'),(11,'递归','2024-12-05 19:48:08'),(12,'数学','2024-12-05 19:48:08'),(13,'二叉树','2024-12-05 19:48:08'),(14,'栈','2024-12-05 19:48:08'),(15,'队列','2024-12-05 19:48:08'),(16,'链表','2024-12-05 19:48:08'),(17,'哈希表','2024-12-05 19:48:08'),(18,'图','2024-12-05 19:48:08'),(19,'矩阵','2024-12-05 19:48:08'),(20,'树','2024-12-05 19:48:08'),(21,'二分查找2','2024-12-14 14:28:27'),(22,'二分查找3','2024-12-14 14:33:42');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `testcases`
--

DROP TABLE IF EXISTS `testcases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testcases` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `problem_id` bigint DEFAULT NULL COMMENT '问题ID',
  `input_path` varchar(255) NOT NULL COMMENT '测试用例的输入文件路径',
  `input_desc` varchar(300) DEFAULT NULL,
  `output_path` varchar(255) NOT NULL COMMENT '测试用例的输出文件路径',
  `output_desc` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_pid` (`problem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储问题测试用例信息的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `testcases`
--

LOCK TABLES `testcases` WRITE;
/*!40000 ALTER TABLE `testcases` DISABLE KEYS */;
INSERT INTO `testcases` VALUES (16,1,'/judge/file/case/in/1.txt',NULL,'/judge/file/case/out/1.txt',NULL);
/*!40000 ALTER TABLE `testcases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nick_name` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `avatar` varchar(300) NOT NULL,
  `ac_count` int DEFAULT '0',
  `submit_count` int DEFAULT '0',
  `role` int DEFAULT '0',
  `score` int DEFAULT '1200',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1864364122813108225 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1864364122813108224,'saber','4547E2781BD1D816166766FDA702C7D9','喜若汐','2918628219@qq.com','https://yibu-judger.oss-cn-beijing.aliyuncs.com/36773a139dad490591b9c88ceec441bc.webp',1,0,1,1200,'2024-12-05 01:40:20','2024-12-05 18:22:55');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-14 16:55:09
