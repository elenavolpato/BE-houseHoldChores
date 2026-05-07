package raposinha.houseHoldChores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.GroupResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.GroupService;
import raposinha.houseHoldChores.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public GroupResponseDTO createGroup(@RequestBody GroupCreateDTO body, @RequestHeader("User-ID") UUID userId // Manually pass the ID in Postman headers
                                        ) {
        //TODO: after adding authentication use this to get the user logged in @AuthenticationPrincipal User user
        User adminUser = userService.findById(userId);
        return groupService.create(body, adminUser);
    }
    //todo for authorization: only admin can add people to a group? think about it. is this something I want to restrict?
    @PatchMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> joinGroup(@PathVariable String groupId, @PathVariable UUID userId) {
        String message = groupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{groupId}")
    public List<User> findGroupId(@PathVariable("groupId") String groupId){
        return  userService.findByGroupId(groupId);
    }

    // remove user from group

    // see all groups members

    //see all group tasks

}
