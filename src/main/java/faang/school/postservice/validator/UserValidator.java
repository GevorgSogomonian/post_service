package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserServiceClient userServiceClient;

    public void validateUserExistence(long userId) {
        if (!userServiceClient.existsById(userId)) {
            String errMessage = String.format("User with ID: %d was not found in Database", userId);
            log.info(errMessage);
            throw new EntityNotFoundException(errMessage);
        }
    }

    public void validateFollowersExistence(List<Long> followerIds) {
        if (!userServiceClient.doesFollowersExist(followerIds)) {
            String errMessage = "Not all provided followers registered in database";
            log.error(errMessage);
            throw new EntityNotFoundException(errMessage);
        }
    }
}