package dev.monosoul.spring.data.mongo.issue

import dev.monosoul.spring.data.mongo.issue.FieldType.FieldTypeImpl
import dev.monosoul.spring.data.mongo.issue.FieldType.OtherFieldTypeImpl
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import strikt.api.expectCatching
import strikt.assertions.isEqualTo
import strikt.assertions.isSuccess
import strikt.java.isPresent
import kotlin.streams.asStream

class SaveAndGetWithRepositoryTest @Autowired constructor(
    private val repository: DocumentRepository,
) : AbstractRepositoryTest() {

    @TestFactory
    fun `should save the document and then get it`() = sequenceOf(
        DocumentWithInterfaceField(FieldTypeImpl()),
        DocumentWithInterfaceField(OtherFieldTypeImpl()),
    ).map { document ->
        dynamicTest("should save $document and then get it") {
            // given
            repository.save(document)

            // when & then
            expectCatching {
                repository.findById(document.id)
            }.isSuccess().isPresent() isEqualTo document
        }
    }.asStream()
}
