package fr.afpa.dev.pompey.dao;

import fr.afpa.dev.pompey.modele.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO<User>{

    /**
     * @param obj 
     * @return
     */
    @Override
    public int create(User obj) {
        int newid = 0;
        StringBuilder insertSQL = new StringBuilder(
                "INSERT INTO utilisateur " +
                "(USER_EMAIL, USER_PASSWORD, USER_EMAIL, USER_ROLE, USER_DATE)" +
                " VALUES (?, ?, ?, ?, ?)");
        try {
            PreparedStatement pstmt = connect.prepareStatement(insertSQL.toString(),
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getPassword());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getRole());
            pstmt.setDate(5, obj.getDate());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newid = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newid;
    }

    /**
     * @param obj 
     * @return
     */
    @Override
    public boolean delete(User obj) {
        StringBuilder deleteSQL = new StringBuilder("DELETE FROM utilisateur WHERE USER_ID = ?");

        try {
            PreparedStatement pstmt = connect.prepareStatement(deleteSQL.toString());
            pstmt.setInt(1, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param obj 
     * @return
     */
    @Override
    public boolean update(User obj) {
        StringBuilder updateSQL = new StringBuilder("UPDATE utilisateur SET USER_EMAIL = ?, USER_PASSWORD = ?, USER_NAME = ?, USER_DATE = ?, USER_ROLE = ? WHERE USER_ID = ?");

        try {
            PreparedStatement pstmt = connect.prepareStatement(updateSQL.toString());
            pstmt.setString(1, obj.getEmail());
            pstmt.setString(2, obj.getPassword());
            pstmt.setString(3, obj.getEmail());
            pstmt.setDate(4, obj.getDate());
            pstmt.setString(5, obj.getRole());
            pstmt.setInt(6, obj.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public User find(int id) {
        User user = new User(id);

        StringBuilder selectById = new StringBuilder("SELECT * FROM utilisateur WHERE USER_ID = ?");

        try{
            PreparedStatement pstmt = connect.prepareStatement(selectById.toString());
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
            }
            return user;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * @return 
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<User>();
        StringBuilder selectSQL = new StringBuilder("SELECT * FROM utilisateur");

        try{
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL.toString());

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("USER_EMAIL"));
                user.setPassword(rs.getString("USER_PASSWORD"));
                user.setName(rs.getString("USER_NAME"));
                user.setDate(rs.getDate("USER_DATE"));
                user.setRole(rs.getString("USER_ROLE"));
                users.add(user);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return users;
    }
}
