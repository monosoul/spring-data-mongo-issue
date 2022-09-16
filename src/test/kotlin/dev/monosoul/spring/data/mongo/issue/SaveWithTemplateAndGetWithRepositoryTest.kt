package dev.monosoul.spring.data.mongo.issue

import com.mongodb.BasicDBObjectBuilder
import org.bson.types.ObjectId
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS
import strikt.api.expectCatching
import strikt.assertions.isEqualTo
import strikt.assertions.isSuccess
import strikt.java.isPresent
import kotlin.streams.asStream

@DirtiesContext(classMode = BEFORE_CLASS) // required to prevent spring from caching the type in the other test
class SaveWithTemplateAndGetWithRepositoryTest @Autowired constructor(
    private val repository: DocumentRepository,
    private val mongoTemplate: MongoTemplate,
) : AbstractRepositoryTest() {

    @TestFactory
    fun `should save the document and then get it`() = sequenceOf(
        ObjectId.get().let { id ->
            id to buildDbObject {
                this["_id"] = id
                this["_class"] = "document"
                this["interface_field"] = buildDbObject {
                    this["_class"] = "field_type_impl"
                    this["some_int_field"] = 123
                    this["some_string_field"] = "qwe"
                }
            }
        },
        ObjectId.get().let { id ->
            id to buildDbObject {
                this["_id"] = id
                this["_class"] = "document"
                this["interface_field"] = buildDbObject {
                    this["_class"] = "other_field_type_impl"
                    this["some_int_field"] = 456
                    this["other_string_field"] = "asd"
                }
            }
        },
    ).map { (id, document) ->
        dynamicTest("should save $document and then get it") {
            // given
            mongoTemplate.save(document, "document")

            // when & then
            expectCatching {
                repository.findById(id) // will throw MappingInstantiationException
            }.isSuccess().isPresent().get { this.id } isEqualTo id
        }
    }.asStream()

    private fun buildDbObject(block: BasicDBObjectBuilder.() -> Unit) = BasicDBObjectBuilder.start().apply(block).get()
    private operator fun BasicDBObjectBuilder.set(field: String, value: Any) = add(field, value)
}
