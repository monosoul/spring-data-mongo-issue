package dev.monosoul.spring.data.mongo.issue

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration(proxyBeanMethods = false)
@EnableMongoRepositories(
    basePackageClasses = [DocumentRepository::class]
)
@EnableConfigurationProperties(MongoProperties::class)
class MongoDbConfig : AbstractMongoClientConfiguration() {

    @Autowired
    private lateinit var mongoProperties: MongoProperties

    override fun getDatabaseName() = mongoProperties.dbName

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoProperties.uri)
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()

        return MongoClients.create(mongoClientSettings)
    }
}

@ConstructorBinding
@ConfigurationProperties("mongo")
private data class MongoProperties(
    val dbName: String = "spring_data_mongo_issue",
    val uri: String,
)
