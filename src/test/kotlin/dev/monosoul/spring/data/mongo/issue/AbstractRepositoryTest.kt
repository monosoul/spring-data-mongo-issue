package dev.monosoul.spring.data.mongo.issue

import org.junit.jupiter.api.AfterEach
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer

@SpringBootTest
abstract class AbstractRepositoryTest {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private val logger = LoggerFactory.getLogger(this::class.java)

    @AfterEach
    fun tearDown() {
        mongoTemplate.db.getCollection("document").also { collection ->
            collection.find().forEach { document ->
                logger.info("Document in DB: ${document.toJson()}")
            }
            collection.drop()
        }
    }

    companion object {

        private val mongoDb = MongoDBContainer("mongo:5.0.12").also {
            it.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun mongoProperties(registry: DynamicPropertyRegistry) {
            registry.add("mongo.uri") { mongoDb.replicaSetUrl }
        }
    }
}
