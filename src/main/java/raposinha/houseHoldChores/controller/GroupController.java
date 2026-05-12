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
    public GroupResponseDTO createGroup(@RequestBody GroupCreateDTO body, @AuthenticationPrincipal User adminUser
                                        ) {

        return groupService.create(body, adminUser);
    }
    @PatchMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> joinGroup(@PathVariable String groupId, @PathVariable UUID userId, @AuthenticationPrincipal User requester) {
        String message = groupService.addUserToGroup(groupId, userId, requester.getId());
        return ResponseEntity.ok(message);
    }
    // see all groups members
    @GetMapping("/{groupId}")
    public List<User> findGroupId(@PathVariable("groupId") String groupId){
        return  userService.findByGroupId(groupId);
    }

    @DeleteMapping("/delete/{groupId}")
    public void deleteGroup(@PathVariable("groupId") String groupId, @RequestHeader("User-ID") UUID userId){
        // todo: how to get the id from the loggedin user?
        groupService.deleteGroup(groupId, userId);
    }

    // remove user from group
    @PatchMapping("/remove/members/{groupId}/{userToRemove}")
    public ResponseEntity<String> removeUser(@PathVariable("groupId") String groupId,  @PathVariable("userToRemove") UUID userToRemove, @RequestHeader("User-ID") UUID requesterId){
        String message = groupService.removeUserFromGroup(groupId, userToRemove, requesterId);
        return ResponseEntity.ok(message);
    }



    //see all group tasks

}
