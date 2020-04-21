package unique.usernames;

import arc.Events;
import arc.graphics.Color;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.entities.type.Player;
import mindustry.game.EventType;
import mindustry.net.Packets;
import mindustry.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class UniqueUsernamesPlugin extends Plugin {
    Config config;
    Database database;

    public HashMap<String, String> uniqueUsernames = new HashMap<>();

    public UniqueUsernamesPlugin() {
        config = new Config();

        database = new Database(config.getString("driver"), config.getString("url"), config.getString("username"), config.getString("password"));

        uniqueUsernames.putAll(database.readAllPlayers());

        Events.on(EventType.PlayerJoin.class, playerJoin -> {
            Player player = playerJoin.player;

            player.name = Strings.stripColors(player.name);
            player.color = Color.white;

            if (uniqueUsernames.get(player.uuid) == null) {
                if (uniqueUsernames.containsValue(player.name)) {
                    player.con.kick(Packets.KickReason.nameInUse);
                } else {
                    uniqueUsernames.put(player.uuid, player.name);
                    database.addPlayer(player);
                }
            }
            if (uniqueUsernames.get(player.uuid) != player.name) {
                if (uniqueUsernames.containsValue(player.name)) {
                    player.con.kick(Packets.KickReason.nameInUse);
                } else {
                    uniqueUsernames.replace(player.uuid, player.name);
                    database.updatePlayer(player);
                }
            }
        });
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("freeusername", "<username>", "List all thorium reactors in the map.", args -> {
            String username = args[0];

            for (Player player : Vars.playerGroup.all()) {
                if (player.name.equals(username)) player.con.kick(Packets.KickReason.nameInUse);
            }
            for (Map.Entry<String, String> entry : uniqueUsernames.entrySet()) {
                if (entry.getValue().equals(username)) {
                    uniqueUsernames.remove(entry.getKey());
                    break;
                }
            }


            if(database.deletePlayer(username)) {
                Log.info("Successfully freed up username " + username + ".");
            } else {
                Log.info("Username " + username + " was not occupied.");
            }
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler){

    }
}
