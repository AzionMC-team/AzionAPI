package fr.azion.sothis.api.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.azion.sothis.api.database.DatabaseManager;
import fr.azion.sothis.api.listeners.ListenerManager;
import fr.azion.sothis.api.pojo.Grade;
import fr.azion.sothis.api.pojo.Report;
import fr.azion.sothis.api.pojo.User;
import org.bson.Document;

import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class ReportManager {

    private MongoCollection<Report> reports;
    private ListenerManager listenerManager;

    public ReportManager(DatabaseManager databaseManager, ListenerManager listenerManager) {
        this.reports = databaseManager.getReports();
        this.listenerManager = listenerManager;
    }

    public Report getReport(String uuid) {
        return reports.find(eq("playerreport", uuid)).first();
    }

    public boolean isRegistered(String uuid) {
        if(getReport(uuid) != null) {
            return true;
        }
        return false;
    }

    public void updateReport(Report report) {
        Document filterById = new Document("playerreport", report.getPlayerreport());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        Report updatedGuildData = reports.findOneAndReplace(filterById, report , returnDocAfterReplace);
    }

    public void updateReport(String uuid, Consumer<Report> consumer) {
        Report report = getReport(uuid);
        consumer.accept(report);
        updateReport(report);
    }

    public void createReport(Report report) {
        reports.insertOne(report);
        listenerManager.deflectReportListener(report);
    }
}