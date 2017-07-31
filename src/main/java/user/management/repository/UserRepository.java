package user.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import user.management.entity.Group;
import user.management.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<User> getAllUsers() {
        return jdbcTemplate.query("SELECT " +
                "user_pk, " +
                "user_name, " +
                "first_name, " +
                "last_name, " +
                "date_of_birth " +
                "FROM user", getUserRowMapper());
    }

    public Collection<User> getUsersInGroup(long groupId) {
        return jdbcTemplate.query("SELECT " +
                "user_pk, " +
                "user_name, " +
                "first_name, " +
                "last_name, " +
                "date_of_birth " +
                "FROM user " +
                "inner join user_group " +
                "on user.user_pk = user_group.user_id " +
                "WHERE group_id = ?", new Object[]{groupId}, getUserRowMapper());
    }

    public User getUser(long userId) {
        return jdbcTemplate.queryForObject("SELECT " +
                "user_pk, " +
                "user_name, " +
                "first_name, " +
                "last_name, " +
                "date_of_birth " +
                "FROM user " +
                "WHERE user_pk = ?", new Object[]{userId}, getUserRowMapper());
    }

    public void deleteUser(long userId) {
        jdbcTemplate.update("DELETE FROM user WHERE user_pk = ?", userId);
    }

    public void createUser(User user) {
        jdbcTemplate.update("INSERT INTO user " +
                        "(user_name, " +
                        "password, " +
                        "first_name, " +
                        "last_name, " +
                        "date_of_birth) VALUES (?, ?, ?, ?, ?)",
                user.getName(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthday()
        );
    }

    public void updateUser(long userId, User user) {
        jdbcTemplate.update("UPDATE user set " +
                        "user_name = ?, " +
                        "password = ?, " +
                        "first_name = ?, " +
                        "last_name = ?, " +
                        "date_of_birth = ? " +
                        "WHERE user_pk = ?",
                user.getName(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthday(),
                userId
        );
    }

    public void updateUserGroups(long userId, List<Group> groups) {
        jdbcTemplate.update("DELETE FROM user_group WHERE user_id = ?", userId);

        List<Object[]> ids = new ArrayList<>();

        for(Group group : groups) {
            ids.add(new Object[] {group.getId(), userId});
        }

        jdbcTemplate.batchUpdate("INSERT INTO user_group (group_id, user_id) VALUES (?, ?)", ids);
    }

    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("user_pk"),
                rs.getString("user_name"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("date_of_birth")
        );
    }
}
