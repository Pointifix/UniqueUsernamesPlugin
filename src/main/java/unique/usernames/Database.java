package unique.usernames;

import arc.util.Log;
import mindustry.entities.type.Player;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private Connection connection;

    public Database(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement("create table if not exists unique_usernames(id int identity(1,1) primary key, uuid varchar(255), username varchar(255))");
            ps.execute();
            ps.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> readAllPlayers() {
        HashMap<String, String> result = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from unique_usernames");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(rs.getString(2), rs.getString(3));
            }

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addPlayer(Player player) {
        try {
            PreparedStatement ps = connection.prepareStatement("insert into unique_usernames(uuid, username) values (?, ?)");
            ps.setString(1, player.uuid);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(Player player) {
        try {
            PreparedStatement ps = connection.prepareStatement("update unique_usernames set username = ? where uuid = ?");
            ps.setString(1, player.name);
            ps.setString(2, player.uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletePlayer(String username) {
        int rows = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("delete from unique_usernames where username = ?");
            ps.setString(1, username);
            rows = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows > 0;
    }
}
