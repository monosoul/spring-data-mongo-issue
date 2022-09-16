package dev.monosoul.spring.data.mongo.issue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IssueApplication

fun main(args: Array<String>) {
    runApplication<IssueApplication>(*args)
}
