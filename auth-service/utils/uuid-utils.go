package utils

import (
	"fmt"
	"log"
	"os/exec"
)

func CreateUUID() string {
	newUUID, err := exec.Command("uuidgen").Output()
	if err != nil {
		log.Fatalln(fmt.Sprintf("ERROR: %s", err))
		return ""
	}
	return fmt.Sprintf("%s", newUUID)
}
