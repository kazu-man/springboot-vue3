-- 初期のユーザ作成
DROP TABLE users;
CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"name" TEXT NOT NULL primary key,
	"password" TEXT NOT NULL
);

INSERT INTO "users" ("name", "password") VALUES ('zenn', 'zenn');

-- tokenテーブル
DROP TABLE tokens;
CREATE TABLE "tokens" (
	"id" SERIAL NOT NULL primary key,
	"token" TEXT NOT NULL,
	"refresh_token" TEXT NOT NULL
);
INSERT INTO "tokens" ("token", "refresh_token") VALUES ('zenn', 'zennRefresh');
