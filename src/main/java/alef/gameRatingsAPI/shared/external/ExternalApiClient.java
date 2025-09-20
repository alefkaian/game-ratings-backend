package alef.gameRatingsAPI.shared.external;

import alef.gameRatingsAPI.entities.game.ExternalGamesResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalApiClient {
    private final WebClient webClient;
    @Value("${base.api.url}")
    private String baseUrl;
    @Value("${rawg.api.key}")
    private String apiKey;

    public ExternalApiClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<ExternalGamesResponseDTO> fetchGamesPage(String nextUrl, int pageSize) {
        String uri = (nextUrl != null) ? nextUrl : "/games?key=" + apiKey + "&ordering=-rating&page_size=" + pageSize;
        System.out.println("[Fetch Called]");
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ExternalGamesResponseDTO.class);
    }
}