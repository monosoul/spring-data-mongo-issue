package dev.monosoul.spring.data.mongo.issue

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("document")
@TypeAlias("document")
data class DocumentWithInterfaceField(
    @Field("interface_field")
    val interfaceField: FieldType,
    @Id
    val id: ObjectId = ObjectId.get(),
)

sealed interface FieldType {
    @get:Field("some_int_field")
    val someIntField: Int

    @TypeAlias("field_type_impl")
    data class FieldTypeImpl(
        override val someIntField: Int = 123,
        @Field("some_string_field")
        val someStringField: String = "qwe",
    ) : FieldType

    @TypeAlias("other_field_type_impl")
    data class OtherFieldTypeImpl(
        override val someIntField: Int = 456,
        @Field("other_string_field")
        val otherStringField: String = "asd",
    ) : FieldType
}
