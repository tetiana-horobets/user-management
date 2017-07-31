package user.management.controller;

import user.management.entity.Group;
import user.management.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import user.management.repository.UserRepository;

import java.util.*;

@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Collection<User> getUsers(@RequestParam(value = "groupId", required = false) Long groupId) {
        if(groupId == null) {
            return userRepository.getAllUsers();
        } else {
            return userRepository.getUsersInGroup(groupId);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable(value = "id") long id) {
        return userRepository.getUser(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "id") long id) {
        userRepository.deleteUser(id);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public void createUser(@RequestBody User user) {
        userRepository.createUser(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public void editUser(@PathVariable(value = "id") long id, @RequestBody User user) {
        userRepository.updateUser(id, user);
    }

    @RequestMapping(value = "/user/{id}/group", method = RequestMethod.PUT)
    public void editUserGroups(@PathVariable(value = "id") long userId, @RequestBody List<Group> groups) {
        userRepository.updateUserGroups(userId, groups);
    }
}
