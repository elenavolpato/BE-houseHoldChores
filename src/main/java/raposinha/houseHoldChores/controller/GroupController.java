package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.group.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.group.GroupResponseDTO;
import raposinha.houseHoldChores.DTO.group.UpdateGroupNameRequestDTO;
import raposinha.houseHoldChores.DTO.user.MemberOfGroupResponseDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.GroupService;
import raposinha.houseHoldChores.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@AllArgsConstructor
@Validated
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    // POST /api/groups/create
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public GroupResponseDTO createGroup(@Valid @RequestBody GroupCreateDTO body, @AuthenticationPrincipal User adminUser) {
        return groupService.create(body, adminUser);
    }

    // add user to a group - admin only feature
    // PATCH /api/groups/1/members/UUID
    @PatchMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId, @PathVariable UUID userId, @AuthenticationPrincipal User requester) {
        String message = groupService.addUserToGroup(groupId, userId, requester.getId());
        return ResponseEntity.ok(message);
    }

    // remove user from group - admin only feature
    // PATCH /api/groups/remove/members/1/UUID
    @PatchMapping("/remove/members/{groupId}/{userToRemove}")
    public ResponseEntity<String> removeUser(@PathVariable("groupId") String groupId,  @PathVariable("userToRemove") UUID userToRemove, @AuthenticationPrincipal User requester){
        String message = groupService.removeUserFromGroup(groupId, userToRemove, requester);
        return ResponseEntity.ok(message);
    }

    // PATCH /api/groups/{groupId}/rename
    @PatchMapping("/{groupId}/rename")
    public ResponseEntity<?> changeGroupName (
            @PathVariable("groupId") Long groupId,
            @Valid @RequestBody UpdateGroupNameRequestDTO newGroupName
    ) {
        groupService.updateGroupName(groupId, newGroupName);

        return ResponseEntity.ok(Map.of("message", "Group name changed to " + newGroupName.newGroupName()));
    }

    // see all groups members
    // GET /api/groups/1
    @GetMapping("/{groupId}")
    public List<MemberOfGroupResponseDTO> getAllGroupMembers(@PathVariable("groupId") Long groupId){
        return  groupService.findByGroupId(groupId);
    }

    // DELETE  /api/groups/delete/1
    @DeleteMapping("/delete/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal User requester){
        groupService.deleteGroup(groupId, requester);
    }



}
