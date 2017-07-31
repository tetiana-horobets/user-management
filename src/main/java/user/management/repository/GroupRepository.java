package user.management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import user.management.entity.Group;

import java.util.Collection;

@Repository
public class GroupRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Group> getAllGroups() {
        return jdbcTemplate.query("SELECT group_pk, Group_Name FROM agroup", getGroupRowMapper());
    }

    public Collection<Group> getGroupsForUser(long userId) {
        return jdbcTemplate.query("select group_pk, group_name from user_group " +
                        "inner join agroup on user_group.group_id=agroup.group_pk " +
                        "where user_group.user_id = ?",
                new Object[] {userId},
                getGroupRowMapper());
    }

    public Group getGroup(long groupId) {
        return jdbcTemplate.queryForObject("SELECT group_pk, Group_Name FROM agroup WHERE group_pk = ?",
                new Object[]{groupId}, getGroupRowMapper());
    }

    public void deleteGroup(long groupId) {
        jdbcTemplate.update("DELETE FROM agroup WHERE group_pk = ?", groupId);
    }

    public void editGroup(long groupId, Group group) {
        jdbcTemplate.update("UPDATE agroup set Group_Name = ? WHERE group_pk  = ?", group.getGroupName(), groupId);
    }

    public void createGroup(Group group) {
        jdbcTemplate.update("INSERT INTO agroup (Group_Name) VALUES (?)", group.getGroupName());
    }

    private RowMapper<Group> getGroupRowMapper() {
        return (rs, rowNum) -> new Group(rs.getLong("group_pk"), rs.getString("Group_Name"));
    }
}
