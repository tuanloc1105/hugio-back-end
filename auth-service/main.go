package main

import (
	"auth-service/utils"
	"bytes"
	"fmt"
	"github.com/gin-gonic/gin"
	"io"
	"log"
	"time"
)

type bodyLogWriter struct {
	gin.ResponseWriter
	body *bytes.Buffer
}

func (w bodyLogWriter) Write(b []byte) (int, error) {
	w.body.Write(b)
	return w.ResponseWriter.Write(b)
}

func Logger() gin.HandlerFunc {
	return func(context *gin.Context) {
		uuid := utils.CreateUUID()
		context.Set("traceId", uuid)
		t := time.Now()
		// get request body
		body, _ := io.ReadAll(context.Request.Body)
		log.Print(string(body))
		blw := &bodyLogWriter{
			body:           bytes.NewBufferString(""),
			ResponseWriter: context.Writer,
		}
		context.Writer = blw

		context.Next()

		// Response
		latency := time.Since(t)
		log.Print(latency)
		httpStatusCode := context.Writer.Status()
		if httpStatusCode == 404 {
			log.Print("404 ERROR")
		}
		log.Print(blw.body.String())
	}
}

func main() {
	fmt.Print(fmt.Sprintf("This is Gin server written by %s\n", "locvt"))
	r := gin.Default()
	r.Use(Logger())
	r.Run(":8080")
}
