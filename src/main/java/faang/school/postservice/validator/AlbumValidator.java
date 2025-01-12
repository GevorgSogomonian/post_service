package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.exception.AlbumRejectedInAccessException;
import faang.school.postservice.model.Album;
import faang.school.postservice.model.AlbumVisibility;
import faang.school.postservice.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumValidator {
    private final AlbumRepository albumRepository;
    private final UserServiceClient userServiceClient;

    public void validateAlbumExistence(long albumId) {
        if (!albumRepository.existsById(albumId)) {
            String errMessage = String.format("Album with ID: %d was not found in Database", albumId);
            log.error(errMessage);
            throw new EntityNotFoundException(errMessage);
        }
    }

    public void validateAlbumTitleDoesNotDuplicatePerAuthor(long authorId, String albumTitle) {
        if (albumRepository.existsByTitleAndAuthorId(albumTitle, authorId)) {
            String errMessage = String.format("User with ID: %d already has album with title: %s", authorId, albumTitle);
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);
        }
    }

    public void validateAlbumBelongsToRequester(long requesterId, Album album) {
        if (album.getAuthorId() != requesterId) {
            String errMessage = String.format("Album with ID: %d does not belong to author with ID: %d",
                    album.getId(), requesterId);
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);
        }
    }

    public void validateVisibilityToRequester(long requesterId, Album album) {
        if (!isVisibleToRequester(requesterId, album)) {
            throw new AlbumRejectedInAccessException(album.getId());
        }
    }

    public boolean isVisibleToRequester(long requesterId, Album album) {
        AlbumVisibility albumVisibility = album.getVisibility();
        if (requesterId == album.getAuthorId() || albumVisibility.equals(AlbumVisibility.PUBLIC)) {
            return true;
        } else if (albumVisibility.equals(AlbumVisibility.ONLY_ALLOWED_USERS)) {
            List<Long> authorAllowedUsers = album.getAllowedUserIds();
            return authorAllowedUsers.contains(requesterId);
        } else if (albumVisibility.equals(AlbumVisibility.ONLY_FOLLOWERS)) {
            List<UserDto> authorFollowers = userServiceClient.getUserFollowers(album.getAuthorId());
            return authorFollowers.stream().map(UserDto::getId).anyMatch(userId -> userId == requesterId);
        }
        return false;
    }
}