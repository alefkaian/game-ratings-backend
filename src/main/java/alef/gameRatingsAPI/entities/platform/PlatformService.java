package alef.gameRatingsAPI.entities.platform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Platform getPlatform(PlatformDTO platformInfo) {
        if (platformInfo == null) return null;

        return platformRepository.findById(platformInfo.id()).orElseGet(() -> {
           Platform platform = new Platform(platformInfo.id(), platformInfo.slug(), platformInfo.name(), platformInfo.image_background());
           return platformRepository.save(platform);
        });
    }

    public Set<Platform> getPlatformsList(List<PlatformWrapperDTO> platformsInfo) {
        return platformsInfo.stream()
                .map(PlatformWrapperDTO::platform)
                .map(this::getPlatform)
                .collect(Collectors.toSet());
    }
}
