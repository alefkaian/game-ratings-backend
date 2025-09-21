package alef.gameRatingsAPI.entities.platform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlatformService {
    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Transactional
    public Platform findOrCreatePlatform(PlatformDTO platformInfo) {
        if (platformInfo == null) return null;

        return platformRepository.findById(platformInfo.id()).orElseGet(() -> {
            Platform platform = new Platform(platformInfo.id(),
                    platformInfo.slug(), platformInfo.name(),
                    platformInfo.image_background());
            return platformRepository.save(platform);
        });
    }

    public Set<Platform> getPlatformsList(List<PlatformWrapperDTO> platformsInfo) {
        if (platformsInfo == null || platformsInfo.isEmpty())
            return Collections.emptySet();
        return platformsInfo.stream()
                .map(PlatformWrapperDTO::platform)
                .map(this::findOrCreatePlatform)
                .collect(Collectors.toSet());
    }
}
