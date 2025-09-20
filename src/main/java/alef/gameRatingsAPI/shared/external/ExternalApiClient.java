package alef.gameRatingsAPI.shared.external;

import alef.gameRatingsAPI.entities.game.ExternalGamesResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ExternalApiClient {
    private final WebClient webClient;
    private final String apiKey;

    public ExternalApiClient(WebClient.Builder builder, @Value("${base.api" +
            ".url}") String baseUrl, @Value("${rawg.api.key}") String apiKey) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public Mono<ExternalGamesResponseDTO> fetchGamesPage(Set<String> genres, Integer pageSize, Integer pageNumber, String ordering) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString("/games")
                .queryParam("key", apiKey)
                .queryParam("page_size", ((pageSize != null) ? pageSize : 40));
        if (genres != null && !genres.isEmpty()) {
            uriBuilder.queryParam("genres", String.join(",", genres));
        }
        if (pageNumber != null) uriBuilder.queryParam("page", pageNumber);
        if (ordering != null) uriBuilder.queryParam("ordering", ordering);
        String uri = uriBuilder.toUriString();

        System.out.println(uri);

        System.out.println("[Fetch Called]");
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ExternalGamesResponseDTO.class);
    }
}