package com.zerooversell.campaign.api;

import com.zerooversell.campaign.domain.Campaign;
import com.zerooversell.campaign.service.CampaignService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {
    private final CampaignService service;
    public CampaignController(CampaignService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignResponse create(@Valid @RequestBody CreateCampaignRequest request) {
        return CampaignResponse.from(service.create(request.name(), request.unitPrice(), request.currency(), request.quantity()));
    }

    @GetMapping("/{campaignId}")
    public CampaignResponse get(@PathVariable UUID campaignId) {
        return service.find(campaignId).map(CampaignResponse::from)
                .orElseThrow(() -> new CampaignNotFoundException(campaignId));
    }

    public record CreateCampaignRequest(@NotBlank String name, @NotNull @DecimalMin("0.01") BigDecimal unitPrice,
                                        @NotBlank String currency, @Min(0) int quantity) {}
    public record CampaignResponse(UUID campaignId, String name, BigDecimal unitPrice,
                                   String currency, int initialQuantity, String status, Instant createdAt) {
        static CampaignResponse from(Campaign c) {
            return new CampaignResponse(c.getId(), c.getName(), c.getUnitPrice(), c.getCurrency(),
                    c.getInitialQuantity(), c.getStatus().name(), c.getCreatedAt());
        }
    }
}
