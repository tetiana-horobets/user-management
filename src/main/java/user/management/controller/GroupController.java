package user.management.controller;


import user.management.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import user.management.repository.GroupRepository;

import java.util.Collection;

@RestController
public class GroupController {
    private JdbcTemplate jdbcTemplate;

    private GroupRepository groupRepository;

    @Autowired
    public GroupController(JdbcTemplate jdbcTemplate, GroupRepository groupRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupRepository = groupRepository;
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public Collection<Group> getGroups(@RequestParam(value = "userId", required = false) Long userId) {
        if(userId == null) {
            return groupRepository.getAllGroups();
        } else {
            return groupRepository.getGroupsForUser(userId);
        }
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
    public Group getGroup(@PathVariable(value = "id") long id) {
        return groupRepository.getGroup(id);
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable(value = "id") long id) {
        groupRepository.deleteGroup(id);
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.PUT)
    public void editGroup(@PathVariable(value = "id") long id, @RequestBody Group group) {
        groupRepository.editGroup(id, group);
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public void createGroup(@RequestBody Group group) {
        groupRepository.createGroup(group);
    }
}
