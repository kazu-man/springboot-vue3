-- 初期のユーザテーブル
DROP TABLE IF EXISTS users;
CREATE TABLE "users" (
	"id" SERIAL NOT NULL primary key,
	"username" VARCHAR(255) NOT NULL,
	"password" VARCHAR(255) NOT NULL,
	"email" VARCHAR(255) NOT NULL,
	"role" VARCHAR(255) NOT NULL
);

-- tokenテーブル
DROP TABLE IF EXISTS tokens;
CREATE TABLE "tokens" (
	"id" SERIAL NOT NULL primary key,
	"token" TEXT NOT NULL,
	"refresh_token" TEXT NOT NULL
);

--勤怠テーブル
DROP TABLE IF EXISTS "attendances";
CREATE TABLE "attendances" (
	"id" SERIAL NOT NULL primary key,
	"user_id" INTEGER NOT NULL,
	"start_at" timestamp DEFAULT CURRENT_TIMESTAMP,
	"finish_at" timestamp DEFAULT CURRENT_TIMESTAMP,
	"title" VARCHAR(255) DEFAULT NULL,
	"comment" TEXT DEFAULT NULL
);


--タグテーブル
DROP TABLE IF EXISTS "tags";
CREATE TABLE "tags" (
	"id" SERIAL NOT NULL primary key,
	"tag_name" VARCHAR(255) NOT NULL
);

--タグ・勤怠中間テーブル
DROP TABLE IF EXISTS "tag_attendances";
CREATE TABLE "tag_attendances" (
	"id" SERIAL NOT NULL primary key,
	"attendance_id" INTEGER NOT NULL,
	"tag_id" INTEGER NOT NULL
);

-- 初期タグ
INSERT INTO "tags" ("tag_name") VALUES ('first tag');
INSERT INTO "tags" ("tag_name") VALUES ('second tag');
INSERT INTO "tags" ("tag_name") VALUES ('third tag');
INSERT INTO "tags" ("tag_name") VALUES ('forth tag');

-- 初期タグリレーション
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,1);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (2,1);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (3,1);

INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (2,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (3,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (4,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (5,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (6,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (7,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (8,2);

INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,3);
