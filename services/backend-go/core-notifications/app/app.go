package app

import (
	"context"
	"corenotif/config"
	"corenotif/database"
	"corenotif/message"
	"corenotif/redis"
	"corenotif/service"
	"fmt"
	"os"

	"github.com/jackc/pgx/v4/pgxpool"
)

func Start() {
	config.InitConfig()
	messageProcessor := message.New()
	db := database.NewRepository(initDB())
	notificationService := service.GetService(db, messageProcessor)
	redisInstance := redis.GetRedis(
		1, 100,
		1, 100,
		notificationService,
	)
	redisInstance.Start()
}

func initDB() *pgxpool.Pool {
	dbUrl := "postgres://" + "tsypk" + ":" + "15.Aleksei" + "@" + "c-c9qikb36ojt5s6c7vpfo.rw.mdb.yandexcloud.net" + ":" + "6432" + "/tsypk?search_path=dev"
	config, err := pgxpool.ParseConfig(dbUrl)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unnable to parse config: %v\n", err)
		os.Exit(1)
	}

	config.MaxConns = 2

	pool, err := pgxpool.ConnectConfig(context.Background(), config)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unnable to connect to database: %v\n", err)
		os.Exit(1)
	}

	return pool
}
