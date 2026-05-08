package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.GroupResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.entities.enums.GroupRole;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.exception.UnauthorizedException;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private UserService userService;

    // create group
    @Transactional
    public GroupResponseDTO create(GroupCreateDTO body, User adminUser) {

        // create group entity
        Group newGroup = new Group();

        // set id for group with G-001
        long count = groupRepo.count() + 1;
        String newId = String.format("G-%03d", count + 1);
        newGroup.setId(newId);

        newGroup.setId(newId); // Manually setting the ID
        newGroup.setGroupName(body.getGroupName());
        newGroup.setOwner(adminUser);
        adminUser.setRole(GroupRole.ADMIN);

        Group savedGroup = groupRepo.save(newGroup);

        // adds group to user entity
        adminUser.setGroup(savedGroup);
        userRepo.save(adminUser);

        return new GroupResponseDTO(
                newId,
                savedGroup.getGroupName(),
                adminUser.getId(),
                // initialize empty list of members
                null
        );
    }

    @Transactional
    public String addUserToGroup(String groupId, UUID userId){
        // fetch user
        User user = userService.findById(userId);

        // fetch group
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NotFoundException("Group  with id " + groupId + " not found."));

        // link group and user
        user.setGroup(group);

        // save user as a group member
        userRepo.save(user);

        return user.getUsername() + " has joined " + group.getGroupName();

    }
    @Transactional
        public void deleteGroup(String groupId, UUID requesterId) {
        // check if logged in user is the admin of this group todo
            User foundUser = userRepo.findById(requesterId)
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

    // remove user from group
    @Transactional
    public String removeUserFromGroup(String groupId, UUID userIdToRemove, UUID requesterId) {
        User member = userRepo.findById(userIdToRemove)
                .orElseThrow(() -> new NotFoundException("User to remove not found"));
        User requester = userRepo.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Requester not found"));

        // admin cannot remove themselves
        if (userIdToRemove.equals(requesterId)) {
            throw new BadRequestException("Admins cannot remove themselves. Please delete the group or transfer ownership first.");
        }

        // check if requester is admin of the group
        if (requester.getRole() != GroupRole.ADMIN) {
            throw new UnauthorizedException("Only the group admin can remove members.");
        }

        member.setGroup(null);
        member.setRole(null); // Reset their power

        userRepo.save(member);

        return member.getUsername() + " was succesfully removed from group " + groupId;
    }


    // filters by category - see what Giorgia did on the previous project todo
    // see all group tasks todo



}
