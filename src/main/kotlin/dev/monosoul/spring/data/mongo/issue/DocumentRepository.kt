package dev.monosoul.spring.data.mongo.issue

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DocumentRepository : MongoRepository<DocumentWithInterfaceField, ObjectId>
