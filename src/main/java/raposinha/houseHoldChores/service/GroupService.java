package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.group.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.group.GroupResponseDTO;
import raposinha.houseHoldChores.DTO.group.UpdateGroupNameRequestDTO;
import raposinha.houseHoldChores.DTO.user.MemberOfGroupResponseDTO;
import raposinha.houseHoldChores.DTO.user.UserRegistrationResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.entities.enums.GroupRole;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.exception.UnauthorizedException;
import raposinha.houseHoldChores.exception.UserAlreadyHasGroupException;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private UserService userService;

    // create group
    @Transactional
    public GroupResponseDTO create(GroupCreateDTO body, User adminUser) {
        // a user can only be in one group check
        if (adminUser.getGroup() != null) {
            throw new UserAlreadyHasGroupException("Cannot create group: You already belong to a household!");
        }

        // create group entity
        Group newGroup = new Group();
        newGroup.setGroupName(body.getGroupName());
        newGroup.setOwner(adminUser);
        adminUser.setRole(GroupRole.ADMIN);

        Group savedGroup = groupRepo.save(newGroup);

        // adds group to user entity
        adminUser.setGroup(savedGroup);
        userRepo.save(adminUser);

        return new GroupResponseDTO(
                savedGroup.getId(),
                savedGroup.getGroupName(),
                adminUser.getId(),
                // initialize empty list of members, but admin is a member?
                null
        );
    }

    @Transactional
    public String addUserToGroup(Long groupId, UUID userId, UUID requesterId){
        // fetch user
        User member = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User to remove not found"));
        User requester = userRepo.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Requester not found"));

        // fetch group
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NotFoundException("Group  with id " + groupId + " not found."));

        // if requester is not admin of group, it cannot add a member
        if(!requesterId.equals(group.getOwner().getId())){ throw new UnauthorizedException("Only a group admin can add a member to the group");}
        member.setGroup(group);

        // save user as a group member
        userRepo.save(member);

        return member.getUsername() + " has joined " + group.getGroupName();

    }

    // remove user from group
    @Transactional
    public String removeUserFromGroup(String groupId, UUID userIdToRemove, User requester) {
        User member = userRepo.findById(userIdToRemove)
                .orElseThrow(() -> new NotFoundException("User to remove not found"));
        User requesterId = userRepo.findById(requester.getId())
                .orElseThrow(() -> new NotFoundException("Requester not found"));

        // admin cannot remove themselves
        if (userIdToRemove.equals(requesterId) && requester.getRole() == GroupRole.ADMIN) {
            throw new BadRequestException("Admins cannot remove themselves. Please delete the group or transfer ownership first.");
        }

        // You can pass if: You ARE the Admin OR you are removing YOURSELF
        boolean isAdmin = requester.getRole() == GroupRole.ADMIN;
        boolean isSelfRemoval = userIdToRemove.equals(requesterId);

        if (!isAdmin && !isSelfRemoval) {
            throw new UnauthorizedException("You don't have permission to remove this user.");
        }

        member.setGroup(null);
        member.setRole(null); // Reset their power

        userRepo.save(member);

        return member.getUsername() + " was succesfully removed from group " + groupId;
    }

    @Transactional
    public void deleteGroup(Long groupId, User requester) {
        User foundUser = userRepo.findById(requester.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Group foundGroup = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        // only group admin/creator can delete group
        if (foundUser.getRole() != GroupRole.ADMIN) {
            throw new UnauthorizedException("Only a group admin can delete the household!");
        }

        //  Clear the relationship for ALL members
        for (User member : foundGroup.getMembers()) {
            member.setGroup(null);
            member.setRole(null); // Reset their role too
            userRepo.save(member);
        }

        groupRepo.delete(foundGroup);

    }

    @Transactional
    public String updateGroupName(Long groupId, UpdateGroupNameRequestDTO dto) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Household group not found with ID: " + groupId));

        group.setGroupName(dto.newGroupName());
        return "Group name has been changed to " + dto.newGroupName();
    }

    public List<MemberOfGroupResponseDTO> findByGroupId(Long id) {
        List<User> members = userRepo.findByGroup_Id(id);
        if (!groupRepo.existsById(id)) {
            throw new NotFoundException("Group with ID " + id + " not found");
        }
        return members.stream()
                .map(user -> new MemberOfGroupResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getAvatarUrl(),
                        user.getGroup(),
                        user.getRole(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    public Group findGroupByAdminEmail(String email){
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        return groupRepo.findByOwnerEmail(email)
                .orElseThrow(() -> new NotFoundException("No active group managed by this administrator email."));
    }
}


