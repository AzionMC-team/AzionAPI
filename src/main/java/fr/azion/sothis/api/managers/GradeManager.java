package fr.azion.sothis.api.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.azion.sothis.api.database.DatabaseManager;
import fr.azion.sothis.api.pojo.Grade;
import org.bson.Document;

import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class GradeManager {

    private MongoCollection<Grade> grades;

    public GradeManager(DatabaseManager databaseManager) {
        this.grades = databaseManager.getGrades();
    }

    public Grade getGrade(String name) {
        return grades.find(eq("name", name)).first();
    }

    public boolean isRegistered(String name) {
        if(getGrade(name) != null) {
            return true;
        }
        return false;
    }

    public void updateGrade(Grade grade) {
        Document filterById = new Document("name", grade.getName());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        Grade updatedGuildData = grades.findOneAndReplace(filterById, grade , returnDocAfterReplace);
    }

    public void updateGrade(String name, Consumer<Grade> consumer) {
        Grade grade = getGrade(name);
        consumer.accept(grade);
        updateGrade(grade);
    }
}