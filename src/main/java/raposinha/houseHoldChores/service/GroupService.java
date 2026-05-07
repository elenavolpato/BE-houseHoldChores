package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.GroupCreateDTO;
import raposinha.houseHoldChores.DTO.GroupResponseDTO;
import raposinha.houseHoldChores.entities.Group;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.repositories.GroupRepo;
import raposinha.houseHoldChores.repositories.UserRepo;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    // create group
    @Transactional
    public GroupResponseDTO create(GroupCreateDTO body, User adminUser) {
        // create group entity
        Group newGroup = new Group();
        newGroup.setGroupName(body.getGroupName());

        // set owner
        newGroup.setOwner(adminUser);

        Group savedGroup = groupRepo.save(newGroup);

        // adds group to user entity
        adminUser.setGroup(savedGroup);
        userRepo.save(adminUser);

        return new GroupResponseDTO(
                savedGroup.getId(),
                savedGroup.getGroupName(),
                adminUser.getId(),
                // initialize empty list of members
                null
        );

    }


}
