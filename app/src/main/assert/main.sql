/*
Navicat SQLite Data Transfer

Source Server         : iBook
Source Server Version : 30714
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30714
File Encoding         : 65001

Date: 2015-04-05 21:26:35
*/

PRAGMA foreign_keys = OFF;

-- ----------------------------
-- Table structure for hisBookInfo
-- ----------------------------
DROP TABLE IF EXISTS "main"."hisBookInfo";
CREATE TABLE "hisBookInfo" (
"userNumber"  INTEGER NOT NULL,
"number"  TEXT,
"barcode"  TEXT,
"name"  TEXT,
"author"  TEXT,
"borrowDate"  TEXT,
"returnDate"  TEXT,
"place"  TEXT,
PRIMARY KEY ("userNumber"),
CONSTRAINT "userNumber" FOREIGN KEY ("userNumber") REFERENCES "user" ("number") ON DELETE SET NULL ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of hisBookInfo
-- ----------------------------

-- ----------------------------
-- Table structure for myComment
-- ----------------------------
DROP TABLE IF EXISTS "main"."myComment";
CREATE TABLE "myComment" (
"userNumber"  INTEGER NOT NULL,
"documentNum"  TEXT,
"bookName"  TEXT,
"author"  TEXT,
"time"  TEXT,
"content"  TEXT,
"supportNum"  INTEGER,
"againstNum"  INTEGER,
PRIMARY KEY ("userNumber"),
CONSTRAINT "userNumber" FOREIGN KEY ("userNumber") REFERENCES "user" ("number") ON DELETE SET NULL ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of myComment
-- ----------------------------

-- ----------------------------
-- Table structure for nowBookInfo
-- ----------------------------
DROP TABLE IF EXISTS "main"."nowBookInfo";
CREATE TABLE "nowBookInfo" (
"userNumber"  INTEGER NOT NULL,
"barcode"  TEXT,
"name_author"  TEXT,
"borrowDate"  TEXT,
"returnDate"  TEXT,
"renewNum"  INTEGER,
"place"  TEXT,
PRIMARY KEY ("userNumber"),
CONSTRAINT "userNumber" FOREIGN KEY ("userNumber") REFERENCES "user" ("number") ON DELETE SET NULL ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of nowBookInfo
-- ----------------------------

-- ----------------------------
-- Table structure for sqlite_sequence
-- ----------------------------
DROP TABLE IF EXISTS "main"."sqlite_sequence";
CREATE TABLE sqlite_sequence(name,seq);

-- ----------------------------
-- Records of sqlite_sequence
-- ----------------------------
INSERT INTO "main"."sqlite_sequence" VALUES ('user', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS "main"."user";
CREATE TABLE "user" (
"id"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ON CONFLICT FAIL,
"number"  INTEGER NOT NULL,
"name"  TEXT,
"password"  TEXT NOT NULL,
"registerdate"  BLOB,
"unit"  TEXT,
"gender"  TEXT
);

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for _user_old_20150320
-- ----------------------------
DROP TABLE IF EXISTS "main"."_user_old_20150320";
CREATE TABLE "_user_old_20150320" (
"id"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ON CONFLICT FAIL,
"number"  INTEGER NOT NULL,
"name"  TEXT,
"password"  TEXT NOT NULL,
"registerdate"  BLOB,
"unit"  TEXT,
"gender"  TEXT
);

-- ----------------------------
-- Records of _user_old_20150320
-- ----------------------------
