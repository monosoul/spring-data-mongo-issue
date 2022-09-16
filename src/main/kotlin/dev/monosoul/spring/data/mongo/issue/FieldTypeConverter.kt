package dev.monosoul.spring.data.mongo.issue

import dev.monosoul.spring.data.mongo.issue.FieldType.FieldTypeImpl
import dev.monosoul.spring.data.mongo.issue.FieldType.OtherFieldTypeImpl
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class FieldTypeConverter : Converter<Document, FieldType> {
    override fun convert(source: Document): FieldType? = when (source.getString("_class")) {
        "field_type_impl" -> {
            FieldTypeImpl(
                someIntField = source.getInteger("some_int_field"),
                someStringField = source.getString("some_string_field"),
            )
        }

        "other_field_type_impl" -> {
            OtherFieldTypeImpl(
                someIntField = source.getInteger("some_int_field"),
                otherStringField = source.getString("other_string_field"),
            )
        }

        else -> null
    }
}
