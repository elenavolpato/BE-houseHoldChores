package raposinha.houseHoldChores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.group.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.group.GroupResponseDTO;
import raposinha.houseHoldChores.DTO.group.UpdateGroupNameRequestDTO;
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
    public GroupResponseDTO createGroup(@RequestBody GroupCreateDTO body, @AuthenticationPrincipal User adminUser) {
        return groupService.create(body, adminUser);
    }
    // add user to a group - admin only feature
    @PatchMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId, @PathVariable UUID userId, @AuthenticationPrincipal User requester) {
        String message = groupService.addUserToGroup(groupId, userId, requester.getId());
        return ResponseEntity.ok(message);
    }

    // remove user from group - admin only feature
    @PatchMapping("/remove/members/{groupId}/{userToRemove}")
    public ResponseEntity<String> removeUser(@PathVariable("groupId") String groupId,  @PathVariable("userToRemove") UUID userToRemove, @AuthenticationPrincipal User requester){
        String message = groupService.removeUserFromGroup(groupId, userToRemove, requester);
        return ResponseEntity.ok(message);
    }

    // see all groups members
    @GetMapping("/{groupId}")
    public List<User> findGroupId(@PathVariable("groupId") Long groupId){
        return  groupService.findByGroupId(groupId);
    }

    @DeleteMapping("/delete/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal User requester){
        groupService.deleteGroup(groupId, requester);
    }

    @PatchMapping("/{groupId}/rename")
    public ResponseEntity<String> changeGroupName (@PathVariable("groupId") Long groupId, @RequestBody UpdateGroupNameRequestDTO newGroupName){
        groupService.updateGroupName(groupId, newGroupName);
        return ResponseEntity.ok("Group name changed to " + newGroupName.newGroupName());
    }

}
