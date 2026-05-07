package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.GroupResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;

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
        newGroup.setGroupName(body.getGroupName());

        // set owner
        newGroup.setOwner(adminUser);

        // set id for group with G-001
        long count = groupRepo.count() + 1;
        String formattedCode = String.format("G-%03d", count);
        newGroup.setId(formattedCode);

        Group savedGroup = groupRepo.save(newGroup);

        // adds group to user entity
        adminUser.setGroup(savedGroup);
        userRepo.save(adminUser);

        return new GroupResponseDTO(
                formattedCode,
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


}
