package fr.azion.sothis.api.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.azion.sothis.api.pojo.Grade;
import fr.azion.sothis.api.pojo.User;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseManager {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<User> users;
    private MongoCollection<Grade> grades;

    public void init() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION, Conventions.USE_GETTERS_FOR_SETTERS)).build();
        CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        mongoClient = MongoClients.create("credentials");
        mongoDatabase = mongoClient.getDatabase("azion").withCodecRegistry(codecRegistry);
        users = mongoDatabase.getCollection("users", User.class);
        grades = mongoDatabase.getCollection("grades", Grade.class);
    }

    public void close() {
        mongoClient.close();
    }

    public MongoCollection<User> getUsers() {
        return users;
    }

    public MongoCollection<Grade> getGrades() {
        return grades;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
