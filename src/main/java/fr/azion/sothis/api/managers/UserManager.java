package fr.azion.sothis.api.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.azion.sothis.api.database.DatabaseManager;
import fr.azion.sothis.api.pojo.User;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class UserManager {

    private MongoCollection<User> users;

    public UserManager(DatabaseManager databaseManager) {
        this.users = databaseManager.getUsers();
    }

    public User getUser(String uuid) {
        return users.find(eq("uuid", uuid)).first();
    }

    public User getUser(UUID uuid) {
        return getUser(uuid.toString());
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId().toString());
    }

    public boolean isRegistered(String uuid) {
        if(getUser(uuid) != null) {
            return true;
        }
        return false;
    }

    public boolean isRegistered(UUID uuid) {
        return isRegistered(uuid.toString());
    }

    public boolean isRegistered(Player player) {
        return isRegistered(player.getUniqueId().toString());
    }

    public void updateUser(User user) {
        Document filterById = new Document("uuid", user.getUuid());
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        User updatedGuildData = users.findOneAndReplace(filterById, user , returnDocAfterReplace);
    }

    public void updateUser(String uuid, Consumer<User> consumer) {
        User user = getUser(uuid);
        consumer.accept(user);
        updateUser(user);
    }

    public void updateUser(UUID uuid, Consumer<User> consumer) {
        User user = getUser(uuid);
        consumer.accept(user);
        updateUser(user);
    }

    public void updateUser(Player uuid, Consumer<User> consumer) {
        User user = getUser(uuid);
        consumer.accept(user);
        updateUser(user);
    }

}