package dev.monosoul.spring.data.mongo.issue

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import dev.monosoul.spring.data.mongo.issue.FieldType.FieldTypeImpl
import dev.monosoul.spring.data.mongo.issue.FieldType.OtherFieldTypeImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.ConfigurableTypeInformationMapper
import org.springframework.data.convert.SimpleTypeInformationMapper
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper.DEFAULT_TYPE_KEY
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(
    basePackageClasses = [DocumentRepository::class]
)
@EnableConfigurationProperties(MongoProperties::class)
class MongoDbConfig : AbstractMongoClientConfiguration() {

    @Autowired
    private lateinit var mongoProperties: MongoProperties

    @Bean
    fun configurableTypeInformationMapper() = ConfigurableTypeInformationMapper(
        mapOf(
            FieldTypeImpl::class.java to "field_type_impl",
            OtherFieldTypeImpl::class.java to "other_field_type_impl",
        )
    )

    override fun mappingMongoConverter(
        databaseFactory: MongoDatabaseFactory,
        customConversions: MongoCustomConversions,
        mappingContext: MongoMappingContext
    ) = super.mappingMongoConverter(databaseFactory, customConversions, mappingContext).apply {
        setTypeMapper(
            DefaultMongoTypeMapper(
                DEFAULT_TYPE_KEY,
                listOf(
                    configurableTypeInformationMapper(),
                    SimpleTypeInformationMapper(),
                )
            )
        )
    }

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
