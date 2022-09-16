package dev.monosoul.spring.data.mongo.issue

import org.reflections.Reflections
import org.springframework.core.annotation.AnnotationUtils.getAnnotation
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.convert.ConfigurableTypeInformationMapper
import org.springframework.data.convert.SimpleTypeInformationMapper
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper

class ReflectiveMongoTypeMapper(
    private val reflections: Reflections = Reflections("dev.monosoul.spring.data.mongo.issue")
) : DefaultMongoTypeMapper(
    DEFAULT_TYPE_KEY,
    listOf(
        ConfigurableTypeInformationMapper(
            reflections.getTypesAnnotatedWith(TypeAlias::class.java).associateWith { clazz ->
                getAnnotation(clazz, TypeAlias::class.java)!!.value
            }
        ),
        SimpleTypeInformationMapper(),
    )
)
